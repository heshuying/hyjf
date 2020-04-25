<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${cdn}/css/page.css" />
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="shSuccess-Top">
		<span>自动投资授权</span>
	</div>
	<div class="shfail-Ban shSuccess-Ban">
			<h4>授权失败</h4>
			<p>
				${message}
				<br>
				<br>
				失败原因：${error}
		    </p>
			<div class="shSuccess-click">
			    <a href="${ctx}/user/pandect/pandect.do" class="inputBg">返回重试</a>
			</div>
		</div>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>