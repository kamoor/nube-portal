/**
 * Code to load any info in secure page
 */

$(document).ready(function() {
	
	var msgHandler  = new MsgHandler(
			{
				 "default": {
					"en_us":"Something went wrong at server. Please try later."
				 },
				 "no_app_found":{
					 "en_us":"No applications found."
				 }
			}
		);
	
	
	
	/**
	 * Switch app to another one
	 */
	var switchApp = function(){
		var input = {
				"primaryContext" : $(this).attr("app")
		};
		Rest.post("/v1/users/update",input, reloadCurrentUser);
		
	}
	
	
	var renderAppContexts= function(profile){
		var noOfApps=0;
		if(profile.data.roles != null){
			$("#contexts").html("");
			for(var app in profile.data.roles){
				//Do not show any nube internal apps, check for ROLE_ADMIN role
				if(app.lastIndexOf("nube", 0) < 0 &&  profile.data.roles[app][0].name == "ROLE_ADMIN"){
					if(app == profile.data.primaryContext){
						$("#contexts").append("<li class='btn btn-primary'>"+app+"</li>");

					}else{
						$("#contexts").append("<li class='btn btn-default' id='switchapp' app='"+ app +"' >" + app +"</li>");

					}
					noOfApps++;
				}
			}
		}
		if(noOfApps == 0){
			$("#contexts").append("<li class='label label-warning'>"+msgHandler.val("no_app_found")+"</li>");
		}
		//On click of any app, switch that to primary
		$("li[id^=switchapp]").click(switchApp);
	}
	
	/**
	 * Get history call back
	 */
	var renderUserInfo= function(response){
		if(response.status == "success"){
			//Make profile public
			currUser = response.data;
			//Now populate prime areas
			var fullName = response.data.firstName + " " + response.data.lastName;
			fullName = (fullName.length > 16)?fullName.substr(0,16) + "..":fullName;
			$("#u-fullname").text(fullName);
			$("nav").show();
			//render all app contexts
			renderAppContexts(response);
			//Now publish an event 
			 $("body").trigger( "com.nube.onUserLoad");
		}
			
	}
	
	var reloadCurrentUser = function(){
		Rest.get("/v1/users/my",null, renderUserInfo);
	}
	
	/**
	 * Get history
	 */
	var postLogin = function(){
		reloadCurrentUser();
		//More activities todo
		
	}
	
	/**
	 * Load history on page load
	 */
	 postLogin();
	 
	 
	
});



