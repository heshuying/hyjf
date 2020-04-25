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
				<h5>支付成功！</h5>
				<p>恭喜您成功加入会员Club，欢迎礼包已飞入您的账户</p>
			</div>
			<div class="process">
					<div class="process_line btn_bg_color"><a a href="hyjf://jumpMine/?" id="vipDetail">立即查看</a></div>
			</div>
		</div>
	</body>
</html>