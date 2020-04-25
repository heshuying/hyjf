<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>

<%--<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">--%>
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="推送管理" />

    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <c:set var="jspEditType" value="${empty appPushManageForm.id ? '添加' : '修改'}"></c:set>
        <div class="container-fluid container-fullw">
            <div class="row">
                <div class="col-md-12">
                    <h2></h2>
                    <p>
                        <i class="ti-info-alt text-primary"></i> 请在这里填写您本次提App推送管理的基本信息.
                    </p>
                    <hr>
                    <form id="mainForm"	action="${empty appPushManageForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" >
                            <%-- 推送管理列表一览 --%>
                        <input type="hidden" name="id" id="id" value="${appPushManageForm.id }" />
                        <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
                        <input type="hidden" id="success" value="${success}" />

                        <div class="row">
                            <div class="col-md-12">
                                <div class="errorHandler alert alert-danger no-display">
                                    <i class="fa fa-times-sign"></i> You have some form errors. Please check below.
                                </div>
                                <div class="successHandler alert alert-success no-display">
                                    <i class="fa fa-ok"></i> Your form validation is successful!
                                </div>
                            </div>
                            <div class="col-md-6">

                                <div class="form-group">
                                    <label class="control-label" for="title">
                                        <span class="symbol required"></span>标题名称
                                    </label>
                                    <input type="text" placeholder="标题名称" id="title" name="title"	value="${appPushManageForm.title}" class="form-control" datatype="*2-50" errormsg="标题名称 只能是字符汉字，长度2~50个字符！" maxlength="50">
                                    <hyjf:validmessage key="title" label="标题名称"></hyjf:validmessage>
                                </div>

                                <div class="form-group">
                                    <label class="control-label">
                                        <span class="symbol required" aria-required="true"></span>跳转类型
                                    </label>
                                    <div class="radio clip-radio radio-primary ">
                                        <input type="radio" id="jumpTypeOn" name="jumpType" datatype="*" value="0" class="event-categories" <c:if test="${appPushManageForm.jumpType == 0}">checked="checked"</c:if>>
                                        <label for="jumpTypeOn"> 原生 </label>
                                        <input type="radio" id="jumpTypeOff" name="jumpType" datatype="*" value="1" class="event-categories" <c:if test="${appPushManageForm.jumpType == 1}">checked="checked"</c:if>>
                                        <label for="jumpTypeOff"> H5 </label>
                                    </div>
                                    <hyjf:validmessage key="jumpType" label="跳转类型"></hyjf:validmessage>
                                </div>

                                <div class="form-group h5-class" >
                                    <label class="control-label">
                                        <span class="symbol required" aria-required="true"></span>跳转内容
                                    </label>
                                    <div class="radio clip-radio radio-primary ">
                                        <input type="radio" id="diyOn" name="jumpContent" datatype="*" value="0" class="event-categories" <c:if test="${appPushManageForm.jumpContent==1 or appPushManageForm.jumpContent==0}">checked="checked"</c:if>>
                                        <label for="diyOn"> URL </label>
                                        <input type="radio" id="diyOff" name="jumpContent" datatype="*" value="1" class="event-categories" <c:if test="${appPushManageForm.jumpContent==2}">checked="checked"</c:if>>
                                        <label for="diyOff"> 自定义 </label>
                                    </div>
                                </div>

                                <div class="form-group url-class">
                                    <label class="control-label" for="title">
                                        <span class="symbol required"></span>URL
                                    </label>
                                    <input type="text" placeholder="跳转URL" id="jumpUrl" name="jumpUrl"	value="${appPushManageForm.jumpUrl}" class="form-control" datatype="*2-50" errormsg="跳转URL不能为空" maxlength="50">
                                </div>
                                <div class="form-group">
                                    <label class="control-label" for="title">
                                        <span class="symbol required"></span>排序
                                    </label>
                                    <input type="text" placeholder="排序:只能为数字" id="order" name="order"	value="${appPushManageForm.order}" class="form-control" datatype="*1-3" errormsg="排序只能为数字" maxlength="50">
                                </div>

                                <div class="form-group">
                                    <label class="control-label">
                                        <span class="symbol required" aria-required="true"></span>状态
                                    </label>
                                    <div class="radio clip-radio radio-primary ">
                                        <input type="radio" id="statusOn" name="status" datatype="*" value="0" class="event-categories" <c:if test="${appPushManageForm.status == 0}">checked="checked"</c:if>>
                                        <label for="statusOn"> 禁用 </label>
                                        <input type="radio" id="statusOff" name="status" datatype="*" value="1" class="event-categories" <c:if test="${appPushManageForm.status == 1}">checked="checked"</c:if>>
                                        <label for="statusOff"> 启用 </label>
                                    </div>
                                    <hyjf:validmessage key="status" label="推送管理"></hyjf:validmessage>
                                </div>

                                <div class="form-group">
                                    <label class="control-label" for="title">
                                        <span class="symbol required"></span>时间
                                    </label>
                                    <div class="input-group ">


                                        <input type="text" name="timeStartDiy" id="timeStartDiy" class="form-control underline" value="<hyjf:date value="${appPushManageForm.timeStart}"></hyjf:date>" datatype="*1-10" errormsg="时间必填！" maxlength="50" onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd', errDealMode: 1})"/>&nbsp;&nbsp;至&nbsp;&nbsp;
                                        <input type="text" name="timeEndDiy" id="timeEndDiy" class="form-control underline" value="<hyjf:date value="${appPushManageForm.timeEnd}"></hyjf:date>" datatype="*1-10" errormsg="时间必填！" maxlength="50" onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd', errDealMode: 1})"/>
                                    </div>
                                </div>

                            </div>
                            <div class="col-md-6">
                                <div class="row  h5-class">
                                    <div class="col-md-8">
                                        <div class="form-group  custom-class">
                                            <label class="col-xs-2 control-label padding-top-5" for="imgurl"> <span
                                                    class="symbol"></span>文章图片
                                            </label>
                                            <div class="fileinput fileinput-new col-xs-6" data-provides="fileinput">
                                                <!-- 缺省图片 -->
                                                <div class="fileinput-new thumbnail">
                                                    <img width="160" height="120" src="${fileDomainUrl}${appPushManageForm.thumb}" alt="">
                                                </div>
                                                <!-- 显示图片 -->
                                                <div class="fileinput-preview fileinput-exists thumbnail"></div>
                                                <!-- 图片路径 -->
                                                <div class = "purMargin">
                                                    <input type="text" readonly="readonly"
                                                           name="thumb" id="imgurl" value="${appPushManageForm.thumb}" placeholder="上传图片路径"/>
                                                </div>
                                                <!-- 按钮管理 -->
                                                <div class="user-edit-image-buttons">
													<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span>
														<input type="file" name="file" id="fileupload" class= "fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
													</span>
                                                    <a href="#" class="btn fileinput-exists btn-red" data-dismiss="fileinput" onclick='$("#imgurl").val("")'>
                                                        <i class="fa fa-times"></i> 删除
                                                    </a>
                                                </div>
                                                    <%--<hyjf:validmessage key="type" label="imgurl"></hyjf:validmessage>--%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 文本域开始 -->
                        <div class="row  h5-class" >
                            <div class="col-md-12">
                                <div class="form-group custom-class">
                                    <label class="control-label" > <span class="symbol required"></span>推送内容
                                    </label>
                                    <div>
                                        <textarea name="content" id="content" placeholder="推送内容" maxlength="50000" class="form-control tinymce" cols="10" rows="5" datatype="*1-50000" errormsg="推送内容最多只能输入50000个文字！" errormsg="未填写文章内容">${appPushManageForm.content }</textarea>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <div>
                                    <span class="symbol required"></span>必须填写的项目
                                    <hr>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-8">
                                <p>
                                    点击【提交保存】按钮，保存当前的填写的资料。
                                </p>
                            </div>
                            <div class="col-md-4">
                                <a class="btn btn-primary fn-Confirm  pull-right"><i
                                        class="fa fa-check"></i> 提交保存</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- end -->
    </tiles:putAttribute>

    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
        <style>
            #acTime{
                margin-left:15px;
            }
            .input-group.input-daterange.datepicker span{
                padding-left:15px;
            }
        </style>
    </tiles:putAttribute>
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
        <script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
        <script type='text/javascript' src="${webRoot}/jsp/app/maintenance/pushmanage/pushmanageInfo.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
