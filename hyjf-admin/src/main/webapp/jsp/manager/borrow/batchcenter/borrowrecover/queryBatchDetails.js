var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	
	//查询批次交易明细
	queryBatchDetailClkAction : "queryBatchDetailClkAction"
},
/* 事件动作处理 */
Events = {
	
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.queryBatchDetailClkAction);
	}
	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#borrowNid"),
	// 画面布局
	doLayout: function() {
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct)
	}
});
