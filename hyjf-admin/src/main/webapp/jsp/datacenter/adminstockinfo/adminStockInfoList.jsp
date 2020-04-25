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

<shiro:hasPermission name="DATACENTERCOUPON:VIEW" >
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="修改记录" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">记录</h1>
			<span class="mainDescription">本功能可以修改查询相应的记录。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="DATACENTERCOUPON:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${AdminStockInfoForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${AdminStockInfoForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
								    <tr>
										<th class="center">序号</th>	
										<th class="center">开盘价格</th>
										<th class="center">当前价格</th>	
										<th class="center">增幅</th>	
										<th class="center">降幅</th>	
										<th class="center">成交量</th>	
										<th class="center">市盈率</th>
										<th class="center">每股收益</th>		
										<th class="center">总市值</th>	
										<th class="center">时间</th> 
								</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty AdminStockInfoForm.recordList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
 											<c:forEach items="${AdminStockInfoForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${record.id}"></c:out></td>
													<td class="center"><c:out value="${record.openPrice}"></c:out></td>
													<td class="center"><c:out value="${record.nowPrice}"></c:out></td>
													<td class="center"><c:out value="${record.increase}"></c:out></td>
													<td class="center"><c:out value="${record.decline}"></c:out></td>
													<td class="center"><c:out value="${record.volume}"></c:out></td>
													<td class="center"><c:out value="${record.peRatio}"></c:out></td>
													<td class="center"><c:out value="${record.eps}"></c:out></td>
													<td class="center"><c:out value="${record.marketCap}"></c:out></td>
													<td class="center"><c:out value="${record.date}"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<%--ADMIN 不用Shiro --%>
															<c:if test="${IS_ADMIN}">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-id="${record.id}" data-toggle="tooltip"
																	data-placement="top" data-original-title="修改"><i
																	class="fa fa-pencil"></i></a>
																<a class="btn btn-transparent btn-xs tooltips fn-Delete"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="删除"><i
																	class="fa fa-times fa fa-white"></i></a>
															</c:if>
														</div>
													</td>											
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="DATACENTERCOUPON:SEARCH">
 								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="adminStockInfoAction" paginator="${AdminStockInfoForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />
 			<input type="hidden" name="paginatorPage" id="paginator-page" value="${preRegistChannelExclusiveActivityListForm.paginatorPage == null ? 0 : preRegistChannelExclusiveActivityListForm.paginatorPage}" />
			<input type="hidden" name="fid" id="fid" value="${fid}" /> 
			
			<div class="form-group">
					<label>时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${AdminStockInfoForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${AdminStockInfoForm.timeEndSrch}" />
					</div>
			</div>
			
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/datacenter/adminstockinfo/adminStockInfoList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
