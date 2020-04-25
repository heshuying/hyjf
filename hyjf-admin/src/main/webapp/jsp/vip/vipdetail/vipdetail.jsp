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


<shiro:hasPermission name="vipdetail:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="VIP详情" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">VIP详情</h1>
		<span class="mainDescription">本功能可以查询VIP出借详细记录。</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="container-fluid container-fullw bg-white">
		<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<li class="active"><a href="javascript:void(0);">VIP详情</a></li>
				<shiro:hasPermission name="vipdetail:VIEW">
				<li><a href="${webRoot}/vip/vipupgrade/init?userId=${vipDetailForm.userId}">VIP成长</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${vipDetailForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${vipDetailForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a
							class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
					</div>
					<br />
					<%-- 返现列表一览 --%>
					<table id="returncashList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width: 55px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">用户名</th>
								<th class="center">VIP等级</th>
								<th class="center">订单号</th>
								<th class="center">交易金额</th>
								<th class="center">交易时间</th>
								<th class="center">交易V值</th>
								<th class="center">账户V值</th>
								<th class="center">备注</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty vipDetailForm.recordList}">
									<tr>
										<td colspan="10">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${vipDetailForm.recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((vipDetailForm.paginatorPage - 1) * vipDetailForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.userName }"></c:out></td>
											<td class="center"><c:out value="${record.vipName }"></c:out></td>
											<td class="center"><c:out value="${record.tenderNid }"></c:out></td>
											<td class="center"><c:out value="${record.account }"></c:out></td>
											<td class="center"><c:out value="${record.tradeTime}" /></td>
											<td class="center"><c:out value="${record.tenderVipValue}" /></td>
											<td class="center"><c:out value="${record.sumVipValue}"  /></td>
											<td class="center"><c:out value="${record.remark}" /></td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${vipDetailForm.paginator}"></hyjf:paginator>
					<br />
					<br />
				</div>
			</div>
		</div>
	</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<shiro:hasPermission name="userslist:SEARCH">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="userId" id="userId" value= "${vipDetailForm.userId}"/>
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${vipDetailForm.paginatorPage}" />
		</shiro:hasPermission>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/vip/vipdetail/vipdetail.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
