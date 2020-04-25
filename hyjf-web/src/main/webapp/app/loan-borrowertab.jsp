<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    <article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 --> 
            <div class="loan-guarantors">
                <div class="loan-top">
                    <div class="loan-item">
                        <div class="loan-title">
                            <p class="loan-line"></p>
                            <p class="loan-mid">待垫付总额（元）</p>
                            <p class="loan-line"></p>
                        </div>
                        <div class="loan-main">
                            <span class="lg">10,000.</span>
                            <span class="sm">00</span>
                        </div>
                    </div>
                    <div class="loan-item-line2"></div>
                    <div class="loan-item">
                        <div class="loan-title">
                            <p class="loan-line"></p>
                            <p class="loan-mid">待收垫付总额（元）</p>
                            <p class="loan-line"></p>
                        </div>
                        <div class="loan-main">
                            <span class="lg">10,000.</span>
                            <span class="sm">00</span>
                        </div>
                    </div>
                </div>
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="active" panel="0"><a href="#">待还款</a></li>
                        <li panel="1"><a href="#">已还款</a></li>
                        <li panel="2"><a href="#">待还垫付款</a></li>
                    </ul>
                    <ul class="tab-panels">
                        <li class="active" panel="0">
                            <div class="loan-div">
                                <div class="loan-div-top">
                                    <div class="loan-divleft">
                                        <label>项目编号</label>
                                        <input type="text" class="text" name="text" placeholder="HDD170300101">
                                    </div>
                                    <div class="loan-divright">
                                        <label>应还时间</label>
                                        <input type="text" class="date start" onclick="layDateStart(start)" id="start" placeholder="2016-01-01">
                                        <label>至</label>
                                        <input type="text" class="date end" onclick="layDateEnd(end)" id="end" placeholder="2016-01-02">
                                        <span class="btn sm">搜索</span>
                                        <a class="btn-batch batch-repay-advance">批量还款垫付</a>
                                    </div>
                                </div>
                                <div class="loan-div-container">
                                    <table class="loan-table">
                                        <thead>
                                            <tr class="loan-div-title">
                                                <th class="item1">应还时间</th>
                                                <th class="item2">项目编号</th>
                                                <th class="item3">预期年收益</th>
                                                <th class="item4">借款金额</th>
                                                <th class="item5">应还总额</th>
                                                <th class="item6" style="padding-left: 20px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="loan-div-a">
                                            <td class="item1 loan-div-row">
                                                    <span class="font-nocolor">2016-08-08</span><a href="#" class="font-bg font-nocolor">下载协议</a>
                                            </td>
                                            <td class="item2 loan-div-row">HDD170300101</td>
                                            <td class="item3 loan-div-row">12%</td>
                                            <td class="item4 loan-div-row">5000元</td>
                                            <td class="item5 loan-div-row">6584.6元</td>
                                            <td class="item6 loan-div-row"><span class="btn sm">还款</span></td>
                                            </tr>
                                            <tr class="loan-div-a">
                                            <td class="item1 loan-div-row">
                                                    <span class="font-nocolor">2016-08-08</span><a href="#" class="font-bg font-nocolor">下载协议</a>
                                            </td>
                                            <td class="item2 loan-div-row">HDD170300101</td>
                                            <td class="item3 loan-div-row">12%</td>
                                            <td class="item4 loan-div-row">5000元</td>
                                            <td class="item5 loan-div-row">6584.6元</td>
                                            <td class="item6 loan-div-row"><span class="btn sm">还款</span></td>
                                            </tr>
                                            <tr class="loan-div-a">
                                            <td class="item1 loan-div-row">
                                                    <span class="font-nocolor">2016-08-08</span><a href="#" class="font-bg font-nocolor">下载协议</a>
                                            </td>
                                            <td class="item2 loan-div-row">HDD170300101</td>
                                            <td class="item3 loan-div-row">12%</td>
                                            <td class="item4 loan-div-row">5000元</td>
                                            <td class="item5 loan-div-row">6584.6元</td>
                                            <td class="item6 loan-div-row"><span class="grey">个人还款中</span></td>
                                            </tr>
                                        </tbody>
                                    </table>
                          
                                </div>
                            </div>                 
                        </li>
                        <li panel="1">
                               <div class="loan-div">
                                <div class="loan-div-top">
                                    <div class="loan-divleft">
                                        <label>项目编号</label>
                                        <input type="text" class="text" name="text" placeholder="HDD170300101">
                                    </div>
                                    <div class="loan-divright">
                                        <label>应还时间</label>
                                        <input type="text" class="date" onclick="layDateStart(start2)" id="start2" placeholder="2016-01-01">
                                        <label>至</label>
                                        <input type="text" class="date" onclick="layDateEnd(end2)" id="end2" placeholder="2016-01-02">
                                        <span class="btn sm">搜索</span>
                                        <a class="btn-batch batch-repay-advance">批量还款垫付</a>
                                    </div>
                                </div>
                                <div class="loan-div-container">
                                    <table class="loan-table">
                                        <thead>
                                            <tr class="loan-div-title">
                                                <th class="bor1">应还时间</th>
                                                <th class="bor2">项目编号</th>
                                                <th class="bor3">期数</th>
                                                <th class="bor4">预期年收益</th>
                                                <th class="bor5">垫付总额</th>
                                                <th class="bor6">垫付时间</th>
                                                <th class="bor7">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="loan-div-a">
                                                <td class="bor1 loan-div-row">2016-08-08</td>
                                                <td class="bor2 loan-div-row">HDD170300101</td>
                                                <td class="bor3 loan-div-row">第1期</td>
                                                <td class="bor4 loan-div-row">12%</td>
                                                <td class="bor5 loan-div-row">5000元</td>
                                                <td class="bor6 loan-div-row">6584.56元</td>
                                                <td class="bor7 loan-div-row">
                                                <a class="value" style="text-decoration: none;" href="#">详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                            <tr class="loan-div-a">
                                                <td class="bor1 loan-div-row">2016-08-08</td>
                                                <td class="bor2 loan-div-row">HDD170300101</td>
                                                <td class="bor3 loan-div-row">第1期</td>
                                                <td class="bor4 loan-div-row">12%</td>
                                                <td class="bor5 loan-div-row">5000元</td>
                                                <td class="bor6 loan-div-row">6584.56元</td>
                                                <td class="bor7 loan-div-row">
                                                <a class="value" style="text-decoration: none;" href="#">详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div> 
                        </li>
                        <li panel="2">
                            <div class="loan-div">
                                <div class="loan-div-top">
                                    <div class="loan-divleft">
                                        <label>项目编号</label>
                                        <input type="text" class="text" name="text" placeholder="HDD170300101">
                                    </div>
                                    <div class="loan-divright">
                                        <label>应还时间</label>
                                        <input type="text" class="date" onclick="layDateStart(start3)" id="start3" placeholder="2016-01-01">
                                        <label>至</label>
                                        <input type="text" class="date" onclick="layDateEnd(end3)" id="end3" placeholder="2016-01-02">
                                        <span class="btn sm">搜索</span>
                                        <a class="btn-batch batch-repay-advance">批量还款垫付</a>
                                    </div>
                                </div>
                                <div class="loan-div-container">
                                <table class="loan-table">
                                        <thead>
                                            <tr class="loan-div-title">
                                                <th class="bor1">应还时间</th>
                                                <th class="bor2">项目编号</th>
                                                <th class="bor3">期数</th>
                                                <th class="bor4">预期年收益</th>
                                                <th class="bor5">换垫付总额</th>
                                                <th class="bor6">换垫付时间</th>
                                                <th class="bor7">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="loan-div-a">
                                                <td class="bor1 loan-div-row">2016-08-08</td>
                                                <td class="bor2 loan-div-row">HDD170300101</td>
                                                <td class="bor3 loan-div-row">第1期</td>
                                                <td class="bor4 loan-div-row">12%</td>
                                                <td class="bor5 loan-div-row">5000元</td>
                                                <td class="bor6 loan-div-row">6584.56元</td>
                                                <td class="bor7 loan-div-row">
                                                <a class="value" style="text-decoration: none;" href="#">详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                            <tr class="loan-div-a">
                                                <td class="bor1 loan-div-row">2016-08-08</td>
                                                <td class="bor2 loan-div-row">HDD170300101</td>
                                                <td class="bor3 loan-div-row">第1期</td>
                                                <td class="bor4 loan-div-row">12%</td>
                                                <td class="bor5 loan-div-row">5000元</td>
                                                <td class="bor6 loan-div-row">6584.56元</td>
                                                <td class="bor7 loan-div-row">
                                                <a class="value" style="text-decoration: none;" href="#">详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>              
                        </li>
                    </ul>
                </div>
            </div>                      
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
    <script src="../dist/js/lib/laydate.dev.js"></script>
    <script src="../dist/js/date.js"></script>
	<script src="../dist/js/loan/loan.js"></script>
</body>
</html>