<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<div id="service">
		<div class="banner-bg">
			<div class="banner-1">
				<div class="contant">
					<header class="app-header">
						<a class="logo" href="${ctx}/"><img src="${cdn}/dist/images/logo-white@2x.png?v=20171123"/></a>
						<div class="top-right"><span class="icon iconfont icon-dianhua"></span> 客服服务时间 9:00-18:00</div>
					</header>
				</div>
			</div>
		</div>
		<div class="banner-2">
			<div class="contant">
			<c:choose>
			<c:when test="${cookie['hyjfUsername'].value == null || cookie['hyjfUsername'].value == ''}">
				<a class="consultation" href="https://www.sobot.com/chat/pc/index.html?sysNum=1625b67dbd104a8f8d6a9b4ef8d77d5b&version=1527648376&moduleType=1" target="_blank">我要在线咨询</a>
			</c:when>
			 <c:otherwise>
			 	<a class="consultation" href="https://www.sobot.com/chat/pc/index.html?sysNum=1625b67dbd104a8f8d6a9b4ef8d77d5b&version=1527648376" target="_blank">我要在线咨询</a>
			 </c:otherwise>
			 </c:choose>
			</div>
		</div>
		<div class="banner-3">
			<div class="contant">
				<div class="contact-info">
					<p class="tit">INVESTOR RELATIONS CONTACT INFO.</p>
					<p>
						Company Investor Relations Contact:
						<br />
						Ede Yang
						<br />
						Tel: +86-21-2357-0055 ext. 8023
						<br />
						Email: ir@hyjf.com
					</p>
					<a class="go-investor" href="http://ir.hyjf.com/" target="_blank">进入Investor Relations 网站</a>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>