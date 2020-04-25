<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="calist:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="CA认证记录" />

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">CA认证记录</h1>
            <span class="mainDescription">CA认证记录</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <div class="tab-content">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="search-classic">
                                        <%-- 功能栏 --%>
                                    <div class="well">
                                        <c:set var="jspPrevDsb" value="${CertificateAuthorityFrom.paginator.firstPage ? ' disabled' : ''}"></c:set>
                                        <c:set var="jspNextDsb" value="${CertificateAuthorityFrom.paginator.lastPage ? ' disabled' : ''}"></c:set>
                                        <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
                                           data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
                                        <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
                                           data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
                                        <shiro:hasPermission name="calist:EXPORT">
                                            <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
                                               data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
                                        </shiro:hasPermission>
                                        <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
                                        <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
                                           data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel">
                                            <i class="fa fa-search margin-right-10"></i>
                                            <i class="fa fa-chevron-left"></i></a>
                                    </div>
                                    <br />
                                        <%-- 模板列表一览 --%>
                                    <table id="equiList"
                                           class="table table-striped table-bordered table-hover">
                                        <colgroup>
                                            <col style="width: 55px;" />
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th class="center">序号</th>
                                            <th class="center">用户名</th>
                                            <th class="center">ca认证手机号</th>
                                            <th class="center">姓名/名称</th>
                                            <th class="center">证件号码</th>
                                            <th class="center">用户类型</th>
                                            <th class="center">邮箱</th>
                                            <th class="center">客户编号</th>
                                            <th class="center">状态</th>
                                            <th class="center">申请时间</th>
                                            <th class="center">备注</th>
                                        </tr>
                                        </thead>
                                        <tbody id="roleTbody">
                                        <c:choose>
                                            <c:when test="${empty recordList}">
                                                <tr>
                                                    <td colspan="12">暂时没有数据记录</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${recordList }" var="record" begin="0"
                                                           step="1" varStatus="status">
                                                    <tr>
                                                        <td class="center"><c:out
                                                                value="${status.index+((CertificateAuthorityFrom.paginatorPage - 1) * CertificateAuthorityFrom.paginator.limit) + 1 }"></c:out></td>
                                                        <td><c:out value="${record.userName }"></c:out></td>
                                                        <td><c:out value="${record.mobile }"></c:out></td>
                                                        <td><c:out value="${record.trueName }"></c:out></td>
                                                        <td><c:out value="${record.idNo }"></c:out></td>
                                                        <td><c:out value="${record.idType eq 0?'个人':'企业' }"></c:out></td>
                                                        <td><c:out value="${record.email}"></c:out></td>
                                                        <td><c:out value="${record.customerId }"></c:out></td>
                                                        <td><c:out value="${record.code eq 1000?'认证成功':'未认证或认证失败' }"></c:out></td>
                                                        <td class="center"><hyjf:datetime value="${record.createTime }"></hyjf:datetime></td>
                                                        <td><c:out value="${record.remark }"></c:out></td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                        <%-- 分页栏 --%>
                                    <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${CertificateAuthorityFrom.paginator}"></hyjf:paginator>
                                    <br /> <br />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
            <input type="hidden" name="userId" id="userId" />
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${CertificateAuthorityFrom.paginatorPage}" />
            <div class="form-group">
                <label>用户名:</label>
                <input type="text" name="userNameSrch" id="userNameSrch" class="form-control input-sm underline" value="${CertificateAuthorityFrom.userNameSrch}" />
            </div>
            <div class="form-group">
                <label>手机号:</label>
                <input type="text" name="mobileSrch" id="mobileSrch" class="form-control input-sm underline" value="${CertificateAuthorityFrom.mobileSrch}" />
            </div>
            <div class="form-group">
                <label>姓名:</label>
                <input type="text" name="trueNameSrch" id="trueNameSrch" class="form-control input-sm underline" value="${CertificateAuthorityFrom.trueNameSrch}" />
            </div>

            <div class="form-group">
            <label>用户类型:</label>
            <select name="idTypeSrch"  class="form-control underline form-select2">
            <option value="" selected="selected"></option>
            <option value="0"
            <c:if test="${CertificateAuthorityFrom.idTypeSrch eq '0'}">selected="selected"</c:if>>个人</option>
            <option value="1"
            <c:if test="${CertificateAuthorityFrom.idTypeSrch eq '1'}">selected="selected"</c:if>>企业</option>
            </select>
            </div>

            <div class="form-group">
                <label>客户编号:</label>
                <input type="text" name="customerIdSrch" id="customerIdSrch" class="form-control input-sm underline" value="${CertificateAuthorityFrom.customerIdSrch}" maxlength="32"/>
            </div>

            <div class="form-group">
                <label>申请时间</label>
                <div class="input-group datepicker">
					<span class="input-icon">
						<input type="text" name="startTimeSrch" id="start-date-time" class="form-control underline" value="${CertificateAuthorityFrom.startTimeSrch}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="endTimeSrch" id="end-date-time" class="form-control underline" value="${CertificateAuthorityFrom.endTimeSrch}" />
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel"
                    name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
                  rel="stylesheet" media="screen">
        </tiles:putAttribute>

        <%-- JS全局变量定义、插件 (ignore) --%>
        <tiles:putAttribute name="pageGlobalImport" type="string">
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
        </tiles:putAttribute>

        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type='text/javascript' src="${webRoot}/jsp/manager/users/certificateauthority/certificateauthoritylist.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>
