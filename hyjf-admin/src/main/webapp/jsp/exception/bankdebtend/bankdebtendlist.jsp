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


<shiro:hasPermission name="bankdebtend:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="债权结束" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">债权结束</h1>
			<span class="mainDescription">债转结束</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<!-- 新结束债券 相同的权限 后期可以加：class="active"-->
						<shiro:hasPermission name="bankdebtend:VIEW">
							<li><a href="${webRoot}/exception/bankdebtend/init">(新)结束债权</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="bankdebtend:VIEW">
							<li class="active" ><a href="${webRoot}/exception/bankdebtend/oldinit">结束债权</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="hjhcreditend:VIEW">
							<li><a href="${webRoot}/exception/hjhcreditendexception/init">汇计划债转结束债权</a></li>
						</shiro:hasPermission>
					</ul>
					<div class="tab-content">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb"
									value="${bankDebtEndForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb"
									value="${bankDebtEndForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel">
									<i class="fa fa-search margin-right-10"></i> 
									<i class="fa fa-chevron-left"></i></a>
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
										<th class="center">项目编号</th>
										<th class="center">用户名</th>
										<th class="center">出借订单号</th>
										<th class="center">银行授权码</th>
										<th class="center">手机号</th>
										<th class="center">放款时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="8">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0"
												step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((bankDebtEndForm.paginatorPage - 1) * bankDebtEndForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.borrowNid }"></c:out></td>
													<td><c:out value="${record.userName }"></c:out></td>
													<td><c:out value="${record.tenderNid }"></c:out></td>
													<td><c:out value="${record.authCode}"></c:out></td>
													<td><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.addTime }"></hyjf:datetime></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="bankdebtend:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-userid="${record.userId }" data-tendernid="${record.tenderNid }" data-borrownid="${record.borrowNid }" data-toggle="tooltip" tooltip-placement="top" data-original-title="更新">
																	<i class="fa fa-pencil"></i>
																</a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<ul class="dropdown-menu pull-right dropdown-light"
																	role="menu">
																	<shiro:hasPermission name="bankdebtend:MODIFY">
																		<li><a class="fn-Modify" data-userid="${record.userId }" data-tendernid="${record.tenderNid }" data-borrownid="${record.borrowNid }">更新</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${bankDebtEndForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
			</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name=borrowNid id="borrowNid" />
			<input type="hidden" name="userId" id="userId" />
			<input type="hidden" name="tenderNid" id="tenderNid" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${bankDebtEndForm.paginatorPage}" />
			<div class="form-group">
				<label>用户名:</label>
				<input type="text" name="userNameSrch"
					id="userNameSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.userNameSrch}" />
			</div>
			<div class="form-group">
				<label>项目编号:
				</label> <input type="text" name="borrowNidSrch"
					id="borrowNidSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.borrowNidSrch}" />
			</div>
			<div class="form-group">
				<label>手机号:
				</label> <input type="text" name="mobileSrch"
					id="mobileSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.mobileSrch}" />
			</div>
			<div class="form-group">
				<label>出借订单号:
				</label> <input type="text" name="tenderNidSrch"
					id="tenderNidSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.tenderNidSrch}" />
			</div>
			
			<div class="form-group">
				<label>银行授权码:</label> 
				<input type="text" name="authCodeSrch"
					id="authCodeSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.authCodeSrch}" />
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript'
				src="${webRoot}/jsp/exception/bankdebtend/bankdebtendlist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
