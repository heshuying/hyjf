var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 发标的Action
	updateAction : "updateAction"
},
/* 事件动作处理 */
Events = {
	// 按钮的单击动作事件
	updateClkAct : function(event) {
		if(Page.validate.check(false) && Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要执行本次操作吗？", function(isConfirm) {
				isConfirm && Page.submit(Action.updateAction);
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
	// 画面布局
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
			allowClear: true,
			language: "zh-CN"
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按钮的单击事件绑定
		$(".fn-Confirm").click(Events.updateClkAct);
		// 未交保证金按钮的单击事件绑定
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
	// 画面初始化
	initialize : function() {

		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3,
		});
	}
}),

Page.initialize();
