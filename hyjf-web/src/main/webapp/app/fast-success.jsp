<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body  style="background:#f2f2f2">
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="register-content result-content">
            	<div class="register-top">
            		<img src="http://img.hyjf.com/dist/images/result/register-success.png" alt="" />
            	</div>
            	<div class="register-success result-mid">
            		<h3>恭喜您授权成功！</h3>
            	</div>
            	<div class="register-mid">
            		<p>开通江西银行存管账户，享资金安全保障</p>
            	</div>
            	<p class="count-down"><a href="#">返回用户中心</a>，<span>5</span>秒后自动跳转</p>
            	<div class="result-btn">
            		<a href="" class="register-btn import" itemid="lt1">立即跳转</a>
            	</div>
            </div>
        </div>
    </article>
	<script src="${ctx}/dist/js/lib/jquery.min.js"></script>
    <script>
	$(function(){
		var time=5
		setInterval(function(){
			time--
			$('.count-down span').text(time)
		},1000)
		setTimeout(function(){
			location.href='https://www.baidu.com/'
		},5000)
	})
	</script>
</body>
</html>