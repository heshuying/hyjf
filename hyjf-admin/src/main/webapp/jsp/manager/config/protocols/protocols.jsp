<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="protocols:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="协议管理" />
        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
        </tiles:putAttribute>

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">协议管理</h1>
            <span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                                <%-- 功能栏 --%>
                            <div class="well">
                                <c:set var="jspPrevDsb" value="${protocolsForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                <c:set var="jspNextDsb" value="${protocolsForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                                <shiro:hasPermission name="protocols:EXPORT">
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="protocols:ADD">
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
                                </shiro:hasPermission>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
                            </div>
                            <br />
                                <%-- 列表一览 --%>
                                <table id="equiList" class="table table-striped table-bordered table-hover">
                                    <colgroup>
                                        <col style="width:55px;" />
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th class="center">序号</th>
                                        <th class="center">模版编号</th>
                                        <th class="center">协议类型</th>
                                        <th class="center">启用状态</th>
                                        <th class="center">CA认证</th>
                                        <th class="center">认证时间</th>
                                        <th class="center">操作人</th>
                                        <th class="center">操作时间</th>
                                        <th class="center">备注</th>
                                        <th class="center">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="roleTbody">
                                    <c:choose>
                                        <c:when test="${empty protocolsForm.recordList}">
                                            <tr><td colspan="10">暂时没有数据记录</td></tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${protocolsForm.recordList }" var="record" begin="0" step="1" varStatus="status">
                                                <tr>
                                                    <td class="center"><c:out value="${status.index+((protocolsForm.paginatorPage - 1) * protocolsForm.paginator.limit) + 1 }"></c:out></td>
                                                    <td><c:out value="${record.templetId }"></c:out></td>
                                                    <td><c:out value="${record.protocolTypeName }"></c:out></td>
                                                    <td class="center"><c:out value="${record.isActive== '1' ? '启用' : '关闭' }"></c:out></td>
                                                    <td class="center"><c:out value="${record.caFlagName }"></c:out></td>
                                                    <td class="center"><hyjf:datetime value="${record.certificateTime }"></hyjf:datetime></td>
                                                    <td class="center"><c:out value="${record.createUserName }"></c:out></td>
                                                    <td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
                                                    <td class="center"><c:out value="${record.remark }"></c:out></td>
                                                    <td class="center">
                                                        <div class="visible-md visible-lg hidden-sm hidden-xs">
                                                            <shiro:hasPermission name="protocols:MODIFY">
                                                                <a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
                                                            </shiro:hasPermission>
                                                            <%--<shiro:hasPermission name="borrowflow:DELETE">--%>
                                                                <%--<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>--%>
                                                            <%--</shiro:hasPermission>--%>
                                                        </div>
                                                        <div class="visible-xs visible-sm hidden-md hidden-lg">
                                                            <div class="btn-group" dropdown="">
                                                                <button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
                                                                    <i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
                                                                </button>
                                                                <ul class="dropdown-menu pull-right dropdown-light" role="menu">
                                                                    <shiro:hasPermission name="protocols:MODIFY">
                                                                        <li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
                                                                    </shiro:hasPermission>
                                                                    <%--<shiro:hasPermission name="borrowflow:DELETE">--%>
                                                                        <%--<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>--%>
                                                                    <%--</shiro:hasPermission>--%>
                                                                </ul>
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                                <%-- 分页栏 --%>
                            <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${protocolsForm.paginator}"></hyjf:paginator>
                            <br /> <br />
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <shiro:hasPermission name="protocols:SEARCH">
                <input type="hidden" name="id" id="id" />
                <input type="hidden" name="paginatorPage" id="paginator-page" value="${protocolsForm.paginatorPage}" />
                <!-- 查询条件 -->
                <div class="form-group">
                    <label>项目类型</label>
                    <select name="borrowCdSrch" id="borrowCdSrch" class="form-control underline form-select2">
                        <option value=""></option>
                        <c:forEach items="${borrowProjectTypeList }" var="projectType" begin="0" step="1">
                            <option value="${projectType.borrowCd }"
                                    <c:if test="${projectType.borrowCd eq protocolsForm.borrowCdSrch}">selected="selected"</c:if>>
                                <c:out value="${projectType.borrowName }"></c:out>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>资产来源</label>
                    <select name="instCodeSrch" id="instCodeSrch" class="form-control underline form-select2">
                        <option value=""></option>
                        <c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
                            <option value="${inst.instCode }"
                                    <c:if test="${inst.instCode eq protocolsForm.instCodeSrch}">selected="selected"</c:if>>
                                <c:out value="${inst.instName }"></c:out>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>产品类型</label>
                    <select name="assetTypeSrch" id="assetTypeSrch" class="form-control underline form-select2">
                        <option value=""></option>
                        <c:forEach items="${assetTypeList }" var="assetType" begin="0" step="1">
                            <option value="${assetType.assetType }"
                                    <c:if test="${assetType.assetType eq protocolsForm.assetTypeSrch}">selected="selected"</c:if>>
                                <c:out value="${assetType.assetTypeName }"></c:out>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>状态</label>
                    <select name="statusSrch" id="statusSrch" class="form-control underline form-select2">
                        <option value=""></option>
                        <c:forEach items="${statusList }" var="status" begin="0" step="1">
                            <option value="${status.nameCd }"
                                    <c:if test="${status.nameCd eq protocolsForm.statusSrch}">selected="selected"</c:if>>
                                <c:out value="${status.name }"></c:out>
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </shiro:hasPermission>
        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
            <link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
        </tiles:putAttribute>

        <%-- JS全局变量定义、插件 (ignore) --%>
        <tiles:putAttribute name="pageGlobalImport" type="string">
            <script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
        </tiles:putAttribute>

        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type='text/javascript' src="${webRoot}/jsp/manager/config/protocols/protocols.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>
