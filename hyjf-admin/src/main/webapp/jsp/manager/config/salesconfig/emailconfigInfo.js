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
        Page.submit(Action.initAction);
    },

    confirmClkAct : function() {
        //var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$"); //正则表达式
        //var reg=new RegExp(" /^[a-zA-z]\\w{3,15}$/");
        var reg=new RegExp("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$");


        if($('#email').val()=="undifind"||$('#email').val()==""){
            alert("邮箱不能为空");
        }else if($('#labelName').val()=="undifind"||$('#labelName').val()==""){
            alert("业务名称不能为空");
        } else {
            var flag=0;
            var emails = $('#email').val().split(";");
            for(var i = 0;i<emails.length;i++){
                if(emails[i]!=""){
                if(!reg.test(emails[i])){ //正则验证不通过，格式不对
                    flag++;
                    alert("邮箱格式验证不通过!");
                }}
            }
            if(flag==0){
                $.ajax({
                    type : "GET",
                    async : false,
                    url :  "checkEmail.do?email="+$('#email').val()+"&id="+$('#id').val(),
                    dataType : 'json',
                    success : function(data) {
                        if(data.result){
                            $("#mainForm").submit();
                        }else{
                            alert("邮箱重复");
                        }
                    },
                    error : function() {
                        alert("校验邮箱失败");
                    }
                });

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
