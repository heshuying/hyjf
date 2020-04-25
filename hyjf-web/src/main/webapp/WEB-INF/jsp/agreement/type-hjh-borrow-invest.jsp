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
                    汇盈金服互联网金融服务平台居间服务协议
                    <%-- <a class="download-txt" href="${ctx}/user/mytender/createAgreementPDF.do?borrowNid=${borrowNid }&nid=${nid }">下载协议</a> --%>
                    
                    <c:choose>
                    	<c:when test="${borrowNid == null || borrowNid == '' }">
                    		<!-- 不显示 下载协议 -->
                    	</c:when>
                    	<c:otherwise>
                    		<a class="download-txt" href="${ctx}/createAgreementPDF/creditPaymentPlan.do?borrowNid=${borrowNid }&nid=${nid }">下载协议</a>
                    	</c:otherwise>
                    </c:choose>
                </div>
                <div class="detial-list">
                    <div style="margin-top: 20px;border: #CCC solid 1px; padding: 5px 20px">
                        编号：${borrowNid }&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <!--融资人：杨旭升&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;还款方式：
            到期还本还息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;出借人：详见本协议第一条&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                        签订日期： ${fn:substring(recoverTime,0,10) }<br>
                    </div>
                    <div class="t20">
                    <p>甲方（出借方/债权受让方）：</p>
                    <ul class="agreement-bg">
                        <li>
                            <table width="100%" class="table-name" border="0" cellspacing="0" cellpadding="0">
                        		<thead>
									<tr class="head">
										<td>用户名</td>
										<td>投资金额</td>
										<td>投资期限</td>
										<td>历史年回报率</td>
										<td>投资开始日</td>
										<td>投资到期日</td>
										<td>借款方还款方式</td>
										<td>总还款本金及收益</td>
									</tr>
								</thead>
                                <tbody>
                                    <tr class="head">
                                        <td>${userInvest.realName }${userInvest.idCard }</td>
                                        <td><fmt:formatNumber value="${userInvest.account }" pattern="#,##0.00#" />元</td>
                                        <td>${userInvest.investPeriod }</td>
                                        <td>${userInvest.interest }%</td>
                                        <td>${userInvest.investStartDay }</td>
                                        <td>${userInvest.investEndDay }</td>
                                        <td>${userInvest.repayMethod }</td>
                                        <td><fmt:formatNumber value="${userInvest.total }" pattern="#,##0.00#" />元</td>
                                    </tr>
                                </tbody>
                            </table>
                        </li>
                        <li style="display: none">
                            <h4 style="text-align: center; padding: 5px 0">2、还款明细：</h4>
                            <c:if test="${!empty repayList}">
	                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabyel">
	                                <thead>
	                                    <tr class="head">
	                                        <td>融资期数</td>
	                                        <td>历史年回报率</td>
	                                        <td>应还时间</td>
	                                        <td>还款本息</td>
	                                        <td>还款本金</td>
	                                        <td>还款利息</td>
	                                    </tr>
	                                </thead>
	                                <tbody>
	                                	<c:forEach items="${repayList }" var="record" begin="0" step="1" varStatus="status">
		                                    <tr>
		                                        <td>${record.projectPeriod }</td>
		                                        <td>${userInvest[0].interest }%</td>
		                                        <td>${record.repayDay }</td>
		                                        <td><fmt:formatNumber value="${record.projectTotal }" pattern="#,##0.00#" />元</td>
		                                        <td><fmt:formatNumber value="${record.projectCapital }" pattern="#,##0.00#" />元</td>
		                                        <td><fmt:formatNumber value="${record.projectInterest }" pattern="#,##0.00#" />元</td>
		                                    </tr>
	                                    </c:forEach>
	                                </tbody>
	                            </table> <!--<p>注：因计算中存在四舍五入，最后一期应收本息与之前略有不同。</p>-->
                            </c:if>
                        </li>
                    </ul>
                </div>
                <div style="margin-bottom: 20px;">
                    <p style="margin-bottom: 6px;">乙方（借款方/债权转让方）：</p>
                    
                    
                    <p style="margin-bottom: 10px;">
	                    <c:choose>
		                    <c:when test="${borrowUsername == null || borrowUsername == '' }">
		                    </c:when>
		                    <c:otherwise>
		                    用户名：${borrowUsername.substring(0,1) }**
		                    </c:otherwise>
	                    </c:choose>
                    </p>
                    <p style="margin-bottom: 10px;">说明：为保护其隐私权，因此未显示全部乙方的信息，若出现乙方违反本协议的情况，丙方有权向甲方披露其全部信息，包括但不限于乙方的姓名、身份证号码及住址等信息。</p>
                    <p style="margin-bottom: 6px;">
	                    <c:choose>
		                    <c:when test="${borrowNid == null || borrowNid == '' }">
		                     投资项目：详见平台网站www.hyjf.com的项目内容。
		                    </c:when>
		                    <c:otherwise>
		                     投资项目：详见平台网站www.hyjf.com编号为${borrowNid }的项目内容。
		                    </c:otherwise>
	                    </c:choose>
                    </p>  
                </div>
                <div style="margin-bottom: 20px;">
                    <p style="margin-bottom: 6px;">丙方（居间人）：惠众商务顾问（北京）有限公司</p>
                    <p style="margin-bottom: 10px;">经营地址：<b style="display:inline">上海市长宁区延安西路2299号世贸大厦24楼</b></p>
                </div>
                <div style="margin-bottom: 20px;">
                    <p style="margin-bottom: 6px;">鉴于：</p>
                    <p style="margin-bottom: 10px;">1、丙方是一家在北京合法成立并有效存续的有限责任公司，拥有www.hyjf.com网站（以下简称“本网站”）的经营权，提供信用咨询，为交易提供信息服务；</p>
                    <p style="margin-bottom: 10px;">2、甲、乙双方已在本网站注册，并承诺其提供给丙方的信息是完全真实的；</p>
                    <p style="margin-bottom: 10px;">3、甲方为出借方，承诺对本协议涉及的投资款具有完全的支配能力，是其自有闲置资金，且为合法所得；</p>
                    <p style="margin-bottom: 10px;">4、乙方为借款方，拟通过本网站平台借款/转让债权，进行借款。</p>
                    <p style="margin-bottom: 10px;">5、经丙方居间服务，甲方同意出借借款/受让债权，甲、乙双方成立借贷/债权转让法律关系。</p>
                    <p style="margin-bottom: 10px;">6、甲方同意向乙方出借借款/受让债权时，委托丙方通过第三方存管机构在本协议成立后将相应投资款项直接划付至乙方在第三方存管机构开立的电子银行账户。</p>
                </div>
				<div class="t20">
                    <br>
                    <h3 style="text-align: center;">第一条&nbsp;权利和义务</h3>
                    <br>
                    <p>一、甲方的权利和义务</p>
                    <p>1、&nbsp;甲方按合同约定将投资款（借款/债权转让价款）支付给乙方；&nbsp;</p>
                    <p>2、&nbsp;甲方享有其投资款项所带来的收益；&nbsp;</p>
                    <p>3、&nbsp;如乙方违约，甲方有权要求丙方提供其已获得的乙方信息；&nbsp;</p>
                    <p>4、&nbsp;无须通知乙方，甲方可根据自己的意愿进行本协议下债权转让/再转让。甲方转让/再转让债权的，乙方继续履行本协议下还款/债权回购义务，不得以未接到债权转让/再转让通知为由拒绝履行。&nbsp;</p>
                    <p>5、&nbsp;甲方应主动缴纳因投资收益所得产生的税费；&nbsp;</p>
                    <p>二、乙方权利和义务</p>
                    <p>1、&nbsp;如丙方未就乙方的还款方式在丙方平台的“还款计划”中做出特殊列明，乙方均应在借款到期日一次性足额偿还借款本金及利息；债权对应的债务履行期限届满前，乙方必须全额回购债权，向甲方支付回购款及利息；  &nbsp;</p>
                    <p>2、&nbsp;乙方必须按期足额向丙方支付服务费用；&nbsp;</p>
                    <p>3、&nbsp;乙方承诺通过借贷/债权转让获取的甲方投资款必须用于特定合法用途；&nbsp;</p>
                    <p>4、&nbsp;乙方应确保其提供的信息和资料的真实性，不得提供虚假信息或隐瞒重要事实；&nbsp;</p>
                    <p>5、&nbsp;乙方不得将本协议项下的任何权利义务转让给任何其他方。&nbsp;</p>
                    <p>三、丙方的权利和义务</p>
                    <p>1、甲、乙双方同意，丙方有权代甲方在必要时对乙方进行还款/债权回购的违约提醒及催收工作，包括但不限于电话通知、发律师函以及受托对乙方提起诉讼等。甲方在此确认委托丙方为其进行以上工作，并授权丙方可以将此工作委托给其他方进行。乙方对前述委托的提醒、催收事项已明确知晓并应积极配合。&nbsp;</p>
                    <p>2、丙方有权向乙方收取双方约定的服务费，并在有必要时对乙方进行还款/债权回购的违约提醒及催收工作，包括但不限于电话通知、发律师函、对乙方提起诉讼等。丙方有权将此违约提醒及催收工作委托给其他方进行。 &nbsp;</p>
                    <p>3、丙方接受甲、乙双方的委托行为，其所产生的法律后果由委托方承担。一方违约给守约方造成经济损失的，守约方有权根据本协议约定追究违约方的法律责任。&nbsp;</p>
                    <p>4、丙方应对甲方和乙方的信息及本协议内容保密；如任何一方违约，或因相关权力部门要求（包括但不限于司法机关、仲裁机构、金融监管机构等），则丙方可以披露。&nbsp;</p>
                    <br>
                    <h3 style="text-align: center;">第二条&nbsp;违约责任</h3>
                    <br>
                    <p>1、合同各方均应严格履行合同义务，非经各方协商一致或依照本协议约定，任何一方不得解除本协议。&nbsp;</p>
                    <p>2、任何一方违约，违约方应承担因违约使得其他各方产生的费用和损失，包括但不限于投资款本金及利息损失、服务费、守约方实现债权的费用（包括但不限于调查、诉讼费用、律师费等）等，均由违约方承担。 如乙方违约，甲方有权委托丙方追究乙方违约责任并要求乙方立即偿还未偿还的投资本金、利息、罚息、违约金、服务费及甲、丙方实现债权的费用。乙方在本网站账户有任何余款，丙方有权直接冻结，并按照相关法律规定予以扣收、划款，按照本协议第二条第3项的清偿顺序将乙方余款用于清偿上述应付甲、丙方款项，并要求乙方支付因此产生的所有相关费用。&nbsp;</p>
                    <p>3、乙方还款按照如下顺序清偿：</p>
                    <p>（1）实现债权的费用；</p>
                    <p>（2）约定的甲方投资款利息、罚息；</p>
                    <p>（3）甲方投资款本金；</p>
                    <p>（4）约定的服务费用；</p>
                    <p>4、乙方应严格履行还款/支付回购款义务，如乙方逾期，则应按照下述条款向甲方支付逾期罚息（因法定节假日顺延还款日期的，延长期间按正常借款计息，不作为逾期）： 罚息总额 = 逾期本息总额×对应日罚息利率×逾期天数；日罚息利率0.06%；</p>
                    <p>5、本协议所涉借贷/债权转让项目的所有平台出借方与乙方之间的借贷/债权转让法律关系均是相互独立的，一旦乙方逾期还款/逾期履行债权回购义务，任何一个平台出借方均有权单独向乙方追索或者提起诉讼，或委托丙方向乙方追索或者提起诉讼。如乙方逾期支付服务费或提供虚假信息的，丙方亦可单独向乙方追索或者提起诉讼。</p>
                    <br>
                    <h3 style="text-align: center;">第三条&nbsp;提前/延期还款说明</h3>
                    <br>
                    <p>1、借款方/债权转让方可先于约定日期偿还借款本息/回购债权；</p>
                    <p>2、借款方/债权转让方提前还款/回购债权的，按月计息的借款项目，若提前八日以内，则仍按先前规定的金额履行还款/债权回购义务；若提前八日（包括八日）以上，则借款方/债权转让方按实际用款时间履行还款/债权回购义务的同时需再向甲方支付对应借款/债权回购价款三天的日利息。日利息计算方式为：该笔借款年化利息/360。按天计息的借款项目，若发生提前还款的，则借款方/债权转让方按其实际用款时间履行还款/债权回购义务。</p>
                    <p>3、全部借款/债权转让项目还款日期遇法定节假日，则还款日期自动提前至法定节假日开始前的最后一个工作日。甲乙双方同意，丙方有权根据丙方平台及第三方存管系统安排通知还款日期顺延至法定节假日结束后的第一个工作日。</p>
                    <p>4、对于丙方向乙方收取服务费的具体标准和规则将由丙方与乙方的居间服务推荐人签署的协议确定，乙方对此予以确认并同意。乙方提前还款的，应按上述协议的约定向丙方支付全额服务费。</p>
                    <br>
                    <h3 style="text-align: center;">第四条&nbsp;法律及争议解决</h3>
                    <br>
                    <p>本协议的签订、履行、终止、解释均适用中华人民共和国法律，并由丙方住所地上海市长宁区具有管辖权的人民法院管辖。</p>
                    <br>
                    <h3 style="text-align: center;">第五条&nbsp;风险提示</h3>
                    <br>
                    <p>1、甲方确认在签署本协议之前已经阅读以下与本协议的订立及履行有关的风险提示，并对该等风险有充分理解和预期。&nbsp;</p>
                    <p>（1）政策风险：国家因宏观政策、财政政策、货币政策、行业政策、地区发展政策等因素引起的系统风险。&nbsp;</p>
                    <p>（2）不可抗力：包括但不限于丙方及相关第三方的设备、系统故障或缺陷、病毒、黑客攻击、网络故障、网络中断、地震、台风、水灾、海啸、雷电、火灾、瘟疫、流行病、战争、恐怖主义、敌对行为、暴动、罢工、交通中断、停止供应主要服务、电力中断、经济形式严重恶化或其它类似事件导致的风险。&nbsp;</p>
                    <p>（3）境外操作风险：在使用丙方网站进行投融资服务的所有期间，甲方应当在中华人民共和国大陆境内进行操作，如因甲方在中国大陆境外（包括港澳台地区）操作导致丙方网站无法向甲方提供服务，或发生错误，或受到境外法律监管，由此导致的全部法律责任和后果将由甲方独自承担。</p>
                    <p>甲方确认在同意订立本协议前已仔细阅读了本协议的所有条款，对本协议的所有条款及内容已经阅读，均无异议，并对双方的合作关系、有关权利、义务、和责任条款的法律含义达成充分的理解甲方自愿承受自主出借行为可能产生的风险。</p>
                    <br>
                    <h3 style="text-align: center;">第六条&nbsp;附则</h3>
                    <br>
                    <p>1、本协议采用电子文本形式制成，并永久保存在丙方为此设立的专用服务器上 备查，各方均认可该形式的协议效力。</p>
                    <p>2、本协议自文本自甲方委托丙方将甲方的投资款项支付至乙方在第三方存管机构开立的电子银行账户之日起生效。&nbsp;</p>
                    <p>3、本协议签订之日起至履行完毕日止，乙方或甲方有义务在下列信息变更三日内提供更新后的信息给丙方：本人、本人的家庭联系人及紧急联系人、工作单位、居住地址、住所电话、手机号码、电子邮箱、银行账户的变更。若因任何一方不及时提供上述变更信息而带来的损失或额外费用应由该方承担。</p>
                    <p>4、如果本协议中的任何一条或多条违反适用的法律法规，则该条将被视为无效， 但该无效条款并不影响本协议其他条款的效力。</p>
                </div>
                </div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>