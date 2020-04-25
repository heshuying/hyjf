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

<shiro:hasPermission name="planlock:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="加入明细" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">加入明细</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<table  class="table table-striped table-bordered table-hover">
			<tr class="active">	 <th>运营信息</th> </tr>
			<tr >	
				<th rowspan="3"> 基本信息</th> 
				<td> 智投编号: ${plan.debtPlanNid }</td>
				<td> 智投名称: ${plan.debtPlanName }</td>
				<td> 智投类型: ${plan.debtPlanTypeName }</td>
			 </tr>
			<tr>
				<td> 服务回报期限: ${plan.debtLockPeriod } 个月</td>
				<td> 满标/到期时间:
				<c:if test="${plan.fullExpireTime ne 0}"> 
				 <hyjf:datetimeformat value="${plan.fullExpireTime }"></hyjf:datetimeformat>
				</c:if>
				<c:if test="${plan.fullExpireTime eq 0 and plan.debtPlanStatus ne 4}"> 
				 <hyjf:datetimeformat value="${plan.buyEndTime }"></hyjf:datetimeformat>
				</c:if>
				</td>
					<td> 应清算时间:
					<c:if test="${plan.liquidateShouldTime ne 0}"> 
					<hyjf:date value="${plan.liquidateShouldTime }"></hyjf:date>
					</c:if></td>
			</tr>
			<tr>
				<td> 计划总额: ${plan.debtPlanMoney } 元</td>
				<td> 授权服务金额: ${plan.debtPlanMoneyYes } 元</td>
				<td> 加入订单数: ${accedeCount}</td>
			</tr>
			<tr >	<th> 资金信息</th>
					<td> 可用余额: ${plan.debtPlanBalance }</td>
					<td> 冻结金额: ${plan.debtPlanFrost }</td>
					<td> </td>
			</tr>
			<tr >	<th rowspan="2"> 债权信息</th>
					<td> 持有专属资产笔数: ${investCount } 笔</td>
					<td> 持有专属资产本金: ${account } 元</td>
					<td> </td>
			</tr>
			<tr>
					<td> 持有债权笔数: ${creditTenderCount } 笔</td>
					<td> 持有债权本金: ${creditCapital } 元</td>
					<td> 实际支付: ${assignPay } 元</td>
			</tr>
			<tr >	<th> 收益信息</th>
					<td> 参考年回报率: ${plan.expectApr } %</td>
					<td><span style="color:red"> 当前年化: ${plan.actualApr } %</span></td>
					<td><span style="color:red"> 预计到期年化: ${expireApr } %</span> </td>
			</tr>
			</table>
			</div>
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="planlock:VIEW">
			      		<li <c:if test="${empty planForm.type or planForm.type eq '0' or planForm.type eq '3'}"> class="active" </c:if> ><a href="${webRoot}/manager/borrow/planLock/infoAction?type=0&planNidSrch=${planForm.planNidSrch}">加入明细</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="planlock:VIEW">
			      		<li <c:if test="${not empty planForm.type and planForm.type eq '1'}"> class="active" </c:if>><a href="${webRoot}/manager/borrow/planLock/infoAction?type=1&planNidSrch=${planForm.planNidSrch}">债权明细</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="planlock:VIEW">
			      		<li <c:if test="${not empty planForm.type and planForm.type eq '2'}"> class="active" </c:if>><a href="${webRoot}/manager/borrow/planLock/infoAction?type=2&planNidSrch=${planForm.planNidSrch}">回款明细</a></li>
			      	</shiro:hasPermission>
			</ul>
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="planlock:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${planForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${planForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
			<%-- 加入明细 --%>
			<c:if test="${empty planForm.type or planForm.type eq '0' or  planForm.type eq '3'}">
				<c:choose>
					<%-- 手动出借 --%>
					<c:when test="${not empty planForm.accedeOrderId  }">
					<a href="infoAction?planNidSrch=${planLockCustomize.debtPlanNid }">《《返回</a> &nbsp&nbsp&nbsp&nbsp
					订单号：<c:out value="${planForm.accedeOrderId}"></c:out>，用户名：<c:out value="${planLockCustomize.userName}"></c:out>，
							授权服务金额：<c:choose>
									<c:when test="${not empty planLockCustomize.accedeAccount }">
										<fmt:formatNumber type="number" value="${planLockCustomize.accedeAccount }" /> 
									</c:when>
									<c:otherwise>
										0.00
									</c:otherwise>
								</c:choose>元，
								可用余额：
								<c:choose>
									<c:when test="${not empty planLockCustomize.accedeBalance }">
										${planLockCustomize.accedeBalance }
									</c:when>
									<c:otherwise>
										0.00
									</c:otherwise>
								</c:choose>元
								<ul class="nav nav-tabs"  id="myTab"> 
									<li <c:if test="${empty planForm.type or planForm.type eq '0'}"> class="active" </c:if> ><a href="${webRoot}/manager/borrow/planLock/infoAction?type=0&planNidSrch=${planForm.planNidSrch}&accedeOrderId=${planForm.accedeOrderId}"">汇添金专属产品</a></li>
									<li <c:if test="${not empty planForm.type and planForm.type eq '3'}"> class="active" </c:if>><a href="${webRoot}/manager/borrow/planLock/infoAction?type=3&planNidSrch=${planForm.planNidSrch}&accedeOrderId=${planForm.accedeOrderId}">汇添金转让类产品</a></li>
								</ul>
						<%-- 列表一览 --%>
						<c:if test="${empty planForm.type or planForm.type eq '0' }">
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">项目编号</th>
										<th class="center">项目类型</th>
										<th class="center">
											<c:choose>
												<c:when test="${ !empty planForm.sort and planForm.col eq 'b.borrow_period' }">
													<a href="#" class="fn-Sort" data-col="b.borrow_period" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${ planForm.sort eq 'desc' }">asc</c:if>">项目期限</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="b.borrow_period" data-sort="desc">项目期限</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose></th>
										<th class="center">还款方式</th>
										<th class="center">
											<c:choose>
												<c:when test="${ !empty planForm.sort and planForm.col eq 'b.borrow_apr' }">
													<a href="#" class="fn-Sort" data-col="b.borrow_apr" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${ planForm.sort eq 'desc' }">asc</c:if>">参考年回报率</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="b.borrow_apr" data-sort="desc">参考年回报率</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose></th>
										<th class="center">项目总额</th>
										<th class="center">
											<c:choose>
												<c:when test="${ !empty planForm.sort and planForm.col eq 'b.borrow_account_wait' }">
													<a href="#" class="fn-Sort" data-col="b.borrow_account_wait" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${ planForm.sort eq 'desc' }">asc</c:if>">剩余可投</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="b.borrow_account_wait" data-sort="desc">剩余可投</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose></th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty borrowList}">
											<tr><td colspan="13">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${borrowList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(planForm.paginatorPage - 1 ) * planForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.projectTypeName }"></c:out></td>
													<td class="center"><c:out value="${record.borrowPeriod }"></c:out></td>
													<td class="center"><c:out value="${record.borrowStyleName }"></c:out></td>
													<td class="center"><c:out value="${record.borrowApr }"></c:out></td>
													<td class="center">
														${record.account }
													</td>
													<td class="center">
													${record.borrowAccountWait }
													</td>
													<td class="center">
													<a class="fn-invest" data-borrownid="${record.borrowNid}" data-accedeorderid="${planForm.accedeOrderId}">出借</a>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</c:if>
					</c:when>
					<c:otherwise>
					<%-- 加入明细 --%>
						<%-- 列表一览 --%>
					<c:if test="${empty planForm.type or planForm.type eq '0' }">
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">智投订单号</th>
										<th class="center">用户名</th>
										<th class="center">
											<c:choose>
												<c:when test="${ !empty planForm.sort and planForm.col eq 'dpa.accede_account' }">
													<a href="#" class="fn-Sort" data-col="dpa.accede_account" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${ planForm.sort eq 'desc' }">asc</c:if>">授权服务金额</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="dpa.accede_account" data-sort="desc">授权服务金额</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose>
										</th>
										<th class="center">
										<c:choose>
												<c:when test="${ !empty planForm.sort and planForm.col eq 'dpa.accede_balance' }">
													<a href="#" class="fn-Sort" data-col="dpa.accede_balance" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${ planForm.sort eq 'desc' }">asc</c:if>">订单余额</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="dpa.accede_balance" data-sort="desc">订单余额</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose>
										</th>
										<th class="center">
											<c:choose>
												<c:when test="${ !empty planForm.sort and planForm.col eq 'dpa.accede_frost' }">
													<a href="#" class="fn-Sort" data-col="dpa.accede_frost" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${ planForm.sort eq 'desc' }">asc</c:if>">冻结金额</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="dpa.accede_frost" data-sort="desc">冻结金额</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose>
										</th>
										<th class="center">待还款冻结</th>
										<th class="center">当前年化</th>
										<th class="center">到期公允价值</th>
										<th class="center">清算手续费率</th>
										<th class="center">已收服务费</th>
										<th class="center">授权服务时间</th>
										<th class="center">遍历次数</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="17">暂时没有数据记录</td></tr>
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
															<c:when test="${not empty record.accedeFrost }">
																<fmt:formatNumber type="number" value="${record.accedeFrost }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.liquidatesRepayFrost }">
																<fmt:formatNumber type="number" value="${record.liquidatesRepayFrost }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:out value="${record.factApr}"></c:out>%
													</td>
													<td class="center">
														<c:out value="${record.expireFairValue}"></c:out>
													</td>
													<td class="center">
														<c:out value="${record.serviceFeeRate}"></c:out>%
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
														<c:out value="${record.createTime}"></c:out>
													</td>
													<td class="center">
														<c:out value="${record.cycleTimes}"></c:out>
													</td>
													<td class="center">
														<shiro:hasPermission name="planlock:MODIFY">
																<a class="fn-update" data-accedeorderId="${record.accedeOrderId }">次数清零</a>
														</shiro:hasPermission>
														|
														<shiro:hasPermission name="planlock:MODIFY">
																<a class="fn-Modify" data-accedeorderid="${record.accedeOrderId }">手动出借</a>
														</shiro:hasPermission>
													</td>
												</tr>
											</c:forEach>
												<tr>
													<td class="center">总计</td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center">	
														<c:choose>
															<c:when test="${not empty joinMoney }">
																<fmt:formatNumber type="number" value="${joinMoney }" /> 
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
															<c:when test="${not empty frostMoney }">
																<fmt:formatNumber type="number" value="${frostMoney }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
														<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
										      </tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</c:if>
					</c:otherwise>
					</c:choose>
						
			</c:if>
			
			<%-- 债权明细  --%>
			<c:if test="${planForm.type eq '1'}">
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
										<th class="center">出借/购买订单号</th>
										<th class="center">债转编号</th>
										<th class="center">项目编号</th>
										<th class="center">还款方式</th>
										<th class="center">出借利率</th>
										<th class="center">持有本金</th>
										<th class="center">持有期限</th>
										<th class="center">剩余期限</th>
										<th class="center">公允价值</th>
										<th class="center">到期公允价值</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty debtInvestList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${debtInvestList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(planForm.paginatorPage - 1 ) * planForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.planOrderId }"></c:out></td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.creditNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowStyleName }"></c:out></td>
													<td class="center"><c:out value="${record.borrowApr }"></c:out>%</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.account }">
																<fmt:formatNumber type="number" value="${record.account }" /> 
															</c:when>
															<c:otherwise>
																0
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
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.expireFairValue }">
																<fmt:formatNumber type="number" value="${record.expireFairValue }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
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
													<td class="center"></td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty accountSum }">
																<fmt:formatNumber type="number" value="${accountSum }" /> 
															</c:when>
															<c:otherwise>
																0
															</c:otherwise>
														</c:choose></td>
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
														<td class="center">
															<c:choose>
																<c:when test="${not empty expireFairValueSum }">
																	<fmt:formatNumber type="number" value="${expireFairValueSum }" /> 
																</c:when>
																<c:otherwise>
																	0.00
																</c:otherwise>
															</c:choose>
														
														
														</td>
										      </tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
			</c:if>
			<%-- 回款明细   --%>
			<c:if test="${planForm.type eq '2'}">
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
										<th class="center">出借/购买订单号</th>
										<th class="center">项目编号</th>
										<th class="center">回款期次</th>
										<th class="center">应回款本金</th>
										<th class="center">应回款利息</th>
										<th class="center">应回款总额</th>
										<th class="center">实际回款总额</th>
										<th class="center">状态</th>
										<th class="center">应回款日期</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty debtLoanList}">
											<tr><td colspan="13">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${debtLoanList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(planForm.paginatorPage - 1 ) * planForm.paginator.limit + status.index + 1 }</td>
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
													<td class="center">
														<c:if test="${record.repayStatus eq 0}">
														未还款
														</c:if>
														<c:if test="${record.repayStatus eq 1}">
															已还款
														</c:if>
													</td>
													<td class="center">
														<c:out value="${record.repayTime}"></c:out>
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
													<td class="center"></td>
													<td class="center"></td>
										      </tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
			</c:if>
							<c:if test="${planForm.type eq '3'}">
								<table id="equiList" class="table table-striped table-bordered table-hover">
									<colgroup>
										<col style="width:55px;" />
									</colgroup>
									<thead>
										<tr>
											<th class="center">序号</th>
											<th class="center">债转编号</th>
											<th class="center">项目编号</th>
											<th class="center">
												<c:choose>
													<c:when test="${ !empty planForm.sort and planForm.col eq 'hdc.actual_apr' }">
														<a href="#" class="fn-Sort" data-col="hdc.actual_apr" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${planForm.sort eq 'desc' }">asc</c:if>">实际出借利率</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
													</c:when>
													<c:otherwise>
														<a href="#" class="fn-Sort" data-col="hdc.actual_apr" data-sort="desc">实际出借利率</a>&nbsp;<i class="fa fa-sort"></i>
													</c:otherwise>
												</c:choose>
											</th>
											<th class="center">持有期限</th>
											<th class="center">
												<c:choose>
													<c:when test="${ !empty planForm.sort and planForm.col eq 'hdc.remain_days' }">
														<a href="#" class="fn-Sort" data-col="hdc.remain_days" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${ planForm.sort eq 'desc' }">asc</c:if>">剩余期限</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
													</c:when>
													<c:otherwise>
														<a href="#" class="fn-Sort" data-col="hdc.remain_days" data-sort="desc">剩余期限</a>&nbsp;<i class="fa fa-sort"></i>
													</c:otherwise>
												</c:choose>
											</th>
											<th class="center">还款方式</th>
											<th class="center">转让本金</th>
											<th class="center">
												<c:choose>
													<c:when test="${ !empty planForm.sort and planForm.col eq 'hdc.credit_capital_wait' }">
														<a href="#" class="fn-Sort" data-col="hdc.credit_capital_wait" data-sort="<c:if test="${ planForm.sort eq 'asc' }">desc</c:if><c:if test="${ planForm.sort eq 'desc' }">asc</c:if>">剩余本金</a>&nbsp;<i class="fa fa-sort-${planForm.sort}"></i>
													</c:when>
													<c:otherwise>
														<a href="#" class="fn-Sort" data-col="hdc.credit_capital_wait" data-sort="desc">剩余本金</a>&nbsp;<i class="fa fa-sort"></i>
													</c:otherwise>
												</c:choose>
											</th>
											<th class="center">发起类型</th>
											<th class="center">操作</th>
										</tr>
									</thead>
									<tbody id="roleTbody">
										<c:choose>
											<c:when test="${empty creditProjectList}">
												<tr><td colspan="11">暂时没有数据记录</td></tr>
											</c:when>
											<c:otherwise>
												<c:forEach items="${creditProjectList }" var="record" begin="0" step="1" varStatus="status">
													<tr>
														<td class="center">${(planForm.paginatorPage - 1 ) * planForm.paginator.limit + status.index + 1 }</td>
														<td class="center"><c:out value="${record.creditNid }"></c:out></td>
														<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
														<td class="center"><c:out value="${record.actualApr }"></c:out>%</td>
														<td class="center"><c:out value="${record.holdDays }"></c:out>天</td>
														<td class="center"><c:choose><c:when test="${record.remainDays > 0 }"><c:out value="${record.remainDays }"></c:out></c:when>
															<c:otherwise>
																0
															</c:otherwise>
														</c:choose>天</td>
														<td class="center"><c:out value="${record.borrowStyle }"></c:out></td>
														<td class="center"><c:out value="${record.creditCapital }"></c:out>元</td>
														<td class="center"><c:out value="${record.creditCapitalWait }"></c:out>元</td>
														<td class="center">
															<c:if test="${record.creditType eq '0'}">
																<c:out value="系统清算"></c:out>
															</c:if>
															<c:if test="${record.creditType eq '1'}">
																<c:out value="用户发起"></c:out>
															</c:if>
														</td>
														<td class="center">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<shiro:hasPermission name="planlock:MODIFY">
																	<a class="btn btn-transparent btn-xs tooltips fn-CreditTender" data-creditNid ="${record.creditNid}" data-accedeorderid="${planForm.accedeOrderId}"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="购买">购买</a>
																</shiro:hasPermission>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group" dropdown="">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<shiro:hasPermission name="planlock:MODIFY">
																			<li>
																				<a class="fn-CreditTender" data-creditNid ="${record.creditNid}" data-accedeorderid="${planForm.accedeOrderId}">购买</a>
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
							
							</c:if>
							
							<%-- 分页栏 --%>
							<shiro:hasPermission name="planlock:SEARCH">
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
			<shiro:hasPermission name="planlock:SEARCH">
				<input type="hidden" name="debtPlanNid" id="debtPlanNid"  value="${planForm.planNidSrch}"/>
				<input type="hidden" name="planNidSrch" id="planNidSrch" value="${planForm.planNidSrch}"/>
				<input type="hidden" name="debtPlanNidSrch" id="debtPlanNidSrch" value="${planForm.planNidSrch}" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${planForm.paginatorPage}" />
				<input type="hidden" name="accedeOrderId" id="accedeOrderId"  value="${planForm.accedeOrderId}"/>
				<input type="hidden" name="type" id="type"  value="${planForm.type}"/>
				<input type="hidden" name="col" id="col"  value="${planForm.col}"/>
				<input type="hidden" name="sort" id="sort"  value="${planForm.sort}"/>
				<input type="hidden" name="borrowNid" id="borrowNid"  />
				<input type="hidden" name="creditNid" id="creditNid"  />
				<%--加入明细   --%>	
		<c:if test="${empty planForm.type  or planForm.type eq '0'}">
			<%-- 检索条件 --%>
			<c:choose>
					<%-- 手动出借  --%>
					<c:when test="${not empty planForm.accedeOrderId  }">
					<div class="form-group">
					<label>项目编号:</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${planForm.borrowNidSrch}" />
					</div>
					<div class="form-group">
					<label>项目类型</label>
					<select name="projectTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
							<option value="${borrowProjectType.borrowCd }"
								<c:if test="${borrowProjectType.borrowCd eq planForm.projectTypeSrch}">selected="selected"</c:if>>
								<c:out value="${borrowProjectType.borrowName }"></c:out></option>
						</c:forEach>
					</select>
					</div>
					<div class="form-group">
						<label>还款方式</label>
						<select name="borrowStyleSrch" class="form-control underline form-select2">
							<option value=""></option>
							<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
								<option value="${borrowStyle.nid }" 
									<c:if test="${borrowStyle.nid eq planForm.borrowStyleSrch}">selected="selected"</c:if>>
									<c:out value="${borrowStyle.name}"></c:out>
								</option>
							</c:forEach>
						</select>
					</div>
							
					</c:when>
					<c:otherwise>
					
					<%-- 加入明细 --%>
					<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" id="userName" class="form-control input-sm underline" value="${planForm.userName}" />
					</div>
				<div class="form-group">
					<label>计划余额</label>
					<span class="input-icon">
						<input type="number" name="planWaitMoneyMin" id="planWaitMoneyMin" class="form-control input-sm underline" value="${planForm.planWaitMoneyMin}" />
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="number" name="planWaitMoneyMax" id="planWaitMoneyMax" class="form-control input-sm underline" value="${planForm.planWaitMoneyMax}" />
					</span>
				</div>
				<div class="form-group">
					<label>授权服务时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="joinTimeStart" id="joinTimeStart" class="form-control underline" value="${planForm.joinTimeStart}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<span class="input-icon">
							<input type="text" name="joinTimeEnd" id="joinTimeEnd" class="form-control underline" value="${planForm.joinTimeEnd}" />
						</span>
					</div>
				</div>
					</c:otherwise>
			</c:choose>
				
		</c:if>
		
		<%-- 债权明细 --%>
		<c:if test="${planForm.type eq '1'}">
					<div class="form-group">
					<label>智投订单号:</label>
					<input type="text" name="planOrderId" id="planOrderId" class="form-control input-sm underline" value="${planForm.planOrderId}" />
					</div>
					<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" id="userName" class="form-control input-sm underline" value="${planForm.userName}" />
					</div>
					 <div class="form-group">
						<label>还款方式</label>
						<select name="borrowStyleSrch" class="form-control underline form-select2">
							<option value=""></option>
							<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
								<option value="${borrowStyle.nid }" 
									<c:if test="${borrowStyle.nid eq planForm.borrowStyleSrch}">selected="selected"</c:if>>
									<c:out value="${borrowStyle.name}"></c:out>
								</option>
							</c:forEach>
						</select>
					</div>
		</c:if>
						
		<%--回款明细  --%>
		<c:if test="${planForm.type eq '2'}">
					<div class="form-group">
					<label>智投订单号:</label>
					<input type="text" name="planOrderId" id="planOrderId" class="form-control input-sm underline" value="${planForm.planOrderId}" />
					</div>
					<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" id="userName" class="form-control input-sm underline" value="${planForm.userName}" />
					</div>
					<div class="form-group">
					<label>项目编号:</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${planForm.borrowNidSrch}" />
					</div>
					<div class="form-group">
						<label>还款状态</label>
						<select name="repayStatus" class="form-control underline form-select2">
							<option value="" <c:if test="${'' eq planForm.repayStatus or empty planForm.repayStatus}">selected="selected"</c:if>></option>
								<option value="0" <c:if test="${'0' eq planForm.repayStatus}">selected="selected"</c:if>>
									未回款
								</option>
								<option value="1" <c:if test="${'1' eq planForm.repayStatus}">selected="selected"</c:if>>
										已还款
								</option>
						</select>
					</div>
		</c:if>
			<%--回款明细  --%>
			<c:if test="${planForm.type eq '3'}">
				 <div class="form-group">
					<label>还款方式</label>
					<select name="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
							<option value="${borrowStyle.nid }" 
								<c:if test="${borrowStyle.nid eq planForm.borrowStyleSrch}">selected="selected"</c:if>>
								<c:out value="${borrowStyle.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
			</c:if>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/planlock/planLockInfo.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
