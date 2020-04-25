<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
<title>二维码</title>
</head>
<body class="friend-code">
		<div>
			<p class="friend-code-title">请您的好友扫描以下二维码</p>
			<div class="friend-code-div">
				<!-- <img src="img/firend-code.jpg" class="friend-code-img-1"/> -->
				<input type="hidden" id="qrcodeValue" value="${qrcode}" />
				<div id="qrcode" class="friend-code-img-1"></div>
					<c:choose>
					<c:when test="${ empty iconUrl}">
					<img src="${ctx}/img/LOGO2.png?version=20171124" class="friend-code-img-2" style="width:28%"/>
					</c:when>
					<c:otherwise>
					<img src="${iconUrl }" class="friend-code-img-2"/>
					</c:otherwise>
				</c:choose>
			</div>
			<p>说明：扫描成功后，您的好友即可在打开的链接中根据提示完成注册等相关操作</p>
		</div>
		<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.qrcode.js"></script>
		<script type="text/javascript" src="${ctx}/js/qrcode.js"></script>
		<script type="text/javascript" src="${ctx}/js/code.js"></script>
		<script type="text/javascript">
		window.onload = function(){
			var img = document.querySelector(".friend-code-img-2");
			var width = img.offsetWidth/2;
			var height = img.offsetHeight/2;
			img.style.marginLeft = -width+"px";
			img.style.marginTop = -height+"px";
		}  
		</script>
	</body>
</html>