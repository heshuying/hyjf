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


<shiro:hasPermission name="userslist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="会员列表" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">会员列表</h1>
			<span class="mainDescription">本功能可以修改查询相应的会员信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="userslist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${usersListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${usersListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<%-- <shiro:hasPermission name="evaluationList:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission> --%>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
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
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">姓名</th>
										<th class="center">手机</th>
										<%-- DEL BY LIUSHOUYI 合规检查 START --%><%--
										<th class="center">用户属性</th>
										--%><%-- DEL BY LIUSHOUYI 合规检查 END --%>
										<th class="center">开户状态</th>
										<th class="center">51老用户</th>
										<th class="center">测评状态</th>
										<th class="center">风险测评分</th>
										<th class="center">风险等级</th>
										<th class="center">最后一次测评时间</th>
										<th class="center">风险测评到期时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty usersListForm.recordList}">
											<tr><td colspan="13">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${usersListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(usersListForm.paginatorPage-1)*usersListForm.paginator.limit+status.index+1 }</td>
													<td class="left"><c:out value="${record.userName }"></c:out></td>
													<td class="left"><c:out value="${record.realName }"></c:out></td>
													<td class="center"><hyjf:asterisk value="${record.mobile }" permission="userslist:HIDDEN_SHOW"></hyjf:asterisk></td>
													<%-- DEL BY LIUSHOUYI 合规检查 START --%><%--
													<td class="center"><c:out value="${record.userProperty }"></c:out></td>
													--%><%-- DEL BY LIUSHOUYI 合规检查 END --%>
													<td class="center"><c:out value="${record.accountStatus }"></c:out></td>
													<td class="center"><c:if test="${record.is51 =='1'}">是</c:if><c:if test="${record.is51 =='0'}">否</c:if></td>
													<td class="center"><c:out value="${record.evaluationStatus }"></c:out></td>
													<td class="center"><c:out value="${record.evaluationScore }"></c:out></td>
													<td class="center"><c:out value="${record.evaluationType }"></c:out></td>
													<td class="center"><c:out value="${record.createtime }"></c:out></td>
													<td class="center">
														<c:if test="${record.evaluationStatus ne '未测评'}">
															<c:out value="${record.evalationTime }"></c:out>
														</c:if>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="userslist:INFO">
																<a class="btn btn-transparent btn-xs tooltips fn-Info" data-userid="${record.userId }"
																		data-toggle="tooltip" data-placement="top" data-original-title="详情"><i class="fa fa-file-text"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="userslist:INFO">
																	<li>
																		<a class="fn-Info" data-userid="${record.userId }">详情</a>
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
							<shiro:hasPermission name="userslist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="evaluationList" paginator="${usersListForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="userslist:SEARCH">
				<input type="hidden" name="ids" id="ids" />
				<input type="hidden" name="userId" id="userId" value= "${usersListForm.userId}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${usersListForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${ usersListForm.userName}" />
				</div>
				<div class="form-group">
					<label>姓名</label>
					<input type="text" name="realName" class="form-control input-sm underline" maxlength="20" value="${usersListForm.realName}"/>
				</div>
				<div class="form-group">
					<label>手机号码</label>
					<input type="text" name="mobile" class="form-control input-sm underline" maxlength="20" value="${usersListForm.mobile}"/>
				</div>
				<%-- DEL BY LIUSHOUYI 合规检查 START
				<div class="form-group">
					<label>用户属性</label>
					<select name="userProperty" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${userPropertys }" var="property" begin="0" step="1" >
							<option value="${property.nameCd }"
								<c:if test="${property.nameCd eq usersListForm.userProperty}">selected="selected"</c:if>>
								<c:out value="${property.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				DEL BY LIUSHOUYI 合规检查 END--%>
				<div class="form-group">
					<label>开户状态</label>
					<select name="accountStatus" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${accountStatus }" var="acstatus" begin="0" step="1">
							<option value="${acstatus.nameCd }"
								<c:if test="${acstatus.nameCd eq usersListForm.accountStatus}">selected="selected"</c:if>>
								<c:out value="${acstatus.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>测评状态</label>
					<select name="evaluationStatus" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${evaluationStatus }" var="estatus" begin="0" step="1">
							<option value="${estatus.nameCd }"
								<c:if test="${estatus.nameCd eq usersListForm.evaluationStatus}">selected="selected"</c:if>>
								<c:out value="${estatus.nameCd}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>测评等级</label>
					<select name="evaluationType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${evaluationType }" var="etype" begin="0" step="1">
							<option value="${etype.nameCd }"
								<c:if test="${etype.nameCd eq usersListForm.evaluationType}">selected="selected"</c:if>>
								<c:out value="${etype.nameCd}"></c:out>
							</option>
						</c:forEach>
					</select>
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
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/evaluation/evaluationList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
