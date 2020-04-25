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

<shiro:hasPermission name="couponrepaymonitor:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="投之家日报表" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">投之家日报表</h1>
			<span class="mainDescription">投之家日报表</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
		      		<li class="active"><a href="${webRoot}/promotion/tzj/dayreport/init">统计报表</a></li>
		      		<li><a href="${webRoot}/promotion/tzj/dayreport/chart">统计图表</a></li>
			    </ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${tzjDayReportForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${tzjDayReportForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="couponrepaymonitor:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								   
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
									 <span  style="display:block;float:right;margin-right:20px;line-height:30px"> 更新时间 ：<hyjf:datetimeformat value="${latestUpdateTime}"></hyjf:datetimeformat></span>
								</div>
							<br/>
							<div><span  style="display:block;float:left;margin-right:20px;line-height:30px;font-weight:bold">累计出借人数：${tenderCountTotal } 人</span>
							<span  style="display:block;float:left;margin-right:20px;line-height:30px;font-weight:bold">累计出借总额：${tenderMoneyTotal } 元</span>
							</div>
							<div><span  style="display:block;float:left;margin-right:20px;line-height:30px">由2017-03-27开始累计，截止到昨天23:59:59</span></div>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">日期</th>
										<th class="center">注册</th>
										<th class="center">开户</th>
										<th class="center">开户比</th>
										<th class="center">绑卡</th>
										<th class="center">绑卡比</th>
										<th class="center">新充人数</th>
										<th class="center">新投人数</th>
										<th class="center">新投转化率</th>
										<th class="center">充值人数</th>
										<th class="center">出借人数</th>
										<th class="center">出借额</th>
										<th class="center">首投人数</th>
										<th class="center">首投金额</th>
										<th class="center">复投人数</th>
										<th class="center">复投率</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="17">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(tzjDayReportForm.paginatorPage - 1 ) * tzjDayReportForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.day }"></c:out></td>
													<td class="center"><c:out value="${record.registCount }"></c:out></td>
													<td class="center"><c:out value="${record.openCount}"></c:out></td>
													<td class="center"><c:out value="${record.openRate}"></c:out>%</td>
													<td class="center"><c:out value="${record.cardbindCount}"></c:out></td>
													<td class="center"><c:out value="${record.cardbindRate }"></c:out>%</td>
													<td class="center"><c:out value="${record.rechargenewCount }"></c:out></td>
													<td class="center"><c:out value="${record.tendernewCount }"></c:out></td>
													<td class="center"><c:out value="${record.tendernewRate }"></c:out>%</td>
													<td class="center"><c:out value="${record.rechargeCount }"></c:out></td>
													<td class="center"><c:out value="${record.tenderCount }"></c:out></td>
													<td class="center"><fmt:formatNumber value="${record.tenderMoney }" type="number" pattern="#,##0.00#"/></td>
													<td class="center"><c:out value="${record.tenderfirstCount }"></c:out></td>
													<td class="center"><fmt:formatNumber value="${record.tenderfirstMoney }" type="number" pattern="#,##0.00#"/></td>
													<td class="center"><c:out value="${record.tenderAgainCount }"></c:out></td>
													<td class="center"><c:out value="${record.tenderAgainRate }"></c:out>%</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="couponrepaymonitor:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${tzjDayReportForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
					</div>
				</div>
			</div>
		  </div>
		</tiles:putAttribute>
		
				<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${tzjDayReportForm.paginatorPage}" />
			<shiro:hasPermission name="couponrepaymonitor:SEARCH">
				<div class="form-group">
					<label>统计时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${tzjDayReportForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${tzjDayReportForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>	
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/promotion/tzjdayreport/dayreport.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
