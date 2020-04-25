var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	//  复制链接点击单击事件绑定
	copyClkAct : function() {
		Page.clip = new ZeroClipboard($(".fn-Copy"));
		Page.clip.on("copy", function(e){
		    e.clipboardData.setData("text/plain", $(e.target).data("url"));
		    Page.notice("复制成功");
		});
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 复制链接点击
		$(".fn-Copy").click(Events.copyClkAct());
	}
});
