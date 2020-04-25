<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>


<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="" scope="request"></c:set>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
var webPath = "${ctx}";
</script>

<!-- 画面功能路径(ignore) -->
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="操作页面" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty operationreportCommonForm.operationReport.id ? '添加' : '修改'}"></c:set>
		    <!-- start -->
		    
			<div class="panel-body" style="margin: 0 auto;">
				<form id="mainForm" action="insertAction" method="post" role="form" class="form-horizontal">
					<%-- 运营报告 --%>
						    <input type="hidden" name="id" id="id" value="${operationreportCommonForm.operationReport.id }" />
							<input type="hidden" name="isRelease" id="isRelease" value="${operationreportCommonForm.operationReport.isRelease }" />
<%-- 							<input type="hidden" name="operationReportType" id="operationReportType" value="${operationreportCommonForm.operationReport.isRelease }" /> --%>
							
							  <div style="display:inline-block">运营报告类型：</div>
							 	  <!-- Unnamed (下拉列表框) -->
								<div style="display:inline-block;margin:0px 0px 0px 50px" id="u873" class="ax_下拉列表框">
										<c:choose> 
										 <c:when test="${operationreportCommonForm==null}">
											<select name="operationReportType" id="operationReportType" class="form-select2"　style="width:200%" data-placeholder="月度运营报告" >
												<c:forEach items="${selectList }" var="bean" begin="0" step="1" varStatus="status">
													<option value="${bean.code }"><c:out value="${bean.name }"></c:out></option>
												</c:forEach>
											</select>
												<div id="monthDiv" style="display:inline-block;margin:8px 0px 0px 78px">
													<span style="margin: 0px 75px 0px -46px;">月份：</span><input type="text" id="month"   class="monthStyle" ></input><span>月</span>
												</div>
												<div id="yearDiv" style="display:inline-block;margin:8px 0px 0px 119px" > 
													<span style="margin: 0px 75px 0px -46px;">年度：</span><input type="text" id="year"   style="margin: 0px 0px 0px 15px;" class="yearStyle" ></input><span>年</span>
												</div>
										</c:when> 
										<c:when test="${operationreportCommonForm!=null&&operationreportCommonForm.operationReport.operationReportType!=1}">
											<select name="operationReportType" id="operationReportType"  class="form-select2"　style="width:200%" disabled="disabled" >
												<c:forEach items="${selectList }" var="bean" begin="0" step="1" varStatus="status">
													<option value="${bean.code }"
															<c:if test="${bean.code eq reportType}">selected="selected"</c:if>>
															<c:out value="${bean.name }"></c:out></option>
												</c:forEach>
											</select>
												
												<div id="yearDiv" style="display:inline-block;margin:8px 0px 0px 119px" > 
													<span style="margin: 0px 75px 0px -46px;">年度：</span><input type="text" id="year" value="${operationreportCommonForm.operationReport.year }"  disabled="disabled" style="margin: 0px 0px 0px 15px;" class="yearStyle" ></input><span>年</span>
												</div>
										</c:when>
										<c:otherwise >
										
											<select name="operationReportType" id="operationReportType" class="form-select2"　 style="width:200%;" disabled="disabled"  >
												<c:forEach items="${selectList }" var="bean" begin="0" step="1" varStatus="status">
													<option value="${bean.code }"
															<c:if test="${bean.code eq reportType}">selected="selected"</c:if>>
														<c:out value="${bean.name }"></c:out></option>
												</c:forEach>
											</select>
													
												<div id="monthDiv" style="display:inline-block;margin:8px 0px 0px 78px">
													<span style="margin: 0px 75px 0px -46px;">选择月份：</span><input type="text" id="month" name="month" value="${operationreportCommonForm.monthlyOperationReport.month }" disabled="disabled" style="margin:0px 0px 0px 30px;" class="monthStyle"></input><span>月</span>
												</div>

												<div id="yearDiv" style="display:inline-block;margin:8px 0px 0px 119px" >
													<span style="margin: 0px 75px 0px -46px;">年度：</span><input type="text" id="year" name="year" value="${operationreportCommonForm.operationReport.year }" disabled="disabled" style="margin: 0px 0px 0px 15px;" class="yearStyle" ></input><span>年</span>
												</div>

										</c:otherwise>
									</c:choose> 
								  </div>
								<div id="myiframe">
                                    <iframe style='height:80%;width:100%;' frameborder="0" id="myFrame" name='myFrame' src=''></iframe>
								</div>
	  			</form>
			</div>

			<!-- end -->
			</tiles:putAttribute>

			<%-- 画面的CSS (ignore) --%>
			<%-- 画面的CSS (ignore) --%>
			<tiles:putAttribute name="pageCss" type="string">
				<link
					href="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda-themeless.min.css"
					rel="stylesheet" media="screen">
				<style>
			
.table-heads .table-striped {
	margin-bottom: 0;
}

.table-body {
	position: relative;
}

.table-body .vertical-align-top {
	vertical-align: top !important;
}

.table-body .table-striped {
	border-top: none;
	margin-bottom: 0;
}

.table-body .table-striped tr:first-child td {
	border-top: 0;
}

.table-body .form-group {
	margin-bottom: 0;
}

.thumbnails-wrap {
	background: #f5f5f5;
	border: 1px solid #ccc;
	padding: 3px;
	display: inline-block;
}

.thumbnails-wrap img {
	min-width: 35px;
	max-width: 70px;
	height: 22px;
}

.popover {
	max-width: 500px;
}

.popover img {
	max-width: 460px;
}


.clip-radio {
	margin-top: 4px
}
</style>
			</tiles:putAttribute>

			<%-- JS全局变量定义、插件 (ignore) --%>
			<tiles:putAttribute name="pageGlobalImport" type="string">
				<!-- Form表单插件 -->
				<%@include file="/jsp/common/pluginBaseForm.jsp"%>
				<script
					src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
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
				<script type="text/javascript"
					src="${themeRoot}/assets/js/common.js"></script>
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
<!-- 				<script type='text/javascript' -->
<%-- 					src="${webRoot}/jsp/manager/label/hjhlabelInfo.js"></script> --%>
					<script type='text/javascript'
						src="${webRoot}/jsp/manager/content/contentoperationreport/monthoperationreport.js"></script>
				<script type="text/javascript">
			//日历选择器																						
			$('.datepicker').datepicker({
				autoclose : true,
				todayHighlight : true
			});
			$(function(){
               	var src = "${ctx}/manager/content/operationreport/monthinit?month="+$("#month").val()+"&id="+$("#id").val()+"&year="+$("#year").val()+"";
                $("#myFrame").attr("src",src+"?time="+new Date().getTime());

            });
			
			
		</script>
			</tiles:putAttribute>
</tiles:insertTemplate>
