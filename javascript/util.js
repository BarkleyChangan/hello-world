/**
 * 判断浏览器是否支持flash的方法
 */
function hasUsableSWF(){
	var swf;     
	if(typeof window.ActiveXObject != "undefined"){
        swf = new  ActiveXObject("ShockwaveFlash.ShockwaveFlash");
    }else{
         swf = navigator.plugins['Shockwave Flash'];
    }
    return swf ? true : false;
};