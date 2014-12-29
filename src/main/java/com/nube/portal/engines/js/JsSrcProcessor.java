//package com.nube.portal.engines.js;
//
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.Arguments;
//import org.thymeleaf.dom.Element;
//import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;
//
//import jdk.nashorn.api.scripting.*;
//
//import com.nube.core.exception.NubeException;
//
//
///**
// * Src Attribute in sjs taglib
// * 
// * @author kamoorr
// *
// */
//@Component
//public class JsSrcProcessor extends AbstractTextChildModifierAttrProcessor {
//	
//	@Autowired
//	JsEngine jsEngine;
//	
//	static Logger logger = Logger.getLogger(JsSrcProcessor.class);
//
//
//	public JsSrcProcessor() throws IOException {
//		// Only execute this processor for 'src' attributes.
//		super("src");
//	}
//	@PostConstruct
//	public void init() throws NubeException{
//		jsEngine.evalByFile("/Users/kamoorr/git/nube-portal-1.0/nube-portal/src/main/webapp/~core/sjs/mustache.js");
//		logger.info("Mustache javascript engine initialized");
//		jsEngine.evalByFile("/Users/kamoorr/git/nube-portal-1.0/nube-portal/src/main/webapp/~core/sjs/rest.js");
//		logger.info("Rest javascript engine initialized");
//		
//	}
//
//	public int getPrecedence() {
//		return 1;
//	}
//	
//	
//	public String eval(String content) throws NubeException{
//		return jsEngine.eval(content).toString();
//	}
//	
//	
//	@Override
//	protected String getText(Arguments args, Element element,
//			String attrName) {
//		
//		//try {
//			//JSObject response = (JSObject)jsEngine.eval("REST.get('http://localhost:9000/v1/test/hello-world')");
//			//logger.info("Processed get call" + element.getAttributeValue("attrName") + ", res =  "+ response.getMember("status").toString());
//			//logger.info("Processed get call" + element.getAttributeValue("attrName") + ", res =  "+ response.getMember("data").toString());
//			//JSObject array = (JSObject)response.getMember("data");
//			//logger.info(((JSObject)array.getSlot(0)).getMember("firstName"));
//			//logger.info(jsEngine.eval("var test=REST.get('http://localhost:9000/v1/test/hello-world');"));
//			//logger.info(jsEngine.eval("Mustache.render('Status at mustache is {{status}}',test);"));
//			//logger.info(jsEngine.eval("Mustache.render('<html><body> {{#data}} Name: {{firstName}} {{/data}}</body></html>', test); "));
//		//}catch (NubeException e) {
//			// TODO Auto-generated catch block
//			//e.printStackTrace();
//		//}
//		return "DOne";
//	}
//
//}