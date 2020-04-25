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
	<article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->
            <div class="rights-manage">
                <div class="rights-top">
                    <div class="rights-arr">
                        <div class="rights-item">
                            <div class="rights-title">
                                <span class="lg">52587.</span>
                                <span class="sm">23</span>
                            </div>
                            <p class="rights-grey">接收总额（元）</p>
                        </div>
                        <div class="rights-line"></div>
                        <div class="rights-item">
                            <div class="rights-title">
                                <span class="black">52000</span>
                            </div>
                            <p class="rights-grey">接收本金（元）</p>
                        </div>
                        <div class="rights-line"></div>
                        <div class="rights-item">
                            <div class="rights-title">
                                <span class="black">587.</span>
                                <span class="black-sm">23</span>
                            </div>
                            <p class="rights-grey">接收收益（元）</p>
                        </div>
                    </div>
                    <div class="rights-main">
                        <div class="rights-fl">
                            <div id="main" style="width:300px;height:300px;"></div>
                        </div>
                        <div class="rights-fr">
                            <p class="p1">资产</p>
                            <p class="p2">待收金额（元）</p>
                            <p class="p3">待收本金（元）</p>
                            <p class="p4">待收收益（元）</p>
                            <ul class="table-main">
                                <li class="p1" style="padding-left: 0;"><span class="icon-weibiaoti"><i></i></span><span class="fr1">债权</span></li>
                                <li class="p2"><span class="lg">10100.</span><span class="sm">00</span></li>
                                <li class="p3"><span class="lg">100.</span><span class="sm">00</span></li>
                                <li class="p4"><span class="lg">10100.</span><span class="sm">00</span></li>
                            </ul>
                            <ul class="table-main">
                                <li class="p1" style="padding-left: 0;"><span class="icon-weibiaoti"><i></i></span><span class="fr1">计划</span></li>
                                <li class="p2"><span class="lg">20100.</span><span class="sm">00</span></li>
                                <li class="p3"><span class="lg">200.</span><span class="sm">00</span></li>
                                <li class="p4"><span class="lg">20100.</span><span class="sm">00</span></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="rights-list">
                    <ul class="tab-tags">
                        <li class="tab-tagsli show1 active">债权</li>
                        <li class="tab-tagsli show2">计划</li>
                    </ul>
                    <div class="tab-itempar">
                        <ul class="tab-divul">
                            <li class="tab-div-li active-in" onclick="onShow(this)"><a href="#">当前持有(0)</a></li>
                            <li class="tab-div-li" onclick="onShow(this)"><a href="#">已回款(0)</a></li>
                            <li class="tab-div-li" onclick="onShow(this)"><a href="#">转让记录(0)</a></li>
                            <a href="" class="ui-button-orange">我要转让</a>
                        </ul>
                        <ul class="tab-divin">
                            <li>
                                <div class="tab-divin-item">
                                    <table style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title pl1">项目编号</th>
                                                <th class="ui-list-title pl2">期限/预期年收益</th>
                                                <th class="ui-list-title pl3">
                                                    <a href="javascript:void(0)">持有本金<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl4">待收总额</th>
                                                <th class="ui-list-title pl5">
                                                    <a href="javascript:void(0)">投资时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl6">
                                                    <a href="javascript:void(0)">预计还款时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl7">说明</th>
                                                <th class="ui-list-title pl8" style="padding-left: 10px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype blue">加息券</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                <!-- <div class="ui-poptip-content">
                                                        <ul>
                                                            <li>投资本金：10,000.00元</li>
                                                            <li>已转让本金：6,000.00元</li>
                                                            <li>当前持有本金：<span class="value">4,000.00</span>元</li>
                                                        </ul>
                                                </div>  --> 
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow value">转让中</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <div class="fn-text-overflow text-right">
                                                        <a href="#" class="small value">还款计划</a>
                                                        <a class="no-dection" href="#" title="下载《投资协议》"><i class="icon iconfont icon-xiazai"></i></a>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype value" style="border: 1px solid #fa5e28;">代金券</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    3000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow blue">投资中</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <div class="fn-text-overflow text-right">
                                                        <a href="#" class="small value">还款计划</a>
                                                        <a class="no-dection" href="#" title="下载《投资协议》"><i class="icon iconfont icon-xiazai"></i></a>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype green" style="border: 1px solid #5bc1a8;">体验金</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    2000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow">1.5加资券</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <div class="fn-text-overflow text-right">
                                                        <a href="#" class="small value">还款计划</a>
                                                        <a class="no-dection" href="#" title="下载《投资协议》"><i class="icon iconfont icon-xiazai"></i></a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </li>
                            <li class="hide">
                                <div class="tab-divin-item">
                                    <table style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title pl1">项目编号</th>
                                                <th class="ui-list-title pl2">期限/预期年收益</th>
                                                <th class="ui-list-title pl3">
                                                    <a href="javascript:void(0)">投资金额<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl4">还款总额</th>
                                                <th class="ui-list-title pl5">
                                                    <a href="javascript:void(0)">实际收益<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl6">
                                                    <a href="javascript:void(0)">还款时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl7">说明</th>
                                                <th class="ui-list-title pl8" style="padding-left: 10px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype blue">加息券</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow">部分转让</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <div class="fn-text-overflow text-right">
                                                        <a href="#" class="small value">还款计划</a>
                                                        <a class="no-dection" href="#" title="下载《投资协议》"><i class="icon iconfont icon-xiazai"></i></a>
                                                    </div>
                                                </td>
                                            </tr>
                                             <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype value" style="border: 1px solid #fa5e28;">代金券</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow">现金投资</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <div class="fn-text-overflow text-right">
                                                        <a href="#" class="small value">还款计划</a>
                                                        <a class="no-dection" href="#" title="下载《投资协议》"><i class="icon iconfont icon-xiazai"></i></a>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype green" style="border: 1px solid #5bc1a8;">体验金</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow">1.5加资券</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <div class="fn-text-overflow text-right">
                                                        <a href="#" class="small value">还款计划</a>
                                                        <a class="no-dection" href="#" title="下载《投资协议》"><i class="icon iconfont icon-xiazai"></i></a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </li>
                            <li class="hide">
                                <div class="tab-divin-item">
                                    <table style="width: 100%;">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title zr1">项目编号</th>
                                                <th class="ui-list-title zr2">原始转让本金</th>
                                                <th class="ui-list-title zr3">折让比例</th>
                                                <th class="ui-list-title zr4">转让时间</th>
                                                <th class="ui-list-title zr5">剩余期限</th>
                                                <th class="ui-list-title zr6">已转让金额</th>
                                                <th class="ui-list-title zr7">累计收到金额</th>
                                                <th class="ui-list-title zr8">说明</th>
                                                <th class="ui-list-title zr9" style="padding-left: 20px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="ui-list-item zr1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item zr2">1,000,000.00</td>
                                                <td class="ui-list-item zr3">
                                                    0.2%
                                                </td>
                                                <td class="ui-list-item zr4">2016-02-05 12:11</td>
                                                <td class="ui-list-item zr5">125天</td>
                                                <td class="ui-list-item zr6">9,000.00</td>
                                                <td class="ui-list-item zr7">
                                                    9,999.56
                                                </td>
                                                <td class="ui-list-item value zr8">80%</td>
                                                <td class="ui-list-item zr9">
                                                    <a class="value" onclick="showPop()" style="text-decoration: none;" href="javascript:void(0)">查看详情<span class="iconfont icon-more"></span></a></td>
                                            </tr>
                                            <tr>
                                                <td class="ui-list-item zr1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item zr2">1,000,000.00</td>
                                                <td class="ui-list-item zr3">
                                                    0.2%
                                                </td>
                                                <td class="ui-list-item zr4">2016-02-05 12:11</td>
                                                <td class="ui-list-item zr5">125天</td>
                                                <td class="ui-list-item zr6">9,000.00</td>
                                                <td class="ui-list-item zr7">
                                                    9,999.56
                                                </td>
                                                <td class="ui-list-item zr8">80%</td>
                                                <td class="ui-list-item zr9">
                                                    <a class="value" onclick="showPop()" style="text-decoration: none;" href="javascript:void(0)">查看详情<span class="iconfont icon-more"></span></a></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="tab-parents">
                        <ul class="tab-divul2">
                            <li class="tab-div-li active-in" onclick="onShow(this)"><a href="#">当前持有(0)</a></li>
                            <li class="tab-div-li" onclick="onShow(this)"><a href="#">已回款(0)</a></li>
                        </ul>
                        <ul class="tab-divin2">
                            <li>
                                <div class="tab-divin-item">
                                    <table style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title pl1">项目编号</th>
                                                <th class="ui-list-title pl2">期限/预期年收益</th>
                                                <th class="ui-list-title pl3">
                                                    <a href="javascript:void(0)">持有本金<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl4">待收总额</th>
                                                <th class="ui-list-title pl5">
                                                    <a href="javascript:void(0)">投资时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl6">
                                                    <a href="javascript:void(0)">预计退出时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl7">说明</th>
                                                <th class="ui-list-title pl8" style="padding-left: 10px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype blue">加息券</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow value">转让中</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <a class="value" onclick="showPop2()" style="text-decoration: none;" href="javascript:void(0)">查看详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                             <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype value" style="border: 1px solid #fa5e28;">代金券</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow blue">投资中</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <a class="value"  onclick="showPop2()" style="text-decoration: none;" href="javascript:void(0)">查看详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                             <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype green" style="border: 1px solid #5bc1a8;">体验金</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow">1.5加资券</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <a class="value"  onclick="showPop2()" style="text-decoration: none;" href="javascript:void(0)">查看详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </li>
                            <li class="hide">
                                <div class="tab-divin-item">
                                    <table style="width: 100%">
                                        <thead>
                                            <tr>
                                                <th class="ui-list-title pl1">项目编号</th>
                                                <th class="ui-list-title pl2">期限/预期年收益</th>
                                                <th class="ui-list-title pl3">
                                                    <a href="javascript:void(0)">投资金额<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl4">回款总额</th>
                                                <th class="ui-list-title pl5">
                                                    <a href="javascript:void(0)">实际收益<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl6">
                                                    <a href="javascript:void(0)">回款时间<i class="icon iconfont icon-jiantou"></i></a>
                                                </th>
                                                <th class="ui-list-title pl7">说明</th>
                                                <th class="ui-list-title pl8" style="padding-left: 10px;">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype blue">加息券</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow value">转让中</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <a class="value" style="text-decoration: none;" href="#">查看详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                             <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype value" style="border: 1px solid #fa5e28;">代金券</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow">投资中</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <a class="value" style="text-decoration: none;" href="#">查看详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                             <tr>
                                                <td class="ui-list-item pl1">
                                                    <div class="fn-text-overflow">
                                                        <span class="rrd-dimgray">HDD170300101</span>
                                                        <em class="ui-loantype green" style="border: 1px solid #5bc1a8;">体验金</em>
                                                    </div>
                                                </td>
                                                <td class="ui-list-item pl2">3个月/10.5%</td>
                                                <td class="ui-list-item pl3">
                                                    4000<a title="当前持有1000元" class="icon iconfont icon-zhu"></a>
                                                </td>
                                                <td class="ui-list-item pl4">13.36</td>
                                                <td class="ui-list-item pl5">2016-10-12</td>
                                                <td class="ui-list-item pl6">2016-10-12</td>
                                                <td class="ui-list-item pl7">
                                                    <div class="fn-text-overflow">1.5加资券</div>
                                                </td>
                                                <td class="ui-list-item pl8">
                                                    <a class="value" style="text-decoration: none;" href="#">查看详情<span class="iconfont icon-more"></span></a>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="wraper"></div>
                <div class="wraper-main">
                    <div class="top">
                        <p class="fn-left">HFF123644885</p>
                        <span class="fn-btn">部分转让</span>
                        <p class="fn-time">转让时间：2017-02-02 12:12:25</p>
                        <i onclick="closePop()" class="iconfont icon-chahao1"></i>
                    </div>
                    <div class="fn-info">
                        <div class="fn-div">
                            <p class="fn-div-lg">15,000.00</p>
                            <p class="fn-div-text">原始转让本金</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg">5,000.00</p>
                            <p class="fn-div-text">已转让本金</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg">15,000.00</p>
                            <p class="fn-div-text">已收垫付利息</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg">15,000.00</p>
                            <p class="fn-div-text">已付服务费</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg value">15,000.00</p>
                            <p class="fn-div-text">累计到账金额</p>
                        </div>
                    </div>
                    <div class="ui-list-header">转让明细</div>
                    <div class="ui-box">
                        <table>
                            <thead>
                                <tr>
                                    <th class="ui-list-title pl1">用户</th>
                                    <th class="ui-list-title pl2">购买本金</th>
                                    <th class="ui-list-title pl3">垫付利息</th>
                                    <th class="ui-list-title pl4">实付金额</th>
                                    <th class="ui-list-title pl5">服务费</th>
                                    <th class="ui-list-title pl6">到账金额</th>
                                    <th class="ui-list-title pl7">承接时间</th>
                                    <th class="ui-list-title pl8">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="ui-list-item pl1 ui-name">
                                        <span class="fam">任</span>
                                        <span class="fam-name">小玉</span>
                                    </td>
                                    <td class="ui-list-item pl2">10,000,000</td>
                                    <td class="ui-list-item pl3">1,234.00</td>
                                    <td class="ui-list-item pl4">1,322.12</td>
                                    <td class="ui-list-item pl5">1.23</td>
                                    <td class="ui-list-item pl6">1,320.25</td>
                                    <td class="ui-list-item pl7">
                                        <span class="ui-day">2016-12-12 </span>
                                        <span class="ui-time">12 :12:16</span>
                                    </td>
                                    <td class="ui-list-item pl8 value"><a href="">协议</a></td>
                                </tr>
                                <tr>
                                    <td class="ui-list-item pl1 ui-name">
                                    <span class="fam">任</span>
                                    <span class="fam-name">小玉</span>
                                    </td>
                                    <td class="ui-list-item pl2">10,000,000</td>
                                    <td class="ui-list-item pl3">1,234.00</td>
                                    <td class="ui-list-item pl4">1,322.12</td>
                                    <td class="ui-list-item pl5">1.23</td>
                                    <td class="ui-list-item pl6">1,320.25</td>
                                    <td class="ui-list-item pl7">
                                        <span class="ui-day">2016-12-12 </span>
                                        <span class="ui-time">12 :12:16</span>
                                    </td>
                                    <td class="ui-list-item pl8 value"><a href="">协议</a></td>
                                </tr>
                                <tr>
                                    <td class="ui-list-item pl1 ui-name">
                                        <span class="fam">任</span>
                                        <span class="fam-name">小玉</span>
                                    </td>
                                    <td class="ui-list-item pl2">10,000,000</td>
                                    <td class="ui-list-item pl3">1,234.00</td>
                                    <td class="ui-list-item pl4">1,322.12</td>
                                    <td class="ui-list-item pl5">1.23</td>
                                    <td class="ui-list-item pl6">1,320.25</td>
                                    <td class="ui-list-item pl7">
                                        <span class="ui-day">2016-12-12 </span>
                                        <span class="ui-time">12 :12:16</span>
                                    </td>
                                    <td class="ui-list-item pl8 value"><a href="">协议</a></td>
                                </tr>
                                <tr>
                                    <td class="ui-list-item pl1 ui-name">
                                        <span class="fam">任</span>
                                        <span class="fam-name">小玉</span>
                                    </td>
                                    <td class="ui-list-item pl2">10,000,000</td>
                                    <td class="ui-list-item pl3">1,234.00</td>
                                    <td class="ui-list-item pl4">1,322.12</td>
                                    <td class="ui-list-item pl5">1.23</td>
                                    <td class="ui-list-item pl6">1,320.25</td>
                                    <td class="ui-list-item pl7">
                                        <span class="ui-day">2016-12-12 </span>
                                        <span class="ui-time">12 :12:16</span>
                                    </td>
                                    <td class="ui-list-item pl8 value"><a href="">协议</a></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="arr">
                        <p>注</p>
                        <ul>
                            <li>1. 实付金额=购买本金*（1-折让率）+垫付利息；</li>
                            <li>2. 服务费=实付金额 * 服务费率）；</li>
                            <li>3. 到账金额=实付金额-手续费.</li>
                        </ul>
                    </div>
                    <div class="fn-bom"><a href="" class="ui-btn btn">确认</a></div>
                </div>
                <div class="wraper-main-con">
                    <div class="top">
                        <p class="fn-left">HFF123644885</p>
                        <span class="fn-btn">部分转让</span>
                        <p class="fn-time">投资时间：2017-02-02 12:12:25</p>
                        <i onclick="closePop2()" class="iconfont icon-chahao1"></i>
                    </div>
                    <div class="fn-info">
                        <div class="fn-div">
                            <p class="fn-div-lg  value">15,000.00</p>
                            <p class="fn-div-text">已收本息</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg">5,000.00</p>
                            <p class="fn-div-text">待收本金</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg">5,000.00</p>
                            <p class="fn-div-text">待收利息</p>
                        </div>
                        <div class="fn-div">
                            <p class="fn-div-lg">15,000.00</p>
                            <p class="fn-div-text value">待收本息</p>
                        </div>
                    </div>
                    <div class="ui-list-header">还款计划</div>
                    <div class="ui-box">
                        <table>
                            <thead>
                                <tr>
                                    <th class="ui-list-title pc1">期数</th>
                                    <th class="ui-list-title pc2">待收本息</th>
                                    <th class="ui-list-title pc3">待收本金</th>
                                    <th class="ui-list-title pc4">待收利息</th>
                                    <th class="ui-list-title pc5">待收时间</th>
                                    <th class="ui-list-title pc6">应收本息</th>
                                    <th class="ui-list-title pc7 awaylengh">状态</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="ui-list-item pc1">1</td>
                                    <td class="ui-list-item pc2">1,234,000</td>
                                    <td class="ui-list-item pc3">1,234.00</td>
                                    <td class="ui-list-item pc4">1,322.12</td>
                                    <td class="ui-list-item pc5">2015-05-25</td>
                                    <td class="ui-list-item pc6">5423.26</td>
                                    <td class="ui-list-item pc7 value">已还款</td>
                                </tr>
                                <tr>
                                    <td class="ui-list-item pc1">1</td>
                                    <td class="ui-list-item pc2">1,234,000</td>
                                    <td class="ui-list-item pc3">1,234.00</td>
                                    <td class="ui-list-item pc4">1,322.12</td>
                                    <td class="ui-list-item pc5">2015-05-25</td>
                                    <td class="ui-list-item pc6">5423.26</td>
                                    <td class="ui-list-item pc7 value">已还款</td>
                                </tr>
                                <tr>
                                    <td class="ui-list-item pc1">1</td>
                                    <td class="ui-list-item pc2">1,234,000</td>
                                    <td class="ui-list-item pc3">1,234.00</td>
                                    <td class="ui-list-item pc4">1,322.12</td>
                                    <td class="ui-list-item pc5">2015-05-25</td>
                                    <td class="ui-list-item pc6">5423.26</td>
                                    <td class="ui-list-item pc7 value">已还款</td>
                                </tr>
                                <tr>
                                    <td class="ui-list-item pc1">1</td>
                                    <td class="ui-list-item pc2">1,234,000</td>
                                    <td class="ui-list-item pc3">1,234.00</td>
                                    <td class="ui-list-item pc4">1,322.12</td>
                                    <td class="ui-list-item pc5">2015-05-25</td>
                                    <td class="ui-list-item pc6">5423.26</td>
                                    <td class="ui-list-item pc7 value">未还款</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="fn-bom"><a href="" class="ui-btn btn">确认</a></div>
                </div>
            </div>
            <!-- end 内容区域 -->
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/rights-manage/right-manage.js"></script>
</body>
</html>