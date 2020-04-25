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


<shiro:hasPermission name="msgpushtemplate:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="自动触发消息模版" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">自动触发消息模版</h1>
			<span class="mainDescription">本功能可以增加修改删除自动触发消息模版。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${msgPushTemplateForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${msgPushTemplateForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="msgpushtemplate:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add" data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a> <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 列表 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
									<col style="width: 80px;" />
									<col style="width: 80px;" />
									<col style="width: 80px;" />
									<col style="width: 180px;" />
									<col style="width: 100px;" />
									<col style="width: 100px;" />
									<col style="width: 100px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">标签类型</th>
										<th class="center">标题</th>
										<th class="center">消息编码</th>
										<th class="center">内容</th>
										<th class="center">推送终端</th>
										<th class="center">状态</th>
										<th class="center">编辑</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty msgPushTemplateForm.recordList}">
											<tr>
												<td colspan="8">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${msgPushTemplateForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((msgPushTemplateForm.paginatorPage - 1) * msgPushTemplateForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center">
													<c:forEach items="${templatePushTags }" var="tag" begin="0" step="1">
															<c:if test="${tag.id eq record.tagId}"> <c:out value="${tag.tagName }"></c:out> </c:if>
													</c:forEach>	</td>
													<td class="center"><c:out value="${record.templateTitle }"></c:out></td>
													<td class="center"><c:out value="${record.templateCode }"></c:out></td>
													<td class="center"><c:if test="${fn:length(record.templateContent)<=20 }"><c:out value="${record.templateContent }"></c:out></c:if><c:if test="${fn:length(record.templateContent)>20 }"><c:out value="${fn:substring(record.templateContent,0,20) }"></c:out>...</c:if></td>
													<td class="center"><c:set var="tempStrs" value="${fn:split(record.templateTerminal, ',')}" /> 
													<c:forEach items="${plats }" var="plat" begin="0" step="1">
															<c:forEach items="${tempStrs }" var="tempStr" begin="0" step="1">
																<c:if test="${plat.nameCd eq tempStr}">
																	<c:out value="${plat.name }"></c:out>,
																</c:if>
															</c:forEach>
														</c:forEach></td>
													<td class="center"><c:forEach items="${templateStatus }" var="template" begin="0" step="1">
															<c:if test="${template.nameCd eq record.status}">
																<c:out value="${template.name }"></c:out>
															</c:if>
														</c:forEach></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="msgpushtemplate:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="编辑"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="msgpushtemplate:MODIFY">
																<a class="btn btn-transparent btn-xs tooltips fn-UpdateBy" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="${record.status == '0' || record.status == '2' ? '启用' : '禁用' }"><i class="fa fa-lightbulb-o fa-white"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="msgpushtemplate:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">编辑</a></li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="msgpushtemplate:MODIFY">
																		<a class="fn-UpdateBy" data-id="${record.id }">${record.status == '0' || record.status == '2' ? '启用' : '禁用' }</a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${msgPushTemplateForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${msgPushTemplateForm.paginatorPage}" />
			<div class="form-group">
				<label>标签类型</label> <select name="templateTagIdSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${templatePushTags }" var="tags" begin="0" step="1">
						<option value="${tags.id }" <c:if test="${tags.id eq msgPushTemplateForm.templateTagIdSrch}">selected="selected"</c:if>>
							<c:out value="${tags.tagName }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>标题</label> <input type="text" name="templateTitleSrch" class="form-control input-sm underline" value="${msgPushTemplateForm.templateTitleSrch}" maxlength="20" />
			</div>
			<div class="form-group">
				<label>消息编码</label> <input type="text" name="templateCodeSrch" class="form-control input-sm underline" value="${msgPushTemplateForm.templateCodeSrch}" maxlength="20" />
			</div>
			<div class="form-group">
				<label>状态</label> <select name="templateStatusSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${templateStatus }" var="status" begin="0" step="1">
						<option value="${status.nameCd }" <c:if test="${status.nameCd eq msgPushTemplateForm.templateStatusSrch}">selected="selected"</c:if>>
							<c:out value="${status.name }"></c:out>
						</option>
					</c:forEach>
				</select>
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
.thumbnails-wrap {
	border: 1px solid #ccc;
	padding: 3px;
	display: inline-block;
}

.thumbnails-wrap img {
	min-width: 35px;
	height: 22px;
}

.popover {
	max-width: 500px;
}

.popover img {
	max-width: 460px;
}
</style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/msgpush/template/template_list.js"></script>
		</tiles:putAttribute>

	</tiles:insertTemplate>
</shiro:hasPermission>
