var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)) {
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if (isConfirm) {
					var param = {};
					param.value = $("input[name=\"value\"]:checked").val();
					$.post($("#mainForm").attr("action"), param,
							function(data) {
								var success = data.success;
								if (success) {
									Page.confirm("操作成功", data.info, "info", {
										closeOnConfirm : false,
										showCancelButton : false
									}, function() {
										window.location.reload();
									});
								} else {
									Page.confirm("操作失败", data.info, "info", {
										closeOnConfirm : false,
										showCancelButton : false
									}, function() {
										window.location.reload();
									});
								}
							});
				}
			})
		}
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
