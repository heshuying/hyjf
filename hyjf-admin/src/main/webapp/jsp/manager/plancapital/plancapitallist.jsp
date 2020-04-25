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

<shiro:hasPermission name="plancapitallist:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="资金计划列表" />
        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
        </tiles:putAttribute>

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">资金计划列表</h1>
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
                                <c:set var="jspPrevDsb" value="${planCapitalListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                <c:set var="jspNextDsb" value="${planCapitalListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                                <shiro:hasPermission name="plancapitallist:EXPORT">
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
                                </shiro:hasPermission>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
                                <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
                                   data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
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
                                        <th class="center">日期</th>
                                        <th class="center">智投编号</th>
                                        <th class="center">智投名称 </th>
                                        <th class="center">服务回报期限</th>
                                        <th class="center">复投总额（元）</th>
                                        <th class="center">债转总额（元）</th>
                                        <th class="center">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="roleTbody">
                                    <c:choose>
                                        <c:when test="${empty planCapitalListForm.recordList}">
                                            <tr><td colspan="8">暂时没有数据记录</td></tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${planCapitalListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
                                                <tr>
                                                    <td class="center"><c:out value="${status.index+((planCapitalListForm.paginatorPage - 1) * planCapitalListForm.paginator.limit) + 1 }"></c:out></td>
                                                    <td class="center"><fmt:formatDate value="${record.date}" pattern="yyyy-MM-dd"/></td>
                                                    <td class="center"><c:out value="${record.planNid }"></c:out></td>
                                                    <td class="center"><c:out value="${record.planName }"></c:out></td>
                                                    <td class="center">
                                                        <c:out value="${record.lockPeriod}" ></c:out>
                                                        <c:out value="${record.isMonth == '0' ? '天' : '个月'}" ></c:out>
                                                    </td>
                                                    <td align="right"><fmt:formatNumber value="${record.reinvestAccount}" type="number" pattern="#,##0.00#" /></td>
                                                    <td align="right"><fmt:formatNumber value="${record.creditAccount}" type="number" pattern="#,##0.00#" /></td>
                                                    <td class="center">
                                                        <div class="visible-md visible-lg hidden-sm hidden-xs">
                                                            <shiro:hasPermission name="plancapitallist:INFO">
                                                                <a class="btn btn-transparent btn-xs fn-ReinvestInfo" data-date="<fmt:formatDate value="${record.date}" pattern="yyyy-MM-dd"/>" data-plannid="${record.planNid }" data-toggle="tooltip" tooltip-placement="top" data-original-title="复投详情"><i class="fa fa-pencil"></i></a>
                                                                <a class="btn btn-transparent btn-xs fn-CreditInfo" data-date="<fmt:formatDate value="${record.date}" pattern="yyyy-MM-dd"/>" data-plannid="${record.planNid }" data-toggle="tooltip" tooltip-placement="top" data-original-title="转让详情"><i class="fa fa-pencil"></i></a>
                                                            </shiro:hasPermission>
                                                            <%--<shiro:hasPermission name="borrowflow:DELETE">--%>
                                                                <%--<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>--%>
                                                            <%--</shiro:hasPermission>--%>
                                                        </div>
                                                        <%--<div class="visible-xs visible-sm hidden-md hidden-lg">--%>
                                                            <%--<div class="btn-group" dropdown="">--%>
                                                                <%--<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">--%>
                                                                    <%--<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>--%>
                                                                <%--</button>--%>
                                                                <%--<ul class="dropdown-menu pull-right dropdown-light" role="menu">--%>
                                                                    <%--<shiro:hasPermission name="plancapitallist:INFO">--%>
                                                                        <%--<li><a class="fn-ReinvestInfo" data-id="${record.id }">复投详情</a></li>--%>
                                                                        <%--<li><a class="fn-CreditInfo" data-id="${record.id }">转让详情</a></li>--%>
                                                                    <%--</shiro:hasPermission>--%>
                                                                        <%--&lt;%&ndash;<shiro:hasPermission name="borrowflow:DELETE">&ndash;%&gt;--%>
                                                                        <%--&lt;%&ndash;<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>&ndash;%&gt;--%>
                                                                        <%--&lt;%&ndash;</shiro:hasPermission>&ndash;%&gt;--%>
                                                                <%--</ul>--%>
                                                            <%--</div>--%>
                                                        <%--</div>--%>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    <%-- 总计 --%>
                                    <tr>
                                        <th class="center">总计</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <th>&nbsp;</th>
                                        <td align="right"><fmt:formatNumber value="${sumRecord.sumReinvestAccount }" pattern="#,##0.00#"/></td>
                                        <td align="right"><fmt:formatNumber value="${sumRecord.sumCreditAccount }" pattern="#,##0.00#"/></td>
                                        <th>&nbsp;</th>
                                    </tr>
                                    </tbody>
                                </table>
                                <%-- 分页栏 --%>
                            <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${planCapitalListForm.paginator}"></hyjf:paginator>
                            <br /> <br />
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <shiro:hasPermission name="plancapitallist:SEARCH">
                <input type="hidden" name="dateKey" id="dateKey" />
                <input type="hidden" name="planNid" id="planNid" />
                <input type="hidden" name="paginatorPage" id="paginator-page" value="${planCapitalListForm.paginatorPage}" />
                <!-- 查询条件 -->
                <div class="form-group">
                    <label>日期</label>
                    <div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="dateFromSrch" id="dateFromSrch" class="form-control underline" value="${planCapitalListForm.dateFromSrch}" />
							<i class="ti-calendar"></i> </span>
                        <span class="input-group-addon no-border bg-light-orange">~</span>
                        <input type="text" name="dateToSrch" id="dateToSrch" class="form-control underline" value="${planCapitalListForm.dateToSrch}" />
                    </div>
                </div>
                <div class="form-group">
                    <label>智投编号</label>
                    <input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${planCapitalListForm.planNidSrch}" />
                </div>
                <div class="form-group">
                    <label>智投名称</label>
                    <input type="text" name="planNameSrch" id="planNameSrch" class="form-control input-sm underline" value="${planCapitalListForm.planNameSrch}" />
                </div>
                <div class="form-group">
                    <label>服务回报期限</label>
                    <input type="text" name="lockPeriodSrch" id="lockPeriodSrch" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" class="form-control input-sm underline" value="${planCapitalListForm.lockPeriodSrch}" />
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
            <script type='text/javascript' src="${webRoot}/jsp/manager/plancapital/plancapitallist.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>
