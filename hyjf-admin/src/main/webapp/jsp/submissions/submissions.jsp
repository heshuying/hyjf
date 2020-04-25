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


<shiro:hasPermission name="submissions:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="意见反馈" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">意见反馈列表</h1>
			<span class="mainDescription">本功能可以回复用户的意见反馈。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="submissions:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${submissionsListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${submissionsListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="submissions:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">操作系统</th>
										<th class="center">版本号</th>
										<th class="center">手机型号</th>
										<th class="center text-justify" style="width:30%;">反馈内容</th>
										<th class="center">时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty submissionsListForm.submissionsList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${submissionsListForm.submissionsList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(submissionsListForm.paginatorPage-1)*submissionsListForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.sysType }"></c:out></td>
													<td class="center"><c:out value="${record.platformVersion }"></c:out></td>
													<td class="center"><c:out value="${record.phoneType }"></c:out></td>
													<td class="left"><c:out value="${record.content }"></c:out></td>
													<td class="center"><c:out value="${record.addTime }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="submissions:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.submissionsId }"
																		data-toggle="tooltip" data-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="submissions:MODIFY">
																	<li>
																		<a class="fn-Modify" data-id="${record.submissionsId }">修改</a>
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
							<shiro:hasPermission name="submissions:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="querySubmissionsAction" paginator="${submissionsListForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="submissions:SEARCH">
				<input type="hidden" name="submissionsId" id="submissionsId" value= "${submissionsListForm.submissionsId}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${submissionsListForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${ submissionsListForm.userName}" />
				</div>
				
				<div class="form-group">
					<label>操作系统</label>
					<select name="sysType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${submissionsListForm.sysTypeList }" var="property" begin="0" step="1" >
							<option value="${property.nameCd }"
								<c:if test="${property.nameCd eq submissionsListForm.sysType}">selected="selected"</c:if>>
								<c:out value="${property.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				
				<div class="form-group">
					<label>版本号</label>
					<input type="text" name="platformVersion" class="form-control input-sm underline" maxlength="20" value="${submissionsListForm.platformVersion}"/>
				</div>
				<div class="form-group">
					<label>手机型号</label>
					<input type="text" name="phoneType" class="form-control input-sm underline" maxlength="20" value="${submissionsListForm.phoneType}"/>
				</div>
				
				<div class="form-group">
					<label>时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="addTimeStart" id="start-date-time" class="form-control underline" value="${submissionsListForm.addTimeStart}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="addTimeEnd" id="end-date-time" class="form-control underline" value="${submissionsListForm.addTimeEnd}" />
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
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/submissions/submissions.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
