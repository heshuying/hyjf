<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<%-- 画面功能路径 (ignore) --%>
<shiro:hasPermission name="debtborrowrecover:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="放款明细" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">放款明细</h1>
			<span class="mainDescription">放款明细的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="debtborrowrecover:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${debtborrowRecoverForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${debtborrowRecoverForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="borrow:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
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
										<th class="center">项目编号</th>
										<th class="center">出借订单号</th>
										<th class="center">放款订单号</th>
										<th class="center">出借人</th>
										<th class="center">授权服务金额</th>
										<th class="center">应收服务费</th>
										<th class="center">应放款</th>
										<th class="center">已放款</th>
										<th class="center">放款状态</th>
										<th class="center">放款时间</th>
										<th class="center">出借时间</th>
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
													<td class="center">${(debtborrowRecoverForm.paginatorPage - 1 ) * debtborrowRecoverForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.orderNum }"></c:out></td>
													<td class="center"><c:out value="${record.loanOrdid }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.account}" /></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.servicePrice}" /></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.recoverPrice}" /></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.recoverPriceOver}" /></td>
													<td class="center"><c:out value="${record.isRecover }"></c:out></td>
													<td class="center"><c:out value="${record.addtime }"></c:out></td>
													<td class="center"><c:out value="${record.timeRecover }"></c:out></td>
												</tr>
											</c:forEach>
												<tr>
													<th class="center">总计</th>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${sumAccount.account}" /></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${sumAccount.servicePrice}" /></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${sumAccount.recoverPrice}" /></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${sumAccount.recoverPriceOver}" /></td>
													<td></td>
													<td></td>
													<td></td>
												</tr>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="debtborrowrecover:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${debtborrowRecoverForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="debtborrowrecover:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${debtborrowRecoverForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${debtborrowRecoverForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>出借人</label>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${debtborrowRecoverForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>出借订单号</label>
					<input type="text" name="orderNumSrch" id="orderNumSrch" class="form-control input-sm underline" value="${debtborrowRecoverForm.orderNumSrch}" />
				</div>
				<div class="form-group">
					<label>放款订单号</label>
					<input type="text" name="loanOrdid" id="loanOrdid" class="form-control input-sm underline" value="${debtborrowRecoverForm.loanOrdid}" />
				</div>
				<div class="form-group">
					<label>放款状态</label>
					<select name="isRecoverSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${recoverList }" var="recover" begin="0" step="1" varStatus="status">
							<option value="${recover.nameCd }"
								<c:if test="${recover.nameCd eq debtborrowRecoverForm.isRecoverSrch}">selected="selected"</c:if>>
								<c:out value="${recover.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>出借时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeRecoverStartSrch" id="recover-start-date-time" class="form-control underline" value="${debtborrowRecoverForm.timeRecoverStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeRecoverEndSrch" id="recover-end-date-time" class="form-control underline" value="${debtborrowRecoverForm.timeRecoverEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>放款时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${debtborrowRecoverForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${debtborrowRecoverForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/debt/debtborrowrecover/debtborrowrecover.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
