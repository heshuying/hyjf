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

<shiro:hasPermission name="evaluationcheck:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="限额配置" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">测评配置</h1>
			<span class="mainDescription">本功能可以修改测评配置信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<shiro:hasPermission name="evaluationcheck:VIEW">
							<li><a href="${webRoot}/manager/config/evaluationcheck/init">开关</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="evaluationmoney:VIEW">
							<li class="active"><a href="${webRoot}/manager/config/evaluationmoney/init">限额配置</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="borrowLevelConfig:VIEW">
							<li><a href="${webRoot}/manager/config/borrowlevelconfig/init">信用等级配置</a></li>
						</shiro:hasPermission>
					</ul>
						<div class="tab-content">
							<div class="tab-pane fade in active">
									<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${evaluationmoneyForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${evaluationmoneyForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								</div>
								<br />
									<%-- 列表一览 --%>
								<table id="equiList" class="table table-striped table-bordered table-hover">
									<colgroup>
										<col style="width: 55px;" />
									</colgroup>
									<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">测评有效期</th>
										<th class="center">保守型限额</th>
										<th class="center">稳健型限额</th>
										<th class="center">成长型限额</th>
										<th class="center">进取型限额</th>
										<th class="center">保守型待收本金限额</th>
										<th class="center">稳健型待收本金限额</th>
										<th class="center">成长型待收本金限额</th>
										<th class="center">进取型待收本金限额</th>
										<th class="center">操作</th>
									</tr>
									</thead>
									<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty evaluationmoneyForm.recordList}">
											<tr>
												<td colspan="9">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${evaluationmoneyForm.recordList }" var="check" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((evaluationmoneyForm.paginatorPage - 1) * evaluationmoneyForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${check.validityEvaluationDate}"></c:out></td>
													<td class="center"><c:out value="${check.conservativeEvaluationSingleMoney}"></c:out></td>
													<td class="center"><c:out value="${check.steadyEvaluationSingleMoney}"></c:out></td>
													<td class="center"><c:out value="${check.growupEvaluationSingleMoney}"></c:out></td>
													<td class="center"><c:out value="${check.enterprisingEvaluationSinglMoney}"></c:out></td>
													<td class="center"><c:out value="${check.conservativeEvaluationPrincipalMoney}"></c:out></td>
													<td class="center"><c:out value="${check.steadyEvaluationPrincipalMoney}"></c:out></td>
													<td class="center"><c:out value="${check.growupEvaluationPrincipalMoney}"></c:out></td>
													<td class="center"><c:out value="${check.enterprisingEvaluationPrincipalMoney}"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="borrowflow:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-id="${check.id}"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="编辑"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="borrowflow:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
																	</shiro:hasPermission>
																</ul>
															</div>
														</div>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
									</tbody>
								</table>
									<%-- 分页栏 --%>
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${evaluationcheckForm.paginator}"></hyjf:paginator>
								<br /> <br />
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/evaluationconfig/evaluationmoney/evaluationmoneylist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
