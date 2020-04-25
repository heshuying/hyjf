var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 发标的Action
	arraignmentOkAction : "arraignmentOkAction"
},
/* 事件动作处理 */
Events = {
	// 按钮的单击动作事件
	arraignmentClkAct : function(event) {
		Page.confirm("", "确定要执行本次操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.arraignmentOkAction);
		})
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
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
		// 刷新按钮的单击事件绑定
		$(".fn-Confirm").click(Events.arraignmentClkAct);
		// 未交保证金按钮的单击事件绑定
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
	}
}),

Page.initialize();
