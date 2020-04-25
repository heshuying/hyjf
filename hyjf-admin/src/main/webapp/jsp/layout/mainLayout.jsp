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
	<title><tiles:getAsString name="pageTitle" /> - 汇盈金服后台管理系统</title>
	<%-- 画面的共通CSS --%>
	<%@include file="/jsp/common/headCss.jsp"%>
	<%-- 各个画面的CSS --%>
	<tiles:insertAttribute name="pageCss" ignore="true" />
	<%-- 上部分共通的JS --%>
	<%@include file="/jsp/common/headJavascript.jsp"%>
	<%-- 消息信息提示通知插件 --%>
	<%@include file="/jsp/common/pluginMessageInfo.jsp"%>
	<%-- 上部分的JS --%>
	<tiles:insertAttribute name="pageGlobalImport" ignore="true" />
</head>
<body>

<!--lockscreen-->
<div class="lockscreen" style="display:none">
	<div class="lock-overlay"></div>
	<div class="logwindow">
		<div class="logwindow-inner" align="center">
			<h3>Locked</h3>
			<div class="iconWrap"><i class="ti-user"></i></div>
			<h5>登录者: ${sessionScope.LOGIN_USER_INFO.username}</h5>
			<div class="input-group" style="width:260px;">
				<input type="text" size="9" placeholder="请输入解锁口令..." class="form-control unlockPWD">
				<span class="input-group-btn">
					<button class="btn btn-primary" id="unlockBtn">
						<i class="fa fa-chevron-right"></i>
					</button>
				</span>
			</div>
		</div>
	</div>
</div>

<div id="app" class="${hasMenu ne 'false' ? '' : 'no-menu' }">
	<%@include file="/jsp/common/pageLeft.jsp"%>
	<div class="app-content">
	<%@include file="/jsp/common/pageTop.jsp"%>
	<%@include file="/jsp/common/pageContent.jsp"%>
	</div>
	<%@include file="/jsp/common/pageFooter.jsp"%>
	<%@include file="/jsp/common/pageSettings.jsp"%>
</div>

<%-- 边界面板 --%>
<tiles:insertAttribute name="asidePanels" ignore="true" />
<%-- 对话框面板 --%>
<tiles:insertAttribute name="dialogPanels" ignore="true" />

</body>

<!-- Javascripts required for this page only -->
<script type="text/javascript" src="${themeRoot}/assets/js/page.admin.js"></script>
<tiles:insertAttribute name="pageJavaScript" ignore="true" />

</html>
