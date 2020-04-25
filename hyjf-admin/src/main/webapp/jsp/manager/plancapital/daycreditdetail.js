var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 导出
	exportAction : "exportAction"
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
	// 详细的单击动作事件
	infoClkAct : function(event) {
		$("#creditNid").val($(this).data("creditnid"));
		Page.submit(Action.infoAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 导出按钮
	exportClkAct : function() {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function() {
			Page.coverLayer()
		}, 1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	}
	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#creditNid"),
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
		$('#endTimeStart').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#endTimeEnd').datepicker({
			autoclose: true,
			todayHighlight: true
		});
	    // 日历选择器
		$('#repayNextTimeStart').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#repayNextTimeEnd').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		var day30 = 30 * 24 * 60 * 60 * 1000;
		$("#endTimeStart").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#endTimeEnd').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#endTimeEnd').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#endTimeEnd').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#endTimeEnd').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#endTimeEnd").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#endTimeStart').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#endTimeStart').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#endTimeStart').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#endTimeStart').datepicker("setDate", finalEndDate);
	        }
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
		// 详细按钮的单击事件绑定
		$(".fn-Info").click(Events.infoClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 导出的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
	}
});
