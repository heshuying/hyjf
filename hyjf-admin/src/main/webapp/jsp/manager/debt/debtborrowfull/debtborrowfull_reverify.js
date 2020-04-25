var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 复审的Action
	fullAction : "fullAction",
	backAction : "searchAction"
},
/* 事件动作处理 */
Events = {
	// 复审按钮的单击动作事件
	fullClkAct : function(selection) {
		if(Page.validate.check(false) && Page.form.find(".has-error").length == 0) {
			Page.confirm("确认放款", "操作不可逆转，是否确定放款并收取服务费？", function(isConfirm) {
				isConfirm && Page.submit(Action.fullAction);
			})
		}
	},
	// 返回列表按钮的单击动作事件
	backClkAct : function(selection) {
		Page.validate.submitForm(true, Action.backAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 复审 提交按钮的单击事件绑定
		$(".fn-Full").click(Events.fullClkAct),
		// 返回列表 提交按钮的单击事件绑定
		$(".fn-Back").click(Events.backClkAct)
	},
	// 画面初始化
	initialize : function() {
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3,
		});
	}
}),

Page.initialize();