var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)
				&& Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要发起此笔转账吗？", function(isConfirm) {
				isConfirm && Page.submit();
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	keyupClkAct : function() {
		$.ajax({
			url : "searchBalance",
			type : "POST",
			async : true,
			data : {
				outUserName : $("#outUserName").val()
			},
			dataType : "json",
			success : function(data) {
				$("#balance").html(data.result);
			},
			error : function(error) {
				Page.alert("", "数据取得失败!");
			}
		});
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
		//用户输入用户名后查询余额
		$("#outUserName").keyup(Events.keyupClkAct).change(Events.keyupClkAct);
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
	}
}),

Page.initialize();
