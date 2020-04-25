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


<shiro:hasPermission name="mailtemplate:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="模板管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">邮件模板设置</h1>
			<span class="mainDescription">本功能可以增加修改删除邮件模板。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${listForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${listForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
 								<shiro:hasPermission name="mailtemplate:ADD"> 
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加新模板">添加 <i class="fa fa-plus"></i></a>
 								</shiro:hasPermission> 
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 模板列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">模板名称</th>
										<th class="center">模板标识</th>
										<th class="center">模板内容</th>
										<th class="center">状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty listForm.recordList}">
											<tr>
												<td colspan="6">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${listForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((listForm.paginatorPage - 1) * listForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.mailName }"></c:out></td>
													<td><c:out value="${record.mailValue}"></c:out></td>
													<td><c:if test="${fn:length(record.mailContent)<=100 }">${record.mailContent }</c:if>
													<c:if test="${fn:length(record.mailContent)>100 }"><c:out value="${fn:substring(record.mailContent,0,100) }..."></c:out></c:if></td>
													<td class="center"><c:out
															value="${record.mailStatus== '0' ? '关闭' : '开启' }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="mailtemplate:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-id="${record.id }" data-code="${record.mailValue}"
																	data-toggle="tooltip" tooltip-placement="top"
																	data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="mailtemplate:MODIFY">

																<c:if test="${record.mailStatus==0}">
																	<a class="btn btn-transparent btn-xs tooltips fn-Open"
																		data-id="${record.id }"
																		data-code="${record.mailValue}" data-toggle="tooltip"
																		tooltip-placement="top" data-original-title="开启">
																		<i class="fa fa-lightbulb-o"> </i>
																	</a>
																</c:if>
																<c:if test="${record.mailStatus==1}">
																	<a class="btn btn-transparent btn-xs tooltips fn-Close"
																		data-id="${record.id }"
																		data-code="${record.mailValue}" data-toggle="tooltip"
																		tooltip-placement="top" data-original-title="关闭">
																		<i class="fa fa-lightbulb-o"> </i>
																	</a>
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
																	<shiro:hasPermission name="mailtemplate:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }"
																			data-code="${record.mailValue}">修改</a></li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="mailtemplate:MODIFY">
																		<li><c:if test="${record.mailStatus==1}">
																				<a class="fn-Close" data-id="${record.id }"
																					data-code="${record.mailValue}">关闭</a>
																			</c:if> <c:if test="${record.mailStatus==0}">
																				<a class="fn-Open" data-id="${record.id }"
																					data-code="${record.mailValue}">启用</a>
																			</c:if></li>
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
							<br />
							<br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="mailValue" id="mailValue" />
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${listForm.paginatorPage}" />
			<div class="form-group">
				<label>模板名称</label> <input type="text" name="mailName" id="mailName"
					class="form-control input-sm underline"
					value="${listForm.mailName}" />
			</div>
			<div class="form-group">
				<label>状态</label> <select name="mailStatus"
					class="form-control underline form-select2">
					<option value="2"
						<c:if test="${listForm.mailStatus==2}">selected="selected"</c:if>><c:out
							value="--全部--"></c:out></option>
					<option value="0"
						<c:if test="${listForm.mailStatus==0}">selected="selected"</c:if>><c:out
							value="关闭"></c:out></option>
					<option value="1"
						<c:if test="${listForm.mailStatus==1}">selected="selected"</c:if>><c:out
							value="开启"></c:out></option>
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
			<script type='text/javascript' src="${webRoot}/jsp/message/mailTemplate/mailTemplatelist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
