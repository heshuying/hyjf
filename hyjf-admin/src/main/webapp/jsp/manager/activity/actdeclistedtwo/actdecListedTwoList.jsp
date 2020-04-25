<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
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
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<shiro:hasPermission name="activitylist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="2018上市活动2" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">2018上市活动2</h1>
			<span class="mainDescription">上市活动。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="activitylist:VIEW">
				      		<li><a href="${webRoot}/manager/activity/actdeclistedone/init">活动一</a></li>
				      	</shiro:hasPermission>
						<shiro:hasPermission name="activitylist:VIEW">
				      		<li class="active"><a href="${webRoot}/manager/activity/actdeclistedtwo/init">活动二</a></li>
				      	</shiro:hasPermission>
						<shiro:hasPermission name="activitylist:VIEW">
							<li><a href="${webRoot}/manager/activity/actdeclistedthree/init">活动三1</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="activitylist:VIEW">
							<li><a href="${webRoot}/manager/activity/actdeclistedfour/init">活动三2</a></li>
						</shiro:hasPermission>
				    </ul>
					<div class="tab-content">
						<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${actdecListedTwoForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${actdecListedTwoForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="activitylist:EXPORT">
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
										<th class="center">用户名</th>
										<th class="center">姓名</th>
										<th class="center">手机号</th>
										<th class="center">类型</th>
										<th class="center">领取奖励时增投金额</th>
										<th class="center">领取奖励</th>
										<th class="center">操作金额</th>
										<th class="center">当前新增充值金额</th>
										<th class="center">当前新增冲投金额</th>
										<th class="center">操作时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="11">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<jsp:useBean id="myDate" class="java.util.Date"/>
											<jsp:useBean id="createDate" class="java.util.Date"/>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(actdecListedTwoForm.paginatorPage -1 ) * actdecListedTwoForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.userName }"/></td>
													<td class="center"><c:out value="${record.trueName }"/></td>
													<td align="center"><c:out value="${record.mobile }" /></td>
													<td class="center">
														<c:choose>
															<c:when test="${record.trade eq '0'}">领奖</c:when>
															<c:when test="${record.trade eq '1'}">充值</c:when>
															<c:when test="${record.trade eq '2'}">出借</c:when>
															<c:when test="${record.trade eq '3'}">提现</c:when>
														</c:choose>
													</td>
													<td align="center"><fmt:formatNumber value="${record.investedAmount/100 }" pattern="#,##0.00#"/></td>
													<td align="center">
														<c:choose>
															<c:when test="${record.acceptPrize eq 0 }"></c:when>
															<c:when test="${record.acceptPrize eq 20000}">1%加息券*1张</c:when>
															<c:when test="${record.acceptPrize eq 50000}">2%加息券*1张</c:when>
															<c:when test="${record.acceptPrize eq 100000}">3%加息券*1张</c:when>
															<c:when test="${record.acceptPrize eq 200000}">3%加息券*2张</c:when>
														</c:choose>
													</td>
													<td align="center"><fmt:formatNumber value="${record.amount/100 }" pattern="#,##0.00#"/></td>
													<td align="center"><fmt:formatNumber value="${record.cumulativeCharge/100 }" pattern="#,##0.00#"/></td>
													<td align="center"><fmt:formatNumber value="${record.cumulativeInvest/100 }" pattern="#,##0.00#"/></td>
													<c:set target="${createDate}" property="time" value="${record.createTime * 1000}"/>
													<td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${createDate}" type="both"/></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${actdecListedTwoForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="activitylist:SEARCH">
				<input type="hidden" name="export" id="export" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${actdecListedTwoForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userNameSrch" class="form-control input-sm underline" value="${actdecListedTwoForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>姓名</label>
					<input type="text" name="trueNameSrch" class="form-control input-sm underline" value="${actdecListedTwoForm.trueNameSrch}" />
				</div>
				<div class="form-group">
					<label>手机号</label>
					<input type="text" name="mobileSrch" class="form-control input-sm underline" value="${actdecListedTwoForm.mobileSrch}" />
				</div>
				<div class="form-group">
					<label>操作</label>
					<select name="tradeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${actdecListedTwoForm.tradeSrch eq '0'}">selected="selected"</c:if>>领奖</option>
						<option value="1" <c:if test="${actdecListedTwoForm.tradeSrch eq '1'}">selected="selected"</c:if>>充值</option>
						<option value="2" <c:if test="${actdecListedTwoForm.tradeSrch eq '2'}">selected="selected"</c:if>>出借</option>
						<option value="3" <c:if test="${actdecListedTwoForm.tradeSrch eq '3'}">selected="selected"</c:if>>提现</option>
					</select>
				</div>
				<div class="form-group">
					<label>领取奖励</label>
					<select name="acceptPrizeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="20000" <c:if test="${actdecListedTwoForm.acceptPrizeSrch eq 20000}">selected="selected"</c:if>>1%加息券*1张</option>
						<option value="50000" <c:if test="${actdecListedTwoForm.acceptPrizeSrch eq 50000}">selected="selected"</c:if>>2%加息券*1张</option>
						<option value="100000" <c:if test="${actdecListedTwoForm.acceptPrizeSrch eq 100000}">selected="selected"</c:if>>3%加息券*1张</option>
						<option value="200000" <c:if test="${actdecListedTwoForm.acceptPrizeSrch eq 200000}">selected="selected"</c:if>>3%加息券*2张</option>
					</select>
				</div>
				<div class="form-group">
					<label>时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="acceptTimeStartSrch" id="start-date-time" class="form-control underline" value="${actdecListedTwoForm.acceptTimeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="acceptTimeEndSrch" id="end-date-time" class="form-control underline" value="${actdecListedTwoForm.acceptTimeEndSrch}" />
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
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/actdeclistedtwo/actdecListedTwoList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
