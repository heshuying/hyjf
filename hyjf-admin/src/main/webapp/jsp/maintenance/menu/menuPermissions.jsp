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
	<tiles:putAttribute name="pageTitle" value="菜单权限管理" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<div class="panel-scroll height-650">
				<form id="mainForm" action="updateSettingAction" method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="menuUuid" id="menuUuid" value="${adminMenuForm.menuUuid }" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
						<div class="col-sm-12">
						<c:choose>
							<c:when test="${empty adminMenuForm.recordList}">
								<p>当前没有任何权限，请<a href="/maintenance/permissions/init">点此</a>设置权限</p>
							</c:when>
							<c:otherwise>
								<c:forEach items="${adminMenuForm.recordList }" var="record" begin="0" step="1" varStatus="status">
									<div class="checkbox clip-check check-primary checkbox-inline">

										<input type="checkbox" id="menu-${status.index }" name="permissionsUuid" value="${record.permissionUuid}" <c:out value="${record.selected ? 'checked=checked' : '' }"></c:out>>
										<label for="menu-${status.index }">
											${record.permissionName }
										</label>
									</div>
								</c:forEach>
							</c:otherwise>
						</c:choose>
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
		<script type='text/javascript' src="${webRoot}/jsp/maintenance/menu/menuPermissions.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
