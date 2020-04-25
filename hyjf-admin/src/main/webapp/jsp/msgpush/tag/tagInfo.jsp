<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="推送标签" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty msgPushTagForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm" action="${empty msgPushTagForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" class="form-horizontal">
						<%-- 着落页 --%>
						<input type="hidden" name="id" id="id" value="${msgPushTagForm.id }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> 
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-2 control-label" for="tagName"> <span class="symbol required"></span>标签名称
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="标签名称" id="tagName" name="tagName" value="${msgPushTagForm.tagName}" class="form-control" maxlength="10" datatype="*1-10" errormsg="名称 只能是字符汉字，长度1~10个字符！"  >
								<hyjf:validmessage key="tagName" label="标签名称"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="tagCode"> <span class="symbol required"></span>标签编码
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="标签编码" id="tagCode" name="tagCode" value="${msgPushTagForm.tagCode}" <c:if test="${msgPushTagForm.status != null && msgPushTagForm.status != 0}">readonly</c:if> class="form-control" maxlength="20" datatype="*1-20" errormsg="标签编码只能是字符汉字，长度1~20个字符！" ajaxurl="checkAction?id=${msgPushTagForm.id }">
								<hyjf:validmessage key="tagCode" label="标签编码"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="introduction"> <span class="symbol required"></span>简介
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="简介" id="introduction" name="introduction" value="${msgPushTagForm.introduction}" class="form-control" maxlength="200" datatype="*1-200" errormsg="简介只能是字符汉字，长度1~20个字符！">
								<hyjf:validmessage key="introduction" label="简介"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="codeUrl"><span class="symbol required"></span> icon </label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<%-- <img width="160" height="120" src="${fileDomainUrl}${msgPushTagForm.codeUrl}" alt=""> --%>
								</div>
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class="purMargin">
									<input type="text" readonly="readonly" datatype="*" name="iconUrl" id="iconUrl" value="${msgPushTagForm.iconUrl}" placeholder="上传图片路径" />
								</div>
								<!-- 按钮管理 -->
								<div class="user-edit-image-buttons">
									<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span>
									<input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"> </span>
									<a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput"> <i class="fa fa-times"></i> 删除</a>
								</div>
								<hyjf:validmessage key="iconUrl" label="icon"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="isLogin"> <span class="symbol required"></span>是否登录查看：
							</label>
							<div class="col-xs-10">
								<select id="isLogin" name="isLogin" class="form-select2">
									<option value="0" <c:if test="${msgPushTagForm.isLogin == 0}">selected="selected"</c:if>>是</option>
									<option value="1" <c:if test="${msgPushTagForm.isLogin == 1}">selected="selected"</c:if>>否</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="sort"> <span class="symbol"></span>排序</label>
							<div class="col-xs-10">
								<input type="text" placeholder="排序" id="sort" name="sort" value="${msgPushTagForm.sort}" class="form-control" maxlength="3" datatype="n1-3" errormsg="排序只能是数字，长度1~3位！">
								<hyjf:validmessage key="sort" label="排序"></hyjf:validmessage>
							</div>
						</div>						
						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-2 col-xs-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a> <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
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
		<script type='text/javascript' src="${webRoot}/jsp/msgpush/tag/tagInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
