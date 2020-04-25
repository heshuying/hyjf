var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 事件动作处理 */
    Events = {
        // 确认按键单击事件绑定
        confirmClkAct : function() {
            if(Page.validate.check(false)) {
                Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
                    if(isConfirm) {
                        $.ajax({
                                url : "validationField" ,//url
                                type : "POST",
                                async:false,
                                dataType: "json",//预期服务器返回的数据类型
                                data :  $("#mainForm").serialize(),
                                success:function(data){
                                    var res = eval(data);
                                    var n_error=res.n_error;
                                    var v_error=res.v_error;
                                    var s_error=res.s_error;
                                    if(n_error !=null||s_error !=null||v_error !=null){
                                        $("#n_error").html (n_error);
                                        if(n_error){
                                            $("#titleName").parent().parent().attr("class","form-group has-error");
                                        }
                                        $("#s_error").html (s_error);
                                        $("#v_error").html (v_error);
                                        if(v_error){
                                            $("#uniqueIdentifier").parent().parent().attr("class","form-group has-error");
                                        }
                                    }else{
                                        Page.submit();
                                    }
                                }
                            });
                    }
                })
            }
        },
        // 取消按键单击事件绑定
        cancelClkAct : function() {
            parent.$.colorbox.close();
        },
        // 唯一标识按键单击事件绑定
        validateClkAct : function() {
            //之前的唯一标识错误信息清空
            $("#v_error").html("");
            var uniqueIdentifierClass = $("#uniqueIdentifier").parent().parent().attr("class");
            if(uniqueIdentifierClass == "form-group has-error"){
                $("#uniqueIdentifier").parent().parent().attr("class","form-group");
            }
        },
        // 标题名称按键单击事件绑定
        validateTitleNameClkAct : function () {
            $("#n_error").html("");
            var titleClass = $("#titleName").parent().parent().attr("class");
            if(titleClass == "form-group has-error"){
                $("#titleName").parent().parent().attr("class","form-group");
            }
        },
        // 统计方式按键单击事件绑定
        validateMethodClkAct : function () {
            $("#s_error").html("");
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
        $("#uniqueIdentifier").click(Events.validateClkAct);
        $("#titleName").click(Events.validateTitleNameClkAct);
        $("#statisticalMethod").click(Events.validateMethodClkAct);
    },
    // 画面初始化
    initialize : function() {
        // 执行成功后刷新画面
        ($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();
        // 初始化表单验证
        Page.validate = Page.form.Validform({
            tiptype: 3
        });
    }
}),

    Page.initialize();
