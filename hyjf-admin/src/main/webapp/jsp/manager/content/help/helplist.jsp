<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="help:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="问题分类管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">问题列表</h1>
			<span class="mainDescription">本功能可以对问题进行操作。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab">
					<shiro:hasPermission name="help:VIEW">
						<li><a href="${webRoot}/manager/content/help/init">问题分类列表</a></li>
					</shiro:hasPermission>
					<li class="active"><a href="javascript:void(0);">问题列表</a></li>
					<shiro:hasPermission name="help:VIEW">
						<li><a href="${webRoot}/manager/content/help/oftenInit">常见问题</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="help:VIEW">
						<li><a href="${webRoot}/manager/content/help/zhiChiInit">智齿客服常见问题</a></li>
					</shiro:hasPermission>
				</ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb" value="${helpForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb" value="${helpForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<shiro:hasPermission name="help:ADD">
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										data-toggle="tooltip" data-placement="bottom" data-original-title="添加新问题">添加 <i class="fa fa-plus"></i></a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 返现列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">问题分类</th>
									<th class="center">问题子分类</th>
									<th class="center">问题名称</th>
									<th class="center">排序</th>
									<th class="center">状态</th>
									<th class="center">添加时间</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty listHelp}">
										<tr>
											<td colspan="8">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${listHelp}" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out
														value="${status.index+((helpForm.paginatorPage - 1) * helpForm.paginator.limit) + 1 }"></c:out></td>
												<td><c:forEach
														items="${catelist }" var="categorysRecord" begin="0"
														step="1" varStatus="categorysStatus">
														<c:if test="${record.pcateId==categorysRecord.id }">
															<c:out value="${categorysRecord.title }"></c:out>
														</c:if>
													</c:forEach></td>
												<td><c:forEach
														items="${catelist }" var="categorysRecord" begin="0"
														step="1" varStatus="categorysStatus">
														<c:if test="${record.cateId==categorysRecord.id }">
															<c:out value="${categorysRecord.title }"></c:out>
														</c:if>
													</c:forEach></td>
												<td><c:out
														value="${record.title }"></c:out></td>
												<td class="center"><c:out
														value="${record.order}"></c:out></td>
												<td class="center"><c:if
														test="${record.status==0 }">关闭</c:if> <c:if
														test="${record.status==1 }">启用</c:if> <c:if
														test="${record.status==2 }">启用</c:if></td>
												<td class="center"><c:out
														value="${record.add_time}"></c:out></td>
												<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<shiro:hasPermission
															name="help:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify"
																data-id="${record.id }" data-toggle="tooltip"
																tooltip-placement="top" data-original-title="修改"><i
																class="fa fa-pencil"></i></a>
														</shiro:hasPermission> <shiro:hasPermission name="help:MODIFY">
															<c:if test="${record.status==0}">
																<a class="btn btn-transparent btn-xs tooltips fn-Open"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="开启">
																	<i class="fa fa-lightbulb-o"> </i>
																</a>
															</c:if>
															<c:if test="${record.status==1}">
																<a
																	class="btn btn-transparent btn-xs tooltips fn-Close"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="关闭">
																	<i class="fa fa-lightbulb-o"> </i>
																</a>
															</c:if>
														</shiro:hasPermission> <shiro:hasPermission name="help:DELETE">
															<a
																class="btn btn-transparent btn-xs tooltips fn-Delete"
																data-id="${record.id }" data-toggle="tooltip"
																tooltip-placement="top" data-original-title="删除"><i
																class="fa fa-times fa fa-white"></i></a>
														</shiro:hasPermission> <shiro:hasPermission name="help:MODIFY">
															<c:if test="${record.status==1}">
																<a
																	class="btn btn-transparent btn-xs tooltips fn-Often"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="添加到常见问题">添加到常见问题</a>
															</c:if>
															<c:if test="${record.status==1&&record.zhiChiStatus!=1&&record.isZhiChi=='true'}">
															<a
																	class="btn btn-transparent btn-xs tooltips fn-zhichi"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="添加到智齿客服常见问题">添加到智齿客服常见问题</a>
															</c:if>
														</shiro:hasPermission>
													</div>
													<div class="visible-xs visible-sm hidden-md hidden-lg">
														<div class="btn-group" dropdown="">
															<button type="button"
																class="btn btn-primary btn-o btn-sm"
																data-toggle="dropdown">
																<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
															</button>
															<ul class="dropdown-menu pull-right dropdown-light"
																role="menu">
																<shiro:hasPermission name="help:MODIFY">
																	<li><a class="fn-Modify"
																		data-id="${record.id }">修改</a></li>
																</shiro:hasPermission>
																<shiro:hasPermission name="help:MODIFY">
																	<li><c:if test="${record.status==1}">
																			<a class="fn-Close" data-id="${record.id }">关闭</a>
																		</c:if> <c:if test="${record.status==0}">
																			<a class="fn-Open" data-id="${record.id }">启用</a>
																		</c:if></li>
																</shiro:hasPermission>
																<shiro:hasPermission name="help:DELETE">
																	<li><a class="fn-Delete"
																		data-id="${record.id }">删除</a></li>
																</shiro:hasPermission>
																<shiro:hasPermission name="help:MODIFY">
																	<li><a class="fn-Often" data-id="${record.id }">添加到常见问题</a>
																	</li>
																</shiro:hasPermission>
															</ul>
														</div>
													</div></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page"
							action="helpInit" paginator="${helpForm.paginator}"></hyjf:paginator>
						<br /> <br />
					</div>
				</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${helpForm.paginatorPage}" />
			<div class="form-group">
				<label>问题分类</label> <select name="pcateId" id="pcateId"
					class="form-control underline form-select2">
					<option value="">--全部--</option>
					<c:forEach items="${parentCategorys }" var="record" begin="0"
						step="1" varStatus="status">
						<option value="${record.id }"
							<c:if test="${record.id==helpForm.pcateId}">selected="selected"</c:if>>${record.title }</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>问题子分类</label> <select name="cateId" id="cateId"
					class="form-control underline form-select2">
					<option value="">--全部--</option>
					<c:forEach items="${childCategorys }" var="record" begin="0"
						step="1" varStatus="status">
						<option value="${record.id }"
							<c:if test="${record.id==helpForm.cateId}">selected="selected"</c:if>>${record.title }</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>问题名称</label> <input type="text" name="title"
					class="form-control input-sm underline" value="${helpForm.title}" />
			</div>

			<div class="form-group">
				<label>状态</label> <select name="status" id="status"
					class="form-control underline form-select2">
					<option value="">--全部--</option>
					<option value="0"
						<c:if test="${helpForm.status ==0}">selected="selected"</c:if>>关闭</option>
					<option value="1"
						<c:if test="${helpForm.status ==1}">selected="selected"</c:if>>启用</option>
				</select>
			</div>
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker" width="10"
					height="10">
					<span class="input-icon"> <input type="text"
						name="post_time_begin" id="post_time_begin"
						class="form-control underline" value="${helpForm.post_time_begin}" />
						<i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="post_time_end" id="post_time_end"
						class="form-control underline" value="${helpForm.post_time_end}" />
				</div>
			</div>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/content/help/helplist.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
