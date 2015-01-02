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
					 "en_us": "App package has been uploaded. You can manage this version from version history tab."
				 },
				 "file_not_selected":{
					 "en_us":"Please select a valid package(zip) to upload"
				 }
			}
		);
	
	/**
	 * login success call back,
	 */
	var uploadCallBack = function(data){
		
		if(data.status == "success"){
			resetMsgBox("success");
			appendToMsgBox(msgHandler.val(data.status));
			$('#appUploadForm').trigger("reset");
		}else{
			resetMsgBox();
			appendToMsgBox(msgHandler.val(data.status));
		}
	};
	
	/**
	 * login after validation
	 */
	var upload = function(){
		
		var formData = new FormData();
		formData.append('app', $('#app').get(0).files[0]);
		
		$.ajax({
			    url: "/v1/apps/upload",
			    data: formData,
			    cache: false,
			    processData: false,
			    contentType:false,
			    type: 'POST',
			    success: function(data){
			    	resetMsgBox("success");
					appendToMsgBox(msgHandler.val(data.status));
			    }
			});
	}
	/**
	 * File upload
	 * 
	 */
	$("#submitApp").click( function(){
		if(typeof $('#app').get(0).files[0] == 'undefined'){
			resetMsgBox();
			appendToMsgBox(msgHandler.val("file_not_selected"));
		}else{
			upload();
		}
	});
	
	
	

});

/**
 * App history
 */
$(document).ready(function() {
	var msgHandler  = new MsgHandler(
			{
				 "default": {
					"en_us":"Something went wrong at server. Please try later."
				 },
				 "no_data_found":{
					 "en_us":"There are no application version found."
				 }
			}
		);
	
	

	/**
	 * Get history call back
	 */
	var updateAppCallBack= function(response){
		if(response.status == "success"){
			getHistory(currUser.primaryContext);
		}else{
			resetMsgBox()
			appendToMsgBox(msgHandler.val("default"));
			return false;
		}
	}
	
	
	var deleteApp = function(){
		var input = {
				  "context":$("#app-id").html()
		};
		Rest.post("/v1/apps/delete-context",input, function(){
			window.location.href = "/admin";
		});
		
	}
	
	/**
	 * updateApps
	 */
	var updateApps = function(){
		var input = {
				  "context":currUser.primaryContext,
				  "activeVersion":$(this).attr("version")
		};
		Rest.put("/v1/apps/update",input, updateAppCallBack);
		return false;
	}
	
	
	
	/**
	 * Add each version to <ul>
	 */
	var addVersion=function(html){
		var ul = $("#versions").append("<li>"+html+"</li");
	}
	
	/**
	 * Get history call back
	 */
	var historyCallBack= function(response){
		if(response.status == "success"){
			$("#versions").empty();
			if(response.data.apps.length == 0){
				addVersion(msgHandler.val("no_data_found"));
			}else{
				
				$("#app-curr-version").html(response.data.activeVersion);
				
				for(var i=response.data.apps.length -1 ; i>=0; i--){
					if(response.data.apps[i].version == response.data.activeVersion){
						addVersion(response.data.apps[i].name + " - " + response.data.apps[i].version + " <a href='/"+response.data.context+"' target='_blank'>Preview</a> <span class=cactive>Active</span>");
					}else{
						addVersion(response.data.apps[i].name + " - " + response.data.apps[i].version + " <a href='#' version='" + response.data.apps[i].version + "' id='updateVersion'>Activate</a>");
					}
					
				}
				$('a[id^=updateVersion]').click(updateApps);
				
			}
			
		}else{
			resetMsgBox()
			appendToMsgBox(msgHandler.val("default"));
			return false;
		}
	}
	
	/**
	 * Get history
	 */
	var getHistory = function(context){
		
		$("#app-id").html(context);
		$("#app-to-be-deleted").html(context);
		$("#delete-app-btn").click(deleteApp);
		var input = {
				  "context":context
		  	};
		 Rest.get("/v1/apps/history",input, historyCallBack);
		  
		  
		  
	}
	
	var msgHandlerLocal  = new MsgHandler(
			{
				 "default": {
					"en_us":"Something went wrong at server. Please try later."
				 },
				 "success":{
					 "en_us": "App update process initiated. Check log for any potential errors"
				 }
			}
		);
	
	/**
	 * Local setup, update app from local workspace
	 */
	var updateAppsFromLocalCallBk = function(response){
		resetMsgBox()
		if(response.status="success"){
			appendToMsgBox(msgHandlerLocal.val("success"), "success");
		}else{
			appendToMsgBox(msgHandlerLocal.val("default"));
		}
		
	}
	
	/**
	 * Make a request to update app from local setup
	 */
	var updateAppsFromLocal= function(){
		var input = {
				  "appFolder":$("#appfolder").val()
		};
		Rest.post("/v1/apps/update-from-local",input, updateAppsFromLocalCallBk);
	}
	
	
	/**
	 * Load history on user logged in
	 */
	$( "body").on( "com.nube.onUserLoad", function(){
		if(currUser.primaryContext != null){
			//get app history
			getHistory(currUser.primaryContext);
			//local app update
			$("#updateAppBtn").click(updateAppsFromLocal);
			
		}
		
	 });
	
});
	


