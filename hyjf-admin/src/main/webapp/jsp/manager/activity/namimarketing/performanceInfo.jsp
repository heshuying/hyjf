<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
                      flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="单笔投资详情" />

    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <div class="panel panel-white" style="text-align:center">
            <div class="panel-body" style="margin: -30px 0px 0px 60px;">
                <div class="center" style="text-align:center;margin: 40px;">
                <c:if test="${active != null}">
                    <div class="row center" style="position: relative;left: 50%;transform: translateX(-50%);">
                        <div class="row center" >
                            <div class="form-group" style="width:80px;height:60px;float:left;display:inline;margin-bottom: 0px;">
                                <img class="activityCon_as"  style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${active == 1}">${ctx}/hyjf-admin/image/namimarketing/AA.png</c:if><c:if test="${active >1}">${ctx}/hyjf-admin/image/namimarketing/A.png</c:if>">
                            </div>
                            <div class="form-group" style="width:200px;height:60px;float:left;display:inline;margin-bottom: 0px;padding-top: 10px;">
                                <p style="font-weight:bold;float:left;"> 账户名：${A.userName}</p>
                                <p style="font-weight:bold;float:left;">获得返现金额：${A.returnAmount}元</p>
                            </div>
                        </div>
                        <div class="row center" >
                            <c:if test="${active >= 2}">
                                <div class="form-group" style="width:80px;height:40px;float:left;margin-bottom: 0px;">
                                    <img class="activityCon_as"  style="width: 60px;height: 40px; " src="${ctx}/hyjf-admin/image/namimarketing/1.png">
                                </div>
                            </c:if>
                        </div>
                        <div class="row center">
                            <c:if test="${active >= 2}">
                                <div class="form-group" style="width:80px;height:60px;float:left;display:inline;margin-bottom: 0px;">
                                    <img class="activityCon_as"  style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${active == 2}">${ctx}/hyjf-admin/image/namimarketing/BB.png</c:if><c:if test="${active >2}">${ctx}/hyjf-admin/image/namimarketing/B.png</c:if>">
                                </div>
                                <div class="form-group" style="width:200px;height:60px;float:left;display:inline;margin-bottom: 0px;padding-top: 10px;">
                                    <p style="font-weight:bold;float:left;"> 账户名：${B.userName}</p>
                                    <c:if test="${active > 2}">
                                        <p style="font-weight:bold;float:left;">获得返现金额：${B.returnAmount}元</p>
                                    </c:if>
                                </div>
                            </c:if>
                        </div>
                        <div class="row center">
                            <c:if test="${active >= 3}">
                                <div class="form-group" style="width:80px;height:40px;float:left;margin-bottom: 0px;">
                                    <img class="activityCon_as"  style="width: 60px;height: 40px; " src="${ctx}/hyjf-admin/image/namimarketing/1.png">
                                </div>
                            </c:if>
                        </div>
                        <div class="row center">
                            <c:if test="${active >= 3}">
                                <div class="form-group" style="width:80px;height:60px;float:left;display:inline;margin-bottom: 0px;">
                                    <img class="activityCon_as"  style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${active == 3}">${ctx}/hyjf-admin/image/namimarketing/CC.png</c:if><c:if test="${active >3}">${ctx}/hyjf-admin/image/namimarketing/C.png</c:if>">
                                </div>
                                <div class="form-group" style="width:200px;height:60px;float:left;display:inline;margin-bottom: 0px;padding-top: 10px;">
                                    <p style="font-weight:bold;float:left;"> 账户名：${C.userName}</p>
                                    <c:if test="${active > 3}">
                                        <p style="font-weight:bold;float:left;">获得返现金额：${C.returnAmount}元</p>
                                    </c:if>
                                </div>
                            </c:if>
                        </div>
                        <div class="row center">
                            <c:if test="${active >= 4}">
                                <div class="form-group" style="width:80px;height:40px;float:left;margin-bottom: 0px;">
                                    <img class="activityCon_as"  style="width: 60px;height: 40px; " src="${ctx}/hyjf-admin/image/namimarketing/1.png">
                                </div>
                            </c:if>
                        </div>
                        <div class="row center">
                            <c:if test="${active == 4}">
                                <div class="form-group" style="width:80px;height:60px;float:left;display:inline;margin-bottom: 0px;">
                                    <img class="activityCon_as"  style="width: 60px;height: 60px; border-radius: 45px;" src="${ctx}/hyjf-admin/image/namimarketing/DD.png">
                                </div>
                                <div class="form-group" style="width:200px;height:60px;float:left;display:inline;margin-bottom: 0px;padding-top: 10px;">
                                    <p style="font-weight:bold;float:left;"> 账户名：${D.userName}</p>
                                </div>
                            </c:if>
                        </div>
                    </div>
                        <div class="row center" style="height:20px;">
                            <div class="form-group" style="width:200px;height:20px;float:left;display:inline;margin-bottom: 0px;">
                                <p style="font-weight:bold;float:left;">
                                    <c:if test="${active == 2}">单笔返现金额：${B.returnAmount}元</c:if>
                                    <c:if test="${active == 3}">单笔返现金额：${C.returnAmount}元</c:if>
                                    <c:if test="${active == 4}">单笔返现金额：${D.returnAmount}元</c:if>
                                </p>
                            </div>
                        </div>
                </c:if>
                </div>
                <div class="row center" style="text-align:center;margin: 40px;height:20px;">
                    <div class="form-group" style="width:200px;height:20px;float:left;display:inline;">
                        <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        <a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
                    </div>
                </div>
            </div>
        </div>
    </tiles:putAttribute>

    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
        <%--<style>--%>
            <%--.rowcenter{--%>
                <%--border: 1px solid red;--%>
                <%--padding: 5px;--%>
            <%--}--%>
        <%--</style>--%>
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
                src="${webRoot}/jsp/manager/activity/namimarketing/performanceInfo.js"></script>
    </tiles:putAttribute>

</tiles:insertTemplate>
