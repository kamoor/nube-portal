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
					 "en_us": "api call is success. Use read api to get new content"
				 }
			}
		);
	
	
	/**
	 * Success call back
	 */
	var apiCallBk = function(response){
		
		var content = "<b>Status: " + response.status + "</b><hr/>";
		if(response.status == "success"){
			content+=JSON.stringify(response.data);
		}
		$("#api-response-content").html(content);
		
	}

	/**
	 * Error call back
	 */
	var apiCallErrorBk = function(response){
		$("#api-response-content").html("Error occured while processing. Error: "+ response.status + "-" +response.statusText);
	}
	
	/**
	 * This will try recursively got to object and attach value
	 * index =0 to length
	 * keys in array contains all keys location.name etc
	 */
	var attachValue = function(index, input, keys, value){
		
		length = keys.length;
		
		if(index == length - 1){
		
		  input[keys[index]] = value;
		}else{
		  if(typeof input[keys[index]] == "undefined"){
			  input[keys[index]] = {};
		  }
		  index++;
		  attachValue(index, input[keys[index-1]], keys, value);
		}
		return;
	}
	
	/**
	 * Invoke api on submit
	 */
	var invokeApi = function(uri, index){
		var uri = $(this).attr("uri");
		var methodIndex = $(this).attr("methodIndex");
		var httpMethod = $(this).attr("httpMethod");
		
		var input = {};
		$("input[id^="+methodIndex+"_req]").each(function(){
			//populate value
			if($(this).val() != ""){
				//check for sub properties, all names are separated by . location.name will go to input["location"]["name"]
				var keys = $(this).attr("param").split(".");
				var val = $(this).attr("param") == "number"? parseInt($(this).val()):$(this).val();
				attachValue(0, input, keys,val);
				///input[$(this).attr("param")] = $(this).val();
			}
		});
		$("#api-response-content").html("Invoking api..");
		if(httpMethod == "POST"){
			Rest.post(uri,input, apiCallBk, apiCallErrorBk);
		}else{
			Rest.get(uri,input, apiCallBk, apiCallErrorBk);
		}
		
	}
	
	/**
	 * Render a pretty UI based on API
	 * based on schema.json
	 */
	var renderApiAdmin = function(schema){
		var apiSchema = JSON.parse(schema);
		
		//Add index on arrays
		for(var key in apiSchema.methods){
			//Add index on all methods, 0...n
			apiSchema.methods[key].index = key;
			//Replace default values in request
			for(var reqKey in apiSchema.methods[key].request.properties){
				//see if any request need default variable
				if(typeof apiSchema.methods[key].request.properties[reqKey].default != 'undefined' && 
				   apiSchema.methods[key].request.properties[reqKey].default.indexOf("$") ==0 ){
					//replace default values from current user object
					apiSchema.methods[key].request.properties[reqKey].default = eval("currUser."+apiSchema.methods[key].request.properties[reqKey].default.substring(1));
				}
			}
		}
		
		$("#apiAdmin").html(Mustache.render($("#api-admin-template").html(), apiSchema));
		$("#li-0").addClass("active");
		$("#api-tab-0").addClass("active");
		
		for(var key in apiSchema.methods){
			$("#api-tab-"+ key).html($("#api-tab-content-"+ key).html());
		}
		//attach onclick event for all screens
		$(".invoke-api-btn-class").on("click", invokeApi);
	}
	/**
	 * Error handler if there error in calling schema.json
	 */
	var errorSchemaLoad = function(){
		$("#apiAdmin").html(Mustache.render($("#api-admin-template").html(), {"error":"This api is not available to use now. Please try again later or contact support."}));
	}
	
	/**
	 * This will create screen to manage all methods in one api
	 * On click of admin button
	 */
	var populateApiInfo = function(){
		var apiId = $(this).attr("apiId");
		$("#apiAdmin").html("Loading...");
		Rest.get("/docs/api/"+apiId+"/schema.json","", renderApiAdmin, errorSchemaLoad);
	}
	
	/**
	 * Find out all API info and render as API - list
	 */
	var findAllApis = function(content){
		var apisObjects = JSON.parse(content);
		for(var key in apisObjects.apis){
			var api = apisObjects.apis[key];
			$("#api-list-div").append(Mustache.render($("#api-list-template").html(), api));
		}
		//Onclick of admin button
		$(".api-admin-btn").click(populateApiInfo);
		
	}
	
	
	/**
	 * Load all schema.json on user logged in
	 */
	$( "body").on( "com.nube.onUserLoad", function(){
		if(currUser.primaryContext != null){
			Rest.get("/docs/api/apis.json","", findAllApis);
		}
		
	 });
	/**
	 * Clear api response on click of tab
	 */
	$("#api-admin-tabs").on("click", function(){
		$("#api-response-content").html("");
	});
	
});
	


