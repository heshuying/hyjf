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


<shiro:hasPermission name="pushMoneyListHJH:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="提成管理" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">提成管理</h1>
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
							<c:set var="jspPrevDsb" value="${pushMoneyForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb" value="${pushMoneyForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a
									class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
							<shiro:hasPermission name="pushMoneyListHJH:EXPORT">
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
									<th class="center">智投订单号</th>
									<th class="center">智投编号</th>
									<th class="center">服务回报期限</th>
									<th class="center">参考年回报率</th>
									<th class="center">提成人</th>
									<th class="center">提成人属性(出借时)</th>
									<th class="center">出借人</th>
									<th class="center">授权服务金额</th>
									<th class="center">提成金额</th>
									<th class="center">提成发放状态</th>
									<th class="center">提成发放时间</th>
									<th class="center">智投订单锁定时间</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty pushMoneyForm.recordList}">
										<tr>
											<td colspan="18">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${pushMoneyForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((pushMoneyForm.paginatorPage - 1) * pushMoneyForm.paginator.limit) + 1 }"></c:out></td>
												<td class="center"><c:out value="${record.ordid }"></c:out></td> <!-- 加入订单号 -->
												<td class="center"><c:out value="${record.borrowNid }"></c:out></td><!-- 计划编号 -->
												<td class="center"><!-- 锁定期 -->
													<c:out value="${record.lockPeriod }"></c:out>
													<c:choose>
														<c:when test="${record.isMonth eq 0 }">天</c:when>
														<c:when test="${record.isMonth eq 1 }">个月</c:when>
													</c:choose>
												</td>
												<td class="center"><c:out value="${record.expectApr }"></c:out>%</td><!-- 预期出借利率率 -->
												<td class="center"><c:out value="${record.username }"></c:out></td> <!-- 提成人 -->
												<!-- 提成人用户属性 -->
												<td class="center">
												<c:if test="${record.attribute eq 0}"><c:out value = "无主单"></c:out></c:if>
												<c:if test="${record.attribute eq 1}"><c:out value = "有主单"></c:out></c:if>
												<c:if test="${record.attribute eq 2}"><c:out value = "线下员工"></c:out></c:if>
												<c:if test="${record.attribute eq 3}"><c:out value = "线上员工"></c:out></c:if> 
												</td> 
												<td class="center"><c:out value="${record.usernameTender }"></c:out></td> 
												<td class="text-right"><fmt:formatNumber value="${record.accountTender}" type="number" pattern="#,##0.00#" /></td><!-- 加入金额 -->
												<td class="text-right"><fmt:formatNumber value="${record.commission}" type="number" pattern="#,##0.00#" /> </td><!-- 提成金额 -->
												<td class="center"><!-- 提成发放状态 -->
													<c:choose>
														<c:when test="${record.status=='1' }">已发放</c:when>
														<c:otherwise>未发放</c:otherwise>
													</c:choose>
												</td>
												<td class="center"><c:out value="${record.sendTimeView }"></c:out></td><!-- 提成发放时间 -->
												<td class="center"><c:out value="${record.countInterestTimeView }"></c:out></td><!-- 计划订单锁定时间 -->
												<td class="center">
													<shiro:hasPermission name="pushMoneyListHJH:CONFIRM">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<c:choose>
																<c:when test="${record.status=='1' }"><span class="text-red">已发放</span></c:when>
																<c:otherwise>
																	<c:if test="${record.commission > 0 }">
																		<a class="btn btn-transparent btn-xs fn-ConfirmShow" 
																		data-id="${record.id }"    
																		data-planorderid="${record.ordid }"    
																		data-plannid="${record.borrowNid }"        
																		data-borrowstyle="${record.isMonth }"
																		data-lockperiod="${record.lockPeriod }" 
																		data-expectapr="${record.expectApr }" 
																		data-username="${record.username }" 
																		data-commission="${record.commission }" 
																		data-countInteresttime="${record.countInterestTimeView }" 
																		data-toggle="tooltip" tool-placement="top" data-original-title="发提成">发提成</a>
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
																		<c:otherwise><c:if test="${record.commission > 0 }"><li><a class="fn-Confirm" data-id="${record.id }">发提成</a></li></c:if></c:otherwise>
																	</c:choose>
																</ul>
															</div>
														</div>
													</shiro:hasPermission>
												</td>
											</tr>
										</c:forEach>
										<tr>
									<td class="center">总计</th>
									<td class="center"></td>
									<td class="center"></td>
									<td class="center"></td>
									<td class="center"></td>
									<td class="center"></td>
									<td class="center"></td>
									<td class="center"></td>
									<td class="center">${pushMoneyTotle.tenderTotle}</td>
									<td class="center">${pushMoneyTotle.commissionTotle}</td>
									<td class="center"></td>
									<td class="center"></td>
									<td class="center"></td>
									<td class="center"></td>
								</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="pushMoneyList" paginator="${pushMoneyForm.paginator}"></hyjf:paginator>
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
		<input type="hidden" name="ordid" id="ordid" />
		<input type="hidden" name="borrowNid" id="borrowNid" />
		<input type="hidden" name="borrowStyleHjh" id="borrowStyleHjh" />
		<input type="hidden" name="lockPeriod" id="lockPeriod" />
		<input type="hidden" name="expectApr" id="expectApr" />
		<input type="hidden" name="username" id="username" />
		<input type="hidden" name="commission" id="commission" />
		<input type="hidden" name="countInterestTimeView" id="countInterestTimeView" />
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${pushMoneyForm.paginatorPage}" />
		<!-- 查询条件 -->
		<div class="form-group">
			<label>智投订单号</label> <input type="text" name="accedeOrderIdSearch" class="form-control input-sm underline" value="${pushMoneyForm.ordid}" />
		</div>
		<div class="form-group">
			<label>智投编号</label> <input type="text" name="borrowNidSearch" class="form-control input-sm underline" value="${pushMoneyForm.borrowNidSearch}" datatype="s20-20" errormsg="项目编号限制20位长度！"/>
		</div>
		<div class="form-group">
			<label>提成发放时间</label>
			<div class="input-group input-daterange datepicker">
				<span class="input-icon">
				<input type="text" name="startDateSend" id="start-date-time-send" class="form-control input-sm underline" value="${pushMoneyForm.startDateSend}" />
				<i class="ti-calendar"></i> </span>
				<span class="input-group-addon no-border bg-light-orange">~</span>
				<span class="input-icon">
				<input type="text" name="endDateSend" id="end-date-time-send" class="form-control input-sm underline" value="${pushMoneyForm.endDateSend}" />
				<i class="ti-calendar"></i> </span>
			</div>
		</div>
		<div class="form-group">
			<label>提成人</label> <input type="text" name="referernameSearch" class="form-control input-sm underline" value="${pushMoneyForm.referernameSearch}" />
		</div>
		<div class="form-group">
			<label>出借人</label> <input type="text" name="usernameSearch" class="form-control input-sm underline" value="${pushMoneyForm.usernameSearch}" />
		</div>
	<%-- 	<div class="form-group">
			<label>电子账号</label> <input type="text" name="accountId" class="form-control input-sm underline" value="${pushMoneyForm.accountId}" />
		</div>
		 --%>
		<div class="form-group">
			<label>提成发放状态</label>
			<select name="statusSearch" class="form-control underline form-select2">
				<option value=""></option>
				<option value="1"
					<c:if test="${'1' eq pushMoneyForm.statusSearch and (!empty pushMoneyForm.statusSearch)}">selected="selected"</c:if>>
					<c:out value="已发放"></c:out>
				</option>
				<option value="0"
					<c:if test="${'0' eq pushMoneyForm.statusSearch and (!empty pushMoneyForm.statusSearch)}">selected="selected"</c:if>>
					<c:out value="未发放"></c:out>
				</option>
			</select>
		</div>
		<%----%>
		<div class="form-group">
			<label>智投订单锁定时间</label>
			<div class="input-group input-daterange datepicker" >
				<span class="input-icon">
				<input type="text" name="startDatePlan" id="start-date-time-Plan" class="form-control underline" value="${pushMoneyForm.startDatePlan}" />
				<i class="ti-calendar"></i> </span>
				<span class="input-group-addon no-border bg-light-orange">~</span>
				<span class="input-icon">
				<input type="text" name="endDatePlan" id="end-date-time-Plan" class="form-control underline" value="${pushMoneyForm.endDatePlan}" />
				<i class="ti-calendar"></i> </span>
			</div>
		</div> 
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/finance/pushmoneyhjh/pushMoneyList.js"></script>
	</tiles:putAttribute>
		
	<tiles:putAttribute name="dialogPanels" type="string">
		<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
	</tiles:putAttribute>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
	</tiles:putAttribute>
		
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
