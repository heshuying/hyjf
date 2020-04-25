<%--
  Created by IntelliJ IDEA.
  User: wgx
  Date: 2017/12/22
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="分账" />
    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <div class="panel panel-white" style="margin:0">
            <div class="panel-body">
                <form id="mainForm" action="transferAction" method="post"  role="form" class="form-horizontal" >
                    <input type="hidden" id="success" value="${success}" />
                    <input type="hidden" id="status" value="${status}" />
                    <input type="hidden" id="result" value="${result}" />
                    <input type="hidden" name="id" value="${poundageForm.id}" />
                    <input type="hidden" name="status" value="${poundageForm.status}" />
                    <input type="hidden" id="accountId" name="accountId" value="${poundageForm.accountId}" />
                    <input type="hidden" id="ledgerId" name="ledgerId" value="${poundageForm.ledgerId}" />
                    <input type="hidden" id="balance" name="balance" value="${poundageForm.balance}" />
                    <input type="hidden" id="userId" name="userId" value="${poundageForm.userId}" />
                    <input type="hidden" id="userName" name="userName" value="${poundageForm.userName}" />
                    <input type="hidden" id="realName" name="realName" value="${poundageForm.realName}" />
                    <input type="hidden" id="account" name="account" value="${poundageForm.account}" />
                    <input type="hidden" id ="amount" name="amount" value="${poundageForm.amount}" />
                    <div class="panel-scroll height-340 margin-bottom-15">
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0" for="accountId">
                                转出电子账户号:
                            </label>
                            <div class="col-xs-9">
                                <span name="accountId"> ${poundageForm.accountId} </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0">
                                用户余额:
                            </label>
                            <div class="col-xs-9">
                                <span name ="balance">
                                    <c:choose>
                                        <c:when test="${empty poundageForm.balance}">
                                            0
                                        </c:when>
                                        <c:otherwise>
                                            ${poundageForm.balance}
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0" for="userName">
                                转入用户名:
                            </label>
                            <div class="col-xs-9">
                                <span name="userName">${poundageForm.userName}</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0" for="realName">
                                转入姓名:
                            </label>
                            <div class="col-xs-9">
                                <span name="realName">${poundageForm.realName }</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0" for="account">
                                江西银行电子账号:
                            </label>
                            <div class="col-xs-9">
                                <span name="account">${poundageForm.account} </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0" for="amount">
                                转账金额:
                            </label>
                            <div class="col-xs-9">
                                <span name="amount">${poundageForm.amount} </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0" for="password"> <span
                                    class="symbol required"></span> 交易密码:
                            </label>
                            <div class="col-xs-6">
                                <input type="password" placeholder="交易密码" id="password" name="password" class="form-control" maxlength="20" datatype="*1-20" errormsg="交易密码的长度不能大于20个字符！">
                                <hyjf:validmessage key="error" label="错误信息"></hyjf:validmessage>
                            </div>
                        </div>
                    </div>
                    <div class="form-group margin-bottom-0">
                        <div class="col-xs-offset-2 col-xs-9">
                            <a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
                            <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </tiles:putAttribute>

    <%-- JS全局变量定义、插件 (ignore) --%>
    <tiles:putAttribute name="pageGlobalImport" type="string">
        <!-- Form表单插件 -->
        <%@include file="/jsp/common/pluginBaseForm.jsp"%>
    </tiles:putAttribute>

    <%-- Javascripts required for this page only (ignore) --%>
    <tiles:putAttribute name="pageJavaScript" type="string">
        <script type='text/javascript' src="${webRoot}/jsp/finance/poundage/poundageDetail.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
