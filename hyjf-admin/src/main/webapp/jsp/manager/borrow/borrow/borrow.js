var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 导出的Action
	exportAction : "exportAction",
	// 详细画面的Action
	infoAction : webRoot + "/manager/borrow/borrowcommon/infoAction",
	// 详细画面的Action
	infoConsumeAction : webRoot + "/manager/borrow/consume/infoAction",
	// 出借明细画面的Action
	tenderInfoAction : webRoot + "/manager/borrow/borrowinvest/init?fid=311114",
	// 预览的Action
	previewAction : "previewAction"
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
		
		$("#borrowNidSrch").val("");
		$("#borrowNameSrch").val("");
		$("#usernameSrch").val("");
		$("#start-date-time").val("");
		$("#end-date-time").val("");
	},
	exportClkAct : function() {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		var esTime = $("#recover-start-date-time").val();
		var eeTime = $("#recover-end-date-time").val();
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
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},
	// 修改按钮的单击动作事件
	modifyClkAct: function(event) {
		Page.primaryKey.val($(this).data("borrownid"));
		$("#pageUrl").val($(this).data("pageurl"));
		if($(this).data("projecttype") == "8") {//汇消费
			Page.submit(Action.infoConsumeAction);
		} else {
			/*受托支付不处理汇消费*/
			/*信批需求也不处理汇消费*/
			Page.submit(Action.infoAction);
		}
	},
	
	// 预览按钮的单击动作事件
	previewClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("borrownid"))
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 预览地址",
			width: "50%", height: 300,
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.previewAction).submit();
	        	}, 0)
	        },
	        // Close事件回调
	        onClosed: function() {
	        	Page.form.attr("target", "");
	        }
		})
	},
	
	// 修改按钮的单击动作事件
	tenderInfoClkAct: function(event) {
		$("#borrowNidSrch").val($(this).data("borrownid"));
		$("#usernameSrch").val("");
		Page.submit(Action.tenderInfoAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	sortClkAct : function() {
		$("#col").val($(this).data("col"));
		$("#sort").val($(this).data("sort") == "asc" ? 'asc' : 'desc');
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#borrowNid"),
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
		$('#recover-start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#recover-end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
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
		$('#verify-start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#verify-end-date-time').datepicker({
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
	    };
		$("#recover-start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#recover-end-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#recover-end-date-time').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#recover-end-date-time').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	 $('#recover-end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#recover-end-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#recover-start-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#recover-start-date-time').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#recover-start-date-time').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#recover-start-date-time').datepicker("setDate", finalEndDate);
	        }
	    });

		$("#verify-start-date-time").on("changeDate", function(ev) {
			var selectedDate = new Date(ev.date.valueOf());
			var endDate = $('#verify-end-date-time').datepicker("getDate");
			var finalEndDate = new Date(+selectedDate + day30);
			if(endDate==null){
				$('#verify-end-date-time').datepicker("setDate", finalEndDate);
			}else if (+selectedDate > +endDate) {
				$('#verify-end-date-time').datepicker("setDate", finalEndDate);
			}else if(+endDate - selectedDate > day30){
				$('#verify-end-date-time').datepicker("setDate", finalEndDate);
			}
		});
		$("#verify-end-date-time").on("changeDate", function(ev) {
			var selectedDate = new Date(ev.date.valueOf());
			var startDate = $('#verify-start-date-time').datepicker("getDate");
			var finalEndDate = new Date(+selectedDate -day30);
			if(startDate==null){
				$('#verify-start-date-time').datepicker("setDate", finalEndDate);
			}else if (+startDate > + selectedDate) {
				$('#verify-start-date-time').datepicker("setDate", finalEndDate);
			}else if(+selectedDate - startDate > day30){
				$('#verify-start-date-time').datepicker("setDate", finalEndDate);
			}
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
//		$("#pageUrl").val(window.location.href);
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Sort").click(Events.sortClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-preview").click(Events.previewClkAct),
		// 出借明细按钮的单击事件绑定
		$(".fn-TenderInfo").click(Events.tenderInfoClkAct);
	}
});
