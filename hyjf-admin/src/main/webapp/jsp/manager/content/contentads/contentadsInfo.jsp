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
	<tiles:putAttribute name="pageTitle" value="广告管理" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty contentadsForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm" action="${empty contentadsForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" class="form-horizontal">
						<%-- 广告列表一览 --%>
						<input type="hidden" name="id" id="id" value="${contentadsForm.id }" /> <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-2 control-label" for="adsType"> <span class="symbol required"></span>广告位
							</label> <label class="col-xs-10 control-label" for="adsType"> <select name="typeid" class="form-control underline form-select2">
									<c:forEach items="${adsTypeList}" var="adsType" begin="0" step="1" varStatus="status">
										<option value="${adsType.typeid }" <c:if test="${adsType.typeid eq contentadsForm.typeid}">selected="selected"</c:if>>
											<c:out value="${adsType.typename }"></c:out></option>
									</c:forEach>
							</select>
							</label>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="name"> <span class="symbol required"></span>广告名称
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="名称" id="name" name="name" value="${contentadsForm.name}" class="form-control" maxlength="20" datatype="*2-20" errormsg="名称 只能是字符汉字，长度2~20个字符！">
								<hyjf:validmessage key="type" label="名称"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label" for="name"> <span class="symbol required"></span>活动描述
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="活动描述" id="activitiDesc" name="activitiDesc" value="${contentadsForm.activitiDesc}" class="form-control" maxlength="250">
								<hyjf:validmessage key="type" label="名称"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="image"> <span class="symbol required"></span>广告图片
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${contentadsForm.image}" alt="">
								</div>
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class="purMargin">
									<input type="text" readonly="readonly" datatype="*" name="image" id="image" value="${contentadsForm.image}" placeholder="上传图片路径" />
								</div>
								<!-- 按钮管理 -->
								<div class="user-edit-image-buttons">
									<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span> <input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"> </span> <a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput"> <i class="fa fa-times"></i> 删除
									</a>
								</div>
								<hyjf:validmessage key="type" label="image"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label" for="url"> <span class="symbol"></span>URL
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="网址" id="url" name="url" datatype="*0-100" value="${contentadsForm.url}" class="form-control" maxlength="100" errormsg="网址 只能是字符汉字，长度1~100个字符！">
								<hyjf:validmessage key="type" label="网址"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="shareTitle"> <span class="symbol"></span>分享标题
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="分享标题" id="shareTitle" maxlength="30" name="shareTitle" value="${contentadsForm.shareTitle}" class="form-control" errormsg="必填选项！">
								<hyjf:validmessage key="type" label="分享标题"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="shareContent"> <span class="symbol"></span>分享内容
							</label>
							<div class="col-xs-10">
								<textarea placeholder="分享内容" id="shareContent" maxlength="200" name="shareContent" class="form-control limited">${contentadsForm.shareContent}</textarea>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="shareImage"> <span class="symbol"></span>分享图片
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${contentadsForm.shareImage}" alt="">
								</div>
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class="purMargin">
									<input type="text" readonly="readonly" maxlength="100" name="shareImage" id="shareImage" value="${contentadsForm.image}" placeholder="上传图片路径" />
								</div>
								<!-- 按钮管理 -->
								<div class="user-edit-image-buttons">
									<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span> <input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"> </span> <a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput"> <i class="fa fa-times"></i> 删除
									</a>
								</div>
								<hyjf:validmessage key="type" label="shareImage"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label" for="shareUrl"> <span class="symbol"></span>分享URL
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="分享网址" id="shareUrl" datatype="*0-100" name="shareUrl" value="${contentadsForm.shareUrl}" maxlength="100" class="form-control" errormsg="分享网址长度100个字符！">
								<hyjf:validmessage key="type" label="分享网址"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="order"> <span class="symbol required"></span>排序
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="排序" id="order" name="order" value="${contentadsForm.order}" class="form-control" datatype="n1-3" errormsg="排序 只能是数字，长度1-3位数字！" maxlength="3">
								<hyjf:validmessage key="type" label="排序"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label"> <span class="symbol required"></span>广告状态
							</label>
							<div class="col-xs-10">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" name="status" id="statusOff" datatype="*" class="event-categories" value="0" ${contentadsForm.status == '0' ? 'checked' : ''}> <label for="statusOff"> 关闭 </label> 
									<input type="radio" name="status" id="statusOn" datatype="*" value="1" ${contentadsForm.status == '1' ? 'checked' : ''}> <label for="statusOn"> 启用 </label>
								</div>
							</div>
							<hyjf:validmessage key="status" label="广告状态"></hyjf:validmessage>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label"> <span class="symbol required"></span>Banner是否可悬浮
							</label>
							<div class="col-xs-10">
								<div class="radio clip-radio radio-primary ">
							    	<input type="radio" name="isIndex" id="isIndexOff" datatype="*" class="event-categories" value="0" ${contentadsForm.isIndex == '0' ? 'checked' : ''}> <label for="isIndexOff"> 不可悬浮</label> 
									<input type="radio" name="isIndex" id="isIndexOn" datatype="*" class="event-categories" value="1" ${contentadsForm.isIndex == '1' ? 'checked' : ''}> <label for="isIndexOn"> 可悬浮 </label>
								</div>
							</div>
							<hyjf:validmessage key="isIndex" label="状态"></hyjf:validmessage>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label"> <span class="symbol required"></span>开始日期
							</label>
							<div class="input-group input-daterange datepicker col-xs-6">
								<span class="input-icon"> <input type="text" name="startTime" id="startTime" size="11" class="form-control underline" datatype="*" 
								 value="${contentadsForm.startTime}"  onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}', errDealMode: 1})" />
								</span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label"> <span class="symbol required"></span>结束日期
							</label>
							<div class="input-group input-daterange datepicker col-xs-6">
								<span class="input-icon"> <input type="text" name="endTime" id="endTime" size="11" class="form-control underline" datatype="*" 
								 value="${contentadsForm.endTime}" onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}', errDealMode: 1})"/>
								</span>
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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/content/contentads/contentadsInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
