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
	<tiles:putAttribute name="pageTitle" value="添加智投配置" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<!-- 是否传id来判断新增还是修改 -->
		<c:set var="jspEditType"
			value="${empty allocationengineForm.id ? '添加' : '修改'}">
		</c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<%-- <form id="mainForm" action="${empty allocationengineForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" class="form-horizontal"> --%>
					<!-- 不同的action -->
					<form id="mainForm" action="${empty allocationengineForm.id ? 'insertConfigAction' : 'updateConfigActionInfo'}" method="post" role="form" class="">
						
						<%-- 配置计划 --%>
						<input type="hidden" name="id" id="id" value="${allocationengineForm.id }" /> 
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> 
						<input type="hidden" id="success" value="${success}" />
						<input type="hidden" id="planNidSrch" name = "planNidSrch"value="${allocationengineForm.planNid}" />
						<input type="hidden" id="planName" name = "planName"value="${allocationengineForm.planName}" />
	
						<input type="hidden" name="labelName" id="labelName" value="${labelName }" /> 
							           
		                 	<div class="form-group">
								<label class="control-label padding-top-5" for="webname"> <span class="symbol required"></span>智投编号:
								</label>
								<div style="width:400px">
										<input type="text"   id="planNid" name="planNid" value="<c:out value="${allocationengineForm.planNid}" />"   
									class="form-control" datatype="*1-20"  maxlength="20" ajaxurl="isExistsPlaneNumber" >
								<span class="Validform_checktip"></span></div>
							</div>
							
							<%-- <hyjf:validmessage key="transferTimeSort" label="债转时间排序"></hyjf:validmessage> --%>
						</div>
						<div class="form-group">
							<label class=" control-label" for="labelSort"> <span
								class="symbol required"></span>标签排序
							</label>
							<div>
								<input type="text" placeholder="请填写优先级" id="labelSort" name="labelSort" value="${allocationengineForm.labelSort}"
									class="form-control" datatype="s1-20" nullmsg="未填写标签排序" errormsg="输入的标签名排序不正确" style="width:100px;display:inline-block">
									<%-- <hyjf:validmessage key="labelSort" label="标签排序"></hyjf:validmessage> --%>
								<%-- <hyjf:validmessage key="webname" label="名称"></hyjf:validmessage> --%>
							</div>
							
						</div>
						
						<p style='color:red;'>备注：优先级填写数字，数字越大排序越高；已使用过的数字不能重复使用。</p>
						
						
						<div class="form-group">
							<label class="control-label"> <span
								class="symbol required"></span>状态 
							</label>
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="statusOn" name="labelStatus" datatype="*" value="0" class="event-categories"
										${allocationengineForm.labelStatus eq 0 ? 'checked' : ''}> 
										<label for="statusOn"> 停用 </label> 
										
										<input type="radio" id="statusOff" name="labelStatus" datatype="*" value="1" class="event-categories"
										${allocationengineForm.labelStatus eq 1 ? 'checked' : ''}> <label
										for="statusOff"> 启用 </label>
								</div>
							
							<hyjf:validmessage key="configStatus" label="状态"></hyjf:validmessage>
						</div>
						
						
						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-2 col-xs-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认 </a> 
								<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消 </a>
							</div>
						</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
			.purMargin{
				margin:8px 0;
			}
			.purMargin input{
				width:200px;
			}
		</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		<script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
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
		<script type="text/javascript"src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		<script type='text/javascript'src="${webRoot}/jsp/manager/allocationengine/planconfigChange.js?v=853926"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
