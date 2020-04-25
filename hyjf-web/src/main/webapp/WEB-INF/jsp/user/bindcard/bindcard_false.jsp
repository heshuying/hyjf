<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/head.jsp"></jsp:include>
<title>绑卡失败 - 汇盈金服官网</title>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>

	<div class="shSuccess-Top">
		<span>账户绑卡</span>
	</div>
	<div class="txfail-Ban shSuccess-Ban">
		<h4>绑卡失败!</h4>
		<p>
			对不起，绑卡失败！请 <a class="contact-customer" href="#">联系客服 &gt;&gt;</a>
		</p>
		<p>
			<span class="fail-reason">失败原因：</span> <span class="fail-reason-cont">${error }</span>
		</p>
	</div>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>