<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/headResponsive.jsp"></jsp:include>
	<link rel="stylesheet" href="${cdn}/css/fast-authorize.css" type="text/css" />
</head>
<body  style="background:#f2f2f2">
<header id="header">
	<div class="nav-main">
		<div class="container">
			<a href="javascript:;" class="logo" itemid="hd1"><img src="${cdn}/dist/images/logo.png?v=20171123" alt="汇盈金服" /></a>
		</div>
	</div>
</header>
	<article class="main-content">
        <div class="container result">
            <div class="register-content result-content">
            	<div class="register-top">
            		<c:if test="${ resultForm.status == '0' }">
            			<img src="${cdn }/dist/images/result/register-success@2x.png" width="390" alt="" />
            		</c:if>
            		<c:if test="${ resultForm.status != '0' }">
            			<img src="${cdn }/dist/images/result/register-failure@2x.png" width="125" alt="" />
            		</c:if>
            	</div>
            	<div class="register-success result-mid">
            		<h3>${resultForm.statusDesc }</h3>
            	</div>
            	<c:if test="${ !empty resultForm.data.retUrl }">
	            	<p class="count-down" style="padding-top:40px"><a href="${resultForm.data.retUrl }">返回</a>，<span>5</span>秒后自动跳转</p>
	            	<div class="result-btn">
	            		<a href="${resultForm.data.retUrl }" class="register-btn import" itemid="lt1" id="returnUrl">${resultForm.data.butMes }</a>
	            	</div>
            	</c:if>
            	<c:if test="${ empty resultForm.data.retUrl }">
	            	<p class="count-down" style="padding-top:40px"><a href="javascript:window.history.go(-1)">返回上一页</a>，<span>5</span>秒后自动跳转</p>
	            	<div class="result-btn">
	            		<a href="javascript:window.history.go(-1)" class="register-btn import" itemid="lt1" id="returnUrl">返回上一页</a>
	            	</div>
            	</c:if>
            </div>
        </div>
    </article>
	<script src="${cdn}/dist/js/lib/jquery.min.js"></script>
	<script src="${cdn}/dist/js/lib/jquery-migrate-1.2.1.js"></script>
    <script>
	$(function(){
		var time=5
		setInterval(function(){
			time--
			$('.count-down span').text(time)
		},1000)
		setTimeout(function(){
			location.href=$("#returnUrl").attr("href");
		},5000)
	})
	</script>
</body>
</html>