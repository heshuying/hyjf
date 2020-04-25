<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>汇添金</title>
		<style>
		img{width:100%;display:block;}
		p{text-align:center;padding:20px 0;font-size:14px;letter-spacing:1px;display:block;position:fixed;bottom:0;background:#fff;width:100%}
		.img-last{margin-bottom:40px;}
		</style>
	</head>
	<body>
		<img src="${ctx}/images/htj/htjD01.jpg">
		<img src="${ctx}/images/htj/htjD02.jpg">
		<img src="${ctx}/images/htj/htjD03.jpg">
		<img src="${ctx}/images/htj/htjD04.jpg">
		<img src="${ctx}/images/htj/htjD05.jpg" class = "img-last">
		<p>即将开放，敬请期待..</p>
	</body>
</html>