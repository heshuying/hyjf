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

<shiro:hasPermission name="pcchannelrecon:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="PC渠道对账" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">PC渠道对账</h1>
			<span class="mainDescription">PC渠道对账的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<!-- 新增tab区分 -->
					<ul class="nav nav-tabs" id="myTab">
						<!-- 还款明细 -->
						<shiro:hasPermission name="pcchannelrecon:VIEW">
				      		<li class="active"><a href="${webRoot}/promotion/channelreconciliation/hjhinit">智投服务</a></li>
				      	</shiro:hasPermission>
				    	 <!-- 还款-承接明细  -->	
						<shiro:hasPermission name="pcchannelrecon:VIEW">
							<li><a href="${webRoot}/promotion/channelreconciliation/init">散标</a></li>
						</shiro:hasPermission>	
					</ul>
					<div class="tab-content">
					<div class="tab-pane fade in active">	
					<div class="row">
						<div class="col-md-12">
							<div class="search-classic">
								<shiro:hasPermission name="pcchannelrecon:SEARCH">
									<%-- 功能栏 --%>
									<div class="well">
										<c:set var="jspPrevDsb" value="${channelreconciliationHjhForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
										<c:set var="jspNextDsb" value="${channelreconciliationHjhForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
												data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
										<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
												data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
										<shiro:hasPermission name="pcchannelrecon:EXPORT">
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
											<th class="center">用户名</th>
											<th class="center">渠道</th>
											<th class="center" >
												注册时间
											</th>
											<th class="center">智投订单号</th>
											<th class="center">智投编号</th>
											<th class="center">服务回报期限</th>
											<th class="center">授权服务金额</th>
											<th class="center" >
												是否首投
											</th>
											<th class="center">出借时间</th>
										</tr>
									</thead>
									<tbody id="roleTbody">
										<c:choose>
											<c:when test="${empty recordList}">
												<tr><td colspan="10">暂时没有数据记录</td></tr>
											</c:when>
											<c:otherwise>
												<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
													<tr>
														<td class="center">${(channelreconciliationHjhForm.paginatorPage - 1 ) * channelreconciliationHjhForm.paginator.limit + status.index + 1 }</td>
														<td class="center"><c:out value="${record.userName }"></c:out></td>
														<td align="center"><c:out value="${record.utmName }"></c:out></td>
														<td align="center"><hyjf:datetime value="${record.regTime }"></hyjf:datetime></td>
														<td align="center"><c:out value="${record.orderCode }"></c:out></td>
														<td align="center"><c:out value="${record.borrowNid }"></c:out></td>
														<td align="center"><c:out value="${record.borrowPeriod }"></c:out></td>
														<td align="right"><c:out value="${record.investAmount }"></c:out></td>
														<td align="center">
															<c:if test="${record.isFirst ==1 }">是</c:if>
															<c:if test="${record.isFirst ==0 }">否</c:if>
														</td>
														<td align="center"><hyjf:datetime value="${record.investTime }"></hyjf:datetime></td>
													</tr>
												</c:forEach>					
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
								<%-- 分页栏 --%>
								<shiro:hasPermission name="pcchannelrecon:SEARCH">
									<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="hjhSearchAction" paginator="${channelreconciliationHjhForm.paginator}"></hyjf:paginator>
								</shiro:hasPermission>
								<br/><br/>
							</div>
						</div>
					</div>
				</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="pcchannelrecon:SEARCH">
				<%-- <input type="hidden" name="utmIds" id="utmIds" value="${channelreconciliationHjhForm.utmIds}" /> --%>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${channelreconciliationHjhForm.paginatorPage}" />
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userNameSrch" class="form-control input-sm underline" value="${channelreconciliationHjhForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>渠道</label>
					<select name="utmIds" class="form-control underline form-select2">
						<option value="">全部</option>
						<c:forEach items="${utmtTypeList }" var="utmType" begin="0" step="1" varStatus="status">
							<option value="${utmType.sourceId }"
								<c:if test="${utmType.sourceId eq channelreconciliationHjhForm.utmIds}">selected="selected"</c:if>>
								<c:out value="${utmType.sourceName }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="orderCodeSrch" class="form-control input-sm underline" value="${channelreconciliationHjhForm.orderCodeSrch}" />
				</div>
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="borrowNidSrch" class="form-control input-sm underline" value="${channelreconciliationHjhForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>出借时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${channelreconciliationHjhForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${channelreconciliationHjhForm.timeEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>是否首投</label>
					<select name="isFirst" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="1"
								<c:if test="${channelreconciliationHjhForm.isFirst == 1}">selected="selected"</c:if>>
							<c:out value="是"></c:out></option>
						<option value="0"
								<c:if test="${channelreconciliationHjhForm.isFirst == 0}">selected="selected"</c:if>>
							<c:out value="否"></c:out></option>
					</select>
				</div>
				<div class="form-group">
					<label>注册时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="regStartTime" id="reg-start-time" class="form-control underline" value="${channelreconciliationHjhForm.regStartTime}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="regEndTime" id="reg-end-time" class="form-control underline" value="${channelreconciliationHjhForm.regEndTime}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/promotion/reconciliation/channelreconciliationHjh.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
