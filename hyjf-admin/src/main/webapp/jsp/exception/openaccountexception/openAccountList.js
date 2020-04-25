var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	searchAction : "initAction",
	// 开户确认Action
	initUpdateAccountAction : "initUpdateAccountAction",
	// 开户确认Action
	updateAccountLogAction : "updateAccountLogAction"
},
/* 事件动作处理 */
Events = {
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 更新按钮的单击动作事件
	initUpdateAccountClkAct : function(event) {
		Page.primaryKey.val($(this).data("userid")), 
		Page.confirm("", "确定要更新开户信息吗？", function(isConfirm) {
			if (isConfirm) {
			    Page.coverLayer("正在操作数据，请稍候...");
				$.post(Action.updateAccountLogAction,
					$("#mainForm").serialize(), function(data) {
	    				Page.coverLayer();
						if (data.success == "0") {
							Page.confirm("操作成功", "更新成功!", "success", {
								showCancelButton : false
							}, function() {
								Events.refreshClkAct();
							});
						} else if (data.success == "2"){
							$.colorbox({
								overlayClose : false,
								href : "#urlDialogPanel",
								title : "<i class='fa fn-fix'></i> 开户确认",
								width : 650,
								height : 450,
								inline : true,
								fixed : true,
								returnFocus : false,
								open : true,
								// Open事件回调
								onOpen : function() {
									setTimeout(function() {
										Page.submit(Action.initUpdateAccountAction, null, null, "dialogIfm");
									}, 0)
								},
								// Close事件回调
								onClosed : function() {
									Page.form.attr("target", "");
								}
							})
						}else{
							Page.alert("操作失败", data.msg);
						}
					});
				}
		})
	},
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userId"),
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
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct)
		// 查找按钮的单击事件绑定
		$(".fn-fix").click(Events.initUpdateAccountClkAct)
	}
});