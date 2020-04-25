var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : webRoot + "/manager/borrow/plancommon/infoAction.do",
	// 提审画面的Action
	arraignmentAciton : "arraignmentAction",
	// 审核画面迁移Action
	auditPlanAction : "auditPlanAction.do",
},
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
		$("#borrowNidSrch").val("");
		$("#borrowNameSrch").val("");
	},

	// 提交审核按钮的单击动作事件
	arraignmentClkAct : function(event) {
		if (event) {
			Page.primaryKey.val($(this).data("debtplannid"))
		}

		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 提交审核",
			width : "30%",
			height : "410",
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action",
							Action.arraignmentAciton).submit();
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 审核按钮的单击动作事件
	auditClkAct : function() {
		Page.primaryKey.val($(this).data("debtplannid"));
		Page.submit(Action.auditPlanAction);
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
		// 日历范围限制
		$('#start-date-time').on("change", function(evnet, d) {
			d = $('#end-date-time').datepicker("getDate"),
			d && $('#start-date-time').datepicker("setEndDate", d)
		}),
		$('#end-date-time').on("change", function(evnet, d) {
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
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 提审按钮的单击事件绑定
		$(".fn-Tishen").click(Events.arraignmentClkAct),
		// 审核按钮的单击事件绑定
		$(".fn-audit").click(Events.auditClkAct)
	}
});
