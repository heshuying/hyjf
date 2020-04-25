var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 导出Action
	exportExcelAction:"exportExcel",

    enhanceExportAction:"enhanceExportAction",
	// 还款明细查询Action(暂未实现)
	repayInfoAction :"repayInfoAction",
},
/* 事件动作处理 */
Events = {
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		var start = $("#start-time").val();
		var end = $("#end-time").val();
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
		Page.submit(Action.exportExcelAction);
	},
    enhanceExportAction : function(selection, cds) {
        var startTime = $("#start-date-time").val();
        var endTime = $("#end-date-time").val();
        var start = $("#start-time").val();
        var end = $("#end-time").val();
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
        Page.submit(Action.enhanceExportAction);
    },
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},

	// 出借查询按钮的单击事件
	repayInfoClkAct : function(event) {
		$("#accedeOrderIdSrch").val($(this).data("planorderid"));
		$("#debtPlanNidSrch").val($(this).data("debtplannid"));
		Page.submit(Action.repayInfoAction);
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
			todayHighlight: true
		});
		$('#end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#start-time').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#end-time').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		var day30 = 30 * 24 * 60 * 60 * 1000;
	    $("#start-date-time").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#end-date-time').datepicker("getDate");
	        var finalEndDate = /*+selectedDate + day30 >= +now ? now :*/ new Date(+selectedDate + day30);
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过30天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day30 || +endDate - selectedDate < 0 )) {
	            $('#end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    if($("#start-date-time").val() != ''){
	    	 $('#start-date-time').datepicker("setDate", $("#start-date-time").val());
	    }
	  // ----------------计划实际还款
	    $("#start-time").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#end-time').datepicker("getDate");
	        var finalEndDate = /*+selectedDate + day30 >= +now ? now : */new Date(+selectedDate + day30);
	        $('#end-time').datepicker("setStartDate", selectedDate);
	        $('#end-time').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过30天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day30 || +endDate - selectedDate < 0)) {
	            $('#end-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    if($("#start-time").val() != ''){
	    	 $('#start-time').datepicker("setDate", $("#start-time").val());
	    }
   
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
		$(".fn-EnhanceExport").click(Events.enhanceExportAction);
		// 查询还款明细
		$(".fn-RepayInfo").click(Events.repayInfoClkAct);
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
	}
});
