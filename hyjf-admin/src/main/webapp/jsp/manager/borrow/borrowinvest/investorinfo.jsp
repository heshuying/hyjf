<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<%-- <shiro:hasPermission name="borrowinvest:VIEW"> --%>
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="出借明细" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">出借明细</h1>
			<span class="mainDescription">出借明细的说明。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
<%-- 							<div class="well">
								<c:set var="jspPrevDsb" value="${borrowInvestForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${borrowInvestForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="borrowinvest:EXPORT">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a> <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br /> --%>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<!-- <th class="center">序号</th> -->
										<th class="center" width="110">标的号</th>
										<th class="center">投标日期</th>
										<th class="center" width="160">订单号</th>
										<th class="center">交易金额</th>
										<th class="center">参考年回报率</th>
										<th class="center">预期收益</th>
										<th class="center">预期本息收益</th>
										<th class="center" width="70">实际收益</th>
										<th class="center"  width="95">实际收益符号</th>
										<th class="center">到期日</th>
										<th class="center">状态</th>
										<!-- <th class="center">操作</th> -->
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty detailList}">
											<tr>
												<td colspan="13">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${detailList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<%-- <td class="center">${(borrowInvestForm.paginatorPage - 1 ) * borrowInvestForm.paginator.limit + status.index + 1 }</td> --%>
													<td class="center"><c:out value="${borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.buyDate }"></c:out></td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.txAmount }"></c:out></td>
													<td class="center">
													<c:out value="${record.yield }"></c:out>
<%-- 													<c:set value="${ fn:split(record.yield, '.') }" var="planAprStr" />
														 <span>${planAprStr[0]}</span><span>%</span> --%>
													</td>
													<td class="center"><c:out value="${record.forIncome }"></c:out></td>
													<td class="center"><c:out value="${record.intTotal }"></c:out></td>
													<td class="center"><c:out value="${record.income }"></c:out></td>
													<td class="center"><c:out value="${record.incFlag }"></c:out></td>
													<td class="center">
														<fmt:formatDate value="${record.endDate}" pattern="yyyy-MM-dd" />
													</td>
													<td class="center">
													<%-- <c:out value="${record.state }"></c:out> --%>
														<c:choose>
															<c:when test="${record.state eq 1}">投标中</c:when>
															<c:when test="${record.state eq 2}">计息中</c:when>
															<c:when test="${record.state eq 4}">本息已返回</c:when>
															<c:when test="${record.state eq 8}">审核中</c:when>
															<c:when test="${record.state eq 9}">已撤销</c:when>
														</c:choose>
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
									<td align="center"><fmt:formatNumber value="${detailList[0].sumTxAmount }" pattern="#,##0.00#"/></td>
									<th>&nbsp;</th>
									<td align="center"><fmt:formatNumber value="${detailList[0].sumForIncome }" pattern="#,##0.00#"/></td>
									<td align="center"><fmt:formatNumber value="${detailList[0].sumIntTotal }" pattern="#,##0.00#"/></td>
									<td align="center"><fmt:formatNumber value="${detailList[0].sumIncome }" pattern="#,##0.00#"/></td>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
									</tr>
								<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
<%-- 							<shiro:hasPermission name="borrowinvest:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${borrowInvestForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission> --%>
							<br />
							<br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
<%-- 		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowinvest:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowInvestForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label> <input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowInvestForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>推荐人</label> <input type="text" name="referrerNameSrch" id="referrerNameSrch" class="form-control input-sm underline" value="${borrowInvestForm.referrerNameSrch}" />
				</div>
				<div class="form-group">
					<label>项目编号</label> <input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${borrowInvestForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>借款期限</label> <input type="text" name="borrowPeriod" id="borrowPeriod" class="form-control input-sm underline" value="${borrowInvestForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2" />
				</div>
				<!-- 				<div class="form-group"> -->
				<!-- 					<label>渠道</label> -->
				<!-- 					<select name="utmIdSrch" class="form-control underline form-select2"> -->
				<!-- 						<option value=""></option> -->
										<c:forEach items="${utmList }" var="utm" begin="0" step="1" varStatus="status">
											<option value="${utm.sourceId }"
												<c:if test="${utm.sourceId eq borrowInvestForm.utmIdSrch}">selected="selected"</c:if>>
												<c:out value="${utm.sourceName }"></c:out></option>
										</c:forEach>
				<!-- 					</select> -->
				<!-- 				</div> -->
				<div class="form-group">
					<label>出借方式</label> <select name="investType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${investTypeList }" var="investType" begin="0" step="1" varStatus="status">
							<option value="${investType.nameCd }" <c:if test="${investType.nameCd eq borrowInvestForm.investType}">selected="selected"</c:if>>
								<c:out value="${investType.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>还款方式</label> <select name="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
							<option value="${borrowStyle.nid }" <c:if test="${borrowStyle.nid eq borrowInvestForm.borrowStyleSrch}">selected="selected"</c:if>>
								<c:out value="${borrowStyle.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>操作平台</label> <select name="clientSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${clientList }" var="client" begin="0" step="1" varStatus="status">
							<option value="${client.nameCd }" <c:if test="${client.nameCd eq borrowInvestForm.clientSrch}">selected="selected"</c:if>>
								<c:out value="${client.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>出借时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${borrowInvestForm.timeStartSrch}" /> <i class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${borrowInvestForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute> --%>
		
		<%-- 对话框面板 (ignore) --%>
<%-- 		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute> --%>
		
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowinvest/investorinfo.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
<%-- </shiro:hasPermission> --%>
