<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<title>新手福利</title>
		<style type="text/css">
			*{margin:0 auto;padding:0 auto;}
			.colorBlue{background-color:#161824}
			.newWelfare img{display: block;width:100%}
			.newWelfare-btn{position: fixed;bottom: 0;width:100%;}
			.newWelfare-btn a{display: block;width:100%;background:#ebbf82;text-align: center;color: #FFF;text-decoration: none;}
			.walfareColor{background:#161824;position: relative;}
			@media only screen and (max-width: 480px) {.newWelfare-btn a{font-size: 16px;padding: 9px 0;}}
			@media only screen and (min-width: 480px) {.newWelfare-btn a{font-size: 26px;padding: 9px 0;}}
			@media only screen and (min-width: 768px) {.newWelfare-btn a{font-size: 30px;padding: 9px 0;}}
		</style>
	</head>
	<body class="colorBlue">
		<div class="newWelfare">
			<img src="${ctx}/images/188newer/newWelfare01.jpg" alt="" />
			<img src="${ctx}/images/188newer/newWelfare02.jpg" alt="" />
			<img src="${ctx}/images/188newer/newWelfare03.jpg" alt="" />
			<img src="${ctx}/images/188newer/newWelfare04.jpg" alt="" />
			<img src="${ctx}/images/188newer/newWelfare05.jpg" alt="" />
			<img src="${ctx}/images/188newer/newWelfare06.jpg" alt="" />
			<img src="${ctx}/images/188newer/newWelfare07.jpg" alt="" />
		</div>
		<c:if test="${!empty regUrl }">
		<div style="height:30px"></div>
		<div class="newWelfare-btn">
			<a href="${regUrl }">立即注册</a>
		</div>
		</c:if>
		<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script>
		var token = GetQueryString("token");
		if(token){
			$(".newWelfare-btn").hide();
		}
		</script>
	</body>
</html>