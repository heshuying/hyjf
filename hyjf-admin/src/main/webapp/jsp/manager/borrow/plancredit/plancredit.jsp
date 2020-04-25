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
<shiro:hasPermission name="planCredit:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇转让" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">转让记录</h1>
			<span class="mainDescription">汇添金计划清算转让债权。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="planCredit:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="planCredit:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
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
										<th class="center">出让人智投编号</th>
										<th class="center">出让人智投订单号</th>
										<th class="center">出让人</th>
										<th class="center">债转编号</th>
										<th class="center">项目编号</th>
										<th class="center">项目收益率</th>
										<th class="center">还款方式</th>
										<th class="center">债权本金</th>
										<th class="center">预期实际收益率</th>
										<th class="center">清算手续费率</th>
										<th class="center">实际服务费</th>
										<th class="center">收到转让本金</th>
										<th class="center">收到垫付利息</th>
										<th class="center">出让人实际到账金额</th>
										<th class="center">最新清算时间</th>
										<th class="center">转让状态</th>
										<th class="center">还款状态</th>
										<th class="center">项目总期数</th>
										<th class="center">承接原项目所在期数</th>
										<th class="center">清算时所在期数</th>
										<th class="center">当期应还款时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="24">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(planCreditForm.paginatorPage -1 ) * planCreditForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.planNid }"/></td>
													<td class="center"><c:out value="${record.planOrderId }"/></td>
													<td align="right"><c:out value="${record.userName }"/></td>
													<td class="center"><c:out value="${record.creditNid }"/></td>
													<td class="center"><c:out value="${record.borrowNid }"/></td>
													<td align="right"><c:out value="${record.borrowApr }" />%</td>
													<td align="right"><c:out value="${record.repayStyleName }" /></td>
													<td align="right"><c:out value="${record.creditCapital }"/></td>
													<td align="right"><c:out value="${record.actualApr }" />%</td>
													<td align="right"><c:out value="${record.serviceFeeRate }" />%</td>
													<td class="center"><c:out value="${record.serviceFee }"/></td>
													<td class="center"><c:out value="${record.assignCapital }"/></td>
													<td class="center"><c:out value="${record.assignAdvanceInterest }"/></td>
													<td class="center"><c:out value="${record.accountReceive }"/></td>
													<td class="center"><c:out value="${record.liquidatesTime }"/></td>
													<td class="center"><c:out value="${record.creditStatusName }"/></td>
													<td class="center"><c:out value="${record.repayStatusName }"/></td>
													<td class="center"><c:out value="${record.borrowPeriod }"/></td>
													<td class="center"><c:out value="${record.assignPeriod }"/></td>
													<td class="center"><c:out value="${record.liquidatesPeriod }"/></td>
													<td class="center"><c:out value="${record.repayNextTime }"/></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="planCredit:INFO">
																<a class="btn btn-transparent btn-xs tooltips fn-Info" data-creditnid="${record.creditNid }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="查看">查看</a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="planCredit:INFO">
																		<li>
																			<a class="fn-Info" data-creditnid="${record.creditNid }">查看</a>
																		</li>
																	</shiro:hasPermission>
																</ul>
															</div>
														</div>
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="planCredit:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${planCreditForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="planCredit:SEARCH">
				<input type="hidden" name="export" id="export" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${planCreditForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="planNid" class="form-control input-sm underline" value="${planCreditForm.planNid}" />
				</div>
				<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="planOrderId" class="form-control input-sm underline" value="${planCreditForm.planOrderId}" />
				</div>
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" class="form-control input-sm underline" value="${planCreditForm.userName}" />
				</div>
				<div class="form-group">
					<label>债转编号</label>
					<input type="text" id="creditNid" name="creditNid" class="form-control input-sm underline" value="${planCreditForm.creditNid}" />
				</div>
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNid" class="form-control input-sm underline" value="${planCreditForm.borrowNid}" />
				</div>
				<div class="form-group">
					<label>还款方式</label>
					<select name="repayStyle" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStyleList }" var="repayStyle" begin="0" step="1">
							<option value="${repayStyle.nid }"
								<c:if test="${repayStyle.nid eq planCreditForm.repayStyle}">selected="selected"</c:if>>
								<c:out value="${repayStyle.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>转让状态</label>
					<select name="creditStatus" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${creditStatusList }" var="creditStatus" begin="0" step="1">
							<option value="${creditStatus.nameCd }"
								<c:if test="${creditStatus.nameCd eq planCreditForm.creditStatus}">selected="selected"</c:if>>
								<c:out value="${creditStatus.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>还款状态</label>
					<select name="repayStatus" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${repayStatusList }" var="repayStatus" begin="0" step="1">
							<option value="${repayStatus.nameCd }"
								<c:if test="${repayStatus.nameCd eq planCreditForm.repayStatus}">selected="selected"</c:if>>
								<c:out value="${repayStatus.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>清算时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="liquidatesTimeStart" id="liquidatesTimeStart" class="form-control underline" value="${planCreditForm.liquidatesTimeStart}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="liquidatesTimeEnd" id="liquidatesTimeEnd" class="form-control underline" value="${planCreditForm.liquidatesTimeEnd}" />
					</div>
				</div>
				<div class="form-group">
					<label>还款时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="repayNextTimeStart" id="repayNextTimeStart" class="form-control underline" value="${planCreditForm.repayNextTimeStart}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="repayNextTimeEnd" id="repayNextTimeEnd" class="form-control underline" value="${planCreditForm.repayNextTimeEnd}" />
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
				#equiList{display:block;min-width:2000px;}
			</style>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/plancredit/plancredit.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
