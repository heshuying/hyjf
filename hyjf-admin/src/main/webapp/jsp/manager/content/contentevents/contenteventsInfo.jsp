<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>

<%--<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">--%>
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="纪事管理" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty contenteventsForm.id ? '添加' : '修改'}"></c:set>
		<div class="container-fluid container-fullw">
			<div class="row">
				<div class="col-md-12">
					<h2></h2>
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您本次提交公司纪事的基本信息.
					</p>
					<hr>
					<form id="mainForm"	action="${empty contenteventsForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" >
						<%-- 公司纪事列表一览 --%>
						<input type="hidden" name="id" id="id" value="${contenteventsForm.id }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />

						<div class="row">
							<div class="col-md-12">
								<div class="errorHandler alert alert-danger no-display">
									<i class="fa fa-times-sign"></i> You have some form errors. Please check below.
								</div>
								<div class="successHandler alert alert-success no-display">
									<i class="fa fa-ok"></i> Your form validation is successful!
								</div>
							</div>
							<div class="col-md-6">

                                <div class="form-group">
                                    <label class="control-label" for="title">
                                        <span class="symbol required"></span>纪事时间
                                    </label>
									<div class="input-group ">
										<input type="text" name="acTime" id="acTime" class="form-control underline" value="${actTimeStr}" datatype="*1-10" errormsg="记事时间必填！" maxlength="50" onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd', errDealMode: 1})"/>
									</div>
                                </div>

								<div class="form-group">
									<label class="control-label" for="title">
										<span class="symbol required"></span>纪事标题
									</label>
									<input type="text" placeholder="纪事标题" id="title" name="title"	value="${contenteventsForm.title}" class="form-control" datatype="*2-50" errormsg="纪事标题 只能是字符汉字，长度2~50个字符！" maxlength="50">
									<hyjf:validmessage key="type" label="纪事标题"></hyjf:validmessage>
								</div>

								<div class="form-group">
									<label class="control-label">
										<span class="symbol required" aria-required="true"></span>状态
									</label>
									<div class="radio clip-radio radio-primary ">
                                        <input type="radio" id="statusOn" name="status" datatype="*" value="0" class="event-categories" <c:if test="${contenteventsForm.status == 0}">checked="checked"</c:if>>
										<label for="statusOn"> 关闭 </label>
										<input type="radio" id="statusOff" name="status" datatype="*" value="1" class="event-categories" <c:if test="${contenteventsForm.status == 1}">checked="checked"</c:if>>
										<label for="statusOff"> 启用 </label>
									</div>
									<hyjf:validmessage key="status" label="纪事状态"></hyjf:validmessage>
								</div>


							</div>
							<div class="col-md-6">
								<div class="row">
									<div class="col-md-8">
										<div class="form-group">
											<label class="col-xs-2 control-label padding-top-5" for="imgurl"> <span
													class="symbol"></span>文章图片
											</label>
											<div class="fileinput fileinput-new col-xs-6" data-provides="fileinput">
												<!-- 缺省图片 -->
												<div class="fileinput-new thumbnail">
													<img width="160" height="120" src="${fileDomainUrl}${contenteventsForm.thumb}" alt="">
												</div>
												<!-- 显示图片 -->
												<div class="fileinput-preview fileinput-exists thumbnail"></div>
												<!-- 图片路径 -->
												<div class = "purMargin">
													<input type="text" readonly="readonly"
														   name="thumb" id="imgurl" value="${contenteventsForm.thumb}" placeholder="上传图片路径"/>
												</div>
												<!-- 按钮管理 -->
												<div class="user-edit-image-buttons">
													<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span>
														<input type="file" name="file" id="fileupload" class= "fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
													</span>
													<a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput" onclick='$("#imgurl").val("")'>
														<i class="fa fa-times"></i> 删除
													</a>
												</div>
												<%--<hyjf:validmessage key="type" label="imgurl"></hyjf:validmessage>--%>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<!-- 文本域开始 -->
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label" > <span class="symbol required"></span>纪事内容
									</label>
									<div>
										<textarea name="content" id="content" placeholder="纪事内容" maxlength="50000" class="form-control tinymce" cols="10" rows="5" datatype="*1-50000" errormsg="纪事内容最多只能输入50000个文字！" errormsg="未填写文章内容">${contenteventsForm.content }</textarea>
									</div>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<div>
									<span class="symbol required"></span>必须填写的项目
									<hr>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-8">
								<p>
									点击【提交保存】按钮，保存当前的填写的资料。
								</p>
							</div>
							<div class="col-md-4">
								<a class="btn btn-primary fn-Confirm  pull-right"><i
										class="fa fa-check"></i> 提交保存</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<!-- end -->
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
	<style>
		#acTime{
			margin-left:15px;
		}
		.input-group.input-daterange.datepicker span{
			padding-left:15px;
		}
	</style>
	</tiles:putAttribute>
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
		<script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/content/contentevents/contenteventsInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
