<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="用户画像修改页面" />

    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <form id="mainForm" method="post" action="saveuserportrait">
            <hyjf:message key="message"></hyjf:message>
            <input type="hidden" name=userId id="userId" value="${usersPortrait.userId}"/>
            <input type="hidden" name=userName id="userName" value="${usersPortrait.userName}"/>
            <input type="hidden" id="success" value="${success}" />
            <div class="row bg-white">
                <div class="col-sm-12">
                    <div class="panel panel-white">
                        <div class="panel-body panel-table">
                            <table class="center" style="width:95%">
                                <tr>
                                    <td align="right" class="tlabel">用户名：</td>
                                    <td align="left"> <c:out value="${usersPortrait.userName}" /></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">学历：</td>
                                    <td align="left"><input type="text" name="education" id="education" value="${usersPortrait.education}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">职业：</td>
                                    <td align="left"><input type="text" name="occupation" id="occupation" value="${usersPortrait.occupation}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">爱好：</td>
                                    <td align="left"><input type="text" name="interest" id="interest" value="${usersPortrait.interest}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">登录活跃：</td>
                                    <td align="left"><input type="text" name="loginActive" id="loginActive" value="${usersPortrait.loginActive}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">客户来源：</td>
                                    <td align="left"><input type="text" name="customerSource" id="customerSource" value="${usersPortrait.customerSource}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">同时出借平台数：</td>
                                    <td align="left"><input onkeyup="value=value.replace(/[^\d]/g,'')" type="text" name="investPlatform" id="investPlatform" value="${usersPortrait.investPlatform}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">投龄：</td>
                                    <td align="left"><input onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="2" type="text" name="investAge" id="investAge" value="${usersPortrait.investAge}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">当前拥有人：</td>
                                    <td align="left"><input type="text" name="currentOwner" id="currentOwner" value="${usersPortrait.currentOwner}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">是否加微信：</td>
                                    <td align="left"><input type="text" name="addWechat" id="addWechat" value="${usersPortrait.addWechat}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">客户投诉：</td>
                                    <td align="left"><input type="text" name="customerComplaint" id="customerComplaint" value="${usersPortrait.customerComplaint}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">邀约客户数 ：</td>
                                    <td align="left"><input onkeyup="value=value.replace(/[^\d]/g,'')" type="text" name="inviteCustomer" id="inviteCustomer" value="${usersPortrait.inviteCustomer}"/></td>
                                </tr>
                                <tr>
                                    <td align="right" class="tlabel">备注：</td>
                                    <td align="left"><input type="text" name="remark" id="remark" value="${usersPortrait.remark}"/></td>
                                </tr>
                            </table>
                            <hr>
                            <div class="form-group margin-bottom-0" align="center">
                                <div class="col-sm-offset-2 col-sm-10">
                                    <a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确认</a>
                                    <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取消</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </tiles:putAttribute>

    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <style>
            .panel-table .tlabel {
                height: 30px;
                font-weight: 700;
                width: 35%;
            }
        </style>
    </tiles:putAttribute>

    <%-- JS全局变量定义、插件 (ignore) --%>
    <tiles:putAttribute name="pageGlobalImport" type="string">
        <!-- Form表单插件 -->
        <%@include file="/jsp/common/pluginBaseForm.jsp"%>
    </tiles:putAttribute>

    <%-- Javascripts required for this page only (ignore) --%>
    <tiles:putAttribute name="pageJavaScript" type="string">
        <script type='text/javascript' src="${webRoot}/jsp/manager/users/userportrait/userportraitupdate.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
