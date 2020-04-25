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
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>

<shiro:hasPermission name="borrowexceptionlog:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款异常列表" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">借款异常列表</h1>
			<span class="mainDescription">借款异常列表的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="borrowexceptionlog:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowexceptiondeleteForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowexceptiondeleteForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<shiro:hasPermission name="borrowexceptionlog:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
											data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i> </a>
									</shiro:hasPermission>
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
										<th class="center">项目编号</th>
										<th class="center">项目名称</th>
										<th class="center">借款期限</th>
										<th class="center">借款金额</th>
										<th class="center">借款人</th>
										<th class="center">保证金</th>
										<th class="center">项目状态</th>
										<th class="center">添加时间</th>
										<th class="center">操作类型</th>
										<th class="center">
											<c:choose>
												<c:when test="${ !empty borrowexceptiondeleteForm.sort and borrowexceptiondeleteForm.col eq 'hbdl.operater_time' }">
													<a href="#" class="fn-Sort" data-col="hbdl.operater_time" data-sort="<c:if test="${ borrowexceptiondeleteForm.sort eq 'asc' }">desc</c:if><c:if test="${ borrowexceptiondeleteForm.sort eq 'desc' }">asc</c:if>">操作时间</a>&nbsp;<i class="fa fa-sort-${borrowexceptiondeleteForm.sort}"></i>
												</c:when>
												<c:otherwise>
													<a href="#" class="fn-Sort" data-col="hbdl.operater_time" data-sort="desc">操作时间</a>&nbsp;<i class="fa fa-sort"></i>
												</c:otherwise>
											</c:choose>
										</th>
										<th class="center">操作人员</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="14">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowexceptiondeleteForm.paginatorPage - 1 ) * borrowexceptiondeleteForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrow_nid }"></c:out></td>
													<td><c:out value="${record.borrow_name }"></c:out></td>
													<td align="center"><c:out value="${record.borrow_period }"></c:out></td>
													<td align="right"><c:out value="${record.account }"></c:out></td>
													<td align="center"><c:out value="${record.username }"></c:out></td>
													<td align="center"><c:out value="${record.bail_num }"></c:out></td>
													<td align="center"><c:out value="${record.status }"></c:out></td>
													<td align="center"><c:out value="${record.addtime }"></c:out></td>
													<td align="center"><c:out value="${record.operater_type }"></c:out></td>
													<td align="center"><c:out value="${record.operater_time_str }"></c:out></td>
													<td align="center"><c:out value="${record.operater_user }"></c:out></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="borrowexceptionlog:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="borrowdeleteinit" paginator="${borrowexceptiondeleteForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowexceptionlog:SEARCH">
				<input type="hidden" name=moveFlag id="moveFlag" value="BORROW_LIST"/>
				<input type="hidden" name=borrowNid id="borrowNid" />
				<input type="hidden" name="projectType" id="projectType" />
				<input type="hidden" name="col" id="col" value="${borrowexceptiondeleteForm.col}" />
				<input type="hidden" name="sort" id="sort" value="${borrowexceptiondeleteForm.sort}" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowexceptiondeleteForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrow_nidSrch" id="borrow_nidSrch" class="form-control input-sm underline" value="${borrowexceptiondeleteForm.borrow_nidSrch}" />
				</div>
				<div class="form-group">
					<label>项目名称</label>
					<input type="text" name="borrow_nameSrch" id="borrow_nameSrch" class="form-control input-sm underline" value="${borrowexceptiondeleteForm.borrow_nameSrch}" />
				</div>
				<div class="form-group">
					<label>操作人员</label>
					<input type="text" name="operater_userSrch" id="operater_userSrch" class="form-control input-sm underline" value="${borrowexceptiondeleteForm.operater_userSrch}" />
				</div>
				<div class="form-group">
					<label>删除时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="operater_time_startSrch" id="operater_time_startSrch" class="form-control underline" value="${borrowexceptiondeleteForm.operater_time_startSrch}" />
							<i class="ti-calendar"></i> 
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="operater_time_endSrch" id="operater_time_endSrch" class="form-control underline" value="${borrowexceptiondeleteForm.operater_time_endSrch}" />
					</div>
				</div>
				<div class="form-group">
					<label>操作类型</label>
					<select name="operater_typeSrch" id="operater_typeSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="0">删除</option>
						<option value="1">撤销</option>
					</select>
				</div>
			</shiro:hasPermission>
		</tiles:putAttribute>		

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/borrowexception/borrowexceptiondelete.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
