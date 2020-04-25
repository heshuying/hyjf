<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="stzfconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="受托支付白名单" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">受托支付白名单</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${stzfConfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${stzfConfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="stzfconfig:ADD"> 
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加受托支付白名单">添加 <i class="fa fa-plus"></i></a>
 								</shiro:hasPermission> 
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel disabled"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
						</div>
							<br/>
							<%--  --%>
							<table id="equiList" style="table-layout:fixed;" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">受托支付收款人</th>
										<th class="center">收款人电子账号</th>
										<th class="center">收款人手机号</th>
										<th class="center">收款人名称/姓名</th>
										<th class="center">机构名称/姓名</th>
										<th class="center">用户名</th>
										<th class="center">手机号</th>
										<th class="center">电子账号</th>
										<th class="center">状态</th>
										<th class="center">审批时间</th> 
										<th class="center">审批人</th>
										<th class="center">备注</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty stzfConfigForm.recordList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${stzfConfigForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((stzfConfigForm.paginatorPage - 1) * stzfConfigForm.paginator.limit) + 1 }"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.stUserName}"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.stAccountid }"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.stMobile}"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.stCustomerName }"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.instname }"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.userName}"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.mobile }"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.accountid}"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.stateName }"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.approvalTime }"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.approvalName}"></c:out></td>
													<td style="word-wrap:break-word;" align="center"><c:out value="${record.remark}"></c:out></td>
													
													<%-- <td align="center"><fmt:formatNumber value="${record.capitalToplimit}" type="number" pattern="#,##0.00#" /> </td>
													<td align="center"><fmt:formatNumber value="${record.capitalAvailable}" type="number" pattern="#,##0.00#" /> </td> --%>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="stzfconfig:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${stzfConfigForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${stzfConfigForm.paginatorPage}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/stzhwhitelist/stzhwhitelist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
