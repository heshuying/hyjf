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
<style type="text/css">
	.main-content .container .account-set-up-2 .sub.disable{
		background: #CCCCCC;
	}
	.term-checkbox{
		width: 16px;
	    height: 16px;
	    display: inline-block;
	    vertical-align: middle;
	    position: relative;
	    top: -2px;
	    margin-right: 2px;
	    cursor: pointer;
	    background-image: url(/dist/images/product/product_check@2x.png);
	    background-size: 16px;
	}
	.term-checkbox.checked {
   		background-image: url(/dist/images/product/product_cancel@2x.png);
	}
</style>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	    <article class="main-content">
	    	<div class="container result">
	    		<!-- start 内容区域 -->
	    		<div class="account-set-up-2">
	    			<p class="title">授权须知</p>
	    			<div class="set-content" style="width: 900px;text-align: center;padding-bottom: 40px;">
	    				<c:if test="${roleId==1}">
		    			<p style="font-size: 20px;margin-top: 55px;margin-bottom: 50px;font-weight: bold;">应合规要求，您在进行出借、提现等交易前需进行授权。</p>
		    			</c:if>
		    			<c:if test="${roleId!=1}">
		    			<p style="font-size: 20px;margin-top: 55px;margin-bottom: 50px;font-weight: bold;">应合规要求，您在进行还款、提现等交易前需进行授权。</p>
	    				</c:if>
	    				<p style="line-height: 25px;" align="center">
							温馨提示：此项授权操作是汇盈金服平台应合规要求进行升级，升级后需要获得您的授权，才能进行资金相关操作，您的资金安全会更加有保障。<br>
此外，为了保障您授权操作的顺利进行，授权操作后跳转的银行界面，请不要改动，给您带来的不便，敬请谅解！<br>
<br>
如有疑问，请联系汇盈金服官方客服电话：400-900-7878
	    				</p>
	    				<div style="margin-top: 80px; color: #999;">
	    					<c:if test="${roleId==1}">
	    					<div class="term-checkbox"></div>我已阅读并同意 <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=zdtbsq" target="_blank" style="color: #ff5b29;text-decoration: none;">${zdtbsq }</a><a href="${ctx}/agreement/goAgreementPdf.do?aliasName=zdzzsq" target="_blank" style="color:#ff5b29;text-decoration: none;">${zdzzsq }</a><a href="${ctx}/agreement/goAgreementPdf.do?aliasName=jfsq" target="_blank" style="color: #ff5b29;text-decoration: none;">${jfsq }</a>
	    					</c:if>
	    					<c:if test="${roleId==2}">
	    					<div class="term-checkbox"></div>我已阅读并同意 <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=jfsq" target="_blank" style="color: #ff5b29;text-decoration: none;">${jfsq }</a><a href="${ctx}/agreement/goAgreementPdf.do?aliasName=hksq" target="_blank" style="color:#ff5b29;text-decoration: none;">${hksq }</a>
							</c:if>
							<c:if test="${roleId==3}">
	    					<div class="term-checkbox"></div>我已阅读并同意 <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=jfsq" target="_blank" style="color: #ff5b29;text-decoration: none;">${jfsq }</a>
							</c:if>
						</div>
	    				<a class="sub disable" >去授权</a>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	    
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>
		$('.term-checkbox').click(function(){
			if($(this).hasClass('checked')){
	    		$(this).removeClass('checked')
	    		$('.sub').addClass('disable')
	    	}else{
	    		$(this).addClass('checked')
	    		$('.sub').removeClass('disable')
	    	}
		})
		    //提交按钮
	    $(".sub").click(function(){
	    	if(!$(this).hasClass('disable')){

	    		<c:if test="${roleId==1}">
                saEvents.submitPlanAuth('出借人授权');
	    		setTimeout(function(){
                    window.location='/bank/user/auth/mergeauthpageplus/page.do'
				},300)
    			</c:if>
	    		<c:if test="${roleId==2}">
                saEvents.submitPlanAuth('借款人授权');
                setTimeout(function(){
                    window.location='/bank/user/auth/payrepayauth/auth.do'
                },300)

    			</c:if>
                <c:if test="${roleId==3}">
                saEvents.submitPlanAuth('担保机构授权');
                setTimeout(function(){
                    window.location='/bank/user/auth/paymentauthpageplus/page.do'
                },300)

    			</c:if>
	    	}
	    })
	</script>
</body>
</html>