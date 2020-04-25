var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false) && $(".has-error").length == 0) {
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 选择事件
	changeClkAct : function() {
		if ($(this).val() == "endday") {
			$("#chargeTimeDiv").hide();
			$("#chargeTime").val(""), Page.validate.ignore("#chargeTime");
		} else {
			$("#chargeTimeDiv").show();
			Page.validate.unignore("#chargeTime");
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
		$("#chargeTimeType").change(Events.changeClkAct).change();
	},
	// 画面布局
	doLayout: function() {
		// 初始化下拉框
		$(".form-select2").select2({
			allowClear: true,
			language: "zh-CN"
		})
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
	}
}),

Page.initialize();
