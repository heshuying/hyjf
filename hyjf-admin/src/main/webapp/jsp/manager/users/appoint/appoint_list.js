var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 检索画面的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction"

},
/* 事件动作处理 */
Events = {
		// 重置表单
		resetFromClkAct : function() {
			$(".form-select2").val("").trigger('change');
		},
		// 查找按钮的单击事件绑定
		searchClkAct : function(selection, cds) {
			$("#paginator-page").val(1);
			Page.submit(Action.searchAction);
		},
		// 刷新按钮的单击动作事件
		refreshClkAct : function() {
			window.location.reload();
		},
		exportClkAct : function() {
			$("#mainForm").attr("target", "_blank");
			Page.notice("正在处理下载数据,请稍候...");
			setTimeout(function() {
				Page.coverLayer()
			}, 1);
			$("#paginator-page").val(1);
			Page.submit(Action.exportAction);
		},
		// 更新按钮的单击动作事件
		initOpenAccountClkAct : function(event) {
			Page.primaryKey.val($(this).data("userid")),
			$.colorbox({
				overlayClose : false,
				href : "#urlDialogPanel",
				title : "<i class='fa fa-pencil'></i> 开户确认",
				width : 650,
				height : 450,
				inline : true,
				fixed : true,
				returnFocus : false,
				open : true,
				// Open事件回调
				onOpen : function() {
					setTimeout(function() {
						Page.submit(Action.initOpenAccountAction, null, null, "dialogIfm");
					}, 0)
				},
				// Close事件回调
				onClosed : function() {
					Page.form.attr("target", "");
				}
			})
		},
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
		$(".fn-Refresh").click(Events.refreshClkAct);
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct)
		// 查找按钮的单击事件绑定
		$(".fn-ConfirmAccount").click(Events.initOpenAccountClkAct)
	}
});