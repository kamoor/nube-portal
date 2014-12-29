package com.nube.portal.engines.js;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

/**
 * This class us used to configure all the server side javascript adopted for nube.
 * @author kamoorr
 *
 */
@Component
public class CoreJsFrameworks {
	
	static Logger logger = Logger.getLogger(JsEngine.class);
	
	private Map<String, String> jsFilesInOrder;
	
	@PostConstruct
	public void init()throws IOException{
		 jsFilesInOrder = new LinkedHashMap<String, String>();
		 jsFilesInOrder.put("rest", Arrays.toString(FileCopyUtils.copyToByteArray(new ClassPathResource("sjs/common/rest.js").getInputStream())));
		 jsFilesInOrder.put("mustache", Arrays.toString(FileCopyUtils.copyToByteArray(new ClassPathResource("sjs/common/mustache.js").getInputStream())));
		 logger.info("All core javascript frameworks cached.");
	}
	
	/**
	 * Return a map of <jsname, js frmeworks source>
	 * @return
	 */
	public Map<String, String> getCoreJsFrameWorks(){
		return jsFilesInOrder;
	}
	

}
