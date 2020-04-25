<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>赎回失败 - 汇盈金服官网</title>
	    <jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
		<div class="shSuccess-Top">
		    <span>汇天利赎回</span>
		</div>
		<div class="shfail-Ban shSuccess-Ban">
			<h4>赎回失败，${message }</h4>
			<p>
				对不起，汇天利赎回异常！请
			<a class="contact-customer" href="#">联系客服  &gt;&gt;</a>
				
		    </p>
			<div class="shSuccess-click">
			    <a href="${ctx}/htl/moveToRedeemPage.do" class="inputBg">返回重试</a>
			</div>
		</div>
		<jsp:include page="/footer.jsp"></jsp:include>	
	</body>
</html>
