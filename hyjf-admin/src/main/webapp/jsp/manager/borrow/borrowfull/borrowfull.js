var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 复审的Action
	fullInfoAction : "fullInfoAction",
	// 重新放款画面的Action
	repeatAction : "repeatAction",
	// 流标画面的Action
	overAction : "overAction",
	// 出借明细画面的Action
	repeatInfoAction : webRoot + "/manager/borrow/borrowrecover/init?fid=311115"

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
		
		$("#borrowNidSrch").val("");
		$("#borrowNameSrch").val("");
		$("#usernameSrch").val("");
		$("#start-date-time").val("");
		$("#end-date-time").val("");

	},
	repeatClkAct : function() {
		var borrownid = $(this).data("borrownid");
		Page.confirm("确认重新放款", "操作不可逆转，是否确定重新放款？", function(isConfirm) {
			if(isConfirm) {
				Page.primaryKey.val(borrownid);
				Page.submit(Action.repeatAction);
			}
		});
	},
	overClkAct : function() {
		var borrownid = $(this).data("borrownid");
		Page.confirm("确认流标", "操作不可逆转，是否确定流标？", function(isConfirm) {
			if(isConfirm) {
				Page.primaryKey.val(borrownid);
				Page.submit(Action.overAction);
			}
		});
	},
	// 复审按钮的单击动作事件
	fullInfoClkAct : function(selection) {
		Page.primaryKey.val($(this).data("borrownid")),
		Page.submit(Action.fullInfoAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 放款明细的单击动作事件
	repeatInfoClkAct: function(event) {
		$("#borrowNidSrch").val($(this).data("borrownid"));
		$("#usernameSrch").val("");
		$("#borrowNameSrch").val("");
		$("#start-date-time").val("");
		$("#end-date-time").val("");
		Page.submit(Action.repeatInfoAction);
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
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
		}),
		// 日历范围限制
		$('#start-date-time').on("show", function(evnet, d) {
			d = $('#end-date-time').datepicker("getDate"),
			d && $('#start-date-time').datepicker("setEndDate", d)
		}),
		$('#end-date-time').on("show", function(evnet, d) {
			d = $('#start-date-time').datepicker("getDate"),
			d && $('#end-date-time').datepicker("setStartDate", d)
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 复审按钮的单击事件绑定
		$(".fn-Full").click(Events.fullInfoClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重新放款的单击事件绑定
		$(".fn-Repeat").click(Events.repeatClkAct),
		// 放款明细的单击事件绑定
		$(".fn-RepeatInfo").click(Events.repeatInfoClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 流标按钮的单击事件绑定
		$(".fn-Over").click(Events.overClkAct);
	}
});