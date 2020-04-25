<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="批次交易明细查询" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<table id="equiList" class="table table-striped table-bordered table-hover">
					<colgroup>
						<col style="width:55px;" />
					</colgroup>
					<thead>
						<tr>
							<th class="center">序号</th>
							<th class="center">标的编号</th>
							<th class="center">借款人入账金额</th>
							<th class="center">手续费金额</th>
							<th class="center">风险准备金</th>
							<th class="center">借款人银行账户</th>
							<th class="center">借款人姓名</th>
							<th class="center">交易状态</th>
							<th class="center">响应代码</th>
							<th class="center">响应描述</th>
						</tr>
					</thead>
					<tbody id="roleTbody">
						<c:choose>
							<c:when test="${empty detailList}">
								<tr><td colspan="10">暂时没有数据记录</td></tr>
							</c:when>
							<c:otherwise>
									<tr>
										<td class="center">${(form.paginatorPage - 1 ) * form.paginator.limit + status.index + 1 }</td>
										<td class="center"><c:out value="${detailList.productId }"></c:out></td>
										<td class="center"><fmt:formatNumber pattern="#,##0.00#" value="${detailList.txAmount}" /></td>
										<td class="center"><fmt:formatNumber pattern="#,##0.00#" value="${detailList.feeAmount}" /></td>
										<td class="center"><fmt:formatNumber pattern="#,##0.00#" value="${detailList.riskAmount}" /></td>
										<td class="center"><c:out value="${detailList.forAccountId}"></c:out></td>
										<td align="center"><c:out value="${detailList.name }"></c:out></td>
										<td class="center"><c:out value="${detailList.txState }"></c:out></td>
										<td class="center"><c:out value="${detailList.retCode }"></c:out></td>
										<td class="center"><c:out value="${detailList.fileMsg }"></c:out></td>
									</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
				<%-- 分页栏 --%>
				<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="queryBatchDetailClkAction" paginator="${form.paginator}"></hyjf:paginator>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
		</div>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
	<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/batchcenter/borrowrecover/queryBatchDetails.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
