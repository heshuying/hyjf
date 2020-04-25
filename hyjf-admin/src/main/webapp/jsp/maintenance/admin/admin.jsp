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

<shiro:hasPermission name="admin:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="用户管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">用户管理</h1>
			<span class="mainDescription">本功能可以增加修改删除用户。</span>
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
								<c:set var="jspPrevDsb" value="${adminForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${adminForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<shiro:hasPermission name="admin:ADD">
								<a class="btn btn-o btn-primary btn-sm fn-Add" data-toggle="tooltip" data-placement="bottom" data-original-title="添加新用户"> 添加 <i class="fa fa-plus"></i></a>
							</shiro:hasPermission>
							<shiro:hasPermission name="admin:DELETE">
								<a class="btn btn-o btn-primary btn-sm fn-Deletes" data-toggle="tooltip" data-placement="bottom" data-original-title="删除用户">删除 <i class="fa fa-trash-o"></i> </a>
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
										<th class="center">用户名</th>
										<th class="center">姓名</th>
										<th class="center">所属部门</th>
										<th class="center">邮箱</th>
										<th class="center">手机号码</th>
										<th class="center">添加时间</th>
										<th class="center">用户状态</th>
										<th class="center">角色</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty adminForm.recordList}">
											<tr><td colspan="11">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${adminForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="hidden-xs center">
														<div align="left" class="checkbox clip-check check-primary checkbox-inline">
															<input type="checkbox" class="listCheck" id="row${status.index }" value="${record.id }">
															<label for="row${status.index }"></label>
														</div>
													</td>
													<td class="center"><c:out value="${status.index+((adminForm.paginatorPage - 1) * adminForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td><c:out value="${record.truename }"></c:out></td>
													<td><c:out value="${record.departmentName }"></c:out></td>
													<td><c:out value="${record.email }"></c:out></td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><c:out value="${record.addtimeFormated }"></c:out></td>
													<td class="center"><c:out value="${record.state == '0' ? '启用' : '禁用' }"></c:out></td>
													<td><c:out value="${record.roleName }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="admin:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="admin:DELETE">
															<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-id="${record.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="admin:RESETPWD">
															<a class="btn btn-transparent btn-xs tooltips fn-ResetPwd" data-id="${record.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="重置密码"><i class="fa fa-undo fa fa-white"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="admin:MODIFY">
																	<li>
																		<a class="fn-Modify" data-id="${record.id }">修改</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="admin:DELETE">
																	<li>
																		<a class="fn-Delete" data-id="${record.id }">删除</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="admin:RESETPWD">
																	<li>
																		<a class="fn-ResetPwd" data-id="${record.id }">重置密码</a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${adminForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${adminForm.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>用户名</label>
				<input type="text" name="usernameSrch" class="form-control input-sm underline" value="${adminForm.usernameSrch}" />
			</div>
			<div class="form-group">
				<label>姓名</label>
				<input type="text" name="truenameSrch" class="form-control input-sm underline" value="${adminForm.truenameSrch}" />
			</div>
			<div class="form-group">
				<label>手机号码</label>
				<input type="text" name="mobileSrch" class="form-control input-sm underline" value="${adminForm.mobileSrch}" />
			</div>
			<div class="form-group">
				<label>部门</label>
				<input type="text" name="departmentSrch" class="form-control input-sm underline" value="${adminForm.departmentSrch}" />
			</div>
			<div class="form-group">
				<label>用户状态</label>
				<%-- <div class="row">
					<div class="col-xs-12">
						<div class="checkbox clip-check check-primary checkbox-inline">
							<input type="checkbox" id="stateOn" name="stateSrchOn" value="0" class="listCheck" ${adminForm.stateSrchOn == '0' ? 'checked' : ''}> <label for="stateOn">  启用
							</label>
							<input type="checkbox" id="stateOff" name="stateSrchOff" value="1" class="listCheck" ${adminForm.stateSrchOff == '1' ? 'checked' : ''}> <label for="stateOff">  禁用
							</label>
						</div>
					</div>
				</div> --%>
				<select name="stateSrch" class="form-control underline form-select2">
						<option value="" selected="selected">全部</option>
						<option value="0">启用</option>
						<option value="1">禁用</option>
			<%-- 			<c:forEach items="${userRoles }" var="role" begin="0" step="1">
							<option value="${role.nameCd }"
								<c:if test="${role.nameCd eq usersListForm.userRole}">selected="selected"</c:if>>
								<c:out value="${role.name }"></c:out>
							</option>
						</c:forEach> --%>
					</select>
			</div>
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="addtimeStartSrch" id="addtimeStartSrch" class="form-control underline" value="${adminForm.addtimeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="addtimeEndSrch" id="addtimeEndSrch" class="form-control underline" value="${adminForm.addtimeEndSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/maintenance/admin/admin.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
