<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/open-acount.css"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/validate.css"/>
	<title>银行开户</title>
</head>

	<body class="wap-reg-body">
	<form id="subForm" method="POST" onsubmit="return false;">
		<input id="msgOrderId" type="hidden" name="orderid" value="${logOrderId}" />
		<input id="platform" type="hidden" name="platform" value="${platform}" />
		<input type="hidden" name="userid" id="userid" value="${userId}" />
		<input type="hidden" name="sign" id="sign" value="${sign}" />
		<input type="hidden" name="token" id="token" value="${token}" />
		<div class="wap-reg-div">
				<div class="wap-reg-item">	
					<label for="name">姓名</label>
					<input class="wap-reg-input wap-reg-input-phone wap-reg-phone-first"  name="name" placeholder="请与身份证姓名保持一致" type="text" id="trueName" oninput="if(value.length>10)value=value.slice(0,10)"  data-validate="name" data-describedby="name-description">
				</div>
				<div class="wap-reg-item">	
					<label for="id-card">身份证</label>
					<input class="wap-reg-input wap-reg-input-phone wap-reg-phone-first"  name="id-card" placeholder="仅支持二代身份证" type="text" id="id_card" oninput="if(value.length>18)value=value.slice(0,18)" data-validate="idcard" data-describedby="phone-description">
				</div>
				<div class="wap-reg-item">	
					<label for="bank-card">银行卡</label>
					<input class="wap-reg-input wap-reg-input-phone wap-reg-phone-first" id="isbank" name="bank-card" placeholder="请确保卡号对应身份证" type="tel"  oninput="if(value.length>19)value=value.slice(0,19)" data-conditional="isbank" data-required="true" data-descriptions="bank">
				</div>
				<div class="wap-reg-item">	
					<label for="phone">手机号</label>
					<input class="wap-reg-input wap-reg-input-phone wap-reg-phone-first" value="${phone}" <c:if test="${not empty phone}">readonly="readonly"</c:if> name="phone" placeholder="请输入手机号" type="tel" id="phone" oninput="if(value.length>11)value=value.slice(0,11)" data-validate="phone" data-describedby="phone-description">
				</div>
				<div class="wap-reg-item">	
					<label for="">手机验证码</label>
					<input class="wap-reg-input wap-reg-input-code" placeholder="短息验证码" name="verificationCode" id="msg_code" type="tel" oninput="if(value.length>6)value=value.slice(0,6)"  data-describedby="vercode-description" data-validate="vercode">
					<input class="wap-reg-input wap-reg-input-getcode wap-reg-getcode" value="获取验证码" type="button" >
				</div>
		</div>
		<input class="wap-reg-btn " id="subutton" value="开通银行存管账户" type="submit">
	</form>
	<div class="wap-reg-bottom">
		<p class="tac">点击“提交”按钮，即表示您同意</p>
		<p class="tac"><a href="" class="color-blue hy-jumpH5">《开户协议》</a>&nbsp;<a href="" class="color-blue hy-jumpH5">《用户授权协议》</a></p>
	</div>
	<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/jquery-mvalidate.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
	<script type ="text/javascript">
		document.documentElement.style.fontSize = $(document.documentElement).width() /12.42 + 'px';
		$(window).on( 'resize', function () {
		document.documentElement.style.fontSize = $(document.documentElement).width() /12.42 + 'px';
		});
	</script>
	<script type="text/javascript">
	
	$(function(){
	var userId = $("#userid").val();
	jumpH5("${ctx}/bank/user/bankopen/jxBankService?userId="+userId)
	jumpH5("/hyjf-app/jsp/open/user-authorization-contract.jsp",1)
	//设置底部文字位置，防止输入表单弹出键盘时页面错乱
	var bodyHeight = $("body").height();
	var top = bodyHeight - 24;
	$(".wap-reg-bottom").css("top",top+"px");
		//前端交验完后提交后台ajax
		
  //   	var orderId = $("#orderid").val();
		// var trueName = $("#trueName").val();
		// var idCard = $("#id_card").val();
		// var bankCard = $("#isbank").val();
		// var msgCode = $("#msg_code").val();
		//银行卡Luhn规则
		function bankCardVer(cardnum){
			if(cardnum.length<16 || cardnum.length>19){
				return false;
			}
		    var sum = 0;
		    var digit = 0;
		    var addend = 0;
		    var timesTwo = false;
		    for (var i = cardnum.length - 1; i >= 0; i--)
		    {
		        digit = cardnum[i] - '0';
		        if (timesTwo)
		        {
		            addend = digit * 2;
		            if (addend > 9) {
		                addend -= 9;
		            }
		        }
		        else {
		            addend = digit;
		        }
		        sum += addend;
		        timesTwo = !timesTwo;
		    }
		    var modulus = sum % 10;
		    return modulus == 0;
		}
		
        /*validate start*/
	    $.mvalidateExtend({
	            phone:{
	                required : true,   
	                pattern : /^[1][3,4,5,6,7,8,9][0-9]{9}$/,
	                each:function(){
	                   
	                },
	                descriptions:{
	                    required : '<div class="flow-message">请填写手机号</div>',
	                    pattern : '<div class="flow-message">手机号输入错误，请重新输入</div>',
	                }
	            },
	            name:{
	                required : true, 
	                pattern : /^([\u4e00-\u9fa5]{1,20}|[a-zA-Z\.\s]{1,20})$/,
	                descriptions:{
	                    required : '<div class="flow-message">填写入姓名</div>',
	                    pattern : '<div class="flow-message">姓名输入不正确，请重新输入</div>',
	                }
	            },
	            idcard:{
	                required : true,   
	                pattern : /(^\d{17}([0-9]|X|x)$)/,
	                descriptions:{
	                    required : '<div class="flow-message">请填写身份证</div>',
	                    pattern : '<div class="flow-message">身份证输入不正确，请重新输入</div>',
	                }
	            },
	            vercode:{
                required : true,   
                descriptions:{
                    required : '<div class="flow-message">请填验证码</div>',
                }
            },
        });

        $("#subForm").mvalidate({
            type:1,
            onKeyup:true,
            sendForm:true,
            firstInvalidFocus:false,

            valid:function(event,options){

            	$.ajax({

					url : "${ctx}/bank/user/bankopen/openAccount",
					type : "POST",
					data :"trueName=" + $("#trueName").val() + "&idNo=" + $("#id_card").val() + "&cardNo=" +  $("#isbank").val() +
						"&logOrderId=" + $("#msgOrderId").val() + "&smsCode=" + $("#msg_code").val()+"&userId="+$("#userid").val()
						+"&platform=" + $("#platform").val(),
					success : function(result) {
						if(result.status == 1){//失败
							 // window.location.href = "registerSuccessPageAction?mobile="+$("#mobile").val();
							$.mvalidateTip(result.returnMsg);
						}else{//成功
							window.location.href = "${ctx}/bank/user/bankopen/openSuccess?sign="+$("#sign").val()+"&token="+$("#token").val();
						}
					},
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

            conditional:{
                isbank:function(val,options){
                    return bankCardVer(val);
                }
            },
	        descriptions:{
	            bank:{
	                required : '请输入银行卡号',
	                conditional : '请输入正确的银行卡号',
	            }
            },   
	           
	    })
	})
	/*validate end*/

	/*验证手机号提示*/
	/*function realtimePhone(){
	 	var a = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-3,5-9]{1})|(17[0,1,3,5-8]{1})|(18[0-9]{1}))+\d{8})$/.test($(".wap-reg-phone-first").val());
	 	if(!a){
	 		$.mvalidateTip("请输入正确的手机号");
	 	}else{
	 		var el = document.getElementsByClassName("wap-reg-btn-first") || $(".wap-reg-btn-first");
	 		el[0].parentNode.removeChild(el[0]);
	 		$(".reg-hidden").removeClass("hidden");
	 		$("#subutton").removeClass("hideImportant");
	 	}
	 }
	 $(".wap-reg-btn-first").on("click",realtimePhone)*/
	 function verIsMobile(value) {
		    //验证是否是手机号
		    var length = value.toString().length;
		    return /^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value);
	}
	/*实时提示结束*/
	$(function(){

		var  remainingTime = 60;//倒计时时间
		var timerClock = null;
        var userId = $("#userid").val();
        var phone = $("#phone").val();

	    //发送验证码 校验手机号 手机号校验成功后才能发送手机号
	    var getver = function(){
      		var phoneNum = $("#phone").val();
      		//var checkVer = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-3,5-9]{1})|(17[0,1,3,5-8]{1})|(18[0-9]{1}))+\d{8})$/.test(phoneNum);
      		if(verIsMobile(phoneNum)){
      			$.ajax({
      				url : "${ctx}/bank/user/bankopen/sendCode",
      				type : "POST",
      				data :"userId=" + userId + "&phone=" + phone,
      				success : function(result) {
      					// $.mvalidateTip(result.statusDesc);
      					startTimerClock();
      					if(result.status == 0){
      						$("#msgOrderId").empty();
							$("#msgOrderId").val(result.logOrderId);
							$.mvalidateTip(result.returnMsg);
      					}else{
      						$.mvalidateTip(result.returnMsg);
      					}
      				}, 
      			})
      			
      		}else{
      			$.mvalidateTip("请填写您的真实手机号码！");
      		} 
	            
	    }
	    $(".wap-reg-getcode").on("click",getver);
	    
		  /*开始计时器*/
		function startTimerClock(){
			clearTimeout(timerClock);
			if(remainingTime <= 0){
			    endTimerClock();
			    return;
			}
			$(".wap-reg-getcode").val("剩余" + remainingTime + "秒");
			$(".wap-reg-getcode").innerHTML = remainingTime;
			timerClock = setTimeout(function(){
			startTimerClock();
			remainingTime--;
			}, 1000);
		};
		/*结束计时器*/
		function endTimerClock(){
			clearTimeout(timerClock);
			isTimeing = false;//重置倒计时状态
			$(".wap-reg-getcode").val("重新获取");
			remainingTime = 60;
		};
	});
</script>
</body>