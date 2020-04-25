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
		<tiles:putAttribute name="pageTitle" value="邀请会员列表" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">推荐星获取明细列表</h1>
			<span class="mainDescription">本功能可以查询相应的推荐星获取明细信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<!-- <div class="container-fluid container-fullw bg-white"> -->
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="activitylist:VIEW">
						<li  class="active"><a href="javascript:void(0);">春节活动明细</a></li>
					</shiro:hasPermission>
			    </ul>
			    
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<shiro:hasPermission name="activitylist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${getActdecListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${getActdecListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
										<th class="center">手机号</th>
										<th class="center">操作</th>
										<th class="center">操作金额</th>
										<th class="center">当前新增金额</th>
										<th class="center">新增出借余额</th>
										<th class="center">碎片变更</th>
										<th class="center">获得奖励</th>
										<th class="center">累计获得碎片</th>
										<th class="center">可使用碎片</th>
										<th class="center">时间</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty getActdecListForm.recordList}">
											<tr><td colspan="16">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${getActdecListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(getActdecListForm.paginatorPage-1)*getActdecListForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.userMobile }"></c:out></td>
													<td class="center"><c:out value="${record.operType }"></c:out></td>
													<td class="center"><c:out value="${record.operAmount }元"></c:out></td>
													<td class="center"><c:out value="${record.newRecharge }元"></c:out></td>
													<td class="center"><c:out value="${record.newInvestment }元"></c:out></td>
													<td class="center"><c:out value="${record.number }"></c:out></td>
													<td class="center"><c:out value="${record.reward }"></c:out></td>
													<td class="center"><c:out value="${record.totalNumber }"></c:out></td>
													<td class="center"><c:out value="${record.availableNumber }"></c:out></td>
													<td class="center"><c:out value="${record.createTime }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="search" paginator="${getActdecListForm.paginator}"></hyjf:paginator>
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
				<input type="hidden" name="userId" id="userId" value= "${getActdecListForm.userId}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${getActdecListForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userNameSrch" class="form-control input-sm underline"  maxlength="20" value="${ getActdecListForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>手机号</label>
					<input type="text" name="numberSrch" class="form-control input-sm underline" maxlength="20" value="${getActdecListForm.numberSrch}"/>
				</div>
				<div class="form-group">
					<label>操作</label>
					<select name="operTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="1"
							<c:if test="${1 eq getActdecListForm.operTypeSrch}">selected="selected"</c:if>>
							我的加息我做主
						</option>
						<option value="2"
							<c:if test="${2 eq getActdecListForm.operTypeSrch}">selected="selected"</c:if>>
							零元夺宝抽奖机
						</option>
						<option value="3"
							<c:if test="${3 eq getActdecListForm.operTypeSrch}">selected="selected"</c:if>>
							充值
						</option>
						<option value="4"
							<c:if test="${4 eq getActdecListForm.operTypeSrch}">selected="selected"</c:if>>
							提现
						</option>
						<option value="5"
							<c:if test="${5 eq getActdecListForm.operTypeSrch}">selected="selected"</c:if>>
							出借
						</option>
					</select>
				</div>
				<div class="form-group">
					<label>获得奖励</label>
					<select name="rewardSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="1"
							<c:if test="${ getActdecListForm.rewardSrch eq '1'}">selected="selected"</c:if>>
							0.5%加息券
						</option>
						<option value="2"
							<c:if test="${getActdecListForm.rewardSrch eq '2'}">selected="selected"</c:if>>
							1.0%加息券
						</option>
						<option value="3"
							<c:if test="${getActdecListForm.rewardSrch eq '3'}">selected="selected"</c:if>>
							1.5%加息券
						</option>
						<option value="4"
							<c:if test="${getActdecListForm.rewardSrch eq '4'}">selected="selected"</c:if>>
							2.0%加息券
						</option>
						<option value="5"
							<c:if test="${getActdecListForm.rewardSrch eq '5'}">selected="selected"</c:if>>
							1000元京东E卡
						</option>
						<option value="6"
							<c:if test="${getActdecListForm.rewardSrch eq '6'}">selected="selected"</c:if>>
							500元京东E卡
						</option>
						<option value="7"
							<c:if test="${getActdecListForm.rewardSrch eq '7'}">selected="selected"</c:if>>
							200元京东E卡
						</option>
						<%-- <option value="8"
							<c:if test="${getActdecListForm.rewardSrch eq '8'}">selected="selected"</c:if>>
							1%加息券
						</option> --%>
						<option value="9"
							<c:if test="${getActdecListForm.rewardSrch eq '9'}">selected="selected"</c:if>>
							10张金彩碎片
						</option>
						<option value="10"
							<c:if test="${getActdecListForm.rewardSrch eq '10'}">selected="selected"</c:if>>
							抽奖未中奖
						</option>
						<option value="11"
							<c:if test="${ getActdecListForm.rewardSrch eq '11'}">selected="selected"</c:if>>
							充值
						</option>
						<option value="12"
							<c:if test="${getActdecListForm.rewardSrch eq '12'}">selected="selected"</c:if>>
							提现
						</option>
						<option value="13"
							<c:if test="${getActdecListForm.rewardSrch eq '13'}">selected="selected"</c:if>>
							出借
						</option>
					</select>
				</div>
				<div class="form-group">
					<label>时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="timeStartSrch" class="form-control underline" value="${getActdecListForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="timeEndSrch" class="form-control underline" value="${getActdecListForm.timeEndSrch}" />
					</div>
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
			<script type='text/javascript' src="${webRoot}/jsp/invite/ActdecList/ActdecList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
