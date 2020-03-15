var EventUtil = {
        // 添加事件
        addEvent: function(element, eventName, fn){
            if(element.addEventListener){
                element.addEventListener(eventName, fn, false);
            }else if(element.attachEvent){
                element.attachEvent('on' + eventName, fn)
            }else{
                element.eventName = fn;
            }
        },

        // 移除事件
        removeEvent: function(element, eventName, fn){
            if(element.removeEventListener){
                element.removeEventListener(eventName, fn, false);
            }else if (element.deleteEvent){
                element.deleteEvent('on' + eventName, fn);
            }else{
                element.eventName = null;
            }
        },

        // 获取事件对象
        getEvent: function(event){
            return event?event:window.event;
        },

        // 获取事件类型
        getEventType: function(event){
            return event.type;
        },

        // 获取被执行事件的目标元素
        getEventTarget: function(event){
            return event.target | event.srcElement;
        },

        // 禁用元素的默认行为
        preventDefault: function(event){
            if(event.preventDefault){
                event.preventDefault();
            }else{ //IE8以及更低版本
                event.returnValue = false;
            }
        },

        // 阻止元素冒泡
        stopPropagation: function(event){
            if(event.stopPropagation){                    
                event.stopPropagation();
            }else{ //IE8以及更s低版本
                event.cancelable = true;
            }
        }
    }; 