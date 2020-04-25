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
<shiro:hasPermission name="borrowrecover:VIEW">
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
							<shiro:hasPermission name="borrowrecover:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowRecoverForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowRecoverForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="borrowrecover:EXPORT">
										<shiro:hasPermission name="borrowrecover:ORGANIZATION_VIEW">
											<a class="btn btn-o btn-primary btn-sm hidden-xs fn-EnhanceExport"
											   data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
										</shiro:hasPermission>
										<shiro:lacksPermission name="borrowrecover:ORGANIZATION_VIEW">
											<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
											   data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
										</shiro:lacksPermission>
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
										<%-- add by LSY START --%>
										<%--<th class="center">资产来源</th>--%>
										<%-- add by LSY END --%>
										<th class="center">智投编号</th>
										<th class="center">放款批次号</th>
										<th class="center">出借订单号</th>
										<th class="center">放款订单号</th>
										<%-- UPD BY LIUSHOUYI 合规检查 START --%><%--
										<th class="center">合作机构编号</th>
										<th class="center">机构编号</th>--%>
										<%-- UPD BY LIUSHOUYI 合规检查 END --%>
										<th class="center">出借人</th>
										<th class="center">出借金额</th>
										<%-- upd by LSY START --%>
										<%-- <th class="center">应收服务费</th> --%>
										<%--<th class="center">放款服务费</th>--%>
									    <%-- upd by LSY END --%>
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
											<%-- UPD BY LSY START --%>
											<%-- <tr><td colspan="14">暂时没有数据记录</td></tr> --%>
											<tr><td colspan="16">暂时没有数据记录</td></tr>
											<%-- UPD BY LSY END --%>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowRecoverForm.paginatorPage - 1 ) * borrowRecoverForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<%-- add by LSY START --%>
													<%--<td class="center"><c:out value="${record.instName }"></c:out></td>--%>
													<%-- add by LSY END --%>
													<td class="center"><c:out value="${record.planNid }"></c:out></td>
													<td class="center"><c:out value="${record.loanBatchNo }"></c:out></td>
													<td class="center"><c:out value="${record.orderNum }"></c:out></td>
													<td class="center"><c:out value="${record.loanOrdid }"></c:out></td>
													<%--<td class="center"><c:out value="${record.instCode }"></c:out></td>--%>
													<td><c:out value="${record.username }"></c:out></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.account}" /></td>
													<%--<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${record.servicePrice}" /></td>--%>
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
													<%-- add by LSY START --%>
													<%--<td></td>--%>
													<%-- add by LSY END --%>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<%--<td></td>--%>
													<td></td>
													<td align="right"><fmt:formatNumber pattern="#,##0.00#" value="${sumAccount.account}" /></td>
													<%--<td align="right"></td>--%>
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
							<shiro:hasPermission name="borrowrecover:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${borrowRecoverForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowrecover:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowRecoverForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${borrowRecoverForm.borrowNidSrch}" />
				</div>
				<%-- add by LSY START --%>
				<%--<div class="form-group">--%>
					<%--<label>资产来源</label>--%>
					<%--<select name="instCodeSrch" id="instCodeSrch" class="form-control underline form-select2">--%>
						<%--<option value=""></option>--%>
						<%--<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">--%>
							<%--<option value="${inst.instCode }"--%>
								<%--<c:if test="${inst.instCode eq borrowRecoverForm.instCodeSrch}">selected="selected"</c:if>>--%>
								<%--<c:out value="${inst.instName }"></c:out>--%>
							<%--</option>--%>
						<%--</c:forEach>--%>
					<%--</select>--%>
				<%--</div>--%>
				<%-- add by LSY END --%>
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${borrowRecoverForm.planNidSrch}" />
				</div>
				<div class="form-group">
					<label>放款批次号</label>
					<input type="text" name="loanBatchNo" id="loanBatchNo" class="form-control input-sm underline" value="${borrowRecoverForm.loanBatchNo}" />
				</div>
				<div class="form-group">
					<label>出借人</label>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowRecoverForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>出借订单号</label>
					<input type="text" name="orderNumSrch" id="orderNumSrch" class="form-control input-sm underline" value="${borrowRecoverForm.orderNumSrch}" />
				</div>
				<div class="form-group">
					<label>放款订单号</label>
					<input type="text" name="loanOrdid" id="loanOrdid" class="form-control input-sm underline" value="${borrowRecoverForm.loanOrdid}" />
				</div>
				<%--<div class="form-group">--%>
					<%-- UPD BY LIUSHOUYI 合规检查 START --%>
					<%--<label>合作机构编号</label>--%>
					<%--<label>机构编号</label>--%>
					<%-- UPD BY LIUSHOUYI 合规检查 END --%>
					<%--<input type="text" name="instCodeOrgSrch" id="instCodeOrgSrch" class="form-control input-sm underline" value="${borrowRecoverForm.instCodeOrgSrch}" />--%>
				<%--</div>--%>
				<div class="form-group">
					<label>放款状态</label>
					<select name="isRecoverSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${recoverList }" var="recover" begin="0" step="1" varStatus="status">
							<option value="${recover.nameCd }"
								<c:if test="${recover.nameCd eq borrowRecoverForm.isRecoverSrch}">selected="selected"</c:if>>
								<c:out value="${recover.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>出借时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeRecoverStartSrch" id="recover-start-date-time" class="form-control underline" value="${borrowRecoverForm.timeRecoverStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeRecoverEndSrch" id="recover-end-date-time" class="form-control underline" value="${borrowRecoverForm.timeRecoverEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>放款时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${borrowRecoverForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${borrowRecoverForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowrecover/borrowrecover.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
