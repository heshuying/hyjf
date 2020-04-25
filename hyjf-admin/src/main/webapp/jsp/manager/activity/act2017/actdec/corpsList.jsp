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
		<tiles:putAttribute name="pageTitle" value="双十二组队活动" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">双十二组队活动</h1>
			<span class="mainDescription">双十二组队活动</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="activitylist:VIEW">
			      		<li ><a href="${webRoot}/manager/activity/actdec/balloon/init">周年活动</a></li>
			      	</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li class="active"><a href="${webRoot}/manager/activity/actdec/corps/init">组队活动</a></li>
					</shiro:hasPermission>
			    </ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${corpsListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${corpsListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
					 				<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
											
									<c:if test="${sessionScope.LOGIN_USER_INFO.username=='yanglishuang'}">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-hongbao"
											data-toggle="tooltip" data-placement="bottom" data-original-title="抽奖红包">抽奖红包 <i class="fa"></i></a>
																				
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-pingguo"
											data-toggle="tooltip" data-placement="bottom" data-original-title="抽奖苹果">抽奖苹果 <i class="fa "></i></a>
									</c:if>

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
										<th class="center">手机号</th>
										<th class="center">微信昵称</th>
										<th class="center">所在战队</th>
										<th class="center">队长</th>
										<th class="center">奖励类型</th>
										<th class="center">奖励金额</th>
										<th class="center">奖励发放</th>
										<th class="center">发放时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="10">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(corpsListForm.paginatorPage - 1 ) * corpsListForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><c:out value="${record.winningName }"></c:out></td>
													<td class="center"><c:out value="${record.corpsName }"></c:out></td>
													<td class="center"><c:out value="${record.captainName }"></c:out></td>
													<td class="center"><c:out value="${record.winningType eq 1 ? '红包' : 'iphoneX' }"></c:out></td>
													<td class="center"><c:out value="${record.amount }"></c:out></td>
													<td class="center"><c:out value="${record.type eq 0 ? '未发放' : '已发放'}"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${corpsListForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
					</div>
				</div>
			</div>
		  </div>
		</tiles:putAttribute>
		
<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${corpsListForm.paginatorPage}" />
			<shiro:hasPermission name="activitylist:SEARCH">
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="usernameSrch" class="form-control input-sm underline"  maxlength="80" value="${ corpsListForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>手机号</label>
					<input type="text" name="mobileSrch" class="form-control input-sm underline"  maxlength="80" value="${ corpsListForm.mobileSrch}" />
				</div>
				<div class="form-group" style="display:none ">
					<label>微信昵称</label>
					<input type="text" name="wxNameSrch" class="form-control input-sm underline"  maxlength="80" value="${ corpsListForm.wxNameSrch}" />
				</div>
				<div class="form-group">
					<label>奖励类型</label>
					<select  id="select" class="form-control underline form-select2"  onchange="document.getElementById('couponNameSrch').value=document.getElementById('select').value">
						<option value=""></option>
							<option value="1"
								<c:if test="${1 eq corpsListForm.couponNameSrch}">selected="selected"</c:if>>
								<c:out value="红包"></c:out>
							</option>
							<option value="2"
								<c:if test="${2 eq corpsListForm.couponNameSrch}">selected="selected"</c:if>>
								<c:out value="苹果X"></c:out>
							</option>
					</select>
					<input type="text" style="display:none" id="couponNameSrch" name="couponNameSrch" class="form-control input-sm underline"  maxlength="80" value="${ corpsListForm.couponNameSrch}" />
				</div>
				<div  style="display:none" class="form-group">
					<label>奖励发放</label>
					<select  id="select2" class="form-control underline form-select2"  onchange="document.getElementById('typeSrch').value=document.getElementById('select2').value">
						<option value=""></option>
							<option value="0"
								<c:if test="${0 eq corpsListForm.couponNameSrch}">selected="selected"</c:if>>
								<c:out value="未发放"></c:out>
							</option>
							<option value="1"
								<c:if test="${1 eq corpsListForm.couponNameSrch}">selected="selected"</c:if>>
								<c:out value="已发放"></c:out>
							</option>
					</select>
					<input type="text" name="typeSrch" id="typeSrch" style="display:none"  class="form-control input-sm underline"  maxlength="20" value="${ corpsListForm.typeSrch}" />
				</div>
				<div class="form-group">
				<label>发放时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${corpsListForm.timeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${corpsListForm.timeEndSrch}" />
				</div>
			</div>
			</shiro:hasPermission>
		</tiles:putAttribute>	
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/act2017/actdec/corpsList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
