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

<shiro:hasPermission name="lockUserPermission:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="后台登录失败配置" />
		
		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">后台登录失败配置</h1>
			<span class="mainDescription">后台登录失败配置。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="lockUserPermission:VIEW">
				      		<li><a href="${webRoot}/loginerrorlockuser/frontinit">前台登录失败配置</a></li>
				      	</shiro:hasPermission>
				      	<shiro:hasPermission name="lockUserPermission:VIEW">
				      		<li class="active"><a href="${webRoot}/loginerrorlockuser/backinit">后台登录失败配置</a></li>
				      	</shiro:hasPermission>
				    </ul>
				    <div class="tab-content">
					    <div class="tab-pane fade in active">
							<div class="row">
								<div class="col-md-12">
									<div class="search-classic">
										<shiro:hasPermission name="lockUserPermission:VIEW">
											<%-- 功能栏 --%>
											<div class="well">
												<c:set var="jspPrevDsb" value="${lockUserForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
												<c:set var="jspNextDsb" value="${lockUserForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
														data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
												<shiro:hasPermission name="lockUserPermission:VIEW">
													<a class="btn btn-o btn-primary btn-sm hidden-xs fn-config"
													   data-toggle="tooltip" data-placement="bottom" data-original-title="登录失败锁定配置">登录失败锁定 <i class="fa fa-plus"></i></a>
												</shiro:hasPermission>
												<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
														data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
														data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
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
												<col style="width:;" />
											</colgroup>
											<thead>
												<tr>
													<th class="center">序号</th>
													<th class="center">用户名</th>
													<th class="center">手机号</th>
													<th class="center">最后一次登录失败时间</th>
													<th class="center">操作</th>
												</tr>
											</thead>
											<tbody id="accountTbody">
												<c:choose>
													<c:when test="${empty lockUserForm.lstLockUser}">
														<tr><td colspan="9">暂时没有数据记录</td></tr>
													</c:when>
													<c:otherwise>
														<c:forEach items="${lockUserForm.lstLockUser }" var="record" begin="0" step="1" varStatus="status">
															<tr>
																<td class="center">${(lockUserForm.paginatorPage-1)*lockUserForm.paginator.limit+status.index+1 }</td>
																<td><c:out value="${record.username }"></c:out></td>
																<td><c:out value="${record.mobile }"></c:out></td>
																<td class="center"><c:out value="${record.lockTimeStr }"></c:out></td>
																<td class="center">
																	<div class="visible-md visible-lg hidden-sm hidden-xs">
																		<shiro:hasPermission name="lockUserPermission:UNLOCK">
																			<c:if test="${not empty record.unlocked}">
																				<c:if test="${record.unlocked==0}">
																					<a class="btn btn-transparent btn-xs fn-unlock" data-userid="${record.id }"
																						data-toggle="tooltip" data-placement="top" data-original-title="解锁"><i class="fa fa-unlock"></i></a>
																				</c:if>
																			</c:if>
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
										<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="backinit" paginator="${lockUserForm.paginator}"></hyjf:paginator>
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
			<input type="hidden" name="userid" id="userid" value= "${lockUserForm.id}"/>
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${lockUserForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="username" class="form-control input-sm underline"  maxlength="20" value="${lockUserForm.username}" />
			</div>
			<div class="form-group">
				<label>手机号</label>
				<input type="text" name="mobile" class="form-control input-sm underline"  maxlength="20" value="${lockUserForm.mobile}" />
			</div>
			<div class="form-group">
				<label>最后一次登录失败时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="lockTimeStartStr" id="start-date-time" class="form-control underline" value="${lockUserForm.lockTimeStartStr}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="lockTimeEndStr" id="end-date-time" class="form-control underline" value="${lockUserForm.lockTimeEndStr}" />
					</span>
				</div>
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
			<script type='text/javascript' src="${webRoot}/jsp/loginerror/backlockuserlist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
