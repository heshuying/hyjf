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
		<title>江西银行</title>
	</head>
	<body>
		<input type="hidden" name="sign" id="sign" value="${sign }" />
		<input type="hidden" name="token" id="token" value="${token }" />
		
		<div class="container specialFont response">
			<div class="outcome">
				<div class="outcome_icon"><i class="icon-ok-circle"></i></div>
				<h5>${statusDesc}</h5>
			</div>
			<div class="process">
				<%-- <c:if test="${ activityFlg }">
				<div>
					<p class="active-tip"><c:out value="${ activityMsg }" /></p>
					<p class="active-join"><a href="${financialUrl }" >立即参与&gt;&gt; </a></p>
				</div>
				</c:if> --%>
			
				<div class="process_line btn_bg_color"><a href="javascript:void(0)" class="hy-jumpH5">去设置交易密码</a></div>
				<c:if test="${not empty gesture}">
					<c:if test="${gesture eq 0}">
						<div class="process_line btn_bg_color"><a href="#" class="hy-jumpSetPassword">设置手势密码</a></div>
					</c:if>
				</c:if>
				<%-- <c:if test="${empty gesture}">
					<div class="process_line btn_bg_color"><a href="#" class="hy-jumpRecharge">立即充值，开始赚钱</a></div>
				</c:if> --%>
			</div>
		</div>
		<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script  type="text/javascript">
			var sign = $("#sign").val();
			var token =$("#token").val();
			jumpH5("/hyjf-app/bank/user/transpassword/setPassword.do?sign="+sign+"&token="+token,0,1)
			
			setupWebViewJavascriptBridge(function(bridge) {
				/* Initialize your app here */
				bridge.callHandler('openAccountSucceed', function responseCallback(responseData) {
					console.log("JS received response:", responseData)
				})
			})
		</script>
	</body>
</html>