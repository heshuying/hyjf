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

<shiro:hasPermission name="borrowlog:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="借款列表" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">借款操作日志列表</h1>
			<span class="mainDescription">借款操作日志列表的说明。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="borrowlog:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowLogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowLogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="borrowlog:EXPORT">
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
										<th class="center">项目编号</th>
										<th class="center">项目状态</th>
										<th class="center">修改类型</th>
										<th class="center">操作人</th>
										<th class="center">操作时间</th>
										<!-- <th class="center">备注</th> -->
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr><td colspan="19">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(borrowLogForm.paginatorPage - 1 ) * borrowLogForm.paginator.limit + status.index + 1 }</td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.borrowStatus }"></c:out></td>
													<td><c:out value="${record.type }"></c:out></td>
													<td><c:out value="${record.createUserName }"></c:out></td>
													<td><c:out value="${record.createTime }"></c:out></td>
													<%-- <td><c:out value="${record.remrk }"></c:out></td> --%>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="borrowlog:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${borrowLogForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowlog:SEARCH">
				<input type="hidden" name=moveFlag id="moveFlag" value="BORROW_LIST"/>
				<input type="hidden" name=borrowNid id="borrowNid" />
				<input type="hidden" name="projectType" id="projectType" />
				<input type="hidden" name="col" id="col" value="${borrowLogForm.col}" />
				<input type="hidden" name="sort" id="sort" value="${borrowLogForm.sort}" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowLogForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>项目编号</label>
					<input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${borrowLogForm.borrowNidSrch}" />
				</div>
				<div class="form-group">
					<label>项目状态</label>
						<select name="statusSrch" id="statusSrch" class="form-control underline form-select2">
							<option value=""></option>
							<c:forEach items="${borrowStatusList }" var="borrowStatus" begin="0" step="1" varStatus="status">
								<option value="${borrowStatus.name }"
								<c:if test="${borrowStatus.nameCd eq borrowLogForm.statusSrch}">selected="selected"</c:if>>
								<c:out value="${borrowStatus.name }"></c:out></option>
							</c:forEach>
						</select>
				</div>
				<div class="form-group">
					<label>修改类型</label>
					<select name="typeSrch" id="typeSrch" class="form-control underline form-select2">
					    <option value=""></option>
						<option value="新增" <c:if test="${borrowLogForm.typeSrch eq '新增'}">selected="selected"</c:if>>新增</option>
						<option value="修改" <c:if test="${borrowLogForm.typeSrch eq '修改'}">selected="selected"</c:if>>修改</option>
					</select>
				</div>
				<div class="form-group">
					<label>操作人</label>
					<input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowLogForm.usernameSrch}" />
				</div>
				<div class="form-group">
					<label>添加时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline"  value="${borrowLogForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline"  value="${borrowLogForm.timeEndSrch}" />
					</div>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrow/borrow.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
