<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="debtborrowinvest:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="出借明细" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">出借明细</h1>
			<span class="mainDescription">出借明细的说明。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${debtBorrowInvestForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${debtBorrowInvestForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="debtborrowinvest:EXPORT">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a> <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">推荐人</th>
										<th class="center">项目编号</th>
										<th class="center">出借利率</th>
										<th class="center">借款期限</th>
										<th class="center">授权服务金额</th>
										<th class="center">还款方式</th>
										<!-- <th class="center">用户属性</th> -->
										<th class="center">操作平台</th>
										<th class="center">投标方式</th>
										<th class="center">出借时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="13">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(debtBorrowInvestForm.paginatorPage - 1 ) * debtBorrowInvestForm.paginator.limit + status.index + 1 }</td>
													<td><c:out value="${record.username }"></c:out></td>
													<td><c:out value="${record.referrerName }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td align="right"><c:out value="${record.borrowApr }"></c:out></td>
													<td align="right"><c:out value="${record.borrowPeriod }"></c:out></td>
													<td align="right"><c:out value="${record.account }"></c:out></td>
													<td><c:out value="${record.borrowStyleName }"></c:out></td>
													<%-- <td class="center"><c:out value="${record.tenderUserAttribute }"></c:out></td> --%>
													<td><c:out value="${record.operatingDeck }"></c:out></td>
													<td class="center"><c:out value="${record.investType }"></c:out></td>
													<td class="center"><c:out value="${record.addtime }"></c:out></td>
													<td class="center">
													<%-- 	<div class="visible-md visible-lg hidden-sm hidden-xs">
															<c:if test="${record.resendMessage=='1' }">
																<shiro:hasPermission name="debtborrowinvest:SEARCH">
																	<a class="btn btn-transparent btn-xs tooltips fn-Modify" data-userid="${record.userId }" data-nid="${record.nid }" data-borrownid="${record.borrowNid}" data-toggle="tooltip" data-placement="top" data-original-title="出借协议重发"><i class="fa fa-file-text"></i></a>
																</shiro:hasPermission>
															</c:if>
															<shiro:hasPermission name="debtborrowinvest:EXPORTAGREEMENT">
																<a class="btn btn-transparent btn-xs tooltips fn-ExportAgreement" data-userid="${record.userId }" data-nid="${record.nid }" data-borrownid="${record.borrowNid}" data-toggle="tooltip" data-placement="top" data-original-title="发送协议"><i class="fa fa-download"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<c:if test="${record.resendMessage=='1' }">
																		<shiro:hasPermission name="debtborrowinvest:SEARCH">
																			<li><a class="fn-Modify" data-userid="${record.userId }" data-nid="${record.nid }" data-borrowNid="${record.borrowNid}">出借协议重发</a></li>
																		</shiro:hasPermission>
																	</c:if>
																	<shiro:hasPermission name="debtborrowinvest:EXPORTAGREEMENT">
																		<li><a class="fn-ExportAgreement" data-userid="${record.userId }" data-nid="${record.nid }" data-borrowNid="${record.borrowNid}">发送协议</a></li>
																	</shiro:hasPermission>
																</ul>
															</div>
														</div> --%>
													</td>

												</tr>
											</c:forEach>
											<tr>
												<th class="center">总计</th>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td align="right"><c:out value="${sumAccount }"></c:out></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="debtborrowinvest:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${debtBorrowInvestForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br />
							<br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="debtborrowinvest:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${debtBorrowInvestForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label> <input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${debtBorrowInvestForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>推荐人</label> <input type="text" name="referrerNameSrch" id="referrerNameSrch" class="form-control input-sm underline" value="${debtBorrowInvestForm.referrerNameSrch}" />
				</div>
				<div class="form-group">
					<label>项目编号</label> <input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${debtBorrowInvestForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>借款期限</label> <input type="text" name="borrowPeriod" id="borrowPeriod" class="form-control input-sm underline" value="${debtBorrowInvestForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2" />
				</div>
				<!-- 				<div class="form-group"> -->
				<!-- 					<label>渠道</label> -->
				<!-- 					<select name="utmIdSrch" class="form-control underline form-select2"> -->
				<!-- 						<option value=""></option> -->
				<%-- 						<c:forEach items="${utmList }" var="utm" begin="0" step="1" varStatus="status"> --%>
				<%-- 							<option value="${utm.sourceId }" --%>
				<%-- 								<c:if test="${utm.sourceId eq debtBorrowInvestForm.utmIdSrch}">selected="selected"</c:if>> --%>
				<%-- 								<c:out value="${utm.sourceName }"></c:out></option> --%>
				<%-- 						</c:forEach> --%>
				<!-- 					</select> -->
				<!-- 				</div> -->
				<div class="form-group">
					<label>投标方式</label> <select name="investType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${investTypeList }" var="investType" begin="0" step="1" varStatus="status">
							<option value="${investType.nameCd }" <c:if test="${investType.nameCd eq debtBorrowInvestForm.investType}">selected="selected"</c:if>>
								<c:out value="${investType.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>还款方式</label> <select name="borrowStyleSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
							<option value="${borrowStyle.nid }" <c:if test="${borrowStyle.nid eq debtBorrowInvestForm.borrowStyleSrch}">selected="selected"</c:if>>
								<c:out value="${borrowStyle.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>操作平台</label> <select name="clientSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${clientList }" var="client" begin="0" step="1" varStatus="status">
							<option value="${client.nameCd }" <c:if test="${client.nameCd eq debtBorrowInvestForm.clientSrch}">selected="selected"</c:if>>
								<c:out value="${client.name}"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>出借时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${debtBorrowInvestForm.timeStartSrch}" /> <i class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${debtBorrowInvestForm.timeEndSrch}" />
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

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/debt/debtborrowinvest/debtborrowinvest.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
