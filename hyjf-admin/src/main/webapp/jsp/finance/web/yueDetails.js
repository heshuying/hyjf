var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 刷新按键单击事件绑定
	confirmClkAct : function() {
		Page.submit();
	},
	// 关闭按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct),
		// 关闭
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
	// 画面初始化
	initialize : function() {
		
	}
}),

Page.initialize();
