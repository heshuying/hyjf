<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>申请失败 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
		<div class="hjkS_Top">
		    <span>融资汇</span>
		    <p>资金一站到位，融资期限，融资形式任选</p>
		</div>
		<div class="hjkS_Ban">
			<div class="hjkS_Banner">
			    <div class="hjkS_left">
			    	<img src="${ctx}/img/jk_sucess02.jpg">
			    </div>
			    <div class="hjkS_right">
			        <span>您的借款申请提交失败！</span>
			        <p>${msg} }……………………………………………………<br>时间与您联系，请您保持通话畅通。</p>
			        <a href="${ctx}/rzh/apply/toIndexPage.do">返回首页</a>
			    </div>
			</div>
	   </div>
	   <jsp:include page="/footer.jsp"></jsp:include>
	</body>
</html>