package com.nube.portal.local.apps;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.nube.core.exception.NubeException;
import com.nube.portal.service.apps.AppPackageProcessorService;

/**
 * Process app package uploaded by a developer.
 * 
 * @author kamoorr
 * 
 */
@Profile("local")
@Service("localAppProcessorTask")
@DependsOn("localSetup")
public class LocalAppProcessorTask {

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	AppPackageProcessorService appPackageProcessorService;

	
	/**
	 * Call this method to start processing file,
	 * copy to a temp folder and then process
	 * 
	 * @param file
	 * @throws IOException 
	 * @throws NubeException 
	 */
	public void processFile(String folder) throws NubeException {
		
		String path = LocalSetup.LOCAL_ROOTDIR + File.separator + folder;
		File f = new File(path);
		if(!f.isDirectory()){
			throw new NubeException("no_folder_exists");
		}
		
		File fTemp = new File(f.getAbsolutePath() +"_temp");
		fTemp.delete();
		
		try {
			FileUtils.copyDirectory(f, fTemp);
		} catch (IOException e) {
			throw new NubeException("error_while_copy_to_temp");
		}
		taskExecutor.execute(new AppProcessorThread(fTemp.getAbsolutePath()));
	}

	static Logger logger = Logger.getLogger(LocalAppProcessorTask.class);

	/**
	 * This thread will process file which are uploaded by user
	 * 
	 * @author kamoorr
	 * 
	 */
	private class AppProcessorThread implements Runnable {

		private String folderToProcess;

		public AppProcessorThread(String folder) {
			this.folderToProcess = folder;
		}

		public void run() {
			try {
				logger.info("Processing folder " + folderToProcess);
				appPackageProcessorService.processUnPackedApp(folderToProcess);

			} catch (IOException e) {
				logger.error(e);
			} catch (NubeException e) {
				logger.error(e);
			}

		}

		public String getName() {
			return Thread.currentThread().getName();
		}

	}

}
