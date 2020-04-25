var Action = {
	validateBefore : "validateBeforeAction",
	queryUser:"insertAction"
};
function Functions(){
	this.doDisabled = doDisabled;
	this.doUnDisabled = doUnDisabled;
	this.checkDisabled = checkDisabled;
	
	function doDisabled(){
		$('#loanType').attr("disabled",true);
		$('#loanMoney').attr("disabled",true);
		$('#loanTimeLimit').attr("disabled",true);
		$('#creditAddress').attr("disabled",true);
		
	};
	function doUnDisabled(){
		$('#loanType').attr("disabled",false);
		$('#loanMoney').attr("disabled",false);
		$('#loanTimeLimit').attr("disabled",false);
		$('#creditAddress').attr("disabled",false);
	};
	function checkDisabled(){
		var $selectedvalue = $("input[name='isAuto']:checked").val();
		if($selectedvalue==1){
			doDisabled();
		}else{
			doUnDisabled();
		}
	}
}
var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)) {
			Page.coverLayer("正在操作数据，请稍候...");
			new Functions().doUnDisabled();
			$.post(Action.queryUser, $("#mainForm").serialize(), function(
					data) {
				
				if (data.success) {
					Page.alert("成功", "请求成功");
					parent.$.colorbox.close();
				} else {
					new Functions().checkDisabled();
					Page.coverLayer();
					Page.alert("错误", data.msg);
				}
			});
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	// 取消按键单击事件绑定
	biao : function() {
		var itme=$(this).children('option:selected').val(); 
		if(itme!=0||itme!=null){
			var strs= new Array(); //定义一数组 
			 strs=itme.split("|"); //字符分割${item.id}|${item.loanType}|${item.loanMoney}|${item.loanTimeLimit}
			 $("#loanType").find("option[value='"+strs[1]+"']").attr("selected",true);
			 $("#loanMoney").attr("value",strs[2]);
			 $("#loanTimeLimit").attr("value",strs[3]);
			 $("#creditAddress").find("option[value='"+strs[4]+"']").attr("selected",true);
			// alert($("#select2-creditAddress-container").html().substring(0,$("#select2-creditAddress-container").html().indexOf('</span>')+7)+$("#creditAddress").find("option:selected").text()); 
			 $("#select2-creditAddress-container").html($("#select2-creditAddress-container").html().substring(0,$("#select2-creditAddress-container").html().indexOf('</span>')+7)+$("#creditAddress").find("option:selected").text())
			 $("#configureId").attr("value",strs[0]);
			 
		}
		 
		 
	},	radio : function() {
			var $selectedvalue = $("input[name='isAuto']:checked").val();
			if($selectedvalue==1){
				$("#biao").find('option[value="0"]').attr("selected",true);
				new Functions().doDisabled();
				$("#biaodiv").show();
				$("#configureId").attr("value",(($("#biao").children('option:selected').val()).split("|"))[0]);
				
				$("#select2-biao-container").html($("#select2-biao-container").html().substring(0,$("#select2-biao-container").html().indexOf('</span>')+7)+'请选择');
				
			}else{
				new Functions().doUnDisabled();
				$("#biaodiv").hide();
				$("#configureId").attr("value",'');
				$("#loanMoney").attr("value",'');
				$("#loanTimeLimit").attr("value",'');

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
		$("#biao").change(Events.biao);
		$(".event-categories").change(Events.radio);
	},
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 300,
			placeholder: "",
			allowClear: true,
			language: "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
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
