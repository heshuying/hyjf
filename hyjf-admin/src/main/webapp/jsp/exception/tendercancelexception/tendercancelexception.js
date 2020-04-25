var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 解冻资金的的Action
	bidCancelAction : "bidCancelAction",
	// 查询标的状态
	queryBorrowStatusAction : "queryBorrowStatusAction"
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
	// 解冻资金的单击事件
	bidCancelClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("orderid"))
		}
		var borrowNid = $(this).context.dataset.borrownid;
		var isTender = checkBorrowStatus(borrowNid);
		var msg = "确定要执行出借撤销吗？";
		if (!isTender) {
			msg = "该标的已经不是出借中的数据,请谨慎操作！确定要继续执行出借撤销吗？";
		}
		Page.confirm("", msg, function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.orderId = Page.primaryKey.val();
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.bidCancelAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType: "json",
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
						Page.notice("出借撤销发生错误,请重新操作!", "","error");
					}
				});
			};

		})
	}
};

function checkBorrowStatus(borrowNid){
	var result = false;
	var param = {};
	param.borrowNid = borrowNid;
	$.ajax({
		url : Action.queryBorrowStatusAction,
		type : "POST",
		async : false,
		data : JSON.stringify(param),
		dataType: "json",
		contentType : "application/json",
		success : function(data) {
			console.log(data);
			if (data.status == "success") {
			    result =  true;
			} else {
				result =  false;
			}
		},
		error : function(err) {
			Page.notice("校验标的状态发生错误,请重新操作!", "","error");
			result = false;
		}
	});
	return result;
}
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
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
		$(".fn-UpdateBalance").click(Events.bidCancelClkAct);
	}
});