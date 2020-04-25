<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content p-top-0">
        <div class="container">
            <!-- start 内容区域 --> 
            <div class="acc-set  P-mtp20  P-mtb80">
            	<ul class="acc-set-tab clear">
            		<li class="tab-active" style="width: 100%;border: 0;">账户信息</li>
            		<!-- <li>我的银行卡</li> -->
            	</ul>
            	<input type="hidden" name="roleId" id="roleId" value="${roleId}" />
            	<ul class="acc-set-item">
            		<li class="acc-set-1 acc-set-item-li acc-set-active">
            			<div class="acc-set-1-top clear">
            				<div class="fl">
            					<a href="${ctx}/user/safe/uploadAvatarInitAction.do">
	            					<c:choose>
										 <c:when test="${iconUrl == null || iconUrl == ''}">
											<img id="faceUrl" src="${cdn}/img/default.png" alt="" class="acc-set-1-top-img">
										 </c:when>
										 <c:otherwise>
											<img id="faceUrl" src="${iconUrl}" alt="" class="acc-set-1-top-img">
										 </c:otherwise>
									</c:choose>
            					</a>
            					<p class="P-mtp6">${webViewUser.username}</p>
            				</div>
            				<div class="fr">
            					<div class="friend-code-div partner-how-code">
									<input type="hidden" id="qrcodeValue" value="${inviteLink}" />
									<div id="qrcode" class="friend-code-img-1"></div>
								</div>
            					<p>我的二维码</p>
            				</div>
            			</div>
            			<ul class="acc-set-1-list">
							<c:if test="${bankOpenAccount != null && bankOpenAccount.account != ''}">
	            				<li>
									<span class="acc-set-1-list-1">银行存管账户</span>
	            					<span class="acc-set-1-list-2">江西银行：${bankOpenAccount.account }</span>
	            					<span class="acc-set-1-list-3 act-set-setted">
	            						<i class="icon iconfont icon-duihao"></i>已开通
	            					</span>
	            					<input type="hidden" id="account_hidden" value="${bankOpenAccount.account}" />
	            				</li>
							</c:if>
							<c:if test="${bankOpenAccount == null || bankOpenAccount.account == ''}">
								<li>
	            					<span class="acc-set-1-list-1">银行存管账户</span>
	            					<span class="acc-set-1-list-2">开通银行存管，为资金安全保驾护航</span>
	            					<span class="acc-set-1-list-3 act-set-not-setted">
	            						<span><i class="icon iconfont icon-tanhao"></i>未开通</span>
	            						<span class=""><a href="${ctx}/bank/web/user/bankopen/init.do">开通</a></span>
	            					</span>
	            				</li>
							</c:if>
							
							<c:if test="${webViewUser.chinapnrUsrid != null && webViewUser.chinapnrUsrid != ''}">
	            				<li>
									<span class="acc-set-1-list-1">汇付托管账户</span>
	            					<span class="acc-set-1-list-2">汇付天下：${chinapnr.chinapnrUsrid }</span>
	            					<span class="acc-set-1-list-3 act-set-setted">
	            						<span><i class="icon iconfont icon-duihao"></i>已开通</span>
	            						<span class=""><a href="${ctx }/chinapnrLogin/login.do">查看</a></span>
	            						
	            					</span>
	            				</li>
							</c:if>
							<c:if test="${truename != null && truename != ''}">
	            				<li>
	            					<span class="acc-set-1-list-1">实名信息</span>
	            					<span class="acc-set-1-list-2">${truename }丨${idcard }</span>
	            					<span class="acc-set-1-list-3 act-set-setted">
	            						<i class="icon iconfont icon-duihao"></i>已认证
	            					</span>
	            				</li>
							</c:if>
            				<li>
            				<c:if test="${mobile == null || mobile == '' }">
            					<span class="acc-set-1-list-1">绑定手机</span>
            					<span class="acc-set-1-list-2">绑定手机号，账户更安全</span>
            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<span><i class="icon iconfont icon-tanhao"></i>未绑定</span>
            						<span class=""><a href="${ctx}/bank/user/transpassword/initMobile.do">绑定</a></span>
            					</span>
            				</c:if>
            				<c:if test="${mobile != null && mobile != '' }">
            					<span class="acc-set-1-list-1">绑定手机</span>
            					<span class="acc-set-1-list-2">${mobile }</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已绑定</span>
            						<span class="" style="height:0"><%-- <a href="${ctx}/bank/user/transpassword/initMobile.do">修改</a> --%></span>
            					</span>
            				</c:if>
            				</li>
            				<li>
            				<c:if test="${bankCard == null || bankCard  == '' }">
            					<span class="acc-set-1-list-1">我的银行卡</span>
            					<span class="acc-set-1-list-2">您尚未绑定银行卡，请先绑定</span>
            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<span><i class="icon iconfont icon-tanhao"></i>未绑定</span>
            						<span class=""><a href="${ctx}/bank/web/bindCard/myCardInit.do">管理</a></span>
            					</span>
            				</c:if>
            				<c:if test="${bankCard  != null && bankCard != '' }">
            					<span class="acc-set-1-list-1">我的银行卡</span>
            					<span class="acc-set-1-list-2">${bankCard.cardNo }</span>
            					<input type="hidden" id='cardId' value='${bankCard.id}'>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已绑定</span>
            						<span class=""><a href="${ctx}/bank/web/bindCard/myCardInit.do">管理</a></span>
            					</span>
            				</c:if>
            				</li>
            				<li>
	            				<c:choose>
									<c:when test="${email==null||email=='' }">
										<span class="acc-set-1-list-1">绑定邮箱</span>
		            					<span class="acc-set-1-list-2">及时获取相关协议和资讯</span>
		            					<span class="acc-set-1-list-3 act-set-not-setted">
	            						<span><i class="icon iconfont icon-tanhao"></i>未绑定</span>
	            						<span class=""><a href="${ctx}/user/safe/bindingEmail.do">绑定</a></span>
	            					</span>
									</c:when>
									<c:otherwise>
										<span class="acc-set-1-list-1">绑定邮箱</span>
		            					<span class="acc-set-1-list-2">${email }</span>
		            					<span class="acc-set-1-list-3 act-set-setted">
	            						<span><i class="icon iconfont icon-duihao"></i>已绑定</span>
	            						<span class=""><a href="${ctx}/user/safe/bindingEmail.do?isUpdate=isUpdate">修改</a></span>
	            					</span>
									</c:otherwise>
								</c:choose>
            				</li>
            				
            				<li>
            					<span class="acc-set-1-list-1">登录密码</span>
            					<span class="acc-set-1-list-2">为了您的账户安全，请定时修改密码<%-- <hyjf:datetime value="${lastTime}"></hyjf:datetime> --%></span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="${ctx}/user/safe/modifyCode.do">修改</a></span>
            					</span>
            				</li>
            				           				
            				<li>
            					<c:choose>
								<c:when test="${isSetPassword == 0 }">
									<span class="acc-set-1-list-1">交易密码</span>
	            					<span class="acc-set-1-list-2">设置交易密码，为资金安全保驾护航</span>
	            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<span><i class="icon iconfont icon-tanhao"></i>未设置</span>
            						<c:if test="${bankOpenAccount == null || bankOpenAccount.account == '' }">
            						<span class=""><a onclick="alertKH()">设置</a></span>
            						</c:if>
            						<c:if test="${ bankOpenAccount != null && bankOpenAccount.account != ''}">
            						<span class=""><a href="${ctx}/bank/user/transpassword/setPassword.do">设置</a></span>
            						</c:if>
            					</span>
								</c:when>
								<c:when test="${isSetPassword == 2 }">
									<span class="acc-set-1-list-1">交易密码</span>
	            					<span class="acc-set-1-list-2">设置交易密码，为资金安全保驾护航</span>
	            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<span><i class="icon iconfont icon-tanhao"></i>未设置</span>
            						<span class=""><a href="${ctx}/bank/user/transpassword/resetPassword.do">处理中</a></span>
								</c:when>
								<c:otherwise>
									<span class="acc-set-1-list-1">交易密码</span>
	            					<span class="acc-set-1-list-2">*********</span>
	            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="${ctx}/bank/user/transpassword/resetPassword.do" target="_blank">修改</a></span>
								</c:otherwise>
								</c:choose>
            				</li>
            				<c:if test="${roleId==1 }">
            				
            				<li>
            				<c:if test="${hjhUserAuth.autoInvesStatus==0}">
            					<span class="acc-set-1-list-1">自动投标</span>
            					<span class="acc-set-1-list-2">使用自动投标功能，体验更多产品</span>
            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<a><i class="icon iconfont icon-tanhao"></i>未授权</a>
            						<c:if test="${isSetPassword == 1 && bankOpenAccount != null && bankOpenAccount.account != ''}">
            							<span class=""><a href="javascript:;" onclick="goPlanAuth()">授权</a></span>
            						</c:if>
            						<c:if test="${isSetPassword == 0 || bankOpenAccount == null || bankOpenAccount.account == ''}">
            							<span class=""><a class="alertPS" style="cursor:pointer;">授权</a></span>
            						</c:if>
            					</span>
            				</c:if>
            				<c:if test="${hjhUserAuth.autoInvesStatus==1}">
            					<span class="acc-set-1-list-1">自动投标</span>
            					<span class="acc-set-1-list-2">到期时间：${hjhUserAuth.autoBidEndTime }</span><!--未授权文案：使用自动投标功能，体验更多产品-->
            					<span class="acc-set-1-list-3 act-set-setted">
            						<a><i class="icon iconfont icon-duihao"></i>已授权</a>
            					</span>
            				</c:if>
            				
            				</li>
            				
            				<li>
        				    <c:if test="${hjhUserAuth.autoCreditStatus==0}">
            					<span class="acc-set-1-list-1">自动债转</span>
            					<span class="acc-set-1-list-2">使用自动债转功能，体验更多产品</span>
            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<a><i class="icon iconfont icon-tanhao"></i>未授权</a>
            						<c:if test="${isSetPassword == 1 && bankOpenAccount != null && bankOpenAccount.account != ''}">
            							<span class=""><a href="javascript:;" onclick="goPlanAuth()">授权</a></span>
            						</c:if>
            						<c:if test="${isSetPassword == 0 || bankOpenAccount == null || bankOpenAccount.account == ''}">
            							<span class=""><a class="alertPS" style="cursor:pointer;">授权</a></span>
            						</c:if>
            					</span>
            				</c:if>
            				<c:if test="${hjhUserAuth.autoCreditStatus==1}">
            					<span class="acc-set-1-list-1">自动债转</span>
            					<span class="acc-set-1-list-2">到期时间：${hjhUserAuth.autoCreditEndTime }</span><!--未授权文案：使用自动投标功能，体验更多产品-->
            					<span class="acc-set-1-list-3 act-set-setted">
            						<a><i class="icon iconfont icon-duihao"></i>已授权</a>
            					</span>
            				</c:if>
            				</li>
            				
            				</c:if>
            				<!--缴费授权start-->
            				<li>
        				    <c:if test="${hjhUserAuth.autoPaymentStatus==0}">
            					<span class="acc-set-1-list-1">服务费授权</span>
            					<span class="acc-set-1-list-2">用于提现，充值，债权转让等服务费收取</span>
            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<a><i class="icon iconfont icon-tanhao"></i>未授权</a>
            						<c:if test="${isSetPassword == 1 && bankOpenAccount != null && bankOpenAccount.account != ''}">
            							<span class=""><a href="javascript:;" onclick="goFeeAuth()">授权</a></span>
            						</c:if>
            						<c:if test="${isSetPassword == 0 || bankOpenAccount == null || bankOpenAccount.account == ''}">
            							<span class=""><a class="alertPS" style="cursor:pointer;">授权</a></span>
            						</c:if>
            					</span>
            				</c:if>
            				<c:if test="${hjhUserAuth.autoPaymentStatus==1}">
            					<span class="acc-set-1-list-1">服务费授权</span>
            					<span class="acc-set-1-list-2">到期时间：${hjhUserAuth.autoPaymentEndTime }</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<a><i class="icon iconfont icon-duihao"></i>已授权</a>
            					</span>
            				</c:if>
            				</li>
            				<!--缴费授权end-->
            				<c:if test="${roleId==2}">
            				<!--还款授权start-->
	            				<li>
	        				    <c:if test="${hjhUserAuth.autoRepayStatus==0}">
	            					<span class="acc-set-1-list-1">还款授权</span>
	            					<span class="acc-set-1-list-2">授权最大还款金额，还款时不能超过最大还款金额</span>
	            					<span class="acc-set-1-list-3 act-set-not-setted">
	            						<a><i class="icon iconfont icon-tanhao"></i>未授权</a>
	            						<c:if test="${isSetPassword == 1 && bankOpenAccount != null && bankOpenAccount.account != ''}">
            								<span class=""><a href="javascript:;" onclick="goFeeAuth()">授权</a></span>
	            						</c:if>
	            						<c:if test="${isSetPassword == 0 || bankOpenAccount == null || bankOpenAccount.account == ''}">
	            							<span class=""><a class="alertPS" style="cursor:pointer;">授权</a></span>
	            						</c:if>
	            					</span>
	            				</c:if>
	            				<c:if test="${hjhUserAuth.autoRepayStatus==1}">
	            					<span class="acc-set-1-list-1">还款授权</span>
	            					<span class="acc-set-1-list-2">到期时间：${hjhUserAuth.autoRepayEndTime }</span>
	            					<span class="acc-set-1-list-3 act-set-setted">
	            						<a><i class="icon iconfont icon-duihao"></i>已授权</a>
	            					</span>
	            				</c:if>
	            				</li>
            				<!--还款授权end-->
	            			</c:if>
            				<li>
            					<span class="acc-set-1-list-1">消息通知</span>
            					<span class="acc-set-1-list-2">及时获取出借资讯和账户资金变动通知</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="${ctx}/user/safe/messageNotificationInitAction.do">修改</a></span>
            					</span>
            				</li>
            				<li>
            					<c:choose>
									<c:when test="${ifEvaluation == 1}">
									    <span class="acc-set-1-list-1">风险测评</span>
		            					<span class="acc-set-1-list-2"><span class="acc-set-risk-type">${userEvalationResult.typeString }</span>&nbsp;&nbsp;&nbsp;&nbsp;到期时间：${evaluationExpireDate}<!--了解自己风险承受能力，降低投资风险--></span>
		            					<span class="acc-set-1-list-3 act-set-setted">
		            						<span><i class="icon iconfont icon-duihao"></i>已测评</span>
		            						<span class=""><a href="${ctx}/financialAdvisor/questionnaireInit.do">重测</a></span>
		            					</span>
									</c:when>
									<c:when test="${ifEvaluation == 0}">
									    <span class="acc-set-1-list-1">风险测评</span>
		            					<span class="acc-set-1-list-2">了解自己风险承受能力，降低出借风险 <!--了解自己风险承受能力，降低投资风险--></span>
		            					<span class="acc-set-1-list-3 act-set-not-setted">
		            						<span><i class="icon iconfont icon-tanhao"></i>未测评</span>
		            						<span class=""><a href="${ctx}/financialAdvisor/financialAdvisorInit.do">测评</a></span>
		            						                             						                  
		            					</span>
									</c:when>
									<c:when test="${ifEvaluation == 2}">
									    <span class="acc-set-1-list-1">风险测评</span>
		            					<span class="acc-set-1-list-2">已到期 &nbsp;&nbsp;&nbsp;&nbsp;到期时间：${evaluationExpireDate} <!--了解自己风险承受能力，降低投资风险--></span>
		            					<span class="acc-set-1-list-3 act-set-not-setted">
		            						<span><i class="icon iconfont icon-tanhao"></i>已到期</span>
		            						<span class=""><a href="${ctx}/financialAdvisor/financialAdvisorInit.do">测评</a></span>
		            						                             						                  
		            					</span>
									</c:when>
								</c:choose>
            				</li>
            				<li>
             					<c:choose>
								<c:when test="${webViewUser.usersContract==null||webViewUser.usersContract.rlName==null||webViewUser.usersContract.rlName==''}">
									<span class="acc-set-1-list-1">紧急联系人</span>
	            					<span class="acc-set-1-list-2">紧急情况无法联系您的时候，优先联系您的亲友</span>
		            				<span class="acc-set-1-list-3 act-set-not-setted">
	            						<span><i class="icon iconfont icon-tanhao"></i>未设置</span>
	            						<span class=""><a href="${ctx}/user/safe/contactSetInit.do">设置</a></span>
	            					</span>
								</c:when>
								<c:otherwise>
	           						<span class="acc-set-1-list-1">紧急联系人</span>
	            					<span class="acc-set-1-list-2">${fn:substring(webViewUser.usersContract.rlName,0,1)}**</span>
	            					<span class="acc-set-1-list-3 act-set-setted">
	            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
	            						<span class=""><a href="${ctx}/user/safe/contactSetInit.do">修改</a></span>
	            				    </span>
								</c:otherwise>
								</c:choose>
            					
            				</li>
            			</ul>
            		</li>
            		<li class="acc-set-2 acc-set-item-li">
            			<ul>
            				<!--未绑卡-->
            				<li class="not-bind-card">
            					<img src="${cdn }/dist/images/acc-set/acc-add-cards@2x.png" width="70"/>
            					<p>添加银行卡</p>
            				</li>
            				<!--已绑卡-->
            				<li class="binded-card">
            					
            				</li>
            			</ul>
            		</li>
            	</ul>
            </div>
                      
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn }/dist/js/lib/jquery.qrcode.js"></script>
	<script src="${cdn }/dist/js/lib/cropper.min.js"></script>
	<script src="${cdn }/dist/js/lib/qrcode.js"></script>
	<script src="${cdn }/dist/js/acc-set/code.js?version=${version}"></script>
	<script src="${cdn }/dist/js/acc-set/acc-set.js?version=${version}"></script>
	<script src="${ctx}/dist/js/lib/moment.min.js" type="text/javascript" charset="utf-8"></script>
	<!-- 设置定位  -->
	<script>
		setActById("accountSet");
		document.cookie = 'authUrl='+window.location.href+';path=/'
		document.cookie = 'authPayUrl='+window.location.href+';path=/'
		document.cookie = 'authrePayUrl='+window.location.href+';path=/'
		document.cookie = 'beforeUrl='+window.location.href+';path=/'
		// 去计划授权
		function goPlanAuth(){
			window.location.href = '/bank/user/auth/paymentauthpageplus/authRequirePage.do';
		}
        // 去服务费授权
        function goFeeAuth(){
			window.location.href = '/bank/user/auth/paymentauthpageplus/authRequirePage.do';
        }
    $('.alertPS').click(function(){
    	var content = "";
    	var btntxt = "";
    	var fnconfirm  = "";
    	if($("#account_hidden") && $("#account_hidden").val()&& $("#account_hidden").val().length>5){
    		content = '请先设置交易密码。';
    		btntxt = "去设置";
    		fnconfirm  = "/bank/user/transpassword/setPassword.do";
    	}else{
    		content = "您尚未开户，请先去开户。";
    		btntxt = "去开户";
    		fnconfirm  = "/bank/web/user/bankopen/init.do";
    	}
    	utils.alert({
			id:"setPassword",
			content:content,
			btntxt:btntxt,
			fnconfirm:function(){
				//跳转到对应的链接
				window.location.href = webPath + fnconfirm;
			}
		});
    })
    function alertKH(){
    	utils.alert({
			id:"setPassword",
			content:"您尚未开户，请先去开户。",
			btntxt:"去开户",
			fnconfirm:function(){
				//跳转到对应的链接
				window.location.href = webPath + "/bank/web/user/bankopen/init.do";
			}
		});
    }

    //解绑卡
	var stageNum; //暂存要删除的银行卡
	$(document).ready(function() {
		$("#unbundling").click(function(){
			//删除银行卡操作
			stageNum = $('#cardId').val();
			var content = "您确认要解绑这张银行卡吗？";
			utils.alert({
				id:"deleteCardDialog",
				type:"confirm",
				content:content,
				fnconfirm:function(){
                    /*
					  解绑提交
					  unbind_submit
					  @params bank_name 银行名称  String  eg:招商银行，工商银行，建设银行，农业银行
					  @params unbind_time 解绑时间  Date
					*/
                    sa && sa.track('unbind_submit',{
                        // entrance: document.referrer,
                        bank_name: "${bankname}",
                        unbind_time: moment().format('YYYY-MM-DD HH:mm:ss.SSS')
                    })
					var card=$('#cardId').val()
					setTimeout(function(){
                        window.location.href='/bank/web/deleteCardPage/deleteCardPage?cardId='+card;
					},300)
				}
			});
		});
		
	});


	</script>
</body>
</html>