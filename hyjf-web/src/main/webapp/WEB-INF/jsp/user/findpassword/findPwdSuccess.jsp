<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/head.jsp"></jsp:include>
<title>找回密码成功 - 汇盈金服官网</title>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
    	<article class="main-content">
	    	<div class="container result">
	    		<!-- start 内容区域 -->
	    		<div class="account-set-up">
	    			<p class="title">找回密码</p>
	    			<div class="set-content">
	    				<img src="${cdn }/dist/images/acc-set/set-pw@2x.png" width="355" class="success-img"/>
	    				<p class="success-word">密码找回成功！</p>
	    			</div>
		    		<div class="result-btn P-mtp20">
	            		<a href="${ctx }/user/login/init.do" class="register-btn import" itemid="bt1">立即登录</a>
	            	</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>