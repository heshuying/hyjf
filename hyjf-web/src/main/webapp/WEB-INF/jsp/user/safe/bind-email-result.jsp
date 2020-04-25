<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/head.jsp"></jsp:include>
<title>绑定邮箱成功 - 汇盈金服官网</title>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
		<c:choose>
			<c:when test="${status==true }">
			<!-- start 内容区域 -->
	    		<div class="account-set-up">
	    			<p class="title">邮箱认证</p>
	    			<div class="set-content P-mtp">
	    				<img src="${cdn}/dist/images/result/setemail-success.png" alt="" />
	    				<p class="success-word">绑定邮箱成功！</p>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
			</c:when>
			<c:otherwise>
           	<!-- start 内容区域 -->
    		<div class="account-set-up">
    			<p class="title">邮箱认证</p>
    			<div class="set-content">
    				<img src="${cdn}/dist/images/result/setemail-error@2x.png" alt="" />
    				<p class="success-word">绑定邮箱失败！</p>
    			</div>
	           	<div class="result-add">
	           		<p>失败原因 ：${error }</p>
	           	</div>
	           	<div class="result-btn">
	           		<a href="${ctx}/user/safe/bindingEmail.do" class="register-btn import" itemid="bt1">返回重试</a>
	           	</div>
		    </div>
    		<!-- end 内容区域 --> 
			</c:otherwise>
		</c:choose>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
</body>
</html>