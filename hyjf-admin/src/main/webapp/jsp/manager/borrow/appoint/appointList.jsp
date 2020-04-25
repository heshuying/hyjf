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

<shiro:hasPermission name="appointlist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="预约记录" />
		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">预约记录</h1>
			<span class="mainDescription">本功能可以查询相应的用户的预约记录信息。</span>
		</tiles:putAttribute>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="appointlist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${appointListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${appointListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="accountlist:EXPORT">
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
			
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:80px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">预约订单号</th>
										<th class="center">出借订单号</th>
										<th class="center">项目编号</th>
										<th class="center">项目期限</th>
										<th class="center">项目金额</th>
										<th class="center">出借利率</th>
										<th class="center">预约金额</th>
										<th class="center">预约时间</th>
										<th class="center">预约状态</th>
										<th class="center">预约撤销时间</th>
										<th class="center">发标时间</th>
										<th class="center">出借状态</th>
										<th class="center">备注</th>
									</tr>
								</thead>
								<tbody id="accountTbody">
									<c:choose>
										<c:when test="${empty appointListForm.recordList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${appointListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(appointListForm.paginatorPage-1)*appointListForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.tenderNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowPeriod }"></c:out></td>
													<td align="right"><c:out value="${record.borrowAccount }"></c:out></td>
													<td align="right"><c:out value="${record.borrowApr }"></c:out>%</td>
													<td align="right"><c:out value="${record.account }"></c:out></td>
													<td class="center"><c:out value="${record.appointTime }"></c:out></td>
													<td align="center"><c:out value="${record.appointStatusInfo }"></c:out></td>
													<td class="center"><c:out value="${record.cancelTime }"></c:out></td>
													<td class="center"><c:out value="${record.verifyTime }"></c:out></td>
													<td align="center"><c:out value="${record.tenderStatusInfo }"></c:out></td>
													<td align="left">
														<c:choose>
														    <c:when test="${record.appointStatus ne 1}">
														       <c:out value="${record.appointRemark }"></c:out>
														    </c:when>
														    <c:when test="${record.appointStatus eq 1 and record.tenderStatus ne 1}">
														        <c:out value="${record.tenderRemark }"></c:out>
														    </c:when>
														    <c:otherwise>
														       <c:out value="${record.tenderRemark }"></c:out>
														    </c:otherwise>
														</c:choose>
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="appointList" paginator="${appointListForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${appointListForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${appointListForm.userName}" />
			</div>
			<div class="form-group">
				<label>预约订单号</label> 
				<input type="text" name="orderId" class="form-control input-sm underline"  maxlength="20" value="${appointListForm.orderId}" />
			</div>
			<div class="form-group">
				<label>预约状态</label>
				<select name="appointStatus" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${appointStatusList }" var="appointStatus" begin="0" step="1">
						<option value="${appointStatus.nameCd }"
							<c:if test="${appointStatus.nameCd eq appointListForm.appointStatus}">selected="selected"</c:if>>
							<c:out value="${appointStatus.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>出借订单号</label> 
				<input type="text" name="tenderNid" class="form-control input-sm underline"  maxlength="20" value="${appointListForm.tenderNid}" />
			</div>
			<div class="form-group">
				<label>项目编号</label> 
				<input type="text" name="borrowNid" class="form-control input-sm underline"  maxlength="20" value="${appointListForm.borrowNid}" />
			</div>
			<div class="form-group">
				<label>出借状态</label>
				<select name="tenderStatus" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${tenderStatusList }" var="tenderStatus" begin="0" step="1">
						<option value="${tenderStatus.nameCd }"
							<c:if test="${tenderStatus.nameCd eq appointListForm.tenderStatus}">selected="selected"</c:if>>
							<c:out value="${tenderStatus.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>预约时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="appointTimeStart" id="appointTimeStart" class="form-control underline" value="${appointListForm.appointTimeStart}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="appointTimeEnd" id="appointTimeEnd" class="form-control underline" value="${appointListForm.appointTimeEnd}" />
					</span>
				</div>
			</div>
			<div class="form-group">
				<label>撤销时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="cancelTimeStart" id="cancelTimeStart" class="form-control underline" value="${appointListForm.cancelTimeStart}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="cancelTimeEnd" id="cancelTimeEnd" class="form-control underline" value="${appointListForm.cancelTimeEnd}" />
					</span>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/appoint/appointList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
