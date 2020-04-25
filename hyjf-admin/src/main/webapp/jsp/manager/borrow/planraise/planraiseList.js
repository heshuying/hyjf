var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 审核画面迁移Action
	infoAction : webRoot + "/manager/borrow/planLock/infoAction",
},
/* 事件动作处理 */
Events = {

	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	
	// 修改按钮的单击动作事件
	modifyClkAct: function(event) {
		$("#planNidSrch").val($(this).data("debtplannid"));
		Page.submit(Action.infoAction);
	},
	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#debtPlanNid"),
	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width : 268,
			placeholder : "请选择条件",
			allowClear : true,
			language : "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		})
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 运营按钮的点击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 刷新按钮的点击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct)
	}
});
