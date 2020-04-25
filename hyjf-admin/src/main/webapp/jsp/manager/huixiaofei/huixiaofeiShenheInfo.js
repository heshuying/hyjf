var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 审核
	shenheAction : "shenheAction",
	// 详细画面的Action
	infoAction : "infoAction",
},
/* 事件动作处理 */
Events = {
	// 审核通过
	shenhetongguoClkAct : function(event) {
		$("#updatestatus").val("0");
		Page.confirm("", "确定审核通过么?", function(isConfirm) {
			if (isConfirm) {
				Events.shenhe(event);
			}
		});
	},
	// 审核不通过
	shenhebutongguoClkAct : function(event) {
		$("#updatestatus").val("1");
		Page.confirm("", "确定审核不通过么?", function(isConfirm) {
			if (isConfirm) {
				Events.shenhe(event);
			}
		});
	},
	// 审核
	shenhe : function(event) {
		// var param = {};
		// param.ids=$("#ids").val();
		// param.updatestatus=$("#updatestatus").val();
		$.ajax({
			url : Action.shenheAction,
			type : "POST",
			async : false,
			data : {
				'ids' : $("#ids").val(),
				'updatestatus' : $("#updatestatus").val()
			},
			success : function(data) {
				Page.coverLayer();
				setTimeout(function() {
					if (data.success) {
						Page.confirm("操作成功", data.msg, "success", {
							showCancelButton : false
						}, function() {
							parent.Events.refreshClkAct() || Page.coverLayer();
						});
					} else {
						Page.confirm("操作失败", data.msg, "error", {
							showCancelButton : false
						}, function() {
							parent.Events.refreshClkAct() || Page.coverLayer();
						});
					}
				}, 500);
			},
			error : function(err) {
				Page.coverLayer();
				Page.notice("执行错误,请重新操作!", "", "error");
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
	// 初始化画面事件处理
	initEvents : function() {
		$(".fn-shenhetongguo").click(Events.shenhetongguoClkAct);
		$(".fn-shenhebutongguo").click(Events.shenhebutongguoClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
});