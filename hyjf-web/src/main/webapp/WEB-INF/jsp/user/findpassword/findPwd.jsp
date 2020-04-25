<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
	    			<form action="${ctx}/user/findpassword/sencodPage.do" method="post" id="pwForm">
	    				<input type="hidden" name="validCodeType" id="validCodeType" value="TPL_ZHAOHUIMIMA" />
						<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
						<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
	    				<label>
	    					<span class="tit">手机号</span>
	    					<input type="text" name="telnum" id="telnum" placeholder="请输入手机号"/>
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
	    					<input type="text" name="code"  id="code" maxlength="6" placeholder="请输入验证码"/>
	    					<span class="get-code disable"><em class="rule"></em>获取验证码</span>
	    				</label>
	    				<a class="sub" id="validform2">下一步</a>
	    			</form>
    			</div>
		    </div>
    		<!-- end 内容区域 --> 
    	</div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn }/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn }/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn }/dist/js/lib/jquery.slideunlock.min.js"></script>
    <script src="${cdn }/dist/js/login/find-password.js?version=${version}"></script>
</body>
</html>