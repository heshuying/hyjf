<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="protocolLogView:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="协议模板日志管理" />

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">协议模板日志管理</h1>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="row">
                    <div class="col-md-12">
                        <div class="search-classic">
                                <%-- 功能栏 --%>
                            <div class="well">
                                <c:set var="jspPrevDsb" value="${protocolLogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                <c:set var="jspNextDsb" value="${protocolLogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                            </div>
                            <br/>
                            <table id="equiList" class="table table-striped table-bordered table-hover">
                                <colgroup>
                                    <col style="width:55px;" />
                                </colgroup>
                                <thead>
                                <tr>

                                    <th class="center">序号</th>
                                    <th class="center">协议模板名称</th>
                                    <th class="center">版本号</th>
                                    <th class="center">操作</th>
                                    <th class="center">修改时间</th>
                                    <th class="center">修改人</th>
                                </tr>
                                </thead>
                                <tbody id="roleTbody">
                                <c:choose>
                                    <c:when test="${empty protocolLogForm.recordList}">
                                        <tr><td colspan="7">暂时没有数据记录</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${protocolLogForm.recordList }" var="record" begin="0" step="1" varStatus="status">
                                            <tr>
                                                <td class="center"><c:out value="${status.index+1 }"></c:out></td>
                                                <td class="center"><c:out value="${record.protocolName }"></c:out></td>
                                                <td class="center"><c:out value="${record.versionNumber }"></c:out></td>
                                                <td class="center">
                                                    <c:if test="${record.operation == 0 }">创建协议</c:if>
                                                    <c:if test="${record.operation == 1 }">修改协议</c:if>
                                                    <c:if test="${record.operation == 2 }">删除协议</c:if>
                                                </td>
                                                <td class="center">
                                                    <%--<fmt:formatDate value="${record.createTime }" pattern="yyyy年MM月dd日HH点mm分ss秒" />--%>
                                                    <c:out value="${record.time }"></c:out>
                                                </td>
                                                <td class="center"><c:out value="${record.userName }"></c:out></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                                <%-- 分页栏 --%>
                                <%--<shiro:hasPermission name="borrow:SEARCH">--%>
                            <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${protocolLogForm.paginator}"></hyjf:paginator>
                                <%--</shiro:hasPermission>--%>
                            <br/><br/>
                            <br/><br/>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>
        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="ids" id="ids" />
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${protocolLogForm.paginatorPage}" />
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

         <%--Javascripts required for this page only (ignore)--%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type='text/javascript' src="${webRoot}/jsp/manager/config/protocolog/protocoltemplatelog.js"></script>
        </tiles:putAttribute>

    </tiles:insertTemplate>
</shiro:hasPermission>
