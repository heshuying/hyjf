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
	    			<p class="title">自动投标授权</p>
	    			<div class="set-content">
		    			<form action="" id="telForm1">
		    				<label class="no-border">
		    					<span class="tit">手机号　</span>
		    					<span>187****6587</span>
		    					<input type="hidden" name="newRegPhoneNum" id="telnum1"/>
		    				</label>
		    				<label>
		    					<span class="tit">手机验证</span>
		    					<input type="text" name="newRegVerify" maxlength="8" class="code" placeholder="请输入验证码"/>
		    					<span class="get-code"><em class="rule"></em>获取验证码</span>
		    				</label>
		    				<a class="sub">下一步</a>
		    				<div class="reminder">
		    					<h3><span class="icon iconfont icon-bulb"></span>温馨提示：<span class="word">若手机号码停用，请联系客服解决</span></h3>
		    				</div>
		    			</form>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/acc-set/authorization.js"></script>
</body>
</html>