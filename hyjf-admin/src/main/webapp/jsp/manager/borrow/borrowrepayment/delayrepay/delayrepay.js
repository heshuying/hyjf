var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 延期的Action
	delayRepayAction : "delayRepayAction"
},
/* 事件动作处理 */
Events = {
	// 按钮的单击动作事件
	delayRepayClkAct : function(event) {
		if(Page.validate.check(false)) {
			Page.confirm("", "确定要执行本次操作吗？", function(isConfirm) {
				isConfirm && Page.submit(Action.delayRepayAction);
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
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
		// 刷新按钮的单击事件绑定
		$(".fn-Confirm").click(Events.delayRepayClkAct);
		// 未交保证金按钮的单击事件绑定
		$(".fn-Cancel").click(Events.cancelClkAct);
		
		$("#delayDays").change(function() {
			var date = $(this).val() * 24 * 3600 * 1000;
			var repayTime = $("#repayTime").val() * 1000
			repayTime = $.dateFormat(new Date(repayTime + date), "yyyy-MM-dd");
			$("#afterTime").html(repayTime);
		});
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
		
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
		
		if($("#delayDays").val() != "") {
			var date = $("#delayDays").val() * 24 * 3600 * 1000;
			var repayTime = $("#repayTime").val() * 1000
			repayTime = $.dateFormat(new Date(repayTime + date), "yyyy-MM-dd");
			$("#afterTime").html(repayTime);
		}
	}
}),

Page.initialize();
