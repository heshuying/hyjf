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

<shiro:hasPermission name="borrowflow:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="项目流程配置-新" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">项目流程配置-新</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<shiro:hasPermission name="projecttype:VIEW">
					<li><a href="${webRoot}/manager/config/projecttype/init">项目类型</a></li>
				</shiro:hasPermission>

		      	<shiro:hasPermission name="finmanchargenew:VIEW">
		      		<li><a href="${webRoot}/manager/config/finmanchargenew/init">费率配置</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="borrowflow:VIEW">
		      		<li class="active"><a href="${webRoot}/manager/config/borrowflow/init">流程配置</a></li>
		      	</shiro:hasPermission>
				<shiro:hasPermission name="sendtype:VIEW">
					<li><a href="${webRoot}/manager/config/sendtype/init">发标/复审</a></li>
				</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${borrowflowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${borrowflowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="borrowflow:ADD">
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
								<th class="center">项目类型</th>
								<th class="center">资产来源</th>
								<th class="center">产品类型</th>
								<!-- <th class="center">是否关联计划</th> -->
								<th class="center">自动录入标的</th>
								<%--<th class="center">项目申请人</th>--%>
								<!-- <th class="center">担保机构</th> -->
								<th class="center">银行备案</th>
								<%--<th class="center">确认保证金</th>--%>
								<th class="center">初审</th>
								<th class="center">复审</th>
								<%--<th class="center">拆标自动发标时间间隔（分钟）</th>--%>
								<%--<th class="center">自动复审时间间隔（分钟）</th>--%>
								<th class="center">状态</th>
								<th class="center">说明</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty borrowflowForm.recordList}">
									<tr>
										<td colspan="17">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${borrowflowForm.recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((borrowflowForm.paginatorPage - 1) * borrowflowForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.projectName }"></c:out></td>
											<td class="center"><c:out value="${record.instName }"></c:out></td>
											<td class="center"><c:out value="${record.assetTypeName }"></c:out></td>
											<%-- <td class="center"><c:out value="${record.isAssociatePlan == '0' ? '否' : '是' }"></c:out></td> --%>
											<td class="center"><c:out value="${record.autoAdd == '0' ? '否' : '是'  }"></c:out></td>
											<%--<td class="center"><c:out value="${record.applicant }"></c:out></td>--%>
											<%-- <td class="center"><c:out value="${record.repayOrgName }"></c:out></td> --%>
											<td class="center"><c:out value="${record.autoRecord == '0' ? '手动' : '自动' }"></c:out></td>
											<%--<td class="center"><c:out value="${record.autoBail == '0' ? '手动' : '自动' }"></c:out></td>--%>
											<td class="center"><c:out value="${record.autoAudit == '0' ? '手动' : '自动' }"></c:out></td>
											<td class="center"><c:out value="${record.autoReview == '0' ? '手动' : '自动' }"></c:out></td>
											<%--<td class="center"><c:out value="${record.autoSendMinutes }"></c:out></td>--%>
											<%--<td class="center"><c:out value="${record.autoReviewMinutes }"></c:out></td>--%>
											<td class="center"><c:out value="${record.status }"></c:out></td>
											<td class="center"><c:out value="${record.remark }"></c:out></td>
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<shiro:hasPermission name="borrowflow:MODIFY">
														<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
													</shiro:hasPermission>
													<shiro:hasPermission name="borrowflow:DELETE">
														<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
													</shiro:hasPermission>
												</div>
												<div class="visible-xs visible-sm hidden-md hidden-lg">
													<div class="btn-group" dropdown="">
														<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
															<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
														</button>
														<ul class="dropdown-menu pull-right dropdown-light" role="menu">
															<shiro:hasPermission name="borrowflow:MODIFY">
																<li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
															</shiro:hasPermission>
															<shiro:hasPermission name="borrowflow:DELETE">
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
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${borrowflowForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="borrowflow:SEARCH">
				<input type="hidden" name="id" id="id" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${borrowflowForm.paginatorPage}" />
				<!-- 查询条件 -->
				<div class="form-group">
					<label>项目类型</label>
					<select name="borrowCdSrch" id="borrowCdSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowProjectTypeList }" var="projectType" begin="0" step="1">
							<option value="${projectType.borrowCd }"
								<c:if test="${projectType.borrowCd eq borrowflowForm.borrowCdSrch}">selected="selected"</c:if>>
								<c:out value="${projectType.borrowName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				
				<div class="form-group">
					<label>资产来源</label>
					<select name="instCodeSrch" id="instCodeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
							<option value="${inst.instCode }"
								<c:if test="${inst.instCode eq borrowflowForm.instCodeSrch}">selected="selected"</c:if>>
								<c:out value="${inst.instName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>产品类型</label>
					<select name="assetTypeSrch" id="assetTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${assetTypeList }" var="assetType" begin="0" step="1">
							<option value="${assetType.assetType }"
								<c:if test="${assetType.assetType eq borrowflowForm.assetTypeSrch}">selected="selected"</c:if>>
								<c:out value="${assetType.assetTypeName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>状态</label>
					<select name="statusSrch" id="statusSrch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${statusList }" var="status" begin="0" step="1">
							<option value="${status.nameCd }"
								<c:if test="${status.nameCd eq borrowflowForm.statusSrch}">selected="selected"</c:if>>
								<c:out value="${status.name }"></c:out>
							</option>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/borrow/borrowflow/borrowflow.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
