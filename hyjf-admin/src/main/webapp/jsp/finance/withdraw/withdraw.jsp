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
<c:set var="searchAction" value="" scope="request"></c:set>


<shiro:hasPermission name="withdrawlist:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="提现管理" />

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">提现管理</h1>
		<span class="mainDescription">本功能可以查询提现明细和执行提现操作。</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<hyjf:message key="withdraw-error"></hyjf:message>
		<hyjf:message key="withdraw-success"></hyjf:message>
		<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb" value="${withdrawForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb" value="${withdrawForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a
								class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新<i class="fa fa-refresh"></i> </a>
							<shiro:hasPermission name="withdrawlist:EXPORT">
								<shiro:hasPermission name="withdrawlist:ORGANIZATION_VIEW">
									<a class="btn btn-o btn-primary btn-sm fn-EnhanceExport"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i> </a>
								</shiro:hasPermission>
								<shiro:lacksPermission name="withdrawlist:ORGANIZATION_VIEW">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i> </a>
								</shiro:lacksPermission>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 提现列表一览 --%>
						<table id="withdrawList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">用户名</th>
									<th class="center">电子账号</th>
									<th class="center">资金托管平台</th>
									<th class="center">流水号</th>
									<th class="center">订单号</th>
									<!-- <th class="center">用户属性</th> -->
									<th class="center">提现金额</th>
									<th class="center">手续费</th>
									<th class="center">到账金额</th>
									<th class="center">实际出账金额</th>
									<th class="center">提现银行</th>
									<th class="center">提现方式</th>
									<th class="center">提交时间</th>
									<th class="center">提现平台</th>
									<th class="center">处理状态</th>
									<th class="center">失败原因</th>
									<th class="center">操作</th>
									<th class="center">发送日期</th>
									<th class="center">发送时间</th>
									<th class="center">系统跟踪号</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty withdrawForm.recordList}">
										<tr>
											<td colspan="21">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${withdrawForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((withdrawForm.paginatorPage - 1) * withdrawForm.paginator.limit) + 1 }"></c:out></td>
												<td><c:out value="${record.username }"></c:out></td>
												<td><c:out value="${record.accountId }"></c:out></td>
												<c:if test="${not empty record.bankFlag and record.bankFlag eq 1}">
												<td>江西银行</td>
												</c:if>
												<c:if test="${empty record.bankFlag or record.bankFlag eq 0}">
												<td>汇付天下</td>
												</c:if>
												<td><c:out value="${record.bankSeqNo }"></c:out></td>
												<td class="center"><c:out value="${record.ordid }"></c:out></td>
												<%-- <td class="center"><c:out value="${record.userProperty }"></c:out></td> --%>
												<td class="text-right"><fmt:formatNumber value="${record.total}" type="number" pattern="#,##0.00#" /> </td>
												<td class="text-right"><fmt:formatNumber value="${record.fee}" type="number" pattern="#,##0.00#" /> </td>
												<td class="text-right"><fmt:formatNumber value="${record.credited}" type="number" pattern="#,##0.00#" /> </td>
												<td class="text-right"><fmt:formatNumber value="${record.total}" type="number" pattern="#,##0.00#" /> </td>
												<td><c:out value="${record.bank }"></c:out></td>
												<c:if test="${not empty record.withdrawType and record.withdrawType eq 1}">
												<td>代提现</td>
												</c:if>
												<c:if test="${empty record.withdrawType or record.withdrawType eq 0}">
												<td>主动提现</td>
												</c:if>
												<td class="center"><c:out value="${record.addtimeStr }"></c:out></td>
												<td class="center"><c:out value="${record.clientStr }"></c:out></td>
												<td class="center"><span class="${record.status eq 3 ? 'text-red' : (record.status eq 2 ? 'text-success' : 'text-primary') }"><c:out value="${record.statusStr }"></c:out></span></td>
												<td class="center"><c:out value="${record.reason }"></c:out></td>
												<td class="center">
														<shiro:hasPermission name="withdrawlist:CONFIRM">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<c:if test="${record.status eq '1'}">
																	<a class="btn btn-transparent btn-xs tooltips fn-fix" data-userId="${record.userId }" data-nid="${record.ordid }" data-status="0" data-toggle="tooltip" tooltip-placement="top" data-original-title="提现失败"><i class="fa fa-frown-o"></i></a>
																	<a class="btn btn-transparent btn-xs tooltips fn-fix" data-userId="${record.userId }" data-nid="${record.ordid }" data-status="1" data-toggle="tooltip" tooltip-placement="top" data-original-title="提现成功"><i class="fa fa-smile-o"></i></a>
																</c:if>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group" dropdown="">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<c:if test="${record.status  eq '1'}">
																			<li><a class="fn-fix" data-nid="${record.ordid }" data-status="0" data-userId="${record.userId }">提现失败</a></li>
																			<li><a class="fn-fix" data-nid="${record.ordid }" data-status="1" data-userId="${record.userId }">提现成功</a></li>
																		</c:if>
																	</ul>
																</div>
															</div>
														</shiro:hasPermission>
													</td>
													<td class="center"><c:out value="${record.txDateS  }"></c:out></td>
													<td class="center"><c:out value="${record.txTimeS  }"></c:out></td>
													<td class="center"><c:out value="${record.seqNo  }"></c:out></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${withdrawForm.paginator}"></hyjf:paginator>
						<br />
						<br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="userId" id="userId" />
		<input type="hidden" name="nid" id="nid" />
		<input type="hidden" name="status" id="status" />
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${withdrawForm.paginatorPage}" />
		<!-- 查询条件 -->
		<div class="form-group">
			<label>用户名</label> <input type="text" name="usernameSrch" class="form-control input-sm underline" value="${withdrawForm.usernameSrch}" />
		</div>
		<div class="form-group">
			<label>订单号</label> <input type="text" name="ordidSrch" class="form-control input-sm underline" value="${withdrawForm.ordidSrch}" />
		</div>
<%-- 		<div class="form-group">
					<label>用户属性</label>
					<select name="userProperty" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${userPropertys }" var="property" begin="0" step="1" >
					<option value="${property.nameCd }"
						<c:if test="${property.nameCd eq withdrawForm.userProperty}">selected="selected"</c:if>>
						<c:out value="${property.name }"></c:out></option>
				</c:forEach>
			</select>
		</div> --%>
		<div class="form-group">
			<label>资金托管平台</label>
			<select name="bankFlagSrch" class="form-control underline form-select2">
				<option value="">全部</option>
				<option value="0" <c:if test="${withdrawForm.bankFlagSrch eq '0'}">selected="selected"</c:if>> 汇付天下</option>
				<option value="1" <c:if test="${withdrawForm.bankFlagSrch eq '1'}">selected="selected"</c:if>> 江西银行</option>
			</select>
		</div>
		<div class="form-group">
			<label>电子账号</label> <input type="text" name="accountIdSrch" class="form-control input-sm underline" value="${withdrawForm.accountIdSrch}" />
		</div>
		<div class="form-group">
			<label>提现平台</label>
			<select name="clientSrch" class="form-control underline form-select2">
				<option value=""></option>
				<c:forEach items="${clientlist }" var="client" begin="0" step="1" varStatus="status">
					<option value="${client.nameCd }" <c:if test="${client.nameCd eq withdrawForm.clientSrch}">selected="selected"</c:if>> <c:out value="${client.name }"></c:out></option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group">
			<label>提现状态</label>
			<select name="statusSrch" class="form-control underline form-select2">
				<option value=""></option>
				<c:forEach items="${statuslist }" var="status" begin="0" step="1" varStatus="s">
					<option value="${status.nameCd }" <c:if test="${status.nameCd eq withdrawForm.statusSrch}">selected="selected"</c:if>> <c:out value="${status.name }"></c:out></option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group">
			<label>提交时间</label>
			<div class="input-group input-daterange datepicker">
				<span class="input-icon">
				<input type="text" name="addtimeStartSrch" id="start-date-time" class="form-control underline" value="${withdrawForm.addtimeStartSrch}" />
				<i class="ti-calendar"></i> </span>
				<span class="input-group-addon no-border bg-light-orange">~</span>
				<span class="input-icon">
				<input type="text" name="addtimeEndSrch" id="end-date-time" class="form-control underline" value="${withdrawForm.addtimeEndSrch}" />
				<i class="ti-calendar"></i> </span>
			</div>
		</div>
		<div class="form-group">
			<label>流水号</label> <input type="text" name="bankSeqNoSrch" class="form-control input-sm underline" value="${withdrawForm.bankSeqNoSrch}" />
		</div>
		<div class="form-group">
			<label>提现方式</label>
			<select name="withdrawType" class="form-control underline form-select2">
				<option value=""></option>
				<option value="0" <c:if test="${'0' eq withdrawForm.withdrawType}">selected="selected"</c:if>> 主动提现</option>
				<option value="1" <c:if test="${'1' eq withdrawForm.withdrawType}">selected="selected"</c:if>> 代提现</option>
			</select>
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/finance/withdraw/withdraw.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
