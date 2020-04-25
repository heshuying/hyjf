<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
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
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="htjstatis:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="数据中心明细" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">数据中心明细</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="htjstatis:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${htlstatisForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${htlstatisForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<shiro:hasPermission name="loandetail:EXPORT">
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
										<th class="center">日期</th>
										<th class="center">待成交资产-专属资产</th>
										<th class="center">待成交资产--债权转让</th>
										<th class="center">计划持有债权数量-专属资产</th>
										<th class="center">计划持有债权数量-债权转让</th>
										<th class="center">计划持有债权待还总额</th>
										<th class="center">计划持有债权已还总额</th>
										<th class="center">出借人加入总额</th>
										<th class="center">应还款总额</th>
										<th class="center">已还款总额</th>
										<th class="center">到期公允价值</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty planStatis}">
											<tr><td colspan="17">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${planStatis }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(htlstatisForm.paginatorPage - 1 ) * htlstatisForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.dataHour }"></c:out></td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.waitInvest }">
																<fmt:formatNumber type="number" value="${record.waitInvest }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.waitCredit }">
																<fmt:formatNumber type="number" value="${record.waitCredit }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.yesInvest }">
																<fmt:formatNumber type="number" value="${record.yesInvest }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.yesCredit }">
																<fmt:formatNumber type="number" value="${record.yesCredit }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.waitRepay }">
																<fmt:formatNumber type="number" value="${record.waitRepay }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.yesRepay }">
																<fmt:formatNumber type="number" value="${record.yesRepay }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.planAccedeAll }">
																<fmt:formatNumber type="number" value="${record.planAccedeAll }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.planRepayWait }">
																<fmt:formatNumber type="number" value="${record.planRepayWait }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.planRepayYes }">
																<fmt:formatNumber type="number" value="${record.planRepayYes }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.expireFairValue }">
																<fmt:formatNumber type="number" value="${record.expireFairValue }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="htjstatis:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchStatis" paginator="${htlstatisForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="loandetail:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${htlstatisForm.paginatorPage}" />
					<div class="form-group">
					<label>日期</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStart" id="timeStart" class="form-control underline" value="${htlstatisForm.timeStart}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEnd" id="timeEnd" class="form-control underline" value="${htlstatisForm.timeEnd}" />
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

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/htj/statis/statisDetailList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
