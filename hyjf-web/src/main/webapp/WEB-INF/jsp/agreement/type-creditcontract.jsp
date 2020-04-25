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
	<article class="main-content">
        <div class="container">
            <section class="about-detial creditcontract content">
                <div class="main-title">
                   	 汇盈金服互联网金融服务平台债权转让协议
                    <c:if test="${creditResult.data.borrow.borrowNid != null && creditResult.data.borrow.borrowNid != '' }">
                    	 <a class="download-txt" href="${ctx}/createAgreementPDF/creditTransferAgreementPDF.do?bidNid=${creditResult.data.borrow.borrowNid}&creditNid=${creditResult.data.creditTender.creditNid}&creditTenderNid=${creditResult.data.creditTender.creditTenderNid}&assignNid=${creditResult.data.creditTender.assignNid}" target="blank">下载协议</a>
                    </c:if>
                </div>
                <div class="detial-list">
                    <div style="margin: 10px 0;">
						编号：
                    </div>
                    <div style="margin: 10px 0;">
                       	签署日期： 
                    </div>
                    <div class="t20" style="margin-top: 30px;">
                    <p>甲方（转让人）：</p>
                   	<p> 证件号码：</p>
                   	</div>
                   	<div class="t20" style="margin-top: 30px;">
                    <p>乙方（受让人）：</p>
                    <p> 证件号码：</p>
                   	</div>
                   	<p>鉴于甲方在惠众商务顾问（北京）有限公司运营管理的汇盈金服平台网站www.hyjf.com（以下简称“汇盈金服平台”，指惠众商务顾问（北京）有限公司及汇盈金服网站的统称）上对借款人拥有合法债权，现甲方将其债权通过汇盈金服平台转让给乙方。双方达成如下协议：</p>
					<div style="margin-bottom: 20px;">
                    	<h3>第一条&nbsp;主要内容</h3>
                    	<p>1.1债权转让标的</p>
                    	<p>甲方同意将其通过汇盈金服平台形成的民间借贷债权转让给乙方，乙方同意受让该债权，转让标的信息如下：</p>
                    	<ul class="agreement-bg">
	                        <li>
	                            <table style="width:50%" class="table-name" border="0" cellspacing="0" cellpadding="0">
	                                <tbody>
	                                    <tr>
	                                    	<td style="width:10%">1</td>
	                                    	<td style="width:25%">标的ID</td>
	                                    	<td style="width:55%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	                                    </tr>
	                                    <tr>
	                                    	<td>2</td>
	                                    	<td>借款人用户名</td>
	                                    	<td></td>
	                                    </tr>
	                                    <tr>
	                                    	<td>3</td>
	                                    	<td>借款本金总额</td>
	                                    	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人民币&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;元</td>
	                                    </tr>
	                                    <tr>
	                                    	<td>4</td>
	                                    	<td>借款利率</td>
	                                    	<td></td>
	                                    </tr>
	                                    <tr>
	                                    	<td>5</td>
	                                    	<td>还款方式</td>
	                                    	<td></td>
	                                    </tr>
	                                    <tr>
	                                    	<td>6</td>
	                                    	<td>借款期限</td>
	                                    	<td></td>
	                                    </tr>
	                                    <tr>
	                                    	<td>7</td>
	                                    	<td>转让债权本金</td>
	                                    	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人民币&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;元</td>
	                                    </tr>
	                                    <tr>
	                                    	<td>8</td>
	                                    	<td>转让价款</td>
	                                    	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人民币&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;元</td>
	                                    </tr>
	                                    <tr>
	                                    	<td>9</td>
	                                    	<td>转让剩余期限</td>
	                                    	<td></td>
	                                    </tr>
	                                    <tr>
	                                    	<td>10</td>
	                                    	<td>转让日期</td>
	                                    	<td></td>
	                                    </tr>
	                                </tbody>
	                            </table>
	                        </li>
                    	</ul>
                   	</div>
                   	<p>1.2转让规则</p>
                   	<p>1.2.1甲、乙双方同意并确认，双方通过自行或授权有关方根据汇盈金服平台相关的规则和说明，在汇盈金服平台进行债权转让和受让购买操作等方式来确认签署本协议。</p>
                   	<p>1.2.2 双方同意，本协议通过汇盈金服平台审核通过时，本协议成立，在乙方转让价款支付完成时生效。协议成立的同时，甲方不可撤销地授权汇盈金服平台代委托第三方存管银行将债权标的转让价款在扣除甲方应支付给汇盈金服平台的相关款项后（包括但不限于转让服务费）划转给甲方，上述债权标的转让价款划转完成即视为本协议生效且债权标的转让成功。</p>
                   	<p>1.2.3乙方受让甲方债权后有权再转让给第三人。</p>
                   	<p>1.2.4 本协议生效且债权标的转让成功后，汇盈金服平台将通过以下方式通知借款人（包括但不限于网站公告、电子邮件、短信提醒等）。</p>
                   	<p>1.2.5甲方同意，乙方有权获得受让的甲方债权信息；甲方同意委托汇盈金服平台将本协议推送给乙方。</p>
                   	<div style="margin-bottom: 20px;">
	                   	<h3>第二条&nbsp;保证和承诺</h3>
	                   	<p>2.1 乙方保证其支付受让标的债权的资金来源合法，乙方是该资金的合法所有人，如果第三方对资金归属、合法性问题发生争议，由乙方自行负责解决。</p>
	                   	<p>2.2 甲乙双方签署本协议并办理相关手续均为各自真实意思表示并获得相关授权。</p>
                   	</div>
                   	<div style="margin-bottom: 20px;">
                   		<h3>第三条&nbsp;适用法律及争议解决</h3>
                   		<p>3.1本协议的订立、效力、解释、履行、修改和终止以及争议的解决适用中国的法律。如果本协议中的任何一条或多条违反适用的法律法规，则该条将被视为无效，但该无效条款并不影响本协议其他条款的效力。</p>
                   		<p>3.2 本协议在履行过程中，如发生任何争执或纠纷，双方应友好协商解决；若协商不成，任何一方均有权向汇盈金服平台所在地上海市长宁区人民法院提起诉讼。</p>
                   	</div>
                   	<div style="margin-bottom: 20px;">
                   		<h3>第四条&nbsp;违约责任</h3>
                   		<p>4.1双方同意，如果一方违反其在本协议中所作的保证、承诺或任何其他义务，致使其他方遭受或发生损害、损失等责任，违约方须向守约方赔偿守约方因此遭受的一切经济损失。</p>
                   		<p>4.2 双方均有过错的，应根据双方实际过错程度，分别承担各自的违约责任。</p>
                   	</div>
                	<div style="margin-bottom: 20px;">
                   		<h3>第五条&nbsp;其他</h3>
                   		<p>5.1 甲、乙双方均同意并确认，甲、乙双方通过汇盈金服平台账户和其银行账户所作的行为均通过第三方存管银行进行，所产生的法律效果及法律责任归属于甲方、乙方。</p>
                   		<p>5.2 本协议项下的附件和补充协议构成本协议不可分割的一部分，本协议项下无约定事项的，以汇盈金服平台公布的相关规则为准。</p>
                   		<p>甲方、乙方声明：对于本协议条款，汇盈金服平台已应甲方、乙方的要求进行充分解释及说明，甲方、乙方对本协议内容及相应风险已完全知悉并理解。</p>
                   		<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;（以下无正文）</p>
                   	</div>
					<div style="margin-bottom: 20px;">
						<p>甲方（转让人）：</p>
						<p>乙方（受让人）：</p>
					</div>
				</div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>