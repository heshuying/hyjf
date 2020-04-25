var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要保存当前的会员信息吗？", function(isConfirm) {
				isConfirm && Page.submit();
			})
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
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
		selectCheck();
	}
}),

Page.initialize();

function selectCheck(){
	if($("#userRole").val()==2){
		$("#borrowerTypeDiv").css('display','block');
		$("#borrowerType").attr("datatype","*1-100");
		if($("#borrowerType").val() =="" ||$("#borrowerType").val() ==" " ||$("#borrowerType").val() ==null){
			 $("#borrowerType").get(0).selectedIndex = 1;
		}
	}else{
		$("#borrowerType").val(" ");
		$("#borrowerTypeDiv").css('display','none');
		$("#borrowerType").attr("datatype","*0-100");
	}
}
