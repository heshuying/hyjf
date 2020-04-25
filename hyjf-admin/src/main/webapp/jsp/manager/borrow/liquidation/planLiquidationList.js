var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 导出的Action
	exportAction : "exportAction",
	// 详细画面的Action
	infoAction : webRoot + "/manager/borrow/liquidation/detailAction.do",
	// 详情画面
	liquidationAction : webRoot + "/manager/borrow/liquidation/detailAction.do",
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
		
		$("#planNidSrch").val("");
		$("#planStatusSrch").val("");
		$("#liquidateShouldTimeStartSrch").val("");
		$("#liquidateShouldTimeEndSrch").val("");
		$("#liquidateFactTimeStartSrch").val("");
		$("#liquidateFactTimeEndSrch").val("");
	},
	exportClkAct : function() {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},
	// 修改按钮的单击动作事件
	modifyClkAct: function(event) {
		Page.primaryKey.val($(this).data("debtplannid"));
		Page.primaryKey1.val($(this).data("debtplannid"));
			Page.submit(Action.infoAction);
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
	
	// 清算按钮的单击动作事件
	detailClkAct: function(event) {
		Page.primaryKey.val($(this).data("debtplannid"));
		Page.primaryKey1.val($(this).data("debtplannid"));
		Page.primaryKey2.val($(this).data("debtplanstatus"));
			Page.submit(Action.infoAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#planNid"),
	primaryKey1 : $("#debtPlanNidSrch"),
	primaryKey2 : $("#debtPlanStatus"),
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
		// 日历范围限制
		$('#liquidateShouldTimeStartSrch').on("change", function(evnet, d) {
			d = $('#liquidateShouldTimeEndSrch').datepicker("getDate"),
			d && $('#liquidateShouldTimeStartSrch').datepicker("setEndDate", d)
		}),
		$('#liquidateShouldTimeEndSrch').on("change", function(evnet, d) {
			d = $('#liquidateShouldTimeStartSrch').datepicker("getDate"),
			d && $('#liquidateShouldTimeEndSrch').datepicker("setStartDate", d)
		}),
		// 日历范围限制
		$('#liquidateFactTimeStartSrch').on("change", function(evnet, d) {
			d = $('#liquidateFactTimeEndSrch').datepicker("getDate"),
			d && $('#liquidateFactTimeStartSrch').datepicker("setEndDate", d)
		}),
		$('#liquidateFactTimeEndSrch').on("change", function(evnet, d) {
			d = $('#liquidateFactTimeStartSrch').datepicker("getDate"),
			d && $('#liquidateFactTimeEndSrch').datepicker("setStartDate", d)
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Sort").click(Events.sortClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-liquidation").click(Events.detailClkAct),
		// 详情按钮的单击事件绑定
		$(".fn-Info").click(Events.detailClkAct)
	}
});
