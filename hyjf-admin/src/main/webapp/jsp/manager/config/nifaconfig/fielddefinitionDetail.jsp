<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/jsp/base/pageBase.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags" %>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
                      flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="管理"/>
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
                <div class="panel-scroll height-630">
                    <form id="mainForm" class="form-horizontal">
                            <%-- 列表一览 --%>
                        <input type="hidden" name="id" id="id" value="${nifaconfigForm.id }"/>
                        <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}"/>
                        <input type="hidden" id="success" value="${success}"/>
                        <div class="form-group">
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="borrowingRestrictions"> <span
                                        class="symbol required"></span>借款用途限制
                                </label>
                                <div class="col-sm-10">
									<textarea datatype="*" errormsg="必填选项" placeholder="借款用途限制"
                                              id="borrowingRestrictions"
                                              name="borrowingRestrictions"
                                              class="form-control limited" readonly="readonly">${nifaconfigForm.borrowingRestrictions}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="judgmentsBased"> <span
                                        class="symbol required"></span>借款放款日判断<br>依据
                                </label>
                                <div class="col-sm-10">
                                <textarea datatype="*" errormsg="必填选项" placeholder="借款放款日判断依据"
                                      id="judgmentsBased" name="judgmentsBased"
                                      class="form-control limited" readonly="readonly">${nifaconfigForm.judgmentsBased}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="repayDateRule"> <span
                                        class="symbol required"></span>还款日定义
                                </label>
                                <div class="col-sm-10">
                            <textarea datatype="*" errormsg="必填选项"
                                      placeholder="还款日定义" id="repayDateRule" name="repayDateRule"
                                      class="form-control limited" readonly="readonly">${nifaconfigForm.repayDateRule}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="overdueDefinition"> <span
                                        class="symbol required"></span>逾期定义
                                </label>
                                <div class="col-sm-10">
                                    <textarea datatype="*" errormsg="必填选项" placeholder="逾期定义"
                                              id="overdueDefinition"
                                              name="overdueDefinition"
                                              class="form-control limited" readonly="readonly">${nifaconfigForm.overdueDefinition}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="overdueResponsibility"> <span
                                        class="symbol required"></span>逾期还款责任
                                </label>
                                <div class="col-sm-10">
                                    <textarea datatype="*" errormsg="必填选项" placeholder="逾期还款责任"
                                              id="overdueResponsibility"
                                              name="overdueResponsibility"
                                              class="form-control limited" readonly="readonly">${nifaconfigForm.overdueResponsibility}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="form-group">
                                <label class="col-sm-2 control-label" for="overdueProcess"> <span class="symbol required"></span>逾期还款流程</label>
                                <div class="col-sm-10">
                                    <textarea datatype="*" errormsg="必填选项" placeholder="逾期还款流程"
                                              id="overdueProcess"
                                              name="overdueProcess"
                                              class="form-control limited" readonly="readonly">${nifaconfigForm.overdueProcess}</textarea>
                                </div>
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
    </tiles:putAttribute>
</tiles:insertTemplate>
