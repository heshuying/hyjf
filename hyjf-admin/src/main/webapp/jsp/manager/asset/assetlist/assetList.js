var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	infoAction : "detailAction",
	// 页面查询
	searchAction : "searchAction",
	// 导出的Action
	exportAction : "exportAction",
	// 联动下拉事件
	assetTypeAction : "assetTypeAction"
},
/* 事件动作处理 */
Events = {

	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		//window.location.reload();
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	exportClkAct : function() {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		if(startTime == "" || endTime == ""){
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
	},
	instCodeSrchOnchangeAct : function() {
		var instCode = $("#instCodeSrch").val();
		$("#assetTypeSrch").empty();
		if (instCode == "") {
			return;
		}
		$.ajax({
			url : Action.assetTypeAction,
			type : "POST",
			async : true,
			dataType : "json",
			data :  {
				instCode : instCode
			},
			success : function(data) {
				$("#assetTypeSrch").select2({
					data: data,
				  	width : 268,
					placeholder : "全部",
					allowClear : true,
					language : "zh-CN"
				});
				$("#assetTypeSrch").val("").change();
			},
			error : function(err) {
				Page.alert("","没有对应的产品类型!");
			}
		});
		
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
//	// 画面的主键
//	primaryKey : $("#assetId"),
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
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 清空按钮的单击事件绑定
		$(".fn-ClearForm").click(Events.clearClkAct);
		// 资产来源选择事件绑定
		$("#instCodeSrch").change(Events.instCodeSrchOnchangeAct);
		
//		Events.instCodeSrchOnchangeAct();
		
	}
});

