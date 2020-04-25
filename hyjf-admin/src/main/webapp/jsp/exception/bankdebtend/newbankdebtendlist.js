var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchNewAction",
	// 同步单个用户
	updateAction : "modifyNewAction",
	// 更新	
	changeAction : "changeNewAction"
},
/* 事件动作处理 */
Events = {
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").change();
	},
	// 同步按钮
	updateClkAct : function(event) {
		Page.primaryKey1.val($(this).data("txcounts"));
		Page.primaryKey2.val($(this).data("txdate"));
		Page.primaryKey3.val($(this).data("batchno"));
        Page.primaryKey4.val($(this).data("status"));
		Page.confirm("", "确定要同步该笔债权么？", "info", {
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
				$.post(Action.updateAction, $("#mainForm").serialize(),
						function(data) {
							clearTimeout(tid);
							Page.coverLayer();
							if (data.status == "success") {
								Page.coverLayer();
								Page.confirm("同步信息成功", "", "info", {
									closeOnConfirm : true,
									showCancelButton : false
								}, function() {
									window.location.reload();
								});
							} else {
								Page.alert("同步信息失败", data.info);
							}
						});
			}
		});
	},
	
	// 更新按钮
	changeClkAct : function(event) {
		Page.primaryKey1.val($(this).data("txcounts"));
		Page.primaryKey2.val($(this).data("txdate"));
		Page.primaryKey3.val($(this).data("batchno"));
		Page.primaryKey4.val($(this).data("status"));
		
		Page.primaryKey5.val($(this).data("orderid"));
		
		Page.confirm("", "确定要批次恢复为初始状态么？", "info", {
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
				$.post(Action.changeAction, $("#mainForm").serialize(),
						function(data) {
							clearTimeout(tid);
							Page.coverLayer();
							if (data.status == "success") {
								Page.coverLayer();
								Page.confirm("批次恢复为初始状态，操作成功", "", "info", {
									closeOnConfirm : true,
									showCancelButton : false
								}, function() {
									window.location.reload();
								});
							} else {
								Page.alert("批次恢复为初始状态，操作失败", data.info);
							}
						});
			}
		});
	},
	
	
	
	
	
	
	
	
	
	
	
	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	primaryKey1 : $("#txCounts"),
	primaryKey2 : $("#txDate"),
	primaryKey3 : $("#batchNo"),
	primaryKey4 : $("#status"),
	
	primaryKey5 : $("#orderId"),
	
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
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 同步按钮
		$(".fn-Modify").click(Events.updateClkAct);
		// 同步全部按钮
		$(".fn-ModifyAll").click(Events.updateAllClkAct);
		// 更新按钮
		$(".fn-Change").click(Events.changeClkAct);
	}
});
