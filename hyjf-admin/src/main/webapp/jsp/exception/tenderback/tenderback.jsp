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

<shiro:hasPermission name="tenderback:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="出借爆标处理" />
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${form.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							<br/>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
									<col style="" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">撤销方</th>
										<th class="center">项目编号</th>
										<th class="center">项目名称</th>
										<th class="center">借款金额</th>
										<th class="center">借到金额</th>
										<th class="center">出借用户名</th>
										<th class="center">出借金额</th>
										<th class="center">出借订单号</th>
										<th class="center">解冻订单号</th>
										<th class="center">撤销操作者</th>
										<th class="center">撤销时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<jsp:useBean id="myDate" class="java.util.Date"/> 
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(form.paginatorPage - 1 ) * form.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:if test="${ record.type eq '0' }">运营方</c:if><c:if test="${ record.type ne '0' }">出借方</c:if></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td><c:out value="${record.borrowName }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.account }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber value="${record.accountYes }" pattern="#,##0.00#" /></td>
													<td><c:out value="${record.userName }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.amount }" pattern="#,##0.00#" /></td>
													<td class="center"><c:out value="${record.ordId }"></c:out></td>
													<td class="center"><c:out value="${record.trxId }"></c:out></td>
													<td><c:out value="${record.createUser }"></c:out></td>
													<c:set target="${myDate}" property="time" value="${record.createTime * 1000}"/> 
													<td class="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${myDate}" type="both"/></td>
												</tr>
											</c:forEach>				
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="tenderback:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${form.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="tenderback:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${form.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>项目名称</label>
					<input type="text" name="borrowNameSrch" id="borrowNameSrch" class="form-control input-sm underline" value="${form.borrowNameSrch}" />
				</div>
				<div class="form-group">
					<label>撤销时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${form.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${form.timeEndSrch}" />
					</div>
				</div>
				<!-- 检索条件 -->
			</shiro:hasPermission>
		</tiles:putAttribute>			
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/tenderback/tenderback.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
