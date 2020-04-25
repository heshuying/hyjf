<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="hjhstatis:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="每月交易量" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">每日交易量</h1>
			<span class="mainDescription">每日交易量</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<shiro:hasPermission name="hjhstatis:VIEW">
							<li><a href="${webRoot}/manager/statis/init">每日交易量</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="hjhstatis:VIEW">
							<li class="active"><a
								href="${webRoot}/manager/statis/init?time=month">每月交易量</a></li>
						</shiro:hasPermission>
					</ul>
					<div class="tab-content">
						<div class="tab-pane fade in active">
							<shiro:hasPermission name="hjhstatis:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb"
										value="${hjhstatisForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb"
										value="${hjhstatisForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a
										class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a
										class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>



									<shiro:hasPermission name="hjhstatis:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
											data-toggle="tooltip" data-placement="bottom"
											data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="检索条件" data-toggle-class="active"
										data-toggle-target="#searchable-panel"><i
										class="fa fa-search margin-right-10"></i><i
										class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br />
							<%-- 列表一览 --%>
							<table id="equiList"
								class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">日期</th>
										<th class="center">原始资产交易额(元)</th>
										<th class="center">债转资产交易额(元)</th>
										<th class="center">复出借金额(元)</th>
										<th class="center">新加入资金额(元)</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty HjhAccountBalanceForm}">
											<tr>
												<td colspan="17">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${HjhAccountBalanceForm.recordList}"
												var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(HjhAccountBalanceForm.paginatorPage - 1 ) * HjhAccountBalanceForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.dataFormt }"></c:out></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.investAccount }">
																<fmt:formatNumber type="number"
																	value="${record.investAccount }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.creditAccount }">
																<fmt:formatNumber type="number"
																	value="${record.creditAccount }" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.reinvestAccount}">
																<fmt:formatNumber type="number"
																	value="${record.reinvestAccount}" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.addAccount }">
																<fmt:formatNumber type="number"
																	value="${record.addAccount}" />
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>

									<th class="center">总计</th>

									<th>&nbsp;</th>
									<td align="center"><fmt:formatNumber
											value="${HjhAccountBalanceForm.sumObj.investAccount }"
											pattern="#,##0.00#" /></td>
									<td align="center"><fmt:formatNumber
											value="${HjhAccountBalanceForm.sumObj.creditAccount }"
											pattern="#,##0.00#" /></td>
									<td align="center"><fmt:formatNumber
											value="${HjhAccountBalanceForm.sumObj.reinvestAccount }"
											pattern="#,##0.00#" /></td>
									<td align="center"><fmt:formatNumber
											value="${HjhAccountBalanceForm.sumObj.addAccount }"
											pattern="#,##0.00#" /></td>

								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="hjhstatis:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page"
									action="searchAction"
									paginator="${HjhAccountBalanceForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="hjhstatis:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page"
					value="${hjhstatisForm.paginatorPage}" />
				<div class="form-group">
					<label>日期</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text"
							name="addTimeStart" id="addTimeStart"
							class="form-control underline"
							value="${hjhstatisForm.addTimeStart}" /> <i class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="addTimeEnd" id="addTimeEnd"
							class="form-control underline"
							value="${hjhstatisForm.addTimeEnd}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript'
				src="${webRoot}/jsp/manager/statis/hjhAccountBalanceMonth.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
