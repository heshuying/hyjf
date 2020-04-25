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


<shiro:hasPermission name="landingpage:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="着落页管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">着落页管理</h1>
			<span class="mainDescription">本功能可以增加修改删除着落页信息。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${landingPageForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${landingPageForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
 									<shiro:hasPermission name="landingpage:ADD"> 
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加着落页">添加 <i class="fa fa-plus"></i></a>
 									</shiro:hasPermission> 
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
										data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
										data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
										data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 列表 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
								<col style="width:55px;" />
								<col />
								<col />
								<col />
								<col style="width:93px;" />
								<col style="width:210px;" />
							</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">着落页名称</th>
										<th class="center">渠道</th>
										<th class="center">地址</th>
										<th class="center">二维码</th>
										<th class="center">备注</th>
										<th class="center">添加时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty landingPageForm.recordList}">
											<tr>
												<td colspan="8">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${landingPageForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((landingPageForm.paginatorPage - 1) * landingPageForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.pageName }"></c:out></td>
													<td class="center"><c:out value="${record.channelName }"></c:out></td>
													<td><a href="${record.pageUrl }" target="_blank"><c:out value="${record.pageUrl }"></c:out></a></td>
													<td class="center">
													<c:if test="${record.codeUrl != ''}">
														<a href="${fileDomainUrl}${record.codeUrl}" target="_blank" class="thumbnails-wrap"
																data-toggle="popover" data-placement="right" data-trigger="hover" data-html="true"
																data-title="图片预览" data-content="<img src='${fileDomainUrl}${record.codeUrl}' style='max-height:350px;'/>">
															<img src="${fileDomainUrl}${record.codeUrl}" />
														</a>
													</c:if>
													</td>
													<td class="center"><c:out value="${record.remark }"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="landingpage:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="修改"><i
																	class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="landingpage:DELETE">
																<a class="btn btn-transparent btn-xs tooltips fn-Delete"
																	data-id="${record.id }" data-toggle="tooltip"
																	tooltip-placement="top" data-original-title="删除"><i
																	class="fa fa-times fa fa-white"></i></a>
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
																	<shiro:hasPermission name="landingpage:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">修改</a>
																		</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="landingpage:DELETE">
																		<li><a class="fn-Delete" data-id="${record.id }">删除</a>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${landingPageForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${landingPageForm.paginatorPage}" />
			<div class="form-group">
				<label>着落页名称</label> <input type="text" name="pageNameSrch" class="form-control input-sm underline" value="${landingPageForm.pageNameSrch}" maxlength="20" />
			</div>
			<div class="form-group">
				<label>渠道</label> <input type="text" name="channelNameSrch" class="form-control input-sm underline" value="${landingPageForm.channelNameSrch}" maxlength="20" />
			</div>
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startTime" id="start-date-time" class="form-control underline" value="${landingPageForm.startTime}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endTime" id="end-date-time" class="form-control underline" value="${landingPageForm.endTime}" />
				</div>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/content/landingpage/landingpage.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
