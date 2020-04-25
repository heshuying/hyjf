<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>赎回成功 - 汇盈金服官网</title>
	<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
		<div class="shSuccess-Top">
		    <span>汇天利赎回</span>
		</div>
		<div class="shSuccess-Ban">
			<h4>恭喜您，赎回成功!</h4>
			<p>
				您已成功从账户转出
				<span class="shSuccess-cont">360.00</span>
				元
		    </p>
			<p>
				系统将在
			    <span class="num-jump">3</span>
				秒后自动跳转，如果没有请点击
				<a class="hand-jump" href="#" target="_blank">手动跳转 &gt;&gt;</a>
		    </p>
			<div class="shSuccess-click">
			    <a href="${ctx }/htlgetUserProductInfo.do" class="inputBg">查看详情</a>
			</div>
		</div>
		<jsp:include page="/footer.jsp"></jsp:include>
	</body>
</html>