<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
	<title>内容详情</title>
</head>
<style type="text/css">
.text-top{border-bottom: 1px dashed gainsboro;width: 96%;margin: 0 auto;}
.text-top h3{font-size: 18px;margin-top: 20px;}
.text-top p{font-size: 14px;margin-top: -7px;}
html{font-family: "微软雅黑";}
</style>
<body>
	<div class="text-top">
		<h3><c:out value="${msgHistory.msgTitle }"></c:out></h3>
		<p><hyjf:datetime value="${msgHistory.sendTime }"></hyjf:datetime></p>
	</div>
	<!--富文本内容-->
	<div>${msgHistory.msgContent }</div>
	<!--富文本内容结束-->
	<script type="text/javascript" src="${ctx}/js/jquery.min.js" ></script>
	<script>
	var img = $("img");
	var imgH = [];
	var winWidth = $(window).width(); 
	var imgLength = $("img").length;
	for(var i=0;i<imgLength;i++){
		var oldH = img.eq(i).height();
		var oldW = img.eq(i).width();
		var newH = winWidth*oldH/oldW;
		img.eq(i).height(newH);
		img.eq(i).css("width","100%");
	}
	</script>
</body>
</html>