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
	
	
//第一个表单验证(Form1 表单submit事件发生，到这)
	var validFlag=false;
 	$("#telForm1").validate({
    	submitHandler:function(form){
			$(form).hide();
			$('#telForm2').show();//显示  Form2(原本隐藏) 表单
			mobFlag=true;
			validFlag=true;
		},
		onkeyup:false,
		ignore: ".ignore",
		rules: {
			code:{
				required:true,
				rangelength:[4,8],
				remote:{
					url : webPath + "/bank/user/transpassword/checkCode.do", //去后台 短信验证码 校验(校验发送过来的验证码和手机收到填入的验证码是否一致)的方法     
	               	type: "post", //数据发送方式
	               	data:{
	               		mobile:function() {
	               			return $('#telnum1').val();
	               		},
	               		code:function() {
	               			return $('#telForm1 .code').val();
	               		}
	               	},
	               	cache:false
				}
			},
	    },
	    
	    messages:{
			code:{
		    	required:'请输入验证码',
		    	rangelength:'请输入正确的验证码',
		    	remote:'验证码不正确'
		    },
	    },
	    //更改错误信息加入的位置
	    errorPlacement: function(error, element) {
	    	$(element).parent().children(".i-success").remove();
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
 	
 	
 	var checkOne=true
	//验证表单2
	var validator=$("#telForm2").validate({
		submitHandler:function(form){
			if(validFlag && checkOne){
				checkOne=false
				var modify_url = "";
				var is_open_account = $("#is_open_account").val();
				if(is_open_account==0){
					modify_url = "/bank/user/transpassword/platMobileModify.do";
				}else if(is_open_account==1){
					modify_url = "/bank/user/transpassword/mobileModify.do";
				}
				$.ajax({
					url: webPath + modify_url,//去后台 mobileModify 修改手机号的方法
		    		data:{
		    			newMobile : $("#telnum2").val(),
		    			srvAuthCode :$("#srvAuthCode").val(),
		    			smsCode : $("#smsCode").val(),
		    			tokenCheck : $("#tokenCheck").val(),
		    			mobile : $("#telnum2").val(),
		    			code : $("#smsCode").val()
		    		},
		    		success:function(data){
		    			if(data.status){
		    				location.href= webPath + "/bank/user/transpassword/successMobile.do";//去后台表单验证成功后对应的方法，并去新画面
		    			}else if(!data.status){
		    				checkOne=true
		    				utils.alert({id:'errorCode',content:data.message});
		    				validator.resetForm();
		    			}
		    		}
		    	});
			}
		},
		onkeyup:false,
		ignore: ".ignore",
		rules: {
			mobile:{
				required:true,
				number:true,
				isMobile:true,
				remote:{
					url : webPath + "/bank/user/transpassword/checkPhone.do",//去后台关于手机号(新输入)验证的方法
	               	type: "post",               //数据发送方式
	                cache:false,
	                async:false,
	               	data:{
	               		mobile:function() {
	               			return $('#telnum2').val();
	               		}
	               	}
				}
			},
			code:{
				required:true,
				rangelength:[4,8],
			},
	    },
	    
	    messages:{
	    	mobile:{
				required:'请输入手机号',
				number:'请输入正确的手机号',
				isMobile:'请输入正确的手机号',
				remote:'手机号已存在'
			},
			code:{
		    	required:'请输入验证码',
		    	rangelength:'请输入正确的验证码',
		    },
	    },
	    //更改错误信息加入的位置
	    errorPlacement: function(error, element) {
	    	element.parent().children(".i-success").remove();
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
    $("#telForm1 .sub").click(function(){ /*Form1点击下一步，去  JS里的 第一个表单验证部分*/
    	$("#telForm1").submit()
    })
    
    $("#telForm2 .sub").click(function(){/*Form2点击下一步，去  JS里的 第二个表单验证部分*/
    	$("#telForm2").submit()
    })
    
    
    
	//发送验证码相关操作
	$('#telForm1 .get-code').click(function(){
		//$("#registerForm").valid();
		var that=this /*that是指 '#telForm1 .get-code'*/
		if(mobFlag){
			var t=countdown($(that),60,'<em class="rule"></em>获取验证码')
			
			$.ajax({
				type:"post",
				url:webPath + "/bank/user/transpassword/sendCode.do",/*后台发送一般验证码的方法，已验证*/
				data:$(that).parents('form').serialize(),
				success:function(data){
					if(!data.status){
						utils.alert({id:'errorCode',content:data.error});
						$(that).siblings('input').val('');
						clearInterval(t)
						$(that).html('<em class="rule"></em>获取验证码');
						mobFlag=true
					}
				},
				error:function(){
					utils.alert({id:'errorCode',content:'发送失败'});
					$(that).siblings('input').val('');
					clearInterval(t);
					$(that).html('<em class="rule"></em>获取验证码');
					mobFlag=true
				}
			});
		}
	})
	
	$('#telForm2 .get-code').click(function(){
		var send_code_url = "";
		var is_open_account = $("#is_open_account").val();
		if(is_open_account==0){
			send_code_url = "/bank/user/transpassword/sendCode.do";
		}else if(is_open_account==1){
			send_code_url = "/bank/user/transpassword/sendPlusCode.do";
		}
		var that=this
		if(mobFlag && validFlag && $('#telnum2').valid()){
			var t=countdown($(that),60,'<em class="rule"></em>获取验证码')
			$.ajax({
				type:"post",
				url:webPath + send_code_url,/*后台发送加强（江西银行）验证码的方法，已验证*/
				cache:false,
                async:false,//解决提示重叠
				data:{
					mobile : $("#telnum2").val()
				},
				success:function(data){
					if(!data.status){
						utils.alert({id:'errorCode',content:data.message});
						$(that).siblings('input').val('');
						clearInterval(t)
						$(that).html('<em class="rule"></em>获取验证码');
						mobFlag=true
					}else if(data.status){
						$("#srvAuthCode").val(data.info);
					}
				},
				error:function(){
					utils.alert({id:'errorCode',content:'发送失败'});
					$(that).siblings('input').val('');
					clearInterval(t);
					$(that).html('<em class="rule"></em>获取验证码');
					mobFlag=true
				}
			});
		}
	})
	
})