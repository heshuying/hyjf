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

<shiro:hasPermission name="projecttype:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="项目类型" />
		
		<%-- 画面功能路径 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">项目类型</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab"> 
				<shiro:hasPermission name="projecttype:VIEW">
					<li class="active"><a href="${webRoot}/manager/config/projecttype/init">项目类型</a></li>
				</shiro:hasPermission>
		      	<shiro:hasPermission name="finmanchargenew:VIEW">
		      		<li><a href="${webRoot}/manager/config/finmanchargenew/init">费率配置</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="borrowflow:VIEW">
		      		<li><a href="${webRoot}/manager/config/borrowflow/init">流程配置</a></li>
		      	</shiro:hasPermission>
				<shiro:hasPermission name="sendtype:VIEW">
					<li><a href="${webRoot}/manager/config/sendtype/init">发标/复审</a></li>
				</shiro:hasPermission>
		    </ul>
	 		<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${projecttypeForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${projecttypeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="projecttype:ADD">
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
									data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
						</shiro:hasPermission>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
					</div>
					<br/>
					<%-- 角色列表一览 --%>
					<table id="equiList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width:55px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">项目类型</th>
								<th class="center">项目编号</th>
								<th class="center">名称</th>
								<th class="center">项目编号前缀</th>
								<th class="center">出借用户</th>
								<th class="center">出借范围(下限)</th>
								<th class="center">出借范围(上限)</th>
								<th class="center">递增金额</th> 
								<th class="center">支持还款方式</th>
								<th class="center">可用券</th>
								<th class="center">加息状态</th>
								<th class="center">状态</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="userTbody">
							<c:choose>
								<c:when test="${empty list}">
									<tr><td colspan="12">暂时没有数据记录</td></tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${list }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center">${(projecttypeForm.paginatorPage-1)*projecttypeForm.paginator.limit+status.index+1 }</td>
											<td class="center"><c:out value="${record.borrowProjectType }"></c:out></td>
											<td class="center"><c:out value="${record.borrowCd }"></c:out></td>
											<td><c:out value="${record.borrowName }"></c:out></td>
											<td class="center"><c:out value="${record.borrowClass }"></c:out></td>
											<td><c:out value="${record.investUserType }"></c:out></td>
											<td align="right"><fmt:formatNumber value="${record.investStart }" pattern="#,##0.00#" />元</td>
											<td align="right"><fmt:formatNumber value="${record.investEnd }" pattern="#,##0.00#" />元</td>
											<td align="right"><fmt:formatNumber value="${record.increaseMoney }" pattern="#,##0.00#" />元</td>
											<td><c:out value="${record.repayName }"></c:out></td>
											<td><c:if test="${record.interestCoupon eq '1' }">优惠券</c:if>
												<c:if test="${record.interestCoupon eq '1' and record.tasteMoney eq '1' }">,</c:if>
												<c:if test="${record.tasteMoney eq '1' }">体验金</c:if>
											</td>
											<td><c:if test="${record.increaseInterestFlag eq '1' }">开启</c:if>
												<c:if test="${( empty record.increaseInterestFlag ) or record.increaseInterestFlag eq '0' }">禁用</c:if>
											</td>
											<td class="center"><c:out value="${record.status }"></c:out></td>
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<shiro:hasPermission name="projecttype:MODIFY">
														<a class="btn btn-transparent btn-xs fn-Modify" data-borrowcd="${record.borrowCd }"
																data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
													</shiro:hasPermission>
													<shiro:hasPermission name="projecttype:DELETE">
														<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-borrowcd="${record.borrowCd }"
																data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
													</shiro:hasPermission>
												</div>
												<div class="visible-xs visible-sm hidden-md hidden-lg">
													<div class="btn-group" dropdown="">
														<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
															<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
														</button>
														<ul class="dropdown-menu pull-right dropdown-light" role="menu">
															<shiro:hasPermission name="projecttype:MODIFY">
																<li>
																	<a class="fn-Modify" data-borrowCd="${record.borrowCd }">修改</a>
																</li>
															</shiro:hasPermission>
															<shiro:hasPermission name="projecttype:DELETE">
																<li>
																	<a class="fn-Delete" data-borrowCd="${record.borrowCd }">删除</a>
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
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${projecttypeForm.paginator}"></hyjf:paginator>
					<br/><br/>
					</div>
				</div>
			</div>
		</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name=borrowCd id="borrowCd" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${projecttypeForm.paginatorPage}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/projecttype/projecttype.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
