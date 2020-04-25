var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
},
/* 事件动作处理 */
Events = {
	// 添加按钮的单击动作事件
	addClkAct : function() {
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 添加汇转让配置",
			width : "50%",
			height : 430,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.primaryKey.val(""), Page.form.attr("target",
							"dialogIfm").attr("action", Action.infoAction)
							.submit();
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 编辑按钮的单击动作事件
	modifyClkAct : function(event) {
		if (event) {
			Page.primaryKey.val($(this).data("id"))
		}
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 修改汇转让配置",
			width : "50%",
			height : 430,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action",
							Action.infoAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 删除按钮的单击动作事件
	deleteClkAct : function(selection, cds) {
		// 取得选择行
		Page.primaryKey.val($(this).data("id"))
		if (!Page.primaryKey.val()) {
			Page.alert("请选择要删除的数据！", "");
		} else {
			Page.confirm("", "该删除执行的是不可逆操作，确定要物理删除吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit(Action.deleteAction);
				}
			});
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
	primaryKey : $("#id"),
	// 画面布局
	doLayout : function() {
	},
	// 初始化画面事件处理
	initEvents : function() {
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
