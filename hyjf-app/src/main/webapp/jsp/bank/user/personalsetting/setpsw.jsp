<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/validate.css"/>
		<title>设置密码</title>
	</head>
	<body class="bg_grey">
		<div class="wx-register">
			<form   method="get" id="form1" onsubmit="return false;">
				
				<div class="wx-register-con">
				    <div class="wx-register-txt">
					    <input type="tel"  maxlength="11" value="${mobile}" placeholder="手机号" data-validate="phone" data-describedby="phone-description" readonly="true"/>
					    <input type="hidden" name="mobile" id="mobile" value="${mobile}">
					    <input type="hidden" name="sign" id="sign" value="${sign}" />
				    </div>
				    <div class="wx-register-txt">
					    <input type="number" name="code" id="code" maxlength="6" placeholder="短信校验码" oninput="if(value.length>6)value=value.slice(0,6)" data-descriptions="phoneyzm-description" data-validate="phoneyzm"/>
				        <span class="wx-register-yz text_blue" id="getver J-register-getcode" data-sended="false">发送验证码</span>
				    </div>
			   </div>		
				<input type="submit" class="newRegReg wx-register-btn" id="checkMsg"  value="确定" />	
			</form>
		</div>
		<div class="clickContact">
			<p>
				若您收不到短信或手机号码已停止使用，</br>
				可点此<a href="#">联系客服</a>
			</p>
		</div>
	</body>
        <script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/jquery-mvalidate.js" type="text/javascript" charset="utf-8"></script>
        <script type="text/javascript">
	$(function(){
		$.mvalidateExtend({
			//验证手机号 密码  校验码
			phoneyzm:{
				required : true,
				each:function(){

				},
				descriptions:{
					required : '<div class="field-invalidmsg">请填写验证码</div>',
				}
			},
		});
		
	    $("#checkMsg").on("click",function(){
	    	$.ajax({
	            type: "get",
	            url: "/hyjf-app/bank/user/transpassword/transPasswordCheckCode",
	            data:{"mobile":$("#mobile").val(),"sign":$("#sign").val(),"code":$("#code").val()},
	            success: function(msg) {
	                //msg = $.parseJSON(msg);
	                 if(msg.status != '1'){
	                  $.mvalidateTip(msg.message);  //提示
	                }else{
	                    window.location.href = msg.url;//提交成功后跳转url
	                }
	            }
 
        	});
	    })
          /* $("#form1").mvalidate({
            type:1,
            onKeyup:true,
            sendForm:false,
            firstInvalidFocus:false,
            descriptions: {
                phoneyzm: {
                    required: '请输入手机验证码',
                }
            },
            valid:function(event,options){
                //点击提交按钮时,表单通过验证触发函数
              $.ajax({
	            type: "get",
	            url: "/hyjf-app/bank/user/transpassword/transPasswordCheckCode",
	            data:{"mobile":$("#mobile").val(),"sign":$("#sign").val(),"code":$("#code").val()},
	            success: function(msg) {
	                //msg = $.parseJSON(msg);
	                 if(msg.status != '1'){
	                  $.mvalidateTip(msg.message);  //提示
	                }else{
	                    window.location.href = msg.url;//提交成功后跳转url
	                }
	            }
 
        	});
            },
            invalid:function(event, status, options){
                //点击提交按钮时,表单未通过验证触发函数
            },
            eachField:function(event,status,options){
                //点击提交按钮时,表单每个输入域触发这个函数 this 执向当前表单输入域，是jquery对象
            },
            eachValidField:function(val){},
            eachInvalidField:function(event, status, options){},
        })   */                  
		//获取注册码的url地址
		var auth_url="/hyjf-app/bank/user/transpassword/transPasswordSendCode";
		var  remainingTime = 59;//倒计时时间
		var timerClock = null;
		//发送验证码 校验手机号 手机号校验成功后才能发送手机号
	      var getver = function(){
			var sended = $("#getver").prop("data-sended");
			if(!sended){
				$.ajax({
		    		   type: "get",
		    		   url: auth_url,
		    		    data: {"mobile":$("#mobile").val(),"sign":$("#sign").val()},
		    		   success: function(msg){
		    		   	//var msg = $.parseJSON(msg);//json
		    				if(msg.status != '1'){
		    			    	$.mvalidateTip(msg.message);
		    				}else{
		    			   		$.mvalidateTip(msg.message);
		    			   		startTimerClock();
		    			   }
		    		   }
		    		});
			}
	      			
	      }
           
          
	      $(".wx-register-yz").on("click",getver);
		//倒计时
		//开始计时器
		function startTimerClock(){
			clearTimeout(timerClock);
			if(remainingTime <= 0){
				endTimerClock();
				return;
			}
			$("#getver").prop("data-sended","true")
			$('.text_blue').css({"color":"#e5e5e5"});
			$(".wx-register-yz").text("剩余" + remainingTime + "秒");
			$(".wx-register-yz").innerHTML = remainingTime;
			timerClock = setTimeout(function(){
				startTimerClock();
				remainingTime--;
			}, 1000);
		};
		//结束计时器
		function endTimerClock(){
			clearTimeout(timerClock);
			isTimeing = false;//重置倒计时状态
			$(".wx-register-yz").text("点击重新获取");
			remainingTime = 60;
			$('.text_blue').css({"color":"#49a7f6"});
			$("#getver").prop("data-sended","false")
		};

		var marginTop = $(window).height()-$(".wx-top").height()-400;
		$(".wx-register-ban").css("margin-top",marginTop+"px")
	});
</script>
</html>
