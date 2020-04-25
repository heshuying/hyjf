var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 更新单个用户
	updateAction : "modifyAction"
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
		Page.primaryKey1.val($(this).data("userid"));
		Page.confirm("", "确定要结束同步用户CA认证么？", "info", {
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
								Page.confirm("用户CA认证已提交", "", "info", {
									closeOnConfirm : true,
									showCancelButton : false
								}, function() {
									window.location.reload();
								});
							} else {
								Page.alert("用户CA认证已提交失败", data.info);
							}
						});
			}
		});
	},
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	primaryKey1 : $("#userId"),
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

	}
});
