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
<c:set value="${fn:split('汇盈金服,配置设置,邮件设置', ',')}" var="functionPaths"
	scope="request"></c:set>

<shiro:hasPermission name="emailsetting:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="邮件设置" />
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
			<h1 class="mainTitle">邮件设置</h1>
			<span class="mainDescription">本功能可以增加修改删除邮件信息。</span>
		</tiles:putAttribute>
		<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty emailsettingForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty emailsettingForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%-- 活动列表一览 --%>
					<input type="hidden" name="id" id="id" value="${emailsettingForm.id }" />
					<div class="form-group">
						<label class="col-sm-2 control-label" for="smtpServer"> <span class="symbol required"></span>SMTP服务器 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="SMTP服务器 " id="smtpServer" name="smtpServer" value="${emailsettingForm.smtpServer}"  class="form-control"
								datatype="s2-20" errormsg="SMTP服务器  只能是字符汉字，长度2~20个字符！" >
							<hyjf:validmessage key="smtpServer" label="SMTP服务器"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="smtpUsername"> <span class="symbol required"></span>邮件账户 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="邮件账户" id="smtpUsername" name="smtpUsername" value="${emailsettingForm.smtpUsername}"  class="form-control"
								datatype="s2-20" errormsg="邮件账户 只能是字符汉字，长度2~20个字符！" >
							<hyjf:validmessage key="smtpUsername" label="邮件账户"></hyjf:validmessage>
						</div>
						</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="smtpReply"> <span class="symbol required"></span>回复邮箱</label>
						<div class="col-sm-10">
							<input type="text" placeholder="邮件关键字" id="smtpReply" name="smtpReply" value="${emailsettingForm.smtpReply}"  class="form-control"
								datatype="*,e" errormsg="回复邮箱只能是字符汉字，长度2~20个字符！" >
							<hyjf:validmessage key="smtpReply" label="回复邮箱"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="smtpPort"> <span class="symbol required"></span>邮箱密码 </label>
						<div class="col-sm-10">
							<input type="password" placeholder="邮箱密码" id="smtpPassword" name="smtpPassword" value="${emailsettingForm.smtpPassword}"  class="form-control"
								datatype="s2-20" errormsg="邮箱密码 只能是字符汉字，长度2~20个字符！" >
							<hyjf:validmessage key="smtpPassword" label="邮箱密码"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="smtpPort"> <span class="symbol required"></span>邮箱端口号 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="邮箱端口号" id="smtpPort" name="smtpPort" value="${emailsettingForm.smtpPort}"  class="form-control"
								datatype="s2-20" errormsg="邮箱端口号 只能是字符汉字，长度2~20个字符！" >
							<hyjf:validmessage key="smtpPort" label="邮箱端口号"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="smtpFromName"> <span class="symbol required"></span>发件人名 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="发件人名" id="smtpFromName" name="smtpFromName" value="${emailsettingForm.smtpFromName}"  class="form-control"
								datatype="*1-20" errormsg="发件人名 只能是字符汉字，长度2~20个字符！" >
							<hyjf:validmessage key="smtpPort" label="邮箱端口号"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" > <span class="symbol required"></span>是否需要身份验证  </label>
						<div class="col-sm-10">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="smtpVerifyOn" name="smtpVerify" datatype="*" value="1" class="event-categories" ${emailsettingForm.smtpVerify == '1' ? 'checked' : ''}> <label for="smtpVerifyOn">  启用
								</label>
								<input type="radio" id="smtpVerifyOff" name="smtpVerify" datatype="*" value="0" class="event-categories" ${emailsettingForm.smtpVerify == '0' ? 'checked' : ''}> <label for="smtpVerifyOff">  禁用
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" > <span class="symbol required"></span>是否SSL加密 </label>
						<div class="col-sm-10">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="smtpSslOn" name="smtpSsl" datatype="*" value="1" class="event-categories" ${emailsettingForm.smtpSsl == '1' ? 'checked' : ''}> <label for="smtpSslOn">  启用
								</label>
								<input type="radio" id="smtpSslOff" name="smtpSsl" datatype="*" value="0" class="event-categories" ${emailsettingForm.smtpSsl == '0' ? 'checked' : ''}> <label for="smtpSslOff">   禁用
								</label>
							</div>
						</div>
						</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" > <span class="symbol required"></span>是否发送测试邮件 </label>
						<div class="col-sm-10">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="siteStatusOn" name="siteStatus" datatype="*" value="1" class="event-categories" ${emailsettingForm.siteStatus == '1' ? 'checked' : ''}> <label for="siteStatusOn">  启用
								</label>
								<input type="radio" id="siteStatusOff" name="siteStatus" datatype="*" value="0" class="event-categories" ${emailsettingForm.siteStatus == '0' ? 'checked' : ''}> <label for="siteStatusOff">  禁用
								</label>
							</div>
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
