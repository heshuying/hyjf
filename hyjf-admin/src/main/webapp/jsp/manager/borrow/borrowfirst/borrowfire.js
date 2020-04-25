var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 发标的Action
	fireUpdateAction : "fireUpdateAction"
},
/* 事件动作处理 */
Events = {
	// 按钮的单击动作事件
	bailClkAct : function(event) {
		if (Page.validate.check(false)) {
			var message = "确定要执行本次操作吗？";
//			if (!$("#verifyStatus4").prop("checked")) {
//				var borrowNid = $("#borrowNid").val();
//				var borrowNidIdx = borrowNid.substring(borrowNid.length - 2, borrowNid.length) * 1;
//				if (borrowNidIdx != 1) {
//					message = "发标的标的不是第一期，确定要发标吗？";
//				}
//			}
			var disableds = $(":disabled");
			// 增加disabled属性
			disableds.each(function(index, domEle) {
				$(domEle).attr("disabled", "disabled");
			});
			Page.confirm("", message, function(isConfirm) {
				if(isConfirm){
					// 移除disabled属性
					disableds.each(function(index, domEle) {
						$(domEle).removeAttr("disabled");
					});
					Page.submit(Action.fireUpdateAction);
				}
			})
			
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	// 发标方式事件绑定
	radioClkAct : function(event) {
		$("#ontimeCtrl1").hide();
		if ($(this).val() == "2") {
			$("#confirm").html("暂不发标");
			Page.validate.ignore("#ontime");
			$("#ontime").val("");
		}else if ($(this).val() == "3") {
			$("#confirm").html("定时发标");
			$("#ontimeCtrl1").show();
			Page.validate.unignore("#ontime");
		} else if ($(this).val() == "4") {
			$("#confirm").html("立即发标");
			Page.validate.ignore("#ontime");
			$("#ontime").val("");
		} 
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout : function() {
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
		// 刷新按钮的单击事件绑定
		$(".fn-Confirm").click(Events.bailClkAct);
		// 未交保证金按钮的单击事件绑定
		$(".fn-Cancel").click(Events.cancelClkAct);
		// 发标方式事件绑定
		$(".fn-Radio").change(Events.radioClkAct);
	},
	ready : function() {
		$("input[name=verifyStatus]:checked").change();
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
		Events.radioClkAct();
	}
}),

Page.initialize();
