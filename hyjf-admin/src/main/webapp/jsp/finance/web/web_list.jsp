<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="web:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="网站收支" />

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">网站收支</h1>
		<span class="mainDescription">这里添加网站收支描述。</span>
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
			      		<li class="active"><a href="${webRoot}/finance/web/web_list">网站收支</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="merchanttransferlist:VIEW">
			      		<li><a href="${webRoot}/finance/merchant/transfer/transferList">子账户间转账</a></li>
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
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb"
								value="${webForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb"
								value="${webForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a
								class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a
								class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
							<shiro:hasPermission name="web:EXPORT">
								<shiro:hasPermission name="web:ORGANIZATION_VIEW">
									<a class="btn btn-o btn-primary btn-sm fn-EnhanceExport"
									   data-toggle="tooltip" data-placement="bottom"
									   data-original-title="导出Excel">
										导出Excel <i class="fa fa-EnhanceExport"></i></a>
								</shiro:hasPermission>
								<shiro:lacksPermission name="web:ORGANIZATION_VIEW">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
									   data-toggle="tooltip" data-placement="bottom"
									   data-original-title="导出Excel">
										导出Excel <i class="fa fa-Export"></i></a>
								</shiro:lacksPermission>
							</shiro:hasPermission>
							<shiro:hasPermission name="web:VIEW">
								<a class="btn btn-o btn-primary btn-sm fn-yue-Search"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="查询余额">
									查询余额 <i class="fa fa-yue-Search"></i></a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 角色列表一览 --%>
						<table id="equiList"
							class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">订单号</th>
									<shiro:hasPermission name="web:ORGANIZATION_VIEW">
										<th class="center">分公司</th>
										<th class="center">分部</th>
										<th class="center">团队</th>
									</shiro:hasPermission>
									<th class="center">用户名</th>
									<th class="center">姓名</th>
									<th class="center">收支类型</th>
									<th class="center">交易类型</th>
									<th class="center">交易金额</th>
									<th class="center">说明</th>
									<th class="center">发生时间</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty webForm.recordList}">
										<tr>
											<td colspan="12">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${webForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center">${(webForm.paginatorPage - 1 ) * webForm.paginator.limit + status.index + 1 }</td>
												<td><c:out value="${record.ordid }"></c:out></td>
												<shiro:hasPermission name="web:ORGANIZATION_VIEW">
													<td class="center"><c:out value="${record.regionName }"></c:out></td>
													<td class="center"><c:out value="${record.branchName }"></c:out></td>
													<td><c:out value="${record.departmentName }"></c:out></td>
												</shiro:hasPermission>
												<td><c:out value="${record.username }"></c:out></td>
												<td><c:out value="${record.truename }"></c:out></td>
												<td class="center"><c:if test="${record.type==1 }">收入</c:if> <c:if
														test="${record.type==2 }">支出</c:if></td>
												<td class="center"><c:out value="${record.tradeType  }"></c:out></td>
												<td align="right"><fmt:formatNumber value="${record.amount}" type="number" pattern="#,##0.00#" /></td>  
												<td>${record.remark  }</td>
												<td class="center"><c:out value="${record.createTime  }"></c:out></td>
											</tr>
										</c:forEach>
										<tr>
											<th class="center">总计</th>
											<td></td>
											<shiro:hasPermission name="web:ORGANIZATION_VIEW">
												<td></td>
												<td></td>
												<td></td>
											</shiro:hasPermission>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td align="right"><c:out value="${sumAccount }"></c:out></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page"
							action="web_list" paginator="${webForm.paginator}"></hyjf:paginator>
						<br />
						<br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="permissionUuid" id="permissionUuid" />
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${webForm.paginatorPage}" />
		<!-- 查询条件 -->
		<div class="form-group">
			<label>用户名</label> <input type="text" name="usernameSearch"
				class="form-control input-sm underline"
				value="${webForm.usernameSearch}" />
		</div>
		
		<div class="form-group">
			<label>交易类型</label> <select name="tradeTypeSearch" class="form-control underline form-select2">
				<option value=""></option>
				<c:forEach items="${webForm.tradeList }" var="tradetype" begin="0" step="1" varStatus="status">
					<option value="${tradetype.value }" <c:if test="${tradetype.value eq webForm.tradeTypeSearch}">selected="selected"</c:if>> <c:out value="${tradetype.name }"></c:out></option>
				</c:forEach> 
			</select>
		</div>
		<div class="form-group">
			<label>交易时间</label>
			<div class="input-group input-daterange datepicker">
				<span class="input-icon"> <input type="text"
					name="startDate" id="start-date-time"
					class="form-control underline" value="${webForm.startDate}" /> <i
					class="ti-calendar"></i>
				</span> <span class="input-group-addon no-border bg-light-orange">~</span>
				<input type="text" name="endDate" id="end-date-time"
					class="form-control underline" value="${webForm.endDate}" />
			</div>
		</div>
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
		<script type='text/javascript' src="${webRoot}/jsp/finance/web/web_list.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>
</shiro:hasPermission>
