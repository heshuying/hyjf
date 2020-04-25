<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
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
	         <div class="left-side">您所在的位置： <a href="${ctx}/">首页</a> &gt; <a href="${ctx}/user/pandect/pandect.do">账户中心</a> &gt; <a href="${ctx}/user/assetmanage/init.do">资产管理</a> &gt; 我要转让</div>
	     </div>
	 </section>
	<article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->   
            <div class="my-transfer">
                <div class="mytransfer-top">
                    <div class="rights-item">
                        <div class="rights-title">
                            <span class="lg value">${canCreditMoney}</span>
                            <span class="sm"></span>
                        </div>
                        <p class="rights-grey">可转让本金（元）</p>
                    </div>
                    <div class="rights-line"></div>
                    <div class="rights-item">
                        <div class="rights-title">
                            <span class="lg black">${holdMoneyTotal}</span>
                            <span class="sm"></span>
                        </div>
                        <p class="rights-grey">当前持有本金（元）</p>
                    </div>
                    <div class="rights-line"></div>
                    <div class="rights-item">
                        <div class="rights-title">
                            <span class="lg black">${inCreditMoney}</span>
                            <span class="sm"></span>
                        </div>
                        <p class="rights-grey">转让中本金（元）</p>
                    </div>
                    <div class="rights-line"></div>
                    <div class="rights-item">
                        <div class="rights-title">
                            <span class="lg black">${creditSuccessMoney}</span>
                            <span class="sm"></span>
                        </div>
                        <p class="rights-grey">累计已转让本金（元）</p>
                    </div>
                </div>
                <div class="main">
                    <div class="title">可转让列表</div>
                    <table class="table" id="tableList">
                        <thead>
                            <th class="ui-list-title pl1">项目编号</th>
                            <th class="ui-list-title pl2">期限/历史年回报率</th>
                            <th class="ui-list-title pl3">
                                <a href="javascript:void(0)" sortValue="tenderTimeSort">出借时间<i class="icon iconfont icon-jiantou"></i></a>                                            
                            </th>
                            <th class="ui-list-title pl4">
                                <a href="javascript:void(0)" sortValue="creditAccountSort">可转让本金<i class="icon iconfont icon-jiantou"></i></a>                                            
                            </th>
                            <th class="ui-list-title pl5">
                                <a href="javascript:void(0)" sortValue="tenderPeriodSort">持有期限<i class="icon iconfont icon-jiantou"></i></a>                                            
                            </th>
                            <th class="ui-list-title pl6">
                                <a href="javascript:void(0)" sortValue="remainDaysSort">剩余期限<i class="icon iconfont icon-jiantou"></i></a>                                            
                            </th>
                            <th class="ui-list-title pl7">操作</th>
                        </thead>
	                    <!-- <tbody>
                            <tr>
                                <td class="ui-list-item pl1"></td>
                                <td class="ui-list-item pl2"></td>
                                <td class="ui-list-item pl3"></td>
                                <td class="ui-list-item pl4"></td>
                                <td class="ui-list-item pl5"></td>
                                <td class="ui-list-item pl6"></td>
                                <td class="ui-list-item pl7"></td>
                            </tr>
	                    </tbody> -->
                    </table>
                    
                    <!--分页-->
                    <div class="pages-nav" id="new-pagination"></div> 
                </div>
            </div>  
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/rights-manage/my-transfer.js?version=${version}"></script>
	<!-- 设置定位  -->
	<script>setActById("mytender");</script>
</body>
</html>