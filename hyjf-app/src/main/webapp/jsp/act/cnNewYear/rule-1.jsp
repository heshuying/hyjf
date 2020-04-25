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
		<div class="newYear-active-rule">
			<div class="active1-rule-1">
				<h3>财神来敲我家门</h3>
			</div>
			<div class="active1-rule-2">
				<dl style=" padding-top: 0.4rem;">
					<dt>活动时间：</dt>
					<dd>2017年1月20日至2017年2月4日</dd>
				</dl>
				<dl style=" padding-top: 0.3rem;">
					<dt>活动规则：</dt>
					<dd>活动期内，用户每集齐一套"金" "鸡" "纳" "福" 财神卡即可获得一次点爆竹赢大奖机会，100%中奖。除各式代金券奖励外，更有iPhone 7 Plus等你来赢！</dd>
				</dl>
			</div>
			<div class="active1-rule-3">
				<dl>
					<dt>财神卡获取方式：</dt>
					<dd>1.于活动期内成功注册并开户。</dd>
					<dd>2.单笔投资每满1万元。</dd>
					<dd>3.通过分享邀请码，每成功邀请1位好友注册开户。</dd>
					<dd>4.由好友赠送财神卡。凡拥有任意一张财神卡的用户，皆可通过输入好友注册手机号方式，将自己拥有的财神卡赠送给平台注册好友。</dd>
				</dl>
			</div>
			<div class="active1-rule-4">
				<dl>
					<dt>奖励设置：</dt>
					<dd>iPhone 7 Plus (128G，颜色随机)</dd>
					<dd>10元代金券 (单笔投资达6000元可用)</dd>
					<dd>50元代金券 (单笔投资达25000元可用)</dd>
					<dd>120元代金券 (单笔投资达50000元可用)</dd>
					<dd>200元代金券 (单笔投资达80000元可用)</dd>
				</dl>
			</div>
			<div class="active1-rule-5">
				<dl>
					<dt>奖励发放：</dt>
					<dd>1.代金券奖励将于用户成功抽取后，由系统自动发放至用户汇盈金服账户，用户登陆后可于“优惠券”中查看。</dd>
					<dd>2.iPhone 7 Plus将于活动结束后3个工作日内，由客服电话回访核实用户信息后，统一采购发放，如遇货源短缺，发放时间相应顺延。</dd>
				</dl>
			</div>
			<div class="active1-rule-6">
				<dl>
					<dd>注：</dd>
					<dd>1.本活动仅限投资汇直投、新手汇、汇添金项目金额参与。</dd>
					<dd>2.通过好友赠送方式获得的财神卡，其种类由好友指定；通过其他方式获得的财神卡，由系统随机发放。</dd>
					<dd>3.未于活动期内成功使用财神卡抽奖的用户，视为自动放弃获奖机会，奖励不予发放。</dd>
					<dd>4.本活动所发优惠券奖励均自用户获得之日起15日内有效，过期作废。</dd>
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