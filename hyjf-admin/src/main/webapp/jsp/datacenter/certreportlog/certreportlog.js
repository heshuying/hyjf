var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	searchAction : "init?fid="+$("#fid").val()
},
/* 事件动作处理 */
Events = {
		// 刷新按钮的单击动作事件
		refreshClkAct : function() {
			//window.location.reload();
			$("#mainForm").attr("target", "");
			$("#paginator-page").val(1), Page.submit(Action.searchAction);
		},
		// 查找按钮的单击事件绑定
		searchClkAct : function(selection, cds) {
			$("#mainForm").attr("target", "");
			$("#paginator-page").val(1), Page.submit(Action.searchAction);
		},
		// 重置表单
		resetFromClkAct : function() {
			$(".form-select2").val("").trigger('change');
		}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),
	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width : 268,
			placeholder : "全部",
			allowClear : true,
			language : "zh-CN"
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
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// ‘推荐人修改申请’按钮的单击事件绑定
		$(".fn-AdminStockInfo").click(Events.AdminStockInfoClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct),
		// 重置表单
        $(".fn-Reset").click(Events.resetFromClkAct)
        $(".fn-DownFile").click(Events.downLoadClkAct)
        $(".fn-Feedback").click(Events.downloadFeeBack)
	}
});



