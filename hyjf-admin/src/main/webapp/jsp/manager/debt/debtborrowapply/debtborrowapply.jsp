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
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="debtborrowapply:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款申请" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">借款申请</h1>
			<span class="mainDescription">借款申请的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="debtborrowapply:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${debtborrowApplyForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${debtborrowApplyForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">姓名</th>
										<th class="center">性别</th>
										<th class="center">手机号</th>
										<th class="center">所在地</th>
										<th class="center">融资金额（万元）</th>
										<th class="center">借款期限</th>
										<th class="center">审核状态</th>
										<th class="center">申请时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(debtborrowApplyForm.paginatorPage - 1 ) * debtborrowApplyForm.paginator.limit + status.index + 1 }</td>
													<td><c:out value="${record.name }"></c:out></td>
													<td class="center"><c:out value="${record.sex }"></c:out></td>
													<td class="right"><c:out value="${record.tel }"></c:out></td>
													<td><c:out value="${record.address }"></c:out></td>
													<td align="right"><c:out value="${record.money }"></c:out></td>
													<td align="left"><c:out value="${record.day }"></c:out></td>
													<td class="center"><c:out value="${record.stateName }"></c:out></td>
													<td class="center"><c:out value="${record.addtime }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="debtborrowapply:AUDIT">
																<c:if test="${record.state eq '0'}">
																	<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																			data-toggle="tooltip" tooltip-placement="top" data-original-title="审核">审核</a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="debtborrowapply:INFO">
																<c:if test="${record.state eq '1' or record.state eq '2'}">
																	<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																			data-toggle="tooltip" tooltip-placement="top" data-original-title="详细">详细</a>
																</c:if>
															</shiro:hasPermission>
															</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="debtborrowapply:AUDIT">
																		<li>
																			<a class="fn-Modify" data-id="${record.id }">审核</a>
																		</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="debtborrowapply:INFO">
																		<li>
																			<a class="fn-Modify" data-id="${record.id }">详细</a>
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
							<shiro:hasPermission name="debtborrowapply:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${debtborrowApplyForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="debtborrowapply:SEARCH">
				<input type="hidden" name="id" id="id" value="" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${debtborrowApplyForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>姓名</label>
					<input type="text" name="nameSrch" id="nameSrch" class="form-control input-sm underline" value="${debtborrowApplyForm.nameSrch}" />
				</div>
				<div class="form-group">
					<label>审核状态</label>
					<select name="stateSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${applyStatusList }" var="record" begin="0" step="1" varStatus="status">
							<option value="${record.nameCd }"
								<c:if test="${record.nameCd eq debtborrowApplyForm.stateSrch}">selected="selected"</c:if>>
								<c:out value="${record.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>申请时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${debtborrowApplyForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${debtborrowApplyForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			
				
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/debt/debtborrowapply/debtborrowapply.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
