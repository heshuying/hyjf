<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
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
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="htjconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇添金配置" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇添金配置</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="htjconfig:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${htjconfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${htjconfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="htjconfig:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
												data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<shiro:hasPermission name="htjconfig:ADD">
									</shiro:hasPermission>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">产品名称</th>
										<th class="center">产品ID</th>
										<th class="center">编号前缀</th>
										<th class="center">最低授权服务金额</th>
										<th class="center">递增金额</th>
										<th class="center">最高授权服务金额</th>
										<th class="center">服务回报期限</th>
										<th class="center">退出所需天数</th>
										<th class="center">汇添金专属资产最后一笔界定金额</th>
										<th class="center">债转拆分笔数下限</th>
										<th class="center">债转拆分笔数上限</th>
										<th class="center">遍历次数</th>
										<th class="center">可用券</th>
										<th class="center">状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty htjconfigForm.recordList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${htjconfigForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(htjconfigForm.paginatorPage - 1 ) * htjconfigForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.debtPlanTypeName }"></c:out></td>
													<td class="center"><c:out value="${record.debtPlanType }"></c:out></td>
													<td class="center"><c:out value="${record.debtPlanPrefix }"></c:out></td>
													<!-- 最低加入金额 -->
													<td class="center">
														<fmt:formatNumber type="number" value="${record.debtMinInvestment }" /> 
													</td>
													<td class="center">
														<fmt:formatNumber type="number" value="${record.debtInvestmentIncrement }" /> 
													</td>
													<td class="center">
														<fmt:formatNumber type="number" value="${record.debtMaxInvestment }" /> 
													</td>
													<td class="center">
														<c:out value="${record.debtLockPeriod }"></c:out> 
													</td>
													<td class="center">
														<c:out value="${record.debtQuitPeriod }"></c:out> 
													</td>
													<!-- 汇添金专属资产最后一笔界定金额 -->
													<td class="center">
														<fmt:formatNumber type="number" value="${record.investAccountLimit }" /> 
													</td>
													<td class="center">
														<c:out value="${record.minInvestNumber }"></c:out>
													</td>
													<td class="center">
														<c:out value="${record.maxInvestNumber }"></c:out>
													</td>
													<td class="center">
														<c:out value="${record.cycleTimes }"></c:out>
													</td>
													<td class="center">
													<c:set value="${ fn:split(record.couponConfig, ',') }" var="coupName" />
														<c:forEach items="${coupName }" var="name">
															<c:if test="${name == 0}">
																【不可用】
															</c:if>
															<c:if test="${name == 1}">
																【体验金】
															</c:if>
															<c:if test="${name == 2}">
																【加息券】
															</c:if>
															<c:if test="${name == 3}">
																【代金券】
															</c:if>
														</c:forEach>
													</td>
													<td class="center">
														<c:if test="${record.status eq 0}">
															关闭
														</c:if>
														<c:if test="${record.status eq 1}">
															启用
														</c:if>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="htjconfig:MODIFY">
																<a class="btn btn-transparent btn-xs tooltips fn-Modify" data-ids="${record.id }" data-debtplantype="${record.debtPlanType }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="修改">修改</a>
															</shiro:hasPermission>
															<shiro:hasPermission name="htjconfig:DELETE">
																<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-ids="${record.id }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="htjconfig:MODIFY">
																		<li>
																			<a class="fn-Modify" data-ids="${record.id }" data-debtplantype="${record.debtPlanType }">修改</a>
																		</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="htjconfig:DELETE">
																		<li>
																			<a class="fn-Delete" data-ids="${record.id }">删除</a>
																		</li>
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
							<shiro:hasPermission name="htjconfig:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${htjconfigForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${htjconfigForm.paginatorPage}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/htjconfig/htjconfigList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
