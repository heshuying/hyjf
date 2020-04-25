<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2018/10/11
  Time: 9:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<%--<shiro:hasPermission name="operationLog:VIEW">--%>
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="会员操作日志" />

        <%-- 画面主面板标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">会员操作日志</h1>
            <span class="mainDescription">本功能可以查询相应的会员操作日志信息。</span>
        </tiles:putAttribute>

        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <%--<div class="tabbable">--%>
                    <%--<ul class="nav nav-tabs" id="myTab">--%>
                        <%--<shiro:hasPermission name="userPortrait:SEARCH">--%>
                            <%--<li class="active"><a href="${webRoot}/manager/userPortrait/userPortrait">用户画像信息</a></li>--%>
                        <%--</shiro:hasPermission>--%>
                        <%--<shiro:hasPermission name="userPortrait:SEARCH">--%>
                            <%--<li><a href="${webRoot}/manager/userPortrait/userPortraitScore">用户画像评分</a></li>--%>
                        <%--</shiro:hasPermission>--%>
                    <%--</ul>--%>
                    <%--<div class="tab-content">--%>
                        <%--<div class="tab-pane fade in active">--%>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="search-classic">
                                            <%--<shiro:hasPermission name="userportrait:SEARCH">--%>
                                            <%-- 功能栏 --%>
                                        <div class="well">
                                            <c:set var="jspPrevDsb" value="${obj.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                            <c:set var="jspNextDsb" value="${obj.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                            <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                               data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
                                            <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                               data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                                            <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
                                               data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
                                            <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
                                               data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
                                            <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
                                               data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
                                               data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
                                        </div>
                                            <%--</shiro:hasPermission>--%>
                                        <br/>
                                            <%-- 角色列表一览 --%>
                                        <table id="equiList" class="table table-striped table-bordered table-hover">
                                            <thead>
                                            <tr>
                                                <th class="center">序号</th>
                                                <th class="center">活动类型</th>
                                                <th class="center">用户角色</th>
                                                <th class="center">用户名</th>
                                                <th class="center">操作平台</th>
                                                <th class="center">备注</th>
                                                <th class="center">IP</th>
                                                <th class="center">操作时间</th>
                                            </tr>
                                            </thead>
                                            <tbody id="userTbody">
                                            <c:choose>
                                                <c:when test="${empty obj.recordList}">
                                                    <tr><td colspan="8">暂时没有数据记录</td></tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${obj.recordList }" var="record" begin="0" step="1" varStatus="status">
                                                        <tr>
                                                            <td class="center">${(obj.paginatorPage-1)*obj.paginator.limit+status.index+1 }</td>
                                                            <td class="center">
                                                                <c:choose>
                                                                    <c:when test="${record.operationType == 1}">
                                                                        登录
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 2}">
                                                                        登出
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 3}">
                                                                        开户
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 4}">
                                                                        出借确认
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 5}">
                                                                        转让确认
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 6}">
                                                                        修改交易密码
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 7}">
                                                                        修改登录密码
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 8}">
                                                                        绑定邮箱
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 9}">
                                                                        修改邮箱
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 10}">
                                                                        绑定银行卡
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 11}">
                                                                        解绑银行卡
                                                                    </c:when>
                                                                    <c:when test="${record.operationType == 12}">
                                                                        风险测评
                                                                    </c:when>
                                                                </c:choose>
                                                            </td>
                                                            <td class="center">
                                                                <c:choose>
                                                                    <c:when test="${record.userRole eq 1}">
                                                                        出借人
                                                                    </c:when>
                                                                    <c:when test="${record.userRole eq 2}">
                                                                        借款人
                                                                    </c:when>
                                                                    <c:when test="${record.userRole eq 3}">
                                                                        担保机构
                                                                    </c:when>
                                                                </c:choose>
                                                            </td>
                                                            <td class="center"><c:out value="${record.userName }"></c:out></td>
                                                            <td class="center">
                                                                <c:choose>
                                                                    <c:when test="${record.platform == 0}">
                                                                        PC
                                                                    </c:when>
                                                                    <c:when test="${record.platform == 1}">
                                                                        wechat
                                                                    </c:when>
                                                                    <c:when test="${record.platform == 2}">
                                                                        Android
                                                                    </c:when>
                                                                    <c:when test="${record.platform == 3}">
                                                                        IOS
                                                                    </c:when>
                                                                </c:choose>
                                                            </td>
                                                            <td class="center"><c:out value="${record.remark }"></c:out>
                                                            <td class="center"><c:out value="${record.ip}"></c:out></td>
                                                            <td class="center"><fmt:formatDate value="${record.operationTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                            <td class="center">

                                                                <%--<div class="visible-md visible-lg hidden-sm hidden-xs">--%>
                                                                        <%--&lt;%&ndash;<shiro:hasPermission name="userportrait:MODIFY">&ndash;%&gt;--%>
                                                                    <%--<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.userId }"--%>
                                                                       <%--data-toggle="tooltip" data-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>--%>
                                                                        <%--&lt;%&ndash;</shiro:hasPermission>&ndash;%&gt;--%>
                                                                <%--</div>--%>
                                                                <div class="visible-xs visible-sm hidden-md hidden-lg">
                                                                    <div class="btn-group">
                                                                        <button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
                                                                            <i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
                                                                        </button>
                                                                        <%--<ul class="dropdown-menu pull-right dropdown-light" role="menu">--%>
                                                                                <%--&lt;%&ndash;<shiro:hasPermission name="userportrait:MODIFY">&ndash;%&gt;--%>
                                                                            <%--<li>--%>
                                                                                <%--<a class="fn-Modify" data-id="${record.userId }">修改</a>--%>
                                                                            <%--</li>--%>
                                                                                <%--&lt;%&ndash;</shiro:hasPermission>&ndash;%&gt;--%>
                                                                        <%--</ul>--%>
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
                                        <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="operationLog" paginator="${obj.paginator}"></hyjf:paginator>
                                        <br/><br/>
                                    </div>
                                </div>
                            </div>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            </div>
        </tiles:putAttribute>
        <%-- 边界面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="id" id="id" value= ""/>
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${obj.paginatorPage}" />
            <!-- 检索条件 -->
            <div class="form-group">
                <label>用户名</label>
                <input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${obj.userName}" />
            </div>
            <div class="form-group">
                <label>活动类型</label>
                <select name="operationType" id="operationType" class="form-control underline form-select2">
                    <option value="">全部</option>
                    <option value="1" <c:if test="${obj.operationType == 1}">selected="selected"</c:if>>
                        <c:out value="登录"></c:out>
                    </option>
                    <option value="2" <c:if test="${obj.operationType == 2}">selected="selected"</c:if>>
                        <c:out value="登出"></c:out>
                    </option><option value="3" <c:if test="${obj.operationType == 3}">selected="selected"</c:if>>
                        <c:out value="开户"></c:out>
                    </option></option><option value="4" <c:if test="${obj.operationType == 4}">selected="selected"</c:if>>
                        <c:out value="出借确认"></c:out>
                    </option></option></option><option value="5" <c:if test="${obj.operationType == 5}">selected="selected"</c:if>>
                        <c:out value="转让确认"></c:out>
                    </option></option></option><option value="6" <c:if test="${obj.operationType == 6}">selected="selected"</c:if>>
                        <c:out value="修改交易密码"></c:out>
                    </option></option></option><option value="7" <c:if test="${obj.operationType == 7}">selected="selected"</c:if>>
                        <c:out value="修改登录密码"></c:out>
                    </option></option></option><option value="8" <c:if test="${obj.operationType == 8}">selected="selected"</c:if>>
                        <c:out value="绑定邮箱"></c:out>
                    </option></option></option><option value="9" <c:if test="${obj.operationType == 9}">selected="selected"</c:if>>
                        <c:out value="修改邮箱"></c:out>
                    </option></option></option><option value="10" <c:if test="${obj.operationType == 10}">selected="selected"</c:if>>
                        <c:out value="绑定银行卡"></c:out>
                    </option></option></option><option value="11" <c:if test="${obj.operationType == 11}">selected="selected"</c:if>>
                        <c:out value="解绑银行卡"></c:out>
                    </option></option></option><option value="12" <c:if test="${obj.operationType == 12}">selected="selected"</c:if>>
                        <c:out value="风险测评"></c:out>
                    </option>
                </select>
            </div>
            <div class="form-group">
                <label>用户角色</label>
                <select name="userRole" id="userRole" class="form-control underline form-select2">
                    <option value="">全部</option>
                    <option value="1" <c:if test="${obj.userRole == 1}">selected="selected"</c:if>>
                        <c:out value="出借人"></c:out>
                    </option><option value="2" <c:if test="${obj.userRole == 2}">selected="selected"</c:if>>
                        <c:out value="借款人"></c:out>
                    </option></option><option value="3" <c:if test="${obj.userRole == 3}">selected="selected"</c:if>>
                        <c:out value="担保机构"></c:out>
                    </option>
                </select>
            </div>
            <div class="form-group">
                <label>操作时间</label>
                <div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
                                                     name="operationTimeStart" id="start-date-time" class="form-control underline" value="${obj.operationTimeStart}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="operationTimeEnd" id="end-date-time" class="form-control underline" value="${obj.operationTimeEnd}" />
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
        </tiles:putAttribute>

        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type="text/javascript"> var webRoot = "${webRoot}";</script>
            <script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
            <script type='text/javascript' src="${webRoot}/jsp/manager/users/operationlog/useroperationlog.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
<%--</shiro:hasPermission>--%>

