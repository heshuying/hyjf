var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// fix
	fixAction : "rechargeFixAction"
},
/* 事件动作处理 */
Events = {

	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		Page.coverLayer("正在处理,请稍候...");
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// fix单击事件
	fixClkAct : function(event) {
		if (event) {
			Page.primaryKey.val($(this).data("id"))
		}
		Page.confirm("", "您确定要修复该条充值记录吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.id = Page.primaryKey.val();
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.fixAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType : "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						if (data.status == "success") {
							Page.confirm("", data.result, "success", {
								showCancelButton : false
							}, function() {
								Page.coverLayer("正在处理,请稍候...");
								Events.refreshClkAct()
							});
						} else {
							Page.confirm("", data.result, "error", {
								showCancelButton : false
							}, function() {
								Page.coverLayer("正在处理,请稍候...");
								Events.refreshClkAct()
							});
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.notice("系统异常,请重新操作!", "", "error");
					}
				});
			}
			;
			Page.primaryKey.val("");
		})
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {

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
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		});
		$('#start-date-time').datepicker("setStartDate", "2016-04-01");
		$('#end-date-time').datepicker("setStartDate", "2016-04-01");
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// FIX单击事件绑定
		$(".fn-fix").click(Events.fixClkAct);
	}
});
