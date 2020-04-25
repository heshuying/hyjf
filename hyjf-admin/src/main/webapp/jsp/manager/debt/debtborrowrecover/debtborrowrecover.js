var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 导出的Action
	exportAction : "exportAction",
},
/* 事件动作处理 */
Events = {
	// 导出按钮的单击动作事件
	exportClkAct: function(event) {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		var esTime = $("#recover-start-date-time").val();
		var eeTime = $("#recover-end-date-time").val();
		var flag1 = false;
		var flag2 = false;
		if(startTime == "" || endTime == ""){
			flag1 = true;
		}
		if(esTime == "" || eeTime == ""){
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
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").trigger('change');
		
		$("#borrowNidSrch").val("");
		$("#orderNumSrch").val("");
		$("#usernameSrch").val("");
		$("#recover-start-date-time").val("");
		$("#recover-end-date-time").val("");
		$("#start-date-time").val("");
		$("#end-date-time").val("");

	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#borrowNid"),
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
	    // 日历选择器
		$('#recover-start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#recover-end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$("#recover-start-date-time").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#recover-end-date-time').datepicker("getDate");
	        var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
	        $('#recover-end-date-time').datepicker("setStartDate", selectedDate);
	        $('#recover-end-date-time').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过30天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day30)) {
	            $('#recover-end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    if($("#recover-start-date-time").val() != ''){
	    	 $('#recover-start-date-time').datepicker("setDate", $("#recover-start-date-time").val());
	    }
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct);
	}
});
