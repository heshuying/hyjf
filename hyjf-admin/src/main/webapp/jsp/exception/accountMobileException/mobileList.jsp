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

	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="手机号修改" />
		
		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">手机号修改</h1>
			<span class="mainDescription">手机号修改。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<li class="active"><a href="javascript:void(0);">手机号修改</a></li>
							<li>
								<a href="${webRoot}/exception/accountmobilesynch/accountlist">银行卡修改</a></li>
					</ul>
				    <div class="tab-content">
					    <div class="tab-pane fade in active">
							<div class="row">
								<div class="col-md-12">
									<div class="search-classic">
											<%-- 功能栏 --%>
											<div class="well">
												<c:set var="jspPrevDsb" value="${mobilesynchronizeForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
												<c:set var="jspNextDsb" value="${mobilesynchronizeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
														data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
												   data-toggle="tooltip" data-placement="bottom"
												   data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
												<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
														data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
														data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
											</div>
										<br/>
						
										<%-- 角色列表一览 --%>
										<table id="equiList" class="table table-striped table-bordered table-hover">
											<colgroup>
												<col style="width:55px;" />
												<col />
												<col />
												<col />
												<col />
												<col />
												<col />
												<%-- <col style="width:;" /> --%>
											</colgroup>
											<thead>
												<tr>
													<th class="center">序号</th>
													<th class="center">用户名</th>
													<th class="center">姓名</th>
													<th class="center">电子账户</th>
													<th class="center">原手机号</th>
													<th class="center">新手机号</th>
													<th class="center">查询次数</th>
													<th class="center">添加日期</th>
													<th class="center">状态</th>
													<th class="center">操作</th>
												</tr>
											</thead>
											<tbody id="accountTbody">
											<c:choose>
												<c:when test="${empty mobilesynchronizeForm.recordList}">
													<tr>
														<td colspan="10">暂时没有数据记录</td>
													</tr>
												</c:when>
												<c:otherwise>
													<c:forEach items="${mobilesynchronizeForm.recordList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="center">
																<c:out value="${status.index+((mobilesynchronizeForm.paginatorPage - 1) * mobilesynchronizeForm.paginator.limit) + 1 }"></c:out></td>
															<td><c:out value="${record.username }"></c:out></td>
															<td><c:out value="${record.name }"></c:out></td>
															<td><c:out value="${record.accountid }"></c:out></td>
															<td><c:out value="${record.mobile }"></c:out></td>
															<td><c:out value="${record.newMobile }"></c:out></td>
															<td><c:out value="${record.searchtime }"></c:out></td>
															<%--<td><c:out value="${record.createTime }"></c:out></td>--%>
															<td><fmt:formatDate value="${record.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

															<c:if test="${record.status == '1' }">
																<td><c:out value="已同步"></c:out></td>
															</c:if>
															<c:if test="${record.status == '0' }">
																<td><c:out value="未同步"></c:out></td>
															</c:if>

															<td class="center">
																<div class="visible-md visible-lg hidden-sm hidden-xs">
																		<c:if test="${record.status == '0' }">
																			<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-id="${record.id }"
																			   data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
																		</c:if>


																</div>

															</td>
														</tr>
													</c:forEach>
												</c:otherwise>
											</c:choose>
											</tbody>
										</table>
											<%-- 分页栏 --%>
										<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchMobileAction"
														paginator="${mobilesynchronizeForm.paginator}"></hyjf:paginator>
										<br/><br/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="flag" id="flag" />
			<input type="hidden" name="paginatorPage" id="paginator-page"
				   value="${mobilesynchronizeForm.paginatorPage}" />
			<div class="form-group">
				<label>用户名:</label>
				<input type="text" name="username" id="username" class="form-control input-sm underline" value="${mobilesynchronizeForm.username}" />
			</div>
			<div class="form-group">
				<label>银行电子账号:</label>
				<input type="text" name="accountId" id="accountId" class="form-control input-sm underline" value="${mobilesynchronizeForm.accountId}" />
			</div>
			<div class="form-group">
				<label>原手机号:</label>
				<input type="text" name="mobile" id="mobile" class="form-control input-sm underline" value="${mobilesynchronizeForm.mobile}" />
			</div>
			<div class="form-group">
				<label>新手机号:</label>
				<input type="text" name="newMobile" id="newMobile" class="form-control input-sm underline" value="${mobilesynchronizeForm.newMobile}" />
			</div>
			<div class="form-group">
				<label>状态</label>
				<select name="status" id="status" class="form-control underline form-select2">

					<option value="">全部</option>
					<option value="0" <c:if test="${mobilesynchronizeForm.status eq '0'}">selected="selected"</c:if>>未同步</option>
					<option value="1" <c:if test="${mobilesynchronizeForm.status eq '1'}">selected="selected"</c:if>>已同步</option>
				</select>
			</div>

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
			<script type='text/javascript' src="${webRoot}/jsp/exception/accountMobileException/mobileList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
