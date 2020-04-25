var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 提交按钮
	insertAction : "insertAction",
	getAccountIdAction : "getAccountIdAction"
},
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定执行当前的处理吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit();
				}
			})
		}
	},
	userNameChgAct : function() {
		var userName = $("#userName").val();
		if (userName == "") {
			$("#accountId").attr("readOnly",false); 
			return;
		}
		$.ajax({
			url : Action.getAccountIdAction,
			type : "POST",
			async : true,
			data :  {
				userName : userName
			},
			success : function(result) {
				$("#accountId").val(result);
				if("" == result || null == result) {
					$("#accountId").attr("readOnly",false);
				} else {
					$("#accountId").attr("readOnly",true);
				}
			},
		});
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		// 用户名修改事件绑定
		$("#userName").change(Events.userNameChgAct);
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
