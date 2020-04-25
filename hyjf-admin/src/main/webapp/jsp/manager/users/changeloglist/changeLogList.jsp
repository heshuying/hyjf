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
<c:set value="${fn:split('汇盈金服,操作日志', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="changeloglist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="操作日志" />
		
		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">操作日志</h1>
			<span class="mainDescription">本功能可以查看用户信息修改的记录列表。</span>
		</tiles:putAttribute>
		
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="changeloglist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${changeLogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${changeLogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="changeloglist:EXPORT">
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
										<th class="center">姓名</th>
										<th class="center">手机号</th>
										<!--合规四期,天机邮箱-->
										<th class="center">邮箱</th>
										<th class="center">身份证</th>
										<th class="center">用户角色</th>
										<!-- <th class="center">用户属性</th> -->
										<th class="center">推荐人</th>
										<th class="center">51老用户</th>
										<th class="center">用户状态</th>
										<th class="center">修改人</th>
										<th class="center">修改时间</th>
										<th class="center">说明</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty changeLogForm.recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${changeLogForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(changeLogForm.paginatorPage-1)*changeLogForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.username }"></c:out></td>
													<td class="center"><c:out value="${record.realName }"></c:out></td>
													<td class="center"><hyjf:asterisk value="${record.mobile }" permission="changeloglist:HIDDEN_SHOW"></hyjf:asterisk></td>
													<td class="center"><c:out value="${record.email}"></c:out></td>
													<td class="center"><c:out value="${record.idCard }"></c:out></td>
													<td class="center"><c:out value="${record.role == '1' ? '出借人' : '借款人'  }"></c:out></td>
													<%-- <td class="center"><c:if test="${record.attribute == '0'}">无主单</c:if> <c:if test="${record.attribute == '1'}">有主单</c:if> <c:if test="${record.attribute == '2'}">线下员工</c:if><c:if test="${record.attribute == '3'}">线上员工</c:if></td> --%>
													<td class="center"><c:out value="${record.recommendUser }"></c:out></td>
													<td class="center"><c:out value="${record.is51 =='1' ? '是' : '否'}"></c:out></td>
													<td class="center"><c:out value="${record.status == '0' ? '启用' : '禁用'}"></c:out></td>
													<td class="center"><c:out value="${record.changeUser }"></c:out></td>
													<td class="center"><c:out value="${record.changeTime }"></c:out></td>
													<td class="center"><c:out value="${record.remark }"></c:out></td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${changeLogForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${changeLogForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="username" class="form-control input-sm underline"  maxlength="20" value="${changeLogForm.username}" />
			</div>
			<div class="form-group">
				<label>姓名</label>
				<input type="text" name="realName" class="form-control input-sm underline" maxlength="20" value="${changeLogForm.realName}"/>
			</div>
			<div class="form-group">
				<label>邮箱</label>
				<input type="text" name="email" class="form-control input-sm underline"  maxlength="20" value="${changeLogForm.email}" />
			</div>
			<div class="form-group">
				<label>手机号码</label> 
				<input type="text" name="mobile" class="form-control input-sm underline"  maxlength="20" value="${changeLogForm.mobile}" />
			</div>
			<div class="form-group">
				<label>推荐人</label> 
				<input type="text" name="recommendUser" class="form-control input-sm underline"  maxlength="20" value="${changeLogForm.recommendUser}" />
			</div>
			<div class="form-group">
				<label>用户属性</label>
				<select name="attribute" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${userPropertys }" var="property" begin="0" step="1" >
						<option value="${property.nameCd }"
							<c:if test="${property.nameCd eq changeLogForm.attribute}">selected="selected"</c:if>>
							<c:out value="${property.name }"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>51老用户</label>
				<select name="is51" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${is51 }" var="is51" begin="0" step="1">
						<option value="${is51.nameCd }"
							<c:if test="${is51.nameCd eq changeLogForm.is51}">selected="selected"</c:if>>
							<c:out value="${is51.name }"></c:out>
						</option>
					</c:forEach>
				</select>
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
				<label>修改时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="startTime" id="start-date-time" class="form-control underline" value="${changeLogForm.startTime}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="endTime" id="end-date-time" class="form-control underline" value="${changeLogForm.endTime}" />
					</span>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogpanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/changeloglist/changeLogList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
