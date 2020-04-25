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
	//滚动解锁
	var slider = new SliderUnlock("#slider", {
        duration: 0 
    }, function(){
   		$('#slide-process').text('验证成功');
   		$('#slide-box').append('<span class="lock"></span>')
    	$('#label').unbind()
    	$('.get-code').removeClass('disable')
    	$('#slide-box').find('span.error').remove()
    }, function(){
    	$("#slide-process").width(slider.index)
    });
    slider.init();
        //添加滑动的验证
    jQuery.validator.addMethod("isUnlock", function(value, element) {
    	var val=parseInt(value);
		return this.optional(element) ||(val==1) ;
	}, "请移动滑块进行验证");
    //表单验证
    var validator=$("#telForm").validate({ 
		onkeyup:false,
		ignore: ".ignore",
		rules: {
			newRegPhoneNum:{
				required:true,
				number:true,
				isMobile:true,
				remote:{
					url : '',     //后台处理程序
	               	type: "post",               //数据发送方式
	               	data:{
	               		newRegPhoneNum:function() {
	               			return $('#telnum').val();
	               		}
	               	}
				}
			},
			newRegVerify:{
				required:true,
				rangelength:[4,8],
				remote:{
					url : '',     //后台处理程序
	               	type: "post",               //数据发送方式
	               	data:{
	               		newRegPhoneNum:function() {
	               			return $('#telnum').val();
	               		},
	               		newRegVerify:function() {
	               			return $('#code').val();
	               		},
	               		validCodeType:function() {
	               			return $('#validCodeType').val();
	               		},
	               	},
	               	cache:false
				}
			},
			lockable:{
	    		isUnlock:true,
	    	}
	    },
	    
	    messages:{
	    	newRegPhoneNum:{
				required:'请输入手机号',
				number:'请输入正确的手机号',
				isMobile:'请输入正确的手机号',
				remote:'手机号已存在'
			},
			newRegVerify:{
		    	required:'请输入验证码',
		    	rangelength:'请输入正确的验证码',
		    	remote:'验证码不正确'
		 	},
			lockable:{
	    		isUnlock:'请移动滑块进行验证'
	    	},
	    	newRegPsw:{
	    		required:'请输入密码',
	    		ispwd:'8-16位字母数字组成'
	    	}
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
    $("#telForm .sub").click(function(){
    	$("#telForm").submit()
    })
	//发送验证码相关操作
	$('.get-code').click(function(){
		//$("#registerForm").valid();
		if(mobFlag&&$("#lockable").valid()&&$("#telnum").valid()){
			var t=countdown($('.get-code'),60,'<em class="rule"></em>获取验证码')
			$.ajax({
				type:"post",
				url:webPath + "/user/regist/sendcode.do",
				data:$('#registerForm').serialize(),
				success:function(data){
					if(!data.status){
						utils.alert({id:'errorCode',title:'错误',content:data.error});
						$('#code').val('');
						clearInterval(t)
						$('.get-code').html('<em class="rule"></em>获取验证码');
						mobFlag=true
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
})
