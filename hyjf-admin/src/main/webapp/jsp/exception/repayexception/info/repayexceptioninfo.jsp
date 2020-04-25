<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="repayexception:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款异常明细" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">还款异常明细</h1>
			<span class="mainDescription">以出借的角度查看还款信息,查询的是每个项目下每一个出借人每一笔的还款信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<hyjf:message key="delete_error"></hyjf:message>
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${repayexceptioninfoForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${repayexceptioninfoForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-toRepay" data-id="${repayexceptioninfoForm.borrowNidParam }"  data-period="${repayexceptioninfoForm.borrowPeriodParam }"
										data-toggle="tooltip" data-placement="bottom" data-original-title="返回到还款掉单页面">返回 <i class="fa fa-mail-reply"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">项目编号</th>
										<th class="center">借款人</th>
										<th class="center">项目名称</th>
										<th class="center">当前期数</th>
										<th class="center">出借人</th>
										<th class="center">应还本息</th>
										<th class="center">应还管理费</th>
										<th class="center">实还本息</th>
										<th class="center">还款状态</th>
										<th class="center">实际还款日</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="10">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0"
												step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((repayexceptioninfoForm.paginatorPage - 1) * repayexceptioninfoForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td><c:out value="${record.borrowUserName }"></c:out></td>
													<td><c:out value="${record.borrowName }"></c:out></td>
													<td class="center">
														<c:if test="${record.recoverPeriod ne ''}">
														<span class="text-green">第 <strong><c:out value="${record.recoverPeriod }"></c:out></strong> 期</span>
														</c:if>

														<c:if test="${record.recoverPeriod eq ''}">
														-
														</c:if>
													</td>
													<td><c:out value="${record.tenderUserName }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.recoverAccount }" pattern="#,##0.00#"/></td>
													<td align="right"><fmt:formatNumber value="${record.recoverFee }" pattern="#,##0.00#"/></td>
													<td align="right"><fmt:formatNumber value="${record.recoverAccountYes }" pattern="#,##0.00#"/></td>
													<td class="center">
														<c:if test="${record.recoverStatus==0 }">
														还款中
														</c:if>
														<c:if test="${record.recoverStatus==1 }">
														已还款
														</c:if>
													</td>
													<td class="center"><c:out value="${record.recoverYestime }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
									<tr>
										<th class="center">总计</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumObject.recoverAccount }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.recoverFee }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.recoverAccountYes }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="init" paginator="${repayexceptioninfoForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="borrowNidHidden" id="borrowNidHidden" value="" />
			<input type="hidden" name="periodNowHidden" id="periodNowHidden" value="" />
			<input type="hidden" name="monthType" id="monthType" value="${repayexceptioninfoForm.monthType}" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${repayexceptioninfoForm.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>项目编号</label>
				<input type="text" name="borrowNidSrch" class="form-control input-sm underline" value="${repayexceptioninfoForm.borrowNidSrch}" />
			</div>
			<div class="form-group">
				<label>项目名称</label>
				<input type="text" name="borrowNameSrch" class="form-control input-sm underline" value="${repayexceptioninfoForm.borrowNameSrch}" />
			</div>
			<div class="form-group">
				<label>借款人</label>
				<input type="text" name="borrowUserNameSrch" class="form-control input-sm underline" value="${repayexceptioninfoForm.borrowUserNameSrch}" />
			</div>
			<div class="form-group">
				<label>出借人</label>
				<input type="text" name="tenderUserNameSrch" class="form-control input-sm underline" value="${repayexceptioninfoForm.tenderUserNameSrch}" />
			</div>
			<div class="form-group">
				<label>还款状态</label>
				<select name="recoverStatusSrch"
					class="form-control underline form-select2">
					<option value="">全部</option>
					<option value="0" <c:if test="${repayexceptioninfoForm.recoverStatusSrch != '' && repayexceptioninfoForm.recoverStatusSrch==0}">selected="selected"</c:if>>还款中</option>
					<option value="1" <c:if test="${repayexceptioninfoForm.recoverStatusSrch != '' && repayexceptioninfoForm.recoverStatusSrch==1}">selected="selected"</c:if>>已还款</option>
				</select>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/repayexception/info/repayexceptioninfo.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>

