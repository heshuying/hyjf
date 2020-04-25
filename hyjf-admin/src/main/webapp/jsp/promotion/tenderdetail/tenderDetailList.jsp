<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="userTenderDetailList:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="投之家用户出借明细" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">投之家用户出借明细列表</h1>
			<span class="mainDescription">投之家用户出借明细列表</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="userTenderDetailList:SEARCH">
							<div class="well">
									<c:set var="jspPrevDsb" value="${userTenderDetailForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${userTenderDetailForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="userTenderDetailList:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							</shiro:hasPermission>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">渠道</th>
										<th class="center">注册时间</th>
										<th class="center">开户时间</th>
										<th class="center">出借金额</th>
										<th class="center">项目编号</th>
										<th class="center">标的期限</th>
										<th class="center">出借时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty userTenderDetailForm.recordList}">
											<tr>
												<td colspan="9">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${userTenderDetailForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((userTenderDetailForm.paginatorPage - 1) * userTenderDetailForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<c:choose>
														<c:when test="${!empty record.appUtmSource }">
															<td class="center"><c:out value="${record.appUtmSource }"></c:out></td>
														</c:when>
														<c:otherwise>
															<td class="center"><c:out value="${record.pcUtmSource }"></c:out></td>
														</c:otherwise>
													</c:choose>
													<td class="center"><c:out value="${record.regTime }"></c:out></td>
													<td class="center"><c:out value="${record.openAccountTime }"></c:out></td>
													<td class="center"><c:out value="${record.tenderAccount}"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowPeriod }"></c:out></td>
													<td class="center"><c:out value="${record.tenderTime }"></c:out></td>
												</tr>
											</c:forEach>
											<tr>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"><c:out value="${userTenderDetailForm.tenderAccountTotal }"></c:out></td>
												<td class="center"></td>
												<td class="center"></td>
												<td class="center"></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="search" paginator="${userTenderDetailForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="primaryKey" id="primaryKey" value="${userTenderDetailForm.primaryKey}" /> 
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${userTenderDetailForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="userNameSrch" class="form-control input-sm underline"  maxlength="20" value="${userTenderDetailForm.userNameSrch}" />
			</div>
			<div class="form-group">
				<label>渠道</label> 
				<input type="text" name="channelNameSrch" class="form-control input-sm underline"  maxlength="20" value="${userTenderDetailForm.channelNameSrch}" />
			</div>
			<div class="form-group">
				<label>注册时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="regTimeStartSrch" id="regTimeStartSrch" class="form-control underline" value="${userTenderDetailForm.regTimeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="regTimeEndSrch" id="regTimeEndSrch" class="form-control underline" value="${userTenderDetailForm.regTimeEndSrch}" />
				</div>
			</div>
			<div class="form-group">
				<label>开户时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="openAccountTimeStartSrch" id="start-date-time" class="form-control underline" value="${userTenderDetailForm.openAccountTimeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="openAccountTimeEndSrch" id="end-date-time" class="form-control underline" value="${userTenderDetailForm.openAccountTimeEndSrch}" />
				</div>
			</div>
			<div class="form-group">
				<label>项目编号</label>
				<input type="text" name="borrowNidSrch" class="form-control input-sm underline"  maxlength="20" value="${userTenderDetailForm.borrowNidSrch}" />
			</div>
			<div class="form-group">
				<label>出借时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="tenderTimeStartSrch" id="start-date-time" class="form-control underline" value="${userTenderDetailForm.tenderTimeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="tenderTimeEndSrch" id="end-date-time" class="form-control underline" value="${userTenderDetailForm.tenderTimeEndSrch}" />
				</div>
			</div>
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
			<script type='text/javascript' src="${webRoot}/jsp/promotion/tenderdetail/tenderDetailList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
