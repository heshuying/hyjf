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
    <tiles:putAttribute name="pageTitle" value="配置" />
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
        <div class="panel panel-white" style="margin: 0">
            <div class="panel-body" style="margin: 0 auto">
                <div class="panel-scroll height-430">
                    <form id="mainForm" action="updateAuthConfigAction" method="post" role="form" class="form-horizontal">
                            <%-- 列表一览 --%>
                        <input type="hidden" name="id" id="id" value="${authConfigForm.id }" />
                        <input type="hidden" name="authType" id="authType" value="${authConfigForm.authType }" />
                        <input type="hidden"  name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
                        <input type="hidden" id="success" value="${success}" />

                        <div class="form-group">
                            <label class="col-xs-3 control-label" for="authType">
                                授权类型 :
                            </label>
                            <div class="col-xs-9">
                                <c:if test="${authConfigForm.authType==1}">
                                    服务费授权
                                </c:if>
                                <c:if test="${authConfigForm.authType==2}">
                                    还款授权
                                </c:if>
                                <c:if test="${authConfigForm.authType==3}">
                                    自动投标
                                </c:if>
                                <c:if test="${authConfigForm.authType==4}">
                                    自动债转
                                </c:if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-3 control-label" for="personalMaxAmount">个人最高金额：</label>
                            <div class="col-xs-9">
                                <div class="input-group">
                                    <input type="text" placeholder="个人最高金额" id="personalMaxAmount" name="personalMaxAmount" value="<c:out value="${authConfigForm.personalMaxAmount}" />"  class="form-control"
                                           datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="个人最高金额必须为数字，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
                                    <span class="input-group-addon">元</span>
                                </div>
                                <hyjf:validmessage key="personalMaxAmount" label="发标额度上限"></hyjf:validmessage>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-3 control-label" for="enterpriseMaxAmount">企业最高金额：</label>
                            <div class="col-xs-9">
                                <div class="input-group">
                                    <input type="text" placeholder="企业最高金额" id="enterpriseMaxAmount" name="enterpriseMaxAmount" value="<c:out value="${authConfigForm.enterpriseMaxAmount}" />"  class="form-control"
                                           datatype="/^\d{1,8}(\.\d{1,2})?$/" errormsg="企业最高金额必须为数字，整数部分不能超过8位，小数部分不能超过2位！" maxlength="11">
                                    <span class="input-group-addon">元</span>
                                </div>
                                <hyjf:validmessage key="enterpriseMaxAmount" label="企业最高金额"></hyjf:validmessage>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-3 control-label" for="authPeriod">授权最高期限：</label>
                            <div class="col-xs-9">
                                <div class="input-group">
                                    <input type="text" placeholder="授权最高期限" id="authPeriod" name="authPeriod"
                                           value="<c:out value="${authConfigForm.authPeriod}" />"  class="form-control"
                                           datatype="/^\d{1,2}$/" errormsg="授权最高期限必须为数字不能超过2位!"
                                           maxlength="7">
                                    <span class="input-group-addon">年</span>
                                </div>
                                <hyjf:validmessage key="authPeriod" label="授权最高期限"></hyjf:validmessage>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-3 control-label" for="remark">备注
                            </label>
                            <div class="col-xs-9">
                        <textarea datatype="*" placeholder="备注"
                                  id="remark" name="remark">${authConfigForm.remark}</textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-3 control-label" for="enabledStatus">
                                启用状态：
                            </label>
                            <div class="col-xs-9 ">
                                <div class="radio clip-radio radio-primary ">
                                    <input type="radio" id="stateOn" name="enabledStatus" value=1  datatype="*" <c:if test="${ authConfigForm.enabledStatus eq 1 }" >checked="checked"</c:if> ><label for="stateOn"> 启用 </label>
                                    <input type="radio" id="stateOff" name="enabledStatus" value=0 datatype="*" <c:if test="${ authConfigForm.enabledStatus ne 1 }" >checked="checked"</c:if> > <label for="stateOff"> 禁用 </label>
                                    <hyjf:validmessage key="status" label="启用状态"></hyjf:validmessage>
                                </div>
                            </div>
                        </div>

                        <div class="form-group margin-bottom-0">
                            <div class="col-xs-offset-3 col-xs-9">
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
        <script type="text/javascript"
                src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
        <script type='text/javascript'
                src="${webRoot}/jsp/manager/config/authconfig/authConfigInfo.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
