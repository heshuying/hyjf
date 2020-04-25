<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/head.jsp"></jsp:include>
<title>注册成功 - 真诚透明自律的互联网金融服务平台</title>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="sucess-content" style="padding-bottom:1px">
            	<video src="${ctx }/dist/images/hongbao.mov" autoplay  style="height:284px;width:450px">
            		<img src="${cdn }/dist/images/login/login-success-logo.png" />
            	</video>
            	<p>您已获得<a> 888元 </a>新手礼包，您可在<a href="${ctx}/user/invite/toInvite.do">“我的奖励”</a>中查看</p>
            	<img src="${cdn}/dist/images/login/readyflow-2@2x.png" width="278px"/>
            	<div class="btn-box"><a href="${ctx }/user/regist/businessUsersGuide.do">企业开户指南</a></div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${cdn}/js/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.metadata.js" charset="utf-8"></script>
	<script>
 	</script>
</body>
</html>