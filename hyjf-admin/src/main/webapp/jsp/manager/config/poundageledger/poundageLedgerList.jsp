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

<shiro:hasPermission name="poundageledger:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="手续费分账配置"/>

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
                            <shiro:hasPermission name="poundageledger:SEARCH">
                                <%-- 功能栏 --%>
                                <div class="well">
                                    <c:set var="jspPrevDsb"
                                           value="${poundageLedgerForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                    <c:set var="jspNextDsb"
                                           value="${poundageLedgerForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i
                                            class="fa fa-chevron-left"></i></a>
                                    <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                       data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i
                                            class="fa fa-chevron-right"></i></a>
                                    <shiro:hasPermission name="poundageledger:MODIFY">
                                        <a class="btn btn-o btn-primary btn-sm fn-PoundageLedger"
                                           data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i
                                                class="fa fa-plus"></i></a>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="poundageledger:EXPORT">
                                        <a class="btn btn-o btn-primary btn-sm fn-Export"
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
                                    <th class="center">用户名</th>
                                    <th class="center">姓名</th>
                                    <th class="center">电子账号</th>
                                    <th class="center">分账类型</th>
                                    <th class="center">分账来源</th>
                                    <th class="center">服务费分账比例</th>
                                    <th class="center">债转服务费分账比例</th>
                                    <th class="center">管理费分账比例</th>
                                    <th class="center">出借人分公司</th>
                                    <th class="center">适用项目类型</th>
                                    <th class="center">状态</th>
                                    <th class="center">说明</th>
                                    <th class="center">操作</th>
                                </tr>
                                </thead>
                                <tbody id="userTbody">
                                <c:choose>
                                    <c:when test="${empty poundageLedgerForm.recordList}">
                                        <tr>
                                            <td colspan="15">暂时没有数据记录</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${poundageLedgerForm.recordList }" var="record" begin="0"
                                                   step="1" varStatus="status">
                                            <tr>
                                                <td class="center">${(poundageLedgerForm.paginatorPage-1)*poundageLedgerForm.paginator.limit+status.index+1 }</td>
                                                <td class="center"><c:out value="${record.username}"></c:out></td>
                                                <td class="center"><c:out value="${record.truename}"></c:out></td>
                                                <td class="center"><c:out value="${record.account}"></c:out></td>
                                                <td class="center">
                                                    <c:if test="${'1' eq record.type}">
                                                        <c:out value="按出借人分账"></c:out>
                                                    </c:if>
                                                    <c:if test="${'2' eq record.type}">
                                                        <c:out value="按借款人分账"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <c:if test="${'0' eq record.source}">
                                                        <c:out value="全部"></c:out>
                                                    </c:if>
                                                    <c:if test="${'1' eq record.source}">
                                                        <c:out value="服务费"></c:out>
                                                    </c:if>
                                                    <c:if test="${'3' eq record.source}">
                                                        <c:out value="管理费"></c:out>
                                                    </c:if>
                                                    <c:if test="${'2' eq record.source}">
                                                        <c:out value="债转服务费"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty record.serviceRatio }">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:when test="${record.serviceRatio == '0.00'}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${record.serviceRatio}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty record.creditRatio}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:when test="${record.creditRatio == '0.00'}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${record.creditRatio}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty record.manageRatio}">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:when test="${record.manageRatio == '0.00' }">
                                                            <c:out value="--"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${record.manageRatio}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${empty record.investorCompany }">
                                                            <c:out value="无"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:out value="${record.investorCompany}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </td>
                                                <td class="center">
                                                    <c:choose>
                                                        <c:when test="${'0' eq record.projectType }">
                                                            <c:out value="全部"></c:out>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="projectTypes" value=""/>
                                                            <c:forEach items="${projects}" var="project" begin="0"
                                                                       step="1" varStatus="status">
                                                                <c:set var="projectType"
                                                                       value=",${record.projectType},"/>
                                                                <c:set var="projectId" value=",${project.id},"/>
                                                                <c:if test="${fn:contains(projectType, projectId)}">
                                                                    <c:choose>
                                                                        <c:when test="${'' eq projectTypes}">
                                                                            <c:set var="projectTypes"
                                                                                   value="${project.borrowName}"/>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:set var="projectTypes"
                                                                                   value="${projectTypes}，${project.borrowName}"/>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:if>
                                                            </c:forEach>
                                                            <c:out value="${projectTypes}"></c:out>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="center">
                                                    <c:if test="${'0' eq record.status}">
                                                        <c:out value="启用"></c:out>
                                                    </c:if>
                                                    <c:if test="${'1' eq record.status}">
                                                        <c:out value="禁用"></c:out>
                                                    </c:if>
                                                </td>
                                                <td class="center"><c:out value="${record.explan}"></c:out></td>
                                                <td class="center">
                                                    <div class="visible-md visible-lg hidden-sm hidden-xs">
                                                        <shiro:hasPermission name="poundageledger:MODIFY">
                                                            <a class="btn btn-transparent btn-xs fn-Modify"
                                                               data-id="${record.id}" data-toggle="tooltip"
                                                               data-placement="top" data-original-title="修改"><i
                                                                    class="fa fa-pencil"></i></a>
                                                        </shiro:hasPermission>
                                                        <shiro:hasPermission name="poundageledger:DELETE">
                                                            <a class="btn btn-transparent btn-xs tooltips fn-Delete"
                                                               data-id="${record.id }" data-toggle="tooltip"
                                                               tooltip-placement="top" data-original-title="删除"><i
                                                                    class="fa fa-times fa fa-white"></i></a>
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
                            <shiro:hasPermission name="poundageledger:SEARCH">
                                <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction"
                                                paginator="${poundageLedgerForm.paginator}"></hyjf:paginator>
                            </shiro:hasPermission>
                            <br/><br/>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <shiro:hasPermission name="poundageledger:SEARCH">
                <input type="hidden" name="id" id="id"/>
                <input type="hidden" name="paginatorPage" id="paginator-page"
                       value="${poundageLedgerForm.paginatorPage == null ? 0 : poundageLedgerForm.paginatorPage}"/>
                <input type="hidden" name="fid" id="fid" value="${fid}"/>
                <div class="form-group">
                    <label>用户名:</label>
                    <input type="text" name="usernameSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageLedgerForm.usernameSer}"/>
                </div>
                <div class="form-group">
                    <label>姓名:</label>
                    <input type="text" name="truenameSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageLedgerForm.truenameSer}"/>
                </div>
                <div class="form-group">
                    <label>电子账号:</label>
                    <input type="text" name="accountSer" class="form-control input-sm underline" maxlength="20"
                           value="${poundageLedgerForm.accountSer}"/>
                </div>
                <div class="form-group">
                    <label>分账类型:</label>
                    <select name="typeSer" class="form-control underline form-select2">
                        <option value="">全部</option>
                        <option value="1"
                                <c:if test="${'1' eq poundageLedgerForm.typeSer}">selected="selected"</c:if>>
                            <c:out value="按出借人分账"></c:out>
                        </option>
                        <option value="2"
                                <c:if test="${'2' eq poundageLedgerForm.typeSer}">selected="selected"</c:if>>
                            <c:out value="按借款人分账"></c:out>
                        </option>
                    </select>
                </div>
                <div class="form-group">
                    <label>分账来源:</label>
                    <select name="sourceSer" class="form-control underline form-select2">
                        <option disabled value
                                <c:if test="${empty poundageLedgerForm.sourceSer}">selected="selected"</c:if>></option>
                        <option value="0"
                                <c:if test="${'0' eq poundageLedgerForm.sourceSer}">selected="selected"</c:if>>
                            <c:out value="全部(服务/债转/管理)"></c:out>
                        </option>
                        <option value="1"
                                <c:if test="${'1' eq poundageLedgerForm.sourceSer}">selected="selected"</c:if>>
                            <c:out value="服务费"></c:out>
                        </option>
                        <option value="2"
                                <c:if test="${'2' eq poundageLedgerForm.sourceSer}">selected="selected"</c:if>>
                            <c:out value="债转服务费"></c:out>
                        </option>
                        <option value="3"
                                <c:if test="${'3' eq poundageLedgerForm.sourceSer}">selected="selected"</c:if>>
                            <c:out value="管理费"></c:out>
                        </option>
                    </select>
                </div>
                <div class="form-group">
                    <label>状态:</label>
                    <select name="statusSer" class="form-control underline form-select2">
                        <option value="">全部</option>
                        <option value="0"
                                <c:if test="${'0' eq poundageLedgerForm.statusSer}">selected="selected"</c:if>>
                            <c:out value="启用"></c:out>
                        </option>
                        <option value="1"
                                <c:if test="${'1' eq poundageLedgerForm.statusSer}">selected="selected"</c:if>>
                            <c:out value="禁用"></c:out>
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
            <script type='text/javascript'
                    src="${webRoot}/jsp/manager/config/poundageledger/poundageLedgerList.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>
