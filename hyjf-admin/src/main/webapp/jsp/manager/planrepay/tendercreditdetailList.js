var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 导出Action
	exportExcelAction:"exportExcel",
	// 出借明细查询Action(暂未实现)
	tenderInfoAction:"tenderInfoAction",
	// 还款明细查询Action(暂未实现)
	repayInfoAction :"repayInfoAction",
	// 发送协议(暂未实现)
	exportAgreementAction : "toExportAgreementAction",
	// 重发邮件的Action(暂未实现)
	resendMessageAction : "resendMessageAction",
	// 跳转到还款计划页面
	repayPlanDetailAction : "repayPlanDetailAction",
},
/* 事件动作处理 */
Events = {
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的应还款起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},
	
	// 出借查询按钮的单击事件
	tenderInfoClkAct : function(event) {
		$("#accedeOrderIdSrch").val($(this).data("planorderid"));
		$("#debtPlanNidSrch").val($(this).data("debtplannid"));
		Page.submit(Action.tenderInfoAction);
	},
	
	// 出借查询按钮的单击事件
	repayInfoClkAct : function(event) {
		$("#accedeOrderIdSrch").val($(this).data("planorderid"));
		$("#debtPlanNidSrch").val($(this).data("debtplannid"));
		Page.submit(Action.repayInfoAction);
	},
	// 跳转到还款明细页面
	toRepayPlanDetailClkAct : function(event) {
		$("#borrowNid").val($(this).data("id"));
		Page.submit(Action.repayPlanDetailAction);
	},
	
	// 发送协议
	exportAgreementClkAct : function(event) {
		var userid = $(this).data("userid");
		var planOrderId = $(this).data("planorderid");
		var debtPlanNid = $(this).data("debtplannid");
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 发送协议",
			width : "30%",
			height : "200",
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.exportAgreementAction + "?userid=" + userid + "&planOrderId=" + planOrderId + "&debtPlanNid=" + debtPlanNid).submit();
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	
	
	// 协议重发按钮的单击动作事件
	updateClkAct : function(event) {
		var param = {};
		param.userid = $(this).data("userid");
		param.planOrderId = $(this).data("planorderid");
		param.debtPlanNid = $(this).data("debtplannid");
		Page.coverLayer("正在操作数据，请稍候...");
		// alert("updateClkAct");
		$.ajax({
			url : Action.resendMessageAction,
			type : "post",
			data : {
				userid : param.userid,
				planOrderId : param.planOrderId,
				debtPlanNid : param.debtPlanNid
			},
			dataType : "json",
			success : function(data) {
				Page.coverLayer();
				if (data.status == "success") {
					Page.confirm("", data.result, "success", {
						showCancelButton : false
					}, function() {
						Page.coverLayer("正在处理,请稍候...");
						Events.refreshClkAct()
					});
				} else {
					Page.confirm("", data.result, "error", {
						showCancelButton : false
					}, function() {
						Page.coverLayer("正在处理,请稍候...");
						Events.refreshClkAct()
					});
				}
			},
			error : function(err) {
				Page.coverLayer();
				Page.notice("系统异常,请重新操作!", "", "error");
			}
		});
	},
	
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
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
		$('#start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		var day30 = 30 * 24 * 60 * 60 * 1000;
	    $("#start-date-time").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#end-date-time').datepicker("getDate");
	        var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过30天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day30)) {
	            $('#end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    if($("#start-date-time").val() != ''){
	    	 $('#start-date-time').datepicker("setDate", $("#start-date-time").val());
	    }
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
		// 查询出借明细
		$(".fn-TenderInfo").click(Events.tenderInfoClkAct);
		// 查询还款明细
		$(".fn-RepayInfo").click(Events.repayInfoClkAct);
		// 协议重发 按钮的单击事件绑定
		$(".fn-Modify").click(Events.updateClkAct);
		// 发送协议 单击事件绑定
		$(".fn-ExportAgreement").click(Events.exportAgreementClkAct);
		// 跳转到还款明细页面
		$(".fn-toRepayPlanDetail").click(Events.toRepayPlanDetailClkAct);
	},
	// 画面初始化
	initialize : function() {
	}
});
