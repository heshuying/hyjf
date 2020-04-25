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
			value="${empty withdrawalstimeconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty withdrawalstimeconfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 银行列表一览 --%>
						<input type="hidden" name="id" id="id"
							value="${withdrawalstimeconfigForm.id }" /> <input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>工作日
							</label>
							<div class="col-xs-7">
								<select id="ifWorkingday" name="ifWorkingday" class="form-control">
									<option value="是"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.ifWorkingday=='是'}">selected="selected"</c:if>>工作日</option>
									<option value="否"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.ifWorkingday=='否'}">selected="selected"</c:if>>非工作日</option>
								</select>
								
								<%-- <input type="text" placeholder="工作日" id="name" name="name"
									value="${withdrawalstimeconfigForm.ifWorkingday}" class="form-control"> --%>
								<hyjf:validmessage key="type" label="工作日"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>提现时间段
							</label>
							<div class="col-xs-3">
								<select id="withdrawalsStart" name="withdrawalsStart" class="form-control">
										<c:forEach items="${times }" var="t" begin="0" step="1" varStatus="status">
											<c:choose>
												<c:when test="${withdrawalstimeconfigForm.withdrawalsStart == t }">
													<option value="${t}"  data-class="fa" selected="selected">${t}</option>
												</c:when>
												<c:otherwise>
													<option value="${t}" data-class="fa">${t}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
								</select>
								
							</div>
							<div class="col-xs-1" class="form-control">
								~
							</div>
							<div class="col-xs-3">
								<select id="withdrawalsEnd" name="withdrawalsEnd" class="form-control">
										<c:forEach items="${times }" var="t" begin="0" step="1" varStatus="status">
											<c:choose>
												<c:when test="${withdrawalstimeconfigForm.withdrawalsEnd == t }">
													<option value="${t}"  data-class="fa" selected="selected">${t}</option>
												</c:when>
												<c:otherwise>
													<option value="${t}" data-class="fa">${t}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>即时提现
							</label>
							<div class="col-xs-7">
								<select id="immediatelyWithdraw" name="immediatelyWithdraw" class="form-control">
									<option value="成功"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.immediatelyWithdraw=='成功'}">selected="selected"</c:if>>成功</option>
									<option value="失败"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.immediatelyWithdraw=='失败'}">selected="selected"</c:if>>失败</option>
									<option value="转T+1"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.immediatelyWithdraw=='转T+1'}">selected="selected"</c:if>>转T+1</option>
								</select>
								
								<%-- <input type="text" placeholder="工作日" id="name" name="name"
									value="${withdrawalstimeconfigForm.ifWorkingday}" class="form-control"> --%>
								<hyjf:validmessage key="type" label="即时提现"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>快速提现
							</label>
							<div class="col-xs-7">
								<select id="quickWithdraw" name="quickWithdraw" class="form-control">
									<option value="成功"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.quickWithdraw=='成功'}">selected="selected"</c:if>>成功</option>
									<option value="失败"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.quickWithdraw=='失败'}">selected="selected"</c:if>>失败</option>
									<option value="转T+1"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.quickWithdraw=='转T+1'}">selected="selected"</c:if>>转T+1</option>
								</select>
								
								<%-- <input type="text" placeholder="工作日" id="name" name="name"
									value="${withdrawalstimeconfigForm.ifWorkingday}" class="form-control"> --%>
								<hyjf:validmessage key="type" label="快速提现"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>一般提现
							</label>
							<div class="col-xs-7">
								<select id="normalWithdraw" name="normalWithdraw" class="form-control">
									<option value="成功"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.normalWithdraw=='成功'}">selected="selected"</c:if>>成功</option>
									<option value="失败"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.normalWithdraw=='失败'}">selected="selected"</c:if>>失败</option>
									<option value="转T+1"  data-class="fa" 
										<c:if test="${withdrawalstimeconfigForm.normalWithdraw=='转T+1'}">selected="selected"</c:if>>转T+1</option>
								</select>
								
								<%-- <input type="text" placeholder="工作日" id="name" name="name"
									value="${withdrawalstimeconfigForm.ifWorkingday}" class="form-control"> --%>
								<hyjf:validmessage key="type" label="一般提现"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="name"> <span
								class="symbol required"></span>状态
							</label>
							<div class="col-xs-9">
								<div>
									<input type="radio" name="status"
										datatype="*" value="1" class="event-categories"
										${withdrawalstimeconfigForm.status == '1' ? 'checked' : ''}>
									<label> 启用 </label> 
									<input type="radio" name="status" 
										datatype="*" value="0" class="event-categories"
										${withdrawalstimeconfigForm.status == '0' ? 'checked' : ''}>
									<label> 禁用 </label>
								</div>
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
			src="${webRoot}/jsp/manager/config/withdrawalstimeconfig/withdrawalstimeconfigInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
