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
					 "en_us": "New application id created. Now you can upload an to this context."
				 },
				 "context_invalid":{
					 "en_us":"Please enter a valid id, english letters or numbers seperated by hyphen(-). Exampple: nube-noodles"
				 },
				 "context_exists":{
					 "en_us":"This app id is already taken, try another one"
				 }
			}
		);
	
	/**
	 * login success call back,
	 */
	var createContextCallBk = function(data){
		
		if(data.status == "success"){
			location.reload();
		}else{
			resetMsgBox("error", "msg-app");
			appendToMsgBox(msgHandler.val(data.status), null, "msg-app");
		}
	};
	
	/**
	 * login after validation
	 */
	var createContext = function(){
		
		var input = {
				  "context":$("#new-context").val(),
				  "description":$("#context-descr").val()
		  	};
		  Rest.post("/v1/apps/create-context",input, createContextCallBk);
	}
	/**
	 * File upload
	 * 
	 */
	$("#create-context-btn").click( function(){
			createContext();
	});
	
	
});
	


