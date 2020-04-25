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

<shiro:hasPermission name="productIntoRecord:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="转入记录" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇天利转入记录</h1>
			<span class="mainDescription">本功能可以查看汇天利转入记录信息</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="productIntoRecord:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${productIntoRecordForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${productIntoRecordForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
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
										<th class="center">订单号</th>
										<th class="center">用户名</th>
										<th class="center">推荐人</th>
										<th class="center">转入金额</th>
										<th class="center">实际到账</th>
										<th class="center">本金总额</th>
										<th class="center">操作平台</th>
										<th class="center">状态</th>
										<th class="center">转入时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty productIntoRecordForm.recordList}">
											<tr><td colspan="10">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${productIntoRecordForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(productIntoRecordForm.paginatorPage - 1 ) * productIntoRecordForm.paginator.limit + status.index + 1 }</td>
	    											<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td><c:out value="${record.refername }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.amount }" pattern="#,##0.00#" /></td>
													<td align="right">
														<c:if test="${record.investStatus == 0 }">
															<fmt:formatNumber value="${record.amount }" pattern="#,##0.00#" />
														</c:if>
														<c:if test="${record.investStatus !=0 }">
															0.00
														</c:if>
													</td>
													<td align="right"><fmt:formatNumber value="${record.balance }" pattern="#,##0.00#" /></td>
													<td class="center">
														<c:forEach items="${clients }" var="cnt" begin="0" step="1">
															<c:if test="${cnt.nameCd eq record.client}"><c:out value="${cnt.name }"></c:out></c:if>
														</c:forEach>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${record.investStatus==0}">成功</c:when>
														    <c:when test="${record.investStatus==1}">未付款</c:when>
														    <c:when test="${record.investStatus==2}">失败</c:when>
													    </c:choose>
													</td>
													<td class="center"><c:out value="${record.investTime }"></c:out></td>
													<td class="center">
													<shiro:hasPermission name="producterror:CHULI">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<a class="btn btn-transparent btn-xs fn-Handle" 
																	data-toggle="tooltip" tooltip-placement="top" data-id="${record.id }" data-original-title="处理"><i class="fa fa-gavel"></i></a>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<li>
																		<a href= "" class="fn-Handle" data-id="${record.id }" >处理</a>
																	</li>
																</ul>
															</div>
														</div>
														</shiro:hasPermission>
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="producterror:VIEW">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="into" paginator="${productIntoRecordForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="producterror:SEARCH">
				<input type="hidden" name="recordId" id="recordId" />
			    <input type="hidden" name="paginatorPage" id="paginator-page" value="${productIntoRecordForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label> 
					<input type="text" name="usernameSrh" class="form-control input-sm underline"  maxlength="20" value="${ productIntoRecordForm.usernameSrh}" />
				</div>
				<div class="form-group">
					<label>推荐人</label> 
					<input type="text" name="refernameSrh" class="form-control input-sm underline" maxlength="20" value="${productIntoRecordForm.refernameSrh}"/>
				</div>
				<div class="form-group">
					<label>操作平台</label> 
					<select name="clientSrh" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${clients }" var="cnt" begin="0" step="1">
							<option value="${cnt.nameCd }"
								<c:if test="${cnt.nameCd eq productIntoRecordForm.clientSrh}">selected="selected"</c:if>>
								<c:out value="${cnt.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>状态</label>
					<select name="investStatusSrh" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${productIntoRecordForm.investStatusSrh=='0'}">selected</c:if>>成功</option>
						<option value="1" <c:if test="${productIntoRecordForm.investStatusSrh=='1'}">selected</c:if>>未付款</option>
						<option value="2" <c:if test="${productIntoRecordForm.investStatusSrh=='2'}">selected</c:if>>失败</option>
					</select>
				</div>
				<div class="form-group">
					<label>转入时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${productIntoRecordForm.timeStartSrch}" />
							<i class="ti-calendar"></i> 
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<span class="input-icon">
							<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${productIntoRecordForm.timeEndSrch}" />
							<i class="ti-calendar"></i> 
						</span>
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/htlexception/into.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
