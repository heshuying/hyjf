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

<shiro:hasPermission name="planmanager:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="计划列表" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">计划列表</h1>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="planmanager:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${planForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${planForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="planmanager:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
												data-toggle="tooltip" data-placement="bottom" data-original-title="添加智投">添加智投 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<shiro:hasPermission name="planmanager:EXPORT">
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
										<th class="center">智投名称</th>
										<th class="center">智投类型</th>
										<th class="center">服务回报期限</th>
										<th class="center">参考年回报率</th>
										<th class="center">计划金额</th>
										<th class="center">授权服务金额</th>
										<th class="center">计划可用余额</th>
										<th class="center">状态</th>
										<th class="center">发起时间</th>
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
													<td class="center"><c:out value="${record.debtPlanNid }"></c:out></td>
													<td class="center">
														<%-- <c:choose>--%>
															<%-- <c:when test="${record.debtPlanStatus eq '0' or record.debtPlanStatus eq '1' or record.debtPlanStatus eq '2' }"> --%>
																${record.debtPlanName }
															<%-- </c:when>
															<c:otherwise>
																<a target="_blank" href="https://www.hyjf.com/plan/getPlanPreview.do?planNid=${record.debtPlanNid }">${record.debtPlanName }</a>
															</c:otherwise> --%>
														<%--</c:choose>--%>
													</td>
													<td class="center"><c:out value="${record.debtPlanTypeName }"></c:out></td>
													<td class="center"><c:out value="${record.debtLockPeriod }"></c:out>个月</td>
													<td class="center"><c:out value="${record.expectApr }"></c:out>%</td>
													<td class="center">
													<fmt:formatNumber type="number" value="${record.debtPlanMoney }" /> 
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.debtPlanMoneyYes }">
																<fmt:formatNumber type="number" value="${record.debtPlanMoneyYes }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:choose>
															<c:when test="${not empty record.debtPlanBalance }">
																<fmt:formatNumber type="number" value="${record.debtPlanBalance }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center">
														<c:if test="${record.debtPlanStatus eq 0}">
															发起中
														</c:if>
														<c:if test="${record.debtPlanStatus eq 1}">
															待审核
														</c:if>
														<c:if test="${record.debtPlanStatus eq 2}">
															审核不通过
														</c:if>
														<c:if test="${record.debtPlanStatus eq 3}">
															待开放
														</c:if>
														<c:if test="${record.debtPlanStatus eq 4}">
															募集中
														</c:if>
														<c:if test="${record.debtPlanStatus eq 5}">
															锁定中
														</c:if>
														<c:if test="${record.debtPlanStatus eq 6}">
															清算中
														</c:if>
														<c:if test="${record.debtPlanStatus eq 7}">
															清算中
														</c:if>
														<c:if test="${record.debtPlanStatus eq 8}">
															清算完成
														</c:if>
														<c:if test="${record.debtPlanStatus eq 9}">
															未还款
														</c:if>
														<c:if test="${record.debtPlanStatus eq 10}">
															还款中
														</c:if>
														<c:if test="${record.debtPlanStatus eq 11}">
															还款完成
														</c:if>
														<c:if test="${record.debtPlanStatus eq 12}">
															流标
														</c:if>
													</td>
													<td class="center">
														<hyjf:datetimeformat value="${record.createTime}"></hyjf:datetimeformat>
													</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="planmanager:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-debtPlanNid="${record.debtPlanNid }"
																		data-toggle="tooltip" data-placement="top" data-debtplanstatus="${record.debtPlanStatus }" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="planmanager:PREVIEW">
															<a class="btn btn-transparent btn-xs" target="_blank" href="https://www.hyjf.com/plan/getPlanPreview.do?planNid=${record.debtPlanNid }" 
																	data-toggle="tooltip" data-placement="top" data-original-title="预览"><i class="ti-layers-alt"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="planmanager:MODIFY">
																		<li>
																			<a class="fn-Modify" data-debtplanstatus="${record.debtPlanStatus }" data-debtplannid="${record.debtPlanNid }">修改</a>
																		</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="planmanager:PREVIEW">
																		<li>
																			<a target="_blank" href="https://www.hyjf.com/plan/getPlanPreview.do?planNid=${record.debtPlanNid }" >预览</a>
																		</li>
																	</shiro:hasPermission>
																</ul>
																
															</div>
														</div>
													</td>
												</tr>
											</c:forEach>
											<tr>
													<td>总计</td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td align="right">
														<c:choose>
															<c:when test="${not empty planMoneySum }">
																<fmt:formatNumber type="number" value="${planMoneySum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td align="right">
														<c:choose>
															<c:when test="${not empty planJoinMoneySum }">
																<fmt:formatNumber type="number" value="${planJoinMoneySum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td align="right">
														<c:choose>
															<c:when test="${not empty planMoneyWaitSum }">
																<fmt:formatNumber type="number" value="${planMoneyWaitSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td></td>
													<td></td>
													<td></td>
												</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="planmanager:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${planForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="planmanager:SEARCH">
				<input type="hidden" name="moveFlag" id="moveFlag" value="PLAN_LIST"/>
				<input type="hidden" name="debtPlanNid" id="debtPlanNid" />
				<input type="hidden" name="debtPlanNidSrch" id="debtPlanNidSrch" />
				<input type="hidden" name="debtPlanStatus" id="debtPlanStatus" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${planForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${planForm.planNidSrch}" />
				</div>
				<div class="form-group">
					<label>智投名称</label>
					<input type="text" name="planNameSrch" id="planNameSrch" class="form-control input-sm underline" value="${planForm.planNameSrch}" />
				</div>
				<div class="form-group">
					<label>智投类型</label>
						<select name="planTypeSrch" id="planTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${planTypeList }" var="planType" begin="0" step="1" varStatus="status">
							<option value="${planType.debtPlanType }"
								<c:if test="${planType.debtPlanType eq planForm.planTypeSrch}">selected="selected"</c:if>>
								<c:out value="${planType.debtPlanTypeName }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>状态</label>
					<select name="planStatusSrch" id="planStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
							<option value="0"<c:if test="${planForm.planStatusSrch eq '0'}">selected="selected"</c:if>>
								发起中</option>
							<option value="1"<c:if test="${planForm.planStatusSrch eq '1'}">selected="selected"</c:if>>
								待审核</option>
							<option value="2"<c:if test="${planForm.planStatusSrch eq '2'}">selected="selected"</c:if>>
								审核不通过</option>
							<option value="3"<c:if test="${planForm.planStatusSrch eq '3'}">selected="selected"</c:if>>
								待开放</option>
							<option value="4"<c:if test="${planForm.planStatusSrch eq '4'}">selected="selected"</c:if>>
								募集中</option>
							<option value="5"<c:if test="${planForm.planStatusSrch eq '5'}">selected="selected"</c:if>>
								锁定中</option>
							<option value="6"<c:if test="${planForm.planStatusSrch eq '6'}">selected="selected"</c:if>>
								清算中</option>
							<option value="7"<c:if test="${planForm.planStatusSrch eq '7'}">selected="selected"</c:if>>
								清算完成</option>
							<option value="8"<c:if test="${planForm.planStatusSrch eq '8'}">selected="selected"</c:if>>
								未还款</option>
							<option value="9"<c:if test="${planForm.planStatusSrch eq '9'}">selected="selected"</c:if>>
								还款中</option>
							<option value="10"<c:if test="${planForm.planStatusSrch eq '10'}">selected="selected"</c:if>>
								还款完成</option>
							<option value="11"<c:if test="${planForm.planStatusSrch eq '11'}">selected="selected"</c:if>>
								流标</option>
					</select>
				</div>
				<div class="form-group">
					<label>发起时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${planForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
							<span class="input-group-addon no-border bg-light-orange">~</span>
							<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${planForm.timeEndSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/plan/planList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
