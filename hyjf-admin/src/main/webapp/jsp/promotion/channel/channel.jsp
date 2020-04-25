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

<shiro:hasPermission name="channel:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="推广管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">推广管理</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="channel:SEARCH">
							<div class="well">
								<c:set var="jspPrevDsb" value="${borrowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${borrowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="channel:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="channel:IMPORT">
									<span class="btn btn-o btn-primary btn-sm hidden-xs fn-Import fileinput-button" data-toggle="tooltip" data-placement="bottom" data-original-title="导入">导入 <i class="fa fa-upload"></i>
										<input type="file" name="file" class="fileupload">
									</span>
								</shiro:hasPermission>
								<shiro:hasPermission name="channel:DOWNLOAD_TEMPLATE">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Download"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下载模板">下载模板 <i class="fa fa-download"></i></a>
								</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							</shiro:hasPermission>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">渠道</th>
										<th class="center">推广方式</th>
										<th class="center">推广单元</th>
										<th class="center">推广计划</th>
										<th class="center">关键字</th>
										<th class="center">推荐人</th>
										<th class="center">推荐人用户名</th>
										<th class="center">状态</th>
										<th class="center">备注</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="11">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((channelForm.paginatorPage - 1) * channelForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.utmSource }"></c:out></td>
													<td><c:out value="${record.utmMedium }"></c:out></td>
													<td><c:out value="${record.utmContent }"></c:out></td>
													<td><c:out value="${record.utmCampaign }"></c:out></td>
													<td><c:out value="${record.utmTerm }"></c:out></td>
													<td><c:out value="${record.utmReferrer }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td class="center"><c:out
															value="${record.status == '0' ? '启用' : '禁用'  }"></c:out></td>
													<td><c:out value="${record.remark }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="channel:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-utmid="${record.utmId }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="channel:DELETE">
																<a class="btn btn-transparent btn-xs fn-Delete" data-utmid="${record.utmId }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="channel:COPY">
																<a class="btn btn-transparent btn-xs fn-Copy" data-url="${record.url }" data-toggle="tooltip" tooltip-placement="top" data-original-title="复制链接">复制链接</a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown"> <i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="channel:MODIFY">
																		<li><a class="fn-Modify" data-utmid="${record.utmId }">修改</a></li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="channel:DELETE">
																		<li><a class="fn-Delete" data-utmid="${record.utmId }">删除</a></li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="channel:COPY">
																		<li><a class="fn-Copy" data-url="${record.url }">复制链接</a></li>
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
								action="init" paginator="${channelForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="utmId" id="utmId" /> 
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${channelForm.paginatorPage}" />
			<div class="form-group">
				<label>关键字</label>
				<input type="text" name="utmTermSrch" class="form-control input-sm underline" value="${borrowForm.utmTermSrch}" />
			</div>
			<div class="form-group">
				<label>渠道</label>
				<select name="sourceIdSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${utmPlatList }" var="record" begin="0" step="1" varStatus="status">
						<option value="${record.sourceId }"
							<c:if test="${record.sourceId eq channelForm.sourceIdSrch}">selected="selected"</c:if>>
							<c:out value="${record.sourceName }"></c:out></option>
					</c:forEach>
				</select>
			</div>
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
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/zeroclipboard/ZeroClipboard.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
			<!-- The Templates plugin is included to render the upload/download listings -->
<%-- 		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/tmpl.min.js"></script> --%>
			<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/load-image.all.min.js"></script>
			<!-- The Canvas to Blob plugin is included for image resizing functionality -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/canvas-to-blob.min.js"></script>
			<!-- blueimp Gallery script -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.blueimp-gallery.min.js"></script>
			<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.iframe-transport.js"></script>
			<!-- The basic File Upload plugin -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload.js"></script>
			<!-- The File Upload processing plugin -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-process.js"></script>
			<!-- The File Upload image preview & resize plugin -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-image.js"></script>
			<!-- The File Upload audio preview plugin -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-audio.js"></script>
			<!-- The File Upload video preview plugin -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-video.js"></script>
			<!-- The File Upload validation plugin -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-validate.js"></script>
			<!-- The File Upload user interface plugin -->
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-ui.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/promotion/channel/channel.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
