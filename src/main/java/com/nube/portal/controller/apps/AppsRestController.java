package com.nube.portal.controller.apps;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nube.core.exception.NubeException;
import com.nube.core.service.idm.AuthService;
import com.nube.core.vo.apps.App;
import com.nube.core.vo.response.Response;
import com.nube.core.vo.response.ValidResponse;
import com.nube.core.vo.response.VoidResponse;
import com.nube.portal.context.SessionContext;
import com.nube.portal.local.apps.LocalAppProcessorTask;
import com.nube.portal.request.apps.AppUpdateRequest;
import com.nube.portal.request.apps.ContextRequest;
import com.nube.portal.response.apps.AppHistory;
import com.nube.portal.service.apps.AppManagerService;
import com.nube.portal.service.apps.AppUploaderService;
import com.nube.portal.threads.apps.AppProcessorTask;

/**
 * authentication controller
 * 
 * @author kamoorr
 */
@Controller("appsRestController")
public class AppsRestController {

	
	@Autowired
	@Qualifier("nubeAuthService")
	AuthService authService;
	
	@Autowired
	AppUploaderService uploaderService;
	
	@Autowired
	AppProcessorTask appProcessorTask;
	
	@Autowired(required=false)
	LocalAppProcessorTask localAppProcessorTask;
	
	@Autowired
	AppManagerService appManagerService;
	
	@Value("${nube.app.filetype}")
	String fileType;



	static Logger logger = Logger.getLogger(AppsRestController.class);
	
	
	/**
	 * Create a new context, Content-Type should be application/json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/v1/apps/create-context", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody
	Response<VoidResponse> createNewContext(
			@RequestBody ContextRequest context,
			final HttpServletRequest request, final HttpServletResponse response) {

		try {

			logger.info(String.format("Create new context %s", context));
			
			appManagerService.createAppContext(SessionContext.getUser(), context.getContext(), context.getDescription());
			
		    return new ValidResponse<VoidResponse>(new VoidResponse());

		}catch (NubeException nException) {
			logger.error("Error", nException);
			return new ValidResponse<VoidResponse>(nException);
		}

	}
	
	
	/**
	 * Delete context, Content-Type should be application/json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/v1/apps/delete-context", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody
	Response<VoidResponse> deleteContext(
			@RequestBody ContextRequest context,
			final HttpServletRequest request, final HttpServletResponse response) {

		try {

			logger.info(String.format("Delete  context %s", context));
			
			appManagerService.deleteAppContext(SessionContext.getUser(), context.getContext());
			
		    return new ValidResponse<VoidResponse>(new VoidResponse());

		}catch (NubeException nException) {
			return new ValidResponse<VoidResponse>(nException);
		}

	}
	

	/**
	 * Login, Content-Type should be application/json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/v1/apps/history", method = RequestMethod.GET, consumes = "application/json")
	public @ResponseBody
	Response<AppHistory> appHistory(
			@RequestParam(value="context", required=false) String context,
			final HttpServletRequest request, final HttpServletResponse response) {

		try {

			logger.info(String.format("History request for %s", context));
			
			List<App> appVersions = appManagerService.getAppVersions(context);
			
			String currentVersion = appManagerService.getCurrentVersion(context).getVersion();
			
		    return new ValidResponse<AppHistory>(new AppHistory(context, appVersions, currentVersion));

		}catch (NubeException nException) {
			return new ValidResponse<AppHistory>(nException);
		}

	}
	
	/**
	 * Login, Content-Type should be application/json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/v1/apps/update", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody
	Response<VoidResponse> appUpdate(
			@RequestBody AppUpdateRequest updateRequest,
			final HttpServletRequest request, final HttpServletResponse response) {

		try {

			logger.info(String.format("Update request for %s", updateRequest.getContext()));
	
			appManagerService.updateActiveVersion(updateRequest.getContext(), updateRequest.getActiveVersion());
			
		    return new ValidResponse<VoidResponse>(new VoidResponse());

		}catch (NubeException nException) {
			return new ValidResponse<VoidResponse>(nException);
		}

	}
	
	/**
	 * Upload a file
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/v1/apps/upload", method = RequestMethod.POST)
	public @ResponseBody
	Response<VoidResponse> uploadApp(@RequestParam("app") MultipartFile file) {
		try {
			String destFile = uploaderService.save(file,
					file.getOriginalFilename(), fileType);
			appProcessorTask.processFile(destFile);
			logger.info("Upload completed, but not processed yet");
		} catch (NubeException e) {
			logger.error(e);
			return new ValidResponse<VoidResponse>(e);
		} catch (Exception e) {
			logger.error(e);
		}
		return new ValidResponse<VoidResponse>(new VoidResponse());
	}

	

	/**
	 * Login, Content-Type should be application/json
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/v1/apps/update-from-local", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody
	Response<VoidResponse> appUpdateFromLocal(
			@RequestBody AppUpdateRequest updateRequest,
			final HttpServletRequest request, final HttpServletResponse response) {

		try {

			logger.info(String.format("appUpdateFromLocal request for %s", updateRequest.getAppFolder()));
			
			if(localAppProcessorTask == null){
				logger.error("Local setup not enabled yet");
				throw new NubeException("local_disabled");
			}else{
				localAppProcessorTask.processFile(updateRequest.getAppFolder());
			}
		    return new ValidResponse<VoidResponse>(new VoidResponse());

		}catch (NubeException nException) {
			logger.error("Error in request", nException);
			return new ValidResponse<VoidResponse>(nException);
		}

	}
}
