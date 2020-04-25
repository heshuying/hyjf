<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/css/NewYear.css"/>
<title>汇盈金服新年活动</title>
</head>
<body>
		<div class="newYear-active-rule2 newYear-active-rule">
			<div class="active2-rule-1">
				<h3>红红火火闹元宵</h3>
				<dl>
					<dt>活动时间：</dt>
					<dd>2017年2月5日至2017年2月11日</dd>
				</dl>
				<dl style="margin-top:0.35rem">
					<dt>活动规则：</dt>
					<dd>活动期内，凡汇盈金服用户每日登陆皆可获得一次猜灯谜机会，2分钟内答对谜题即可获得代金券奖励。</dd>
				</dl>
			</div>
			<div class="active2-rule-2">
				<dl>
					<dt>奖励设置：</dt>
					<dd>1.首次答对谜题可获得一张10元代金券（单笔投资达20000元可用），之后每答对一题可为代金券增值10元，使用条件不变。</dd>
					<dd>2.活动最后一天答对谜题者，可获得之前累计最高面值的代金券张数翻倍机会，代金券面值不再增值。</dd>
					<dd>3.首次答对谜题日为活动最后一天者，仅可获得10元代金券奖励一张，代金券数量不翻倍。</dd>
				</dl>
				
			</div>
			<div class="active2-rule-3">
				<dl>
					<dt>奖励发放：</dt>
					<dd>代金券奖励将于活动结束后，由系统自动发放至用户汇盈金服账户，用户登陆后可于“优惠券”中查看。</dd>
					<dd style="margin-top:0.65rem">注：本活动所发代金券均自用户获得之日起15日内有效，过期作废。</dd>
				</dl>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
	<script type ="text/javascript">
		document.documentElement.style.fontSize = $(document.documentElement).width() /12.42 + 'px';
		$(window).on( 'resize', function () {
		document.documentElement.style.fontSize = $(document.documentElement).width() /12.42 + 'px';
	});
</script>
</html>