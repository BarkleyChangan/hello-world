;(function(window, document, undefined){
    window.FUN_UTIL = window.FUN_UTIL || {};

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

    // 判断浏览器是否支持flash的方法
    window.FUN_UTIL.hasUsableSWF = function() {
        var swf;
        if(typeof window.ActiveXObject != "undefined"){
            swf = new  ActiveXObject("ShockwaveFlash.ShockwaveFlash");
        }else{
            swf = navigator.plugins['Shockwave Flash'];
        }
        return swf ? true : false;
    };

    window.FUN_UTIL.codePointLength = function(text) {
        var result = text.match(/[\s\S]/gu);
        return result ? result.length : 0;
    };

    window.FUN_UTIL.length = function(str) {
        return [...str].length;
    };

    window.FUN_UTIL.clone = function(origin) {
        let originProto = Object.getPrototypeOf(origin);
        return Object.assign(Object.create(originProto), origin);
    };

    // 函数防抖
    window.FUN_UTIL.debounce = function(fn, delay) {
        var timerId = 0;
        return function () {
            clearTimeout(timerId);
            timerId = setTimeout(function () {
                fn.call(this);
            }, delay);
        };
    };

    // 函数节流
    window.FUN_UTIL.throttle = function(fn, delay) {
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
    window.FUN_UTIL.ab2str = function(buffer) {
        return String.fromCharCode.apply(null, new Uint16Array(buffer));
    };

    // 字符串转为ArrayBuffer对象,参数为字符串
    window.FUN_UTIL.str2ab = function(str) {
        const buf = new ArrayBuffer(str.length * 2);
        const bufView = new Uint16Array(buf);
        for (let i = 0, strLen = str.length; i < strLen; i++) {
            bufView[i] = str.charCodeAt(i);
        }

        return buf;
    };

    // 解析URL
    window.FUN_UTIL.urlParse = function(url, key) {
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
    };

    // 生成指定长度的随机字符串
    window.FUN_UTIL.generateRandomAlpha = function(len) {
        var rdmString = "";
        for (; rdmString.length < len; rdmString += Math.random().toString(36).substr(2));
        return rdmString.substr(0, len);
    };

    // 是否是数组
    window.FUN_UTIL.isArrayFn = function(value) {
        // 首先判断浏览器是否支持Array.isArray这个方法
        if (typeof Array.isArray === "function") {
            return Array.isArray(value);
        }else{
            return Object.prototype.toString.call(value) === "[object Array]";
            // return obj.__proto__ === Array.prototype;
        }
    };

    // 判断是否为4个字节组成字符
    window.FUN_UTIL.is32Bit = function(c) {
        return c.codePointAt(0) > 0xFFFF;
    };

    // 验证手机号
    window.FUN_UTIL.isMobile = function(mobile) {
        var result = false;
        if (mobile && mobile.length === 11) {
            var e = /^((1[3-9]{1})+\d{9})$/;
            result = e.test(mobile);
        }
        return result;
    };

    // 验证身份证号
    window.FUN_UTIL.isIDCard = function(idcard) {
        var area = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" }
        var ereg;
        var Y, JYM;
        var S, M;
        var idcard_array = [];
        idcard_array = idcard.split("");
        //地区检验
        if (area[parseInt(idcard.substr(0, 2))] == null) return false;
        //身份号码位数及格式检验
        switch (idcard.length) {
            //15位身份号码检测
            case 15:
                if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
                    ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
                } else {
                    ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
                }
                if (ereg.test(idcard)) return true;
                else return false;
                break;
            //18位身份号码检测
            case 18:
                if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
                    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
                } else {
                    ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
                }
                if (ereg.test(idcard)) {//测试出生日期的合法性
                    //计算校验位
                    S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
                        + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
                        + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
                        + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
                        + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
                        + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
                        + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
                        + parseInt(idcard_array[7]) * 1
                        + parseInt(idcard_array[8]) * 6
                        + parseInt(idcard_array[9]) * 3;
                    Y = S % 11;
                    M = "F";
                    JYM = "10X98765432";
                    M = JYM.substr(Y, 1);//判断校验位
                    if (M === idcard_array[17]) return true;//检测ID的校验位
                    else return false;
                }
                else return false;
            default:
                return false;
        }
    };

    // 格式化数值
    window.FUN_UTIL.formatNum = function(num, fmt) {
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
})(window, document);