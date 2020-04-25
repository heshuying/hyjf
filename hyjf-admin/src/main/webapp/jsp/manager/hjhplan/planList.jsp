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

<shiro:hasPermission name="planlist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="智投管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">智投管理</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="planlist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${planListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${planListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
											
									<shiro:hasPermission name="planlist:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
												data-toggle="tooltip" data-placement="bottom" data-original-title="添加智投">添加智投 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<!-- 汇计划三期 -->
									<shiro:hasPermission name="planlist:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel<i class="fa fa-Export"></i></a>
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
										<th class="center">智投名称</th>
										<th class="center">还款方式</th>
										<th class="center">服务回报期限</th>
										<th class="center">参考年回报率</th>
										<th class="center">开放额度(元)</th>
										<th class="center">累计授权服务金额(元)</th>
										<th class="center">待还总额(元)</th>
										<th class="center">智投状态</th>
										<th class="center">显示状态</th>
										<th class="center">添加时间</th>
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
													<td class="center">${(planListForm.paginatorPage - 1 ) * planListForm.paginator.limit + status.index + 1 }</td><!-- 原planForm -->
													<td class="center"><c:out value="${record.planNid }"></c:out></td>
													<td class="center">${record.planName }</td>
													<!-- 还款方式 汇计划三期新增 -->
													<td class="center">
														<c:if test="${record.borrowStyle eq 'endday'}">
															按天计息，到期还本还息
														</c:if>
														<c:if test="${record.borrowStyle eq 'end'}">
															按月计息，到期还本还息
														</c:if>
													</td>								
													<td class="center"><c:out value="${record.lockPeriod }"></c:out>
														<c:if test="${record.isMonth eq 0}">
															天
														</c:if>
														<c:if test="${record.isMonth eq 1}">
															个月
														</c:if>
													</td>
													<td class="center"><c:out value="${record.expectApr }"></c:out>%</td>										
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.availableInvestAccount }">
																<fmt:formatNumber type="number" value="${record.availableInvestAccount }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<!-- 还款方式 汇计划三期新增 -->
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.joinTotal }">
																<fmt:formatNumber type="number" value="${record.joinTotal }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.repayWaitAll }">
																<fmt:formatNumber type="number" value="${record.repayWaitAll }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:if test="${record.planInvestStatus eq 0}">
															全部
														</c:if>
														<c:if test="${record.planInvestStatus eq 1}">
															启用
														</c:if>
														<c:if test="${record.planInvestStatus eq 2}">
															禁用
														</c:if>
													</td>
													<td class="center">
														<c:if test="${record.planDisplayStatus eq 0}">
															全部
														</c:if>
														<c:if test="${record.planDisplayStatus eq 1}">
															显示
														</c:if>
														<c:if test="${record.planDisplayStatus eq 2}">
															隐藏
														</c:if>
													</td>

													<td class="center">
														<hyjf:datetime value="${record.addTime}"></hyjf:datetime>
													</td>

													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
														
															<shiro:hasPermission name="planlist:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-debtPlanNid="${record.planNid }"
																		data-toggle="tooltip" data-placement="top"
																   data-debtplanstatus="${record.planInvestStatus }" data-original-title="修改">
																	<i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="planlist:MODIFY">
																<a class="btn btn-transparent btn-xs fn-DisplayShow" data-debtplannid="${record.planNid }"
																data-addtime="${record.addTime }" data-debtplanstatus="${record.planInvestStatus }"
																   data-toggle="tooltip" data-placement="top"
																		data-original-title="
																			<c:if test="${record.planInvestStatus eq 1}">
																				禁用
																			</c:if>
																			<c:if test="${record.planInvestStatus eq 2}">
																				启用
																			</c:if>">
																			
																			<c:if test="${record.planInvestStatus eq 1}">
																				禁用
																			</c:if>
																			<c:if test="${record.planInvestStatus eq 2}">
																				启用
																			</c:if>
																</a>
															</shiro:hasPermission>
															<shiro:hasPermission name="planlist:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Display" data-debtplannid="${record.planNid }"
																		data-toggle="tooltip" data-placement="top"
																   		data-addtime="${record.addTime }" data-debtplanstatus="${record.planDisplayStatus }"
																		data-original-title="
																			<c:if test="${record.planDisplayStatus eq 1}">
																				隐藏
																			</c:if>
																			<c:if test="${record.planDisplayStatus eq 2}">
																				显示
																			</c:if>">
																			
																			<c:if test="${record.planDisplayStatus eq 1}">
																				隐藏
																			</c:if>
																			<c:if test="${record.planDisplayStatus eq 2}">
																				显示
																			</c:if>
																</a>
															</shiro:hasPermission>
															
															<!-- 汇计划三期新增 -->
															<shiro:hasPermission name="planlist:VIEW">
																<a class="btn btn-transparent btn-xs fn-OptRecord" data-debtPlanNid="${record.planNid }"
																		data-toggle="tooltip" data-placement="top" data-debtplanstatus="${record.planInvestStatus }" data-original-title="运营记录">运营记录</a>
															</shiro:hasPermission>
							
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																
																	<shiro:hasPermission name="planlist:MODIFY">
																		<li>
																			<a class="fn-Modify" data-debtplanstatus="${record.planInvestStatus }" data-debtplannid="${record.planNid }">修改</a>
																		</li>
																	</shiro:hasPermission>
													<%-- 				<shiro:hasPermission name="planlist:MODIFY">
																		<li>
																			<a class="fn-DisplayShow" data-debtplanstatus="${record.planInvestStatus }" data-debtplannid="${record.planNid }">
																				<c:if test="${record.planInvestStatus eq 1}">
																					启用
																				</c:if>
																				<c:if test="${record.planInvestStatus eq 2}">
																					禁用
																				</c:if>
																			</a>
																		</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="planlist:MODIFY">
																		<li>
																			<a class="fn-Display" data-debtplanstatus="${record.planDisplayStatus }" data-debtplannid="${record.planNid }">
																				<c:if test="${record.planDisplayStatus eq 1}">
																					显示
																				</c:if>
																				<c:if test="${record.planDisplayStatus eq 2}">
																					隐藏
																				</c:if>
																			</a>
																		</li>
																	</shiro:hasPermission> --%>
																	<!-- 汇计划三期新增 -->
																	<shiro:hasPermission name="planlist:VIEW">
																		<a class="btn btn-transparent btn-xs fn-OptRecord" data-debtPlanNid="${record.planNid }"
																				data-toggle="tooltip" data-placement="top" data-debtplanstatus="${record.planInvestStatus }" data-original-title="运营记录">运营记录</a>
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
									<th align="center"><fmt:formatNumber value="${sumOpenAccount }" pattern="#,##0.00#"/></th>
									<th align="center"><fmt:formatNumber value="${sumJoinTotal }" pattern="#,##0.00#"/></th>
									<th align="center"><fmt:formatNumber value="${sumHjhPlan }" pattern="#,##0.00#"/></th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
									<th>&nbsp;</th>
								</tr>
								<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="planlist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${planListForm.paginator}"></hyjf:paginator><!-- 原planForm -->
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="planlist:SEARCH">
				<!-- 隐藏域设计 -->
				<input type="hidden" name="debtPlanNid" id="debtPlanNid" />
				<input type="hidden" name="debtplanstatus" id="debtplanstatus" />
				<input type="hidden" name="planlistNid" id="planlistNid" />
				<input type="hidden" name="PlanlistNidSrch" id="PlanlistNidSrch" />
				<input type="hidden" name="minInvestCounts" id="minInvestCounts" value="${planListForm.minInvestCounts}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${planListForm.paginatorPage}" />
				
				<!-- 检索条件 -->
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${planListForm.planNidSrch}" />
				</div>
				<div class="form-group">
					<label>智投名称</label>
					<input type="text" name="planNameSrch" id="planNameSrch" class="form-control input-sm underline" value="${planListForm.planNameSrch}" />
				</div>
				<div class="form-group">
					<label>服务回报期限</label>
					<input type="text" name="lockPeriodSrch" id="lockPeriodSrch" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="3" class="form-control input-sm underline" value="${planListForm.lockPeriodSrch}" 
					/>
				</div>

				<!-- 显示状态 -->
				<div class="form-group">
					<label>显示状态</label>
					<select name="planDisplayStatusSrch" id="planDisplayStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="1"<c:if test="${planListForm.planDisplayStatusSrch eq '1'}">selected="selected"</c:if>>
							显示</option>
						<option value="2"<c:if test="${planListForm.planDisplayStatusSrch eq '2'}">selected="selected"</c:if>>
							隐藏</option>
					</select>
				</div>

				<!-- 还款方式  汇计划三期新增 -->
				<div class="form-group">
					<label>还款方式</label>
					<select name="borrowStyleSrch" id="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="endday"<c:if test="${planListForm.borrowStyleSrch eq 'endday'}">selected="selected"</c:if>>
							按天计息，到期还本还息</option>
						<option value="end"<c:if test="${planListForm.borrowStyleSrch eq 'end'}">selected="selected"</c:if>>
							按月计息，到期还本还息</option>
					</select>
				</div>

				<div class="form-group">
					<label>智投状态</label>
					<select name="planStatusSrch" id="planStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
							<option value="1"<c:if test="${planListForm.planStatusSrch eq '1'}">selected="selected"</c:if>>
								启用</option>
							<option value="2"<c:if test="${planListForm.planStatusSrch eq '2'}">selected="selected"</c:if>>
								禁用</option>
					</select>
				</div>

				
				<div class="form-group">
					<label>添加时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="addTimeStart" id="start-date-time" class="form-control underline" value="${planListForm.addTimeStart}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="addTimeEnd" id="end-date-time" class="form-control underline" value="${planListForm.addTimeEnd}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/hjhplan/planList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
