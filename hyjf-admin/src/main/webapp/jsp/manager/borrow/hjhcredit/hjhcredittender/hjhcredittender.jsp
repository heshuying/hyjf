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
<shiro:hasPermission name="hjhcredittender:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="出借明细-债转" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">che
			<h1 class="mainTitle">出借明细-债转</h1>
			<span class="mainDescription">出借明细-债转</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<c:if test="${hjhcredittenderForm.isAccedelist eq '1'}">
			<ul class="nav nav-tabs" id="myTab">
				<shiro:hasPermission name="finmanchargenew:VIEW">
		      		<li><a href="${webRoot}/manager/hjhplan/accedelist/tenderInfoAction?debtPlanNidSrch=${hjhcredittenderForm.assignPlanNid }&accedeOrderIdSrch=${hjhcredittenderForm.assignPlanOrderId }">投标明细-原始</a></li>
		      	</shiro:hasPermission>
				<shiro:hasPermission name="projecttype:VIEW">
					<li class="active"><a href="${webRoot}/manager/borrow/hjhcredit/hjhcredittender/init?isAccedelist=1&assignPlanNid=${hjhcredittenderForm.assignPlanNid}&assignPlanOrderId=${hjhcredittenderForm.assignPlanOrderId}">出借明细-债转</a></li>
				</shiro:hasPermission>
			</ul>
			</c:if>
				<div class="tab-content">
				<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="hjhcredittender:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${hjhcredittenderForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${hjhcredittenderForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="hjhcredittender:EXPORT">
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
										<th class="center">承接人</th>
										<th class="center">承接智投编号</th>
										<th class="center">承接智投订单号</th>
										<th class="center">承接订单号</th>
										<th class="center">出让人</th>
										<th class="center">债转编号</th>
										<th class="center">原项目编号</th>
										<th class="center">还款方式</th>
										<th class="center">承接本金</th>
										<th class="center">垫付利息</th>
										<th class="center">实际支付金额</th>

										<th class="center">债转服务费率</th>
										<th class="center">债转服务费(元)</th>
										<th class="center">复投承接(是/否)</th>
										<%--<th class="center">承接方式</th>--%>
										<th class="center">承接时间</th>
										<th class="center">项目期数</th>
										<%--X/X  项目总期数与清算时所在期数合并， 前面显示清算时所在期数，后面显示标的的总期数--%>
										<%--<th class="center">承接时所在期数</th>--%>
										<th class="center">合同状态</th>
										<th class="center">合同编号</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="22">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(hjhcredittenderForm.paginatorPage -1 ) * hjhcredittenderForm.paginator.limit + status.index + 1 }</td>
													<td align="right"><c:out value="${record.assignUserName }"/></td>
													<td><c:out value="${record.assignPlanNid }"/></td>
													<td><c:out value="${record.assignPlanOrderId }"/></td>
													<td><c:out value="${record.assignOrderId }"/></td>
													<td align="right"><c:out value="${record.creditUserName }"/></td>
													<td align="right"><c:out value="${record.creditNid }"/></td>
													<td align="right"><c:out value="${record.borrowNid }"/></td>
													<td align="right"><c:out value="${record.repayStyleName }"/></td>
													<td align="right"><c:out value="${record.assignCapital }"/></td>
													<td align="right"><c:out value="${record.assignInterestAdvance }"/></td>
													<td align="right"><c:out value="${record.assignPay }"/></td>
													<td class="center"><c:out value="${record.assignServiceApr }"/></td>
													<td align="right"><c:out value="${record.assignServiceFee }"/></td>
													<td align="center"><c:out value="${record.tenderType}"/></td>
													<%--<td class="center"><c:out value="${record.assignTypeName }"/></td>--%>
													<td class="center"><c:out value="${record.assignTime }"/></td>
													<td class="center"><c:out value="${record.assignPeriod }/${record.borrowPeriod }"/></td>
													<%--<td class="center"><c:out value="${record.assignPeriod }"/></td>--%>

													<td class="center"><c:if test="${record.contractStatus eq '' || record.contractStatus eq '0'}"><c:out value="初始"/></c:if>
														<c:if test="${record.contractStatus eq '1'}"><c:out value="生成成功"/></c:if>
														<c:if test="${record.contractStatus eq '2'}"><c:out value="签署成功"/></c:if>
														<c:if test="${record.contractStatus eq '3'}"><c:out value="下载成功"/></c:if>
													</td>
													<td class="center"><c:out value="${record.contractNumber }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<!-- 合同状态 为成功-->
															<c:if test="${record.contractStatus == '3'}">
																<shiro:hasPermission name="hjhcredittender:PDFDOWNLOAD">
																	<a class="btn btn-transparent btn-xs tooltips fn-PdfDownload"  target="_blank" href="${record.downloadUrl}" data-toggle="tooltip" data-placement="top" data-original-title="下载"><i class="fa fa-download"></i></a>
																</shiro:hasPermission>
																<shiro:hasPermission name="hjhcredittender:PDFVIEW">
																	<a class="btn btn-transparent btn-xs tooltips fn-Pdfview" target="_blank" href="${record.viewpdfUrl}" data-toggle="tooltip" data-placement="top" data-original-title="查看"><i class="fa fa-file-text"></i></a>
																</shiro:hasPermission>
																<shiro:hasPermission name="hjhcredittender:PDFPREVIEW">
																	<a class="btn btn-transparent btn-xs tooltips fn-PdfPreview" target="_blank" href="${webRoot}/manager/borrow/hjhcredit/hjhcredittender/pdfPreviewAction.do?nid=${record.assignOrderId }" data-toggle="tooltip" data-placement="top" data-original-title="预览"><i class="fa ti-layers-alt"></i></a>
																</shiro:hasPermission>
															</c:if>
                                                            <c:if test="${record.contractStatus != '3'}">
                                                                <shiro:hasPermission name="hjhcredittender:PDFSIGN">
                                                                    <a class="btn btn-transparent btn-xs tooltips fn-PdfSign" data-userid="${record.userId }" data-assignorderid="${record.assignOrderId }" data-borrownid="${record.borrowNid}" data-contractstatus="${record.contractStatus}" data-creditnid="${record.creditNid}" data-toggle="tooltip" data-placement="top" data-original-title="PDF签署"><i class="fa ti-layers-alt"></i></a>
                                                                </shiro:hasPermission>
                                                            </c:if>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<c:if test="${record.contractStatus == '3'}" >
																		<shiro:hasPermission name="hjhcredittender:PDFDOWNLOAD">
																			<li>
																				<a target="_blank" href="${record.downloadUrl}" >下载</a>
																			</li>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="hjhcredittender:PDFVIEW">
																			<li>
																				<a target="_blank" href="${record.viewpdfUrl}" >查看</a>
																			</li>
																		</shiro:hasPermission>
																		<shiro:hasPermission name="hjhcredittender:PDFPREVIEW">
																			<li>
																				<a target="_blank" href="${webRoot}/manager/borrow/hjhcredit/hjhcredittender/pdfPreviewAction.do?nid=${record.assignOrderId}" >预览</a>
																			</li>
																		</shiro:hasPermission>
																	</c:if>

                                                                    <c:if test="${record.contractStatus != '3'}">
                                                                        <shiro:hasPermission name="hjhcredittender:PDFSIGN">
                                                                            <li>
                                                                                <a class="fn-PdfSign"  data-userid="${record.userId }" data-assignorderid="${record.assignOrderId }" data-borrownid="${record.borrowNid}">PDF签署</a>
                                                                            </li>
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
												<td></td>
												<td></td>
												<td align="right"><c:out value="${sum.sumAssignCapital}"></c:out></td>
												<td align="right"><c:out value="${sum.sumAssignInterestAdvance}"></c:out></td>
												<td align="right"><c:out value="${sum.sumAssignPay}"></c:out></td>
												<td></td>
												<td align="right"><c:out value="${sum.sumAssignServiceFee}"></c:out></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<%--<td></td>--%>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="hjhcredittender:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${hjhcredittenderForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>

							<br/><br/>
						</div>
					</div>
				</div>
			</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="hjhcredittender:SEARCH">
				<input type="hidden" name="id" id="id" />
                <input type="hidden" name="borrowNidHidden" id="borrowNidHidden" />
                <input type="hidden" name="userIdHidden" id="userIdHidden" />
                <input type="hidden" name="assignNidHidden" id="assignNidHidden" />
                <input type="hidden" name="contractStatusHidden" id="contractStatusHidden" />
                <input type="hidden" name="creditNidHidden" id="creditNidHidden" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${hjhcredittenderForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>承接人</label>
					<input type="text" name="assignUserName" class="form-control input-sm underline" value="${hjhcredittenderForm.assignUserName}" />
				</div>
				<div class="form-group">
					<label>承接智投编号</label>
					<input type="text" name="assignPlanNid" class="form-control input-sm underline" value="${hjhcredittenderForm.assignPlanNid}" />
				</div>
				<div class="form-group">
					<label>承接智投订单号</label>
					<input type="text" name="assignPlanOrderId" class="form-control input-sm underline" value="${hjhcredittenderForm.assignPlanOrderId}" />
				</div>
				<div class="form-group">
					<label>出让人</label>
					<input type="text" name="creditUserName" class="form-control input-sm underline" value="${hjhcredittenderForm.creditUserName}" />
				</div>
				<div class="form-group">
					<label>债转编号</label>
					<input type="text" name="creditNid" class="form-control input-sm underline" value="${hjhcredittenderForm.creditNid}" />
				</div>
				<div class="form-group">
					<label>原项目编号</label>
					<input type="text" name="borrowNid" id="borrowNid" class="form-control input-sm underline" value="${hjhcredittenderForm.borrowNid}" />
				</div>
				<div class="form-group">
					<label>还款方式</label>
					<select name="repayStyle" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowStyleList }" var="repayStyle" begin="0" step="1">
							<option value="${repayStyle.nid }"
								<c:if test="${repayStyle.nid eq hjhcredittenderForm.repayStyle}">selected="selected"</c:if>>
								<c:out value="${repayStyle.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>承接方式</label>
					<select name="assignType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${assignTypeList }" var="assignType" begin="0" step="1">
							<option value="${assignType.nameCd }"
								<c:if test="${assignType.nameCd eq hjhcredittenderForm.assignType}">selected="selected"</c:if>>
								<c:out value="${assignType.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>

				<div class="form-group">
					<label>复投承接</label>
					<select name="tenderType"  class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="1" <c:if test="${'1' eq hjhcredittenderForm.tenderType}">selected="selected"</c:if>>是</option>
						<option value="0" <c:if test="${'0' eq hjhcredittenderForm.tenderType}">selected="selected"</c:if> >否</option>
					</select>
				</div>
				<div class="form-group">
					<label>承接时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="assignTimeStart" id="assignTimeStart" class="form-control underline" value="${hjhcredittenderForm.assignTimeStart}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="assignTimeEnd" id="assignTimeEnd" class="form-control underline" value="${hjhcredittenderForm.assignTimeEnd}" />
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
			<style type ="text/css">
				.search-classic{overflow-x:scroll;}
				#equiList{display:block;}
				.table>caption+thead>tr:first-child>th, 
				.table>colgroup+thead>tr:first-child>th, 
				.table>thead:first-child>tr:first-child>th, 
				.table>caption+thead>tr:first-child>td,
				 .table>colgroup+thead>tr:first-child>td, 
				 .table>thead:first-child>tr:first-child>td {
				    border-top: 0;
				    width: 6%;
				}
			</style>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp//manager/borrow/hjhcredit/hjhcredittender/hjhcredittender.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
