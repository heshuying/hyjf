<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/headerWithoutNav.jsp"></jsp:include>
	    <section class="main-content">
        <div class="container result">
            <div class="fast-sq">
           		<p class="tit">快速授权</p>
           		<p class="welcome">欢迎使用汇盈金服账户进行安全授权，并同时登录汇盈金服</p>
           		<img src="../dist/images/bond/failure-img.png" alt="" class="failure" />
           		<p class="message">帐号授权出问题啦！</p>
           		<a class="btn-sq">返回重试</a>
            </div>
        </div>
    </section>
	<script src="${ctx}/dist/js/lib/jquery.min.js"></script>
	<script src="${ctx}/dist/js/lib/jquery-migrate-1.2.1.js"></script>
	<script src="${ctx}/dist/js/lib/echarts.common.min.js"></script>
	<script src="${ctx}/dist/js/lib/jquery.placeholder.min.js"></script>
	<script src="${ctx}/dist/js/lib/nprogress.js"></script>	
	<script src="${ctx}/dist/js/utils.js"></script>
</body>
</html>