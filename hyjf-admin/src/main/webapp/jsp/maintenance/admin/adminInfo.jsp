<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="用户管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	.admin-select .select2-container { 
		width: 100% !important;
	}
	.admin-select .select2-container--default .select2-selection--single { 
		border-radius: 0px;
		border: 1px solid #BBBAC0 !important;
	}
	.admin-select .select2-container--default .select2-selection--single .select2-selection__rendered, .admin-select .select2-container--default .select2-selection--single { 
		height: 34px;
		line-height:34px;
	}
	.admin-select .select2-container .select2-selection--single .select2-selection__rendered {
		padding-left: 4px;
	}
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty adminForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改用户。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
				<form id="mainForm" action="${empty adminForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="id" id="id" value="${adminForm.id }" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
						<label class="col-sm-2 control-label" for="truename"> 
							<span class="symbol required"></span>用户名 
						</label>
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${empty adminForm.id}">
									<input type="text" placeholder="用户名" id="username" name="username" value="${adminForm.username}"  class="form-control"
										datatype="*1-20" errormsg="用户名不能超过20个字符！" maxlength="20" ajaxurl="checkAction?id=${adminForm.id }" >
									<hyjf:validmessage key="username" label="用户名"></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text"  value="${adminForm.username}"  class="form-control" disabled>
									<input type="hidden" placeholder="姓名" id="username" name="username" value="${adminForm.username}" >
								</c:otherwise>
							</c:choose>
							
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="truename"> <span class="symbol required"></span>姓名 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="姓名" id="truename" name="truename" value="${adminForm.truename}"  class="form-control"
								datatype="/^[\u4E00-\u9FA5\uf900-\ufa2d]{1,10}$/" errormsg="姓名 只能是汉字，长度1~10个字符！" maxlength="10">
							<hyjf:validmessage key="truename" label="姓名"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="departmentId"> <span class="symbol required"></span>所属部门 </label>

						<div class="col-sm-10 admin-select">
							<select id="departmentId" name="departmentId" class="form-control" datatype="*" errormsg="请选择所属部门！" >
								<option value="" disabled>选择所属部门</option>
								<c:if test="${!empty adminForm.departmentList}">
									<c:forEach items="${adminForm.departmentList }" var="dep" begin="0" step="1" varStatus="status">
										<c:choose>
											<c:when test="${adminForm.departmentId == dep.id }">
												<option value="${dep.id }" data-class="fa" selected="selected">${dep.name }</option>
											</c:when>
											<c:otherwise>
												<option value="${dep.id }" data-class="fa">${dep.name }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:if>
							</select>
							<hyjf:validmessage key="departmentId" label="所属部门"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="email"> <span class="symbol"></span>邮箱 </label>
						<div class="col-sm-10">
							<input type="email" placeholder="邮箱" id="email" name="email" value="${adminForm.email}"  class="form-control"
								ignore="ignore" datatype="* , e" errormsg="邮箱地址格式不对！" ajaxurl="checkAction?id=${adminForm.id }"  maxlength="100">
							<hyjf:validmessage key="email" label="邮箱"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="mobile"> <span class="symbol required"></span>手机号码 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="手机号码" id="mobile" name="mobile" value="${adminForm.mobile}"  class="form-control" 
								datatype="n11-11" errormsg="手机号码 只能是11位数字!" ajaxurl="checkAction?id=${adminForm.id }"  maxlength="11">
							<hyjf:validmessage key="mobile" label="手机号码"></hyjf:validmessage>
						</div>
					</div>
					<shiro:hasPermission name="admin:AUTH">
					<div class="form-group">
						<label class="col-sm-2 control-label" for="roleId"> <span class="symbol"></span>角色 </label>

						<div class="col-sm-10 admin-select">
							<select id="roleId" name="roleId" class="form-control">
								<option value="" ></option>
								<c:if test="${!empty adminForm.adminRoleList}">
									<c:forEach items="${adminForm.adminRoleList }" var="role" begin="0" step="1" varStatus="status">
										<c:choose>
											<c:when test="${adminForm.roleId == role.id }">
												<option value="${role.id }" data-class="fa" selected="selected">${role.roleName }</option>
											</c:when>
											<c:otherwise>
												<option value="${role.id }" data-class="fa">${role.roleName }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:if>
							</select>
							<hyjf:validmessage key="departmentId" label="角色"></hyjf:validmessage>
						</div>
					</div>
					</shiro:hasPermission>
					<div class="form-group">
						<label class="col-sm-2 control-label" > <span class="symbol required"></span>用户状态 </label>
						<div class="col-sm-10 ">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="stateOn" name="state" value="0"  datatype="*" ${adminForm.state == '0' ? 'checked' : ''}> <label for="stateOn"> 启用 </label> 
								<input type="radio" id="stateOff" name="state" value="1" datatype="*" ${adminForm.state == '1' ? 'checked' : ''}> <label for="stateOff"> 禁用 </label>
								<hyjf:validmessage key="state" label="用户状态"></hyjf:validmessage>
							</div>
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
		<script type='text/javascript' src="${webRoot}/jsp/maintenance/admin/adminInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
