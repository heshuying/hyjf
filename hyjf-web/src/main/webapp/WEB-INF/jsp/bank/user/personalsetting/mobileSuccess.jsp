<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>设置手机号成功 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
           <div class="fxcp-title">
                                       绑定手机
           </div>
            <div class="changetel-content result-content">
            	<input type="hidden" value="${userid }" id="userid"/>
            	<div class="register-top">
            		<img src="${cdn }/dist/images/result/changetel-success.png" alt="" />
            	</div>
            	<div class="register-success result-mid">
            		<p  class="font-text">手机绑定成功！</p>
            	</div>
            	<%-- <div class="result-btn">
            		<a href="${ctx}/user/safe/init.do" class="register-btn import" itemid="bt1">返回设置</a>
            	</div> --%>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
    </body>
</html>