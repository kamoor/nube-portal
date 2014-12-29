package com.nube.portal.local.apps;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

/**
 * Local set up common taks can go here
 * @author kamoorr
 *
 */
@Primary
@Repository("localSetup")
@Profile("local")
public class LocalSetup {

	static Logger logger = Logger.getLogger(LocalSetup.class);
	
	static boolean enabled = false;
	
	public static String LOCAL_ROOTDIR;
	
	
	@Value("${local.data.dir:/tmp/nube-portal/}")
	private String localWorkspace;
	
	@PostConstruct
	public void init() {
		enabled = true;
		LOCAL_ROOTDIR = localWorkspace;
		logger.info("........LOCAL SETUP ENABLED......");
	}

	public static boolean isEnabled() {
		return enabled;
	}
	
	

}