/**
 * Code to register a new user
 */

$(document).ready(function() {
	
	
	var msgHandler  = new MsgHandler(
			{
				"wrong_credentials": {
					"en_us": "Login failed. Wrong email or password."
				 },
				 "default": {
					"en_us":"Something went wrong at server. Please try later."
				 }
			}
		);
	
	/**
	 * login success call back,
	 */
	var loginCallBack = function(data){
		if(data.status == "success"){
			window.location = "/admin";
		}else{
			resetMsgBox();
			appendToMsgBox(msgHandler.val(data.status));
		}
	};
	
	/**
	 * login after validation
	 */
	var login = function(){
		var input = {
				  "email":$("#email").val(),
				  "password":$("#pwd").val()
		  	};
		  Rest.post("/v1/auth/login",input, loginCallBack);
	}
	/**
	 * OnSubmit validator, this will use http://rickharrison.github.io/validate.js/ to validate,
	 * 
	 */
	var validator = new FormValidator('loginForm', [{
		name: 'email',
	    display: 'email',    
	    rules: 'required|valid_email'
	},{
		name: 'pwd',
	    display: 'password',    
	    rules: 'required'
	}
	], function(errors, event) {
		//After validate handler
	    if (errors.length == 0) {
	    	login();
	    }else{
	    	resetMsgBox();
	        for (var i = 0; i < errors.length; i++) {
	        	appendToMsgBox(errors[i].message, errors[i].id);
	        }
	        
	    }
	});
	
	
});
