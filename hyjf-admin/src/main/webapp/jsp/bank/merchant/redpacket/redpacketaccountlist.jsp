<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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


<shiro:hasPermission name="bankredpacketaccount:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="红包账户明细" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">红包账户明细</h1>
			<span class="mainDescription">红包账户明细</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="bankredpacketaccount:VIEW">
			      		<li><a href="${webRoot}/bank/merchant/account/init">账户信息</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="bankredpacketaccount:VIEW">
			      		<li class="active"><a href="${webRoot}/bank/merchant/redpacket/init">红包账户明细</a></li>
			      	</shiro:hasPermission>
			      	<shiro:hasPermission name="web:VIEW">
			      		<li><a href="${webRoot}/bank/merchant/poundage/init">手续费账户明细</a></li>
			      	</shiro:hasPermission>
			    </ul>
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<shiro:hasPermission name="bankredpacketaccount:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${form.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<%-- <shiro:hasPermission name="bankredpacketaccount:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission> --%>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">交易日期</th>
										<th class="center">流水号</th>
										<th class="center">订单号</th>
										<th class="center">分公司</th>
										<th class="center">分部</th>
										<th class="center">团队</th>
										<th class="center">用户名</th>
										<th class="center">电子帐号</th>
										<th class="center">收支类型</th>
										<th class="center">交易类型</th>
										<th class="center">交易金额</th>
										<th class="center">交易状态</th>
										<th class="center">红包可用金额</th>
										<th class="center">红包冻结金额</th>
										<th class="center">备注</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty form.recordList}">
											<tr><td colspan="16">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${form.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(form.paginatorPage-1)*form.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.createTime }"></c:out></td>
													<td class="center"><c:out value="${record.seqNo }"></c:out></td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.regionName }"></c:out></td>
													<td class="center"><c:out value="${record.branchName }"></c:out></td>
													<td class="center"><c:out value="${record.departmentName }"></c:out></td>
													<td class="center">
														<c:choose>
															<c:when test="${empty record.userName}">admin</c:when>
															<c:otherwise><c:out value="${record.userName }"></c:out></c:otherwise>
														</c:choose>
													</td>
													<td class="center"><c:out value="${record.accountId }"></c:out></td>
													<td class="center">
														<c:forEach items="${bankMerType }" var="type" begin="0" step="1">
																<c:if test="${type.nameCd eq record.type}"> <c:out value="${type.name }"></c:out> </c:if>
														</c:forEach>
													</td>
													<td class="center">
														<c:forEach items="${transTypes }" var="transType" begin="0" step="1">
																<c:if test="${transType.nameCd eq record.transType}"> <c:out value="${transType.name }"></c:out> </c:if>
														</c:forEach>
													</td>
													<td align="right"><fmt:formatNumber value="${record.amount}" type="number" pattern="#,##0.00" /></td>
													<td class="center">
														<c:forEach items="${transStatus }" var="transStatu" begin="0" step="1">
																<c:if test="${transStatu.nameCd eq record.status}"> <c:out value="${transStatu.name }"></c:out> </c:if>
														</c:forEach>
													</td>
													<td align="right"><fmt:formatNumber value="${record.bankAccountBalance}" type="number" pattern="#,##0.00" /></td>
													<td align="right"><fmt:formatNumber value="${record.bankAccountFrost}" type="number" pattern="#,##0.00" /></td>
													<td class="center"><c:out value="${record.remark }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${form.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="bankredpacketaccount:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>流水号：</label>
					<input type="text" name="seqNo" class="form-control input-sm underline"  value="${form.seqNo}"/>
				</div>
				<div class="form-group">
					<label>订单号：</label>
					<input type="text" name="orderId" class="form-control input-sm underline"  value="${form.orderId}"/>
				</div>
				<div class="form-group">
					<label>电子帐号：</label>
					<input type="text" name="accountId" class="form-control input-sm underline"  value="${form.accountId}"/>
				</div>
				
				<div class="form-group">
					<label>收支类型:</label>
					<select name="type" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${bankMerType }" var="type" begin="0" step="1">
							<option value="${type.nameCd }"
								<c:if test="${type.nameCd eq form.type}">selected="selected"</c:if>>
								<c:out value="${type.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>	
				<div class="form-group">
					<label>交易类型：</label>
					<select name="transType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${transTypes }" var="type" begin="0" step="1">
							<option value="${type.nameCd }"
								<c:if test="${type.nameCd eq form.transType}">selected="selected"</c:if>>
								<c:out value="${type.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>	
				<div class="form-group">
					<label>日期：</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="transferTimeStart" id="start-date-time" class="form-control underline" value="${form.timeStartSrch}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="transferTimeEnd" id="end-date-time" class="form-control underline" value="${form.timeEndSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>交易状态：</label>
					<select name="status" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${transStatus }" var="status" begin="0" step="1">
							<option value="${status.nameCd }"
								<c:if test="${status.nameCd eq form.status}">selected="selected"</c:if>>
								<c:out value="${status.name }"></c:out>
							</option>
						</c:forEach>
					</select>
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

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/zeroclipboard/ZeroClipboard.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/bank/merchant/redpacket/redpacketaccountlist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
