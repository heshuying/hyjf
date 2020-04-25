<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/validate.css"/>
	    <link rel="stylesheet" type="text/css" href="${ctx}/css/page.css?version=221514"/>
	<title>充值验证</title>
	</head>
	<body class="bg_grey">
		<div class="wx-recharge">
              <input type="hidden" name="cardNo" id="cardNo" value="${cardNo}" /> <!-- 开户银行卡号 -->
			  <input type="hidden" name="sign" id="sign" value="${sign}" /> <!-- sigh -->
			  <input type="hidden" name="platform" id="platform"   value="${platform}" /> <!-- 终端类型 -->
			  <input type="hidden" name="isMencry" id="isMencry"   value="1" />
			  <input type="hidden" name="smsSeq" id="smsSeq"   value="" />
				<div class="wx-recharge-form" >
				    <label for="" class="wx-recharge-label">充值金额</label>
				    <input type="number" name="money" id="money" maxlength="7" oninput="if(value.length>7)value=value.slice(0,7)"  class=" wx-login-input wxInvestInput" onkeyup="javascript:this.value.substring(0,1)=='0'?this.value='':this.value=this.value;value=value.replace(/[^\d]/g,'') "   onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" value="${money}"/>
				    <span class="wx-recharge-yuan">元</span>
				</div>
		      	<div class="wx-recharge-form" >
		  		    <label for="" class="wx-recharge-label">银行预留手机号</label>
		            <input type="number" name="mobile" id="mobile" maxlength="11" oninput="if(value.length>11)value=value.slice(0,11)"  class="wx-login-input wxPhone" onkeyup="javascript:this.value.substring(0,1)=='0'?this.value='':this.value=this.value;value=value.replace(/[^\d]/g,'') "   onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" value="${mobile}"/>
		        </div>
		      	<div class="wx-recharge-form" >
		  		    <label for="" class="wx-recharge-label">验证码</label>
		            <input type="number" name="code" id="code" maxlength="6" oninput="if(value.length>6)value=value.slice(0,6)"  class="wx-login-input wxCode" onkeyup="javascript:this.value=this.value;value=value.replace(/[^\d]/g,'') "   onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"/>
		            <input type="button" class="getCode" value="获取验证码" disabled="disabled"/>
		      	</div>
		        <div class="wx-recharge-bottom">
		            <input class="newRegReg submit invest-invaild" id = "agree-btn" type="button" value="确认充值" disabled="disabled"></input>
		  	    </div>
         </div>
	</body>
</html>
<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/js/jquery-mvalidate.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/js/num.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/js/wx-verify.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
 function fomatFloat(src,pos){      
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);             
}   
 $(function(){
	var remainingTime = 60;//初始化倒计时时间
	var timerClock = null;//初始化及时器
	var cardNo = $("#cardNo").val();
    var sign = $('#sign').val();
    var platform = $('#platform').val();
    var isMencry = $('#isMencry').val();
//  开始计时器
	function startTimerClock(){
		clearTimeout(timerClock);
		if(remainingTime <= 0){
			endTimerClock();
			return;
		}
		$(".getCode").val("剩余" + remainingTime + "秒").prop("disabled","disabled").css({"color":"#61748D"});
		$(".getCode").innerHTML = remainingTime;
		timerClock = setTimeout(function(){
			startTimerClock();
			remainingTime--;
		}, 1000);
	};
//  结束计时器
	function endTimerClock(){
		clearTimeout(timerClock);
		isTimeing = false;//重置倒计时状态
		$(".getCode").val("获取验证码").removeAttr("disabled").css({"color":"#00A0E9"});
		remainingTime = 60;
	};
	
	
//  验证手机号，验证成功后发送验证码
	var auth_url="${ctx}/user/bank/recharge/sendCode.do";//获取验证码的url地址
 	var identify = function(){
		$.ajax({
  		    type: "POST",
  		    url: auth_url,
  		    data:{"mobile":$("#mobile").val(),"sign":sign,"platform":platform,"cardNo":cardNo,"isMencry":isMencry},
  		    success : function(msg) {
  		    	   
		        if (msg.status == 0){
                    $.mvalidateTip(msg.statusDesc);
                    startTimerClock();
                    $('#smsSeq').val(msg.smsSeq);
                }else {
                    $.mvalidateTip(msg.statusDesc);
                }
			}, 
		 });
	}
 	
//  用户点击获取验证码	
 	$('.getCode').on("click", identify);
 	
//  用户提交时，验证充值金额、手机号、验证码
	$("#agree-btn").on('click', function () {
            $.ajax({
                type: "POST",
                url: "${ctx}/user/bank/recharge/rechargeOnline.do",
                data:{"mobile":$("#mobile").val(),"money":$("#money").val(),"smsCode":$("#code").val(),"smsSeq":$('#smsSeq').val(),"sign":sign,"platform":platform,"cardNo":cardNo,"isMencry":isMencry},
                success: function (msg) {
                	if(msg.status  == 0){
                		 window.location.href = msg.rechargeUrl;//提交成功后跳转url
  	                  
  	                }else{
  	                	$.mvalidateTip(msg.statusDesc);  //提示
  	                }
                	
                }
            });
     })
}); 
</script>
<script type="text/javascript">
var agent = navigator.userAgent.toLowerCase();        //检测是否是ios
var iLastTouch = null;                                //缓存上一次tap的时间
if (agent.indexOf('iphone') >= 0 || agent.indexOf('ipad') >= 0)
{
    document.body.addEventListener('touchend', function(event)
    {
        var iNow = new Date().getTime();
        iLastTouch = iLastTouch || iNow + 1 /** 第一次时将iLastTouch设为当前时间+1 */ ;
        var delta = iNow - iLastTouch;
        if (delta < 500 && delta > 0)
        {
            event.preventDefault();
            return false;
        }
        iLastTouch = iNow;
    }, false);
}
</script>