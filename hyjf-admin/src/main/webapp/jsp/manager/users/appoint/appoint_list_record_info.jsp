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

 <shiro:hasPermission name="appoint:VIEW"> 
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="产品图片" />
		<style>
		 td{text-align:center;}
		</style>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${form.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
						<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
					</div>
					<br />
					<%-- 列表一览 --%>
					<div class="row">
						<%-- 产品管理列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">用户名</th>
									<th class="center">违约分值</th>
									<th class="center">累计积分</th>
									<th class="center">违约标的</th>
									<th class="center">违约金额</th>
									<th class="center">违约类型</th>
									<th class="center">操作时间</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty recordList}">
										<tr>
											<td colspan="7">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
									
									<c:forEach items="${recordList}" var="record"
											begin="0" step="1" varStatus="vs">
											<tr>
										<td>${vs.count }</td>
												<td>${record.username} </td>
												<td>${record.recod} </td>
												<td>${record.recodTotal} </td>
												<td>
												${record.recodNid}
												</td>
												<td>
												${record.recodMoney}
												</td>
												<td>
												<c:if test="${record.recodType eq 1}">
												金额不足
												</c:if>
												<c:if test="${record.recodType eq 0 and empty record.apointOrderId}">
												取消预约
												</c:if>
												<c:if test="${record.recodType eq 0 and not empty record.apointOrderId}">
												${record.recodRemark}
												</c:if>
												</td>
												<td>
												${record.addTime}
												</td>
											
										</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="appointlistrecord" paginator="${form.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>
	
	
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" /> 
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
				<div class="form-group">
				<label>用户名</label> 
			<input type="text" name="username" id="username" class="form-control input-sm underline"  maxlength="20" value="${form.username}" />
				</div>
				<div class="form-group">
				<label>项目编号</label> 
			<input type="text" name="recodNid" id="recodNid" class="form-control input-sm underline"  maxlength="20" value="${form.recodNid}" />
			</div>
			<div class="form-group">
				<label>操作时间</label>
					<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="addTime" id="addTime" class="form-control underline" value="${form.addTime}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="addTimeEnd" id="addTimeEnd" class="form-control underline" value="${form.addTimeEnd}" />
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
			<style>
            table tr td{text-align:center;}
            </style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/appoint/appoint_list_record_info.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
 </shiro:hasPermission> 
