var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 新增修改信息
	addOrSaveAction : "addOrSaveAdminStockInfoAction"
};
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout : function() {
		$("#qrcode").each(function() {
			$(this).qrcode({
				text : $("#qrcodeValue").val(),
				render : "canvas",
				width : 128,
				height : 128,
				typeNumber : -1,
				correctLevel : QRErrorCorrectLevel.H,
				background : "#ffffff",
				foreground : "#000000"
			})
		});
	}
});

var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)
				&& Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要保存当前信息吗？", function(isConfirm) {
				if (isConfirm) {
					Page.submit();
				}
			})
		}
	},
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),

	// 初始化画面事件处理
	initEvents : function() {
		// 确认按钮的点击事件
		$(".fn-Confirm").click(Events.confirmClkAct);
		// 取消按钮的点击事件
		$(".fn-Cancel").click(Events.cancelClkAct);
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
});
Page.initialize();
