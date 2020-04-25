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


<shiro:hasPermission name="subCommission:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="账户分佣" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">账户分佣</h1>
			<span class="mainDescription">本功能可以发起账户分佣。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="subCommission:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${subCommissionForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${subCommissionForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<shiro:hasPermission name="subCommission:ADD">
										<a class="btn btn-o btn-primary btn-sm fn-Add"
												data-toggle="tooltip" data-placement="bottom" data-original-title="发起转账">发起转账<i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<shiro:hasPermission name="subCommission:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
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
										<th class="center">转账订单号</th>
										<th class="center">转出电子账户号</th>
										<th class="center">转账金额</th>
										<th class="center">转入用户名</th>
										<th class="center">转入姓名</th>
										<th class="center">转入电子账户号</th>
										<th class="center">转账状态</th>
										<th class="center">转账时间</th>
										<th class="center">操作人</th>
										<th class="center">备注</th>
										<th class="center">发送日期</th>
										<th class="center">发送时间</th>
										<th class="center">系统跟踪号</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty subCommissionForm.recordList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${subCommissionForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(subCommissionForm.paginatorPage-1)*subCommissionForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.accountId }"></c:out></td>
													<td class="left"><fmt:formatNumber value="${record.account}" type="number" pattern="#,##0.00#" /></td>
													<td class="center"><c:out value="${record.receiveUserName }"></c:out></td>
													<td class="center"><c:out value="${record.truename }"></c:out></td>
													<td class="center"><c:out value="${record.receiveAccountId }"></c:out></td>
													<td class="center">
														<c:forEach items="${transferStatus }" var="transstatus" begin="0" step="1">
																<c:if test="${transstatus.nameCd == record.tradeStatus}"> <c:out value="${transstatus.name }"></c:out> </c:if>
														</c:forEach>
													</td>
													<td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
													<td class="center"><c:out value="${record.createUserName }"></c:out></td>
													<td class="center"><c:out value="${record.remark }"></c:out></td>
													<td class="center"><hyjf:longtodate value="${record.txDate }"></hyjf:longtodate></td>
													<td class="center"><hyjf:longtodate value="${record.txTime }"></hyjf:longtodate></td>
													<td class="center"><c:out value="${record.seqNo  }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="subCommission:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${subCommissionForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="subCommission:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${subCommissionForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>订单号</label>
					<input type="text" name="orderIdSrch" class="form-control input-sm underline" maxlength="20" value="${subCommissionForm.orderIdSrch}"/>
				</div>
				<div class="form-group">
					<label>转入用户名:</label>
					<input type="text" name="receiveUserNameSrch" class="form-control input-sm underline"  maxlength="30" value="${ subCommissionForm.receiveUserNameSrch}" />
				</div>
				<div class="form-group">
					<label>转账状态</label>
					<select name="tradeStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${transferStatus }" var="transstatus" begin="0" step="1">
							<option value="${transstatus.nameCd }"
								<c:if test="${transstatus.nameCd eq subCommissionForm.tradeStatusSrch}">selected="selected"</c:if>>
								<c:out value="${transstatus.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${subCommissionForm.timeStartSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${subCommissionForm.timeEndSrch}" />
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

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/zeroclipboard/ZeroClipboard.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/finance/subcommission/subcommissionlist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
