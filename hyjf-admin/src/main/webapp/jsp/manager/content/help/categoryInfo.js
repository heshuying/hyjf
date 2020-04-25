var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)) {
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
	// 初始化画面事件处理
	initialize : function() {
		// 鎵ц鎴愬姛鍚庡埛鏂扮敾闈�
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 鍒濆鍖栬〃鍗曢獙璇�
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
	}
}),

Page.initialize();
