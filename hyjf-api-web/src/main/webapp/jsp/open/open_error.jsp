<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.net.URLDecoder" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

  <%@include file="/jsp/base/pageBase.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/dist/css/page.css"/>
	<title></title>
</head>
<body class="response zz-outcome">
	<div class="container specialFont response">
		<div class="outcome">
			<div class="outcome-icon"><i class="icon iconfont icon-cha fail"></i></div>
			<h5>开户失败</h5>
			<p style="font-size:10px" class="color898989">失败原因：${statusDesc}</p>
		</div>
		<div class="process process-2">
			<div class="process_line process_line_border fl"><a href="" class="color108ee9 rdf-callCener">咨询客服</a></div>
			<div class="process_line btn_bg_color fr"><a href="" class="colorWhite rdf-jumpMine">完成</a></div>
		</div>
	</div>
	<script src="${ctx}/dist/js/lib/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/dist/js/common.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>