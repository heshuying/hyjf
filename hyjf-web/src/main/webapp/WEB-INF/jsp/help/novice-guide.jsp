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
	<div class="bond-nav">
        <div class="bond-nav-container">
            <div class="bond-nav-div"><a href="${ctx}/bank/web/borrow/newBorrowList.do">新手专区</a></div>
            <div class="bond-nav-div"><a class="bg-bgcolor" href="${ctx}/help/fresher.do">新手指引</a></div>
        </div>
    </div>
	 <article class="main-content novice-guide">
	    <div class="guide-banner">
	        <div class="core" style="color:#fff;">
	        	<div style="font-size:54px;font-weight:bold;padding-top:80px;padding-bottom:36px;letter-spacing:5px;">什么是汇盈金服？</div>
            <div style="font-size:16px;line-height:1.5em; width:680px;">
            汇盈金服（www.hyjf.com），成立于2013年，是美国公众公司品牌（OTC股票代码：SFHD）旗下创新的网络借贷信息中介服务平台。<br/>
            专注于解决借款用户融资难、融资成本高等问题，并向出借用户提供优质、安全、高收益的出借项目，为借贷两端提供安全、公平、透明、高效的居间服务。</div>
	        </div>
	    </div>
	    <div class="guide-box-2">
	    	<div class="core">
	    		<div class="title">
	    			<p class="word-2">品牌保障</p>
	    			<div class="rule"></div>
	    			<p class="summary">安全、合规、透明</p>
	    		</div>
	    		<div class="zhanshi">
	    			<div class="img-box">
	    				<img src="${cdn}/dist/images/guide/zhanshi-1@2x.png?version=${version}" alt="" width="790px"/>
	    			</div>
	    			<p class="introduce">汇盈金服成立于2013年12月，是国内成立较早的网络借贷信息中介服务平台之一。累计交易额${tenderSum }亿元，累计为用户赚取${interestSum }亿元，服务超过${totalTenderSum}万人次出借人（数据来源汇盈金服内部统计，实时更新）</p>
	    		</div>
	    	</div>
	    </div>
	    <div class="guide-box-1">
	    	<div class="core">
	    		<div class="title">
	    			<p class="word-1">资金存管</p>
	    			<div class="rule"></div>
	    			<p class="summary">保障资金安全</p>
	    		</div>
	    		<div class="zhanshi">
	    			<div class="img-box">
	    				<img src="${cdn}/dist/images/guide/zhanshi-2@2x.png?version=${version}" alt="" width="839px"/>
	    			</div>
	    			<p class="introduce">您在汇盈金服平台注册后，需在线开通存管银行存管账户；银行分账户管理用户与平台的资金，做到了资金的完全隔离，保证平台无法碰触用户资金；所有涉及到资金变动的操作，需由用户在银行页面输入交易密码授权，银行方面才会进行相应的资金操作，资金走向清晰可查。</p>
	    		</div>
	    	</div>
	    </div>
	    <div class="guide-box-4">
	    	<div class="core">
	    		<div class="title">
	    			<p class="word-1" style="background-color:#f7f7f7;">合作伙伴</p>
	    			<div class="rule"></div>
	    			<p class="summary"></p>
	    		</div>
	    		<div class="zhanshi">
	    			<div class="img-box">
	    				<img src="${cdn}/dist/images/guide/zhanshi-4@2x.png" alt="" width="660px" />
	    			</div>
	    			<p class="introduce">汇盈金服的合作伙伴都是通过严格的筛选条件和严苛的风控标准，选择的在金融、法律、审计等领域深耕多年的优质企业。</p>
	    		</div>
	    	</div>
	    </div>
	    <div class="guide-box-5">
	    	<div class="core">
	    		<div class="title">
	    			<p class="word-2">信息安全技术</p>
	    			<div class="rule"></div>
	    			<p class="summary">全方位保护您的交易信息和用户信息</p>
	    		</div>
	    		<div class="zhanshi">
	    			<div class="img-box">
	    				<img src="${cdn}/dist/images/guide/zhanshi-5@2x.png" alt="" />
	    			</div>
	    		</div>
	    	</div>
	    </div>
	    <div class="guide-box-6">
	    	<div class="core">
		    		<div class="title">
		    			<p class="word-1">风控体系</p>
		    			<div class="rule"></div>
		    			<p class="summary">领先的风控管理体系与完善的管理机制</p>
		    		</div>
		    		<div class="zhanshi">
		    			<div class="img-box">
		    				<img src="${cdn}/dist/images/guide/zhanshi-6@2x.png" alt="" width="972px"/>
		    			</div>

		    		</div>
	    	</div>
	    </div>
	    <div class="guide-box-5">
	    	<div class="core">
	    		<div class="title">
	    			<p class="word-2">法大大电子合同存证</p>
	    			<div class="rule"></div>
	    			<p class="summary">专业的电子合同服务，让签约更高效</p>
	    		</div>
	    		<div class="zhanshi">
	    			<div class="img-box">
	    				<img src="${cdn}/dist/images/guide/zhanshi-9@2x.png?version=1" alt="" width="965px"/>
	    			</div>
	    		</div>
	    	</div>
	    </div>
	    <div class="guide-box-6">
	    	<div class="core">
	    		<div class="title">
	    			<p class="word-1">CFCA认证</p>
	    			<div class="rule"></div>
	    			<p class="summary">中国金融认证中心电子签章服务，有效保护您的出借安全</p>
	    		</div>
	    		<div class="zhanshi">
	    			<div class="img-box">
	    				<img src="${cdn}/dist/images/guide/zhanshi-7@2x.png" alt="" width="689px"/>
	    			</div>
	    		</div>
	    	</div>
	    </div>
	    <div class="guide-box-5">
	    		<div class="core">
		    		<div class="title">
		    			<p class="word-2">产品介绍</p>
		    			<div class="rule"></div>
		    			<p class="summary">动动手指，轻松出借</p>
		    		</div>
		    		<div class="zhanshi">
		    			<div class="img-box">
		    				<img src="${cdn}/dist/images/guide/zhanshi-8@2x.png?version=1" alt="" width="812px"/>
		    			</div>
		    			<p class="introduce">汇盈金服平台根据用户的需求，提供多元化的产品供用户自主选择；产品历史年回报率最高达10.50%，稳健的历史回报，为您的财富增值提供源源不断的动力。</p>
		    		</div>
		    	</div>
	    </div>
	    <div class="guide-box-7">
	    	<div class="core">
	    		<div class="title">
	    			<p class="word-1">轻松五步完成出借</p>
	    			<div class="rule"></div>
	    			<p class="summary">出借就是如此简单</p>
	    		</div>
	    		<div class="zhanshi">
	    			<div class="buzhou">
	    				<a class="bz-btn-1 now">
	    					<span class="num">1</span>
	    					<span class="bz-word">注册</span>
	    				</a>
	    				<a class="bz-btn-2 left-1">
	    					<span class="num">2</span>
	    					<span class="bz-word">银行开户</span>
	    				</a>
	    				<a class="bz-btn-2 left-2">
	    					<span class="num">3</span>
	    					<span class="bz-word">充值</span>
	    				</a>
	    				<a class="bz-btn-2 left-3">
	    					<span class="num">4</span>
	    					<span class="bz-word">出借</span>
	    				</a>
	    				<a class="bz-btn-3">
	    					<span class="num">5</span>
	    					<span class="bz-word">获取回报</span>
	    				</a>
	    			</div>
	    			<div class="img-box">
	    				<img src="${cdn}/dist/images/guide/buzhou-img-1@2x.png?v=20171123" alt="" class="show"/>
	    				<img src="${cdn}/dist/images/guide/buzhou-img-2@2x.png?v=20171123" alt="" />
	    				<img src="${cdn}/dist/images/guide/buzhou-img-3@2x.png?v=20171123" alt="" />
	    				<img src="${cdn}/dist/images/guide/buzhou-img-4@2x.png?v=20171123" alt="" />
	    				<img src="${cdn}/dist/images/guide/buzhou-img-5@2x.png?v=20171123" alt="" />
	    			</div>
	    			<c:if test="${isLogin eq 0}">
	    				<div class="btn-box"><a href='${ctx}/user/regist/init.do'>立即注册</a></div>
	    			</c:if>
	    		</div>
	    	</div>
	    </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById("indexNew");</script>
	<script>
    	$('.guide-box-7 .buzhou a').each(function(index,el){
    		$(el).click(function(){
    			$('.guide-box-7 .buzhou a').removeClass('now')
    			$(this).addClass('now')
    			$('.guide-box-7 .zhanshi .img-box img').removeClass('show')
    			$('.guide-box-7 .zhanshi .img-box img').eq(index).addClass('show')
    		})
    	})
    </script>
</body>
</html>
