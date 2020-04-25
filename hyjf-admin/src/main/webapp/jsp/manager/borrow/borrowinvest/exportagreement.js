var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出协议的Action
	exportAgreementAction : "exportAgreementAction",
},
/* 事件动作处理 */
Events = {
	// 重发按钮的单击动作事件
	exportAgreementClkAct : function(event) {
		var param = {};
		param.userid = $("#userid").val();
		param.nid = $("#nid").val();
		param.borrownid = $("#borrownid").val();
		param.email = $("#email").val();
		Page.coverLayer("正在操作数据，请稍候...");
		// alert("updateClkAct");
		$.ajax({
			url : Action.exportAgreementAction,
			type : "post",
			data : {
				userid : param.userid,
				nid : param.nid,
				borrownid : param.borrownid,
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
	// 初始化画面事件处理
	initEvents : function() {
		// 导出Excel的单击事件绑定
		$(".fn-ExportAgreement").click(Events.exportAgreementClkAct);
	}
});