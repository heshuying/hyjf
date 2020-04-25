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

<shiro:hasPermission name="allocationengine:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="智投配置" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle"></h1>
			<span class="mainDescription">本功能可以增加修改删除计划配置。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
							
								<c:set var="jspPrevDsb" value="${allocationengineForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${allocationengineForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								
								<shiro:hasPermission name="allocationengine:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel<i class="fa fa-Export"></i></a>
								</shiro:hasPermission>
	
 								<shiro:hasPermission name="allocationengine:ADD"> 
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加配置">添加配置 <i class="fa fa-plus"></i></a>
 								</shiro:hasPermission> 
 								
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
										
<!-- 								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a> -->
							</div>
							<br/>
							<%-- 计划配置 --%>
							<label>智投名称：${planName}</label>
							
							
							
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<!-- <th class="center">标签编号</th> -->
										<th class="center">标签名称</th>
										<th class="center">添加时间</th>
										<th class="center">状态</th>
										<th class="center">排序</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty allocationengineForm.recordAllocationEngineList}">
											<tr><td colspan="7">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${allocationengineForm.recordAllocationEngineList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
												    <!-- 序号 -->
													<td class="center"><c:out value="${status.index+((allocationengineForm.paginatorPage - 1) * allocationengineForm.paginator.limit) + 1 }"></c:out></td>
													<!-- 标签编号 -->
													<%-- <td class="center"><c:out value="${record.labelId }"></c:out></td> --%>
													<!-- 标签名称 -->
													<td class="center"><c:out value="${record.labelName }"></c:out></td>
													<!-- 添加时间 -->
													<td class="center"><hyjf:datetime value="${record.addTime }"></hyjf:datetime></td>
													<!-- 状态 0：停用 1：启用-->
													<td class="center"><c:out value="${record.labelStatus== '1' ? '启用' : '停用' }"></c:out></td>
													<!-- 排序 -->
													<td class="center"><c:out value="${record.labelSort }"></c:out></td>
													<!-- 操作 -->
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">

															<shiro:hasPermission name="allocationengine:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-labelid="${record.labelId }"
																		data-toggle="tooltip" data-placement="top" data-planname="${planName}" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>

															<shiro:hasPermission name="allocationengine:MODIFY">
																<a class="btn btn-transparent btn-xs tooltips fn-UpdateBy"
																	data-id="${record.id }" data-toggle="tooltip"
																	data-status="${record.labelStatus}"
																	data-label-name="${record.labelName }"
																	tooltip-placement="top" data-original-title="${record.labelStatus== '0' ? '启用' : '停用' }"><i
																	class="fa fa-lightbulb-o fa-white"></i></a>
															</shiro:hasPermission>
														<c:if test=""></c:if>
															<shiro:hasPermission name="allocationengine:MODIFY">
																	<a class="btn btn-transparent btn-xs fn-changeClkAct" data-id="${record.id }" data-labelid="${record.labelId }"
																			data-toggle="tooltip" data-placement="top" data-planname="${planName}" data-original-title="换绑"><i class="fa fa-pencil"></i></a>
																</shiro:hasPermission>
															
															
														</div>

														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="planConfig" paginator="${allocationengineForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="labelName" id="labelName" value="${record.labelName }" /> 
			<input type="hidden" name="planName" id="planName" value="${planName }" /> 
			<input type="hidden" name="id" id="planId"/>
			<input type="hidden" name="labelId" id="labelId" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${allocationengineForm.paginatorPage}" />
			<div class="form-group">
				<label>智投编号</label>
				<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${allocationengineForm.planNidSrch}" />
			</div>
			
			<div class="form-group">
				<label>智投名称</label>
				<input type="text" name="planNameSrch" class="form-control input-sm underline" value="${allocationengineForm.planNameSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/allocationengine/planconfig.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
