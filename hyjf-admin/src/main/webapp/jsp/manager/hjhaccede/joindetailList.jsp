<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="accedelist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="智投订单" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">智投订单</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<div class="tab-content">
				<div class="tab-pane fade in active">
				
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${accedeForm.paginator.firstPage ? ' disabled' : ''}"></c:set><!-- 原 joindetailForm -->
						<c:set var="jspNextDsb" value="${accedeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="accedelist:EXPORT">
							<shiro:hasPermission name="accedelist:ORGANIZATION_VIEW">
								<a class="btn btn-o btn-primary btn-sm fn-EnhanceExport" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel<i class="fa fa-Export"></i></a>
							</shiro:hasPermission>
							<shiro:lacksPermission name="accedelist:ORGANIZATION_VIEW">
								<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel<i class="fa fa-Export"></i></a>
							</shiro:lacksPermission>
						</shiro:hasPermission>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>	
						<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" 
							data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel">
							<i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i>
						</a>
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
								<th class="center">智投订单号</th>
								<th class="center">智投编号</th>
								<th class="center">服务回报期限</th>
								<th class="center">参考年回报率</th>
								<th class="center">用户名</th>
								<th class="center">推荐人</th>
								<th class="center">授权服务金额(元)</th>
								<th class="center">自动投标进度</th>
								<th class="center">可用余额(元)</th>
								<th class="center">冻结金额(元)</th>
								<th class="center">公允价值(元)</th>
								<th class="center">实际出借利率</th>
								<th class="center">出借笔数</th>
								<th class="center">操作平台</th>
								<th class="center">订单状态</th>
								<th class="center">匹配期</th>

								<th class="center">授权服务时间</th>
								<th class="center">开始计息时间</th>

								<th class="center">预计开始退出时间</th>
								<th class="center">实际退出时间</th>

								<th class="center">合同状态</th>
								<th class="center">合同编号</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty accedeForm.recordList}">
									<tr>
										<td colspan="22">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${accedeForm.recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((accedeForm.paginatorPage - 1) * accedeForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.planOrderId }"></c:out></td>
											<td class="center"><c:out value="${record.debtPlanNid }"></c:out></td>
											<td class="center"><c:out value="${record.debtLockPeriod }"></c:out></td>
											<td class="center"><c:out value="${record.expectApr }"></c:out>%</td>
											<td class="center"><c:out value="${record.userName }"></c:out></td>
											<td class="center"><c:out value="${record.inviteName }"></c:out></td>
											<%-- <td class="center"><c:out value="${record.accedeAccount }"></c:out></td> --%>
											<td class="center"><fmt:formatNumber value="${record.accedeAccount }" pattern="#,##0.00#"/></td>
											<td class="center"><c:out value="${record.autoBidProgress }"></c:out>%</td>
											<td class="center"><fmt:formatNumber value="${record.availableInvestAccount }" pattern="#,##0.00#"/></td>											
											<td class="center"><fmt:formatNumber value="${record.frostAccount }" pattern="#,##0.00#"/></td>
											<td class="center"><fmt:formatNumber value="${record.fairValue }" pattern="#,##0.00#"/></td>
											<td class="center"><c:out value="${record.actualApr }"></c:out>%</td>
											<td class="center"><c:out value="${record.investCounts }"></c:out></td>
											<td class="center">
												<c:if test="${record.platform eq '0' }">
													PC
												</c:if>
												<c:if test="${record.platform eq '1' }">
													微官网
												</c:if>
												<c:if test="${record.platform eq '2' }">
													android
												</c:if>
												<c:if test="${record.platform eq '3' }">
													iOS
												</c:if>
												<c:if test="${record.platform eq '4' }">
													其他
												</c:if>
											</td>
											<!-- 订单状态 ：0自动投标中 2自动投标成功 3锁定中 5退出中 7已退出 99 自动出借异常-->
											<td class="center">
												<c:if test="${record.orderStatus eq '0' }">
													自动投标中
												</c:if>
												<c:if test="${record.orderStatus eq '2' }">
													自动投标成功
												</c:if>
												<c:if test="${record.orderStatus eq '3' }">
													锁定中
												</c:if>
												<c:if test="${record.orderStatus eq '5' }">
													退出中
												</c:if>
												<c:if test="${record.orderStatus eq '7' }">
													已退出
												</c:if>
												<c:if test="${record.orderStatus eq '80' }">
													80:出借失败(异常中心)
												</c:if>
												<c:if test="${record.orderStatus eq '82' }">
													82:复投失败(异常中心)
												</c:if>
												<c:if test="${record.orderStatus eq '83' }">
													83:复投失败(异常中心)
												</c:if>
												<c:if test="${record.orderStatus eq '90' }">
													90:出借失败(联系管理员)
												</c:if>
												<c:if test="${record.orderStatus eq '92' }">
													92:复投失败(联系管理员)
												</c:if>
												<c:if test="${record.orderStatus eq '93' }">
													93:复投失败(联系管理员)
												</c:if>
												<c:if test="${record.orderStatus eq '99' }">
													99:失败(联系管理员)
												</c:if>
											</td>
											<td class="center">
												<c:out value="${record.matchDates }天"></c:out>
											</td>

											<td class="center">
												<c:out value="${record.createTime }"></c:out>
											</td>
											<td class="center">
												<c:out value="${record.countInterestTime }"></c:out>
											</td>

											<td class="center">
												<fmt:formatDate value="${record.endDate}" pattern="yyyy-MM-dd" />
											</td>
											<td class="center">
												<c:if test="${record.acctualPaymentTime ne 0}">
													<hyjf:datetime value="${record.acctualPaymentTime }"></hyjf:datetime>
												</c:if>
											</td>

											<td class="center">
												<c:if test="${record.contractStatus eq '' || record.contractStatus eq '0'}"><c:out value="初始"/></c:if>
												<c:if test="${record.contractStatus eq '1'}"><c:out value="生成成功"/></c:if>
												<c:if test="${record.contractStatus eq '2'}"><c:out value="签署成功"/></c:if>
												<c:if test="${record.contractStatus eq '3'}"><c:out value="下载成功"/></c:if>
											</td>
											<td class="center"><c:out value="${record.contractNumber }"></c:out></td>
											<!-- 操作 -->
											<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<shiro:hasPermission name="accedelist:VIEW">
															<a class="btn btn-transparent btn-xs fn-TenderInfo" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }"
																	data-toggle="tooltip" data-placement="top" data-original-title="出借明细">出借明细</a>
														</shiro:hasPermission>
														<!-- 协议发送状态 ：0未发送 1已发送 -->
														<!-- 汇计划三期协议重发注掉 -->
<%-- 														<shiro:hasPermission name="accedelist:SEARCH">
															<a class="btn btn-transparent btn-xs tooltips fn-Modify" data-userid="${record.userId }" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }" data-toggle="tooltip" data-placement="top" data-original-title="出借协议重发"><i class="fa fa-file-text"></i></a>
														</shiro:hasPermission> --%>
															
														<!-- 汇计划三期 修改发送协议 -->	
														<!-- 前置条件：计划订单的状态为“锁定中”/“退出中”/“已退出” -->
														<c:if test="${record.orderStatus == '3' || record.orderStatus == '5' || record.orderStatus == '7'}">
															<shiro:hasPermission name="accedelist:SEARCH">
																<a class="btn btn-transparent btn-xs tooltips fn-ExportAgreement" data-userid="${record.userId }" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }" data-toggle="tooltip" data-placement="top" data-original-title="发送协议">发送协议</a>
															</shiro:hasPermission>
														</c:if>
														
														<!-- 合同状态 为成功-->
														<c:if test="${record.contractStatus == '3'}">
															<shiro:hasPermission name="accedelist:PDFDOWNLOAD">
																<a class="btn btn-transparent btn-xs tooltips fn-PdfDownload" target="_blank" href="${record.downloadUrl}" data-toggle="tooltip" data-placement="top" data-original-title="下载"><i class="fa fa-download"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="accedelist:PDFVIEW">
																<a class="btn btn-transparent btn-xs tooltips fn-PdfView"  target="_blank" href="${record.viewpdfUrl}" data-toggle="tooltip" data-placement="top" data-original-title="查看"><i class="fa fa-file-text"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="accedelist:PDFPREVIEW">
																<a class="btn btn-transparent btn-xs tooltips fn-PdfPreview" target="_blank" href="${webRoot}/manager/hjhplan/accedelist/pdfPreviewAction.do?nid=${record.planOrderId }" data-toggle="tooltip" data-placement="top" data-original-title="预览"><i class="fa ti-layers-alt"></i></a>
															</shiro:hasPermission>
														</c:if>
														<!--PDF签署-->
														<c:if test="${record.contractStatus != '3'}">
															<shiro:hasPermission name="accedelist:PDFSIGN">
																<a class="btn btn-transparent btn-xs tooltips fn-PdfSign"  data-userid="${record.userId }" data-planorderid="${record.planOrderId }" data-toggle="tooltip" data-placement="top" data-original-title="PDF签署"><i class="fa ti-layers-alt"></i></a>
															</shiro:hasPermission>
														</c:if>
													</div>
													<div class="visible-xs visible-sm hidden-md hidden-lg">
														<div class="btn-group">
															<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
															</button>
															<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																<shiro:hasPermission name="accedelist:VIEW">
																	<li>
																		<a class="fn-TenderInfo" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }">出借明细</a>
																	</li>
																</shiro:hasPermission>
<%-- 																<shiro:hasPermission name="accedelist:SEARCH">
																	<li><a class="fn-Modify" data-userid="${record.userId }" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }">出借协议重发</a></li>
																</shiro:hasPermission> --%>
																<shiro:hasPermission name="accedelist:SEARCH">
																	<li><a class="fn-ExportAgreement" data-userid="${record.userId }" data-planOrderId="${record.planOrderId }" data-debtPlanNid="${record.debtPlanNid }">发送协议</a></li>
																</shiro:hasPermission>
																<c:if test="${record.contractStatus == '3'}" >
																	<shiro:hasPermission name="accedelist:PDFDOWNLOAD">
																		<li>
																			<a target="_blank" href="${record.downloadUrl}" >下载</a>
																		</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="accedelist:PDFVIEW">
																		<li>
																			<a target="_blank" href="${record.viewpdfUrl}" >查看</a>
																		</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="accedelist:PDFPREVIEW">
																		<li>
																			<a target="_blank" href="${webRoot}/manager/hjhplan/accedelist/pdfPreviewAction.do?nid=${record.planOrderId }" >预览</a>
																		</li>
																	</shiro:hasPermission>
																</c:if>
																<!--PDF签署-->
																<c:if test="${record.contractStatus != '3'}" >
																	<shiro:hasPermission name="accedelist:PDFSIGN">
																		<li><a class="fn-PdfSign" data-userid="${record.userId }" data-planorderid="${record.planOrderId }">PDF签署</a></li>
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
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<td align="center"><fmt:formatNumber value="${sumAccedeRecord.sumAccedeAccount }" pattern="#,##0.00#"/></td>
								<%-- <td align="center"><fmt:formatNumber value="${sumAccedeRecord.sumAlreadyInvest }" pattern="#,##0.00#"/></td>
								<td align="center"><fmt:formatNumber value="${sumAccedeRecord.sumWaitTotal }" pattern="#,##0.00#"/></td>
								<td align="center"><fmt:formatNumber value="${sumAccedeRecord.sumWaitCaptical }" pattern="#,##0.00#"/></td>
								<td align="center"><fmt:formatNumber value="${sumAccedeRecord.sumWaitInterest }" pattern="#,##0.00#"/></td> --%>
								<th>&nbsp;</th>
								<td align="center"><fmt:formatNumber value="${sumAccedeRecord.sumAvailableInvestAccount }" pattern="#,##0.00#"/></td>
								<td align="center"><fmt:formatNumber value="${sumAccedeRecord.sumFrostAccount }" pattern="#,##0.00#"/></td>
								<td align="center"><fmt:formatNumber value="${sumAccedeRecord.sumFairValue }" pattern="#,##0.00#"/></td>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
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
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${accedeForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="accedelist:SEARCH">
				<input type="hidden" name="planOrderId" id="planOrderId" />
				<input type="hidden" name="userId" id="userId" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${accedeForm.paginatorPage}" />
				<!-- 查询条件 -->
				<div class="form-group">
					<label>智投订单号</label>
					<input type="text" name="accedeOrderIdSrch" id="accedeOrderIdSrch" class="form-control input-sm underline" value="${accedeForm.accedeOrderIdSrch}" />
				</div>
				<div class="form-group">
					<label>智投编号</label>
					<input type="text" name="debtPlanNidSrch" id="debtPlanNidSrch" class="form-control input-sm underline" value="${accedeForm.debtPlanNidSrch}" />
				</div>

				<div class="form-group">
					<label>服务回报期限</label>
					<input type="text" name="debtLockPeriodSrch" id="debtLockPeriodSrch" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" class="form-control input-sm underline" value="${accedeForm.debtLockPeriodSrch}"
					/>
				</div>
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userNameSrch" id="userNameSrch" class="form-control input-sm underline" value="${accedeForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>推荐人</label>
					<input type="text" name="refereeNameSrch" id="refereeNameSrch" class="form-control input-sm underline" value="${accedeForm.refereeNameSrch}" />
				</div>
				<div class="form-group">
					<label>操作平台</label>
					<select name="platformSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="0" <c:if test="${'0' eq accedeForm.platformSrch}">selected="selected"</c:if>>
							<c:out value="PC"></c:out>
						</option>
						<option value="1" <c:if test="${'1' eq accedeForm.platformSrch}">selected="selected"</c:if>>
							<c:out value="微官网"></c:out>
						</option>
						<option value="2" <c:if test="${'2' eq accedeForm.platformSrch}">selected="selected"</c:if>>
							<c:out value="android"></c:out>
						</option>
						<option value="3" <c:if test="${'3' eq accedeForm.platformSrch}">selected="selected"</c:if>>
							<c:out value="ios"></c:out>
						</option>
					</select>
				</div>

				<div class="form-group">
					<label>订单状态</label>
					<select name="orderStatus" class="form-control underline form-select2"><!-- 原 userAttributeSrch -->
						<option value="">全部</option>
						<option value="0" <c:if test="${'0' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="自动投标中"></c:out>
						</option>
						<option value="2" <c:if test="${'2' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="自动投标成功"></c:out>
						</option>
						<option value="3" <c:if test="${'3' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="锁定中"></c:out>
						</option>
						<option value="5" <c:if test="${'5' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="退出中"></c:out>
						</option>
						<option value="7" <c:if test="${'7' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="已退出"></c:out>
						</option>
						<option value="80" <c:if test="${'80' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="80:出借失败(异常中心)"></c:out>
						</option>
						<option value="82" <c:if test="${'82' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="82:复投失败(异常中心)"></c:out>
						</option>
						<option value="83" <c:if test="${'83' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="83:复投失败(异常中心)"></c:out>
						</option>
						<option value="90" <c:if test="${'90' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="90:出借失败(联系管理员)"></c:out>
						</option>
						<option value="92" <c:if test="${'92' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="92:复投失败(联系管理员)"></c:out>
						</option>
						<option value="93" <c:if test="${'93' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="93:复投失败(联系管理员)"></c:out>
						</option>
						<option value="99" <c:if test="${'99' eq accedeForm.orderStatus}">selected="selected"</c:if>>
							<c:out value="99:失败(联系管理员)"></c:out>
						</option>
					</select>
				</div>


				<!-- 汇计划三期新增 -->
				<div class="form-group">
					<label>匹配期</label>
					<input type="text" name="matchDatesSrch" id="matchDatesSrch" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" class="form-control input-sm underline" value="${accedeForm.matchDatesSrch}" 
					/>
				</div>
				
				<!-- 汇计划三期新增 -->
				<div class="form-group">
					<label>出借笔数</label>
					<input type="text" name="investCountsSrch" id="investCountsSrch" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" class="form-control input-sm underline" value="${accedeForm.investCountsSrch}" 
					/>
				</div>
				
				<div class="form-group">
					<label>授权服务时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"><input type="text" name="searchStartDate" id="start-date-time" class="form-control underline" value="${accedeForm.searchStartDate}" /> <i class="ti-calendar"></i></span>
						<span class="input-group-addon no-border bg-light-orange">~</span><input type="text" name="searchEndDate" id="end-date-time" class="form-control underline" value="${accedeForm.searchEndDate}" />
					</div>
				</div>
				<div class="form-group">
					<label>开始计息时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"><input type="text" name="countInterestTimeStartDate" id="start-time" class="form-control underline" value="${accedeForm.countInterestTimeStartDate}" /> <i class="ti-calendar"></i></span>
						<span class="input-group-addon no-border bg-light-orange">~</span><input type="text" name="countInterestTimeEndDate" id="end-time" class="form-control underline" value="${accedeForm.countInterestTimeEndDate}" />
					</div>
				</div>
				<div class="form-group">
					<label>预计开始退出时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
	<input type="text" name="endDateStartSrch" id="endDateStartSrch" class="form-control underline" value="${accedeForm.endDateStartSrch}" />
							<i class="ti-calendar"></i></span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
	<input type="text" name="endDateEndSrch" id="endDateEndSrch" class="form-control underline" value="${accedeForm.endDateEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>实际退出时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
	<input type="text" name="acctualPaymentTimeStartSrch" id="acctualPaymentTimeStartSrch" class="form-control underline" value="${accedeForm.acctualPaymentTimeStartSrch}" /> <i class="ti-calendar"></i></span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
	<input type="text" name="acctualPaymentTimeEndSrch" id="acctualPaymentTimeEndSrch" class="form-control underline" value="${accedeForm.acctualPaymentTimeEndSrch}" />
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
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/hjhaccede/joindetailList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
