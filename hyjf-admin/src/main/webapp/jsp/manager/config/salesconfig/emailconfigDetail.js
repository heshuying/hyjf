var
// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
    initAction:"init"
},
/* 事件动作处理 */
Events = {
    //返回列表
    returnClkAct:function() {
        //Page.submit(Action.initAction);

        window.location.href="init";

    },

    confirmClkAct : function() {
        var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
        if($('#email').val()=="undifind"||$('#email').val()==""){
            alert("邮箱不能为空");
        }else {
            var flag=0;
            var emails = $('#email').val().split(";");
            for(var i = 0;i<emails.length;i++){
                if(!reg.test(emails[i])){ //正则验证不通过，格式不对
                    flag++;
                    alert("验证不通过!");
                }
            }
            if(flag==0){
                $("#mainForm").submit();
            }

        }
    }

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
        $(".fn-return").click(Events.returnClkAct),
        $(".fn-save").click(Events.confirmClkAct)
    }
});
