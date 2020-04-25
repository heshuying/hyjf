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

<shiro:hasPermission name="accedelist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="出借明细" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">出借明细</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<div class="tab-content">
				<div class="tab-pane fade in active">					
					<br />
					<%-- 列表一览 --%>
					<table id="equiList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width: 55px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">用户名</th>
								<!-- <th class="center">用户属性</th> -->
								<th class="center">推荐人</th>
								<th class="center">项目编号</th>
								<th class="center">智投编号</th>
								<th class="center">出借利率</th>
								<th class="center">借款期限</th>
								<th class="center">还款方式</th>
								<th class="center">出借金额(元)</th>
								<th class="center">资产来源</th>
								<th class="center">产品类型</th>
								<th class="center">复审通过时间</th>
								<th class="center">出借时间</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty recordList}">
									<tr>
										<td colspan="15">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((tenderForm.paginatorPage - 1) * tenderForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.username }"></c:out></td>
											<%-- <td class="center"><c:out value="${record.tenderUserAttribute }"></c:out></td> --%>
											<td class="center"><c:out value="${record.referrerName }"></c:out></td>
											<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
											<td class="center"><c:out value="${record.planNid }"></c:out></td>
											<td class="center"><c:out value="${record.borrowApr }"></c:out></td>
											<td class="center"><c:out value="${record.borrowPeriod }"></c:out></td>
											<td class="center"><c:out value="${record.borrowStyleName }"></c:out></td>
											<td class="center"><c:out value="${record.account }"></c:out></td>
											<td class="center"><c:out value="${record.instName }"></c:out></td>
											<td class="center"><c:out value="${record.productType }"></c:out></td>
											<td class="center"><c:out value="${record.reAuthPassTime }"></c:out></td>
											<td class="center">
												<c:out value="${record.addtime }"></c:out>
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${tenderForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="accedelist:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${tenderForm.paginatorPage}" />
				<!-- 查询条件 -->
			</shiro:hasPermission>
		</tiles:putAttribute>
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/autotenderexception/tenderdetailList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
