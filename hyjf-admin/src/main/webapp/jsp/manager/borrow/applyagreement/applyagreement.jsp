<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<c:set var="searchAction" value="" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<shiro:hasPermission name="applyagreement:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇转让" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">垫付协议管理</h1>
			<span class="mainDescription">垫付协议管理。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="applyagreement:SEARCH">
								<div class="well">
									<input type="hidden" id="success" value="${success}" />
									<c:set var="jspPrevDsb" value="${applyagreementForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${applyagreementForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="applyagreement:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
												data-toggle="tooltip" data-placement="bottom" data-original-title="新增申请 ">新增申请 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
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
										<th class="center">项目编号</th>
										<th class="center">申请期数</th>
										<th class="center">申请人</th>
										<th class="center">申请时间</th>
										<th class="center">协议份数</th>
										<th class="center">状态</th>
										<th class="center">更新时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="9">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(applyagreementForm.paginatorPage -1 ) * applyagreementForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"/></td>
													<td class="center"><c:out value="${record.repayPeriod }"/></td>
													<td class="center"><c:out value="${record.applyUserName }"/></td>
													<td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
													<td align="left"><c:out value="${record.agreementNumber }"/></td>
													<c:if test="${record.status eq 1}">
														<td class="center">申请中</td>
													</c:if>
													<c:if test="${record.status eq 3}">
														<td class="center">申请成功</td>
													</c:if>
													<c:if test="${record.status eq 2}">
														<td class="center">申请中</td>
													</c:if>
													<td class="center"><hyjf:datetime value="${record.updateTime }"></hyjf:datetime></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
														<!-- 申请状态 0 全部；1申请失败(hyjf_tender_agreement没有记录)：2申请中；3申请成功 -->
																<c:if test="${record.status eq 1}">
																	<%-- <a class="fn-Delete" data-id="${record.id }">删除</a> --%>
																		<a class="btn btn-transparent btn-xs tooltips fn-PdfSign" data-borrownid="${record.borrowNid}" data-repayperiod="${record.repayPeriod}" data-toggle="tooltip" data-placement="top" data-original-title="PDF签署"><i class="fa ti-layers-alt"></i></a>
																</c:if>
																<c:if test="${record.status eq 3}">
																	<a href="${webRoot}/manager/borrow/applyagreement/downloadAction.do?nid=${record.borrowNid }&period=${record.repayPeriod }&status=1" data-assignnid="${record.id }">脱敏协议包下载</a>&nbsp;<a href="${webRoot}/manager/borrow/applyagreement/downloadAction.do?nid=${record.borrowNid }&period=${record.repayPeriod }&status=0" data-assignnid="${record.id }">原始协议包下载 </a>
																</c:if>
																<c:if test="${record.status eq 2}">
																	<a class="btn btn-transparent btn-xs tooltips fn-PdfSign" data-borrownid="${record.borrowNid}" data-repayperiod="${record.repayPeriod}" data-toggle="tooltip" data-placement="top" data-original-title="PDF签署"><i class="fa ti-layers-alt"></i></a>
																</c:if>
														</div>
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${applyagreementForm.paginator}"></hyjf:paginator>
							
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="applyagreement:SEARCH">
				 <input type="hidden" name="ids" id="ids" />
			 <input type="hidden" name="borrowNidHidden" id="borrowNidHidden" />
	         <input type="hidden" name="repayperiodHidden" id="repayperiodHidden" />
	         <input type="hidden" name="paginatorPage" id="paginator-page" value="${applyagreementForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${applyagreementForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>申请期数</label>
					<input type="text" name="borrowPeriod" id="borrowPeriod" class="form-control input-sm underline" value="${applyagreementForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2"  />
				</div>
				<div class="form-group">
					<label>申请时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${applyagreementForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${applyagreementForm.timeEndSrch}" />
					</div>
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
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/applyagreement/applyagreement.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
