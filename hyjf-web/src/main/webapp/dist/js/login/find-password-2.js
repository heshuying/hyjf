//确认密码验证
jQuery.validator.addMethod("pwSure", function(value, element) {
	//定义密码的id为passowrd，获取密码的val
	var pw=$('#repassword1').val()
	return this.optional(element) || (pw==value);
}, "确认密码不正确");
$("#pwForm").validate({
	submitHandler: function(form) {
		var telnum = $("#telnum").val();
		var code = $("#code").val();
		var password1 = $("#password1").val();
		var password2 = $("#password2").val();
		var tokenCheck = $("#tokenCheck").val();
    	$.ajax({
    		url: webPath + '/user/findpassword/checkPwd.do',
    		type:'post',
    		data:{"telnum":telnum,"code":code,"password1":password1,"password2":password2,"tokenCheck":tokenCheck},
    		success:function(data){
    			if(data.status){
    				location.href=location.href = webPath + "/user/findpassword/successPage.do";	
    			}else if(!data.status){
    				utils.alert({id:'errorCode',title:'错误',content:data.error});
    				validator.resetForm();
    			}
    		}
    	});
    },  
	
	onkeyup:false,
	ignore: ".ignore",
	rules: {
		repassword1:{
			required:true,
			minlength:8,
			ispassword:true
		},
		repassword2:{
			required:true,
			pwSure:true,
		},
    },
    
    messages:{
    	repassword1:{
			required:'请输入登录密码',
			minlength:'8~16位数字、字母或者符号组合',
			ispassword:'必须包含数字、字母、符号至少两种'
		},
		repassword2:{
	    	required:'请输入确认密码',
	    	pwSure:'确认密码不正确',
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
$('#repassword1').keyup(function(){
	var _this=$(this)
	var val=_this.val()
	var box=$('.strength-box')
	var num=0
	if(val.length>7){
		if($('#repassword1').valid()){
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
//提交按钮
$("#pwForm .sub").click(function(){
	rsapassword();
	$("#pwForm").submit()
})

function rsapassword(){
	
	var pubexponent =$("#pubexponent").val();  
    var pubmodules =$("#pubmodules").val();  
    setMaxDigits(200);    
    var key = new RSAKeyPair(pubexponent, "", pubmodules);   
    var password1=$("#repassword1").val();  
    var password2=$("#repassword2").val();  
    var encrypedPwd1 = encryptedString(key, password1);
    var encrypedPwd2 = encryptedString(key, password2);
    $("#password1").val(encrypedPwd1);
    $("#password2").val(encrypedPwd2);
}