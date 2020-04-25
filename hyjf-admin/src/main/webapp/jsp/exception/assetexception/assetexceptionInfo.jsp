<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="添加保证金配置" />

    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <style>
            .panel-title { font-family: "微软雅黑" }
            .admin-select .select2-container {
                width: 100% !important;
            }
            .admin-select .select2-container--default .select2-selection--single {
                border-radius: 0px;
                border: 1px solid #BBBAC0 !important;
            }
            .admin-select .select2-container--default .select2-selection--single .select2-selection__rendered, .admin-select .select2-container--default .select2-selection--single {
                height: 34px;
                line-height:34px;
            }
            .admin-select .select2-container .select2-selection--single .select2-selection__rendered {
                padding-left: 4px;
            }
        </style>
        <link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
    </tiles:putAttribute>

    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <c:set var="jspEditType"
               value="${empty assetexceptionForm.id ? '添加' : '修改'}"></c:set>
        <div class="panel panel-white" style="margin: 0">
            <div class="panel-body">
                <div class="panel-scroll height-830">
                    <form id="mainForm"
                          action="${empty assetexceptionForm.id ? 'insertAction' : 'updateAction'}"
                          method="post" role="form" class="form-horizontal">
                            <%-- 列表一览 --%>
                        <input type="hidden" name="id" id="id" value="${assetexceptionForm.id }"/>
                        <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}"/>
                        <input type="hidden" id="success" value="${success}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="exceptionType"><span
                                    class="symbol required"></span>异常类别 </label>
                            <c:if test="${ empty assetexceptionForm.id }">
                                <div class="col-sm-8">
                                    <select id="exceptionType" name="exceptionType" class="form-select2" datatype="*"
                                            nullmsg="未指定异常类别！" style="width:100%" data-placeholder="请选择异常类别...">
                                        <option value="">请选择异常类别...</option>
                                        <option value="0" <c:if test="${assetexceptionForm.exceptionType eq '0'}">selected="selected"</c:if>>流标 </option>
                                        <option value="1" <c:if test="${assetexceptionForm.exceptionType eq '1'}">selected="selected"</c:if>>删标 </option>
                                    </select>
                                </div>
                                <hyjf:validmessage key="instCode" label="资产来源"></hyjf:validmessage>
                            </c:if>
                            <c:if test="${ !empty assetexceptionForm.id }">
                                <div class="col-sm-8">
                                    <select id="exceptionType" name="exceptionType" class="form-select2" datatype="*" style="width:100%" disabled = "disabled">
                                        <option value="0" <c:if test="${assetexceptionForm.exceptionType eq '0'}">selected="selected"</c:if>>流标 </option>
                                        <option value="1" <c:if test="${assetexceptionForm.exceptionType eq '1'}">selected="selected"</c:if>>删标 </option>
                                    </select>
                                </div>
                            </c:if>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="borrowNid"> <span
                                    class="symbol required"></span>项目编号</label>
                            <c:if test="${ empty assetexceptionForm.id }">
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input type="text" placeholder="项目编号" id="borrowNid" name="borrowNid"
                                               value="<c:out value="${assetexceptionForm.borrowNid}" />" class="form-control"
                                               datatype="*1-20"
                                               errormsg="项目编号不能为空！" maxlength="20" ajaxurl="isExistsBorrowNid">
                                    </div>
                                    <hyjf:validmessage key="borrowNid" label="项目编号"></hyjf:validmessage>
                                </div>
                            </c:if>
                            <c:if test="${ !empty assetexceptionForm.id }">
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input type="text" placeholder="项目编号" id="borrowNid" name="borrowNid"  disabled="disabled"
                                               value="<c:out value="${assetexceptionForm.borrowNid}" />" class="form-control"
                                               maxlength="20">
                                    </div>
                                </div>
                            </c:if>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="exceptionRemark"> <span
                                    class="symbol required"></span>异常原因
                            </label>
                            <div class="col-sm-6">
                        <textarea datatype="*" errormsg="必填选项，长度不超过20!" placeholder="异常原因" maxlength="20"
                                  id="exceptionRemark" name="exceptionRemark"
                                  class="form-control limited">${assetexceptionForm.exceptionRemark}</textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-2 control-label padding-top-5" for="exceptionTime"> <span
                                    class="symbol required"></span>异常时间
                            </label>
                            <div class="input-group input-daterange datepicker">
                                <input type="text" name="exceptionTime" id="exceptionTime"
                                       maxlength="20" style="margin-left: 15px;"
                                       onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode: 1,maxDate: '%y-%M-%d'})"
                                       value="${assetexceptionForm.exceptionTime}" datatype="*" errormsg="授信周期开始时间必填！" />
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
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
        <script type='text/javascript' src="${webRoot}/jsp/exception/assetexception/assetexceptionInfo.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
