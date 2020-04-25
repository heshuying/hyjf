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
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
 	    <article class="main-content">
	    	<div class="container result">
	    		<!-- start 内容区域 -->
	    		<div class="account-set-up">
	    			<p class="title">修改登录密码</p>

	    			<div class="set-content">
		    			<form action="${ctx}/user/safe/updatePassword.do" name="setPwForm" id="setPwForm" method="post">
                            <input type="hidden" name="tokenCheck" id="tokenCheck" value="${tokenGrant}" />
                            <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
		    				<input type="hidden" name="userId" id="userId">
		    				<input type="hidden" id="pubmodules" value="${pubmodules }" name="pubmodules" />
							<input type="hidden" id="pubexponent" value="${pubexponent }" name="pubexponent" />
							<input type="hidden" id="oldPassword" value="" name="oldPassword" />
							<input type="hidden" id="newPw" value="" name="newPw" />
							<input type="hidden" id="pwSure" value="" name="pwSure" />
		    				<label>
		    					<span class="tit">原密码</span>
		    					<input type="password" name="oldPword" id="oldPword" placeholder="请输入密码" maxlength="16"/>
		    				</label>
		    				<label>
		    					<span class="tit">新密码</span>
		    					<input type="password" name="newrsaPw" id="password" placeholder="请输入新密码" maxlength="16" onkeyup="this.value=this.value.replace(/\s+/g,'')"/>
		    				</label>
		    				<div class="strength-box" style="display: none;">
		    					<div class="strip"></div>
		    					<div class="strip"></div>
		    					<div class="strip"></div>
		    					<span></span>
		    				</div>
		    				<label>
		    					<span class="tit">确认新密码</span>
		    					<input type="password" name="pwrsaSure" id="surePassword" placeholder="请确认新密码" maxlength="16"/>
		    				</label>
		    				<a class="sub">提交</a>
		    			</form>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
    <script src="${cdn }/dist/js/login/RSA.js?version=${version}"></script>
    <script src="${cdn }/dist/js/login/Barrett.js?version=${version}"></script>
    <script src="${cdn }/dist/js/login/BigInt.js?version=${version}"></script>
	<script src="${cdn }/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn }/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn }/dist/js/lib/jquery.slideunlock.min.js"></script>
    <script src="${cdn }/dist/js/acc-set/account-password.js?version=${version}"></script>
    
</body>
</html>