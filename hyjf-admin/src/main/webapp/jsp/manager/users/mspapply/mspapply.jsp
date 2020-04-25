<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<jsp:useBean id="dateObject" class="java.util.Date" scope="page"></jsp:useBean>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="mspapply:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="安融反欺诈查询" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">安融反欺诈查询</h1>
			<span class="mainDescription">本功能可以增加修改删除。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<div class="tab-content">
						<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb"
									value="${mspapplyForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb"
									value="${mspapplyForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a
									class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a
									class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="mspapply:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="mspapply:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="导出列表">导出 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="检索条件" data-toggle-class="active"
									data-toggle-target="#searchable-panel"><i
									class="fa fa-search margin-right-10"></i> <i
									class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 列表一览 --%>
							<table id="equiList"
								class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
									<col />
									<col />
									<col style="width: 93px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">姓名</th>
										<th class="center">身份证号</th>
										<th class="center">操作人</th>
										<th class="center">查询时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty mspapplyForm.recordList}">
											<tr>
												<td colspan="6">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${mspapplyForm.recordList }" var="record"
												begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((mspapplyForm.paginatorPage - 1) * mspapplyForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.name }"></c:out></td>
													<td><c:out value="${record.identityCard }"></c:out></td>
													<td><c:out value="${record.createUser }"></c:out></td>
													<td><hyjf:datetime value="${record.createTime}"></hyjf:datetime></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="mspapply:MODIFY">

																	<a  class="btn btn-transparent btn-xs" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="下载"  href="downloadFile.do?applyId=${record.applyId}&ids=${record.id}" target="_blank">下载</a>
															</shiro:hasPermission>
															<c:if test="${record.shareIdentification == 0}">
																<shiro:hasPermission name="mspapply:MODIFY">
																	<a class="btn btn-transparent btn-xs fn-Modify"
																		data-id="${record.id }" data-toggle="tooltip"
																		tooltip-placement="top" data-original-title="共享">共享</a>
																</shiro:hasPermission>
															</c:if>
															<c:if test="${record.shareIdentification != 0}">
																<shiro:hasPermission name="mspapply:MODIFY">
																	<a class="btn btn-transparent btn-xs fn-Modify"
																		data-id="${record.id }" data-toggle="tooltip"
																		tooltip-placement="top" data-original-title="再次共享">再次共享</a>
																</shiro:hasPermission>
															</c:if>


														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button"
																	class="btn btn-primary btn-o btn-sm"
																	data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light"
																	role="menu">
																	<shiro:hasPermission name="mspapply:MODIFY">
																		<li><a href="downloadFile.do" target="_blank">下载</a>
																		<a class="companyInfo"
																			data-id="${record.id }">下载</a></li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="mspapply:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">共享</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="init" paginator="${mspapplyForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" id="ids" name="ids" value="${mspapplyForm.ids }" />
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${mspapplyForm.paginatorPage}" />
			<!-- 检索条件 -->

			<div class="form-group">
				<label>姓名</label> <input type="text" name="name"
					class="form-control input-sm underline" maxlength="20"
					value="${mspapplyForm.name}" />
			</div>
			<div class="form-group">
				<label>身份证号</label> <input type="text" name="identityCard"
					class="form-control input-sm underline" maxlength="20"
					value="${mspapplyForm.identityCard}" />
			</div>
			<div class="form-group">
				<label>操作人</label> <input type="text" name="createUser"
					class="form-control input-sm underline" maxlength="20"
					value="${mspapplyForm.createUser}" />
			</div>
			<div class="form-group">
				<label>时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="startCreate" id="start-date-time"
						class="form-control underline" value="${mspapplyForm.startCreate}" />
						<i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon"> <input type="text"
						name="endCreate" id="end-date-time" class="form-control underline"
						value="${mspapplyForm.endCreate}" />
					</span>
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
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript'
				src="${webRoot}/jsp/manager/users/mspapply/mspapply.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
