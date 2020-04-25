<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2018/5/11
  Time: 9:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/jsp/base/pageBase.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%--<c:set value="${fn:split('汇盈金服,注册记录', ',')}" var="functionPaths" scope="request"></c:set>--%>

<shiro:hasPermission name="userPortrait:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="用户画像评分"/>

        <%-- 画面主面板标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">用户画像评分</h1>
            <span class="mainDescription">本功能可以修改查询相应的用户画像信息。</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <shiro:hasPermission name="userPortrait:SEARCH">
                            <li><a href="${webRoot}/manager/userPortrait/userPortrait">用户画像信息</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="userPortrait:SEARCH">
                            <li class="active"><a href="${webRoot}/manager/userPortrait/userPortraitScore">用户画像评分</a></li>
                        </shiro:hasPermission>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="search-classic">
                                        <shiro:hasPermission name="userPortrait:SEARCH">
                                            <%-- 功能栏 --%>
                                            <div class="well">
                                                <c:set var="jspPrevDsb"
                                                       value="${obj.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                                <c:set var="jspNextDsb"
                                                       value="${obj.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                                   data-toggle="tooltip" data-placement="bottom"
                                                   data-original-title="上一页"><i
                                                        class="fa fa-chevron-left"></i></a>
                                                <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                                   data-toggle="tooltip" data-placement="bottom"
                                                   data-original-title="下一页"><i
                                                        class="fa fa-chevron-right"></i></a>
                                                    <%--<shiro:hasPermission name="userportrait:EXPORT">--%>
                                                    <%--<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"--%>
                                                    <%--data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>--%>
                                                    <%--</shiro:hasPermission>--%>
                                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
                                                   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新
                                                    <i
                                                            class="fa fa-refresh"></i></a>
                                                <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
                                                   data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
                                                   data-toggle-class="active" data-toggle-target="#searchable-panel"><i
                                                        class="fa fa-search margin-right-10"></i> <i
                                                        class="fa fa-chevron-left"></i></a>
                                            </div>
                                        </shiro:hasPermission>
                                        <br/>
                                            <%-- 角色列表一览 --%>
                                        <table id="equiList" class="table table-striped table-bordered table-hover">
                                            <thead>
                                            <tr>
                                                <th class="center">用户名</th>
                                                <th class="center">性别&年龄</th>
                                                <th class="center">资金量</th>
                                                <th class="center">累计收益</th>
                                                <th class="center">交易笔数</th>
                                                <th class="center">客源</th>
                                                <th class="center">出借进程</th>
                                                <th class="center">回款活跃</th>
                                                <th class="center">登陆活跃</th>
                                                <th class="center">邀约活跃</th>
                                                <th class="center">资金留存比</th>
                                                <th class="center">是否大客户</th>
                                                <th class="center">状态标签</th>
                                                <th class="center">身份标签</th>
                                                <th class="center">VIP</th>
                                                <th class="center">信任</th>
                                                <th class="center">竞争</th>
                                                <th class="center">流失</th>
                                                <th class="center">意向</th>
                                            </tr>
                                            </thead>
                                            <tbody id="userTbody">
                                            <c:choose>
                                                <c:when test="${empty obj.recordList}">
                                                    <tr>
                                                        <td colspan="19">暂时没有数据记录</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${obj.recordList }" var="record" begin="0"
                                                               step="1"
                                                               varStatus="status">
                                                        <tr>
                                                                <%--<td style="display: none" class="center">${(obj.paginatorPage-1)*obj.paginator.limit+status.index+1 }</td>--%>
                                                            <td style="display: none" id="userId"><c:out value="${record.userId}"></c:out></td>
                                                            <td class="center"><c:out value="${record.userName }"></c:out></td>
                                                            <td class="center"><c:out value="${record.sexAge }"></c:out></td>
                                                            <td class="center"><c:out value="${record.funds }"></c:out></td>
                                                            <td class="center"><c:out value="${record.interest }"></c:out></td>
                                                            <td class="center"><c:out value="${record.tradeNumber }"></c:out></td>
                                                            <td class="center"><c:out value="${record.customerSource }"></c:out>
                                                            <td class="center"><c:out value="${record.investProcess }"></c:out></td>
                                                            <td class="center"><c:out value="${record.returnActive }"></c:out></td>
                                                            <td class="center"><c:out value="${record.loginActive }"></c:out></td>
                                                            <td class="center"><c:out value="${record.inviteActive }"></c:out></td>
                                                            <td class="center"><c:out value="${record.fundRetentionPercent }"></c:out></td>
                                                            <td class="center"><c:out value="${record.isBigCustomer }"></c:out></td>
                                                            <td class="center"><c:out value="${record.statusTab }"></c:out>
                                                            <td class="center"><c:out value="${record.identityLabel}"></c:out></td>
                                                            <td class="center"><c:out value="${record.vip }"></c:out></td>
                                                            <td class="center"><c:out value="${record.trust }"></c:out></td>
                                                            <td class="center"><c:out value="${record.compete }"></c:out></td>
                                                            <td class="center"><c:out value="${record.loss }"></c:out></td>
                                                            <td class="center"><c:out value="${record.intention }"></c:out></td>
                                                            <%--<td class="center">--%>

                                                                <%--<div class="visible-md visible-lg hidden-sm hidden-xs">--%>
                                                                        <%--&lt;%&ndash;<shiro:hasPermission name="userportrait:MODIFY">&ndash;%&gt;--%>
                                                                    <%--<a class="btn btn-transparent btn-xs fn-Modify"--%>
                                                                       <%--data-id="${record.userId }"--%>
                                                                       <%--data-toggle="tooltip" data-placement="top"--%>
                                                                       <%--data-original-title="修改"><i--%>
                                                                            <%--class="fa fa-pencil"></i></a>--%>
                                                                        <%--&lt;%&ndash;</shiro:hasPermission>&ndash;%&gt;--%>
                                                                <%--</div>--%>
                                                                <%--<div class="visible-xs visible-sm hidden-md hidden-lg">--%>
                                                                    <%--<div class="btn-group">--%>
                                                                        <%--<button type="button"--%>
                                                                                <%--class="btn btn-primary btn-o btn-sm"--%>
                                                                                <%--data-toggle="dropdown">--%>
                                                                            <%--<i class="fa fa-cog"></i>&nbsp;<span--%>
                                                                                <%--class="caret"></span>--%>
                                                                        <%--</button>--%>
                                                                        <%--<ul class="dropdown-menu pull-right dropdown-light"--%>
                                                                            <%--role="menu">--%>
                                                                                <%--&lt;%&ndash;<shiro:hasPermission name="userportrait:MODIFY">&ndash;%&gt;--%>
                                                                            <%--<li>--%>
                                                                                <%--<a class="fn-Modify"--%>
                                                                                   <%--data-id="${record.userId }">修改</a>--%>
                                                                            <%--</li>--%>
                                                                                <%--&lt;%&ndash;</shiro:hasPermission>&ndash;%&gt;--%>
                                                                        <%--</ul>--%>
                                                                    <%--</div>--%>
                                                                <%--</div>--%>

                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                            </tbody>
                                        </table>
                                            <%-- 分页栏 --%>
                                        <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="userPortraitScore" paginator="${obj.paginator}"></hyjf:paginator>
                                        <br/><br/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>
        <%-- 边界面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="userId" id="userId" value="${obj.userId}"/>
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${obj.paginatorPage}"/>
            <!-- 检索条件 -->
            <div class="form-group">
                <label>用户名</label>
                <input type="text" name="userName" class="form-control input-sm underline" maxlength="20"
                       value="${obj.userName}"/>
            </div>

        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm"
                    style="border:none;width:100%;height:100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
        </tiles:putAttribute>

        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type="text/javascript"> var webRoot = "${webRoot}";</script>
            <script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
            <script type='text/javascript'
                    src="${webRoot}/jsp/manager/users/userportrait/userportraitscore.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>