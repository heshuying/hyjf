<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title></title>
	</head>
	<body class="bg_grey">
		<div class="container specialFont response">
			<p class="others_recharge_text">1.为保证您的资金安全，资金账户由汇付天下进行第三方资金托管，民生银行全程监督</p>
			<p class="others_recharge_text">2.<span class="text_orange text_orange_red">投资人充值投资所有项目均不收取充值费用</span></p>
			<p class="others_recharge_text">3.<span class="text_orange text_orange_red">目前手机端仅支持快捷充值</span></p>
			<p class="others_recharge_text">4.<span class="text_orange text_orange_red">只能绑定一张银行卡作为快捷卡</span>，且只可以提现到该快捷卡；一经绑定，其余银行卡自动解绑</p>
			<p class="others_recharge_text">5.为了您的资金安全，更换快捷卡需人工审核，请选择常用银行卡作为快捷卡</p>
			<p class="others_recharge_text">6.严禁利用充值功能进行信用卡套现、转账、洗钱等行为，一经发现将冻结账号</p>
			<p class="others_recharge_text">7.限额说明</p>
			<p class="others_recharge_text"></p>
			<p class="others_recharge_text"></p>
			<table border="1" cellspacing="0" cellpadding="0" class="tac others_table">
				<tr>
					<td>银行名称</td>
					<td>单笔限额</td>
					<td>单日累计限额</td>
				</tr>
	 		    <c:forEach items="${bankRechargeLimitList}" var="record" begin="0" step="1" varStatus="status">
					<tr>
						<td>
							<c:forEach items="${quickBankList }" var="bank" begin="0" step="1">
								<c:if test="${bank.id eq record.bankId}"><c:out value="${bank.name }"></c:out></c:if>
							</c:forEach>
						</td>
						<td>
							<c:if test="${record.singleQuota == null}">无限额</c:if>
							<c:if test="${record.singleQuota != null}">
								<fmt:formatNumber value="${record.singleQuota >= 10000 ? record.singleQuota/10000: record.singleQuota}" pattern="#,###" />
								<c:out value="${record.singleQuota >= '10000' ? '万元': '元'}"></c:out>
							</c:if>
						</td>
						<td>
							<c:if test="${record.singleCardQuota == null}">无限额</c:if>
							<c:if test="${record.singleCardQuota != null}">
								<fmt:formatNumber value="${record.singleCardQuota >= 10000 ? record.singleCardQuota/10000: record.singleCardQuota}" pattern="#,###" />
								<c:out value="${record.singleCardQuota >= '10000' ? '万元': '元'}"></c:out>										
							</c:if>
						</td>
					</tr>
				</c:forEach>
			
			</table>
		</div>
	</body>
</html>