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
<link rel="stylesheet" type="text/css" href="${ctx}/jsp/act/yuebing/css/september.css">
<title>当月饼成精的时候——会送礼哦</title>
</head>
<body>
<div class="main">
<div class="content_box"> 
<img src="${ctx}/jsp/act/yuebing/images/top_logo.jpg">
<div class="next_trailer">
<div class="box lf">
<a href="javascript:void(0)">
<!--- 红包样式 <span class="lf packet"><img src="images/home_packet03.png"></span>-->
<span class="lf"><img src="${ctx}/jsp/act/yuebing/images/home_mocha.png"></span>
<span class="lr">
<p>下一期奖励：<em>月饼升级（抹茶）</em></p>

<p>记得准时来兑换哟~</p>
</span>
</a>
</div>
</div> 
<img src="${ctx}/jsp/act/yuebing/images/prize.jpg">
<div class="host_menu">
<span><a href="javascript:void(0)"></a><a href="javascript:void(0)"></a></span>
</div>
<div class="base_nav">
<ul>
<li><a href="javascript:void(0)"><em>奖励预告</em></a></li>
<li><a href="javascript:void(0)"><em>兑换记录</em></a></li>
<li><a class="helf" href="javascript:void(0)"><em>活动规则</em></a></li>
</ul>
</div>
</div>
<!--活动规则 start-->
<div class="popup" id="activity-rules">
<div class="activity_box"> 
<img src="${ctx}/jsp/act/yuebing/images/rules_tit.png">
<img src="${ctx}/jsp/act/yuebing/images/popup_head.png">
<img src="${ctx}/jsp/act/yuebing/images/x_close.png" class="close">
<div class="popup_body">
<span>
活动时间：2015年9月1日至2015年9月27日<br />
活动期内，关注汇盈金服官方微信公众号（huiyingdai），点击“金牌服务”中“月饼精灵”进入活动页面。用户按“召集精灵密语”每完成一项任务即可领取一只月饼精灵，每天限领10只。8款汇盈金服专属月饼精灵随机发放。月饼精灵将在每个工作日指定时间发放奖励或进行精灵升级，用户需在指定时间登陆活动页面兑换领取，兑换将消耗规定数量的月饼精灵。每次兑换结束后系统将公布下一期奖励内容及兑换时间。集齐8款月饼精灵更有机会兑换中秋终级大礼！奖励有限，先兑先得，领完为止。
</span>
</div>
<div class="popup_footer">&nbsp;</div>       
</div>
</div>
<!--活动规则 end-->
</div>
<script type="text/javascript" src="${ctx}/jsp/act/yuebing/js/jquery-1.8.0.min.js"></script> 
<script type="text/javascript">
$(function(){
	$(".helf").click(function(){
		$("#activity-rules").show();
	});
	$("#activity-rules").click(function(){
		$(".popup").hide();
	});					
});
</script>
</body>
</html>