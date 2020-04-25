<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>

<!-- 画面功能路径(ignore) -->
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="文章管理" />
		
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty contentarticleForm.id ? '添加' : '修改'}"></c:set>
		<!-- start -->
		<div class="container-fluid container-fullw">
			<div class="row">
				<div class="col-md-12">
					<h2></h2>
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您本次提交文章的基本信息.
					</p>
					<hr>
					<form id="mainForm"	action="${empty contentarticleForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" >
					<%-- 文章列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${contentarticleForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
							
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
										<span class="symbol required"></span>文章标题
									</label>
									<input type="text" placeholder="文章标题" id="title" name="title"	value="${contentarticleForm.title}" class="form-control" datatype="*2-50" errormsg="文章标题 只能是字符汉字，长度2~50个字符！" maxlength="50">
									<hyjf:validmessage key="type" label="文章标题"></hyjf:validmessage>
								</div>
								
								<div class="form-group">
									<label class="control-label" for="author">
										<span class="symbol required"></span>文章作者
									</label>
									<input type="text" placeholder="文章作者" id="author" name="author" value="${contentarticleForm.author}" class="form-control"	datatype="*2-20" errormsg="文章作者 只能是字符汉字，长度2~20个字符！" maxlength="20">
									<hyjf:validmessage key="type" label="文章作者"></hyjf:validmessage>
								</div>
								
								<div class="form-group">
									<label class="control-label" for="type">
										<span class="symbol required"></span>文章类型
									</label>
									<select name="type" class="form-control underline form-select2"
										datatype="*" errormsg="请选择文章类型">
										<option value="">全部</option>
										<c:forEach items="${categoryList }" var="record" begin="0"
											step="1" varStatus="status">
											<option value="${record.id }"
												<c:if test="${contentarticleForm.type==record.id}">selected="selected"</c:if>>${record.title }</option>
										</c:forEach>
									</select>
								</div>
								
								
								<div class="form-group">
									<label class="control-label">
										<span class="symbol required" aria-required="true"></span>状态
									</label>
									<div class="radio clip-radio radio-primary ">
										<input type="radio" id="statusOn" name="status" datatype="*"
											value="0" class="event-categories"
											${contentarticleForm.status == '0' ? 'checked' : ''}>
										<label for="statusOn"> 关闭 </label> <input type="radio"
											id="statusOff" name="status" datatype="*" value="1"
											class="event-categories"
											${contentarticleForm.status == '1' ? 'checked' : ''}>
										<label for="statusOff"> 启用 </label>
									</div>
									<hyjf:validmessage key="status" label="文章状态"></hyjf:validmessage>
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
													<img width="160" height="120" src="${fileDomainUrl}${contentarticleForm.imgurl}" alt="">
												</div> 
												<!-- 显示图片 -->
												<div class="fileinput-preview fileinput-exists thumbnail"></div>
												<!-- 图片路径 -->
												<div class = "purMargin">
													<input type="text" readonly="readonly"
														name="imgurl" id="imgurl" value="${contentarticleForm.imgurl}" placeholder="上传图片路径"/>
												</div>
												<!-- 按钮管理 -->
												<div class="user-edit-image-buttons">
													<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span>
														<input type="file" name="file" id="fileupload" class= "fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
													</span>
													<a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput">
														<i class="fa fa-times"></i> 删除
													</a>
												</div>
												<hyjf:validmessage key="type" label="imgurl"></hyjf:validmessage>
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
									<label class="control-label" for="author"> <span class="symbol required"></span>文章内容
									</label>
									<div>
										<textarea name="content" id="content" placeholder="文章内容" maxlength="50000"
											class="form-control tinymce" cols="10" rows="5" datatype="*1-50000" errormsg="文章内容最多只能输入50000个文字！"
											errormsg="未填写文章内容">${contentarticleForm.content }</textarea>
									</div>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="control-label" for="summary"> <span class="symbol required"></span>文章简介
									</label>
									<div class="">
										<textarea name="summary" id="summary" placeholder="文章简介"
											class="form-control" cols="10" rows="5" datatype="*1-50000" errormsg="文章简介最多只能输入50000个文字！"
											maxlength="50000">${contentarticleForm.summary }</textarea>
									</div>
								</div>
							</div>
						</div>
						<!-- 文本域结束 -->
						
						<!-- 
							<div class="form-group">
								<label class="col-xs-2 control-label" for="click"> <span
									class="symbol required"></span>点击率
								</label>
								<div class="col-xs-10">
									<input type="text" placeholder="点击率" id="click" name="click"
										value="${contentarticleForm.click}" class="form-control" readonly="readonly"
										maxlength="20" />
								</div>
							</div> -->
							
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
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/content/contentarticle/contentarticleInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
