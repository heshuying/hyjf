var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 提现确认Action
	confirmWithdrawAction : "confirmWithdrawAction",
	// 导出execlAction
	exportExcelAction : "exportWithdrawExcelAction",
	// 查询的Action
	searchAction : "searchWithdrawAction",
},
/* 事件动作处理 */
Events = {
	// 提现确认按钮的单击事件
	confirmWithdrawClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("id"))
		}
		Page.confirm("", "确定要执行本次提现确认操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.ids = Page.primaryKey.val();
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.confirmWithdrawAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType: "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						if (data.status == "success") {
						    Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
						} else {
							Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.notice("提现确认发生错误,请重新操作!", "","error");
					}
				});
			};

		})
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		Page.submit(Action.searchAction);
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
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
	primaryKey : $("#ids"),
	// 画面布局
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "全部",
			allowClear: true,
			language: "zh-CN"
		}),
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
		// 提现成功按钮的单击事件
		$(".fn-Confirm").click(4, Events.confirmWithdrawClkAct),
		// 提现失败按钮的单击事件
		$(".fn-Confirm-Fail").click(0, Events.confirmWithdrawClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
	}
});
