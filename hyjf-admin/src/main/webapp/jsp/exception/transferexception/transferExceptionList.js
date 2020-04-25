var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "init",
	//fix
	confirmAction : "confirmAction",
	//fix
	transferAgainAction : "transferAgainAction"
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
	// fix单击事件
	confirmClkAct : function(event) {
		var transStatus = $(this).data("status");
		var transUUID = $(this).data("id");
		Page.confirm("", "确定要执行本次更新操作吗？", function(isConfirm) {
			if (isConfirm) {
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.confirmAction,
					type : "POST",
					async : true,
					data : "uuid=" +  transUUID + "&status=" + transStatus,
					success : function(data) {
						Page.coverLayer();
						if (data == "0") {
							alert("更新转账异常状态未成功");
						} else {
							window.location.reload();
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.notice("更新转账异常状态出错!", "","error");
					}
				});
			};

		})
	},
	
	// fix单击事件
	transferAgainClkAct : function(event) {
		var transUUID = $(this).data("id");
		Page.confirm("", "确定要执行本次更新操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.uuid = $(this).data("id");
				param.status = $(this).data("status");
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.transferAgainAction,
					type : "POST",
					async : true,
					data : "uuid=" +  transUUID,
					success : function(data) {
						var dataArr = data.split(":");
						Page.coverLayer();
						if (dataArr[0] == "0") {
							alert("转账失败：" + dataArr[1]);
						} else {
							window.location.reload();
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.notice("更新转账异常状态出错!", "","error");
					}
				});
			};

		})
	}
	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	//primaryKey : $("#id"),
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
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
		// 修复单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		// 修复单击事件绑定
		$(".fn-Transfer-Again").click(Events.transferAgainClkAct);
	}
});
