<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="apple-touch-fullscreen" content="yes">
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<link rel="stylesheet" type="text/css" href="${ctx}/jsp/act/hongbao/css/december.css">
<title>红包盛筵</title>
</head>
<body>
<div class="content"> 
<img src="${ctx}/jsp/act/hongbao/images/home_top.jpg">
<div class="home_box">
<div class="tips"><em>下一时间段开抢时间为</em><span class="date"><i>21:00</i></span></div>
<span class="intro">
<a href="hyjf://jumpLogin/?" class="btn"><img src="${ctx}/jsp/act/hongbao/images/home_btn.png"></a>
<h4>您本时段剩余<em>3</em>次抢红包机会</h4>
</span>
</div>
<div class="rule_tit"><em>活动规则</em><a href="hyjf://jumpLogin/?" class="js_wallet">我的红包</a></div>
<div class="rule_box">
<em>活动时间</em>
<p>2015年12月23日0:00至2015年12月25日23:59</p>
<em>活动规则</em>
<p>活动期内，凡账户待收本金≥200元的用户，皆可于特定整点时刻参与红包盛筵。关注汇盈金服微信公众号（huiyingdai），点击“金牌服务”中“红包盛筵”参与活动。红包金额为1~20元不等，每个时段内每用户有3次抢红包机会，60秒内分享即可将红包现金收入囊中。红包数量有限，手慢则无，速速来抢！</p>
<em>攒RP提示</em>
<p>1、提前登陆<br>2、时常刷新<br> 3、提前熟悉分享操作</p>
<em>奖励发放</em>
<p>红包奖励将于成功领取后5个工作日内发放至用户汇盈金服账户。</p>
<p>注：<br>1、抢得红包但未在规定时间内分享的用户，红包将自动释放，该红包奖励将不予发放。<br>2、各时段内未使用的抢红包机会将被清零，不计入下一时段。</p>
<p>本活动最终解释权归汇盈金服所有</p>
</div>
<div id="toolbar">本<br>时<br>段<br>剩<br>余<em>437</em>个<br>红<br>包</div>
</div>

<!--中奖 start-->
<div class="popup" id="win_wallet">
<img class="share" src="${ctx}/jsp/act/hongbao/images/share.png">
<div class="popup_box"> 
<h1>抢红包结果</h1>
<p><img src="${ctx}/jsp/act/hongbao/images/win_tp.png"></p>
<h2>恭喜您抢到20元红包</h2>
<h4><em><b class="number">60</b>秒</em>内分享即可收入囊中</h4>
<p><a href="hyjf://jumpLogin/?" class="js_back">返 回</a></p>       
</div>
</div>
<!--中奖 end-->

<!--中奖后 start-->
<div class="popup" id="release">
<div class="popup_box hint"> 
<h3>这枚红包将被释放，你确定吗？</h3>
<p><a href="hyjf://jumpLogin/?" class="release_ok">确定</a><a href="hyjf://jumpLogin/?" class="release_no">取消</a></p> 
</div>
</div>
<!--中奖后 end-->

<!--分享后 start-->
<div id="result"><em>手速太慢，红包已飞！</em></div>
<!--分享后 end-->

<!--未中奖 start-->
<div class="popup" id="pity_wallet">
<div class="popup_box"> 
<h1>抢红包结果</h1>
<p><img src="${ctx}/jsp/act/hongbao/images/pity_tp.png"></p>
<h2>哎呀，好可惜</h2>
<h4>您来晚了，本时段红包已被抢光</h4>
<p><a href="hyjf://jumpLogin/?" class="js_back">好 吧</a></p>  
</div>
</div>
<!--未中奖 end-->

<!--我的红包 start-->
<div class="popup" id="my_wallet">
<div class="popup_box wallet"> 
<h1>我的红包</h1>
<img class="close" src="${ctx}/jsp/act/hongbao/images/close.png" >
<div class="brief">
<table>
<thead>
<tr>
<th width="54%">时间</th>
<th width="46%">红包金额</th>
</tr>
</thead>
<tbody>
<tr><td>2015-09-07 13:00:18</td><td>2元红包</td></tr>
<tr><td>2015-09-07 13:00:18</td><td>5元红包</td></tr>
<tr><td>2015-09-07 13:00:18</td><td>1元红包</td></tr>
<tr><td>2015-09-07 13:00:18</td><td>2元红包</td></tr>
<tr><td>2015-09-07 13:00:18</td><td>5元红包</td></tr>
<tr><td>2015-09-07 13:00:18</td><td>1元红包</td></tr>
<tr><td>2015-09-07 13:00:18</td><td>211元红包</td></tr>
</tbody>
</table>
</div>
</div>
</div>
<!--我的红包 end-->
<script type="text/javascript" src="${ctx}/jsp/act/hongbao/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript">
//右侧漂浮
$(function() {  
	var timer, scrollTop, sideDiv = $('#toolbar').appendTo('body');  	
	$(window).scroll(function() {  
		timer && clearTimeout(timer);  
		scrollTop = $(this).scrollTop();  
		timer = setTimeout(function() {  
		sideDiv.animate({  
			top: scrollTop + 320 + 'px'  
		}, 600);  
   }, 200);  
}); 	 
}); 
//60秒倒计时
/*$(function () {
	$('.intro .btn').click(function () {
	$("#win_wallet").show();				
	var count = 60;
	var countdown = setInterval(CountDown, 1000);
	function CountDown() {                 
		$(".number").text(count);						
		if (count == 0) {			      
			$("#result").fadeIn();
			clearInterval(countdown);
			}
		count--;
		}
	})
	
});		 */
$(function(){		
	$("#win_wallet .js_back").click(function(){
		$("#release").fadeIn();
	});	
		
	$("#result,#release .release_ok").click(function(){
		$("#result,.popup").fadeOut();
	});			
	$("#release .release_no").click(function(){
		$("#release").fadeOut();		
	});		
		
	$("#pity_wallet .js_back").click(function(){
		$(".popup").fadeOut();
	});		
	$(".js_wallet").click(function(){
		$("#my_wallet").fadeIn();
	});			
	$("#my_wallet .close").click(function(){
		$("#my_wallet").fadeOut();
	});			
});
</script>
</body>
</html>