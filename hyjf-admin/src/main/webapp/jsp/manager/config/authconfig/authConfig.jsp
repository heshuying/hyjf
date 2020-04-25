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

<shiro:hasPermission name="authconfig:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="授权配置" />

        <%-- 画面主面板标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">授权配置</h1>
            <span class="mainDescription">本功能可以修改授权配置。</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <shiro:hasPermission name="accountlist:SEARCH">
                            <li  class="active"><a href="${webRoot}/manager/config/authconfig/initAuthConfigAction">授权配置</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="accountlist:SEARCH">
                            <li><a href="${webRoot}/manager/config/authconfig/initAuthConfigLogAction">配置记录</a></li>
                        </shiro:hasPermission>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="search-classic">
                                            <%-- 功能栏 --%>
                                        <div class="well">
                                            <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel disabled"
                                               data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
                                               data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
                                        </div>
                                             <br/>

                                        <%-- 授权配置列表一览 --%>
                                        <table id="equiList" class="table table-striped table-bordered table-hover">
                                            <colgroup>
                                                <col style="width:55px;" />
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th class="center">序号</th>
                                                <th class="center">授权类型</th>
                                                <th class="center">个人最高金额(万)</th>
                                                <th class="center">企业最高金额(万)</th>
                                                <th class="center">授权期限(年)</th>
                                                <th class="center">启用状态</th>
                                                <th class="center">操作用户</th>
                                                <th class="center">更新时间</th>
                                                <th class="center">备注</th>
                                                <th class="center">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody id="authConfigTbody">
                                            <c:choose>
                                                <c:when test="${empty authConfigForm.recordList}">
                                                    <tr><td colspan="9">暂时没有数据记录</td></tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${authConfigForm.recordList}" var="record" begin="0" step="1" varStatus="status">
                                                        <tr>
                                                            <td class="center">${status.count}</td>
                                                            <td class="center">
                                                                <c:if test="${record.authType==1 }">
                                                                    服务费授权
                                                                </c:if>
                                                                <c:if test="${record.authType==2 }">
                                                                    还款授权
                                                                </c:if>
                                                                <c:if test="${record.authType==3 }">
                                                                    自动投标
                                                                </c:if>
                                                                <c:if test="${record.authType==4 }">
                                                                    自动债转
                                                                </c:if>
                                                            </td>
                                                            <td class="center">
                                                                <fmt:formatNumber value="${record.personalMaxAmount}" type="number" pattern="#,##0.00#" />
                                                            </td>
                                                            <td class="center">
                                                                <fmt:formatNumber value="${record.enterpriseMaxAmount}" type="number" pattern="#,##0.00#" />
                                                            </td>
                                                            <td class="center"><c:out value="${record.authPeriod }"></c:out></td>
                                                            <td class="center">
                                                                <c:if test="${record.enabledStatus!=1 }">
                                                                    未启用
                                                                </c:if>
                                                                <c:if test="${record.enabledStatus==1 }">
                                                                    已启用
                                                                </c:if>
                                                            </td>
                                                            <td class="center"><c:out value="${record.updateUserStr }"></c:out></td>
                                                            <td class="center"><c:out value="${record.updateTimeStr }"></c:out></td>
                                                            <td class="center"><c:out value="${record.remark }"></c:out></td>
                                                            <td class="center">
                                                                <shiro:hasPermission name="authConfig:MODIFY">
                                                                    <a class="btn btn-transparent btn-xs tooltips fn-Modify" data-id="${record.id }"
                                                                       data-toggle="tooltip" data-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
                                                                </shiro:hasPermission>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                                <%-- 分页栏 --%>
                                        <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="initAuthConfigAction" paginator="${authConfigForm.paginator}"></hyjf:paginator>
                                        <br/><br/>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="ids" id="ids" />
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${authConfigForm.paginatorPage}" />
        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
        </tiles:putAttribute>

        <%-- JS全局变量定义、插件 (ignore) --%>
        <tiles:putAttribute name="pageGlobalImport" type="string">
            <script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
        </tiles:putAttribute>

        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type='text/javascript' src="${webRoot}/jsp/manager/config/authconfig/authConfig.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>