<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/head.jsp"></jsp:include>
<title>绑卡成功 - 汇盈金服官网</title>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<section class="main-content">
		<div class="container result">
            <div class="register-content result-content">
            	<div class="register-top" style="margin: 30px 0">
            		<img src="${cdn}/dist/images/result/success-img@2x.png" width="126" alt="" style="margin-right:-20px" />
            	</div>
            	<div class="register-success result-mid">
            		<h3>绑卡成功</h3>
            	</div>
            	<c:if test="${urlstatus==null}">
            		<div class="result-btn">
	            		<a href="${ctx}/" class="register-btn import">返回首页</a>
	            	</div>
            	</c:if>
            	
            	<c:if test="${urlstatus!=null}">
            		<div class="result-btn">
	            		<a href="${url}" class="register-btn import">${buttonName}</a>
	            	</div>
            	</c:if>
            	
            </div>
        </div>
    </section>
	
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>