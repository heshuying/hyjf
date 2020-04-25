<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>银行存管下载</title>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<style type="text/css">
	*{
		margin:0;padding: 0;
	}
	html{height: 100%;}
    body{background: #282b3b;height: 100%;}
	img{width: 100%;}
	div{text-align: center;}
	.updated-download-3{background: url(../../images/updated-download-3.jpg) no-repeat;background-size: 100%;height: 50.2%;}
	.updated-download-3 a{display: inline-block;width: 84%;height: 11%;}
</style>
	</head>
	<body>
		<img src="${ctx}/images/updated-download-1.jpg?v=20171123"/>
		<img src="${ctx}/images/updated-download-2.jpg"/>
		<div class="updated-download-3">
			<a href="${downLoadUrl }"></a>
		</div>
	</body>
</html>