/**
 * This will allow easy way to make Rest api(post, get, put) call
 * Example: Rest.post("/admin/users/create",{"email":"test@test.com",callBack});
 */
var Rest = Rest || (function($) {
			
	return {
			post : function(api,request,callbackFn,errorCallBkFn){
				this.ajax(api,request,callbackFn,errorCallBkFn,"POST");
			},
			get : function(api,request,callbackFn,errorCallBkFn){
				this.ajax(api,request,callbackFn,errorCallBkFn,"GET");
			},
			put : function(api,request,callbackFn,errorCallBkFn){
				this.ajax(api,request,callbackFn,errorCallBkFn,"PUT");
			},
			defaultCallBackOnSuccess : function(data, text) {
				console.log("200 OK, but no handler found. Status: "+ data.status);
			},
			defaultCallBackOnError : function(data, text) {
				console.log("ERROR, but no handler found. Status: "+ data.status);
				resetMsgBox();
				appendToMsgBox("Unknown server error, Please try again later");
			},
		   
			ajax : function(api, request, callBkFn, errorCallBkFn, httpMethod) {

				console.log("calling : "+ api);
				//assign gew default values 
				httpMethod = (httpMethod == null)?"post":httpMethod;
				callBkFn = (callBkFn == null)?this.defaultCallBackOnSuccess:callBkFn;
				errorCallBkFn = (errorCallBkFn == null)?this.defaultCallBackOnError :errorCallBkFn;
				;
				
				/*convert all request to URL for get*/
				if(httpMethod == "GET"){
					input ="";
					for (var key in request) {
							if (request.hasOwnProperty(key)) {
								input = input + key+"="+request[key] +"&";
							}				
					}
				}else{
					input = (typeof request == "object")?JSON.stringify(request):request
				}
			   
			    $.ajax({
			    	url:api,
					context : document.body,
					data : input,
					success : callBkFn,
					error : errorCallBkFn,
					type : httpMethod,
					contentType: "application/json"
				});

			}
			
	};
}(jQuery));


