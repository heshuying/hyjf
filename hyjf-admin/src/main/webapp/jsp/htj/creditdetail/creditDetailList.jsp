<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="creditdetail:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="债权明细" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">债权明细</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="creditdetail:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${creditForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${creditForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<shiro:hasPermission name="creditdetail:EXPORT">
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
										<th class="center">智投编号</th>
										<th class="center">智投订单号</th>
										<th class="center">出借/承接订单号</th>
										<th class="center">用户名</th>
										<th class="center">债转编号</th>
										<th class="center">项目编号</th>
										<th class="center">原始项目类型</th>
										<th class="center">出借利率</th>
										<th class="center">持有本金</th>
										<th class="center">持有期限</th>
										<th class="center">剩余期限</th>
										<th class="center">公允价值</th>
										<th class="center">出借/承接时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty debtInvestList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${debtInvestList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(creditForm.paginatorPage - 1 ) * creditForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.planNid }"></c:out></td>
													<td class="center"><c:out value="${record.planOrderId }"></c:out></td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.creditNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowTypeName }"></c:out></td>
													<td class="center"><c:out value="${record.borrowApr }"></c:out>%</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.account }">
																<fmt:formatNumber type="number" value="${record.account }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
												<td class="center">
															<c:out value="${record.holdDays}"></c:out>
													</td>
													<td class="center">
														<c:if test="${record.surplusDays ge 0}">
															<c:out value="${record.surplusDays}"></c:out>
													</c:if>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.fairValue }">
																<fmt:formatNumber type="number" value="${record.fairValue }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center"><c:out value="${record.createTime }"></c:out></td>
												</tr>
											</c:forEach>
												<tr>
													<td class="center">总计</td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
														<td class="center"></td>
														<td class="center"></td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty fairValueSum }">
																<fmt:formatNumber type="number" value="${fairValueSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
															<td class="center"></td>
										      </tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="creditdetail:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${creditForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="loandetail:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${creditForm.paginatorPage}" />
				
					<!-- 检索条件 -->
					<div class="form-group">
						<label>智投编号</label>
						<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${creditForm.planNidSrch}" />
					</div>
					<div class="form-group">
					<label>智投订单号:</label>
					<input type="text" name="planOrderId" id="planOrderId" class="form-control input-sm underline" value="${creditForm.planOrderId}" />
					</div>
					<div class="form-group">
					<label>出借/承接订单号:</label>
					<input type="text" name="orderId" id="orderId" class="form-control input-sm underline" value="${creditForm.orderId}" />
					</div>
					<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" id="userName" class="form-control input-sm underline" value="${creditForm.userName}" />
					</div>
					<div class="form-group">
						<label>项目编号</label>
						<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${creditForm.borrowNidSrch}" />
					</div>
					<div class="form-group">
					<label>项目类型</label>
					<select name="projectTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
							<option value="${borrowProjectType.borrowCd }"
								<c:if test="${borrowProjectType.borrowCd eq creditForm.projectTypeSrch}">selected="selected"</c:if>>
								<c:out value="${borrowProjectType.borrowName }"></c:out></option>
						</c:forEach>
					</select>
					</div>
					<div class="form-group">
					<label>出借/承接时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="investTimeStart" id="investTimeStart" class="form-control underline" value="${creditForm.investTimeStart}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<span class="input-icon">
							<input type="text" name="investTimeEnd" id="investTimeEnd" class="form-control underline" value="${creditForm.investTimeEnd}" />
						</span>
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
			<script type='text/javascript' src="${webRoot}/jsp/htj/creditdetail/creditDetailList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
