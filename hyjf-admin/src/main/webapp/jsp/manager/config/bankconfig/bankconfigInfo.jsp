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
	<tiles:putAttribute name="pageTitle" value="银行管理" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty bankconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty bankconfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${bankconfigForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>银行名称
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="银行名称" id="name" name="name"
									value="${bankconfigForm.name}" class="form-control"
									datatype="s1-50" errormsg="银行名称 只能是字符汉字，长度1~50个字符！">
								<hyjf:validmessage key="type" label="银行名称"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="personalCredit">
								<span class="symbol required"></span>银行代码
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="银行代码" id="code" name="code"
									value="${bankconfigForm.code}" class="form-control"
									datatype="s1-10" errormsg="银行代码 只能是字符汉字，长度1~10个字符！">
								<hyjf:validmessage key="type" label="银行代码"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="appLogo"> <span
								class="symbol required"></span>银行ICON
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${bankconfigForm.appLogo}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" datatype="*"
										name="appLogo" id="appLogo" value="${bankconfigForm.appLogo}" placeholder="上传图片路径"/>
								</div>
								<!-- 按钮管理 -->
								<div class="user-edit-image-buttons">
									<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span>
										<input type="file" name="file" id="fileupload1" class= "fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
									</span>
									<a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput">
										<i class="fa fa-times"></i> 删除
									</a>
								</div>
								<hyjf:validmessage key="type" label="appLogo"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="logo"> <span
								class="symbol required"></span>logo
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${bankconfigForm.logo}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" datatype="*"
										name="logo" id="logo" value="${bankconfigForm.logo}" placeholder="上传图片路径"/>
								</div>
								<!-- 按钮管理 -->
								<div class="user-edit-image-buttons">
									<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span>
										<input type="file" name="file" id="fileupload2" class= "fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
									</span>
									<a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput">
										<i class="fa fa-times"></i> 删除
									</a>
								</div>
								<hyjf:validmessage key="type" label="logo"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>个人网银支持
							</label>
							<div class="col-xs-9">
								<div>
									<input type="radio" name="personalEbank"
										datatype="*" value="1" class="event-categories"
										${bankconfigForm.personalEbank == '1' ? 'checked' : ''}>
									<label> 是 </label> <input type="radio"
										 name="personalEbank" datatype="*" value="0"
										class="event-categories"
										${bankconfigForm.personalEbank == '0' ? 'checked' : ''}>
									<label> 否 </label>
								</div>
							</div>
							<hyjf:validmessage key="personalEbank" label="个人网银支持"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>企业网银支持
							</label>
							<div class="col-xs-9">
								<div>
									<input type="radio" name="enterpriseEbank"
										datatype="*" value="1" class="event-categories"
										${bankconfigForm.enterpriseEbank == '1' ? 'checked' : ''}>
									<label> 是 </label> <input type="radio"
										name="enterpriseEbank" datatype="*" value="0"
										class="event-categories"
										${bankconfigForm.enterpriseEbank == '0' ? 'checked' : ''}>
									<label> 否 </label>
								</div>
							</div>
							<hyjf:validmessage key="enterpriseEbank" label="企业网银支持"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>快捷支付支持
							</label>
							<div class="col-xs-9">
								<div>
									<input type="radio" name="quickPayment"
										datatype="*" value="1" class="event-categories"
										${bankconfigForm.quickPayment == '1' ? 'checked' : ''}>
									<label> 是 </label> <input type="radio"
									 name="quickPayment" datatype="*" value="0"
										class="event-categories"
										${bankconfigForm.quickPayment == '0' ? 'checked' : ''}>
									<label> 否 </label>
								</div>
							</div>
							<hyjf:validmessage key="quickPayment" label="快捷支付支持"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>即时提现
							</label>
							<div class="col-xs-9">
								<div>
									<input type="radio" name="immediatelyWithdraw"
										datatype="*" value="1" class="event-categories"
										${bankconfigForm.immediatelyWithdraw == '1' ? 'checked' : ''}>
									<label> 是 </label> <input type="radio"
										name="immediatelyWithdraw" datatype="*" value="0"
										class="event-categories"
										${bankconfigForm.immediatelyWithdraw == '0' ? 'checked' : ''}>
									<label> 否 </label>
								</div>
							</div>
							<hyjf:validmessage key="immediatelyWithdraw" label="即时提现"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>快速提现
							</label>
							<div class="col-xs-9">
								<div>
									<input type="radio" name="quickWithdraw"
										datatype="*" value="1" class="event-categories"
										${bankconfigForm.quickWithdraw == '1' ? 'checked' : ''}>
									<label> 是 </label> <input type="radio"
										name="quickWithdraw" datatype="*" value="0"
										class="event-categories"
										${bankconfigForm.quickWithdraw == '0' ? 'checked' : ''}>
									<label> 否 </label>
								</div>
							</div>
							<hyjf:validmessage key="quickWithdraw" label="一般提现"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>一般提现
							</label>
							<div class="col-xs-9">
								<div>
									<input type="radio" name="normalWithdraw"
										datatype="*" value="1" class="event-categories"
										${bankconfigForm.normalWithdraw == '1' ? 'checked' : ''}>
									<label> 是 </label> <input type="radio"
										name="normalWithdraw" datatype="*" value="0"
										class="event-categories"
										${bankconfigForm.normalWithdraw == '0' ? 'checked' : ''}>
									<label> 否 </label>
								</div>
							</div>
							<hyjf:validmessage key="normalWithdraw" label="一般提现"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>状态
							</label>
							<div class="col-xs-9">
								<div style="float:left;">
									<input type="radio" name="status" datatype="*"
										value="1" class="event-categories"
										${bankconfigForm.status == '1' ? 'checked' : ''}> <label
										> 启用 </label> <input type="radio" 
										name="status" datatype="*" value="0" class="event-categories"
										${bankconfigForm.status == '0' ? 'checked' : ''}> <label
										> 禁用 </label>
								</div>
							</div>
							<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>默认提现方式
							</label>
							<div class="col-xs-9">
								<div">
									<input type="radio" name="withdrawDefaulttype"
										datatype="*" value="0" class="event-categories"
										${bankconfigForm.withdrawDefaulttype == '0' ? 'checked' : ''}>
									<label > 一般提现 </label> <input type="radio"
										 name="withdrawDefaulttype" datatype="*" value="1"
										class="event-categories"
										${bankconfigForm.withdrawDefaulttype == '1' ? 'checked' : ''}>
									<label > 快速提现 </label> <input type="radio"
										 name="withdrawDefaulttype" datatype="*" value="2"
										class="event-categories"
										${bankconfigForm.withdrawDefaulttype == '2' ? 'checked' : ''}>
									<label > 即时提现 </label>
								</div>
							</div>
							<hyjf:validmessage key="withdrawDefaulttype" label="默认提现方式"></hyjf:validmessage>
						</div>
						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-2 col-xs-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 确 认</a> <a
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
			src="${webRoot}/jsp/manager/config/bankconfig/bankconfigInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
