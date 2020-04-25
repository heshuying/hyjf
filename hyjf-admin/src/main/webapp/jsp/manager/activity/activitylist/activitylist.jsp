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


<shiro:hasPermission name="activitylist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="活动设置" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">活动设置</h1>
			<span class="mainDescription">本功能可以增加修改删除活动。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${activitylistForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${activitylistForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
 									<shiro:hasPermission name="activitylist:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加新活动">添加 <i class="fa fa-plus"></i></a>
 									</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 活动列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
								<col style="width:55px;" />
								<col />
								<col style="width:93px;" />
								<col style="width:93px;" />
							</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">活动编号</th>
										<th class="center">活动名称</th>
										<th class="center">主图</th>
										<th class="center">二维码</th>
										<th class="center">平台</th>
										<th class="center">活动时间</th>
										<th class="center">状态</th>
										<th class="center">添加时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty activitylistForm.forBack}">
											<tr>
												<td colspan="9">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${activitylistForm.forBack }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((activitylistForm.paginatorPage - 1) * activitylistForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.id }"></c:out></td>
													<td><c:out value="${record.title }"></c:out></td>
													<td class="center">
														<a href="${fileDomainUrl}${!empty record.imgPc?record.imgPc : !empty record.imgApp?record.imgApp : !empty record.imgWei?record.imgWei:record.img}" target="_blank" class="thumbnails-wrap"
																data-toggle="popover" data-placement="right" data-trigger="hover" data-html="true"
																data-title="图片预览" data-content="<img src='${fileDomainUrl}${!empty record.imgPc?record.imgPc : !empty record.imgApp?record.imgApp : !empty record.imgWei?record.imgWei:record.img}' style='max-height:350px;'/>">
															<img src="${fileDomainUrl}${!empty record.imgPc?record.imgPc : !empty record.imgApp?record.imgApp : !empty record.imgWei?record.imgWei:record.img}" />
														</a>
													</td>
													<td class="center">
														<a href="${fileDomainUrl}${record.qr}" target="_blank" class="thumbnails-wrap no-border"
																data-toggle="popover" data-placement="right" data-trigger="hover" data-html="true"
																data-title="二维码预览" data-content="<img src='${fileDomainUrl}${record.qr}' style='max-height:350px;'/>">
															<i class="fa fa-qrcode" style="font-size:21px;"></i>
														</a>
													</td>
													<td>${record.platform }</td>
													<td class="center"><c:out value="${record.startTime } ~ ${record.endTime }"></c:out></td>
													<td class="center"><c:out value="${record.status }"></c:out></td>
													<td class="center"><c:out value="${record.startCreate }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="activitylist:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="activitylist:INFO">
																<c:if test="${!empty record.urlBackground}">
																	<c:if test="${fn:contains(record.urlBackground,'?')}">
																		<a class="btn btn-transparent btn-xs tooltips fn-Info" action="${record.urlBackground}&startTime=${record.startTime}&endTime=${record.endTime}&activityId=${record.id}"
																			 data-toggle="tooltip" tooltip-placement="top" data-original-title="详情"><i class="fa fa-file-text"></i></a>
																	</c:if>
																	<c:if test="${!fn:contains(record.urlBackground,'?')}">
																		<a class="btn btn-transparent btn-xs tooltips fn-Info" action="${record.urlBackground}?startTime=${record.startTime}&endTime=${record.endTime}&activityId=${record.id}"
																			 data-toggle="tooltip" tooltip-placement="top" data-original-title="详情"><i class="fa fa-file-text"></i></a>
																	</c:if>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="activitylist:DELETE">
																<a class="btn btn-transparent btn-xs tooltips fn-Delete"
																	data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
<%-- 															<c:if test="${record.id == activityId }"> --%>
<%-- 																<shiro:hasPermission name="activitylist:INFO"> --%>
<%-- 																	<a class="fn-Info" href ="${webRoot}/activity/returncash/init" ><i class="fa fa-file-text"></i></a> --%>
<%-- 																</shiro:hasPermission> --%>
<%-- 															</c:if> --%>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light"
																	role="menu">
																	<shiro:hasPermission name="activitylist:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">修改</a>
																		</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="activitylist:INFO">
																		<c:if test="${!empty record.urlBackground}">
																			<li>
																				<c:if test="${fn:contains(record.urlBackground,'?')}">
																					<a class="fn-Info" action="${record.urlBackground}&startTime=${record.startTime}&endTime=${record.endTime}&activityId=${record.id}" >详情</a>
																				</c:if>
																				<c:if test="${!fn:contains(record.urlBackground,'?')}">
																					<a class="fn-Info" action="${record.urlBackground}?startTime=${record.startTime}&endTime=${record.endTime}&activityId=${record.id}" >详情</a>
																				</c:if>
																			</li>
																		</c:if>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="activitylist:DELETE">
																		<li><a class="fn-Delete" data-id="${record.id }">删除</a>
																		</li>
																	</shiro:hasPermission>
<%-- 																	<c:if test="${record.id == activityId }"> --%>
<%-- 																		<shiro:hasPermission name="activitylist:INFO"> --%>
<!-- 																		<li> -->
<%-- 																			<a class="fn-Info" href ="${webRoot}/activity/returncash/init" >详情</a> --%>
<!-- 																		</li> -->
<%-- 																		</shiro:hasPermission> --%>
<%-- 																	</c:if> --%>
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
								action="init" paginator="${activitylistForm.paginator}"></hyjf:paginator>
							<br />
							<br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${activitylistForm.paginatorPage}" />
			<div class="form-group">
				<label>活动名称</label>
						<input type="text" name="title" class="form-control input-sm underline" value="${activitylistForm.title}" />
			</div>
			<div class="form-group">
				<label>活动时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="startTime" id="startTime" class="form-control underline"
						value="${activitylistForm.startTime}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="endTime" id="endTime"
						class="form-control underline" value="${activitylistForm.endTime}" />
				</div>
			</div>
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="startCreate" id="startCreate" class="form-control underline"
						value="${activitylistForm.startCreate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="endCreate" id="endCreate"
						class="form-control underline"
						value="${activitylistForm.endCreate}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/activity/activitylist/activitylist.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
