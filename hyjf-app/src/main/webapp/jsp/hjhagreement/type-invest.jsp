<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
		<title>居间服务协议</title>
		<style type="text/css">
			.contract_item_hjh{margin-bottom:15px}
			.contract_item_hjh:last-child{margin:0}
			.contract_item{margin:0!important}
			.contract_item>div{padding:10px;margin:0;}
			.contract_item_block h3{font-weight:normal;font-size:16px;text-align:center;padding-bottom:10px}
			.contract_item_block{margin-bottom:10px}
			.bounce-bg{width:100%;height:10px;backgorund:#f3f3f3}
			
			.specialFont{font-weight:bold;}
			.contract_item_block h3{font-weight:bold;}
		</style>
	</head>
	<body class="bg_grey">
		<div class="specialFont response">
			<!--item begin-->
			<div class="contract_item bg_white">
				<div>
					<div class="contract_item_hjh">
						<p>编号：${borrowNid } </p>
						<p>签订日期：${fn:substring(recoverTime,0,10) }</p>
					</div>
					<div class="contract_item_hjh">
						<p>甲方（出借人）：</p>
						<p>证件号码：${userInfo.idcard}</p>
						<p>用户名：${userInfo.truename}</p>
					<div class="contract_item_hjh">
						<p>乙方（借款人）： </p>
						<p>证件号码： ${borrowerUserInfo.idcard}</p>
						<p>用户名：${borrowerUserInfo.truename}</p>
					</div>
					<div class="contract_item_hjh">
						<p>丙方（平台方）：惠众商务顾问（北京）有限公司</p>
						<p>经营地址：上海市长宁区延安西路2299号世贸大厦24楼 </p>
					</div>
					<div class="contract_item_hjh">
						<p>鉴于：</p>
						<p>1、丙方是一家在合法成立并有效存续的有限责任公司，拥有汇盈金服www.hyjf 网站（以下简称“平台方”）的经营权，提供信用咨询，为交易提供信息服务，并与江西银行（以下简称存管银行）建立第三方银行存管合作关系。</p>
						<p>2、甲、乙双方已在平台方注册并在存管银行开立电子银行账户，并承诺其提供给丙方的信息是完全真实的；</p>
						<p>3、甲方拟通过平台方出借资金，乙方拟通过平台方借入资金。经丙方居间服务，甲、乙双方成立借贷法律关系，并签订电子《借款协议》。</p>
						<p>4、甲方同意，《借款协议》签署后，委托丙方通过存管银行将出借资金划付至乙方在存管银行开立银行账户。</p>
						<p>5、乙方同意，在《借款协议》约定的还款日，按时将本金、利息、丙方服务费等其他应付款项足额划付至乙方在存管银行开立银行账户，委托丙方通过存管银行将本金利息支付给出借人。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<div class="bounce-bg"></div>
			<!--item begin-->
			<div class="contract_item bg_white">
				<div>
			        <div class="contract_item_block">
			            <h3>第一条 权利和义务</h3>
					    <div class="contract_item_hjh">
					        <p>一、甲方的权利和义务</p>
					        <p>1、甲方有权按《借款协议》的约定将出借资金支付给乙方；</p> 
							<p>2、甲方享有其出借资金所带来的收益； </p>
							<p>3、如乙方违约，甲方有权要求丙方提供其已获得的乙方信息； </p>
							<p>4、甲方应主动缴纳因出借资金获得收益产生的税费； </p>
							<p>5、对于丙方向甲方提供的服务是否收取服务费及服务费的具体标准和规则将由丙方与甲方签署的协议，以及丙方不时公开公布的规则确定。</p>
						</div>
						<div class="contract_item_hjh">
					        <p>二、乙方的权利和义务</p>
					        <p>1、乙方必须按期向甲方足额支付借款本金、利息；</p>
							<p>2、乙方必须按期足额向丙方支付服务费等其他应付费用； </p>
							<p>3、乙方承诺通过借贷获取的资金必须用于特定合法用途； </p>
							<p>4、乙方应确保其提供的信息和资料的真实性，不得提供虚假信息或隐瞒重要事实； </p>
							<p>5、乙方不得将本协议项下的任何权利义务转让给任何其他方；</p>
							<p>6、对于丙方向乙方提供的服务是否收取服务费及服务费的具体标准和规则将由丙方与乙方的居间服务推荐人签署的协议确定，乙方对此予以确认并同意。</p>
						</div>
						<div class="contract_item_hjh">
					        <p>三、丙方的权利和义务</p>
					        <p>1、甲、乙双方同意，丙方有权代甲方在必要时对乙方进行还款的违约提醒及催收工作，包括但不限于电话通知、发律师函以及受托对乙方提起诉讼等。甲方在此确认委托丙方为其进行以上工作，并授权丙方可以将此工作委托给其他方进行。乙方对前述委托的提醒、催收事项已明确知晓并应积极配合； </p>
							<p>2、在甲乙双方借贷关系存续期间，丙方有权采取合法合理的措施向乙方进行还款提醒及催收工作（包括但不限于短信、电子邮件、电话、上门催收提醒、发律师函等），积极履行受托义务。甲方、乙方承诺对甲方的前述提醒及催收工作不持异议并积极配合。丙方有权将此违约提醒及催收工作委托给其他方进行；</p>
							<p>3、丙方接受甲、乙双方的委托行为，其所产生的法律后果由委托方承担。丙方应对甲方和乙方的信息及本协议内容保密；如任何一方违约，或因相关权力部门要求（包括但不限于司法机关、仲裁机构、金融监管机构等），则丙方可以披露；</p>
							<p>4、丙方有权向甲方、乙方收取约定的服务费及其他应收款项。</p>
						</div>
			        </div>
				     <div class="contract_item_block">
			            <h3>第二条 违约责任</h3>
					    <div class="contract_item_hjh">
					     <p>1、合同各方均应严格履行合同义务，非经各方协商一致或依照本协议约定，任何一方不得解除本协议。 </p>
						 <p>2、任何一方违约，违约方应承担因违约使得其他各方产生的费用和损失，包括但不限于借款本金及利息损失、服务费、管理费、守约方实现债权的费用（包括但不限于调查、诉讼费用、律师费等）等，均由违约方承担。</p>
						 <p>3、乙方应严格履行还款义务，如乙方逾期，则应按《借款协议》的约定条款向甲方、丙方支付逾期罚息、违约金；</p>
						 <p>4、如乙方违约，甲方有权委托丙方追究乙方违约责任并要求乙方立即偿还未偿还的借款本金、利息、罚息、违约金、服务费及甲、丙方实现债权的费用。乙方在平台方账户有任何余款，丙方有权直接冻结，并按照相关法律规定予以扣收、划款，按照《借款协议》约定清偿顺序将乙方余款用于清偿上述应付甲、丙方款项，并要求乙方支付因此产生的所有相关费用。</p> 
						 <p>5、丙方平台所列借贷项目的所有出借人与借款人之间的借贷法律关系均是相互独立的，一旦借款人逾期还款，任何一个出借人均有权单独向借款人追索或者提起诉讼，或委托平台方向借款人追索或者提起诉讼。如借款人逾期支付丙方服务费或提供虚假信息的，丙方亦可单独向借款人追索或者提起诉讼。</p>
						</div>
			        </div>
			        <div class="contract_item_block">
			            <h3>第三条 风险提示</h3>
					    <div class="contract_item_hjh">
						    <p>1、甲方确认在签署本协议之前已经阅读以下与本协议的订立及履行有关的风险提示，并对该等风险有充分理解和预期。 </p>
							<p>（1）政策风险：国家因宏观政策、财政政策、货币政策、行业政策、地区发展政策等因素引起的系统风险。 </p>
							<p>（2）不可抗力：包括但不限于丙方及相关第三方的设备、系统故障或缺陷、病毒、黑客攻击、网络故障、网络中断、地震、台风、水灾、海啸、雷电、火灾、瘟疫、流行病、战争、恐怖主义、敌对行为、暴动、罢工、交通中断、停止供应主要服务、电力中断、经济形式严重恶化或其它类似事件导致的风险。</p>
							<p>（3）境外操作风险：在使用丙方网站进行投融资服务的所有期间，甲方应当在中华人民共和国大陆境内进行操作，如因甲方在中国大陆境外（包括港澳台地区）操作导致丙方网站无法向甲方提供服务，或发生错误，或受到境外法律监管，由此导致的全部法律责任和后果将由甲方独自承担。</p>
							<p>（4）在任何情形下，丙方只是提供投融资网络交易的撮合服务平台，对甲方的出借资金不承担任何担保责任。</p>
							<p>（5）丙方保留对可疑交易、非法交易、高风险交易或交易纠纷的独立判断和确定，并有权依法采取暂停交易、终止交易、向有关单位报告等必要处理措施或依有关单位合法指示行事。</p>
							<p>2、甲方确认在同意订立本协议前已仔细阅读了本协议的所有条款，对本协议的所有条款及内容已经阅读，均无异议，并对双方的合作关系、有关权利、义务、和责任条款的法律含义达成充分的理解甲方自愿承受自主出借行为可能产生的风险。</p>
						</div>
			        </div>
			         <div class="contract_item_block">
			            <h3>第四条 法律及争议解决</h3>
					    <div class="contract_item_hjh">
					        <p>1、本协议的签订、履行、终止、解释均适用中华人民共和国法律，并由丙方住所地上海市长宁区具有管辖权的人民法院管辖。</p>
						</div>
			        </div>
			        <div class="contract_item_block">
			            <h3>第五条  附则</h3>
					    <div class="contract_item_hjh">
					     <p>1、本协议采用电子文本形式制成，并永久保存在丙方为此设立的专用服务器上 备查，各方均认可该形式的协议效力。</p>
						 <p>2、本协议采用电子文本形式制成，自甲方、乙方分别点击签署本协议时协议文本生成，生成的协议文本自借款人发布的借款需求所对应的出借资金已经全部募集成功且支付至借款人存管银行账户时生效。</p>
						 <p>3、本协议签订之日起至履行完毕日止，乙方或甲方有义务在下列信息变更三日内提供更新后的信息给丙方：本人、本人的家庭联系人及紧急联系人、工作单位、居住地址、住所电话、手机号码、电子邮箱、银行账户的变更。若因任何一方不及时提供上述变更信息而带来的损失或额外费用应由该方承担。</p>
						 <p>4、如果本协议中的任何一条或多条违反适用的法律法规，则该条将被视为无效， 但该无效条款并不影响本协议其他条款的效力。</p>
						</div>
			        </div>
				</div>
			</div>
			
	</body>
</html>