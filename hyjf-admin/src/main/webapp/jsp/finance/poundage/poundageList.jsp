<%@ page import="com.hyjf.common.util.GetDate" %>
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

<shiro:hasPermission name="poundage:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="手续费分账"/>

        <%-- 画面主面板标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">记录</h1>
            <span class="mainDescription">本功能可以修改查询相应的记录。</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="row">
                    <div class="col-md-12">
                        <div class="search-classic">
                            <shiro:hasPermission name="poundage:SEARCH">
                                <%-- 功能栏 --%>
                                <div class="well">
                                    <c:set var="jspPrevDsb"
                                           value="${poundageForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                    <c:set var="jspNextDsb"
                                           value="${poundageForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i
                                            class="fa fa-chevron-left"></i></a>
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i
                                            class="fa fa-chevron-right"></i></a>
                                    <shiro:hasPermission name="poundage:EXPORT">
                                        <a class="btn btn-o btn-primary btn-sm fn-Export"
                                           data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表
                                            <i class="fa fa-plus "></i> </a>
                                    </shiro:hasPermission>
                                    <a class="btn btn-o btn-primary btn-sm fn-Refresh"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i
                                            class="fa fa-refresh"></i></a>
                                    <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
                                       data-toggle-class="active" data-toggle-target="#searchable-panel"><i
                                            class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
                                </div>
                            </shiro:hasPermission>
                            <br/>
                                <%-- 角色列表一览 --%>
                            <table id="equiList" class="table table-striped table-bordered table-hover">
                                <colgroup>
                                    <col style="width:55px;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th class="center">序号</th>
                                    <th class="center">收款方用户名</th>
                                    <th class="center">收款方姓名</th>
                                    <th class="center">总分账金额(元)</th>
                                    <th class="center">总分账笔数</th>
                                    <th class="center">分账时间段</th>
                                    <th class="center">分账状态</th>
                                    <th class="center">分账时间</th>
                                    <th class="center">分账订单号</th>
                                    <th class="center">分账流水号</th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>
                                <tbody id="userTbody">
                                <c:choose>
                                    <c:when test="${empty poundageForm.recordList}">
                                        <tr>
                                            <td colspan="11">暂时没有数据记录</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="amountSum" value="0" />
                                        <c:set var="quantitySum" value="0" />
                                        <c:forEach items="${poundageForm.recordList }" var="record" begin="0" step="1"
                                                   varStatus="status">
                                            <tr>
                                                <td class="center"><c:out
                                                        value="${status.index+((subconfigForm.paginatorPage - 1) * subconfigForm.paginator.limit) + 1 }"></c:out></td>
                                                <td class="center"><c:out value="${record.userName}"></c:out></td>
                                                <td class="center"><c:out value="${record.realName}"></c:out></td>
                                                <td class="center"><c:out value="${record.amount}"></c:out></td>
                                                <td class="center"><c:out value="${record.quantity}"></c:out></td>
                                                <td class="center"><hyjf:date value="${record.poundageTime}"></hyjf:date></td>
                                                <td class="center">
                                                    <c:if test="${'0' eq record.status}">
                                                        <c:out value="未审核"></c:out>
                                                    </c:if>
                                                    <c:if test="${'1' eq record.status}">
                                                        <c:out value="审核通过"></c:out>
                                                    </c:if>
                                                    <c:if test="${'2' eq record.status}">
                                                        <c:out value="分账成功"></c:out>
                                                    </c:if>
                                                    <c:if test="${'3' eq record.status}">
                                                        <c:out value="分账失败"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center"><hyjf:datetime
                                                        value="${record.addTime}"></hyjf:datetime></td>
                                                <td class="center"><c:out value="${record.nid}"></c:out></td>
                                                <td class="center"><c:out value="${record.seqNo}"></c:out></td>
                                                <td class="center">
                                                    <div class="visible-md visible-lg hidden-sm hidden-xs">
                                                        <shiro:hasPermission name="poundage:AUDIT">
                                                            <c:if test="${'0' eq record.status}">
                                                                <a class="btn btn-transparent btn-xs fn-AUDIT"
                                                                   data-id="${record.id}" data-toggle="tooltip"
                                                                   data-placement="top" data-original-title="审核">审核</a>
                                                            </c:if>
                                                        </shiro:hasPermission>
                                                        <shiro:hasPermission name="poundage:ADD">
                                                            <c:if test="${'1' eq record.status}">
                                                                <a class="btn btn-transparent btn-xs fn-ADD"
                                                                   data-id="${record.id}" data-toggle="tooltip"
                                                                   data-placement="top" data-original-title="分账">分账</a>
                                                            </c:if>
                                                        </shiro:hasPermission>
                                                        <shiro:hasPermission name="poundagedetail:VIEW">
                                                            <a class="btn btn-transparent btn-xs fn-INFO"
                                                               data-id="${record.id}" data-toggle="tooltip"
                                                               href="${webRoot}/poundagedetail/searchAction?poundageId=${record.id}"
                                                               data-placement="top" data-original-title="详情">详情</a>
                                                        </shiro:hasPermission>
                                                        <shiro:hasPermission name="poundagedetail:EXPORT">
                                                            <a class="btn btn-transparent btn-xs tooltips fn-DOWNLOAD"
                                                               data-id="${record.id }" data-toggle="tooltip"
                                                               tooltip-placement="top" data-original-title="下载">下载</a>
                                                        </shiro:hasPermission>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <tr>
                                            <td class="center">总计</td>
                                            <td class="center"></td>
                                            <td class="center"></td>
                                            <td class="center"><c:out value="${poundageSum.amount}"></c:out></td>
                                            <td class="center"><c:out value="${poundageSum.quantity}"></c:out></td>
                                            <td class="center"></td>
                                            <td class="center"></td>
                                            <td class="center"></td>
                                            <td class="center"></td>
                                            <td class="center"></td>
                                            <td class="center"></td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                                <%-- 分页栏 --%>
                            <shiro:hasPermission name="poundage:SEARCH">
                                <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction"
                                                paginator="${poundageForm.paginator}"></hyjf:paginator>
                            </shiro:hasPermission>
                            <br/><br/>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>

        <tiles:putAttribute name="searchPanels" type="string">
            <shiro:hasPermission name="poundage:SEARCH">
                <input type="hidden" name="id" id="id"/>
                <input type="hidden" name="paginatorPage" id="paginator-page"
                       value="${poundageForm.paginatorPage == null ? 0 : poundageForm.paginatorPage}"/>
                <input type="hidden" name="fid" id="fid" value="${fid}"/>
                <div class="form-group">
                    <label>收款方用户名:</label>
                    <input type="text" name="userNameSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageForm.userNameSer}"/>
                </div>
                <div class="form-group">
                    <label>收款方姓名:</label>
                    <input type="text" name="realNameSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageForm.realNameSer}"/>
                </div>
                <div class="form-group">
                    <label>分账状态:</label>
                    <select name="statusSer" class="form-control underline form-select2">
                        <option disabled value
                                <c:if test="${empty poundageForm.statusSer}">selected="selected"</c:if>></option>
                        <option value="0"
                                <c:if test="${'0' eq poundageForm.statusSer}">selected="selected"</c:if>>
                            <c:out value="未审核"></c:out>
                        </option>
                        <option value="1"
                                <c:if test="${'1' eq poundageForm.statusSer}">selected="selected"</c:if>>
                            <c:out value="审核通过"></c:out>
                        </option>
                        <option value="2"
                                <c:if test="${'2' eq poundageForm.statusSer}">selected="selected"</c:if>>
                            <c:out value="分账成功"></c:out>
                        </option>
                        <option value="3"
                                <c:if test="${'3' eq poundageForm.statusSer}">selected="selected"</c:if>>
                            <c:out value="分账失败"></c:out>
                        </option>
                        </option>
                    </select>
                </div>
                <div class="form-group">
                    <label>分账订单号:</label>
                    <input type="text" name="nidSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageForm.nidSer}"/>
                </div>
                <div class="form-group">
                    <label>分账流水号:</label>
                    <input type="text" name="seqNoSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageForm.seqNoSer}"/>
                </div>
                <div class="form-group">
                    <label>分账时间段:</label>
                    <div class="input-group input-daterange datepicker">
                        <span class="input-icon">
                        <input type="text" name="poundageTimeStart" id="start-date-time2" class="form-control underline"
                               value="${poundageForm.poundageTimeStart}"/>
                        <i class="ti-calendar"></i>
                        </span>
                        <span class="input-group-addon no-border bg-light-orange">~</span>
                        <input type="text" name="poundageTimeEnd" id="end-date-time2" class="form-control underline"
                               value="${poundageForm.poundageTimeEnd}"/>
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <label>分账时间:</label>
                    <div class="input-group input-daterange datepicker">
                        <span class="input-icon">
                        <input type="text" name="addTimeStart" id="start-date-time" class="form-control underline"
                               value="${poundageForm.addTimeStart}"/>
                        <i class="ti-calendar"></i>
                        </span>
                        <span class="input-group-addon no-border bg-light-orange">~</span>
                        <input type="text" name="addTimeEnd" id="end-date-time" class="form-control underline"
                               value="${poundageForm.addTimeEnd}"/>
                        </span>
                    </div>
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
            <script type='text/javascript' src="${webRoot}/jsp/finance/poundage/poundageList.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>
