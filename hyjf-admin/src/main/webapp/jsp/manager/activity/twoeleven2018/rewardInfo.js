var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 事件动作处理 */
    Events = {
        // 确认按键单击事件绑定
        confirmClkAct : function() {
            Page.submit();

        },
        // 取消按键单击事件绑定
        cancelClkAct : function() {
            parent.$.colorbox.close();
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
    // 初始化画面事件处理
    initEvents : function() {
        // 确认按键单击事件绑定
        $(".fn-Confirm").click(Events.confirmClkAct);
        $(".fn-Cancel").click(Events.cancelClkAct);
    }
});
Page.initialize();