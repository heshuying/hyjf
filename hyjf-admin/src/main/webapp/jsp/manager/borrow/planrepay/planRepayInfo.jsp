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

<shiro:hasPermission name="planrepay:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">还款</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<table  class="table table-striped table-bordered table-hover">
			<tr class="active">	 <th>还款</th> </tr>
			<tr >	
				<th rowspan="2"> 基本信息</th> 
				<td> 智投编号: ${plan.debtPlanNid }</td>
				<td> 智投名称: ${plan.debtPlanName }</td>
				<td> 智投类型: ${plan.debtPlanTypeName }</td>
			 </tr>
			<tr>
				<td> 服务回报期限: ${plan.debtLockPeriod } 个月</td>
				<td> 募集完成时间:
				<c:if test="${plan.fullExpireTime ne 0}"> 
				 <hyjf:date value="${plan.fullExpireTime }"></hyjf:date>
				</c:if>
				<c:if test="${plan.fullExpireTime eq 0 and plan.debtPlanStatus ne 4}"> 
				 <hyjf:date value="${plan.buyEndTime }"></hyjf:date>
				</c:if>
				</td>
					<td> 到期时间:
					<c:if test="${plan.liquidateShouldTime ne 0}"> 
					<hyjf:date value="${plan.liquidateShouldTime }"></hyjf:date>
					</c:if></td>
			</tr>
			<tr >	<th> 资金信息</th>
					<td> 可用余额: ${plan.debtPlanBalance }</td>
					<td> 冻结金额: ${plan.debtPlanFrost }</td>
						<td> </td>
			</tr>
			<tr >	<th rowspan="2"> 清算信息</th>
					<td> 清算债权笔数: ${planCredit.creditCount } 笔</td>
					<td> 持有债权本金: ${planCredit.creditCapital } 元</td>
					<td> </td>
			</tr>
			<tr >	
					<td> 用户收到清算金额: ${planCredit.creditAccountAssigned }  元</td>
					<td> 用户收到还款金额: ${planCredit.RepayFrost }  元</td>
					<td> 累计清算服务费: ${planCredit.creditFee } 元</td>
			</tr>
			<tr >	<th> 收益信息</th>
					<td> 承诺年化: ${plan.expectApr } %</td>
					<td><span style="color:red"> 实际运营年化: ${plan.actualApr } %</span></td>
					<td> </td>
			</tr>
			</table>
			</div>
			<div class="tabbable">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="planrepay:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${planForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${planForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<c:if test="${plan.debtPlanStatus eq 8 }">
									<shiro:hasPermission name="planrepay:MODIFY">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-update"
												data-toggle="tooltip" data-placement="bottom"
												data-plannid="${plan.debtPlanNid }" data-original-title="开始还款">开始还款 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									</c:if>
									<c:if test="${plan.debtPlanStatus eq 9 }">
										<a class="btn btn-o btn-primary btn-sm hidden-xs disable"
												data-toggle="tooltip" data-placement="bottom" data-original-title="未还款">未还款 <i class="fa fa-plus"></i></a>
									</c:if>
									<c:if test="${plan.debtPlanStatus eq 10 }">
										<a class="btn btn-o btn-primary btn-sm hidden-xs disable"
												data-toggle="tooltip" data-placement="bottom" data-original-title="还款中">还款中 <i class="fa fa-plus"></i></a>
									</c:if>
									<c:if test="${plan.debtPlanStatus eq 11 }">
										<a class="btn btn-o btn-primary btn-sm hidden-xs disable"
												data-toggle="tooltip" data-placement="bottom" data-original-title="已还款">已还款 <i class="fa fa-plus"></i></a>
									</c:if>
									<shiro:hasPermission name="planrepay:EXPORT">
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
					<%-- 加入明细 --%>
						<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">智投订单号</th>
										<th class="center">用户名</th>
										<th class="center">授权服务金额</th>
										<th class="center">应还利息</th>
										<th class="center">应还总额</th>
										<th class="center">可用余额</th>
										<th class="center">本期实还利息</th>
										<th class="center">实际还款总额</th>
										<th class="center">服务费</th>
										<th class="center">还款状态</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="13">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(planForm.paginatorPage - 1 ) * planForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.accedeOrderId }"></c:out></td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><fmt:formatNumber type="number" value="${record.accedeAccount }" /></td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.repayInterest }">
																<fmt:formatNumber type="number" value="${record.repayInterest }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.repayAccount }">
																<fmt:formatNumber type="number" value="${record.repayAccount }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.accedeBalance }">
																<fmt:formatNumber type="number" value="${record.accedeBalance }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.repayInterestYes }">
																<fmt:formatNumber type="number" value="${record.repayInterestYes }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.repayAccountYes }">
																<fmt:formatNumber type="number" value="${record.repayAccountYes }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.serviceFee }">
																<fmt:formatNumber type="number" value="${record.serviceFee }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
															<c:if test="${record.status eq '3'}">
																未还款
															</c:if>
															<c:if test="${record.status eq '4' }">
																还款中
															</c:if>
															<c:if test="${record.status eq '5'}">
																已还款
															</c:if>
													</td>
												</tr>
											</c:forEach>
												<tr>
													<td class="center">总计</td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center">	
														<c:choose>
															<c:when test="${not empty repayAccount }">
																<fmt:formatNumber type="number" value="${repayAccount }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center">	
														<c:choose>
															<c:when test="${not empty orderMoney }">
																<fmt:formatNumber type="number" value="${orderMoney }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center">	
														<c:choose>
															<c:when test="${not empty repayInterestFact }">
																<fmt:formatNumber type="number" value="${repayInterestFact }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center">	
														<c:choose>
															<c:when test="${not empty repayAccountFact }">
																<fmt:formatNumber type="number" value="${repayAccountFact }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty serviceFeeRate }">
																<fmt:formatNumber type="number" value="${serviceFeeRate }" /> 
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
							<shiro:hasPermission name="planrepay:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="infoSearchAction" paginator="${planForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="planrepay:SEARCH">
				<input type="hidden" name="planNidSrch" id="planNidSrch" value="${planForm.planNidSrch}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${planForm.paginatorPage}" />
					<%-- 加入明细 --%>
					<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="planOrderId" id="planOrderId" class="form-control input-sm underline" value="${planForm.planOrderId}" />
					</div>
					<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" id="userName" class="form-control input-sm underline" value="${planForm.userName}" />
					</div>
					<div class="form-group">
						<label>状态</label>
						<select name="planStatusSrch" class="form-control underline form-select2">
							<option value="" <c:if test="${'' eq planForm.planStatusSrch or empty planForm.planStatusSrch}">selected="selected"</c:if>></option>
								<option value="8" <c:if test="${'8' eq planForm.planStatusSrch}">selected="selected"</c:if>>
									未还款
								</option>
								<option value="10" <c:if test="${'9' eq planForm.planStatusSrch}">selected="selected"</c:if>>
										还款中
								</option>
								<option value="11" <c:if test="${'10' eq planForm.planStatusSrch}">selected="selected"</c:if>>
										已还款
								</option>
						</select>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/planrepay/planRepayInfo.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
