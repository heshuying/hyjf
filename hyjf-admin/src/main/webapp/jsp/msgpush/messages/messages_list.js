var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 启用Action
	statusAction : "statusAction"
},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$(".listCheck").prop("checked", this.checked);
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},
	// 编辑按钮的单击动作事件
	modifyClkAct : function(event) {
		if (event) {
			Page.primaryKey.val($(this).data("id"))
			Page.updateOrReSend.val(0);
		}
		Page.submit(Action.infoAction);
	},
	// 查看按钮的单击动作事件
	infoClkAct : function(event) {
		if (event) {
			Page.primaryKey.val($(this).data("id"))
			Page.updateOrReSend.val(1);
		}
		Page.submit(Action.infoAction);
	},
	// 转发按钮的单击动作事件
	reSendClkAct : function(event) {
		if (event) {
			Page.primaryKey.val($(this).data("id"));
			Page.updateOrReSend.val(2);
		}
		Page.submit(Action.infoAction);
	},
	// 删除
	deleteClkAct : function(event) {
		if (event) {
			Page.primaryKey.val(JSON.stringify([ $(this).data("id") ]))
		}
		Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.deleteAction);
		})
	},
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
	resetFromClkAct : function() {
		$(".form-select2").val("").change();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 操作类型(更新还是转发)
	updateOrReSend : $("#updateOrReSend"),
	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width : 268,
			placeholder : "请选择条件",
			allowClear : true,
			language : "zh-CN"
		})
	},
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$("#checkall").change(Events.selectAllAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 查看按钮的单击事件绑定
		$(".fn-Info").click(Events.infoClkAct),
		// 转发按钮的单击事件绑定
		$(".fn-ReSend").click(Events.reSendClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);

	}
});
