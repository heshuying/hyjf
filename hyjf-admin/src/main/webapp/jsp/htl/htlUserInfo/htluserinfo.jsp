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

<shiro:hasPermission name="htlUserInfo:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇天利账户信息" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇天利账户信息</h1>
			<span class="mainDescription">本功能可以查看汇天利收益记录信息</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="htlUserInfo:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${htlUserInfoForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${htlUserInfoForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								    <shiro:hasPermission name="htlUserInfo:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出excel"><i class="fa fa-plus"></i> 导出EXCEL</a>
									 </shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"><i class="fa fa-refresh"></i> 刷新</a>
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
										<th class="center">用户名</th>
										<th class="center">推荐人</th>
										<th class="center">本金总额</th>
										<th class="center">今日预计收益</th>
										<th class="center">历史累计收益</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty htlUserInfoForm.recordList}">
											<tr><td colspan="7">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${htlUserInfoForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(htlUserInfoForm.paginatorPage - 1 ) * htlUserInfoForm.paginator.limit + status.index + 1 }</td>
													<td><c:out value="${record.username }"></c:out></td>
													<td><c:out value="${record.refername }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.principal }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber value="${record.todayEarnings }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber value="${record.historyEarnings }" pattern="#,##0.00#" /></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<a class="btn btn-transparent btn-xs" 
																	data-toggle="tooltip" tooltip-placement="top" href= "${webRoot}/htl/productinto/productintorecord/searchAction?userid=${record.userid }" data-original-title="转入记录"><i class="fa fa-sign-in"></i></a>
															<a class="btn btn-transparent btn-xs tooltips" 
																	data-toggle="tooltip" tooltip-placement="top" href= "${webRoot}/htl/productredeem/searchAction?userid=${record.userid }" data-original-title="转出记录"><i class="fa fa-sign-out"></i></a>
															<a class="btn btn-transparent btn-xs tooltips" 
																	data-toggle="tooltip" tooltip-placement="top" href= "${webRoot}/htl/productinterest/searchAction?userid=${record.userid }" data-original-title="收益明细"><i class="fa fa-list"></i></a>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<li>
																		<a class="fn-Modify" href= "${webRoot}/htl/productinto/productintorecord/searchAction?userid=${record.userid }">转入记录</a>
																	</li>
																	<li>
																		<a class="fn-Modify" href= "${webRoot}/htl/productredeem/searchAction?userid=${record.userid }">转出记录</a>
																	</li>
																	<li>
																		<a class="fn-Modify" href= "${webRoot}/htl/productinterest/searchAction?userid=${record.userid }" >收益明细</a>
																	</li>
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
							<shiro:hasPermission name="htlUserInfo:VIEW">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${htlUserInfoForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="htlUserInfo:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${htlUserInfoForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label> 
					<input type="text" name="username" class="form-control input-sm underline"  maxlength="20" value="${ htlUserInfoForm.username}" />
				</div>
				<div class="form-group">
					<label>推荐人</label> 
					<input type="text" name="refername" class="form-control input-sm underline" maxlength="20" value="${htlUserInfoForm.refername}"/>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>			

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/htl/htlUserInfo/htluserinfo.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
