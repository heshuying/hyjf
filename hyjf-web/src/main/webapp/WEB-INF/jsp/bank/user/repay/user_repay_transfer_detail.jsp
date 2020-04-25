<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
    <jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/header.jsp"></jsp:include>
<jsp:include page="/subMenu.jsp"></jsp:include>
<section class="breadcrumbs">
    <div class="container">
        <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt; <a href="${ctx}/user/pandect/pandect.do">账户中心</a> &gt; <a href="${ctx}/bank/web/user/repay/userRepayPage.do">借款管理</a> &gt; 借款详情</div>
    </div>
</section>
<article class="main-content product-zhitou" style="padding-top: 0;">
    <div class="container">
        <!-- start 内容区域 -->
        <div class="loan-paymentdetails">
            <div class="paymentdetails-top">
                <div class="list-fl">
                    <div class="list" style="height:210px">
                        <div class="title">
                            <input type="hidden" name="verificationFlag" id="verificationFlag" value="${verificationFlag}" />
                            <input type="hidden" name="borrowNid" id="borrowNid" value="${borrowInfo.borrowNid}" />
                            <p class="fn-left">${borrowInfo.borrowNid }</p>
                            <c:if test="${borrowInfo.projectType  != '13' && fddStatus == 1}">
                                <a class='fn-right value' href="${ctx}/bank/web/user/repay/downloadBorrowerPdf.do?borrowNid=${borrowInfo.borrowNid}">下载协议</a>
                            </c:if>
                            <ul class="ui-list-title">
                                <li style="white-space:nowrap">
                                    <span class="basic-label">项目期限：</span>
                                    <span class="basic-value" >${borrowInfo.borrowPeriod }个月</span>
                                </li>
                                <li style="white-space: nowrap;">
                                    <span class="basic-label" style="white-space: nowrap;">还款方式：</span>
                                    <span class="basic-value">${borrowInfo.borrowStyle}</span>
                                </li>
                                <li>
                                </li>
                                <li>
                                    <span class="basic-label">借款金额：</span>
                                    <span class="basic-value"><c:if test="${borrowInfo.account != null && borrowInfo.account != ''}"><fmt:formatNumber value="${borrowInfo.account}" pattern="#,###.00" />元</c:if></span>
                                </li>
                                <li>
                                    <span class="basic-label">到账金额：</span>
                                    <span class="basic-value"><c:if test="${borrowInfo.sucSmount != null && borrowInfo.sucSmount != ''}"><fmt:formatNumber value="${borrowInfo.sucSmount}" pattern="#,###.00" />元</c:if></span>
                                </li>
                                <li style="white-space: nowrap;">
                                    <span class="basic-label">&nbsp;&nbsp;历史年回报率：</span>
                                    <span class="basic-value">
                                        <span class="unit">${borrowInfo.borrowApr}%</span>
                                    </span>
                                </li>

                                <li>
                                    <span class="basic-label">开标时间：</span>
                                    <span class="basic-value">${borrowInfo.sendTime}</span>
                                </li>
                                <li>
                                    <span class="basic-label">满标时间：</span>
                                    <span class="basic-value">${borrowInfo.borrowFullTime}</span>
                                </li>
                                <li>
                                    <span class="basic-label"><font style="color: red"> 放款时间</font>：</span>
                                    <span class="basic-value">${borrowInfo.recoverLastTime}</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="list-fr">
                    <c:if test="${borrowInfo.borrowStatus eq 14}">
                        <span class="yhk"></span>
                    </c:if>
                    <!-- 还款中  -->
                    <c:if test="${borrowInfo.borrowStatus eq 13}">
                        <span class="hkz"></span>
                    </c:if>
                    <!-- 复审中  -->
                    <c:if test="${borrowInfo.borrowStatus eq 12}">
                        <span class="fsz"></span>
                    </c:if>
                    <!-- 复审中  -->
                    <c:if test="${borrowInfo.borrowStatus eq 15}">
                        <span class="ylb"></span>
                    </c:if>
                </div>
            </div>

            <div class="main">
                <div class="title">出借人转让记录</div>
                <table class="table">
                    <thead>
                    <th class="ui-list-title pl1" style="width: 24%;">承接人</th>
                    <th class="ui-list-title pl2" style="width: 20%;">转让人</th>
                    <th class="ui-list-title pl3" style="width: 20%;">承接金额</th>
                    <th class="ui-list-title pl4" style="width: 20%;">时间</th>
                    </thead>

                    <tbody id="projectList">
                    <tr>
                        <td colspan="6">
                            <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div class="pages-nav" id="new-pagination"></div>
            </div>
        </div>
        <!-- end 内容区域 -->
    </div>
</article>
<jsp:include page="/footer.jsp"></jsp:include>
<script src="${cdn}/dist/js/loan/user-repay-transfer.js"></script>
<script>setActById("loanManage");</script>

</body>
</html>