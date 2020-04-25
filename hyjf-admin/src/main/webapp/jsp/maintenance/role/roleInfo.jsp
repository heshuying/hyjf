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
	<tiles:putAttribute name="pageTitle" value="角色管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty roleForm.id ? '添加' : '修改'}"></c:set>
		<hyjf:message key="state_error"></hyjf:message>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改角色。
				</p>
				<hr/>
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty roleForm.id ? 'insertAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="id" id="id" value="${roleForm.id }" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
						<label class="col-sm-2 control-label" for="roleName"> 
							<span class="symbol required"></span>角色名称 
						</label>
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${empty roleForm.id}">
									<input type="text" placeholder="角色名称" id="roleName" name="roleName" value="${roleForm.roleName}"  class="form-control" maxlength="20"
										datatype="*1-20" errormsg="角色名称的长度不能大于20个字符！" ajaxurl="checkAction?id=${roleForm.id }"  >
									<hyjf:validmessage key="roleName" label="角色名称 "></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text"  value="${roleForm.roleName}"  class="form-control" disabled>
									<input type="hidden" placeholder="角色名称" id="roleName" name="roleName" value="${roleForm.roleName}" >
								</c:otherwise>
							</c:choose>
							
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="description"> <span class="symbol"></span>角色说明 </label>
						<div class="col-sm-10">
							<textarea placeholder="角色说明" id="description" name="description" class="form-control limited">${roleForm.description}</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="sort"> <span class="symbol"></span>排序 </label>
						<div class="col-sm-10">
							<input type="text" placeholder="排序" id="sort" name="sort" value="${roleForm.sort}"  class="form-control" maxlength="5"
								ignore="ignore" datatype="n1-5" errormsg="排序只能输入长度小于5的数字！" >
							<hyjf:validmessage key="sort" label="排序"></hyjf:validmessage>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" > <span class="symbol required"></span>角色状态 </label>
						<div class="col-sm-10">
							<div class="radio clip-radio radio-primary ">
								<input type="radio" id="stateOn" name="state" datatype="*" value="0" class="event-categories" ${roleForm.state == '0' ? 'checked' : ''}> <label for="stateOn">  启用
								</label>
								<input type="radio" id="stateOff" name="state" datatype="*" value="1" class="event-categories" ${roleForm.state == '1' ? 'checked' : ''}> <label for="stateOff">  禁用
								</label>
							</div>
						</div>
						<hyjf:validmessage key="state" label="角色状态"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/maintenance/role/roleInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
