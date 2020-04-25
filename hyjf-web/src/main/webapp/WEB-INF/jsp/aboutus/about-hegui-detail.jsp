<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<style>
	.banner{
		background: url(/dist/images/aboutUs/hegui/detail-banner.jpg);
		max-width: 1920px;
		margin: 0 auto;
		height: 600px;
		background-position:center 0;
	}
	.list{
		padding-top: 1px;
		max-width:1920px;
		margin: 0 auto;
	}
	.list img{
		display: block;
		margin: 0 auto;
		
	}
	.hegui-detail .list .footer{
	 background: url(/dist/images/aboutUs/hegui/detail-footer.png);
	 height:353px;
	 background-position: center;
	}
	.hegui-detail .list .epilogue{
		background: url(/dist/images/aboutUs/hegui/detail7.png);
	 	height:791px;
		background-position: center;
		margin-top: 90px;
	}
</style>
</head>
<body>
	<div class="hegui-detail">
			<div class="banner"></div>
			<div class="list">
				<img src="/dist/images/aboutUs/hegui/detail1.png" alt="" style="margin-top: 35px;"/>
				<img src="/dist/images/aboutUs/hegui/detail2.png" alt="" style="margin-top: 62px;"/>
				<img src="/dist/images/aboutUs/hegui/detail3.png" alt="" style="margin-top: 20px;"/>
				<img src="/dist/images/aboutUs/hegui/detail4.png" alt="" style="margin-top: 105px;"/>
				<img src="/dist/images/aboutUs/hegui/detail5.png" alt="" style="margin-top: 85px;"/>
				<img src="/dist/images/aboutUs/hegui/detail6.png" alt="" style="margin-top: 20px;"/>
				<div class="epilogue"></div>
				<div class="footer"></div>
			</div>
		</div>
</body>
</html>