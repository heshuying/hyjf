<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="rechargefee:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="充值管理" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">充值手续费对账</h1>
			<span class="mainDescription">这里添加充值手续费对账描述。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${rechargeFeeForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${rechargeFeeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" tooltip-placement="top" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" tooltip-placement="top" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" tooltip-placement="top" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i>
								</a>
								<shiro:hasPermission name="rechargefee:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" tooltip-placement="top" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i>
									</a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">账单编号</th>
										<th class="center">账单周期</th>
										<th class="center">累计充值金额</th>
										<th class="center">平台垫付手续费</th>
										<th class="center">应付款</th>
										<th class="center">转账订单号</th>
										<th class="center">状态</th>
										<th class="center">生成时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty rechargeFeeForm.recordList}">
											<tr>
												<td colspan="11">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${rechargeFeeForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((rechargeFeeForm.paginatorPage - 1) * rechargeFeeForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.rechargeNid }"></c:out></td>
													<td class="center"><hyjf:date value="${record.startTime}"></hyjf:date> 至 <hyjf:date value="${record.endTime}"></hyjf:date></td>
													<td class="right"><fmt:formatNumber value="${record.rechargeAmount}" type="number" pattern="#,##0.00#" /></td>
													<td class="right"><fmt:formatNumber value="${record.rechargeFee}" type="number" pattern="#,##0.00#" /></td>
													<td class="right"><fmt:formatNumber value="${record.rechargeFee}" type="number" pattern="#,##0.00#" /></td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.status == '0'?'待付款':'已付款' }"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.addTime}"></hyjf:datetime></td>
													<td class="center">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<a class="btn btn-transparent btn-xs tooltips " href="${webRoot}/finance/recharge/searchAction?userId=${record.userId}&startDate=<hyjf:date value='${record.startTime}'></hyjf:date>&endDate=<hyjf:date value='${record.endTime}'></hyjf:date>&statusSearch=1" data-toggle="tooltip" tooltip-placement="top" data-original-title="充值明细"><i class="fa fa-list"></i></a>
																<a class="btn btn-transparent btn-xs tooltips " href="${webRoot}/finance/transfer/transferList?reconciliationIdSrch=${record.rechargeNid}" data-toggle="tooltip" tooltip-placement="top" data-original-title="转账明细"><i class="fa fa-list"></i></a>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group" dropdown="">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<li><a class="fn-fix" href="${webRoot}/finance/recharge/searchAction?userId=${record.userId}&startDate=<hyjf:date value='${record.startTime}'></hyjf:date>&endDate=<hyjf:date value='${record.endTime}'></hyjf:date>&statusSearch=1">充值明细</a></li>
																		<li><a class="fn-fix" href="${webRoot}/finance/transfer/transferList?reconciliationIdSrch=${record.rechargeNid}" >转账明细</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${rechargeFeeForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>


		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="userId" id="userId" />
			<input type="hidden" name="status" id="status" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${rechargeFeeForm.paginatorPage}" />

			<!-- 查询条件 -->
			<div class="form-group">
				<label>用户名</label> <input type="text" name="userNameSrch" class="form-control input-sm underline" value="${rechargeFeeForm.userNameSrch}" />
			</div>
			<div class="form-group">
				<label>账单编号</label> <input type="text" name="rechargeNidSrch" class="form-control input-sm underline" value="${rechargeFeeForm.rechargeNidSrch}" />
			</div>
			<div class="form-group">
				<label>账单周期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startTimeSrch" id="en-start-date-time" class="form-control underline" value="${rechargeFeeForm.startTimeSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endTimeSrch" id="en-end-date-time" class="form-control underline" value="${rechargeFeeForm.endTimeSrch}" />
				</div>
			</div>
			
			<div class="form-group">
				<label>状态</label> <select name="statusSrch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq rechargeFeeForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="待付款"></c:out>
					</option>
					<option value="1" <c:if test="${'1' eq rechargeFeeForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="已付款"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startDateSrch" id="start-date-time" class="form-control underline" value="${rechargeFeeForm.startDateSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endDateSrch" id="end-date-time" class="form-control underline" value="${rechargeFeeForm.endDateSrch}" />
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

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/finance/rechargefee/rechargefee_list.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
