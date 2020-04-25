var
// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 联动下拉事件
	assetTypeAction : "assetTypeAction"
},
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
	instCodeOnchangeAct : function() {
		//产品类型控制处理
		var instCode = $("#instCode").val();
		$("#assetType").empty();
		var instName = $("#instCode").find("option:selected").attr("data-cd");
		if (instCode == "") {
			return;
		}
		$.ajax({
			url : Action.assetTypeAction,
			type : "POST",
			text : "",
			async : true,
			dataType : "json",
			data :  {
				instCode : instCode
			},
			success : function(data) {
				$("#assetType").select2({
					data: data,
				  	width : 240,
					placeholder : "全部",
					allowClear : true,
					language : "zh-CN"
				});
				$("#instName").val(instName);
				Events.assetTypeChange();
			},
			error : function(err) {
				Page.alert("","没有对应的产品类型!");
			}
		});
	},
	//还款方式
	borrowStyleOnchangeAct : function() {
		var borrowStyleName = $("#borrowStyle").find("option:selected").attr("data-cd");
		if (borrowStyleName == "") {
			return;
		}
		$("#borrowStyleName").val(borrowStyleName);
	},
	isCreditNoOnchangeAct : function() {
		$("#creditSumMaxDiv").css('display','none'); 
		$("#creditSumMax").val(''); 
	},
	isCreditYesOnchangeAct : function() {
		$("#creditSumMaxDiv").css('display','block'); 
	},
	//项目类型
	projectTypeOnchangeAct : function() {
		var projectTypeName = $("#projectType").find("option:selected").attr("data-cd");
		if (projectTypeName == "") {
			return;
		}
		if ($("#instCode").val() == INST_CODE_HYJF){
		//产品类型联动控制处理
			Events.assetTypeChange();
		}
		$("#projectTypeName").val(projectTypeName);
	},
	//产品类型
	assetTypeOnchangeAct : function() {
		var val =  $("#assetType").val();
		var assetTypeName = $("#assetType").find("option[value="+val+"]").text()
		if (assetTypeName == "") {
			return;
		}
		$("#assetTypeName").val(assetTypeName);
	},
	//产品类型联动控制处理
	assetTypeChange : function() {
		if ($("#instCode").val() == INST_CODE_HYJF){
			$("#assetType").attr("readonly",true);
			var cd = $("#projectType").find("option:selected").data("cd");
			var cn = $("#projectType").find("option:selected").data("cn");
			$("#assetType").val(cn).trigger("change");
			$("#assetTypeName").val(cd);
		}else{
			$("#assetType").removeAttr("readonly");
			$("#assetType").val("").change();
		}
	},
	chargeModeOnchangeAct : function() {
		//
		if($(".chargeOn").checked){
			$("#serviceFeeTotal").hide();
			/*$("#serviceFeeTotal").remove();*/
		}
		if($(".chargeOff").checked){
			
			$("#serviceFeeTotal").show();
		}
	},	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	doLayout : function(){
		// 条件下拉框
		$(".form-select2").select2({
			width : 240,
			placeholder : "全部",
			allowClear : true,
			language : "zh-CN"
		})
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		// 资产来源选择事件绑定
		$("#instCode").change(Events.instCodeOnchangeAct);
		// 产品类型选择事件绑定
		$("#assetType").change(Events.assetTypeOnchangeAct);
		// 还款类型选择事件绑定
		$("#borrowStyle").change(Events.borrowStyleOnchangeAct);
		// 项目类型选择事件绑定
		$("#projectType").change(Events.projectTypeOnchangeAct);
		// 标的是否发生债转单选按钮点击 
		$("#isCreditNo").change(Events.isCreditNoOnchangeAct);
		// 标的是否发生债转单选按钮点击  
		$("#isCreditYes").change(Events.isCreditYesOnchangeAct);
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && Page.submit("searchAction")) || Page.coverLayer();
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
		if ($("#instCode").val() == INST_CODE_HYJF){
			$("#assetType").attr("readonly",true);
			var cd = $("#projectType").find("option:selected").data("cd");
			var cn = $("#projectType").find("option:selected").data("cn");
			$("#assetType").val(cn).trigger("change");
			$("#assetTypeName").val(cd);
		}
	}
}),

Page.initialize();
