var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 页面查询
        searchAction: "searchAction?fid=" + $("#fid").val(),
        //弹出申请框
        InfoAction: "infoAction",
        detailAction: "detailAction",
        auditAction: "auditAction",
        //删除
        deleteAction: "deleteAction",
        // 导出
        exportAction: "exportAction",
        exportDetailAction: "detail/exportAction"
    },
    /* 事件动作处理 */
    Events = {
        // 刷新按钮的单击动作事件
        refreshClkAct: function (event, page) {
            page = page ? page : 1;
            //window.location.reload();
            $("#mainForm").attr("target", "");
            $("#paginator-page").val(page), Page.submit(Action.searchAction);
        },
        // 查找按钮的单击事件绑定
        searchClkAct: function (selection, cds) {
            $("#mainForm").attr("target", "");
            $("#paginator-page").val(1), Page.submit(Action.searchAction);
        },
        // 重置表单
        resetFromClkAct: function () {
            $(".form-select2").val("").trigger('change');
        },
        auditClkAct: function (event) {
            if (event) {
                Page.primaryKey.val($(this).data("id"));
            }
            Page.confirm("", "确定要执行本次审核操作吗？", function (isConfirm) {
                isConfirm && Page.submit(Action.auditAction);
            })
        },
        addClkAct: function (event) {
            if (event) {
                Page.primaryKey.val($(this).data("id"));
            }
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class='fa fa-pencil'></i> 分账",
                width: "50%",
                maxWidth: 800,
                height: "50%",
                inline: true,
                fixed: true,
                returnFocus: false,
                open: true,
                // Open事件回调
                onOpen: function () {
                    setTimeout(function () {
                        Page.submit(Action.detailAction, null, null, "dialogIfm");
                    }, 0)
                },
                // Close事件回调
                onClosed: function () {
                    Page.form.attr("target", "");
                    if ($("#mainForm").attr("status")) {
                        parent.Events.refreshClkAct();
                    }
                }
            })
        },
        //导出报表
        exportClkAct: function () {
            var addStartTime = $("#start-date-time").val();
            var addEndTime = $("#end-date-time").val();
            var poundageStartTime = $("#start-date-time2").val();
            var poundageEndTime = $("#end-date-time2").val();
            var flag1 = false;
            var flag2 = false;
            if(addStartTime == "" || addEndTime == ""){
                flag1 = true;
            }
            if(poundageStartTime == "" || poundageEndTime == ""){
                flag2 = true;
            }
            if(flag1 && flag2){
                Page.confirm("","请选择导出数据的分账时间（分账时间需小于等于31天）","error",{showCancelButton: false}, function(){});
                return false;
            }
            $("#mainForm").attr("target", "_blank");
            Page.notice("正在处理下载数据,请稍候...");
            setTimeout(function () {
                Page.coverLayer()
            }, 1);
            $("#paginator-page").val(1);
            Page.submit(Action.exportAction);
        },
        //导出明细
        exportDetailClkAct: function (event) {
            if (event) {
                Page.primaryKey.val($(this).data("id"));
            }
            $("#mainForm").attr("target", "_blank");
            Page.notice("正在处理下载数据,请稍候...");
            setTimeout(function () {
                Page.coverLayer()
            }, 1);
            $("#paginator-page").val(1);
            Page.submit(Action.exportDetailAction);
        }
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面的主键
    primaryKey: $("#id"),
    // 画面布局
    doLayout: function () {
        // 条件下拉框
        $(".form-select2").select2({
            width: 268,
            placeholder: "全部",
            allowClear: true,
            language: "zh-CN"
        })
        // 日历选择器
        $('#start-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate: new Date()
        });
        $('#end-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate: new Date()
        });
        var day30 = 30 * 24 * 60 * 60 * 1000;
        $("#start-date-time").on("changeDate", function (ev) {
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
        if ($("#start-date-time").val() != '') {
            $('#start-date-time').datepicker("setDate", $("#start-date-time").val());
        }
        // 日历选择器2
        $('#start-date-time2').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate: new Date()
        });
        $('#end-date-time2').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate: new Date()
        });
        var day30 = 30 * 24 * 60 * 60 * 1000;
        $("#start-date-time2").on("changeDate", function (ev) {
            var now = new Date();
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#end-date-time2').datepicker("getDate");
            var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
            $('#end-date-time2').datepicker("setStartDate", selectedDate);
            $('#end-date-time2').datepicker("setEndDate", finalEndDate);
            //如果end值范围超过30天，设置end最大结束时间
            if (endDate != null && (+endDate - selectedDate >= day30)) {
                $('#end-date-time2').datepicker("setDate", finalEndDate);
            }
        });
        if ($("#start-date-time2").val() != '') {
            $('#start-date-time2').datepicker("setDate", $("#start-date-time2").val());
        }
    },
    // 初始化画面事件处理
    initEvents: function () {
        // 刷新按钮的单击事件绑定
        $(".fn-Refresh").click(Events.refreshClkAct);
        // 查找按钮的单击事件绑定
        $(".fn-Search").click(Events.searchClkAct);
        // 重置表单
        $(".fn-Reset").click(Events.resetFromClkAct);
        // 审核的单击事件绑定
        $(".fn-AUDIT").click(Events.auditClkAct);
        // 分账的单击事件绑定
        $(".fn-ADD").click(Events.addClkAct);
        // 导出手续费分账列表
        $(".fn-Export").click(Events.exportClkAct);
        // 下载详情
        $(".fn-DOWNLOAD").click(Events.exportDetailClkAct);
    }
});



