<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>开通失败 - 汇盈金服官网</title>
<%@ include file="/head.jsp"%>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="kaihu-content result-content">
            	<div class="register-top">
            		<img src="${cdn}/dist/images/result/kaihu-success@2x.png" width="388"  alt="" />
            	</div>
            	<div class="register-success result-mid">
            		<h3>恭喜您开户成功！</h3>
            	</div>
            	<div class="result-btn">
            		<a href="${ctx}/bank/user/transpassword/setPassword.do" class="register-btn import" itemid="bt1">设置交易密码</a>
            	</div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>	
</body>
</html>
