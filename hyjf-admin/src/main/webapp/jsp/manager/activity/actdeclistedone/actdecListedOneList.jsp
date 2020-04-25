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
		<tiles:putAttribute name="pageTitle" value="2018上市活动1" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">2018上市活动1</h1>
			<span class="mainDescription">上市活动。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="activitylist:VIEW">
				      		<li class="active"><a href="${webRoot}/manager/activity/actdeclistedone/init">活动一</a></li>
				      	</shiro:hasPermission>
						<shiro:hasPermission name="activitylist:VIEW">
				      		<li><a href="${webRoot}/manager/activity/actdeclistedtwo/init">活动二</a></li>
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
									<c:set var="jspPrevDsb" value="${actdecListedOneForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${actdecListedOneForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
										<th class="center">授权服务金额</th>
										<th class="center">出借年化额</th>
										<th class="center">标的编号</th>
										<th class="center">订单号</th>
										<th class="center">首尾笔订单</th>
										<th class="center">获得奖励</th>
										<th class="center">是否领取</th>
										<th class="center">操作时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<jsp:useBean id="myDate" class="java.util.Date"/> 
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(actdecListedOneForm.paginatorPage -1 ) * actdecListedOneForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.userName }"/></td>
													<td class="center"><c:out value="${record.userTureName }"/></td>
													<td align="center"><c:out value="${record.userMobile }" /></td>
													<td align="center"><c:out value="${record.investment/100 }" /></td>
													<td align="center"><c:out value="${record.annual/100 }" /></td>
													<td align="center"><c:out value="${record.number }" /></td>
													<td align="center"><c:out value="${record.orderNumber }" /></td>
													<td class="center">
														<c:choose>
															<c:when test="${record.type eq 0}">首</c:when>
															<c:when test="${record.type eq 1}">尾</c:when>
															<c:when test="${record.type eq 2}">首尾</c:when>
														</c:choose>
													</td>
													<td align="center"><c:out value="${record.reward }" /></td>
													<td class="center">
														<c:choose>
															<c:when test="${record.whether eq 0}">未领取</c:when>
															<c:when test="${record.whether eq 1}">已领取</c:when>
														</c:choose>
													</td>
													<c:set target="${myDate}" property="time" value="${record.createTime * 1000}"/> 												
													<td class="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${myDate}" type="both"/></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${actdecListedOneForm.paginator}"></hyjf:paginator>
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
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${actdecListedOneForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userNameSrch" class="form-control input-sm underline" value="${actdecListedOneForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>姓名</label>
					<input type="text" name="userTureNameSrch" class="form-control input-sm underline" value="${actdecListedOneForm.userTureNameSrch}" />
				</div>
				<div class="form-group">
					<label>手机号</label>
					<input type="text" name="userMobileSrch" class="form-control input-sm underline" value="${actdecListedOneForm.userMobileSrch}" />
				</div>
				<div class="form-group">
					<label>标的编号</label>
					<input type="text" name="numberSrch" class="form-control input-sm underline" value="${actdecListedOneForm.numberSrch}" />
				</div>
				<div class="form-group">
					<label>订单号</label>
					<input type="text" name="orderNumberSrch" class="form-control input-sm underline" value="${actdecListedOneForm.orderNumberSrch}" />
				</div>
				<div class="form-group">
					<label>获得奖励</label>
					<input type="text" name="rewardSrch" class="form-control input-sm underline" value="${actdecListedOneForm.rewardSrch}" />
				</div>
				<div class="form-group">
					<label>是否领取</label>
					<select name="whetherSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${actdecListedOneForm.whetherSrch eq '0'}">selected="selected"</c:if>>未领取</option>
						<option value="1" <c:if test="${actdecListedOneForm.whetherSrch eq '1'}">selected="selected"</c:if>>已领取</option>
					</select>
				</div>
				<div class="form-group">
					<label>时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="createTimeStartSrch" id="start-date-time" class="form-control underline" value="${actdecListedOneForm.createTimeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="createTimeEndSrch" id="end-date-time" class="form-control underline" value="${actdecListedOneForm.createTimeEndSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/actdeclistedone/actdecListedOneList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
