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


<shiro:hasPermission name="activitylist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="活动设置" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">活动详情</h1>
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
								<c:set var="jspPrevDsb" value="${activityinfoForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${activityinfoForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 活动列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
								<col style="width:55px;" />
								<col />
								<col style="width:93px;" />
								<col style="width:93px;" />
							</colgroup>
								<thead>
									<tr>
										<th class="center">当前排名</th>
										<th class="center">用户名</th>
										<th class="center">真实姓名</th>
										<th class="center">手机号</th>
										<th class="center">一级部门</th>
										<th class="center">二级部门</th>
										<th class="center">三级部门</th>
										<th class="center">当前时速</th>
										<th class="center">累计出借金额</th>
										<th class="center">使用APP出借</th>
										<th class="center">新用户首投>=5000</th>
										<th class="center">返现金额</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty activityinfoForm.forBack}">
											<tr>
												<td colspan="13">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${activityinfoForm.forBack }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(activityinfoForm.paginatorPage - 1 ) * activityinfoForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.userName}"></c:out></td>
													<td class="center"><c:out value="${record.realName}"></c:out></td>
													<td class="center"><c:out value="${record.mobile}"></c:out></td>
													<td class="center"><c:out value="${record.first_level_department}"></c:out></td>
													<td class="center"><c:out value="${record.second_level_department }"></c:out></td>
													<td class="center"><c:out value="${record.third_level_department }"></c:out></td>
													<td class="center"><c:out value="${record.speed }"></c:out></td>
													<td class="center"><c:out value="${record.tenderAccountAll }"></c:out></td>
													<td class="center"><c:out value="${record.isAppFlg }"></c:out></td>
													<td class="center"><c:out value="${record.isFirstFlg }"></c:out></td>
													<td class="center"><c:out value="${record.returnAmount }"></c:out></td>
													<td class="center">
														<!-- 未返现  返现金额> 0 -->
														
															<shiro:hasPermission name="activitylist:RETURNCASH">
															<c:if test="${record.isover == 1 and record.returnAmount != null and record.returnAmount != 0 and record.isReturnFlg == null }">
																<div class="visible-md visible-lg hidden-sm hidden-xs">
																	<a class="btn btn-transparent btn-xs fn-Returncash" data-toggle="tooltip" data-userId="${record.userId }" data-returnAmount="${record.returnAmount }" data-toggle="tooltip" tooltip-placement="top" data-original-title="返现"><i class="fa fa-mail-reply "></i></a>
																</div>
																<div class="visible-xs visible-sm hidden-md hidden-lg">
																	<div class="btn-group" dropdown="">
																		<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																			<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																		</button>
																		<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																			<li><a class="fn-Returncash" data-userId="${record.userId }" data-returnAmount="${record.returnAmount }">返现</a></li>
																		</ul>
																	</div>
																</div>
																</c:if>
															</shiro:hasPermission>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="getActivityInfoAction" paginator="${activityinfoForm.paginator}">
							</hyjf:paginator>
							<br />
							<br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="userId" id="userId" />
			<input type="hidden" name="returnAmount" id="returnAmount" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${activityinfoForm.paginatorPage}" />
			<div class="form-group">
				<label>用户名</label>
				<input type="text" name="userName" class="form-control input-sm underline" value="${activityinfoForm.userName}" />
			</div>
			<div class="form-group">
				<label>真实姓名</label>
				<input type="text" name="realName" class="form-control input-sm underline" value="${activityinfoForm.realName}" />
			</div>
			<div class="form-group">
				<label>手机号</label>
				<input type="text" name="mobile" class="form-control input-sm underline" value="${activityinfoForm.mobile}" />
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.thumbnails-wrap {
				border: 1px solid #ccc;
				padding: 3px;
				display: inline-block;
			}
			.thumbnails-wrap img {
				min-width: 35px;
				height: 22px;
			}
			.popover {
				max-width: 500px;
			}
			.popover img {
				max-width: 460px;
			}
			</style>
		</tiles:putAttribute>
		
		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/activitylist/activityInfoList.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
