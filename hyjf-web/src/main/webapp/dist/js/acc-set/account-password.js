$(function(){
	//确认密码验证
	jQuery.validator.addMethod("pwrsaSure", function(value, element) {
		
		//定义密码的id为passowrd，获取密码的val
		var pw=$('#password').val()
		return this.optional(element) || (pw==value);
	}, "确认密码不正确");
	
	//不能与原密码相同
	jQuery.validator.addMethod("oldPw", function(value, element) {
		//定义密码的id为passowrd，获取密码的val
		var old=$('#oldPword').val()
		return this.optional(element) || (old!=value);
	}, "新密码不能与原密码相同");
	
	var validator=$("#setPwForm").validate({
		submitHandler: function(form) {
			var userId = $("#userId").val();
			var oldPassword = $("#oldPassword").val();
			var newPw = $("#newPw").val();
			var pwSure = $("#pwSure").val();
			var tokenCheck = $("#tokenCheck").val();
	    	$.ajax({
	    		url:$(form).attr('action'),
	    		type:'post',
	    		data:{"userId":userId,"oldPassword":oldPassword,"newPw":newPw,"pwSure":pwSure,"tokenCheck":tokenCheck},
	    		success:function(data){
	    			if(data.status){
	    				location.href= webPath + "/user/safe/modifySuccess.do";	
	    			}else if(!data.status){
	    				utils.alert({id:'errorCode',title:'错误',content:data.error});
	    				validator.resetForm();
	    			}
	    		}
	    	});
	    },  
		onkeyup:false,
		rules: {
			oldPword:{
				required:true,
				//ispwd:true,
				remote:{
					url : webPath + "/user/safe/checkOriginaPw.do", //后台处理程序 
	               	type: "post", //数据发送方式
	               	data:{
	               		oldPassword:function() {
	               			setpwd();
	               			return $('#oldPassword').val();
	               		},
	               		userId:function() {
	               			return $('#userId').val();
	               		}
	               	}
				}
			},
			newrsaPw:{
				required:true,
				minlength:8,
				ispassword:true,
				oldPw:true
			},
			pwrsaSure:{
				required:true,
				pwrsaSure:true,
			},
	    },
	    
	    messages:{
	    	oldPword:{
				required:'请输入原密码',
				//ispwd:'请输入正确的密码',
				remote:'原密码不正确'
			},
			newrsaPw:{
				required:'请输入新密码',
				minlength:'8~16位数字、字母或者符号组合',
				ispassword:'必须包含数字、字母、符号至少两种',
				oldPw:'新密码不能与原密码相同'
			},
			pwrsaSure:{
		    	required:'请确认新密码',
		    	pwrsaSure:'两次密码不一致',
		 	}
	    },
		success:function(value, element, param){
			if($(element).siblings('.i-success').length==0){
				$(element).parent().append('<span class="i-success iconfont icon-duihao"></span>')
			}
		},		
		onfocusout: function( element ) {
	        /*1.帮助提示信息移除*/
	        $(element).parent().children(".i-success").remove();
	        this.element( element );
	    }
	});
	$('#password').keyup(function(){
    	var _this=$(this)
    	var val=_this.val()
    	var box=$('.strength-box')
    	var num=0
    	if(val.length>7){
    		if($('#password').valid()){
    			num=score(val)
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
	function setpwd(){
		var pubexponent =$("#pubexponent").val();  
        var pubmodules =$("#pubmodules").val();  
        setMaxDigits(200);    
        var key = new RSAKeyPair(pubexponent, "", pubmodules);   
        var password=$("#password").val();  
        var encrypassword = encryptedString(key, password);
        var surePassword=$("#surePassword").val();  
        var encrysurePassword = encryptedString(key, surePassword);
        var oldPword=$("#oldPword").val();  
        var encryoldPword = encryptedString(key, oldPword);
        $("#newPw").val(encrypassword);
        $("#pwSure").val(encrysurePassword);
        $("#oldPassword").val(encryoldPword);
	}
	

	//提交按钮
	$("#setPwForm .sub").click(function(){
		
		setpwd();
		$("#setPwForm").submit()
	})
})
