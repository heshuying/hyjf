var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction"

},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$.uniform.update($(".listCheck").attr("checked", this.checked));
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},
	// 编辑按钮的单击动作事件
	modifyClkAct : function(selection) {
		// 取得选择行
		selection = $(".listCheck:checked").last();
		console.info(!selection[0]);
		if (!selection[0]) {
			Page.alert("请指定要修改的权限维护！", "alert-warning");
		} else {
			var primaryKey = selection.val();
			Page.primaryKey.val(primaryKey), Page.submit(Action.infoAction);
		}// Endif
	},
	// 删除按钮的单击动作事件
	deleteClkAct : function(selection, cds) {
		// 取得选择行
		selection = $(".listCheck:checked");
		if (!selection[0]) {
			Page.alert("请选择要删除的权限维护！", "alert-warning");
		} else {
			cds = [], selection.each(function() {
				cds.push(this.value);
			}), Page.primaryKey.val(JSON.stringify(cds)), Page.submit(Action.deleteAction);
		}// Endif
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#permissionUuid"),
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$(".checkall").change(Events.selectAllAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
	}
});