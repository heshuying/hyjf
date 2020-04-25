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
	    		<div class="account-set-up">
	    			<p class="title">修改登录密码</p>
	    			<div class="set-content">
		    			<form action="" id="setPwForm">
		    				<label>
		    					<span class="tit">原密码</span>
		    					<input type="password" name="oldPassword" id="oldPassword" placeholder="请输入密码" maxlength="16"/>
		    				</label>
		    				<label>
		    					<span class="tit">新密码</span>
		    					<input type="password" name="new" id="password" placeholder="请输入新密码" maxlength="16"/>
		    				</label>
		    				<label>
		    					<span class="tit">确认新密码</span>
		    					<input type="password" name="pwSure" id="surePassword" placeholder="请确认新密码" maxlength="16"/>
		    				</label>
		    				<a class="sub">提交</a>
		    			</form>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>