var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	searchAction : "accountlist",
	// 导出的Action
	exportAction : "exportaccount",
	// 添加
    addAction:"toAddAccountAction",

    deleteAction : "deleteAction",
},
    /* 事件动作处理 */
    Events = {
        // 重置表单
        resetFromClkAct : function() {
            $(".form-select2").val("").trigger('change');
        },
        // 查找按钮的单击事件绑定
        searchClkAct : function(selection, cds) {
            $("#paginator-page").val(1);
            Page.submit(Action.searchAction);
        },
        // 刷新按钮的单击动作事件
        refreshClkAct : function() {
            window.location.reload();
        },
        // 添加按钮的单击动作事件
        addClkAct : function() {
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 添加",
                width: 650, height: 430,
                inline: true,  fixed: true, returnFocus: false, open: true,
                // Open事件回调
                onOpen: function() {
                    setTimeout(function() {
                        Page.primaryKey.val(""),
                            Page.form.attr("target", "dialogIfm").attr("action", Action.addAction).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function() {
                    window.location.reload();
                    Page.form.attr("target", "");
                }
            })
        },
        deleteClkAct : function(event) {
            if(event) {
                Page.primaryKey.val($(this).data("id"))
                Page.flagKey.val("2")
            }
            Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
                isConfirm && Page.submit(Action.deleteAction);
            })
        },
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面的主键
    primaryKey : $("#id"),
    flagKey : $("#flag"),
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
            $(".fn-Refresh").click(Events.refreshClkAct);
        // 查找按钮的单击事件绑定
        $(".fn-Search").click(Events.searchClkAct);
        // 查找按钮的单击事件绑定
        $(".fn-ConfirmAccount").click(Events.initOpenAccountClkAct);
        //添加按钮绑定
        $(".fn-Add").click(Events.addClkAct);
        $(".fn-Delete").click(Events.deleteClkAct);
    }
});