package com.nube.portal.service.apps;

import org.springframework.web.multipart.MultipartFile;

import com.nube.core.exception.NubeException;

/**
 * Application uploader service
 * 
 * @author kamoorr
 * 
 */
public interface AppUploaderService {

	public String save(MultipartFile file, String name, String fileType)
			throws NubeException;

}
