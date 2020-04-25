function repaymentStatus(){
	var param = {};
	param.repaymentStatus = $("#repaymentStatus option:selected").val();
//    obj = $("#repaymentStatus").text("");
    for(i=0;i<param.repaymentStatus.length;i++){
        if(param.repaymentStatus[i].value=="04")
        	$("#repaymentStatus").text("0");
    }
};
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
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	repaymentStatusChange:function(){
		/*if($("#repaymentStatus option:selected").val()=='04'){
			document.getElementById('unredeemedMoney').value='0';
		}*/
		var param = {};
		param.repaymentStatus = $("#repaymentStatus option:selected").val();
	    for(i=0;i<param.repaymentStatus.length;i++){
	        if(param.repaymentStatus[i]=="4"){
	        	$("#unredeemedMoney").val("0");
	        	$("#unredeemedMoney").attr("readOnly",true); //不可编辑，可以传值
	        }else{
	        	$("#unredeemedMoney").val("");
	        	document.getElementById("unredeemedMoney").disabled =false;
	        	$("#unredeemedMoney").attr("readOnly",false); //可编辑，可以传值
	        }
	        	
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
		/*$("#approvalResult").change(Events.repaymentStatus);*/
		$("#repaymentStatus").change(Events.repaymentStatusChange);
	},
	// 画面布局
	doLayout: function() {
		// 初始化下拉框
		$(".form-select2").select2({
			allowClear: true,
			language: "zh-CN"
		})
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
