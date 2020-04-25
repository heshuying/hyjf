<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<style type="text/css">
	footer#footer {
		background: #f7f7f7;
	}
</style>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
    <article class="" style="padding-top: 0;">
        <!-- start 内容区域 -->  
        <div class="bank-money">
            <div class="banner"></div>
            <div class="">
                <div class="bank-depository">
                    <div class="title">
                       安全
                       <strong class="circle">·</strong>
                       合规<strong class="circle">·</strong>透明
                    </div>
                    <div class="dep-introdiv">
                        <p class="dep-intro">为符合监管政策要求，汇盈金服较早的与江西银行签署了银行存管服务协议，经过技术部门数月缜密的存管</p>
                        <p class="dep-intro">系统对接，汇盈金服于2017年7月5日正式上线江西银行资金存管业务系统。</p>
                        <p class="dep-intro">由安全的资金托管升级为更加安全的资金存管，进一步保障用户的资金安全。</p>
                    </div>
                    <div class="dep-bigtitle">
                        <span class="cross-line"></span>
                        <span class="dep-h">银行存管的模式</span>
                        <span class="cross-line"></span>
                        <img class="bank-img" src="${cdn}/dist/images/bank-money/liucheng@2x.png?v=20171123">
                    </div>
                </div>
                <div class="good-control-box dep-bigtitle">
                    <span class="cross-line"></span>
                    <span class="dep-h">银行存管的优势</span>
                    <span class="cross-line"></span>
                    <div class="good-data-div">
                        <div class="good-data">
                            <p class="data-title">资金安全隔离</p>
                            <img src="${cdn}/dist/images/bank-money/icon1@2x.png" width="106px">
                            <p class="data-desc">用户资金与平台资金完全隔离，分账户管理模式，平台无法碰触用户资金。</p>
                        </div>
                        <div class="good-data">
                            <p class="data-title">交易信息透明</p>
                            <img src="${cdn}/dist/images/bank-money/icon2@2x.png" width="76px">
                            <p class="data-desc">资金操作均在银行账户体系内完成，资金走向清晰可查。</p>
                        </div>
                        <div class="good-data">
                            <p class="data-title">用户授权交易</p>
                            <img src="${cdn}/dist/images/bank-money/icon3@2x.png" width="96px">
                            <p class="data-desc">所有涉及资金变动的操作，均需用户在银行页面输入交易密码授权后，银行方面才会进行相应的资金操作。</p>
                        </div>
                    </div>
                </div>
                <div class="reason-box">
                    <div class="contain">
                        <!-- <img src="../images/bank-money/bg.png"> -->
                        <p class="dep-h">为什么选择银行存管？</p>
                        <p class="dep-intro">符合2016年8月24日由银监会、工信部、公安部、网信办联合公布的《网络借贷信息中介机构业务活动管理</p>
                        <p class="dep-intro">暂行办法》要求，做到合规、自律、健康的发展，进一步保障用户资金安全，提升平台安全等级。</p>
                        <p class="dep-h">什么是银行存管？</p>
                        <p class="dep-intro">银行存管是由银行管理资金，平台管理交易，做到资金与交易的分离，使得平台无法直接接触资金，避免客</p>
                        <p class="dep-intro">户资金被挪用。</p>
                    </div>
                </div>
                <div class="problem-box">
                    <div class="pro-title">[常见问题解决]</div>
                    <div class="pro-box">
                        <span class="pro-order">1</span>
                        <span class="pro-big">银行存管上线后，对用户体验上有何影响？</span>
                        <p class="dep-intro">新注册用户需开通银行存管账户并绑定本人银行卡，老用户需要激活（绑卡并设置银行交易密码）后，才可进行正常的充值、出借、提现等操作。</p>
                    </div>
                    <div class="pro-box" style="margin-top: 90px;">
                        <span class="pro-order">2</span>
                        <span class="pro-big">银行存管上线后，对原汇付天下托管账户的账户余额和债权关系有何影响？</span>
                        <p class="dep-intro">银行存管上线后，老用户汇付天下的账户余额不可再进行出借操作，只能进行提现操作（在“我的账户”根据提示进行操作），原债权关系在还款时，会还款至用户在江西银行的存管账户，此资金可进行出借、提现等操作。</p>
                    </div>
                    <div class="pro-box" style="margin-top: 90px;">
                        <span class="pro-order">3</span>
                        <span class="pro-big">银行存管上线后，对提现的影响？</span>
                        <p class="dep-intro">（1）工商银行/中国银行：提现5万（含）以下时，实时到账；提现5万以上时，需要填写开户银行网点的联行号，一般30分钟内到账，最晚T+1到账；</p>
                         <p class="dep-intro">（2）其它银行：提现20万（含）以下时，实时到账；提现20万以上时，需要填写开户银行网点的联行号，一般30分钟内到账，最晚T+1到账；</p>
                    </div>
                    <div class="pro-box" style="margin-top: 90px;">
                        <span class="pro-order">4</span>
                        <span class="pro-big">银行存管上线后，对充值的影响？</span>
                        <p class="dep-intro">（1）新增线下转账的充值方式，可通过支付宝/网银转账等方式，充值到自己在江西银行开立的虚拟账户中；</p>
                         <p class="dep-intro">（2）快捷充值支持的银行卡变更，支持的银行卡如下：</p>
                    </div>
                </div>
                <div class="pro-table">
                    <div class="title">以下银行支持快捷支付</div>
                    <table>
                        <thead>
                            <tr>
                                <th>银行名称</th>
                                <th>单笔限额(元)</th>
                                <th>单日限额(元)</th>
                                <th>单月限额(元)</th>
                            </tr>
                        </thead>
                        <tbody id="ruleList">
                            
                        </tbody>
                    </table>
                    <div class="bom">如果以上解答无法解决您的问题，请联系在线客服或拨打
                    <strong>400-900-7878</strong>
                    </div>
                </div>
            </div>
        </div>          
        <!-- end 内容区域 -->            
    </article>
 	<jsp:include page="/footer.jsp"></jsp:include>
 	<script>
 	$.get("${ctx}/contentarticle/rechargeRule.do",function(res){
 		var list  = res.list;
 		listStr = '';
 		for( var i=0;i<list.length;i++){
 			listStr+="<tr><td>"+list[i].bankName+"</td>"+"<td>"+list[i].once+"</td>"+"<td>"+list[i].day+"</td>"+"<td>"+list[i].month+"</td></tr>"
 		}
 		$("#ruleList").html(listStr)
 	})
 	</script>
</body>
</html>