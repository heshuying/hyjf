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


<shiro:hasPermission name="offline_rechargelist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="用户线下充值列表" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">用户线下充值列表</h1>
			<span class="mainDescription">本功能可以查询指定用户的线下充值列表信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="offline_rechargelist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<shiro:hasPermission name="offline_rechargelist:EXPORT">
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
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">真实姓名</th>
										<th class="center">手机号码</th>
										<th class="center">电子账号</th>
										<th class="center">交易金额</th>
										<th class="center">交易后余额</th>
										<th class="center">交易日期</th>
										<th class="center">交易时间</th>
										<th class="center">状态</th>
										<th class="center">交易描述</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty rechargelistForm.recordList}">
											<tr><td colspan="19">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${rechargelistForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${status.index+1 }</td>
													<td class="center"><c:out value="${truename }"></c:out></td>
													<td class="center"><c:out value="${mobile }"></c:out></td>
													<td class="center"><c:out value="${record.accountId }"></c:out></td>
													<td class="center"><c:out value="${record.txAmount }"></c:out></td>
													<td class="center"><c:out value="${record.currBal}"></c:out></td>
													<td class="center"><c:out value="${record.inpDate }"></c:out></td>
													<td class="center"><c:out value="${record.inpTime }"></c:out></td>
													<td class="center">
														<c:choose>
															<c:when test="${record.orFlag eq 'O' }">
																原始交易
															</c:when>
															<c:when test="${record.orFlag eq 'R' }">
																冲正或撤销
															</c:when>
															<c:otherwise>
																-
															</c:otherwise>
														</c:choose>
													</td>
													<td class="center"><c:out value="${record.describe }"></c:out></td>
													
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="offline_rechargelist:SEARCH">
				<input type="hidden" name="ids" id="ids" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名:</label>
					<input type="text" name="usernameSrch" class="form-control input-sm underline"  maxlength="20" value="${ rechargelistForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>手机号码:</label>
					<input type="text" name="phoneSrch" class="form-control input-sm underline" maxlength="20" value="${rechargelistForm.phoneSrch}"/>
				</div>
				<div class="form-group">
					<label>电子账号:</label>
					<input type="text" name="bankOpenAccountSrch" class="form-control input-sm underline" maxlength="20" value="${rechargelistForm.bankOpenAccountSrch}"/>
				</div>
				
				<div class="form-group">
					<label>交易时间:</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="startTimeSrch" id="start-date-time" class="form-control underline" value="${rechargelistForm.startTimeSrch}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<span class="input-icon">
							<input type="text" name="endTimeSrch" id="end-date-time" class="form-control underline" value="${rechargelistForm.endTimeSrch}" />
						</span>
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
			<script type='text/javascript' src="${webRoot}/jsp/offline-recharge/rechargeList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
