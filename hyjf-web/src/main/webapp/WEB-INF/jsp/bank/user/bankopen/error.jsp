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
            <div class="result-content">
            	<div class="result-top">
            		<img src="${cdn}/dist/images/result/kaihu-failure@2x.png" width="124" alt="" />
            	</div>
            	<div class="result-mid">
            		<h3>很遗憾开户失败！</h3>
            		<p></p>
            	</div>
            	<div class="result-add">
            		<p>失败原因：${message}</p>
            		
            	</div>
            	<div class="result-btn">
            		<a href="${ctx}/bank/web/user/bankopen/init.do" class="register-btn import" itemid="bt1">返回重试</a>
            	</div>
            	<p class="failure-reason"></p>
            </div>
            <div class="open-fail-prompt">
        		*温馨提示<br />
				当平台注册手机号与银行卡预留手机号不一致导致开户失败时，您可以进行如下操作：<br />
				1、将平台注册手机号修改为银行预留手机号（我的-个人中心-修改手机号）；<br />
				2、重新进行开户。
        	</div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>	

</body>
</html>
