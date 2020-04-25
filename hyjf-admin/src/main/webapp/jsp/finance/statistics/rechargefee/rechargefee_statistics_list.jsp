<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="refeestatistic:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="充值手续费统计" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">充值手续费垫付统计</h1>
			<span class="mainDescription">这里添加充值手续费垫付统计描述。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		
		 <div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="merchantaccountlist:VIEW">
			      		<li><a href="${webRoot}/finance/merchant/account/accountList">账户信息</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="web:VIEW">
			      		<li><a href="${webRoot}/finance/web/web_list">网站收支</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="merchanttransferlist:VIEW">
			      		<li><a href="${webRoot}/finance/merchant/transfer/transferList">子账户间转账</a></li>
			      	</shiro:hasPermission>
	   				<shiro:hasPermission name="refeestatistic:VIEW">
						<li  class="active"><a href="${webRoot}/finance/statistics/rechargefee/statistics">充值手续费统计</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="couponrepaymonitor:VIEW">
						<li><a href="${webRoot}/finance/couponrepaymonitor/chart">加息券还款统计</a></li>
					</shiro:hasPermission>
			    </ul>
		
				<div class="tab-content">
					<div class="tab-pane fade in active">

							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${rechargeFeeStatisticsForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${rechargeFeeStatisticsForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" tooltip-placement="top" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" tooltip-placement="top" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" tooltip-placement="top" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i>
								</a>
								<shiro:hasPermission name="refeestatistic:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" tooltip-placement="top" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i>
									</a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								<span class = "dateStyle"> 更新时间 ：${rechargeFeeStatisticsForm.updateTimeView}</span>
							</div>
							<br />
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">日期</th>
										<th class="center">充值金额</th>
										<th class="center">快捷充值</th>
										<th class="center">个人网银</th>
										<th class="center">企业网银</th>
										<th class="center">平台垫付手续费</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty rechargeFeeStatisticsForm.recordList}">
											<tr>
												<td colspan="7">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${rechargeFeeStatisticsForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((rechargeFeeStatisticsForm.paginatorPage - 1) * rechargeFeeStatisticsForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.statDate }"></c:out></td>
													<td class="right"><fmt:formatNumber value="${record.rechargeAmount}" type="number" pattern="#,##0.00#" /></td>
													<td class="right"><fmt:formatNumber value="${record.quickAmount}" type="number" pattern="#,##0.00#" /></td>
													<td class="right"><fmt:formatNumber value="${record.personalAmount}" type="number" pattern="#,##0.00#" /></td>
													<td class="right"><fmt:formatNumber value="${record.comAmount}" type="number" pattern="#,##0.00#" /></td>
													<td class="right"><fmt:formatNumber value="${record.fee}" type="number" pattern="#,##0.00#" /></td>
												</tr>
											</c:forEach>
											
										<tr>
											<td class="center">总计</td>
											<td class="center"></td>
											<td class="left" style="color:red!important"><fmt:formatNumber value="${rechargeFeeStatisticsForm.rechargeAmountSum}" type="number" pattern="#,##0.00" /></td>
											<td class="left" style="color:red!important"><fmt:formatNumber value="${rechargeFeeStatisticsForm.quickAmountSum}" type="number" pattern="#,##0.00" /></td>
											<td class="left" style="color:red!important"><fmt:formatNumber value="${rechargeFeeStatisticsForm.personalAmountSum}" type="number" pattern="#,##0.00" /></td>
											<td class="left" style="color:red!important"><fmt:formatNumber value="${rechargeFeeStatisticsForm.comAmountSum}" type="number" pattern="#,##0.00" /></td>
											<td class="left" style="color:red!important"><fmt:formatNumber value="${rechargeFeeStatisticsForm.feeSum}" type="number" pattern="#,##0.00" /></td>
										</tr>
											
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${rechargeFeeStatisticsForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>


		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${rechargeFeeStatisticsForm.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>日期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startDateSrch" id="start-date-time" class="form-control underline" value="${rechargeFeeStatisticsForm.startDateSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endDateSrch" id="end-date-time" class="form-control underline" value="${rechargeFeeStatisticsForm.endDateSrch}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.dateStyle{display:block;float:right;margin-right:20px;line-height:30px}
			</style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/finance/statistics/rechargefee/rechargefee_statistics_list.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
