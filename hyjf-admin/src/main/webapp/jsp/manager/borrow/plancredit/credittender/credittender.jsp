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
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<shiro:hasPermission name="planCreditTender:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇转让-承接信息" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇转让-承接信息</h1>
			<span class="mainDescription">汇转让-承接信息</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="planCreditTender:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${creditTenderForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${creditTenderForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="planCreditTender:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
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
										<th class="center">承接人</th>
										<th class="center">承接智投编号</th>
										<th class="center">智投订单号</th>
										<th class="center">承接订单号</th>
										<th class="center">出让人</th>
										<th class="center">债转编号</th>
										<th class="center">原项目编号</th>
										<th class="center">还款方式</th>
										<th class="center">承接本金</th>
										<th class="center">垫付利息</th>
										<th class="center">实际支付金额</th>
										<th class="center">服务费率</th>
										<th class="center">实际服务费</th>
										<th class="center">承接方式</th>
										<th class="center">承接时间</th>
										<th class="center">项目总期数</th>
										<th class="center">承接时所在期数</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="18">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(creditTenderForm.paginatorPage -1 ) * creditTenderForm.paginator.limit + status.index + 1 }</td>
													<td align="right"><c:out value="${record.assignUserName }"/></td>
													<td><c:out value="${record.assignPlanNid }"/></td>
													<td><c:out value="${record.assignPlanOrderId }"/></td>
													<td><c:out value="${record.assignOrderId }"/></td>
													<td align="right"><c:out value="${record.creditUserName }"/></td>
													<td align="right"><c:out value="${record.creditNid }"/></td>
													<td align="right"><c:out value="${record.borrowNid }"/></td>
													<td align="right"><c:out value="${record.repayStyleName }"/></td>
													<td align="right"><c:out value="${record.assignCapital }"/></td>
													<td align="right"><c:out value="${record.assignInterestAdvance }"/></td>
													<td align="right"><c:out value="${record.assignPay }"/></td>
													<td align="right"><c:out value="${record.serviceFeeRate }"/>%</td>
													<td align="right"><c:out value="${record.serviceFee }"/></td>
													<td class="center"><c:out value="${record.assignTypeName }"/></td>
													<td class="center"><c:out value="${record.assignTime }"/></td>
													<td class="center"><c:out value="${record.borrowPeriod }"/></td>
													<td class="center"><c:out value="${record.assignPeriod }"/></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="planCreditTender:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${creditTenderForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>

							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="planCreditTender:SEARCH">
				<input type="hidden" name="id" id="id" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${creditTenderForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>承接人</label>
					<input type="text" name="assignUserName" class="form-control input-sm underline" value="${creditTenderForm.assignUserName}" />
				</div>
				<div class="form-group">
					<label>承接智投编号</label>
					<input type="text" name="assignPlanNid" class="form-control input-sm underline" value="${creditTenderForm.assignPlanNid}" />
				</div>
				<div class="form-group">
					<label>承接智投订单号</label>
					<input type="text" name="assignPlanOrderId" class="form-control input-sm underline" value="${creditTenderForm.assignPlanOrderId}" />
				</div>
				<div class="form-group">
					<label>出让人</label>
					<input type="text" name="creditUserName" class="form-control input-sm underline" value="${creditTenderForm.creditUserName}" />
				</div>
				<div class="form-group">
					<label>债转编号</label>
					<input type="text" name="creditNid" class="form-control input-sm underline" value="${creditTenderForm.creditNid}" />
				</div>
				<div class="form-group">
					<label>原始项目编号</label>
					<input type="text" name="borrowNid" class="form-control input-sm underline" value="${creditTenderForm.borrowNid}" />
				</div>
				<div class="form-group">
					<label>还款方式</label>
					<select name="repayStyle" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStyleList }" var="repayStyle" begin="0" step="1">
							<option value="${repayStyle.nid }"
								<c:if test="${repayStyle.nid eq creditTenderForm.repayStyle}">selected="selected"</c:if>>
								<c:out value="${repayStyle.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>承接方式</label>
					<select name="assignType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${assignTypeList }" var="assignType" begin="0" step="1">
							<option value="${assignType.nameCd }"
								<c:if test="${assignType.nameCd eq creditTenderForm.assignType}">selected="selected"</c:if>>
								<c:out value="${assignType.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>承接时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="assignTimeStart" id="assignTimeStart" class="form-control underline" value="${creditTenderForm.assignTimeStart}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="assignTimeEnd" id="assignTimeEnd" class="form-control underline" value="${creditTenderForm.assignTimeEnd}" />
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
			<style type ="text/css">
				.search-classic{overflow-x:scroll;}
				#equiList{display:block;min-width:1580px;}
			</style>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/plancredit/credittender/credittender.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
