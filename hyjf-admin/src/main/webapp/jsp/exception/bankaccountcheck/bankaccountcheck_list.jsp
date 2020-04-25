<%@page import="com.hyjf.common.util.GetDate"%>
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

 <shiro:hasPermission name="bankaccountcheck:VIEW" > 
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="银行对账" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">银行对账</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- <shiro:hasPermission name="snatch:SEARCH"> --%>
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${accountcheckForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${accountcheckForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<%-- <shiro:hasPermission name="snatch:EXPORT"> --%>
										<!-- <a class="btn btn-o btn-primary btn-sm fn-Export"	
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a> -->
									<%-- </shiro:hasPermission> --%>
									<!-- <a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a> -->
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							<%-- </shiro:hasPermission> --%>
							<br/>
							<%-- 对账列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center" style="width:90px">序号</th>
										<th class="center">日期</th>
										<th class="center">流水号</th>
										<th class="center">用户名</th>
										<th class="center">电子账号</th>
										<th class="center">订单号</th>
										<th class="center">交易类型</th>
										<th class="center">交易金额</th>
										<th class="center">到账金额</th>
										<th class="center">交易状态</th>
										<th class="center">到账时间</th>
										<th class="center">对账时间</th>
										<th class="center">对账状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty accountcheckForm.accountCheckList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${accountcheckForm.accountCheckList}" var="accountCheck" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((accountcheckForm.paginatorPage - 1) * accountcheckForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${accountCheck.createDate}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.bankSeqNo}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.userName}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.accountId}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.nId}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.tradeType}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.amount}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.accountBalance}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.accountCheckTradeStr}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.acountTime}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.checkDate}"></c:out></td>
													<td class="center"><c:out value="${accountCheck.accountCheckStr}"></c:out></td>
													<td class="center">
													   <%-- <a class="btn btn-transparent btn-xs fn-Detail" data-id="${record.id }" 
																	data-toggle="tooltip" data-placement="top" data-original-title="更新"><i class="fa fa-database"></i></a> --%>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<%-- <shiro:hasPermission name="snatch:SEARCH"> --%>
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${accountcheckForm.paginator}"></hyjf:paginator>
							<%-- </shiro:hasPermission> --%>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<%-- <shiro:hasPermission name="snatch:SEARCH"> --%>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${accountcheckForm.paginatorPage == null ? 0 : accountcheckForm.paginatorPage}"  />
			<%-- 	<input type="hidden" name="fid" id="fid" value="${fid}" />
				<input type="hidden" name="id" id="id" /> --%>
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" value="${accountcheckForm.userName}" class="form-control input-sm underline" maxlength="20" />
				</div>
				<div class="form-group">
					<label>电子账号</label>
					<input type="text" name="accountId" value="${accountcheckForm.accountId}" class="form-control input-sm underline" maxlength="20" />
				</div>
				<div class="form-group">
					<label>订单号</label>
					<input type="text" name="nId" value="${accountcheckForm.nId}" class="form-control input-sm underline" maxlength="20" />
				</div>
				<div class="form-group">
				<label>对账状态</label> <select name="checkStatus" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq accountcheckForm.checkStatus}">selected="selected"</c:if>>未对账</option>
					<option value="1" <c:if test="${'1' eq accountcheckForm.checkStatus}">selected="selected"</c:if>> 已对账</option>
				</select>
			</div>
			<div class="form-group">
				<label>交易类型</label> <select name="tradeTypeSearch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${accountcheckForm.tradeList }" var="tradetype" begin="0" step="1" varStatus="status">
						<option value="${tradetype.id }" <c:if test="${tradetype.id eq accountcheckForm.tradeTypeSearch}">selected="selected"</c:if>> <c:out value="${tradetype.name }"></c:out></option>
					</c:forEach> 
				</select>
			</div>
			<div class="form-group">
				<label>时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> 
					<input type="text" name="startDate" id="start-date-time" class="form-control underline" value="${accountcheckForm.startDate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="endDate" id="end-date-time" class="form-control underline" value="${accountcheckForm.endDate}" />
				</div>
			</div>
			<div class="form-group">
					<label>流水号</label>
					<input type="text" name="bankSeqNo" value="${accountcheckForm.bankSeqNo}" class="form-control input-sm underline" maxlength="20" />
			</div>
			<div class="form-group">
				<label>交易状态</label> <select name="tradeStatus" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq accountcheckForm.tradeStatus}">selected="selected"</c:if>>失败</option>
					<option value="1" <c:if test="${'1' eq accountcheckForm.tradeStatus}">selected="selected"</c:if>> 成功</option>
					<option value="2" <c:if test="${'2' eq accountcheckForm.tradeStatus}">selected="selected"</c:if>> 冲正</option>
				</select>
			</div>
			<%-- </shiro:hasPermission> --%>
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/exception/bankaccountcheck/bankaccountcheck_list.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission> 
