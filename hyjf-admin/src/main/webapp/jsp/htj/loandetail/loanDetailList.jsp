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
		<tiles:putAttribute name="pageTitle" value="回款明细" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">回款明细</h1>
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
									<c:set var="jspPrevDsb" value="${loanForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${loanForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<shiro:hasPermission name="loandetail:EXPORT">
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
							<%-- 回款明细   --%>
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
										<th class="center">用户名</th>
										<th class="center">出借/购买订单号</th>
										<th class="center">项目编号</th>
										<th class="center">回款期次</th>
										<th class="center">应回款本金</th>
										<th class="center">应回款利息</th>
										<th class="center">应回款总额</th>
										<th class="center">实际回款总额</th>
										<th class="center">待还本金</th>
										<th class="center">待还利息</th>
										<th class="center">待还总额</th>
										<th class="center">服务费</th>
										<th class="center">到期公允价值</th>
										<th class="center">状态</th>
										<th class="center">应回款日期</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty debtLoanList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${debtLoanList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(loanForm.paginatorPage - 1 ) * loanForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.planNid }"></c:out></td>
													<td class="center"><c:out value="${record.planOrderId }"></c:out></td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.investOrderId }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center">第<c:out value="${record.repayPeriod }"></c:out>期</td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.loanCapital }">
																<fmt:formatNumber type="number" value="${record.loanCapital }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.loanInterest }">
																<fmt:formatNumber type="number" value="${record.loanInterest }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.loanAccount }">
																<fmt:formatNumber type="number" value="${record.loanAccount }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.repayAccountYes }">
																<fmt:formatNumber type="number" value="${record.repayAccountYes }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
														<td class="center"><c:choose>
															<c:when test="${not empty record.repayCapitalWait }">
																<fmt:formatNumber type="number" value="${record.repayCapitalWait }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
														<td class="center"><c:choose>
															<c:when test="${not empty record.repayInterestWait }">
																<fmt:formatNumber type="number" value="${record.repayInterestWait }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
														<td class="center"><c:choose>
															<c:when test="${not empty record.repayAccountWait }">
																<fmt:formatNumber type="number" value="${record.repayAccountWait }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty record.serviceFee }">
																<fmt:formatNumber type="number" value="${record.serviceFee }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
														<td class="center"><c:choose>
															<c:when test="${not empty record.expireFairValue }">
																<fmt:formatNumber type="number" value="${record.expireFairValue }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center">
														<c:if test="${record.repayStatus eq 0}">
														未回款
														</c:if>
														<c:if test="${record.repayStatus eq 1}">
															已回款
														</c:if>
													</td>
													<td class="center">
														${record.repayTime}
													</td>
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
													<td class="center"><c:choose>
															<c:when test="${not empty loanCapitalSum }">
																<fmt:formatNumber type="number" value="${loanCapitalSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty loanInterestSum }">
																<fmt:formatNumber type="number" value="${loanInterestSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty loanAccountSum }">
																<fmt:formatNumber type="number" value="${loanAccountSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty repayAccountSum }">
																<fmt:formatNumber type="number" value="${repayAccountSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
														<td></td>
														<td></td>
														<td></td>
														<td class="center"><c:choose>
															<c:when test="${not empty serviceFeeSum }">
																<fmt:formatNumber type="number" value="${serviceFeeSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty fairValueSum }">
																<fmt:formatNumber type="number" value="${fairValueSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"></td>
													<td class="center"></td>
										      </tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="creditdetail:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${loanForm.paginator}"></hyjf:paginator>
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
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${loanForm.paginatorPage}" />
				
					<!-- 检索条件 -->
					<div class="form-group">
					<label>智投编号:</label>
					<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${loanForm.planNidSrch}" />
					</div>
					<div class="form-group">
					<label>智投订单号:</label>
					<input type="text" name="planOrderId" id="planOrderId" class="form-control input-sm underline" value="${loanForm.planOrderId}" />
					</div>
					<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" id="userName" class="form-control input-sm underline" value="${loanForm.userName}" />
					</div>
					<div class="form-group">
					<label>项目编号:</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${loanForm.borrowNidSrch}" />
					</div>
					<div class="form-group">
						<label>还款方式</label>
						<select name="repayStatus" class="form-control underline form-select2">
							<option value="" <c:if test="${'' eq loanForm.repayStatus or empty borrowForm.repayStatus}">selected="selected"</c:if>></option>
								<option value="0" <c:if test="${'0' eq loanForm.repayStatus}">selected="selected"</c:if>>
									未回款
								</option>
								<option value="1" <c:if test="${'1' eq loanForm.repayStatus}">selected="selected"</c:if>>
									已还款
								</option>
						</select>
					</div>
					<div class="form-group">
					<label>应回款时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="repayTimeStart" id="repayTimeStart" class="form-control underline" value="${loanForm.repayTimeStart}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<span class="input-icon">
							<input type="text" name="repayTimeEnd" id="repayTimeEnd" class="form-control underline" value="${loanForm.repayTimeEnd}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/htj/loandetail/loanDetailList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
