<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="liquidationManager:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="计划列表" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">计划列表</h1>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="liquidationManager:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb"
										value="${planForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb"
										value="${planForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a
										class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a
										class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="liquidationManager:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
											data-toggle="tooltip" data-placement="bottom"
											data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="检索条件" data-toggle-class="active"
										data-toggle-target="#searchable-panel"><i
										class="fa fa-search margin-right-10"></i> <i
										class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br />
							<%-- 列表一览 --%>
							<table id="equiList"
								class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">智投编号</th>
										<th class="center">服务回报期限</th>
										<th class="center">参考年回报率</th>
										<th class="center">授权服务金额</th>
										<th class="center">应还利息</th>
										<th class="center">应还总额</th>
										<th class="center">清算前年化</th>
										<th class="center">计划可用余额</th>
										<th class="center">冻结金额</th>
										<th class="center">清算到账</th>
										<th class="center">转让进度</th>
										<th class="center">已收服务费</th>
										<th class="center">应清算日期</th>
										<th class="center">实际清算日期</th>
										<th class="center">计划状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="17">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0"
												step="1" varStatus="status">
												<tr>
													<td class="center">${(planForm.paginatorPage - 1 ) * planForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out
															value="${record.debtPlanNid }"></c:out></td>
													<td class="center"><c:out
															value="${record.debtLockPeriod }"></c:out>个月</td>
													<td class="center"><c:out value="${record.expectApr }"></c:out>%</td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.debtPlanMoneyYes }">
																<fmt:formatNumber type="number"
																	value="${record.debtPlanMoneyYes }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.repayAccountInterest }">
																<fmt:formatNumber type="number"
																	value="${record.repayAccountInterest }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.repayAccountAll }">
																<fmt:formatNumber type="number"
																	value="${record.repayAccountAll }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:out value="${record.actualApr }"></c:out>%</td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.debtPlanBalance }">
																<fmt:formatNumber type="number"
																	value="${record.debtPlanBalance }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.debtPlanFrost }">
																<fmt:formatNumber type="number"
																	value="${record.debtPlanFrost }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when
																test="${not empty record.liquidateArrivalAmount }">
																<fmt:formatNumber type="number"
																	value="${record.liquidateArrivalAmount }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center">
														<c:out
															value="${record.liquidateApr }"></c:out>%</td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.serviceFee }">
																<fmt:formatNumber type="number"
																	value="${record.serviceFee }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center">
														<hyjf:date value="${record.liquidateShouldTime}"></hyjf:date>
													</td>
													<td class="center"><c:choose>
															<c:when test="${record.debtPlanStatus == 6 or record.debtPlanStatus == 7 }">
																<hyjf:datetime value="${record.liquidateFactTime}"></hyjf:datetime>
															</c:when>
															<c:otherwise>
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:if
															test="${record.debtPlanStatus eq 0}">
															发起中
														</c:if> <c:if test="${record.debtPlanStatus eq 1}">
															待审核
														</c:if> <c:if test="${record.debtPlanStatus eq 2}">
															审核不通过
														</c:if> <c:if test="${record.debtPlanStatus eq 3}">
															待开放
														</c:if> <c:if test="${record.debtPlanStatus eq 4}">
															募集中
														</c:if> <c:if test="${record.debtPlanStatus eq 5}">
															锁定中
														</c:if> <c:if test="${record.debtPlanStatus eq 6}">
															清算中
														</c:if> <c:if test="${record.debtPlanStatus eq 7}">
															清算中
														</c:if><c:if test="${record.debtPlanStatus eq 8}">
															清算完成
														</c:if> <c:if test="${record.debtPlanStatus eq 9}">
															未还款
														</c:if> <c:if test="${record.debtPlanStatus eq 10}">
															还款中
														</c:if> <c:if test="${record.debtPlanStatus eq 11}">
															还款完成
														</c:if> <c:if test="${record.debtPlanStatus eq 12}">
															流标
														</c:if></td>
													<td class="center">
														<c:set var="nowDate" value="<%=System.currentTimeMillis() / 1000%>"></c:set> 
														<c:choose>
															<c:when
																test="${record.debtPlanStatus eq 5  and record.liquidateShouldTime-nowDate <= 259200}">
																<div class="visible-md visible-lg hidden-sm hidden-xs">
																	<shiro:hasPermission
																		name="liquidationManager:LIQUIDATION">
																		<a class="btn btn-transparent btn-xs fn-liquidation"
																			data-debtPlanNid="${record.debtPlanNid }"
																			data-toggle="tooltip" data-placement="top"
																			data-original-title="清算"><i class="fa fa-pencil"></i></a>
																	</shiro:hasPermission>
																</div>
																<div class="visible-xs visible-sm hidden-md hidden-lg">
																	<div class="btn-group">
																		<button type="button"
																			class="btn btn-primary btn-o btn-sm"
																			data-toggle="dropdown">
																			<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																		</button>
																		<ul class="dropdown-menu pull-right dropdown-light"
																			role="menu">
																			<shiro:hasPermission
																				name="liquidationManager:INFO">
																				<li><a class="fn-liquidation"
																					data-debtplannid="${record.debtPlanNid }">清算</a></li>
																			</shiro:hasPermission>
																		</ul>
																	</div>
																</div>
															</c:when>
															<c:otherwise>
																<div class="visible-md visible-lg hidden-sm hidden-xs">
																	<shiro:hasPermission name="liquidationManager:INFO">
																		<a class="btn btn-transparent btn-xs tooltips fn-Info"
																			data-debtPlanStatus="${record.debtPlanStatus }"
																			data-debtPlanNid="${record.debtPlanNid }"
																			data-toggle="tooltip" data-placement="top"
																			data-original-title="详情"><i
																			class="fa fa-file-text"></i></a>
																	</shiro:hasPermission>
																</div>
																<div class="visible-xs visible-sm hidden-md hidden-lg">
																	<div class="btn-group">
																		<button type="button"
																			class="btn btn-primary btn-o btn-sm fn-Info"
																			data-toggle="dropdown">
																			<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																		</button>
																		<ul class="dropdown-menu pull-right dropdown-light"
																			role="menu">
																			<shiro:hasPermission name="liquidationManager:INFO">
																				<li><a class="fn-Info"
																					data-debtplannid="${record.debtPlanNid }">详情</a></li>
																			</shiro:hasPermission>
																		</ul>
																	</div>
																</div>
															</c:otherwise>
														</c:choose></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="liquidationManager:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page"
									action="searchAction" paginator="${planForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br />
							<br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="liquidationManager:SEARCH">
				<input type="hidden" name="moveFlag" id="moveFlag" value="PLAN_LIST" />
				<input type="hidden" name="planNid" id="planNid" />
				<input type="hidden" name="debtPlanNidSrch" id="debtPlanNidSrch" />
				<input type="hidden" name="debtPlanStatus" id="debtPlanStatus" />
				<input type="hidden" name="paginatorPage" id="paginator-page"
					value="${planForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>智投编号</label> <input type="text" name="planNidSrch"
						id="planNidSrch" class="form-control input-sm underline"
						value="${planForm.planNidSrch}" />
				</div>
				<div class="form-group">
					<label>应清算时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text"
							name="liquidateShouldTimeStartSrch"
							id="liquidateShouldTimeStartSrch" class="form-control underline"
							value="${planForm.liquidateShouldTimeStartSrch}" /> <i
							class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="liquidateShouldTimeEndSrch"
							id="liquidateShouldTimeEndSrch" class="form-control underline"
							value="${planForm.liquidateShouldTimeEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>最新清算时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text"
							name="liquidateFactTimeStartSrch" id="liquidateFactTimeStartSrch"
							class="form-control underline"
							value="${planForm.liquidateFactTimeStartSrch}" /> <i
							class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="liquidateFactTimeEndSrch"
							id="liquidateFactTimeEndSrch" class="form-control underline"
							value="${planForm.liquidateFactTimeEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>状态</label> <select name="planStatusSrch" id="planStatusSrch"
						class="form-control underline form-select2">
						<option value=""></option>
						<option value="-1"
							<c:if test="${planForm.planStatusSrch eq '-1'}">selected="selected"</c:if>>
							全部</option>
						<option value="5"
							<c:if test="${planForm.planStatusSrch eq '5'}">selected="selected"</c:if>>
							锁定中</option>
						<option value="6"
							<c:if test="${planForm.planStatusSrch eq '6'}">selected="selected"</c:if>>
							清算中</option>
					</select>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript'
				src="${webRoot}/jsp/manager/borrow/liquidation/planLiquidationList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
