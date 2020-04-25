<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="转让明细" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-datetimepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">	
		<form id="mainForm" method="post" role="form">
			<input type="hidden" name="creditNid" id="creditNid" value="${borrowcreditForm.creditNid}" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowcreditForm.paginatorPage}" />
			<div class="row bg-white">
				<div class="col-sm-12">
					<div class="panel panel-white">
						<div class="panel-body panel-table">
						<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">订单号</th>
										<th class="center">债转编号</th>
										<th class="center">项目编号</th>
										<th class="center">出让人</th>
										<th class="center">认购人</th>
										<th class="center">转让本金</th>
										<th class="center">转让价格</th>
										<th class="center">折让率</th>
										<th class="center">认购金额</th>
										<th class="center">垫付利息</th>
										<%-- upd by LSY START --%>
										<%-- <th class="center">服务费</th> --%>
										<th class="center">债转服务费</th>
										<%-- upd by LSY END --%>
										<th class="center">支付金额</th>
										<th class="center">认购时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowcreditForm.paginatorPage -1 ) * borrowcreditForm.paginator.limit + status.index + 1 }</td>
													<td><c:out value="${record.assignNid }"/></td>
													<td><c:out value="${record.creditNid }"/></td>
													<td><c:out value="${record.bidNid }"/></td>
													<td><c:out value="${record.creditUsername }"/></td>
													<td><c:out value="${record.username }"/></td>
													<td align="right"><c:out value="${record.assignCapital }"/></td>
													<td align="right"><c:out value="${record.assignCapitalPrice }"/></td>
													<td align="right"><c:out value="${record.creditDiscount }"/>%</td>
													<td align="right"><c:out value="${record.assignPrice }"/></td>
													<td align="right"><c:out value="${record.assignInterestAdvance }"/></td>
													<td align="right"><c:out value="${record.creditFee }"/></td>
													<td align="right"><c:out value="${record.assignPay }"/></td>
													<td class="center"><c:out value="${record.addTime }"/></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
									<%-- add by LSY START --%>
									<tr>
										<th class="center">总计</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumCreditInfo.sumAssignCapital }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCreditInfo.sumAssignCapitalPrice }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumCreditInfo.sumAssignPrice }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCreditInfo.sumAssignInterestAdvance }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCreditInfo.sumCreditFee }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCreditInfo.sumAssignPay }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
									</tr>
									<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="borrowcredit:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="infoAction" paginator="${borrowcreditForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
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
											
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowcredit/borrowcredit_info.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
