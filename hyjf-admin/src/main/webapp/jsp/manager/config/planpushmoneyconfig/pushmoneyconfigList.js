var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详情画面Action
	infoAction : "infoAction",
	// 删除Action
	deleteAction : "deleteAction",
	// 检索Action
	searchAction : "searchAction"
},
/* 事件动作处理 */
Events = {
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},

	// 编辑按钮的单击动作事件
	modifyClkAct : function(event) {
		if (event) {
			Page.primaryKey.val($(this).data("ids"))
		}
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 修改汇添金配置",
			width : "50%",
			height : "50%",
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

	// 添加按钮的单击动作事件
	addClkAct : function() {
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 添加汇添金配置",
			width : "50%",
			height : "50%",
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
	// 删除按钮的单击动作事件
	deleteClkAct : function(event) {
		if (event) {
			Page.primaryKey.val(JSON.stringify([ $(this).data("ids") ]))
		}
		Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.deleteAction);
		})
	},
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
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
		// 添加按钮的点击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct)
	}
});
