<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="权限功能按钮" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		
	</tiles:putAttribute>
	<%-- 画面功能路径 --%>
	<tiles:putAttribute name="pageFunPath" type="string">
		1
	</tiles:putAttribute>
	<%-- 画面右面板的头标题 --%>
	<tiles:putAttribute name="pageHeader" type="string">
		权限功能按钮
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<form id="mainForm" action="insertAction" method="post">
			<div class="widget">
				<h4 class="widgettitle">角色管理</h4>
				<div class="widgetcontent nopadding">
					<%-- 角色列表一览 --%>
					<input type="hidden" name="permissionUuid" id="permissionUuid" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<p>
						<label>权限</label> 
						<span class="field"> 
							<input type="text" name="permission" class="span4" value="${permissionsForm.permission}" datatype="s2-20" errormsg="用户只能是数字、字母和汉字，长度2~20个字符！" />
						</span>
					</p>
					<p>
						<label>权限名称</label> 
						<span class="field"> 
							<input type="text" name="permissionName" class="span4" value="${permissionsForm.permissionName}" datatype="s2-20" errormsg="用户只能是数字、字母和汉字，长度2~20个字符！" />
						</span>
					</p>
					<p>
						<label>描述</label> 
						<span class="field"> 
							<input type="text" name="description" class="span4" value="${permissionsForm.description}" datatype="s2-20" errormsg="用户只能是数字、字母和汉字，长度2~20个字符！" />
						</span>
					</p>
					<p class="stdformbutton">
						<a href="init" class="btn btn-primary" type="button"><i class="icon-share icon-white"></i> 返 回</a> 
						<a class="btn btn-primary marginleft15" type="button" id="registBtn"><i class="icon-ok icon-white"></i>确 认</a>
					</p>
					<span class="clearall"></span>
				</div>
			</div>
		</form>
	</tiles:putAttribute>
	<%-- 边界面板 (ignore) --%>
	<tiles:putAttribute name="asidepanels" type="string">
		
	</tiles:putAttribute>
	<%-- 对话框面板 (ignore) --%>
	<tiles:putAttribute name="dialogpanels" type="string">
		
	</tiles:putAttribute>
	
	<%-- JS插件补丁 (ignore) --%>
	<tiles:putAttribute name="pageJsPatch" type="string">
		
	</tiles:putAttribute>
	<%-- JS全局变量定义 (ignore) --%>
	<tiles:putAttribute name="pageGlobalVariables" type="string">
		
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/maintenance/permissions/permissionsInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
