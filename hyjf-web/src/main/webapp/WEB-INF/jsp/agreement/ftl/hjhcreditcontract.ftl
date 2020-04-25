<html>
<head>
    <style>
	    /*news-content*/
	    body, html {padding: 0; margin: 0;}
	    * {font-family: NSimSun;}
	    u, ins {text-decoration: none;}
	    .new-contract {position: relative; overflow: hidden; width: 700px; margin: 0 auto; background-color: #fff;}
	    .new-contract h2 {font-weight: 400; text-align: center; font-size: 22px; color: #000000;}
	    .new-contract-content {font-size: 15px; line-height: 1; word-wrap: break-word; word-break:break-strict; color: #000000;}
	    .new-contract-content img {margin: 15px auto; display: block; max-width: 100%; height: auto;}
	    .new-contract-content p {word-wrap: break-word; line-height: 1.5; font-size: 15px; font-weight: normal}
		.new-contract-content table{border-right:1px solid #ddd;border-bottom:1px solid #ddd} 
		.new-contract-content table td{border-left:1px solid #ddd;border-top:1px solid #ddd} 
    </style>
</head>

<body>
    <div class="new-contract">
        <h2>汇盈金服互联网金融服务平台债权转让协议</h2>
        <hr />
        <div class="new-contract-content">
            <div>
               <div style="margin: 10px 0;">
                   	编号：${creditTender.assignOrderId}
                </div>
                <div style="margin: 10px 0;">
                   	签署日期：${tenderToCreditDetail.signTime?substring(0,10)}
                </div>
                <div class="t20" style="margin-top: 30px;">
	                <p>甲方（转让人）：${usersInfoCredit.truename}</p>
	               	<p> 证件号码：${usersInfoCredit.idcard}</p>
               	</div>
               	<div class="t20" style="margin-top: 30px;">
	                <p>乙方（受让人）：${usersInfo.truename}</p>
	                <p> 证件号码：${usersInfo.idcard}</p>
               	</div>
               	<p>鉴于甲方在惠众商务顾问（北京）有限公司运营管理的汇盈金服平台网站www.hyjf.com（以下简称“汇盈
				金服平台”,指惠众商务顾问（北京）有限公司及汇盈金服网站的统称）上对借款人拥有合法债权，现甲方将其
				债权通过汇盈金服转让给乙方。双方达成如下协议：</p>
				<div style="margin-bottom: 20px;">
                	<h3>第一条 主要内容</h3>
                	<p>1.1 债权转让标的</p>
                	<p>甲方同意将其通过汇盈金服平台形成的民间借贷债权转让给乙方，乙方同意受让该债权，转让标的信息如
					下：</p>
                	<div class="agreement-bg" style="line-height:20px;text-align:center">
                        <table style="width:60%" class="table-name" border="0" cellspacing="0" cellpadding="0">
                            <tbody>
                                <tr>
                                	<td style="width:10%">1</td>
                                	<td style="width:27%">标的ID</td>
                                	<td style="width:53%">${borrow.borrowNid}</td>
                                </tr>
                                <tr>
                                	<td>2</td>
                                	<td>借款人用户名：</td>
                                	<td>${usersBorrow.username}</td>
                                </tr>
                                <tr>
                                	<td>3</td>
                                	<td>借款本金总额</td>
                                	<td>人民币${borrow.account}          元</td>
                                </tr>
                                <tr>
                                	<td>4</td>
                                	<td>借款利率</td>
                                	<td>${borrow.borrowApr}%</td>
                                </tr>
                                <tr>
                                	<td>5</td>
                                	<td>还款方式</td>
                                	<td>
									 <#if borrow.borrowStyle=='month'>
									等额本息
									</#if>
									<#if borrow.borrowStyle=='principal'>
									等额本金
									</#if>
									<#if borrow.borrowStyle=='end'>
									按月计息，到期还本还息
									</#if>
									<#if borrow.borrowStyle='endday'>
									按天计息，到期还本还息
									</#if>
									<#if borrow.borrowStyle=='endmonth'>
									先息后本
									</#if>
									</td>
                                </tr>
                                <tr>
                                	<td>6</td>
                                	<td>借款期限</td>
                                	<td>
                                	${borrow.borrowPeriod}
                                	<#if borrow.borrowStyle='endday'>
                                	天， 
                                	</#if>
                                	<#if borrow.borrowStyle!='endday'>
                                	个月， 
                                	</#if>
                                	${borrow.reverifyTime} 起，至 ${borrow.repayLastTime} 止</td>
                                </tr>
                                <tr>
                                	<td>7</td>
                                	<td>转让债权本金</td>
                                	<td>人民币${creditTender.assignCapital}          元</td>
                                </tr>
                                <tr>
                                	<td>8</td>
                                	<td>转让价款</td>
                                	<td>人民币${creditTender.assignPrice}          元</td>
                                </tr>
                                <tr>
                                	<td>9</td>
                                	<td>转让剩余期限</td>
                                	<td>${tenderToCreditDetail.creditTerm}天， ${tenderToCreditDetail.creditTime} 起，至 ${tenderToCreditDetail.creditRepayEndTime} 止</td>
                                </tr>
                                <tr>
                                	<td>10</td>
                                	<td>转让日期</td>
                                	<td>${tenderToCreditDetail.creditTime}</td>
                                </tr>
                            </tbody>
                        </table>
                	</div>
               	</div>
               	<p>1.2 转让规则</p>
               	<p>1.2.1 甲、乙双方同意并确认，双方通过自行或授权有关方根据汇盈金服平台相关的规则和说明，在汇盈
				金服平台进行债权转让和受让购买操作等方式来确认签署本协议.</p>
               	<p>1.2.2双方同意，本协议通过汇盈金服平台审核通过时，本协议成立，在乙方转让价款支付完成时生效.协
				议成立的同时，甲方不可撤销地授权汇盈金服平台代委托第三方存管银行将债权标的转让价款在扣除甲方应
				支付给汇盈金服平台的相关款项后（包括但不限于转让服务费）划转给甲方，上述债权标的转让价款划转完
				成即视为本协议生效且债权标的转让成功.</p>
               	<p>1.2.3 乙方受让甲方债权后有权再转让给第三人.</p>
               	<p>1.2.4 本协议生效且债权标的转让成功后，汇盈金服平台将通过以下方式通知借款人（包括但不限于网站公告、
				电子邮件、短信提醒等）.</p>
               	<p>1.2.5甲方同意，乙方有权获得受让甲方的债权信息；甲方同意委托汇盈金服将本协议推送给乙方.</p>
               	<div style="margin-bottom: 20px;">
                   	<h3>第二条 保证和承诺</h3>
                   	<p>2.1 乙方保证其支付受让标的债权的资金来源合法，乙方是该资金的合法所有人，如果第三方对资金归属、合
					法性问题发生争议，由乙方自行负责解决.</p>
                   	<p>2.2 甲乙双方签署本协议并办理相关手续均为各自真实意思表示并获得相关授权.</p>
               	</div>
               	<div style="margin-bottom: 20px;">
               		<h3>第三条 适用法律及争议解决</h3>
               		<p>3.1本协议的订立、效力、解释、履行、修改和终止以及争议的解决适用中国的法律.如果本协议中的任何
					一条或多条违反适用的法律法规，则该条将被视为无效，但该无效条款并不影响本协议其他条款的效力.</p>
               		<p>3.2 本协议在履行过程中，如发生任何争执或纠纷，双方应友好协商解决；若协商不成，任何一方均有权向汇
					盈金服平台所在地上海市长宁区人民法院提起诉讼.</p>
               	</div>
               	<div style="margin-bottom: 20px;">
               		<h3>第四条 违约责任</h3>
               		<p>4.1双方同意，如果一方违反其在本协议中所作的保证、承诺或任何其他义务，致使其他方遭受或发生损害、
					损失等责任，违约方须向守约方赔偿守约方因此遭受的一切经济损失.</p>
               		<p>4.2 双方均有过错的，应根据双方实际过错程度，分别承担各自的违约责任.</p>
               	</div>
            	<div style="margin-bottom: 20px;">
               		<h3>第五条 其他</h3>
               		<p>5.1 甲、乙方均同意并确认，甲、乙方通过汇盈金服平台账户和其银行账户的行为均通过第三方存管银行进行，
					所产生的法律效果及法律责任归属于甲、乙方.</p>
               		<p>5.2 本协议项下的附件和补充协议构成本协议不可分割的一部分，本协议项下无约定的事项以汇盈金服平台公
					布的相关规则为准.</p>
               		<p>甲、乙方声明：对于本协议条款，汇盈金服平台已应甲、乙方要求进行充分解释及说明，甲、乙方对本协
					议内容及相应风险已完全知悉并理解.</p>
               	</div>
               	<div style="margin-bottom: 20px;">
						<p>甲方（转让人）：</p>
						<p>乙方（受让人）：</p>
				</div>
            </div>
            
            <div>              
				<img src="/img/gongzhang.gif"  style="float:right;margin-right:80px;"/>
            </div>
        </div>
    </div>
</body>
</html>