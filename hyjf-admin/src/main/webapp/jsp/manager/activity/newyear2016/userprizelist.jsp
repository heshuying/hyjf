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


<shiro:hasPermission name="activitylist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="新年活动奖励发放" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">奖励发放</h1>
			<span class="mainDescription">财神来敲我家门和红红火火闹元宵活动奖励发放明细。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="activitylist:VIEW">
			      		<li><a href="${webRoot}/newyear2016/caiShenListInit">财神来敲我家门</a></li>
			      	</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li ><a href="${webRoot}/newyear2016/yuanXiaoListInit">红红火火闹元宵</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li class="active"><a href="javascript:void(0);">奖励发放</a></li>
					</shiro:hasPermission>
			    </ul>
			    
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<shiro:hasPermission name="activitylist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${userPrizeListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${userPrizeListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="activitylist:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 角色列表一览 --%>
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
										<th class="center">活动来源</th>
										<th class="center">优惠券编号</th>
										<th class="center">优惠券面额</th>
										<th class="center">发放时间</th>
										<th class="center">状态</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty userPrizeListForm.recordList}">
											<tr><td colspan="9">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${userPrizeListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(userPrizeListForm.paginatorPage-1)*userPrizeListForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.username }"></c:out></td>
													<td class="center"><c:out value="${record.truename }"></c:out></td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><c:out value="${record.activity }"></c:out></td>
													<td class="center"><c:out value="${record.couponCode }"></c:out></td>
													<td class="center"><c:out value="${record.couponJine }"></c:out></td>
													<td class="center"><c:out value="${record.sendTime }"></c:out></td>
													<td class="center"><c:out value="${record.sendStatusStr }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchUserPrizeAction" paginator="${userPrizeListForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="activitylist:SEARCH">
				<input type="hidden" name="ids" id="ids" />
				<input type="hidden" name="primaryKey" id="primaryKey" value= "${userPrizeListForm.primaryKey}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${userPrizeListForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="usernameSrch" class="form-control input-sm underline"  maxlength="20" value="${ userPrizeListForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>姓名</label>
					<input type="text" name="truenameSrch" class="form-control input-sm underline" maxlength="20" value="${userPrizeListForm.truenameSrch}"/>
				</div>
				<div class="form-group">
					<label>手机号</label>
					<input type="text" name="mobileSrch" class="form-control input-sm underline" maxlength="20" value="${userPrizeListForm.mobileSrch}"/>
				</div>
				<div class="form-group">
					<label>活动来源</label>
					<select name="activitySrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="1"
							<c:if test="${1 eq userPrizeListForm.activitySrch}">selected="selected"</c:if>>
							财神来敲我家门
						</option>
						<option value="2"
							<c:if test="${2 eq userPrizeListForm.activitySrch}">selected="selected"</c:if>>
							红红火火闹元宵
						</option>
					</select>
				</div>
				<div class="form-group">
					<label>优惠券面额</label>
					<input type="text" name="couponJineSrch" class="form-control input-sm underline" maxlength="20" value="${userPrizeListForm.couponJineSrch}"/>
				</div>
				<div class="form-group">
					<label>发放时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="timeStartSrch" class="form-control underline" value="${userPrizeListForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="timeEndSrch" class="form-control underline" value="${userPrizeListForm.timeEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>状态</label>
					<select name="sendStatusSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="0"
							<c:if test="${0 eq userPrizeListForm.sendStatusSrch}">selected="selected"</c:if>>
							失败
						</option>
						<option value="1"
							<c:if test="${1 eq userPrizeListForm.sendStatusSrch}">selected="selected"</c:if>>
							成功
						</option>
					</select>
				</div>
			</shiro:hasPermission>
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
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/newyear2016/userprizelist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
