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
		<tiles:putAttribute name="pageTitle" value="订单退出" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">订单退出</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="planrepay:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${planRepayForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${planRepayForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="planrepay:EXPORT">
										<shiro:hasPermission name="planrepay:ORGANIZATION_VIEW">
											<a class="btn btn-o btn-primary btn-sm fn-EnhanceExport" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel<i class="fa fa-Export"></i></a>
										</shiro:hasPermission>
										<shiro:lacksPermission name="planrepay:ORGANIZATION_VIEW">
											<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel<i class="fa fa-Export"></i></a>
										</shiro:lacksPermission>
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
										<th class="center">智投订单号</th>
										<th class="center">智投编号</th>
										<th class="center">服务回报期限</th>
										<th class="center">参考年回报率</th>
										<th class="center">用户名</th>
										<th class="center">推荐人</th>
										<th class="center">授权服务金额(元)</th>
										<th class="center">参考回报(元)</th><!-- 应还利息 -->
										<th class="center">实际收益(元)</th>
										<th class="center">已退出金额（元）</th>
										<th class="center">清算服务费（元）</th>
										<th class="center">清算服务费率</th>
										<th class="center">出借服务费率</th>
										<th class="center">清算进度</th>
										<!-- <th class="center">还款状态</th> -->
										<!-- <th class="center">还款方式</th> -->
										<!-- <th class="center">已还款(元)</th> -->
										<!-- <th class="center">待还款(元)</th> -->
										<!-- <th class="center">已还本金(元)</th> -->
										<!-- <th class="center">已还利息(元)</th> -->
										<th class="center">订单状态</th>
										<th class="center">实际退出时间</th><!-- 计划实际还款时间 -->
										<th class="center">预计开始退出时间</th><!-- 计划应还时间 -->
										<!-- <th class="center">操作</th> -->
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
													<td class="center">${(planRepayForm.paginatorPage - 1 ) * planRepayForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.accedeOrderId }"></c:out></td>
													<td class="center"><c:out value="${record.planNid }"></c:out></td>
													<td class="center"><c:out value="${record.lockPeriod }"></c:out>
													    <c:if test="${record.isMonth eq 0}">
															天
														</c:if>
														<c:if test="${record.isMonth eq 1}">
															个月
														</c:if> 
													</td>
													<!-- 预期出借利率 -->
													<td class="center"><c:out value="${record.expectApr }"></c:out>%</td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>		
													<!-- 推荐人 -->
													<td class="center"><c:out value="${record.inviteUser }"></c:out></td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.accedeAccount }">
																<fmt:formatNumber type="number" value="${record.accedeAccount }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
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
													<!-- 实际收益（元） -->
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.actualRevenue }">
																<fmt:formatNumber type="number" value="${record.actualRevenue }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<!-- 实际回款总额（元） -->
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.actualPayTotal }">
																<fmt:formatNumber type="number" value="${record.actualPayTotal }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<!-- 清算服务费（元） -->
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.lqdServiceFee }">
																<fmt:formatNumber type="number" value="${record.lqdServiceFee }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													
													<!-- 清算服务费率 -->
													<td class="center"><c:out value="${record.lqdServiceApr }"></c:out></td>
													
													<!-- 出借服务费率 -->
													<td class="center"><c:out value="${record.investServiceApr }"></c:out></td>
													
													<!-- 清算进度 -->
													<td class="center"><c:out value="${record.lqdProgress }"></c:out></td>
													
<%-- 													<td class="center">
														<c:if test="${record.repayStatus eq 0}">
															未回款
														</c:if>
														<c:if test="${record.repayStatus eq 1}">
															部分回款
														</c:if>
														<c:if test="${record.repayStatus eq 2}">
															已回款
														</c:if>
													</td> --%>
<%-- 													<td class="center">
														<c:if test="${record.borrowStyle eq 'month'}">
															等额本息
														</c:if>
														<c:if test="${record.borrowStyle eq 'season'}">
															按季还款
														</c:if>
														<c:if test="${record.borrowStyle eq 'end'}">
															按月计息，到期还本还息
														</c:if>
														<c:if test="${record.borrowStyle eq 'endmonth'}">
															先息后本
														</c:if>
														<c:if test="${record.borrowStyle eq 'endday'}">
															按天计息，到期还本还息
														</c:if>
														<c:if test="${record.borrowStyle eq 'endmonths'}">
															按月付息到期还本
														</c:if>
														<c:if test="${record.borrowStyle eq 'principal'}">
															等额本金
														</c:if>
													</td> --%>
													<!-- 已还款 -->
<%-- 													<td class="center">
														<c:choose>
															<c:when test="${not empty record.repayAlready && record.orderStatus eq 7}">
																<fmt:formatNumber type="number" value="${record.repayAlready }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td> --%>
													<!-- 已还本金 -->
<%-- 													<td class="center">
														<c:choose>
															<c:when test="${not empty record.planRepayCapital && record.orderStatus eq 7 }">
																<fmt:formatNumber type="number" value="${record.planRepayCapital }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td> --%>
													<!-- 已还利息 -->
<%-- 													<td class="center">
														<c:choose>
															<c:when test="${not empty record.planRepayInterest && record.orderStatus eq 7}">
																<fmt:formatNumber type="number" value="${record.planRepayInterest }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td> --%>
													<td class="center">
<%-- 														<c:if test="${record.orderStatus eq 0}">
															自动投标中
														</c:if> --%>
<%-- 														<c:if test="${record.orderStatus eq 2}">
															自动投标成功
														</c:if> --%>
														<c:if test="${record.orderStatus eq 3}">
															锁定中
														</c:if>
														<c:if test="${record.orderStatus eq 5}">
															退出中
														</c:if>
														<c:if test="${record.orderStatus eq 7}">
															已退出
														</c:if>
<%-- 														<c:if test="${record.orderStatus eq 99}">
															自动出借异常
														</c:if> --%>
													</td>
													<td class="center">
														<hyjf:datetime value="${record.repayActualTime }"></hyjf:datetime>
													</td>
													<td class="center">
														<hyjf:date value="${record.repayShouldTime}"></hyjf:date>
													</td>
											        <!-- 操作 -->
<%-- 													<td class="center">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<shiro:hasPermission name="planrepay:VIEW">
																	<a class="btn btn-transparent btn-xs fn-RepayInfo" data-planOrderId="${record.accedeOrderId }" data-debtPlanNid="${record.planNid }"
																			data-toggle="tooltip" data-placement="top" data-original-title="还款明细"><i class="fa fa-list-ul fa-white"></i></a>
																</shiro:hasPermission>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<shiro:hasPermission name="planrepay:VIEW">
																			<li>
																				<a class="fn-RepayInfo" data-planOrderId="${record.accedeOrderId }" data-debtPlanNid="${record.planNid }">还款明细</a>
																			</li>
																		</shiro:hasPermission>
																	</ul>
																</div>
															</div>
													</td> --%>
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
										<th>&nbsp;</th>
										<td align="center"><fmt:formatNumber value="${sumAccedeAccount }" pattern="#,##0.00#"/></td>
										<td align="center"><fmt:formatNumber value="${sumRepayInterest }" pattern="#,##0.00#"/></td>
										<td align="center"><fmt:formatNumber value="${sumActualRevenue }" pattern="#,##0.00#"/></td>
										<td align="center"><fmt:formatNumber value="${sumActualPayTotal }" pattern="#,##0.00#"/></td>
										<td align="center"><fmt:formatNumber value="${sumLqdServiceFee }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										
<%-- 										<td align="center">
											<c:choose>
												<c:when test="${(not empty sumAccedeAccount || not empty sumRepayInterest || not empty sumRepayWait) && empty sumHjhRepay.sumRepayAlready}">
													0.00
												</c:when>
												<c:otherwise>
													<fmt:formatNumber value="${sumHjhRepay.sumRepayAlready }" pattern="#,##0.00#"/>
												</c:otherwise>
											</c:choose>
										</td> --%>
										<%-- <td align="center"><fmt:formatNumber value="${sumRepayWait }" pattern="#,##0.00#"/></td> --%>
<%-- 										<td align="center">
											<c:choose>
												<c:when test="${(not empty sumAccedeAccount || not empty sumRepayInterest || not empty sumRepayWait) && empty sumHjhRepay.sumPlanRepayCapital}">
													0.00
												</c:when>
												<c:otherwise>
													<fmt:formatNumber value="${sumHjhRepay.sumPlanRepayCapital }" pattern="#,##0.00#"/>
												</c:otherwise>
											</c:choose>
										</td> --%>
<%-- 										<td align="center">
											<c:choose>
												<c:when test="${(not empty sumAccedeAccount || not empty sumRepayInterest || not empty sumRepayWait) && empty sumHjhRepay.sumPlanRepayInterest}">
													0.00
												</c:when>
												<c:otherwise>
													<fmt:formatNumber value="${sumHjhRepay.sumPlanRepayInterest }" pattern="#,##0.00#"/>
												</c:otherwise>
											</c:choose>
										</td> --%>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
									<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="planrepay:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${planRepayForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="planrepay:SEARCH">
				<!-- 隐藏域设计 -->
				<input type="hidden" name="debtPlanNid" id="debtPlanNid" />
				<input type="hidden" name="debtplanstatus" id="debtplanstatus" />
				<input type="hidden" name="planlistNid" id="planlistNid" />
				<input type="hidden" name="PlanlistNidSrch" id="PlanlistNidSrch" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${planRepayForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="accedeOrderIdSrch" id="accedeOrderIdSrch" class="form-control input-sm underline" value="${planRepayForm.accedeOrderIdSrch}" />
				</div>
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${planRepayForm.planNidSrch}" />
				</div>	
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userNameSrch" id="userNameSrch" class="form-control input-sm underline" value="${planRepayForm.userNameSrch}" />
				</div>
				<!-- 汇计划三期新增 -->
				<div class="form-group">
					<label>推荐人</label>
					<input type="text" name="refereeNameSrch" id="refereeNameSrch" class="form-control input-sm underline" value="${planRepayForm.refereeNameSrch}" />
				</div>
				<div class="form-group">
					<label>服务回报期限</label>
					<input type="text" name="debtLockPeriodSrch" id="debtLockPeriodSrch" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" class="form-control input-sm underline" value="${planRepayForm.debtLockPeriodSrch}"
					/>
				</div>
<%-- 				<div class="form-group">
					<label>还款状态</label>
					<select name="repayStatusSrch" id="repayStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
							<option value="0"<c:if test="${planRepayForm.repayStatusSrch eq '0'}">selected="selected"</c:if>>
								未回款</option>
							<option value="1"<c:if test="${planRepayForm.repayStatusSrch eq '1'}">selected="selected"</c:if>>
								部分回款</option>
							<option value="2"<c:if test="${planRepayForm.repayStatusSrch eq '2'}">selected="selected"</c:if>>
								已回款</option>
					</select>
				</div> --%>
				<div class="form-group">
					<label>订单状态</label>
					<select name="orderStatusSrch" id="orderStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
<%-- 							<option value="0"<c:if test="${planRepayForm.orderStatusSrch eq '0'}">selected="selected"</c:if>>
								自动投标中</option> --%>
<%-- 							<option value="2"<c:if test="${planRepayForm.orderStatusSrch eq '2'}">selected="selected"</c:if>>
								自动投标成功</option> --%>
							<option value="3"<c:if test="${planRepayForm.orderStatusSrch eq '3'}">selected="selected"</c:if>>
								锁定中</option>
							<option value="5"<c:if test="${planRepayForm.orderStatusSrch eq '5'}">selected="selected"</c:if>>
								退出中</option>	
							<option value="7"<c:if test="${planRepayForm.orderStatusSrch eq '7'}">selected="selected"</c:if>>
								已退出</option>
<%-- 							<option value="99"<c:if test="${planRepayForm.orderStatusSrch eq '99'}">selected="selected"</c:if>>
								自动出借异常</option> --%>
					</select>
				</div>
<%-- 				<div class="form-group">
					<label>还款方式</label>
					<select name="borrowStyleSrch" id="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="end"<c:if test="${planRepayForm.borrowStyleSrch eq 'end'}">selected="selected"</c:if>>
							按月计息，到期还本还息</option>
						<option value="endday"<c:if test="${planRepayForm.borrowStyleSrch eq 'endday'}">selected="selected"</c:if>>
							按天计息，到期还本还息</option>
						<option value="principal"<c:if test="${planRepayForm.borrowStyleSrch eq 'principal'}">selected="selected"</c:if>>
							等额本金</option>
						<option value="month"<c:if test="${planRepayForm.borrowStyleSrch eq 'month'}">selected="selected"</c:if>>
							等额本息</option>	
						<option value="endmonth"<c:if test="${planRepayForm.borrowStyleSrch eq 'endmonth'}">selected="selected"</c:if>>
							先息后本</option>
					</select>
				</div> --%>
				<div class="form-group">
					<label>实际退出时间</label><!-- 计划实际还款时间 -->
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"><input type="text" name="actulRepayTimeStart" id="start-time" class="form-control underline" value="${planRepayForm.actulRepayTimeStart}" /> <i class="ti-calendar"></i></span>
						<span class="input-group-addon no-border bg-light-orange">~</span><input type="text" name="actulRepayTimeEnd" id="end-time" class="form-control underline" value="${planRepayForm.actulRepayTimeEnd}" />
					</div>
				</div>
				<div class="form-group">
					<label>预计开始退出时间</label><!-- 计划应还日期 -->
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"><input type="text" name="repayTimeStart" id="start-date-time" class="form-control underline" value="${planRepayForm.repayTimeStart}" /> <i class="ti-calendar"></i></span>
						<span class="input-group-addon no-border bg-light-orange">~</span><input type="text" name="repayTimeEnd" id="end-date-time" class="form-control underline" value="${planRepayForm.repayTimeEnd}" />
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
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/planrepay/planrepay.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
