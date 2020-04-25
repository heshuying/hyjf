var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 查询的Action
        searchAction : "init",
        // 导出的Action
        exportExcelAction : "exportExcelAction",
        // 添加画面的Action
        infoAction : "infoAction",
        // 修改画面的Action
        updateAction : "updateAction",
        // 删除的Action
        deleteAction : "deleteAction"
    },
    /* 事件动作处理 */
    Events = {
        // 全选checkbox的change动作事件
        selectAllAct : function() {
            $(".listCheck").prop("checked", this.checked);
        },
        // 添加按钮的单击动作事件
        addClkAct : function() {
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 添加统计方式配置",
                width : 650, height: 360,
                inline: true,  fixed: true, returnFocus: false, open: true,
                // Open事件回调
                onOpen: function() {
                    setTimeout(function() {
                        Page.primaryKey.val(""),
                            Page.form.attr("target", "dialogIfm").attr("action", Action.infoAction).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function() {
                    Page.form.attr("target", "");
                }
            })
        },
        // 编辑按钮的单击动作事件
        modifyClkAct: function(event) {
            if(event) {
                Page.primaryKey.val($(this).data("id"))
            }
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 修改统计方式配置配置",
                width : 650, height: 360,
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
        // 导出按钮的单击事件绑定
        exportClkAct : function(selection, cds) {
            $("#mainForm").attr("target", "_blank");
            Page.notice("正在处理下载数据,请稍候...");
            setTimeout(function(){Page.coverLayer()},1);
            Page.submit(Action.exportExcelAction);
        },
        deleteClkAct : function(event) {
            if(event) {
                Page.primaryKey.val($(this).data("id"))
                // Page.primaryKey.val(JSON.stringify([$(this).data("id")]))
            }
            Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
                isConfirm && Page.submit(Action.deleteAction);
            })
        },
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
        }
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面的主键
    primaryKey : $("#ids"),
    pageStatus:$("#pageStatus"),
    // 画面布局
    doLayout: function() {
        // 日历选择器
        $('.datepicker').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
    },
    // 初始化画面事件处理   previewClkAct
    initEvents : function() {
        // 添加按钮的单击事件绑定
        $(".fn-Add").click(Events.addClkAct),
        // 预览按钮的单击事件绑定
        //$(".fn-View").click(Events.previewClkAct),
        // 修改按钮的单击事件绑定
        $(".fn-Modify").click(Events.modifyClkAct),
        // 删除按钮的单击事件绑定
        //	$(".fn-Deletes").click(Events.deletesClkAct),
        $(".fn-Delete").click(Events.deleteClkAct),
        // 刷新按钮的单击事件绑定
        $(".fn-Refresh").click(Events.refreshClkAct),
        // 边界面板查询按钮的单击事件绑定
        $(".fn-Search").click(Events.searchClkAct);
        // 重置按钮的单击事件绑定
        $(".fn-Reset").click(Events.resetFromClkAct);
        $(".fn-Export").click(Events.exportClkAct);


    }
});

