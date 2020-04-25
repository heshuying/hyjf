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
	//项目期限 选择事件
	changeProjectClkAct : function() {
		if ($(this).val() == "0") {
			$("#protype-2").hide();
			$("#protype-1").hide();
			$("#projectExpirationLengthMin").val(""), Page.validate.ignore("#projectExpirationLengthMin");
			$("#projectExpirationLengthMax").val(""), Page.validate.ignore("#projectExpirationLengthMax");
			$("#projectExpirationLength").val(""), Page.validate.ignore("#projectExpirationLength");
		} else if($(this).val() == "2") {
			$("#protype-2").hide();
			$("#protype-1").show();
			$("#projectExpirationLength").val(""), Page.validate.ignore("#projectExpirationLength");
			Page.validate.unignore("#projectExpirationLengthMin");
			Page.validate.unignore("#projectExpirationLengthMax");
		}else{
			$("#protype-1").hide();
			$("#protype-2").show();
			Page.validate.unignore("#projectExpirationLength");
			$("#projectExpirationLengthMin").val(""), Page.validate.ignore("#projectExpirationLengthMin");
			$("#projectExpirationLengthMax").val(""), Page.validate.ignore("#projectExpirationLengthMax");
		}
	},
	
	//有效期类型 选择事件
	changeExpiraClkAct : function() {
		if ($(this).val() == "1") {
			$("#expType-2").hide();
			$("#expType-1").show();
			$("#expType-3").hide();
			$("#expirationLength").val("");
			$("#expirationLengthDay").val("");
			Page.validate.ignore("#expirationLength");
			Page.validate.ignore("#expirationLengthDay");
			Page.validate.unignore("#expirationDateStr");
		}else if($(this).val() == "2"){
			$("#expType-1").hide();
			$("#expType-2").show();
			$("#expType-3").hide();
			$("#expirationDateStr").val("");
			$("#expirationLengthDay").val("");
			Page.validate.ignore("#expirationDateStr");
			Page.validate.ignore("#expirationLengthDay");
			Page.validate.unignore("#expirationLength");
		}else if($(this).val() == "3"){
			$("#expType-1").hide();
			$("#expType-2").hide();
			$("#expType-3").show();
			$("#expirationDateStr").val("");
			$("#expirationLength").val("");
			Page.validate.ignore("#expirationLength");
			Page.validate.ignore("#expirationDateStr");
			Page.validate.unignore("#expirationLengthDay");
		}
	},
	//出借金额范围 选择事件
	changeTenderClkAct : function() {
		if ($(this).val() == "0") {
			$("#tender-1").hide();
			$("#tender-2").hide();
			$("#tenderQuotaMin").val(""), Page.validate.ignore("#tenderQuotaMin");
			$("#tenderQuotaMax").val(""), Page.validate.ignore("#tenderQuotaMax");
			$("#tenderQuota").val(""), Page.validate.ignore("#tenderQuota");
		} else if($(this).val() == "1") {
			$("#tender-1").show();
			$("#tender-2").hide();
			$("#tenderQuota").val(""), Page.validate.ignore("#tenderQuota");
			Page.validate.unignore("#tenderQuotaMin");
			Page.validate.unignore("#tenderQuotaMax");
		}else{
			$("#tender-1").hide();
			$("#tender-2").show();
			Page.validate.unignore("#tenderQuota");
			$("#tenderQuotaMin").val(""), Page.validate.ignore("#tenderQuotaMin");
			$("#tenderQuotaMax").val(""), Page.validate.ignore("#tenderQuotaMax");
		}
	},
	
	systemAllSelect : function(){
		var isChecked = $("#ct-all").prop("checked");
		
		$("input[name='couponSystem']").each(function(index,element){
			$(this).prop("checked",isChecked);
			Page.validate.check(false,element);
		});
		
	},
	
	projectTypeAllSelect : function(){
		var isChecked = $("#pt-all").prop("checked");
		
		$("input[name='projectType']").each(function(index,element){
			$(this).prop("checked",isChecked);
			Page.validate.check(false,element);
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

        $('#expirationDateStr').datepicker("setStartDate", new Date());

        // 日历范围限制
		$('#start-date-time').on("change", function(evnet, d) {
			d = $('#end-date-time').datepicker("getDate"),
			d && $('#start-date-time').datepicker("setEndDate", d)
		}),
		$('#end-date-time').on("change", function(evnet, d) {
			d = $('#start-date-time').datepicker("getDate"),
			d && $('#end-date-time').datepicker("setStartDate", d)
		}),
		// 体验金
		$('#couponTypeExperience').on("change", function(evnet, d) {
			
			$('#couponQuota').val("");
			$('#couponQuota').attr("datatype","/^[1-9][\\d]{0,7}?$/");
			$('#couponQuota').attr("maxlength","8");
			$('#profitTimeArea').show();
			$('#addFlgArea').show();
			$('#repayTimeConfigArea').show();
			$('#spanOn').show();
			$('#spanOff').hide();
			Page.validate.unignore("#couponProfitTime");
			Page.validate.unignore("input[name='addFlg']");
			Page.validate.unignore("input[name='repayTimeConfig']");
		}),
		// 代金券
		$('#couponTypeCash').on("change", function(evnet, d) {
			$('#couponQuota').val("");
			$('#couponQuota').attr("datatype","/^[1-9][\\d]{0,7}?$/");
			$('#couponQuota').attr("maxlength","8");
			$('#profitTimeArea').hide();
			$('#addFlgArea').hide();
			$('#repayTimeConfigArea').hide();
			$('#spanOn').show();
			$('#spanOff').hide();
			$('#couponProfitTime').val("");
			$("input[name='addFlg']").attr("checked", false);
			Page.validate.ignore("#couponProfitTime");
			Page.validate.ignore("input[name='addFlg']");
			Page.validate.ignore("input[name='repayTimeConfig']");
		}),
		// 加息券
		$('#couponTypeInterestIncrease').on("change", function(evnet, d) {
			$('#couponQuota').val("");
			$('#couponQuota').attr("datatype","/^[1-9][\\d]{0,4}?$|^(([1-9][\\d]{0,4}|0)+(.[0-9]{1,2}))?$/");
			$('#couponQuota').attr("maxlength","5");
			$('#profitTimeArea').hide();
			$('#addFlgArea').hide();
			$('#repayTimeConfigArea').hide();
			$('#spanOff').show();
			$('#spanOn').hide();
			$('#couponProfitTime').val("");
			$("input[name='addFlg']").attr("checked", false);
			Page.validate.ignore("#couponProfitTime");
			Page.validate.ignore("input[name='addFlg']");
			Page.validate.ignore("input[name='repayTimeConfig']");
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
