var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 事件动作处理 */
    Events = {
        // 确认按键单击事件绑定
        confirmClkAct : function() {
            if (Page.validate.check(false)) {
                Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
                    if (isConfirm) {
                        if(coustomValidform()){
                            Page.submit();
                        }
                    }
                });
            }
        }

    };

$.extend(Page, {

    // 初始化画面事件处理
    initEvents : function() {
        // 确认按键单击事件绑定
        $(".fn-Confirm").click(Events.confirmClkAct);
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
//自定义校验
function coustomValidform(){
    var flag = true;
    if($("#attornRate").val()==""){
        alert("请填写服务费率");
        flag =false;
    }
    var concessionRateUp = $("#concessionRateUp").val();
    var concessionRateDown = $("#concessionRateDown").val();
    if(concessionRateUp==""){
        alert("请填写折让率上限");
        flag =false;
    }
    if(concessionRateDown==""){
        alert("请填写折让率下限");
        flag =false;
    }
    if(parseFloat(concessionRateDown)<0){
        alert("请设置折让率下限大于等于0");
        flag =false;
    }
    if(parseFloat(concessionRateUp)>10){
        alert("请设置折让率上限小于等于10的值");
        flag =false;
    }
    if(parseFloat(concessionRateDown)>parseFloat(concessionRateUp)){
        alert("折让率下限必须小于等于让率上限");
        flag =false;
    }
    return flag;
};
