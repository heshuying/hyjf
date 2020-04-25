var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出execlAction
	exportExcelAction : "exportAccountsExcel",
	// 查询的Action
	searchAction : "accountmanage_list",
	// 详细画面的Action
	infoAction : "accountdetail_list?fid=1112",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction"
},
/* 事件动作处理 */
Events = {
//	// 全选checkbox的change动作事件
//	selectAllAct : function() {
//		$(".listCheck").prop("checked", this.checked);
//	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// ‘查看’按钮的单击事件
	selectDetailAct : function(event) {
		// alert($(this).data("userId")+"==="+$(this).data("userid"));
		Page.primaryKey.val($(this).data("userid"));
		Page.submit(Action.infoAction);
	},
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	}
	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userId"),
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$("#checkall").change(Events.selectAllAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-search").click(Events.searchClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// ‘查看’按钮的单击事件
		$(".fn-info").click(Events.selectDetailAct);
	}
});
