<%--
  Created by IntelliJ IDEA.
  User: wgx
  Date: 2017/12/15
  Time: 14:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/jsp/base/pageBase.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags" %>
<shiro:hasPermission name="poundageException:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="手续费异常处理"/>
        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet"
                  media="screen">
        </tiles:putAttribute>

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">手续费异常处理</h1>
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
                                <c:set var="jspPrevDsb"
                                       value="${poundageExceptionForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                <c:set var="jspNextDsb"
                                       value="${poundageExceptionForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i
                                        class="fa fa-chevron-left"></i></a>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i
                                        class="fa fa-chevron-right"></i></a>
                                <shiro:hasPermission name="poundageExceptionList:ADD">
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i
                                            class="fa fa-plus"></i></a>
                                </shiro:hasPermission>
                                <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
                                   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i
                                        class="fa fa-refresh"></i></a>
                                <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip"
                                   data-placement="bottom"
                                   data-original-title="检索条件" data-toggle-class="active"
                                   data-toggle-target="#searchable-panel">
                                    <i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i>
                                </a>
                            </div>
                            <br/>
                                <%-- 列表一览 --%>
                            <table id="equiList" class="table table-striped table-bordered table-hover">
                                <colgroup>
                                    <col style="width: 55px;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th class="center">序号</th>
                                    <th class="center">分账订单号</th>
                                    <th class="center">分账流水号</th>
                                    <th class="center">分账时间段</th>
                                    <th class="center">分账金额</th>
                                    <th class="center">出借人分公司</th>
                                    <th class="center">分账类型</th>
                                    <th class="center">分账来源</th>
                                    <th class="center">收款方用户名</th>
                                    <th class="center">分账状态</th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>
                                <tbody id="roleTbody">
                                <c:choose>
                                    <c:when test="${empty poundageExceptionForm.recordList}">
                                        <tr>
                                            <td colspan="12">暂时没有数据记录</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${poundageExceptionForm.recordList }" var="record"
                                                   begin="0" step="1" varStatus="status">
                                            <tr>
                                                <td class="center"><c:out
                                                        value="${status.index+((poundageExceptionForm.paginatorPage - 1) * poundageExceptionForm.paginator.limit) + 1 }"></c:out></td>
                                                <td class="center"><c:out value="${record.nid}"></c:out></td>
                                                <td class="center"><c:out value="${record.seqNo}"></c:out></td>
                                                <td class="center"><hyjf:date value="${record.poundageTime}"></hyjf:date></td>
                                                <td class="center"><c:out value="${record.ledgerAmount}"></c:out></td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty record.investorCompany}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${record.investorCompany}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center">
                                                    <c:if test="${'1' eq record.ledgerType}">
                                                        <c:out value="按出借人分账"></c:out>
                                                    </c:if>
                                                    <c:if test="${'2' eq record.ledgerType}">
                                                        <c:out value="按借款人分账"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <c:if test="${'0' eq record.ledgerSource}">
                                                        <c:out value="全部"></c:out>
                                                    </c:if>
                                                    <c:if test="${'1' eq record.ledgerSource}">
                                                        <c:out value="服务费"></c:out>
                                                    </c:if>
                                                    <c:if test="${'3' eq record.ledgerSource}">
                                                        <c:out value="管理费"></c:out>
                                                    </c:if>
                                                    <c:if test="${'2' eq record.ledgerSource}">
                                                        <c:out value="债转服务费"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center"><c:out value="${record.payeeName}"></c:out></td>
                                                <td class="center">
                                                    <c:if test="${'0' eq record.ledgerStatus}">
                                                        <c:out value="未分账"></c:out>
                                                    </c:if>
                                                    <c:if test="${'1' eq record.ledgerStatus}">
                                                        <c:out value="已分账"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <div class="visible-md visible-lg hidden-sm hidden-xs">
                                                        <shiro:hasPermission name="poundageException:ADD">
                                                            <c:if test="${'0' eq record.ledgerStatus}">
                                                                <a class="btn btn-transparent btn-xs fn-ADD"
                                                                   data-id="${record.id }" data-toggle="tooltip"
                                                                   tooltip-placement="top" data-original-title="分账">分账</a>
                                                            </c:if>
                                                        </shiro:hasPermission>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                                <%-- 分页栏 --%>
                            <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction"
                                            paginator="${poundageExceptionForm.paginator}"></hyjf:paginator>
                            <br/> <br/>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <shiro:hasPermission name="poundageException:SEARCH">
                <input type="hidden" name="id" id="id"/>
                <input type="hidden" name="paginatorPage" id="paginator-page"
                       value="${poundageExceptionForm.paginatorPage}"/>
                <!-- 查询条件 -->
                <div class="form-group">
                    <label>分账订单号:</label>
                    <input type="text" name="nidSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageExceptionForm.nidSer}"/>
                </div>
                <div class="form-group">
                    <label>分账流水号:</label>
                    <input type="text" name="seqNoSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageExceptionForm.seqNoSer}"/>
                </div>
                <%--<div class="form-group">
                    <label>项目编号:</label>
                    <input type="text" name="projectNoSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageExceptionForm.projectNoSer}"/>
                </div>--%>
                <div class="form-group">
                    <label>分账时间段:</label>
                    <div class="input-group input-daterange datepicker">
                        <span class="input-icon">
                        <input type="text" name="poundageTimeStart" id="start-date-time" class="form-control underline"
                               value="${poundageExceptionForm.poundageTimeStart}"/>
                        <i class="ti-calendar"></i>
                        </span>
                        <span class="input-group-addon no-border bg-light-orange">~</span>
                        <input type="text" name="poundageTimeEnd" id="end-date-time" class="form-control underline"
                               value="${poundageExceptionForm.poundageTimeEnd}"/>
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <label>收款方用户名:</label>
                    <input type="text" name="userNameSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageExceptionForm.userNameSer}"/>
                </div>
                <div class="form-group">
                    <label>分账状态:</label>
                    <select name="ledgerStatusSer" class="form-control underline form-select2">
                        <option disabled value
                                <c:if test="${empty poundageExceptionForm.ledgerStatusSer}">selected="selected"</c:if>></option>
                        <option value="0"
                                <c:if test="${'0' eq poundageExceptionForm.ledgerStatusSer}">selected="selected"</c:if>>
                            <c:out value="未分账"></c:out>
                        </option>
                        <option value="1"
                                <c:if test="${'1' eq poundageExceptionForm.ledgerStatusSer}">selected="selected"</c:if>>
                            <c:out value="已分账"></c:out>
                        </option>
                        </option>
                    </select>
                </div>
            </shiro:hasPermission>
        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm"
                    style="border:none;width:100%;height:100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
            <link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet"
                  media="screen">
        </tiles:putAttribute>

        <%-- JS全局变量定义、插件 (ignore) --%>
        <tiles:putAttribute name="pageGlobalImport" type="string">
            <script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
        </tiles:putAttribute>
        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type='text/javascript' src="${webRoot}/jsp/exception/poundage/poundageExceptionList.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>