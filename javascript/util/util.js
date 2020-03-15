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

function isArrayFn(value){
    // 首先判断浏览器是否支持Array.isArray这个方法
    if (typeof Array.isArray === "function") {
        return Array.isArray(value);
    }else{
        return Object.prototype.toString.call(value) === "[object Array]";
        // return obj.__proto__ === Array.prototype;
    }
}