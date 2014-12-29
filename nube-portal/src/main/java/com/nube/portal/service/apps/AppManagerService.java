package com.nube.portal.service.apps;

import java.io.OutputStream;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.nube.core.exception.NubeException;
import com.nube.core.vo.apps.App;
import com.nube.core.vo.idm.User;

/**
 * Read app
 * 
 * @author kamoorr
 * 
 */
public interface AppManagerService {

	/**
	 * Create a new context
	 * @param context
	 */
	public void createAppContext(User user, String context, String descr)throws NubeException;
	
	/**
	 * Delete context and all data in it
	 * @param context
	 */
	public void deleteAppContext(User user, String context)throws NubeException;
	
	/**
	 * Read content of file and write to output stream
	 * @param ios
	 * @param domain
	 * @param uri
	 * @throws NubeException
	 */
	public void read(OutputStream ios, String domain, String uri) throws NubeException;
	/**
	 * get all app versions
	 * @param context
	 * @return
	 * @throws NubeException
	 */
	public List<App> getAppVersions(String context) throws NubeException;
	/**
	 * Get current version
	 * @param context
	 * @return
	 * @throws NubeException
	 */
	public App getCurrentVersion(String context)throws NubeException;
	
	/**
	 * Update active version
	 * @param context
	 * @param newVersion
	 * @throws NubeException
	 */
	public void updateActiveVersion(String context,String newVersion) throws NubeException;

}
