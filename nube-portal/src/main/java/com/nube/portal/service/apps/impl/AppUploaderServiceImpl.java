package com.nube.portal.service.apps.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.nube.core.exception.NubeException;
import com.nube.portal.service.apps.AppUploaderService;

@Component
@Profile("default")
public class AppUploaderServiceImpl implements AppUploaderService {

	@Value("${nube.app.upload.temp-folder}")
	String appTempFolder;

	static Logger logger = Logger.getLogger(AppUploaderServiceImpl.class);

	/**
	 * Save file to a folder.
	 * 
	 * @param file
	 * @return
	 */
	public String save(MultipartFile file, String name, String fileType)
			throws NubeException {
		if (file.isEmpty()) {
			throw new NubeException("File is empty");
		}

		if (!file.getOriginalFilename().endsWith(fileType)) {
			throw new NubeException(
					"Unrecognized file type. Package should of type  "
							+ fileType);
		}

		String destination = appTempFolder + File.separator + name;
		try {

			byte[] bytes = file.getBytes();
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(destination)));
			stream.write(bytes);
			stream.close();
			return destination;
		} catch (Exception e) {
			String msg = "Error while saving " + destination;
			logger.error(msg, e);
			throw new NubeException(msg, e);
		}

	}

}
