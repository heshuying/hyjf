<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="借款盖章用户" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty loancoverForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty loancoverForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${loancoverForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<input type="hidden" name="status" id="status"value="${loancoverForm.status}" /> 

						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>用户类型
							</label>
							<div class="col-xs-9 admin-select">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" name="idType" id="idTypeQ" datatype="*"value="1" class="event-categories" ${loancoverForm.idType == '1' ? 'checked' : ''}>
									<label for="idTypeQ" id="idTypeQQ">企业</label> 
									<input type="radio" id="idTypeG" name="idType" datatype="*" value="0" class="event-categories" ${loancoverForm.idType == '0' ? 'checked' : ''}>
									<label for="idTypeG"id="idTypeGG">个人 </label>
								</div>
							</div>
						</div>



						<div class="form-group">
							<label  class="col-xs-3 control-label padding-top-5" for="name">
								<span class="symbol required"></span><font id="namee">企业名称</font>
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="请输入姓名或企业名称" id="name" 
									name="name" value="${loancoverForm.name }" maxlength="50" class="form-control" datatype="*1-50"
									errormsg="请输入姓名或企业名称，长度1~50个字符！">
								<hyjf:validmessage key="name" label="姓名"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="mobile">
								<span class="symbol required"></span>手机号 </label>
							<div class="col-xs-9">
								<input type="text" placeholder="手机号" id="mobile" name="mobile"
									value="${loancoverForm.mobile}" class="form-control" datatype="n11-11" maxlength="11" errormsg="手机号">
								<hyjf:validmessage key="mobile" label="手机号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="email">
								邮箱 </label>
							<div class="col-xs-9">
								<input type="text" placeholder="邮箱" id="email" name="email"
									value="${loancoverForm.email}" class="form-control" maxlength="50" errormsg="邮箱">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="idNo">
								<span class="symbol required"></><font id="nameee">统一社会信用代码 </font>
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="请输入统一社会信用代码或身份证" id="idNo"
									name="idNo" value="${loancoverForm.idNo}" class="form-control" maxlength="20" datatype="s1-50"
									errormsg="请输入统一社会信用代码或身份证">
								<hyjf:validmessage key="idNo" label="身份证号代码"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-2 col-xs-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 确认</a> <a
									class="btn btn-o btn-primary fn-Cancel"><i
									class="fa fa-close"></i> 取 消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link
			href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css"
			rel="stylesheet" media="screen">
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
		<script
			src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
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
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/users/loancover/loancoverInfo.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
