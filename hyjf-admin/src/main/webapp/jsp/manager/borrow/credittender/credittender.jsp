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
<shiro:hasPermission name="credittender:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇转让-承接信息" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇转让-承接信息</h1>
			<span class="mainDescription">汇转让-承接信息</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="credittender:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${creditTenderForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${creditTenderForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="credittender:EXPORT">
										<shiro:hasPermission name="credittender:ORGANIZATION_VIEW">
											<a class="btn btn-o btn-primary btn-sm hidden-xs fn-EnhanceExport"
											   data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
										</shiro:hasPermission>
										<shiro:lacksPermission name="credittender:ORGANIZATION_VIEW">
											<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
											   data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
										</shiro:lacksPermission>
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
										<th class="center">订单号</th>
										<th class="center">债转编号</th>
										<th class="center">项目编号</th>
										<th class="center">出让人</th>
										<th class="center">承接人</th>
										<th class="center">承接本金</th>
										<th class="center">折让率</th>
										<th class="center">认购价格</th>
										<th class="center">垫付利息</th>
										<%-- upd by LSY START --%>
										<%-- <th class="center">服务费</th> --%>
										<th class="center">债转服务费</th>
										<%-- upd by LSY END --%>
										<th class="center">实付金额</th>
										<th class="center">承接平台</th>
										<th class="center">承接时间</th>
										<th class="center">合同状态</th>
										<th class="center">合同编号</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="17">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(creditTenderForm.paginatorPage -1 ) * creditTenderForm.paginator.limit + status.index + 1 }</td>
													<td><c:out value="${record.assignNid }"/></td>
													<td><c:out value="${record.creditNid }"/></td>
													<td><c:out value="${record.bidNid }"/></td>
													<td><c:out value="${record.creditUsername }"/></td>
													<td><c:out value="${record.username }"/></td>
													<td align="right"><c:out value="${record.assignCapital }"/></td>
													<td align="right"><c:out value="${record.creditDiscount }"/>%</td>
													<td align="right"><c:out value="${record.assignPrice }"/></td>
													<td align="right"><c:out value="${record.assignInterestAdvance }"/></td>
													<td align="right"><c:out value="${record.creditFee }"/></td>
													<td align="right"><c:out value="${record.assignPay }"/></td>
													<td align="right">
													<c:if test="${record.client eq 0}">
													pc
													</c:if>
													<c:if test="${record.client eq 1}">
													微信
													</c:if>
													<c:if test="${record.client eq 2}">
													android
													</c:if>
													<c:if test="${record.client eq 3}">
													iOS
													</c:if>
													</td>
													<td class="center"><c:out value="${record.addTime }"/></td>
													<td class="center">
														<c:if test="${record.contractStatus eq '' || record.contractStatus eq '0'}"><c:out value="初始"/></c:if>
														<c:if test="${record.contractStatus eq '1'}"><c:out value="生成成功"/></c:if>
														<c:if test="${record.contractStatus eq '2'}"><c:out value="签署成功"/></c:if>
														<c:if test="${record.contractStatus eq '3'}"><c:out value="下载成功"/></c:if>
													</td>
													<td class="center"><c:out value="${record.contractNumber }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<!-- 合同状态 为成功-->
															<c:if test="${record.contractStatus == '3'}">
																<shiro:hasPermission name="credittender:PDFDOWNLOAD">
																	<a class="btn btn-transparent btn-xs tooltips fn-PdfDownload" target="_blank" href="${record.downloadUrl}" data-toggle="tooltip" data-placement="top" data-original-title="下载"><i class="fa fa-download"></i></a>
																</shiro:hasPermission>
																<shiro:hasPermission name="credittender:PDFVIEW">
																	<a class="btn btn-transparent btn-xs tooltips fn-PdfView" target="_blank" href="${record.viewpdfUrl}" data-toggle="tooltip" data-placement="top" data-original-title="查看"><i class="fa fa-file-text"></i></a>
																</shiro:hasPermission>
																<shiro:hasPermission name="credittender:PDFPREVIEW">
																	<a class="btn btn-transparent btn-xs tooltips fn-PdfPreview" target="_blank" href="${webRoot}/manager/borrow/credittender/pdfPreviewAction.do?nid=${record.assignNid }" data-toggle="tooltip" data-placement="top" data-original-title="预览"><i class="fa ti-layers-alt"></i></a>
																</shiro:hasPermission>
															</c:if>
															<c:if test="${record.contractStatus != '3'}">
																<shiro:hasPermission name="credittender:PDFSIGN">
																	<a class="btn btn-transparent btn-xs tooltips fn-PdfSign"  data-userid="${record.userId }" data-creditnid="${record.creditNid }" data-assignnid ="${record.assignNid }" data-credittendernid="${record.creditTenderNid}" data-borrownid="${record.bidNid}" data-toggle="tooltip" data-placement="top" data-original-title="PDF签署"><i class="fa ti-layers-alt"></i></a>
																</shiro:hasPermission>
															</c:if>
															<!-- 出借人债权明细查询 -->
															<shiro:hasPermission name="credittender:QUERY_INVEST_DEBT">
																<a class="btn btn-transparent btn-xs tooltips fn-check" data-userid="${record.userId }" data-assignnid="${record.assignNid }" data-toggle="tooltip" data-placement="top" data-original-title="查询出借人债权明细"><i class="fa fa-exchange"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<c:if test="${record.contractStatus == '3'}" >
																		<shiro:hasPermission name="credittender:PDFDOWNLOAD">
																			<li>
																				<a target="_blank" href="${record.downloadUrl}" >下载</a>
																			</li>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="credittender:PDFVIEW">
																			<li>
																				<a target="_blank" href="${record.viewpdfUrl}" >查看</a>
																			</li>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="credittender:PDFPREVIEW">
																			<li>
																				<a target="_blank" href="${webRoot}/manager/borrow/credittender/pdfPreviewAction.do?nid=${record.assignNid }">预览</a>
																			</li>
																		</shiro:hasPermission>
																		<c:if test="${record.contractStatus != '3'}">
																			<shiro:hasPermission name="credittender:PDFSIGN">
																				<li><a class="fn-PdfSign" data-userid="${record.userId }" data-creditnid="${record.creditNid }" data-assignnid ="${record.assignNid }" data-credittendernid="${record.creditTenderNid}" data-borrownid="${record.bidNid}">PDF签署</a></li>
																			</shiro:hasPermission>
																		</c:if>
																	</c:if>
																	<!-- 出借人债权明细查询 -->
																	<shiro:hasPermission name="credittender:QUERY_INVEST_DEBT">
																		<li><a class="btn btn-transparent btn-xs tooltips fn-check" data-userid="${record.userId }"  data-assignnid="${record.assignNid }">查询出借人债权明细</a></li>
																	</shiro:hasPermission>
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
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumBorrowCreditInfo.sumAssignCapital }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumBorrowCreditInfo.sumAssignPrice }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumBorrowCreditInfo.sumAssignInterestAdvance }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumBorrowCreditInfo.sumCreditFee }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumBorrowCreditInfo.sumAssignPay }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
									<%-- add by LSY END --%>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="credittender:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${creditTenderForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>

							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="credittender:SEARCH">
				<input type="hidden" name="creditNid" id="creditNid" />
				<input type="hidden" name="userId" id="userId" />
				<input type="hidden" name="borrowNid" id="borrowNid" />
				<input type="hidden" name="assignNid" id="assignNid" />
				<input type="hidden" name="creditTenderNid" id="creditTenderNid" />
				<input type="hidden" name="export" id="export" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${creditTenderForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>出让人</label>
					<input type="text" name="creditUsernameSrch" class="form-control input-sm underline" value="${creditTenderForm.creditUsernameSrch}" />
				</div>
				<div class="form-group">
					<label>承接人</label>
					<input type="text" name="usernameSrch" class="form-control input-sm underline" value="${creditTenderForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="bidNidSrch" class="form-control input-sm underline" value="${creditTenderForm.bidNidSrch}" />
				</div>
					<div class="form-group">
				<label>发起平台</label>
					<select name="client" class="form-control underline form-select2">
						<option value="" <c:if test="${borrowcreditForm.client eq ''}">selected="selected"</c:if>></option>
							<option value="0"
								<c:if test="${borrowcreditForm.client eq 0}">selected="selected"</c:if>>
								pc
							</option>
							<option value="1"
								<c:if test="${borrowcreditForm.client eq 1}">selected="selected"</c:if>>
								微信
							</option>
							<option value="2"
								<c:if test="${borrowcreditForm.client eq 2}">selected="selected"</c:if>>
								android
							</option>
							<option value="3"
								<c:if test="${borrowcreditForm.client eq 3}">selected="selected"</c:if>>
								ios
							</option>
					</select>
				</div>
				<div class="form-group">
					<label>订单号</label>
					<input type="text" name="assignNidSrch" class="form-control input-sm underline" value="${creditTenderForm.assignNidSrch}" />
				</div>
				<div class="form-group">
					<label>债转编号</label>
					<input type="text" name="creditNidSrch" class="form-control input-sm underline" value="${creditTenderForm.creditNidSrch}" />
				</div>
				<div class="form-group">
					<label>承接时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${creditTenderForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${creditTenderForm.timeEndSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/credittender/credittender.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
