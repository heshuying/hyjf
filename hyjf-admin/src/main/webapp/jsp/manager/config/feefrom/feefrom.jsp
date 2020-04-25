<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,配置设置,充值手续费收取方式配置', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="emailsetting:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="充值手续费收取方式配置" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">充值手续费收取方式配置</h1>
			<span class="mainDescription">本功能可以配置充值手续费收取方式。</span>
		</tiles:putAttribute>
		<tiles:putAttribute name="mainContentinner" type="string">
			<shiro:hasPermission name="feefrom:VIEW"> 
		修改
		<div class="panel panel-white" style="margin: 0">
					<div class="panel-body" style="margin: 0 auto">
						<div class="panel-scroll height-430">
							<form id="mainForm" action="updateAction" method="post" role="form" class="form-horizontal">
								<input type="hidden" name="id" id="id" value="${feeFromForm.id }" />
								<div class="form-group">
									<label class="col-sm-2 control-label" for="title"> 名称 </label>
									<div class="col-sm-10">${feeFromForm.title}</div>
								</div>

								<div class="form-group">
									<label class="col-sm-2 control-label"> <span class="symbol required"></span>收取方式
									</label>
									<div class="col-sm-10">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="siteStatusOn" name="value" datatype="*" value="0" class="event-categories" ${feeFromForm.value == '0' ? 'checked' : ''}> <label for="siteStatusOn"> 向用户收取 </label> <input type="radio" id="siteStatusOff" name="value" datatype="*" value="1" class="event-categories" ${feeFromForm.value == '1' ? 'checked' : ''}> <label for="siteStatusOff"> 向商户收取 </label>
										</div>
									</div>
								</div>

								<div class="form-group margin-bottom-0">
									<div class="col-sm-offset-2 col-sm-10">
										<shiro:hasPermission name="feefrom:MODIFY">
											<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i>修改确认</a>
										</shiro:hasPermission>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/feefrom/feefrom.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
