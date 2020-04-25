var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	//
	updateAction : "openaccountenquiryupdateAction",
}
var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)) {
			Page.confirm("", "确定要同步当前的信息吗？", function(isConfirm) {
				if(isConfirm) {
					$.post(Action.updateAction, $("#mainForm").serialize(),
							function(data) {
//								Page.coverLayer();
								if (data.status == "success") {
								    	Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.cancelClkAct()});
								} else {
										Page.confirm("同步失败",data.result,"error",{showCancelButton: false}, function(){Events.cancelClkAct()});
								}
							});
//					parent.$.colorbox.close();
//					Page.submit();
//					Page.confirm("开户掉单同步成功",{showCancelButton: false}, function(){Events.refreshClkAct()});
				}
			})
		}
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	// 删除图片
	redClkAct : function() {
		$("#iconUrl").val('');
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
		$(".fn-Confirms").click(Events.cancelClkAct);
		
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
