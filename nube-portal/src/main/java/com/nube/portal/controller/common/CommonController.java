package com.nube.portal.controller.common;

import org.springframework.web.servlet.ModelAndView;

public class CommonController {

		public static final String EXT_JSP=".jsp";
		
		/**
		 * Return JSP view
		 * @param screen
		 * @return
		 */
		public  ModelAndView jspView(String screen){
			return new ModelAndView(screen + EXT_JSP);
		}
}
