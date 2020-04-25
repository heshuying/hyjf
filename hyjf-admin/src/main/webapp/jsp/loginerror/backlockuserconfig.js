// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面布局
    doLayout: function() {}
});

var
//--------------------------------------------------------------------------------------------------------------------------------
    /* 事件动作处理 */
    Events = {
        // 确认按键单击事件绑定
        confirmClkAct : function() {
            if($("#error-lockLong").val()==""){
                Page.alert("请正确填写锁定时间!");
                return;
            }
            if($("#error-max").val()==""){
                    Page.alert("请正确填写登录失败最大次数");
                    return;
            }
            Page.confirm("", "确定要提交修改吗？", function(isConfirm) {
                var paramete={};
                paramete.maxLoginErrorNum=$.trim($("#error-max").val());
                paramete.lockLong=$.trim($("#error-lockLong").val());
                if (isConfirm) {
                    $.ajax({
                        url: ctx + '/loginerrorconfig/adminconfig',
                        method: 'post',
                        dataType: "json",
                        contentType: 'application/json;charset=UTF-8',
                        data: JSON.stringify(paramete),
                        success: function (data) {
                            if(data.status == '0'){
                                Page.notice("保存配置信息成功!", "","success");
                                parent.$.colorbox.close();
                            }else{
                                Page.notice("保存配置信息发生错误,请重新操作!", "","error");
                                parent.$.colorbox.close();
                            }
                        },
                        error: function (response) {
                            Page.notice("保存配置信息发生错误,请重新操作!", "","error");
                            parent.$.colorbox.close();
                        }
                    });
                }
            })
        },

        cancelClkAct : function(){
            parent.$.colorbox.close();
        }
    };

//--------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面的主键
    primaryKey : $("#id"),

    // 初始化画面事件处理
    initEvents : function() {
        // 确认按钮的点击事件
        $(".fn-Confirm").click(Events.confirmClkAct);
        //取消按钮的点击事件
        $(".fn-Cancel").click(Events.cancelClkAct);
    },

});
