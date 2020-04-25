var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
		// 确认按键单击事件绑定
		confirmClkAct : function() {
			if(Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
				Page.confirm("", "确定要充值吗？", function(isConfirm) {
					if(isConfirm) {
						Page.submit();
					}
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
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		$("#projectExpirationType").change(Events.changeProjectClkAct).change();
		$("#expirationType").change(Events.changeExpiraClkAct).change();
		$("#tenderQuotaType").change(Events.changeTenderClkAct).change();
		// 操作平台全部选中
		$("#ct-all").click(Events.systemAllSelect);
		// 项目类别全部选中
		$("#pt-all").click(Events.projectTypeAllSelect);
		
		$("input[name='couponSystem']").change(function(){
			var len = $("input[name='couponSystem']").length;
			var checkedLen = $("input[name='couponSystem']:checked").length;
			if(checkedLen<len){
				$("#ct-all").prop("checked",false).change();
			}else{
				$("#ct-all").prop("checked",true).change();
			}
		});
		$("input[name='projectType']").change(function(){
			var len = $("input[name='projectType']").length;
			var checkedLen = $("input[name='projectType']:checked").length;
			if(checkedLen<len){
				$("#pt-all").prop("checked",false);
			}else{
				$("#pt-all").prop("checked",true);
			}
		});
	},
	// 画面布局
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
			allowClear: true,
			language: "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
		}),
		// 日历范围限制
		$('#start-date-time').on("change", function(evnet, d) {
			d = $('#end-date-time').datepicker("getDate"),
			d && $('#start-date-time').datepicker("setEndDate", d)
		}),
		$('#end-date-time').on("change", function(evnet, d) {
			d = $('#start-date-time').datepicker("getDate"),
			d && $('#end-date-time').datepicker("setStartDate", d)
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
