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


<shiro:hasPermission name="noticeconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="通知配置" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">通知配置设置</h1>
			<span class="mainDescription">本功能可以增加修改关闭通知配置。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${borrowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${borrowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb} disabled"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb} disabled"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="noticeconfig:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right disabled"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br/>
							<%-- 模板列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">名称</th>
										<th class="center">标识</th>
										<th class="center">配置值</th>
										<th class="center">通知内容</th>
										<th class="center">状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty listForm}">
											<tr><td colspan="7">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${listForm }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${record.id}"></c:out></td>
													<td><c:out value="${record.title }"></c:out></td>
													<td>
													<c:choose>
													<c:when test="${fn:length(record.name)<=20}">${record.name }</c:when>
													<c:otherwise>${fn:substring(record.name,0,20) }...</c:otherwise>
													</c:choose></td>
													<td>
													<c:choose>
													<c:when test="${fn:length(record.value)<=20}">${record.value }</c:when>
													<c:otherwise>${fn:substring(record.value,0,20) }...</c:otherwise>
													</c:choose></td>
													<td>
													<c:choose>
													<c:when test="${fn:length(record.content)<=20}">${record.content }</c:when>
													<c:otherwise>${fn:substring(record.content,0,20) }...</c:otherwise>
													</c:choose></td>
													<td class="center"><c:out value="${record.status==null||record.status== '0' ? '关闭' : '开启' }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="noticeconfig:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-name="${record.name}"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="noticeconfig:MODIFY">
																																
																<c:if test="${record.status==null||record.status==0}">
																	<a class="btn btn-transparent btn-xs tooltips fn-Open" data-id="${record.id }" data-name="${record.name}"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="开启">
																		<i class="fa fa-lightbulb-o">
																		</i>
																	</a>
																</c:if>	
																<c:if test="${record.status==1}">
																	<a class="btn btn-transparent btn-xs tooltips fn-Close" data-id="${record.id }" data-name="${record.name}"
																			data-toggle="tooltip" tooltip-placement="top" data-original-title="关闭">
																			<i class="fa fa-lightbulb-o">
																			</i>
																	</a>
																</c:if>	
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="noticeconfig:MODIFY">
																	<li>
																		<a class="fn-Modify" data-id="${record.id }" data-name="${record.name}">修改</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="noticeconfig:MODIFY">
																	<li>
																		<c:if test="${record.status==null ||record.status==0}">
																			<a class="fn-Open" data-id="${record.id }" data-code="${record.name}">开启</a>
																		</c:if>	
																		<c:if test="${record.status==1}">
																			<a class="fn-Close" data-id="${record.id }" data-name="${record.name}">关闭</a>
																		</c:if>	
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
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="asidePanels" type="string">
			<div id="searchable-panel" class="perfect-scrollbar">
				<form id="mainForm" method="post">
					<input type="hidden" name="id" id="id" />
					<input type="hidden" name="name" id="name" />
				</form>
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
			<script type='text/javascript' src="${webRoot}/jsp/message/noticeConfig/noticeConfig.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
