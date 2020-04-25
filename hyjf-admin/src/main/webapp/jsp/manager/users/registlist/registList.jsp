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
<c:set value="${fn:split('汇盈金服,注册记录', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="registlist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="注册记录" />
		
		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">注册记录</h1>
			<span class="mainDescription">本功能可以修改查询相应的注册记录信息。</span>
		</tiles:putAttribute>
		
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="registlist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${registListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${registListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="registlist:EXPORT">
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
									<%-- <col style="width:;" /> --%>
									<col style="width:;" />
									<col style="width:;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">手机号</th>
										<th class="center">推荐人</th>
										<!-- <th class="center">用户属性</th> -->
										<th class="center">注册渠道</th>
										<th class="center">注册平台</th>
										<th class="center">注册IP</th>
										<th class="center">注册时间</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty registListForm.recordList}">
											<tr><td colspan="9">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${registListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(registListForm.paginatorPage-1)*registListForm.paginator.limit+status.index+1 }</td>
													<td><c:out value="${record.userName }"></c:out></td>
													<td class="center"><hyjf:asterisk value="${record.mobile }" permission="registlist:HIDDEN_SHOW"></hyjf:asterisk></td>
													<td><c:out value="${record.recommendName }"></c:out></td>
													<%-- <td><c:out value="${record.userProperty }"></c:out></td> --%>
													<td><c:out value="${record.sourceName }"></c:out></td>
													<td><c:out value="${record.registPlat }"></c:out></td>
													<td><c:out value="${record.regIP }"></c:out></td>
													<td class="center"><c:out value="${record.regTime }"></c:out></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="registlist" paginator="${registListForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
		<div  id = "testff">
			<input type="hidden" name="userId" id="userId" value= "${registListForm.userId}"/>
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${registListForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${registListForm.userName}" />
			</div>
			<div class="form-group">
				<label>手机号码</label> 
				<input type="text" name="mobile" class="form-control input-sm underline"  maxlength="20" value="${registListForm.mobile}" />
			</div>
			<div class="form-group">
				<label>推荐人</label> 
				<input type="text" name="recommendName" class="form-control input-sm underline"  maxlength="20" value="${registListForm.recommendName}" />
			</div>
			<%-- <div class="form-group">
				<label>注册渠道</label>
				<select name="sourceId" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${utmPlat }" var="plat" begin="0" step="1">
						<option value="${plat.sourceId }"
							<c:if test="${plat.sourceId eq registListForm.sourceId}">selected="selected"</c:if>>
							<c:out value="${plat.sourceName }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div> --%>
			<div class="form-group">
				<label>注册平台</label>
				<select name="registPlat" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${registPlat }" var="replat" begin="0" step="1">
						<option value="${replat.nameCd }"
							<c:if test="${replat.nameCd eq registListForm.registPlat}">selected="selected"</c:if>>
							<c:out value="${replat.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>注册时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="regTimeStart" id="start-date-time" class="form-control underline" value="${registListForm.regTimeStart}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="regTimeEnd" id="end-date-time" class="form-control underline" value="${registListForm.regTimeEnd}" />
					</span>
				</div>
			</div>
			</div>
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogpanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/registlist/registList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
