<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/jsp/base/pageBase.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags" %>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="推广管理"/>
    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <style>
            .panel-title {
                font-family: "微软雅黑"
            }
        </style>
        <link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
    </tiles:putAttribute>
    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <c:set var="jspEditType" value="${empty utmForm.id ? '添加' : '修改'}"></c:set>
        <div class="panel panel-white" style="margin:0">
            <div class="panel-body" style="margin:0 auto">
                <div class="panel-scroll height-430">
                    <form id="mainForm" action="${empty utmForm.id ? 'insertAction' : 'updateAction'}"
                          method="post" role="form" class="form-horizontal">
                            <%-- 银行列表一览 --%>
                        <input type="hidden" name="id" id="id" value="<c:out value="${utmForm.id }"/>"/>
                        <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}"/>
                        <input type="hidden" id="success" value="${success}"/>

                        <div id="chargeTimeDiv" class="form-group">
                            <label class="col-sm-2 control-label" for="sourceId">
                                <span class="symbol required"></span>渠道编号:
                            </label>
                            <div class="col-sm-9">
                                <input type="text" id="sourceId" name="sourceId"
                                       value="<c:out value="${utmForm.sourceId}"></c:out>"   <c:if
                                        test="${!empty utmForm.id}"> readonly="readonly" </c:if>
                                       class="form-control" datatype="n1-10" errormsg="渠道编号应该输入1~10个数字！" maxlength="10"
                                       <c:if test="${empty utmForm.id}">ajaxurl="checkAction"</c:if> />
                                <hyjf:validmessage key="sourceId" label="渠道编号"></hyjf:validmessage>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="roomId"> <span class="symbol required"></span>渠道类型</label>
                            <div class="col-sm-9 admin-select">
                                <select id="sourceType" name="sourceType" class="form-control">
                                    <option value="0"
                                            <c:if test="${utmForm.sourceType == 0}">selected="selected"</c:if>>PC渠道
                                    </option>
                                    <option value="1"
                                            <c:if test="${utmForm.sourceType == 1}">selected="selected"</c:if>>APP渠道
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div id="chargeTimeDiv" class="form-group">
                            <label class="col-sm-2 control-label" for="sourceName">
                                <span class="symbol required"></span>渠道:
                            </label>
                            <div class="col-sm-9">
                                <input type="text" id="sourceName" name="sourceName"
                                       value="<c:out value="${utmForm.sourceName}"></c:out>"
                                       class="form-control" datatype="*1-50" errormsg="渠道编号应该输入1~50个字符！"
                                       maxlength="50"/>
                                <hyjf:validmessage key="sourceName" label="渠道"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"> <span class="symbol required"></span>状态 </label>
                            <div class="col-sm-10">
                                <div class="radio clip-radio radio-primary ">
                                    <input type="radio" id="statusOn" name="delFlag" datatype="*" value="0"
                                           class="event-categories" ${ ( ( utmForm.delFlag eq '0' ) or ( empty utmForm.delFlag ) ) ? 'checked' : ''}>
                                    <label for="statusOn"> 启用
                                    </label>
                                    <input type="radio" id="statusOff" name="delFlag" datatype="*" value="1"
                                           class="event-categories" ${utmForm.delFlag eq '1' ? 'checked' : ''}> <label
                                        for="statusOff"> 禁用
                                </label>
                                </div>
                            </div>
                            <hyjf:validmessage key="delFlag" label="状态"></hyjf:validmessage>
                        </div>


                        <div class="form-group">
                            <label class="col-sm-2 control-label"> <span class="symbol required"></span>是否可以发起债转 </label>
                            <div class="col-sm-10">
                                <div class="radio clip-radio radio-primary ">
                                    <input type="radio" id="attornOn" name="attornFlag" datatype="*" value="1"
                                           class="event-categories" ${ ( ( utmForm.attornFlag eq '1' ) or ( empty utmForm.attornFlag ) ) ? 'checked' : ''}>
                                    <label for="attornOn"> 是
                                    </label>
                                    <input type="radio" id="attornOff" name="attornFlag" datatype="*" value="0"
                                           class="event-categories" ${utmForm.attornFlag eq '0' ? 'checked' : ''}> <label
                                        for="attornOff"> 否
                                </label>
                                </div>
                            </div>
                            <hyjf:validmessage key="delFlag" label="状态"></hyjf:validmessage>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="remark"> 说明 </label>
                            <div class="col-sm-10">
                                <input type="text" placeholder="说明" id="remark" name="remark"
                                       value="<c:out value="${utmForm.remark}"/>" maxlength="255" class="form-control"
                                       datatype="*1-255" errormsg="说明 只能是字符汉字，长度1~50个字符！" ignore="ignore">
                                <hyjf:validmessage key="remark" label="说明"></hyjf:validmessage>
                            </div>
                        </div>


                        <div class="form-group margin-bottom-0">
                            <div class="col-sm-offset-2 col-sm-10">
                                <a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
                                <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </tiles:putAttribute>

    <%-- Javascripts required for this page only (ignore) --%>
    <tiles:putAttribute name="pageJavaScript" type="string">
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
        <script type='text/javascript' src="${webRoot}/jsp/promotion/utm/utmInfo.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
