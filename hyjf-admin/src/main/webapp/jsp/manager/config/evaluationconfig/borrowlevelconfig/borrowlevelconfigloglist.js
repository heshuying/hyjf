var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        searchAction:"searchAction"
    },
    /* 事件动作处理 */
    Events = {
        // 查询按钮的单击事件
        searchClkAct : function(event) {
            $("#paginator-page").val(1);
            Page.submit(Action.searchAction);
        },
        // 刷新按钮的单击动作事件
        refreshClkAct : function() {
            window.location.reload();
        }
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面的主键
    primaryKey : $("#id"),
    // 画面布局
    doLayout: function() {
        // 日历选择器
        $('.datepicker').datepicker({
            autoclose: true,
            todayHighlight: true
        });
    },
    // 查询按钮的单击事件
    searchClkAct : function(event) {
        $("#paginator-page").val(1);
        Page.submit(Action.searchAction);
    },
    // 初始化画面事件处理
    initEvents : function() {
        // 刷新按钮的单击事件绑定
        $(".fn-Refresh").click(Events.refreshClkAct),
        // 重置表单
        $(".fn-Reset").click(Events.resetFromClkAct),
            $(".fn-Search").click(Events.searchClkAct);

    },
    // 画面初始化
    initialize : function() {
    }
});
