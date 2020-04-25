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


<shiro:hasPermission name="bankmerchantaccount:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="账户信息" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">账户信息</h1>
			<span class="mainDescription">本功能可以查看商户子账户信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="bankmerchantaccount:VIEW">
			      		<li class="active"><a href="${webRoot}/bank/merchant/account/init">账户信息</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="bankredpacketaccount:VIEW">
			      		<li><a href="${webRoot}/bank/merchant/redpacket/init">红包账户明细</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="web:VIEW">
			      		<li><a href="${webRoot}/bank/merchant/poundage/init">手续费账户明细</a></li>
			      	</shiro:hasPermission>
			    </ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
						<shiro:hasPermission name="bankmerchantaccount:SEARCH">
							<!-- 功能栏 -->
							<div class="well">
								<a class="btn btn-o btn-primary btn-sm fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
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
									<th class="center">子账户名称</th>
									<th class="center">子账户类型</th>
									<th class="center">电子账户</th>
									<th class="center">账户余额</th>
									<th class="center">可用余额</th>
									<th class="center">冻结金额</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody id="userTbody">
								<c:choose>
									<c:when test="${empty accountListForm.recordList}">
										<tr><td colspan="8">暂时没有数据记录</td></tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${accountListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center">${status.index+1}</td>
												<td class="center"><c:out value="${record.accountName }"></c:out></td>
												<td class="center"><c:out value="${record.accountType }"></c:out></td>
												<td class="center"><c:out value="${record.accountCode }"></c:out></td>
												<td class="left"><fmt:formatNumber value="${record.accountBalance}" type="number" pattern="#,##0.00" /></td>
												<td class="left"><fmt:formatNumber value="${record.availableBalance}" type="number" pattern="#,##0.00" /></td>
												<td class="left"><fmt:formatNumber value="${record.frost}" type="number" pattern="#,##0.00" /></td>
												<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<a class="btn btn-transparent btn-xs tooltips fn-UpdateBalance" data-account-code="${record.accountCode }" data-toggle="tooltip" tooltip-placement="top" data-original-title="更新"><i class="fa fa-cloud-download"></i></a>
														<a class="btn btn-transparent btn-xs fn-Returncash" data-name="${record.accountName }" data-account-code="${record.accountCode }" data-toggle="tooltip" tooltip-placement="top" data-original-title="充值"><i class="fa fa-mail-reply"></i></a>
														<a class="btn btn-transparent btn-xs fn-Delete" data-name="${record.accountName }" data-account-code="${record.accountCode }" data-toggle="tooltip" tooltip-placement="top" data-original-title="提现"><i class="fa fa-times fa fa-white"></i></a>
														<shiro:hasPermission name="bankmerchantaccount:REDPOCKETSEND">
															<a class="btn btn-transparent btn-xs fn-initSendPocket" data-toggle="tooltip" tooltip-placement="top" data-original-title="红包发放"><i class="fa fa-arrow-circle-o-up fa-initSendPocket"></i></a>
														</shiro:hasPermission>
														<shiro:hasPermission name="bankmerchantaccount:SETPASSWORD">
															<c:if test="${record.isSetPassword==0}">
															<a class="btn btn-transparent btn-xs fn-Update" data-toggle="tooltip" tooltip-placement="top" data-original-title="设置交易密码" 
															href="${webRoot}/bank/merchant/account/setPassword.do?accountCode=${record.accountCode }">
															<i class="fa fa-arrow-circle-o-up fn-Update"></i></a>
															</c:if>
															<c:if test="${record.isSetPassword==1}">
															<a class="btn btn-transparent btn-xs fn-Update" data-toggle="tooltip" tooltip-placement="top" data-original-title="设置交易密码" 
															href="${webRoot}/bank/merchant/account/resetPassword.do?accountCode=${record.accountCode }">
															<i class="fa fa-arrow-circle-o-up fn-Update"></i></a>
															</c:if>
														</shiro:hasPermission>
													</div>
													<div class="visible-xs visible-sm hidden-md hidden-lg">
														<div class="btn-group" dropdown="">
															<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
															</button>
																<li><a class="fn-UpdateBalance" data-account-code="${record.accountCode }">更新</a></li>
																<li><a class="fn-Returncash" data-name="${record.accountName }" data-account-code="${record.accountCode }">充值</a></li>
																<li><a class="fn-Delete" data-name="${record.accountName }" data-account-code="${record.accountCode }">提现</a></li>
																<shiro:hasPermission name="bankmerchantaccount:REDPOCKETSEND">
																	<li><a class="fn-initSendPocket" data-account-code="${record.accountCode }">红包发放</a></li>
																</shiro:hasPermission>
																<shiro:hasPermission name="bankmerchantaccount:SETPASSWORD">
																	<c:if test="${record.isSetPassword==0}">
																	<li><a class="fn-Update" href="${webRoot}/bank/merchant/account/setPassword.do?accountCode=${record.accountCode }">设置交易密码</a></li>
																	</c:if>
																	<c:if test="${record.isSetPassword==1}">
																	<li><a class="fn-Update" href="${webRoot}/bank/merchant/account/resetPassword.do?accountCode=${record.accountCode }">设置交易密码</a></li>
																	</c:if>
																</shiro:hasPermission>
															</ul>
														</div>
													</div>
												</td>
											</tr>
										</c:forEach>
										<tr>
											<td class="center">总计</td>
											<td class="center"></td>
											<td class="center"></td>
											<td class="center"></td>
											<td class="left" style="color:red!important"><fmt:formatNumber value="${accountListForm.accountBalanceSum}" type="number" pattern="#,##0.00" /></td>
											<td class="left" style="color:red!important"><fmt:formatNumber value="${accountListForm.availableBalanceSum}" type="number" pattern="#,##0.00" /></td>
											<td class="left" style="color:red!important"><fmt:formatNumber value="${accountListForm.frostSum}" type="number" pattern="#,##0.00" /></td>
											<td class="center"></td>
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<shiro:hasPermission name="merchantaccountlist:SEARCH">
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${accountListForm.paginator}"></hyjf:paginator>
						</shiro:hasPermission>
						<br/><br/>
					</div>
				</div>
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
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/zeroclipboard/ZeroClipboard.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
		
		
			<script type='text/javascript' src="${webRoot}/jsp/bank/merchant/merchantaccountlist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
