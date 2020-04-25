var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 发提成Action
	calculatePushMoneyAction : "calculatePushMoneyAction",
	// 导出execlAction
	exportExcelAction : "exportPushMoneyExcelAction",
	// 查询的Action
	searchAction : "searchAction",
},
/* 事件动作处理 */
Events = {
	// 计算提成按钮的单击事件
	calculatePushMoneyClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("debtplannid"))
		}
		Page.confirm("", "确定要执行本次计算提成操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.debtPlanNid = Page.primaryKey.val();
				
				// 异步 防止前面没处理完，后面需求又到，导致锁死
//				tid = setTimeout(function() {
//					Page.coverLayer("正在处理,请稍候...");
//				}, 100);
				setTimeout(function() {
					Page.coverLayer("正在处理,请稍候...");
					
					$.ajax({
						url : Action.calculatePushMoneyAction,
						type : "POST",
						async : true,
						data : JSON.stringify(param),
						dataType : "json",
						contentType : "application/json",
						success : function(data) {
						//	clearTimeout(tid);// 如果后台脚本执行完毕，就把上面的等待命令取消
							Page.coverLayer();
							if (data.status == "success") {
								Page.confirm("", data.result, "success", {
									showCancelButton : false
								}, function() {
									Events.refreshClkAct()
								});
							} else {
//								Page.confirm("", data.result, "error", {
//									showCancelButton : false
//								}, function() {
//									Events.refreshClkAct()
//								});
								Page.notice(data.result, "", "error");
							}
						},
						error : function(err) {
							Page.coverLayer();
							Page.notice("计算提成出现错误,请重新操作!", "", "error");
						}
					});
				}, 100);


			};
			
			Page.primaryKey.val("");
		})
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		$("#mainForm").attr("target", "");
		Page.submit(Action.searchAction);
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	},
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
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
	primaryKey : $("#debtPlanNid"),
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
		
		var day30 = 30 * 24 * 60 * 60 * 1000;
	    $("#start-date-time").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#end-date-time').datepicker("getDate");
	        var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过30天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day30)) {
	            $('#end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    if($("#start-date-time").val() != ''){
	    	 $('#start-date-time').datepicker("setDate", $("#start-date-time").val());
	    }
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 计算提成按钮的单击事件
		$(".fn-Calculator").click(Events.calculatePushMoneyClkAct),
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
