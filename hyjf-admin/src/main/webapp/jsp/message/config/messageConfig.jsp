<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,消息中心,短信加固', ',')}" var="functionPaths"
	scope="request"></c:set>
<shiro:hasPermission name="messageConfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="短信加固" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">短信加固</h1>
			<span class="mainDescription">短信加固模块</span>
		</tiles:putAttribute>
		<br />
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<form id="mainForm"
				action="${empty conform.id ? 'addAction' : 'updateAction'}"
				method="post" role="form" class="form-horizontal">
				<%-- 文章列表一览 --%>
				<input type="hidden" name="id" id="id" value="${conform.id }" /> <input
					type="hidden" name="pageToken"
					value="${sessionScope.RESUBMIT_TOKEN}" /> <input type="hidden"
					id="success" value="${success}" />
				<br/>
				<br/>
				<div class="col-md-12 col-lg-8">
				<fieldset>
					<legend>
						每天最大发送量
					</legend>
				<div class="form-group">
					<label class="col-sm-3 control-label" for="type"> <span
						class="symbol required"></span>每天最大发送量(同一IP)
					</label>
					<div class="col-sm-3">
						<c:choose>
							<c:when test="${empty conform.id}">
							<div class="input-group">
								<input type="text" placeholder="每天最大发送量 (同一ID)" id="maxIpCount"
									name="maxIpCount" class="form-control" maxlength="10"
									datatype="n1-10" errormsg="每天最大发送量必须是不能大于10位的数字 ！">
									<span class="input-group-addon">条</span>
							</div>
								<hyjf:validmessage key="maxIpCount" label="每天最大发送量 (同一IP)"></hyjf:validmessage>
							</c:when>
							<c:otherwise>
							<div class="input-group">
								<input type="text" placeholder="每天最大发送量" id="maxIpCount"
									name="maxIpCount" class="form-control" maxlength="10"
									datatype="n1-10" errormsg="每天最大发送量必须是不能大于10位的数字 ！"
									value="${conform.maxIpCount}"><span class="input-group-addon">条</span>
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label" for="title"> <span
						class="symbol required"></span>每天最大发送量(同一设备)
					</label>
					<div class="col-sm-3">
						<c:choose>
							<c:when test="${empty conform.id}">
							<div class="input-group">
								<input type="text" placeholder="每天最大发送量(同一设备)"
									id="maxMachineCount" name="maxMachineCount"
									class="form-control" maxlength="10" datatype="n1-10"
									errormsg="必须是不能大于10位的数字！"><span class="input-group-addon">条</span>
							</div>
								<hyjf:validmessage key="maxMachineCount" label="每天最大发送量(同一设备) "></hyjf:validmessage>
							</c:when>
							<c:otherwise>
							<div class="input-group">
								<input type="text" placeholder="每天最大发送量(同一设备)"
									id="maxMachineCount" name="maxMachineCount"
									class="form-control" maxlength="10" datatype="n1-10"
									errormsg="必须是不能大于10位的数字！" value="${conform.maxMachineCount}">
									<span class="input-group-addon">条</span>
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label" for="author"> <span
						class="symbol required"></span>每天最大发送量(同一浏览器)
					</label>
					<div class="col-sm-3">
						<c:choose>
							<c:when test="${empty conform.id}">
							<div class="input-group">
								<input type="text" placeholder="每天最大发送量(同一浏览器)"
									id="maxBrowserCount" name="maxBrowserCount"
									class="form-control" maxlength="10" datatype="n1-10"
									errormsg="必须是不能大于10位的数字！"><span class="input-group-addon">条</span>
							</div>
								<hyjf:validmessage key="maxBrowserCount" label="每天最大发送量(同一浏览器) "></hyjf:validmessage>
							</c:when>
							<c:otherwise>
							<div class="input-group">
								<input type="text" placeholder="每天最大发送量(同一浏览器)"
									id="maxBrowserCount" name="maxBrowserCount"
									class="form-control" maxlength="10" datatype="n1-10"
									errormsg="必须是不能大于10位的数字！" value="${conform.maxBrowserCount}">
									<span class="input-group-addon">条</span>
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label" for="author"> <span
						class="symbol required"></span>每天最大发送量(同一手机号)
					</label>
					<div class="col-sm-3">
						<c:choose>
							<c:when test="${empty conform.id}">
							<div class="input-group">
								<input type="text" placeholder="每天最大发送量(同一手机号)"
									id="maxPhoneCount" name="maxPhoneCount" class="form-control"
									maxlength="10" datatype="n1-10" errormsg="必须是不能大于10位的数字！">
									<span class="input-group-addon">条</span>
							</div>
								<hyjf:validmessage key="maxPhoneCount" label="每天最大发送量(同一手机号) "></hyjf:validmessage>
							</c:when>
							<c:otherwise>
							<div class="input-group">
								<input type="text" placeholder="每天最大发送量(同一手机号)"
									id="maxPhoneCount" name="maxPhoneCount" class="form-control"
									maxlength="10" datatype="n1-10" errormsg="必须是不能大于10位的数字！"
									value="${conform.maxPhoneCount}"><span class="input-group-addon">条</span>
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				</fieldset>
				</div>
				
				<div class="col-md-12 col-lg-8">
				<fieldset>
					<legend>
						手机时间参数设置
					</legend>
				<div class="form-group">
					<label class="col-sm-3 control-label" for="author"> <span
						class="symbol required"></span>发送时间间隔(同一手机号)
					</label>
					<div class="col-sm-3">
						<c:choose>
							<c:when test="${empty conform.id}">
								<div class="input-group">
								<input type="text" placeholder="发送时间间隔(同一手机号)"
									id="maxIntervalTime" name="maxIntervalTime"
									class="form-control" maxlength="10" datatype="n1-10"
									errormsg="必须是不能大于10位的数字！"><span class="input-group-addon">秒</span>
								</div>
								<hyjf:validmessage key="maxIntervalTime" label="发送时间间隔(同一手机号) "></hyjf:validmessage>
							</c:when>
							<c:otherwise>
							<div class="input-group">
								<input type="text" placeholder="发送时间间隔(同一手机号)"
									id="maxIntervalTime" name="maxIntervalTime"
									class="form-control" maxlength="10" datatype="n1-10"
									errormsg="必须是不能大于10位的数字！" value="${conform.maxIntervalTime}">
									<span class="input-group-addon">秒</span>
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label" for="author"> <span
						class="symbol required"></span>短信验证码有效时间(同一手机号)
					</label>
					<div class="col-sm-3">
						<c:choose>
							<c:when test="${empty conform.id}">
							<div class="input-group">
								<input type="text" placeholder="短信验证码有效时间(同一手机号)"
									id="maxValidTime" name="maxValidTime" class="form-control"
									maxlength="10" datatype="n1-10" errormsg="必须是不能大于10位的数字！">
									<span class="input-group-addon">分钟</span>
							</div>
								<hyjf:validmessage key="maxValidTime" label="短信验证码有效时间(同一手机号) "></hyjf:validmessage>
							</c:when>
							<c:otherwise>
							<div class="input-group">
								<input type="text" placeholder="短信验证码有效时间(同一手机号)"
									id="maxValidTime" name="maxValidTime" class="form-control"
									maxlength="10" datatype="n1-10" errormsg="必须是不能大于10位的数字！"
									value="${conform.maxValidTime}"><span class="input-group-addon">分钟</span>
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label" for="author"> <span
						class="symbol required"></span>发送超限通知的间隔时间(针对同一IP、浏览器、设备的设置)
					</label>
					<div class="col-sm-3">
						<c:choose>
							<c:when test="${empty conform.id}">
							<div class="input-group">
								<input type="text" placeholder="发送超限通知的间隔时间(针对同一IP、浏览器、设备的设置)"
									id="noticeToTime" name="noticeToTime" class="form-control"
									maxlength="10" datatype="n1-10" errormsg="必须是不能大于10位的数字！">
									<span class="input-group-addon">分钟</span>
							</div>
								<hyjf:validmessage key="noticeToTime"
									label="发送超限通知的间隔时间(针对同一IP、浏览器、设备的设置)"></hyjf:validmessage>
							</c:when>
							<c:otherwise>
							<div class="input-group">
								<input type="text" placeholder="发送超限通知的间隔时间(针对同一IP、浏览器、设备的设置)"
									id="noticeToTime" name="noticeToTime" class="form-control"
									maxlength="10" datatype="n1-10" errormsg="必须是不能大于10位的数字！"
									value="${conform.noticeToTime}"><span class="input-group-addon">分钟</span>
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				</fieldset>
				</div>
				
				<div class="col-md-12">
					<div class="margin-top-15">
						<span class="symbol required"></span>必须填写的项目
						<hr>
					</div>
				</div>
				
				<div class="col-md-5">
					<p>
						点击【确认】按钮，保存当前的填写的资料。
					</p>
				</div>
				<div class="col-md-7">
					<div class="form-group margin-bottom-0">
					<div class="col-sm-offset-3 col-sm-10">
						<a class="btn btn-o btn-primary fn-Confirm"><i
							class="fa fa-check"></i> 确 认</a>
					</div>
					</div>
				</div>
				
				<!--  <div class="form-group margin-bottom-0">
					<div class="col-sm-offset-3 col-sm-10">
						<a class="btn btn-o btn-primary fn-Confirm"><i
							class="fa fa-check"></i> 确 认</a>
					</div>
				</div> --> 
			</form>
			</div>
			</div>
			</div>
		</tiles:putAttribute>
		
<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css"
				rel="stylesheet" media="screen">
			<link
				href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
			<style>
			.table-striped .checkbox {
				width: 20px;
				margin-right: 0 !important;
				overflow: hidden
			}
			.form-group{
				margin-bottom:30px;
			}
			</style>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type='text/javascript'
				src="${webRoot}/jsp/message/config/messageConfig.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>