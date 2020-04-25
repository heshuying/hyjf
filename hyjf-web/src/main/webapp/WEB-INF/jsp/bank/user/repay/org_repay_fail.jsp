<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
            		<img src="${cdn}/dist/images/result/failure-img@2x.png" width="125">
            	</div>
            	<div class="result-mid">
            		<h3>还款申请提交失败</h3>
            	</div>
            	<div class="result-add">
            		<p class="failure-reason">${message }</p>
            	</div>
            	<div class="result-btn">
            		<a href="${ctx}/bank/web/user/repay/userRepayPage.do" class="register-btn import" itemid="lt1">返回重试</a>
            	</div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>