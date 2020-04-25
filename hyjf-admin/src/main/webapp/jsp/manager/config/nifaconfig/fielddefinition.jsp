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

<shiro:hasPermission name="nifaconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="互金系统配置" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">互金系统配置</h1>
			<span class="mainDescription">本功能可以修改查询相应的互金系统配置信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<shiro:hasPermission name="nifaconfig:SEARCH">
							<li  class="active"><a href="${webRoot}/manager/config/nifaconfig/init">修改字段定义</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="nifaconfig:SEARCH">
							<li ><a href="${webRoot}/manager/config/nifaconfig/initContract">合同模板条款</a></li>
						</shiro:hasPermission>
					</ul>
					<div class="tab-content">
						<div class="tab-pane fade in active">
							<div class="row">
								<div class="col-md-12">
									<div class="search-classic">
										<shiro:hasPermission name="nifaconfig:SEARCH">
											<%-- 功能栏 --%>
											<div class="well">
												<c:set var="jspPrevDsb" value="${nifaconfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
												<c:set var="jspNextDsb" value="${nifaconfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
												   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
												   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
											<shiro:hasPermission name="nifaconfig:ADD">
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
												   data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
											</shiro:hasPermission>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
												   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
											</div>
										</shiro:hasPermission>
										<br/>

											<%-- 角色列表一览 --%>
										<table id="equiList" class="table table-striped table-bordered table-hover">
											<colgroup>
												<col style="width:55px;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
											</colgroup>
											<thead>
											<tr>
												<th class="center">序号</th>
												<th class="center">更新时间</th>
												<th class="center">更新人</th>
												<th class="center">操作</th>
											</tr>
											</thead>
											<tbody id="accountTbody">
											<c:choose>
												<c:when test="${empty nifaconfigForm.recordList}">
													<tr><td colspan="9">暂时没有数据记录</td></tr>
												</c:when>
												<c:otherwise>
													<c:forEach items="${nifaconfigForm.recordList}" var="record" begin="0" step="1" varStatus="status">
													<tr>
															<td class="center"><c:out value="${status.index+((nifaconfigForm.paginatorPage - 1) * nifaconfigForm.paginator.limit) + 1 }"></c:out></td>
															<%--<td class="center"><hyjf:datetime value="${record.updateTime}"></hyjf:datetime></td>--%>
															<td align="center"><c:out value="${record.updateDate}"></c:out></td>
															<td align="center"><c:out value="${record.updateUserName}"></c:out></td>
															<td class="center">
																<div class="visible-md visible-lg hidden-sm hidden-xs">
																	<shiro:hasPermission name="nifaconfig:VIEW">
																		<a class="btn btn-transparent btn-xs fn-Info" data-id="${record.id}"
																		   data-toggle="tooltip" tooltip-placement="top" data-original-title="查看">查看</a>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="nifaconfig:MODIFY">
																		<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																		   data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
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
										<shiro:hasPermission name="nifaconfig:SEARCH">
											<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${nifaconfigForm.paginator}"></hyjf:paginator>
										</shiro:hasPermission>
										<br/><br/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" value= "${nifaconfigForm.id}"/>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/nifaconfig/fielddefinition.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
