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

<shiro:hasPermission name="liquidationManager:INFO">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">清算</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<table  class="table table-striped table-bordered table-hover">
			<tr class="active">	 <th>运营详情</th> </tr>
			<tr >	
				<th rowspan="3"> 基本信息</th> 
				<td> 智投编号: ${plan.planNid }</td>
				<td> 智投名称: ${plan.planName }</td>
				<td> 智投类型: ${plan.planTypeName }</td>
			 </tr>
			<tr>
				<td> 服务回报期限: ${plan.lockPeriod } 个月</td>
				<td> 募集完成时间:
				<c:if test="${plan.fullExpireTime ne 0}"> 
				 <hyjf:datetime value="${plan.fullExpireTime }"></hyjf:datetime>
				</c:if>
				<c:if test="${plan.fullExpireTime eq 0 and plan.planStatus ne 4}"> 
				 <hyjf:datetime value="${plan.buyEndTime }"></hyjf:datetime>
				</c:if>
				</td>
					<td> 应清算时间:
					<c:if test="${plan.liquidateShouldTime ne 0}"> 
					<hyjf:date value="${plan.liquidateShouldTime }"></hyjf:date>
					</c:if></td>
			</tr>
			<tr >	
				<td> 计划总额: ${plan.planMoney }元</td>
				<td> 授权服务金额: ${plan.planMoneyYes }元</td>
				<td> 加入订单数: ${plan.accedeTimes }</td>
			</tr>
			<tr >	<th> 资金信息</th>
					<td> 可用余额: ${plan.planBalance }元</td>
					<td> 冻结金额: ${plan.planFrost }元</td>
					<td> 公允价值: 
						<c:choose>
						  <c:when test="${not empty plan.fairValueTotal }">
							<fmt:formatNumber type="number" value="${plan.fairValueTotal }" /> 
						  </c:when>
						<c:otherwise>
							0.00
						  </c:otherwise>
						</c:choose>
						元
					</td>
			</tr>
			<tr >	<th rowspan="2"> 债权信息</th> 
					<td> 持有专属资产笔数: ${plan.tenderNum } 笔</td>
					<td> 持有本金: 
					<c:choose>
						  <c:when test="${not empty plan.tenderCapital }">
							<fmt:formatNumber type="number" value="${plan.tenderCapital }" /> 
						  </c:when>
						  <c:otherwise>
							0.00
						  </c:otherwise>
					</c:choose>
					元</td>
					<td> </td>
			</tr>
			<tr >
					<td> 持有债权笔数: ${plan.creditNum } 笔</td>
					<td> 持有债权本金: 
					<c:choose>
						  <c:when test="${not empty plan.creditCapital }">
							<fmt:formatNumber type="number" value="${plan.creditCapital }" /> 
						  </c:when>
						  <c:otherwise>
							0.00
						  </c:otherwise>
					</c:choose> 
					元</td>
					<td> 实际支付:
					<c:choose>
						  <c:when test="${not empty plan.creditAccount }">
							<fmt:formatNumber type="number" value="${plan.creditAccount }" /> 
						  </c:when>
						  <c:otherwise>
							0.00
						  </c:otherwise>
					</c:choose> 
					元</td>
			</tr>
			<tr >	<th> 收益信息</th>
					<td> 参考年回报率: ${plan.expectApr } %</td>
					<td><span style="color:red"> 当前年化: ${plan.actualApr } %</span></td>
					<td> </td>
			</tr>
			</table>
			</div>
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
								<%-- 功能栏 --%>
								<div class="well">
									<hyjf:dateSet value="<%=String.valueOf(System.currentTimeMillis()/1000)%>" var="nowDate"/>
									<hyjf:dateSet value="${plan.liquidateShouldTime}" var="liquidateShouldTime"/>
									<c:if test="${(plan.planStatus eq 5) and (nowDate ge liquidateShouldTime)}">
									<shiro:hasPermission name="liquidationManager:LIQUIDATION">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-liquidation"
												data-toggle="tooltip" data-placement="bottom"
												data-plannid="${plan.planNid }" data-original-title="开始清算">开始清算 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									</c:if>
									<c:if test="${plan.planStatus eq 6 }">
										<a class="btn btn-o btn-primary btn-sm hidden-xs disable"
												data-toggle="tooltip" data-placement="bottom" data-original-title="清算中">清算中 <i class="fa fa-plus"></i></a>
									</c:if>
									<shiro:hasPermission name="liquidationManager:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
											<span style="color:red">${message }</span>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
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
										<th class="center">预期收益</th>
										<th class="center">清算时公允价值</th>
										<th class="center">服务费率</th>
										<th class="center">计划订单余额</th>
										<th class="center">冻结金额</th>
										<th class="center">清算-项目还款</th>
										<th class="center">收到转让金额</th>
										<th class="center">累计服务费</th>
										<th class="center">转让进度</th>
										<th class="center">操作</th>
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
													<td class="center"><c:out value="${record.planOrderId }"></c:out></td>
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
															<c:when test="${not empty record.liquidationFairValue }">
																<fmt:formatNumber type="number" value="${record.liquidationFairValue }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.serviceFeeRate }">
																<fmt:formatNumber type="number" value="${record.serviceFeeRate }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>%
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
														<c:choose>
															<c:when test="${not empty record.accountReceive }">
																<fmt:formatNumber type="number" value="${record.accountReceive }" /> 
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
															<c:choose>
															<c:when test="${empty record.liquidatesApr }">
																0.00%
															</c:when>
															<c:otherwise>
																${record.liquidatesApr }% 
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
																		<a class="btn btn-transparent btn-xs fn-transferRecord"
																			data-planorderid="${record.planOrderId }"
																			data-toggle="tooltip" data-placement="top"
																			data-original-title="查看"><i class="fa fa-pencil"></i></a>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
																	<div class="btn-group">
																		<button type="button"
																			class="btn btn-primary btn-o btn-sm"
																			data-toggle="dropdown">
																			<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																		</button>
																		<ul class="dropdown-menu pull-right dropdown-light"
																			role="menu">
																				<li><a class="fn-transferRecord"
																					data-planorderid="${record.planOrderId }">查看</a></li>
																		</ul>
																	</div>
													   </div>
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
															<c:when test="${not empty liquidationFairValueSum }">
																<fmt:formatNumber type="number" value="${liquidationFairValueSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"></td>
													<td class="center">	
														<c:choose>
															<c:when test="${not empty accedeBalanceSum }">
																<fmt:formatNumber type="number" value="${accedeBalanceSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center">	
														<c:choose>
															<c:when test="${not empty accedeFrostSum }">
																<fmt:formatNumber type="number" value="${accedeFrostSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center">	
														<c:choose>
															<c:when test="${not empty liquidatesRepayFrostSum }">
																<fmt:formatNumber type="number" value="${liquidatesRepayFrostSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"><c:choose>
															<c:when test="${not empty accountReceiveSum }">
																<fmt:formatNumber type="number" value="${accountReceiveSum }"  /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose></td>
													<td class="center"></td>
													<td class="center"></td>
													<td class="center"></td>
										      </tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
			
							<%-- 分页栏 --%>
							<shiro:hasPermission name="planrepay:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="detailAction" paginator="${creditForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="liquidationManager:SEARCH">
				<input type="hidden" name="planNidSrch" id="planNidSrch" value="${plan.planNid}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${creditForm.paginatorPage}" />
				<input type="hidden" name="planNid" id="planNid"  value="${plan.planNid}"/>
				<input type="hidden" name="planOrderId" id="planOrderId" />
					<%-- 加入明细 --%>
					<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="planOrderIdSrh" id="planOrderIdSrh" class="form-control input-sm underline" value="${planForm.planOrderId}" />
					</div>
					<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" id="userName" class="form-control input-sm underline" value="${planForm.userName}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/liquidation/planLiquidationDetail.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
