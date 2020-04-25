var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		
		
		if (Page.validate.check(false)) {
			Page.coverLayer("正在操作数据，请稍候...");
			$('.form-control').attr("disabled",false);
			$.post("shareUser", $("#mainForm").serialize(), function(
					data) {
			//	alert(data);
				if (data.success) {
					Page.alert("成功", "请求成功");
					parent.$.colorbox.close();
				} else {
					Page.coverLayer();
					Page.alert("错误", data.msg);
				}
			});
		}
		
		
		
		
		
//		if(Page.validate.check(false)) {
//			$('.form-control').attr("disabled",false);
//			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
//				if(isConfirm) {
//					$('.form-control').attr("disabled",false);
//					Page.submit();
//				}
//			})
//		}
	},
	// 选择事件
	changeClkAct : function() {
		if ($(this).val() == "endday") {
			$("#chargeTimeDiv").hide();
			$("#chargeTime").val(""), Page.validate.ignore("#chargeTime");
		} else {
			$("#chargeTimeDiv").show();
			Page.validate.unignore("#chargeTime");
		}
	},
	//改变 
	// 选择事件
	sourceClkAct : function() {
		if ($(this).val() == "02") {
			$('#overdueAmount').attr("disabled",false);
			$('#overdueDate').attr("disabled",false);
			$('#overdueLength').attr("disabled",false);
			$('#overdueReason').attr("disabled",false);
		} else {

			$('#overdueAmount').val("");
			$('#overdueDate').val("");
			$('#overdueLength').val("");
			$('#overdueAmount').attr("disabled",true);
			$('#overdueDate').attr("disabled",true);
			$('#overdueLength').attr("disabled",true);
			$('#overdueReason').attr("disabled",true);

		}
	},
	// 选择事件
	approvalClkAct : function() {
		if ($(this).val() == "01") {
			$('#unredeemedMoney').attr("disabled",false);
			$('#repaymentStatus').attr("disabled",false);
			$('#contractBegin').attr("disabled",false);
			$('#contractEnd').attr("disabled",false);
			$('#guaranteeType').attr("disabled",false);
			$('.form-control').css('color','#444444');
			$('.select2-selection').css('color','#444444');
			$('.hui').css('color','#444444');
		} else {
			$('#unredeemedMoney').attr("disabled",true);
			$('#repaymentStatus').attr("disabled",true);
			$('#contractBegin').attr("disabled",true);
			$('#contractEnd').attr("disabled",true);
			$('#guaranteeType').attr("disabled",true);
			$('.form-control').css('color','#c5c5c8');
			$('.select2-selection').css('color','#c5c5c8');
			$('.hui').css('color','#c5c5c8');
			

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
		$("#chargeTimeType").change(Events.changeClkAct).change();
		$("#repaymentStatus").change(Events.sourceClkAct).change();
		$("#approvalResult").change(Events.approvalClkAct).change();
		
	},
	// 画面布局
	doLayout: function() {
		// 初始化下拉框
		$(".form-select2").select2({
			language: "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
		});
	}
	,
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
