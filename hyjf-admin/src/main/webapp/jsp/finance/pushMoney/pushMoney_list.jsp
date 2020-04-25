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
<c:set var="searchAction" value="" scope="request"></c:set>


<shiro:hasPermission name="pushMoneyManage:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="提成管理" />

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">提成管理</h1>
		<span class="mainDescription">本功能可以查询提成明细和发提成操作。</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<hyjf:message key="pushMoney-error"></hyjf:message>
		<hyjf:message key="pushMoney-success"></hyjf:message>
		<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb" value="${pushMoneyForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb" value="${pushMoneyForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a
									class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
							<shiro:hasPermission name="pushMoneyManage:EXPORT">
							<a class="btn btn-o btn-primary btn-sm fn-Export"
									data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-plus "></i> </a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 提现列表一览 --%>
						<table id="pushMoneyList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">项目编号</th>
									<th class="center">项目名称</th>
									<th class="center">借款期限</th>
									<th class="center">融资金额</th>
									<th class="center">提成总额</th>
									<th class="center">放款时间</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty pushMoneyForm.recordList}">
										<tr>
											<td colspan="8">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${pushMoneyForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((pushMoneyForm.paginatorPage - 1) * pushMoneyForm.paginator.limit) + 1 }"></c:out></td>
												<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
												<td><c:out value="${record.borrowName }"></c:out></td>
												<td align="right">
													<c:out value="${record.borrowPeriod }"></c:out>
													<c:choose>
														<c:when test="${record.borrowStyle=='endday' }">天</c:when>
														<c:otherwise>个月</c:otherwise>
													</c:choose>
												</td>
												<td class="text-right"><fmt:formatNumber value="${record.account}" type="number" pattern="#,##0.00#" /> </td>
												<td class="text-right"><fmt:formatNumber value="${record.commission}" type="number" pattern="#,##0.00#" /> </td>
												<td class="center"><c:out value="${record.recoverLastTime }"></c:out></td>
												<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<c:if test="${record.webStatus eq 1 }">
															<a class="btn btn-transparent btn-xs text-green" data-id="${record.borrowNid }" data-toggle="tooltip" tooltip-placement="top" data-original-title="提成明细"
																href="pushMoneyList?borrowNidSearch=${record.borrowNid }">
															提成明细</a>
														</c:if>
														<c:if test="${record.webStatus ne 1 }">
															<shiro:hasPermission name="pushMoneyManage:CALCULATE">
															<a class="btn btn-transparent btn-xs fn-Calculator" data-id="${record.borrowNid }" data-toggle="tooltip" tooltip-placement="top" data-original-title="计算提成">
															计算提成</a>
															</shiro:hasPermission>
														</c:if>
													</div>
													<div class="visible-xs visible-sm hidden-md hidden-lg">
														<div class="btn-group" dropdown="">
															<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
															</button>
															<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																<c:if test="${record.webStatus eq 1 }">
																	<li><a class="fn-Details" data-id="${record.borrowNid }" href="pushMoneyList?borrowNidSearch=${record.borrowNid }">提成明细</a></li>
																</c:if>

																<c:if test="${record.webStatus ne 1 }">
																	<shiro:hasPermission name="pushMoneyManage:CALCULATE">
																	<li><a class="fn-Calculator" data-id="${record.borrowNid }">计算提成</a></li>
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
						<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${pushMoneyForm.paginator}"></hyjf:paginator>
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
		<input type="hidden" name="borrowNid" id="borrowNid" />
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${pushMoneyForm.paginatorPage}" />
		<!-- 查询条件 -->
		<div class="form-group">
			<label>项目编号</label> <input type="text" name="borrowNidSearch" class="form-control input-sm underline" value="${pushMoneyForm.borrowNidSearch}" />
		</div>
		<div class="form-group">
			<label>项目名称</label> <input type="text" name="borrowNameSearch" class="form-control input-sm underline" value="${pushMoneyForm.borrowNameSearch}" />
		</div>
		<div class="form-group">
			<label>放款时间</label>
			<div class="input-group input-daterange datepicker">
				<span class="input-icon">
				<input type="text" name="recoverDateStart" id="start-date-time" class="form-control underline" value="${pushMoneyForm.recoverDateStart}" />
				<i class="ti-calendar"></i> </span>
				<span class="input-group-addon no-border bg-light-orange">~</span>
				<span class="input-icon">
				<input type="text" name="recoverDateEnd" id="end-date-time" class="form-control underline" value="${pushMoneyForm.recoverDateEnd}" />
				<i class="ti-calendar"></i> </span>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/finance/pushMoney/pushMoney_list.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
