var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 跳转到详情页面
	toRepayAction : "toRepayAction",
	// 导出Excel
	exportAction:"exportAction"
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
	toRepayClkAct : function(event) {
		if(event) {
			$("#borrowNidHidden").val($(this).data("id"));
			$("#periodNowHidden").val($(this).data("period"));
		}
		console.info($("#borrowNidHidden").val());
		console.info($("#periodNowHidden").val());
		Page.submit(Action.toRepayAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	},
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
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 详情
		$(".fn-toRepay").click(Events.toRepayClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
	}
});
