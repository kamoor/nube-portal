package com.nube.portal.engines.html.tags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nube.core.exception.NubeException;
import com.nube.portal.context.SessionContext;
import com.nube.portal.engines.js.JsEngine;


/**
 * New tag
 * <nube:script src="x.js"/>
 * @author kamoorr
 *
 */
public class HtmlTagScript extends HtmlTag{
	
	JsEngine jsEngine;
	
	public static final String PREFIX="nube";
	public static final String SUFFIX="script";
	
	public static final String ATTR_SRC="src";
	
	public static final HtmlTagScript instance = new HtmlTagScript(null,Short.MIN_VALUE);
	
	public HtmlTagScript(){
		super(PREFIX, SUFFIX, Short.MIN_VALUE, true, false);
	}
	/**
	 * New HTML tag
	 * @param prefix
	 * @param suffix
	 */
	public HtmlTagScript(String source, short position){
		super(PREFIX, SUFFIX, position, true, false);
		super.addAttribute(ATTR_SRC, source);
	}
	
	
	public String getSource(){
		return super.getAttribute(ATTR_SRC);
	}
	
	
	public String tagFinder(){
		return PREFIX + "|"+ SUFFIX +"[" +ATTR_SRC+ "]";
	}


	/**
	 * logic to what ever needs to do
	 * @throws NubeException 
	 */
	public String eval(File sourceFile) throws NubeException {
		if(SessionContext.isDebug()){
			//In debug, do not run script
			return this.getContent();
		}else{
			return (String)jsEngine.eval(this.getContent());
		}
		
	}


	/**
	 * This will include content of the file as pre-runtime step.
	 */
	@Override
	public String preEval(File sourceFile)throws NubeException {
		StringBuffer buffer = new StringBuffer("");
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(sourceFile.getParent() + File.separator + getSource()));
			String line= null;
			
			while((line = reader.readLine()) != null){
				buffer.append(line);
			}
		} catch (IOException e) {
			return "<!-- No Content found to include -->";
			
		}finally{
			try{
					reader.close();
			}catch(Exception e){
				
			}
		}
		return buffer.toString();
	}
	public void setJsEngine(JsEngine jsEngine) {
		this.jsEngine = jsEngine;
	}

	
}
