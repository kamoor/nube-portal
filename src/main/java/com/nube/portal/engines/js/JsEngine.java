package com.nube.portal.engines.js;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.nube.core.exception.NubeException;
import com.nube.core.util.file.FileUtil;
import com.nube.portal.context.SessionContext;
import com.nube.portal.local.apps.LocalSetup;

@Component
public class JsEngine {
	
	
	static Logger logger = Logger.getLogger(JsEngine.class);

	public static final String NUBE_OBJECTS="nubeObjects";
	
	private ScriptEngine scriptEngine;
	
	@Autowired
	CoreJsFrameworks coreJsFrameworks;

	/**
	 * TODO, initializing for each request due to a bug in re-using nashorn
	 * instance. This is a huge issue, needs to be fixed
	 */
	public  JsEngine() {
		try{
				this.init();
		}catch(Exception e){
			
		}
	}
	
	
	/**
	 * Initialize js engine and evaluate core javasc
	 * @throws ScriptException
	 * @throws IOException
	 */
	@PostConstruct
	public void init() throws ScriptException, IOException{
		scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
		if(scriptEngine == null){
			throw new RuntimeException("Failed to start nashorn javascript engine. JDK8 must be installed.");
		}
		for(String key: coreJsFrameworks.getCoreJsFrameWorks().keySet()){
			scriptEngine.eval(coreJsFrameworks.getCoreJsFrameWorks().get(key));
			logger.info(key + " initialized");
		}
	}
	
	
	
	/**
	 * Eval from a file 
	 * @param jsFilePath
	 * @throws NubeException
	 */
	public Object evalByFile(String jsFilePath) throws  NubeException{
		try{
			return scriptEngine.eval(new FileReader(jsFilePath));
		}catch(FileNotFoundException fe){
			logger.error(fe);
			throw new NubeException(404, "js file read error "+jsFilePath);
		}catch (ScriptException se) {
			logger.error(se);
			throw new NubeException(500, "js file execution error "+jsFilePath);
		}
		
	}

	/**
	 * Eval content of js source
	 * @param source
	 * @throws NubeException
	 */
	public Object eval(String source) throws  NubeException{
		try{
			
			return scriptEngine.eval (source);
			
		}catch (ScriptException se) {
			logger.error(se);
			if(LocalSetup.isEnabled() || SessionContext.isDebug()){
				logger.info("Error is in " + source);
			}
			throw new NubeException(500, "js file execution error ", se);
		}
	}
	
	/**
	 * Move all global objects with prefix to a another object "nubeObjects"
	 * @param prefix
	 * @throws NubeException
	 */
	public void movePublicObjects(String prefix) throws NubeException{
		Bindings b1 = scriptEngine.getContext().getBindings(ScriptContext.ENGINE_SCOPE);
		Map<String, Object> nubeObjects = new HashMap<String, Object>();
		for(Entry<String, Object> entry: b1.entrySet()){
			if(!entry.getKey().equals(NUBE_OBJECTS)){
				nubeObjects.put(entry.getKey(), entry.getValue());
				logger.info("js objects moved: "+ entry.getKey());
			}
		}
		b1.put(NUBE_OBJECTS, nubeObjects);
		return;
	}
	



}
