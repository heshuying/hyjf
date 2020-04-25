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

<shiro:hasPermission name="evaluationchecklog:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="用户测评配置日志" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">测评配置</h1>
			<span class="mainDescription">本功能可以修改测评配置信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<shiro:hasPermission name="evaluationchecklog:SEARCH">
							<li><a href="${webRoot}/manager/config/evaluationchecklog/init">开关</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="evaluationmoneylog:SEARCH">
							<li><a href="${webRoot}/manager/config/evaluationmoneylog/init">限额配置</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="borrowLevelConfigLog:SEARCH">
							<li class="active"><a href="${webRoot}/manager/config/evaluationconfig/borrowlevelconfiglog/init">信用等级配置日志</a></li>
						</shiro:hasPermission>
					</ul>
						<div class="tab-content">
							<div class="tab-pane fade in active">
									<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowLevelConfigLogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowLevelConfigLogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>

									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom"
									   data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel">
										<i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i>
									</a>
								</div>
								<br />
									<%-- 列表一览 --%>
								<table id="equiList" class="table table-striped table-bordered table-hover">
									<colgroup>
										<col style="width: 55px;" />
									</colgroup>
									<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">BBB信用等级对应的建议出借者类型</th>
										<th class="center">A信用等级对应的建议出借者类型</th>
										<th class="center">AA-信用等级对应的建议出借者类型</th>
										<th class="center">AA信用等级对应的建议出借者类型</th>
										<th class="center">AA+信用等级对应的建议出借者类型</th>
										<th class="center">AAA信用等级对应的建议出借者类型</th>
										<th class="center">修改人</th>
										<th class="center">IP</th>
										<th class="center">操作时间</th>
									</tr>
									</thead>
									<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty borrowLevelConfigLogForm.recordList}">
											<tr>
												<td colspan="10">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${borrowLevelConfigLogForm.recordList }" var="check" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((borrowLevelConfigLogForm.paginatorPage - 1) * borrowLevelConfigLogForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center">
														<c:if test="${check.bbbEvaluationProposal ne '进取型'}"><c:out value="${check.bbbEvaluationProposal}"></c:out>及以上</c:if>
														<c:if test="${check.bbbEvaluationProposal eq '进取型'}"><c:out value="${check.bbbEvaluationProposal}"></c:out></c:if>
													</td>
													<td class="center">
														<c:if test="${check.aEvaluationProposal ne '进取型'}"><c:out value="${check.aEvaluationProposal}"></c:out>及以上</c:if>
														<c:if test="${check.aEvaluationProposal eq '进取型'}"><c:out value="${check.aEvaluationProposal}"></c:out></c:if>
													</td>
													<td class="center">
														<c:if test="${check.aa0EvaluationProposal ne '进取型'}"><c:out value="${check.aa0EvaluationProposal}"></c:out>及以上</c:if>
														<c:if test="${check.aa0EvaluationProposal eq '进取型'}"><c:out value="${check.aa0EvaluationProposal}"></c:out></c:if>
													</td>
													<td class="center">
														<c:if test="${check.aa1EvaluationProposal ne '进取型'}"><c:out value="${check.aa1EvaluationProposal}"></c:out>及以上</c:if>
														<c:if test="${check.aa1EvaluationProposal eq '进取型'}"><c:out value="${check.aa1EvaluationProposal}"></c:out></c:if>
													</td>
													<td class="center">
														<c:if test="${check.aa2EvaluationProposal ne '进取型'}"><c:out value="${check.aa2EvaluationProposal}"></c:out>及以上</c:if>
														<c:if test="${check.aa2EvaluationProposal eq '进取型'}"><c:out value="${check.aa2EvaluationProposal}"></c:out></c:if>
													</td>
													<td class="center">
														<c:if test="${check.aaaEvaluationProposal ne '进取型'}"><c:out value="${check.aaaEvaluationProposal}"></c:out>及以上</c:if>
														<c:if test="${check.aaaEvaluationProposal eq '进取型'}"><c:out value="${check.aaaEvaluationProposal}"></c:out></c:if>
													</td>
													<td class="center"><c:out value="${check.updateUser}"></c:out></td>
													<td class="center"><c:out value="${check.ip}"></c:out></td>
													<td class="center"><fmt:formatDate value="${check.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
									</tbody>
								</table>
									<%-- 分页栏 --%>
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${borrowLevelConfigLogForm.paginator}"></hyjf:paginator>
								<br /> <br />
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowLevelConfigLog:SEARCH">
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowLevelConfigLogForm.paginatorPage}" />
				<input type="hidden" name="id" id="id" />
				<!-- 查询条件 -->
				<div class="form-group">
					<label>修改时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="startTimeSrch" id="start-date-time" class="form-control underline" value="${borrowLevelConfigLogForm.startTimeSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="endTimeSrch" id="end-date-time" class="form-control underline" value="${borrowLevelConfigLogForm.endTimeSrch}" />
					</div>
				</div>

				<div class="form-group">
					<label>修改人</label>
					<input type="text" name="updateUserSrch" id="updateUserSrch" class="form-control input-sm underline" value="${borrowLevelConfigLogForm.updateUserSrch}" />
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
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/evaluationconfig/borrowlevelconfig/borrowlevelconfigloglist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
