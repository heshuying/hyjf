var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	infoAction : "infoAction",
},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$(".listCheck").prop("checked", this.checked);
	},
	// 添加按钮的单击动作事件
	infoClkAct : function() {
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 添加汇天利配置",
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
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 画面布局
	doLayout : function() {
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$("#checkall").change(Events.selectAllAct),
		// 添加按钮的单击事件绑定
		$(".fn-AddUpdate").click(Events.infoClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
	}
});
