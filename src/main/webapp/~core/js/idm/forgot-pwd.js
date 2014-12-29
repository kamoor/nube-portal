/**
 * Code to forgot password
 */

$(document).ready(function() {
	
	
	var msgHandler  = new MsgHandler(
			{
				 "default": {
					"en_us":"Something went wrong at server. Please try later."
				 },
				 "success":{
					 "en_us": "Please check your email for next steps. You may have different email in records if you don't receive any notification."
				 }
			}
		);
	
	/**
	 * login success call back,
	 */
	var forgotPasswordCallBack = function(data){
		
		if(data.status == "success"){
			resetMsgBox("success");
			appendToMsgBox(msgHandler.val(data.status));
		}else{
			resetMsgBox();
			appendToMsgBox(msgHandler.val(data.status));
		}
	};
	
	/**
	 * login after validation
	 */
	var forgotPwd = function(){
		var input = {
				  "email":$("#email").val()
		  	};
		  Rest.post("/v1/auth/forgot-password",input, forgotPasswordCallBack);
	}
	/**
	 * OnSubmit validator, this will use http://rickharrison.github.io/validate.js/ to validate,
	 * 
	 */
	var validator = new FormValidator('forgotPwdForm', [{
		name: 'email',
	    display: 'email',    
	    rules: 'required|valid_email'
	}
	], function(errors, event) {
		//After validate handler
	    if (errors.length == 0) {
	    	forgotPwd();
	    }else{
	    	resetMsgBox();
	        for (var i = 0; i < errors.length; i++) {
	        	appendToMsgBox(errors[i].message, errors[i].id);
	        }
	        
	    }
	});
	
	
});
