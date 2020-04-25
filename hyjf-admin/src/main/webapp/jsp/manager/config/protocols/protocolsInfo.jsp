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
	<tiles:putAttribute name="pageTitle" value="添加协议模板" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<style>
			.panel-title { font-family: "微软雅黑" }
			.admin-select .select2-container {
				width: 100% !important;
			}
			.admin-select .select2-container--default .select2-selection--single {
				border-radius: 0px;
				border: 1px solid #BBBAC0 !important;
			}
			.admin-select .select2-container--default .select2-selection--single .select2-selection__rendered, .admin-select .select2-container--default .select2-selection--single {
				height: 34px;
				line-height:34px;
			}
			.admin-select .select2-container .select2-selection--single .select2-selection__rendered {
				padding-left: 4px;
			}
		</style>
		<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty protocolsForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					${jspEditType}
				</p>
				<hr/>
				<div class="panel-scroll height-535">
					<form id="mainForm" class="form-horizontal" action="${empty protocolsForm.id ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" enctype="multipart/form-data">
							<%-- 角色列表一览 --%>
						<input type="hidden" name="id" id="id" value="${protocolsForm.id }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<input type="hidden" name="fileUrl" id="fileUrl" value="${protocolsForm.fileUrl }" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="protocolType"><span class="symbol required">协议类型 </span></label>
							<c:if test="${ empty protocolsForm.id }">
								<div class="col-sm-4">
									<select id="protocolType" name="protocolType" class="form-select2" datatype="*" nullmsg="未指定协议类型！"  style="width:100%" data-placeholder="请选择协议类型...">
										<option value=""></option>
										<c:forEach items="${protocolTypeList }" var="protocolType" begin="0" step="1" >
											<option value="${protocolType.nameCd }"
													<c:if test="${protocolType.nameCd eq protocolsForm.protocolType}">selected="selected"</c:if>>
												<c:out value="${protocolType.name }"></c:out></option>
										</c:forEach>
									</select>
								</div>
							</c:if>
							<c:if test="${ !empty protocolsForm.id }">
								<div class="col-sm-4">
									<select id="protocolType" name="protocolType" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择协议类型...">
										<option value=""></option>
										<c:forEach items="${protocolTypeList }" var="protocolType" begin="0" step="1" >
											<option value="${protocolType.nameCd }"
													<c:if test="${protocolType.nameCd eq protocolsForm.protocolType}">selected="selected"</c:if>>
												<c:out value="${protocolType.name }"></c:out></option>
										</c:forEach>
									</select>
									<input type="hidden" name="protocolType" value="${protocolsForm.protocolType}" />
								</div>
							</c:if>
							<hyjf:validmessage key="protocolType" label="协议类型"></hyjf:validmessage>
						</div>


						<div class="form-group">
							<label class="col-sm-2 control-label" for="templetId">
								<span class="symbol required"></span>模版编号
							</label>
							<div class="col-sm-10 ">
								<input type="text" id="templetId" name="templetId" value="${protocolsForm.templetId}" readonly="readonly" style="border: 0"/>
							</div>
						</div>

						<c:if test="${ empty protocolsForm.id }">
						<div class="form-group">
							<label class="col-sm-2 control-label" for="fileupload">
								<span class="symbol required"></span>模版上传
							</label>
							<div class="col-sm-10 ">
								<!-- 按钮管理 -->
								<div class="user-edit-image-buttons">
									<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fn-Upload"></i>上传PDF文件</span>
										<input type="file" name="file" id="fileupload" class= "fileupload" accept="application/pdf">
									</span>
									<label id="fileuploadmsg" class="Validform_checktip Validform_wrong"></label>
									<input type="hidden" name="caFlag" id="caFlag" value="${protocolsForm.caFlag }" datatype="*" nullmsg="请上传模板"/>
									<hyjf:validmessage key="caFlag" label="模版"></hyjf:validmessage>
								</div>
								<div id="progress">
									<div class="bar" style="width: 0%;"></div>
								</div>
								（文件名不可含汉字）
							</div>
						</div>
						</c:if>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="isActive">
								<span class="symbol required"></span>状态
							</label>
							<div class="col-sm-10 ">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="isActive" value="1"  datatype="*" ${ ( empty protocolsForm.isActive || protocolsForm.isActive eq 1 ) ? 'checked' : ''}> <label for="stateOn"> 启用 </label>
									<input type="radio" id="stateOff" name="isActive" value="0" datatype="*" ${ protocolsForm.isActive eq 0 ? 'checked' : ''}> <label for="stateOff"> 禁用 </label>
									<hyjf:validmessage key="isActive" label="状态"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span class="symbol required"></span>备注 </label>
							<div class="col-sm-10">
								<textarea placeholder="" id="remark" name="remark" class="form-control limited"
										  datatype="*1-50" nullmsg="未填写备注" errormsg="备注不能超过50个字符！" maxlength="50"><c:out value="${protocolsForm.remark}"></c:out></textarea>
								<hyjf:validmessage key="remark" label="备注"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-2 col-sm-10">
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
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
			.purMargin{
				margin:8px 0;
			}
			.purMargin input{
				width:200px;
			}
			.bar {
				height: 8px;
				border-radius:4px;
				background: green;
			}
			.content{
				width: 100%;text-align: center;margin-top: 70px;
			}
			#progress{
				height: 8px;
				border-radius:4px;
				background: #f2f2f2;
				width: 200px;

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
		<!-- The templets plugin is included to render the upload/download listings -->
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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/protocols/protocolsInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
