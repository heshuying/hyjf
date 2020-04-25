var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Action = {
	// 查询的Action
	loadCouponConfigAction : "loadCouponConfigAction",
	checkCouponValidAction : "checkCouponValidAction" 
},
	
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要发布优惠券给该用户吗？", function(isConfirm) {
				if(isConfirm){
				    //Page工具不稳定性，需要等待短暂时间
					setTimeout("Events.submitFunction()", 100);
				}
			})
		}
	},
	
	submitFunction : function() {
		var param = {};
		param.couponCode = $("#couponCode option:selected").val();
		var amount=$("#amount").val();
		$.ajax({
			url : Action.checkCouponValidAction,
			type : "POST",
			async : true,
			data : "couponCode=" + param.couponCode+"&amount="+amount,
			success : function(result) {
				if(result == "success"){
					Page.submit();
				}else{
					//alert(result);
					Page.alert("", result);
				}
			},
			error : function(err) {
				Page.alert("","接口调用失败!");
			}
		});
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	
	couponSelectChangeAct : function() {
		var param = {};
		param.couponCode = $("#couponCode option:selected").val();
		console.info(JSON.stringify(param));
		$.ajax({
			url : Action.loadCouponConfigAction,
			type : "POST",
			async : true,
			data : "couponCode=" + param.couponCode,
			success : function(result) {
				result = eval('(' + result + ')');
				if(result.couponType == 1){
					$("#couponQuota").text(result.couponQuota + "元");
				}else if(result.couponType == 2){
					$("#couponQuota").text(result.couponQuota + "%");
				}else if(result.couponType == 3){
					$("#couponQuota").text(result.couponQuota + "元");
				}
				$("#expirationDate").text(result.content);
				if(result.couponType == 1){
					$("#profitTime").text(result.couponProfitTime + "天");
					$("#addFlg").text(result.addFlg==0?'是':"否");
					$("#addFlgArea").show();
					$("#profitTimeArea").show();
				}else{
					$("#profitTime").text("——");
					$("#addFlg").text("——");
					$("#addFlgArea").hide();
					$("#profitTimeArea").hide();
				}
				$("#couponSystem").text(result.couponSystem);
				if(result.projectExpirationType == 0){
					$("#projectExpirationLength").text("不限");
				}else if(result.projectExpirationType == 1) {
					$("#projectExpirationLength").text("等于" + result.projectExpirationLength + "月");
				}else if(result.projectExpirationType == 2) {
					$("#projectExpirationLength").text(result.projectExpirationLengthMin + "月~" + result.projectExpirationLengthMax + "月");
				}else if(result.projectExpirationType == 3) {
					$("#projectExpirationLength").text("大于等于" + result.projectExpirationLength + "月");
				}else if(result.projectExpirationType == 4) {
					$("#projectExpirationLength").text("小于等于" + result.projectExpirationLength + "月");
				}
				$("#projectType").text(result.projectType);
				
				if(result.tenderQuotaType == 0){
					$("#tenderQuota").text("不限");
				}else if(result.tenderQuotaType == 1){
					$("#tenderQuota").text(result.tenderQuotaMin + "元~" + result.tenderQuotaMax + "元");
				}else if(result.tenderQuotaType == 2){
					$("#tenderQuota").text("大于等于" + result.tenderQuota + "元");
				}
				
			},
			error : function(err) {
				Page.alert("","数据取得失败!");
			}
		});
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
		$("#couponCode").change(Events.couponSelectChangeAct);
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
