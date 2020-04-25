<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
	<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/header.jsp"></jsp:include>
<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
<article class="main-content">
	<div class="container">
		<section class="about-detial content">
			<div class="main-title">
				<span class="title-head">信披声明</span>
			</div>
			<div class="main-tab jion-us">
				<div class="event">
					<img src="${cdn}/dist/images/aboutUs/commitment/chengnuohan.jpg" alt="" />
				</div>
				<div class="go-back">
					<a href="${ctx}/aboutus/getInformation.do">返回列表<i class="xiayibu"></i></a>
				</div>
			</div>
		</section>
	</div>
</article>

<jsp:include page="/footer.jsp"></jsp:include>
<!-- 设置定位  -->
<script>setActById("aboutInformation");</script>
<!-- 导航栏定位  -->
<script>setActById("indexMessage");</script>
</body>
</html>