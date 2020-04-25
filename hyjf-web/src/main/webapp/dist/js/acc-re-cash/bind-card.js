var mobFlag=true;
function countdown(el,s,w){ //获取手机验证码
    mobFlag=false;
    var w=w || '获取验证码' //结束后的文案
    var s=parseInt(s);
    el.text(s+'s');
    var time=window.setInterval(function(){
        s--;
        if(s>0||s==0){
            el.text(s+'s');
        }else{
            el.html(w);
            mobFlag=true;
            clearInterval(time);
        }
    },1000)
    return time
}
$('.get-code').click(function(){
    if(mobFlag&&$("#card").valid()&&$("#mobile").valid()){
        var t=countdown($('.get-code'),60,'<em class="rule"></em>获取验证码')
        $.ajax({
            type:"post",
            url:webPath + "/bank/web/bindCard/sendPlusCode.do", //绑卡验证码地址
            data:$('#cashForm').serialize(),
            success:function(data){
                if(!data.status){
                    utils.alert({id:'errorCode',title:'错误',content:data.message});
                    $('#code').val('');
                    clearInterval(t)
                    $('.get-code').html('<em class="rule"></em>获取验证码');
                    mobFlag=true
                }else{
                    $('#smsSeq').val(data.info)
                }
            },
            error:function(){
                utils.alert({id:'errorCode',title:'错误',content:'发送失败'});
                $('#code').val('');
                clearInterval(t);
                $('.get-code').html('<em class="rule"></em>获取验证码');
                mobFlag=true
            }
        });
    }
})
$("#cashForm").validate({//表单验证
    onkeyup:false,
    ignore: ".ignore",
    rules: {
        cardNo:{
            required:true,
            number:true,
            isBankCard:true
        },
        mobile:{
            required:true,
            isMobile:true,
        },
        smsCode:{
            required:true,
        },
    },
    messages:{
        cardNo:{
            required:'请输入银行卡号',
            number:'请输入正确的银行卡号',
            isBankCard:'请输入正确的银行卡号'
        },
        mobile:{
            required:'请输入银行预留手机号',
            isMobile:'请输入正确手机号'
        },
        smsCode:{
            required:'请输入验证码'
        }
    },
    errorPlacement: function(error, element) {
        error.appendTo(element.parent());

    },
    success:function(value, element, param){
        if(!$(element).siblings().is('.i-success')){
            $(element).parent().append('<span class="i-success iconfont icon-duihao"></span>')
        }
    },
    onfocusout: function( element ) {
		/*1.帮助提示信息移除*/
        $(element).parent().children(".i-success").remove();

        this.element( element );

    },
    submitHandler:function(form){
        if(checkToken()){

            $.ajax({
                type:"post",
                url:webPath + "/bank/web/bindCard/bindCardPlus.do", //绑卡地址
                data:$('#cashForm').serialize(),
                success:function(data){
                    if(!data.status){
                        utils.alert({id:'errorBind',title:'绑卡失败',content:data.message,type:"alert",fnconfirm:utils.refresh});
                    }else{
                        setCookie("bindCardToken","1",360);
                        utils.alert({id:'errorBind',title:'绑卡成功',content:'恭喜您，银行卡绑卡成功',type:"alert",fnconfirm:function(){
                            location.href = webPath+'/bank/web/withdraw/toWithdraw.do'
                        }});
                    }
                },
                error:function(){
                    utils.alert({id:'errorBind',title:'绑卡失败',content:'绑卡失败，请稍后重试',type:"alert",fnconfirm:utils.refresh});
                }
            });
        }
    },
});

$('#sub').click(function(){//提交
    $('#cashForm').submit()
})

$(function(){
    //token过期刷新
    var bindCardToken = getCookie("bindCardToken");
    if(bindCardToken != ""){
        setCookie("bindCardToken","");
        utils.refresh();
    }
})