/**
 * Error Message handler
 * this object require 2D array of [key][locale] >> actual text to show
 */

var MsgHandler = function(msgMap){
	
	this.map = msgMap;
	
	MsgHandler.prototype.val = function(key, locale){
		//default to en_us
		locale = (typeof locale == 'undefined') ? "en_us" : locale;
		
		if(this.map[key] == null){
			return (this.map["default"][locale] == null)?"Unknown message":this.map["default"][locale];
		}else{
			return this.map[key][locale];
		}
	}
	
}
