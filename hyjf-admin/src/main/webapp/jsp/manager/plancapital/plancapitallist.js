var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
    // 导出的Action
    exportAction : "exportAction",
    // 复投详细画面的Action
    reinvestInfoAction : "reinvestInfoAction",
    // 债转详细画面的Action
    creditInfoAction : "creditInfoAction"
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
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	},
    // 导出表单
    exportClkAct : function() {
        var startTime = $("#dateFromSrch").val();
        var endTime = $("#dateToSrch").val();
        if(startTime == "" || endTime == ""){
            Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
            return false;
        }
        $("#mainForm").attr("target", "_blank");
        Page.notice("正在处理下载数据,请稍候...");
        setTimeout(function(){Page.coverLayer()},1);
        $("#paginator-page").val(1);
        Page.submit(Action.exportAction);
    },
    // 复投详细的单击动作事件
    reinvestInfoClkAct : function(event) {
        $("#dateKey").val($(this).data("date"));
        $("#planNid").val($(this).data("plannid"));
        Page.submit(Action.reinvestInfoAction);
    },
    // 债转详细的单击动作事件
    creditInfoClkAct : function(event) {
        $("#dateKey").val($(this).data("date"));
        $("#planNid").val($(this).data("plannid"));
        Page.submit(Action.creditInfoAction);
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey1 : $("#dateKey"),
    primaryKey2 : $("#planNid"),
	// 画面布局
	doLayout: function() {
		// 日历选择器
		$('#dateFromSrch').datepicker({
			autoclose: true,
			todayHighlight: true
		});
        $('#dateToSrch').datepicker({
            autoclose: true,
            todayHighlight: true
        });
        $("#dateFromSrch").on("changeDate", function(ev) {
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#dateToSrch').datepicker("getDate");
            if(endDate==null){
                $('#dateToSrch').datepicker("setDate", selectedDate);
            }else if (+selectedDate > +endDate) {
                $('#dateToSrch').datepicker("setDate", selectedDate);
            }
        });
        $("#dateToSrch").on("changeDate", function(ev) {
            var selectedDate = new Date(ev.date.valueOf());
            var startDate = $('#dateFromSrch').datepicker("getDate");
            if(startDate==null){
                $('#dateFromSrch').datepicker("setDate", selectedDate);
            }else if (+startDate > + selectedDate) {
                $('#dateFromSrch').datepicker("setDate", selectedDate);
            }
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
        // 导出按钮的单击事件绑定
        $(".fn-Export").click(Events.exportClkAct);
        // 复投详细按钮的单击事件绑定
        $(".fn-ReinvestInfo").click(Events.reinvestInfoClkAct);
        // 债转详细按钮的单击事件绑定
        $(".fn-CreditInfo").click(Events.creditInfoClkAct);
	}
});
