package com.nube.portal.service.apps.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.script.ScriptContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBList;
import com.nube.core.constants.AppConstants;
import com.nube.core.constants.ErrorCodes;
import com.nube.core.dao.apps.AppBundleDao;
import com.nube.core.dao.apps.AppFileDao;
import com.nube.core.dao.apps.AppsDao;
import com.nube.core.dao.idm.UserDao;
import com.nube.core.exception.NubeException;
import com.nube.core.util.apps.InetUtil;
import com.nube.core.util.string.StringUtil;
import com.nube.core.vo.apps.App;
import com.nube.core.vo.idm.Role;
import com.nube.core.vo.idm.User;
import com.nube.portal.engines.html.HtmlParserEngine;
import com.nube.portal.engines.html.tags.HtmlTag;
import com.nube.portal.engines.html.tags.HtmlTagScript;
import com.nube.portal.engines.js.JsEngine;
import com.nube.portal.engines.mustache.MustacheEngine;
import com.nube.portal.service.apps.AppManagerService;

/**
 * App Retriver service
 * 
 * @author kamoorr
 * 
 */
@Service
@Profile("default")
public class AppManagerServiceImpl implements AppManagerService {

	@Autowired
	AppFileDao appFileDao;

	@Autowired
	AppsDao appsDao;

	@Autowired
	AppBundleDao appBundleDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	JsEngine jsEngine;
	
	@Autowired
	MustacheEngine mustacheEngine;
	
	
	@Autowired
	HtmlParserEngine htmlParserEngine;

	static Logger logger = Logger.getLogger(AppManagerServiceImpl.class);

	
	
	/**
	 * Create a new context
	 * @param context
	 */
	public void createAppContext(User user, String context, String name)throws NubeException{
		
		App app  = new App(1, context, name, "0");
		appsDao.createAppContext(app);
		//Now grand admin rights to that context, read first to make sure object is most recent
		userDao.authorize(userDao.findByUserId(user.getId()), user, context, new Role(Role.role_admin));
	}
	
	/**
	 * Delete context and all data in it
	 * @param context
	 */
	public void deleteAppContext(User user, String context)throws NubeException{
		appsDao.deleteAppContext(context);
		//revoke permission
		userDao.revoke(userDao.findByUserId(user.getId()), user, context, new Role(Role.role_admin));
		//set primary context to another one, not internal app, not the same one just got deleted
		user = userDao.findByUserId(user.getId());
		String newPrimaryContext = null;
		for(String tempContext: user.getRoles().keySet()){
			if(!tempContext.equals(context) && 
			   !tempContext.equals(User.INTERNAL_APP) && 
			   user.getRoles(tempContext).contains(new Role(Role.role_admin))){
				newPrimaryContext = tempContext;
				break;
			}
		}
		// this could be null
		user.setPrimaryContext(newPrimaryContext);
		userDao.update(user, user);
		return;
	}
	
	/**
	 * Read content of any URL from document store, write to Outputstream
	 * 
	 * @param url
	 * @throws NubeException
	 */
	public void read(OutputStream ioStream, String domain, String uri) throws NubeException {

		// Find app info, current version etc, url etc
		App appInfo = this.findAppInfo(domain, uri);
	
		String documentName = appInfo.getContext() + AppConstants.SEPERATOR + appInfo.getVersion() + 
								this.makeFileUri(domain, uri, appInfo.getContext(), appInfo.getVersion(), appInfo);
		
		//Check for Dynamic files
		if(appInfo.getDynamicFiles() != null && appInfo.getDynamicFiles().contains(documentName)){
			processDynamicContent(ioStream, documentName);
		}else{
			//static content, directly write
			appFileDao.read(ioStream, documentName);
		}
		return;

	}
	
	/**
	 * Try to resolve serverside dynamic content
	 * @param tempSteam
	 * @return
	 * @throws NubeException
	 */
	private void processDynamicContent(OutputStream ioStream, String documentName) throws NubeException{
		
		try{
			
			logger.info("Dyna file request: "+documentName);
			
			OutputStream tempSteam = new ByteArrayOutputStream();
			appFileDao.read(tempSteam, documentName).toString();
		
			Document document = htmlParserEngine.parse(tempSteam.toString());
			
			//TODO: huge bug, reusing nashorn is giving error after sometime. Need to find out the root cause and fix it
			jsEngine = new JsEngine();
			
			List<HtmlTag> customTags = htmlParserEngine.findCustomTags(document);
			for(HtmlTag tag: customTags){
				if(tag.isDynamic()){
					logger.info("Dyna tag "+tag.getSuffix());
					((HtmlTagScript)tag).setJsEngine(jsEngine);
					htmlParserEngine.replace(document, tag, tag.eval(null));
				}
			}
			//Move all public objects to nubeObjects, this prefix could be configured script tag
			jsEngine.movePublicObjects("rest");
			
			//This can be more intelligent
			document = mustacheEngine.eval(document, jsEngine, true);
			
			ioStream.write(document.toString().getBytes());
			ioStream.flush();
			
		}catch(IOException ioe){
			logger.error(ioe);
			throw new NubeException(ErrorCodes.SERVER_ERROR,"unable to write content");
		}catch (NubeException e) {
			logger.error("Error while content execution. ", e);
			throw e;
		}
	}
	
	/**
	 * Get app versions	
	 */
	public List<App> getAppVersions(String context) throws NubeException{
		return appBundleDao.findByContext(context);
	}
	/**
	 * Get current version
	 * @param context
	 * @return
	 * @throws NubeException
	 */
	public App getCurrentVersion(String context)throws NubeException{
		return appsDao.findByContext(context);
	}
	
	/**
	 * Update current version, and its meta data
	 */
	public void updateActiveVersion(String context, String newVersion) throws NubeException{
		//By copying most of the info from bundle to app, we dont need to query bundle to render app
		App app = appBundleDao.findByContextAndVersion(context, newVersion);
		appsDao.update(app);
		return;
	}

	/**
	 * create file name from url Format context/version/uri
	 * 
	 * @param domain
	 * @param uri
	 * @param context
	 * @param currentVersion
	 * @param appInfo
	 * @return
	 * @throws NubeException
	 */
	private String makeFileUri(String domain, String uri, String context,
			String currentVersion, App appInfo) throws NubeException {

		String finalUri = null;
		if (InetUtil.isLocalHost(domain)) {
			// Remove context from url, but dont run this if user didnt specify
			// home page url
			finalUri = uri.replaceFirst(AppConstants.SEPERATOR + context,
					StringUtil.EMPTY);
			// Find home page if uri dont have home page url
			if (finalUri.length() <= 1) {
				throw new NubeException(
						HttpServletResponse.SC_MOVED_TEMPORARILY,
						AppConstants.SEPERATOR
								+ context
								+ AppConstants.SEPERATOR
								+ appBundleDao.findByContextAndVersion(context,
										currentVersion).getHomePage());
			}

		} else {
			// Find home page
			if (uri.length() <= 1) {
				throw new NubeException(
						HttpServletResponse.SC_MOVED_TEMPORARILY, appBundleDao
								.findByContextAndVersion(context,
										currentVersion).getHomePage());
			} else {
				finalUri = uri;
			}

		}
		return finalUri;
	}

	/**
	 * Find site context, then find app info
	 * 
	 * @param domain
	 * @param uri
	 * @return
	 */
	@Cacheable("appInfo")
	private App findAppInfo(String domain, String uri)
			throws NubeException {

		try {
			String context = null;
			App app = null;

			if (InetUtil.isLocalHost(domain)) {
				// Localhost will need to use context in url
				// logger.info("[Dev] Local address found");
				context = uri.split(AppConstants.SEPERATOR)[1];
				app = appsDao.findByContext(context);
			} else {
				// Production, context will be found from domain
				app = appsDao.findByDomain(domain);
				context = app.getContext();
			}

			if (app == null) {
				throw new NubeException(404, "Resource not found");
			}

			return app;
		} catch (IndexOutOfBoundsException e) {
			throw new NubeException(
					500,
					"Context is required to use localhost. example http://localhost:<port>/<context>/<url>");
		}

	}

}
