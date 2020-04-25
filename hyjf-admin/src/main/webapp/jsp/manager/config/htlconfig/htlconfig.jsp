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

<shiro:hasPermission name="htlconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇天利设置" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇天利设置</h1>
			<span class="mainDescription">本功能可以增加修改删除汇天利。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${htlconfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${htlconfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
 								<shiro:hasPermission name="htlconfig:MODIFY"> 
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-AddUpdate"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加/修改汇天利是一个操作">添加<i class="fa fa-plus"></i></a>
 								</shiro:hasPermission> 
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
							</div>
							<br />
							<%-- 汇天利列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">名称</th>
										<th class="center">数值</th>
										<th class="center"></th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty htlconfigForm.recordList}">
											<tr>
												<td colspan="3">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${htlconfigForm.recordList }" var="record"
												begin="0" step="1" varStatus="status">
									<tr>
										<td class="center">1</td>
										<td>出借利率</td>
										<td align="right">${record.interestRate }</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td class="center">2</td>
										<td>产品总额</td>
										<td align="right"><fmt:formatNumber value="${record.allpupper }" pattern="#,##0.00#" /></td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td class="center">3</td>
										<td>单户上限</td>
										<td align="right"><fmt:formatNumber value="${record.pupper }" pattern="#,##0.00#" /></td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td class="center">4</td>
										<td>单户下限</td>
										<td align="right"><fmt:formatNumber value="${record.plower }" pattern="#,##0.00#" /></td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td class="center">5</td>
										<td>可赎回</td>
										<td align="right"><fmt:formatNumber value="${record.isRedeem }" pattern="#,##0.00#" /></td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td class="center">6</td>
										<td>可转让</td>
										<td align="right"><fmt:formatNumber value="${record.isTender }" pattern="#,##0.00#" /></td>
										<td>&nbsp;</td>
									</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="init" paginator="${htlconfigForm.paginator}"></hyjf:paginator>
							<br />
							<br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${htlconfigForm.paginatorPage}" />
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/htlconfig/htlconfig.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
