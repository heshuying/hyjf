<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="productinfo:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="转入记录" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">每日报表</h1>
			<span class="mainDescription">本功能可以查看汇天利每日报表信息</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="productinfo:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${productinfoForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${productinfoForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="productinfo:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
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
										<th class="center">日期</th>
										<th class="center">转入用户数</th>
										<th class="center">转出用户数</th>
										<th class="center">转入金额</th>
										<th class="center">转出金额</th>
										<th class="center">转出收益</th>
										<th class="center">本金总额</th>
										<th class="center">资管公司账户余额</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty productinfoForm.recordList}">
											<tr><td colspan="9">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${productinfoForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
	    											<td class="center">${(productinfoForm.paginatorPage - 1 ) * productinfoForm.paginator.limit + status.index + 1 }</td>
	    											<td class="center"><c:out value="${record.dataDate }"></c:out></td>
													<td align="right"><c:out value="${record.inCount }"></c:out></td>
													<td align="right"><c:out value="${record.outCount }"></c:out></td>
													<td align="right">
													<c:if test="${record.inAmount == null }">0.00</c:if>
													<c:if test="${record.inAmount != null }"><fmt:formatNumber value="${record.inAmount }" pattern="#,##0.00#" /></c:if>
													</td>
													<td align="right">
													<c:if test="${record.outAmount == null }">0.00</c:if>
													<c:if test="${record.outAmount != null }"><fmt:formatNumber value="${record.outAmount }" pattern="#,##0.00#" /></c:if>
													</td>
													<td align="right">
													<c:if test="${record.outInterest == null }">0.00</c:if>
													<c:if test="${record.outInterest != null }">
													<fmt:formatNumber value="${record.outInterest }" pattern="0.00"/>
													</c:if>
													</td>
													<td align="right">
													<c:if test="${record.investAmount == null }">0.00</c:if>
													<c:if test="${record.investAmount != null }"><fmt:formatNumber value="${record.investAmount }" pattern="#,##0.00#" /></c:if>
													</td>
													<td align="right"><fmt:formatNumber value="${record.loanBalance }" pattern="#,##0.00#" /></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="productinfo:VIEW">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${productinfoForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="productinfo:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${productinfoForm.paginatorPage}" />
				<div class="form-group">
					<label>日期</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${productinfoForm.timeStartSrch}" />
							<i class="ti-calendar"></i> 
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<span class="input-icon">
							<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${productinfoForm.timeEndSrch}" />
							<i class="ti-calendar"></i> 
						</span>
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/htl/productinfo/productinfo.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
