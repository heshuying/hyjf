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
            <div class="sucess-content">
            	<video src="${ctx }/dist/images/hongbao.mov" autoplay  style="height:284px;width:450px">
            		<img src="${ctx }/dist/images/login/login-success-logo.png" />
            	</video>
            	<p>您已获得<a> 888元 </a>新手红包，您可在<a href="">“我的奖励”</a>中查看</p>
            	<h3>投资前的流程</h3>
            	<img src="${ctx }/dist/images/login/steps.jpg" alt="" />
            	<div class="btn-box"><a href="">开通银行存管</a></div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>