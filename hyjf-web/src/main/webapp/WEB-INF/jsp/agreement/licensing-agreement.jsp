<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<style>
        .top-bar,.nav-right,.toolbar,#footer,.nav-sub,.breadcrumbs{
            display: none;
        }
        .back-page{
            color: #ff5b29;
            float: right;
            font-size: 14px;
        }
        .main-content {
            min-height: 700px;
        }
    </style>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<jsp:include page="/bread.jsp"></jsp:include>
	<article class="main-content">
	    <div class="container">
	        <section class="about-detial creditcontract content">
	            <div class="main-title">
	                授权协议
	             
	            </div>
	            <div class="detial-list">
                <div>
                    <b>尊敬的用户：</b>
                    <p>
                        您好，根据您与惠众商务顾问（北京）有限公司运营平台汇盈金服（以下简称本网站）以电子合同签署的《注册协议》第5.2.3条，根据您的授权，本网站可将您必要的个人信息和资料（包括但不限于真实姓名、身份证号码、联系方式、信用状况等）披露给通过本网站与您交易的交易服务商及其他交易服务主体。范太克（上海）投资控股有限公司运营平台汇晶社为《注册协议》第5.2.3条约定的与汇盈金服平台合作的第三方交易服务商。
                    </p>
                    <p>
                        因此，在您同意授权的前提下，本网站在不透露您的隐私资料的前提下，向本网站的合作机构第三方交易服务商
                    </p>
                    <p>
                        （1）提供您的用户和投资信息
                    </p>
                    <p>
                        （2）由人工或自动程序对您信息进行评估、分类、研究
                    </p>
                    <p>
                        （3）使用您提供的联系方式与您联络并向您传递有关业务和管理方面的信息。本网站将不定期对第三方交易服务商使用您的用户信息进行抽查、核实以识别问题或解决争议以保护你的合法权益。
                    </p>
                    <p>
                        您只要勾选位于页面下方的“我同意授权服务协议”选项后，即视为您已经充分理解和同意本协议全部条款和内容。
                    </p>
                </div>
            </div>
	        </section>
	    </div>
	</article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${ctx}/dist/js/lib/jquery.validate.js"></script>
    <script src="${ctx}/dist/js/lib/jquery.slideunlock.min.js"></script>
	<script src="${ctx}/dist/js/rights-manage/transfer-details.js"></script>
</body>
</html>