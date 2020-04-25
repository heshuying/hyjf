var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)) {
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	// 删除图片
	redClkAct : function() {
		$("#codeUrl").val('');
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
		$(".btn-red").click(Events.redClkAct);
		
		// 图片上传
		$('.fileupload').fileupload({
			url : "uploadFile",
			autoUpload : true,
			done : function(e, data) {
				var file = data.result[0];
				$(this).parents("div.picClass").find("div.purMargin input").val(file.imagePath);
				/*$("#image").val(file.imagePath);*/
			}
		});
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true,
			dateFormat: "yyyy年MM月dd日"
		});
		// 日历范围限制
		$('#startTime').on(
				"change",
				function(evnet, d) {
					d = $('#endTime').datepicker("getDate"), d
							&& $('#startTime')
									.datepicker("setEndDate", d)
				}), $('#endTime').on(
				"change",
				function(evnet, d) {
					d = $('#startTime').datepicker("getDate"), d
							&& $('#endTime')
									.datepicker("setStartDate", d)
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
