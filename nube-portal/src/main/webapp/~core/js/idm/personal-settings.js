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
					 "en_us": "Password updated successfully."
				 },
				 "user_not_found":{
					 "en_us": "Current password is not matching with our records."
				 }
			}
		);
	
	/**
	 * login success call back,
	 */
	var changePwdCallBack = function(data){
		
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
	var changePwd = function(){
		var input = {
				  "currentPassword":$("#currentPwd").val(),
				  "newPassword":$("newPwd").val()
		  	};
		  Rest.post("/v1/users/change-password",input, changePwdCallBack);
	}
	/**
	 * OnSubmit validator, this will use http://rickharrison.github.io/validate.js/ to validate,
	 * 
	 */
	var validator = new FormValidator('changePwdForm', [{
		name: 'currentPwd',
	    display: 'current password',    
	    rules: 'required'
	},{
		name:"newPwd",
		display:"new password",
		rules:'required'
	}
	], function(errors, event) {
		//After validate handler
	    if (errors.length == 0) {
	    	changePwd();
	    }else{
	    	resetMsgBox();
	        for (var i = 0; i < errors.length; i++) {
	        	appendToMsgBox(errors[i].message, errors[i].id);
	        }
	        
	    }
	});
	
	
});
