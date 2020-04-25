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
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
	    	<div class="container result">
	    		<!-- start 内容区域 -->
	    		<div class="register">
	    			<p class="go-login">已有账号？<a href="">马上登录</a></p>
	    			<div class="re-content">
		    			<form action="" id="registerForm">
		    				<label>
		    					<span class="tit">手机号</span>
		    					<input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" name="telnum" id="telnum" placeholder="输入手机号"/>
		    				</label>
		    				<label>
		    					<span class="tit">密码</span>
		    					<input type="password" name="password" placeholder="6-16位字母和数字组成"/>
		    				</label>
		    				<div id="slide-box">
			    				<div id="slideUnlock">
							        <input type="hidden" value="" name="lockable" id="lockable"/>
							        <div id="slider">
							            <span id="label"></span>
							            <span class="slideunlock-lable-tip">移动滑块验证</span>
							            <span id="slide-process"></span>
							        </div>
							        
							    </div>
						    </div>
		    				<label>
		    					<span class="tit">手机验证</span>
		    					<input type="text" name="code" maxlength="6" placeholder="输入验证码"/>
		    					<span class="get-code disable"><em class="rule"></em>获取验证码</span>
		    				</label>
		    				<label>
		    					<span class="tit">推荐人</span>
		    					<input type="text" placeholder="输入推荐人手机号或推荐码"/>
		    				</label>
		    				<div class="read-protocol">
		    					<input type="checkbox" value="1" id="protocol"/>
		    					<span>我已阅读并同意 <a href="">《网站注册协议》</a></span>
		    				</div>
		    				<a class="sub disable">注册</a>
		    			</form>
	    			</div>
	    			<a class="red-banner" href="">
	    				<img src="http://img.hyjf.com/dist/images/login/banner.jpg">
	    			</a>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/lib/jquery.slideunlock.min.js"></script>
    <script src="../dist/js/login/register.js"></script>
</body>
</html>