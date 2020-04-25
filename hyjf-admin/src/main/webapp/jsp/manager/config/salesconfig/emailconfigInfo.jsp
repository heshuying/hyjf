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
		<tiles:putAttribute name="pageTitle" value="添加信息" />
		
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty labelForm.id ? '添加' : '修改'}"></c:set>
		<!-- start -->
		<div class="container-fluid container-fullw">
			<div class="row">
				<div class="col-md-12">
					<h2></h2>
					<hr>
					<form id="mainForm"	action="${empty EmailRecipientForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" >
					<%-- 文章列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${EmailRecipientForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
							
						<!-- 新加字段 -->
					<div class="row">
						<div class="col-md-10">
							<div class="row">
								<div class="col-md-10 col-lg-4">
									<div class="form-group">
										<label class="control-label">
											业务名称：<span class="symbol required"></span>
										</label>
										<input type="text"
											   name="businessName" id="businessName"
											   class="form-control input-sm"
											   value="销售日报"  readonly="true" maxlength="20"  />
									</div>
									<div class="row">
										<div class="col-sm-15">
											<div class="form-group">
												<label class="control-label" >
													邮件发送时间：
												</label>
												<div class="clip-radio radio-primary">
													<input type="radio" value="1" name="timePoint" id="everyWorkDay" <c:if test="${ ( EmailRecipientForm.timePoint eq '1' or empty EmailRecipientForm.id) }">checked</c:if>>
													<label for="everyWorkDay">每个工作日</label>
													<input type="radio" value="2" name="timePoint" id="everyday" <c:if test="${ ( EmailRecipientForm.timePoint eq '2' )  }">checked</c:if>>
													<label for="everyday">每天</label>
													<input type="radio" value="3" name="timePoint" id="monthday" <c:if test="${ ( EmailRecipientForm.timePoint eq '3' )  }">checked</c:if>>
													<label for="monthday">每月第一个工作日</label>
													<div class="col-xs-3" style="float:right;">
														<input type="text"  id="time" name="time" readonly="readonly"
															   value="07:00:00" class="form-control" maxlength="50">
													</div>
												</div>

											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="col-xs-3 control-label padding-top-3" for="email">
											收件人邮箱： </label>
										<div class="col-xs-10">
											<input type="text" placeholder="收件人邮箱，不同邮箱用英文符号（;）隔开" id="email" name="email"
												   value="${EmailRecipientForm.email}" class="form-control" maxlength="100" errormsg="收件人邮箱，不同邮箱用英文（;）隔开">
										</div>
									</div>
									<div class="row">
										<div class="col-md-8">
											<p>
												点击【提交保存】按钮，保存当前的填写的资料。
											</p>
										</div>
										<div class="col-md-8">
										</div>
										<div class="col-md-4">
											<div class="row">
											<a class="btn btn-o btn-primary btn-sm hidden-xs fn-return"
											   data-toggle="tooltip" data-placement="bottom" data-original-title="返回列表">返回列表<i class="fa "></i></a>
											<a class="btn btn-o btn-primary btn-sm hidden-xs fn-save"
											   data-toggle="tooltip" data-placement="bottom" data-original-title="提交保存">提交保存<i class="fa "></i></a>
											</div>
										</div>
										<div class="col-md-4">
										</div>
									</div>
								</div>

							</div>
						</div>
					</div>


					</form>
				</div>
			</div>
		</div>
		<!-- end -->
			
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda-themeless.min.css" rel="stylesheet" media="screen">
	<style>
	.table-heads .table-striped {
		margin-bottom: 0;
	}
	.table-body {
		position: relative;
	}
	.table-body .vertical-align-top {
		vertical-align: top!important;
	}
	.table-body .table-striped {
		border-top: none;
		margin-bottom: 0;
	}
	.table-body .table-striped tr:first-child td {
		border-top: 0;
	}
	.table-body .form-group {
		margin-bottom: 0;
	}
	.thumbnails-wrap {
		background: #f5f5f5;
		border: 1px solid #ccc;
		padding: 3px;
		display: inline-block;
	}
	.thumbnails-wrap img {
		min-width: 35px;
		max-width: 70px;
		height: 22px;
	}
	.popover {
		max-width: 500px;
	}
	.popover img {
		max-width: 460px;
	}
	.clip-radio{
		margin-top:4px
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
			<!-- The File Upload validation plugin -->
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/config/salesconfig/emailconfigInfo.js"></script>
			<script type="text/javascript">
		//日历选择器																						
		$('.datepicker').datepicker({																				
			autoclose: true,																			
			todayHighlight: true																			
		});																		
		// 日历范围限制																				
		/* $('#start-date').on("change", function(evnet, d) {																				
			d = $('#end-date').datepicker("getDate"),																			
			d && $('#start-date').datepicker("setEndDate", d)																			
		}),																				
		$('#end-date').on("change", function(evnet, d) {																				
			d = $('#start-date').datepicker("getDate"),																			
			d && $('#end-date').datepicker("setStartDate", d)																			
		});	 */																			

</script>
	</tiles:putAttribute>
</tiles:insertTemplate>
