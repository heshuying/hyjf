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
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="accountdetail:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="资金明细" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">资金明细</h1>
			<span class="mainDescription">这里添加资金明细描述。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${accountdetailForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${accountdetailForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
								<shiro:hasPermission name="accountdetail:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
										data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i> </a>
								</shiro:hasPermission>
								<shiro:hasPermission name="accountdetail:DATA_REPAIR">
								<a class="btn btn-o btn-primary btn-sm fn-Repay-Data-Repair" data-toggle="tooltip" data-placement="bottom" data-original-title="还款交易明细修复">
									还款交易明细修复 <i class="fa fn-Repay-Data-Repair"></i></a>
								</shiro:hasPermission>		
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">电子账号</th>
										<th class="hidden-xs">推荐人</th>
										<th class="center">资金托管平台</th>
										<th class="center">流水号</th>
										<th class="center">订单号</th>
										<th class="center">收支类型</th>
										<th class="center">交易类型</th>
										<th class="center">操作金额</th>
										<th class="center">银行总资产</th>
										<th class="center">银行可用金额</th>
										<th class="center">银行冻结金额</th>
										<th class="center">汇付可用金额</th>
										<th class="center">汇付冻结金额</th>
										<th class="center">智投服务可用余额</th>
										<th class="center">智投服务冻结金额</th>
										<th class="center">交易状态</th>
										<th class="center">对账状态</th>
										<th class="center">综合信息</th>
										<th class="center">时间</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty accountdetailForm.recordList}">
											<tr><td colspan="21">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${accountdetailForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((accountdetailForm.paginatorPage - 1) * accountdetailForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td><c:out value="${record.accountId }"></c:out></td>
													<td><c:out value="${record.referrerName }"></c:out></td>
<%-- 													<td><c:out value="${record.referrerGroup }"></c:out></td> --%>
													<td class="center"><c:out value="${record.isBank == 1 ? '江西银行' : '汇付天下' }"></c:out></td>
													<td class="center"><c:out value="${record.seqNo }"></c:out></td>
													<td class="center"><c:out value="${record.nid }"></c:out></td>
													<td class="center"><c:out value="${record.type }"></c:out></td>
													<td class="center"><c:out value="${record.tradeType }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.amount}" type="number" pattern="#,##0.00#" /> </td>
													<td align="right"><fmt:formatNumber value="${record.bankTotal}" type="number" pattern="#,##0.00#" /> </td>
													<td align="right"><fmt:formatNumber value="${record.bankBalance}" type="number" pattern="#,##0.00#" /> </td>
													<td align="right"><fmt:formatNumber value="${record.bankFrost}" type="number" pattern="#,##0.00#" /> </td>
													<td align="right" class="hidden-xs"><fmt:formatNumber value="${record.balance}" type="number" pattern="#,##0.00#" /> </td>
													<td align="right"><fmt:formatNumber value="${record.frost}" type="number" pattern="#,##0.00#" /> </td>
													<td align="right"><fmt:formatNumber value="${record.planBalance}" type="number" pattern="#,##0.00#" /> </td>
													<td align="right"><fmt:formatNumber value="${record.planFrost}" type="number" pattern="#,##0.00#" /> </td>
													<td><c:out value="${record.tradeStatus == 0 ? '失败' : '成功' }"></c:out></td>
													<td><c:out value="${record.checkStatus == 0 ? '未对账' : '已对账'  }"></c:out></td>
													<td><c:out value="${record.remark  }"></c:out></td>
													<td class="center"><c:out value="${record.createTime  }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searhAction" paginator="${accountdetailForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="permissionUuid" id="permissionUuid" />
		<input type="hidden" name="paginatorPage" id="paginator-page"
			value="${accountdetailForm.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>用户名</label> <input type="text" name="username"
					class="form-control input-sm underline"
					value="${accountdetailForm.username}" />
			</div>
			<div class="form-group">
				<label>推荐人</label> <input type="text" name="referrerName"
					class="form-control input-sm underline"
					value="${accountdetailForm.referrerName}" />
			</div>
			<div class="form-group">
				<label>订单号</label> <input type="text" name="nid"
					class="form-control input-sm underline"
					value="${accountdetailForm.nid}" />
			</div>
			<div class="form-group">
				<label>电子账号</label> <input type="text" name="accountId"
					class="form-control input-sm underline"
					value="${accountdetailForm.accountId}" />
			</div>
			<div class="form-group">
				<label>流水号</label> <input type="text" name="seqNo"
					class="form-control input-sm underline"
					value="${accountdetailForm.seqNo}" />
			</div>
			<div class="form-group">
				<label>资金托管平台</label> <select name="isBank" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq accountdetailForm.isBank}">selected="selected"</c:if>>汇付天下</option>
					<option value="1" <c:if test="${'1' eq accountdetailForm.isBank}">selected="selected"</c:if>> 江西银行</option>
				</select>
			</div>
			
			<div class="form-group">
				<label>对账状态</label> <select name="checkStatus" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq accountdetailForm.checkStatus}">selected="selected"</c:if>>未对账</option>
					<option value="1" <c:if test="${'1' eq accountdetailForm.checkStatus}">selected="selected"</c:if>> 已对账</option>
				</select>
			</div>
			<div class="form-group">
				<label>交易状态</label> <select name="tradeStatus" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq accountdetailForm.tradeStatus}">selected="selected"</c:if>>失败</option>
					<option value="1" <c:if test="${'1' eq accountdetailForm.tradeStatus}">selected="selected"</c:if>> 成功</option>
				</select>
			</div>
			<div class="form-group">
				<label>收支类型</label> <select name="typeSearch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1" <c:if test="${'1' eq accountdetailForm.typeSearch}">selected="selected"</c:if>>收入</option>
					<option value="2" <c:if test="${'2' eq accountdetailForm.typeSearch}">selected="selected"</c:if>> 支出</option>
					<option value="3" <c:if test="${'3' eq accountdetailForm.typeSearch}">selected="selected"</c:if>>冻结</option>
					<option value="4" <c:if test="${'4' eq accountdetailForm.typeSearch}">selected="selected"</c:if>> 解冻</option>
				</select>
			</div>
			
			<div class="form-group">
				<label>交易类型</label> <select name="tradeTypeSearch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${accountdetailForm.tradeList }" var="tradetype" begin="0" step="1" varStatus="status">
						<option value="${tradetype.id }" <c:if test="${tradetype.id eq accountdetailForm.tradeTypeSearch}">selected="selected"</c:if>> <c:out value="${tradetype.name }"></c:out></option>
					</c:forEach> 
				</select>
			</div>
			<div class="form-group">
				<label>时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> 
					<input type="text" name="startDate" id="start-date-time" class="form-control underline" value="${accountdetailForm.startDate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="endDate" id="end-date-time" class="form-control underline" value="${accountdetailForm.endDate}" />
				</div>
			</div>
			<div class="form-group">
				<label>综合信息</label> <input type="text" id="remarkSrch" name="remarkSrch"
					class="form-control input-sm underline"
					value="${accountdetailForm.remarkSrch}" />
			</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/finance/accountdetail/accountdetail_list.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
