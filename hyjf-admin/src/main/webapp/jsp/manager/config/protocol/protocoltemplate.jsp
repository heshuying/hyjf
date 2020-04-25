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


<shiro:hasPermission name="protocolView:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="协议模板管理" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">协议模板管理</h1>
			<span class="mainDescription">本功能可以增加修改删除协议模板。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${protocoltemplateForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${protocoltemplateForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<%--<shiro:hasPermission name="borrow:EXPORT">--%>
									<%--<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"--%>
									   <%--data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>--%>
								<%--</shiro:hasPermission>--%>
 								<shiro:hasPermission name="protocolView:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加协议模板">添加协议模板 <i class="fa fa-plus"></i></a>
 								</shiro:hasPermission>
							</div>
							<br/>
							<%-- 友情链接列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>

										<th class="center">序号</th>
										<th class="center">协议ID</th>
										<th class="center">协议模板名称</th>
										<th class="center">协议类别</th>
										<th class="center">启用版本</th>
										<th class="center">更新时间</th>
										<th class="center">协议预览</th>
										<th class="center">协议操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty protocoltemplateForm.recordList}">
											<tr><td colspan="8">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${protocoltemplateForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((protocoltemplateForm.paginatorPage - 1) * protocoltemplateForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.protocolTemplate.protocolId }"></c:out></td>
													<td class="center"><c:out value="${record.protocolTemplate.protocolName }"></c:out></td>
													<td class="center"><c:out value="${record.protocolTemplate.protocolType }"></c:out></td>
													<td class="center"><c:out value="${record.protocolTemplate.versionNumber }"></c:out></td>
													<td class="center"><c:out value="${record.updateTime }"></c:out></td>
													<%--<td class="center"> <fmt:formatDate value="${record.updateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>--%>
													<td class="center">
														<a href="${fileDomainUrl}${record.protocolTemplate.protocolUrl }" target="_blank">
															文件预览
														</a>
													</td>
													<%--<td class="center"><c:out value="${fileDomainUrl}${record.protocolUrl }"></c:out></td>--%>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="protocolView:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.protocolTemplate.id }"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="protocolView:VIEW">
															<a class="btn btn-transparent btn-xs fn-Info" data-id="${record.protocolTemplate.id }"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="查看"><i class="fa fa-list"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="protocolView:DELETE">
															<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-id="${record.protocolTemplate.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
														</div>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
									<%-- 分页栏 --%>
								<%--<shiro:hasPermission name="borrow:SEARCH">--%>
									<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${protocoltemplateForm.paginator}"></hyjf:paginator>
								<%--</shiro:hasPermission>--%>
								<br/><br/>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${protocoltemplateForm.paginatorPage}" />
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
			<iframe class="colobox-dialog-panel" id="urlDialogPanelInfoInfo" name="dialogIfm2" style="border:none;width:100%;height:100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/protocol/protocoltemplate.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
