package com.nube.portal.request.apps;

/**
 * App update request
 * @author kamoorr
 *
 */
public class AppUpdateRequest {
	
	private String appFolder;
	private String context;
	private String activeVersion;
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getActiveVersion() {
		return activeVersion;
	}
	public void setActiveVersion(String activeVersion) {
		this.activeVersion = activeVersion;
	}
	public String getAppFolder() {
		return appFolder;
	}
	public void setAppFolder(String appFolder) {
		this.appFolder = appFolder;
	}
	
	

}
