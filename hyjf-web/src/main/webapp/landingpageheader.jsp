<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<header id="header">
	<div class="hd-inner">
		<h1 class="hd-logo">
			<a href="${ctx}/" title="汇盈金服"><img src="${cdn}/img/logo.png?v=20171123"
				width="140" height="36" alt="汇盈金服" />
			</a>
		</h1>
		<div class="hd-login">
			<a id="login" href="${ctx}/user/login/init.do" class="action-link">登录</a>
			<div class="cut-line"></div>
			<a id="register" href="${ctx}/user/regist/init.do?referer=${landingpageForm.from_id}&inittime=${inittime}" href="#" class="action-link">注册</a>
			<!--  登录后-->
			<a href="${ctx}/user/pandect/pandect.do" id="face" style="display: none;"  class="hd-face">
				<img id="faceUrl" src="" alt="" width="40" height="40" />
			</a> 
			<a href="${ctx }/user/pandect/pandect.do" id="username" style="display: none;" class="hd-user"></a> 
			<a href="${ctx}/user/login/logout.do" id="logout" style="display: none;" href="#" class="iconfont iconfont-logout hd-logout"></a>
	</div>
	</div>
</header>
<script src="${cdn}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>