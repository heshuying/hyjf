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


<shiro:hasPermission name="accountmanage:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="账户管理" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">账户管理</h1>
		<span class="mainDescription">账户管理账户管理账户管理账户管理账户管理账户管理。</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb"
								value="${accountmanageForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb"
								value="${accountmanageForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a
								class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a
								class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
							<shiro:hasPermission name="accountmanage:EXPORT">
								<shiro:hasPermission name="accountmanage:ORGANIZATION_VIEW">
									<a class="btn btn-o btn-primary btn-sm fn-EnhanceExport"
									   data-toggle="tooltip" data-placement="bottom"
									   data-original-title="导出Excel">
										导出Excel <i class="fa fa-EnhanceExport"></i></a>
								</shiro:hasPermission>
								<shiro:lacksPermission name="accountmanage:ORGANIZATION_VIEW">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
									   data-toggle="tooltip" data-placement="bottom"
									   data-original-title="导出Excel">
										导出Excel <i class="fa fa-Export"></i></a>
								</shiro:lacksPermission>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 角色列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">用户名</th>
									<shiro:hasPermission name="accountmanage:ORGANIZATION_VIEW">
										<th class="center">分公司</th>
										<th class="center">分部</th>
										<th class="center">团队</th>
									</shiro:hasPermission>
									<th class="hidden-xs">资产总额</th>
									<th class="center">总可用金额</th>
									<th class="center">总冻结金额</th>
									<th class="center">可用金额</th>
									<th class="center">冻结金额</th>
									<th class="center">待收金额</th>
									<th class="center">待还金额</th>
							<!--  	<th class="center">待返充值金额</th>
									<th class="center">待返充手续费</th>
									<th class="center">待返累计出借金额</th>		-->
									<th class="center">智投服务可用金额</th>
									<th class="center">智投服务冻结金额</th>
									<th class="center">汇付账户</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty accountmanageForm.recordList}">
										<tr>
											<td colspan="12">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${accountmanageForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((accountmanageForm.paginatorPage - 1) * accountmanageForm.paginator.limit) + 1 }"></c:out></td>
												<td><c:out value="${record.username }"></c:out></td>
												<shiro:hasPermission name="accountmanage:ORGANIZATION_VIEW">
													<td class="center"><c:out value="${record.regionName }"></c:out></td>
													<td class="center"><c:out value="${record.branchName }"></c:out></td>
													<td><c:out value="${fn:replace(record.departmentName, '&amp;', '&') }"></c:out></td>
												</shiro:hasPermission>
												<td align="right"><fmt:formatNumber value="${record.total}" type="number" pattern="#,##0.00#" /></td>
												<td align="right"><fmt:formatNumber value="${record.balanceTotal}" type="number" pattern="#,##0.00#" /></td>
												<td align="right"><fmt:formatNumber value="${record.frostTotal}" type="number" pattern="#,##0.00#" /></td>
												<td align="right" class="hidden-xs"><fmt:formatNumber value="${record.balance}" type="number" pattern="#,##0.00#" /></td>
												<td align="right"><fmt:formatNumber value="${record.frost}" type="number" pattern="#,##0.00#" /></td>
												<td align="right"><fmt:formatNumber value="${record.await}" type="number" pattern="#,##0.00#" /></td>
												<td align="right"><fmt:formatNumber value="${record.repay}" type="number" pattern="#,##0.00#" /></td>
												<td align="right"><fmt:formatNumber value="${record.planBalance}" type="number" pattern="#,##0.00#" /></td>
												<td align="right"><fmt:formatNumber value="${record.planFrost}" type="number" pattern="#,##0.00#" /></td>
												<td align="right">
													<c:if test="${record.balanceCash ne record.balanceTotal}">
														<span style="color: red"><fmt:formatNumber value="${record.balanceCash}" type="number" pattern="#,##0.00#" /></span>
													</c:if>
													<c:if test="${record.balanceCash eq record.balanceTotal}">
														<fmt:formatNumber value="${record.balanceCash}" type="number" pattern="#,##0.00#" />
													</c:if>
													/
													<c:if test="${record.balanceFrost ne record.frostTotal}">
														<span style="color: red"><fmt:formatNumber value="${record.balanceFrost}" type="number" pattern="#,##0.00#" /></span>
													</c:if>
													<c:if test="${record.balanceFrost eq record.frostTotal}">
														<fmt:formatNumber value="${record.balanceFrost}" type="number" pattern="#,##0.00#" />
													</c:if>
												</td>
												<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<shiro:hasPermission name="accountmanage:MODIFY">
<%-- 														<c:if test="${(record.balanceCash ne record.balance) or (record.balanceFrost ne record.frost)}"> --%>
															<a
																class="btn btn-transparent btn-xs tooltips fn-UpdateBalance"
																data-userId="${record.userId }" data-toggle="tooltip"
																tooltip-placement="top" data-original-title="更新"><i
																class="fa fa-cloud-download"></i></a>
<%-- 														</c:if> --%>
														</shiro:hasPermission>
														<shiro:hasPermission name="accountdetail:VIEW">
														<a class="btn btn-transparent btn-xs tooltips fn-info"
															data-userId="${record.userId }" data-toggle="tooltip"
															data-username="${record.username }"
															tooltip-placement="top" data-original-title="查看"><i
															class="fa fa-eye"></i></a>
														</shiro:hasPermission>
													</div>
													<div class="visible-xs visible-sm hidden-md hidden-lg">
														<div class="btn-group" dropdown="">
															<button type="button"
																class="btn btn-primary btn-o btn-sm"
																data-toggle="dropdown">
																<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
															</button>
															<ul class="dropdown-menu pull-right dropdown-light"
																role="menu">
																<shiro:hasPermission name="accountmanage:MODIFY">
<%-- 																<c:if test="${(record.balanceCash ne record.balance) or (record.balanceFrost ne record.frost)}"> --%>
																	<li><a class="fn-UpdateBalance"
																		data-userId="${record.userId }">更新</a></li>
<%-- 																</c:if> --%>
																</shiro:hasPermission>
																<shiro:hasPermission name="accountdetail:VIEW">
																<li><a class="fn-info" data-username="${record.username }"
																	data-userId="${record.userId }">查看</a></li>
																</shiro:hasPermission>
															</ul>
														</div>
													</div>
												</td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page"
							action="accountmanage_list"
							paginator="${accountmanageForm.paginator}"></hyjf:paginator>
						<br /> <br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="userId" id="userId" />
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${accountmanageForm.paginatorPage}" />
		<!-- 查询条件 -->
		<div class="form-group">
			<label>用户名</label> <input type="text" name="username" id="username"
				class="form-control input-sm underline"
				value="${accountmanageForm.username}" />
		</div>
		<div class="form-group">
			<label>部门</label>
			<div class="dropdown-menu no-radius">
				<input type="text" class="form-control input-sm underline margin-bottom-10 " value="" id="combotree_search" placeholder="Search" >
				<input type="hidden" id="combotree_field_hidden"  name="combotreeSrch" value="${accountmanageForm.combotreeSrch}">
				<div id="combotree-panel" style="width:270px;height:300px;position:relative;overflow:hidden;">
					<div id="combotree" class="tree-demo" ></div>
				</div>
			</div>

			<span class="input-icon input-icon-right" data-toggle="dropdown" >
				<input id="combotree-field" type="text" class="form-control underline form " readonly="readonly">
				<i class="fa fa-remove fn-ClearDep" style="cursor:pointer;"></i>
			</span>
		</div>
<%-- 			<div class="form-group">
				<label>账户类别</label>
				<div class="row">
					<div class="col-xs-12">
						<div class="radio clip-radio radio-primary ">
							<input type="radio" id="userType0" name="userTypeSearche"
								value="0" class="event-categories"
								${accountmanageForm.userTypeSearche == '0' ? 'checked' : ''}>
							<label for="userType0"> 所有账户 </label> <input type="radio"
								id="userType1" name="userTypeSearche" value="1"
								class="event-categories"
								${accountmanageForm.userTypeSearche == '1' ? 'checked' : ''}>
							<label for="userType1"> 异常账户 </label>
						</div>
					</div>
				</div>
			</div> --%>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/finance/accountmanage/accountmanage_list.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
