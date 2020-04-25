var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 事件动作处理 */
    Events = {
       /* // 确认按键单击事件绑定
        confirmClkAct : function() {
                Page.confirm("", "确定已完成审核吗？", function(isConfirm) {
                    if(isConfirm) {
                        Page.submit();
                    }
                })

        },*/
        // 确认按键单击事件绑定
        confirmClkAct : function() {
            if($("#mark").val().length<=20){
                Page.confirm("", "确定已完成审核吗？", function(isConfirm) {
                    if(isConfirm) {
                        Page.coverLayer("正在操作数据，请稍候...");
                        $.post($("#mainForm").attr("action"), $("#mainForm").serialize(), function(
                            data) {
                            if (data.success) {
                                Page.coverLayer();
                                parent.$.colorbox.close();
                            } else {
                                Page.coverLayer();
                                setTimeout(function(){Page.alert("错误", data.msg);
                                },200)
                                //
                            }
                        });
                    }
                })
            }else{
                Page.alert("备注字数过长！");
            }


        },
        // 取消按键单击事件绑定
        cancelClkAct : function() {
            parent.$.colorbox.close();
        }
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 初始化画面事件处理
    initEvents : function() {
        // 确认按键单击事件绑定
        $(".fn-Confirm").click(Events.confirmClkAct);
        $(".fn-Cancel").click(Events.cancelClkAct);
    },
   /* // 画面初始化
    initialize : function() {
       /!* // 执行成功后刷新画面
        ($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

        // 初始化表单验证
        Page.validate = Page.form.Validform({
            tiptype: 3,
        });*!/

    },*/
}),

    Page.initialize();
