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
<%-- 画面功能路径 (ignore) --%>
<shiro:hasPermission name="bidcancelexception:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="银行出借撤销" />
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="bidcancelexception:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${form.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
						<br/>
						<%-- 列表一览 --%>
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width: 55px;" />
							</colgroup>
							<thead>
								<tr>
									<th class="center">序号</th>
									<th class="center">项目编号</th>
									<th class="center">订单号</th>
									<th class="center">出借人</th>
									<th class="center">出借金额</th>
									<th class="center">出借时间</th>
									<th class="center">操作</th>
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
										<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
											<tr>
												<td class="center">${(form.paginatorPage -1 ) * form.paginator.limit + status.index + 1 }</td>
												<td><c:out value="${record.borrowNid }"/></td>	
												<td class="center"><c:out value="${record.nid }"/></td>
												<td><c:out value="${record.tenderUserName }"/></td>
												<td><c:out value="${record.account }"/></td>													
												<td class="center"><hyjf:datetime value="${record.addtime }"></hyjf:datetime></td>
												<td class="center">
													<div class="visible-md visible-lg hidden-sm hidden-xs">
														<shiro:hasPermission name="bidcancelexception:BIDCANCEL">
															<a class="btn btn-transparent btn-xs tooltips fn-UpdateBalance" data-borrowNid ="${record.borrowNid }" data-orderid="${record.nid }" data-toggle="tooltip" tooltip-placement="top" data-original-title="出借撤销"><i class="fa fa-cloud-download"></i></a>
														</shiro:hasPermission>
													</div>
													<div class="visible-xs visible-sm hidden-md hidden-lg">
														<div class="btn-group" dropdown="">
															<button type="button"
																class="btn btn-primary btn-o btn-sm"
																data-toggle="dropdown">
																<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
															</button>
															<ul class="dropdown-menu pull-right dropdown-light"
																role="menu">
																<shiro:hasPermission name="bidcancelexception:BIDCANCEL">
																	<li><a class="fn-UpdateBalance" data-borrowNid ="${record.borrowNid }" data-orderid="${record.nid }">撤销</a></li>
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
						<shiro:hasPermission name="bidcancelexception:SEARCH">
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${form.paginator}"></hyjf:paginator>
						</shiro:hasPermission>
						<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="bidcancelexception:SEARCH">
				<input type="hidden" name="orderId" id="orderId" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />
				<div class="form-group">
					<label>用户名:</label>
					<input type="text" name="userName" value="${form.userName}" class="form-control input-sm underline" maxlength="20" />
				</div>
				<div class="form-group">
					<label>项目编号:</label>
					<input type="text" name="borrowNid" value="${form.borrowNid}" class="form-control input-sm underline" maxlength="20" />
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

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/exception/tendercancelexception/tendercancelexception.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>

