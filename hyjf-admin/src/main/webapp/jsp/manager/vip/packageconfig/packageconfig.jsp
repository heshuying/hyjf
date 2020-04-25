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


<shiro:hasPermission name="PACKAGECONFIG:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="vip礼包配置" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">vip礼包配置</h1>
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
						<c:set var="jspPrevDsb" value="${packageconfigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${packageconfigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
							<shiro:hasPermission name="PACKAGECONFIG:ADD"> 
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
								<!-- <th class="center">年份</th> -->
								<th class="center">VIP等级</th>
								<th class="center">优惠券类型</th>
								<th class="center">名称</th>
								<th class="center">面值</th>
								<th class="center">数量</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty packageconfigForm.recordList}">
									<tr>
										<td colspan="7">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${packageconfigForm.recordList }" var="record"
										begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out
													value="${status.index+((packageconfigForm.paginatorPage - 1) * packageconfigForm.paginator.limit) + 1 }"></c:out></td>
											
											<%-- <td align="center"><c:out value="${record.year }"></c:out></td> --%>
											<td align="center"><c:out value="${record.vipName }"></c:out></td>
											<td class="center"><c:out value="${record.couponType == '1' ? '体验金' : '加息券'  }"></c:out></td>
											<td align="center"><c:out value="${record.couponName }"></c:out></td>
											<td class="center">
												<c:if test="${record.couponType == '1'}">
													<c:out value="${record.couponQuota }"></c:out>元
												</c:if>
												<c:if test="${record.couponType == '2'}">
													<c:out value="${record.couponQuota }"></c:out>%
												</c:if>
											</td>
											<td align="center"><c:out value="${record.couponQuantity }"></c:out></td>
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<shiro:hasPermission name="PACKAGECONFIG:MODIFY">
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
															<shiro:hasPermission name="PACKAGECONFIG:MODIFY">
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
						action="init" paginator="${packageconfigForm.paginator}"></hyjf:paginator>
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
				value="${packageconfigForm.paginatorPage}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/vip/packageconfig/packageconfig.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
