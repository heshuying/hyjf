<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

<shiro:hasPermission name="accountconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="账户配置" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">账户配置</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
		      	<shiro:hasPermission name="accountconfig:VIEW">
		      		<li class="active"><a href="${webRoot}/manager/config/accountconfig/init">账户配置</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="accountbalance:VIEW">
		      		<li><a href="${webRoot}/manager/config/accountbalance/init">余额监控</a></li>
		      	</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${accountconfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${accountconfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="accountconfig:ADD">
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
									data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
						</shiro:hasPermission>
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
								<th class="center">子账户名称</th>
								<th class="center">子账户类型</th>
								<th class="center">子账户代号</th>
								<th class="center">子账户转入</th>
								<th class="center">子账户转出</th>
								<th class="center">用途</th>
								<th class="center">排序</th>
								<th class="center">更新时间</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty accountconfigForm.subAccountList}">
									<tr>
										<td colspan="10">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${accountconfigForm.subAccountList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((accountconfigForm.paginatorPage - 1) * accountconfigForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.subAccountName }"></c:out></td>
											<td class="center"><c:out value="${record.subAccountType }"></c:out></td>
											<td class="center"><c:out value="${record.subAccountCode }"></c:out></td>
											<td class="center"><c:out value="${record.transferIntoFlg == '0' ? '不支持' : '支持'  }"></c:out></td>
											<td class="center"><c:out value="${record.transferOutFlg == '0' ? '不支持' : '支持'  }"></c:out></td>
											<td class="center"><c:out value="${record.purpose }"></c:out></td>
											<td class="center"><c:out value="${record.sort }"></c:out></td>
											<td class="center"><hyjf:datetime value="${record.updateTime}"></hyjf:datetime></td>
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<shiro:hasPermission name="accountconfig:MODIFY">
														<a class="btn btn-transparent btn-xs fn-Modify" data-ids="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
													</shiro:hasPermission>
												</div>
												<div class="visible-xs visible-sm hidden-md hidden-lg">
													<div class="btn-group" dropdown="">
														<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
															<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
														</button>
														<ul class="dropdown-menu pull-right dropdown-light" role="menu">
															<shiro:hasPermission name="accountconfig:MODIFY">
																<li><a class="fn-Modify" data-ids="${record.id }">修改</a></li>
															</shiro:hasPermission>
														</ul>
													</div>
												</div>
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${accountconfigForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="accountconfig:SEARCH">
				<input type="hidden" name="ids" id="ids" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${accountconfigForm.paginatorPage}" />
				<!-- 查询条件 -->
				<div class="form-group">
					<label>子账户名称</label>
					<input type="text" name="subAccountNameSear" id="subAccountNameSear" class="form-control input-sm underline" value="${accountconfigForm.subAccountNameSear}" />
				</div>
				<div class="form-group">
					<label>子账户类型</label>
					<select name="subAccountTypeSear" class="form-control underline form-select2">
						<option value="">全部</option>
						<c:forEach items="${subaccountType }" var="record" begin="0" step="1" varStatus="status">
							<option value="${record.nameCd }"
								<c:if test="${record.nameCd eq accountconfigForm.subAccountTypeSear}">selected="selected"</c:if>>
								<c:out value="${record.name }"></c:out></option>
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
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/account/accountconfig/accountconfigList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
