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
		<title>资金存管账户</title>
	</head>
	<body class="bg_grey">
	<input type="hidden" name="jumpcommend" id="jumpcommend" value="" />
		<div class="container specialFont">
			<p class="others_title response">江西银行资金完全存管</p>
			<p class="others_title response">保证用户资金独立存放</p>
			<div class="about_content response">
				<p class="others_text">&nbsp;&nbsp;&nbsp;&nbsp;为了加强P2P行业的自律意识，汇盈金服从自身做起，也是第一批使用标准资金存管系统的平台。</p>
				<p class="others_text">&nbsp;&nbsp;&nbsp;&nbsp;相比于只开通第三方支付功能的平台，江西银行资金存管可保障用户账户中的资金独立存放，用户可通过平台对本人在江西银行的账户进行充值、提现，商户在未经用户的许可、授权或法律的规定的情况下，无权动用。在这基础上，江西银行账户中每笔资金流动明细都能被投资者清晰地监控，凸显了资金的透明度，用户对资金有完全的控制权力。汇盈金服互联网金融服务平台全程做到不吸储、不放贷、不集资。</p>
				<p class="others_text">&nbsp;&nbsp;&nbsp;&nbsp;资金流运行在江西银行存管账户中，而不经过平台的银行账户。从而避免平台因为经营不善导致挪用交易资金而给交易双方带来风险，将用户的资金安全放到了第一位。</p>
				<p style="margin-top:20px;font-size:14px">资金存管流程</p>
				<img src="${ctx}/img/other_flow.png" alt="flow" class="flow_img"/>
			<div class="process">
				<div class="process_line btn_bg_color"><a href="#" id="openAccount" class="hy-jumpH5">开通资金存管账户，开始赚钱</a></div>
			</div>
			</div>
		</div>
		<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script>
		var sign = GetQueryString("sign");
		var platform = GetQueryString("platform");
		var token = GetQueryString("token");
		var version = GetQueryString("version");
		$("#openAccount").attr("href","${ctx}/bank/user/bankopen/open?sign="+sign+"&platform="+platform+"&token="+token+"&version="+version);
		</script>
	</body>
</html>