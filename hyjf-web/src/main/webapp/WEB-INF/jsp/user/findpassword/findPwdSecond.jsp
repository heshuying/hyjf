<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
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
		    			<form action="${ctx}/user/findpassword/successPage.do" id="pwForm" method="post">
                            <input type="hidden" name="tokenCheck" id="tokenCheck" value="${tokenGrant}" />
                            <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
		    				<input  type="hidden" name="telnum" id="telnum" value="${telnum}"/>
		    				<input  type="hidden" name="code" id="code" value="${code}"/>
		    				<input type="hidden" id="pubmodules" value="${pubmodules }" name="pubmodules" />
							<input type="hidden" id="pubexponent" value="${pubexponent }" name="pubexponent" />
							<input type="hidden" id="password1" value="" name="password1" />
							<input type="hidden" id="password2" value="" name="password2" />
		    				<label>
		    					<span class="tit">新密码</span>
		    					<input type="password" name="repassword1" id="repassword1" placeholder="重置登录密码" maxlength="16" onkeyup="this.value=this.value.replace(/\s+/g,'')"/>
		    				</label>
		    				<div class="strength-box" style="display: none;">
		    					<div class="strip"></div>
		    					<div class="strip"></div>
		    					<div class="strip"></div>
		    					<span></span>
		    				</div>
		    				<label>
		    					<span class="tit">确认密码</span>
		    					<input type="password" name="repassword2" id="repassword2" placeholder="确认登录密码" maxlength="16"/>
		    				</label>
		    				<a class="sub">下一步</a>
		    			</form>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn }/dist/js/login/RSA.js?version=${version}"></script>
    <script src="${cdn }/dist/js/login/Barrett.js?version=${version}"></script>
    <script src="${cdn }/dist/js/login/BigInt.js?version=${version}"></script>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn}/dist/js/lib/jquery.slideunlock.min.js"></script>
    <script src="${cdn}/dist/js/login/find-password-2.js?version=${version}"></script>
</body>
</html>