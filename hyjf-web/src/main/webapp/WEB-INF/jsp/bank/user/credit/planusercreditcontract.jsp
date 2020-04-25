<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
<meta charset="UTF-8">
<title>汇盈金服互联网金融服务平台债权转让协议 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
<link rel="stylesheet" href="${cdn}/css/baguetteBox.min.css"
	type="text/css" />
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<section class="new-warp clf">
	<div class="inner">
		<div class="new-contract">
			<h2>汇盈金服互联网金融服务平台债权转让协议</h2>
			<hr>
			<a class="agreement-dw" href="${ctx}/credit/planusercreditcontractsealpdf.do?bidNid=${creditResult.data.borrow.borrowNid}&creditNid=${creditResult.data.creditTender.creditNid}&creditTenderNid=${creditResult.data.creditTender.assignOrderId}" target="blank">下载PDF版本</a>
			<div class="new-contract-content">
				<div>
					<p style="text-align: right;">合同编号：${creditResult.data.creditTender.assignOrderId}</p>
					<p>原债权人（转让方，以下简称甲方）：${creditResult.data.usersInfoCredit.truename}</p>
					<p>身份证（护照）号码：${creditResult.data.usersInfoCredit.idcard}</p>
					<p>汇盈金服用户名：${creditResult.data.usersCredit.username}</p>
					<br>
					<p>新债权人（受让方，以下简称乙方）： ${creditResult.data.usersInfo.truename} </p>
					<p>身份证（护照）号码：${creditResult.data.usersInfo.idcard}</p>
					<p>汇盈金服用户名：${creditResult.data.users.username}</p>
					<br>
					<p>鉴于甲方在惠众商务顾问（北京）有限公司运营管理的汇盈金服平台网站www.hyjf.com（以下简称“汇盈金服”，
					指惠众商务顾问（北京）有限公司及汇盈金服网站的统称）上对债务人 拥有合法债权现甲方将其债权通过汇盈金服转让给乙方双方达成如下协议：</p>
					<br>
					<p>	第一条 主要内容</p>
					<p>1.1债权转让标的：</p>
					<p>甲方同意将其通过汇盈金服平台形成的民间借贷债权（详见下表，下称“债权标的”）转让给乙方，乙方同意受让该债权</p>
					<p>债权标的信息</p>
					<table border="1">
					  <tr>
					    <td>标的ID</td>
					    <td>${creditResult.data.borrow.borrowNid} </td>
					  </tr>
					  <tr>
					    <td>融资方汇盈金服用户名</td>
					    <td>${creditResult.data.usersBorrow.username} </td>
					  </tr>
					  <tr>
					    <td>融资本金总额</td>
					    <td>${creditResult.data.borrow.account}元</td>
					  </tr>
					  <tr>
					    <td>历史年回报率</td>
					    <td>${creditResult.data.borrow.borrowApr}% </td>
					  </tr>
					  <tr>
					    <td>还款方式</td>
					    <td>
						   <c:if test="${creditResult.data.borrow.borrowStyle=='month'}">
							等额本息
							</c:if>
							<c:if test="${creditResult.data.borrow.borrowStyle=='principal'}">
							等额本金
							</c:if>
							<c:if test="${creditResult.data.borrow.borrowStyle=='end'}">
							按月计息，到期还本还息
							</c:if>
							<c:if test="${creditResult.data.borrow.borrowStyle=='endday'}">
							按天计息，到期还本还息
							</c:if>
							<c:if test="${creditResult.data.borrow.borrowStyle=='endmonth'}">
							先息后本
							</c:if>
					    </td>
					  </tr>
					  <tr>
					    <td>还款日</td>
					    <td>
					    自 ${creditResult.data.creditTender.updateUserName} 月起，每月  ${creditResult.data.creditTender.addip} 日（24:00前）
					     </td>
					  </tr>
					   <tr>
					    <td>融资期限</td>
					    <td>${creditResult.data.borrow.borrowPeriod}个月， ${creditResult.data.borrow.reverifyTime} 起，至 ${creditResult.data.borrow.repayLastTime} 止</td>
					  </tr>
					</table>
					
					<p>债权标的转让信息</p>
					<table border="1">
					  <tr>
					    <td>标的债权价值</td>
					    <td>￥${creditResult.data.creditTender.assignCapital} </td>
					  </tr>
					  <tr>
					    <td>转让价款</td>
					    <td>${creditResult.data.creditTender.assignPay} </td>
					  </tr>
					  <tr>
					    <td>转让手续费</td>
					    <td>￥${creditResult.data.creditTender.assignServiceFee}</td>
					  </tr>
					  <tr>
					    <td>转让剩余期限</td>
					    <td>${creditResult.data.tenderToCreditDetail.creditTerm}天， ${creditResult.data.tenderToCreditDetail.creditTime} 起，至 ${creditResult.data.tenderToCreditDetail.creditRepayEndTime} 止</td>
					  </tr>
					  <tr>
					    <td>转让日期</td>
					    <td>${creditResult.data.tenderToCreditDetail.creditTime}</td>
					  </tr>
					</table>
					<p>1.2转让规则</p>
					<p>1.2.1甲、乙双方同意并确认，双方通过自行或授权有关方根据汇盈金服平台相关的规则和说明，在汇盈金服平台进行债权转让和受让购买操作等方式来确认签署本协议</p>
					<p>1.2.2  双方同意，本协议通过汇盈金服平台审核通过时，本协议成立，在乙方转让价款支付完成时生效协议成立的同时，甲方不可撤销地授权汇盈金服平台贷委托第三方支付平台将债权标的转让价款在扣除甲方应支付给汇盈金服平台的相关款项后（包括但不限于转让管理费）划转给甲方，上述债权标的转让价款划转完成即视为本协议生效且债权标的转让成功</p>
					<p>1.2.3乙方受让甲方债权后不得再转让给第三人</p>
					<p>1.2.4  本协议生效且债权标的转让成功后，汇盈金服平台将通过以下方式通知融资方（包括但不限于网站公告、电子邮件、短信提醒等）</p>
					<p>第二条 保证和承诺</p>
					<p>2.1  乙方保证其支付受让标的债权的资金来源合法，乙方是该资金的合法所
						有人，如果第三方对资金归属、合法性问题发生争议，由乙方自行负责解决</p>
					<p>2.2  甲乙双方签署本协议并办理相关手续均为各自真实意思表示并获得相关授权</p>
					<p>第三条 适用法律及争议解决</p>
					<p>3.1本协议的订立、效力、解释、履行、修改和终止以及争议的解决适用中国的法律如果本协议中的任何一条或多条违反适用的法律法规，则该条将被视为无效，但该无效条款并不影响本协议其他条款的效力</p>
					<p>3.2  本协议在履行过程中，如发生任何争执或纠纷，双方应友好协商解决；若协商不成，任何一方均有权向有管辖权的人民法院提起诉讼</p>
					<p>第四条 违约责任</p>
					<p>4.1双方同意，如果一方违反其在本协议中所作的保证、承诺或任何其他义务，致使其他方遭受或发生损害、损失等责任，违约方须向守约方赔偿守约方因此遭受的一切经济损失</p>
					<p>4.2  双方均有过错的，应根据双方实际过错程度，分别承担各自的违约责任</p>
					<p>第五条 其他</p>
					<p>5.1  甲、乙方均同意并确认，甲、乙方通过汇盈金服平台账户和其银行账户的行为均通过第三方支付平台进行，所产生的法律效果及法律责任归属于甲、乙方</p>
					<p>5.2  本协议项下的附件和补充协议构成本协议不可分割的一部分，本协议项下无约定的事项以汇盈金服平台公布的相关规则为准</p>
					<p>甲、乙方声明：对于本协议条款，汇盈金服平台已应甲、乙方要求进行充分解释及说明，甲、乙方对本协议内容及相应风险已完全知悉并理解</p>
					<br> <br> <br> <br> <br> <br>
				</div>
			</div>
		</div>
	</div>
	</section>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>