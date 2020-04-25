<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<c:set var="searchAction" value="#" scope="request"></c:set>
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="新增申请" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">	
		<form id="mainForm" method="post" role="form">
			<input type="hidden" name="infopaginatorPage" id="paginator-page" value="${applyagreementForm.infopaginatorPage}" />
			<div class="row bg-white">
				<div class="col-sm-12">
					<div class="panel panel-white">
						<div class="panel-body panel-table">
							<%-- 检索面板 --%>
							<div id="searchable-panel-clone" class="perfect-scrollbar">
								<form id="borrowcreditrepayForm" method="post">
									<div class="modal-body">
										<input type="hidden" name="ids" id="ids" />
										<div class="form-group">
											<label>项目编号-${success}</label>
											<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${applyagreementForm.borrowNidSrch}" />
										</div>
										<div class="form-group">
											<label>垫付时间</label>
											<div class="input-group input-daterange datepicker">
												<span class="input-icon">
													<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${applyagreementForm.timeStartSrch}" />
													<i class="ti-calendar"></i> </span>
												<span class="input-group-addon no-border bg-light-orange">~</span>
												<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${applyagreementForm.timeEndSrch}" />
											</div>
										</div>
										<div class="modal-footer">
											<button class="btn btn-sm btn-primary btn-o fn-ClearForm" type="button"><i class="fa fa-undo"></i> 清 空</button>
											<button class="btn btn-sm btn-primary btn-o fn-Reset1" type="reset"><i class="fa fa-undo"></i> 重 置</button>
											<button class="btn btn-sm btn-primary btn-o fn-Search" type="button"><i class="fa fa-search"></i> 检 索</button>
										</div>
									</div>
								</form>
							</div>
							<div class="well" id="add" style="display:none">
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										data-toggle="tooltip" data-placement="bottom" data-original-title="申请协议 ">申请协议 <i class="fa fa-plus"></i></a>
							</div>
							<br/>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="hidden-xs center">
											<div align="left" class="checkbox clip-check check-primary checkbox-inline"
													data-toggle="tooltip" tooltip-placement="top" data-original-title="选择所有行">
												<input type="checkbox" id="checkall">
												<label for="checkall"></label>
											</div>
										</th>
										<th class="center">序号</th>
										<th class="center">担保机构</th>
										<th class="center">项目编号</th>
										<th class="center">资产来源</th>
										<th class="center">智投编号</th>
										<th class="center">期数</th>
										<th class="center">垫付总额</th>
										<th class="center">借款人</th>
										<th class="center">垫付时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="14">请根据项目编号查询</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="hidden-xs center">
														<div align="left" class="checkbox clip-check check-primary checkbox-inline">
															<c:if test="${record.applyagreements eq 0}">
																<input type="checkbox" class="listCheck" name="listcheckbox" id="row${status.index }" value="${record.borrowNid }_${record.repayPeriod }">
																<label for="row${status.index }"></label>
															</c:if>
														</div>
													</td>
													<td class="center">${(applyagreementForm.infopaginatorPage -1 ) * applyagreementForm.infopaginator.limit + status.index + 1 }</td>
													<td><c:out value="${record.repayUsername }"/></td>
													<td>
														<c:out value="${record.borrowNid }"/>
													</td>
													<td><c:out value="${record.borrowProjectSource }"/></td>
													<td><c:out value="${record.nid }"/></td>
													<td><c:out value="${record.repayPeriod }"/></td>
													<td align="right"><c:out value="${record.repayCapital }"/></td>
													<td align="right"><c:out value="${record.userName }"/></td>
													<td align="right"><c:out value="${record.repayYseTime }"/></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="infoAction" paginator="${applyagreementForm.infopaginator}"></hyjf:paginator>
							<br/><br/>
							<div class="form-group margin-bottom-0" align="center">
								<div class="col-sm-offset-2 col-sm-6">
									<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 关闭</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</tiles:putAttribute>																			
	<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/applyagreement/borrowrepay_info.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
