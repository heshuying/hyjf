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
                <div class="loan-borrower">
                    <div class="loan-borrower-batch-top">
                        <div class="borrower-title-2">
	                	    <div class="loan-divright">
	                	    	<form>
	                                <label>应还时间</label>
	                                <input type="text" class="date start" id="start" placeholder="开始日期">
	                                <label>至</label>
	                                <input type="text" class="date end" id="end" placeholder="结束日期">
	                                <span class="btn sm">搜索</span>
                                </form>
                            </div>            
                        </div>
                        <div class="borrower-title-main">
                        	<form id="info">
	                        	<div class="borrower-item">
	                        		<div class="total">
	                        			<span><img src="../dist/images/loan-icon2.png"></span>
	                               		<span>应还总额：<em>20154</em> 元</span>
	                        		</div>
	                                <div class="details">
	                                	<span>应还本金：<em>2000</em> 元</span>
	                                	<span>应还利息：<b>100</b> 元</span>
	                                	<span>管理费：<b>10 </b>元</span>
	                                	<br/>
	                                	<span>提前减息：<b>0</b> 元</span>
	                                	<span>延期利息：<b>0</b> 元 </span>
	                                	<span>逾期利息：<b>0</b> 元</span>
	                                </div>
	                            </div>
	                            <div class="borrower-arr">
		                                <label>平台登录密码</label>
		                                <input type="text" name="password" class="password text">
		                                <span class="btn">确认</span>	                                
	                            </div>
                            </form>
                        </div>
                    </div>
                    <div class="loan-borrower-batch-bottom">
                        <div class="loan-thead">
                            <span class="l1">应还时间</span>
                            <span class="l2">项目编号</span>
                            <span class="l3">期数</span>
                            <span class="l4">历史年回报率</span>
                            <span class="l5">借款金额</span>
                            <span class="l6">本期应还总额</span>
                        </div>
                        <ul>
                            <li>
                                <span class="l1">2016-08-08</span>
	                            <span class="l2">HDD170300101</span>
	                            <span class="l3">第1期</span>
	                            <span class="l4">12%</span>
	                            <span class="l5">5000 元</span>
	                            <span class="l6">6584.56元</span>
                            </li>
                            <li>
                                <span class="l1">2016-08-08</span>
	                            <span class="l2">HDD170300101</span>
	                            <span class="l3">第1期</span>
	                            <span class="l4">12%</span>
	                            <span class="l5">5000 元</span>
	                            <span class="l6">6584.56元</span>
                            </li>
                            <li>
                                <span class="l1">2016-08-08</span>
	                            <span class="l2">HDD170300101</span>
	                            <span class="l3">第1期</span>
	                            <span class="l4">12%</span>
	                            <span class="l5">5000 元</span>
	                            <span class="l6">6584.56元</span>
                            </li>
                            <li>
                                <span class="l1">2016-08-08</span>
	                            <span class="l2">HDD170300101</span>
	                            <span class="l3">第1期</span>
	                            <span class="l4">12%</span>
	                            <span class="l5">5000 元</span>
	                            <span class="l6">6584.56元</span>
                            </li>
                        </ul>
                        <div class="pages-nav">
		                    <div class="prev">上一页</div>
		                    <a href="" class="active">1</a>
		                    <a href="">2</a>
		                    <a href="">3</a>
		                    <a href="">4</a>
		                    <a href="">...</a>
		                    <a href="">50</a>
		                    <a href="">51</a>
		                    <a class="next">下一页</a>
		                </div>
                    </div>
                </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/loan/loan-batch.js"></script>
</body>
</html>