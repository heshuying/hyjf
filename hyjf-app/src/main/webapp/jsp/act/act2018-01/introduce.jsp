<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<title>元旦活动</title>
		<style type="text/css">
			* {
				margin: 0;
				padding: 0;
			}
			
			ul,
			li {
				list-style: none;
			}
			
			.active2018-introduce img {
				display: block;
				width: 100%;
			}
			
		</style>
	</head>

	<body>
		<div class="active2018-introduce">
			<img src="${ctx}/img/act201801/active-20180101.jpg" alt="" />
			<img src="${ctx}/img/act201801/active-20180102.jpg" alt="" />
			<img src="${ctx}/img/act201801/active-20180103.jpg" alt="" />
			<img src="${ctx}/img/act201801/active-20180104.jpg" alt="" />
			<img src="${ctx}/img/act201801/active-20180105.jpg" alt="" />
			<img src="${ctx}/img/act201801/active-20180106.jpg" alt="" />
			<img src="${ctx}/img/act201801/active-20180107.jpg" alt="" />
		</div>
	</body>
</html>
<script src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript">
	document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
	$(window).on('resize', function() {
		document.documentElement.style.fontSize = $(document.documentElement).width() / 12.42 + 'px';
	});
</script>