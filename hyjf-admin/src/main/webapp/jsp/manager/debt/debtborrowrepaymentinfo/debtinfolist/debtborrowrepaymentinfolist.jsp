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

<shiro:hasPermission name="debtborrowrepay:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款明细详情" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">还款明细详情</h1>
			<span class="mainDescription"><c:if
					test="${borrowRepaymentInfoListForm.actfrom=='0' }">以借款的角度查看还款信息,查询的是该借款下该期的每个人的还款信息。</c:if>
				<c:if test="${borrowRepaymentInfoListForm.actfrom=='1' }">以出借的角度查看还款信息,查询的是该出借下该人每一期每一笔还款信息。</c:if>
			</span>
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
								<c:set var="jspPrevDsb"
									value="${borrowRepaymentInfoListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb"
									value="${borrowRepaymentInfoListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a
									class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a
									class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>

								<shiro:hasPermission name="debtborrowrepay:EXPORT">
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
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
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">项目编号</th>
										<th class="center">还款期数</th>
										<th class="center">出借人</th>
										<th class="center">授权服务金额</th>
										<th class="center">应还本金</th>
										<th class="center">应还利息</th>
										<th class="center">应还本息</th>
										<th class="center">管理费</th>
										<th class="center">还款状态</th>
										<th class="center">应还日期</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="11">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0"
												step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((borrowRepaymentInfoListForm.paginatorPage - 1) * borrowRepaymentInfoListForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td>第<c:out value="${record.recoverPeriod }"></c:out>期
													</td>
													<td><c:out value="${record.recoverUserName }"></c:out></td>
													<td align="right"><fmt:formatNumber
															value="${record.recoverTotal }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber
															value="${record.recoverCapital }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber
															value="${record.recoverInterest }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber
															value="${record.recoverAccount }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber
															value="${record.recoverFee }" pattern="#,##0.00#" /></td>
													<td class="center"><c:if test="${record.status==0 }">
													未还款
													</c:if> <c:if test="${record.status==1 }">
													已还款
													</c:if></td>
													<td class="center"><c:out
															value="${record.recoverLastTime }"></c:out></td>
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
										<td align="right"><fmt:formatNumber
												value="${sumObject.recoverCapital }" pattern="#,##0.00#" /></td>
										<td align="right"><fmt:formatNumber
												value="${sumObject.recoverInterest }" pattern="#,##0.00#" /></td>
										<td align="right"><fmt:formatNumber
												value="${sumObject.recoverAccount }" pattern="#,##0.00#" /></td>
										<td align="right"><fmt:formatNumber
												value="${sumObject.recoverFee }" pattern="#,##0.00#" /></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="init"
								paginator="${borrowRepaymentInfoListForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${borrowRepaymentInfoListForm.paginatorPage}" />
			<input type="hidden" name="nid" id="nid"
				class="form-control input-sm underline"
				value="${borrowRepaymentInfoListForm.nid}" />
			<!-- 判断页面从哪来,Plan或Info -->
			<input type="hidden" name="actfrom" id="actfrom"
				value="${borrowRepaymentInfoListForm.actfrom}" />
			<div class="form-group">
				<label>项目编号</label> <input type="text" name="borrowNid"
					id="borrowNid" class="form-control input-sm underline"
					value="${borrowRepaymentInfoListForm.borrowNid}" />
			</div>
			<div class="form-group">
				<label>借款期数</label> <input type="text" name="recoverPeriod"
					id="recoverPeriod" class="form-control input-sm underline"
					value="${borrowRepaymentInfoListForm.recoverPeriod}" />
			</div>
			<div class="form-group">
				<label>出借人</label> <input type="text" name="recoverUserName"
					class="form-control input-sm underline"
					value="${borrowRepaymentInfoListForm.recoverUserName}" />
			</div>
			<div class="form-group">
				<label>还款状态</label> <select name="status"
					class="form-control underline form-select2">
					<option value="">全部</option>
					<option value="0"
						<c:if test="${borrowRepaymentInfoListForm.status==0}">selected="selected"</c:if>>未还款</option>
					<option value="1"
						<c:if test="${borrowRepaymentInfoListForm.status==1}">selected="selected"</c:if>>已还款</option>

				</select>
			</div>
			<div class="form-group">
				<label>应还日期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="recoverTimeStartSrch" id="recoverTimeStartSrch"
						class="form-control underline"
						value="${borrowRepaymentInfoListForm.recoverTimeStartSrch}" /> <i
						class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="recoverTimeEndSrch"
						id="recoverTimeEndSrch" class="form-control underline"
						value="${borrowRepaymentInfoListForm.recoverTimeEndSrch}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/debt/debtborrowrepaymentinfo/debtinfolist/debtborrowrepaymentinfolist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
