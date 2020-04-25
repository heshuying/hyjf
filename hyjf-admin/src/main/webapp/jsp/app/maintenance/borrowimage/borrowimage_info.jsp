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
	<tiles:putAttribute name="pageTitle" value="产品图片" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${isEdit ne 'Y' ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430" style="left:10px">
					<form id="mainForm" action="${isEdit ne 'Y' ? 'insertAction' : 'updateAction'}" method="post" role="form" class="form-horizontal">
						<input type="hidden" id="success" value="${success}" />
						<input type="hidden" id="isEdit" name="isEdit" value="${isEdit}" />
						<div style="display: none;" >
								<input type="text" maxlength="20" placeholder="id" id="id" name="id" value="${form.id}" >
								<hyjf:validmessage key="id" label="id"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="borrowImage"> 
								<span class="symbol required"></span>图片标识
							</label>
							<div class="col-xs-9">
								<input type="text" maxlength="20" placeholder="图片标识" id="borrowImage" name="borrowImage" value="${form.borrowImage}"  class="form-control" datatype="*,/^[a-zA-Z0-9_]+$/" errormsg="图片标识 只能是数字,字母和下划线，长度1~20个字符！">
								<hyjf:validmessage key="borrowImage" label="图片标识"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="borrowImageTitle"> 
								<span class="symbol required"></span>图片名称
							</label>
							<div class="col-xs-9">
								<input type="text" maxlength="100" placeholder="图片名称" id="borrowImageTitle" name="borrowImageTitle" value="${form.borrowImageTitle}" class="form-control" datatype="s1-100" errormsg="图片名称 长度1~100个字符！">
								<hyjf:validmessage key="borrowImageTitle" label="图片名称"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="borrowImageTitle"> 
								<span class="symbol required"></span>跳转标示
							</label>
							<div class="col-xs-9">
								<input type="text" maxlength="100" placeholder="跳转标示" id="jumpName" name="jumpName" value="${form.jumpName}" class="form-control" >
								<hyjf:validmessage key="jumpName" label="跳转标示"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
								<label class="col-xs-3 control-label padding-top-5" for="pageType"> 
									<span class="symbol required"></span>页面类型
								</label>
								<div class="col-xs-9">
									<select id="pageType" name="pageType" onchange="optionChange()" class="form-control underline form-select2" style="width:100%;" datatype="*" data-placeholder="请选择页面类型">
											<option value ="2" <c:if test="${'2' eq form.pageType}">selected</c:if>>默认</option>
										<option value ="1" <c:if test="${'1' eq form.pageType}">selected</c:if> >H5页面</option>
										<option value ="0" <c:if test="${'0' eq form.pageType}">selected</c:if> >原生页面</option>
									</select>
								</div>
						</div>
						<%-- 
						<div id="borrowImageListDiv" class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageUrl"> 
								<span class=""></span>    
							</label>
							<div class="col-xs-9">
								<select id="pageUrlSelect"  class="form-control underline form-select2" style="width:100% " data-placeholder="请选择对应选项">
									<option value ="">请选择对应选项</option>
										<option value ="HZT" <c:if test="${'HZT' eq form.pageUrl}">selected</c:if> >惠直投</option>
										<option value ="HXF" <c:if test="${'HXF' eq form.pageUrl}">selected</c:if> >惠消费</option>
								</select>
							</div>
						</div>  
						--%>
						<div id="pageUrlDiv" class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="pageUrl"> 
								<span class=""></span>    
							</label>
							<div class="col-xs-9">
								<input type="text" maxlength="100" placeholder="跳转地址" id="pageUrl" name="pageUrl" value="${form.pageUrl}"  class="form-control" datatype="*0-100" errormsg="URL 长度1~100个字符！">
								<hyjf:validmessage key="pageUrl" label="跳转地址"></hyjf:validmessage>
							</div>
						</div>
						<div id="pageUrlSelect" class="form-group">
								<div class="col-xs-9">
									<select id="jumpName" name="jumpName"  class="form-control underline form-select2 form-style2" datatype="*" data-placeholder="请选择页面类型">
										<option value ="HZT" <c:if test="${'HZT' eq form.jumpName}">selected</c:if>>汇直投列表</option>
										<option value ="HXF" <c:if test="${'HXF' eq form.jumpName}">selected</c:if>>汇消费列表</option>
										<option value ="XSH" <c:if test="${'XSH' eq form.jumpName}">selected</c:if>>新手汇</option>
										<option value ="ZXH" <c:if test="${'ZXH' eq form.jumpName}">selected</c:if>>尊享汇</option>
										<option value ="RTB" <c:if test="${'RTB' eq form.jumpName}">selected</c:if>>汇金理财</option>
										<option value ="HZR" <c:if test="${'HZR' eq form.jumpName}">selected</c:if>>汇转让</option>
									</select>
								</div>
						</div>
						<div class="form-group">
							
							<div class="col-xs-9">
								<select id="versionSelect" name="versionSelect"  onchange="optionChangeVersion()"  class="form-control underline form-select2"  style="width:100%;margin-left:145px " datatype="*" data-placeholder="请选择版本号">
									<option value ="0" <c:if test="${form.versionMax eq null || form.versionMax eq ''}">selected</c:if>>大于等于</option>
									<option value ="1" <c:if test="${(form.versionMax ne null && form.versionMax ne '') && (form.version ne null && form.version ne '')}">selected</c:if> >版本之间<option>
									<option value ="2" <c:if test="${form.version eq null || form.version eq ''}">selected</c:if> >小于等于</option>
								</select>
							</div>
						</div>
									<div class="form-group"  >
								 <label class="col-xs-3 control-label padding-top-5" > 
										<span class="" style="padding-left:5px"></span>  
										版本 :
									    </label>
											<li id="versionmin" style="float:left;"> 
											<input type="text" id="version" name="version" value="${form.version}">
											</li>
											<li id="versionmax" style="float:left;">
											<input type="text" id="versionMax" name="versionMax" value="${form.versionMax}">
											</li>
									</div>
						
						</div>
						<div class="form-group" >
							<label class="col-xs-3 control-label padding-top-5" for="notes"> 
								<span class=""></span>图片描述
							</label>
							<div class="col-xs-9 col-xs-style">
								<input type="text" maxlength="10" placeholder="图片描述" id="notes" name="notes" value="${form.notes}" class="form-control" datatype="s0-10" errormsg="图片描述不能超过10个汉字！">
								<hyjf:validmessage key="notes" label="图片描述"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="sort"> 
								<span class="symbol required"></span>排序
							</label>
							<div class="col-xs-9 col-xs-style">
								<input type="text" maxlength="4" placeholder="排序" id="sort" name="sort" value="${form.sort}" class="form-control" datatype="n0-4" errormsg="请填写小于4位的数字！">
								<hyjf:validmessage key="sort" label="排序"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="logo"> 
								<span class="symbol required"></span>图片
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail" style="height: 200px;">
									<img width="190" height="143" src="${form.borrowImageUrl}" alt="">
								</div>
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class="purMargin">
									<input type="hidden" name="borrowImageName" id="borrowImageName" value="${form.borrowImageName}" />
									<input type="hidden" name="borrowImageRealname" id="borrowImageRealname" value="${form.borrowImageRealname}" />
									<input type="hidden" name="borrowImageUrl" id="borrowImageUrl" value="${form.borrowImageUrl}" />
								</div>
								<!-- 按钮管理 -->
								<div class="user-edit-image-buttons">
									<span class="btn btn-azure btn-file"><i class="fa fa-picture"></i> 上传图片<input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"> </span>
								</div>
								<hyjf:validmessage key="borrowImageRealname" label="图片"></hyjf:validmessage>
							</div>
						</div>
					</form>
					<div class="form-group margin-bottom-0" align="center">
						<div class="col-sm-offset-2 col-sm-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a> <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
		.purMargin {margin: 8px 0;}
		.purMargin input {width: 200px;}
		.margin-bottom-0{float:left;width:100%;margin-top:15px}
		.form-style2{width:100%;margin-left:145px}
		.col-xs-style{margin-bottom:15px}
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
		<script type='text/javascript' src="${webRoot}/jsp/app/maintenance/borrowimage/borrowimage_info.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
