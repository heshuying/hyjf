<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="css/page.css"/>
		<link rel="stylesheet" href="css/font-awesome.min.css">
		<title></title>
	</head>
	<body class="bg_grey">
		<div class="container specialFont response">
			<div class="recharge-main">
				<p class="tac recharge-main-title"><span>其他充值方式</span></p>
				<p>支付宝、网银转账：</p>
				<p>收款方户名：${userName}</p>
				<p>收款方账号：${account }</p>
				<p>收款方开户行：江西银行总行营业部</p>
				<p style="color: #B2B2B2;margin-top: 5%;">友情提示：你可以通过支付宝转账、银行柜台转账、网银转账、手机银行转账的方式、将资金充值到您的江西银行存管账户，实现账户充值，须填写的信息如上。</p>
			</div>
		</div>
	</body>
</html>