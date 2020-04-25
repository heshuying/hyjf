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


<shiro:hasPermission name="bankjournal:VIEW">
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="银行账务明细" />

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">银行账务明细</h1>
		<span class="mainDescription">银行账务明细。</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="row">
				<div class="col-md-12">
					<div class="search-classic">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb"
								value="${bankAleveForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb"
								value="${bankAleveForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a
								class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a
								class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm fn-Refresh"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i> </a>
							<shiro:hasPermission name="bankaleve:EXPORT">
								<a class="btn btn-o btn-primary btn-sm fn-Export"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="导出Excel">
									导出Excel <i class="fa fa-Export"></i></a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
						</div>
						<br />
						<%-- 角色列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">电子账号</th>
									<th class="center">交易金额</th>
									<th class="center">交易金额符号</th>
									<th class="center">入帐日期</th>
									<th class="center">交易日期</th>
									<th class="center">自然日期</th>
									<th class="center">交易时间</th>
									<th class="center">交易流水号</th>
									<th class="center">关联交易流水号</th>
									<th class="center">交易类型</th>
									<th class="center">交易描述</th>
									<th class="center">交易后余额</th>
									<th class="center">对手交易帐号</th>
									<th class="center">冲正撤销标志</th>
									<th class="center">交易标识</th>
									<th class="center">系统跟踪号</th>
									<th class="center">原交易流水号</th>
									<th class="center">保留域</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty bankAleveForm.recordList}">
										<tr>
											<td colspan="19">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${bankAleveForm.recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center"><c:out value="${status.index+((bankAleveForm.paginatorPage - 1) * bankAleveForm.paginator.limit) + 1 }"></c:out></td>
												<td class="center"><c:out value="${record.cardnbr }"></c:out></td>
												<td class="center"><fmt:formatNumber value="${record.amount}" type="number" pattern="#,##0.00#"/></td>
												<td align="center"><c:out value="${record.crflag}"></c:out></td>
												<td align="center"><c:out value="${record.valdate}"></c:out></td>
												<td align="center"><c:out value="${record.inpdate}"></c:out></td>
												<td align="center"><c:out value="${record.reldate}"></c:out></td>
												<td align="center"><c:out value="${record.inptime}"></c:out></td>

												<td align="center"><c:out value="${record.tranno}"></c:out></td>
												<td class="center"><c:out value="${record.oriTranno }"></c:out></td>
												<td class="center"><c:out value="${record.transtype }"></c:out></td>
												<td class="center"><c:out value="${record.desline }"></c:out></td>
												<td class="center"><fmt:formatNumber value="${record.currBal}" type="number" pattern="#,##0.00#"/></td>

												<td class="center"><c:out value="${record.forcardnbr }"></c:out></td>
												<td class="center"><c:out value="${record.revind == 1? '已撤销/冲正':'' }"></c:out></td>
												<td class="center"><c:out value="${record.accchg == 1?'调账':''}"></c:out></td>
												<td class="center"><c:out value="${record.seqno }"></c:out></td>
												<td class="center"><c:out value="${record.oriNum }"></c:out></td>
												<td class="center"><c:out value="${record.resv }"></c:out></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<hyjf:paginator id="equiPaginator" hidden="paginator-page"
							action="bankaleve_list"
							paginator="${bankAleveForm.paginator}"></hyjf:paginator>
						<br /> <br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="userId" id="userId" />
		<input type="hidden" name="paginatorPage" id="paginator-page" value="${bankAleveForm.paginatorPage}" />
		<!-- 查询条件 -->
		<div class="form-group">
			<label>电子账号</label> <input type="text" name="cardnbr" id="cardnbr"
				class="form-control input-sm underline"
				value="${bankAleveForm.cardnbr}" />
		</div>
		<div class="form-group">
			<label>系统跟踪号</label> <input type="text" name="seqno" id="seqno"
									   class="form-control input-sm underline"
									   value="${bankAleveForm.seqno}" />
		</div>
		<div class="form-group">
			<label>交易类型</label> <input type="text" name="transtype" id="transtype"
									   class="form-control input-sm underline"
									   value="${bankAleveForm.transtype}" />
		</div>
		<div class="form-group">
			<label>入账日期</label>
			<div class="input-group input-daterange datepicker">
					<span class="input-icon">
					<input type="text" name="startValdate" id="start-valdate-time" class="form-control underline" value="${bankAleveForm.startValdate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
				<input type="text" name="endValdate" id="end-valdate-time" class="form-control underline" value="${bankAleveForm.endValdate}" />
			</div>
		</div>
		<div class="form-group">
			<label>交易日期</label>
			<div class="input-group input-daterange datepicker">
					<span class="input-icon">
					<input type="text" name="startInpdate" id="start-inpdate-time" class="form-control underline" value="${bankAleveForm.startInpdate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
				<input type="text" name="endInpdate" id="end-inpdate-time" class="form-control underline" value="${bankAleveForm.endInpdate}" />
			</div>
		</div>
		<div class="form-group">
			<label>自然日期</label>
			<div class="input-group input-daterange datepicker">
					<span class="input-icon">
					<input type="text" name="startReldate" id="start-reldate-time" class="form-control underline" value="${bankAleveForm.startReldate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
				<input type="text" name="endReldate" id="end-reldate-time" class="form-control underline" value="${bankAleveForm.endReldate}" />
			</div>
		</div>
	</tiles:putAttribute>

	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/finance/bankaleve/bankaleve_list.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
</shiro:hasPermission>
