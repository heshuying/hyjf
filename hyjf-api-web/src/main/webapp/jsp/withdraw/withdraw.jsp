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
		<title></title>
	</head>
	<body class="bg_grey">
		<div class="container specialFont response">
			<p class="others_recharge_text">1. 身份认证、账户托管开通、提现银行设置均成功后，才能进行提现操作</p>
			<p class="others_recharge_text">2. 提现费用为每笔1.00元，费用为汇付天下收取</p>
			<p class="others_recharge_text">3.<span class="text_orange">用户绑定银行卡作为快捷卡后，只可以提现到快捷卡；</span>为了您的资金安全，更换快捷卡需人工审核，请选择常用银行卡作为快捷卡</p>
			<p class="others_recharge_text">4.工作日（周六/周日/法定节假日均除外）21:00之前申请的提现，当日审核完毕并预计于下一工作日12:00-16:00到账， 否则将于下一工作日审核并预计于再下一工作日12:00-16:00到账；实际到账时间依据账户托管方及提现银行而有所差异</p>
			<p class="others_recharge_text">5.严禁利用充值功能进行信用卡套现、转账、洗钱等行为，一经发现将冻结账号</p>
		</div>
	</body>
</html>