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
			<h1 class="mainTitle">问题分类</h1>
			<span class="mainDescription">本功能可以对问题分类进行操作。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab">
					<li class="active"><a href="javascript:void(0);">问题分类列表</a></li>
					<shiro:hasPermission name="help:VIEW">
						<li><a href="${webRoot}/manager/content/help/helpInit">问题列表</a></li>
					</shiro:hasPermission>
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
							<c:set var="jspPrevDsb" value="${listForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb" value="${listForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>

							<shiro:hasPermission name="help:ADD">
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-AddType"
									data-toggle="tooltip" data-placement="bottom" 
									data-original-title="添加新分类"><i class="fa fa-plus"></i>
									添加分类</a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-AddSubType"
									data-toggle="tooltip" data-placement="bottom" 
									data-original-title="添加子分类"><i class="fa fa-plus"></i>
									添加子分类</a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 问题分类列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">问题分类</th>
									<th class="center">问题子分类</th>
									<th class="center">问题数量</th>
									<th class="center">排序</th>
									<th class="center">状态</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty listForm.recordList}">
										<tr>
											<td colspan="7">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${listForm.recordList }" var="record"
											begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((listForm.paginatorPage - 1) * listForm.paginator.limit) + 1 }"></c:out></td>
												<td><c:if test="${record.level==0 }">${record.title }</c:if>
													<c:if test="${record.level==1 }">
														<c:forEach items="${parentCategorys }"
															var="categorysRecord" begin="0" step="1"
															varStatus="categorysStatus">
															<c:if test="${record.pid==categorysRecord.id }">
																<c:out value="${categorysRecord.title }"></c:out>
															</c:if>
														</c:forEach>
													</c:if></td>
												<td><c:if test="${record.level==0 }">&nbsp;</c:if> <c:if test="${record.level==1 }">${record.title }</c:if></td>
												<td align="right"><c:out value="${record.tip}"></c:out></td>
												<td class="center"><c:out value="${record.sort}"></c:out></td>
												<td class="center"><c:out value="${record.hide ==0?'启用':'关闭'}"></c:out></td>
												<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<shiro:hasPermission name="help:MODIFY">
															<c:if test="${record.level==0 }">
																<a class="btn btn-transparent btn-xs fn-ModifyType"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="修改"><i
																	class="fa fa-pencil"></i></a>
															</c:if>
															<c:if test="${record.level==1 }">
																<a
																	class="btn btn-transparent btn-xs fn-ModifySubType"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="修改"><i
																	class="fa fa-pencil"></i></a>
															</c:if>
														</shiro:hasPermission>
														<shiro:hasPermission name="help:MODIFY">
															<c:if test="${record.hide==1}">
																<a
																	class="btn btn-transparent btn-xs tooltips fn-Open"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="开启">
																	<i class="fa fa-lightbulb-o"> </i>
																</a>
															</c:if>
															<c:if test="${record.hide==0}">
																<a
																	class="btn btn-transparent btn-xs tooltips fn-Close"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="关闭">
																	<i class="fa fa-lightbulb-o"> </i>
																</a>
															</c:if>

														</shiro:hasPermission>
														<shiro:hasPermission name="help:DELETE">
															<a
																class="btn btn-transparent btn-xs tooltips fn-Delete"
																data-id="${record.id }" data-toggle="tooltip"
																tooltip-placement="top" data-original-title="删除"><i
																class="fa fa-times fa fa-white"></i></a>
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
																	<c:if test="${record.level==0 }">
																		<li><a class="fn-ModifyType"
																			data-id="${record.id }">修改</a></li>
																	</c:if>
																	<c:if test="${record.level==1 }">
																		<li><a class="fn-ModifySubType"
																			data-id="${record.id }">修改</a></li>
																	</c:if>
																</shiro:hasPermission>
																<shiro:hasPermission name="help:MODIFY">
																	<li><c:if test="${record.hide==1}">
																			<a class="fn-Close" data-id="${record.id }">开启</a>
																		</c:if> <c:if test="${record.hide==0}">
																			<a class="fn-Open" data-id="${record.id }">关闭</a>
																		</c:if></li>
																</shiro:hasPermission>
																<shiro:hasPermission name="help:DELETE">
																	<li><a class="fn-Delete"
																		data-id="${record.id }">删除</a></li>
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
						<hyjf:paginator id="equiPaginator" hidden="paginator-page"
							action="init" paginator="${listForm.paginator}"></hyjf:paginator>
						<br/><br/>
					</div>
				</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${listForm.paginatorPage}" />
			<input type="hidden" name="ids" id="ids" value="${listForm.ids}" />
			<div class="form-group">
				<label>问题分类</label> <select name="pid" id="pid"
					class="form-control underline form-select2">
					<option value="">--全部--</option>
					<c:forEach items="${parentCategorys }" var="record" begin="0"
						step="1" varStatus="status">
						<option value="${record.id }"
							<c:if test="${record.id==listForm.pid}">selected="selected"</c:if>>${record.title }</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>问题子分类</label> <select name="id" id="id"
					class="form-control underline form-select2">
					<option value="">--全部--</option>
					<c:forEach items="${childCategorys }" var="record" begin="0"
						step="1" varStatus="status">
						<option value="${record.id }"
							<c:if test="${record.id==listForm.id}">selected="selected"</c:if>>${record.title }</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>状态</label> <select name="hide" id="hide"
					class="form-control underline form-select2">
					<option value="">--全部--</option>
					<option value="0"
						<c:if test="${listForm.hide ==0}">selected="selected"</c:if>>关闭</option>
					<option value="1"
						<c:if test="${listForm.hide ==1}">selected="selected"</c:if>>启用</option>
				</select>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/content/help/category.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
