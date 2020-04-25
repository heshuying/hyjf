<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css?version=${version}"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>转让设置</title>
	</head>
	<body class="bg_grey">
		<div class="activeOutdatePop activeOutdatePopHzr"></div>
		<div class="specialFont  cal-main">
				<input type="hidden" name="borrowNid" id="borrowNid" value="${creditResult.data.borrowNid}"/>
				<input type="hidden" name="tenderNid" id="tenderNid" value="${creditResult.data.tenderNid}"/>
				<input type="hidden" name="userId" id="userId" value="${userId}"/>
				<input type="hidden" name="platform" id="platform" value="${platform}"/>
				<section class="bg_white my-hzr-header">
					<div class="my-hzr-header-div tac">
						<p class="font13">${creditResult.data.borrowNid}</p>
						<p class="font19 HZRprice format-num">${creditResult.data.creditAccount}</p>
						<span>债权本金(元)</span>
					</div>
					<span></span>
					<div class="my-hzr-header-div tac">
						<p class="font13 color898989">持有<i class="hyjf-color">&nbsp;${creditResult.data.tenderPeriod}&nbsp;</i>天&nbsp;&nbsp;剩余<i class="hyjf-color">&nbsp;${creditResult.data.lastDays}&nbsp;</i>天</p>
						<p class="font19">${creditResult.data.borrowApr}<i class="font12">%</i></p>
						<span>历史年回报率</span>
					</div>
				</section>
				<section class="bg_white tac my-hzr-mid">
					<p class="tac hyjf-color">折让率</p>
					<p class="tac hyjf-color font19 my-hzr-percent"><span>0.5</span><i class="font12">%</i></p>
					<img src="${ctx}/img/my-hzr-down.png" class="my-hzr-down"/>
				</section>
				<section class="my-hzr-slider bg_white">
					<div class="my-hzr-percent-slider">
						<div class="my-hzr-percent-slider-item" style="width:189px">
						</div>
						<div class="my-hzr-percent-slider-item" style="width:214px">
							<span>0.5</span>
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
						<span class="HZRrealprice">0</span>
					</div>
					<p class="color898989 font12">转让价格=本金金额*(1-折让率)</p>
					<div>
						<span>预估持有期收益<img src="${ctx}/img/icon-question.png" alt="" class="question-icon"/></span>
						<span class="HZRincome format-num">${assignInterest}</span>
					</div>
					<div>
						<span>预估服务费(1.0%)</span>
						<span class="HZRservice"></span>
					</div>
					<div class="my-hzr-header-div-last">
						<span>预估到账金额<img src="${ctx}/img/icon-question.png" alt="" class="question-icon"/></span>
						<span class="font19 hyjf-color lastRealPrice"></span>
					</div>
				</section>
				<section class="bg_white font13 my-hzr-item my-hzr-message">
					<input type="tel" name="tel" id="tel" value="" placeholder="请输入短信验证码" class="my-hzr-input" maxlength="6"/>
					<span class="process_line btn_bg_color my-hzr-getVer"><a id="phonecode">获取验证码</a></span>
				</section>
				<div class="process_line btn_bg_color my-hzr-btn"><a id="confirmBtn" >确认转让</a></div>
		</div>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js" ></script>
   <script type="text/javascript">
   $(function(){
	   Number.prototype.toFixed2 = function() {
			return parseFloat(this.toString().replace(/(\.\d{2})\d+$/, "$1"));
		}
	   //判断如果没有小数点 则加上.00,如果是一位小数 则加上0
	   function addPoint(num){
	   	var index = num.indexOf(".");
	   	var l = num.substring(index).length;
	   	if(index===-1){
	   		var newNum = num+".00";
	   		return newNum;
	   	}else if(l===2){
	   		return num+"0";
	   	}else{
	   		return num;
	   	}
	   }
	   
	  /*  初始化转让价格 */
	var HZRprice_init = $(".HZRprice").text().replace(/,/g,'');
	var HZRrealprice_init = (HZRprice_init*(1-0.5/100)).toFixed2();
	$(".HZRrealprice").text(HZRrealprice_init);
	
   	var wid = $(window).width();
   	//设置最左最右 使滑块不超过最大最小值
   	var w = 423+wid+3;
   	$(".my-hzr-percent-slider").width(w)
   	$(".my-hzr-percent-slider-item").eq(0).width(wid/2);
   	$(".my-hzr-percent-slider-item").eq(2).width(wid/2)
   	//是滑块居中
   	//var toLeftInit = (976-wid)/2;
   	var toLeftInit = (w-wid)/2;
   	$(".my-hzr-slider").scrollLeft(0);
   	var a = (484-wid)/2;
   	var l = toLeftInit-a;
   	var left = l-wid/2-4;
   	var end = left+488;
   	var toLeft = (($(".my-hzr-slider").scrollLeft()-left)/244)*0.9+0.4;
   	var HZRprice = $(".HZRprice").text().replace(/,/g,'');
	var HZRrealprice = (HZRprice*(1-toLeft.toFixed(1)/100)).toFixed2();
	var s = +HZRrealprice;		
	var f = Number($(".HZRincome").text().replace(/,/g,''));
	var service = ((s+f)*0.01).toFixed2();
	$(".HZRservice").text(addPoint(formatNum(service))); //服务费  	
	var lastRealPrice = (s*100+f*100-service*100)/100 //预估到账金额
	$(".lastRealPrice").text(addPoint(formatNum(lastRealPrice.toFixed2())));//预估到账金额
   $(".my-hzr-slider").scroll(function(){
   		var toLeft = (($(".my-hzr-slider").scrollLeft()-left)/244)*0.9+0.4;
   		if(toLeft<0.5){
   			toLeft = 0.5
   		}else if(toLeft>2){
   			toLeft = 2.0
   		}
   		$(".my-hzr-percent span").text(toLeft.toFixed(1));
   		var HZRprice = $(".HZRprice").text().replace(/,/g,'');
   		var HZRrealprice = (HZRprice*(1-toLeft.toFixed(1)/100)).toFixed2();
   		$(".HZRrealprice").text(addPoint(formatNum(HZRrealprice)) );//转让价格
   		var s = +HZRrealprice;
		var f = Number($(".HZRincome").text().replace(/,/g,''));
		var service = ((s+f)*0.01).toFixed2();
		$(".HZRservice").text(addPoint(formatNum(service))); //服务费  	
		var lastRealPrice = (s*100+f*100-service*100)/100//预估到账金额
		$(".lastRealPrice").text(addPoint(formatNum(lastRealPrice.toFixed2())));//预估到账金额
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
	// $(".my-hzr-getVer a").click(startTimerClock);
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
	
	//获取手机验证码点击事件
	jQuery("#phonecode").click(function(){
		if($("#phonecode").prop("disabled") == true){
			return false;
		}
		if($("#phonecode").text() == "获取验证码" || $("#phonecode").text() == "点击重新获取"){
			jQuery.ajax({
				type: "POST",
				async: "async",
				url: "${ctx}/tender/credit/sendcode.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"userId":jQuery("#userId").val()
				},
				error: function(request) {
			        activeOutdatePop.text("连接服务器出错,请稍后重试").show().delay(3000).fadeOut();
			    },
				success: function(data){
					startTimerClock();
					if(data.resultFlag==0){
						activeOutdatePop.text(data.msg).show().delay(3000).fadeOut();
					}else{
						activeOutdatePop.text(data.msg).show().delay(3000).fadeOut();
					}
				}
				
			});	
		}
	});
	
	//短信验证码失去焦点事件
	jQuery("#tel").blur(function(){
		var telcode = jQuery(this).val();
		if(telcode!=null && telcode!=""){
			jQuery.ajax({
				type: "POST",
				async: "async",
				url: "${ctx}/tender/credit/checkcode.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"code":telcode,
					"userId":jQuery("#userId").val()
				},
				error: function(request) {
					activeOutdatePop.text("连接服务器出错,请稍后重试").show().delay(3000).fadeOut();
				},
				success: function(data){
					if(data){
						jQuery("#telcodeTip").html("");
					}else{
						activeOutdatePop.text("验证码错误,请重新输入").show().delay(3000).fadeOut();
					}
				}
			});
		}else{
			activeOutdatePop.text("请输入验证码").show().delay(3000).fadeOut();
			
		}
		
	});
	
	//债转提交保存
	jQuery("#confirmBtn").click(function(){
		
		$.ajax({
			cache: true,
			type: "POST",
			url:"${ctx}/tender/credit/saveTenderToCredit.do",
			data:{
				"creditDiscount":jQuery(".my-hzr-percent span").text(),
				"userId":jQuery("#userId").val(),
				"borrowNid":jQuery("#borrowNid").val(),
				"tenderNid":jQuery("#tenderNid").val(),
				"telcode":jQuery("#tel").val(),
				"platform":jQuery("#platform").val(),
			},
			async: "async",
			error: function(request) {
				activeOutdatePop.text("连接服务器出错,请稍后重试").show().delay(3000).fadeOut();
			},
			success: function(data) {
				if(data.resultFlag==0){
					// 跳转到债转成功结果画面
					window.location.href="${ctx}/tender/credit/tenderToCreditResult.do?creditNid="+data.data;
				}else if(data.resultFlag==2){
					// 跳转到债转失败结果画面
					window.location.href="${ctx}/tender/credit/tenderToCreditResult.do?creditNid="+data.data+"&resultFlag="+data.resultFlag+"&msg="+data.msg;
				}else{
					activeOutdatePop.text(data.msg).show().delay(3000).fadeOut();
				}
			}
		});
	});
	
   </script>
    </body>
</html>