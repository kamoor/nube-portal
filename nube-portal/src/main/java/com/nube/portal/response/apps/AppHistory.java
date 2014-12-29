package com.nube.portal.response.apps;

import java.util.List;

import com.nube.core.vo.apps.App;

/**
 * App History
 * @author kamoorr
 *
 */
public class AppHistory {
	
	private String context;
	private List<App> apps;
	private String activeVersion;
	
	public AppHistory(String context, List<App> apps, String activeVersion){
		this.context = context;
		this.apps = apps;
		this.activeVersion = activeVersion;
	}
	
	public List<App> getApps() {
		return apps;
	}
	public void setApps(List<App> apps) {
		this.apps = apps;
	}
	public String getActiveVersion() {
		return activeVersion;
	}
	public void setActiveVersion(String activeVersion) {
		this.activeVersion = activeVersion;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	
	
	

}
