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
	<tiles:putAttribute name="pageTitle" value="奖品详情" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty confForm.prizeGroupCode ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty confForm.prizeGroupCode ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<input type="hidden" name="prizeGroupCode" id="prizeGroupCode" value="${confForm.prizeGroupCode }" /> 
						<input type="hidden" name="prizeReminderQuantity" value="${confForm.prizeReminderQuantity }" /> 
						<input type="hidden" name="prizeUsed" value="${confForm.prizeQuantity - confForm.prizeReminderQuantity }" /> 
						<input type="hidden" name="prizeKind" value="${confForm.prizeKind }" /> 
						<input type="hidden" name="prizeApplyTime" value="${confForm.prizeApplyTime }" /> 
						<input type="hidden" name="addTime" value="${confForm.addTime }" /> 
						<input type="hidden" name="addUser" value="${confForm.addUser }" /> 
						<input type="hidden" name="delFlg" value="${confForm.delFlg }" /> 
						
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-2 control-label" for="prizeName"> <span
								class="symbol required"></span>奖品名称
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="奖品名称" id="prizeName" name="prizeName"
									value="${confForm.prizeName}" class="form-control" maxlength="20"
									datatype="*2-20" errormsg="奖品名称只能是字符汉字，长度2~20个字符！">
								<hyjf:validmessage key="prizeName" label="奖品名称"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="prizeProbability"> <span
								class="symbol required"></span>中奖几率
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="中奖几率" id="prizeProbability" name="prizeProbability"
									value="${confForm.prizeProbability}" class="form-control"
									datatype="/^\d{1,3}(\.\d{1,2})?$/,range" data-minvalue="0" data-maxvalue="100" nullmsg="未填写中奖几率" errormsg="中奖几率必须为数字，且最多2位小数，值在0~100之间">
								<hyjf:validmessage key="prizeProbability" label="中奖几率"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="prizeQuantity"> <span
								class="symbol required"></span>数量上限
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="数量上限" id="prizeQuantity" name="prizeQuantity"
									value="${confForm.prizeQuantity}" class="form-control" datatype="n1-6" errormsg="数量上限只能是数字，长度1-6位数字！" 
									 maxlength="6">
								<hyjf:validmessage key="prizeQuantity" label="数量上限"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label"> <span
								class="symbol required"></span>奖品类型
							</label>
							<div class="col-xs-10">
								<c:forEach items="${prizeType }" var="prizeType" begin="0" step="1" >
									<input type="radio" name="prizeType"
										datatype="*" value="${prizeType.nameCd }"
										${confForm.prizeType == prizeType.nameCd ? 'checked' : ''}> <label
										for="statusOn">${prizeType.name}</label> 
								</c:forEach>
							</div>
							<hyjf:validmessage key="prizeType" label="奖品类型"></hyjf:validmessage>
						</div>
						
						<div class="form-group" id="picUpload">
							<label class="col-xs-2 control-label padding-top-5" for="prizePicUrl"> <span
								class="symbol required"></span>图片
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${confForm.prizePicUrl}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" maxlength="20"
									datatype="*2-50" errormsg="奖品图片没有上传"
										name="prizePicUrl" id="prizePicUrl" value="${confForm.prizePicUrl}" placeholder="上传图片路径"/>
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
								<hyjf:validmessage key="prizePicUrl" label="上传图片路径"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group" id="couponConfig">
							<label class="col-xs-2 control-label" for="couponCode"> <span
								class="symbol required"></span>优惠券编号
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="优惠券编号" id="couponCode" name="couponCode" maxlength="100"
									datatype="*2-100" errormsg="优惠券编号没有填写！"
									value="${confForm.couponCodes}" class="form-control" >
								<hyjf:validmessage key="couponCode" label="优惠券编号"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="prizeSort"> <span
								class="symbol required"></span>排序
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="排序" id="prizeSort" name="prizeSort"
									value="${confForm.prizeSort}" class="form-control"
									datatype="n1-3" errormsg="排序 只能是数字，长度1-3位数字！" maxlength="3">
								<hyjf:validmessage key="prizeSort" label="排序"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="couponCode"> 备注
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="备注" id="remark" name="remark"
									value="${confForm.remark}" class="form-control" maxlength="100">
								<hyjf:validmessage key="remark" label="备注"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label" for="successMessage"> 
								<span class="symbol required"></span>成功提示
							</label>
							<div class="col-xs-10">
								<textarea rows="4" placeholder="成功提示" id="successMessage" name="successMessage" class="form-control"
										maxlength="100" datatype="*" nullmsg="请填写成功提示信息" errormsg="成功提示信息最长为100字符" >${confForm.successMessage}</textarea>
							</div>
							<hyjf:validmessage key="successMessage" label="成功提示"></hyjf:validmessage>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label"> <span
								class="symbol required"></span>奖品状态
							</label>
							<div class="col-xs-10">
									<input type="radio" name="prizeStatus" datatype="*"
										value="0"
										${confForm.prizeStatus == '0' ? 'checked' : ''}> <label
										for="statusOn"> 启用 </label> <input type="radio"
										name="prizeStatus" datatype="*" value="1"
										${confForm.prizeStatus == '1' ? 'checked' : ''}> <label
										for="statusOff"> 禁用 </label>
							</div>
							<hyjf:validmessage key="prizeStatus" label="奖品状态"></hyjf:validmessage>
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
			src="${webRoot}/jsp/invite/drawconf/drawconfinfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
