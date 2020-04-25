var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Action = {
	// 查询的Action
	loadCouponConfigAction : "loadCouponConfigAction",
	uploadAction : "uploadAction" 
},
	
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定上传这个文件？", function(isConfirm) {
				if(isConfirm){
				    //Page工具不稳定性，需要等待短暂时间
					setTimeout("Events.submitFunction()", 100);
				}
			})
		}
	},
	
	submitFunction : function() {
		var param = {};
		param.couponCode = $("#couponCode option:selected").val();
		var amount=$("#amount").val();
		$.ajax({
			url : Action.uploadAction,
			type : "POST",
			async : true,
			success : function(result) {
				if(result == "success"){
					Page.submit();
				}else{
					//alert(result);
					Page.alert("", result);
				}
			},
			error : function(err) {
				Page.alert("","接口调用失败!");
			}
		});
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		$("#userRole").select2({
			placeholder: "请选择用户角色"
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct),
		$(".fn-Cancel").click(Events.cancelClkAct);
		// 图片上传
		$('.fileupload').fileupload({
			url : "uploadAction",
			autoUpload : true,
			done : function(e, data) {
				var flag = data.result.flag;
				$("#reslt").val(flag);
			}
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
	}
}),

Page.initialize();
