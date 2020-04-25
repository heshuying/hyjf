<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="banksetting:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="银行设置" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">银行设置</h1>
			<span class="mainDescription">本功能可以增加修改删除。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<div class="tab-content">
						<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${bankconfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${bankconfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="banksetting:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add" data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出">导出<i class="fa fa-plus"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
								data-toggle="tooltip" data-placement="bottom"
								data-original-title="检索条件" data-toggle-class="active"
								data-toggle-target="#searchable-panel"><i
								class="fa fa-search margin-right-10"></i> <i
								class="fa fa-chevron-left"></i></a>
							</div>
							<br />
								
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
									<col />
									<col />
									<col style="width: 93px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">银行名称</th>
										<th class="center">银行联行号</th>
										<th class="center">银行ICON</th>
										<th class="center">LOGO</th>
										<th class="center">支持快捷支付</th>
										<th class="center">快捷充值单笔限额</th>
										<th class="center">快捷充值单日限额</th>
										<th class="center">快捷充值单月限额</th>
										<th class="center">提现手续费</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty banksettingForm.recordList}">
											<tr>
												<td colspan="6">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${banksettingForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((banksettingForm.paginatorPage - 1) * banksettingForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.bankName }"></c:out></td>
													<td><c:out value="${record.payAllianceCode }"></c:out></td>
													<td class="center"><a href="${fileDomainUrl}${record.bankIcon}" target="_blank" class="thumbnails-wrap" data-toggle="popover" data-placement="right" data-trigger="hover" data-html="true" data-title="图片预览" data-content="<img src='${fileDomainUrl}${record.bankIcon}' style='max-height:350px;'/>"> <img src="${fileDomainUrl}${record.bankIcon}" />
													</a></td>
													<td class="center"><a href="${fileDomainUrl}${record.bankLogo}" target="_blank" class="thumbnails-wrap" data-toggle="popover" data-placement="right" data-trigger="hover" data-html="true" data-title="图片预览" data-content="<img src='${fileDomainUrl}${record.bankLogo}' style='max-height:350px;'/>"> <img src="${fileDomainUrl}${record.bankLogo}" />
													</a></td>
													<td class="center"><c:out value="${record.quickPayment == '1' ? '是' : '否'  }"></c:out></td>
													<td class="center"><c:out value="${record.singleQuota == '0.00' ? '无限' : record.singleQuota }"></c:out></td>
													<td class="center"><c:out value="${record.singleCardQuota  == '0.00' ? '无限' : record.singleCardQuota }"></c:out></td>
													<td class="center">
													<c:if test="${record.quickPayment == '1'}">
														<c:out value="${record.monthCardQuota == '0.00' ? '无限' :  record.monthCardQuota}"></c:out></c:if>
														<c:if test="${record.quickPayment == '0'}">
															<c:out value="0.00"></c:out></c:if>
													</td>
													<td class="center"><c:out value="${record.feeWithdraw }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="banksetting:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" 
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="banksetting:MODIFY">
																<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-id="${record.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="banksetting:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="banksetting:DELETE">
																		<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${banksettingForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" id="ids" name="ids" value="${banksettingForm.ids }" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${banksettingForm.paginatorPage}" />
			<div class="form-group">
			<label>银行名称</label> <input type="text" name="bankName" id="bankName"
				class="form-control input-sm underline" value="${banksettingForm.bankName}" />
			</div>
			<div class="form-group">
				<label>银行联行号</label> <input type="text" name="payAllianceCode" id="payAllianceCode"
				class="form-control input-sm underline" value="${banksettingForm.payAllianceCode}" />
			</div>
		</tiles:putAttribute>
		

		
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
.thumbnails-wrap {
	border: 1px solid #ccc;
	padding: 3px;
	display: inline-block;
}

.thumbnails-wrap img {
	min-width: 35px;
	height: 22px;
}

.popover {
	max-width: 500px;
}

.popover img {
	max-width: 460px;
}
</style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/banksetting/banksetting.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
