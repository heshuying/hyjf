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
	<tiles:putAttribute name="pageTitle" value="银行配置" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty banksettingForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty banksettingForm.id ? 'insertAction' : 'updateAction'}" 
						method="post" role="form" class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${banksettingForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="bankName"> <span
								class="symbol required"></span>银行名称
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="银行名称" id="bankName" name="bankName"
									value="${banksettingForm.bankName}" class="form-control"
									datatype="s1-50" errormsg="银行名称 只能是字符汉字，长度1~50个字符！">
								<hyjf:validmessage key="type" label="银行名称"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="payAllianceCode">
								<span class="symbol required"></span>银行联行号
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="银行联行号" id="payAllianceCode" name="payAllianceCode"
									value="${banksettingForm.payAllianceCode}" class="form-control"
									datatype="n12-12" errormsg="银行联行号 只能是数字，长度12个字符！">
								<hyjf:validmessage key="type" label="银行联行号"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="bankIcon"> <span
								class="symbol required"></span>银行ICON
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${banksettingForm.bankIcon}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" datatype="*"
										name="bankIcon" id="bankIcon" value="${banksettingForm.bankIcon}" placeholder="请上传图片"/>
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
								<hyjf:validmessage key="type" label="bankIcon"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="bankLogo"> <span
								class="symbol required"></span>logo
							</label>
							<div class="fileinput fileinput-new col-xs-6 picClass" data-provides="fileinput">
								<!-- 缺省图片 -->
								<div class="fileinput-new thumbnail">
									<img width="160" height="120" src="${fileDomainUrl}${banksettingForm.bankLogo}" alt="">
								</div> 
								<!-- 显示图片 -->
								<div class="fileinput-preview fileinput-exists thumbnail"></div>
								<!-- 图片路径 -->
								<div class = "purMargin">
									<input type="text" readonly="readonly" datatype="*"
										name="bankLogo" id="bankLogo" value="${banksettingForm.bankLogo}" placeholder="请上传图片"/>
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
								class="symbol required"></span>支持快捷支付
							</label>
							<div class="col-xs-9">
								<div>
									<input type="radio" name="quickPayment"
										datatype="*" value="1" class="event-categories"
										${banksettingForm.quickPayment == '1' ? 'checked' : ''}>
									<label> 是 </label> <input type="radio"
										 name="quickPayment" datatype="*" value="0"
										class="event-categories"
										${banksettingForm.quickPayment == '0' ? 'checked' : ''}>
									<label> 否 </label>
								</div>
							</div>
							<hyjf:validmessage key="personalEbank" label=""></hyjf:validmessage>
						</div>
						<div id="kuaijie" >
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="singleQuota">
							 <span class="symbol required"></span>快捷充值单笔限额(元)</label>
							<div class="col-xs-9">
								<input type="text" placeholder="快捷充值单笔限额" id="singleQuota" name="singleQuota" value="${banksettingForm.singleQuota}" style="width: 80%"  class="form-control"
									maxlength="13" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="快捷充值单笔限额只能输入数字和小数点，长度1~13位" >
								<hyjf:validmessage key="type" label="快捷充值单笔限额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="singleCardQuota">
							 <span class="symbol required"></span>快捷充值单日限额(元)</label>
							<div class="col-xs-9">
								<input type="text" placeholder="快捷充值单日限额" id="singleCardQuota" name="singleCardQuota" value="${banksettingForm.singleCardQuota}" style="width: 80%"  class="form-control"
									maxlength="13" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="快捷充值单日限额只能输入数字和小数点，长度1~13位" >
								<hyjf:validmessage key="type" label="快捷充值单日限额"></hyjf:validmessage>
							</div>
						</div>
							<div class="form-group">
								<label class="col-xs-3 control-label padding-top-5" for="monthCardQuota">
									<span class="symbol required"></span>快捷充值单月限额(元)</label>
								<div class="col-xs-9">
									<input type="text" placeholder="快捷充值单月限额" id="monthCardQuota" name="monthCardQuota" value="${banksettingForm.monthCardQuota}" style="width: 80%"  class="form-control"
										   maxlength="13" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="快捷充值单月限额只能输入数字和小数点，长度1~13位" >
									<hyjf:validmessage key="type" label="快捷充值单月限额"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="feeWithdraw">
							 <span class="symbol required"></span>提现手续费(元/笔)</label>
							<div class="col-xs-9">
								<input type="text" placeholder="提现手续费" id="feeWithdraw" name="feeWithdraw" value="${banksettingForm.feeWithdraw != null ? banksettingForm.feeWithdraw : '1.0'}" style="width: 80%"  class="form-control"
									maxlength="9" datatype="/^[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?$/" errormsg="提现手续费只能输入数字和小数点，长度1~9位" >
								<hyjf:validmessage key="type" label="提现手续费"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="sortId">
							</span>排序</label>
							<div class="col-xs-9">
								<input type="text" placeholder="序号" id="sortId" name="sortId" value="${banksettingForm.sortId}" class="form-control input-sm" style="width: 80%" 
									maxlength="5"  onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  >
							</div>
						</div>
						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-2 col-xs-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 保 存</a> <a
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
	    <script type="text/javascript">
	    	var quickSupport = "${banksettingForm.quickPayment}";
	    </script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/config/banksetting/banksettingInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
