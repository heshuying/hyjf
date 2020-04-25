<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
		<title>双十二活动</title>
		<style type="text/css">
		     *{margin:0;padding:0}
		     .double-active-rule>img{display: block;width: 100%;}
		</style>
	</head>
	<body>
		<div class="double-active-rule">
			<img src="${ctx}/img/act20171212/double-active-introduce01.jpg" alt="" />
			<img src="${ctx}/img/act20171212/double-active-introduce02.jpg" alt="" />
			<img src="${ctx}/img/act20171212/double-active-introduce03.jpg" alt="" />
			<img src="${ctx}/img/act20171212/double-active-introduce04.jpg" alt="" />
		    <img src="${ctx}/img/act20171212/double-active-introduce05.jpg" alt="" />
		</div>
	</body>
</html>