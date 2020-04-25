var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 联动下拉事件
	assetTypeAction : "assetTypeAction"
},
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要保存当前的账户信息吗？", function(isConfirm) {
				if (isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 选择事件
	changeClkAct : function() {
		if ($(this).val() == "endday") {
			$("#chargeTimeDivDay").show();
			$("#chargeTimeDivMonth").hide();
			//$("#chargeTime").val("");
			//Page.validate.ignore("#chargeTime");
		} else {
			$("#chargeTimeDivMonth").show();
			$("#chargeTimeDivDay").hide();
			//Page.validate.unignore("#chargeTime");
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	
	autoReviewOnchangeAct : function(event) {
		var obj = event;
		if(event && event.target){
			obj = event.target;
		}
		if ($(obj).val() == "1") {
			$("#autoSendMinutes").val("");
			$("#autoReviewMinutes").val("");
			$('#autoSendMinutes').attr("disabled","disabled");
			$('#autoReviewMinutes').attr("disabled","disabled");
		} else {
			$('#autoSendMinutes').attr("disabled",false);
			$('#autoReviewMinutes').attr("disabled",false);
		}
	},
	
	instCodeSrchOnchangeAct : function() {
		var instCode = $("#instCode").val();
		$("#assetType").empty();
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
				  	width : 268,
					placeholder : "全部",
					allowClear : true,
					language : "zh-CN"
				});
				//产品类型控制处理
				Events.assetTypeChange();
			},
			error : function(err) {
				Page.alert("","没有对应的产品类型!");
			}
		});
	},
	
	borrowCdOnchangeAct : function() {
		//产品类型控制处理
		Events.assetTypeChange();
	},
	
	//初期产品类型样式设置
	initAssetTypeChange : function() {
		//保存后台设定值
		var initAssetType = $("#assetType").val();
		//产品类型控制处理
		Events.assetTypeChange();
		//恢复后台设定值
		$("#assetType").val(initAssetType);
	},
	
	//产品类型控制处理
	assetTypeChange : function() {
		if ($("#instCode").val() == INST_CODE_HYJF){
			$("#assetType").attr("readonly",true);
			$("#assetType").val($("#borrowCd").val()).trigger("change");
		}else{
			$("#assetType").removeAttr("readonly");
			$("#assetType").val("").change();
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
		//$("#manChargeTimeType").change(Events.changeClkAct).change();
		// 资产来源选择事件绑定
		$("#instCode").change(Events.instCodeSrchOnchangeAct);
		// 项目类型选择事件绑定
		$("#borrowCd").change(Events.borrowCdOnchangeAct);
		//复审变动事件绑定
		$('input[name="autoReview"]').change(Events.autoReviewOnchangeAct);
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
		($("#success").val() && parent.Events.refreshClkAct())
				|| Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3,
		});
		//初期产品类型控制处理
		Events.initAssetTypeChange();
		Events.autoReviewOnchangeAct('input[name="autoReview"]');
	},
}),

Page.initialize();
