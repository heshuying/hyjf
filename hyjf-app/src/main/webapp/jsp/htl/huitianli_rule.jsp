<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
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
		<div class="reg_rule_container specialFont response">
			<!--nav ul begin-->
			<ul class="rule_nav tac bg_white" id="rule_nav">
				<li class="rule_nav_cell rule_nav_selected" >产品介绍<!--下划线--><span></span><!--下划线结束--> </li>
				<li class="rule_nav_cell" >产品特点</li>
				<li class="rule_nav_cell" >常见问题</li>
			</ul>
			<!--nav ul end-->
			<!--content begin-->
			<div class="rule_content">
				<div class="rule_0" id = "productId">
					
				</div>
				<div class="rule_0">
					<p class="fw6 mtd18">安全性</p>
					<p>1、本产品由平台的合作机构（如基金公司）定向购买优质债权，银行、证券低风险投资产品等，并出让投资方的灵活期限投资计划，本息安全有保障</p>
					<p>2、由汇付天下资金托管，投资资金进出全程封闭</p>
					<p>3、合作机构（如基金公司）提供全额兑付，本金更安全、收益稳定</p>
					<p class="fw6 mtd18">流动性</p>
					<p>1、资金随进随出，贴心为您着想</p>
					<p>2、投资金额可7*24小时购买赎回汇天利产品</p>
					<p>3、随时赎回，不限时间、不限金额，随取随用更灵活</p>
					<p class="fw6 mtd18">收益性</p>
					<p>1、收益近17倍高于银行活期存款，近1.5倍宝类产品，一样灵活，更高收益</p>
					<p>2、汇天利产品暂不收取平台服务费用，收益100%回馈投资客户</p>
				</div>
				<div class="rule_0">
					<p class="fw6 mtd18">1、汇天利怎么计算收益？</p>
					<p>汇天利历史年回报率为4.0%，计算公式：（1.11x360）÷10000=4.0%。每日每万元收益为1.11元。</p>
					<p>例如</p>
					<p>您汇天利中计息的资金为9000元，</p>
					<p>代入计算公式，当日收益=9000元 x 1.11÷10000=0.99元。</p>
					<p class="fw6 mtd18">2、汇天利什么时候可以购买？</p>
					<p>随时购买，次日0点计息</p>
					<p class="fw6 mtd18">3、汇天利的赎回是否收费？</p>
					<p>免费赎回，不限次数，赎回金额再提现由支付机构收取一元提现费用</p>
					<p class="fw6 mtd18">4、汇天利的赎回金额有限制吗？</p>
					<p>在投资汇天利产品金额内的资金，可部分或全部赎回</p>
					<p class="fw6 mtd18">5、赎回汇天利后资金什么时候能到账？</p>
					<p>到账时间与平台其他产品完全一致，赎回一经发起，即刻到达投资方在支付机构的专属账户，如需提现，参考支付机构规则。</p>
					<p class="fw6 mtd18">6、汇天利的加入金额有限制吗？</p>
					<p>汇天利的加入金额1元起，投资方在汇天利单项产品中的总金额不得超过500000元。</p>
					<p class="fw6 mtd18">7、汇天利收取服务费或其他费用吗？</p>
					<p>汇天利产品暂不收取任何费用，如有变动请以汇盈贷平台公告为准</p>
				</div>
			</div>
			<!--content end-->
		</div>
		<script src="${ctx}/js/doT.min.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
		<script src="${ctx}/js/fill.js" type="text/javascript" charset="utf-8"></script>		
		<script id="tmpl-data" type="text/x-dot-template">
		<p class="rule_introduce_text">汇天利是由合作机构（如基金公司）定向购买优质债权,银行、证券低风险投资产品等，并出让投资方的灵活期限投资计划。投资方的加入资金及加入资金所产生的收益，在满足汇天利产品协议相关规则的前提下，可在随时申请部分或全部赎回。</p>
		<div class="bg_white">
			<p class="rule_introduce_line">投资期限<span class="fr text_grey">{{=it.investLimit}}</span></p>
			<p class="rule_introduce_line">历史年回报率<span class="fr text_grey">{{=it.interestRate}}%</span></p>
			<p class="rule_introduce_line">起投金额<span class="fr text_grey">{{=it.startInvest}}元</span></p>
			<p class="rule_introduce_line">单户上限<span class="fr text_grey">{{=it.userPupper}}元</span></p>
			<p class="rule_introduce_line">首次计息<span class="fr text_grey">{{=it.interestDesc}}</span></p>
			<p class="rule_introduce_line">手续费<span class="fr text_grey">{{=it.fee}}</span></p>
		</div>
		</script>		
		<script>
		$.fillTmplByAjax("/hyjf-app/htl/app/getHtlRule", {flag:0}, "#productId", "#tmpl-data");
		</script>
		<script type="text/javascript" src="${ctx}/js/tabChange.js"></script>
	</body>
</html>