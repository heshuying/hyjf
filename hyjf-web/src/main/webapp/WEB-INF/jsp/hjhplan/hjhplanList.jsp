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
	 <article class="main-content" style="padding-top: 0;">
	 	<div class="zt-list-banner">
        	<p>
        		智投服务是平台根据出借人授权，帮助出借人分散投标、循环出借的服务。
            	<a class="risk-alt alt1">
            		<span class="risk-tips" style="height: 138px;background-size: 544px 138px;">
            			智投服务是汇盈金服为您提供的本金自动循环出借及到期自动转让退出的自动投标服务，自动投标授权服务期限自授权出借之日起至退出完成。当您选择智投服务时，将授权平台系统为您选择符合条件的标的进行分散投标、循环出借，以此提高了您的出借效率和便利性，减少了资金站岗的时间，更好的满足您多样化的出借需求。出借范围仅限于平台发布的借款标的或服务中被转让债权，您可随时查看持有的债权标的列表及标的详情，真正做到信息的公开、透明。参考回报不代表对实际回报的承诺，出借人需自行承担资金出借的风险与责任；市场有风险，出借需谨慎。
            		</span>
            		<i class="icon iconfont icon-zhu "></i>
            	</a>
            	 到期后退出时效以实际债权转让完成用时为准。
            	 <a class="risk-alt alt2">
            		<span class="risk-tips" style="height: 66px;background-size: 544px 66px;">
            			服务回报期限届满，系统对尚未结清标的自动发起债权转让。债权标的全部结清，并且债权转让全部完成。您所持债权转让完成的具体时间，视债权转让市场交易情况而定。
            		</span>
            		<i class="icon iconfont icon-zhu "></i>
            	</a> 
        	</p>
        </div>
        <div class="container zt-list">
        	<p class="timing-notice"><i class="icon iconfont  icon-bulb"></i>温馨提示：近期“智投服务”的额度开放时间在10:00，其余时间根据资产和运营情况适时开放。 </p>
            <!-- start 内容区域 -->  
            <ul>
            	<c:forEach items="${hjhPlanList}" var="record" begin="0" step="1" varStatus="status">
					<li>
						<a href="${ctx}/hjhdetail/getPlanDetail.do?planNid=${record.planNid}" >
							<div class="title"><i class="iconfont  icon-shijian"></i> ${record.planPeriod}
								<c:if test="${record.isMonth eq 0}">
									天
								</c:if>
								<c:if test="${record.isMonth eq 1}">
									个月
								</c:if>
							</div>
							<div class="attr-item attr1">
								<c:set value="${ fn:split(record.planApr, '.') }" var="planAprStr" />
								<span class="lg">${record.planApr}</span><span class="xs">%<%--<c:if test="${record.isIncrease ==  'true'}">+${record.borrowExtraYield}%</c:if>--%></span>
								<!--<div class="poptag-pos">
									<div class="poptag">+1%</div>
								</div>-->
								<p>参考年回报率</p>
							</div>
							<div class="attr-item attr2">
								<div class="residue-box">
                                  		<div class="attr2-key">开放额度</div>
                                   	<div class="attr2-val"><span class="md">${record.availableInvestAccount}</span> <span class="xs">元</span></div>
                                   </div>
                                  </div>
	                        <c:if test="${record.statusName eq '稍后开启'}">
	                        	<div class="link later">${record.statusName}</div>
	                        </c:if>
	                        <c:if test="${record.statusName ne '稍后开启'}">
	                        	<div class="link">授权服务</div>
	                        </c:if>
						</a>
					</li>
				</c:forEach>
            </ul>
            <p class="title"><span></span>服务介绍</p>
            <div class="introduce">
	        	<div class="introduce-1">智投服务是平台运用人工智能，同时配合资产配置、投后监控、风险管理等方法实现为用户提供的本金自动循环出借及到期自动转让<br/>退出的自动投标服务。</div>
	        	<div class="introduce-2">用户授权智投服务后，系统将智能为用户选择符合条件的标的进行分散投标，实现风险分散;服务回报期限内，系统将对回款自动<br/>进行复投，以此提高资金的出借效率，减少资金站岗情况。</div>
	        	<div class="introduce-3">智投服务本身并不具备产品特性，仅是通过自动投标服务来帮助用户实现高效、便捷的分散投标需求的一项服务，所投标的全部为<br/>经过平台风控严格审核并事先发布的真实借款标的。</div>
	        </div>
	        <p class="title"><span></span>服务数据</p>
	        <div class="data">
    			<div>
    				<p class="tit">授权出借</p>
    				<c:set value="${ fn:split(dataStatistic.accede_account_total, '.') }" var="accedeTotal" />
    				<p class="money">${accedeTotal[0]}<span class="sm">.${accedeTotal[1] }</span><span class="unit"> 万元</span></p>
    				<em class="rule"></em>
    			</div>
    			<div>
    				<p class="tit">用户回报</p>
    				<c:set value="${ fn:split(dataStatistic.interest_total, '.') }" var="interestTotal" />
    				<p class="money">${interestTotal[0] }<span class="sm">.${interestTotal[1] }</span><span class="unit"> 万元</span></p>
    				<em class="rule"></em>
    			</div>
    			<div>
    				<p class="tit">服务人次</p>
    				<p class="money">${dataStatistic.accede_times }<span class="unit"> 人</span></p>
    			</div>
        	</div>
        	<p class="title"><span></span>服务特点</p>
        	<img src="/dist/images/zhitou/characteristic@2x.png" alt="" width="1100px"/>
        	<p class="title"><span></span>服务流程</p>
        	<div class="flow">
        		<img src="/dist/images/zhitou/flow@2x.png" alt="" width="1046px"/>
        	</div>
        	<p class="title"><span></span>服务周期</p>
        	<img src="/dist/images/zhitou/cycle@2x.png" alt="" width="1100px"/>

            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<!-- 导航栏定位  -->
	<script>setActById("indexPlan");</script>
	 <script type="text/javascript">
    	$('.risk-alt.alt1').hover(function(){
    		var alt=$(this).find('.risk-tips');
			$(this).find('.risk-tips').stop().fadeIn(150);
    	},function(){
    		$(this).find('.risk-tips').stop().fadeOut(150);
    	})
    	$('.risk-alt.alt2').hover(function(){
    		var alt=$(this).find('.risk-tips');
			$(this).find('.risk-tips').stop().fadeIn(150);
    	},function(){
    		$(this).find('.risk-tips').stop().fadeOut(150);
    	})
    </script>
</body>
</html>