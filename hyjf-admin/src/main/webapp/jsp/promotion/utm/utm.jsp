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


<shiro:hasPermission name="utm:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="推广管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">推广管理</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="utm:SEARCH">
							<div class="well">
									<c:set var="jspPrevDsb" value="${borrowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="utm:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
								</shiro:hasPermission>

									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							</shiro:hasPermission>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">渠道编号</th>
										<th class="center">渠道类型</th>
										<th class="center">渠道</th>
										<th class="center">状态</th>
										<th class="center">是否可以发起债转</th>
										<th class="center">备注</th>
										<th class="center">添加时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="8">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((utmForm.paginatorPage - 1) * utmForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.sourceId }"></c:out></td>
													<td><c:out value="${record.sourceType == '0' ? 'PC渠道' : 'APP渠道' }"></c:out></td>
													<td><c:out value="${record.sourceName }"></c:out></td>
													<td class="center"><c:out
															value="${record.delFlag == '0' ? '启用' : '禁用'  }"></c:out></td>
													<td class="center"><c:out
															value="${record.attornFlag == '0' ? '否' : '是'  }"></c:out></td>
													<td><c:out value="${record.remark }"></c:out></td>
													<td class="center"><c:out value="${record.createTime }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="utm:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="utm:DELETE">
																<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button"
																	class="btn btn-primary btn-o btn-sm"
																	data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="utm:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="utm:DELETE">
																		<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>
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
								action="init" paginator="${utmForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" /> 
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${utmForm.paginatorPage}" />

			<div class="form-group">
				<label>渠道编号</label>
				<input name="sourceId" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'') " ng-pattern="/[^a-zA-Z]/" value="${utmForm.sourceId}"/>
			</div>

			<div class="form-group">
					<label>渠道类型</label>
					<select name="sourceTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${utmForm.sourceTypeSrch=='0'}">selected</c:if>>PC渠道</option>
						<option value="1" <c:if test="${utmForm.sourceTypeSrch=='1'}">selected</c:if>>APP渠道</option>
					</select>
			</div>

			<div class="form-group">
				<label>渠道</label>
				<select name="sourceIdSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${utmPlatList }" var="utm" begin="0" step="1">
						<option value="${utm.sourceId }" <c:if test="${utm.sourceId eq utmForm.sourceIdSrch}">selected="selected"</c:if> >
							<c:out value="${utm.sourceName }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>

			<div class="form-group">
				<label>是否可以发起债转</label>
				<select name="attornFlag" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1" <c:if test="${utmForm.attornFlag=='1'}">selected</c:if>>是</option>
					<option value="0" <c:if test="${utmForm.attornFlag=='0'}">selected</c:if>>否</option>
				</select>
			</div>


			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${utmForm.timeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${utmForm.timeEndSrch}" />
				</div>
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
			<script type='text/javascript' src="${webRoot}/jsp/promotion/utm/utm.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
