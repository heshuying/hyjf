var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 详细画面的Action
	modifyAction : "modifyAction",
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
	// 更新按钮
	updateClkAct : function(event) {
		Page.primaryKey.val($(this).data("creditnid"));
		Page.confirm("", "确定要结束该笔债权么？", "info", {
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
				$.post(Action.modifyAction, $("#mainForm").serialize(),
						function(data) {
							clearTimeout(tid);
							Page.coverLayer();
							if (data.status == "success") {
								Page.coverLayer();
								Page.confirm("债权结束成功", "", "info", {
									closeOnConfirm : true,
									showCancelButton : false
								}, function() {
									window.location.reload();
								});
							} else {
								Page.alert("债权结束失败", data.info);
							}
						});
			}
		});
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 导出按钮
	exportClkAct : function() {
		var startTime = $("#liquidatesTimeStart").val();
		var endTime = $("#liquidatesTimeEnd").val();
		var esTime = $("#repayNextTimeStart").val();
		var eeTime = $("#repayNextTimeEnd").val();
		var flag1 = false;
		var flag2 = false;
		if(startTime == "" || endTime == ""){
			flag1 = true;
		}
		if(esTime == "" || eeTime == ""){
			flag2 = true;
		}
		if(flag1 && flag2){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
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
		$('#liquidatesTimeStart').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#liquidatesTimeEnd').datepicker({
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
		$("#liquidatesTimeStart").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#liquidatesTimeEnd').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#liquidatesTimeEnd').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#liquidatesTimeEnd').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#liquidatesTimeEnd').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#liquidatesTimeEnd").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#liquidatesTimeStart').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#liquidatesTimeStart').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#liquidatesTimeStart').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#liquidatesTimeStart').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#repayNextTimeStart").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#repayNextTimeEnd').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#repayNextTimeEnd').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#repayNextTimeEnd').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#repayNextTimeEnd').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#repayNextTimeEnd").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#repayNextTimeStart').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#repayNextTimeStart').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#repayNextTimeStart').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#repayNextTimeStart').datepicker("setDate", finalEndDate);
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
		// 更新按钮
		$(".fn-Modify").click(Events.updateClkAct);
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 导出的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
	}
});
