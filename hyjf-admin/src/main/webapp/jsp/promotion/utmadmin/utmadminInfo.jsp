<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="渠道账号管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty utmadminForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty utmadminForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal">
					<%-- 银行列表一览 --%>
					<input type="hidden" name="id" id="id" value="<c:out value="${utmadminForm.id }"/>" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />

					<div class="form-group">
						<label class="col-sm-2 control-label" for="adminUserName"> 
							<span class="symbol required"></span>用户名:
						</label>
						<div class="col-sm-9">
							<input type="text" id="adminUserName" name="adminUserName" value="<c:out value="${utmadminForm.adminUserName}"></c:out>"   <c:if test="${!empty utmadminForm.id }"> readonly="readonly" </c:if>
									class="form-control" datatype="*1-20" errormsg="用户名为1-20字符！" maxlength="20" <c:if test="${empty utmadminForm.id }">ajaxurl="checkAction"</c:if> />
							<hyjf:validmessage key="adminUserName" label="用户名"></hyjf:validmessage>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label" for="utmIds"><span class="symbol required"></span>渠道：</label>
							<div class="col-sm-9 checkbox clip-check check-primary inline" style="padding-left: 15px;">
								<c:set var="tempStrs" value="${fn:split(utmadminForm.utmIds, ',')}" />
								<c:forEach items="${utmPlatList }" var="utm" begin="0" step="1" varStatus="status">
									<input type="checkbox" id="ct-${status.index }" datatype="*" name="utmIds" value="${utm.sourceId}" 
									<c:forEach items="${tempStrs }" var="tempStr" begin="0" step="1"> <c:if test="${utm.sourceId eq tempStr}"> checked="checked"</c:if> </c:forEach>>
									<label for="ct-${status.index }">${utm.sourceName }</label>
								</c:forEach>
								<hyjf:validmessage key="utmIds" label="渠道"></hyjf:validmessage>
							</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label" for="keyCode"> 
							<span class="symbol required"></span>关键字:
						</label>
						<div class="col-sm-9">
							<input type="text" id="keyCode" name="keyCode" value="<c:out value="${utmadminForm.keyCode}"></c:out>"
									class="form-control" datatype="*1-20" errormsg="关键字应该输入1~20个字符！" maxlength="20" />
							<hyjf:validmessage key="keyCode" label="关键字"></hyjf:validmessage>
						</div>
					</div>


					<div class="form-group margin-bottom-0">
						<div class="col-sm-offset-2 col-sm-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/promotion/utmadmin/utmadminInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
