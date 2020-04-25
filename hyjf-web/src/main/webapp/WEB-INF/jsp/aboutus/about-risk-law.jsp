<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
	<article class="main-content">
        <div class="container">
            <section class="about-detial content">
                <div class="main-title">
                   <span class="title-head">法律法规</span>
                </div>
                <div class="detial-list">
                	<p style="line-height: 2.5;color: #ff5b29;">
                		<a class="highlight" href="http://www.court.gov.cn/zixun-xiangqing-15146.html" target="_blank">最高人民法院关于审理民间借贷案件适用法律若干问题的规定</a><br />
						<a class="highlight" href="http://www.cbrc.gov.cn/govView_37D312933F1A4CECBC18F9A96293F450.html" target="_blank">网络借贷信息中介机构业务活动管理暂行办法</a><br />
						<a class="highlight" href="http://www.cbrc.gov.cn/chinese/home/docDOC_ReadView/D81B52D3D20A49A99522C48FA8F1C752.html" target="_blank">P2P网络借贷风险专项整治工作实施方案</a><br />
						<a class="highlight" href="http://www.cbrc.gov.cn/chinese/home/docView/ADBA919718424013997B848840730EB1.html" target="_blank">网络借贷资金存管业务指引</a><br />
						<a class="highlight" href="http://www.cbrc.gov.cn/govView_C8D68D4C980A4410B9F4E21BA593B4F2.html" target="_blank">网络借贷信息中介机构业务活动信息披露指引</a><br />
                	</p>
                	<h4 style="margin-top: 40px;margin-bottom: 10px;">
                		汇盈金服平台提供居间撮合服务的合法性
                	</h4>
                	<p>
                		《合同法》第23章专门对“居间合同”作出规定，其第424条明确定义为：“居间合同是居间人向委托人报告订立合同的机会或者提供订立合同的媒介服务，委托人支付报酬的合同”。汇盈金服平台是合法设立的网络借贷信息中介平台，致力于为民间借贷业务提供优质高效的撮合服务，以促成借贷双方形成借贷关系，然后收取相关报酬。此种居间服务有着明确的法律依据。
                	</p>
                	<h4>
                		出借人及借款人之间的借贷关系的合法性
                	</h4>
                	<p>
                		《合同法》第196条规定：“借款合同是借款人向贷款人借款，到期返还借款并支付利息的合同”；根据《合同法》第十二章“借款合同”和《最高人民法院关于审理民间借贷案件适用法律若干问题的规定》，我国法律允许自然人等普通民事主体之间发生借贷关系，并允许投资方到期可以收回本金和符合法律规定的利息。出借人与借款人之间形成的借贷关系受到法律保护。
                	</p>
                	<h4 >
                		投资人通过汇盈金服平台获得的利息的合法性
                	</h4>
                	<p>
                		根据《最高人民法院关于审理民间借贷案件适用法律若干问题的规定》第二十六条：“借贷双方约定的利率未超过年利率24%，投资人请求借款人按照约定的利率支付利息的，人民法院应予支持。借贷双方约定的利率超过年利率36%，超过部分的利息约定无效。借款人请求投资人返还已支付的超过年利率36%部分的利息的，人民法院应予支持。”汇盈金服平台上出借人向借款人投资资金并按照约定利率收取利息，该利率未超过前述规定的上限，为合法利息收益，受到法律保护。
                	</p>
                	<h4>
                		电子合同的合法性
                	</h4>
                	<p>
                		根据《合同法》第11条的规定，当事人可以采用合同书、信件和数据电文（包括电报、电传、传真、电子数据交换和电子邮件）等形式订立合同。电子合同是法律认可的书面合同形式之一。汇盈金服采取用户网上点击确认的方式签署电子合同。点击确认后的电子合同符合《中华人民共和国合同法》规定的合同成立、生效的要件，其有效性也被人民法院的司法实践所接受。
                	</p>
			    </div>
			    <div class="detial-btn"> 
                    <a href="${ctx}/aboutus/fxrisk.do">返回列表<i class="xiayibu"></i></a>
			    </div>
            </section>
        </div>
    </article>

	<jsp:include page="/footer.jsp"></jsp:include>
	<!-- 设置定位  -->
	<script>setActById("aboutInformation");</script>
	<!-- 导航栏定位  -->
	<script>setActById("indexMessage");</script>
</body>
</html>