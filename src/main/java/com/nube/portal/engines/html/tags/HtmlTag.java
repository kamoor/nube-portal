package com.nube.portal.engines.html.tags;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.nube.core.exception.NubeException;

/**
 * Abtract html tags
 * @author kamoorr
 *
 */
public abstract class HtmlTag{
	
	String prefix;
	
	String suffix;
	
	String content;
	
	boolean isHtmlContent = true;
	
	short position = 0;
	
	//Process while user access the tag
	boolean isDynamic;
	
	Map<String, String> attributes;
	
	public HtmlTag (String prefix, String suffix, short position, boolean isDynamic, boolean isHtmlContent){
		this.prefix = prefix;
		this.suffix = suffix;
		this.position = position;
		this.isDynamic = isDynamic;
		this.isHtmlContent = isHtmlContent;
		attributes = new HashMap<String, String>();
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}
	
	public boolean isDynamic() {
		return isDynamic;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public void addAttribute(String key, String value){
		this.getAttributes().put(key, value);
	}
	
	public String getAttribute(String key){
		return this.getAttributes().get(key);
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	/**
	 * Pre compile
	 * @param sourceFile
	 * @return
	 */
	public abstract String preEval(File sourceFile)throws NubeException;

	/**
	 * Actual eval
	 * @param sourceFile
	 * @return
	 */
	public abstract String eval(File sourceFile)throws NubeException;
	
	public abstract String tagFinder();

	public short getPosition() {
		return position;
	}

	public void setPosition(short position) {
		this.position = position;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isHtmlContent() {
		return isHtmlContent;
	}
	
	
	
	
}
