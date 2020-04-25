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

<shiro:hasPermission name="assetexception:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="资产异常列表" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">资产异常列表</h1>
			<span class="mainDescription">资产异常列表的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="assetexception:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${assetexceptionForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${assetexceptionForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<shiro:hasPermission name="assetexception:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
											data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i> </a>
									</shiro:hasPermission>
									<shiro:hasPermission name="assetexception:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										   data-toggle="tooltip" data-placement="bottom" data-original-title="添加异常标的">添加异常标的 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">资产来源</th>
										<th class="center">项目编号</th>
										<th class="center">借款金额</th>
										<th class="center">异常类型</th>
										<th class="center">异常原因</th>
										<th class="center">项目状态</th>
										<th class="center">异常时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="9">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(assetexceptionForm.paginatorPage - 1 ) * assetexceptionForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.instName }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td align="center"><c:out value="${record.account }"></c:out></td>
													<td align="center">
														<c:if test="${record.exceptionType eq '0'}">
															流标
														</c:if>
														<c:if test="${record.exceptionType eq '1'}">
															删标
														</c:if>
													</td>
													<td align="center"><c:out value="${record.exceptionRemark }"></c:out></td>
													<td align="center"><c:out value="${record.status }"></c:out></td>
													<td align="center"><c:out value="${record.exceptionTime }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="assetexception:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="assetexception:DELETE">
																<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-id="${record.id }"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
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
							<shiro:hasPermission name="assetexception:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${assetexceptionForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="assetexception:SEARCH">
				<input type="hidden" name="id" id="id" />
				<input type="hidden" name=borrowNid id="borrowNid" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${assetexceptionForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${assetexceptionForm.borrowNidSrch}" />
				</div>
                <div class="form-group">
                    <label>资产来源</label>
                    <select name="instCodeSrch" class="form-control underline form-select2">
                        <option value=""></option>
                        <c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1" varStatus="status">
                            <option value="${inst.instCode }"
                                    <c:if test="${inst.instCode eq assetexceptionForm.instCodeSrch}">selected="selected"</c:if>>
                                <c:out value="${inst.instName}"></c:out>
                            </option>
                        </c:forEach>
                    </select>
                </div>
				<div class="form-group">
					<label>异常时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${assetexceptionForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${assetexceptionForm.timeEndSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/exception/assetexception/assetexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
