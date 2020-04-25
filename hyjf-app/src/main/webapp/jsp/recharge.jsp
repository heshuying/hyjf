<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title></title>
	</head>
	<body class="bg_grey">
		<div class="container specialFont response">
			<p class="others_recharge_text text_orange">1. 快捷充值须先开通“ <a  style="text-decoration: underline;color:#48b7fe" href="https://www.95516.com/static/union/pages/card/openFirst.html?entry=openPay ">银联在线支付</a>”功能；</p>
			<p class="others_recharge_text">2. 快捷充值时，若银联页面提示错误代码“CI77”则表示未开通“银联在线支付”功能；</p>
			<p class="others_recharge_text">3.投资人充值投资所有项目的充值手续费均有平台垫付；</p>
			<p class="others_recharge_text">4.最低充值金额应大于等于1元；</p>
			<p class="others_recharge_text text_orange"> 5. 用户每日发起快捷充值上限30次；</p>
			<p class="others_recharge_text">6.使用快捷充值的资金，取现时原卡返回；</p>
			<p class="others_recharge_text">7.充值/提现必须为银行借记卡，不支持存折、信用卡充值；</p>
			<p class="others_recharge_text">8.充值期间，请勿关闭浏览器，待充值成功并返回首页后，所充资金才能入账，如有疑问，请联系客服；</p>
			<p class="others_recharge_text">9.绑定银行卡时，绑定的银行卡地开户信息，需要与存管账户的用户信息保持一致；</p>
			<p class="others_recharge_text">10.解绑银行卡时，需要当前存管账户系统余额为0，且没有未结清的标的时(注：清算标的有约两小时的处理时间，22:00后的清算将在次日的8:00后统一清算，请耐心等待)，才可以进行解绑；如不符合条件仍需解绑，请联系客服：400-900-7878</p>
			<p class="others_recharge_text">11.支付限额请参照 支付说明。</p>
			<p class="others_recharge_text"></p>
			<p class="others_recharge_text"></p>
			<table border="1" cellspacing="0" cellpadding="0" class="tac others_table">
           		<thead>
					    <tr>
					       <td>序号</td>
					       <td >支持银行</td>
					       <td >交易限额/单笔限额</td>
					       <td >日累计限额</td>
					    </tr>
				</thead>
				<tbody id="roleTbody">
					<c:choose>
						<c:when test="${empty date}">
							<tr>
								<td colspan="6">暂时没有数据记录</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${date}" var="bank" begin="0" step="1" varStatus="status">
								<tr>
									<td><c:out value="${ status.index + 1}"></c:out></td>
									<td class="center"><c:out value="${bank.bankName }"></c:out></td>
									
								<td>
															<fmt:formatNumber  value="${(bank.singleQuota.setScale(4,1)/10000).setScale(2,1)}"  maxFractionDigits="2"/>
															<c:out value="万元"></c:out>
													</td>
													<td>
														<fmt:formatNumber    value="${(bank.singleCardQuota.setScale(4,1)/10000).setScale(2,1)}" 
														 maxFractionDigits="2"  />
																	
															<c:out value="万元"></c:out>										
													
													</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</body>
</html>