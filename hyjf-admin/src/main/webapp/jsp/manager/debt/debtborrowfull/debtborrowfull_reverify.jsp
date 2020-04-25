<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<%-- 画面功能路径 (ignore) --%>
<shiro:hasPermission name="debtborrowfull:BORROW_FULL">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="复审" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">复审</h1>
			<span class="mainDescription">复审复审复审复审复审的说明。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="invoice">
							<div class="row">
								<div class="col-sm-3">
									<ul
										class="list-unstyled invoice-details padding-top-10 text-dark height-30">
										<li><strong>项目编号:</strong> <c:out
												value="${ reverifyInfo.borrowNid}"></c:out></li>
										<li><strong>借款金额:</strong> <c:out
												value="${ reverifyInfo.account}"></c:out>元</li>
										<li><strong>还款方式:</strong> <c:out
												value="${ reverifyInfo.borrowStyleName }"></c:out></li>
										<li><strong>借到金额:</strong> <c:out
												value="${ reverifyInfo.borrowAccountYes}"></c:out>元</li>
										<li><strong>发标时间:</strong> <c:out
												value="${ reverifyInfo.ontime}"></c:out></li>
									</ul>
								</div>
								<div class="col-sm-3">
									<ul
										class="list-unstyled invoice-details padding-bottom-30 padding-top-10 text-dark height-30">
										<li><strong>项目名称:</strong> <c:out
												value="${ reverifyInfo.borrowName}"></c:out></li>
										<li><strong>借款期限:</strong> <c:out
												value="${ reverifyInfo.borrowPeriod }" /></li>
										<li><strong>有效时间:</strong> <c:out
												value="${ reverifyInfo.borrowValidTime}"></c:out>天</li>												
										<li><strong>出借利率:</strong> <c:out
												value="${ reverifyInfo.borrowApr}" /></li>
										<li><strong>满标时间:</strong> <c:out
												value="${ reverifyInfo.overTime}" /></li>
									</ul>
								</div>
								<div class="col-sm-3">
									<ul
										class="list-unstyled invoice-details padding-bottom-30 padding-top-10 text-dark height-30">
										<li><strong>借款用户:</strong> <c:out
												value="${ reverifyInfo.username}" /></li>
										<li><strong>放款服务费率:</strong> <c:out
												value="${ reverifyInfo.serviceScale}" /></li>
										<li><strong>还款服务费率<c:if test="${ reverifyInfo.projectType eq '8'}">（上限）</c:if>:</strong> <c:out
												value="${ reverifyInfo.managerScale}" /></li>
										<c:if test="${ reverifyInfo.projectType eq '8'}">
											<li><strong>还款服务费率（下限）:</strong> <c:out
												value="${ reverifyInfo.managerScaleEnd}" /></li>
										</c:if>
									</ul>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<table id="equiList" class="table table-striped table-bordered table-hover">
										<colgroup>
											<col style="width:;" />
											<col style="width:;" />
											<col style="width:;" />
											<col style="width:;" />
											<col style="width:;" />
											<col style="width:;" />
											<col style="width:;" />
										</colgroup>
										<thead>
											<tr>
												<th class="center">序号</th>
												<th class="center">出借人</th>
												<th class="center">授权服务金额（元）</th>
												<th class="center">应放款金额</th>
												<th class="center">应收服务费</th>
												<th class="center">操作平台</th>
												<th class="center">操作时间</th>
											</tr>
										</thead>
										<tbody id="roleTbody">
											<c:choose>
												<c:when test="${empty fullList}">
													<tr>
														<td colspan="10">暂时没有数据记录</td>
													</tr>
												</c:when>
												<c:otherwise>
													<c:forEach items="${fullList }" var="record" begin="0"
														step="1" varStatus="status">
														<tr>
															<td>${(debtborrowFullForm.paginatorPage -1 ) * debtborrowFullForm.paginator.limit + status.index + 1 }</td>
															<td><c:out value="${record.username }" /></td>
															<td align="right"><c:out value="${record.investmentAmount }" /></td>
															<td align="right"><c:out value="${record.loanAmount }" /></td>
															<td align="right"><c:out value="${record.serviceCharge }" /></td>
															<td><c:out value="${record.operatingDeck }" /></td>
															<td class="center"><c:out value="${record.operatingTime }" /></td>
														</tr>
													</c:forEach>
													<tr>
														<td>总计</td>
														<td></td>
														<td align="right"><c:out value="${sumAmount.investmentAmount }" /></td>
														<td align="right"><c:out value="${sumAmount.loanAmount }" /></td>
														<td align="right"><c:out value="${sumAmount.serviceCharge }" /></td>
														<td></td>
														<td></td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
									<%-- 分页栏 --%>
									<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="fullInfoAction" paginator="${debtborrowFullForm.paginator}"></hyjf:paginator>
									<br /> <br />
								</div>
							</div>
						</div>
						<div class="col-lg-12 col-md-12">
							<div class="panel panel-white">
								<div class="panel-body">
									<form id="mainForm" method="post" role="form" class="form-horizontal">
										<input type="hidden" name=borrowNid id="borrowNid" value="${debtborrowFullForm.borrowNid}" /> 
										<input type="hidden" name="paginatorPage" id="paginator-page" value="${debtborrowFullForm.paginatorPage}" />
										<%-- 检索条件保持 --%>
										<!-- 项目编号 -->
										<input type="hidden" name="borrowNidSrch"
											value="${debtborrowFullForm.borrowNidSrch}" />
										<!-- 项目名称 -->
										<input type="hidden" name="borrowNameSrch"
											value="${debtborrowFullForm.borrowNameSrch}" />
										<!-- 借  款 人 -->
										<input type="hidden" name="usernameSrch"
											value="${debtborrowFullForm.usernameSrch}" />
										<!-- 满标时间 -->
										<input type="hidden" name="timeStartSrch" value="${debtborrowFullForm.timeStartSrch}" /> 
										<input type="hidden" name="timeEndSrch" value="${debtborrowFullForm.timeEndSrch}" />

										<div class="form-group">
											<!-- 该数据已经被复审通过。 -->
											<hyjf:validmessage key="statusError"></hyjf:validmessage>
											<label class="col-sm-2 control-label" for="inputPassword3">
												<span class="symbol required" aria-required="true"></span>复审备注
											</label>
											<div class="col-sm-6">
												<textarea maxlength="255" name="reverifyRemark" class="form-control limited" rows="5" datatype="*1-255" ignore="ignore" errormsg="审核备注 长度1~255个字符！"><c:out value="${reverifyRemark }" /></textarea>
												<hyjf:validmessage key="reverifyRemark" label="审核备注"></hyjf:validmessage>
											</div>
										</div>
										<div class="form-group margin-bottom-0">
											<div class="col-sm-offset-2 col-sm-10">
												<button class="btn btn-o btn-primary fn-Full" type="button">
													复审通过并放款</button>
												<button class="btn btn-o btn-primary fn-Back" type="button">
													返回列表</button>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<style>
.table-striped .checkbox {
	width: 20px;
	margin-right: 0 !important;
	overflow: hidden
}

.height-30 li {
	height: 30px;
}
</style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<!-- Form表单插件 -->
			<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript'
				src="${webRoot}/jsp/manager/debt/debtborrowfull/debtborrowfull_reverify.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
