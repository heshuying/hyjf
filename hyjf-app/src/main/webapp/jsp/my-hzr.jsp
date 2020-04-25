<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>持有详情</title>
	</head>
	<body class="bg_grey">
		<div class="activeOutdatePop activeOutdatePopHzr"></div>
		<div class="specialFont  cal-main">
				<section class="bg_white my-hzr-header">
					<div class="my-hzr-header-div tac">
						<p class="font13">HXD0554244525</p>
						<p class="font19 HZRprice format-num">14681.83</p>
						<span>债权本金(元)</span>
               		</div>
               		<span></span>
               		<div class="my-hzr-header-div tac">
						<p class="font13 color898989">持有<i class="hyjf-color">&nbsp;50&nbsp;</i>天&nbsp;&nbsp;剩余<i class="hyjf-color">&nbsp;41&nbsp;</i>天</p>
						<p class="font19">14.0<i class="font12">%</i></p>
						<span>历史年回报率</span>
               		</div>
				</section>
				<section class="bg_white tac my-hzr-mid">
					<p class="tac hyjf-color">转让率</p>
					<p class="tac hyjf-color font19 my-hzr-percent"><span>0.2</span><i class="font12">%</i></p>
					<img src="${ctx}/img/my-hzr-down.png" class="my-hzr-down"/>
				</section>
				<section class="my-hzr-slider bg_white">
					<div class="my-hzr-percent-slider">
						<div class="my-hzr-percent-slider-item">
						</div>
						<div class="my-hzr-percent-slider-item">
							<span>0</span>
						</div>
						<div class="my-hzr-percent-slider-item">
							<span>1.0</span>
						</div>
						<div class="my-hzr-percent-slider-item">
							<span>2.0</span>
						</div>
						<div class="clearBoth"></div>
					</div>
				</section>
				<section class="bg_white my-hzr-item font13 ">
					<div>
						<span>转让价格</span>
						<span class="HZRrealprice">11100.00</span>
					</div>
					<p class="color898989 font12">转让价格=本金金额*(1-折让率)</p>
					<div>
						<span>预估持有期收益<img src="${ctx}/img/icon-question.png" alt="" class="question-icon"/></span>
						<span class="HZRincome format-num">14681.83</span>
					</div>
					<div>
						<span>预估服务费(0.8%)</span>
						<span class="HZRservice">50.00</span>
					</div>
					<div class="my-hzr-header-div-last">
						<span>预估到账金额<img src="${ctx}/img/icon-question.png" alt="" class="question-icon"/></span>
						<span class="font19 hyjf-color lastRealPrice">120.00</span>
					</div>
				</section>
				<section class="bg_white font13 my-hzr-item my-hzr-message">
					<input type="tel" name="" id="" value="" placeholder="请输入短信验证码" class="my-hzr-input" maxlength="6"/>
					<span class="process_line btn_bg_color my-hzr-getVer"><a href="#" >获取验证码</a></span>
				</section>
				<div class="process_line btn_bg_color my-hzr-btn"><a href="#" >确认转让</a></div>
		</div>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js" ></script>
   <script type="text/javascript">
   $(function(){
   	var wid = $(window).width();
    Number.prototype.toFixed2 = function() {
		return parseFloat(this.toString().replace(/(\.\d{2})\d+$/, "$1"));
	}
   	//设置最左最右 使滑块不超过最大最小值
   	var w = 488+wid+3;
   	$(".my-hzr-percent-slider").width(w)
   	$(".my-hzr-percent-slider-item").eq(0).width(wid/2);
   	$(".my-hzr-percent-slider-item").eq(3).width(wid/2)
   	//是滑块居中
   	//var toLeftInit = (976-wid)/2;
   	var toLeftInit = (w-wid)/2;
   	$(".my-hzr-slider").scrollLeft(0);
   	var a = (484-wid)/2;
   	var l = toLeftInit-a;
   	var left = l-wid/2-4;
   	var end = left+488;
   $(".my-hzr-slider").scroll(function(){
   		var toLeft = ($(".my-hzr-slider").scrollLeft()-left)/244;
   		if(toLeft<0.2){
   			toLeft = 0.2
   		}else if(toLeft>2){
   			toLeft = 2
   		}
   		$(".my-hzr-percent span").text(toLeft.toFixed(1));
   		var HZRprice = $(".HZRprice").text().replace(/,/g,'');
   		var HZRrealprice = (HZRprice*(1-toLeft.toFixed(1)/100)).toFixed2();
   		$(".HZRrealprice").text(formatNum(HZRrealprice));//转让价格
   		var s = +HZRrealprice;
   		var f = Number($(".HZRincome").text().replace(/,/g,''));
   		var service = (s+f)*0.008;
		$(".HZRservice").text(formatNum(service.toFixed2())); //服务费  	
		var lastRealPrice = s+Number($(".HZRincome").text().replace(/,/g,''))-service.toFixed2()//预估到账金额
		$(".lastRealPrice").text(formatNum(lastRealPrice.toFixed2(0)));//预估到账金额
   })
   })
   $(".format-num").each(function(){
   	var _self = $(this);
   	_self.text(formatNum(_self.text()))
   })
   
   	function formatNum(strNum) {
		//return strNum;
		if (strNum.length <= 3) {
			return strNum;
		}
		if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)) {
			return strNum;
		}
		var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})(,|$)");
		while (re.test(b)) {
			b = b.replace(re, "$1,$2$3");
		}
		return a + "" + b + "" + c;
	}
   	//点击问号
   	var activeOutdatePop = $(".activeOutdatePop")
   	$(".question-icon").eq(0).click(function(){
   		activeOutdatePop.text("持有本金在本次计息期间的所得收益").show().delay(3000).fadeOut();
   	})
	$(".question-icon").eq(1).click(function(){
   		activeOutdatePop.text("预估到账金额=转让价格+预估持有期收益-服务费；具体金额以实际转让为准。").show().delay(3000).fadeOut();
   	})
  	//倒计时
	  //开始计时器
	 $(".my-hzr-getVer a").click(startTimerClock);
    var  remainingTime = 60;//倒计时时间
	var timerClock = null;
	function startTimerClock(){
		clearTimeout(timerClock);
		if(remainingTime <= 0){
			endTimerClock();
			return;
		}
		$('.my-hzr-getVer').css({"background":"#e5e5e5"});
		$(".my-hzr-getVer a").css({"font-size":"13px"})
		$(".my-hzr-getVer a").text("剩余" + remainingTime + "秒");
		$(".my-hzr-getVer a").innerHTML = remainingTime;
		timerClock = setTimeout(function(){
			startTimerClock();
			remainingTime--;
		}, 1000);
	};
	//结束计时器
	function endTimerClock(){
		clearTimeout(timerClock);
		isTimeing = false;//重置倒计时状态
		$(".my-hzr-getVer a").text("点击重新获取");
		$(".my-hzr-getVer a").css({"font-size":"12px"});
		$('.my-hzr-getVer').css({"background":"#fe7e00"});
		remainingTime = 60;
		//setGetAuthCodeBtnStyleForState("disabled");
		//$("#J-register-phone")[0].disabled = false;
	};
   </script>
    </body>
</html>