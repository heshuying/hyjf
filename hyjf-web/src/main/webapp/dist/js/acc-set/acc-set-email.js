$(function(){
	function jumpEmail2(){
		$(".acc-set-email-1").hide();
		$(".acc-set-email-2").show();
	}
	
	//表单内验证
	var validator=$("#email-form").validate({
		submitHandler: function(form) {
	    	$.ajax({
	    		url: webPath + "/user/safe/sendEmail.do",
	    		type:'post',
	    		data:{
	    			email : $("#email").val(),
	    			tokenCheck : $("#tokenCheck").val()
	    		},
	    		success:function(data){
	    			if(data.status){
	    				//邮箱激活地址
	    				$("#toEmailHtml").attr("href", "http://mail." + $("#email").val().substring($("#email").val().indexOf("@") + 1));
	    				$("#receiveEmail").html($("#email").val());
	    				jumpEmail2();
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
			email:{
				required:true,
				email:true	
			},
	   },
	    messages:{
	    	email:{
				required:'请输入邮箱',
				email:'请输入有效的邮箱'
			},	
	    },
	    success:function(value, element, param){
			if($(element).siblings('.i-success').length==0){
				$(element).parent().append('<span class="i-success iconfont icon-duihao"></span>')
			}
		}
	});
	
	
	$('.email-submit-btn').click(function(){
		if(checkToken()){
			$('#email-form').submit();
		}
	});
	
});
