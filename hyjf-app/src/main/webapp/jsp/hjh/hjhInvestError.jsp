<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>加入结果</title>
	</head>
	<body>
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<div class="container specialFont response">
			<div class="outcome">
				<div class="outcome_icon"><i class="icon-remove-circle"></i></div>
				<h5>加入失败！</h5>
				<p>失败原因：${statusDesc}</p>
			</div>
			<div class="process">
				<div class="process_line btn_bg_color"><a href="hyjf://closeView/?{'pop':'1'}" class="">返回重试</a></div>
				<div class="process_line btn_bg_color"><a href="hyjf://jumpHJH/?" class="">完成</a></div>
			</div>
			<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
			<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
			<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		</div>
	</body>
</html>