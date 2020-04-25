<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="returncashlist:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="返现管理" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">返现管理</h1>
		<span class="mainDescription">本功能可以查询返现明细和执行返现操作。</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="container-fluid container-fullw bg-white">
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li class="active"><a href="javascript:void(0);">待返现列表</a></li>
				<shiro:hasPermission name="returnedcashlist:VIEW">
				<li><a href="${webRoot}/finance/returncash/returnedcash">已返现列表</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${returncashForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${returncashForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a
							class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
						<shiro:hasPermission name="returncashlist:EXPORT">
						<a class="btn btn-o btn-primary btn-sm fn-Export"
								data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i> </a>
						</shiro:hasPermission>
						<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
								data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
								data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
					</div>
					<br />
					<%-- 返现列表一览 --%>
					<table id="returncashList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width: 55px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">用户</th>
								<th class="center">分公司</th>
								<th class="center">分部</th>
								<th class="center">团队</th>
								<th class="center">待返现充值金额</th>
								<th class="center">待返手续费</th>
								<th class="center">待返现出借金额</th>
								<th class="center">返现金额</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty returncashForm.recordList}">
									<tr>
										<td colspan="10">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${returncashForm.recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((returncashForm.paginatorPage - 1) * returncashForm.paginator.limit) + 1 }"></c:out></td>
											<td><c:out value="${record.username }"></c:out></td>
											<td class="center"><c:out value="${record.regionName }"></c:out></td>
											<td class="center"><c:out value="${record.branchName }"></c:out></td>
											<td><c:out value="${fn:replace(record.departmentName, '&amp;', '&')  }"></c:out></td>
											<td class="text-right"><fmt:formatNumber value="${record.recMoney}" type="number" pattern="#,##0.00#" /></td>
											<td class="text-right"><fmt:formatNumber value="${record.fee}" type="number" pattern="#,##0.00#" /></td>
											<td class="text-right"><fmt:formatNumber value="${record.inMoney}" type="number" pattern="#,##0.00#" /></td>
											<td class="text-right"><fmt:formatNumber value="${record.maybackmoney}" type="number" pattern="#,##0.00#" /></td>
											<td class="center">
												<shiro:hasPermission name="returncashlist:RETURNCASH">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<a class="btn btn-transparent btn-xs fn-Returncash" data-toggle="tooltip" data-id="${record.userId }" data-type="${record.type } data-toggle="tooltip" tooltip-placement="top" data-original-title="返手续费"><i class="fa fa-mail-reply "></i></a>
												</div>
												<div class="visible-xs visible-sm hidden-md hidden-lg">
													<div class="btn-group" dropdown="">
														<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
															<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
														</button>
														<ul class="dropdown-menu pull-right dropdown-light" role="menu">
															<li><a class="fn-Returncash" data-id="${record.id }" data-type="${record.type }">返手续费</a></li>
														</ul>
													</div>
												</div>
												</shiro:hasPermission>
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="returncash" paginator="${returncashForm.paginator}"></hyjf:paginator>
					<br />
					<br />
				</div>
			</div>
		</div>
	</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="ids" id="ids" />
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${returncashForm.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>用户名</label> <input type="text" name="usernameSrch" class="form-control input-sm underline" value="${returncashForm.usernameSrch}" />
			</div>
			<div class="form-group">
				<label>部门</label>
				<div class="dropdown-menu no-radius">
					<input type="text" class="form-control input-sm underline margin-bottom-10" value="" id="combotree_search" placeholder="Search">
					<input type="hidden" id="combotree_field_hidden"  name="combotreeSrch" value="${returncashForm.combotreeSrch}">
					<div id="combotree-panel" style="width:270px;height:300px;position:relative;overflow:hidden;">
						<div id="combotree" class="tree-demo" ></div>
					</div>
				</div>

				<span class="input-icon input-icon-right" data-toggle="dropdown" >
					<input id="combotree-field" type="text" class="form-control underline form " readonly="readonly">
					<i class="fa fa-remove fn-ClearDep" style="cursor:pointer;"></i>
				</span>
			</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/finance/returncash/returncash.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
