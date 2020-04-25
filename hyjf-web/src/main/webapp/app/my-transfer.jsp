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
            <div class="my-transfer">
                <div class="mytransfer-top">
                    <div class="rights-item">
                        <div class="rights-title">
                            <span class="lg value">1,000,000.00</span>
                            <span class="sm"></span>
                        </div>
                        <p class="rights-grey">可转让本金（元）</p>
                    </div>
                    <div class="rights-line"></div>
                    <div class="rights-item">
                        <div class="rights-title">
                            <span class="lg black">52,000</span>
                            <span class="sm"></span>
                        </div>
                        <p class="rights-grey">当前持有本金（元）</p>
                    </div>
                    <div class="rights-line"></div>
                    <div class="rights-item">
                        <div class="rights-title">
                            <span class="lg black">587.23</span>
                            <span class="sm"></span>
                        </div>
                        <p class="rights-grey">转让中本金（元）</p>
                    </div>
                    <div class="rights-line"></div>
                    <div class="rights-item">
                        <div class="rights-title">
                            <span class="lg black">587.23</span>
                            <span class="sm"></span>
                        </div>
                        <p class="rights-grey">累计已转让本金（元）</p>
                    </div>
                </div>
                <div class="main">
                    <div class="title">可转让列表</div>
                    <table class="table">
                        <thead>
                            <th class="ui-list-title pl1">项目编号</th>
                            <th class="ui-list-title pl2">期限/历史年回报率</th>
                            <th class="ui-list-title pl3">
                                <a href="javascript:void(0)">投资时间<i class="icon iconfont icon-jiantou"></i></a>                                            
                            </th>
                            <th class="ui-list-title pl4">
                                <a href="javascript:void(0)">可转让本金<i class="icon iconfont icon-jiantou"></i></a>                                            
                            </th>
                            <th class="ui-list-title pl5">
                                <a href="javascript:void(0)">持有期限<i class="icon iconfont icon-jiantou"></i></a>                                            
                            </th>
                            <th class="ui-list-title pl6">
                                <a href="javascript:void(0)">剩余期限<i class="icon iconfont icon-jiantou"></i></a>                                            
                            </th>
                            <th class="ui-list-title pl7">操作</th>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="ui-list-item pl1">HDD170300101</td>
                                <td class="ui-list-item pl2">3个月 / 10.50%</td>
                                <td class="ui-list-item pl3">2016-02-05 12:11</td>
                                <td class="ui-list-item pl4">1,000,000</td>
                                <td class="ui-list-item pl5">152天</td>
                                <td class="ui-list-item pl6">36天</td>
                                <td class="ui-list-item pl7">
                                    <a class="value" style="text-decoration: none;" href="#">转让<span class="iconfont icon-more"></span></a>
                                </td>
                            </tr>
                        </tbody>
                        <tbody>
                            <tr>
                                <td class="ui-list-item pl1">HDD170300101</td>
                                <td class="ui-list-item pl2">3个月 / 10.50%</td>
                                <td class="ui-list-item pl3">2016-02-05 12:11</td>
                                <td class="ui-list-item pl4">1,000,000</td>
                                <td class="ui-list-item pl5">152天</td>
                                <td class="ui-list-item pl6">36天</td>
                                <td class="ui-list-item pl7">
                                    <a class="value" style="text-decoration: none;" href="#">转让<span class="iconfont icon-more"></span></a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>  
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/rights-manage/my-transfer.js"></script>
</body>
</html>