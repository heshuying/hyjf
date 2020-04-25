<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>


<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="false" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="appchanneldetail:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="渠道统计" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">app渠道统计</h1>
			<span class="mainDescription">app渠道统计明细</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="appchanneldetail:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${channelstatisticsdetailForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${channelstatisticsdetailForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="appchanneldetail:EXPORT">
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
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">渠道</th>
										<th class="center">用户名</th>
										<th class="center">注册时间</th>
										<th class="center">开户时间</th>
										<th class="center">首次出借时间</th>
										<th class="center">首投项目类型</th>
										<th class="center">首投项目期限</th>
										<th class="center">首投金额</th>
										<th class="center">累计出借金额</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="11">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(channelstatisticsdetailForm.paginatorPage - 1 ) * channelstatisticsdetailForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.sourceName }"></c:out></td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><fmt:formatDate value="${record.registerTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
													<td class="center"><fmt:formatDate value="${record.openAccountTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
													<td class="center"><hyjf:datetime value="${record.firstInvestTime}"></hyjf:datetime></td>
													<td align="center"><c:out value="${record.investProjectType }"></c:out></td>
													<td align="center"><c:out value="${record.investProjectPeriod }"></c:out></td>
													<td align="right"><c:out value="${record.investAmount }"></c:out></td>
													<td align="right"><c:out value="${record.cumulativeInvest }"></c:out></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="appchanneldetail:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${channelstatisticsdetailForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="appchanneldetail:SEARCH">
				<input type="hidden" name="utmIds" id="utmIds" value="${channelstatisticsdetailForm.utmIds}" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${channelstatisticsdetailForm.paginatorPage}" />
				<c:if test="${flag == 0}">
					<div class="form-group">
						<label>渠道</label> 
						<select name="sourceIdSrch" class="form-control underline form-select2">
							<option value=""></option>
							<c:forEach items="${UtmPlatList }" var="utm" begin="0" step="1">
								<option value="${utm.sourceId }"
									<c:if test="${utm.sourceId eq channelstatisticsdetailForm.sourceIdSrch}">selected="selected"</c:if>>
									<c:out value="${utm.sourceName }"></c:out>
								</option>
							</c:forEach>
						</select>
					</div>
				</c:if>
				<div class="form-group">
					<label>用户名</label> 
					<input type="text" name="userNameSrch" class="form-control input-sm underline"  maxlength="20" value="${ channelstatisticsdetailForm.userNameSrch}" />
				</div>				
			</shiro:hasPermission>
		</tiles:putAttribute>			

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/promotion/app/channeldetail/channelstatisticsdetail.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
