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

<shiro:hasPermission name="loancover:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款盖章用户" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">借款盖章用户</h1>
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
									value="${loancoverForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb"
									value="${loancoverForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a
									class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a
									class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="loancover:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										data-toggle="tooltip" data-placement="bottom"
										data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="loancover:EXPORT">
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
										<th class="center">手机号</th>
										<th class="center">名称</th>
										<th class="center">证件号码</th>
										<th class="center">用户类型</th>
										<th class="center">邮箱</th>
										<th class="center">客户编号</th>
										<th class="center">状态</th>
										<th class="center">状态码</th>
										<th class="center">添加时间</th>
										<th class="center">申请时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty loancoverForm.recordList}">
											<tr>
												<td colspan="12">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${loancoverForm.recordList }" var="record"
												begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((loancoverForm.paginatorPage - 1) * loancoverForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.mobile }"></c:out></td>
													<td><c:out value="${record.name }"></c:out></td>
													<td><c:out value="${record.idNo }"></c:out></td>
													<td><c:out
															value="${record.idType == '1' ? '企业' : '个人'  }"></c:out></td>
													<td><c:out value="${record.email }"></c:out></td>
													<td><c:out value="${record.customerId }"></c:out></td>
													<td><c:if test="${record.status == null}">未认证</c:if> <c:if
															test="${record.status == 'success'}">认证成功</c:if> <c:if
															test="${record.status == 'error'}">认证失败</c:if></td>
													<td><c:out value="${record.code }"></c:out></td>
													<td><hyjf:datetime value="${record.createTime}"></hyjf:datetime></td>
													<td><hyjf:datetime value="${record.updateTime}"></hyjf:datetime></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<c:if test="${record.status != 'success'}">
																<shiro:hasPermission name="loancover:MODIFY">
																	<a class="btn btn-transparent btn-xs fn-RenZheng"
																		data-id="${record.id }" data-toggle="tooltip"
																		tooltip-placement="top" data-original-title="认证">认证</a>
																</shiro:hasPermission>
															</c:if>
															<shiro:hasPermission name="loancover:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="修改">修改</a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="init" paginator="${loancoverForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" id="ids" name="ids"
				value="${loancoverForm.ids }" />
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${loancoverForm.paginatorPage}" />
			<!-- 检索条件 -->

			<div class="form-group">
				<label>名称</label> <input type="text" name="name"
					class="form-control input-sm underline" maxlength="50"
					value="${loancoverForm.name}" />
			</div>
			<div class="form-group">
				<label>证件号码</label> <input type="text" name="idNo"
					class="form-control input-sm underline" maxlength="20"
					value="${loancoverForm.idNo}" />
			</div>
			<div class="form-group">
				<label>手机号</label> <input type="text" name="mobile"
					class="form-control input-sm underline" maxlength="20"
					value="${loancoverForm.mobile}" />
			</div>
			<div class="form-group">
				<label>状态</label> <select name="code" id="code"
					class="form-control underline form-select2">
					<option value=""></option>
					<option value="1000"
						<c:if test="${'1000' eq loancoverForm.code}">selected="selected"</c:if>>
						<c:out value="认证成功"></c:out>
					</option>
					<option value="2001"
						<c:if test="${'2001' eq loancoverForm.code}">selected="selected"</c:if>>
						<c:out value="认证失败"></c:out>
					</option>
					<option value="2002"
						<c:if test="${'2002' eq loancoverForm.code}">selected="selected"</c:if>>
						<c:out value="未认证"></c:out>
					</option>
				</select>
			</div>

			<div class="form-group">
				<label>时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="startCreate" id="start-date-time"
						class="form-control underline"
						value="${loancoverForm.startCreate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon"> <input type="text"
						name="endCreate" id="end-date-time" class="form-control underline"
						value="${loancoverForm.endCreate}" />
					</span>
				</div>
			</div>
			<div class="form-group">
				<label>客户编号</label> <input type="text" name="customerId" 
					class="form-control input-sm underline" maxlength="32"
					value="${loancoverForm.customerId}" />
			</div>
			<div class="form-group">
				<label>用户类型</label> <select name="idType" id="idType"
					class="form-control underline form-select2">
					<option value=""></option>
					<option value="0"
						<c:if test="${'0' eq loancoverForm.idType}">selected="selected"</c:if>>
						<c:out value="个人"></c:out>
					</option>
					<option value="1"
						<c:if test="${'1' eq loancoverForm.idType}">selected="selected"</c:if>>
						<c:out value="企业"></c:out>
					</option>

				</select>
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
				src="${webRoot}/jsp/manager/users/loancover/loancover.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
