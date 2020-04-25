<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="转让还款明细" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">	
		<form id="mainForm" method="post" role="form">
			<input type="hidden" name="creditNid" id="creditNid" value="${borrowcreditrepayForm.creditNid}" />
			<input type="hidden" name="assignNid" id="assignNid" value="${borrowcreditrepayForm.assignNid}" />
			<input type="hidden" name="infopaginatorPage" id="paginator-page" value="${borrowcreditrepayForm.infopaginatorPage}" />
			<div class="row bg-white">
				<div class="col-sm-12">
					<div class="panel panel-white">
						<div class="panel-body panel-table">
						<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">项目编号</th>
										<th class="center">还款方式</th>
										<th class="center">项目名称</th>
										<th class="center">还款期数</th>
										<th class="center">待收本金</th>
										<th class="center">待收利息</th>
										<th class="center">待收本息</th>
										<%-- upd by LSY START --%>
										<%-- <th class="center">管理费</th> --%>
										<th class="center">还款服务费</th>
										<%-- upd by LSY END --%>
										<th class="center">还款状态</th>
										<th class="center">应还日期</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowcreditrepayForm.infopaginatorPage -1 ) * borrowcreditrepayForm.infopaginator.limit + status.index + 1 }</td>
													<td><c:out value="${record.borrow.borrowNid }"/></td>
													<td>
													<c:choose>
													<c:when test="${record.borrow.borrowStyle eq 'end'}">
													按月计息，到期还本还息
													</c:when>
													<c:when test="${record.borrow.borrowStyle eq 'month'}">
													等额本息
													</c:when>
													<c:when test="${record.borrow.borrowStyle eq 'principal'}">
													等额本金
													</c:when>
													<c:when test="${record.borrow.borrowStyle eq 'endday'}">
													按天计息，到期还本还息
													</c:when>
													<c:when test="${record.borrow.borrowStyle eq 'endmonth'}">
													先息后本
													</c:when>
													<c:when test="${record.borrow.borrowStyle eq 'season'}">
													按季还款
													</c:when>
													<c:when test="${record.borrow.borrowStyle eq 'endmonths'}">
													按月付息到期还本
													</c:when>
													<c:otherwise>
													${record.borrow.borrowStyle}
													</c:otherwise>
													</c:choose>
													</td>
													<td><c:out value="${record.borrow.name }"/></td>
													<td><c:out value="${record.creditRepay.recoverPeriod }"/></td>
													<td><c:out value="${record.creditRepay.assignCapital }"/></td>
													<td align="right"><c:out value="${record.creditRepay.assignInterest }"/></td>
													<td align="right"><c:out value="${record.creditRepay.assignAccount }"/></td>
													<td align="right"><c:out value="${record.creditRepay.manageFee }"/></td>
													<td align="right">
													<c:if test="${record.creditRepay.status eq 0}">
														还款中
													</c:if>
													<c:if test="${record.creditRepay.status eq 1}">
														已还款
													</c:if></td>
													<td align="right">
													${record.creditRepay.addip }
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
									<%-- add by LSY START --%>
									<tr>
										<th class="center">总计</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<td><fmt:formatNumber value="${sumObject.sumAssignCapital }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.sumAssignInterest }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.sumAssignAccount }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.sumManageFee }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
									<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="borrowcredit:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="infoAction" paginator="${borrowcreditrepayForm.infopaginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
							<div class="form-group margin-bottom-0" align="center">
								<div class="col-sm-offset-2 col-sm-6">
									<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 关闭</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</tiles:putAttribute>											
											
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowcreditrepay/borrowcreditrepay_info.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
