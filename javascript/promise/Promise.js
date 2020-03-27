/**
 * 自定义Promise函数模块
 * @author Barkley.Chang
 * @date 2020-03-21 21:24
 */
; (function (window, undefined) {
    const PENDING = "pending";
    const RESOLVED = "resolved";
    const REJECTED = "rejected";

    function Promise(executor) {
        const self = this;
        this.status = PENDING;
        this.data = undefined;
        this.callbacks = [];

        function resolve(value) {
            if (self.status !== PENDING) {
                return;
            }

            self.status = RESOLVED;
            self.data = value;
            if (self.callbacks.length > 0) {
                self.callbacks.forEach(callbacksObj => {
                    setTimeout(() => {
                        callbacksObj.onResolved(value);
                    });
                });
            }
        }

        function reject(reason) {
            if (self.status !== PENDING) {
                return;
            }

            self.status = REJECTED;
            self.data = reason;
            if (self.callbacks.length > 0) {
                self.callbacks.forEach(callbacksObj => {
                    setTimeout(() => {
                        callbacksObj.onRejected(reason);
                    });
                });
            }
        }

        try {
            executor(resolve, reject);
        } catch (error) {
            reject(error);
        }
    }

    Promise.resolve = function (value) {
        return new Promise((resolve, reject) => {
            if (value instanceof Promise) {
                value.then(resolve, reject);
            } else {
                resolve(value);
            }
        });
    };

    Promise.reject = function (reason) {
        return new Promise((resolve, reject) => {
            reject(reason);
        });
    };

    Promise.all = function (promises) {
        // 用来保存所有成功value数组
        const values = new Array(promises.length);
        // 用来保存成功promise的数量
        let resolvedCount = 0;
        // 返回一个新的Promise
        return new Promise((resolve, reject) => {
            // 遍历promises获取每个的结果
            promises.forEach((p, index) => {
                Promise.resolve(p).then(value => {
                    resolvedCount++;
                    values[index] = value;

                    // 如果全部成功了,将return的Promise改为成功
                    if (resolvedCount === promises.length) {
                        resolve(values);
                    }
                }, reason => {
                    // 只要有一个失败了,return的Promise就失败
                    reject(reason);
                });
            });
        });
    };

    Promise.race = function (promises) {
        return new Promise((resolve, reject) => {
            promises.forEach((p, index) => {
                Promise.resolve(p).then(value => {
                    // 一旦有成功的将return的Promise变为成功
                    resolve(value);
                }, reason => {
                    // 一旦有失败的将return的Promise变为失败
                    reject(reason);
                });
            });
        });
    };

    Promise.resolveDelay = function (value, time) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                if (value instanceof Promise) {
                    value.then(resolve, reject);
                } else {
                    resolve(value);
                }
            }, time);
        });
    };

    Promise.rejectDelay = function (reason, time) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                reject(reason);
            }, time);
        });
    };

    Promise.prototype.then = function (onResolved, onRejected) {
        onResolved = typeof onResolved === "function" ? onResolved : value => value;
        onRejected = typeof onRejected === "function" ? onRejected : reason => { throw reason };
        const self = this;

        return new Promise((resolve, reject) => {
            function handle(callback) {
                try {
                    const result = callback(self.data);
                    if (result instanceof Promise) {
                        result.then(resolve, reject);
                    } else {
                        resolve(result);
                    }
                } catch (error) {
                    reject(error);
                }
            }

            if (self.status === PENDING) {
                self.callbacks.push({
                    onResolved(value) {
                        handle(onResolved);
                    },
                    onRejected(reason) {
                        handle(onRejected);
                    }
                });
            } else if (self.status === RESOLVED) {
                setTimeout(() => {
                    handle(onResolved);
                });
            } else {
                setTimeout(() => {
                    handle(onRejected);
                });
            }
        });
    };

    Promise.prototype.catch = function (onRejected) {
        return this.then(undefined, onRejected);
    };

    window.Promise = Promise;
})(window);