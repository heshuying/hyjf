<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>


<shiro:hasPermission name="pushMoneyManage:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="提成明细" />

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">提成明细</h1>
		<span class="mainDescription">本功能可以查询提成明细和发提成操作。</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<hyjf:message key="pushMoney-error"></hyjf:message>
		<hyjf:message key="pushMoney-success"></hyjf:message>
		<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb" value="${planPushMoneyForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb" value="${planPushMoneyForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a
									class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
							<shiro:hasPermission name="pushMoneyManage:EXPORT">
							<a class="btn btn-o btn-primary btn-sm fn-Export"
									data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-plus "></i> </a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 提现列表一览 --%>
						<table id="pushMoneyList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">提成人</th>
									<th class="center">提成金额</th>
									<th class="center">提成转账订单号</th>
									<th class="center">提成发放时间</th>
									<th class="center">分公司</th>
									<th class="center">分部</th>
									<th class="center">团队</th>
									<th class="center">提成人用户属性</th>
									<th class="center">出借人</th>
									<th class="center">授权服务金额</th>
									<th class="center">智投编号</th>
									<th class="center">智投订单号</th>
									<th class="center">服务回报期限</th>
									<th class="center">授权服务时间</th>
									<th class="center">提成发放状态</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty planPushMoneyForm.pushMoneyDetailList}">
										<tr>
											<td colspan="17">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${planPushMoneyForm.pushMoneyDetailList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((planPushMoneyForm.paginatorPage - 1) * planPushMoneyForm.paginator.limit) + 1 }"></c:out></td>
												<td class="center"><c:out value="${record.userName }"></c:out></td>
												<td class="center"><c:out value="${record.commission }"></c:out></td>
												<td class="center"><c:out value="${record.orderId }"></c:out></td>
												<td class="center">
													<c:if test="${record.returnTime != null && record.returnTime != 0}">
														<hyjf:datetime value="${record.returnTime}"></hyjf:datetime>
													</c:if>
												</td>
												<td class="center"><c:out value="${record.regionName }"></c:out></td>
												<td class="center"><c:out value="${record.branchName }"></c:out></td>
												<td class="center"><c:out value="${record.departmentName }"></c:out></td>
												<td class="center"><c:out value="${record.attribute }"></c:out></td>
												<td class="center"><c:out value="${record.accedeUserName }"></c:out></td>
												<td class="center"><c:out value="${record.accedeAccount }"></c:out></td>
												<td class="center"><c:out value="${record.debtPlanNid }"></c:out></td>
												<td class="center"><c:out value="${record.accedeOrderId }"></c:out></td>
												<td class="center"><c:out value="${record.debtLockPeriod }"></c:out>个月</td>
												<td class="center">
													<hyjf:datetime value="${record.accedeTime}"></hyjf:datetime>
												</td>
												<td class="center">
													<c:if test="${record.status == 0 }">
														<c:out value="未发放"></c:out>
													</c:if>
													<c:if test="${record.status == 1 }">
														<c:out value="已发放"></c:out>
													</c:if>
													<c:if test="${record.status == 2 }">
														<c:out value="发放失败"></c:out>
													</c:if>
												</td>
												<td class="center">
													<shiro:hasPermission name="planPushMoneyDetail:CONFIRM">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<c:choose>
																<c:when test="${record.status=='1' }"><span class="text-red">已发放</span></c:when>
																<c:otherwise>
																	<c:if test="${record.commission > 0 }">
																		<a class="btn btn-transparent btn-xs fn-Confirm" data-id ="${record.id }" data-accedeorderid="${record.accedeOrderId }" data-toggle="tooltip" tool-placement="top" data-original-title="发提成">发提成</a>
																	</c:if>
																</c:otherwise>
															</c:choose>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<c:choose>
																		<c:when test="${record.status=='1' }"><span class="text-red">已发放</span></c:when>
																		<c:otherwise><c:if test="${record.commission > 0 }"><li><a class="fn-Confirm" data-id ="${record.id }" data-accedeorderid="${record.accedeOrderId }">发提成</a></li></c:if></c:otherwise>
																	</c:choose>
																</ul>
															</div>
														</div>
													</shiro:hasPermission>
												</td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="planPushMoneyDetailSearch" paginator="${planPushMoneyForm.paginator}"></hyjf:paginator>
						<br />
						<br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="ids" id="ids" />
		<input type="hidden" name="orderId" id="orderId"/>
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${planPushMoneyForm.paginatorPage}" />
		<!-- 查询条件 -->
		<div class="form-group">
			<label>智投编号</label>
			<input type="text" name="debtPlanNidSrch" class="form-control input-sm underline" value="${planPushMoneyForm.debtPlanNidSrch}"/>
		</div>
		<div class="form-group">
			<label>智投订单号</label>
			<input type="text" name="accedeOrderIdSrch" class="form-control input-sm underline" value="${planPushMoneyForm.accedeOrderIdSrch}"/>
		</div>
		<div class="form-group">
			<label>提成人用户名</label> 
			<input type="text" name="userNameSrch" class="form-control input-sm underline" value="${planPushMoneyForm.userNameSrch}"/>
		</div>
		<div class="form-group">
			<label>提成人部门</label>
			<div class="dropdown-menu no-radius">
				<input type="text" class="form-control input-sm underline margin-bottom-10 " value="" id="combotree_search" placeholder="Search" >
				<input type="hidden" id="combotree_field_hidden"  name="combotreeSrch" value="${planPushMoneyForm.combotreeSrch}">
				<div id="combotree-panel" style="width:270px;height:300px;position:relative;overflow:hidden;">
					<div id="combotree" class="tree-demo" ></div>
				</div>
			</div>

			<span class="input-icon input-icon-right" data-toggle="dropdown" >
				<input id="combotree-field" type="text" class="form-control underline form " readonly="readonly">
				<i class="fa fa-remove fn-ClearDep" style="cursor:pointer;"></i>
			</span>
		</div>
		
		<div class="form-group">
			<label>出借用户名</label> 
			<input type="text" name="accedeUserNameSrch" class="form-control input-sm underline" value="${planPushMoneyForm.accedeUserNameSrch}" />
		</div>
		<div class="form-group">
			<label>提成发放状态</label>
			<select name="statusSrch" class="form-control underline form-select2">
				<option value=""></option>
				<option value="1"
					<c:if test="${'1' eq planPushMoneyForm.statusSrch and (!empty planPushMoneyForm.statusSrch)}">selected="selected"</c:if>>
					<c:out value="已发放"></c:out>
				</option>
				<option value="0"
					<c:if test="${'0' eq planPushMoneyForm.statusSrch and (!empty planPushMoneyForm.statusSrch)}">selected="selected"</c:if>>
					<c:out value="未发放"></c:out>
				</option>
			</select>
		</div>
		<div class="form-group">
			<label>出借时间</label>
			<div class="input-group input-daterange datepicker">
				<span class="input-icon">
				<input type="text" name="accedeStartTimeSearch" id="return-start-date-time" class="form-control underline" value="${planPushMoneyForm.accedeStartTimeSearch}" />
				<i class="ti-calendar"></i> </span>
				<span class="input-group-addon no-border bg-light-orange">~</span>
				<span class="input-icon">
				<input type="text" name="accedeEndTimeSearch" id="return-end-date-time" class="form-control underline" value="${planPushMoneyForm.accedeEndTimeSearch}" />
				<i class="ti-calendar"></i> </span>
			</div>
		</div>
		<div class="form-group">
			<label>提成发放时间</label>
			<div class="input-group input-daterange datepicker">
				<span class="input-icon">
				<input type="text" name="returnStartTimeSearch" id="start-date-time" class="form-control underline" value="${planPushMoneyForm.returnStartTimeSearch}" />
				<i class="ti-calendar"></i> </span>
				<span class="input-group-addon no-border bg-light-orange">~</span>
				<span class="input-icon">
				<input type="text" name="returnEndTimeSearch" id="end-date-time" class="form-control underline" value="${planPushMoneyForm.returnEndTimeSearch}" />
				<i class="ti-calendar"></i> </span>
			</div>
		</div>
	</tiles:putAttribute>
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/finance/planpushmoneydetail/planpushmoneydetaillist.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
