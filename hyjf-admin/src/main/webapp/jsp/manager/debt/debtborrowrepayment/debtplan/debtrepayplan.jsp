<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,还款计划', ',')}" var="functionPaths"
	scope="request"></c:set>

<shiro:hasPermission name="borrowrepayment:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款计划" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css"
				rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
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
			<h1 class="mainTitle">还款计划</h1>
			<span class="mainDescription">以借款的角度查看还款信息,查询的是分期还款的情况下每个借款每一期的还款信息。</span>
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
								<c:set var="jspPrevDsb" value="${debtBorrowRepaymentPlanForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${debtBorrowRepaymentPlanForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>

								<shiro:hasPermission name="borrowrepayment:EXPORT">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
											data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th>项目编号</th>
										<th>项目名称</th>
										<th>还款期数</th>
										<th>应还本金</th>
										<th>应还利息</th>
										<th>应还本息</th>
										<th>管理费</th>
										<th>还款状态</th>
										<th>应还日期</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="11">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0"
												step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((debtBorrowRepaymentPlanForm.paginatorPage - 1) * debtBorrowRepaymentPlanForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td><c:out value="${record.borrowName }"></c:out></td>
													<td>第<c:out value="${record.repayPeriod }"></c:out>期
													</td>
													<td align="right"><fmt:formatNumber value="${record.repayCapital }" pattern="#,##0.00#"/></td>
													<td align="right"><fmt:formatNumber value="${record.repayInterest }" pattern="#,##0.00#"/></td>
													<td align="right"><fmt:formatNumber value="${record.repayAccount }" pattern="#,##0.00#"/></td>
													<td align="right"><fmt:formatNumber value="${record.repayFee }" pattern="#,##0.00#"/></td>
													<td class="center"><c:if test="${record.status==0 }">
													未还款
													</c:if> <c:if test="${record.status==1 }">
													已还款
													</c:if></td>
													<td class="center"><c:out value="${record.repayLastTime }"></c:out></td>
													<td class="center">
															<c:set var="idstr"
																value="${record.borrowNid}:${record.repayPeriod }"></c:set>
															<shiro:hasPermission name="borrowrepayment:INFO">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<a class="btn btn-transparent btn-xs fn-toRepayPlanDetail" data-id="${idstr}"
																		data-toggle="tooltip" data-placement="top" data-original-title="详情">详情</a>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<li>
																			<a class="fn-toRepayPlanDetail" data-id="${idstr}">详情</a>
																		</li>
																	</ul>
																</div>
															</div>
 															</shiro:hasPermission>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
									<tr>
										<th class="center">总计</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumObject.repayCapital }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.repayInterest }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.repayAccount }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.repayFee }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="init" paginator="${debtBorrowRepaymentPlanForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${debtBorrowRepaymentPlanForm.paginatorPage}" />
			<div class="form-group">
				<label>项目编号</label> <input type="text" name="borrowNid"
					id="borrowNid" class="form-control input-sm underline"
					value="${debtBorrowRepaymentPlanForm.borrowNid}" /> <input
					type="hidden" name="repayPeriod" id="repayPeriod"
					class="form-control input-sm underline"
					value="${debtBorrowRepaymentPlanForm.repayPeriod}" />
			</div>
			<div class="form-group">
				<label>项目名称</label> <input type="text" name="borrowName"
					class="form-control input-sm underline"
					value="${debtBorrowRepaymentPlanForm.borrowName}" />
			</div>
			<div class="form-group">
				<label>还款状态</label> <select name="status"
					class="form-control underline form-select2">
					<option value="">全部</option>
					<option value="0"
						<c:if test="${debtBorrowRepaymentPlanForm.status==0}">selected="selected"</c:if>>未还款</option>
					<option value="1"
						<c:if test="${debtBorrowRepaymentPlanForm.status==1}">selected="selected"</c:if>>已还款</option>

				</select>
			</div>
			<div class="form-group">
				<label>应还日期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="repayLastTimeStartSrch" id="repayLastTimeStartSrch"
						class="form-control underline"
						value="${debtBorrowRepaymentPlanForm.repayLastTimeStartSrch}" /> <i
						class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="repayLastTimeEndSrch"
						id="repayLastTimeEndSrch" class="form-control underline"
						value="${debtBorrowRepaymentPlanForm.repayLastTimeEndSrch}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<!-- Form表单插件 -->
			<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript'
				src="${webRoot}/jsp/manager/debt/debtborrowrepayment/debtplan/debtrepayplan.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
