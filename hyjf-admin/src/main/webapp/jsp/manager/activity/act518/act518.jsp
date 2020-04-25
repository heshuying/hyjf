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

<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="518理财活动" />

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">518理财活动</h1>
		<span class="mainDescription">518理财活动</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<div class="tab-content">
					<div class="tab-pane fade in active">
						<%-- 功能栏 --%>
						<div class="well">
							<c:set var="jspPrevDsb"
								value="${act518Form.paginator.firstPage ? ' disabled' : ''}"></c:set>
							<c:set var="jspNextDsb"
								value="${act518Form.paginator.lastPage ? ' disabled' : ''}"></c:set>
							<a
								class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
							<a
								class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
							<shiro:hasPermission name="activitylist:EXPORT">
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
							</shiro:hasPermission>
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="检索条件" data-toggle-class="active"
								data-toggle-target="#searchable-panel"><i
								class="fa fa-search margin-right-10"></i> <i
								class="fa fa-chevron-left"></i></a>

						</div>
						<br />
						<%-- 列表一览 --%>
						<table id="equiList"
							class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">用户名</th>
									<th class="center">手机号</th>
									<th class="center">优惠券面值</th>
									<th class="center">出借门槛</th>
									<th class="center">状态</th>
									<th class="center">生成时间</th>
								</tr>
							</thead>
							<tbody id="roleTbody">
								<c:choose>
									<c:when test="${empty recordList}">
										<tr>
											<td colspan="10">暂时没有数据记录</td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach items="${recordList }" var="record" begin="0"
											step="1" varStatus="status">
											<tr>
												<td class="center">${(act518Form.paginatorPage - 1 ) * act518Form.paginator.limit + status.index + 1 }</td>
												<td class="center"><c:out value="${record.userName }"></c:out></td>
												<td class="center"><c:out value="${record.mobile }"></c:out></td>
												<td class="center"><c:out value="${record.faceValue }"></c:out></td>
												<td class="center"><c:out value="${record.threshold }"></c:out></td>
												<td class="center">
													<c:choose>
														<c:when test="${record.type eq 0 || empty record.type}">未使用</c:when>
														<c:when test="${record.type eq 1}">已使用</c:when>
														<c:when test="${record.type eq 4}">已过期</c:when>
													</c:choose></td>
												<td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
						<%-- 分页栏 --%>
						<shiro:hasPermission name="activitylist:SEARCH">
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="searchAction" paginator="${act518Form.paginator}"></hyjf:paginator>
						</shiro:hasPermission>
						<br /> <br />
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 检索面板 (ignore) --%>
	<tiles:putAttribute name="searchPanels" type="string">
		<input type="hidden" name="paginatorPage" id="paginator-page"
			value="${act518Form.paginatorPage}" />
		<shiro:hasPermission name="activitylist:SEARCH">
			<div class="form-group">
				<label>用户名</label> <input type="text" name="usernameSrch"
					class="form-control input-sm underline" maxlength="80"
					value="${ act518Form.usernameSrch}" />
			</div>
			<div class="form-group">
				<label>手机号</label> <input type="text" name="mobileSrch"
					class="form-control input-sm underline" maxlength="80"
					value="${ act518Form.mobileSrch}" />
			</div>
			<div class="form-group">
				<label>优惠券面值</label> <select id="select"
					class="form-control underline form-select2"
					onchange="document.getElementById('typeSrch').value=document.getElementById('select').value">
					<option value=""></option>
					<option value="10"
						<c:if test="${10 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="10"></c:out>
					</option>
					<option value="20"
						<c:if test="${20 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="20"></c:out>
					</option>
					<option value="30"
						<c:if test="${30 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="30"></c:out>
					</option>
					<option value="40"
						<c:if test="${40 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="40"></c:out>
					</option>
					<option value="50"
						<c:if test="${50 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="50"></c:out>
					</option>
					<option value="60"
						<c:if test="${60 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="60"></c:out>
					</option>
					<option value="70"
						<c:if test="${70 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="70"></c:out>
					</option>
					<option value="80"
						<c:if test="${80 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="80"></c:out>
					</option>
					<option value="90"
						<c:if test="${90 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="90"></c:out>
					</option>
					<option value="100"
						<c:if test="${100 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="100"></c:out>
					</option>
					<option value="110"
						<c:if test="${110 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="110"></c:out>
					</option>
					<option value="120"
						<c:if test="${120 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="120"></c:out>
					</option>
					<option value="130"
						<c:if test="${130 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="130"></c:out>
					</option>
					<option value="140"
						<c:if test="${140 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="140"></c:out>
					</option>
					<option value="150"
						<c:if test="${150 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="150"></c:out>
					</option>
					<option value="160"
						<c:if test="${160 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="160"></c:out>
					</option>
					<option value="170"
						<c:if test="${170 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="170"></c:out>
					</option>
					<option value="180"
						<c:if test="${180 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="180"></c:out>
					</option>
					<option value="190"
						<c:if test="${190 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="190"></c:out>
					</option>
					<option value="200"
						<c:if test="${200 eq act518Form.typeSrch}">selected="selected"</c:if>>
						<c:out value="200"></c:out>
					</option>
				</select> <input type="text" style="display: none" id="typeSrch"
					name="typeSrch" class="form-control input-sm underline"
					maxlength="80" value="${ act518Form.typeSrch}" />
			</div>
			<div class="form-group">
				<label>生成时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="timeStartSrch" id="start-date-time"
						class="form-control underline" value="${act518Form.timeStartSrch}" />
						<i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="end-date-time"
						class="form-control underline" value="${act518Form.timeEndSrch}" />
				</div>
			</div>
		</shiro:hasPermission>
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/activity/act518/act518.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
