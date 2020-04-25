var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	RegistClkAct : function() {
//		jConfirm('确定保存当前权限的信息吗？', '操作确认', function(r) {
//			r && Page.validate.check(false) && Page.submit();
//		});
		Page.submit();
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
		// 确认按键单击事件绑定
		$("#registBtn").click(Events.RegistClkAct);
	},
	// 画面初始化
	initialize : function() {
		// 初始化表单验证
//		Page.validate = Page.form.Validform({
//			tiptype: 3
//		});
	}
}),

Page.initialize();
