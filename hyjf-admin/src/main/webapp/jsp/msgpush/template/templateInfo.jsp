<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
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
<%-- 画面功能菜单设置开关 --%>
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="自动触发消息模版" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty msgPushTemplateForm.id ? '添加' : '修改'}"></c:set>
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul id="mainTabs" class="nav nav-tabs nav-justified">
					<li class="s-ellipsis active"><a href="#tabs_1" data-toggle="tab"><i class="fa fa-edit"></i> 编辑内容</a></li>
					<li><a href="#tabs_2" data-toggle="tab"><i class="fa fa-edit"></i> 定制发送</a></li>
				</ul>
			</div>
			<form id="mainForm" action="${empty msgPushTemplateForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" class="form-horizontal">
				<div class="tab-content">
					<!-- Start:基本信息 -->
					<div class="tab-pane fade in active" id="tabs_1">
						<div class="row">
							<!-- 表单左侧 -->
							<div class="col-xs-12 col-md-12">
								<input type="hidden" name="id" id="id" value="${msgPushTemplateForm.id }" /> <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input type="hidden" id="success" value="${success}" />
								<div class="form-group">
									<label class="col-xs-2 control-label" for="tagCode"> <span class="symbol required"></span>消息标签
									</label>
									<div class="col-xs-8">
										<select id="tagId" name="tagId" class="form-select2" style="width: 70%" datatype="*" data-placeholder="请选择项消息标签...">
											<option value=""></option>
											<c:forEach items="${templatePushTags }" var="tags" begin="0" step="1" varStatus="status">
												<option value="${tags.id }" tid="${tags.tagCode }" <c:if test="${tags.id eq msgPushTemplateForm.tagId}">selected="selected"</c:if>>
													<c:out value="${tags.tagName }"></c:out></option>
											</c:forEach>
										</select><input type="hidden" id="tagCode" name="tagCode" value="${msgPushTemplateForm.tagCode}"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label" for="templateCode"> <span class="symbol required"></span>消息编码
									</label>
									<div class="col-xs-8">
										<div class="col-xs-2" id="templateCodePanel0" ${(empty msgPushTemplateForm.tagId) ? '' : 'style="display:none;"'}>请选择消息标签</div>
										<div id="templateCodePanel1" ${(not empty msgPushTemplateForm.tagId) ? '' : 'style="display:none;"'}>
											<span class="col-xs-1 text-right" id="templateCode_pre" name="templateCode_pre"> ${msgPushTemplateForm.tagCode}_
											</span><input class="col-xs-4" type="text" placeholder="消息编码" id="templateCode" name="templateCode" value="${msgPushTemplateForm.templateCode}" errormsg="消息编码只能是英文！" datatype="/^[A-Za-z_]+$/" ajaxurl="checkAction?id=${msgPushTemplateForm.id}&templateCode=${msgPushTemplateForm.templateCode}" class="form-control" maxlength="30" />
											<hyjf:validmessage key="templateCode" label="消息编码"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label" for="templateTitle"> <span class="symbol required"></span>消息标题
									</label>
									<div class="col-xs-8">
										<input type="text" placeholder="消息标题" id="templateTitle" name="templateTitle" value="${msgPushTemplateForm.templateTitle}" class="form-control" maxlength="20" datatype="*1-20" errormsg="请输入消息标题！">
										<hyjf:validmessage key="templateTitle" label="消息标题"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label padding-top-5" for="templateImageUrl">消息图片</label>
									<div class="col-xs-8">
										<div class="fileinput fileinput-new picClass" data-provides="fileinput">
											<!-- 缺省图片 -->
											<div class="fileinput-new thumbnail">
												<img width="160" height="120" src="${fileDomainUrl}${msgPushTemplateForm.templateImageUrl}" alt="">
											</div>
											<!-- 显示图片 -->
											<div class="fileinput-preview fileinput-exists thumbnail"></div>
											<!-- 图片路径 -->
											<div class="purMargin">
												<input type="text" readonly="readonly" name="templateImageUrl" id="templateImageUrl" value="${msgPushTemplateForm.templateImageUrl}" placeholder="上传图片路径" />
											</div>
											<!-- 按钮管理 -->
											<div class="user-edit-image-buttons">
												<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span> <input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"> </span> <a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput" > <i class="fa fa-times"></i> 删除
												</a>
											</div>
											<hyjf:validmessage key="templateImageUrl" label="消息图片"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label" for="author"> <span class="symbol required"></span>通知内容:
									</label>
									<div class="col-xs-8">
										<textarea name="templateContent" id="templateContent" placeholder="文章内容" maxlength="4000" class="form-control tinymce" cols="10" rows="5" datatype="*" errormsg="通知内容必填！" errormsg="未填写通知内容">${msgPushTemplateForm.templateContent }</textarea>
										<hyjf:validmessage key="templateContent" label="消息标题"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group margin-bottom-0">
									<div class="col-xs-offset-2 col-xs-8">
										<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 提交保存</a> <a href="#tabs_2" class="btn btn-primary btn-o btn-wide pull-right fn-Next"> 下一步 <i class="fa fa-arrow-circle-right"></i>
										</a>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="tab-pane fade" id="tabs_2">
						<div class="row">
							<!-- 表单左侧 -->
							<div class="col-xs-12 col-md-12">
								
							  <div class="form-group">
								<label class="col-sm-2 control-label" for="projectType"><span class="symbol required"></span>推送终端：</label>
									<div class="col-sm-9 checkbox clip-check check-primary inline" style="padding-left: 15px;">
										<c:set var="tempStrs" value="${fn:split(msgPushTemplateForm.templateTerminal, ',')}" />
										<c:forEach items="${plats }" var="plat" begin="0" step="1" varStatus="status">
											<input type="checkbox" id="ct-${status.index }" datatype="*" errormsg="推送终端必选！" name="templateTerminal" value="${plat.nameCd}" 
											<c:forEach items="${tempStrs }" var="tempStr" begin="0" step="1"> <c:if test="${plat.nameCd eq tempStr}"> checked="checked"</c:if> </c:forEach>>
											<label for="ct-${status.index }"><c:out value="${plat.name }"></c:out></label>
										</c:forEach>
										<hyjf:validmessage key="templateTerminal" label="推送终端"></hyjf:validmessage>
									</div>
								</div>
								
								
								<div class="form-group">
									<label class="col-xs-2 control-label" for="templateAction"> <span class="symbol required" aria-required="true"></span>后续动作
									</label>
									<div class="col-xs-8">
										<select id="templateAction" name="templateAction" class="form-select2" style="width: 70%" datatype="*" data-placeholder="请选择后续动作...">
											<option value=""></option>
											<c:forEach items="${templateActions }" var="actions" begin="0" step="1" varStatus="status">
												<option value="${actions.nameCd }" <c:if test="${actions.nameCd eq msgPushTemplateForm.templateAction}">selected="selected"</c:if>>
													<c:out value="${actions.name }"></c:out></option>
											</c:forEach>
										</select>
										<div id="actionPanel0" ${(msgPushTemplateForm.templateAction eq '0') ? '' : 'style="display:none;"'}>
											<!-- 打开APP -->
										</div>
										<c:choose>
											<c:when test="${msgPushTemplateForm.id == null}">
												<div id="actionPanel1" ${(msgPushTemplateForm.templateAction eq '1')||(msgPushTemplateForm.templateAction eq '3') ? '' : 'style="display:none;"'}>
												<!-- 打开H5页面 -->
												<span class="symbol"></span><input type="text" placeholder="请输入http地址（全路径）" id="templateActionUrl1" name="templateActionUrl1" value="${msgPushTemplateForm.templateActionUrl1}" class="form-control" datatype="*" maxlength="100" ajaxurl="checkUrlAction">
												<hyjf:validmessage key="templateActionUrl1" label="http地址"></hyjf:validmessage>
												</div>
											</c:when>
											<c:otherwise>
												<div id="actionPanel1" ${(msgPushTemplateForm.templateAction eq '1') ? '' : 'style="display:none;"'}>
													<!-- 打开H5页面 -->
													<span class="symbol"></span><input type="text" placeholder="请输入http地址（全路径）" id="templateActionUrl1" name="templateActionUrl1" name="templateActionUrl1" value="${msgPushTemplateForm.templateActionUrl1}" class="form-control" datatype="*" maxlength="100" ajaxurl="checkUrlAction">
													<hyjf:validmessage key="templateActionUrl1" label="http地址"></hyjf:validmessage>
												</div>
												<div id="actionPanel3"  ${(msgPushTemplateForm.templateAction eq '3') ? '' : 'style="display:none;"'}>
													<!-- 打开微信页面 -->
													<span class="symbol"></span><input type="text" placeholder="请输入http地址（全路径）" id="templateActionUrl3" name="templateActionUrl3"  value="${msgPushTemplateForm.templateActionUrl3}" class="form-control" datatype="*" maxlength="100" ajaxurl="checkUrlAction">
													<hyjf:validmessage key="templateActionUrl3" label="http地址"></hyjf:validmessage>
												</div>
											</c:otherwise>
										</c:choose>
										<div id="actionPanel2" ${(msgPushTemplateForm.templateAction eq '2') ? '' : 'style="display:none;"'}>
											<!-- 指定原生页面 -->
											<span class="symbol"></span><select id="templateActionUrl2" name="templateActionUrl2" class="form-select2" style="width: 70%" datatype="*" data-placeholder="请选择项原生页面...">
												<option value=""></option>
												<c:forEach items="${naturePages }" var="pages" begin="0" step="1" varStatus="status">
													<option value="${pages.nameCd }" <c:if test="${pages.nameCd eq msgPushTemplateForm.templateActionUrl2}">selected="selected"</c:if>>
														<c:out value="${pages.name }"></c:out></option>
												</c:forEach>
											</select>
										</div>

									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label"> <span class="symbol"></span>发送时间:
									</label>
									<div class="col-xs-8">立即发送</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label"> <span class="symbol"></span>推送用户:
									</label>
									<div class="col-xs-8">自动触发</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label" for="status"> <span class="symbol required" aria-required="true"></span>状态
									</label>
									<div class="col-xs-8">
										<div class="radio clip-radio radio-primary ">
											<input type="radio" id="statusOn" name="status" datatype="*" value="1" class="event-categories" ${msgPushTemplateForm.status == '1' ? 'checked' : ''}> <label for="statusOn"> 启用 </label> <input type="radio" id="statusOff" name="status" datatype="*" value="2" class="event-categories" ${msgPushTemplateForm.status == '2' ? 'checked' : ''}> <label for="statusOff"> 禁用 </label>
										</div>
										<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group margin-bottom-0">
									<div class="col-xs-offset-2 col-xs-8">
										<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 提交保存</a> <a href="#tabs_1" class="btn btn-primary btn-o btn-wide pull-right fn-Next"><i class="fa fa-arrow-circle-left"></i> 上一步 </a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
.purMargin {
	margin: 8px 0;
}

.purMargin input {
	width: 200px;
}
</style>
	</tiles:putAttribute>

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		<script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>

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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/msgpush/template/templateInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
