<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <title>银行存管</title>
    <script src="${ctx}/js/zepto.min.js"></script>
    <script>
    	function setRemRoot(){
    		var w  = window.innerWidth;
	    	var htmlEle = document.querySelector("html");
	    	htmlEle.setAttribute("style","font-size:"+w/103.5+"px");  //宽度1242时，font-size是12；1242/12 = 103.5
    	}
    	window.onresize = function(){
    		setRemRoot();
    	}
    	setRemRoot();
    </script>
    <style>
    body{
    	padding: 0;
    	margin: 0;
    }
    .yhcg-banner img{
    	width: 100%;
    }
    .yhcg-container {
        width: 100%;
        padding: 0 10%;
        box-sizing: border-box;
        overflow: hidden;
    }
    
    .yhcg-1 .main-title {
        color: #1d3068;
        font-size: 3rem;
        text-align: center;
        margin-top: 3.3333rem;
        font-weight: bold;
    }
    
    .yhcg-1 .content {
        font-size: 2.5rem;
        color: #2f2f2f;
    }
    
    .yhcg-2 {
        padding-bottom: 5.833rem;
    }
    
    .yhcg-2 .main-title {
        font-size: 4rem;
        text-align: center;
        color: #2f2f2f;
        margin-top: 3.3333rem;
        font-weight: bold;
        background: url(${ctx}/images/yhcg/title_line.png) center center no-repeat;
        -webkit-background-size: cover;
        background-size: cover;
    }
    .yhcg-2 .content{
        float:left;
        margin-top: 6.666rem;
        width: 100%;
    }
    .yhcg-2 .content img.yhcg-flow {
        width: 100%;
    }
    .yhcg-3 {
        padding-bottom: 5.833rem;
    }
    
    .yhcg-3 .main-title {
        font-size: 4rem;
        text-align: center;
        color: #2f2f2f;
        margin-top: 3.3333rem;
        font-weight: bold;
        background: url(${ctx}/images/yhcg/title_line.png) center center no-repeat;
        -webkit-background-size: cover;
        background-size: cover;
    }
    .yhcg-3 .content{
        float:left;
        margin-top: 2.5rem;
        width: 100%;
    }
    .yhcg-3 ul {
        display: block;
        width: 100%;
        margin: 0;
        padding: 0;
        float: left;
    }
    
    .yhcg-3 ul li {
        text-decoration: none;
        display: block;
        border-radius: 1rem;
        background: #1d3068;
        padding: 2.5rem 10%;
        box-sizing: border-box;
        margin-bottom: 1.25rem;
        color: #fff;
        float: left;
    }
    
    .yhcg-3 ul li .title {
        font-size: 4rem;
        float:left;
        padding: 0 0 1rem 0;
        font-weight: bold;
        width: 100%;
        white-space:nowrap;
    }
    .yhcg-3 ul li .line{
		border-bottom: 2px solid #c9c9c9;
		width: 64%;
		float: left;
    }
    
    .yhcg-3 ul li .content {
        font-size: 2.5rem;
        width: 100%;
        float: left;
    }
    .yhcg-3 .icon{
    	display: inline-block;
    	vertical-align: middle;
    }
    .yhcg-3 .icon img{
    	display: inline-block;
    	vertical-align: middle;
    	width: 100%;
    }
    .yhcg-3 .icon1{
    	width: 13.5%;
    }
    .yhcg-3 .icon2{
    	width: 11.2%;
    }
    .yhcg-3 .icon3{
    	width: 10.7%;
    }
    .yhcg-4 {
        padding-bottom: 4.166rem;
        background: #f9f9f9 url(${ctx}/images/yhcg/yhcg_4bg.jpg) right center no-repeat;
        background-size: contain;
    }
    
    .yhcg-4 .title {
        color: #1d3068;
        font-size: 4rem;
        margin-top: 6.666rem;
        font-weight: bold;
    }
    
    .yhcg-4 .content {
        color: #2f2f2f;
        font-size: 3rem;
        margin-bottom: 4.166rem;
    }
    
    .yhcg-5 .main-title {
        color: #1d3068;
        font-size: 4rem;
        margin-top: 6.666rem;
        text-align: center;
        font-weight: bold;
    }
    
    .yhcg-5 .title {
        color: #1d3068;
        font-size: 3rem;
        font-weight: bold;
    }
    
    .yhcg-5 .content {
        color: #404040;
        font-size: 3rem;
        margin-bottom: 6.666rem;
    }
    .yhcg-5 ul{
    	display: block;
    	margin: 0;
    	padding: 0;
    }
    .yhcg-5 ul li{
    	text-decoration:none;
    	display: block;
    	margin: 0;
    	padding: 0;
    }
    .yhjf-6 table {
        border: 2px solid #fff;
        font-size: 1.5rem;
        width: 100%;
    }
    
    .yhjf-6 table th {
        border: 2px solid #fff;
        background: url(${ctx}/images/yhcg/th_bg.jpg);
        -webkit-background-size: 100% 100%;
        background-size: 100% 100%;
        color: #fff;
        text-align: center;
        padding: 2px;
    }
    
    .yhjf-6 table td {
        border: 2px solid #fff;
        color: #000;
        background: #f6f6f6;
        text-align: center;
        padding: 2px;
    }
    
    .yhjf-6 table tr:nth-child(even) td {
        background: #e3e3e3;
    }
    
    .yhcg-7 {
        font-size: 2.5rem;
        color: #000;
        padding-top: 4.166rem;
        padding-bottom: 7.5rem;
    }
    .yhjf-6 table th.table-title{
    	color: #ffba00;
    }
    </style>
</head>
<body>
    <div class="wrapper">
        <div class="yhcg-banner"><img src="${ctx}/images/yhcg/yhcg_banner.jpg?v=20171123" alt=""></div>
        <div class="yhcg-1">
            <div class="yhcg-container">
                <div class="main-title">安全 · 合规 · 透明</div>
                <div class="content">为符合监管政策要求，汇盈金服较早的与江西银行签署了银行存管服务协议，经过技术部门数月缜密的存管系统对接，汇盈金服于2017年7月5日正式上线江西银行资金存管业务系统。
                    <br/>由安全的资金托管升级为更加安全的资金存管，进一步保障用户的资金安全。</div>
            </div>
        </div>
        <div class="yhcg-2">
            <div class="yhcg-container">
                <div class="main-title">银行存管的模式</div>
                <div class="content">
                    <img src="${ctx}/images/yhcg/yhcg_flow.jpg?v=20171123" alt="" class="yhcg-flow">
                </div>
            </div>
        </div>
        <div class="yhcg-3">
            <div class="yhcg-container">
                <div class="main-title">银行存管的优势</div>
                <div class="content">
                    <ul>
                        <li>
                            <div class="title">资金安全隔离 <div class="icon icon1"><img src="${ctx}/images/yhcg/icon1.png" alt=""></div></div>
                            <div class="line"></div>
                            <div class="content">用户资金与平台资金完全隔离，分账户管理模式，平台无法碰触用户资金。</div>
                        </li>
                        <li>
                            <div class="title">交易信息透明 <div class="icon icon2"><img src="${ctx}/images/yhcg/icon2.png" alt=""></div></div>
                            <div class="line"></div>
                            <div class="content">资金操作均在银行账户体系内完成，资金走向清晰可查。</div>
                        </li>
                        <li>
                            <div class="title">用户授权交易 <div class="icon icon3"><img src="${ctx}/images/yhcg/icon3.png" alt=""></div></div>
                            <div class="line"></div>
                            <div class="content">所有涉及资金变动的操作，均需用户在银行页面输入交易密码授权后，银行方面才会进行相应的资金操作。</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="yhcg-4">
            <div class="yhcg-container">
                <div class="title">为什么选择银行存管？</div>
                <div class="content">
                    符合2016年8月24日由银监会、工信部、公安部、网信办联合公布的《网络借贷信息中介机构业务活动管理暂行办法》要求，做到合规、自律、健康的发展，进一步保障用户资金安全，提升平台安全等级。
                </div>
                <div class="title">什么是银行存管？</div>
                <div class="content">银行存管是由银行管理资金，平台管理交易，做到资金与交易的分离，使得平台无法直接接触资金，避免客户资金被直接挪用。</div>
            </div>
        </div>
        <div class="yhcg-5">
            <div class="yhcg-container">
                <div class="main-title">[常见问题解决]</div>
                <ul>
                    <li>
                        <div class="title">银行存管上线后，对用户体验上有何影响？</div>
                        <div class="content">
                            新注册用户需开通银行存管账户并绑定本人银行卡，老用户需要激活（绑卡并设置银行交易密码）后，才可进行正常的充值、投资、提现等操作。
                        </div>
                    </li>
                    <li>
                        <div class="title">银行存管上线后，
                            <br/> 对原汇付天下托管账户的账户余额和债权关系有何影响？</div>
                        <div class="content">银行存管上线后，老用户汇付天下的账户余额不可再进行投资操作，只能进行提现操作（在“我的账户”根据提示进行操作），原债权关系在还款时，会还款至用户在江西银行的存管账户，此资金可进行投资、提现等操作。</div>
                    </li>
                    <li>
                        <div class="title">银行存管上线后，对提现的影响？</div>
                        <div class="content">（1）工商银行/中国银行：提现5万（含）以下时，实时到账；提现5万以上时，需要填写开户银行网点的联行号，一般30分钟内到账，最晚T+1到账；
                            <br/> （2）其它银行：提现20万（含）以下时，实时到账；提现20万以上时，需要填写开户银行网点的联行号，一般30分钟内到账，最晚T+1到账；
                        </div>
                    </li>
                    <li>
                        <div class="title">银行存管上线后，对充值的影响？</div>
                        <div class="content">（1）新增线下转账的充值方式，可通过支付宝/网银转账等方式，充值到自己在江西银行开立的虚拟账户中；
                            <br/> （2）快捷充值支持的银行卡变更，支持的银行卡如下：
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div class="yhjf-6">
            <div class="yhcg-container">
                <table cellspacing="0" cellpadding="0">
                    <thead>
                        <tr>
                            <th colspan="4" class="table-title">以下银行支持快捷支付</th>
                        </tr>
                        <tr>
                            <th width="25%">序号</th>
                            <th width="25%">支持银行</th>
                            <th width="25%">单日支持限额</th>
                            <th width="25%">日累计限额</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>工行</td>
                            <td>5万</td>
                            <td>5万</td>
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>农行</td>
                            <td>10万</td>
                            <td>10万</td>
                        </tr>
                        <tr>
                            <td>3</td>
                            <td>建行</td>
                            <td>10万</td>
                            <td>10万</td>
                        </tr>
                        <tr>
                            <td>4</td>
                            <td>招行</td>
                            <td>5万</td>
                            <td>5万</td>
                        </tr>
                        <tr>
                            <td>5</td>
                            <td>兴业</td>
                            <td>10万</td>
                            <td>10万</td>
                        </tr>
                        <tr>
                            <td>6</td>
                            <td>光大</td>
                            <td>10万</td>
                            <td>10万</td>
                        </tr>
                        <tr>
                            <td>7</td>
                            <td>中信</td>
                            <td>0.5万</td>
                            <td>1万</td>
                        </tr>
                        <tr>
                            <td>8</td>
                            <td>平安</td>
                            <td>10万</td>
                            <td>10万</td>
                        </tr>
                        <tr>
                            <td>9</td>
                            <td>民生</td>
                            <td>10万</td>
                            <td>10万</td>
                        </tr>
                        <tr>
                            <td>10</td>
                            <td>广发</td>
                            <td>10万</td>
                            <td>10万</td>
                        </tr>
                        <tr>
                            <td>11</td>
                            <td>浦发</td>
                            <td>5万</td>
                            <td>5万</td>
                        </tr>
                        <tr>
                            <td>12</td>
                            <td>交行</td>
                            <td>10万</td>
                            <td>10万</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="yhcg-7">
            <div class="yhcg-container">
                如果以上解答无法解决您的问题，请联系在线客服或拨打<strong>400-900-7878</strong>
            </div>
        </div>
    </div>
</body>
</html>