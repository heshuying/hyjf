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

<shiro:hasPermission name="planrelease:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="计划发布" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">计划发布</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="planrelease:SEARCH">
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
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
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
										<th class="center">服务回报期限</th>
										<th class="center">参考年回报率</th>
										<th class="center">状态</th>
										<th class="center">申购开始时间</th>
										<th class="center">申购截止时间</th>
										<th class="center">发起时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="11">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(planForm.paginatorPage - 1 ) * planForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.debtPlanNid }"></c:out></td>
													<td class="center"><c:out value="${record.debtPlanName }"></c:out></td>
													<td class="center"><c:out value="${record.debtPlanTypeName }"></c:out></td>
													<td class="center"><c:out value="${record.debtLockPeriod }"></c:out>个月</td>
													<td class="center"><c:out value="${record.expectApr }"></c:out>%</td>
													<td class="center">
														<c:if test="${record.debtPlanStatus eq 0}">
															发起中
														</c:if>
														<c:if test="${record.debtPlanStatus eq 1}">
															待审核
														</c:if>
														<c:if test="${record.debtPlanStatus eq 2}">
															审核不通过
														</c:if>
													</td>
													<td class="center">
														<hyjf:datetimeformat value="${record.buyBeginTime}"></hyjf:datetimeformat>
													</td>
													<td class="center">
														<hyjf:datetimeformat value="${record.buyEndTime}"></hyjf:datetimeformat>
													</td>
													<td class="center">
														<hyjf:datetimeformat value="${record.createTime}"></hyjf:datetimeformat>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="planrelease:TISHEN">
																<c:if test="${record.debtPlanStatus eq '0' or record.debtPlanStatus eq '2'}">
																	<a class="btn btn-transparent btn-xs tooltips fn-Tishen" data-debtplannid="${record.debtPlanNid }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="提审">提审</a>
																</c:if>
															</shiro:hasPermission>
															<c:if test="${ record.debtPlanStatus ne '0' and record.debtPlanStatus ne '2'}">
																<shiro:hasPermission name="planrelease:AUDIT">
																	<a class="btn btn-transparent btn-xs tooltips fn-audit" data-debtplannid="${record.debtPlanNid }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="审核">审核</a>
																</shiro:hasPermission>
															</c:if>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="planrelease:TISHEN">
																		<c:if test="${record.debtPlanStatus eq '0' or record.debtPlanStatus eq '2'}">
																			<li>
																				<a class="fn-Tishen" data-debtplannid="${record.debtPlanNid }">提审</a>
																			</li>
																		</c:if>
																	</shiro:hasPermission>
																	<c:if test="${record.debtPlanStatus ne '0' and record.debtPlanStatus ne '2'}">
																		<shiro:hasPermission name="planrelease:AUDIT">
																			<li>
																				<a class="fn-audit" data-debtplannid="${record.debtPlanNid }" >审核</a>
																			</li>
																		</shiro:hasPermission>
																	</c:if>
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
							<shiro:hasPermission name="planrelease:SEARCH">
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
			<shiro:hasPermission name="planrelease:SEARCH">
				<input type="hidden" name="moveFlag" id="moveFlag" value="PLAN_LIST"/>
				<input type="hidden" name="debtPlanNid" id="debtPlanNid" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${planForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${planForm.planNidSrch}" />
				</div>
				<div class="form-group">
					<label>智投名称</label>
					<input type="text" name="planNameSrch" id="planNameSrch" class="form-control input-sm underline" value="${planForm.planNameSrch}" />
				</div>
				<div class="form-group">
					<label>智投类型</label>
						<select name="planTypeSrch" id="planTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${planTypeList }" var="planType" begin="0" step="1" varStatus="status">
							<option value="${planType.debtPlanType }"
								<c:if test="${planType.debtPlanType eq planForm.planTypeSrch}">selected="selected"</c:if>>
								<c:out value="${planType.debtPlanTypeName }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>状态</label>
					<select name="planStatusSrch" id="planStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
							<option value="0"<c:if test="${planForm.planStatusSrch eq '0'}">selected="selected"</c:if>>
								发起中</option>
							<option value="1"<c:if test="${planForm.planStatusSrch eq '0'}">selected="selected"</c:if>>
								待审核</option>
							<option value="2"<c:if test="${planForm.planStatusSrch eq '0'}">selected="selected"</c:if>>
								审核不通过</option>
					</select>
				</div>
				<div class="form-group">
					<label>发起时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${planForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
							<span class="input-group-addon no-border bg-light-orange">~</span>
							<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${planForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/planrelease/planreleaseList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
