<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="borrowRepayMentInfo:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="还款明细" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">还款明细</h1>
			<span class="mainDescription">以出借的角度查看还款信息,查询的是每个项目下每一个出借人每一笔的还款信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<hyjf:message key="delete_error"></hyjf:message>
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${borrowRepaymentInfoForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${borrowRepaymentInfoForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="borrowRepayMentInfo:EXPORT">
									<shiro:hasPermission name="borrowRepayMentInfo:ORGANIZATION_VIEW">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-EnhanceExport"
										   data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<shiro:lacksPermission name="borrowRepayMentInfo:ORGANIZATION_VIEW">
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
							<br />
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">项目编号</th>
										<%-- add by LSY START --%>
										<th class="center">资产来源</th>
										<%-- add by LSY END --%>
										<th class="center">智投编号</th>
										<th class="center">还款批次号</th>
										<!-- <th class="center">担保机构</th> -->
										<th class="center">借款人用户名</th>
										<!-- <th class="center">项目名称</th> -->
										<th class="center">出借订单号</th>
										<th class="center">还款订单号</th>
										<th class="center">还款冻结订单号</th>
										<th class="center">出借人用户名</th>
										<th class="center">应回本金</th>
										<th class="center">应回利息</th>
										<th class="center">应回本息</th>
										<%-- upd by LSY START --%>
										<%-- <th class="center">管理费</th> --%>
										<th class="center">还款服务费</th>
										<%-- upd by LSY END --%>
										<th class="center">回款状态</th>
										<th class="center">到期日</th>
										<th class="center">实际回款时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<%-- UPD BY LSY START --%>
												<%-- <td colspan="17">暂时没有数据记录</td> --%>
												<td colspan="18">暂时没有数据记录</td>
												<%-- UPD BY LSY END --%>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0"
												step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((borrowRepaymentInfoForm.paginatorPage - 1) * borrowRepaymentInfoForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<%-- add by LSY START --%>
													<td class="center"><c:out value="${record.instName }"></c:out></td>
													<%-- add by LSY END --%>
													<td class="center"><c:out value="${record.planNid }"></c:out></td>
													<td class="center"><c:out value="${record.repayBatchNo }"></c:out></td>
													<%-- <td class="center"><c:out value="${record.repayOrgName }"></c:out></td> --%>
													<td><c:out value="${record.borrowUserName }"></c:out></td>
													<%-- <td><c:out value="${record.borrowName }"></c:out></td> --%>
													<td class="center"><c:out value="${record.nid }"></c:out></td>
													<td class="center"><c:out value="${record.repayOrdid }"></c:out></td>
													<td class="center"><c:out value="${record.freezeOrderId }"></c:out></td>
													<td><c:out value="${record.recoverUserName }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.recoverCapital }" pattern="#,##0.00#"/></td>
													<td align="right"><fmt:formatNumber value="${record.recoverInterest }" pattern="#,##0.00#"/></td>
													<td align="right"><fmt:formatNumber value="${record.recoverAccount }" pattern="#,##0.00#"/></td>
													<td align="right"><fmt:formatNumber value="${record.recoverFee }" pattern="#,##0.00#"/></td>
													<td class="center"><c:if test="${record.status==0 }">
													还款中
													</c:if> <c:if test="${record.status==1 }">
													已还款
													</c:if></td>
													<td class="center"><c:out value="${record.recoverLastTime }"></c:out></td>
													<td class="center"><c:out value="${record.repayActionTime }"></c:out></td>
													<td class="center">
																<c:if test="${record.borrowStyle ne 'end' and record.borrowStyle ne 'endday' }">
 														<shiro:hasPermission name="borrowRepayMentInfo:INFO">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																		<a
																			class="btn btn-transparent btn-xs fn-toHuankuanMingxi"
																			data-id="${record.nid }" data-toggle="tooltip"
																			data-placement="top" data-original-title="详情"><i
																			class="fa fa-pencil fa-white"></i></a>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<a
																			class="btn btn-transparent btn-xs fn-toHuankuanMingxi"
																			data-id="${record.nid }" data-toggle="tooltip"
																			data-placement="top" data-original-title="详情"><i
																			class="fa fa-pencil fa-white"></i></a>
																	</ul>
																</div>
															</div>
 														</shiro:hasPermission>
														</c:if>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
									<tr>
										<th class="center">总计</th>
										<th>&nbsp;</th>
										<%-- add by LSY START --%>
										<th>&nbsp;</th>
										<%-- add by LSY END --%>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<td align="right"><fmt:formatNumber value="${sumObject.recoverCapital }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.recoverInterest }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.recoverAccount }" pattern="#,##0.00#"/></td>
										<td align="right"><fmt:formatNumber value="${sumObject.recoverFee }" pattern="#,##0.00#"/></td>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="searchAction" paginator="${borrowRepaymentInfoForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="nid" id="nid"
				class="form-control input-sm underline"
				value="${borrowRepaymentInfoForm.nid}" />
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${borrowRepaymentInfoForm.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>项目编号</label> <input type="text" name="borrowNid"
					id="borrowNid" class="form-control input-sm underline"
					value="${borrowRepaymentInfoForm.borrowNid}" />
			</div>
			<%-- add by LSY START --%>
			<div class="form-group">
				<label>资产来源</label>
				<select name="instCodeSrch" id="instCodeSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
						<option value="${inst.instCode }"
							<c:if test="${inst.instCode eq borrowRepaymentInfoForm.instCodeSrch}">selected="selected"</c:if>>
							<c:out value="${inst.instName }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<%-- add by LSY END --%>
			<div class="form-group">
				<label>智投编号</label>
				<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" 
				value="${borrowRepaymentInfoForm.planNidSrch}" />
			</div>
			<div class="form-group">
				<label>还款冻结订单号</label>
				<input type="text" name="freezeOrderId" id="freezeOrderId" class="form-control input-sm underline"
					   value="${borrowRepaymentInfoForm.freezeOrderId}" />
			</div>
			<div class="form-group">
				<label>批次号</label> <input type="text" name="repayBatchNo"
					id="repayBatchNo" class="form-control input-sm underline"
					value="${borrowRepaymentInfoForm.repayBatchNo}" />
			</div>
<%-- 			<div class="form-group">
				<label>项目名称</label> <input type="text" name="borrowName"
					class="form-control input-sm underline"
					value="${borrowRepaymentInfoForm.borrowName}" />
			</div> --%>
			<div class="form-group">
				<label>备注</label> <input type="text" name="borrowUserName"
					class="form-control input-sm underline"
					value="${borrowRepaymentInfoForm.borrowUserName}" />
			</div>
			<div class="form-group">
				<label>出借人</label> <input type="text" name="recoverUserName"
					class="form-control input-sm underline"
					value="${borrowRepaymentInfoForm.recoverUserName}" />
			</div>
			<div class="form-group">
				<label>还款状态</label> <select name="status"
					class="form-control underline form-select2">
					<option value="">全部</option>
					<option value="0"
						<c:if test="${borrowRepaymentInfoForm.status != '' && borrowRepaymentInfoForm.status==0}">selected="selected"</c:if>>还款中</option>
					<option value="1"
						<c:if test="${borrowRepaymentInfoForm.status != '' && borrowRepaymentInfoForm.status==1}">selected="selected"</c:if>>已还款</option>

				</select>
			</div>
			<div class="form-group">
				<label>应回日期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="recoverTimeStartSrch" id="recoverTimeStartSrch"
						class="form-control underline"
						value="${borrowRepaymentInfoForm.recoverTimeStartSrch}" /> <i
						class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="recoverTimeEndSrch"
						id="recoverTimeEndSrch" class="form-control underline"
						value="${borrowRepaymentInfoForm.recoverTimeEndSrch}" />
				</div>
			</div>
			<div class="form-group">  
				<label>实际回款日期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="yesTimeStartSrch" id="yesTimeStartSrch"
						class="form-control underline"
						value="${borrowRepaymentInfoForm.yesTimeStartSrch}" /> <i
						class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="yesTimeEndSrch"
						id="yesTimeEndSrch" class="form-control underline"
						value="${borrowRepaymentInfoForm.yesTimeEndSrch}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowrepaymentinfo/borrowrepaymentinfo.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>

