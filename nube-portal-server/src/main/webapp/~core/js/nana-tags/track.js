/**
 * Basic analytics tracking calls
 * Usage: 
 *   Just add "evsc" (event source) tag to any button or href with any value you want
 *   example: <a href="/login" evsc="Login"/>Login</a>
 * Nana.fire({"evSc":"register"});
 * 
 */

/**
 * Attach all events and define event handler
 */
$(document).ready(function() {
	
   
   //All Event handlers (view)
   $("body").on( "com.nube.ana.fire.view", function(e){
		Nana.fire();
   });
   //All Event handlers (clicks), will not fire if evsc attribute not present on html element.
   $("body").on( "com.nube.ana.fire.click", function(e){
		Nana.fireClick($(e.target).attr("evsc"));
   });
   
   //Event attach: page view
   $("body").trigger( "com.nube.ana.fire.view");
   //Event attach: All button clicks
   $("body").click(function(e) { 
		 $(e.target).trigger( "com.nube.ana.fire.click");
   });
});

/**
 * Fire a tag on on click
 * */





