<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>年终集结史无前利</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/active/active-20161202.css" />
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
		<div class="content-activity">
			<span class="ay02"></span>
			<span class="ay03"></span>
			<span class="ay04"></span>
			<span class="ay05"></span>
			<span class="ay06"></span>
			<span class="ay08"></span>
			<span class="ay010"></span>
			<span class="ay011"></span>
			<span class="ay013"></span>
			<span class="ay014"></span>
			<span class="ay015"></span>
			<span class="ay016"></span>
			<span class="ay019"></span>
			<span class="bo01">
				<b>
					<a href="${ctx}/project/initProjectList.do?projectType=HZT" class="left">投资汇直投项目≥20000元</a>
					<a href="${ctx}/user/regist/init.do?utm_id=2122" class="right">注册开户即送</a>
				</b>
			</span>
			<div class="clear"></div>
			<span class="bo02"></span>
			<span class="bo03"></span>
			<span class="bo04"></span>
			<span class="bo05"></span>
			<span class="bo06"></span>
			<span class="bo07"></span>
			<span class="bo08"></span>
		</div>
		<a href = "http://www.wdzj.com/zhuanti/pc2016christmas/">
			<img src="${ctx}/css/images/active_201612/oldMan.png" class="merry" id = "merry"/>
		</a>
		
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript">
		$(function(){
			$("#merry").animate({
				top:"300px",
			},1200);
			$("#merry").animate({
				top:"270px",
			},500);
		})
	</script>
	
	</body>
</html>