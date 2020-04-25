var Action = {
	// 查询的Action
	loadCouponConfigAction : "loadCouponConfigAction",
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
	},
	
	couponSelectChangeAct : function() {
		var param = {};
		param.couponCode = $("#couponCode option:selected").val();
		console.info(JSON.stringify(param));
		$.ajax({
			url : Action.loadCouponConfigAction,
			type : "POST",
			async : true,
			data : "couponCode=" + param.couponCode,
			success : function(result) {
				result = eval('(' + result + ')');
				if(result.couponType == 1){
					$("#couponQuota").val(result.couponQuota + "元");
				}else {
					$("#couponQuota").val(result.couponQuota + "%");
				}
			},
			error : function(err) {
				Page.alert("","数据取得失败!");
			}
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
		$(".fn-Cancel").click(Events.cancelClkAct);
		$("#couponCode").change(Events.couponSelectChangeAct);
		// 图片上传
		$('#fileupload1').fileupload({
			url : "uploadFile",
			autoUpload : true,
			done : function(e, data) {
				var file = data.result[0];
				$("#appLogo").val(file.imagePath);
			}
		});
		// 图片上传
		$('#fileupload2').fileupload({
			url : "uploadFile",
			autoUpload : true,
			done : function(e, data) {
				var file = data.result[0];
				$("#logo").val(file.imagePath);
			}
		});
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


