$(function(){
	//验证码倒计时
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
	//发送验证码
	$('#telForm1 .get-code').click(function(){
		var that=this
		if(mobFlag){
			var t=countdown($(that),60,'<em class="rule"></em>重新获取')
			$.ajax({
				type:"post",
				url:webPath+"/bank/user/autoplus/sendcode.do?",
				data:$(that).parents('form').serialize(),
				success:function(data){
					if(data.status==false){
						utils.alert({id:'errorCode',content:data.message});
						clearInterval(t)
						$(that).html('<em class="rule"></em>重新获取');
						mobFlag=true
					}else{
						$("#lastSrvAuthCode").val(data.message);
					}
				},
				error:function(){
					utils.alert({id:'errorCode',content:'发送失败'});
					clearInterval(t);
					$(that).html('<em class="rule"></em>重新获取');
					mobFlag=true
				}
			});
		}
	})
	
//第一个表单验证
 	$("#telForm1").validate({
		onkeyup:false,
		ignore: ".ignore",
		rules: {
			smsCode:{
				required:true,
				rangelength:[6,6],
			},
	    },
	    
	    messages:{
	    	smsCode:{
		    	required:'请输入验证码',
		    	rangelength:'请输入正确的验证码',
		    },
	    },
	    //更改错误信息加入的位置
	    errorPlacement: function(error, element) {
	    	if(element.parent().is('#slideUnlock')){
	    		error.appendTo(element.parents('#slide-box'))
	    	}else{
	    		error.appendTo(element.parent());  
	    	}
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
	
	    }
	});
	//提交按钮
	$("#telForm1 .sub").click(function(){
		if($("#lastSrvAuthCode").val()==''){
			utils.alert({id:'errorCode',content:'请先发送短信验证码'});
		}else{
			$("#telForm1").submit();
		}
    	
    })
})