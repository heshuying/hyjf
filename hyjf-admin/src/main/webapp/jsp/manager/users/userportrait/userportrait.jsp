<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 2018/5/11
  Time: 9:59
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
<%--<c:set value="${fn:split('汇盈金服,注册记录', ',')}" var="functionPaths" scope="request"></c:set>--%>

<shiro:hasPermission name="userPortrait:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="用户画像" />

        <%-- 画面主面板标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">用户画像</h1>
            <span class="mainDescription">本功能可以修改查询相应的用户画像信息。</span>
        </tiles:putAttribute>

        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
            <div class="tabbable">
            <ul class="nav nav-tabs" id="myTab">
                <shiro:hasPermission name="userPortrait:SEARCH">
                    <li class="active"><a href="${webRoot}/manager/userPortrait/userPortrait">用户画像信息</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="userPortrait:SEARCH">
                    <li><a href="${webRoot}/manager/userPortrait/userPortraitScore">用户画像评分</a></li>
                </shiro:hasPermission>
            </ul>
            <div class="tab-content">
            <div class="tab-pane fade in active">
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
            <th class="center">用户名</th>
            <th class="center">手机号</th>
            <th class="center">年龄</th>
            <th class="center">性别</th>
            <th class="center">学历</th>
            <th class="center">职业</th>
            <th class="center">地域</th>
            <th class="center">爱好</th>
            <th class="center">账户总资产(元)</th>
            <th class="center">账户可用金额(元)</th>
            <th class="center">账户待还金额(元)</th>
            <th class="center">账户冻结金额(元)</th>
            <th class="center">资金存留比(%)</th>
            <th class="center">客均收益率(%)</th>
            <th class="center">累计收益(元)</th>
            <th class="center">累计年化出借金额(元)</th>
            <th class="center">累计充值金额(元)</th>
            <th class="center">累计提取金额(元)</th>
            <th class="center">登录活跃</th>
            <th class="center">客户来源</th>
            <th class="center">最后一次登录至今时长(天)</th>
            <th class="center">最后一次充值至今时长(天)</th>
            <th class="center">最后一次提现至今时长(天)</th>
            <th class="center">最后一笔回款时间</th>
            <th class="center">同时出借平台数</th>
            <th class="center">投龄</th>
            <th class="center">交易笔数</th>
            <th class="center">当前拥有人</th>
            <th class="center">是否加微信 </th>
            <th class="center">出借进程</th>
            <th class="center">客户投诉</th>
            <th class="center">邀约客户数</th>
            <th class="center">邀约注册客户数</th>
            <th class="center">邀约充值客户数</th>
            <th class="center">邀约出借客户数</th>
            <th class="center">是否有主单</th>
                <th class="center">注册时间</th>
            <th class="center">操作</th>
            </tr>
            </thead>
            <tbody id="userTbody">
            <c:choose>
                <c:when test="${empty obj.recordlist}">
                    <tr><td colspan="38">暂时没有数据记录</td></tr>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${obj.recordlist }" var="record" begin="0" step="1" varStatus="status">
                        <tr>
                            <%--<td style="display: none" class="center">${(obj.paginatorPage-1)*obj.paginator.limit+status.index+1 }</td>--%>
                            <td style="display: none" id="userId"><c:out value="${record.userId}"></c:out></td>
                            <td class="center"><c:out value="${record.userName }"></c:out></td>
                            <td class="center"><c:out value="${record.mobile }"></c:out></td>
                            <td class="center"><c:out value="${record.age }"></c:out></td>
                            <td class="center"><c:out value="${record.sex }"></c:out></td>
                            <td class="center"><c:out value="${record.education }"></c:out>
                            <td class="center"><c:out value="${record.occupation}"></c:out></td>
                            <td class="center"><c:out value="${record.city }"></c:out></td>
                            <td class="center"><c:out value="${record.interest }"></c:out></td>
                            <td class="center"><c:out value="${record.bankTotal }"></c:out></td>
                            <td class="center"><c:out value="${record.bankBalance }"></c:out></td>
                            <td class="center"><c:out value="${record.accountAwait }"></c:out></td>
                            <td class="center"><c:out value="${record.bankFrost }"></c:out></td>
                            <td class="center"><c:out value="${record.fundRetention }"></c:out></td>
                            <td class="center"><c:out value="${record.yield }"></c:out></td>
                            <td class="center"><c:out value="${record.interestSum }"></c:out></td>
                            <td class="center"><c:out value="${record.investSum }"></c:out></td>
                            <td class="center"><c:out value="${record.rechargeSum }"></c:out></td>
                            <td class="center"><c:out value="${record.withdrawSum }"></c:out>
                            <td class="center"><c:out value="${record.loginActive}"></c:out></td>
                            <td class="center"><c:out value="${record.customerSource }"></c:out></td>
                            <td class="center"><c:out value="${record.lastLoginTime }"></c:out></td>
                            <td class="center"><c:out value="${record.lastRechargeTime }"></c:out></td>
                            <td class="center"><c:out value="${record.lastWithdrawTime }"></c:out></td>
                            <td class="center"><c:out value="${record.lastRepayTime}" ></c:out> </td>
                            <td class="center"><c:out value="${record.investPlatform }"></c:out></td>
                            <td class="center"><c:out value="${record.investAge }"></c:out></td>
                            <td class="center"><c:out value="${record.tradeNumber }"></c:out></td>
                            <td class="center"><c:out value="${record.currentOwner }"></c:out>
                            <td class="center"><c:out value="${record.addWechat}"></c:out></td>
                            <td class="center"><c:out value="${record.investProcess }"></c:out></td>
                            <td class="center"><c:out value="${record.customerComplaint }"></c:out></td>
                            <td class="center"><c:out value="${record.inviteCustomer }"></c:out></td>
                            <td class="center"><c:out value="${record.inviteRegist }"></c:out></td>
                            <td class="center"><c:out value="${record.inviteRecharge }"></c:out></td>
                            <td class="center"><c:out value="${record.inviteTender }"></c:out></td>
                            <td class="center"><c:out value="${record.attribute }"></c:out></td>
                            <td class="center"><c:out value="${record.regTime }"></c:out></td>
                            <td class="center">

                                    <div class="visible-md visible-lg hidden-sm hidden-xs">
                                        <%--<shiro:hasPermission name="userportrait:MODIFY">--%>
                                            <a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.userId }"
                                               data-toggle="tooltip" data-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
                                        <%--</shiro:hasPermission>--%>
                                    </div>
                                    <div class="visible-xs visible-sm hidden-md hidden-lg">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
                                                <i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu pull-right dropdown-light" role="menu">
                                                <%--<shiro:hasPermission name="userportrait:MODIFY">--%>
                                                    <li>
                                                        <a class="fn-Modify" data-id="${record.userId }">修改</a>
                                                    </li>
                                                <%--</shiro:hasPermission>--%>
                                            </ul>
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
                <hyjf:paginator id="equiPaginator" hidden="paginator-page" action="userPortrait" paginator="${obj.paginator}"></hyjf:paginator>
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
            <input type="hidden" name="userId" id="userId" value= "${obj.userId}"/>
            <input type="hidden" name="paginatorPage" id="paginator-page" value="${obj.paginatorPage}" />
            <!-- 检索条件 -->
            <div class="form-group">
            <label>用户名</label>
            <input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${obj.userName}" />
            </div>
            <div class="form-group">
                <label>手机号</label>
                <input type="text" name="mobile" class="form-control input-sm underline" value="${obj.mobile}" />
            </div>
            <div class="form-group">
                <label>性别</label>
                <select name="sex" id="sex" class="form-control underline form-select2">
                    <option value="">全部</option>
                        <option value="男" <c:if test="${obj.sex eq '男'}">selected="selected"</c:if>>
                            <c:out value="男"></c:out>
                        </option>
                        <option value="女" <c:if test="${obj.sex eq '女'}">selected="selected"</c:if>>
                            <c:out value="女"></c:out>
                        </option>
                </select>
            </div>
            <div class="form-group">
                <label>年龄</label>
                <div class="input-group datepicker">
					<input type="text" name="ageStart" id="ageStart" class="form-control underline" value="${obj.ageStart}" />
					<span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="ageEnd" id="ageEnd" class="form-control underline" value="${obj.ageEnd}" />
                </div>
            </div>
            <div class="form-group">
                <label>财户总资产</label>
                <div class="input-group datepicker">
					<input type="text" name="bankTotalStart" id="bankTotalStart" class="form-control underline" value="${obj.bankTotalStart}" />
					<span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="bankTotalEnd" id="bankTotalEnd" class="form-control underline" value="${obj.bankTotalEnd}" />
                </div>
            </div>
            <div class="form-group">
                <label>当前拥有人</label>
                <input type="text" name="currentOwner" class="form-control input-sm underline" value="${obj.currentOwner}" />
            </div>
            <div class="form-group">
                <label>累计收益</label>
                <div class="input-group datepicker">
					<input type="text" name="interestSumStart" id="interestSumStart" class="form-control underline" value="${obj.interestSumStart}" />
                    <span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="interestSumEnd" id="interestSumEnd" class="form-control underline" value="${obj.interestSumEnd}" />
                </div>
            </div>
            <div class="form-group">
                <label>交易笔数</label>
                <div class="input-group datepicker">
					<input type="text" name="tradeNumberStart" id="tradeNumberStart" class="form-control underline" value="${obj.tradeNumberStart}" />
					<span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="tradeNumberEnd" id="tradeNumberEnd" class="form-control underline" value="${obj.tradeNumberEnd}" />
                </div>
            </div>
            <div class="form-group">
                <label>是否有主单</label>
                <select name="attribute" id="attribute" class="form-control underline form-select2">
                    <option value="">全部</option>
                            <option value="0"
                                    <c:if test="${obj.attribute == 0}">selected="selected"</c:if>>
                                <c:out value="无主单"></c:out>
                            </option>
                            <option value="1"
                                    <c:if test="${obj.attribute == 1}">selected="selected"</c:if>>
                                 <c:out value="有主单"></c:out>
                            </option>
                            <option value="2"
                                    <c:if test="${obj.attribute == 2}">selected="selected"</c:if>>
                                  <c:out value="线下员工"></c:out>
                            </option>
                            <option value="3"
                                    <c:if test="${obj.attribute == 3}">selected="selected"</c:if>>
                                  <c:out value="线上员工"></c:out>
                            </option>
                </select>
            </div>
            <div class="form-group">
                <label>出借进程</label>
                <select name="investProcess" id="investProcess" class="form-control underline form-select2">
                    <option value=""></option>
                    <option value="注册"
                            <c:if test="${obj.investProcess eq '注册'}">selected="selected"</c:if>>
                        <c:out value="注册 "></c:out>
                    </option>
                    <option value="开户"
                            <c:if test="${obj.investProcess eq '开户'}">selected="selected"</c:if>>
                        <c:out value="开户 "></c:out>
                    </option>
                    <option value="绑卡"
                            <c:if test="${obj.investProcess eq '绑卡'}">selected="selected"</c:if>>
                        <c:out value="绑卡 "></c:out>
                    </option>
                    <option value="出借"
                            <c:if test="${obj.investProcess eq '出借'}">selected="selected"</c:if>>
                        <c:out value="出借 "></c:out>
                    </option>
                </select>
            </div>
            <div class="form-group">
                <label>注册时间</label>
                <div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
                                                     name="regTimeStart" id="start-date-time" class="form-control underline" value="${obj.regTimeStart}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="regTimeEnd" id="end-date-time" class="form-control underline" value="${obj.regTimeEnd}" />
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
            <script type='text/javascript' src="${webRoot}/jsp/manager/users/userportrait/userportrait.js"></script>
        </tiles:putAttribute>
    </tiles:insertTemplate>
</shiro:hasPermission>