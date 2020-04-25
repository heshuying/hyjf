<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>转账失败 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	<div class="shSuccess-Top">
		    <span>充值手续费转账</span>
		</div>
		<div class="txfail-Ban shSuccess-Ban">
			<h4>转账失败!</h4>
		    <p>
		    	<span class="fail-reason">失败原因：</span>
		    	<span class="fail-reason-cont">${message}</span>
		    </p>
			<div class="shSuccess-click">
			    <a href="${ctx}/rechargeFee/rechargeFeePage.do"  class="inputBg">返回重试</a>
			</div>
		</div>
	<jsp:include page="/footer.jsp"></jsp:include>
	</body>
</html>