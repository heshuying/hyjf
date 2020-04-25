$(function(){
	customForm.init();
	$('#contactForm').validate({
		submitHandler: function(form) {
	    	$.ajax({
	    		url: webPath + "/user/safe/contactSetUpdate.do",
	    		type:'post',
	    		data:{
	    			relationId : $("#relationId").val(),
	    			rlName : $("#rlName").val(),
	    			rlPhone : $("#rlPhone").val()
	    		},
	    		success:function(data){
	    			if(data.status){
	    				location.href= webPath + "/user/safe/contactSetSuccess.do";//去后台表单验证成功后对应的方法，并去新画面
	    			}else if(!data.status){
	    				utils.alert({id:'errorCode',content:data.error});
	    				validator.resetForm();
	    			}
	    		}
	    	});
	    },  
		onkeyup:false,
		ignore: ".ignore",
		rules: {
			relationId:{
				required:true
			},
			rlName:{
				required:true
			},
			rlPhone:{
				required:true,
				isMobile:true
			}
	    },
	    
	    messages:{
	    	relationId:{
				required:'请选择关系'
			},
	    	rlName:{
				required:'请输入紧急联系人姓名',
			},
			rlPhone:{
				required:'请输入紧急联系人手机号',
				isMobile:'请输入正确的手机号'
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
	$("#contactForm .sub").click(function(){
		$("#contactForm").submit();
	})
	
	$("#relationUl li").click(function(){
		var relationId = $(this).attr("value");
		$("#relationId").val(relationId);
	});
	$('#relationUl').on('click','li',function(){
		$('.new-form-input').removeClass('color-999')
	})
})