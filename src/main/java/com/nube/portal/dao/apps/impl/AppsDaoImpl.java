package com.nube.portal.dao.apps.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.nube.core.constants.ErrorCodes;
import com.nube.core.dao.apps.AppsDao;
import com.nube.core.dao.mongo.AbstractMongoDao;
import com.nube.core.dao.mongo.MongoConnection;
import com.nube.core.exception.NubeException;
import com.nube.core.vo.apps.App;
import com.nube.portal.controller.apps.AppsRestController;

@Repository
@Profile("default")
public class AppsDaoImpl extends AbstractMongoDao<BasicDBObject, App> implements AppsDao<BasicDBObject, App> {

	@Autowired
	MongoConnection mongoConnection;

	final static String COLLECTION_APPS = "apps";
	
	static Logger logger = Logger.getLogger(AppsDaoImpl.class);


	public DBCollection getCollection() {
		return mongoConnection.getCollection(COLLECTION_APPS);
	}
	
	
	/**
	 * Create a new context
	 * @param context
	 */
	public void createAppContext(App app)throws NubeException{
		try{
			App existingObject  = this.findByContext(app.getContext().toLowerCase());
			throw new NubeException(ErrorCodes.APP_CONTEXT_ALREADY_EXISTS, "context_exists");
		}catch(NubeException n){
			if(n.getErrorCode() == ErrorCodes.APP_CONTEXT_NOT_FOUND){
				//context dont exists
				this.getCollection().save(this.serialize(app));
			}else{
				throw n;
			}
		}
	}
	
	/**
	 * Delete context and all data in it
	 * @param context
	 */
	public void deleteAppContext(String context)throws NubeException{
		getCollection().remove(new BasicDBObject().append("context", context.toLowerCase()));
	}

	/**
	 * TODO This needs to be revisited. How to map multiple url to one context
	 * How to find current version if context is given (localhost) Save document
	 * it can be comma seperated
	 */
	
	public void save(App app, String aliases) throws NubeException{
		
		//Just to make sure it exists
		App existingObject = this.findByContext(app.getContext().toLowerCase());
		
		BasicDBObject dbObject = this.serialize(app);
		dbObject.append("urls", aliases);
		
		getCollection().update(
				new BasicDBObject().
				append("context", app.getContext().toLowerCase()),
				dbObject
				, true, false);
	}
	
		
	/**
	 * Update a document by context,
	 * context is always the primary key
	 */
	public void update(App app)throws NubeException{
		logger.info("update context "+ app.getContext() + " to version "+ app.getVersion());
		BasicDBObject newDoc = new BasicDBObject().append("$set", this.serialize(app));
	
		//TDDO this one needs to use serialize method
		getCollection().update(
				new BasicDBObject()
				.append("context", app.getContext().toLowerCase()),
				newDoc);
	}
	
	
	public App findByContext(String context) throws NubeException {

		BasicDBObject result = (BasicDBObject) getCollection().findOne(
				new BasicDBObject().append("context", context.toLowerCase()));

		if (result == null) {
			throw new NubeException(ErrorCodes.APP_CONTEXT_NOT_FOUND, "Unable to find app with context "
					+ context);
		}
		return this.parse(result);
	}

	/**
	 * Find app by url TODO Supports only one url at a time
	 * 
	 * @param domain
	 * @return
	 * @throws NubeException
	 */
	public App findByDomain(String domain) throws NubeException {

		BasicDBObject result = (BasicDBObject) getCollection().findOne(
				new BasicDBObject().append("urls", domain.toLowerCase()));

		if (result == null) {
			throw new NubeException(ErrorCodes.APP_CONTEXT_NOT_FOUND, "Unable to find app with context "
					+ domain);
		}
		return this.parse(result);
	}

	public App parse(BasicDBObject dbObject) {
		App app = new App(
				1, dbObject.getString("context"), 
				dbObject.getString("name"), 
				dbObject.getString("currVersion"));
		app.setDynamicFiles(dbObject.getString("currVersionDynaFiles"));
		app.setHomePage(dbObject.getString("currVersionHome"));
		app.setUpdatedBy(dbObject.getString("updatedBy"));
		return app;		
	}

	@Override
	public BasicDBObject serialize(App pojo) {
		return new BasicDBObject().append("context", pojo.getContext().toLowerCase())
		.append("currVersion", pojo.getVersion())
		.append("currVersionHome", pojo.getHomePage())
		.append("currVersionDynaFiles", pojo.getDynamicFiles())
		.append("updatedBy", pojo.getUpdatedBy());
	}

	



	
}
