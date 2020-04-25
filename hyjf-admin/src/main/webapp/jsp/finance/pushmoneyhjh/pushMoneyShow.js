var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出协议的Action
	exportAgreementAction : "exportAgreementAction",
	// 查询的Action
	searchAction : "pushMoneyList",
	//执行后台发提成操作
	confirmPushMoneyAction : "confirmPushMoneyAction",
	//校验发提成状态是不是已经发放
	checkStatusAction:"checkStatusAction",
},
/* 事件动作处理 */
Events = {
		// 发提成按钮的单击事件
		confirmPushMoneyClkAct : function(event) {
			if (event) {
				Page.primaryKey.val($(this).data("id"));
			}
			var param = {};
			param.ids = $("#ids").val();
			$.ajax({
				url : Action.checkStatusAction,
				type : "POST",
				async : true,
				data : JSON.stringify(param),
				dataType : "json",
				contentType : "application/json",
				success : function(data) {
					$.ajax({
						url : Action.confirmPushMoneyAction,
						type : "POST",
						async : true,
						data : JSON.stringify(param),
						dataType : "json",
						contentType : "application/json",
						success : function(data) {
							Page.coverLayer();
							if (data.status == "success") {
								Page.confirm("", data.result, "success", {
									showCancelButton : false
								}, function() {
									parent.Events.refreshClkAct();
								});
							} else {
								Page.notice(data.result, "", "error");
							}
						},
						error : function(err) {
							Page.coverLayer();
							Page.notice("发提成出现错误,请重新操作!", "", "error");
						}
					});
				},
				error : function(err) {
					Page.coverLayer();
					Page.notice("提成已发放，请不要重复操作!", "", "error");
				}
			});
		},

		// 刷新按钮的单击动作事件
		refreshClkAct : function() {
			$("#mainForm").attr("target", "");
			Page.submit(Action.searchAction);
		},

	// 重发按钮的单击动作事件
	exportAgreementClkAct : function(event) {
		var param = {};
		param.userid = $("#userid").val();
		param.planOrderId = $("#planOrderId").val();
		param.debtPlanNid = $("#debtPlanNid").val();
		param.email = $("#email").val();
		Page.coverLayer("正在操作数据，请稍候...");
		// alert("updateClkAct");
		$.ajax({
			url : Action.exportAgreementAction,
			type : "post",
			data : {
				userid : param.userid,
				planOrderId : param.planOrderId,
				debtPlanNid : param.debtPlanNid,
				email : param.email
			},
			dataType : "json",
			success : function(data) {
				Page.coverLayer();
				if (data.status == "success") {
					Page.confirm("发送成功", data.result, "success", {
						showCancelButton : false
					}, function() {
						parent.$.colorbox.close();
					});
				} else {
					Page.confirm(data.JSON_RESULT_KEY, data.result, "error", {
						showCancelButton : false
					}, function() {
						// parent.$.colorbox.close();
					});
				}
			},
			error : function(err) {
				Page.notice("系统异常,请重新操作!", "", "error");
				// parent.$.colorbox.close();
			}
		});
	},
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 初始化画面事件处理
	initEvents : function() {
		// 发提成按钮的单击事件
		$(".fn-Confirm").click(Events.confirmPushMoneyClkAct),
		// 导出Excel的单击事件绑定
		$(".fn-ExportAgreement").click(Events.exportAgreementClkAct);
	}
});
