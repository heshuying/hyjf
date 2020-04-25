var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {},
/* 事件动作处理 */
Events = {
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout : function() {

	},
	// 初始化画面事件处理
	initEvents : function() {
		// 未交保证金按钮的单击事件绑定
		$(".fn-Cancel").click(Events.cancelClkAct);

	},
	ready : function() {

	},
	// 画面初始化
	initialize : function() {

	}
}),

Page.initialize();
