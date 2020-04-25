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
	<article class="main-content  p-top-0">
		<div class="broadcast-box">
	    		<div class="broadcast">
		    		<i class="i-broadcast"></i>
		    		<p>平台现已开通<span>江西银行</span>存管服务，原汇付天下账户将无法投资，请您立即提现。</p>
		    		<a href="">立即提现</a>
		    		<p class="balance">汇付天下余额：3512.00元</p>
	    		</div>
	    		
	    	</div>
	    	<div class="container relative">
	    		<!-- start 内容区域 -->
	    		<div class="basic-infor">
	    			<div class="basic">
	    				<a class="head"><img src="${ctx}/dist/images/acc-set/account-profile@2x.png" width="60" /></a>
	    				<div class="info">
		    				<p class="name">陈先生</p>
		    				<p class="vip"><i class="i-crown"></i><span>白金会员</span></p>
		    				<div class="function">
		    					<a class="i-person-info" href=""></a>
		    					<a class="i-lock" href=""></a>
		    					<a class="i-mail disable" href=""></a>
		    					<a class="i-ce disable" href=""></a>
		    				</div>
	    				</div>
	    			</div>
	    			<div class="account">
	    				<div class="coupons">
	    					<p><i class="i-coupons"></i>优惠券<span>13</span>张</p>
	    				</div>
	    				<div class="state">
	    					<span>存管账户：未开户</span>
	    					<a><i class="i-exclamation"></i>马上开户</a>
	    				</div>
	    				<em class="rule"></em>
	    			</div>
	    			<div class="amount">
	    				<p>可用金额（元）</p>
	    				<p class="money">10,000,000.<span>00</span></p>
	    				<a class="refresh"><i class="i-refresh"></i>刷新</a>
	    				<a class="btn-recharge" href="">充值</a>
	    				<a class="btn-cash" href="">提现</a>
	    			</div>
	    		</div>
	    		
	    		
	    		<div class="rights-manage overview-box">
	                <div class="rights-top">
	                	<div class="title">
		    				<p><i class="i-card"></i>我的资产</p>
		    			</div>
	                     <div class="rights-main">
	                         <div class="rights-fl">
	                            <div id="main" style="width:300px;height:300px;margin-top: 40px;"></div>   
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
	                                 <li class="p2"><span class="lg">10100.</span><span class="sm">00</span></li>
	                                 <li class="p3"><span class="lg">100.</span><span class="sm">00</span></li>
	                                 <li class="p4"><span class="lg">10100.</span><span class="sm">00</span></li>
	                             </ul>
	                             <ul class="table-main total">
	                                 <li class="p1" style="padding-left: 0;"><span class="fr1">总计</span></li>
	                                 <li class="p2"><span class="lg">10100.</span><span class="sm">00</span></li>
	                                 <li class="p3"><span class="lg">100.</span><span class="sm">00</span></li>
	                                 <li class="p4"><span class="lg">10100.</span><span class="sm">00</span></li>
	                             </ul>
	                         </div>
	                    </div>
	                </div>
                </div>
	            
	            
	    		<div class="assets">
	    			<div>
	    				<p class="tit">总资产（元）</p>
	    				<p class="money">10,000,000.<span>00</span></p>
	    				<em class="rule"></em>
	    			</div>
	    			<div>
	    				<p class="tit">冻结金额（元）</p>
	    				<p class="money">10,000,000.<span>00</span></p>
	    				<em class="rule"></em>
	    			</div>
	    			<div>
	    				<p class="tit">累计收益</p>
	    				<p class="money">10,000,000.<span>00</span></p>
	    			</div>
	    		</div>
	    		<div class="repayment overview-box">
	    			<div class="title">
	    				<p><i class="i-repayment"></i>近期还款</p>
	    				<p class='receipt'>待收总额：<span>10,000,000.00</span>元</p>
	    			</div>
	    			<div class="table">
    					<div class="tit">
    						<div class="t1">回款时间</div>
    						<div class="t2">项目名称</div>
    						<div class="t3">预测年化收益率</div>
    						<div class="t4">汇款金额</div>
    						<div class="t5">状态</div>
    					</div>
    					<a class="tr" href="#">
    						<div class="t1">2016-08-08</div>
    						<div class="t2">青岛某化工厂借款...</div>
    						<div class="t3">12%</div>
    						<div class="money t4">5000.<span>00</span></div>
    						<div class="back t5">已还款</div>
    					</a>
    					<a class="tr" href="#">
    						<div class="t1">2016-08-08</div>
    						<div class="t2">青岛某化工厂借款...</div>
    						<div class="t3">12%</div>
    						<div class="money t4">5000.<span>00</span></div>
    						<div class="back t5">已还款</div>
    					</a>
    					<a class="tr" href="#">
    						<div class="t1">2016-08-08</div>
    						<div class="t2">青岛某化工厂借款...</div>
    						<div class="t3">12%</div>
    						<div class="money t4">5000.<span>00</span></div>
    						<div class="t5">未还款</div>
    					</a>
    					<a class="tr" href="#">
    						<div class="t1">2016-08-08</div>
    						<div class="t2">青岛某化工厂借款...</div>
    						<div class="t3">12%</div>
    						<div class="money t4">5000.<span>00</span></div>
    						<div class="t5">未还款</div>
    					</a>
    				</div>
	    		</div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/acc-set/account-overview.js"></script>
</body>
</html>