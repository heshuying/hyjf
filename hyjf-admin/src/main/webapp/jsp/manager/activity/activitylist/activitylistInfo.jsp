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
	<tiles:putAttribute name="pageTitle" value="活动管理" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty activitylistForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty activitylistForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 活动列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${activitylistForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="title"> <span
								class="symbol required"></span>活动标题
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="活动标题" id="title" name="title"
									value="${activitylistForm.title}" class="form-control"
									datatype="*1-30" errormsg="活动标题 只能是字符汉字，长度1~30个字符！"
									maxlength="30">
								<hyjf:validmessage key="title" label="活动标题"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="img"> <span
								class="symbol required"></span>活动主图（PC）
							</label>
							<div class="fileinput fileinput-new col-xs-10 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${!empty activitylistForm.imgPc?activitylistForm.imgPc:activitylistForm.img}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" 
										name="imgPc" id="imgPc" value="${!empty activitylistForm.imgPc?activitylistForm.imgPc:activitylistForm.img}" placeholder="上传图片路径"/>
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
								<hyjf:validmessage key="type" label="imgPc"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="img"> <span
								class="symbol required"></span>活动主图（APP）
							</label>
							<div class="fileinput fileinput-new col-xs-10 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${!empty activitylistForm.imgApp?activitylistForm.imgApp:activitylistForm.img}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" 
										name="imgApp" id="imgApp" value="${!empty activitylistForm.imgApp?activitylistForm.imgApp:activitylistForm.img}" placeholder="上传图片路径"/>
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
								<hyjf:validmessage key="type" label="imgApp"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="img"> <span
								class="symbol required"></span>活动主图（微信）
							</label>
							<div class="fileinput fileinput-new col-xs-10 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${!empty activitylistForm.imgWei?activitylistForm.imgWei:activitylistForm.img}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" 
										name="imgWei" id="imgWei" value="${!empty activitylistForm.imgWei?activitylistForm.imgWei:activitylistForm.img}" placeholder="上传图片路径"/>
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
								<hyjf:validmessage key="type" label="imgWei"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="qr"> <span
								class="symbol required"></span>二维码
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${activitylistForm.qr}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" 
										name="qr" id="qr" value="${activitylistForm.qr}" placeholder="上传图片路径"/>
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
								<hyjf:validmessage key="type" label="qr"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="platform"> <span
								class="symbol required"></span>平台
							</label>
							<div class="col-xs-10">
								<c:forEach items="${allPlatformList}" var="adsType" begin="0"
									step="1">
									<input name="platform" type="checkbox" value="${adsType.nameCd }"
										datatype="*"
										<c:forEach items="${platformList }" var="adsType2" begin="0" step="1" ><c:if test="${adsType.nameCd==adsType2 }">checked="checked"</c:if></c:forEach> />${adsType.name }
				  		</c:forEach>
								<hyjf:validmessage key="type" label="活动平台"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="activityPcUrl">
								<span class="symbol required"></span>前台活动URL（PC）
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="前台活动url" id="activityPcUrl"
									name="activityPcUrl" value="${activitylistForm.activityPcUrl}"
									class="form-control" datatype="*0-100"
									errormsg="前台活动url 只能是字符符号，长度0~100个字符！" maxlength="100" />
								<hyjf:validmessage key="activityPcUrl" label="前台活动url"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="activityAppUrl">
								<span class="symbol required"></span>前台活动URL（APP）
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="前台活动url" id="activityAppUrl"
									name="activityAppUrl" value="${activitylistForm.activityAppUrl}"
									class="form-control" datatype="*0-100"
									errormsg="前台活动url 只能是字符符号，长度0~100个字符！" maxlength="100" />
								<hyjf:validmessage key="activityAppUrl" label="前台活动url"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for=activityWeiUrl>
								<span class="symbol required"></span>前台活动URL（微信）
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="前台活动url" id="activityWeiUrl"
									name="activityWeiUrl" value="${activitylistForm.activityWeiUrl}"
									class="form-control" datatype="*0-100"
									errormsg="前台活动url 只能是字符符号，长度0~100个字符！" maxlength="100" />
								<hyjf:validmessage key="activityWeiUrl" label="前台活动url"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="urlBackground">
								<span class="symbol required"></span>后台管理URL
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="后台管理url" id="urlForeground"
									name="urlBackground" value="${activitylistForm.urlBackground}"
									class="form-control" datatype="*0-200"
									errormsg="后台管理url 只能是字符符号，长度0~200个字符！" maxlength="200" />
								<hyjf:validmessage key="urlBackground" label="后台管理url"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="description">
								<span class="symbol"></span>活动描述
							</label>
							<div class="col-xs-10">
								<textarea placeholder="活动描述" id="description" name="description" maxlength="200"
									class="form-control limited">${activitylistForm.description}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="Time"> <span
								class="symbol required"></span>活动时间
							</label>
							<div class="input-group input-daterange datepicker">
								<input type="text" name="startTime" id="startTime"
									maxlength="19" style="margin-left: 105px;"
									onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode: 1})"
									value="${startTime}" datatype="*" errormsg="活动开始时间必填！" />~<input
									type="text" name="endTime" id="endTime" maxlength="19"
									onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode: 1})"
									value="${endTime}" datatype="*" errormsg="活动结束时间必填！" /></span>
							</div>
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
			.input-group.input-daterange.datepicker span{
				padding-left:105px;
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
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/activity/activitylist/activitylistInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
