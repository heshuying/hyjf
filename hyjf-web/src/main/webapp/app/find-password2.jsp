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
	    		<div class="find-pw">
	    			<p class="title">找回密码</p>
	    			<div class="pw-content">
		    			<form action="" id="pwForm">
		    				<label>
		    					<input class="pw-2" type="password" name="pw" id="password" placeholder="重置登录密码" maxlength="16"/>
		    				</label>
		    				<label>
		    					<input class="pw-2" type="password" name="pwSure" placeholder="确认登录密码" maxlength="16"/>
		    				</label>
		    				<a class="sub">下一步</a>
		    			</form>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/lib/jquery.slideunlock.min.js"></script>
    <script src="../dist/js/login/find-password-2.js"></script>
</body>
</html>