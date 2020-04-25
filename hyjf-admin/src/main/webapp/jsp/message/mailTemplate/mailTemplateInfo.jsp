<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="邮件模板管理" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty infoForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body" style="margin:0 auto">
				<div class="panel-scroll height-430">
				<form id="mainForm" action="${empty infoForm.id ? 'addAction' : 'updateAction'}"
						method="post"  role="form" class="form-horizontal" >
					<%-- 文章列表一览 --%>
					<input type="hidden" name="id" id="id" value="${infoForm.id }" />
					<input type="hidden" name="mailValue" id="mailValue" value="${infoForm.mailValue}" />
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<div class="form-group">
						<label class="col-xs-2 control-label" for="type"> 
							<span class="symbol required"></span>模板名称
						</label>
						<div class="col-xs-10">
							<c:choose>
								<c:when test="${empty infoForm.id}">
									<input type="text" placeholder="模板名称" id="mailName" name="mailName"  class="form-control" maxlength="20"
										datatype="s1-20" errormsg="模板名称的长度不能大于20个字符！" >
									<hyjf:validmessage key="mailName" label="模板名称 "></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="模板名称" id="mailName" name="mailName" class="form-control" value="${infoForm.mailName}" >
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label" for="title"> 
							<span class="symbol required"></span>模板标识
						</label>
						<div class="col-xs-10">
							<c:choose>
								<c:when test="${empty infoForm.id}">
									<input type="text" placeholder="模板标识" id="templateCode" name="templateCode"  class="form-control" maxlength="20"
										datatype="s1-20" errormsg="模板标识的长度不能大于20个字符！" >
									<hyjf:validmessage key="templateCode" label="模板标识 "></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<input type="text" placeholder="模板标识" id="templateCode" name="templateCode" value="${infoForm.templateCode}" disabled>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label" for="author"> 
							<span class="symbol required"></span>模板内容
						</label>
						<div class="col-xs-9">
							<c:choose>
								<c:when test="${empty infoForm.id}">
									<textarea rows="15" cols="15" placeholder="模板内容" id="mailContent" name="mailContent"  class="form-control tinymce" maxlength="400" datatype="*" errormsg="问题名称的长度不能大于400个字符！"></textarea>
									<hyjf:validmessage key="mailContent" label="模板内容 "></hyjf:validmessage>
								</c:when>
								<c:otherwise>
									<textarea rows="15" cols="15" placeholder="模板内容" id="mailContent" name="mailContent"  class="form-control tinymce" maxlength="400" datatype="*" errormsg="问题名称的长度不能大于400个字符！">${infoForm.mailContent}</textarea>
								</c:otherwise>
							</c:choose>
							可选参数： 姓名: [val_name] ；性别: [val_sex] ；金额: [val_amount] ；日期: [val_date] ；余额: [val_balance] ；出借利率: [val_rate] ；项目名称: [val_title]；借款期限: [val_limit]；笔数:[val_account]；智投服务:[val_plan_nid]；
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-2 col-xs-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
		<!-- The Templates plugin is included to render the upload/download listings -->
		<%-- 		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/tmpl.min.js"></script> --%>
		<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/load-image.all.min.js"></script>
		<!-- The Canvas to Blob plugin is included for image resizing functionality -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/canvas-to-blob.min.js"></script>
		<!-- blueimp Gallery script -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.blueimp-gallery.min.js"></script>
		<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.iframe-transport.js"></script>
		<!-- The basic File Upload plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload.js"></script>
		<!-- The File Upload processing plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-process.js"></script>
		<!-- The File Upload image preview & resize plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-image.js"></script>
		<!-- The File Upload audio preview plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-audio.js"></script>
		<!-- The File Upload video preview plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-video.js"></script>
		<!-- The File Upload validation plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-validate.js"></script>
		<!-- The File Upload user interface plugin -->
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-ui.js"></script>
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/message/mailTemplate/mailTemplateInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
