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
<c:set value="${fn:split('汇盈金服,系统管理,操作日志', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="employeeinfo:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="用户管理" />
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
			<h1 class="mainTitle">操作日志</h1>
			<span class="mainDescription">操作日志信息。</span>
		</tiles:putAttribute>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<hyjf:message key="delete_error"></hyjf:message>
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
						
								<c:set var="jspPrevDsb" value="${maintenanceLogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${maintenanceLogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" tooltip-placement="top" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
										data-toggle="tooltip" tooltip-placement="top" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" tooltip-placement="top" data-original-title="刷新列表"><i class="fa fa-refresh"></i> 刷 新</a> 
							<shiro:hasPermission name="accountdetail:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
										data-toggle="tooltip" tooltip-placement="top" data-original-title="导出Excel"><i class="fa fa-Export"></i> 导出Excel</a>
								</shiro:hasPermission>		
										
							<a class="btn btn-primary btn-sm pull-right fn-SearchPanel" data-toggle="tooltip" tooltip-placement="top" data-original-title="查询条件" data-toggle-class="active"
								data-toggle-target="#searchable-panel"><i class="fa fa-search"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-hover">
								<colgroup>
									<col style="width:25px;" />
									<col style="width:120px;" />
									<col style="width:170px;" />
									<col style="width:170px;" />
									<col style="width:170px;" />
									<col style="width:170px;" />
									<col style="width:170px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="hidden-xs center">
											<div align="left" class="checkbox clip-check check-primary checkbox-inline"
													data-toggle="tooltip" tooltip-placement="top" data-original-title="选择所有行">
												<input type="checkbox" id="checkall">
												<label for="checkall"></label>
											</div>
										</th>
										<th>时间</th>
										<th>IP地址</th>
										<th>操作者</th>
										<th>被操作者</th>
										<th>操作模块</th>
										<th>操作方法</th>
										<th>操作状态</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty maintenanceLogForm.recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${maintenanceLogForm.recordList}" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="hidden-xs">
														<div class="checkbox clip-check check-primary checkbox-inline">
															<input type="checkbox" class="listCheck" id="row${status.index }" value="${record.id }">
															<label for="row${status.index }"></label>
														</div>
													</td>
													<td><c:out value="${record.times }"></c:out></td>
													<td><c:out value="${record.ipaddress }"></c:out></td>
													<td><c:out value="${record.operatorname }"></c:out></td>
													<td><c:out value="${record.beoperatorname }"></c:out></td>
													<td><c:out value="${record.type }"></c:out></td>
													<td><c:out value="${record.method }"></c:out></td>
													<td><c:out value="${record.status }"></c:out></td>
											<%--	<td><c:out value="${record.times }"></c:out></td> --%>
											<%--    <td><fmt:formatDate value="${record.times }" pattern="yyyy-MM-dd"/></td>   --%>					
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${maintenanceLogForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
 
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${maintenanceLogForm.paginatorPage}" />
			<div class="modal-body">
				<h3>查询条件</h3>
				<hr/>
				<!-- 查询条件 -->
				<div class="form-group">
					<label>操作者</label>
					<input type="text" name="operatornameSrch" class="form-control input-sm underline" value="${maintenanceLogForm.operatornameSrch}" />
				</div>
				<div class="form-group">
					<label>被操作者</label>
					<input type="text" name="beoperatornameSrch" class="form-control input-sm underline" value="${maintenanceLogForm.beoperatornameSrch}" />
				</div>
				<div class="form-group">
					<label>操作时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timesStartSrch" id="timesStartSrch" class="form-control underline" value="${maintenanceLogForm.timesStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timesEndSrch" id="timesEndSrch" class="form-control underline" value="${maintenanceLogForm.timesEndSrch}" />
					</div>
				</div>
			</div>
		</tiles:putAttribute>
	
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
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/maintenance/log/log_list.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
