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


<shiro:hasPermission name="merchantaccountlist:VIEW">
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
					<shiro:hasPermission name="merchantaccountlist:VIEW">
			      		<li class="active"><a href="${webRoot}/finance/merchant/account/accountList">账户信息</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="web:VIEW">
			      		<li><a href="${webRoot}/finance/web/web_list">网站收支</a></li>
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
						<shiro:hasPermission name="merchantaccountlist:SEARCH">
							<!-- 功能栏 -->
							<div class="well">
								<%-- <c:set var="jspPrevDsb" value="${transferListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${transferListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a> --%>
								<a class="btn btn-o btn-primary btn-sm fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
								<!-- <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a> -->
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
									<th class="center">子账户代号</th>
									<th class="center">账户余额</th>
									<th class="center">可用余额</th>
									<th class="center">冻结金额</th>
								</tr>
							</thead>
							<tbody id="userTbody">
								<c:choose>
									<c:when test="${empty accountListForm.recordList}">
										<tr><td colspan="7">暂时没有数据记录</td></tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${accountListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr <c:if test="${record.autoTransferInto == 1 }">style="background-color:Gray"</c:if>>
												<td class="center">${status.index+1}</td>
												<td class="center"><c:out value="${record.subAccountName }"></c:out></td>
												<td class="center"><c:out value="${record.subAccountType }"></c:out></td>
												<td class="center"><c:out value="${record.subAccountCode }"></c:out></td>
												<c:if test="${record.subAccountType eq 'DEP' }"><!--保证金账户显示空  -->
													<td class="left"></td>
													<td class="left"></td>
													<td class="left"></td>
												</c:if>
												<c:if test="${record.subAccountType ne 'DEP' }"><!--非保证金账户显示金额  -->
													<td class="left"><fmt:formatNumber value="${record.accountBalance}" type="number" pattern="#,##0.00" /></td>
													<td class="left"><fmt:formatNumber value="${record.availableBalance}" type="number" pattern="#,##0.00" /></td>
													<td class="left"><fmt:formatNumber value="${record.frost}" type="number" pattern="#,##0.00" /></td>
												</c:if>
												<%-- <td class="center">
													<c:if test="${record.transferType eq 0}">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="transferlist:TRANSFER_SEND_EMAIL">
																<a class="btn btn-transparent btn-xs fn-Send" data-transferid="${record.id }"
																		data-toggle="tooltip" data-placement="top" data-original-title="发邮件"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="transferlist:TRANSFER_COPY_URL">
																<a class="btn btn-transparent btn-xs tooltips fn-Copy" data-transferurl="${record.transferUrl}"
																		data-toggle="tooltip" data-placement="top" data-original-title="复制链接"><i class="fa fa-group"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="transferlist:TRANSFER_SEND_EMAIL">
																	<li>
																		<a class="fn-Send" data-transferid="${record.id }">发邮件</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="transferlist:TRANSFER_COPY_URL">
																	<li>
																		<a class="fn-Copy" data-transferurl="${record.transferUrl}" data-transferid="${record.id }">复制链接</a>
																	</li>
																	</shiro:hasPermission>
																</ul>
															</div>
														</div>
													</c:if>
												</td> --%>
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
										</tr>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<shiro:hasPermission name="transferlist:SEARCH">
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="accountList" paginator="${transferListForm.paginator}"></hyjf:paginator>
						</shiro:hasPermission>
						<br/><br/>
					</div>
				</div>
			</div>
		</div>
		</tiles:putAttribute>
		<%-- 检索面板 (ignore) --%>
		<%-- <tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="transferlist:SEARCH">
				<input type="hidden" name="id" id="id" value= "${transferListForm.id}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${transferListForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>订单号</label>
					<input type="text" name="orderIdSrch" class="form-control input-sm underline" maxlength="20" value="${transferListForm.orderIdSrch}"/>
				</div>
				<div class="form-group">
					<label>交易类型</label>
					<select name="transferTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${transferTypes }" var="types" begin="0" step="1">
							<option value="${types.nameCd }"
								<c:if test="${types.nameCd eq transferListForm.transferTypeSrch}">selected="selected"</c:if>>
								<c:out value="${types.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>	
				<div class="form-group">
					<label>转出账户:</label>
					<input type="text" name="outUserNameSrch" class="form-control input-sm underline"  maxlength="30" value="${ transferListForm.outUserNameSrch}" />
				</div>
				
				<div class="form-group">
					<label>对账标识</label>
					<input type="text" name="reconciliationIdSrch" class="form-control input-sm underline" maxlength="50" value="${transferListForm.reconciliationIdSrch}"/>
				</div>
				<div class="form-group">
					<label>转账状态</label>
					<select name="statusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${transferStatus }" var="transstatus" begin="0" step="1">
							<option value="${transstatus.nameCd }"
								<c:if test="${transstatus.nameCd eq transferListForm.statusSrch}">selected="selected"</c:if>>
								<c:out value="${transstatus.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
			
				<div class="form-group">
					<label>转账时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="transferTimeStart" id="transfer-start-date-time" class="form-control underline" value="${transferListForm.transferTimeStart}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<span class="input-icon">
							<input type="text" name="transferTimeEnd" id="transfer-end-date-time" class="form-control underline" value="${transferListForm.transferTimeEnd}" />
						</span>
					</div>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute> --%>
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
			<script type='text/javascript' src="${webRoot}/jsp/finance/merchant/account/accountList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
