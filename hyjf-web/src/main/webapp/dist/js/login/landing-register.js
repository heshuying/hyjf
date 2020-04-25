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
    var validator=$("#registerForm").validate({
		submitHandler: function(form) {
			var tokenCheck = $("#tokenCheck").val();
			var tokenGrant = $("#tokenGrant").val();
			var utm_id = $("#utm_id").val();
			var utm_source = $("#utm_source").val();
			var newRegPsw = $("#newRegPsw").val();
			var newRegPhoneNum = $("#telnum").val();
			var lockable = $("#lockable").val();
			var newRegVerify = $("#code").val();
			var newRegReferree = $("[name=newRegReferree]").val();
			// 神策预置属性
            var presetProps = sa.getPresetProperties();
            presetProps._distinct_id = sa.store.getFirstId() || sa.store.getDistinctId();
            presetProps = JSON.stringify(presetProps);
	    	$.ajax({
	    		url:$(form).attr('action'),
	    		type:'post',
	    		data:{"tokenCheck":tokenCheck,"tokenGrant":tokenGrant,"utm_id":utm_id,"utm_source":utm_source,"newRegPsw":newRegPsw,"newRegPhoneNum":newRegPhoneNum,
	    			"lockable":lockable,"newRegVerify":newRegVerify,"newRegReferree":newRegReferree,"presetProps":presetProps},
	    		success:function(data){
	    			if(data.status){
	    				// 神策关联登录用户
						sa && sa.login(data.userid);
						/*
						  注册
						  sign_up
						  @params entrance  入口  String
						  @params success 是否成功 Boolean
						  @params error_message 错误信息 
						*/
						sa && sa.track('sign_up',{
						  entrance: document.referrer,
						  success: true,
						  error_message: ""
						})
                        setTimeout(function(){
	    					location.href=location.href = webPath + "/user/regist/to_regist_success.do?userid=" + data.userid+"&activity68="+ data.activity68+"&couponSendCount="+ data.couponSendCount;
                    	},300)
	    			}else if(!data.status){
	    				/*
						  注册
						  sign_up
						  @params entrance  入口  String
						  @params success 是否成功 Boolean
						  @params error_message 错误信息 
						*/
						sa && sa.track('sign_up',{
						  entrance: document.referrer,
						  success: false,
						  error_message: data.error
						})
	    				utils.alert({id:'errorCode',title:'错误',content:data.error});
	    				validator.resetForm();
	    				
	    			}
	    		}
	    	});
	    },  
		onkeyup:false,
		ignore: ".ignore",
		rules: {
			newRegPhoneNum:{
				required:true,
				number:true,
				isMobile:true,
				remote:{
					url : webPath + "/user/regist/checkPhone.do",     //后台处理程序
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
				maxlength:6,
				remote:{
					url : webPath + "/user/regist/checkcode.do",     //后台处理程序
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
	    	},
	    	password:{
	    		required:true,
	    		minlength:8,
	    		ispassword:true
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
		    	maxlength:"请输入正确的验证码",
		    	remote:'验证码不正确'
		 	},
			lockable:{
	    		isUnlock:'请移动滑块进行验证'
	    	},
	    	password:{
	    		required:'请输入密码',
	    		minlength:'8-16位数字、字母或者符号组合',
	    		ispassword:'必须包含数字、字母、符号至少两种'
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
    $("#registerForm .sub").click(function(){
    	
    	var pubexponent =$("#pubexponent").val();  
        var pubmodules =$("#pubmodules").val();  
        setMaxDigits(200);    
        var key = new RSAKeyPair(pubexponent, "", pubmodules);   
        var password=$("#password").val();  
        var encrypedPwd = encryptedString(key, password);
        $("#newRegPsw").val(encrypedPwd);
    	
    	if(!$(this).hasClass('disable')){
    		$("#registerForm").submit()
    	}
    })
    $('#password').keyup(function(){
    	var _this=$(this)
    	var val=_this.val()
    	var box=$('.strength-box')
    	var num=0
    	if(val.length>7){
    		if($('#password').valid()){
    			num=score(val)
    			console.log(num)
    			if(num>=75){
    				$('.strength-box div').removeClass('light')
    				$('.strength-box div').addClass('light')
    				$('.strength-box span').html('强')
    			}else if(num>=40){
    				$('.strength-box div').removeClass('light')
    				$('.strength-box div').eq(0).addClass('light')
    				$('.strength-box div').eq(1).addClass('light')
    				$('.strength-box span').html('中')
    			}else{
    				$('.strength-box div').removeClass('light')
    				$('.strength-box div').eq(0).addClass('light')
    				$('.strength-box span').html('弱')
    			}
    			
    		}else{
    			$('#password').parent().children(".i-success").remove();
    			$('.strength-box div').removeClass('light')
    			$('.strength-box span').html('')
    		}
    		box.show()
    	}else{
    		box.hide()
    	}
    })
    function score(s){
    	var num=0
    	if(s.length<10){
    		num+=5
    	}else if(s.length>=10&&s.length<=12){
    		num+=10
    	}else{
    		num+=25
    	}
    	if(s.match(/[A-Za-z]/g)==null){
    		num+=0
    	}else if(s.match(/[A-Z]/g)==null){
    		num+=10
    	}else if(s.match(/[a-z]/g)==null){
    		num+=10
    	}else if(/[A-Z]/.test(s)&&/[a-z]/.test(s)){
    		num+=20
    	}
    	if(s.match(/\d/g)==null){
    		num+=0
    	}else if(s.match(/\d/g).length==1){
    		console.log('ddd')
    		num+=10
    	}else if(s.match(/\d/g).length>1){
    		num+=20
    	}
    	if(s.match(/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/g)==null){
    		num+=0
    	}else if(s.match(/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/g).length==1){
    		num+=10
    	}else if(s.match(/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/g).length>1){
    		num+=25
    	}
    	if(/\d/.test(s)&&/[a-z]/.test(s)&&/[A-Z]/.test(s)&&/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/.test(s)){
    		num+=5
    	}else if(/\d/.test(s)&&/[a-zA-Z]/.test(s)&&/[\`\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\{\}\|\[\]\\\;\'\:\"\,\.\/\<\>\?]/.test(s)){
    		num+=3
    	}else if(/\d/.test(s)&&/[a-zA-Z]/.test(s)){
    		num+=2
    	}
    	return num
    }
	//发送验证码相关操作,判断  原好用  mobFlag&&$('#lockable').val()==1&&$('#telnum').hasClass('valid')
	$('.get-code').click(function(){
		//$("#registerForm").valid();
		if(mobFlag&&$("#lockable").valid()&&$("#telnum").valid()){
			var t=countdown($('.get-code'),60,'<em class="rule"></em>获取验证码');
			var validCodeType = $("#validCodeType").val();
			var newRegPhoneNum = $("#telnum").val();
			$.ajax({
				type:"post",
				url:webPath + "/user/regist/sendcode.do",
				data:{"validCodeType":validCodeType,"newRegPhoneNum":newRegPhoneNum},
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
//checkout选择框自定义
$(".term-checkbox").click(function () {
    var _this = $(this);
    if(_this.hasClass('checked')){
    	_this.removeClass("checked");
    	$("#registerForm .sub").addClass('disable')
	}else{
		_this.addClass("checked");
		$("#registerForm .sub").removeClass('disable')
	}
});
//协议的显示隐藏
$("#registerForm .read-protocol").hover(function() {
    $(".agreement").stop().fadeIn(300);
}, function() {
    $(".agreement").fadeOut(300);
});

//密码的明文暗文
$('.yanjing').click(function(){
	if($(this).hasClass('icon-yanjing1')){
		$(this).removeClass('icon-yanjing1').addClass('icon-yanjing').siblings('input').attr('type','text')
	}else if($(this).hasClass('icon-yanjing')){
		$(this).removeClass('icon-yanjing').addClass('icon-yanjing1').siblings('input').attr('type','password')
	}
})

 