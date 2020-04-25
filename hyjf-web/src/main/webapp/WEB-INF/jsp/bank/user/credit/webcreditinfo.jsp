<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>投资结果- 汇盈金服官网</title>
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
	            		<h3>处理中！</h3>
	            		<p>您的投资项目正在处理</p>
	            	</div>
	            	<div class="result-add">
	            		<p>当前处理状态信息</p>
	            		<p class="failure-reason">${INFO }</p>
	            	</div>
	            	<div class="result-btn">
	            		<a href="${ctx}/bank/user/trade/initTradeList.do" target="_self" class="register-btn import" itemid="bt1">查看</a>
	            	</div>
	            </div>
	        </div>
	    </article>
		<jsp:include page="/footer.jsp"></jsp:include>	
	</body>
</html>
