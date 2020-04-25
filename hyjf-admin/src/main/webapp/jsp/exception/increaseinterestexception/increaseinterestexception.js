var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 重新还款
	restartRepayAction:"restartRepayAction",

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
	
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	},
	// 重新放款按钮的单击事件绑定
	restartRepayAction : function(event) {
		if(event) {
			$("#borrowNid").val($(this).data("borrownid"));
			$("#id").val($(this).data("id"));
		}

		Page.confirm("确认重新还款", "操作不可逆转，是否确定重新还款？",  function(isConfirm) {
			if (isConfirm) {
				Page.submit(Action.restartRepayAction);
			}
		});
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
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 重新放款按钮的单击事件绑定
		$(".fn-restartRepay").click(Events.restartRepayAction);
		
	}
});
