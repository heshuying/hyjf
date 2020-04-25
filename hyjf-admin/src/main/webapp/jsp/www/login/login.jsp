<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<!DOCTYPE html>
<!--[if IE 8]><html class="ie8" lang="en"><![endif]-->
<!--[if IE 9]><html class="ie9" lang="en"><![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<head>
	<%-- 画面的共通MATE --%>
	<%@include file="/jsp/common/headMetas.jsp"%>
	<%-- 浏览器左上角显示的文字 --%>
	<title>系统登录 - 汇盈金服</title>
	<%-- 画面的共通CSS --%>
	<%@include file="/jsp/common/headCss.jsp"%>
	<%-- 各个画面的CSS --%>
	<link href="${themeRoot}/assets/admin/css/login.css" type="text/css" rel="stylesheet" />
	<%-- 上部分共通的JS --%>
	<%@include file="/jsp/common/headJavascript.jsp"%>
	<%-- 消息信息提示通知插件 --%>
	<%@include file="/jsp/common/pluginMessageInfo.jsp"%>
</head>
<body class="login">

	<div class="main-login">
		<div class="logo margin-top-0 margin-bottom-10">
			<img src="${themeRoot}/images/admin/logov2.png" alt="汇盈金服"/>
		</div>
		<!-- start: LOGIN BOX -->
		<div class="box-login">
			<form id="mainForm" class="form-login" action="login" method="post">
				<div class="form-group">
					<span class="input-icon">
						<input type="text" class="form-control radius-5" name="username" placeholder="请输入用户名" id="username"  />
						<i class="fa fa-user"></i> </span>
					<span class="fa fa-user1"> ${retTime} </span>
					<hyjf:validmessage key="username-required" message="errors.required" label="ID" ></hyjf:validmessage>
				</div>
				<div class="form-group form-actions padding-top-0 margin-top-0 margin-bottom-0">
					<span class="input-icon">
						<input type="password" class="form-control password radius-5" name="password" placeholder="请输入密码" id="password" />
						<i class="fa fa-lock"></i>
					</span>
					<span class="fa fa-lock1">  </span>
				</div>
				<div class="form-group form-actions padding-top-0 margin-top-0 margin-bottom-0">
					<input type="text" id="validateCode" class="form-control" style="width: 60%;display: inline;height:35px" placeholder="请输入验证码"  name="validateCode" >&nbsp;&nbsp;&nbsp;
					<img src="" id="imgVerify" width="100px" height="35px" onclick="getVerify(this)" style="cursor:pointer;"/>
					<span class="fa fa-validateCode1">  </span>
				</div>
				<div class="form-actions margin-top-0 padding-top-5">
					<div class="checkbox clip-check check-primary inline">
						<input type="checkbox" id="remember" name="remember" value="1">
						<label for="remember" style="color:#4db9f9;font-size:13px">
							记住密码
						</label>
					</div>
					<a class="inline" href="${webRoot}/www/forgotController/init.do" style="font-size:13px; color:#b2b6ad">
						忘记密码？
					</a>
				</div>
				<!-- BEGIN ERROR MESSAGE -->
				<hyjf:message key="error" message=""></hyjf:message>
				<!-- END ERROR MESSAGE -->
				<button type="button" class="btn btn-yellow btn-block margin-top-10 padding-top-10 padding-bottom-10 btn-Login">
					登录
				</button>
			</form>
		</div>
		<!-- end: LOGIN BOX -->
	</div>
</body>

<script>
$(".input-icon input").focus(function() {
	$(this).parent().addClass("focus");
}).blur(function() {
	$(this).parent().removeClass("focus");
})
.first().focus();

function getVerify(obj){
    var date = new Date();
    obj.src = "/hyjf-admin/randomValidateCode?"+date.getTime();
}
</script>
<script type="text/javascript" src="${themeRoot}/vendor/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.cookie.min.js"></script>
<script type='text/javascript' src="${webRoot}/jsp/www/login/login.js"></script>
</html>
