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


<shiro:hasPermission name="worldCupActivity:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="世界杯活动" />

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">世界杯活动</h1>
            <span class="mainDescription">本功能可以查询。</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <shiro:hasPermission name="worldCupActivity:VIEW">
                            <li ><a href="${webRoot}/manager/activity/worldCupActivity/init">竞猜输赢</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="worldCupActivity:VIEW">
                            <li class="active"><a href="${webRoot}/manager/activity/worldCupActivity/winningInit">竞猜冠军</a></li>
                        </shiro:hasPermission>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                                <%-- 功能栏 --%>
                            <div class="well">
                                <c:set var="jspPrevDsb" value="${worldCupActivityForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                <c:set var="jspNextDsb" value="${worldCupActivityForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                                <shiro:hasPermission name="worldCupActivity:EXPORT">
                                <a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel">
                                    导出Excel <i class="fa fa-Export"></i></a>
                                </shiro:hasPermission>
                                <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
                                   data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
                            </div>
                            <br/>
                                <%-- 列表一览 --%>
                            <table id="equiList" class="table table-striped table-bordered table-hover">
                                <colgroup>
                                    <col style="width:55px;" />
                                    <col />
                                    <col style="width:93px;" />
                                </colgroup>
                                <thead>
                                <tr>
                                    <th class="center">序号</th>
                                    <th class="center">账户名</th>
                                    <th class="center">姓名</th>
                                    <th class="center">手机号</th>
                                    <th class="center">支持的球队</th>
                                    <th class="center">投票后胜场数</th>
                                    <th class="center">投票时间</th>
                                </tr>
                                </thead>
                                <tbody id="roleTbody">
                                <c:choose>
                                    <c:when test="${empty worldCupActivityForm.recordList}">
                                        <tr><td colspan="7">暂时没有数据记录</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${worldCupActivityForm.recordList }" var="record" begin="0" step="1" varStatus="status">
                                            <tr>
                                                <td class="center"><c:out value="${status.index+((worldCupActivityForm.paginatorPage - 1) * worldCupActivityForm.paginator.limit) + 1 }"></c:out></td>
                                                <td><c:out value="${record.username }"></c:out></td>
                                                <td class="center"><c:out value="${record.trueName }"></c:out></td>
                                                <td class="center"><c:out value="${record.mobile }"></c:out></td>
                                                <td class="center"><c:out value="${record.teamName}"></c:out></td>
                                                <td class="center" ><c:out value="${record.guessingMatchNum  }"></c:out></td>
                                                <td class="center"><c:out value="${record.time }"></c:out></td>
                                                    <%--<td class="center">操作</td>--%>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                                <%-- 分页栏 --%>
                            <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="winningInit" paginator="${worldCupActivityForm.paginator}"></hyjf:paginator>
                            <br/><br/>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${worldCupActivityForm.paginatorPage}" />
            <div class="form-group">
                <label>账户名:</label>
                <input type="text" name="username" class="form-control input-sm underline" value="${worldCupActivityForm.username}" />
            </div>
            <div class="form-group">
                <label>手机号：</label>
                <input type="text" name="mobile" id="mobile" class="form-control underline" value="${worldCupActivityForm.mobile}" />
            </div>
        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
            <style>
                .thumbnails-wrap {
                    border: 1px solid #ccc;
                    padding: 3px;
                    display: inline-block;
                }
                .thumbnails-wrap img {
                    min-width: 35px;
                    max-width: 70px;
                    height: 22px;
                }
                .popover {
                    max-width: 500px;
                }
                .popover img {
                    max-width: 460px;
                }
            </style>
        </tiles:putAttribute>

        <%-- JS全局变量定义、插件 (ignore) --%>
        <tiles:putAttribute name="pageGlobalImport" type="string">
            <script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
        </tiles:putAttribute>

        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type='text/javascript' src="${webRoot}/jsp/manager/activity/worldCupActivity/worldCupActivitywinning.js"></script>
        </tiles:putAttribute>

    </tiles:insertTemplate>
</shiro:hasPermission>
