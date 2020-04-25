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

<shiro:hasPermission name="apppushmanage:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
                          flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="推送管理" />
        <style>
            td{text-align:center;}
        </style>
        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                                <%-- 功能栏 --%>
                            <div class="well">
                                <c:set var="jspPrevDsb" value="${appPushManageForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                <c:set var="jspNextDsb" value="${appPushManageForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                                <shiro:hasPermission name="apppushmanage:ADD">
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
                                </shiro:hasPermission>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
                                <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
                                   data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
                            </div>
                            <br />
                                <%-- 列表一览 --%>
                            <div class="row">
                                    <%-- 产品管理列表一览 --%>
                                <table id="equiList" class="table table-striped table-bordered table-hover">
                                    <colgroup>
                                        <col style="width: 55px;" />
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th class="center">序号</th>
                                        <th class="center">标题名称</th>
                                        <th class="center">排序</th>
                                        <th class="center">状态</th>
                                        <th class="center">添加时间</th>
                                        <th class="center">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="roleTbody">
                                    <c:choose>
                                        <c:when test="${empty appPushManageForm.recordList}">
                                            <tr>
                                                <td colspan="7">暂时没有数据记录</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>

                                            <c:forEach items="${appPushManageForm.recordList}" var="record" begin="0" step="1" varStatus="vs">
                                                <tr>
                                                    <td><c:out value="${vs.index+((instConfigForm.paginatorPage - 1) * instConfigForm.paginator.limit) + 1 }"></c:out></td>
                                                    <td style="text-align: left;">${record.title}</td>
                                                    <td>${record.order}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${record.status eq 0}">
                                                                禁用
                                                            </c:when>
                                                            <c:otherwise>
                                                                启用
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td><hyjf:datetime value="${record.createTime}"></hyjf:datetime></td>
                                                    <td>
                                                        <shiro:hasPermission name="apppushmanage:MODIFY">
                                                            <a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
                                                               data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
                                                        </shiro:hasPermission>
                                                        <shiro:hasPermission name="contentevents:MODIFY">
                                                            <a class="btn btn-transparent btn-xs tooltips fn-UpdateBy"
                                                               data-id="${record.id }" data-toggle="tooltip"
                                                               tooltip-placement="top" data-original-title="${record.status== '1' ? '启用' : '禁用' }"><i
                                                                    class="fa fa-lightbulb-o fa-white"></i></a>
                                                        </shiro:hasPermission>
                                                        <shiro:hasPermission name="apppushmanage:DELETE">
                                                            <a class="btn btn-transparent btn-xs tooltips fn-Delete" data-id="${record.id }"
                                                               data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
                                                        </shiro:hasPermission>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>
                                <%-- 分页栏 --%>
                                    <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${appPushManageForm.paginator}"></hyjf:paginator>
                            <br /> <br />
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="ids" id="ids" />
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${appPushManageForm.paginatorPage}" />
            <div class="form-group">
                <label>标题名称:</label>
                <input type="text" name="title" class="form-control input-sm underline"  maxlength="20" value="${ appPushManageForm.title}" />
            </div>
            <div class="form-group">
                <label>状态:</label>
                <select name="statusSch" class="form-control underline">
                    <option value=""></option>
                    <option value="1" <c:if test="${appPushManageForm.status==1}">selected="selected"</c:if>>启用</option>
                    <option value="0" <c:if test="${appPushManageForm.status==0}">selected="selected"</c:if>>禁用</option>
                </select>
            </div>
            <div class="form-group">
                <label>时间</label>
                <div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartDiy" id="start-date-time" class="form-control underline" value="" />
							<i class="ti-calendar"></i>
						</span>
                    <span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="timeEndDiy" id="end-date-time" class="form-control underline" value="" />
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
            <style>
                table tr td{text-align:center;}
            </style>
        </tiles:putAttribute>

        <%-- JS全局变量定义、插件 (ignore) --%>
        <tiles:putAttribute name="pageGlobalImport" type="string">
            <script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
        </tiles:putAttribute>

        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type='text/javascript' src="${webRoot}/jsp/app/maintenance/pushmanage/pushmanage.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>
