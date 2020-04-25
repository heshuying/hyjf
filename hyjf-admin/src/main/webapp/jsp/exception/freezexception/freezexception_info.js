var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)) {
			Page.confirm("", "确定要对[" + $("#trxId").val() + "]进行解冻吗？", "info", {
				closeOnConfirm : false,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确定",
				cancelButtonText : "取消",
				showCancelButton : true,
			}, function(isConfirm, tid) {
				if (isConfirm) {
					Page.coverLayer("正在操作数据，请稍候...");
					tid = setTimeout(function() {
						swal.close();
					}, 100);
					$.post("freezeAction", { "trxId": $("#trxId").val(), "notes": $("#notes").val() }, function(data) {
						clearTimeout(tid);
						Page.coverLayer();
						if(data.message != "") {
							Page.alert("操作失败", data.message);
						} else {
							Page.confirm("解冻成功", "", "info", {
								closeOnConfirm : true,
								showCancelButton : false
							}, function() {
								parent.$.colorbox.close(); 
								parent.Events.reSearchClkAct();
							});
						}
					});
				}
			});
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
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
	},
	// 画面初始化
	initialize : function() {

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
	}
}),

Page.initialize();
