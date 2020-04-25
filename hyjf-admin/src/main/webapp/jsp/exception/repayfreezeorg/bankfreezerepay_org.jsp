<%@page import="com.hyjf.common.util.GetDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
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
<c:set var="searchAction" value="#" scope="request"></c:set>
<shiro:hasPermission name="bankrepayFreezeOrg:VIEW" >
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="银行出借撤销" />
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb"
								value="${bankRepayForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb"
								value="${bankRepayForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
							<shiro:hasPermission name="bankrepayFreezeOrg:EXPORT">
								<a class="btn btn-o btn-primary btn-sm fn-Export"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="导出Excel">
									导出Excel <i class="fa fa-Export"></i></a>
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
									<th class="center">项目编号</th>
									<th class="center">资产来源</th>
									<th class="center">计划编号</th>
									<th class="center">借款人</th>
									<th class="center">还款人</th>
									<th class="center">借款期限</th>
									<th class="center">还款期数</th>
									<th class="center">冻结订单号</th>
									<th class="center">借款金额</th>
									<th class="center">应还本息</th>
									<th class="center">还款服务费</th>
									<th class="center">减息金额</th>
									<th class="center">违约金</th>
									<th class="center">逾期罚息</th>
									<th class="center">冻结金额</th>
									<th class="center">提交时间</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty recordList}">
										<tr>
											<td colspan="18">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${(bankRepayForm.paginatorPage -1 ) * bankRepayForm.paginator.limit + status.index + 1 }"></c:out></td>
												<td><c:out value="${record.borrowNid }"></c:out></td>
												<td><c:out value="${record.instName }"></c:out></td>
												<td><c:out value="${record.planNid eq '0' ? '': record.planNid}"></c:out></td>
												<td><c:out value="${record.borrowUserName }"></c:out></td>
												<td><c:out value="${record.repayUserName }"></c:out></td>
												<td><c:out value="${record.borrowPeriod }"></c:out></td>
												<td><c:out value="${record.totalPeriod }"></c:out></td>
												<td><c:out value="${record.orderId }"></c:out></td>
												<td><c:out value="${record.amount }"></c:out></td>
												<td><c:out value="${record.repayAccount }"></c:out></td>
												<td><c:out value="${record.repayFee }"></c:out></td>
												<td><c:out value="${record.lowerInterest }"></c:out></td>
												<td><c:out value="${record.penaltyAmount }"></c:out></td>
												<td><c:out value="${record.defaultInterest }"></c:out></td>
												<td><c:out value="${record.amountFreeze }"></c:out></td>
												<td><hyjf:datetime value="${record.createTimeView }"></hyjf:datetime></td>
												<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<shiro:hasPermission name="bankrepayFreezeOrg:MODIFY">
															<a class="btn btn-transparent btn-xs tooltips fn-UpdateBalance" data-order-id="${record.orderId }" data-borrow-nid="${record.borrowNid }"
															   data-toggle="tooltip" tooltip-placement="top" data-original-title="处理"><i class="fa fa-cloud-download"></i></a>
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
																<shiro:hasPermission name="bankrepayFreezeOrg:MODIFY">
																	<li>
																		<a class="fn-UpdateBalance" data-orderid="${record.orderId }">处理</a>
																	</li>
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
						<shiro:hasPermission name="bankrepayFreezeOrg:SEARCH">
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${bankRepayForm.paginator}"></hyjf:paginator>
						</shiro:hasPermission>
						<br/><br/>
					</div>
				</div>
			</div>
		</div>
		</tiles:putAttribute>	
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="bankrepayFreezeOrg:SEARCH">
				<input type="hidden" name="orderId" id="orderId" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${bankRepayForm.paginatorPage}" />
				<div class="form-group">
					<label>项目编号:</label>
					<input type="text" name="borrowNid" id="borrowNid" class="form-control input-sm underline" value="${bankRepayForm.borrowNid}" />
				</div>
				<div class="form-group">
					<label>资产来源</label>
					<select name="instCode" id="instCode" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
							<option value="${inst.instCode }"
									<c:if test="${inst.instCode eq bankRepayForm.instCode}">selected="selected"</c:if>>
								<c:out value="${inst.instName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>计划编号:</label>
					<input type="text" name="planNid" id="planNid" class="form-control input-sm underline" value="${bankRepayForm.planNid}" />
				</div>
				<div class="form-group">
					<label>借款人:</label>
					<input type="text" name="borrowUserName" id="borrowUserName" class="form-control input-sm underline" value="${bankRepayForm.borrowUserName}" />
				</div>
				<div class="form-group">
					<label>还款人:</label>
					<input type="text" name="repayUserName" id="repayUserName" class="form-control input-sm underline" value="${bankRepayForm.repayUserName}" />
				</div>
				<div class="form-group">
					<label>冻结订单号:</label>
					<input type="text" name="orderIdSrch" id="orderIdSrch" class="form-control input-sm underline" value="${bankRepayForm.orderIdSrch}" />
				</div>
				<div class="form-group">
					<label>提交时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="submitTimeStartSrch" id="submit-start-date-time" class="form-control underline" value="${bankRepayForm.submitTimeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="submitTimeEndSrch" id="submit-end-date-time" class="form-control underline" value="${bankRepayForm.submitTimeEndSrch}" />
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
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/exception/repayfreezeorg/bankfreezerepay_org.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
 
