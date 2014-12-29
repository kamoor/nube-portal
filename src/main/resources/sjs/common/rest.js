
(function (global, factory) {

	factory(global.Rest = {});
	
}(this, function (rest) {
	
	rest.http = function(url, request, method) {
		
		method = (method == null?"GET":method);
		
		if(request != undefined){
			for (var key in request) {
				if (request.hasOwnProperty(key)) {
					url = url + key+"="+request[key] +"&";
				}				
			}
		}
		
	    with (new JavaImporter(java.io, java.net)) {
	        var is = new URL(url).openConnection();
	        try {
	        	
	         	
	            is.setRequestProperty("Content-Type", "application/json");
	            is.setRequestMethod(method);
	            var reader = new BufferedReader(
	                new InputStreamReader(is.getInputStream()));
	            var buf = '', line = null;
	            while ((line = reader.readLine()) != null) {
	                buf += line;
	            }
	        } finally {
	           reader.close();
	        }
	        return buf;
	    }
	};
	
	rest.get = function(url, request){
		return JSON.parse(rest.http(url, request, "GET"));
	};
	
}));