package com.nube.portal.request.apps;

public class ContextRequest {
	
	String context;
	
	String description;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ContextRequest [context=" + context + "]";
	}
	
	

}
