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
<meta content="target-densitydpi=device-dpi,width=750,user-scalable=no" name="viewport">
<link rel="stylesheet" type="text/css" href="${ctx}/jsp/act/pingguo/css/october.css">
<title>收益登高 情意“九九”</title>
</head>
<body>
<div class="content"> 
<p class="btn_rule"><a href="javascript:void(0);">活动规则></a></p>
<img src="${ctx}/jsp/act/pingguo/images/home_top.png">
<div class="home_intro">
<h4>您还有10次摘果机会</h4>
<p><a href="javascript:void(0);" class="btn">点击摘果</a><a href="">一键摘果</a></p> 
<div class="round"><a href="javascript:void(0);"><em>中奖<br>纪录</em></a></div>  
</div>
<div class="home_intro">
<h4></h4>
<p></p> 
<div class="round1"></div>  
</div>
</div>

</div>

<!--中奖 start-->
<div class="popup" id="win_prize">
<div class="popup_box"> 
<img src="${ctx}/jsp/act/pingguo/images/x_close.png" class="close">
<h1>恭喜发财~</h1>
<h4>恭喜获得iPhone6 plus一部</h4>
<p><img src="${ctx}/jsp/act/pingguo/images/prize06.png" class=""></p>
<p><a href="javascript:void(0);" class="btn">收入囊中</a></p>       
</div>
</div>
<!--中奖 end-->

<!--未中奖 start-->
<div class="popup" id="not_win">
<div class="popup_box"> 
<img src="${ctx}/jsp/act/pingguo/images/x_close.png" class="close">
<h1>不好意思噢~</h1>
<h4>没有摘取机会了，快去投资吧！</h4>
<p><img src="${ctx}/jsp/act/pingguo/images/no_prize.png" class=""></p>
<p><a href="javascript:void(0);" class="btn">去投资</a></p>       
</div>
</div>
<!--未中奖 end-->

<script type="text/javascript" src="${ctx}/jsp/act/pingguo/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript">
jQuery(document).ready(function($){
 var arr = [];
 var tarr = [];
 var pic = [];
 pic[0] = "images/lucky01.png";
 pic[1] = "images/lucky02.png";
 pic[2] = "images/lucky03.png";
 pic[3] = "images/lucky01.png";
 pic[4] = "images/lucky01.png";
 pic[5] = "images/lucky02.png";
 pic[6] = "images/lucky03.png";
 var index = 7;
 function rond(){
  for(var i=0; i<index; i++){
   arr[i] = i;
  }
  var j = index;
  for(var i=0; i<index; i++){
   var t = Math.floor(Math.random()*j);
   j--;
   tarr[i] = arr.splice(t,1);
  }
 }
 function resetPic(){
  rond();
  for(var i=0; i<index; i++){
   $("#fruit").append("<li class=\"li_"+tarr[i]+"\"><img src="+pic[i]+"></li>")  
  }
 }
 resetPic();
 });
</script>
<script type="text/javascript">
$(function(){
	$("#fruit img").click(function(){
		$("#win_prize").show();
	});
	$(".close,#win_prize .btn").click(function(){
		$(".popup").hide();
		location.reload();
	});	
	$(".home_intro .btn").click(function(){
		$("#not_win").show();
	});	
});
</script>
</body>
</html>