<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<div class="app-download">
		<div class="banner-1">
			<div class="contant">
				<header class="app-header">
					<a class="logo" href="${ctx}/"><img src="${ctx}/dist/images/logo.png"/></a>
					<div class="top-right"><span class="icon iconfont icon-dianhua"></span> 客服电话：400-900-7878（服务时间 9:00-18:00）</div>
				</header>
				<div class="app-down">
					<img src="${ctx}/dist/images/app-download/tit-word.png"/>
					<div class="down-box">
						<div>
							<a href="https://itunes.apple.com/cn/app/hui-ying-jin-fu/id1044961717?mt=8"><img src="${ctx}/dist/images/app-download/iphone-download.jpg"  /></a>
							<a href="http://img.hyjf.com/data/download/com.huiyingdai.apptest_wangye.apk"><img src="${ctx}/dist/images/app-download/android-download.jpg"  /></a>
						</div>
						<img src="${ctx}/dist/images/app-download/qr-code.png" class="qr-code"/>
					</div>
				</div>
				<img src="${ctx}/dist/images/app-download/banner-img.png" class="banner-img-1"/>
			</div>
		</div>
		<div class="banner-2">
			<div class="contant">
				<img  src="${ctx}/dist/images/app-download/banner-img-2.jpg"/>
			</div>
		</div>
		<div class="banner-3">
			<div class="contant">
				<img  src="${ctx}/dist/images/app-download/banner-img-3.jpg"/>
			</div>
		</div>
	</div>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>