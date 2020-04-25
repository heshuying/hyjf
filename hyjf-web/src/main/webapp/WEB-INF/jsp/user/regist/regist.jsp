<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-cmn-Hans">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<article class="main-content">
	    	<div class="container result" style="padding-top:1px">
	    		<!-- start 内容区域 -->
	    		<div class="register">
	    			<c:if test="${userType==2}">
	    			<p class="go-login"><a href="${ctx }/user/regist/init.do">个人用户注册 ></a></p>
	    			</c:if>
	    			<c:if test="${userType==1}">
	    			<p class="go-login"><a href="${ctx }/user/regist/init.do?userType=2">企业用户注册 ></a></p>
	    			</c:if>
	    			<div class="re-content">
		    			<form action="${ctx}/user/regist/regist.do" name="regForm" id="registerForm" method="post"><!-- 旧版JSP 使用  regForm -->
		    				<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
		    				<input type="hidden" name="userType" id="userType" value="${userType}" />
		    				<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}"/>
		    				<!-- 通过着陆页注册 补充字段 -->
		    				<input type="hidden" id="utm_id" value="${utm_id }" name="utm_id" />
							<input type="hidden" id="utm_source" value="${utm_source }" name="utm_source" />
							<input type="hidden" id="pubmodules" value="${pubmodules }" name="pubmodules" />
							<input type="hidden" id="pubexponent" value="${pubexponent }" name="pubexponent" />
							<input type="hidden" id="newRegPsw" value="" name="newRegPsw" />
							
		    				<label>
		    					<span class="tit">手机号</span>
		    					<input type="text" name="newRegPhoneNum" id="telnum" maxlength="11" placeholder="请输入手机号"/><!-- 旧版JSP用 newRegPhoneNum -->
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
		    					<input type="text" name="newRegVerify" id="code" maxlength="6" placeholder="请输入验证码"/><!-- 旧版JSP用 newRegVerify -->
		    					<input type="hidden" id="validCodeType" name="validCodeType" value="TPL_ZHUCE" />
		    					<span class="get-code disable">
		    						<em class="rule">
		    						</em>获取验证码
		    					</span>
		    				</label>
		    				
		    				<label>
		    					<span class="tit">密码</span>
		    					<input style="width:205px" type="password" name="password" maxlength="16" id="password" placeholder="8-16位数字、字母或者符号组合" onkeyup="this.value=this.value.replace(/\s+/g,'')"/>
		    				</label>
		    				<div class="strength-box" style="display: none;">
		    					<div class="strip"></div>
		    					<div class="strip"></div>
		    					<div class="strip"></div>
		    					<span></span>
		    				</div>
		    				<c:choose>
		    					<c:when test="${empty newRegReferree  }"><!-- 如果通过别的途径进来注册，从session取推荐人 -->
				    				<label>
				    					<span class="tit">推荐人</span>
				    					<input type="text" style="width:205px" name="newRegReferree" id="newRegReferree" value="" maxlength="11" placeholder="请输入推荐人手机号或推荐码"/>
				    				</label>
		    					</c:when>
		    					<c:otherwise>
		    						<!-- 有推荐人的情况  -->
		    						<label>
				    					<span class="tit">推荐人</span>
				    					<input type="text" name="newRegReferree" style="width:205px" id="newRegReferree" value="${newRegReferree }" readonly="readonly" maxlength="11" placeholder="请输入推荐人手机号或推荐码"/>
				    				</label>
		    					</c:otherwise>
		    				</c:choose>
		    				
		    				<!-- <div class="read-protocol">
		    					<input type="checkbox" value="1" id="protocol"/>
		    					<div class="term-checkbox"></div>
			    					<span>我已阅读并同意 </span>
			    					<span class="relevant">相关协议</span>
			    					<div class="agreement">
			    						<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=zcxy" target="_blank">${zcxy}</a><br/>
		    							<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=ptystk" target="_blank">${ptystk}</a><br/>
		    						</div>
		    				</div>
		    				-->
			    			<c:if test="${not empty activity68 && activity68 eq '1'}">
								<input type="hidden" id="activity68" value="1" name="activity68" />
							</c:if>
							<c:if test="${userType==2}">
			    			<a class="sub disable">企业注册</a>
			    			</c:if>
			    			<c:if test="${userType==1}">
			    			<a class="sub disable">个人注册</a>
			    			</c:if>
		    				
		    			</form>
	    			</div>
	    			<div class="red-banner swiper-container" >
	    				<div class="swiper-wrapper">
	    					<a class="swiper-slide"><img src="${cdn}/dist/images/login/banner@2x.png" width="374px"></a>
	    					<a class="swiper-slide"><img src="${cdn}/dist/images/login/register-b@2x.png" width="374px"></a>
	    				</div>
	    			</div>
			    </div>
	    		<!-- end 内容区域 --> 
	    	</div>
	    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<div class="alert" id="readAgreement" style="margin-top: -154.5px;margin-left:-360px;width:760px;display: none;border-radius: 4px;">
	    <div class='content prodect-sq' style="text-align: left;padding:50px 50px 30px ">
			<h3 style="margin-top: 0;margin-bottom:25px;text-align: center;font-size: 24px;font-weight: normal;">汇盈金服用户注册协议和平台隐私条款</h3>
			<p style="line-height:25px;letter-spacing: 0.5;margin-bottom: 20px;">审慎阅读：在您注册成为汇盈金服用户的过程中，您需要完成我们的注册流程，并通过点击同意的形式在线签署以下的协议，请您务必仔细阅读、充分理解协议中的条款内容后，再点击同意并继续。</p>
			<p style="margin:14px 0"><a href="${ctx}/agreement/goAgreementPdf.do?aliasName=zcxy" target="_blank" class="highlight" style="text-decoration: none;">${zcxy}</a></p>
			<p style="margin:14px 0"><a href="${ctx}/agreement/goAgreementPdf.do?aliasName=ptystk" target="_blank"  class="highlight" style="text-decoration: none;">${ptystk}</a></p>
			<p style="line-height:25px;letter-spacing: 0.5;margin-top: 20px;margin-bottom: 10px;">* 友情提示：如果您不同意上述协议或者其中任何条款约定，请您停止注册。如您按照注册流程提示填写信息、阅读点击并同意上述协议且完成全部注册流程后，即表示您已充分阅读、理解并接受协议的全部内容。汇盈金服可以依据以上的隐私政策内容来处理您的个人信息。</p>
	    	<p style="line-height:14px;letter-spacing: 0.5;color: #999999;font-size: 12px;">如您对以上协议内容有任何疑问，您可随时与汇盈金服联系，在线客服电话：400-900-7878</p>
	    	
	    </div>
	    <div class="btn-group">
	    	<a href="/" style="display: inline-block;background: #CCCCCC;border-radius: 4px;color: #fff;height: 40px;width: 120px;line-height: 38px;text-decoration: none;margin-right: 30px;">不同意</a>
	        <a class="btn btn-primary single" style="height: 40px;width: 280px;border-radius: 4px;margin-bottom: 50px;line-height: 38px;" id="readAgreementConfirm">同意并继续</a>
	    </div>
	</div>
	<script src="${cdn }/dist/js/lib/swiper3.jquery.min.js"></script>
	<script src="${cdn }/dist/js/lib/jquery.placeholder.min.js"></script>
	<script src="${cdn }/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn }/dist/js/lib/jquery.metadata.js"></script>
	<script src="${cdn }/dist/js/lib/jquery.slideunlock.min.js"></script>
    <script src="${cdn }/dist/js/login/register.js?version=${version}"></script>
    <script src="${cdn }/dist/js/login/RSA.js?version=${version}"></script>
    <script src="${cdn }/dist/js/login/Barrett.js?version=${version}"></script>
    <script src="${cdn }/dist/js/login/BigInt.js?version=${version}"></script>
    <script src="${ctx}/dist/js/lib/moment.min.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
	       $(function(){ $('input, textarea').placeholder(); 
	       		
	       });
	       var mySwiper = new Swiper('.swiper-container', {
				autoplay: 3000,//可选选项，自动滑动
				loop:true
			})
   			$('.red-banner').mouseenter(function(e){
				mySwiper.stopAutoplay();
			}).mouseleave(function(e){
				mySwiper.startAutoplay();
			})
			utils.alert({
        		id:'readAgreement',
        		btntxt:'同意并继续',
        		type:'confirm',
        		fnconfirm:function(){
					utils.alertClose('readAgreement');
					$(".term-checkbox").addClass("checked");
					$("#registerForm .sub").removeClass('disable')
				}
        	})
  </script>
  <script>
  /* 注册追踪
  regist_from */
	sa && sa.track('regist_from',{
	  entrance: pressetProps.$referrer    // TODO : 枚举入口
	})
	
	if("${utm_source }" != ""){
		/*
		  分享
		  share
		  @params handle_time 操作时间  Date  
		  @params $title  页面标题  String 提现申请页，充值申请页，首页，产品详情页，支付页，投资成功页，修改密码页
		  @params share_content 分享内容  String app，产品详情，邀请注册
		  @params share_channel 分享方式  String 微信，朋友圈，QQ，微博，扫码分享
		*/
		sa && sa.track('share',{
		  handle_time:  moment().format('YYYY-MM-DD HH:mm:ss.SSS'),   
		  share_content: '我的奖励',
		  share_channel: '扫码分享'
		})
	}
  </script>
</body>

</html>