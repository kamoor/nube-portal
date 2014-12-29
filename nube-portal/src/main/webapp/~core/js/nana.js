/**
 * This is the basic version of Nube Analytics. This will fire pixel tags to NubeAnalytics engine.
 * Usage: 
 * Nana.fire();
 * Nana.fire({"evSc":"register"});
 * 
 */
var Nana = (function() {
	
	var sess = null;
	
	function submitDt(event, eventSrc, data){
		input ="";
		for (var key in data) {
				if (data.hasOwnProperty(key)) {
					input = input + key+"="+data[key] +"&";
				}				
		}
		var rand = Math.floor(Math.random()*100000);
		var url = "/v1/ana/nube.png?r=" + rand;
		url+= "&ev="+event;
		url+= "&evsc=" + (typeof(eventSrc) == "undefined"? location.pathname: eventSrc);
		url+= (typeof(app.id) == "undefined"? "" : "&aid=" + app.id);
		url+= document.referrer == ""?"":"&ref=" + document.referrer;
		//url+= "&sid=" + (sess == null? getSess(): sess);
		url+= "&"+input;
		addPx(url);
		
	}
	
	function addPx(url){
		var img = document.getElementById('npix') ;
		if(img == null){
			img = document.createElement("img");
		}
		img.src = url;
		img.id  = "npix";
		document.body.appendChild(img);
	}
	
	return {
		
		    /*track any event*/
		    fire: function(event, eventSrc, data){
		    	event = (event == null)?"view":event;
		    	submitDt( event, eventSrc, data);
		    },
		    /*track  clicks*/
		    fireClick: function(eventSrc, data){
		    	if(eventSrc == null){
		    		return;
		    	}else{
		    		submitDt("click", eventSrc, data);
		    	}
		    },
		    /*track  hover*/
		    fireHover: function(eventSrc, data){
		    	if(eventSrc == null){
		    		return;
		    	}else{
		    		submitDt("hover", eventSrc, event);
		    	}
		    }
	};
})();


