var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出execlAction
	exportExcelAction : "exportBankAleveExcel",
	// 查询的Action
	searchAction : "bankaleve_list",
},
/* 事件动作处理 */
Events = {
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		var startTime = $("#start-inpdate-time").val();
		var endTime = $("#end-inpdate-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选查询数据的起止时间（起止时间需小于等于7天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		var startTime = $("#start-inpdate-time").val();
		var endTime = $("#end-inpdate-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于7天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#permissionUuid"),
	
	// 画面布局
	doLayout: function() {
		// 入账日期
		$('#start-valdate-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#end-valdate-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		var day7 = 7 * 24 * 60 * 60 * 1000;
	    $("#start-valdate-time").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#end-valdate-time').datepicker("getDate");
	        var finalEndDate = +selectedDate + day7 >= +now ? now : new Date(+selectedDate + day7);
	        $('#end-valdate-time').datepicker("setStartDate", selectedDate);
	        $('#end-valdate-time').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过7天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day7)) {
	            $('#end-valdate-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    if($("#start-valdate-time").val() != ''){
	    	 $('#start-valdate-time').datepicker("setDate", $("#start-valdate-time").val());
	    };

		//交易日期
		$('#start-inpdate-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#end-inpdate-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		var day7 = 7 * 24 * 60 * 60 * 1000;
		$("#start-inpdate-time").on("changeDate", function(ev) {
			var now = new Date();
			var selectedDate = new Date(ev.date.valueOf());
			var endDate = $('#end-inpdate-time').datepicker("getDate");
			var finalEndDate = +selectedDate + day7 >= +now ? now : new Date(+selectedDate + day7);
			$('#end-inpdate-time').datepicker("setStartDate", selectedDate);
			$('#end-inpdate-time').datepicker("setEndDate", finalEndDate);
			//如果end值范围超过7天，设置end最大结束时间
			if (endDate != null && (+endDate - selectedDate >= day7)) {
				$('#end-inpdate-time').datepicker("setDate", finalEndDate);
			}
		});
		if($("#start-inpdate-time").val() != ''){
			$('#start-inpdate-time').datepicker("setDate", $("#start-inpdate-time").val());
		};

		//自然日期
		$('#start-reldate-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#end-reldate-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		var day7 = 7 * 24 * 60 * 60 * 1000;
		$("#start-reldate-time").on("changeDate", function(ev) {
			var now = new Date();
			var selectedDate = new Date(ev.date.valueOf());
			var endDate = $('#end-reldate-time').datepicker("getDate");
			var finalEndDate = +selectedDate + day7 >= +now ? now : new Date(+selectedDate + day7);
			$('#end-reldate-time').datepicker("setStartDate", selectedDate);
			$('#end-reldate-time').datepicker("setEndDate", finalEndDate);
			//如果end值范围超过7天，设置end最大结束时间
			if (endDate != null && (+endDate - selectedDate >= day7)) {
				$('#end-reldate-time').datepicker("setDate", finalEndDate);
			}
		});
		if($("#start-reldate-time").val() != ''){
			$('#start-reldate-time').datepicker("setDate", $("#start-reldate-time").val());
		};

	},
	
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
	}
});
