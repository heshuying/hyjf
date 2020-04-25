<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
    <jsp:include page="/head.jsp"></jsp:include>
    <link rel="stylesheet" href="${cdn}/dist/css/lib/bootstrap-datepicker3.standalone.css" />
    <style>
        .loan-guarantors .main-tab .loan-div .loan-div-top .loan-divleft{
            width: 250px;
        }
        .loan-guarantors .main-tab .loan-div .loan-div-top .loan-divright{
            margin-left: 30px;
        }
    </style>
</head>

<body>
<jsp:include page="/header.jsp"></jsp:include>
<jsp:include page="/subMenu.jsp"></jsp:include>
<article class="main-content" style="padding-top: 0;">
    <input id="status" name="status" type="hidden" value=""></input>
    <input id="borrowNid" name="borrowNid" type="hidden" value=""></input>
    <input id="startDate" name="startDate" type="hidden" value=""></input>
    <input id="endDate" name="endDate" type="hidden" value=""></input>
    <input id="repayOrder" name="repayOrder" type="hidden" value=""></input>
    <input id="checkTimeOrder" name="checkTimeOrder" type="hidden" value=""></input>
    <input id="roleId" name="roleId" type="hidden" value="${roleId }"></input>
    <input id="userId" name="userId" type="hidden" value="${userId }"></input>
    <input id="showFlag" name="showFlag" type="hidden" value="${showFlag}"></input>
    <div class="container">
        <!-- start 内容区域 -->
        <div class="loan-guarantors">
            <div class="loan-top">
                <div class="loan-item">
                    <div class="loan-title">
                        <p class="loan-line"></p>
                        <p class="loan-mid">
                            <c:if test="${roleId eq 2 }">待还款总额（元）</c:if>
                            <c:if test="${roleId eq 3 }">待垫付总额（元）</c:if>
                        </p>
                        <p class="loan-line"></p>
                    </div>
                    <div class="loan-main">
                        <c:set value="${ fn:split(repayMoney, '.') }" var="repayMoneyStr" />
                        <span class="lg">${repayMoneyStr[0]}</span>
                        <span class="sm">.${repayMoneyStr[1]}</span>

                    </div>
                </div>
                <div class="loan-item-line2"></div>
                <div class="loan-item">
                </div>
            </div>
            <div class="main-tab">
                <ul class="tab-tags">
                    <li class="active" id="waitTitle" panel="0">
                        <a href="javascript:void(0);">
                            <c:if test="${roleId eq 2 }">待还款</c:if>
                            <c:if test="${roleId eq 3 }">待垫付</c:if>
                        </a>
                    </li>
                    <li panel="1" id="alerdyTitle">
                        <a href="javascript:void(0);">
                            <c:if test="${roleId eq 2 }">已还款</c:if>
                            <c:if test="${roleId eq 3 }">已垫付</c:if>
                        </a>
                    </li>
                    <c:if test="${roleId eq 2 }">
                        <li panel="2"><a href="javascript:void(0);">待授权</a></li>
                        <li panel="3"><a href="javascript:void(0);">已授权</a></li>
                    </c:if>
                </ul>
                <ul class="tab-panels">
                    <!-- 待还款 -->
                    <li id="waitRepay" class="active" panel="0">
                        <div class="loan-div">
                            <!-- 查询 -->
                            <div class="loan-div-top">
                                <div class="loan-divleft">
                                    <label>项目编号</label>
                                    <input type="text" class="text" name="text" id="repayBorrowNidSrch" placeholder="项目编号">
                                </div>
                                <div class="loan-divright">
                                    <label>应还时间</label>
                                    <input type="text" class="date start" id="repayStartDateSrch" placeholder="开始日期">
                                    <label>至</label>
                                    <input type="text" class="date end" id="repayEndDateSrch" placeholder="结束日期">
                                    <span class="btn sm" onclick="getRepayListById()">搜索</span>
                                    <c:if test="${roleId eq 3 }"><a class="btn-batch batch-repay-advance">批量还款垫付</a></c:if>
                                </div>
                            </div>
                            <c:if test="${roleId eq 3 }">
                                <div class="loan-div-top" style="padding-top:0;padding-bottom:0;height:45px;">
                                    <div class="loan-divleft">
                                        <label>本期应还总额:</label>
                                        <span style="color:red;" id="repayMoneyTotal"></span>&nbsp元
                                    </div>
                                    <div class="loan-divright">
                                        <label>应还笔数:</label>
                                        <span style="color:red;" id="repayMoneyNum"></span>&nbsp笔
                                    </div>
                                </div>
                            </c:if>
                            <!-- ajax列表 -->
                            <div class="loan-div-container">
                                <table class="loan-table">
                                    <thead>
                                    <tr class="loan-div-title ui-list-title">
                                        <th class="item1">
                                            <a href="javascript:void(0)"   data-val="1">应还时间<i class="icon iconfont icon-jiantou"></i></a>
                                        </th>
                                        <th class="item2">项目编号</th>
                                        <c:if test="${roleId eq 3 }">
                                            <th class="item3">期数</th>
                                        </c:if>
                                        <th class="item4">历史年回报率</th>
                                        <th class="item5">借款金额</th>
                                        <c:if test="${roleId eq 2 }">
                                            <th class="item6">到账金额</th>
                                            <th class="item6">
                                                <a href="javascript:void(0)"  data-val="2">到账时间<i class="icon iconfont icon-jiantou"></i></a>
                                            </th>
                                        </c:if>
                                        <th class="item6">应还总额</th>
                                        <%--<th class="item6">当前还款期数</th>--%>
                                        <th class="item6">本期应还总额</th>
                                        <th class="item7">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="repayList">
                                    <tr>
                                        <td colspan="8">
                                            <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div id="repayListEmptyStyle"></div>
                            </div>
                            <div class="pages-nav" id="repay-pagination"></div>
                        </div>
                    </li>

                    <!-- 已还款 -->
                    <li id="alerdyRepay" panel="1">
                        <div class="loan-div">
                            <div class="loan-div-top">
                                <div class="loan-divleft">
                                    <label>项目编号</label>
                                    <input type="text" class="text" name="paidBorrowNidSrch" id="paidBorrowNidSrch" placeholder="项目编号">
                                </div>
                                <div class="loan-divright">
                                    <label>应还时间</label>
                                    <input type="text" class="date start" id="paidStartDateSrch" name="paidStartDateSrch" placeholder="开始日期">
                                    <label>至</label>
                                    <input type="text" class="date end" id="paidEndDateSrch" name="paidEndDateSrch" placeholder="结束日期">
                                    <c:if test="${roleId eq 2 }">
                                        <span class="btn sm" onclick="getPaidListById()">搜索</span>
                                    </c:if>
                                    <c:if test="${roleId eq 3 }">
                                        <span class="btn sm" onclick="getOrgRepayListById()">搜索</span>
                                        <a class="btn-batch batch-repay-advance">批量还款垫付</a>
                                    </c:if>

                                </div>
                            </div>

                            <!-- ajax列表 -->
                            <div class="loan-div-container">
                                <c:if test="${roleId eq 2 }">
                                    <table class="loan-table">
                                        <thead>
                                        <tr class="loan-div-title">
                                            <th class="bor1">应还时间</th>
                                            <th class="bor2">项目编号</th>
                                            <th class="bor3">历史年回报率</th>
                                            <th class="bor4">已还总额</th>
                                            <th class="bor5">实还时间</th>
                                            <th class="bor8">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody id="paidList">
                                        <tr>
                                            <td colspan="8">
                                                <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </c:if>
                                <c:if test="${roleId eq 3 }">
                                    <table class="loan-table">
                                        <thead>
                                        <tr class="loan-div-title">
                                            <th class="bor1">应还时间</th>
                                            <th class="bor2">项目编号</th>
                                            <th class="bor3">期数</th>
                                            <th class="bor4">历史年回报率</th>
                                            <th class="bor5">垫付总额</th>
                                            <th class="bor6">垫付时间</th>
                                            <th class="bor7">还款来源</th>
                                            <th class="bor8">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody id="repayOrgList">
                                        <tr>
                                            <td colspan="8">
                                                <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </c:if>
                                <div id="paidListEmptyStyle"></div>
                                <div class="pages-nav" id="paid-pagination"></div>
                            </div>
                        </div>
                    </li>
                    <!-- 待授权 -->
                    <c:if test="${roleId eq 2 }">
                        <li class="" panel="2">
                            <div class="loan-div">
                                <div class="loan-div-top">
                                    <div class="loan-divleft">
                                        <label>项目编号</label>
                                        <input id="auth_borrowNid" type="text" class="text" name="text" placeholder="项目编号">
                                    </div>
                                    <div class="loan-divright">
                                        <label>添加时间</label>
                                        <input type="text" class="date start" id="auth_startDate" placeholder="开始时间">
                                        <label>至</label>
                                        <input type="text" class="date end" id="auth_endDate" placeholder="结束时间">
                                        <span onclick="getBorrowAuthListPage()" class="btn sm">搜索</span>
                                    </div>
                                </div>
                                <div class="loan-div-container">
                                    <table class="loan-table">
                                        <thead>
                                        <tr class="loan-div-title ui-list-title">
                                            <th style="text-align: center;"> 项目编号</th>
                                            <th style="text-align: center;">历史年回报率</th>
                                            <th style="text-align: center;">借款金额</th>
                                            <th style="text-align: center;" >借款期限</th>
                                            <th style="text-align: center;" >还款方式</th>
                                            <th style="text-align: center;" >收款人</th>
                                            <th style="text-align: center;" >收款人名称</th>
                                            <th style="text-align: center;" >标的添加时间</th>
                                            <th style="text-align: center;" >操作</th>
                                        </tr>
                                        </thead>
                                        <tbody id="empowerList">
                                        <tr>
                                            <td colspan="9">
                                                <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <div id="empowerListEmptyStyle"></div>
                                    <div class="pages-nav" id="empower-pagination"></div>
                                </div>
                            </div>
                        </li>
                        <!-- 待授权  end -->
                        <!-- 已授权 -->
                        <li class="" panel="3">
                            <div class="loan-div">
                                <div class="loan-div-top">
                                    <div class="loan-divleft">
                                        <label>项目编号</label>
                                        <input id="authed_borrowNid" type="text" class="text" name="text" placeholder="项目编号">
                                    </div>
                                    <div class="loan-divright">
                                        <label>授权时间</label>
                                        <input type="text" class="date start" id="authed_startDate" placeholder="开始时间">
                                        <label>至</label>
                                        <input type="text" class="date end" id="authed_endDate" placeholder="结束时间">
                                        <span onclick="getBorrowAuthedListPage()" class="btn sm">搜索</span>
                                    </div>
                                </div>
                                <div class="loan-div-container">
                                    <table class="loan-table">
                                        <thead>
                                        <tr class="loan-div-title ui-list-title">
                                            <th style="text-align: center;"> 项目编号</th>
                                            <th style="text-align: center;">历史年回报率</th>
                                            <th style="text-align: center;">借款金额</th>
                                            <th style="text-align: center;" >借款期限</th>
                                            <th style="text-align: center;" >还款方式</th>
                                            <th style="text-align: center;" >收款人</th>
                                            <th style="text-align: center;" >收款人名称</th>
                                            <th style="text-align: center;" >授权完成时间</th>
                                        </tr>
                                        </thead>

                                        <tbody id="empowerOrgList">
                                        <tr>
                                            <td colspan="8">
                                                <div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <div id="empowerOrgListEmptyStyle"></div>
                                    <div class="pages-nav" id="empowerOrg-pagination"></div>
                                </div>
                            </div>
                        </li>
                        <!-- 已授权  END -->
                    </c:if>
                </ul>
            </div>
        </div>
        <!-- end 内容区域 -->
    </div>
</article>
<jsp:include page="/footer.jsp"></jsp:include>
<!-- 设置标签页显示  -->
<script>var tab_index = '${tab}'; </script>
<script src="${cdn}/dist/js/lib/bootstrap-datepicker.min.js"></script>
<script src="${cdn}/dist/js/loan/borrowAuth.js"></script>
<script src="${cdn}/dist/js/loan/loan.js?version=${version}"></script><!--现在使用 loan.js 替换 bank 下的repay.js-->
<!-- 设置定位  -->
<script>setActById("loanManage");</script>
</body>

</html>