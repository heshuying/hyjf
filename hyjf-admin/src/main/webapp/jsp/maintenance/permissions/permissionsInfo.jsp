<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="权限功能按钮" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty permissionsForm.permissionUuid ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-heading">
				<h5 class="panel-title">${jspEditType}权限信息</h5>
			</div>
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改系统功能的权限分配，以便于更好的管理系统的应用。</p>
				<hr />
				<div class="panel-scroll height-270">
					<form id="mainForm" action="${empty permissionsForm.permissionUuid ? 'insertAction' : 'updateAction'}"
							method="post" role="form" class="form-horizontal">
						<%-- 角色列表一览 --%>
						<input type="hidden" name="permissionUuid" id="permissionUuid" value="${permissionsForm.permissionUuid }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputEmail3">
								<span class="symbol required"></span> 权限别名
							</label>
							<div class="col-sm-10">
								<input type="text" name="permission" class="form-control" ${empty permissionsForm.permissionUuid ? '' : 'readonly'}
										value="${permissionsForm.permission}" datatype="s2-20" errormsg="权限别名只能是数字、字母和汉字，长度2~20个字符！" />
								<hyjf:validmessage key="permission"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword3">
								<span class="symbol required"></span> 权限名称
							</label>
							<div class="col-sm-10">
								<input type="text" name="permissionName" class="form-control"
									value="${permissionsForm.permissionName}" datatype="s2-20" errormsg="权限名称只能是数字、字母和汉字，长度2~20个字符！" />
								<hyjf:validmessage key="permissionName"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="inputPassword3">
								描述 </label>
							<div class="col-sm-10">
								<textarea name="description" maxlength="255" placeholder="权限的详细描述信息" rows="4" class="form-control"><c:out value="${permissionsForm.description}"></c:out></textarea>
							</div>
								<hyjf:validmessage key="description"></hyjf:validmessage>
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
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title {
		font-family: "微软雅黑"
	}
	</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>	
	<tiles:putAttribute name="pageGlobalImport" type="string">	
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/maintenance/permissions/permissionsInfo.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
