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


<shiro:hasPermission name="merchanttransferlist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="子账户间转账" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">子账户间转账</h1>
			<span class="mainDescription">本功能可以发起商户子账户转账。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="merchantaccountlist:VIEW">
			      		<li><a href="${webRoot}/finance/merchant/account/accountList">账户信息</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="web:VIEW">
			      		<li><a href="${webRoot}/finance/web/web_list">网站收支</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="merchanttransferlist:VIEW">
			      		<li class="active"><a href="${webRoot}/finance/merchant/transfer/transferList">子账户间转账</a></li>
			      	</shiro:hasPermission>
	   				<shiro:hasPermission name="refeestatistic:VIEW">
						<li><a href="${webRoot}/finance/statistics/rechargefee/statistics">充值手续费统计</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="couponrepaymonitor:VIEW">
						<li><a href="${webRoot}/finance/couponrepaymonitor/chart">加息券还款统计</a></li>
					</shiro:hasPermission>
			    </ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<shiro:hasPermission name="merchanttransferlist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${transferListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${transferListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<shiro:hasPermission name="merchanttransferlist:ADD">
										<a class="btn btn-o btn-primary btn-sm fn-Add"
												data-toggle="tooltip" data-placement="bottom" data-original-title="发起转账">发起转账<i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<shiro:hasPermission name="merchanttransferlist:EXPORT">
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
										<th class="center">订单号</th>
										<th class="center">转出子账户</th>
										<th class="center">转入子账户</th>
										<th class="center">转账金额</th>
										<th class="center">备注</th>
										<th class="center">转账状态</th>
										<th class="center">操作人</th>
										<th class="center">转账类型</th>
										<th class="center">转账时间</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty transferListForm.recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${transferListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(transferListForm.paginatorPage-1)*transferListForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.outAccountName }"></c:out></td>
													<td class="center"><c:out value="${record.inAccountName }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.transferAmount}" type="number" pattern="#,##0.00" /></td>
													<td class="left"><c:out value="${record.remark }"></c:out></td>
													<td class="center" <c:if test="${record.status == 2 }"> title = "${record.message}"</c:if>>
														<c:forEach items="${transferStatus }" var="transstatus" begin="0" step="1">
																<c:if test="${transstatus.nameCd eq record.status}"> <c:out value="${transstatus.name }"></c:out> </c:if>
														</c:forEach>
													</td>
													<td class="center"><c:out value="${record.createUserName }"></c:out></td>
													<td class="center">
														<c:forEach items="${transferTypes }" var="types" begin="0" step="1">
																<c:if test="${types.nameCd eq record.transferType}"> <c:out value="${types.name }"></c:out> </c:if>
														</c:forEach>
													</td>
													<td class="center"><fmt:formatDate value="${record.transferTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="merchanttransferlist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="transferList" paginator="${transferListForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="merchanttransferlist:SEARCH">
				<input type="hidden" name="id" id="id" value= "${transferListForm.id}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${transferListForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>订单号</label>
					<input type="text" name="orderId" class="form-control input-sm underline" maxlength="20" value="${transferListForm.orderId}"/>
				</div>
				<div class="form-group">
					<label>交易类型</label>
					<select name="transferType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${transferTypes }" var="types" begin="0" step="1">
							<option value="${types.nameCd }"
								<c:if test="${types.nameCd eq transferListForm.transferType}">selected="selected"</c:if>>
								<c:out value="${types.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>	
				<div class="form-group">
					<label>转出子账户:</label>
					<select name="outAccountId" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${merchantAccountListOut }" var="merchantAccountOut" begin="0" step="1">
							<option value="${merchantAccountOut.id }"
								<c:if test="${merchantAccountOut.id eq transferListForm.outAccountId}">selected="selected"</c:if>>
								<c:out value="${merchantAccountOut.subAccountName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>转入子账户:</label>
					<select name="inAccountId" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${merchantAccountListIn }" var="merchantAccountIn" begin="0" step="1">
							<option value="${merchantAccountIn.id }"
								<c:if test="${merchantAccountIn.id eq transferListForm.inAccountId}">selected="selected"</c:if>>
								<c:out value="${merchantAccountIn.subAccountName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>转账状态</label>
					<select name="status" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${transferStatus }" var="transstatus" begin="0" step="1">
							<option value="${transstatus.nameCd }"
								<c:if test="${transstatus.nameCd eq transferListForm.status}">selected="selected"</c:if>>
								<c:out value="${transstatus.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>操作人</label>
					<input type="text" name="createUserName" class="form-control input-sm underline" maxlength="20" value="${transferListForm.createUserName}"/>
				</div>
				<div class="form-group">
					<label>转账时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="transferTimeStart" id="start-date-time" class="form-control underline" value="${transferListForm.transferTimeStart}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="transferTimeEnd" id="end-date-time" class="form-control underline" value="${transferListForm.transferTimeEnd}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/finance/merchant/transfer/transferList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
