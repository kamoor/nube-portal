/**
 * This file only has methods used in entire admin screens. DO not add screen specific code.
 */

/*This code will keep track of bootstrap tab history url will have #<tabname>*/
$(document).ready(function() {
			   if (location.hash !== '') $('a[href="' + location.hash + '"]').tab('show');
			    return $('a[data-toggle="tab"]').on('shown', function(e) {
			      return location.hash = $(e.target).attr('href').substr(1);
			    });
});

$(document).ready(function() {
	/*Code to show sidenav highlighted for current page*/
	$('ul > li a[href]').filter(function() {
	    return $(this).attr('href') === window.location.pathname;
	}).addClass('current');

});

/*All public methods for admin screen*/
$(document).ready(function() {
	/**
	 * public method to clear message box
	 */
	resetMsgBox = function resetMsgBox(cssClass, msgId){
		cssClass = (typeof cssClass == 'undefined') ? "error" : cssClass;
		msgId = (typeof msgId == 'undefined') ? "msg" : msgId;
		$("#"+msgId).removeClass().addClass(cssClass).html("");
		$( "input" ).removeClass("highlight");
	}
	/**
	 * public method to append error message.
	 * msgText gets added to message box
	 * Optionally, highlight user entry field to red
	 */
	appendToMsgBox = function(msgText, highlightId, msgId){
		msgId = (typeof msgId == 'undefined') ? "msg" : msgId;
		$("#" + msgId).append(msgText).append("<br/>");
		if(highlightId != 'undefined'){
			$("#"+ highlightId).addClass("highlight");
		}
	}
});

