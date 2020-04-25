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

<shiro:hasPermission name="pcChannelStatistics:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="渠道统计" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">渠道统计</h1>
			<span class="mainDescription">渠道统计的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="pcChannelStatistics:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${pcChannelStatisticsForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${pcChannelStatisticsForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="pcchannelstatistics:EXPORT">
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
										<th class="center">渠道</th>
										<th class="center">访问数</th>
										<th class="center">注册数</th>
										<th class="center">开户数</th>
										<th class="center">出借人数</th>
										<th class="center">累计充值</th>
										<th class="center">累计出借</th>
										<th class="center">汇直投出借金额</th>
										<th class="center">汇消费出借金额</th>
										<th class="center">汇天利出借金额</th>
										<th class="center">汇添金出借金额</th>
										<th class="center">汇金理财出借金额</th>
										<th class="center">汇转让出借金额</th>
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
													<td class="center">${(pcChannelStatisticsForm.paginatorPage - 1 ) * pcChannelStatisticsForm.paginator.limit + status.index + 1 }</td>
													<td><c:out value="${record.sourceName }"></c:out></td>
													<td align="right"><c:out value="${record.accessNumber }"></c:out></td>
													<td align="right"><c:out value="${record.registNumber }"></c:out></td>
													<td align="right"><c:out value="${record.openAccountNumber }"></c:out></td>
													<td align="right"><c:out value="${record.tenderNumber }"></c:out></td>
													<td align="right"><c:out value="${record.cumulativeRecharge }"></c:out></td>
													<td align="right"><c:out value="${record.cumulativeInvestment }"></c:out></td>
													<td align="right"><c:out value="${record.hztTenderPrice }"></c:out></td>
													<td align="right"><c:out value="${record.hxfTenderPrice }"></c:out></td>
													<td align="right"><c:out value="${record.htlTenderPrice }"></c:out></td>
													<td align="right"><c:out value="${record.htjTenderPrice }"></c:out></td>
													<td align="right"><c:out value="${record.rtbTenderPrice }"></c:out></td>
													<td align="right"><c:out value="${record.hzrTenderPrice }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="pcChannelStatistics:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${pcChannelStatisticsForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="pcChannelStatistics:SEARCH">
				<input type="hidden" name="utmIds" id="utmIds" value="${pcChannelStatisticsForm.utmIds}" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${pcChannelStatisticsForm.paginatorPage}" />
				<div class="form-group">
					<label>时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${pcChannelStatisticsForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${pcChannelStatisticsForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/promotion/pcchannelstatistics/pcchannelstatisticslist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
