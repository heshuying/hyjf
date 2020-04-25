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


<shiro:hasPermission name="feeconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="手续费设置" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">提现时间配置</h1>
			<span class="mainDescription">本功能可以增加修改删除。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab"> 
				<shiro:hasPermission name="feeconfig:VIEW">
		      		<li><a href="${webRoot}/manager/config/bankconfig/init">银行配置</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="feeconfig:VIEW">
		      		<li><a href="${webRoot}/manager/config/feeconfig/init">手续费配置</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="feeconfig:VIEW">
		      		<li class="active"><a href="${webRoot}/manager/config/withdrawalstimeconfig/init">提现时间配置</a></li>
		      	</shiro:hasPermission>
      			<shiro:hasPermission name="bankconfig:VIEW">
					<li><a href="${webRoot}/manager/config/bankrecharge/init">快捷充值限额</a></li>
				</shiro:hasPermission>
		    </ul>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${withdrawalstimeconfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${withdrawalstimeconfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<shiro:hasPermission name="feeconfig:ADD"> 
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
									data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
							</shiro:hasPermission> 
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
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
								<th class="center">工作日</th>
								<th class="center">时间</th>
								<th class="center">即时提现</th>
								<th class="center">快速提现</th>
								<th class="center">一般提现</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty withdrawalstimeconfigForm.recordList}">
									<tr>
										<td colspan="10">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${withdrawalstimeconfigForm.recordList }" var="record"
										begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out
													value="${status.index+((withdrawalstimeconfigForm.paginatorPage - 1) * withdrawalstimeconfigForm.paginator.limit) + 1 }"></c:out></td>
											
											<td align="center"><c:out value="${record.ifWorkingday }"></c:out></td>
											<td align="center"><c:out value="${record.withdrawalsStart }-${record.withdrawalsEnd }"></c:out></td>
											<td align="center"><c:out value="${record.immediatelyWithdraw }"></c:out></td>
											<td align="center"><c:out value="${record.quickWithdraw }"></c:out></td>
											<td align="center"><c:out value="${record.normalWithdraw }"></c:out></td>
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<shiro:hasPermission name="feeconfig:MODIFY">
														<a class="btn btn-transparent btn-xs fn-Modify"
															data-id="${record.id}" data-toggle="tooltip"
															tooltip-placement="top" data-original-title="修改"><i
															class="fa fa-pencil"></i></a>
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
															<shiro:hasPermission name="feeconfig:MODIFY">
																<li><a class="fn-Modify" data-id="${record.id }">修改</a>
																</li>
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
					<hyjf:paginator id="equiPaginator" hidden="paginator-page"
						action="init" paginator="${withdrawalstimeconfigForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		 <tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page"
				value="${withdrawalstimeconfigForm.paginatorPage}" />
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/withdrawalstimeconfig/withdrawalstimeconfig.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
