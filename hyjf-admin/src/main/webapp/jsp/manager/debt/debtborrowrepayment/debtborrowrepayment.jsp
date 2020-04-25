<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,还款信息', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="debtborrowrepayment:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款信息" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
.table-striped .checkbox {
	width: 20px;
	margin-right: 0 !important;
	overflow: hidden
}
</style>
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">还款信息</h1>
			<span class="mainDescription">以结款的角度查看还款信息,查询的是每个借款的总还款信息。</span>
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
								<c:set var="jspPrevDsb" value="${debtBorrowRepaymentForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${debtBorrowRepaymentForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a
									class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							 	<shiro:hasPermission name="debtborrowrepayment:EXPORT">
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a> <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>

								<shiro:hasPermission name="debtborrowrepayment:ADD">
								<!-- <a class="btn btn-o btn-primary btn-sm hidden-xs fn-UpdateRecoverFee" data-toggle="tooltip" data-placement="bottom" data-original-title="更新管理费">更新管理费 <i class="fa fa-plus"></i></a> -->
								</shiro:hasPermission>
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
										<th class="center">项目编号</th>
										<th class="center">借款人</th>
										<th class="center">项目名称</th>
										<th class="center">借款期限</th>
										<th class="center">还款方式</th>
										<th class="center">借到金额</th>
										<th class="center">应还本息</th>
										<th class="center">管理费</th>
										<th class="center">还款状态</th>
										<th class="center">下期还款日</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="12">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((debtBorrowRepaymentForm.paginatorPage - 1) * debtBorrowRepaymentForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td><c:out value="${record.borrowUserName }"></c:out></td>
													<td><c:out value="${record.borrowName }"></c:out></td>
													<td><c:out value="${record.borrowPeriod }"></c:out></td>
													<td><c:out value="${record.repayType }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.borrowAccount }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber value="${record.repayAccountAll }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber value="${record.repayFee }" pattern="#,##0.00#" /></td>
													<td class="center">
														<c:if test="${record.status==0 }">
														还款中
														</c:if>
														<c:if test="${record.status==1 }">
														已还款
														</c:if>
													</td>
													<td class="center"><c:out value="${record.repayNextTime }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<c:if test="${record.borrowStyle ne 'end' and record.borrowStyle ne 'endday' }">
																<shiro:hasPermission name="debtborrowrepayment:INFO">
																	<a class="btn btn-transparent btn-xs fn-toRepayPlan" data-id="${record.borrowNid }" data-toggle="tooltip" data-placement="top" data-original-title="还款计划"><i class="fa fa-list-ul fa-white"></i></a>
																</shiro:hasPermission>
															</c:if>
															<c:if test="${record.borrowStyle eq 'end' or record.borrowStyle eq 'endday' }">
																<shiro:hasPermission name="debtborrowrepayment:INFO">
																	<a class="btn btn-transparent btn-xs fn-toRecover" data-id="${record.borrowNid }" data-toggle="tooltip" data-placement="top" data-original-title="详情"><i class="fa fa-list-ul fa-white"></i></a>
																</shiro:hasPermission>
															</c:if>
															<c:if test="${record.status == 0 }">
																<shiro:hasPermission name="debtborrowrepayment:REPAY">
																	<a class="btn btn-transparent btn-xs fn-Repay" data-id="${record.borrowNid }" data-toggle="tooltip" data-placement="top" data-original-title="还款"><i class="fa fa-chevron-circle-down fa-white"></i></a>
																</shiro:hasPermission>
																<shiro:hasPermission name="debtborrowrepayment:REPAY_DELAY">
																	<a class="btn btn-transparent btn-xs fn-delayRepay" data-id="${record.borrowNid }" data-toggle="tooltip" data-placement="top" data-original-title="延期"><i class="fa fa-angle-double-right fa-white"></i></a>
																</shiro:hasPermission>
															</c:if>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<c:if test="${record.borrowStyle ne 'end' and record.borrowStyle ne 'endday' }">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<shiro:hasPermission name="debtborrowrepayment:INFO">
																		<a class="btn btn-transparent btn-xs fn-toRepayPlan" data-id="${record.borrowNid }" data-toggle="tooltip" data-placement="top" data-original-title="还款计划"><i class="fa fa-chevron-circle-down fa-white"></i></a>
																		</shiro:hasPermission>
																	</ul>
																</div>
															</c:if>
															<c:if test="${record.borrowStyle eq 'end' or record.borrowStyle eq 'endday' }">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<shiro:hasPermission name="debtborrowrepayment:INFO">
																		<a class="btn btn-transparent btn-xs fn-toRecover" data-id="${record.borrowNid }" data-toggle="tooltip" data-placement="top" data-original-title="详情"><i class="fa fa-chevron-circle-down fa-white"></i></a>
																		</shiro:hasPermission>
																	</ul>
																</div>
															</c:if>
															<c:if test="${record.status == 0 }">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<a class="btn btn-transparent btn-xs fn-Repay" data-id="${record.borrowNid }" data-toggle="tooltip" data-placement="top" data-original-title="还款"><i class="fa fa-chevron-circle-down fa-white"></i></a>
																	</ul>
																</div>
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<a class="btn btn-transparent btn-xs fn-delayRepay" data-id="${record.borrowNid }" data-toggle="tooltip" data-placement="top" data-original-title="延期"><i class="fa fa-chevron-circle-down fa-white"></i></a>
																	</ul>
																</div>
															</c:if>
														</div>
													</td>
												</tr>
											</c:forEach>
											<tr>
												<th class="center">总计</th>
												<th>&nbsp;</th>
												<th>&nbsp;</th>
												<th>&nbsp;</th>
												<th>&nbsp;</th>
												<th>&nbsp;</th>
												<td align="right"><fmt:formatNumber value="${sumObject.borrowAccountYes }" pattern="#,##0.00#"/></td>
												<td align="right"><fmt:formatNumber value="${sumObject.repayAccountAll }" pattern="#,##0.00#"/></td>
												<td align="right"><fmt:formatNumber value="${sumObject.repayFee }" pattern="#,##0.00#"/></td>
												<th>&nbsp;</th>
												<th>&nbsp;</th>
												<th>&nbsp;</th>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${debtBorrowRepaymentForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${debtBorrowRepaymentForm.paginatorPage}" />
			<input type="hidden" name="nid" id="nid" value="" />
			<input type="hidden" name="borrowNidHidden" id="borrowNidHidden" value="" />
			<div class="form-group">
				<label>项目编号</label> <input type="text" name="borrowNid" id="borrowNid" class="form-control input-sm underline" value="${debtBorrowRepaymentForm.borrowNid}" />
			</div>
			<div class="form-group">
				<label>借款期限</label> <input type="text" name="borrowPeriod" id="borrowPeriod" class="form-control input-sm underline" value="${debtBorrowRepaymentForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2"  />
			</div>
			<div class="form-group">
				<label>借款人</label> <input type="text" name="borrowUserName" class="form-control input-sm underline" value="${debtBorrowRepaymentForm.borrowUserName}" />
			</div>
			<div class="form-group">
				<label>项目名称</label> <input type="text" name="borrowName" class="form-control input-sm underline" value="${debtBorrowRepaymentForm.borrowName}" />
			</div>
			<div class="form-group">
				<label>还款方式</label> <select name="repayType" class="form-control underline form-select2">
					<option value="">全部</option>
					<c:forEach items="${repayTypeList }" var="record" begin="0" step="1" varStatus="status">
						<option value="${record.name}" <c:if test="${record.nid==debtBorrowRepaymentForm.repayType}">selected="selected"</c:if>>${record.name}</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>还款状态</label> <select name="statusSrch" class="form-control underline form-select2">
					<option value="">全部</option>
					<option value="0" <c:if test="${debtBorrowRepaymentForm.statusSrch!=''&&debtBorrowRepaymentForm.statusSrch==0}">selected="selected"</c:if>>还款中</option>
					<option value="1" <c:if test="${debtBorrowRepaymentForm.statusSrch!=''&&debtBorrowRepaymentForm.statusSrch==1}">selected="selected"</c:if>>已还款</option>

				</select>
			</div>
			<div class="form-group">
				<label>应还日期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="repayLastTimeStartSrch" id="repayLastTimeStartSrch" class="form-control underline" value="${debtBorrowRepaymentForm.repayLastTimeStartSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="repayLastTimeEndSrch" id="repayLastTimeEndSrch" class="form-control underline" value="${debtBorrowRepaymentForm.repayLastTimeEndSrch}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/debt/debtborrowrepayment/debtborrowrepayment.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
