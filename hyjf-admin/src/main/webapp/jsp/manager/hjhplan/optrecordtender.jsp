<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
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
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="borrow:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="运营记录" />


		<%-- 原始标的主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul id="mainTabs" class="nav nav-tabs nav-justified">
						<li >
							<a href="#" class="ysbd"> 原始标的 </a>
						</li>
						<li>
							<a href="#" class="zzbd"> 债转标的 </a>
						</li>
						<li class="s-ellipsis active">
							<a href="#" class="tzmx"> 出借明细 </a>
						</li>
						<li>
							<a href="#" class="cjmx"> 承接明细 </a>
						</li>
						<li>
					</ul>



					<div class="tab-content">
						<div class="tab-pane fade in active" id="tab_jbxx_1">
							<div class="row">
								<div class="col-md-12">
									<div class="search-classic">
											<%-- 功能栏 --%>
										<div class="well">
											<c:set var="jspPrevDsb" value="${borrowInvestForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
											<c:set var="jspNextDsb" value="${borrowInvestForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
											<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
											<shiro:hasPermission name="borrowinvest:EXPORT">
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
												<th class="center">智投编号</th>
												<th class="center">出借利率</th>
												<th class="center">借款期限</th>
												<th class="center">出借金额(元)</th>
												<th class="center">还款方式</th>
												<th class="center">循环出借(是/否)</th>
												<!-- <th class="center">用户属性</th> -->
												<th class="center">操作平台</th>
												<th class="center">出借方式</th>
												<th class="center">出借时间</th>
												<th class="center">合同状态</th>
												<th class="center">合同编号</th>
												<th class="center">操作</th>
											</tr>
											</thead>
											<tbody id="roleTbody">
											<c:choose>
												<c:when test="${empty recordList}">
													<tr>
														<td colspan="16">暂时没有数据记录</td>
													</tr>
												</c:when>
												<c:otherwise>
													<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="center">${(borrowInvestForm.paginatorPage - 1 ) * borrowInvestForm.paginator.limit + status.index + 1 }</td>
															<td><c:out value="${record.username }"></c:out></td>
															<td><c:out value="${record.referrerName }"></c:out></td>
															<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
															<td class="center"><c:out value="${record.planNid }"></c:out></td>
															<td align="right"><c:out value="${record.borrowApr }"></c:out></td>
															<td align="right"><c:out value="${record.borrowPeriod }"></c:out></td>
															<td align="right"><c:out value="${record.account }"></c:out></td>
															<td><c:out value="${record.borrowStyleName }"></c:out></td>
															<td align="center"><c:out value="${record.tenderType }"></c:out></td>
																<%-- <td class="center"><c:out value="${record.tenderUserAttribute }"></c:out></td> --%>
															<td><c:out value="${record.operatingDeck }"></c:out></td>
															<td class="center"><c:out value="${record.investType }"></c:out></td>
															<td class="center"><c:out value="${record.addtime }"></c:out></td>
															<td class="center"><c:if test="${record.contractStatus eq '' || record.contractStatus eq '0'}"><c:out value="初始"/></c:if>
																<c:if test="${record.contractStatus eq '1'}"><c:out value="生成成功"/></c:if>
																<c:if test="${record.contractStatus eq '2'}"><c:out value="签署成功"/></c:if>
																<c:if test="${record.contractStatus eq '3'}"><c:out value="下载成功"/></c:if>
															</td>
															<td class="center"><c:out value="${record.contractNumber }"></c:out></td>
															<td class="center">
																<div class="visible-md visible-lg hidden-sm hidden-xs">
																	<c:if test="${record.resendMessage=='1' }">
																		<shiro:hasPermission name="borrowinvest:SEARCH">
																			<a class="btn btn-transparent btn-xs tooltips fn-Modify" data-userid="${record.userId }" data-nid="${record.nid }" data-borrownid="${record.borrowNid}" data-toggle="tooltip" data-placement="top" data-original-title="出借协议重发"><i class="fa fa-file-text"></i></a>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="borrowinvest:EXPORTAGREEMENT">
																			<a class="btn btn-transparent btn-xs tooltips fn-ExportAgreement" data-userid="${record.userId }" data-nid="${record.nid }" data-borrownid="${record.borrowNid}" data-toggle="tooltip" data-placement="top" data-original-title="发送协议"><i class="fa fa-download"></i></a>
																		</shiro:hasPermission>
																	</c:if>
																	<!-- 出借人债权明细查询 -->
																	<shiro:hasPermission name="borrowinvest:DEBTCHECK">
																		<a class="btn btn-transparent btn-xs tooltips fn-check" data-userid="${record.userId }" data-nid="${record.nid }" data-borrownid="${record.borrowNid}" data-toggle="tooltip" data-placement="top" data-original-title="查询出借人债权明细"><i class="fa fa-exchange"></i></a>
																	</shiro:hasPermission>
																	<!-- 合同状态 为成功-->
																	<c:if test="${record.resendMessage == '1' and record.contractStatus == '3'}">
																		<shiro:hasPermission name="borrowinvest:PDFDOWNLOAD">
																			<a class="btn btn-transparent btn-xs tooltips fn-PDFDOWNLOAD"  target="_blank" href="${record.downloadUrl}" data-toggle="tooltip" data-placement="top" data-original-title="下载"><i class="fa fa-download"></i></a>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="borrowinvest:PDFVIEW">
																			<a class="btn btn-transparent btn-xs tooltips fn-PDFVIEW"  target="_blank" href="${record.viewpdfUrl}" data-toggle="tooltip" data-placement="top" data-original-title="查看"><i class="fa fa-file-text"></i></a>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="borrowinvest:PDFPREVIEW">
																			<a class="btn btn-transparent btn-xs tooltips" target="_blank" href="${webRoot}/manager/borrow/borrowinvest/pdfPreviewAction.do?nid=${record.nid }" data-toggle="tooltip" data-placement="top" data-original-title="预览"><i class="fa ti-layers-alt"></i></a>
																		</shiro:hasPermission>
																	</c:if>
																	<c:if test="${record.resendMessage == '1'and record.contractStatus != '3'}">
																		<shiro:hasPermission name="borrowinvest:PDFSIGN">
																			<a class="btn btn-transparent btn-xs tooltips fn-PdfSign"  data-userid="${record.userId }" data-nid="${record.nid }" data-borrownid="${record.borrowNid}" data-toggle="tooltip" data-placement="top" data-original-title="PDF签署"><i class="fa ti-layers-alt"></i></a>
																		</shiro:hasPermission>
																	</c:if>
																</div>
																<div class="visible-xs visible-sm hidden-md hidden-lg">
																	<div class="btn-group">
																		<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																			<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																		</button>
																		<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																			<c:if test="${record.resendMessage=='1' }">
																				<shiro:hasPermission name="borrowinvest:SEARCH">
																					<li><a class="fn-Modify" data-userid="${record.userId }" data-nid="${record.nid }" data-borrowNid="${record.borrowNid}">出借协议重发</a></li>
																				</shiro:hasPermission>
																				<shiro:hasPermission name="borrowinvest:EXPORTAGREEMENT">
																					<li><a class="fn-ExportAgreement" data-userid="${record.userId }" data-nid="${record.nid }" data-borrowNid="${record.borrowNid}">发送协议</a></li>
																				</shiro:hasPermission>
																				<shiro:hasPermission name="borrowinvest:EXPORTAGREEMENT">
																					<li><a class="fn-ExportAgreement" data-userid="${record.userId }" data-nid="${record.nid }" data-borrowNid="${record.borrowNid}">发送协议</a></li>
																				</shiro:hasPermission>
																			</c:if>
																			<shiro:hasPermission name="borrowinvest:DEBTCHECK">
																				<li><a class="fn-check" data-userid="${record.userId }" data-nid="${record.nid }" data-borrowNid="${record.borrowNid}">查询出借人债权明细</a></li>
																			</shiro:hasPermission>
																			<c:if test="${record.resendMessage == '1' and record.contractStatus == '3'}" >
																				<shiro:hasPermission name="borrowinvest:PDFDOWNLOAD">
																					<li>
																						<a class="fn-PDFDOWNLOAD" target="_blank" href="${record.downloadUrl}" >下载</a>
																					</li>
																				</shiro:hasPermission>
																				<shiro:hasPermission name="borrowinvest:PDFVIEW">
																					<li>
																						<a target="_blank" href="${record.viewpdfUrl}" >查看</a>
																					</li>
																				</shiro:hasPermission>
																				<shiro:hasPermission name="borrowinvest:PDFPREVIEW">
																					<li>
																						<a target="_blank" href="${webRoot}/manager/borrow/borrowinvest/pdfPreviewAction.do?nid=${record.nid }">预览</a>
																					</li>
																				</shiro:hasPermission>
																			</c:if>
																			<!--PDF签署-->
																			<c:if test="${record.resendMessage == '1' and record.contractStatus != '3'}" >
																				<shiro:hasPermission name="borrowinvest:PDFSIGN">
																					<li><a class="fn-PdfSign" data-userid="${record.userId }" data-nid="${record.nid }" data-borrowNid="${record.borrowNid}">PDF签署</a></li>
																				</shiro:hasPermission>
																			</c:if>
																		</ul>
																	</div>
																</div>
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
														<td></td>
														<td align="right"><c:out value="${sumAccount }"></c:out></td>
														<td></td>
														<td></td>
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
										<shiro:hasPermission name="borrowinvest:SEARCH">
											<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="optActionSearch" paginator="${borrowInvestForm.paginator}"></hyjf:paginator>
										</shiro:hasPermission>
										<br />
										<br />
									</div>
								</div>
							</div>
							<input type="hidden" id="planNidTemp" name = "planNidTemp" value="${borrowInvestForm.planNidTemp}">
								<%-- 原始标的检索面板 (ignore) --%>
							<tiles:putAttribute name="searchPanels" type="string">
								<shiro:hasPermission name="borrowinvest:SEARCH">
									<input type="hidden" name=borrowNid id="borrowNid" />
									<input type="hidden" name="userId" id="userId" />
									<input type="hidden" name="nid" id="nid" />
									<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowInvestForm.paginatorPage}" />
									<input type="hidden" name="planNid" id="planNid" value="${borrowInvestForm.planNidSrch}">
									<input type="hidden" name="planNidNew" id="planNidNew" value="${borrowInvestForm.planNidSrch}">
									<input type="hidden" name="assignPlanNid" id="assignPlanNid" value="${borrowInvestForm.planNidSrch}">
									<input type="hidden" id="myPlanNid" name="planNidTemp" value="${borrowInvestForm.planNidTemp}">
                                    <input type="hidden" id="isSearch" name="isSearch"/>
									<!-- 检索条件 -->
									<div class="form-group">
										<label>用户名</label> <input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowInvestForm.usernameSrch}" />
									</div>
									<div class="form-group">
										<label>推荐人</label> <input type="text" name="referrerNameSrch" id="referrerNameSrch" class="form-control input-sm underline" value="${borrowInvestForm.referrerNameSrch}" />
									</div>
									<div class="form-group">
										<label>项目编号</label> <input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${borrowInvestForm.borrowNidSrch}" />
									</div>
									<div class="form-group">
										<label>智投编号</label>
										<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${borrowInvestForm.planNidSrch}" />
									</div>
									<div class="form-group">
										<label>借款期限</label> <input type="text" name="borrowPeriod" id="borrowPeriod" class="form-control input-sm underline" value="${borrowInvestForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2" />
									</div>
									<!-- 				<div class="form-group"> -->
									<!-- 					<label>渠道</label> -->
									<!-- 					<select name="utmIdSrch" class="form-control underline form-select2"> -->
									<!-- 						<option value=""></option> -->
									<%-- 						<c:forEach items="${utmList }" var="utm" begin="0" step="1" varStatus="status"> --%>
									<%-- 							<option value="${utm.sourceId }" --%>
									<%-- 								<c:if test="${utm.sourceId eq borrowInvestForm.utmIdSrch}">selected="selected"</c:if>> --%>
									<%-- 								<c:out value="${utm.sourceName }"></c:out></option> --%>
									<%-- 						</c:forEach> --%>
									<!-- 					</select> -->
									<!-- 				</div> -->
									<div class="form-group">
										<label>出借方式</label> <select name="investType" class="form-control underline form-select2">
										<option value=""></option>
										<c:forEach items="${investTypeList }" var="investType" begin="0" step="1" varStatus="status">
											<option value="${investType.nameCd }" <c:if test="${investType.nameCd eq borrowInvestForm.investType}">selected="selected"</c:if>>
												<c:out value="${investType.name}"></c:out>
											</option>
										</c:forEach>
									</select>
									</div>
									<div class="form-group">
										<label>还款方式</label> <select name="borrowStyleSrch" class="form-control underline form-select2">
										<option value=""></option>
										<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
											<option value="${borrowStyle.nid }" <c:if test="${borrowStyle.nid eq borrowInvestForm.borrowStyleSrch}">selected="selected"</c:if>>
												<c:out value="${borrowStyle.name}"></c:out>
											</option>
										</c:forEach>
									</select>
									</div>
									<%--add by zhangyk 增加是否复投投标的查询 start--%>
									<div class="form-group">
										<label>复投投标</label>
										<select name="tenderType" id='isTenderType' class="form-control underline form-select2">
											<option value=""></option>
                                            <option value="1" <c:if test="${borrowInvestForm.tenderType eq '1'}">selected="selected"</c:if>>是</option>
											<option value="0" <c:if test="${borrowInvestForm.tenderType eq '0'}">selected="selected"</c:if>>否</option>
										</select>
									</div>
									<%--add by zhangyk 增加是否复投投标的查询 end--%>
									<div class="form-group">
										<label>操作平台</label> <select name="clientSrch" class="form-control underline form-select2">
										<option value=""></option>
										<c:forEach items="${clientList }" var="client" begin="0" step="1" varStatus="status">
											<option value="${client.nameCd }" <c:if test="${client.nameCd eq borrowInvestForm.clientSrch}">selected="selected"</c:if>>
												<c:out value="${client.name}"></c:out>
											</option>
										</c:forEach>
									</select>
									</div>
									<div class="form-group">
										<label>出借时间</label>
										<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${borrowInvestForm.timeStartSrch}" /> <i class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${borrowInvestForm.timeEndSrch}" />
										</div>
									</div>
								</shiro:hasPermission>
							</tiles:putAttribute>
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
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/hjhplan/optrecordtender.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
