var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "borrowdeleteinit",
	// 列表导出Action
	exportExcelAction : "exportBorrowDelExcelAction"
},
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").trigger('change');
		
		$("#borrow_nidSrch").val("");
		$("#borrow_nameSrch").val("");
		$("#operater_userSrch").val("");
		$("#operater_time_startSrch").val("");
		$("#operater_time_endSrch").val("");
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	sortClkAct : function() {
		$("#col").val($(this).data("col"));
		$("#sort").val($(this).data("sort") == "asc" ? 'asc' : 'desc');
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
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
		$('#operater_time_startSrch').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#operater_time_endSrch').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		
	    $("#operater_time_startSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        $('#operater_time_endSrch').datepicker("setStartDate", selectedDate);
	        $('#operater_time_endSrch').datepicker("setDate", selectedDate);
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Sort").click(Events.sortClkAct),
		// 列表导出
		$(".fn-Export").click(Events.exportClkAct);
	}
});
