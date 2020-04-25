var Action = {
	// 查找的Action
	searchAction : "searchAction",
	repayunfreezeAction : "repayunfreezeAction"
};

var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct : function() {

	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		Page.submit(Action.searchAction);
	},
	// 确认按键单击事件绑定
	confirmClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("orderid"))
		}
		Page.confirm("", "确定要执行本标的还款的资金冻结撤销操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.orderId = Page.primaryKey.val();
				Page.coverLayer("正在撤销,请稍候...");
				$.ajax({
					url : Action.repayunfreezeAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType : "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						Page.primaryKey.val("");
						if (data.status == "success") {
						    Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
						} else {
							Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.primaryKey.val("");
						Page.notice("冻结解冻发生错误,请重新操作!", "", "error");
					}
				});
			};
		})
	}
};
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	primaryKey : $("#orderId"),
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
		$(".fn-UpdateBalance").click(Events.confirmClkAct);
	},
});

Page.initialize();
