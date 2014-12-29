package com.nube.portal.service.apps.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFS;
import com.nube.core.constants.AppConstants;
import com.nube.core.dao.apps.AppBundleDao;
import com.nube.core.dao.apps.AppFileDao;
import com.nube.core.dao.apps.AppsDao;
import com.nube.core.dao.mongo.MongoConnection;
import com.nube.core.exception.NubeException;
import com.nube.core.util.zip.ZipUtil;
import com.nube.core.vo.apps.App;
import com.nube.portal.engines.html.HtmlParserEngine;
import com.nube.portal.engines.html.tags.HtmlTag;
import com.nube.portal.engines.mustache.MustacheEngine;
import com.nube.portal.service.apps.AppPackageProcessorService;
import com.nube.portal.validator.apps.AppPackageValidator;

/**
 * Application package processor
 * 
 * @author kamoorr
 * 
 */
@Service
@Profile("default")
public class AppPackageProcessorServiceImpl implements
		AppPackageProcessorService {

	@Autowired
	private ZipUtil zipUtil;

	@Autowired
	AppPackageValidator packageValidator;

	@Autowired
	MongoConnection mongoConnection;

	@Autowired
	AppsDao appsDao;

	@Autowired
	AppBundleDao appBundleDao;

	@Autowired
	AppFileDao appFileDao;

	@Autowired
	HtmlParserEngine htmlParserEngine;

	@Value("${nube.app.lic.key.site-context}")
	String siteContextKey;

	@Value("${nube.app.lic.key.site-name}")
	String siteNameKey;

	@Value("${nube.app.lic.key.pkg-version}")
	String pkgVersionKey;

	@Value("${nube.app.lic.key.site-url}")
	String siteUrlKey;

	@Value("${nube.app.lic.key.site-home}")
	String homePageKey;

	@Value("${nube.app.default-site-home}")
	String defaultHomePage;

	GridFS gridFS;

	static Logger logger = Logger
			.getLogger(AppPackageProcessorServiceImpl.class);

	@PostConstruct
	public void init() {
		gridFS = mongoConnection.getGridFSApps();
	}

	
	/**
	 * Unpack, Validate and Store app Also cleanup temp folder
	 * 
	 * TODO Archive zipped package for future reference, atleast 30 days
	 * 
	 * @param file
	 * @param tempFoler
	 * @throws IOException
	 * @throws NubeException
	 */
	public void processAppPackage(String zippedPackage, String tempFoler)
			throws IOException, NubeException {

		// Unzip and delete original
		String pkgFolder = zipUtil.unzip(zippedPackage, tempFoler, true);
		this.processUnPackedApp(pkgFolder);
		
	}

	/**
	 * Process unzipped folder, 
	 * @param pkgFolder
	 * @throws IOException
	 * @throws NubeException
	 */
	public void processUnPackedApp(String pkgFolder) throws IOException, NubeException {
	
		logger.info("Processing folder: " + pkgFolder);

		// Validate the package.
		Properties licProperties = packageValidator.validate(pkgFolder);

		// Store in Database or File
		File uzippedFolder = new File(pkgFolder);

		// Store app in document store, also update meta data
		this.storeApp(uzippedFolder, licProperties);

		// Cleanup temp folder
		uzippedFolder.delete();

	}

	/**
	 * Store package in document store
	 * 
	 * @param toInsert
	 * @param licProps
	 * @return
	 * @throws IOException
	 * @throws NubeException
	 */
	private boolean storeApp(File toInsert, Properties licProps)
			throws IOException, NubeException {

		List<String> listOfDynamicFiles = new ArrayList<String>();

		// Delete files if exists, and save each files in the bundle
		appFileDao.delete(licProps.getProperty(siteContextKey)
				+ AppConstants.SEPERATOR + licProps.getProperty(pkgVersionKey)
				+ AppConstants.SEPERATOR + "*");

		this.saveAllContents(licProps.getProperty(siteContextKey),
				licProps.getProperty(pkgVersionKey),
				toInsert.getAbsolutePath(), toInsert, listOfDynamicFiles);

		logger.info("Dynamic files detected: " + listOfDynamicFiles.toString());

		App appBundle = this.saveAppBundle(licProps, listOfDynamicFiles);
		logger.info("App bundle information updated");

		// Update App to newer version
		this.insertOrupdateAppInfo(licProps, appBundle);
		logger.info("App version updated");

		return true;

	}

	/**
	 * Save app bundle info
	 * 
	 * @param licProps
	 * @param listOfDynamicFiles
	 */
	private App saveAppBundle(Properties licProps,
			List<String> listOfDynamicFiles) {
		// Delete (context + version) and save bundle meta data.
		appBundleDao.delete(licProps.getProperty(siteContextKey),
				licProps.getProperty(pkgVersionKey));

		App app = new App(1, licProps.getProperty(siteContextKey),
				licProps.getProperty(siteNameKey),
				licProps.getProperty(pkgVersionKey));

		// keep track of all dynamic pages
		app.setDynamicFiles(listOfDynamicFiles.toString());
		// set default home page
		app.setHomePage(licProps.getProperty(homePageKey) == null ? licProps
				.getProperty(defaultHomePage) : licProps
				.getProperty(homePageKey));
		appBundleDao.save(app);
		return app;
	}

	/**
	 * Insert of update app info
	 * 
	 * @param licProps
	 * @throws NubeException
	 */
	private void insertOrupdateAppInfo(Properties licProps, App appBundleInfo)
			throws NubeException {

		App app = new App(1, licProps.getProperty(siteContextKey),
				licProps.getProperty(siteNameKey),
				licProps.getProperty(pkgVersionKey));
		app.setUpdatedBy("admin");

		// Copy some app bundle info to app to improve performance, this should
		// happen all the time when current version get updated
		app.setDynamicFiles(appBundleInfo.getDynamicFiles());

		appsDao.save(app, licProps.getProperty(siteUrlKey));
	}

	/**
	 * Put all contents in mongo db Grid FS -- Ignore hiden files -- Find
	 * dynamic files
	 * 
	 * @param contextAndVersion
	 *            Example: kamoorr/12/
	 * @param rootFolder
	 *            Root folder name
	 * @param toInsert
	 *            files to be inserted
	 * @throws IOException
	 * @throws NubeException
	 */
	private void saveAllContents(String context, String version,
			String rootFolder, File fileToStore, List<String> dynamicFiles)
			throws IOException, NubeException {
		File listFiles[] = fileToStore.listFiles();
		for (File f : listFiles) {
			if (f.isDirectory()) {
				saveAllContents(context, version, rootFolder, f, dynamicFiles);
			} else {
				// Ignore hidden files
				if (f.isHidden()) {
					continue;
				}
				// URL to be used to find files (URL will be like
				// <context>/<version>
				Map<String, String> metaData = new HashMap<String, String>();
				// process dynamic nature of the file
				boolean isDynamic = processDynamicTagsAndNubeContent(context, version, f, metaData);
				// Index dynamic template positions(mustache)
				indexTemplateTags(f);
			
				String documentName = this.getDocumentName(context, version,
						rootFolder, f);
				if (isDynamic) {
					dynamicFiles.add(documentName);
				}
				appFileDao.save(documentName, f, metaData);

			}
		}
	}

	/**
	 * the the unique name to store
	 * 
	 * @param context
	 * @param version
	 * @param rootFolder
	 * @param f
	 * @return
	 * @throws IOException
	 */
	private String getDocumentName(String context, String version,
			String rootFolder, File f) throws IOException {

		String fileName = f.getAbsolutePath();

		// Remove root folder path
		if (rootFolder != null) {
			fileName = f.getAbsolutePath().substring(rootFolder.length() + 1,
					f.getAbsolutePath().length());
		}

		return context + AppConstants.SEPERATOR + version
				+ AppConstants.SEPERATOR + fileName;
	}

	/**
	 * Find and process dynamic files
	 * Add nube specific content(javascript, analytics etc
	 * 
	 * @param f
	 * @param metaData
	 * @return
	 * @throws NubeException
	 */
	private boolean processDynamicTagsAndNubeContent(String context, String version, File f, Map<String, String> metaData)
			throws NubeException {

		// Only html files allow dynamic tags
		if (!(f.getName().endsWith(".html") || f.getName().endsWith(".htm"))) {
			return false;
		}

		boolean isDynamic = false;
		//boolean isCompileTimeResolved = false;
		Document document = htmlParserEngine.parse(f);
		// document.outputSettings().escapeMode(EscapeMode.);

		List<HtmlTag> customTags = htmlParserEngine.findCustomTags(document);
		// Process them
		for (HtmlTag tag : customTags) {
			if (tag.isDynamic()) {
				// This type tags need runtime processing for example script tag
				isDynamic = true;
				metaData.put("execType", "dynamic");
				// Replace tag with pre evaluated content
				htmlParserEngine.replace(document, tag,
						tag.preEval(f.getAbsoluteFile()));
			} else {
				// This type of tag is ok with compile time processing , html
				// include etc
				htmlParserEngine.replace(document, tag,
						tag.eval(f.getAbsoluteFile()));
			}
			//isCompileTimeResolved = true;
		}
		
		//Add Nube specific content 
		this.addNubeSpecificContents(context, version, document);
		
		
		//if (isCompileTimeResolved) {
			htmlParserEngine.writeDocument(document, f);
		//}

		return isDynamic;

	}
	
	/**
	 * This will add many things to all HTML,
	 * 1. Site specific js & Analytics js files
	 * 2. Any ad divs (TODO) 
	 * @param document
	 */
	private void  addNubeSpecificContents(String context, String version, Document document){
		//add some js object to beginning of body
		document.body().prepend("<script type=\"text/javascript\"> var currUser = {\"firstName\":\"Guest\"};var app = {\"id\":\""+context+"\", \"version\":\""+version+"\"};</script>");
		//add some js files to end of body
		document.body().append("<script type=\"text/javascript\" src=\"/~core/js/rest.js\"/>");
		document.body().append("<script type=\"text/javascript\" src=\"/~core/js/nana.js\"/>");
		document.body().append("<script type=\"text/javascript\" src=\"/~core/js/nana-tags/track.js\"/>");
		
		
	}
	

	/**
	 * Index template tags in html, Find mustache tag and embed <!-- mts --> at
	 * the beginning and <!-- mte --> at the end
	 * 
	 * @param f
	 */
	private void indexTemplateTags(File f) {
		// Only html files allow dynamic tags
		if (!(f.getName().endsWith(".html") || f.getName().endsWith(".htm"))) {
			return;
		}
		Document document = htmlParserEngine.parse(f);
		String html = document.html();
		StringBuffer newHtml = new StringBuffer("");
		int length = html.length();
		int pointer = 0;

		// Find mustache tag and put and indicator
		while (pointer < length) {
			// Find start of {{
			int start = html.indexOf(MustacheEngine.START_TAG, pointer);
			if (start == -1) {
				// copy rest of the content at last
				newHtml.append(html.substring(pointer, length));
				break;
			}
			// try to find end tag
			String endTag = MustacheEngine.END_TAG;
			char thirdChar = html.charAt(start + 2);
			if (thirdChar == '#' || thirdChar == '^') {
				// end tag will be some where else, example {{#person?}} .....
				// {{/person?}}
				endTag = "/"
						+ html.substring(start + 3,
								html.indexOf(MustacheEngine.END_TAG, start))
						+ MustacheEngine.END_TAG;
			} else if (thirdChar == '{') {
				//
				endTag = MustacheEngine.END_TAG + "}";
			}
			// find end Index;

			int end = html.indexOf(endTag, start) + endTag.length();
			// This should never happend for a good syntax
			if (end == -1) {
				end = length;
			}

			logger.info("End Tag " + endTag + "/ Start Index " + start
					+ "/ End Index " + end);
			logger.info("Content " + html.substring(start, end));
			// First put any content before the start of mustache tag
			newHtml.append(html.substring(pointer, start));

			// Put start indicator
			newHtml.append(MustacheEngine.START_INDICATOR);
			// Now put mustache tag
			newHtml.append(html.substring(start, end));
			// now add end indicator
			newHtml.append(MustacheEngine.END_INDICATOR);
			logger.info("New Content " + newHtml.toString());
			// Now move point to next;
			pointer = end;

		}
		document.text(newHtml.toString());
		htmlParserEngine.writeDocument(document, f);
	}

}
