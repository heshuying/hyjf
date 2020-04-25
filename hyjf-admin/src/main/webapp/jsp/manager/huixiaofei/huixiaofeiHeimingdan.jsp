<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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


<shiro:hasPermission name="huixiaofei:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇消费-黑名单" />
		
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇消费-黑名单</h1>
			<span class="mainDescription">本功能查询汇消费-黑名单。</span>
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
									value="${huixiaofeiForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb"
									value="${huixiaofeiForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
										<shiro:hasPermission name="huixiaofei:EXPORT">
								<a class="btn btn-o btn-primary btn-sm fn-Export"
										data-toggle="tooltip" data-placement="bottom" data-original-title="导出表格">导出表格 <i class="fa fa-plus"></i></a>
										</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br/>
							<%-- 公司环境列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">合同号</th>
										<th class="center">编号</th>
										<th class="center">姓名</th>
										<th class="center">身份证号</th>
										<th class="center">性别</th>
										<th class="center">卡号</th>
										<th class="center">贷款金额</th>
										<th class="center">贷款期限</th>
										<th class="center">联系方式</th>
										<th class="center">状态</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty huixiaofeiListList}">
											<tr>
												<td colspan="10">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${huixiaofeiListList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(huixiaofeiForm.paginatorPage - 1 ) * huixiaofeiForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.contractNo }"></c:out></td>
													<td class="center"><c:out value="${record.consumeId }"></c:out></td>
													<td><c:out
															value="${record.personName }"></c:out></td>
													<td class="center"><c:out value="${record.ident }"></c:out></td>
													<td class="center"><c:if test="${record.sex=='m' }">男</c:if>
														<c:if test="${record.sex=='f' }">女</c:if></td>
													<td class="center"><c:out value="${record.accountNo }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.creditAmount }" pattern="#,##0.00#" /></td>
													<td align="right"><c:out
															value="${record.instalmentNum }"></c:out></td>
													<td><c:out value="${record.mobile }"></c:out></td>
													<td class="center">
														<c:if test="${record.status==0}">
															<font color="green">通过</font>
														</c:if>
														<c:if test="${record.status==1}">
															<font color="red">未通过</font>
														</c:if>
														<c:if test="${record.status==2}">未审核</c:if>
														<c:if test="${record.status==3}">已处理</c:if>
													</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="toheimingdanAction" paginator="${huixiaofeiForm.paginator}"></hyjf:paginator>
							<br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 查询面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" value="${huixiaofeiForm.id }" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${huixiaofeiForm.paginatorPage}" />
			<input type="hidden" name="status" id="status" value="3" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>包编号</label>
				<input type="text" name="consumeId" id="consumeId" class="form-control input-sm underline" value="${huixiaofeiForm.consumeId }" />
			</div>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/huixiaofei/huixiaofeiHeimingdan.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>