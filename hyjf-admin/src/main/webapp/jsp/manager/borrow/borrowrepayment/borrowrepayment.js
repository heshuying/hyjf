var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 跳转到还款计划页面
	repayPlanAction : "repayPlanAction",
	// 跳转到详情页面
	toRecoverAction : "toRecoverAction",
	// 导出Excel
	exportAction:"exportAction",
	//导出还款计划
	exportRepayClkAct:"exportRepayClkAct",
	// 还款
	initRepayAction:"initRepayAction",
	// 延期
	initAfterRepayAction:"initDelayRepayAction",
	// 重新放款
	restartRepayAction:"restartRepayAction",
	// 更新管理费
	updateRecoverFeeAction:"updateRecoverFeeAction"
},
/* 事件动作处理 */
Events = {
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 跳转到还款计划页面
	toRepayPlanClkAct : function(event) {
		$("#borrowNid").val($(this).data("id"));
		Page.submit(Action.repayPlanAction);
	},
	// 跳转到详情页面
	toRecoverClkAct : function(event) {
		$("#borrowNid").val($(this).data("id"));
		Page.submit(Action.toRecoverAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	},
	// 导出按钮的单击动作事件
	exportClkAct: function(event) {
		var startTime = $("#repayLastTimeStartSrch").val();
		var endTime = $("#repayLastTimeEndSrch").val();
		var start = $("#actulRepayTimeStartSrch").val();
		var end = $("#actulRepayTimeEndSrch").val();

		var flag1 = false;
		var flag2 = false;

		if(startTime == "" || endTime == ""){
			flag1 = true;
		}
		if(start == "" || end == ""){
			flag2 = true;
		}
		if(flag1 && flag2){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 导出还款计划按钮的单击动作事件
	exportRepayClkAct: function(event) {
        var start = $("#repayLastTimeStartSrch").val();
        var end = $("#repayLastTimeEndSrch").val();
        var startTime = $("#verifyTimeStartSrch").val();
        var endTime = $("#verifyTimeEndSrch").val();
        var repayTimeStart = $("#actulRepayTimeStartSrch").val();
        var repayTimeEnd = $("#actulRepayTimeEndSrch").val();

        var flag1 = false;
        var flag2 = false;
        var flag3 = false;

        if(startTime == "" || endTime == ""){
            flag1 = true;
        }
        if(start == "" || end == ""){
            flag2 = true;
        }
        if (repayTimeStart == "" || repayTimeEnd == ""){
            flag3 = true;
        }
        if(flag1 && flag2 && flag3){
            Page.confirm("","请选择标的下期还款日或实际还款时间或发布时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
            return false;
        }
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportRepayClkAct);
	},

	repayClkAct : function(event) {
		if(event) {
			$("#nid").val($(this).data("id"));
		}

		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 还款",
			width: "50%", height: "70%",
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.initRepayAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
	},

	delayRepayClkAct : function(event) {
		if(event) {
			$("#nid").val($(this).data("id"));
		}

		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 延期",
			width: "50%", height: "70%",
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.initAfterRepayAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 重新放款按钮的单击事件绑定
	restartRepayAction : function(event) {
		if(event) {
			$("#borrowNidHidden").val($(this).data("id"));
		}

		Page.submit(Action.restartRepayAction);
	},
	// 更新管理费按钮的单击事件绑定
	updateRecoverClkAct : function(event) {
		Page.submit(Action.updateRecoverFeeAction);
	}

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
			allowClear: true,
			language: "zh-CN"
		}),
		/* ----------add by LSY START----------------- */
		// 资产来源默认为全部
		$("#instCodeSrch").select2({
			width: 268,
			placeholder: "全部",
			allowClear: true,
			language: "zh-CN"
		}),
		/* ----------add by LSY END----------------- */
		// 日历选择器
		$('#repayLastTimeStartSrch').datepicker({
			autoclose: true,
			todayHighlight: true,
		});
		$('#repayLastTimeEndSrch').datepicker({
			autoclose: true,
			todayHighlight: true,
		});
		$('#actulRepayTimeStartSrch').datepicker({
			autoclose: true,
			todayHighlight: true,
		});
		$('#actulRepayTimeEndSrch').datepicker({
			autoclose: true,
			todayHighlight: true,
		});
		$('#verifyTimeStartSrch').datepicker({
			autoclose: true,
			todayHighlight: true,
		});
		$('#verifyTimeEndSrch').datepicker({
			autoclose: true,
			todayHighlight: true,
		});
		
		var day30 = 30 * 24 * 60 * 60 * 1000;
		$("#repayLastTimeStartSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#repayLastTimeEndSrch').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#repayLastTimeEndSrch').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#repayLastTimeEndSrch').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#repayLastTimeEndSrch').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#repayLastTimeEndSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#repayLastTimeStartSrch').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#repayLastTimeStartSrch').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#repayLastTimeStartSrch').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#repayLastTimeStartSrch').datepicker("setDate", finalEndDate);
	        }
	    });
		$("#actulRepayTimeStartSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#actulRepayTimeEndSrch').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#actulRepayTimeEndSrch').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#actulRepayTimeEndSrch').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#actulRepayTimeEndSrch').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#actulRepayTimeEndSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#actulRepayTimeStartSrch').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#actulRepayTimeStartSrch').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#actulRepayTimeStartSrch').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#actulRepayTimeStartSrch').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#verifyTimeStartSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#verifyTimeEndSrch').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#verifyTimeEndSrch').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#verifyTimeEndSrch').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#verifyTimeEndSrch').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#verifyTimeEndSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#verifyTimeStartSrch').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#verifyTimeStartSrch').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#verifyTimeStartSrch').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#verifyTimeStartSrch').datepicker("setDate", finalEndDate);
	        }
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$(".fn-toRepayPlan").click(Events.toRepayPlanClkAct),
		// 详情
		$(".fn-toRecover").click(Events.toRecoverClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
		// 导出还款计划按钮的单击事件绑定
		$(".fn-repay-Export").click(Events.exportRepayClkAct);
		// 还款按钮的单击事件绑定
		$(".fn-Repay").click(Events.repayClkAct);
		// 延期按钮的单击事件绑定
		$(".fn-delayRepay").click(Events.delayRepayClkAct);
		// 重新放款按钮的单击事件绑定
		$(".fn-restartRepay").click(Events.restartRepayAction);
		// 更新管理费按钮的单击事件绑定
		$(".fn-UpdateRecoverFee").click(Events.updateRecoverClkAct);
	}
});
