package com.nube.portal.engines.mustache;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nube.core.exception.NubeException;
import com.nube.portal.context.SessionContext;
import com.nube.portal.engines.html.HtmlParserEngine;
import com.nube.portal.engines.js.JsEngine;
import com.nube.portal.local.apps.LocalSetup;
/**
 * Mustache runtime engine.
 * This one needs lot of performance and feature environment
 * 1. Need to run multi line mustache tags (for loop etc)
 * 2. Need to detect unwanted lines and ignore, may be keep track of mustag tag occurece as compile step
 * @author kamoorr
 *
 */
@Component
public class MustacheEngine {
	
	
	public static String START_INDICATOR= "<!-- mts -->";
	public static String END_INDICATOR=   "<!-- mte -->";
	
	public static int END_INDICATOR_LEN = END_INDICATOR.length();
	public static int START_INDICATOR_LEN = START_INDICATOR.length();
	
	
	
	public static String START_TAG="{{";
	public static String END_TAG="}}";
	
	

	@PostConstruct
	public void init() {

	}

	
	@Autowired
	HtmlParserEngine htmlParserEngine;

	static Logger logger = Logger.getLogger(MustacheEngine.class);

	/**
	 * This method needs to be more intelligent to avoid unwanted line of
	 * execution Document will
	 * 
	 * @param document
	 * @return
	 */
	public Document  eval(Document document,JsEngine jsEngine, boolean ignoreErrors)
			throws NubeException {

		
		if(SessionContext.isDebug()){
			return document;
		}
		
		String html  = document.toString();
		
		// logger.info("mustache eval.."+html);
		
		StringBuffer outputHtml = new StringBuffer("");
		
		int pointer = 0;
		int length = html.length();
		
		int initialIndex = html.indexOf(START_INDICATOR);
		
			
		while(pointer < length){
			
			int sIndex = html.indexOf(START_INDICATOR, pointer);
			int eIndex = html.indexOf(END_INDICATOR, pointer);
			
			if(sIndex == -1 || eIndex == -1){
				break;
			}
			//Append everything from start to beginning of tag
			if(pointer < sIndex){
				outputHtml.append(html.substring(pointer, sIndex));
			}
			
			String result = null;
			String input = html.substring(sIndex, eIndex + END_INDICATOR_LEN).replace("\n","");
			try {
				
				result = (String) jsEngine.eval("Mustache.render('" + input + "', nubeObjects);");
				logger.info("Result: "+ result);
				
				
			} catch (NubeException e) {
				logger.error(e.getMessage());
				if(LocalSetup.isEnabled() || SessionContext.isDebug()){
					logger.info("Error is around " + input);
				}
				if (!ignoreErrors) {
						throw e;
				}
				result = input;
			}
			outputHtml.append(result);
			pointer = eIndex + END_INDICATOR_LEN;
		}
		//copy left over html
		if(pointer < length){
			outputHtml.append(html.substring(pointer));
		}
		
		document = htmlParserEngine.parse(outputHtml.toString());
		return document;
		
	}
}
