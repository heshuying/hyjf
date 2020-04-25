<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>系统异常</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="result-content">
            	<div class="result-top">
            		<img src="${ctx}/dist/images/result/exce@2x.png" alt="" width="223px"/>
            	</div>
            	<div class="register-mid">
            		<p>系统异常 请稍后重试</p>
            	</div>
            	<div class="result-btn">
            		<div class="result-left">
            			<a href="${ctx}/" class="import" itemid="bt1">返回首页</a>
            		</div>
            		<div class="result-right">
            			<a target="_blank" href="http://b.qq.com/webc.htm?new=0&sid=4000655000&eid=218808P8z8p8p8z8K8p80&o=www.huiyingdai.com&q=7" itemid="bt2">联系在线客服</a>
            		</div>
            		<div class="clearboth"></div>
            	</div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>	
	<script>setActById('accountSet');</script>
	</body>
</html>