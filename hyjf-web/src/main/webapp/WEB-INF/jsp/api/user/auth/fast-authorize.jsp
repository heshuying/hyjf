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
	<jsp:include page="/headerWithoutNav.jsp"></jsp:include>
	 <section class="main-content">
        <div class="container result">
            <div class="fast-sq">
            	<form action="" id="fastForm">
	           		<p class="tit">快速授权</p>
	           		<p class="welcome">欢迎使用汇盈金服账户进行安全授权，并同时登录汇盈金服</p>
	           		<img src="${cdn}/dist/images/acc-set/account-profile@2x.png" width="114"  class="head" />
	           		<p class="name">hyjf177732</p>
	           		<div class="check"><label><input type="checkbox" name="" id="readAgreement" checked/><span>我已阅读并同意</span></label><a href="#">《汇盈金服授权协议》</a></div>
	           		<a class="btn-sq">立即授权</a>
	           		<div class="otherway"><a href="">使用账号密码授权</a><a href="">注册新账号</a></div>
           		</form>
            </div>
        </div>
    </section>
	<script src="${cdn}/dist/js/lib/jquery.min.js"></script>
	<script src="${cdn}/dist/js/lib/jquery-migrate-1.2.1.js"></script>
	<script src="${cdn}/dist/js/lib/jquery.placeholder.min.js"></script>
	<script src="${cdn}/dist/js/lib/nprogress.js"></script>	
	<script src="${cdn}/dist/js/utils.js"></script>
    <script src="${cdn}/dist/js/login/fast-authorize.js"></script>
</body>
</html>