;(function(window, undefined){
	window.DATE_UTIL = window.DATE_UTIL || {};
	
	window.DATE_UTIL.parse = function(strDate, splitDate, spliteTime){
		var date = null;
		if(strDate){
			var year = 0,
			    month = 0,
			    day = 0,
			    hour = 0,
			    minutes = 0,
			    seconds = 0,
			    splitDate = splitDate || "-",
			    spliteTime = spliteTime || ":",
			    arrTemp = strDate.split(" ");
			
			if(arrTemp.length > 0){
				var arrDate = arrTemp[0].split("-");
				year = parseInt(arrDate[0], 10);
				month = parseInt(arrDate[1], 10);
				day = parseInt(arrDate[2], 10);
			}
			
			if(arrTemp.length > 1){
				var arrTime = arrTemp[1].split(":");
				hour = parseInt(arrTime[0], 10);
				minutes = parseInt(arrTime[1], 10);
				seconds = parseInt(arrTime[2], 10);
			}
			
			date = new Date(year, month - 1, day, hour, minutes, seconds);
		}
		return date;
	};
	
	window.DATE_UTIL.formatDate = function(date, splitDate){
		var result = "";
		if(date){
			var splitDate = splitDate || "-";
				arr = [_padLeftZero(date.getFullYear()), splitDate, _padLeftZero(date.getMonth() + 1), splitDate, _padLeftZero(date.getDate())];
			result = arr.join("");  
		}
		return result;
	};

	window.DATE_UTIL.formatDateTime = function(date, splitDate, spliteTime){
		var result = "";
		if(date){
			var splitDate = splitDate || "-",
		    	spliteTime = spliteTime || ":",
				arr = [_padLeftZero(date.getFullYear()), splitDate, _padLeftZero(date.getMonth() + 1), splitDate, _padLeftZero(date.getDate()), " ", _padLeftZero(date.getHours()), spliteTime, _padLeftZero(date.getMinutes()), spliteTime, _padLeftZero(date.getSeconds())];
			result = arr.join("");  
		}
		return result;
	};
	
	function _padLeftZero(i) {
		if (i <	10) {
			i = "0" +i;
      	}
      	return i;
    };
})(window);