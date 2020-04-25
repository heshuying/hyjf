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
		<input type="hidden" id="projectType" name="projectType" value="${projectType}" />
		<div class="container specialFont response">
			<div class="outcome">
				<div class="outcome_icon"><i class="icon-ok-circle"></i></div>
				<h5>加入成功！</h5>
				
				<c:if test="${not empty plan and plan eq '0'}">
					<p>
						<span>加入金额：${account }元;</span>
						<span>历史回报：${earnings }元</span>
					</p>
				</c:if>
				<c:if test="${couponType == 1}">
					<p>
						<span>优惠券：${couponQuota }元 体验金;</span>
						<span>历史回报：${couponInterest}元</span>
					</p>
				</c:if>
				<c:if test="${couponType == 2}">
					<p>
						<span>优惠券：${couponQuota }%加息券;</span>
						<span>历史回报：${couponInterest}元</span>
					</p>
				</c:if>
				<c:if test="${couponType == 3}">
					<p>
						<span>优惠券：${couponQuota }元代金券;</span>
						<c:if test="${not empty couponInterest }">
							<span>历史回报：${couponInterest}元</span>
						</c:if>
					</p>
				</c:if>
			</div>
			<div class="process">
				<div class="process_line btn_bg_color">  
					<a class="invest-success-a	hy-jumpMyPlan" href="#">完成</a>
				</div>
			</div>
		</div>
		<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script>
		</script>
	</body>
</html>