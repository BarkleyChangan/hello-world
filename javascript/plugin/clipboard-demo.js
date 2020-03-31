HTML:
<div class="weui-msg__title">
	邀请码:<b id="bCode">1234中华人民共和国abcd</b>
	<button id="btnCopy" style="position: relative;top:-5px;" class="weui-btn weui-btn_mini weui-btn_primary" data-clipboard-action="copy" data-clipboard-target="#bCode">复制</button>
</div>

JAVASCRIPT:
;(function(window, $, undefined){
    if(ClipboardJS.isSupported()){
        var clipboard = new ClipboardJS("#btnCopy");
        // 显示用户反馈/捕获复制/剪切操作后选择的内容
        clipboard.on("success", function (e) {
               console.info("Action:", e.action)//触发的动作/如：copy,cut等
               console.info("Text:", e.text);//触发的文本
               console.info("Trigger:", e.trigger);//触发的DOm元素
               e.clearSelection();//清除选中样式（蓝色）
        });
        clipboard.on("error", function (e) {
            console.error("Action:", e.action);
            console.error("Trigger:", e.trigger);
        });
    }
})(window, jQuery);