var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 更新单个用户
	updateAction : "updateBankCardExceptionAction",
	// 更新全部
	updateAllAction : "updateAllBankCardExceptionAction"
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
	// 更新按钮
	updateClkAct : function(event) {
		Page.primaryKey.val($(this).data("id"));
		Page.confirm("", "确定要更新该用户的银行卡么？", "info", {
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
							if (data.success) {
								Page.coverLayer();
								Page.confirm("操作成功", data.info, "info", {
									closeOnConfirm : true,
									showCancelButton : false
								}, function() {
									window.location.reload();
								});
							} else {
								Page.alert("操作失败", data.info);
							}
						});
			}
		});
	},
	// 更新全部按钮
	updateAllClkAct : function(event) {
		Page.primaryKey.val($(this).data("id"));
		Page.confirm("", "确定要更新全部的银行卡么？会很耗时!!", "info", {
			closeOnConfirm : false,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确定",
			cancelButtonText : "取消",
			showCancelButton : true,
		}, function(isConfirm, tid) {
			if (isConfirm) {
				Page.alert("处理中", "处理中");
				$.post(Action.updateAllAction, $("#mainForm").serialize(),
						function(data) {
//							clearTimeout(tid);
							if (data.success) {
								Page.alert("操作结果", data.info);
							} else {
								Page.alert("操作结果", data.info);
							}
						});
			}
		});
	},
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	primaryKey : $("#userId"),
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
		// 更新按钮
		$(".fn-Modify").click(Events.updateClkAct);
		// 更新全部按钮
		$(".fn-ModifyAll").click(Events.updateAllClkAct);
	}
});
