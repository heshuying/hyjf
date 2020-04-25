<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,还款异常', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="repayexception:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款异常" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.table-striped .checkbox {
				width: 20px;
				margin-right: 0 !important;
				overflow: hidden
			}
			</style>
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">融通宝还款异常</h1>
			<span class="mainDescription">查询还款失败的还款信息。</span>
		</tiles:putAttribute>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<hyjf:message key="delete_error"></hyjf:message>
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${repayexceptionForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${repayexceptionForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
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
										<th class="center">项目编号</th>
										<th class="center">产品加息收益率</th>
										<th class="center">产品加息收益</th>
										<th class="center">还款方式</th>
										<th class="center">转账状态</th>
										<th class="center">应还时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty repayexceptionForm.recordList}">
											<tr>
												<td colspan="8">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${repayexceptionForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((repayexceptionForm.paginatorPage - 1) * repayexceptionForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td><c:out value="${record.borrowExtraYield }"></c:out>%</td>
													<td><c:out value="${record.repayInterest }"></c:out></td>
													<td><c:out value="${record.borrowStyleName }"></c:out></td>
												
													<td class="center">
													<c:if test="${record.repayStatus==9 }">
														<span class="text-red">还款失败
														<shiro:hasPermission name="repayexception:INFO">
														&nbsp;<i class="fa fa-red fa-info-circle error-info" data-trigger="click" data-toggle="popover" data-html="true" data-placement="bottom" data-title="详细信息"  data-content="<c:out value="${record.data == null ? '还款失败' : record.data }"></c:out>"></i>
														</shiro:hasPermission>
														</span>
													</c:if>
													</td>
													<td class="center">
														<hyjf:datetime value="${record.repayTime}"></hyjf:datetime>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="repayexception:CONFIRM">
																<c:if test="${record.repayStatus==9 }">
																	<a class="btn btn-transparent btn-xs fn-restartRepay"  data-borrownid="${record.borrowNid }" data-id="${record.id }" data-toggle="tooltip" data-placement="top" data-original-title="重新还款">重新还款</a>
																</c:if>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<li>
																		<shiro:hasPermission name="repayexception:CONFIRM">
																		<c:if test="${record.repayStatus==9 }">
																			<a class="btn btn-transparent btn-xs fn-restartRepay" data-borrownid="${record.borrowNid }"  data-id="${record.id }" data-toggle="tooltip" data-placement="top" data-original-title="重新还款">重新还款</a>
																		</c:if>
																		</shiro:hasPermission>
																	</li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${repayexceptionForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${repayexceptionForm.paginatorPage}" />
			<input type="hidden" name="borrowNid" id="borrowNid" value="${repayexceptionForm.borrowNid}" />
			<input type="hidden" name="id" id="id" value="${repayexceptionForm.id}" />
			<div class="form-group">
				<label>项目编号</label>
				<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${repayexceptionForm.borrowNidSrch}" />
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.popover {
				max-width: 600px;
			}
			.error-info {
				cursor:pointer;
			}
			</style>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/exception/increaseinterestexception/increaseinterestexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
