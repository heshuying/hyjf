<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="bindcardexception:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="银行卡异常管理" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">银行卡异常管理</h1>
			<span class="mainDescription">本功能可以解决汇付和汇盈金服银行卡对应不上的BUG。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb"
									value="${bindcardexceptionForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb"
									value="${bindcardexceptionForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a
									class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom"
									data-original-title="检索条件" data-toggle-class="active"
									data-toggle-target="#searchable-panel">
									<i class="fa fa-search margin-right-10"></i> 
									<i class="fa fa-chevron-left"></i>
								</a>
							</div>
							<br />
							<%-- 模板列表一览 --%>
							<table id="equiList"
								class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">银行电子账户</th>
										<th class="center">银行账号</th>
										<th class="center">所属银行</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty bindcardexceptionForm.recordList}">
											<tr>
												<td colspan="6">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${bindcardexceptionForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">
													<c:out value="${status.index+((bindcardexceptionForm.paginatorPage - 1) * bindcardexceptionForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.userName }"></c:out></td>
													<td><c:out value="${record.accountId }"></c:out></td>
													<td><c:out value="${record.cardNo}"></c:out></td>
													<td><c:out value="${record.bankName }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="bindcardexception:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-userid="${record.userId }" data-accountid="${record.accountId }" data-toggle="tooltip" tooltip-placement="top" data-original-title="更新"><i
																	class="fa fa-pencil"></i>
																</a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="bindcardexception:MODIFY">
																		<li><a class="fn-Modify" data-userid="${record.userId }" data-accountid="${record.accountId }">更新</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction"
								paginator="${bindcardexceptionForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="userId" id="userId" />
			<input type="hidden" name="accountId" id="accountId" />
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${bindcardexceptionForm.paginatorPage}" />
			<div class="form-group">
				<label>用户名:</label> 
				<input type="text" name="userNameSrch" id="userNameSrch" class="form-control input-sm underline" value="${bindcardexceptionForm.userNameSrch}" />
			</div>
			<div class="form-group">
				<label>银行电子账号:</label> 
				<input type="text" name="accountIdSrch" id="accountIdSrch" class="form-control input-sm underline" value="${bindcardexceptionForm.accountIdSrch}" />
			</div>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/exception/bindcardexception/bindcardexceptionlist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>

