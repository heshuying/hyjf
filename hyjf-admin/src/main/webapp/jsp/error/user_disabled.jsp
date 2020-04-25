<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>


<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="false" scope="request"></c:set>
<c:set var="hasMenu" value="false" scope="request"></c:set>

<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="禁用" />

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">

	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="text-extra-large">
			<i class="fa fa-exclamation-circle fa-2x margin-right-5"></i>
			<span class="">您的账户已经被禁用，请返回首页重新登录！</span>
			<div align="center" class="padding-top-20">
				<a href="${webRoot}/login/loginOut" class="btn btn-o btn-squared btn-wide btn-info">返回登录 <i class="fa fa-arrow-circle-right"></i></a>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
		.main-content {
			position: absolute;
			top: 65px;
			left: 71px;
			right: 0;
			bottom: 35px;
		}
		.text-extra-large {
			font-family: "微软雅黑";
			position: absolute;
			left: 50%;
			top: 50%;
			margin-top: -100px;
			margin-left: -155px;
		}
		.text-extra-large i {
			color: #46b8da;
		}
	</style>
	</tiles:putAttribute>

	<%-- JS插件补丁 (ignore) --%>
	<tiles:putAttribute name="pageJsPatch" type="string">
		<script>window.top && window.top != window && window.top.location.reload();</script>
	</tiles:putAttribute>

</tiles:insertTemplate>
