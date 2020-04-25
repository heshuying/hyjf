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
	    		<div class="account-set-up-2">
	    			<p class="title">绑定手机</p>
	    			<div class="set-content">
		    			<form action="" id="telForm">
		    				<label>
		    					<span class="tit">手机号</span>
		    					<input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" name="newRegPhoneNum" id="telnum" placeholder="请输入手机号"/>
		    				</label>
		    				<div id="slide-box">
			    				<div id="slideUnlock">
							        <input type="hidden" value="" id="lockable" name="lockable"/>
							        <div id="slider">
							            <span id="label"><i></i></span>
							            <span class="slideunlock-lable-tip">移动滑块验证</span>
							            <span id="slide-process"></span>
							        </div>
							        
							    </div>
						    </div>
		    				<label>
		    					<span class="tit">手机验证</span>
		    					<input type="text" name="newRegVerify" maxlength="8" placeholder="请输入验证码"/>
		    					<span class="get-code disable"><em class="rule"></em>获取验证码</span>
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
    <script src="../dist/js/acc-set/account-tel-binding.js"></script>
</body>
</html>