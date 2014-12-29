package com.nube.portal.service.apps;

import java.io.IOException;

import com.nube.core.exception.NubeException;

/**
 * App package service
 * 
 * @author kamoorr
 * 
 */
public interface AppPackageProcessorService {

	public void processAppPackage(String zippedPackage, String tempFoler)
			throws IOException, NubeException;
	
	public void processUnPackedApp(String pkgFolder) throws IOException, NubeException;

}
