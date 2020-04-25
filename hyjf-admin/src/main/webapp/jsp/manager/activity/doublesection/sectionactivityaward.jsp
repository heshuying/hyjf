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


<shiro:hasPermission name="activitylist:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="双节活动" />

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">双节活动</h1>
            <span class="mainDescription">本功能可以查询导出。</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <shiro:hasPermission name="activitylist:VIEW">
                            <li><a href="${webRoot}/manager/activity/doublesectionactivity/init">单笔出借明细</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="activitylist:VIEW">
                            <li class="active"><a href="${webRoot}/manager/activity/doublesectionactivity/awardinit">奖励明细</a></li>
                        </shiro:hasPermission>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                                <%-- 功能栏 --%>
                            <div class="well">
                                <c:set var="jspPrevDsb" value="${dousectionactivityListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                <c:set var="jspNextDsb" value="${dousectionactivityListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                                <shiro:hasPermission name="activitylist:EXPORT">
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
                                </shiro:hasPermission>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
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
                                    <th class="center">奖励名称</th>
                                    <th class="center">奖励类型</th>
                                    <th class="center">奖励批号</th>
                                    <th class="center">发放方式</th>
                                    <th class="center">账户名</th>
                                    <th class="center">姓名</th>
                                    <th class="center">手机号</th>
                                    <th class="center">状态</th>
                                    <th class="center">
                                        <c:choose>
                                            <c:when test="${ !empty dousectionactivityListForm.sort and dousectionactivityListForm.col eq 'qx.send_time' }">
                                                <a href="#" class="fn-Sort" data-col="qx.send_time" data-sort="${dousectionactivityListForm.sort}" style="color:black;">获得时间</a>&nbsp;<i class="fa fa-sort"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="#" class="fn-Sort" data-col="qx.send_time" data-sort="DESC" style="color:black;">获得时间</a>&nbsp;<i class="fa fa-sort"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                    <th class="center">
                                            <%--<c:if test="${ qixiactivityListForm.sort eq 'ASC' }">DESC</c:if><c:if test="${ qixiactivityListForm.sort eq 'DESC' }">ASC</c:if>"--%>
                                        <c:choose>
                                            <c:when test="${ !empty dousectionactivityListForm.sortTwo and dousectionactivityListForm.colTwo eq 'qx.update_time' }">
                                                <a href="#" class="fn-SortTwo" data-colTwo="qx.update_time" data-sortTwo="${dousectionactivityListForm.sortTwo}" style="color:black;">发放时间</a>&nbsp;<i class="fa fa-sort"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="#" class="fn-SortTwo" data-colTwo="qx.update_time" data-sortTwo="DESC" style="color:black;">发放时间</a>&nbsp;<i class="fa fa-sort"></i>
                                            </c:otherwise>
                                        </c:choose>
                                        <%--出借时间--%>
                                    </th>
                                </tr>
                                </thead>
                                <tbody id="roleTbody">
                                <c:choose>
                                    <c:when test="${empty dousectionactivityListForm.recordList}">
                                        <tr><td colspan="11">暂时没有数据记录</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${dousectionactivityListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
                                            <tr>
                                                <td class="center"><c:out value="${status.index+((dousectionactivityListForm.paginatorPage - 1) * dousectionactivityListForm.paginator.limit) + 1 }"></c:out></td>
                                                <td class="center"><c:out value="${record.rewardName }"></c:out></td>
                                                <td class="center"><c:out value="${record.rewardType }"></c:out></td>
                                                <td class="center"><c:out value="${record.rewardId }"></c:out></td>
                                                <td class="center">
                                                    <c:if test="${record.distributionStatus != null}">
                                                        <c:if test="${record.distributionStatus == '0'}">系统发放</c:if>
                                                        <c:if test="${record.distributionStatus == '1'}">手动发放</c:if>
                                                    </c:if>
                                                    <%--<c:if test="${record.distributionStatus == null}">--%>
                                                            <%--${record.rewardStatus}--%>
                                                     <%--</c:if>--%>
                                                </td>
                                                <td class="center"><c:out value="${record.userName }"></c:out></td>
                                                <td class="center"><c:out value="${record.trueName }"></c:out></td>
                                                <td class="center"><c:out value="${record.mobile }"></c:out></td>
                                                <td class="center">
                                                   <c:if test="${record.rewardStatus != null}">
                                                       <c:if test="${record.rewardStatus == '0'}">
                                                           待发放
                                                           <a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top"
                                                              data-original-title="更改状态"><i class="fa fa-pencil"></i></a>
                                                       </c:if>
                                                       <c:if test="${record.rewardStatus == '1'}">已发放
                                                           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                       </c:if>
                                                   </c:if>
                                                    <%--<c:if test="${record.rewardStatus == null}">--%>
                                                        <%--${record.rewardStatus }--%>
                                                    <%--</c:if>--%>
                                                </td>
                                                <td class="center">
                                                    <c:out value=" ${fn:substring(record.createTime, 0, 19)}"></c:out>
                                                </td>
                                                <td class="center">
                                                    <c:if test="${record.rewardStatus != null}">
                                                        <c:if test="${record.rewardStatus == '1'}">
                                                            <c:out value=" ${fn:substring(record.updateTime, 0, 19)}"></c:out>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                                <%-- 分页栏 --%>
                            <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="awardinit" paginator="${dousectionactivityListForm.paginator}"></hyjf:paginator>
                            <br/><br/>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${dousectionactivityListForm.paginatorPage}" />
            <input type="hidden" name="ids" id="ids" />
            <input type="hidden" name="col" id="col" value="${dousectionactivityListForm.col}" />
            <input type="hidden" name="sort" id="sort" value="${dousectionactivityListForm.sort}" />
            <input type="hidden" name="colTwo" id="colTwo" value="${dousectionactivityListForm.colTwo}" />
            <input type="hidden" name="sortTwo" id="sortTwo" value="${dousectionactivityListForm.sortTwo}" />
            <div class="form-group">
                <label>奖励类型</label>
                <select name="rewardType" class="form-control underline form-select2">
                    <option value="">全部</option>
                        <%--<option value="0" >汇直投</option>--%>
                        <%--<option value="1">汇计划</option>--%>
                    <option value="加息券" <c:if test="${dousectionactivityListForm.rewardType == '加息券'}">selected="selected"</c:if>>加息券</option>
                    <option value="代金券" <c:if test="${dousectionactivityListForm.rewardType == '代金券'}">selected="selected"</c:if>>代金券</option>
                    <option value="实物" <c:if test="${dousectionactivityListForm.rewardType == '实物'}">selected="selected"</c:if>>实物</option>
                </select>
            </div>
            <div class="form-group">
                <label>奖励批号</label>
                <input type="text" name="rewardId" id="rewardId" class="form-control underline" value="${dousectionactivityListForm.rewardId}" />
            </div>
            <div class="form-group">
                <label>发放方式</label>
                <select name="distributionStatus" class="form-control underline form-select2">
                    <option value="">全部</option>
                    <option value="0" <c:if test="${dousectionactivityListForm.distributionStatus == '0'}">selected="selected"</c:if>>系统发放</option>
                    <option value="1" <c:if test="${dousectionactivityListForm.distributionStatus == '1'}">selected="selected"</c:if>>手动发放</option>
                </select>
            </div>
            <div class="form-group">
                <label>账户名</label>
                <input type="text" name="userName" class="form-control input-sm underline" value="${dousectionactivityListForm.userName}" />
            </div>
            <div class="form-group">
                <label>姓名</label>
                <input type="text" name="trueName" id="trueName" class="form-control underline" value="${dousectionactivityListForm.trueName}" />
            </div>
            <div class="form-group">
                <label>手机号</label>
                <input type="text" name="mobile" id="mobile" class="form-control underline" value="${dousectionactivityListForm.mobile}" />
            </div>
            <div class="form-group">
                <label>状态</label>
                <select name="rewardStatus" class="form-control underline form-select2">
                    <option value="">全部</option>
                    <option value="0" <c:if test="${dousectionactivityListForm.rewardStatus == '0'}">selected="selected"</c:if>>待发放</option>
                    <option value="1" <c:if test="${dousectionactivityListForm.rewardStatus == '1'}">selected="selected"</c:if>>已发放</option>
                </select>
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
            <script type='text/javascript' src="${webRoot}/jsp/manager/activity/doublesection/sectionactivityaward.js"></script>
        </tiles:putAttribute>

    </tiles:insertTemplate>
</shiro:hasPermission>
