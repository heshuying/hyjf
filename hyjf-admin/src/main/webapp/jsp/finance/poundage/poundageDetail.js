var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 联动下拉事件
        userNameAction: "userNameAction"
    },
    /* 事件动作处理 */
    Events = {
        // 确认按键单击事件绑定
        confirmClkAct: function () {
            if (Page.validate.check(false)
                && Page.form.find(".has-error").length == 0) {
                Page.confirm("", "确定要发起此笔转账吗？", function (isConfirm) {
                    isConfirm && Page.submit();
                })
            }
        },
        // 取消按键单击事件绑定
        cancelClkAct: function (event) {
            parent.$.colorbox.close();
            // 转账未发生错误时只刷新至当前页数的页面
            var page = $("#status").val() ? null : $("#equiPaginator", window.parent.document).find(".active a").text();
            parent.Events.refreshClkAct(event, page);
        }
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面布局
    doLayout: function () {
    },
    // 初始化画面事件处理
    initEvents: function () {
        // 确认按键单击事件绑定
        $(".fn-Confirm").click(Events.confirmClkAct);
        $(".fn-Cancel").click(Events.cancelClkAct);
    },
    // 画面初始化
    initialize: function () {
        if($("#success").val() && $("#success").val() != 'success') {
            parent.$.colorbox.close();
            Page.notice("请求银行信息失败，请稍后再试！", "","error");
            return false;
        }
        // 执行成功后刷新画面
        ($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
        $("#mainForm", window.parent.document).attr("status", $("#status").val());
        // 初始化表单验证
        Page.validate = Page.form.Validform({
            tiptype: 3
        });
    }
});
Page.initialize();
