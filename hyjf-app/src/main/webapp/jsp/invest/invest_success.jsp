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
		<title>江西银行</title>
	</head>
	<body>
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<input type="hidden" id="projectType" name="projectType" value="${projectType}" />
		<div class="container specialFont response">
			<div class="outcome">
				<div class="outcome_icon"><i class="icon-ok-circle"></i></div>
				<h5>投资成功！</h5>
				<c:if test="${not empty account}">
					<p><span>投资金额：${account}元;</span>
					<c:if test="${not empty interest}">
						<span>历史回报：${interest}元</span>
					</c:if>
					<c:if test="${not empty assignpay}">
						<span>真实支付金额：${assignpay}元</span>
					</c:if>
					</p>
				</c:if>	
				<!-- 优惠券投资 start -->
				<c:if test="${not empty couponQuota }">
					<p>
						<c:if test="${couponType == 1 }">
							<span>体验金投资：${couponQuota}元;</span>
						</c:if>
						<c:if test="${couponType == 2 }">
							<span>已使用加息券${couponQuota}%;</span>
						</c:if>
						<c:if test="${couponType == 3 }">
							<span>代金券投资：${couponQuota}元;</span>
						</c:if>
						<c:if test="${not empty couponInterest }">
							<span>历史回报：${couponInterest}元</span>
						</c:if>
					</p>
				</c:if>
				<!-- 优惠券投资 end -->
				
				<c:if test="${not empty statusDesc}">
					<h5>投资成功！</h5>
					<p><span>${statusDesc}</span>
					</p>
				</c:if>
				
			</div>
			<div class="process">
				<div class="process_line btn_bg_color">  
				<c:choose>
				<c:when test="${projectType eq 4}">
				<a class="invest-success-a	hy-jumpInTheInvestment" href="#">
				</c:when>
				<c:when test="${projectType eq -1 }">
				<a class="invest-success-a	hy-jumpTransfer" href="#">
				</c:when>
				<c:otherwise>
				<a class="invest-success-a	hy-jumpInvest" href="#">
				</c:otherwise>
				</c:choose>完成</a></div>
			</div>
		</div>
		<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script>
		</script>
	</body>
</html>