<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jsp/base/pageBase.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- 画面功能菜单设置开关 --%>
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="手续费分账"/>
    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <div class="panel panel-white no-margin">
            <div class="panel-body">
                <form id="mainForm" action="addOrSaveAction"
                      method="post" role="form" class="form-horizontal">
                    <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}"/>
                    <input type="hidden" id="success" value="${success}"/>
                        <%-- 角色列表一览 --%>
                    <div class="panel-scroll margin-bottom-15">
                        <input type="text" style="display:none" id="id" name="id" value="${poundageLedgerForm.id}">
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10"
                                   for="username">
                                <span class="symbol required"></span>用户名:
                            </label>
                            <div class="col-xs-8">
                                <select id="username" name="username" class="form-control underline form-select2"
                                        style="width:99.9%" datatype="*" data-placeholder="请选择用户名">
                                    <option disabled selected value></option>
                                    <c:forEach items="${userNames }" var="username" begin="0" step="1"
                                               varStatus="status">
                                        <option truename="${username.truename}" account="${username.account}"
                                                value="${username.username}"
                                                <c:if test="${username.username eq poundageLedgerForm.username }">selected="selected"</c:if> >
                                            <c:out value="${username.username}"></c:out>
                                        </option>
                                    </c:forEach>
                                </select>
                                <hyjf:validmessage key="username" label="用户名"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10"
                                   for="truename">
                                <span class="symbol required"></span>姓名:
                            </label>
                            <div class="col-xs-8">
                                <input type="text" placeholder="姓名" class="form-control" id="truename" name="truename"
                                       readonly="readonly"
                                       datatype="s1-30" maxlength="30" nullmsg="姓名不能为空" errormsg="姓名不正确" flag="true"
                                       value="${poundageLedgerForm.truename}">
                                <hyjf:validmessage key="truename" label="姓名"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10"
                                   for="account">
                                <span class="symbol required"></span>江西银行电子账号:
                            </label>
                            <div class="col-xs-8">
                                <input type="text" placeholder="电子账号" class="form-control" id="account"
                                       name="account" readonly="readonly"
                                       datatype="s1-30" maxlength="30" nullmsg="电子账号不能为空" errormsg="电子账号不正确" flag="true"
                                       value="${poundageLedgerForm.account}">
                                <hyjf:validmessage key="account" label="电子账号"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10">
                                <span class="symbol required"></span>分账类型:
                            </label>
                            <div class="col-xs-8">
                                <select id="type" name="type" class="form-control underline form-select2"
                                        style="width:99.9%" datatype="*" data-placeholder="请选择">
                                    <option disabled selected value></option>
                                    <option value="1"
                                            <c:if test="${'1' eq poundageLedgerForm.type}">selected="selected"</c:if>>
                                        <c:out value="按出借人分账"></c:out>
                                    </option>
                                    <option value="2"
                                            <c:if test="${'2' eq poundageLedgerForm.type}">selected="selected"</c:if>>
                                        <c:out value="按借款人分账"></c:out>
                                    </option>
                                </select>
                                <hyjf:validmessage key="type" label="分账类型"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10">
                                <span class="symbol required"></span>分账来源:
                            </label>
                            <div class="col-xs-8">
                                <select id="source" name="source" class="form-control underline form-select2"
                                        style="width:99.9%" datatype="*" data-placeholder="请选择">
                                    <option disabled selected value></option>
                                    <option value="0"
                                            <c:if test="${'0' eq poundageLedgerForm.source}">selected="selected"</c:if>>
                                        <c:out value="全部"></c:out>
                                    </option>
                                    <option value="1"
                                            <c:if test="${'1' eq poundageLedgerForm.source}">selected="selected"</c:if>>
                                        <c:out value="服务费"></c:out>
                                    </option>
                                    <option value="3"
                                            <c:if test="${'3' eq poundageLedgerForm.source}">selected="selected"</c:if>>
                                        <c:out value="管理费"></c:out>
                                    </option>
                                    <c:if test="${'1' eq poundageLedgerForm.type}">
                                        <option value="2"
                                                <c:if test="${'2' eq poundageLedgerForm.source}">selected="selected"</c:if>>
                                            <c:out value="债转服务费"></c:out>
                                        </option>
                                    </c:if>
                                </select>
                                <hyjf:validmessage key="source" label="分账来源"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group"
                                <c:if test="${not empty poundageLedgerForm.source && '0' ne poundageLedgerForm.source && '1' ne poundageLedgerForm.source}"> style="display: none;"</c:if>>
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10"
                                   for="serviceRatio">
                                <span class="symbol required"></span>服务费分账比例:
                            </label>
                            <div class="col-xs-8">
                                <input type="text" placeholder="服务费分账比例" id="serviceRatio" name="serviceRatio"
                                       value="<c:out value="${poundageLedgerForm.serviceRatio}" />" class="form-control"
                                <c:choose>
                                       <c:when test="${'0' eq poundageLedgerForm.source || '1' eq poundageLedgerForm.source}">datatype="*1-10"</c:when>
                                       <c:otherwise>datatype="*0-10" readonly </c:otherwise>
                                </c:choose>
                                       errormsg="服务费分账比例范围为0-1之间！" maxlength="20">
                                <hyjf:validmessage key="serviceRatio" label="服务费分账比例"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group"
                             <c:if test="${'2' eq poundageLedgerForm.type || (not empty poundageLedgerForm.source && '0' ne poundageLedgerForm.source && '2' ne poundageLedgerForm.source)}">style="display:none;"</c:if>>
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10"
                                   for="creditRatio">
                                <span class="symbol required"></span>债转服务费分账比例:
                            </label>
                            <div class="col-xs-8">
                                <input type="text" placeholder="债转服务费分账比例" id="creditRatio" name="creditRatio"
                                       value="<c:out value="${poundageLedgerForm.creditRatio}" />" class="form-control"
                                <c:choose>
                                       <c:when test="${'1' eq poundageLedgerForm.type && (not empty poundageLedgerForm.source && '0' eq poundageLedgerForm.source || '2' eq poundageLedgerForm.source)}">datatype="*1-10"</c:when>
                                       <c:otherwise>datatype="*0-10" readonly </c:otherwise>
                                </c:choose> errormsg="债转服务费分账比例范围为0-1之间！" maxlength="20">
                                <hyjf:validmessage key="creditRatio" label="债转服务费分账比例"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group"
                                <c:if test="${not empty poundageLedgerForm.source && '0' ne poundageLedgerForm.source && '3' ne poundageLedgerForm.source}"> style="display: none;"</c:if>>
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10"
                                   for="manageRatio">
                                <span class="symbol required"></span>管理费分账比例:
                            </label>
                            <div class="col-xs-8">
                                <input type="text" placeholder="管理费分账比例" id="manageRatio" name="manageRatio"
                                       value="<c:out value="${poundageLedgerForm.manageRatio}" />" class="form-control"
                                <c:choose>
                                       <c:when test="${'0' eq poundageLedgerForm.source || '3' eq poundageLedgerForm.source}">datatype="*1-10"</c:when>
                                       <c:otherwise>datatype="*0-10" readonly </c:otherwise>
                                </c:choose> errormsg="管理费分账比例范围为0-1之间！" maxlength="20">
                                <hyjf:validmessage key="manageRatio" label="管理费分账比例"></hyjf:validmessage>
                            </div>
                        </div>
                        <input type="hidden" id="investorCompanyId" name="investorCompanyId" datatype="*0-10"
                               value="${poundageLedgerForm.investorCompanyId}"/>
                        <div class="form-group"
                                <c:if test="${'2' eq poundageLedgerForm.type}"> style="display: none;"</c:if>>
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10"
                                   for="investorCompany">
                                <span class="symbol required"></span>出借人分公司:
                            </label>
                            <div class="col-xs-8">
                                <select id="investorCompany" name="investorCompany"
                                        class="form-control underline form-select2" style="width:99.9%"
                                        <c:choose>
                                            <c:when test="${'2' eq poundageLedgerForm.type}"> datatype="*0-10"</c:when>
                                            <c:otherwise> datatype="*"</c:otherwise>
                                        </c:choose>
                                        data-placeholder="请选择出借人分公司" ajaxurl="companyCount?id=${poundageLedgerForm.id}">
                                    <option disabled selected value></option>
                                    <c:forEach items="${companyNames}" var="companyName" begin="0" step="1"
                                               varStatus="status">
                                        <option value="${companyName.regionName}" investorCompanyId="${companyName.id}"
                                                <c:if test="${companyName.regionName eq poundageLedgerForm.investorCompany }">selected="selected"</c:if> >
                                            <c:out value="${companyName.regionName}"></c:out>
                                        </option>
                                    </c:forEach>
                                </select>
                                <hyjf:validmessage key="investorCompany" label="出借人分公司"></hyjf:validmessage>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10">
                                <span class="symbol required"></span>适用项目类型:
                            </label>
                            <div id="checkboxGroup" class="col-xs-8">
                                <c:if test="${projects.size() gt 1}">
                                    <div class="checkbox clip-check check-primary checkbox-inline">
                                        <input type="checkbox" name="projectType" id="checkboxAll"
                                               value="<c:out value="0" />"
                                            <c:set var="projectType" value=",${poundageLedgerForm.projectType},"/>
                                               <c:if test="${fn:contains(projectType, ',0,')}">checked</c:if>>
                                        <label for="checkboxAll">
                                            &nbsp;<c:out value="全部"/>&nbsp;&nbsp;&nbsp;
                                        </label>
                                    </div>
                                </c:if>
                                <c:forEach items="${projects}" var="project" begin="0" step="1" varStatus="status">
                                    <div class="checkbox clip-check check-primary checkbox-inline">
                                        <input type="checkbox" name="projectType" id="checkbox${ project.id }"
                                            <%--<c:if test="${'0' eq poundageLedgerForm.projectType}">disabled</c:if>--%>
                                               value="<c:out value="${project.id }" />"
                                            <c:set var="projectType" value=",${poundageLedgerForm.projectType},"/>
                                            <c:set var="projectId" value=",${project.id},"/>
                                               <c:if test="${fn:contains(projectType, ',0,') || fn:contains(projectType, projectId)}">checked</c:if>
                                               <c:if test="${status.last }">datatype="*"</c:if>>
                                        <label for="checkbox${ project.id}">
                                            <c:out value="${project.borrowName }"/>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                            <hyjf:validmessage key="projectType" label="适用项目类型"></hyjf:validmessage>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10">
                            <span class="symbol required"></span>项目状态:&nbsp;&nbsp;
                        </label>
                        <div class="col-xs-8 clip-radio radio-primary" style="padding-left:10px;">
                            <input type="radio" id="stateOn" name="status" value="0"
                                   datatype="*" ${ ( poundageLedgerForm.status eq 0 ) ? 'checked' : ''}> <label
                                for="stateOn"> 启用 </label>
                            <input type="radio" id="stateOff" name="status" value="1"
                                   datatype="*" ${ ( poundageLedgerForm.status ) eq 1 ? 'checked' : ''}> <label
                                for="stateOff"> 禁用 </label>
                            <hyjf:validmessage key="status" label="项目状态"></hyjf:validmessage>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-3 control-label text-right padding-right-0 margin-top-10" for="explan">
                            说明:&nbsp;&nbsp;
                        </label>
                        <div class="col-xs-8">
							<textarea placeholder="" id="explan" name="explan" class="form-control limited"
                                      datatype="*0-50" errormsg="说明不能超过50个字符！" maxlength="50" ignore="ignore"><c:out
                                    value="${poundageLedgerForm.explan}"></c:out></textarea>
                            <hyjf:validmessage key="explan" label="说明"></hyjf:validmessage>
                        </div>
                    </div>
                    <div class="form-group margin-bottom-0">
                        <div class="col-xs-offset-2 col-xs-10">
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
        <%@include file="/jsp/common/pluginBaseForm.jsp" %>
    </tiles:putAttribute>
    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
        <link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
    </tiles:putAttribute>
    <%-- Javascripts required for this page only (ignore) --%>
    <tiles:putAttribute name="pageJavaScript" type="string">
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
        <script type='text/javascript'
                src="${webRoot}/jsp/manager/config/poundageledger/poundageLedgerInfo.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
