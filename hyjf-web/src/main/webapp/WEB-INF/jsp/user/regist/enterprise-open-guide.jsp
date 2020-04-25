<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/head.jsp"></jsp:include>
<style type="text/css">
    	.enterprise-open-guide{
    		padding: 0;
    	}
    	.enterprise-banner{
    		height: 320px;
    		background:url(/dist/images/enterprise/banner@2x.png) no-repeat;
    		background-position: center center;
		    background-size:3000px 320px;
		    color: #fff;
    	}
    	.enterprise-banner .tit{
    		width: 1060px;
    		margin: 0 auto;
    		font-size: 50px;
    		padding-top:80px;
    		margin-bottom: 30px;
    		letter-spacing:4px
    	}
    	.enterprise-banner .word{
    		width: 1060px;
    		margin: 0 auto;
    		font-size: 20px;
			color: #FFFFFF;
			letter-spacing: 0;
			line-height: 30px;
			opacity: 0.6;
    	}
    	.enterprise-open-guide .title{
    		text-align: center;
    		font-size: 32px;
    		color: #0D4C9A;
			letter-spacing: 2px;
			line-height: 36px;
			margin: 70px auto 75px;
			position: relative;
    	}
    	.enterprise-open-guide .title em{
    		width:340px;
    		position: absolute;
    		top: 17px;
    		height: 1px;
    		line-height: 1px;
    		background:#979797 ;
    	}
    	.enterprise-open-guide .title em.r1{
    		left: 0;
    	}
    	.enterprise-open-guide .title em.r2{
    		right: 0;
    	}
    	.enterprise-open-guide .procedure{
    		background: #fff;
    		padding-top: 1px;
    	}
    	.enterprise-open-guide .procedure.gray{
    		background:#f8f8f8;
    	}
    	.enterprise-open-guide .procedure.gray .content{
    		background:#f8f8f8;
    	}
    	.enterprise-open-guide .procedure .content img{
    		display: block;
    		margin: 0 auto;
    		padding-bottom: 100px;
    		
    	}
    	.enterprise-open-guide .procedure .content{
    		width: 1060px;
    		margin: 0 auto;
    		padding: 0;
    	}
    	.enterprise-open-guide .procedure .content .data-box{
    		padding: 0 47px;
    		overflow: hidden;
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item{
    		height:127px ;
    		width: 219px;
    		display: block;
    		float: left;
    		background-size:219px 127px ;
    		font-size: 18px;
    		text-decoration: none;
    		color: #fff;
    		padding-top: 25px;
    		padding-left: 23px;
    		letter-spacing: 1px;
			line-height: 28px;
    	}
    	.enterprise-open-guide .procedure .content .data-box a+a{
    		margin-left: 30px;
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item.item1{
			background-image: url(/dist/images/enterprise/data1@2x.png);
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item.item2{
background-image: url(/dist/images/enterprise/data2@2x.png);
padding-top: 36px;
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item.item3{
background-image: url(/dist/images/enterprise/data3@2x.png);
padding-top: 36px;
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item.item4{
background-image: url(/dist/images/enterprise/data4@2x.png);
padding-top: 36px;
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item.item5{
background-image: url(/dist/images/enterprise/data5@2x.png);
padding-top: 36px;
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item.item6{
background-image: url(/dist/images/enterprise/data6@2x.png);
padding-top: 36px;
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item.item7{
background-image: url(/dist/images/enterprise/data7@2x.png);
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item.item8{
background-image: url(/dist/images/enterprise/data8@2x.png);
    	}
    	.enterprise-open-guide .procedure .content .data-box .data-item i{
    		background: url(/dist/images/enterprise/download@2x.png);
    		background-size: 20px 20px;
    		position: relative;
    		display: inline-block;
    		height: 20px;
    		width: 20px;
    		left: 6px;
    		top: 4px;
    	}
    	.enterprise-open-guide .procedure .content .email{
    		text-align: center;
    		font-size: 22px;
    		letter-spacing: 0;
    		margin-top: 70px;
    	}
    	.enterprise-open-guide .procedure .content .label{
    		text-align: center;
    		font-size: 18px;
    		color: #999999;
    		letter-spacing: 0;
    		margin-top: 20px;
    		padding-bottom: 80px;
    		margin-bottom: 0;
    	}
    </style>
<title>企业开户指南 - 汇盈金服官网</title>

</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="enterprise-open-guide main-content">
    	<div class="enterprise-banner">
    		<p class="tit">
    			企业开户指南
    			
    		</p>
    		<p class="word">
    			银行存管开通之后，会由银行管理资金，平台管理交易，<br />
				做到资金与交易的分离，使得平台无法直接接触资金，保障资金安全。
    		</p>
    	</div>
    	<div class="procedure">
    		<div class="content">
	    		<div class="title">
	    			<em class="r1"></em>
	    			企业开户流程
	    			<em  class="r2"></em>
	    		</div>
	    		<img src="/dist/images/enterprise/procedure@2x.png" alt="" width="985px" />
	    	</div>
    	</div>
    	<div class="procedure gray">
    		<div class="content">
	    		<div class="title">
	    			<em class="r1"></em>
	    			企业开户所需资料
	    			<em  class="r2"></em>
	    		</div>
	    		<div class="data-box">
	    			<a class="data-item item1">转账凭证<br>收款方电子账单</a>
	    			<a class="data-item item2">营业执照</a>
	    			<a class="data-item item3">开户许可证</a>
	    			<a class="data-item item4">法人身份证</a>
	    		</div>
	    		<div class="data-box">
	    			<a href="${webUrl}/data/download/申请表.pdf" class="data-item item5" target="_blank">申请表<i></i></a>
	    			<a href="${webUrl}/data/download/预留印鉴表.pdf" class="data-item item6" target="_blank">预留印鉴表<i></i></a>
	    			<a  class="data-item item7">企业公示信息<br>（工商）</a>
	    			<a  class="data-item item8">组织机构信用代码证<br>（非必须）</a>
	    		</div>
	    		<p class="email">请通过邮件形式把以上资料提交至kaihu@hyjf.com，稍后会有业务人员与您联系。</p>
	    		<p class="label">若开户过程中遇到问题，请联系您的业务员，或者拨打客服电话：400-900-7878。</p>
    		</div>
    	</div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>