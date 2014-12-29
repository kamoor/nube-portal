package com.nube.portal.dao.apps.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.nube.core.dao.apps.AppBundleDao;
import com.nube.core.dao.mongo.AbstractMongoDao;
import com.nube.core.dao.mongo.MongoConnection;
import com.nube.core.exception.NubeException;
import com.nube.core.vo.apps.App;

/**
 * Store app bundle meta data
 * 
 * @author kamoorr
 * 
 */
@Repository
@Profile("default")
public class AppBundleDaoImpl extends AbstractMongoDao<BasicDBObject, App> implements AppBundleDao<BasicDBObject, App> {

	@Autowired
	MongoConnection mongoConnection;

	static final String COLLECTION_BUNDLE = "app-bundles";

	static Logger logger = Logger.getLogger(AppBundleDaoImpl.class);

	public DBCollection getCollection() {
		return mongoConnection.getCollection(COLLECTION_BUNDLE);
	}

	/**
	 * Save bundle meta data
	 */
	public void save(App app) {
		getCollection().save(this.serialize(app));
	}

	/**
	 * Find entry by context and version
	 */
	public App findByContextAndVersion(String context, String version)
			throws NubeException {

		BasicDBObject result = (BasicDBObject) getCollection().findOne(
				new BasicDBObject().append("context", context).append(
						"version", version));

		if (result == null) {
			throw new NubeException(500, "Unable to find app bundle for "
					+ context + "/" + version);
		}
		return this.parse(result);

	}

	/**
	 * Find entry by context and version, return only last 100 versions
	 */
	public List<App> findByContext(String context) throws NubeException {

		DBCursor cursor = getCollection().find(
				new BasicDBObject().append("context", context));

		if (cursor == null) {
			throw new NubeException(500, "Unable to find app bundle for "
					+ context);
		}

		List<App> results = new ArrayList<App>();

		for (DBObject row : cursor) {
			BasicDBObject app = (BasicDBObject) row;
			results.add(this.parse(app));
		}

		return results;

	}

	/**
	 * Convert DB Object to pojo
	 */
	@Override
	public App parse(BasicDBObject dbObject) {
		App app = new App(
				1, 
				dbObject.getString("context"),
				dbObject.getString("context"),
				dbObject.getString("version"));
		app.setHomePage(dbObject.getString("homePage"));
		app.setDynamicFiles(dbObject.getString("dynaFiles"));
		return app;
		
	}
	
	@Override
	public BasicDBObject serialize(App pojo) {
		
		return new BasicDBObject()
		.append("context",  pojo.getContext())
		.append("version",  pojo.getVersion())
		.append("homePage", pojo.getHomePage())
		.append("dynaFiles", pojo.getDynamicFiles())
		.append("updatedBy", pojo.getUpdatedBy());
	}

	/**
	 * Delete context/version if exists
	 */
	public void delete(String context, String version) {
		logger.info(String.format("Delete Bundle context: %s, version: %s",
				context, version));
		getCollection().remove(
				new BasicDBObject().append("context", context).append(
						"version", version));
	}


	

}
