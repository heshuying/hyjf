var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	searchAction : "init",
	//查看详情
	syncAction : "sync",
	// 导出的Action
	exportAction : "export"
},
/* 事件动作处理 */
Events = {
//	// 添加按钮的单击动作事件
//	addClkAct : function() {
//		Page.submit(Action.infoAction);
//	},
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
	},

	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		//window.location.reload();
		$("#mainForm").attr("target", "");
		Page.submit(Action.searchAction);
	},
	//查看详情
	detailClkAct : function(){
		Page.primaryKey.val($(this).data("id")),
		$("#mainForm").attr("target", "");
		Page.submit(Action.syncAction);
	},
	//导出数据
	exportClkAct : function() {
		$("#mainForm").attr("target", "");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function() {
			Page.coverLayer()
		}, 1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	// 清空按钮的单击动作事件
	clearClkAct : function() {
		Events.clearDepClkAct();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面的主键
	primaryKey : $("#id"),
	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width : 268,
			placeholder : "全部",
			allowClear : true,
			language : "zh-CN"
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
		
	    $("#start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setDate", selectedDate);
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 详情按钮的单击事件绑定
		$(".fn-Info").click(Events.infoClkAct),
		//详情
		$(".fn-Detail").click(Events.detailClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct)
		
	}
});

document.onkeydown = function(event) {
	//e = event ? event : (window.event ? window.event : null);
	if( $("#searchable-panel").hasClass("active") && event.keyCode == 13){
		// 执行的方法
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	}
}

