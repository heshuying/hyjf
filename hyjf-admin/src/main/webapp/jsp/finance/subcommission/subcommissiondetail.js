var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
		// 联动下拉事件
		userNameAction : "userNameAction"
},
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
		parent.Events.refreshClkAct();
	},
	// 转入用户名下拉change事件
	receiveUserListChgAct: function() {
		/*$("#reAccountId").html($("#receiveUserList").find("option:selected").data("receiveaccountid"));
		$("#receiveUserId").val($("#receiveUserList").find("option:selected").val());
		$("#receiveAccountId").val($("#receiveUserList").find("option:selected").data("receiveaccountid"));
		$("#receiveUserName").val($("#receiveUserList").find("option:selected").data("receiveusername"));
		if($("#receiveUserList").find("option:selected").val()==""){
			$("#reAccountId").html(" ")
			$("#receiveUserId").val("");
			$("#receiveAccountId").val("");
			$("#receiveUserName").val("");
		}*/
		$("#receiveUserId").val($("#receiveUserList").find("option:selected").val());
		$("#receiveUserName").val($("#receiveUserList").find("option:selected").data("receiveusername"));
		var userId = $("#receiveUserList").val();
		
		if (userId == "") {
			return;
		}
		$.ajax({
			url : Action.userNameAction,
			type : "POST",
			async : true,
			dataType : "json",
			data :  {
				userId : userId
			},
			success : function(data) {
				var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
				$("#truename").val(data.truename);
				valid.check(false,"#truename");
				$("#receiveAccountId").val(data.account);
				valid.check(false,"#receiveAccountId");
				/*$("#receiveAccountIds").val(data.account);
				valid.check(false,"#receiveAccountIds");
				
				$("#accountRu").val(data.account);
				$("#account").val(data.account);
				valid.check(false,"#account");*/
				
			},
			error : function(err) {
				Page.alert("","该用户信息不存在！");
			}
		});
	},


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
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		// 项目类型change事件
		$("#receiveUserList").change(Events.receiveUserListChgAct);
	},
	// 画面初始化
	initialize : function() {
		Events.receiveUserListChgAct;
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
	}
}),

Page.initialize();
