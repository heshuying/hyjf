<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>充值失败 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="result-content">
            	<div class="chongzhi-failure">
            		<img src="${cdn}/dist/images/result/chongzhi-failure@2x.png" width="229" alt="" />
            	</div>
            	<div class="result-mid">
            		<h3>您的充值失败了！</h3>
            		<p>抱歉，您的账户充值失败！</p>
            	</div>
            	<div class="result-add">
            		<p>失败原因</p>
            		<p class="failure-reason">${message}</p>
            	</div>
            	<div class="result-btn">
            		<a href="${ctx}/recharge/rechargePage.do" class="register-btn import" itemid="bt1">返回重试</a>
            	</div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	</body>
</html>