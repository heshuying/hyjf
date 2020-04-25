<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/club.css">
<title></title>
</head>
<body>
		<div class="club-level-top text_white font11 club-level-top-detail">
			<div class="my-club-top-left">
				<img src="${ctx}/img/c4.png" alt="头像" class="my-club-top-img"/>
			</div>
			<div class="my-club-top-right vip-detail-right">
				<h3>尊贵标示</h3>
			</div>
		</div>
		<div class="my-club-bottom-detail">
			<div class="my-club-detail-cell">
				<p class="text_black my-club-detail-header">权益对象</p>
				<p class="my-club-detail-content">所有会员用户</p>
				<div class="my-club-detail-line"></div>
			</div>
		</div>
		<div class="my-club-bottom-detail fs13 my-club-bottom-detail2">
			<div class="my-club-detail-cell">
				<p class="text_black my-club-detail-header">权益内容</p>
				<p class="text_light">拥有独特的会员身份标示，彰显您的与众不同</p>
			</div>
		</div>
</body>
</html>