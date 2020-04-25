<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<nav class="nav-sub">
    <div class="container">
        <ul>
            <li id="userPandect"><a href="${ctx}/user/pandect/pandect.do" itemid="userPandect">账户总览</a></li>
            <c:if test="${cookie['roleId'].value != '1'}">
             <li id="loanManage"><a href="${ctx}/bank/web/user/repay/userRepayPage.do" itemid="loanManage">借款管理</a></li>
            </c:if>
            <li id="mytender"><a href="${ctx}/user/assetmanage/init.do" itemid="mytender">资产管理</a></li>
            <li id="userTrade"><a href="${ctx}/bank/user/trade/initTradeList.do" itemid="userTrade">交易明细</a></li>
            <li id="myReward"><a href="${ctx}/user/invite/toInvite.do" itemid="myReward" itemid="myReward">我的奖励</a></li>
            <li id="accountSet"><a href="${ctx}/user/safe/init.do" itemid="accountSet">账户设置</a></li>
        </ul>
    </div>
</nav>
