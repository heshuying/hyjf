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

<shiro:hasPermission name="activitylist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="签到活动" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">签到活动</h1>
			<span class="mainDescription">本功能可以查看。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="activitylist:VIEW">
				      		<li class="active"><a href="${webRoot}/manager/activity/actsignin/init">活动一(元旦活动)</a></li>
				      	</shiro:hasPermission>
						<shiro:hasPermission name="activitylist:VIEW">
							<li><a href="${webRoot}/manager/activity/ten/actQuestion/init">活动二</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="activitylist:VIEW">
							<li><a href="${webRoot}/manager/activity/actten/tenderreward/init">活动三</a></li>
						</shiro:hasPermission>
				    </ul>
					<div class="tab-content">
						<div class="tab-pane fade in active">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${actsigninForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${actsigninForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
<%-- 								<shiro:hasPermission name="banksetting:ADD"> --%>
<!-- 									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add" data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a> -->
<%-- 								</shiro:hasPermission> --%>
<!-- 								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出">导出<i class="fa fa-plus"></i></a> -->
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
										<th class="center">用户名</th>
										<th class="center">姓名</th>
										<th class="center">手机号</th>
										<th class="center">累计签到次数</th>
										<th class="center">最后签到时间</th>
										<th class="center">奖励数量</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty actsigninForm.recordList}">
											<tr>
												<td colspan="6">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${actsigninForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((actsigninForm.paginatorPage - 1) * actsigninForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.truename }"></c:out></td>
													<td class="center"><c:out value="${record.mobile  }"></c:out></td>
													<td class="center"><c:out value="${record.currentSignNum }"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
													<td class="center"><c:out value="${record.remark }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="activitylist:INFO">
																		<a class="btn btn-transparent btn-xs tooltips fn-Info" action="/manager/activity/actsignin/infoAction.do?userId=${record.userId}"
																			 data-toggle="tooltip" tooltip-placement="top" data-original-title="详情"><i class="fa fa-file-text"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="activitylist:INFO">
																			<li>
																					<a class="fn-Info"  action="/manager/activity/actsignin/infoAction.do?userId=${record.userId}" >
																					详情</a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${actsigninForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" id="ids" name="ids" value="${actsigninForm.ids }" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${actsigninForm.paginatorPage}" />
			
			<div class="form-group">
			<label>用户名</label> <input type="text" name="userName" id="userName"
				class="form-control input-sm underline" value="${actsigninForm.userName}" />
				<input type="hidden" name="remark" id="remark" value="${actsigninForm.remark}" />
			</div>
			<div class="form-group">
				<label>手机号</label> <input type="text" name="mobile" id="mobile"
				class="form-control input-sm underline" value="${actsigninForm.mobile}" />
			</div>
			<div class="form-group">
				<label>奖励数量</label>
				<select  id="select" class="form-control underline form-select2"  onchange="document.getElementById('remark').value=document.getElementById('select').value">
					<option value=""></option>
						<option value="0"
							<c:if test="${0 eq actsigninForm.remark}">selected="selected"</c:if>>
							<c:out value="0"></c:out>
						</option>
						<option value="1"
							<c:if test="${1 eq actsigninForm.remark}">selected="selected"</c:if>>
							<c:out value="1"></c:out>
						</option>
						<option value="2"
							<c:if test="${2 eq actsigninForm.remark}">selected="selected"</c:if>>
							<c:out value="2"></c:out>
						</option>
						<option value="3"
							<c:if test="${3 eq actsigninForm.remark}">selected="selected"</c:if>>
							<c:out value="3"></c:out>
						</option>
				</select>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/actsignin/actsignin.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
