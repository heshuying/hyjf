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
		<tiles:putAttribute name="pageTitle" value="七月份推荐活动" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">七月份推荐活动</h1>
			<span class="mainDescription">七月份推荐活动</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${inviteSevenListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${inviteSevenListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
										<th class="center">用户名</th>
										<th class="center">姓名</th>
										<th class="center">手机号</th>
										<th class="center">被邀请人用户名</th>
										<th class="center">首投金额</th>
										<th class="center">出借时间</th>
										<th class="center">奖励</th>
										<th class="center">发放状态</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="9">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(inviteSevenListForm.paginatorPage - 1 ) * inviteSevenListForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.username }"></c:out></td>
													<td class="center"><c:out value="${record.userRealname }"></c:out></td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<c:if test="${record.rewardType eq 3}">
														<td class="center"></td>
														<td class="center"></td>
														<td class="center"></td>
													</c:if>
													<c:if test="${record.rewardType != 3}">
														<td class="center"><c:out value="${record.usernameInvited }"></c:out></td>
														<td class="center"><c:out value="${record.moneyFirst }"></c:out></td>
														<td class="center"><hyjf:datetime value="${record.investTime }"></hyjf:datetime></td>
													</c:if>
													<td class="center"><c:out value="${record.couponName }"></c:out></td>
													<td class="center"><c:out value="${record.sendFlg eq '0' ? '未发放' : '已发放' }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${inviteSevenListForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
					</div>
				</div>
			</div>
		  </div>
		</tiles:putAttribute>
		
				<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${inviteSevenListForm.paginatorPage}" />
			<shiro:hasPermission name="activitylist:SEARCH">
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="usernameSrch" class="form-control input-sm underline"  maxlength="20" value="${ inviteSevenListForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>手机号</label>
					<input type="text" name="mobileSrch" class="form-control input-sm underline"  maxlength="20" value="${ inviteSevenListForm.mobileSrch}" />
				</div>
				<div class="form-group">
					<label>奖励</label>
					<input type="text" name="couponNameSrch" class="form-control input-sm underline"  maxlength="20" value="${ inviteSevenListForm.couponNameSrch}" />
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>	
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/inviteseven/inviteseven.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
