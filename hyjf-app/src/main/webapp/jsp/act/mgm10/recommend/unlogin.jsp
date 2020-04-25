<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/css/mgm.css"/>
<title>邀好友集推荐星</title>
</head>
	<body>
		<div class="hd-unlogin">
			<div class="hd-unlogin-1"></div>
			<div class="hd-unlogin-2">
				<div class="hd-unlogin-cont">
					
					<a class="hd-unlogin-btn"  href="${loginUrl}">立即登录</a>
					<p class="get-start"><img src="${ctx}/images/mgm10/question_mention.png" alt="" />“推荐星”获取秘籍</p>
				</div>
			</div>
			<div class="hd-unlogin-3">
				<div class="hd-unlogin-cont">
					<a class="hd-unlogin-btn" href="${loginUrl}">速去领奖</a>
					<p class="use-start"><img src="${ctx}/images/mgm10/question_mention.png" alt="" />“推荐星”使用指南</p>
				</div>
			</div>
			<div class="hd-unlogin-41"></div>
			<div class="hd-unlogin-4">
				<div class="hd-detial">
					<dl>
						<dt>活动时间：</dt>
						<dd>2016年10月21日起，2016年11月30日止。</dd>
						<dt>活动规则：</dt>
						<dd>凡于汇盈金服有过任意投资的用户，均可在活动期内，通过推荐好友注册投资等方式获得“推荐星”，以兑换好礼或参与幸运抽奖。</dd>
					</dl>
				</div>
				<a class="hd-unlogin-btn" href="hyjf://jumpH5/?{'url':'${host}/jsp/act/mgm10/recommend/hd-detial.jsp'}">更多详情</a>
			</div>
			<div class="hd-unlogin-5"></div>
		</div>
		<!--推荐星获取秘籍弹层-->
		<div class="popup-window getStart">
			<div class="popup-window-bg"></div>
			<div class="popup-window-cont">
				<div class="get-start-rule">
					<img src="${ctx}/images/mgm10/close.png" alt="" />
					<div class="get-start-text">
						<p>1.成功邀请好友注册开户，即可获得1个“推荐星”。</p>
						<p>2.推荐好友自注册之日起30日内累计投资达3000元视为有效邀请，完成一个有效邀请即可获得2个“推荐星”。</p>
						<p>3.每完成3个有效邀请，推荐人可额外再获1个“推荐星”。</p>
					</div>
				</div>
			</div>
		</div>
		<!--推荐星使用秘籍弹层-->
		<div class="popup-window useStart">
			<div class="popup-window-bg"></div>
			<div class="popup-window-cont">
				<div class="use-start-rule get-start-rule">
					<img src="${ctx}/images/mgm10/close.png" alt="" />
					<div class="get-start-text">
						<dl>
							<dt>1.兑换好礼</dt>
							<dd>推荐人可根据不同礼品对应的“推荐星”数量兑换好礼。每月设固定数量奖品，先兑先得，兑完即止。</dd>
							<dt>2.幸运抽奖</dt>
							<dd>推荐人可使用“推荐星”参与幸运抽奖，每次抽奖消耗3个“推荐星”，抽奖次数不限。</dd>
						</dl>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/mgm10/windowCoupop.js"></script>
