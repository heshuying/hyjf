<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title></title>
	</head>
	<body>
		<div class="container specialFont response">
			<div class="outcome">
				<div class="outcome_icon"><i class="icon-ok-circle"></i></div>
				<h5>转入成功！</h5>
				<p><span>转入<i id="money"><c:out value="${trsAmount}"/></i>元,&nbsp;<c:out value="${inDay}"/>开始计算收益</span></p>
			</div>
			<div class="process">
				<div class="process_line btn_bg_color"><a href="hyjf://jumpHTL/?">完成</a></div>
			</div>
		</div>
		<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/num.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
		$(function(){
			var _val=$("#money").text().toString();
			$("#money").text(formatNum(_val));
		})
		
		</script>
		
	</body>
</html>