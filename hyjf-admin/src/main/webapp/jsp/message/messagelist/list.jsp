<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<c:set value="${fn:split('汇盈金服,消息中心,发送记录', ',')}" var="functionPaths"
	scope="request"></c:set>
<shiro:hasPermission name="smsLog:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="发送记录" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css"
			rel="stylesheet" media="screen">
		<link
			href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css"
			rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css"
			rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css"
			rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css"
			rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
			rel="stylesheet" media="screen">
		<style>
.table-striped .checkbox {
	width: 20px;
	margin-right: 0 !important;
	overflow: hidden
}
</style>
	</tiles:putAttribute>
	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">消息记录</h1>
		<span class="mainDescription">消息记录说明。</span>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb"
								value="${logForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb"
								value="${logForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a
								class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a
								class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
							<a class="btn btn-o btn-primary btn-sm hidden-xs "
								data-toggle="tooltip" data-placement="bottom" href="${pageContext.request.contextPath }/message/smsLog/timeinit"
								data-original-title="定时发送列表">定时发送列表 </a>
							<shiro:hasPermission name="smsLog:SINGLE">
							<a class="btn btn-o btn-primary btn-sm hidden-xs "
								data-toggle="tooltip" data-placement="bottom" href="${pageContext.request.contextPath }/message/message/timeinit"
								data-original-title="发送短信">发送短信 </a>
							</shiro:hasPermission>
							<shiro:hasPermission name="smsLog:MASS">
							<a class="btn btn-o btn-primary btn-sm hidden-xs "
								data-toggle="tooltip" data-placement="bottom" href="${pageContext.request.contextPath }/message/message/init"
								data-original-title="群发短信">群发短信 </a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="检索条件" data-toggle-class="active"
								data-toggle-target="#searchable-panel"><i
								class="fa fa-search margin-right-10"></i> <i
								class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 角色列表一览 --%>
						<table id="equiList"
							class="table table-striped table-bordered table-hover">
							<colgroup>
								<col class="con1" style="" />
								<col class="con0" style="" />
								<col class="con1" style="" />
								<col class="con0" style="" />
								<col class="con1" style="" />
								<col class="con0" style="" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">手机号码</th>
									<th class="center">短信类型</th>
									<shiro:hasPermission name="smsLog:SMS_CONTENT_SHOW">
									<th class="center">短信内容</th>
									</shiro:hasPermission>
									<th class="center">提交时间</th>
									<th class="center">状态</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty logs}">
										<tr>
											<td colspan="6">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${logs }" var="record" begin="0" step="1"
											varStatus="status">
											<tr>
												<td class="center">${(logForm.paginatorPage - 1 ) * logForm.paginator.limit + status.index + 1 }</td>
												<td><input type="text" value="<hyjf:asterisk value="${record.mobile }" permission="smsLog:HIDDEN_SHOW"></hyjf:asterisk>" readonly="readonly" size="15" style="border: 0px;" /></td>
												<td><c:out value="${record.type}"></c:out></td>
												<shiro:hasPermission name="smsLog:SMS_CONTENT_SHOW">
												<td><c:out value="${record.allContent}"></c:out></td>
												</shiro:hasPermission>
												<td class="center"><c:out value="${record.postString }"></c:out></td>
												<td>&nbsp;<c:if test="${record.status==0}">
														<c:out value="成功"></c:out>
													</c:if> <c:if test="${record.status==1}">
														<c:out value="失败"></c:out>
													</c:if> <c:if test="${record.status==-1}">
														<c:out value="内容长度越界"></c:out>
													</c:if> <c:if test="${record.status==17}">
														<c:out value="账户余额不足"></c:out>
													</c:if> <c:if test="${record.status==101}">
														<c:out value="网络故障"></c:out>
													</c:if> <c:if test="${record.status==305}">
														<c:out value="服务器错误"></c:out>
													</c:if> <c:if test="${record.status==306}">
														<c:out value="发送队列已满"></c:out>
													</c:if> <c:if test="${record.status==307}">
														<c:out value="号码不规范"></c:out>
													</c:if> <c:if test="${record.status==997}">
														<c:out value="超时短信未确定发送"></c:out>
													</c:if> <c:if test="${record.status==-9020}">
														<c:out value="号码格式不对"></c:out>
													</c:if></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page"
							action="init" paginator="${logForm.paginator}"></hyjf:paginator>
						<br /> <br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 边界面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="logid" id="logid" />
		<input type="hidden" name="paginatorPage" id="paginator-page"
			value="${logForm.paginatorPage}" />
		<div class="form-group">
			<label>状态</label> <select name="status"
				class="form-control underline form-select2">
			<option value="2"
					<c:if test="${logForm.status==2}">selected="selected"</c:if>><c:out
						value="--请选择--"></c:out></option>
				<option value="0"
					<c:if test="${logForm.status==0}">selected="selected"</c:if>><c:out
						value="成功"></c:out></option>
				<option value="1"
					<c:if test="${logForm.status==1}">selected="selected"</c:if>><c:out
						value="失败"></c:out></option>
			</select>
		</div>

		<div class="form-group">
			<label>手机号码</label> <input type="text" name="mobile" id="mobile"
				class="form-control input-sm underline" value="${logForm.mobile}" />
		</div>

		<div class="form-group">
			<label>发送时间</label>
			<div class="input-group input-daterange datepicker">
				<span class="input-icon"> <input type="text"
					name="post_time_begin" id="post_time_begin"
					class="form-control underline" value="${logForm.post_time_begin}" />
					<i class="ti-calendar"></i>
				</span> <span class="input-group-addon no-border bg-light-orange">~</span>
				<input type="text" name="post_time_end" id="post_time_end"
					class="form-control underline" value="${logForm.post_time_end}" />
			</div>
		</div>
			<div class="form-group">
			<label>短信类型</label> <input type="text" name="type" id="type"
				class="form-control input-sm underline" value="${logForm.type}" />
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript">
			var webRoot = "${webRoot}";
		</script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/moment.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/i18n/zh-CN.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/message/timemessagelist/timelist.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
</shiro:hasPermission>
