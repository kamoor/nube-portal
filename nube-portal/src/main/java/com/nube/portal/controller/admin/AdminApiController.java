package com.nube.portal.controller.admin;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nube.api.core.idm.request.UserRequest;
import com.nube.core.constants.ErrorCodes;
import com.nube.core.exception.NubeException;
import com.nube.core.service.idm.AuthService;
import com.nube.core.vo.idm.AuthResponse;
import com.nube.core.vo.response.Response;
import com.nube.core.vo.response.ValidResponse;
import com.nube.portal.context.SessionContext;

/**
 * API Administration controller
 * 
 * @author kamoorr
 */
@Controller("adminApiController")
public class AdminApiController {

	@PostConstruct
	public void postConstruct() {
		logger.info("api admin controller initialized");
	}

	static Logger logger = Logger.getLogger(AdminApiController.class);

	/**
	 * Login, Content-Type should be application/json
	 * 
	 * @param user
	 * @return
	 */

	@RequestMapping(value = "/docs/api/{api-code}/{file-name}.json", method = RequestMethod.GET, consumes = "*/*")
	public String apiSchema(
			@PathVariable(value = "api-code") String apiCode,
			@PathVariable(value = "file-name") String fileName,
			final HttpServletRequest request, final HttpServletResponse response) {

		String file = "docs/api/" + apiCode + "/" + fileName + ".json";
		logger.info("api docs - "+file);
		return "docs/api/" + apiCode + "/" + fileName + ".json";

	}
	
	/**
	 * Return api registration file apis.json
	 */
	@RequestMapping(value = "/docs/api/{file}.json", method = RequestMethod.GET, consumes = "*/*")
	public String getApis(@PathVariable(value = "file") String file, final HttpServletRequest request, final HttpServletResponse response){
		return  "docs/api/"+file+".json";
	}


}
