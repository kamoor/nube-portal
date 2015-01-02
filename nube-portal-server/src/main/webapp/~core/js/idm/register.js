/**
 * Code to register a new user
 */

$(document).ready(function() {
	/**
	 * Declare all the message keys and text here
	 */
	var msgHandler  = new MsgHandler(
		{
			"user_exists": {
				"en_us": "This email address already exists. Try login or reset password."
			 },
			 "default": {
				"en_us":"Something went wrong at server. Please try later."
			 }
		}
	);
	
	/**
	 * Register success call back,
	 */
	var registerCallBack = function(data){
		resetMsgBox();
		if(data.status == "success"){
			window.location = "/admin";
		}else{
			appendToMsgBox(msgHandler.val(data.status));
		}
	};
	
	/**
	 * register a user after validation
	 */
	var register = function(){
		var input = {
				  "firstName":$("#fn").val(),
				  "lastName":$("#ln").val(),
				  "email":$("#email").val(),
				  "password":$("#pwd").val()
		  	};
		  Rest.post("/v1/users/create",input, registerCallBack);
		  
	}
	/**
	 * OnSubmit validator, this will use http://rickharrison.github.io/validate.js/ to validate,
	 * 
	 */
	var validator = new FormValidator('regForm', [{
	    name: 'fn',
	    display: 'first name',    
	    rules: 'required'
	}, {
		name: 'ln',
	    display: 'last name',    
	    rules: 'required'
	},
	{
		name: 'email',
	    display: 'email',    
	    rules: 'required|valid_email'
	},{
		name: 'pwd',
	    display: 'password',    
	    rules: 'required|min_length[8]|matches[re-pwd]'
	}
	], function(errors, event) {
		//after register validator
	    if (errors.length == 0) {
	    	register();
	    }else{
	    	resetMsgBox();
	        for (var i = 0; i < errors.length; i++) {
	        	appendToMsgBox(errors[i].message, errors[i].id);
	        }
	        
	    }
	});
	
	
	
	
	
});
