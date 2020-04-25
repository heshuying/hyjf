var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction"
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
		$(".form-select2").val("").trigger('change');
	},
	// 单击动作事件
	tenderBackClkAct: function(event) {
		var nid = $(this).data("nid");
		Page.confirm("", "确定要对该笔订单做出借撤销么？", "info", {
			closeOnConfirm : false,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确定",
			cancelButtonText : "取消",
			showCancelButton : true,
		}, function(isConfirm, tid) {
			if (isConfirm) {
				Page.coverLayer("正在操作数据，请稍候...");
				tid = setTimeout(function() {
					swal.close();
				}, 100);
				$.post("tenderBackAction", { "nid": nid }, function(data) {
					clearTimeout(tid);
					Page.coverLayer();
					if(data.message != "") {
						Page.alert("操作失败", data.message);
					} else {
						Page.confirm("出借撤销成功", "", "info", {
							closeOnConfirm : true,
							showCancelButton : false
						}, function() {
							window.location.reload();
						});
					}
				});
			}
		});
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function(event) {
		Page.submit(Action.searchAction);
	},
	// 爆标处理记录单击动作事件
	historyClkAct : function(event) {
		Page.submit("historyRecordAction");
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
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
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 单击事件绑定
		$(".fn-TenderBack").click(Events.tenderBackClkAct),
		// 单击事件绑定
		$(".fn-History").click(Events.historyClkAct);
	}
});
