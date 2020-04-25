<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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


<shiro:hasPermission name="CAException:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="CA认证异常" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">CA认证异常</h1>
			<span class="mainDescription">CA认证异常</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<shiro:hasPermission name="CAException:VIEW">
							<li class="active"><a href="${webRoot}/exception/CAException/init">CA认证异常</a></li>
						</shiro:hasPermission>
					</ul>
					<div class="tab-content">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${CAExceptionForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${CAExceptionForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel">
									<i class="fa fa-search margin-right-10"></i> 
									<i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 模板列表一览 --%>
							<table id="equiList"
								class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">当前手机号</th>
										<th class="center">姓名</th>
										<th class="center">邮箱</th>
										<th class="center">身份证号</th>
										<th class="center">客户编号</th>
										<th class="center">CA认证状态</th>
										<th class="center">CA认证返回码</th>
										<th class="center">申请时间</th>
										<th class="center">异常原因</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="12">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((CAExceptionForm.paginatorPage - 1) * CAExceptionForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.userName }"></c:out></td>
													<td><c:out value="${record.mobile }"></c:out></td>
													<td><c:out value="${record.trueName }"></c:out></td>
													<td><c:out value="${record.email}"></c:out></td>
													<td><c:out value="${record.idNo}"></c:out></td>
													<td><c:out value="${record.customerId }"></c:out></td>
													<td><c:if test="${ record.status eq 'success'}"><c:out value="成功"/></c:if>
														<c:if test="${ record.status eq 'error'}"><c:out value="失败"/></c:if>
													</td>
													<td>
                                                        <c:if test="${record.code eq '1000'}"><c:out value="1000：操作成功"/></c:if>
                                                        <c:if test="${record.code eq '2001'}"><c:out value="2001：参数缺失或者不合法"/></c:if>
                                                        <c:if test="${record.code eq '2002'}"><c:out value="2002：业务异常，失败原因见异常原因"/></c:if>
                                                        <c:if test="${record.code eq '2003'}"><c:out value="2003：其他错误，请联系法大大"/></c:if>
                                                    </td>
													<td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
													<td><c:out value="${record.remark }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="CAException:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-userid="${record.userId }" data-toggle="tooltip" tooltip-placement="top" data-original-title="更新">
																	<i class="fa fa-pencil"></i>
																</a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<ul class="dropdown-menu pull-right dropdown-light"
																	role="menu">
																	<shiro:hasPermission name="CAException:MODIFY">
																		<li><a class="fn-Modify" data-userid="${record.userId }">更新</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${CAExceptionForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
			</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="userId" id="userId" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${CAExceptionForm.paginatorPage}" />
			<div class="form-group">
				<label>用户名:</label>
				<input type="text" name="userNameSrch" id="userNameSrch" class="form-control input-sm underline" value="${CAExceptionForm.userNameSrch}" />
			</div>
			<div class="form-group">
				<label>手机号:</label>
				<input type="text" name="mobileSrch" id="mobileSrch" class="form-control input-sm underline" value="${CAExceptionForm.mobileSrch}" />
			</div>
			<div class="form-group">
				<label>姓名:</label>
				<input type="text" name="trueNameSrch" id="trueNameSrch" class="form-control input-sm underline" value="${CAExceptionForm.trueNameSrch}" />
			</div>

			<div class="form-group">
				<label>状态:</label>
				<select name="statusSrch"  class="form-control underline form-select2">
					<option value="" selected="selected"></option>
					<option value="2001"
							<c:if test="${CAExceptionForm.statusSrch eq '2001'}">selected="selected"</c:if>>参数缺失或者不合法</option>
					<option value="2002"
							<c:if test="${CAExceptionForm.statusSrch eq '2002'}">selected="selected"</c:if>>业务异常，失败原因见异常原因</option>
					<option value="2003"
								<c:if test="${CAExceptionForm.statusSrch eq '2003'}">selected="selected"</c:if>>其他错误，请联系法大大</option>
				</select>
			</div>

			<div class="form-group">
				<label>申请时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="startTimeSrch" id="start-date-time" class="form-control underline" value="${CAExceptionForm.startTimeSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="endTimeSrch" id="end-date-time" class="form-control underline" value="${CAExceptionForm.endTimeSrch}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/certificateauthorityexception/certificateauthorityexceptionlist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
