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
<script type="text/javascript">
    var startDate = "";
    var endDate = "";
    getTime();
    function getTime(){
        var nowdays = new Date();
        var year = nowdays.getFullYear();
        var month = nowdays.getMonth()+1;
        if(month==0){
            month = 12;
            year = year-1;
        }
        if(month<10){
            month = '0'+month;
        }
        var myDate = new Date(year,month,0);
        startDate = year+'-'+month+'-01'; //上个月第一天
        endDate = year+'-'+month+'-'+myDate.getDate();//上个月最后一天
    }
    function getPerformanceInit(){
        document.getElementById("performanceInit").href="${webRoot}/manager/activity/namimarketing/performanceInit?joinTimeStart="+startDate+"&joinTimeEnd"+"="+endDate;
    }
    function getRefferDetailInit(){
        document.getElementById("refferDetailInit").href="${webRoot}/manager/activity/namimarketing/refferDetailInit?joinTimeStart="+startDate+"&joinTimeEnd"+"="+endDate;
    }
    //    alert(getYearLastDay());
</script>
<shiro:hasPermission name="activitylist:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="纳米营销方案" />

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">邀请人返现统计</h1>
            <span class="mainDescription">本功能可以查询查看导出。</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <shiro:hasPermission name="activitylist:VIEW">
                            <li ><a href="${webRoot}/manager/activity/namimarketing/init">邀请明细</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="activitylist:VIEW">
                            <li ><a href="${webRoot}/manager/activity/namimarketing/performanceInit" id="performanceInit" onclick="getPerformanceInit()">年化业绩返现详情</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="activitylist:VIEW">
                            <li ><a href="${webRoot}/manager/activity/namimarketing/refferDetailInit" id="refferDetailInit" onclick="getRefferDetailInit()">邀请人返现明细</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="activitylist:VIEW">
                            <li class="active"><a href="${webRoot}/manager/activity/namimarketing/refferTotalInit">邀请人返现统计</a></li>
                        </shiro:hasPermission>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                                <%-- 功能栏 --%>
                            <div class="well">
                                <c:set var="jspPrevDsb" value="${naMiMarketingForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                <c:set var="jspNextDsb" value="${naMiMarketingForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
                            <table id="equiList" class="table table-striped table-bordered table-hover" style="font-size: 13px;">
                                <colgroup>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th class="center">序号</th>
                                    <th class="center">
                                        <c:choose>
                                            <c:when test="${ !empty naMiMarketingForm.sort and naMiMarketingForm.col eq 'join_time'}">
                                                <a href="#" class="fn-Sort" data-col="join_time" data-sort="${naMiMarketingForm.sort}" style="color:black;">月份</a>&nbsp;<i class="fa fa-sort"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="#" class="fn-Sort"  data-col="join_time" data-sort="DESC" style="color:black;">月份</a>&nbsp;<i class="fa fa-sort"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                    <th class="center">账户名</th>
                                    <th class="center">姓名</th>
                                    <%--<th class="center">部门</th>--%>

                                    <th class="center">返现明细</th>
                                    <th class="center">
                                        <c:choose>
                                            <c:when test="${  !empty naMiMarketingForm.sort and naMiMarketingForm.col eq 'return_amount'}">
                                                <a href="#" class="fn-Sort" data-col="return_amount" data-sort="${naMiMarketingForm.sort}" style="color:black;">返现合计（元）</a>&nbsp;<i class="fa fa-sort"></i>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="#" class="fn-Sort"  data-col="return_amount" data-sort="DESC" style="color:black;">返现合计（元）</a>&nbsp;<i class="fa fa-sort"></i>
                                            </c:otherwise>
                                        </c:choose>
                                    </th>
                                </tr>
                                </thead>
                                <tbody id="roleTbody">
                                <c:choose>
                                    <c:when test="${empty recordList}">
                                        <tr><td colspan="6">暂时没有数据记录</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
                                            <tr>
                                                <td class="center"><c:out value="${status.index+((naMiMarketingForm.paginatorPage - 1) * naMiMarketingForm.paginator.limit) + 1 }"></c:out></td>
                                                <td class="center"><c:out value="${record.regTime }"></c:out></td>
                                                <td class="center"><c:out value="${record.username }"></c:out></td>
                                                <td class="center"><c:out value="${record.truename }"></c:out></td>

                                                <%--<td class="center"><c:out value="${record.debtName }"></c:out></td>--%>
                                                <td class="center">
                                                    <a class="fn-Info" data-id="${record.username }" href="refferDetailInit.do?username=${record.username }&month=${record.regTime }">查看 </a>
                                                </td>
                                                <td class="center"><c:out value="${record.returnAmount }"></c:out></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                                <%-- 分页栏 --%>
                            <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="refferTotalInit" paginator="${naMiMarketingForm.paginator}"></hyjf:paginator>
                            <p style="height:40px;float:left;font-size: 13px;">返现总计:${totalAmount }元</p>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="id" id="id" />
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${naMiMarketingForm.paginatorPage}" />
            <input type="hidden" name="col" id="col" value="${naMiMarketingForm.col}" />
            <input type="hidden" name="sort" id="sort" value="${naMiMarketingForm.sort}" />
            <div class="form-group">
                <label>账户名:</label>
                <input type="text" name="username" class="form-control input-sm underline" value="${naMiMarketingForm.username}" />
            </div>
            <div class="form-group">
                <label>姓名:</label>
                <input type="text" name="truename" id="truename" class="form-control underline" value="${naMiMarketingForm.truename}" />
            </div>
            <div class="form-group">
                <label>月份：</label>
                <select name="month" class="form-control underline form-select2">
                    <option value="">全部</option>
                    <c:forEach items="${monthList }" var="item" begin="0" step="1">
                        <option value="${item }"
                            <c:if test="${item eq naMiMarketingForm.month}">selected="selected"</c:if>>
                            <c:out value="${item }"></c:out>
                        </option>
                    </c:forEach>
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
            <script type='text/javascript' src="${webRoot}/jsp/manager/activity/namimarketing/refferTotal.js"></script>
        </tiles:putAttribute>

    </tiles:insertTemplate>
</shiro:hasPermission>
