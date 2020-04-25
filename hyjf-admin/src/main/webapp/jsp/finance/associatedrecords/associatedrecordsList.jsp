<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="associatedrecords:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="关联记录" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">关联记录</h1>
		<span class="mainDescription">关联记录</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb" value="${associatedrecordsForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb" value="${associatedrecordsForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
							<shiro:hasPermission name="associatedrecords:EXPORT">
								<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel<i class="fa fa-Export"></i></a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 角色列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">转出账户</th>
									<th class="center">转出账户手机</th>
									<th class="center">转出账户客户号</th>
									<th class="center">转入账户</th>
									<th class="center">转入账户手机</th>
									<th class="center">转入账户客户号</th>
									<th class="center">关联状态</th>
									<th class="center">关联时间</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty associatedrecordsForm.associatedRecordsList}">
										<tr>
											<td colspan="12">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${associatedrecordsForm.associatedRecordsList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((associatedrecordsForm.paginatorPage - 1) * associatedrecordsForm.paginator.limit) + 1 }"></c:out></td>
												<td class="center"><c:out value="${record.turnOutUsername }"></c:out></td>
												<td class="center"><c:out value="${record.turnOutMobile }"></c:out></td>
												<td class="center"><c:out value="${record.turnOutChinapnrUsrcustid }"></c:out></td>
												<td class="center"><c:out value="${record.shiftToUsername }"></c:out></td>
												<td class="center"><c:out value="${record.shiftToMobile }"></c:out></td>
												<td class="center"><c:out value="${record.shiftToChinapnrUsrcustid }"></c:out></td>
												<td class="center">
													<c:if test="${record.associatedState == 0}"> 未授权 </c:if>
													<c:if test="${record.associatedState == 1}"> 成功 </c:if>
													<c:if test="${record.associatedState == 2}"> 失败 </c:if>
												</td>
												<td class="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${record.associatedTime}" type="both"/></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="getAssociatedrecordsList" paginator="${associatedrecordsForm.paginator}"></hyjf:paginator>
						<br /> <br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${associatedrecordsForm.paginatorPage}" />
		<!-- 查询条件 -->
		<div class="form-group">
			<label>转出账户</label>
			<input type="text" name="turnOutUsername" id="turnOutUsername" class="form-control input-sm underline" value="${associatedrecordsForm.turnOutUsername}" />
		</div>
		<div class="form-group">
			<label>转出账户手机</label>
			<input type="text" name="turnOutMobile" id="turnOutMobile" class="form-control input-sm underline" value="${associatedrecordsForm.turnOutMobile}" />
		</div>
		<div class="form-group">
			<label>关联状态</label>
			<select name="statusSearch" class="form-control underline form-select2">
				<option value="">全部</option>
				<option value="0" <c:if test="${'0' eq associatedrecordsForm.statusSearch}">selected="selected"</c:if>>
					<c:out value="未授权"></c:out>
				</option>
				<option value="1" <c:if test="${'1' eq associatedrecordsForm.statusSearch}">selected="selected"</c:if>>
					<c:out value="成功"></c:out>
				</option>
				<option value="2" <c:if test="${'2' eq associatedrecordsForm.statusSearch}">selected="selected"</c:if>>
					<c:out value="失败"></c:out>
				</option>
			</select>
		</div>
		<div class="form-group">
			<label>转入账户</label>
			<input type="text" name="shiftToUsername" id="shiftToUsername" class="form-control input-sm underline" value="${associatedrecordsForm.shiftToUsername}" />
		</div>
	
		<div class="form-group">
			<label>转入账户手机</label>
			<input type="text" name="shiftToMobile" id="shiftToMobile" class="form-control input-sm underline" value="${associatedrecordsForm.shiftToMobile}" />
		</div>
		<div class="form-group">
			<label>关联时间</label>
			<div class="input-group input-daterange datepicker">
				<span class="input-icon"><input type="text" name="startDate" id="start-date-time" class="form-control underline" value="${associatedrecordsForm.startDate}" /> <i class="ti-calendar"></i></span>
				<span class="input-group-addon no-border bg-light-orange">~</span><input type="text" name="endDate" id="end-date-time" class="form-control underline" value="${associatedrecordsForm.endDate}" />
			</div>
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/finance/associatedrecords/associatedrecordsList.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
