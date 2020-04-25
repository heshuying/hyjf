<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<%-- 画面功能路径 (ignore) --%>
<shiro:hasPermission name="borrowcredit:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇转让" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇转让</h1>
			<span class="mainDescription">汇转让汇转让汇转让汇转让的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="borrowcredit:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="borrowcredit:EXPORT">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
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
										<th class="center">债转编号</th>
										<th class="center">用户名</th>
										<th class="center">项目编号</th>
										<th class="center">债权本金</th>
										<th class="center">转让本金</th>
										<th class="center">折让率</th>
										<th class="center">转让价格</th>
										<th class="center">已转让金额</th>
										<th class="center">转让状态</th>
										<th class="center">还款状态</th>
										<th class="center">发布时间</th>
										<th class="center">发布平台</th>
										<th class="center">还款时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowcreditForm.paginatorPage -1 ) * borrowcreditForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.creditNid }"/></td>
													<td><c:out value="${record.username }"/></td>
													<td class="center"><c:out value="${record.bidNid }"/></td>
													<td align="right"><c:out value="${record.creditCapital }" /></td>
													<td align="right"><c:out value="${record.creditCapitalPrice }" /></td>
													<td align="right"><c:out value="${record.creditDiscount }"/></td>
													<td align="right"><c:out value="${record.creditPrice }" /></td>
													<td align="right"><c:out value="${record.creditCapitalAssigned }" /></td>
													<td class="center"><c:out value="${record.creditStatusName }"/></td>
													<td class="center"><c:out value="${record.repayStatusName }"/></td>
													<td class="center"><c:out value="${record.addTime }"/></td>
													<td class="center">
													<c:out value="${record.client }"/>
													</td>
													<td class="center"><c:out value="${record.repayLastTime }"/></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="borrowcredit:INFO">
																<a class="btn btn-transparent btn-xs tooltips fn-Info" data-creditnid="${record.creditNid }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="查看">查看</a>
															</shiro:hasPermission>
															<c:if test="${record.creditStatus eq '0' }">
																<shiro:hasPermission name="borrowcredit:CANCEL">
																	<a class="btn btn-transparent btn-xs tooltips fn-BeCalcel" data-creditnid="${record.creditNid }"
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="取消">取消</a>
																</shiro:hasPermission>
															</c:if>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="borrowcredit:INFO">
																		<li>
																			<a class="fn-Info" data-creditnid="${record.creditNid }">查看</a>
																		</li>
																	</shiro:hasPermission>
																	<c:if test="${record.creditStatus eq '0' }">
																		<shiro:hasPermission name="borrowcredit:CANCEL">
																			<li>
																				<a class="fn-BeCalcel" data-creditnid="${record.creditNid }">取消</a>
																			</li>
																		</shiro:hasPermission>
																	</c:if>
																</ul>
															</div>
														</div>
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
									<%-- add by LSY START --%>
									<tr>
										<th class="center">总计</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumCredit.sumCreditCapital }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCredit.sumCreditCapitalPrice }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumCredit.sumCreditPrice }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumCredit.sumCreditCapitalAssigned }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
									<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="borrowcredit:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${borrowcreditForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowcredit:SEARCH">
				<input type="hidden" name="creditNid" id="creditNid" />
				<input type="hidden" name="export" id="export" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowcreditForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="usernameSrch" class="form-control input-sm underline" value="${borrowcreditForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>债转编号</label>
					<input type="text" name="creditNidSrch" class="form-control input-sm underline" value="${borrowcreditForm.creditNidSrch}" />
				</div>
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="bidNidSrch" class="form-control input-sm underline" value="${borrowcreditForm.bidNidSrch}" />
				</div>
				<div class="form-group">
				<label>发起平台</label>
					<select name="client" class="form-control underline form-select2">
						<option value="" <c:if test="${borrowcreditForm.client eq ''}">selected="selected"</c:if>></option>
							<option value="0"
								<c:if test="${borrowcreditForm.client eq '0'}">selected="selected"</c:if>>
								pc
							</option>
							<option value="1"
								<c:if test="${borrowcreditForm.client eq '1'}">selected="selected"</c:if>>
								微信
							</option>
							<option value="2"
								<c:if test="${borrowcreditForm.client eq '2'}">selected="selected"</c:if>>
								android
							</option>
							<option value="3"
								<c:if test="${borrowcreditForm.client eq '3'}">selected="selected"</c:if>>
								ios
							</option>
					</select>
				</div>
				<div class="form-group">
					<label>转让状态</label>
					<select name="creditStatusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${creditStatusList }" var="creditStatus" begin="0" step="1" varStatus="status">
							<option value="${creditStatus.nameCd }"
								<c:if test="${creditStatus.nameCd eq borrowcreditForm.creditStatusSrch}">selected="selected"</c:if>>
								<c:out value="${creditStatus.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>添加时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${borrowcreditForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${borrowcreditForm.timeEndSrch}" />
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
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowcredit/borrowcredit.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
