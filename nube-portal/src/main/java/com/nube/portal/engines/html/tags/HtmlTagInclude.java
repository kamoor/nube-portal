package com.nube.portal.engines.html.tags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.nube.core.exception.NubeException;



/**
 * New tag
 * <nube:include src="x.html"/>
 * @author kamoorr
 *
 */
public class HtmlTagInclude extends HtmlTag{
	
	
	public static final String PREFIX="nube";
	public static final String SUFFIX="include";
	
	public static final String ATTR_SRC="src";
	
	public static final HtmlTagInclude instance = new HtmlTagInclude(null, Short.MIN_VALUE);
	
	
	/**
	 * New HTML tag
	 * @param prefix
	 * @param suffix
	 */
	public HtmlTagInclude(String source, short position){
		super(PREFIX, SUFFIX, position, false, true);
		super.addAttribute(ATTR_SRC, source);
	}
	
	
	public String getSource(){
		return super.getAttribute(ATTR_SRC);
	}
	
	
	
	public  String tagFinder(){
		return PREFIX + "|"+ SUFFIX +"[" +ATTR_SRC+ "]";
	}
	
	
	/**
	 * logic to what ever it needs to do
	 */
	public  String eval(File sourceFile)throws NubeException{
		
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
				//Nothing much to do
			}
		}
		return buffer.toString();
		
	}


	@Override
	public String preEval(File sourceFile)throws NubeException {
		throw new RuntimeException("This is not a dynamic tag");
	}
	
	

}
