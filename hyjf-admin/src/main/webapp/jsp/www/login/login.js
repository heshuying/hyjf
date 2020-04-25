$(document).ready(function() {
    $("#imgVerify").attr("src","/hyjf-admin/randomValidateCode");
	if($.cookie("hyjf_remember")) {
		$("#username").val($.cookie("hyjf_username"));
		$("#password").val($.cookie("hyjf_password"));
		$.cookie("hyjf_remember") == "true" ? $("#remember").prop("checked", true) : $("#remember").prop("checked", false);
	}

	$(".btn-Login").click(function(event) {
		if($("#remember").prop("checked")) {
			$.cookie("hyjf_username", $("#username").val());
			$.cookie("hyjf_password", $("#password").val());
			$.cookie("hyjf_remember", true);
		} else {
			$.cookie('hyjf_username', '', { expires: -1 }); 
			$.cookie('hyjf_password', '', { expires: -1 }); 
			$.cookie('hyjf_remember', '', { expires: -1 }); 
		}
		if(event){
			//清除原有的提示信息
  	   		$(".fa-user1").empty();
  	   		$(".fa-lock1").empty();
			var username = $("#username").val().trim();
			var password = $("#password").val().trim();
            var validateCode = $("#validateCode").val().trim();
			var param = {"username":username,"password":password,"validateCode":validateCode};
			var ok = true;
			if(username==""){
				$(".fa-user1").html("用户名不能为空");
		          ok = false;
		      }
		     if(password==""){
		    	  $(".fa-lock1").html("密码不能为空");
		          ok = false;
		     }
		     //此代码为配合测试进行压力测试修改，无需合并主干 update by limeng
            if(validateCode==""){
               $(".fa-validateCode1").html("验证码不能为空");
                ok = false;
            }
		     if(ok){
		    	 $.ajax({ 
		    		 url: "/hyjf-admin/login/checkPasswordIsOk",
		    		 type: "POST",
		    		 async : true,
		    		 data : JSON.stringify(param),
//		    		 data : {"username":username,"password":password},
		    		 dateType : "json",
		    		 contentType : "application/json",
		    		 success:function(data){
		    			 if (data.status == "0") {
		    				 $("#mainForm").submit();
						 } else if(data.status == "1") {
							$(".fa-user1").html("用户名或密码错误");
						 } else if(data.status == "2"){
							$(".fa-user1").html("用户名或密码错误");
						 } else if(data.status == "3"){
							$(".fa-user1").html("用户异常");
						 } else if(data.status == "4"){
                             $(".fa-validateCode1").html("验证码为空");
                             $("#imgVerify").click();
                         } else if(data.status == "5"){
                             $(".fa-validateCode1").html("验证码错误");
                             $("#imgVerify").click();
                         }else if(data.status == "6"){
                        	 $(".fa-user1").html("您的登录失败次数超限，请"+data.retTime+"之后重试");
							/*$(".fa-user1").html("密码错误次数已达上限，请明日再试");*/
						 }
                         else if(data.status == "7"){
 							$(".fa-user1").html("登录失败，您的登录机会还剩3次");
 						 }
                         else if(data.status == "8"){
 							$(".fa-user1").html("登录失败，您的登录机会还剩2次");
 						 }
                         else if(data.status == "9"){
 							$(".fa-user1").html("登录失败，您的登录机会还剩1次");
 						 }

					 },
		    		 error:function(XMLHttpRequest, textStatus, errorThrown){
		    			 alert("系统异常,请稍后重试!");
		    		 }
		    	 });
		     }
					
		}
	});
});