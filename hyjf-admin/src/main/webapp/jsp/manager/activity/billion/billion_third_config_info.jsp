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
	<tiles:putAttribute name="pageTitle" value="秒杀配置" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="编辑秒杀配置"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm" action="updateAction" method="post" role="form" class="form-horizontal">
						<%-- 广告列表一览 --%>
						<input type="hidden" name="id" id="id" value="${billionThirdConfigForm.id }" /> 
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> 
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-2 control-label" for="prizeName"> <span class="symbol required"></span>奖品名称
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="奖品名称" id="prizeName" name="prizeName" value="${billionThirdConfigForm.prizeName}" class="form-control" maxlength="20" datatype="*2-20" errormsg="奖品名称 只能是字符汉字，长度2~20个字符！">
								<hyjf:validmessage key="prizeName" label="奖品名称"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label" for="couponNum"> <span class="symbol required"></span>数量
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="数量" id="couponNum" name="couponNum" value="${billionThirdConfigForm.couponNum}" class="form-control" datatype="n1-10"  maxlength="10" errormsg="数量 只能是1-10位数字！">
								<hyjf:validmessage key="couponNum" label="数量"></hyjf:validmessage>
							</div>
						</div>

						<%-- <div class="form-group">
							<label class="col-xs-2 control-label padding-top-5" for="picUrl"> <span class="symbol required"></span>广告图片
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${billionThirdConfigForm.picUrl}" alt="">
								</div>
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class="purMargin">
									<input type="text" readonly="readonly" datatype="*" name="picUrl" id="picUrl" value="${billionThirdConfigForm.picUrl}" placeholder="上传图片路径" />
								</div>
								<!-- 按钮管理 -->
								<div class="user-edit-image-buttons">
									<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span> <input type="file" name="file" id="fileupload" class="fileupload"> </span> <a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput"> <i class="fa fa-times"></i> 删除
									</a>
								</div>
								<hyjf:validmessage key="picUrl" label="图片"></hyjf:validmessage>
							</div>
						</div>
 --%>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="couponCode"> <span class="symbol required"></span>优惠券编号
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="优惠券编号" id="couponCode" name="couponCode" datatype="*0-200" value="${billionThirdConfigForm.couponCode}" class="form-control" maxlength="100" errormsg="优惠券编号必填，长度1~200个字符！">
								<hyjf:validmessage key="couponCode" label="优惠券编号"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-2 control-label"> <span class="symbol required"></span>奖品状态
							</label>
							<div class="col-xs-10">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" name="status" id="statusOff" datatype="*" class="event-categories" value="0" ${billionThirdConfigForm.status == '0' ? 'checked' : ''}> <label for="statusOff"> 启用 </label> 
									<input type="radio" name="status" id="statusOn" datatype="*" value="1" ${billionThirdConfigForm.status == '1' ? 'checked' : ''}> <label for="statusOn"> 关闭 </label>
								</div>
							</div>
							<hyjf:validmessage key="status" label="广告状态"></hyjf:validmessage>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/activity/billion/billion_third_config_info.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
