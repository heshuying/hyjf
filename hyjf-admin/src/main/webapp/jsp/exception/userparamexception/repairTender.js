var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)
				&& Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要修复出借数据吗？", function(isConfirm) {
				isConfirm && Page.submit();
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
	doLayout : function() {
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		}),
		// 日历范围限制
		$('#repair-start-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#repair-end-date-time').datepicker("getDate"), d
							&& $('#repair-start-date-timee').datepicker(
									"setEndDate", d)
				}), $('#repair-end-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#repair-start-date-time').datepicker("getDate"), d
							&& $('#repair-end-date-time').datepicker(
									"setStartDate", d)
				});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct())
				|| Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
	}
}),

Page.initialize();
