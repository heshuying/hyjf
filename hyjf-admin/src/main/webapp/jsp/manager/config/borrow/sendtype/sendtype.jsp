<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
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
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="sendtype:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="发标/复审" />

		<%-- 画面功能路径 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">发标/复审</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab"> 
				<shiro:hasPermission name="projecttype:VIEW">
					<li><a href="${webRoot}/manager/config/projecttype/init">项目类型</a></li>
				</shiro:hasPermission>

		      	<shiro:hasPermission name="finmanchargenew:VIEW">
		      		<li><a href="${webRoot}/manager/config/finmanchargenew/init">费率配置</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="borrowflow:VIEW">
		      		<li><a href="${webRoot}/manager/config/borrowflow/init">流程配置</a></li>
		      	</shiro:hasPermission>
				<shiro:hasPermission name="sendtype:VIEW">
					<li class="active"><a href="${webRoot}/manager/config/sendtype/init">发标/复审</a></li>
				</shiro:hasPermission>
		    </ul>
	      	<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${sendtypeForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${sendtypeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<shiro:hasPermission name="sendtype:ADD">
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
									data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
							</shiro:hasPermission>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
					</div>
					<br/>
	
					<%-- 角色列表一览 --%>
					<table id="equiList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width:55px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">编号</th>
								<th class="center">名称</th>
								<th class="center">时间（分钟）</th>
								<th class="center">说明</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="userTbody">
							<c:choose>
								<c:when test="${empty sendtypeForm.recordList}">
									<tr><td colspan="6">暂时没有数据记录</td></tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${sendtypeForm.recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center">${(sendtypeForm.paginatorPage-1)*sendtypeForm.paginator.limit+status.index+1 }</td>
											<td><c:out value="${record.sendCd }"></c:out></td>
											<td><c:out value="${record.sendName }"></c:out></td>
											<td align="right"><c:out value="${record.afterTime }"></c:out></td>
											<td><c:out value="${record.remark }"></c:out></td>
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<shiro:hasPermission name="sendtype:MODIFY">
														<a class="btn btn-transparent btn-xs fn-Modify" data-sendcd="${record.sendCd }"
																data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
													</shiro:hasPermission>
													<shiro:hasPermission name="sendtype:DELETE">
														<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-sendcd="${record.sendCd }"
																data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
													</shiro:hasPermission>
												</div>
												<div class="visible-xs visible-sm hidden-md hidden-lg">
													<div class="btn-group" dropdown="">
														<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
															<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
														</button>
														<ul class="dropdown-menu pull-right dropdown-light" role="menu">
															<shiro:hasPermission name="sendtype:MODIFY">
																<li>
																	<a class="fn-Modify" data-sendcd="${record.sendCd }">修改</a>
																</li>
															</shiro:hasPermission>
															<shiro:hasPermission name="sendtype:DELETE">
																<li>
																	<a class="fn-Delete" data-sendcd="${record.sendCd }">删除</a>
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
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${sendtypeForm.paginator}"></hyjf:paginator>
					<br/><br/>
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="sendCd" id="sendCd" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${sendtypeForm.paginatorPage}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/sendtype/sendtype.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
