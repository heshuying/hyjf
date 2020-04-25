<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>债权转让异常 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	   <div class="error-system-top error-top">
			<p>
				对不起，发生异常...<br>
				${message }
			</p>
		</div>
		<div class="error-system-bot error-bot">
				<a class="error-return" href="${ctx}/bank/user/credit/webcreditlist.do">返回债权转让</a>
		</div>
	
	<jsp:include page="/footer.jsp"></jsp:include>
	</body>
</html>