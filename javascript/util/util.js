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

// 判断是否为4个字节组成字符
function is32Bit(c){
    return c.codePointAt(0) > 0xFFFF;
}

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

function codePointLength(text) {
        var result = text.match(/[\s\S]/gu);
        return result ? result.length : 0;
    }

function length(str){
    return [...str].length;
}

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
	
function clone(origin) {
    let originProto = Object.getPrototypeOf(origin);
    return Object.assign(Object.create(originProto), origin);
}

// 捕捉任何出现的错误,并向全局抛出
Promise.prototype.done = function (onFullfilled, onRejected) {
    this.then(onFullfilled, onRejected)
        .catch(function (reason) {
            // 抛出一个全局错误
            setTimeout(() => {
                throw reason
            }, 0);
        });
};
Promise.prototype.finally = function (callback) {
    let P = this.constructor;
    return this.then(value => P.resolve(callback()).then(() => value),
        reason => P.resolve(callback()).then(() => {
            throw reason;
        }));
};

// 函数防抖
function debounce(fn, delay) {
    var timerId = 0;
    return function () {
        clearTimeout(timerId);
        timerId = setTimeout(function () {
            fn.call(this);
        }, delay);
    };
}

// 函数节流
function throttle(fn, delay) {
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
}

// ArrayBuffer转为字符串,参数为ArrayBuffer对象
function ab2str(buffer) {
    return String.fromCharCode.apply(null, new Uint16Array(buffer));
}

// 字符串转为ArrayBuffer对象,参数为字符串
function str2ab(str) {
    const buf = new ArrayBuffer(str.length * 2);
    const bufView = new Uint16Array(buf);
    for (let i = 0, strLen = str.length; i < strLen; i++) {
        bufView[i] = str.charCodeAt(i);
    }

    return buf;
}

// 解析URL
function urlParse(url, key) {
  var a = document.createElement('a')
  a.href = url
  var result = {
    href: url,
    protocol: a.protocol.replace(':', ''),
    port: a.port,
    query: a.search,
    params: (function(){
      var ret = {}, centArr,
        seg = a.search.replace(/^\?/, '').replace(/^\?/,'').split('&')
      for (i = 0, len = seg.length; i < len; i ++) {
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
}

// 生成指定长度的随机字符串
function generateRandomAlpha(len) {
    var rdmString = "";
    for (; rdmString.length < len; rdmString += Math.random().toString(36).substr(2));
    return rdmString.substr(0, len);
}