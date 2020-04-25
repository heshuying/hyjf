var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	//查询转让列表
	transferListAction:"accountList",
	// 导出的Action
	exportAction : "exportAccount"
},
/* 事件动作处理 */
Events = {
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		//window.location.reload();
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.transferListAction);
	},
	// 查找按钮的单击事件绑定
	/*searchClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.transferListAction);
	},*/
	exportClkAct : function() {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function() {
			Page.coverLayer()
		}, 1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userId"),
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
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		}),
		// 日历范围限制
		$('#opreate-start-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#opreate-end-date-time').datepicker("getDate"), d
							&& $('#opreate-start-date-time')
									.datepicker("setEndDate", d)
				}), $('#opreate-end-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#opreate-start-date-time').datepicker("getDate"), d
							&& $('#opreate-end-date-time')
									.datepicker("setStartDate", d)
		}),
		// 日历范围限制
		$('#transfer-start-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#transfer-end-date-time').datepicker("getDate"), d
							&& $('#transfer-start-date-time')
									.datepicker("setEndDate", d)
				}), $('#transfer-end-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#transfer-start-date-time').datepicker("getDate"), d
							&& $('#transfer-end-date-time')
									.datepicker("setStartDate", d)
		})
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		/*$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),*/
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct)
	}
});
Page.initialize();
