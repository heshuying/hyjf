<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/css/club.css">
<title></title>
</head>
<body>
		<div class="club-level-top text_white font11 club-level-top-detail">
			<div class="my-club-top-left">
				<img src="${ctx}/img/c1.png" alt="头像" class="my-club-top-img"/>
			</div>
			<div class="my-club-top-right vip-detail-right">
				<h3>欢迎礼包</h3>
			</div>
		</div>
		<div class="my-club-bottom-detail">
			<div class="my-club-detail-cell">
				<p class="text_black my-club-detail-header">权益对象</p>
				<p class="my-club-detail-content">购买会员礼包用户,成为白银会员用户</p>
				<div class="my-club-detail-line"></div>
			</div>
		</div>
		<div class="my-club-bottom-detail fs13">
			<div class="my-club-detail-cell">
				<p class="text_black my-club-detail-header">权益内容</p>
				<p class="my-club-detail-header2 text_black">1.不限额加息券*3</p>
				<p class="text_light">投资不限额，加息值1.00%，2张；</p>
				<p class="text_light">投资不限额，加息值1.20%，1张；</p>
				<p class="my-club-detail-header2 text_black">2.限额加息券*3</p>
				<p class="text_light">适用投资金额区间为0~2万，加息值1.50%；</p>
				<p class="text_light">适用投资金额区间为2~5万，加息值1.50%；</p>
				<p class="text_light">适用投资金额区间为5~10万，加息值1.50%；</p>
				<p class="my-club-detail-header2 text_black">3.有效期</p>
				<p class="text_light">自领取之日起，有效期为1年</p>
			</div>
		</div>
</body>
</html>