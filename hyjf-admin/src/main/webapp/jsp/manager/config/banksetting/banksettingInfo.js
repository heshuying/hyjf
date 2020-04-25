var Action = {
	validateBefore : "validateBeforeAction"
};
var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)) {
			$.post(Action.validateBefore, $("#mainForm").serialize(), function(
					data) {
				if (data.success) {
					Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
						if (isConfirm) {
							Page.submit();
						}
					});
				} else {
					Page.alert("", data.msg);
				}
			});
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},quickPayment : function() {
		if(this.value==1){
			$('#kuaijie').show();
		}else if (this.value==0){
			$('#kuaijie').hide();
			
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
		$("input[name='quickPayment']").click(Events.quickPayment);
		// 图片上传
		$('#fileupload1').fileupload({
			url : "uploadFile",
			autoUpload : true,
			done : function(e, data) {
				var file = data.result[0];
				$("#bankIcon").val(file.imagePath);
			}
		});
		// 图片上传
		$('#fileupload2').fileupload({
			url : "uploadFile",
			autoUpload : true,
			done : function(e, data) {
				var file = data.result[0];
				$("#bankLogo").val(file.imagePath);
			}
		});
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		Events.quickPayment();
		($("#success").val() && parent.Events.refreshClkAct())
				|| Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
		
		if(quickSupport == 1){
			$('#kuaijie').show();
		}else{
			$('#kuaijie').hide();
		}
		
		
	}
}),

Page.initialize();
