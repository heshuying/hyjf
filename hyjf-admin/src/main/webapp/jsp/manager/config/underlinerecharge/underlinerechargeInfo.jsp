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
    <tiles:putAttribute name="pageTitle" value="管理" />
    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <style>
            .panel-title {
                font-family: "微软雅黑"
            }
        </style>
    </tiles:putAttribute>
    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <c:set var="jspEditType" value="${empty underlinerechargeForm.id ? '添加' : '修改'}"></c:set>
        <div class="panel panel-white" style="margin: 0">
            <div class="panel-body" style="margin: 0 auto">
                <div class="panel-scroll height-430">
                    <form id="mainForm" action="${empty underlinerechargeForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" class="form-horizontal">
                            <%-- 列表一览 --%>
                        <input type="hidden" name="id" id="id" value="${underlinerechargeForm.id }" />
                        <input type="hidden"  name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
                        <input type="hidden" id="success" value="${success}" />
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="code">
                                <span class="symbol required"></span>交易类型 :
                            </label>
                            <div class="col-sm-10">
                                <input type="text" placeholder="交易类型" id="code" name="code" value="<c:out value="${underlinerechargeForm.code}"></c:out>" ajaxurl="checkAction?ids=<c:out value="${underlinerechargeForm.ids}"></c:out>"  class="form-control" datatype="n" errormsg=交易类型为数字且不能超过4个字符！" maxlength="4" />
                                <hyjf:validmessage key="code" label="线下交易类型"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group margin-bottom-0">
                            <div class="col-sm-offset-2 col-sm-10">
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

    <%-- Javascripts required for this page only (ignore) --%>
    <tiles:putAttribute name="pageJavaScript" type="string">
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
        <script type='text/javascript' src="${webRoot}/jsp/manager/config/underlinerecharge/underlinerechargeInfo.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
