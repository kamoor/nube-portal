package com.nube.portal.controller.admin;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nube.core.exception.NubeException;
import com.nube.portal.controller.common.CommonController;
import com.nube.portal.service.apps.AppUploaderService;
import com.nube.portal.threads.apps.AppProcessorTask;

/**
 * Admin controller
 * 
 * @author kamoorr
 */
@Controller("adminController")
public class AdminController extends CommonController{

	

	@PostConstruct
	public void postConstruct() {
		logger.info("Portal admin controller initialized");
	}

	static Logger logger = Logger.getLogger(AdminController.class);

	/**
	 * Just hello world
	 * @param user
	 * @return
	 */
	@RequestMapping("/helloworld")
	public ModelAndView helloworld(final HttpServletRequest request) {
		logger.info("hello worl request");
		return super.jspView("helloworld");
	}
	
	/**
	 * Admin home page
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/admin")
	public ModelAndView defaultAdminRequest(final HttpServletRequest request) {
		logger.info("admin request");
		return super.jspView("index");
	}

	@RequestMapping({"/admin/{screen}", "/auth/{screen}"})
	public ModelAndView adminScreens(final HttpServletRequest request,
			@PathVariable("screen") String screen) {
		logger.info("admin request " + screen);
		return super.jspView(screen);
	}
	
	
	@RequestMapping("/auth/logout")
	public ModelAndView adminLogout(final HttpServletRequest request, HttpServletResponse response) {
		request.getSession(true).invalidate();
		return super.jspView("login");
	}

}
