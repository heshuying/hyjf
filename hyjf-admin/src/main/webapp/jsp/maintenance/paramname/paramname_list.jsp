<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,权限管理', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="paramname:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="数据字典" />

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="paramname:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
												data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">字典区分</th>
										<th class="center">字典编号</th>
										<th class="center">字典名称</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="5">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((formInfo.paginatorPage - 1) * paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.nameClass }"></c:out></td>
													<td><c:out value="${record.nameCd }"></c:out></td>
													<td class="hidden-xs"><c:out value="${record.name }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="paramname:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify" data-nameclass="${record.nameClass }" data-namecd="${record.nameCd }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="paramname:DELETE">
															<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-nameclass="${record.nameClass }" data-namecd="${record.nameCd }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="paramname:MODIFY">
																	<li>
																		<a class="fn-Modify" data-nameclass="${record.nameClass }" data-namecd="${record.nameCd }">修改</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="paramname:DELETE">
																	<li>
																		<a class="fn-Delete" data-nameclass="${record.nameClass }" data-namecd="${record.nameCd }">删除</a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="nameClass" id="nameClass" />
			<input type="hidden" name="nameCd" id="nameCd" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${formInfo.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>字典区分</label>
				<input type="text" name="nameClassSrch" id="nameClassSrch" class="form-control input-sm underline" value="${formInfo.nameClassSrch}" />
			</div>
			<div class="form-group">
				<label>字典编号</label>
				<input type="text" name="nameCdSrch" id="nameCdSrch" class="form-control input-sm underline" value="${formInfo.nameCdSrch}" />
			</div>
			<div class="form-group">
				<label>字典名称</label>
				<input type="text" name="nameSrch" id="nameSrch" class="form-control input-sm underline" value="${formInfo.nameSrch}" />
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

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/maintenance/paramname/paramname_list.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
