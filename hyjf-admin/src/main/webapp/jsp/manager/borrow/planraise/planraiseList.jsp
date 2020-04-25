<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="planraise:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="计划募集" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">计划募集</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${planForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${planForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								</div>
							<br/>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">智投编号</th>
										<th class="center">智投名称</th>
										<th class="center">智投类型</th>
										<th class="center">授权服务金额</th>
										<th class="center">服务回报期限</th>
										<th class="center">参考年回报率</th>
										<th class="center">加入进度</th>
										<th class="center">状态</th>
										<th class="center">申购开始时间</th>
										<th class="center">申购截止时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(planForm.paginatorPage - 1 ) * planForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.debtPlanNid }"></c:out></td>
													<td class="center"><c:out value="${record.debtPlanName }"></c:out></td>
													<td class="center"><c:out value="${record.debtPlanTypeName }"></c:out></td>
													<td class="center">
														<fmt:formatNumber type="number" value="${record.debtPlanMoney }" /> 
													</td>
													<td class="center"><c:out value="${record.debtLockPeriod }"></c:out>个月</td>
													<td class="center"><c:out value="${record.expectApr }"></c:out>%</td>
													<td class="center">
														<c:out value="${record.debtPlanAccountScale}"></c:out>%
													</td>
													<td class="center">
														<c:if test="${record.debtPlanStatus eq 3}">
															待开放
														</c:if>
														<c:if test="${record.debtPlanStatus eq 4}">
															募集中
														</c:if>
													</td>
													<td class="center">
														<hyjf:datetimeformat value="${record.buyBeginTime}"></hyjf:datetimeformat>
													</td>
													<td class="center">
														<hyjf:datetimeformat value="${record.buyEndTime}"></hyjf:datetimeformat>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="planraise:MODIFY">
																<c:if test="${record.debtPlanStatus ne '3'}">
																	<a class="btn btn-transparent btn-xs tooltips fn-Modify" data-debtplannid="${record.debtPlanNid }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="运营">运营</a>
																</c:if>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="planraise:MODIFY">
																		<c:if test="${record.debtPlanStatus ne '3'}">
																			<li>
																				<a class="fn-Modify" data-debtplannid="${record.debtPlanNid }">运营</a>
																			</li>
																		</c:if>
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
							<shiro:hasPermission name="planraise:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${planForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
			<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="planNidSrch" id="planNidSrch" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${planForm.paginatorPage}" />
		</tiles:putAttribute>		
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/planraise/planraiseList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
