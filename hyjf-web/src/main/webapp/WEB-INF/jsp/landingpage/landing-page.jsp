<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<style>
.toolbar ul li.item{
	display:none;
}
.toolbar ul li.item:last-child{
	display:block;
}
</style>
</head>
<body>
	<header id="header">
        <div class="nav-main">
            <div class="container">
                <a href="${ctx}/" class="logo"><img src="${cdn}/dist/images/logo.png?v=20171123" alt="汇盈金服" /></a>
                <nav class="nav-right">
                    <div class="user-nav">
                        <a href="${ctx }/user/regist/init.do">注册</a> | <a href="${ctx }/">返回首页</a>
                    </div>
                </nav>
            </div>
        </div>
    </header>
    
        <section class="main-content" style="padding:0">
        <div class="landing-page">
            <div class="landing_section land-ban register">
                <div class="top-main re-content">
                    <form action="${ctx}/user/regist/regist.do" name="regForm" id="registerForm" method="post"><!-- 旧版JSP 使用  regForm -->
		    			<!-- 通过着陆页注册 补充字段 -->
						<input type="hidden" id='utm_source' name='utm_source' value="${landingpageForm.utm_source}"> <!-- 推广渠道 -->
						<input type="hidden" id='utm_id' name='utm_id' value="${landingpageForm.utm_id}"> <!-- 链接唯一id  -->
						<input type="hidden" id='utm_medium' name='utm_medium' value="${landingpageForm.utm_medium}"> <!-- 推广方式 -->
						<input type="hidden" id='utm_campaign' name='utm_campaign' value="${landingpageForm.utm_campaign}"> <!-- 推广计划-->
						<input type="hidden" id='utm_content' name='utm_content' value="${landingpageForm.utm_content}"> <!-- 推广单元 -->
						<input type="hidden" id='utm_term' name='utm_term' value="${landingpageForm.utm_term}"> <!-- 关键词 -->
						<input type="hidden" id='inittime' name='inittime' value="${inittime}"> <!-- 初始化时间 -->
						<input type="hidden" name="tokenCheck" id="tokenCheck" value="" /> 
						<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
						<!-- 加密工具类需要的两个参数 -->
						<input type="hidden" id="pubmodules" value="${pubmodules }" name="pubmodules" />
						<input type="hidden" id="pubexponent" value="${pubexponent }" name="pubexponent" />
						<!-- 加密password字段 -->
						<input type="hidden" id="newRegPsw" value="" name="newRegPsw" />

		    			<div class="form-main">	
		    				<label>
		    					<span class="tit">手机号</span>
		    					<input type="text" name="newRegPhoneNum" id="telnum" maxlength="11" placeholder="输入手机号"/><!-- 旧版JSP用 newRegPhoneNum -->
		    				</label>
		    				<!-- 滑块验证 -->
		    				<div id="slide-box">
			    				<div id="slideUnlock">
							        <input type="hidden" value="" name="lockable" id="lockable"/>
							        <div id="slider">
							            <span id="label"><i></i></span>
							            <span class="slideunlock-lable-tip">移动滑块验证</span>
							            <span id="slide-process"></span>
							        </div>
							        
							    </div>
						    </div>
		    				<label>
		    					<span class="tit">手机验证</span>
		    					<input type="text" autocomplete="off" name="newRegVerify" id="code" maxlength="6" placeholder="输入验证码"/><!-- 旧版JSP用 newRegVerify -->
		    					<input type="hidden" id="validCodeType" name="validCodeType" value="TPL_ZHUCE" />
		    					<span class="get-code disable">
		    						<em class="rule">
		    						</em>获取验证码
		    					</span>
		    				</label>
		    				<label>
		    					<span class="tit">密码</span>
		    					<input type="password" name="password" maxlength="16" id="password"  style="width: 210px;" placeholder="8-16位数字、字母或者符号组合" onkeyup="this.value=this.value.replace(/\s+/g,'')"/>
		    				</label>
		    				<div class="strength-box" style="display: none;">
		    					<div class="strip"></div>
		    					<div class="strip"></div>
		    					<div class="strip"></div>
		    					<span></span>
		    				</div>
		    				<label>
		    					<span class="tit">推荐人</span>
		    				   	<c:if test="${refferUserId == ''|| refferUserId == null }">
		    				   		<input type="text" name="newRegReferree"  value="${refferUserId}" maxlength="11" placeholder="输入推荐人手机号或推荐码"/>
								</c:if>
								<c:if test="${refferUserId != '' && refferUserId != null }">
		    				   		<input type="text" name="newRegReferree" value="${refferUserId}" maxlength="11" placeholder="输入推荐人手机号或推荐码" readonly="readonly"/>
								</c:if>
		    				</label>	    				
		    				<div class="read-protocol">
		    					<input type="checkbox" value="1" id="protocol"/>
		    					<div class="term-checkbox"></div>
			    					<span>我已阅读并同意 </span>
			    					<span class="relevant">相关协议</span>
			    					<div class="agreement">
			    						<a href="${ctx}/agreement/registrationProtocol.do" target="_blank">《网站注册协议》</a><br/>
		    							<a href="${ctx}/agreement/privacyClause.do" target="_blank">《隐私保护规则》</a><br/>
		    						</div>
		    				</div>
								<a class="sub disable" style="margin-top: 5px;">注册</a>
		    				</div>
		    			</form>
                </div>
            </div>
            <div class="control-box">
            	<div class="product-box">
            		<img src="/dist/images/landing/product.jpg" alt="" />
            		<div class="box-div-main">
            			<img src="/dist/images/landing/adiv-1.jpg?version=20171017" alt="" />
                        <a href="/bank/web/borrow/newBorrowList.do" class="div1-main">我要出借</a>
                    </div>
                    <div class="box-div-main">
                    	<img src="/dist/images/landing/adiv-2.jpg?version=20171017" alt="" />
                        <a href="/bank/web/borrow/initBorrowList.do" class="div2-main">我要出借</a>
                        <a href="/bank/user/credit/initWebCreditPage.do" class="div3-main">我要出借</a>
                    </div>
                    <div class="box-div-main">
                    	<img src="/dist/images/landing/adiv-4.jpg?version=20171017" alt="" />
                        <a href="/hjhplan/initPlanList.do" class="div4-main">我要出借</a>
                    </div>
                    <div class="coin-icon s1">
            		<img src="/dist/images/landing/jinbi.jpg" alt="" />
	            	</div>
	            	<div class="coin-icon s2">
	            		<img src="/dist/images/landing/jinbi.jpg" alt="" />
	            	</div>
            	</div>
            </div>
            <div class="news-user">
            	<div class="news-box">
	            	<img src="/dist/images/landing/newscon-tit.jpg" />
	            	<c:if test="${isLogin eq 0 }">
		                <div class="newscon-box">
		                    <div class="newcomer-box fl">
		                    	<img width="454" height="520" src="/dist/images/landing/hongbao-img1.png" alt="" />
		                    	<a href="${ctx }/user/regist/init.do" class="get-btn">立即领取</a>
		                    </div>
		                    <div class="newcomer-box f2">
		                    	<img width="454" height="520" src="/dist/images/landing/hongbao-img2.png?version=20171016" alt="" />
		                    	<a href="${ctx }/user/regist/init.do" class="get-btn">立即出借</a>
		                    </div>
		                    <div class="clearboth"></div>
		                </div>
	                </c:if>
	                <c:if test="${isLogin eq 1 }">
		                <div class="newscon-box">
		                    <div class="newcomer-box fl">
		                    	<img width="454" height="520" src="/dist/images/landing/hongbao-img1.png" alt="" />
		                    	<a href="/bank/web/borrow/newBorrowList.do" class="get-btn">立即领取</a>
		                    </div>
		                    <div class="newcomer-box f2">
		                    	<img width="454" height="520" src="/dist/images/landing/hongbao-img2.png" alt="" />
		                    	<a href="/bank/web/borrow/newBorrowList.do" class="get-btn">立即出借</a>
		                    </div>
		                    <div class="clearboth"></div>
		                </div>
	                </c:if>
	                <img src="/dist/images/landing/hongbao-img3.jpg" alt="" />
	                <div class="coin-icon s3">
	            		<img src="/dist/images/landing/jinbi.jpg" alt="" />
	            	</div>
	            	<div class="coin-icon s4">
	            		<img src="/dist/images/landing/jinbi.jpg" alt="" />
	            	</div>
	            </div>
            </div>
            <div class="safe-fond">
            	<img src="/dist/images/landing/safe-fond-20181221.jpg" alt="" />
            </div>
            <div class="bottom-txt">
            	<p>
            		&copy;  汇盈金服 All rights reserved  惠众商务顾问（北京) 有限公司<br />
					京ICP备13050958号  公安安全备案证：37021313127<br />
					市场有风险  出借需谨慎
            	</p>
            	
            </div>
        </div>
    </section>
<%--  <jsp:include page="/footer.jsp"></jsp:include> --%>
    <script src="${cdn}/dist/js/lib/jquery.min.js"></script>
    <script src="${cdn }/dist/js/lib/jquery.placeholder.min.js"></script>
	<script src="${cdn }/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn }/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn }/dist/js/lib/jquery.slideunlock.min.js"></script>
	<script src="${cdn }/dist/js/idangerous.swiper.min.js"></script>
	<script src="${cdn}/dist/js/login/landing-register.js?version=${version}"></script>
    <script src="${cdn }/dist/js/landing-page.js?version=${version}"></script>
	<script src="${cdn }/dist/js/login/RSA.js?version=${version}"></script>
	<script src="${cdn }/dist/js/login/Barrett.js?version=${version}"></script>
	<script src="${cdn }/dist/js/login/BigInt.js?version=${version}"></script>
   <script type="text/javascript">
	       $(function(){ $('input, textarea').placeholder(); });
           $("#newRegReferree").val(getQueryString('utm_referrer'));
           function getQueryString(name) {
               var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
               var r = window.location.search.substr(1).match(reg);
               if (r != null) {
                   return unescape(r[2]);
               }
               return null;
           }
   </script>
</body>
</html>