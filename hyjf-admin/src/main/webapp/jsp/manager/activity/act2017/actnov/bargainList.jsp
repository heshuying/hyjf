<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="十月份出借奖励活动" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">十月份出借奖励活动</h1>
			<span class="mainDescription">十月份出借奖励活动</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="activitylist:VIEW">
			      		<li><a href="${webRoot}/manager/activity/nov/actDrawGuess/init">我画你猜</a></li>
			      	</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li><a href="${webRoot}/manager/activity/nov/actFightLuck/init">拼手气</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li><a href="${webRoot}/manager/activity/actnov/bargain/initPrizeWin">砍价（中奖用户）</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li class="active"><a href="${webRoot}/manager/activity/actnov/bargain/initBargain">砍价（砍价用户）</a></li>
					</shiro:hasPermission>
			    </ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${bargainListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${bargainListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								   
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							<br/>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">微信号</th>
										<th class="center">微信昵称</th>
										<th class="center">帮砍人微信号</th>
										<th class="center">帮砍人微信昵称</th>
										<th class="center">砍价金额</th>
										<th class="center">手机号</th>
										<th class="center">砍价时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="8">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(bargainListForm.paginatorPage - 1 ) * bargainListForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.wechatName }"></c:out></td>
													<td class="center"><c:out value="${record.wechatNickname }"></c:out></td>
													<td class="center"><c:out value="${record.wechatNameHelp }"></c:out></td>
													<td class="center"><c:out value="${record.wechatNicknameHelp }"></c:out></td>
													<td class="center"><c:out value="${record.moneyBargain }"></c:out></td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.updateTime }"></hyjf:datetime></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchBargainAction" paginator="${bargainListForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
					</div>
				</div>
			</div>
		  </div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${bargainListForm.paginatorPage}" />
			<shiro:hasPermission name="activitylist:SEARCH">
				<div class="form-group">
					<label>微信号</label>
					<input type="text" name="wechatNameSrch" class="form-control input-sm underline"  maxlength="80" value="${ bargainListForm.wechatNameSrch}" />
				</div>
				<div class="form-group">
					<label>微信昵称</label>
					<input type="text" name="wechatNickNameSrch" class="form-control input-sm underline"  maxlength="80" value="${ bargainListForm.wechatNickNameSrch}" />
				</div>
				<div class="form-group">
					<label>帮砍人微信号</label>
					<input type="text" name="wechatNameHelpSrch" class="form-control input-sm underline"  maxlength="80" value="${ bargainListForm.wechatNameHelpSrch}" />
				</div>
				<div class="form-group">
					<label>帮砍人微信昵称</label>
					<input type="text" name="wechatNickNameHelpSrch" class="form-control input-sm underline"  maxlength="80" value="${ bargainListForm.wechatNickNameHelpSrch}" />
				</div>
				<div class="form-group">
					<label>砍价金额</label>
					<input type="text" name="bargainMoneySrch" class="form-control input-sm underline"  maxlength="20" value="${ bargainListForm.bargainMoneySrch}" />
				</div>
				<div class="form-group">
					<label>手机号</label>
					<input type="text" name="mobileSrch" class="form-control input-sm underline"  maxlength="20" value="${ bargainListForm.mobileSrch}" />
				</div>
				<div class="form-group">
				<label>砍价时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${couponUserForm.timeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${couponUserForm.timeEndSrch}" />
				</div>
			</div>
			</shiro:hasPermission>
		</tiles:putAttribute>	
		
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/act2017/actnov/bargainList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
