<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
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
    			<form action="" id="loginForm" method="post" autocomplete="off">
    				<input type="submit" style="display: none;" />
    				<div class="login-box">
    					<p>登录  </p>
                        <input type="hidden" name="tokenCheck" id="tokenCheck" value="${tokenGrant}" />
                        <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
    					<div>
    						<input type="text" placeholder="请输入手机号"  class="text" name="loginUserName" id="loginUserName"/>
    					</div>
    					<div>
    						<input type="password" placeholder="请输入密码"  class="text" name="loginPassword" id="loginPassword"/>
    					</div>
    					<div class="error-box" style="margin-top:15px;"></div>
    					<div class="error-time" style="padding-left:5px;color: red;margin-top: 15px;display:none"></div>
    					<div>
    						<input type="checkbox" id="remember"/>
    						<label for="remember">记住密码</label>
    						<a href="${ctx }/user/findpassword/init.do" class="forget">忘记密码</a>
    					</div>
    					
    					<div class="sub">立即登录</div>
    					<p class="register">没有账号？<a href="${ctx }/user/regist/init.do">马上注册</a></p>
    				</div>
    			</form>
    		</div>
    	</div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn }/js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn }/js/jquery.md5.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn }/js/drag.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn }/js/messages_zh.js" type="text/javascript" charset="utf-8"></script>
	<script src="${cdn }/dist/js/lib/jquery.metadata.js" type="text/javascript" charset="utf-8"></script>
    <script src="${cdn }/dist/js/login/login.js?version=${version}"></script>
    <script>
	/*
	  登录访问
	  login_from
	  @params entrance  入口
	*/
    sa && sa.track('login_from',{
   	  entrance: document.referrer   
   	})
   	</script>
</body>
</html>