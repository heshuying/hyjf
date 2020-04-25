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
    <tiles:putAttribute name="pageTitle" value="管理" />

    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <c:set var="jspEditType"
               value="${empty recode.id ? '添加' : '修改'}"></c:set>
        <div class="panel panel-white" style="margin: 0">
            <div class="panel-body" style="margin: 0 auto">
                <div class="panel-scroll height-430">
                    <form id="mainForm" action="${empty recode.id ? 'insertAction' : 'updateAction'}" method="post" role="form" class="form-horizontal" target="_parent">
                            <%-- 列表一览 --%>
                        <input type="hidden" name="id" id="id" value="${recode.id }" />
                                <%--<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />--%>
                                <%--<input type="hidden" id="success" value="${success}" />--%>
                        <div class="form-group">
                            <label class="col-xs-2 control-label padding-top-5" for="titleName"> <span
                                    class="symbol required"></span>标题名称
                            </label>
                            <div class="col-xs-10">
                                <input type="text" placeholder="标题名称" id="titleName" name="titleName"
                                       value="${recode.titleName}" class="form-control"
                                       datatype="*" nullmsg="请输入标题名称！">
                                <hyjf:validmessage key="type" label="名称"></hyjf:validmessage>
                                <span style="color:red;" id="n_error"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-2 control-label padding-top-5" for="uniqueIdentifier"> <span
                                    class="symbol required"></span>唯一标识
                            </label>
                            <div class="col-xs-10">
                                <c:if test="${recode.id == null}">
                                    <input type="text" placeholder="唯一标识" id="uniqueIdentifier" name="uniqueIdentifier"
                                           datatype="*" nullmsg="请输入唯一标识！"value="${recode.uniqueIdentifier}" class="form-control">
                                </c:if>
                                <c:if test="${recode.id != null}">
                                    <input type="text" placeholder="唯一标识" id="uniqueIdentifier" name="uniqueIdentifier" readonly ="readonly"
                                           datatype="*" nullmsg="请输入唯一标识！"value="${recode.uniqueIdentifier}" class="form-control">
                                </c:if>
                                <hyjf:validmessage key="type" label="名称"></hyjf:validmessage>
                                <span style="color:red;" id="v_error"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-2 control-label padding-top-5" for="statisticalMethod"> <span
                                    class="symbol"></span>统计规则
                            </label>
                            <div class="col-xs-10">
								<textarea maxlength="240" placeholder="统计规则长度不能超过240个字符！" id="statisticalMethod"
                                          name="statisticalMethod" class="form-control limited">${recode.statisticalMethod}</textarea>
                                <span style="color:red;" id="s_error"></span>
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
        <script type='text/javascript'
                src="${webRoot}/jsp/manager/content/statisticsway/statisticswayinfo.js"></script>
    </tiles:putAttribute>

</tiles:insertTemplate>
