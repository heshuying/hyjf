var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 添加或修改画面的Action
        infoAction: "fieldInfoAction",
        //
        detailAction:"detailAction",
    },
    /* 事件动作处理 */
    Events = {
        // 添加按钮的单击动作事件
        addClkAct: function () {
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 新增字段定义",
                width: "50%",
                height: 430,
                inline: true,
                fixed: true,
                returnFocus: false,
                open: true,
                // Open事件回调
                onOpen: function () {
                    setTimeout(function () {
                        Page.primaryKey.val(""), Page.form.attr("target",
                            "dialogIfm").attr("action", Action.infoAction)
                            .submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function () {
                    Page.form.attr("target", "");
                }
            })
        },

        // 编辑按钮的单击动作事件
        modifyClkAct : function(event) {
            debugger;
            if (event) {
                Page.primaryKey.val($(this).data("id"))
            }
            $.colorbox({
                overlayClose : false,
                href : "#urlDialogPanel",
                title : "<i class=\"fa fa-plus\"></i> 修改字段定义",
                width : "50%",
                height : 430,
                inline : true,
                fixed : true,
                returnFocus : false,
                open : true,
                // Open事件回调
                onOpen : function() {
                    setTimeout(function() {
                        Page.form.attr("target", "dialogIfm").attr("action",
                            Action.infoAction).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed : function() {
                    Page.form.attr("target", "");
                }
            })
        },
        // 查看按钮的单击动作事件
        infoClkAct: function (event) {
            if (event) {
                Page.primaryKey.val($(this).data("id"));
            }
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 字段定义查看详情",
                width: "50%",
                height: 430,
                inline: true,
                fixed: true,
                returnFocus: false,
                open: true,
                // Open事件回调
                onOpen: function () {
                    setTimeout(function () {
                        Page.form.attr("target", "dialogIfm").attr("action",
                            Action.detailAction).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function () {
                    Page.form.attr("target", "");
                }
            })
        },
        // 刷新按钮的单击动作事件
        refreshClkAct: function () {
            window.location.reload();
        }
    };

// --------------------------------------------------------------------------------------------------------------------------------

/* 画面对象 */
$.extend(Page, {
    // 画面的主键
    primaryKey : $("#id"),
    // 画面布局
    doLayout : function() {
    },
    // 初始化画面事件处理
    initEvents : function() {
        // 添加按钮的单击事件绑定
        $(".fn-Add").click(Events.addClkAct),
        // 修改按钮的单击事件绑定
        $(".fn-Modify").click(Events.modifyClkAct),
        // 删除按钮的单击事件绑定
        $(".fn-Info").click(Events.infoClkAct),
        // 刷新按钮的单击事件绑定
        $(".fn-Refresh").click(Events.refreshClkAct);
    }
});

