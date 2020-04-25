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

<shiro:hasPermission name="borrow_image:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="产品图片" />
		<style>
		 td{text-align:center;}
		</style>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${form.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="borrow_image:ADD">
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
									data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
						</shiro:hasPermission>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
					</div>
					<br />
					<%-- 列表一览 --%>
					<div class="row">
						<%-- 产品管理列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">版本号</th>
									<th class="center">排列顺序</th>
									<th class="center">图片标示</th>
									<th class="center">图片名称</th>
									<th class="center">跳转标示</th>
									<th class="center">图片</th>
									<th class="center">图片描述</th>
									<th class="center">跳转类型</th>
									<th class="center">跳转地址</th>
									<th class="center">状态</th>
									<th class="center">编辑</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty recordList}">
										<tr>
											<td colspan="7">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
									
									<c:forEach items="${recordList}" var="record"
											begin="0" step="1" varStatus="vs">
											<tr>
										<td>${vs.count }</td>
												<td>
													<c:choose>
													<c:when test="${(record.versionMax eq null || record.versionMax eq '')||(record.version eq null || record.version eq '')}">
														<c:if test="${record.versionMax eq null || record.versionMax eq ''}">
															${record.version}及以上
																</c:if>
																<c:if test="${record.version eq null || record.version eq ''}">
															${record.versionMax}及以下
																</c:if>
													</c:when>
													<c:otherwise>
													${record.version}--${record.versionMax}
													</c:otherwise>
													</c:choose>
												</td>
												<td>${record.sort} </td>
												<td>${record.borrowImage}</td>
												<td>${record.borrowImageTitle} </td>
												<td>${record.jumpName} </td>
												<td>
													<img style="height: 150px;margin-bottom:10px;" 
														src="${record.borrowImageUrl}" >  
												</td>
												<td><c:if test="${empty record.notes }">&nbsp;</c:if> <c:out value="${record.notes }" /> </td>
													<td>
													<c:choose>
													<c:when test="${record.pageType eq 1}">
													H5页面
													</c:when>
													<c:when test="${record.pageType eq 0}">
													原生页面
													</c:when>
													<c:otherwise>
														默认
													</c:otherwise>
													</c:choose>
												</td>
												<td>
										
													<c:choose>
													<c:when test="${record.pageUrl eq null || record.pageUrl eq ''}">
															<c:if test="${record.jumpName eq 'HXF'}">
																汇消费列表
															</c:if>
															<c:if test="${record.jumpName eq 'HZT'}">
																汇直投列表
															</c:if>
															<c:if test="${record.jumpName eq 'XSH' ||record.jumpName eq 'ZXH'||record.jumpName eq 'RTB'||record.jumpName eq 'HZR'}">
															${record.borrowImageTitle}
															</c:if>
															<c:if test="${record.pageType eq 2}">
															无
															</c:if>
													</c:when>
													<c:otherwise>
													${record.pageUrl}
													</c:otherwise>
													</c:choose>
												</td>
												<td>
													<c:choose>
													<c:when test="${record.status eq 0}">
													启用
													</c:when>
													<c:otherwise>
													<font color="#F00">禁用</font>
													</c:otherwise>
													</c:choose>
												</td>
												<td>
													<c:choose>
													<c:when test="${record.status eq 0}">
													<a href="${webRoot}/app/maintenance/borrowimage/updateAction.do?id=${record.id}&status=1">禁用</a>
													</c:when>
													<c:otherwise>
													<a href="${webRoot}/app/maintenance/borrowimage/updateAction.do?id=${record.id}&status=0">启用</a>
													</c:otherwise>
													</c:choose> |
													<a class="fn-TenderInfo fn-Modify"" data-id="${record.id}" >编辑</a>|
													<a class="fn-TenderInfo fn-Delete" data-id="${record.id}" >删除</a>
												</td>
										</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${form.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>
	
	
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" /> 
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
            table tr td{text-align:center;}
            </style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/app/maintenance/borrowimage/borrowimage.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
