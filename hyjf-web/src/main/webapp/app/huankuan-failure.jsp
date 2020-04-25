<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="result-content">
            	<div class="result-top">
            		<img src="${ctx}/dist/images/result/failure-img.png" alt="" />
            	</div>
            	<div class="result-mid">
            		<h3>您的还款失败了！</h3>
            		<p>抱歉，您的账户还款失败</p>
            	</div>
            	<div class="result-add">
            		<p>失败原因</p>
            		<p class="failure-reason">身份证或其他用户证件才能办理</p>
            	</div>
            	<div class="result-btn">
            		<a href="" class="register-btn import" itemid="lt1">返回重试</a>
            	</div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>