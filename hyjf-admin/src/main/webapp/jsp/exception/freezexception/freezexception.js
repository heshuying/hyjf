var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 解冻资金的的Action
	freezeInfoAction : "freezeInfoAction"

},
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {

	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		Page.submit(Action.searchAction);
	},
	// 刷新按钮的单击动作事件
	reSearchClkAct : function() {
		setTimeout(function() {Page.submit(Action.searchAction) }, 600);
	},
	// 解冻资金的单击事件
	freezeClkAct : function() {
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 解冻资金",
			width: "30%", height: "35%",
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.freezeInfoAction).submit();
	        	}, 0)
	        },
	        // Close事件回调
	        onClosed: function() {
	        	Page.form.attr("target", "");
	        }
		})
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#trxId"),
	// 画面布局
	doLayout: function() {
		// 日历选择器
		$('#start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		
	    $("#start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setDate", selectedDate);
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
		// 解冻资金的单击事件绑定
		$(".fn-Freeze").click(Events.freezeClkAct);
	}
});