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

<shiro:hasPermission name="manualreverse:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="手动冲正列表" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">手动冲正</h1>
			<span class="mainDescription">本功能可以对异常交易进行调账处理，调账可以调增、调减。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="manualreverse:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb"
										value="${manualreverseForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb"
										value="${manualreverseForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-AutoM"
										   data-toggle="tooltip" data-placement="bottom" data-original-title="手动冲正">手动触发MQ发送 <i class="fa fa-plus"></i></a>
									<shiro:hasPermission name="manualreverse:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										   data-toggle="tooltip" data-placement="bottom" data-original-title="手动冲正">手动冲正 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									   data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
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
										<th class="center">原交易流水号</th>
										<th class="center">交易时间</th>
										<th class="center">用户名</th>
										<th class="center">电子账号</th>
										<th class="center">资金托管平台</th>
										<th class="center">收支类型</th>
										<th class="center">交易类型</th>
										<th class="center">操作金额</th>
										<th class="center">操作状态</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty manualreverseForm.recordList}">
											<tr>
												<td colspan="11">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${manualreverseForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">
													<c:out value="${status.index+((manualreverseForm.paginatorPage - 1) * manualreverseForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.seqNo }"></c:out></td>
													<%--<td class="center"><c:out value="${record.txTime }"></c:out></td> --%>
													<td class="center"><fmt:formatDate value="${record.txTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
													<td class="center"><c:out value="${record.userName}"></c:out></td>
													<td class="center"><c:out value="${record.accountId}"></c:out></td>
													<td class="center">
														<c:if test="${record.isBank== ''}"></c:if> 
														<c:if test="${record.isBank== '0'}">汇付</c:if> 
														<c:if test="${record.isBank== '1'}">江西银行</c:if>
													</td>
													<td class="center">
														<c:if test="${record.type== ''}"></c:if> 
														<c:if test="${record.type== '0'}">收入</c:if> 
														<c:if test="${record.type== '1'}">支出</c:if>
													</td>
													<td class="center">
														<c:if test="${record.type== ''}"></c:if> 
														<c:if test="${record.type== '0'}">调帐调增</c:if> 
														<c:if test="${record.type== '1'}">调帐调减</c:if>
													</td>
													<td class="center"><fmt:formatNumber value="${record.amount}" type="number" pattern="#,##0.00#" /></td>
													<td class="center">
														<c:if test="${record.status== ''}"></c:if> 
														<c:if test="${record.status== '0'}">成功</c:if> 
														<c:if test="${record.status== '1'}">失败</c:if>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction"
								paginator="${manualreverseForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${manualreverseForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>原交易流水号</label> 
				<input type="text" name="seqNoSrch" class="form-control input-sm underline" value="${manualreverseForm.seqNoSrch}" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="6" size="6"/>
			</div>
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="userNameSrch" class="form-control input-sm underline"  maxlength="20" value="${manualreverseForm.userNameSrch}" />
			</div>
			<div class="form-group">
				<label>电子账号</label> 
				<input type="text" name="accountIdSrch" class="form-control input-sm underline"  maxlength="19" value="${manualreverseForm.accountIdSrch}" />
			</div>
			<div class="form-group">
				<label>交易时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="txTimeStartSrch" id="start-date-time" class="form-control underline" value="${manualreverseForm.txTimeStartSrch}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="txTimeEndSrch" id="end-date-time" class="form-control underline" value="${manualreverseForm.txTimeEndSrch}" />
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
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/exception/manualreverseexception/manualreverseexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
