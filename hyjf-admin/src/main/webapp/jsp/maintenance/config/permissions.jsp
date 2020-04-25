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
		<form id="mainForm" action="init" method="post">
		<input type="hidden" name="permissionUuid" id="permissionUuid" />
		<div class="mediamgr_head">
			<%-- 功能栏 --%>
			<ul class="mediamgr_menu" style="height:32px;">
				<li class="btn-group">
					<c:set var="jspPrevDsb" value="${paginator.firstPage ? ' disabled' : ''}"></c:set>
					<c:set var="jspNextDsb" value="${paginator.lastPage ? ' disabled' : ''}"></c:set>
					<a class="btn fn-Prev${jspPrevDsb}" title="上一页"><span class="icon-chevron-left"></span></a>
					<a class="btn fn-Next${jspNextDsb}" title="下一页"><span class="icon-chevron-right"></span></a>
				</li>
				<li class="marginleft15" title="添加新的角色">
					<a class="btn fn-Add"><span class="icon-plus"></span> 添加</a>
				</li>
				<li class="marginleft5" title="修改角色信息">
					<a class="btn fn-Modify"><span class="icon-pencil"></span> 修改</a>
				</li>
				<li class="marginleft5" title="删除角色">
					<a class="btn fn-Delete"><span class="icon-trash"></span> 删除</a>
				</li>
				<li class="marginleft15" title="刷新列表">
					<a class="btn fn-Refresh"><span class="icon-refresh"></span> 刷新</a>
				</li>
			</ul>
			<span class="clearall"></span>
		</div><!--mediamgr_head-->
		
		<%-- 角色列表一览 --%>
		<table id="equiList" class="table table-bordered responsive ellipsis hover" style="margin:0;">
			<colgroup>
				<col class="con0" style="width:35px;" />
				<col class="con1" style="width:40%;" />
				<col class="con0" style="width:50%;" />
			</colgroup>
			<thead>
				<tr>
					<th><input type="checkbox" class="checkall" /></th>
					<th>权限</th>
					<th>权限名称</th>
				</tr>
			</thead>
			<tbody id="roleTbody">
				<c:choose>
					<c:when test="${empty permissionsForm.recordList}">
						<tr><td colspan="4">暂时没有数据记录</td></tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${permissionsForm.recordList }" var="record" begin="0" step="1" varStatus="status">
							<tr>
								<td><input type="checkbox" class="listCheck" value="${record.permissionUuid }" /></td>
								<td><c:out value="${record.permission }"></c:out></td>
								<td><c:out value="${record.permissionName }"></c:out></td>
							</tr>
						</c:forEach>					
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<%-- 分页栏 --%>

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
		<script type='text/javascript' src="${webRoot}/jsp/maintenance/permissions/permissions.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
