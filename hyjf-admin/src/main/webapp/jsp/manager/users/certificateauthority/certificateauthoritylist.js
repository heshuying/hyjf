var
// --------------------------------------------------------------------------------------------------------------------------------
	/* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 查询的Action
        searchAction : "searchAction",
        // 导出的Action
        exportAction : "exportAction"
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
        resetFromClkAct : function() {
            $(".form-select2").val("").change();
        },
        // 导出表单
        exportClkAct : function() {
            var startTime = $("#start-date-time").val();
            var endTime = $("#end-date-time").val();
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
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    primaryKey1 : $("#userId"),
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
        // 刷新按钮的单击事件绑定
        $(".fn-Refresh").click(Events.refreshClkAct);
        // 边界面板查询按钮的单击事件绑定
        $(".fn-Search").click(Events.searchClkAct);
        // 重置按钮的单击事件绑定
        $(".fn-Reset").click(Events.resetFromClkAct);
        // 导出按钮的单击事件绑定
        $(".fn-Export").click(Events.exportClkAct);
    }
});
