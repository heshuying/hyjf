<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="worldCupactivityConf:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="世界杯活动配置" />

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">世界杯活动决战赛配置</h1>
            <span class="mainDescription">本功能可以保存修改。</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <shiro:hasPermission name="worldCupactivityConf:VIEW">
                            <li ><a href="${webRoot}/manager/activity/worldCupActivityconfiguration/init">决战赛球队配置</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="worldCupactivityConf:VIEW">
                            <li class="active"><a href="${webRoot}/manager/activity/worldCupActivityconfiguration/matchInit">决战赛比赛配置</a></li>
                        </shiro:hasPermission>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                            <table id="equiList" class="table table-striped table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th class="center">序号</th>
                                    <th class="center">决战赛阶段</th>
                                    <th class="center">比赛场次</th>
                                    <th class="center">比赛时间</th>
                                    <th class="center">比赛结果</th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>
                                <tbody id="roleTbody">
                                        <c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
                                            <tr>
                                                <input type="hidden" name="id${status.index}" id="id${status.index}" value="${record.id }"/>
                                                <input type="hidden" name="matchType${status.index}" id="matchType${status.index}" value="${record.matchType }"/>
                                                <td class="center"><c:out value="${status.index+((worldCupactivityConfForm.paginatorPage - 1) * worldCupactivityConfForm.paginator.limit) + 1 }"></c:out></td>
                                                <td class="center">
                                                    <c:if test="${record.matchType == '1'}"> 1/8决赛</c:if>
                                                    <c:if test="${record.matchType == '2'}"> 1/4决赛</c:if>
                                                    <c:if test="${record.matchType == '3'}"> 半决赛</c:if>
                                                    <c:if test="${record.matchType == '4'}"> 季决赛</c:if>
                                                    <c:if test="${record.matchType == '5'}"> 总决赛</c:if>
                                                </td>
                                                <td class="center">
                                                    <c:out value="${record.batchName }"></c:out>
                                                    <%--<input type="hidden" name="batch" class="batch" value="${record.batchName }" >--%>
                                                </td>
                                                <td class="center"><c:out value="${record.time }"></c:out></td>
                                                <td class="center">
                                                    <input type="hidden" name="winTeamId${status.index}" value="${record.winTeamId}"/>
                                                    <input type="hidden" name="matchResult${status.index}" value="${record.matchResult}"/>
                                                    <c:if test="${record.matchResult == null||record.matchResult == ''}">
                                                        <select id="statusSrch${status.index}" class="form-control underline form-select2"  style="width: 100%" >
                                                            <option value=""></option>
                                                            <option value="${record.homeMatchTeam}" >${ fn:split(record.batchName, 'vs')[0] }</option>
                                                            <option value="${record.visitingMatchTeam}">${ fn:split(record.batchName, 'vs')[1] }</option>
                                                        </select>
                                                    </c:if>
                                                    <c:if test="${record.matchResult != null&&record.matchResult != ''}">
                                                        <select name="statusSrch${status.index}" class="form-control underline form-select2"  style="width: 100%" readonly="true">
                                                            <option value=""></option>
                                                            <option value="${record.homeMatchTeam}"  <c:if test="${record.matchResult == fn:split(record.batchName, 'vs')[0] }">selected="selected"</c:if>>${ fn:split(record.batchName, 'vs')[0] }</option>
                                                            <option value="${record.visitingMatchTeam}"  <c:if test="${record.matchResult == fn:split(record.batchName, 'vs')[1] }">selected="selected"</c:if>>${ fn:split(record.batchName, 'vs')[1] }</option>
                                                        </select>
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <shiro:hasPermission name="worldCupactivityConf:ADD">
                                                        <c:if test="${record.matchResult == null||record.matchResult == ''}">
                                                            <input type="button" class="center fn-Submit"  onclick="submitButton(this)" id="record${status.index}" value="提交">
                                                        </c:if>
                                                    </shiro:hasPermission>
                                            </tr>
                                        </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
            <style>
                .thumbnails-wrap {
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
            <script type='text/javascript' src="${webRoot}/jsp/manager/activity/worldCupActivityconfiguration/worldCupActivityconfigurationmatch.js"></script>
        </tiles:putAttribute>

    </tiles:insertTemplate>
</shiro:hasPermission>
