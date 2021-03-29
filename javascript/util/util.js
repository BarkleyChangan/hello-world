; (function (window, document, undefined) {
  window.BC_UTIL = window.BC_UTIL || {};

  if (!Object.is) {
    // 部署ES6的Object.is
    Object.defineProperty(Object, "is", {
      value: function (x, y) {
        if (x === y) {
          // 针对+0不等于-0情况
          return x !== 0 || 1 / x === 1 / y;
        }
        // 针对NaN情况
        return x !== x && y !== y;
      },
      configurable: true,
      enumerable: false,
      writable: true
    });
  }

  // 捕捉任何出现的错误,并向全局抛出
  if (!Promise.prototype.done) {
    Promise.prototype.done = function (onFullfilled, onRejected) {
      this.then(onFullfilled, onRejected)
        .catch(function (reason) {
          // 抛出一个全局错误
          setTimeout(() => {
            throw reason
          }, 0);
        });
    };
  }

  if (!Promise.prototype.finally) {
    Promise.prototype.finally = function (callback) {
      let P = this.constructor;
      return this.then(value => P.resolve(callback()).then(() => value),
        reason => P.resolve(callback()).then(() => {
          throw reason;
        }));
    };
  }

  // 判断浏览器是否支持flash的方法
  window.BC_UTIL.hasUsableSWF = function () {
    var swf;
    if (typeof window.ActiveXObject != "undefined") {
      swf = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
    } else {
      swf = navigator.plugins['Shockwave Flash'];
    }
    return swf ? true : false;
  };

  window.BC_UTIL.codePointLength = function (text) {
    var result = text.match(/[\s\S]/gu);
    return result ? result.length : 0;
  };

  window.BC_UTIL.length = function (str) {
    return [...str].length;
  };

  window.BC_UTIL.clone = function (origin) {
    let originProto = Object.getPrototypeOf(origin);
    return Object.assign(Object.create(originProto), origin);
  };

  // 函数防抖
  window.BC_UTIL.debounce = function (fn, delay) {
    var timerId = 0;
    return function () {
      clearTimeout(timerId);
      timerId = setTimeout(function () {
        fn.call(this);
      }, delay);
    };
  };

  // 函数节流
  window.BC_UTIL.throttle = function (fn, delay) {
    // 记录上一次函数出发的时间
    var lastTime = 0;
    return function () {
      // 记录当前函数触发的时间
      var nowTime = new Date().getTime();
      // 当当前时间减去上一次执行时间大于这个指定间隔时间才让他触发这个函数
      if ((nowTime - lastTime) > delay) {
        fn.call(this);
        // 同步时间
        lastTime = nowTime;
      }
    };
  };

  // ArrayBuffer转为字符串,参数为ArrayBuffer对象
  window.BC_UTIL.ab2str = function (buffer) {
    return String.fromCharCode.apply(null, new Uint16Array(buffer));
  };

  // 字符串转为ArrayBuffer对象,参数为字符串
  window.BC_UTIL.str2ab = function (str) {
    const buf = new ArrayBuffer(str.length * 2);
    const bufView = new Uint16Array(buf);
    for (let i = 0, strLen = str.length; i < strLen; i++) {
      bufView[i] = str.charCodeAt(i);
    }

    return buf;
  };

  // 解析URL
  window.BC_UTIL.urlParse = function (url, key) {
    var a = document.createElement('a')
    a.href = url
    var result = {
      href: url,
      protocol: a.protocol.replace(':', ''),
      port: a.port,
      query: a.search,
      params: (function () {
        var ret = {}, centArr,
          seg = a.search.replace(/^\?/, '').replace(/^\?/, '').split('&')
        for (i = 0, len = seg.length; i < len; i++) {
          if (!seg[i]) { continue }
          centArr = seg[i].split('=')
          ret[centArr[0]] = centArr[1]
        }
        return ret
      }()),
      hash: a.hash,
      file: (a.pathname.match(/\/([^\/?#]+)$/i) || [, ''])[1],
      path: a.pathname.replace(/^([^\/])/, '/$1'),
      relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [, ''])[1],
      segments: a.pathname.replace(/^\//, '').split('/')
    }
    a = null
    return key ? result[key] : result
  };

  // 生成指定长度的随机字符串
  window.BC_UTIL.generateRandomAlpha = function (len) {
    var rdmString = "";
    for (; rdmString.length < len; rdmString += Math.random().toString(36).substr(2));
    return rdmString.substr(0, len);
  };

  // 是否是数组
  window.BC_UTIL.isArray = function (value) {
    // 首先判断浏览器是否支持Array.isArray这个方法
    if (typeof Array.isArray === "function") {
      return Array.isArray(value);
    } else {
      return Object.prototype.toString.call(value) === "[object Array]";
      // return obj.__proto__ === Array.prototype;
    }
  };

  // 判断是否为4个字节组成字符
  window.BC_UTIL.is32Bit = function (c) {
    return c.codePointAt(0) > 0xFFFF;
  };

  // 验证手机号
  window.BC_UTIL.isMobile = function (mobile) {
    var result = false;
    if (mobile && mobile.length === 11) {
      var e = /^((1[3-9]{1})+\d{9})$/;
      result = e.test(mobile);
    }
    return result;
  };

  // 验证身份证号
  window.BC_UTIL.isIDCard = function (idcard) {
    if (idcard === null || (idcard.length != 15 && idcard.length != 18)) {
      return false;
    }

    var cs = idcard.toUpperCase().split("");

    function getIdcardCalendar(idcard) {
      // 获取出生年月日
      //var birthday = idcard.substring(6, 12);
      return "19" + idcard.substring(6, 8);
    }

    // 校验位数
    var power = 0;
    var POWER_LIST = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
    for (var i = 0; i < cs.length; i++) {
      if (i == cs.length - 1 && cs[i] == 'X') {
        break; //最后一位可以是X或x
      }

      if (cs[i] < '0' || cs[i] > '9') {
        return false;
      }

      if (i < cs.length - 1) {
        power += (cs[i] - '0') * POWER_LIST[i];
      }
    }

    var ZONE_NUM = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" };
    //校验区位码
    if (!ZONE_NUM[idcard.substring(0, 2)]) {
      return false;
    }

    //校验年份
    var year = idcard.length == 15 ? getIdcardCalendar(idcard) : idcard.substring(6, 10);
    var iyear = window.parseInt(year, 10);
    if (iyear < 1900 || iyear > new Date().getFullYear()) {
      return false; //1900年的PASS，超过今年的PASS
    }

    //校验月份
    var month = idcard.length == 15 ? idcard.substring(8, 10) : idcard.substring(10, 12);
    var imonth = window.parseInt(month, 10);
    if (imonth < 1 || imonth > 12) {
      return false;
    }

    //校验天数
    var day = idcard.length == 15 ? idcard.substring(10, 12) : idcard.substring(12, 14);
    var iday = window.parseInt(day, 10);
    if (iday < 1 || iday > 31) {
      return false;
    }

    //校验"校验码"
    if (idcard.length == 15) {
      return true;
    }

    var PARITYBIT = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
    return cs[cs.length - 1] == PARITYBIT[power % 11];
  };

  // 格式化数值
  window.BC_UTIL.formatNum = function (num, fmt) {
    if (num === '' || window.isNaN(num)) num = 0;
    if (!fmt) fmt = 'F:0.2'; //13.00
    if (fmt.indexOf('F:') === 0) fmt = fmt.substr(2);
    var f = fmt.split('.');
    var prefix = '';
    if (f[0] > 0) {
      prefix = new Array(length - parseInt(num) + ''.length + 1).join('0');
    }
    return prefix + parseFloat(num).toFixed(parseInt(f[1]));
  };

  /**
  // 尾递归优化(ES6尾调用优化只在严格模式下开启,正常模式下无效)
  function tco(f) {
      var value;
      var active = false;
      var accumulated = [];

      return function accumulator() {
          accumulated.push(arguments);
          if (!active) {
              active = true;
              while (accumulated.length) {
                  value = f.apply(this, accumulated.shift());
              }
              active = false;
              return value;
          }
      };
  }
  var sum = tco(function (x, y) {
      if (y > 0) {
          return sum(x + 1, y - 1);
      } else {
          return x;
      }
  })
  log(sum(1, 100000));
  */

  /**
     * 通过Symbol.hasInstance属性可以影响instanceof的判断结果,使用自定义的myInstanceof方法不会受到Symbol.hasInstance属性的影响
     * obj 变量
     * fn 构造函数
     */
  window.BC_UTIL.myInstanceOf = function (obj, fn) {
    let _prototype = Object.getPrototypeOf(obj);
    if (_prototype === null) {
      return false;
    }
    let _constructor = _prototype.constructor;
    if (_constructor[Symbol.hasInstance]) {
      return _constructor[Symbol.hasInstance](obj);
    }
    if (_constructor === fn) {
      return true;
    }
    return myInstanceOf(_prototype, fn);
  };

  // 获取数据类型
  window.BC_UTIL.getType = function (object) {
    return Object.prototype.toString.call(object).match(/^\[object\s(.*)\]$/)[1];
  };

  // ES5使用:isNaN('abc')=>true判断不准确
  // ES6使用:Number.isNaN('abc')=>false
  window.BC_UTIL.isNaN = function (param) {
    return param !== param;
  };
})(window, document);