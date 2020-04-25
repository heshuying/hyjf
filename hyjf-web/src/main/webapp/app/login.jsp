<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/headerWithoutNav.jsp"></jsp:include>
	<article>
    	<div class="login-content">
    		<div class="container">
    			<form action="" id="loginForm">
    				<div class="login-box">
    					<p>登录</p>
    					<div>
    						<input type="text" placeholder="请输入手机号或用户名" class="text" name="telnum"/>
    					</div>
    					<div>
    						<input type="password" placeholder="请输入密码" class="text" name="password"/>
    					</div>
    					<div>
    						<input type="checkbox" id="remember"/>
    						<label for="remember">记住密码</label>
    						<a href="#" class="forget">忘记密码</a>
    					</div>
    					<div class="error-box"></div>
    					<a class="sub">立即登陆</a>
    					<p class="register">没有账号？<a href="#">马上注册</a></p>
    				</div>
    			</form>
    		</div>
    	</div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
    <script src="../dist/js/login/login.js"></script>
</body>
</html>