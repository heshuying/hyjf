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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="poundagedetail:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="手续费分账明细"/>

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
                            <shiro:hasPermission name="poundagedetail:SEARCH">
                                <%-- 功能栏 --%>
                                <div class="well">
                                    <c:set var="jspPrevDsb"
                                           value="${poundageDetailForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                    <c:set var="jspNextDsb"
                                           value="${poundageDetailForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i
                                            class="fa fa-chevron-left"></i></a>
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i
                                            class="fa fa-chevron-right"></i></a>
                                    <shiro:hasPermission name="poundagedetail:EXPORT">
                                        <a class="btn btn-o btn-primary btn-sm fn-Export" href="${webRoot}/poundage/detail/exportAction?id=${poundage.id}"
                                           data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表
                                            <i class="fa fa-plus"></i></a>
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
                                    <%--<th class="center">订单号</th>--%>
                                    <%--<th class="center">流水号</th>--%>
                                    <th class="center">项目编号</th>
                                    <th class="center">项目类型</th>
                                    <th class="center">放款/还款时间</th>
                                    <th class="center">出借人</th>
                                    <th class="center">出借人分公司</th>
                                    <th class="center">分账类型</th>
                                    <th class="center">分账来源</th>
                                    <th class="center">服务费分账比例</th>
                                    <th class="center">债转服务费分账比例</th>
                                    <th class="center">管理费分账比例</th>
                                    <th class="center">分账金额</th>
                                    <th class="center">收款方用户名</th>
                                    <th class="center">收款方姓名</th>
                                    <th class="center">收款方电子账号</th>
                                    <th class="center">分账状态</th>
                                    <th class="center">实际分账时间</th>
                                </tr>
                                </thead>
                                <tbody id="userTbody">
                                <c:choose>
                                    <c:when test="${empty poundageDetailForm.recordList}">
                                        <tr>
                                            <td colspan="18">暂时没有数据记录</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${poundageDetailForm.recordList }" var="record" begin="0"
                                                   step="1" varStatus="status">
                                            <tr>
                                                <td class="center"><c:out
                                                        value="${status.index+((poundageDetailForm.paginatorPage - 1) * poundageDetailForm.paginator.limit) + 1 }"></c:out></td>
                                                <td class="center"><c:out value="${record.borrowNid}"></c:out></td>
                                                <td class="center"><c:out value="${record.borrowType}"></c:out></td>
                                                <td class="center"><hyjf:datetime
                                                        value="${record.addtime}"></hyjf:datetime>
                                                </td>
                                                <td class="center"><c:out value="${record.usernname}"></c:out></td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty poundageLedger.investorCompany }">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${poundageLedger.investorCompany}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center">
                                                    <c:if test="${'1' eq poundageLedger.type}">
                                                        <c:out value="按出借人分账"></c:out>
                                                    </c:if>
                                                    <c:if test="${'2' eq poundageLedger.type}">
                                                        <c:out value="按借款人分账"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <c:if test="${'0' eq poundageLedger.source}">
                                                        <c:out value="全部"></c:out>
                                                    </c:if>
                                                    <c:if test="${'1' eq poundageLedger.source}">
                                                        <c:out value="服务费"></c:out>
                                                    </c:if>
                                                    <c:if test="${'3' eq poundageLedger.source}">
                                                        <c:out value="管理费"></c:out>
                                                    </c:if>
                                                    <c:if test="${'2' eq poundageLedger.source}">
                                                        <c:out value="债转服务费"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty poundageLedger.serviceRatio }">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:when test="${poundageLedger.serviceRatio == '0.00'}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${poundageLedger.serviceRatio}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty poundageLedger.creditRatio}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:when test="${poundageLedger.creditRatio == '0.00'}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${poundageLedger.creditRatio}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty poundageLedger.manageRatio}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:when test="${poundageLedger.manageRatio == '0.00' }">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${poundageLedger.manageRatio}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center"><c:out value="${record.amount}"></c:out></td>
                                                <td class="center"><c:out
                                                        value="${poundageLedger.username}"></c:out></td>
                                                <td class="center"><c:out
                                                        value="${poundageLedger.truename}"></c:out></td>
                                                <td class="center"><c:out
                                                        value="${poundageLedger.account}"></c:out></td>
                                                <td class="center">
                                                    <c:if test="${'0' eq poundage.status}">
                                                        <c:out value="未审核"></c:out>
                                                    </c:if>
                                                    <c:if test="${'1' eq poundage.status}">
                                                        <c:out value="审核通过"></c:out>
                                                    </c:if>
                                                    <c:if test="${'2' eq poundage.status}">
                                                        <c:out value="分账成功"></c:out>
                                                    </c:if>
                                                    <c:if test="${'3' eq poundage.status}">
                                                        <c:out value="分账失败"></c:out>
                                                    </c:if>
                                                    <c:if test="${'4' eq poundage.status}">
                                                        <c:out value="处理中"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center"><hyjf:datetime
                                                        value="${poundage.addTime}"></hyjf:datetime>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                                <%-- 分页栏 --%>
                            <shiro:hasPermission name="poundagedetail:SEARCH">
                                <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction"
                                                paginator="${poundageDetailForm.paginator}"></hyjf:paginator>
                            </shiro:hasPermission>
                            <br/><br/>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <shiro:hasPermission name="poundagedetail:SEARCH">
                <input type="hidden" name="id" id="id"/>
                <input type="hidden" name="paginatorPage" id="paginator-page"
                       value="${poundageDetailForm.paginatorPage == null ? 0 : poundageDetailForm.paginatorPage}"/>
                <input type="hidden" name="fid" id="fid" value="${fid}"/>
                <input type="hidden" name="poundageId" id="poundageId" value="${poundage.id}"/>
                <div class="form-group">
                    <label>项目编号:</label>
                    <input type="text" name="borrowNidSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageDetailForm.borrowNidSer}"/>
                </div>
                <div class="form-group">
                    <label>项目类型:</label>

                    <select name="borrowTypeSer" class="form-control underline form-select2">
                        <option disabled value
                                <c:if test="${empty poundageDetailForm.borrowTypeSer}">selected="selected"</c:if>></option>
                        <c:forEach items="${projects}" var="project" begin="0"
                                   step="1" varStatus="status">
                            <option value="${project.borrowName}"
                                <c:if test="${project.borrowName eq poundageDetailForm.borrowTypeSer}">selected="selected"</c:if>>
                                ${project.borrowName}
                            </option>
                        </c:forEach>
                        </option>
                    </select>
                </div>
                <%--<div class="form-group">--%>
                    <%--<label>出借人:</label>--%>
                    <%--<input type="text" name="usernnameSer" class="form-control input-sm underline" maxlength="20"--%>
                           <%--value="${poundageDetailForm.usernnameSer}"/>--%>
                <%--</div>--%>
                <div class="form-group">
                    <label>放款/还款时间:</label>
                    <div class="input-group input-daterange datepicker">
                        <span class="input-icon">
                        <input type="text" name="addTimeStart" id="start-date-time" class="form-control underline"
                               value="${poundageDetailForm.addTimeStart}"/>
                        <i class="ti-calendar"></i>
                        </span>
                        <span class="input-group-addon no-border bg-light-orange">~</span>
                        <input type="text" name="addTimeEnd" id="end-date-time" class="form-control underline"
                               value="${poundageDetailForm.addTimeEnd}"/>
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
            <script type='text/javascript' src="${webRoot}/jsp/finance/poundagedetail/poundageDetailList.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>
