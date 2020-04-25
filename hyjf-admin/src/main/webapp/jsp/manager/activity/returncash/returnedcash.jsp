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


<shiro:hasPermission name="returnedcashlist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="返现管理" />

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">返现管理</h1>
			<span class="mainDescription">本功能可以查询返现明细和执行返现操作。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab">
					<shiro:hasPermission name="returncashlist:VIEW">
			      	<li><a href="${webRoot}/activity/returncash/returncash">未返现列表</a></li>
			      	</shiro:hasPermission>
			      	<li class="active"><a href="javascript:void(0);">已返现列表</a></li>
			    </ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb" value="${returncashForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb" value="${returncashForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
							<shiro:hasPermission name="returnedcashlist:EXPORT">
							<a class="btn btn-o btn-primary btn-sm fn-Export"
								data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i> </a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br/>
						<%-- 返现列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width:55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">用户名</th>
									<th class="center">真实姓名</th>
									<th class="center">手机号</th>
									<th class="center">推荐人</th>
									<!-- <th class="center">用户属性</th> -->
									<th class="center">活动期内累计出借额</th>
									<th class="center">是否有流失用户奖励</th>
									<th class="center">转账订单号</th>
									<th class="center">返现金额</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty returncashForm.recordList}">
										<tr><td colspan="11">暂时没有数据记录</td></tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${returncashForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((returncashForm.paginatorPage - 1) * returncashForm.paginator.limit) + 1 }"></c:out></td>
												<td><c:out value="${record.username }"></c:out></td>
												<td class="center"><c:out value="${record.truename }"></c:out></td>
												<td class="center"><c:out value="${record.mobile }"></c:out></td>
												<td><c:out value="${record.referrerUserName }"></c:out></td>
												<%-- <td class="text-right"><c:out value="${record.attribute}"/></td> --%>
												<td class="text-center"><fmt:formatNumber value="${record.investTotalActivity}" type="number" pattern="#" /></td>
												<td class="text-center"><c:out value="${record.hasLostreward==1?'是':'否' }"></c:out></td>
												<td><c:out value="${record.orderId }"></c:out></td>
												<td><fmt:formatNumber value="${record.rewardTotal}" type="number" pattern="#" /></td>
												<td><c:out value=""></c:out></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="returnedcash" paginator="${returncashForm.paginator}"></hyjf:paginator>
						<br/><br/>
					</div>
				</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${returncashForm.paginatorPage}" />
			<!-- 查询条件 -->

			<div class="form-group">
				<label>用户名</label>
				<input type="text" name="usernameSrch" class="form-control input-sm underline" value="${returncashForm.usernameSrch}" />
			</div>
			<div class="form-group">
				<label>用户属性</label>
				<select name="attributeSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${userPropertys }" var="property" begin="0" step="1" >
						<option value="${property.nameCd }"
							<c:if test="${property.nameCd eq returncashForm.attributeSrch}">selected="selected"</c:if>>
							<c:out value="${property.name }"></c:out></option>
					</c:forEach>
				</select>
			</div>
			
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/returncash/returnedcash.js"></script>
		</tiles:putAttribute>

	</tiles:insertTemplate>
</shiro:hasPermission>
