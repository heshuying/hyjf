<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<c:set value="${fn:split('汇盈金服,配置设置,网站设置', ',')}" var="functionPaths"
	scope="request"></c:set>

<shiro:hasPermission name="sitesetting:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="网站设置" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">网站设置</h1>
			<span class="mainDescription">本功能可以增加修改删除网站信息。</span>
		</tiles:putAttribute>
		<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty sitesettingForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty sitesettingForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%-- 活动列表一览 --%>
					<input type="hidden" name="id" id="id" value="${sitesettingForm.id }" />
					<div class="form-group">
						<label class="col-sm-2 control-label" for="company"> <span class="symbol required"></span>公司名称 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="公司名" id="company" name="company" value="${sitesettingForm.company}"  class="form-control"
								datatype="*2-20" errormsg="公司名只能是字符汉字，长度2~20个字符！" >
							<hyjf:validmessage key="company" label="公司名"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="siteName"> <span class="symbol required"></span>网站标题 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="网站标题" id="siteName" name="siteName" value="${sitesettingForm.siteName}"  class="form-control"
								datatype="*2-50" errormsg="网站标题 只能是字符汉字，长度2~50个字符！" >
							<hyjf:validmessage key="siteName" label="网站标题"></hyjf:validmessage>
						</div>
					</div>
						<div class="form-group">
						<label class="col-sm-2 control-label" for="siteDescription"> <span class="symbol"></span>网站描述 </label>
						<div class="col-sm-10">
							<textarea placeholder="网站描述 " id="siteDescription" name="siteDescription" class="form-control limited">${sitesettingForm.siteDescription}</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="platform"> <span class="symbol required"></span>网站关键字 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="网站关键字" id="siteKeyword" name="siteKeyword" value="${sitesettingForm.siteKeyword}"  class="form-control"
								datatype="*2-100" errormsg="网站关键字 只能是字符汉字，长度2~100个字符！" >
							<hyjf:validmessage key="siteKeyword" label="网站关键字"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="platform"> <span class="symbol required"></span>网站备案号 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="网站备案号" id="siteIcp" name="siteIcp" value="${sitesettingForm.siteIcp}"  class="form-control"
								datatype="s2-20" errormsg="网站备案号 只能是字符汉字，长度2~20个字符！" >
							<hyjf:validmessage key="siteIcp" label="网站备案号"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="platform"> <span class="symbol required"></span>模板样式路径 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="模板样式路径" id="siteThemePath" name="siteThemePath" value="${sitesettingForm.siteThemePath}"  class="form-control"
								datatype="*2-100" errormsg="模板样式路径 只能是字符汉字，长度2~100个字符！" >
							<hyjf:validmessage key="siteThemePath" label="模板样式路径"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="platform"> <span class="symbol required"></span>版权声明 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="版权声明" id="siteFooter" name="siteFooter" value="${sitesettingForm.siteFooter}"  class="form-control"
								datatype="*2-50" errormsg="版权声明 只能是字符汉字，长度2~50个字符！" >
							<hyjf:validmessage key="siteFooter" label="版权声明"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="platform">客服电话</label>
						<div class="col-sm-10">
							<input type="text" placeholder="客服电话" id="servicePhoneNumber" name="servicePhoneNumber" maxlength="20" value="${sitesettingForm.servicePhoneNumber}"  class="form-control"
								datatype="n5-20" errormsg="客服电话 只能是数字类型，长度5~20个数字！" ignore="ignore">
							<hyjf:validmessage key="servicePhoneNumber" label="客服电话"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-sm-offset-2 col-sm-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i>修改确认</a>
						</div>
					</div>
				</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
				<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/sitesetting/setting.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
