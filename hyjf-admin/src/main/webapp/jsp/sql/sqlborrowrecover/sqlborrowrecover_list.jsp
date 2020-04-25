<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,数据,待还金额', ',')}" var="functionPaths" scope="request"></c:set>

<%-- <shiro:hasPermission name="employeeinfo:VIEW">		--%>
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="待还金额" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.table-striped .checkbox { width:20px;margin-right:0!important;overflow:hidden }
			</style>
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">待还金额</h1>
			<span class="mainDescription">待还金额信息。</span>
		</tiles:putAttribute>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<hyjf:message key="delete_error"></hyjf:message>
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-hover">
								<colgroup>
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
								</colgroup>
								<thead>
									<tr>
										<th>大区</th>
										<th>分公司</th>
										<th>待还总额</th>
										
									</tr>
								</thead>
								<tbody id="roleTbody">
											<c:forEach items="${lists}" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td><c:out value="${record.daqu }"></c:out></td>
													<td><c:out value="${record.fgs }"></c:out></td>
													<td><c:out value="${record.sum }"></c:out></td>
													
												</tr>
											</c:forEach>		
								</tbody>
							</table>
							
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>

	
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<!-- Form表单插件 -->
			<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/sql/sqlborrowrecover/sqlborrowrecover_list.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
<%-- </shiro:hasPermission>		--%>
