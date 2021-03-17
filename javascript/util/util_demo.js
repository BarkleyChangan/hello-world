function call(fn, obj, ...args) {
    // 判断
    if (typeof obj === 'undefined' || obj === null) {
        obj = globalThis;   // 全局对象
    }
    // 为obj添加临时的方法
    obj.temp = fn;
    // 调用temp方法
    let result = obj.temp(...args);
    // 删除temp方法
    delete obj.temp
    // 返回执行结果
    return result;
}

function apply(fn, obj, args) {
    // 判断
    if (typeof obj === 'undefined' || obj === null) {
        obj = globalThis;
    }
    // 为obj添加临时方法
    obj.temp = fn;
    // 执行方法
    let result = obj.temp(...args);
    // 删除临时属性
    delete obj.temp;
    // 返回执行结果
    return result;
}

function bind(fn, obj, ...args) {
    // 返回一个新的函数
    return function (...args2) {
        // 执行call函数
        return call(fn, obj, ...args, ...args2);
    };
}

// 函数节流
function throttle(callback, wait) {
    // 定义开始时间
    let start = Date.now();
    // 返回结果是一个函数
    return function (e) {
        // 获取当前时间戳
        let now = Date.now();
        // 判断
        console.log(now - start)
        if (now - start >= wait) {
            // 若满足条件,则执行回调函数
            callback.call(this, e);
            // 修改开始时间
            start = now;
        }
    };
}

// 函数去抖
function debounce(callback, time) {
    // 定时器变量
    let timerId = 0;
    // 返回一个函数
    return function (e) {
        if (timerId > 0) {
            clearTimeout(timerId);
        }

        timerId = window.setTimeout(() => {
            // 执行回调
            callback.call(this, e);
            // 重置定时器timerId
            timerId = 0;
        }, time);
    };
}

function map(arr, callback) {
    // 声明一个空的数组
    let result = [];
    // 遍历数组
    for (let i = 0, len = arr.length; i < len; i++) {
        result.push(callback(arr[i], i));
    }
    return result;
}

function reduce(arr, callback, initValue) {
    // 声明变量
    let result = initValue;
    // 执行回调
    for (let i = 0, len = arr.length; i < len; i++) {
        result = callback(result, arr[i]);
    }
    // 返回最终的结果
    return result;
}

function filter(arr, callback) {
    // 声明空数组
    let result = [];
    // 遍历数组
    for (let i = 0, len = arr.length; i < len; i++) {
        let res = callback(arr[i], i);
        // 判断 如果为真则压入到result结果
        if (res) {
            result.push(arr[i]);
        }
    }
    // 返回结果
    return result;
}

function find(arr, callback) {
    // 遍历数组
    for (let i = 0, len = arr.length; i < len; i++) {
        // 执行回调
        if (callback(arr[i], i)) {
            // 返回当前正在遍历的元素
            return arr[i];
        }
    }
    // 如果没有找到满足条件的 返回undefined
    return undefined;
}

function findIndex(arr, callback) {
    // 遍历数组
    for (let i = 0, len = arr.length; i < len; i++) {
        // 执行回调
        if (callback(arr[i], i)) {
            // 返回当前正在遍历的元素
            return i;
        }
    }
    // 如果没有找到满足条件的 返回-1
    return -1;
}

function every(arr, callback) {
    // 遍历数组
    for (let i = 0, len = arr.length; i < len; i++) {
        // 执行回调
        if (!callback(arr[i], i)) {
            return false;
        }
    }
    // 如果都满足条件则返回true
    return true;
}

function some(arr, callback) {
    // 遍历数组
    for (let i = 0, len = arr.length; i < len; i++) {
        // 执行回调
        if (callback(arr[i], i)) {
            return true;
        }
    }
    return false;
}

function unique(arr) {
    // 声明一个空数组
    const result = [];
    // 遍历原始数组
    for (let i = 0, len = arr.length; i < len; i++) {
        // 检测result数组中是否包含这个元素
        if (result.indexOf(arr[i]) === -1) {
            result.push(arr[i]);
        }
    }
    arr.forEach(item => {

    });
    return result;
}

function unique2(arr) {
    const result = [];
    const obj = {};
    for (let i = 0, len = arr.length; i < len; i++) {
        if (typeof obj[arr[i]] === "undefined") {
            // 将item做为下标存储在obj中
            obj[arr[i]] = null;
            result.push(arr[i]);
        }
    }
    return result;
}

function unique3(arr) {
    return [...new Set(arr)];
}

function concat(arr, ...args) {
    // 声明一个空数组
    const result = [...arr];
    // 遍历数组
    for (let i = 0, len = args.length; i < len; i++) {
        // 判断是否为数组
        if (Array.isArray(args[i])) {
            result.push(...args[i]);
        } else {
            result.push(args[i]);
        }
    }
    return result;
}

function slice(arr, begin, end) {
    begin = begin || 0;

    if (begin > arr.length) {
        return [];
    }

    end = end || arr.length;
    if (end < begin || end > arr.length) {
        end = arr.length;
    }

    const result = [];
    for (let i = 0, len = arr.length; i < len; i++) {
        if (i >= begin && i < end) {
            result.push(arr[i]);
        }
    }
    return result
}

function flatten1(arr) {
    let result = [];
    for (let i = 0, len = arr.length; i < len; i++) {
        if (Array.isArray(arr[i])) {
            result = result.concat(flatten1(arr[i]));
        } else {
            result = result.concat(arr[i]);
        }
    }
    return result;
}

function flatten2(arr) {
    let result = [...arr];
    while (result.some(p => Array.isArray(p))) {
        result = [].concat(...result);
    }
    return result;
}

function chunk(arr, size = 1) {
    if (arr.length === 0) {
        return [];
    }
    // 声明两个变量
    let result = [];
    let tmp = [];
    for (let i = 0, len = arr.length; i < len; i++) {
        if (tmp.length === 0) {
            // 将tmp压入到result中
            result.push(tmp);
        }
        tmp.push(arr[i]);
        if (tmp.length == size) {
            tmp = [];
        }
    }
    return result;
}

// 数组差集
function difference(arr1, arr2 = []) {
    if (arr1.length === 0) {
        return [];
    }
    if (arr2.length === 0) {
        // 返回一个新数组
        return arr1.slice();
    }
    const result = arr1.filter(p => !arr2.includes(p));
    return result;
}

function pull(arr, ...args) {
    const result = [];
    for (let i = 0, len = arr.length; i < len; i++) {
        if (args.includes(arr[i])) {
            result.push(arr[i]);
            arr.splice(i, 1);
            i--;
        }
    }
    return result;
}

function pullAll(arr, values) {
    return pull(arr, ...values);
}

function drop(arr, size) {
    return arr.filter((value, index) => {
        return index >= size;
    });
}

function dropRight(arr, size) {
    return arr.filter((value, index) => {
        return index < (arr.length - size);
    });
}

function newInstance(fn, ...args) {
    const obj = {};
    const result = fn.call(obj, ...args);
    // 修改新对象的原型对象
    obj.__proto__ = fn.prototype;
    return result instanceof Object ? result : obj;
}

function myInstanceOf1(obj, fn) {
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
}

function myInstanceOf2(obj, fn) {
    // 获取函数的显式原型
    let prototype = fn.prototype;
    // 获取obj的隐士原型对象
    let proto = obj.__proto__;
    // 遍历原型链
    while (proto) {
        // 检测原型对象是否相等
        if (prototype === proto) {
            return true;
        }
        proto = proto.__proto__;
    }
    return false;
}

function mergeObject(...args) {
    // 声明一个空对象
    const result = {};
    // 遍历所有的参数对象
    args.forEach(p => {
        // 获取当前对象所有的属性
        Object.keys(p).forEach(key => {
            // 检测result中是否存在key属性
            if (result.hasOwnProperty(key)) {
                result[key] = [].concat(result[key], p[key]);
            } else {
                // 如果没有则直接写入
                result[key] = p[key];
            }
        });
    });
    return result;
}

/**
 * 浅拷贝
 * @param {*} target 
 */
function clone1(target) {
    // 类型判断
    if (typeof target === "object" && target !== null) {
        // 判断是否为数组
        if (Array.isArray(target)) {
            return [...target];
        } else {
            return { ...target };
        }
    } else {
        return target;
    }
}

/**
 * ES5浅拷贝
 * @param {*} target 
 */
function clone2(target) {
    if (typeof target === "object" && target !== null) {
        const result = Array.isArray(target) ? [] : {};
        // 遍历target数据
        for (let key in target) {
            // 判断当前对象的身上是否包含该属性
            if (target.hasOwnProperty(key)) {
                // 将属性设置到result结果数据中
                result[key] = target[key];
            }
        }

        return result;
    } else {
        return target;
    }
}

/**
 * JSON序列化深拷贝
 * 缺点:1不能克隆方法2循环引用
 * @param {*} target 
 */
function deepClone1(target) {
    let str = JSON.stringify(target);
    return JSON.parse(str);
}

/**
 * 递归深拷贝
 * @param {*} target 
 */
function deepClone2(target) {
    // 检测数据类型
    if (typeof target === "object" && target !== null) {
        // 创建一个容器
        const result = Array.isArray(target) ? [] : {};
        // 遍历对象
        for (let key in target) {
            // 检测该属性是否为对象本身的属性
            if (target.hasOwnProperty(key)) {
                // 拷贝
                result[key] = deepClone2(target[key]);
            }
        }
        return result;
    } else {
        return target;
    }
}

/**
 * 递归深拷贝
 * @param {*} target 
 */
function deepClone3(target, map = new Map()) {
    // 检测数据类型
    if (typeof target === "object" && target !== null) {
        // 克隆数据之前判断数据之前是否克隆过
        let cache = map.get(target);
        if (cache) {
            return cache;
        }
        // 创建一个容器
        const result = Array.isArray(target) ? [] : {};
        // 将新的结果存入到容器中
        map.set(target, result);
        // 遍历对象
        for (let key in target) {
            // 检测该属性是否为对象本身的属性
            if (target.hasOwnProperty(key)) {
                // 拷贝
                result[key] = deepClone3(target[key], map);
            }
        }
        return result;
    } else {
        return target;
    }
}

/**
 * deepClone3优化版本
 * @param {*} target 
 */
function deepClone4(target, map = new Map()) {
    // 检测数据类型
    if (typeof target === "object" && target !== null) {
        // 克隆数据之前判断数据之前是否克隆过
        let cache = map.get(target);
        if (cache) {
            return cache;
        }
        // 创建一个容器
        let isArray = Array.isArray(target);
        const result = isArray ? [] : {};
        // 将新的结果存入到容器中
        map.set(target, result);
        // 遍历对象
        if (isArray) {
            target.forEach((item, index) => {
                result[index] = deepClone4(item, map);
            });
        } else {
            // 如果是对象
            Object.keys(target).forEach(key => {
                result[key] = deepClone4(target[key], map);
            });
        }
        return result;
    } else {
        return target;
    }
}

/**
 * 字符串反转1
 * @param {*} str 
 */
function reverseString1(str) {
    let arr = str.split('');
    return arr.reverse().join('');
}

/**
 * 字符串反转2
 * @param {*} str 
 */
function reverseString2(str) {
    let arr = [...str];
    return arr.reverse().join('');
}

/**
 * 检测字符串是否为回文
 * @param {*} str 
 */
function palindrome(str) {
    return reverseString1(str) === str;
}

/**
 * 字符串截断,并用...补全
 * @param {*} str 
 */
function truncate(str, size, defaultStr) {
    defaultStr = defaultStr || "...";
    return str.slice(0, size) + defaultStr;
}

function addEventListener(el, type, fn, selector) {
    // 判断el类型
    if (typeof el === "string") {
        el = document.querySelector(el);
    }
    // 事件绑定
    if (!selector) {
        el.addEventListener(type, fn);
    } else {
        el.addEventListener(type, function (e) {
            // 获取点击目标的事件源
            const target = e.target;
            // 判断子元素选择器与目标元素是否相符
            if (target.matches(selector)) {
                // 若符合,则调用回调
                fn.call(target, e);
            }
        });
    }
}

// 事件总线对象
const eventBus = {
    // 保存类型与回调的容器
    callbacks: {}
};
// 绑定事件
eventBus.on = function (type, callback) {
    if (this.callbacks[type]) {
        this.callbacks[type].push(callback);
    } else {
        this.callbacks[type] = [callback];
    }
};
// 触发事件
eventBus.emit = function (type, data) {
    if (this.callbacks[type] && this.callbacks[type].length > 0) {
        // 遍历数组
        this.callbacks[type].forEach(p => {
            p(data);
        });
    }
};
// 事件解绑
eventBus.off = function (eventName) {
    // 若传入了eventName事件类型
    if (eventName) {
        delete this.callbacks[eventName];
    } else {
        this.callbacks = {};
    }
}

/**
 * 自定义消息订阅和发布
 */
const PubSub = {
    // 订阅唯一id
    id: 1,
    // 频道与回调保存容器
    callbacks: {
    }
};
PubSub.subscribe = function (channel, callback) {
    // 创建唯一编号
    let token = "token_" + this.id++;
    if (this.callbacks[channel]) {
        this.callbacks[channel][token] = callback;
    } else {
        this.callbacks[channel] = {
            [token]: callback
        }
    }
    return token;
};
PubSub.publish = function (channel, data) {
    if (this.callbacks[channel]) {
        Object.values(this.callbacks[channel]).forEach(p => {
            p(data);
        });
    }
};
PubSub.unsubscribe = function (flag) {
    // 如果flag为undefined 则清空所有订阅
    if (typeof flag === "undefined") {
        this.callbacks = [];
    } else if (typeof flag === "string") {
        // 判断是否为token_开头
        if (flag.indexOf('token_') === 0) {
            // 如果是,表明是一个订阅id
            let callbackObj = Object.values(this.callbacks).find(p => p.hasOwnProperty(flag));
            if (callbackObj) {
                delete callbackObj[flag];
            }
        } else {
            // 表明是一个频道名称
            delete this.callbacks[flag];
        }
    }
}

function axios({ method, url, params, data }) {
    method = method.toUpperCase();
    // 返回值
    return new Promise((resolve, reject) => {
        // 四步骤
        // 1创建对象
        const xhr = new XMLHttpRequest();
        // 2初始化
        // 处理params对象 a=100&b=200
        let str = '';
        for (let k in params) {
            str += `${k}=${params[k]}&`;
        }
        str = str.slice(0, -1);
        xhr.open(method, url + '?' + str);
        // 3发送
        if (method === 'POST' || method === 'PUT' || method === 'DELETE') {
            // Content-type mime类型
            xhr.setRequestHeader('Content-type', 'application/json')
            // 设置请求体
            xhr.send(JSON.stringify(data));
        }
        xhr.responseType = 'json';
        // 4处理结果
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                // 判断响应状态码 2xx
                if (xhr.status >= 200 && xhr.status < 300) {
                    resolve({
                        status: xhr.status,
                        message: xhr.statusText,
                        body: xhr.response
                    });
                } else {
                    reject(new Error('请求失败,失败的状态码为:' + xhr.status));
                }
            }
        }
    });
}

axios.get = function (url, options) {
    return axios(Object.assign(options, {
        method: 'GET',
        url: url
    }));
};

axios.post = function (url, options) {
    return axios(Object.assign(options, {
        method: 'POST',
        url: url
    }));
};

axios.put = function (url, options) {
    return axios(Object.assign(options, {
        method: 'PUT',
        url: url
    }));
};

axios.delete = function (url, options) {
    return axios(Object.assign(options, {
        method: 'DELETE',
        url: url
    }));
};