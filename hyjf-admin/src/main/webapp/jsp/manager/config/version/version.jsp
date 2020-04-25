<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="false" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="versionconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="版本控制" />
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">版本控制</h1>
			<span class="mainDescription">本功能可以增加修改删除版本配置信息。</span>
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
								<c:set var="jspPrevDsb" value="${versionconfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${versionconfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<shiro:hasPermission name="versionconfig:ADD">
								<a class="btn btn-o btn-primary btn-sm fn-Add" data-toggle="tooltip" data-placement="bottom" data-original-title="添加"> 添加 <i class="fa fa-plus"></i></a>
							</shiro:hasPermission>
							<shiro:hasPermission name="versionconfig:DELETE">
								<a class="btn btn-o btn-primary btn-sm fn-Deletes" data-toggle="tooltip" data-placement="bottom" data-original-title="删除">删除 <i class="fa fa-trash-o"></i> </a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="hidden-xs center">
											<div align="left" class="checkbox clip-check check-primary checkbox-inline"
													data-toggle="tooltip" tooltip-placement="top" data-original-title="选择所有行">
												<input type="checkbox" id="checkall">
												<label for="checkall"></label>
											</div>
										</th>
										<th class="center">序号</th>
										<th class="center">系统名称</th>
										<th class="center">版本号</th>
										<th class="center" style="width:20%;">更新地址</th>
										<th class="center" style="width:40%;">版本描述</th>
										<th class="center">是否更新</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty versionconfigForm.recordList}">
											<tr><td colspan="8">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${versionconfigForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="hidden-xs center">
														<div align="left" class="checkbox clip-check check-primary checkbox-inline">
															<input type="checkbox" class="listCheck" id="row${status.index }" value="${record.id }">
															<label for="row${status.index }"></label>
														</div>
													</td>
													<td class="center"><c:out value="${status.index+((versionconfigForm.paginatorPage - 1) * versionconfigForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center">
														<c:forEach items="${versionName }" var="ver" begin="0" step="1">
															<c:if test="${ver.nameCd eq record.type}"><c:out value="${ver.name }"></c:out></c:if>
														</c:forEach>
													</td>
													<td class="center"><c:out value="${record.version }"></c:out></td>
													<td class="left"><c:out value="${record.url }"></c:out></td>
													<td class="left"><c:out value="${record.content }"></c:out></td>
													<td class="center">
														<c:forEach items="${isUpdate }" var="isu" begin="0" step="1">
															<c:if test="${isu.nameCd eq record.isupdate}"><c:out value="${isu.name }"></c:out></c:if>
														</c:forEach>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="versionconfig:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="versionconfig:MODIFY">
																	<li>
																		<a class="fn-Modify" data-id="${record.id }">修改</a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${versionconfigForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${versionconfigForm.paginatorPage}" />
			<div class="form-group">
				<label>系统名称</label> 
				<select name="nameSrh" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${versionName }" var="cnt" begin="0" step="1">
						<option value="${cnt.nameCd }"
							<c:if test="${cnt.nameCd eq versionconfigForm.nameSrh}">selected="selected"</c:if>>
							<c:out value="${cnt.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>版本号</label> 
				<input type="text" name="versionSrh" class="form-control input-sm underline"  maxlength="20" value="${ versionconfigForm.versionSrh}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/version/version.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
