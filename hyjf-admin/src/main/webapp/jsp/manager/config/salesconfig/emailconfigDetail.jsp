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
		<!-- start -->
		<div id="container">
			<ul>
				<li>
					<p>
						<span class="mail-name">业务名称:</span>
						<span class="mail-content">${EmailRecipientForm.businessName}</span>
					</p>
					<p>
						<span class="mail-state">状态:</span>
						<span class="mail-content">
							<c:if test="${EmailRecipientForm.status == '1' }">
								<c:out value="有效"></c:out>
							</c:if>
							<c:if test="${EmailRecipientForm.status == '2' }">
								<c:out value="无效"></c:out>
							</c:if>
						</span>
					</p>
				</li>
				<li>
					<p>
						<span class="mail-create-time">创建时间:</span>
						<span class="mail-state">${EmailRecipientForm.addtime}</span>
					</p>
					<p>
						<span class="mail-create-person">创建操作人:</span>
						<span class="mail-content">${EmailRecipientForm.createName}</span>
					</p>
				</li>
				<li>
					<p>
						<span class="mail-update-time">更新时间:</span>
						<span class="mail-content">${EmailRecipientForm.formatUpdatetime}</span>
					</p>
					<p>
						<span class="mail-update-person">更新操作人:</span>
						<span class="mail-content">${EmailRecipientForm.updateName}</span>
					</p>
				</li>
				<li class="mail-line">
				</li>
				<li>
					<p>
						<span class="mail-send-time">邮件发送时间:</span>
						<span class="mail-content">
							<c:if test="${EmailRecipientForm.timePoint == '1' }">
								<c:out value="每个工作日"></c:out>
							</c:if>
							<c:if test="${EmailRecipientForm.timePoint == '2' }">
								<c:out value="每天"></c:out>
							</c:if>
							<c:if test="${EmailRecipientForm.timePoint == '3' }">
								<c:out value="每月第一个工作日"></c:out>
							</c:if>
								<c:out value="${EmailRecipientForm.time}"></c:out>
						</span>
					</p>
				</li>
				<%--<li>
					<p>
						<span class="mail-address">收件人邮箱:</span>
						<span class="mail-content">${EmailRecipientForm.email} </span>
					</p>
				</li>--%>

                <c:forEach items="${EmailRecipientForm.emails}" var="record" begin="0" step="1" varStatus="status">
                    <li>
                        <p>
                            <c:if test="${status.count eq 1}">
                                <span class="mail-address">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;收件人邮箱:</span>
                            </c:if>
                            <c:if test="${status.count ne 1}">
                                <span class="mail-address"></span>
                            </c:if>
                            <span class="mail-content"><c:out value="${record}"></c:out></span>
                        </p>
                    </li>
                </c:forEach>
			</ul>
			<div class="col-md-4">
				<div class="row">
					<a class="btn btn-o btn-primary btn-sm hidden-xs fn-return"
					   data-toggle="tooltip" data-placement="bottom" data-original-title="返回列表">返回列表<i class="fa "></i></a>
				</div>
			</div>
		</div>
		<%--<div class="mail-btn-back">
			返回列表页
		</div>--%>
		<!-- end -->
			
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda-themeless.min.css" rel="stylesheet" media="screen">
		<style>
			#container{
				width:600px;
				height:400px;
				padding:20px;
				background:#fff;
				color:#000;
				border:1px solid #333;
				font-size:16px;
			}
			#container ul{
				margin:0;
				padding:0;
			}
			#container ul li{
				list-style: none;
				display: flex;
				justify-content: space-between;
			}
			#container ul li.mail-line{
				width:100%;
				height:1px;
				background:#333;
			}
			#container ul li p span:nth-child(1){
				display: inline-block;
				width: 120px;
				text-align:right;
			}
			#container ul li p span:nth-child(2){
				padding-left:20px;
			}
			.mail-btn-back{
				width:120px;
				line-height:40px;
				text-align:center;
				font-size:14px;
				color:#000;
				border:1px solid #333;
				border-radius:5px;
				margin-top:20px;
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
		<%--<script type='text/javascript'
			src="${webRoot}/jsp/manager/config/salesconfig/emailconfigInfo.js"></script>--%>
		<script type='text/javascript'
				src="${webRoot}/jsp/manager/config/salesconfig/emailconfigDetail.js"></script>

		<script type="text/javascript">
</script>
	</tiles:putAttribute>
</tiles:insertTemplate>
