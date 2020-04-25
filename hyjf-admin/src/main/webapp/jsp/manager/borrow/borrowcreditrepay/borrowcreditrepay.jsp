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
<shiro:hasPermission name="borrowcredit:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇转让" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇转让还款信息</h1>
			<span class="mainDescription">汇转让汇转让汇转让汇转让的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="borrowcredit:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowcreditrepayForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowcreditrepayForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="borrow:EXPORT">
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
										<th class="center">债转编号</th>
										<th class="center">出让人</th>
										<th class="center">项目编号</th>
										<th class="center">订单号</th>
										<th class="center">应收本金</th>
										<th class="center">应收利息</th>
										<th class="center">应收本息</th>
										<th class="center">已收本息</th>
										<%-- upd by LSY START --%>
										<%-- <th class="center">管理费</th> --%>
										<th class="center">还款服务费</th>
										<%-- upd by LSY END --%>
										<th class="center">还款状态</th>
										<th class="center">债权承接时间</th>
										<th class="center">下次还款时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowcreditrepayForm.paginatorPage -1 ) * borrowcreditrepayForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.userName }"/></td>
													<td class="center">HZR<c:out value="${record.creditNid }"/></td>
													<td align="left"><c:out value="${record.creditUserName }"/></td>
													<td align="center"><c:out value="${record.bidNid }" /></td>
													<td align="center"><c:out value="${record.assignNid }" /></td>
													<td align="right"><c:out value="${record.assignCapital }"/></td>
													<td align="right"><c:out value="${record.assignInterest }" /></td>
													<td align="right"><c:out value="${record.assignAccount }" /></td>
													<td align="right"><c:out value="${record.assignRepayAccount }"/></td>
													<td align="right"><c:out value="${record.creditFee }"/></td>
													<c:if test="${record.status eq 0}">
														<td class="center">还款中</td>
													</c:if>
													<c:if test="${record.status eq 1}">
														<td class="center">已还款</td>
													</c:if>
													<td class="center"><c:out value="${record.addTime }"/></td>
													<td class="center"><c:out value="${record.assignRepayNextTime }"/></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="borrowcreditrepay:INFO">
																<a class="btn btn-transparent btn-xs tooltips fn-Info" data-assignnid="${record.assignNid }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="查看">查看</a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="borrowcreditrepay:INFO">
																		<li>
																			<a class="fn-Info" data-assignnid="${record.assignNid }">查看</a>
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
									<%-- add by LSY START --%>
									<tr>
										<th class="center">总计</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumCreditTender.sumAssignCapital }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCreditTender.sumAssignInterest }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCreditTender.sumAssignAccount }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCreditTender.sumAssignRepayAccount }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCreditTender.sumCreditFee }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
									<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="borrowcredit:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${borrowcreditrepayForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowcredit:SEARCH">
				<input type="hidden" name="assignNid" id="assignNid" />
				<input type="hidden" name="export" id="export" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowcreditrepayForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>承接人</label>
					<input type="text" name="usernameSrch" class="form-control input-sm underline" value="${borrowcreditrepayForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>出让人</label>
					<input type="text" name="creditUserNameSrch" class="form-control input-sm underline" value="${borrowcreditrepayForm.creditUserNameSrch}" />
				</div>
				<div class="form-group">
					<label>债转编号</label>
					<input type="text" name="creditNidSrch" class="form-control input-sm underline" value="${borrowcreditrepayForm.creditNidSrch}" />
				</div>
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="bidNidSrch" class="form-control input-sm underline" value="${borrowcreditrepayForm.bidNidSrch}" />
				</div>
				<div class="form-group">
					<label>订单号</label>
					<input type="text" name="assignNidSrch" class="form-control input-sm underline" value="${borrowcreditrepayForm.assignNidSrch}" />
				</div>
				<div class="form-group">
					<label>还款状态</label>
					<select name="statusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${borrowcreditrepayForm.statusSrch eq '0'}">selected="selected"</c:if>>还款中</option>
						<option value="1" <c:if test="${borrowcreditrepayForm.statusSrch eq '1'}">selected="selected"</c:if>>已还款</option>
					</select>
				</div>
				<div class="form-group">
					<label>下次还款时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="assignRepayNextTimeStartSrch" id="start-date-time" class="form-control underline" value="${borrowcreditrepayForm.assignRepayNextTimeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="assignRepayNextTimeEndSrch" id="end-date-time" class="form-control underline" value="${borrowcreditrepayForm.assignRepayNextTimeEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>债转承接时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="addTimeStartSrch" id="add-start-date-time" class="form-control underline" value="${borrowcreditrepayForm.addTimeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="addTimeEndSrch" id="add-end-date-time" class="form-control underline" value="${borrowcreditrepayForm.addTimeEndSrch}" />
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
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowcreditrepay/borrowcreditrepay.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
