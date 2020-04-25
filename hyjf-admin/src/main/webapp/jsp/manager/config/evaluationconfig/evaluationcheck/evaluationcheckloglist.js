var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 详细画面的Action
        infoAction : "infoAction",
        // 查询Action
        searchAction : "searchAction",
        // 详细画面的Action
        updateAction : "updateAction"
    },
    /* 事件动作处理 */
    Events = {
        // 查询按钮的单击事件
        searchClkAct : function(event) {
            $("#paginator-page").val(1);
            Page.submit(Action.searchAction);
        },
        // 编辑按钮的单击动作事件
        modifyClkAct: function(event) {
            if(event) {
                Page.primaryKey.val($(this).data("id"))
            }
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i>测评配置-开关修改",
                width: "50%", height: "90%",
                inline: true,  fixed: true, returnFocus: false, open: true,
                // Open事件回调
                onOpen: function() {
                    setTimeout(function() {
                        Page.form.attr("target", "dialogIfm").attr("action", Action.infoAction).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function() {
                    Page.form.attr("target", "");
                }
            })
        },
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
            // 查询按钮的单击事件绑定
            $(".fn-Search").click(Events.searchClkAct);

    },
    // 画面初始化
    initialize : function() {
    }
});
