var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 跳转到还款计划页面
	toXiangqingAction : "toXiangqingAction",
	// 导出Excel
	exportAction:"exportAction",

    enhanceExportAction:"enhanceExportAction"
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
        if(!checkQuery()){
            Page.confirm("","请至少选择一个查询条件！","error",{showCancelButton: false}, function(){});
            return false;
        }
		Page.submit(Action.searchAction);
	},
	// 跳转到还款计划页面
	toXiangqingClkAct : function(event) {
		$("#nid").val($(this).data("id"));
		Page.submit(Action.toXiangqingAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	},
	// 导出按钮的单击动作事件
	exportClkAct: function(event) {
		var startTime = $("#recoverTimeStartSrch").val();
		var endTime = $("#recoverTimeEndSrch").val();
		if((startTime == "" || endTime == "")&&($("#yesTimeStartSrch").val()==""||$("#yesTimeEndSrch").val()=="")){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
    enhanceExportAction: function(event) {
        var startTime = $("#recoverTimeStartSrch").val();
        var endTime = $("#recoverTimeEndSrch").val();
        if((startTime == "" || endTime == "")&&($("#yesTimeStartSrch").val()==""||$("#yesTimeEndSrch").val()=="")){
            Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
            return false;
        }
        $("#mainForm").attr("target", "_blank");
        Page.notice("正在处理下载数据,请稍候...");
        setTimeout(function(){Page.coverLayer()},1);
        $("#paginator-page").val(1);
        Page.submit(Action.enhanceExportAction);
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
			allowClear: true,
			language: "zh-CN"
		}),
		/* ----------add by LSY START----------------- */
		// 资产来源默认为全部
		$("#instCodeSrch").select2({
			width: 268,
			placeholder: "全部",
			allowClear: true,
			language: "zh-CN"
		}),
		/* ----------add by LSY END----------------- */
		// 日历选择器
		$('#recoverTimeStartSrch').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#recoverTimeEndSrch').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		// 日历选择器
		$('#yesTimeStartSrch').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#yesTimeEndSrch').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		var day30 = 30 * 24 * 60 * 60 * 1000;
		$("#recoverTimeStartSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#recoverTimeEndSrch').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#recoverTimeEndSrch').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#recoverTimeEndSrch').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#recoverTimeEndSrch').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#recoverTimeEndSrch").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#recoverTimeStartSrch').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#recoverTimeStartSrch').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#recoverTimeStartSrch').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#recoverTimeStartSrch').datepicker("setDate", finalEndDate);
	        }
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$(".fn-toHuankuanMingxi").click(Events.toXiangqingClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);

        $(".fn-EnhanceExport").click(Events.enhanceExportAction);
	}
});
/**
 * add by nxl 20180529
 * 检查查询条件（至少有一个查询条件）
 **/
function checkQuery() {
    var  isChecked = false;
    $("#mainForm").find("input[type='text']").each(function(){
        if($(this).val()!=null&&$(this).val()!=undefined&&$(this).val()!=""){
            isChecked=true;
        }
    });
    if(!isChecked){
        $("#mainForm").find("select").each(function () {
            if($(this).val()!=null&&$(this).val()!=undefined&&$(this).val()!=""){
                isChecked=true;
            }
        });
    }
    return isChecked;
}