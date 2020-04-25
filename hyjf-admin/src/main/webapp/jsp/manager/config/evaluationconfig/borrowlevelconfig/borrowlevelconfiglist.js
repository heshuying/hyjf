var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 详细画面的Action
        infoAction : "infoAction"
    },
    /* 事件动作处理 */
    Events = {
        // 编辑按钮的单击动作事件
        modifyClkAct: function(event) {
            if(event) {
                Page.primaryKey.val($(this).data("id"))
            }
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i>测评配置-信用等级配置",
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
        // 条件下拉框
        $(".form-select2").select2({
            width : 268,
            placeholder : "全部",
            allowClear : true,
            language : "zh-CN"
        })
    },
    // 初始化画面事件处理
    initEvents : function() {
        // 修改按钮的单击事件绑定
        $(".fn-Modify").click(Events.modifyClkAct),
        // 刷新按钮的单击事件绑定
        $(".fn-Refresh").click(Events.refreshClkAct)
    },
    // 画面初始化
    initialize : function() {
    }
});
