(function(window){
	var storage = {};
	// 是否支持localStorage
	if(window.localStorage){
		storage.support = true;
	} else {
		storage.support = false;
	}
	// time为超时时间(单位毫秒),非必填
	storage.setItem = funciton(key, value, time){
		if(this.support) {
			if(typeof key !== "string"){
				console.log("*STORAGE ERROR* key必须是字符串");
				return;
			}

			if(time) {
				if(typeof time !== "number"){
					console.log("*STORAGE ERROR* time必须是数字");
					return;
				} else {
					time = parseInt(time, 10) + (new Date()).getTime();
				}
			} else {
				time = null;
			}

			var setValue = {
				value: JSON.stringify(value),
				time: time
			};
			localStorage.setItem(key, JSON.stringify(setValue));
		} else {
			storage.setCookie(key, value, time);
		}
	};
	// 不存在的值返回null
	storage.getItem = function(key){
		if(this.support){
			var getValue = JSON.parse(localStorage.getItem(key));
			if(!getValue){
				return null;
			}
			if(getValue.time && getValue.time < (new Date()).getTime()){
				localStorage.removeItem(key);
				return null;
			} else {
				return JSON.parse(getValue.value);
			}
		} else {
			return storage.getCookie(key);
		}
	};
	// 移除某个值
	storage.removeItem = function(key){
		if(this.support){
			localStorage.removeItem(key);
		} else {
			storage.removeCookie(key);
		}
	};
	// 情况存储
	storage.clear = function(){
		if(this.support){
			localStorage.clear();
		} else {
			storage.clearCookie();
		}
	};
	/***** cookie方法 *****/
	storage.setCookie = function(key, value, time){
		if(typeof key !== "string"){
			console.log("*STORAGE ERROR* key必须是字符串");
			return;
		} else {
			if(typeof time !== "number"){
				// cookie默认存365天
				time = 365 * 24 * 60 * 60 * 1000;
			}
			var d = new Date();
			d.setTime(d.getTime() + time);
			document.cookie = key + "=" + value + ";expires=" + d.toGMTString();
		}
	};
	storage.getCookie = function(key){
		var cookies = document.cookie.split(";");
		var cookieValule;
		for(var i=0;i < cookies.length;i++){
			if(key === cookies[i].split("=")[0]){
				cookieValue = cookies[i].split("=")[1];
				break;
			}
		}
		return cookieValue;
	};
	storage.removeCookie = function(key){
		document.cookie = key + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
	};
	storage.clearCookie = function(){
		var cookies = document.cookie.split(";");
		for(var i=0;i < cookies.length;i++){
			document.cookie = cookies[i].split("=")[0] + "=;expires=Thu, 01 Jan-1970 00:00:00 GMT";
		}
	};

	window.storage = storage;
}(window));