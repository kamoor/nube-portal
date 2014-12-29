package com.nube.portal.dao.apps.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.DebugBeanDefinitionParser;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.Hash;
import com.nube.core.constants.AppConstants;
import com.nube.core.dao.apps.AppFileDao;
import com.nube.core.dao.mongo.MongoConnection;
import com.nube.core.exception.NubeException;
import com.nube.core.util.apps.MimeTypeUtil;

@Repository
@Profile("default")
public class AppFileDaoImpl implements AppFileDao<GridFSFile, OutputStream> {

	@Autowired
	MongoConnection mongoConnection;

	private GridFS gridFsForApps;

	static Logger logger = Logger.getLogger(AppFileDaoImpl.class);

	
	public GridFS getCollection() {
		return mongoConnection.getGridFSApps();
	}
	

	/**
	 * Store file in GridFS
	 * 
	 * @param name
	 * @param f
	 */
	public void save(String docName, File f, Map<String, String> metaData) throws IOException {

		GridFSInputFile gridFile = getCollection().createFile(f);
		gridFile.setMetaData(this.serializeMetaData(metaData));
		gridFile.setFilename(docName);
		gridFile.setContentType(MimeTypeUtil.getMimeType(f.toPath()));
		gridFile.save();
		logger.info("Doc Saved: " + docName);

	}

	/**
	 * Read a document. Usually stored as context/version/....file path Keep in
	 * mind filUri starts with / Example : /images/a.gif
	 */
	private GridFSDBFile read(String context, String version, String fileUri)
			throws NubeException {

		String documentName = context + AppConstants.SEPERATOR + version
				+ fileUri;
		return this.read(documentName);

	}

	/**
	 * Read a document from document store
	 */
	private GridFSDBFile read(String documentName) throws NubeException {
		// logger.info("Read " + documentName);
		GridFSDBFile result = getCollection().findOne(documentName);

		if (result == null) {
			throw new NubeException(404, "Not found " + documentName);
		}
		return result;
	}

	/**
	 * Read a file
	 * 
	 * @param context
	 * @param version
	 * @param file
	 * @return
	 * @throws NubeException
	 */
	private Map<String, String> read(OutputStream outputStream, String context, String version,
			String fileUri) throws NubeException {
		String documentName = context + AppConstants.SEPERATOR + version
				+ fileUri;
		return  this.read(outputStream, documentName);
		
	}

	/**
	 * 
	 * @param documentName
	 * @return
	 * @throws NubeException
	 * @throws IOException
	 */
	public Map<String, String> read(OutputStream outputStream, String documentName)
			throws NubeException {
		// logger.info("Read " + documentName);
		
		Map<String, String> metaData = new HashMap<String, String>();

		try {
			GridFSDBFile result = getCollection().findOne(documentName);

			if (result == null) {
				throw new NubeException(404, "Not found " + documentName);
			}
			result.writeTo(outputStream);
			metaData = result.getMetaData() ==null? metaData :result.getMetaData().toMap();
			
			return metaData;
		} catch (IOException ioe) {
			throw new NubeException(500, "IOException while reading "
					+ documentName);
		}
	}

	/**
	 * Provide file path including wild card
	 */
	public void delete(String filePath) {
		// TODO Auto-generated method stub
		logger.info("Delete " + filePath);
		DBObject query = QueryBuilder.start("filename")
				.is(Pattern.compile(filePath, Pattern.CASE_INSENSITIVE)).get();
		getCollection().remove(query);

	}
	
	
	private DBObject serializeMetaData(Map<String, String> metaData){
		DBObject dbObject = new BasicDBObject();
		dbObject.putAll(metaData);
		return dbObject;
	}

	@Override
	public OutputStream parse(GridFSFile dbObject) {
		throw new RuntimeException("unimplemented");
	}

	@Override
	public GridFSFile serialize(OutputStream pojo) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<OutputStream> extract(DBCursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

}
