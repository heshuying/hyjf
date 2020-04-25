var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)
				&& Page.form.find(".has-error").length == 0) {

			
			var _investStart=parseFloat($("#investStart").val());
			var _increaseMoney=parseFloat($("#increaseMoney").val());
			if(_investStart<_increaseMoney){
				Page.alert("递增金额不能大于起投值！");
				return false;
			}
			
			Page.confirm("", "确定要保存当前的账户信息吗？", function(isConfirm) {
				if (isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 可用券多选事件
	tzptCheckChgAct: function() {
		$(".tzptCheckbox").prop("checked", this.checked);
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
	// 画面布局
	doLayout : function() {
		// 初始化下拉框
		$(".form-select2").select2({
			allowClear : true,
			language : "zh-CN"
		})
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
		// 可用券多选事件
		$("#tzptCheckAll").change(Events.tzptCheckChgAct);
		
	/*	var validation = $("#mainForm").validate({
		    "rules":{
		        "increaseMoney":{
		            "max" : 100
		        }
		    },
		    "messages":{
		        "increaseMoney":{
		            "max" : "递增金额不能大于起投金额"
		        }
		    }
		});*/

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3,
		});
	},
}),

Page.initialize();


