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
	<tiles:putAttribute name="pageTitle" value="添加协议模板" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType"
			value="${empty protocoltemplateForm.protocolTemplate.id  ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty protocoltemplateForm.protocolTemplate.id  ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal" target="_parent">
						<%-- 列表一览 --%>
						<input type="hidden" name="protocolTemplate.id" id="protocolTemplateId"
							value="${protocoltemplateForm.protocolTemplate.id }" />
							<input type="hidden"
							name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input
							type="hidden" id="success" value="${success}" />
							<c:if test="${ protocoltemplateForm.protocolTemplate.id != null }">
								<div class="form-group">
									<label class="col-xs-2 control-label padding-top-5" > <span
											class="symbol required"></span>协议ID
									</label>
									<div class="col-xs-10">
										<input type="text" placeholder="协议ID" id="protocolId" name="protocolTemplate.protocolId"
											   value="${protocoltemplateForm.protocolTemplate.protocolId}" class="form-control" readonly="readonly">
									</div>
								</div>
							</c:if>

							<div class="form-group">
								<label class="col-xs-2 control-label padding-top-5" for="protocolName"> <span
										class="symbol required"></span>协议模板名称
								</label>
								<div class="col-xs-10">
									<c:if test="${ protocoltemplateForm == null }">
										<input type="text" placeholder="协议模板名称" id="protocolName" name="protocolTemplate.protocolName"
											   value="${protocoltemplateForm.protocolTemplate.protocolName}" class="form-control"
											   datatype="*" >
									</c:if>
									<c:if test="${ protocoltemplateForm.protocolTemplate.id != null }">
										<input type="text" placeholder="协议模板名称" id="protocolName" name="protocolTemplate.protocolName"
											   value="${protocoltemplateForm.protocolTemplate.protocolName}" class="form-control" readonly="readonly"
											   datatype="*" >
									</c:if>
									<hyjf:validmessage key="protocolName"></hyjf:validmessage>
									<span style="color:red;" id="n_error_message"></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-2 control-label padding-top-5" for="displayName"> <span
										class="symbol required"></span>前台展示名称
								</label>
								<div class="col-xs-10">
									<input type="text" placeholder="前台展示名称" id="displayName" name="protocolTemplate.displayName"
										   value="${protocoltemplateForm.protocolTemplate.displayName}" class="form-control"
										   datatype="*" >
										<span style="color:red;" id="d_error_message"></span>
									<input type="hidden" id="oldDisplayName" name="oldDisplayName" value="${protocoltemplateForm.protocolTemplate.displayName}" >
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-2 control-label padding-top-5" for="protocolType"> <span
										class="symbol required"></span>协议类别
								</label>
								<div class="col-xs-10">
									<c:if test="${ protocoltemplateForm == null }">
										<select name="protocolTemplate.protocolType" id="protocolType" class="form-control underline  form-select2" data-placeholder="请选择协议类别..." datatype="*">
											<option value="请选择协议类别...">请选择协议类别...</option>
											<c:forEach items="${selectList }" var="protocolType" begin="0" step="1" varStatus="status">
												<option value="${protocolType.code }" <c:if test="${protocolType.code eq protocoltemplateForm.protocolTemplate.protocolType}">selected="selected"</c:if>><c:out value="${protocolType.name }"></c:out></option>
											</c:forEach>
										</select>
									</c:if>
									<c:if test="${ protocoltemplateForm.protocolTemplate.id != null }">
									<input type="text" placeholder="协议类别" id="protocolType" name="protocolTemplate.protocolType"
										   value="${protocoltemplateForm.protocolTemplate.protocolType}" class="form-control" readonly="readonly"
										   datatype="*" >
									</c:if>
									<span style="color:red;" id="e_error_message"></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-2 control-label padding-top-5" for="versionNumber"> <span
										class="symbol required"></span>协议版本号
								</label>
								<div class="col-xs-10">
									<input type="text" placeholder="协议版本号" id="versionNumber" name="protocolTemplate.versionNumber"
										   value="${protocoltemplateForm.protocolTemplate.versionNumber}" class="form-control"
										   datatype="*" >
									<span style="color:red;" id="v_error_message"></span>
									<input type="hidden" name="checkedVersionId" class="checkedVersionId"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-xs-2 control-label padding-top-5" for="imgurl"> <span
										class="symbol required"></span>协议上传
								</label>
								<div class="fileinput fileinput-new col-xs-10" data-provides="fileinput">
									<!-- 显示文件 -->
									<%--<div class="fileinput-preview fileinput-exists thumbnail" height="20" width="20"></div>--%>
									<!-- 文件路径 -->
									<div class = "purMargin">
										<input type="text" readonly="r"
											   name="protocolTemplate.protocolUrl" id="protocolUrl" value="${protocoltemplateForm.protocolTemplate.protocolUrl }" placeholder="上传文件路径" datatype="*" />
										<span style="color:red;" id="v_error_pdf"></span>
									</div>
									<!-- 按钮管理 -->
									<div class="user-edit-image-buttons">
										<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传文件</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span>
											<input type="file" name="file" id="fileupload" class= "fileupload" accept="application/pdf">
										</span>
									</div>
								</div>
							</div>

							<div class="form-group">
								<label class="col-xs-2 control-label padding-top-5" for="describe"> <span
										class="symbol"></span>备注
								</label>
								<div class="col-xs-10">
							<textarea maxlength="200" placeholder="备注" id="remarks"
									  name="protocolTemplate.remarks" class="form-control limited">${protocoltemplateForm.protocolTemplate.remarks}</textarea>
								</div>
							</div>
					</form>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-2 col-xs-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 保 存</a>
								<%--<a--%>
								<%--class="btn btn-o btn-primary fn-Cancel"><i--%>
								<%--class="fa fa-close"></i> 取 消</a>--%>
						</div>
					</div>
					<c:if test="${ protocoltemplateForm.protocolTemplate.id != null }">
						<div class="form-group">
							<label class="col-xs-3 control-label padding-top-5" for="describe"> <span
									class="symbol"></span>协议更新历史
							</label>
						</div>

						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width:70px;" />
							</colgroup>
							<thead>
							<tr>
								<th class="center">是否启用</th>
								<th class="center">版本</th>
								<th class="center">文件名称</th>
								<th class="center">更新时间</th>
								<th class="center">备注</th>
								<th class="center">更新人</th>
							</tr>
							</thead>
							<tbody id="roleTbody">
							<c:forEach items="${protocoltemplateForm.protocolVersion }" var="record" begin="0" step="1" varStatus="status">
								<tr>
									<input type="hidden" name="protocolVersion.id" value="${record.id }"/>
									<td class="center">
										<input type="hidden" name="hiddenProtocolVersionId" value="${record.id }"/>
										<input type="hidden" name="hiddenProtocolUrl" value="${record.protocolUrl }"/>
										<input type="hidden" name="hiddenVersionNumber" value="${record.versionNumber }"/>
										<input type="hidden" name="hiddenRemarks" value="${record.remarks }"/>
										<input type="hidden" name="hiddenDisplayName" value="${record.displayName }"/>
										<input type="radio" class="displayFlag" name="protocolVersion.displayFlag" value="${ record.displayFlag}" datatype="*" ${ record.displayFlag  eq 1 ? 'checked' : ''} >
										</input>
									</td>
									<td class="center"><c:out value="${record.versionNumber }"></c:out></td>
									<td class="center"><c:out value="${record.protocolName }"></c:out></td>
									<td class="center"><c:out value="${record.time }"></c:out></td>
									<td class="center"><c:out value="${record.remarks }"></c:out></td>
									<td class="center"><c:out value="${record.userName }"></c:out></td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</c:if>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
			/*.thumbnail{*/
				/*height: 100px;*/
				/*width:100px;*/
			/*}*/
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
		<script type='text/javascript'
			src="${webRoot}/jsp/manager/config/protocol/protocoltemplateInfo.js"></script>
	</tiles:putAttribute>

</tiles:insertTemplate>
