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
	<tiles:putAttribute name="pageTitle" value="安融反欺诈" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty mspapplyForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty mspapplyForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${mspapplyForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>借款人姓名
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="请输入借款人姓名" id="name" name="name"
									value="" class="form-control"
									datatype="s1-50" errormsg="请输入借款人姓名，长度1~50个字符！">
								<hyjf:validmessage key="name" label="借款人姓名"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="identityCard">
								<span class="symbol required"></span>身份证号
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="身份证号" id="identityCard" name="identityCard"
									value="" class="form-control"
									datatype="s1-20" errormsg="身份证号">
								<hyjf:validmessage key="identityCard" label="身份证号代码"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="mobileNo">
								手机号
							</label>
							<div class="col-xs-9">
								<input type="text" placeholder="手机号" id="mobileNo" name="mobileNo"
									value="" class="form-control"
									 errormsg="手机号">
							</div>
						</div>
					<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5"> <span
								class="symbol required"></span>是否自动生成借款信息
							</label>
							<div class="col-xs-9 admin-select">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" name="isAuto" id="isAutoYes"
										datatype="*" value="1" class="event-categories"
										checked="true" ><label for="isAutoYes">是 </label>
										<input type="radio" id="isAutoNo"
										 name="isAuto" datatype="*" value="0"
										class="event-categories">
										<label for="isAutoNo">否 </label>
									<hyjf:validmessage key="isAuto" label="是否与本金共用"></hyjf:validmessage>
								</div>
							</div>
							<hyjf:validmessage key="isAuto" label="是否自动生成借款信息"></hyjf:validmessage>
						</div>
						
						
						<div class="form-group" id="expType-1" >
							<label class="col-xs-3 control-label padding-top-5" for="applyDate">
								<span class="symbol required"></span>申请日期
							</label>
							<div class="col-xs-9  input-daterange datepicker" style="width:75%">
								<input type="text" id="applyDate"
									name="applyDate"
									value="${mspapplyForm.applyDate }" datatype="*"
									maxlength="10"  />
								
							</div>
							<hyjf:validmessage key="expirationDateStr" label="申请日期"></hyjf:validmessage>
						</div>	
						
						
						<div id="biaodiv" class="form-group">
									<label class="col-xs-3 control-label" for="biao">
										选择标的信息
									</label>
									<div class="col-xs-9 admin-select">
										<select id="biao" name="biao"
											class="form-select2">
											<option value="0">请选择</option>
											<c:forEach items="${mspapplyForm.configureList}" var="item">
												<option value="${item.id}|${item.loanType}|${item.loanMoney}|${item.loanTimeLimit}|${item.creditAddress}">${item.configureName }</option>
											</c:forEach>
										</select>
									</div>
						</div>
						
										
						<div class="form-group">
									<label class="col-xs-3 control-label" for="loanType">
										<span class="symbol required"></span>借款类型
									</label>
									<div class="col-xs-9 admin-select">
										<select  id="loanType" name="loanType"
											class="" style="width: 300px"  disabled="disabled">
												<option value="01">经营</option>
												<option value="02">消费</option>
												<option value="99">其他</option>
										</select>
									</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="loanMoney">
								<span class="symbol required"></span>借款金额(元)
							</label>
							<div class="col-xs-9">
								<input  type="text" placeholder="借款金额" id="loanMoney" name="loanMoney"
									value="" class="form-control"
									datatype="s1-20" errormsg="借款金额" disabled="disabled">
								<hyjf:validmessage key="type" label="借款金额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label  class="col-xs-3 control-label padding-top-5" for="loanTimeLimit">
								<span class="symbol required"></span>借款期数(月)
							</label>
							<div class="col-xs-9">
								<input  type="text" placeholder="借款期数" id="loanTimeLimit" name="loanTimeLimit"
									value="" class="form-control"
									datatype="s1-20" errormsg="借款期数" disabled="disabled">
								<hyjf:validmessage key="type" label="借款期数"></hyjf:validmessage>
							</div>
						</div>
						<div   class="form-group">
									<label class="col-xs-3 control-label" for="creditAddress">
										<span class="symbol required"></span>借款城市
									</label>
									<div class="col-xs-9 admin-select">
										<select  id="creditAddress" name="creditAddress"
											class="form-select2" disabled="disabled">
											<c:forEach items="${mspapplyForm.regionList}" var="item">
												<option value="${item.regionId}">${item.regionName }</option>
											</c:forEach>
										</select>
									</div>
									<input type="hidden" name="configureId"  id="configureId" value="" >
						</div>
						
						<div class="form-group margin-bottom-0">
							<div class="col-xs-offset-2 col-xs-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 发送请求</a> <a
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
			src="${webRoot}/jsp/manager/users/mspapply/mspapplyInfo.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
