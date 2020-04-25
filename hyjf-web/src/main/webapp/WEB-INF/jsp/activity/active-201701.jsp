<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>汇盈金服贺新春 欢天喜地迎财神 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/active/active-201701.css" />
		<script type="text/javascript">
			var isLogin = "${isLogin}";
			var actStatus = "${actStatus}";
		</script>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	<div class="active-201701-pages">
        <div class="active-201701-1"></div>
        <div class="page <c:if test="${tabFlag==1}">active</c:if>" id="page1">
            <div class="active-201701-2">
                <div class="container-1200">
                    <div class="page-items">
                        <div class='page-item page1 active'></div>
                        <div class="page-item page2"></div>
                    </div>
                    <div class="active-range">2017年1月20日 至 2017年2月4日</div>
                    <div class="active-summary">
                        活动期内，用户每集齐一套<span>“金”“鸡”“纳”“福”</span>财神卡即可获得一次点爆竹赢大奖机会，100%中奖。
                        <br/>除各式代金券奖励外，更有iPhone 7 Plus等你来赢！
                    </div>
                </div>
            </div>
            <div class="active-201701-3">
                <div class="container-1200">
                    <div class="craker-box">
                        <div class="card-area">
                            <div class="card-title">点击已得到的财神卡可送给好友哦！</div>
                            <c:if test="${isLogin eq 1 }">
	                            <div class="card nocard" id="cardJin">
	                                <div class="count"><small>×</small><span class="count-num">0</span></div>
	                                <div class="hoverbox" data-type="1" data-text="金">送财神卡
	                                    <br/>给好友</div>
	                            </div>
	                            <div class="card nocard" id="cardJi">
	                                <div class="count"><small>×</small><span class="count-num">0</span></div>
	                                <div class="hoverbox" data-type="2" data-text="鸡">送财神卡
	                                    <br/>给好友</div>
	                            </div>
	                            <div class="card nocard" id="cardNa">
	                                <div class="count"><small>×</small><span class="count-num">0</span></div>
	                                <div class="hoverbox" data-type="3" data-text="纳">送财神卡
	                                    <br/>给好友</div>
	                            </div>
	                            <div class="card nocard" id="cardFu">
	                                <div class="count"><small>×</small><span class="count-num">0</span></div>
	                                <div class="hoverbox" data-type="4" data-text="福">送财神卡
	                                    <br/>给好友</div>
	                            </div>
                            </c:if>
                            <c:if test="${isLogin eq 0 }">
	                            <div class="card unlogin" id="cardJin">
	                                <div class="hoverbox" data-type="1" data-text="金">登录后
	                                    <br/>查看</div>
	                            </div>
	                            <div class="card unlogin" id="cardJi">
	                                <div class="hoverbox" data-type="2" data-text="鸡">登录后
	                                    <br/>查看</div>
	                            </div>
	                            <div class="card unlogin" id="cardNa">
	                                <div class="hoverbox" data-type="3" data-text="纳">登录后
	                                    <br/>查看</div>
	                            </div>
	                            <div class="card unlogin" id="cardFu">
	                                <div class="hoverbox" data-type="4" data-text="福">登录后
	                                    <br/>查看</div>
	                            </div>
                            </c:if>
                        </div>
                        <div class="craker-area">
                            <div id="crakerbox">
                                <canvas id="crakerCVS" width="553" height="768"></canvas>
                                <div class="crakerbtn">点爆竹，赢大奖</div>
                            </div>
                            <!-- 活动结束显示 -->
                            <c:if test="${actStatus eq 2}">
                            	<div class="craker-active-over"></div>
                            </c:if>
                        </div>
                        <div class="clearfix"></div>
                        <div class="craker-overlayer active-overlayer"></div>
                        <!-- 送好友财神卡 -->
                        <div class="card-pop">
                            <div class="pop-title">送财神卡给好友</div>
                            <div class="info">已选择<span>“金”</span>字财神卡</div>
                            <div class="form">
                                <input type="text" name="card-mobile" id="cardMobile" class="card-input" placeholder="输入好友手机号">
                                <div class="verf-txt"></div>
                                <div class="success-txt"></div>
                                <div class="card-btn confirm">确 认</div>
                                <div class="card-btn cancel">取 消</div>
                                <div class="card-btn done">确 认</div>
                            </div>
                        </div>
                        <!-- 获得奖励 -->
                        <div class="craker-pop success">
                            <div class="pop-title">恭喜获得</div>
                            <div class="pop-content">
                                <div class="pop-closer" onclick="popUp.hideCrakerPop('success');"></div>
                                <div class="pop-content-title"></div>
                                <div class="pop-content-line"></div>
                                <div class="pop-content-info"></div>
                            </div>
                        </div>
                        <!-- 失败 -->
                        <div class="craker-pop faild">
                            <div class="pop-title">抱 歉</div>
                            <div class="pop-content">
                                <div class="pop-closer" onclick="popUp.hideCrakerPop('faild');"></div>
                                <div class="pop-content-title"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="active-201701-4">
                <div class="container-1200">
                    <div class="reward-summary">
                        <div class="title">财神卡获得方式：</div>
                        <br/>
                        <br/> 1.于活动期内成功注册并开户。
                        <br/>
                        <br/> 2.单笔投资每满1万元。
                        <br/>
                        <br/> 3.通过分享邀请码，每成功邀请1位好友注册开户。
                        <br/>
                        <br/> 4.由好友赠送财神卡。凡拥有任意一张财神卡的用户，皆可通过输入好友注册手机号方式，将自己拥有的财神卡赠送给平台注册好友。
                    </div>
                </div>
            </div>
            <div class="active-201701-5">
                <div class="container-1200">
                    <div class="reward-container">
                        <div class="reward-setting">
                            <div class="title">奖励设置</div>
                            <table width="100%" colspan="0" colpadding="0" border="0">
                                <tr>
                                    <td>奖励名称</td>
                                    <td>备注</td>
                                </tr>
                                <tr>
                                    <td style="color:#81511c;">iPhone 7 Plus</td>
                                    <td style="color:#81511c;">128G，颜色随机</td>
                                </tr>
                                <tr>
                                    <td>10元代金券</td>
                                    <td>（单笔投资达6000元可用）</td>
                                </tr>
                                <tr>
                                    <td style="color:#81511c;">50元代金券</td>
                                    <td style="color:#81511c;">（单笔投资达25000元可用）</td>
                                </tr>
                                <tr>
                                    <td>120元代金券</td>
                                    <td>（单笔投资达50000元可用）</td>
                                </tr>
                                <tr>
                                    <td style="color:#81511c;">200元代金券</td>
                                    <td style="color:#81511c;">（单笔投资达80000元可用）</td>
                                </tr>
                            </table>
                        </div>
                        <div class="reward-send">
                            <div class="title">奖励发放：</div>
                            <br/> 1.代金券奖励将于用户成功抽取后，由系统自动发放至用户汇盈金服账户，用户登陆后可于“优惠券”中查看。
                            <br/>
                            <br/> 2.iPhone 7 Plus将于活动结束后3个工作日内，由客服电话回访核实用户信息后，统一采购发放，如遇货源短缺，发放时间相应顺延。
                        </div>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </div>
            <c:if test="${isLogin eq 1 }">
	            <div class="active-201701-6">
	                <div class="container-1200">
	                    <div class="invite-box left-box">
	                        <div class="title">邀请二维码</div>
	                        <div class="content">
	                        	<input type="hidden" id="qrcodeValue" value="${webCatLink }" />
	                            <div class="qrbox" id="qrcode">
                            	</div>
	                            <div class="content-right">
	                                分享二维码给好友，邀请好友通过扫描二维码成功注册开户，即有机会获得好友赠送的财神卡哟！
	                                <div class="invite-btn" style="margin-top: 60px;"><a href="${ctx}/user/invite/download.do">下载二维码</a></div>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="invite-box right-box">
	                        <div class="title">邀请链接</div>
	                        <div class="content">
	                            复制邀请链接给好友，邀请好友通过您的链接成功注册开户，记得让好友把财神卡赠送给您喔！
	                            <br/>
	                            <span id="invite-link">${inviteLink }</span>
	                            <div class="invite-btn" id="copy" style="margin-top: 8px;">复制链接</div>
	                        </div>
	                    </div>
	                </div>
	            </div>
            </c:if>
            <div class="active-201701-7">
                <div class="container-1200">
                    <div class="notes-container">
                        <div class="notes-title">注：</div>
                        1.本活动仅限投资汇直投、新手汇、汇添金项目金额参与。
                        <br/> 2.通过好友赠送方式获得的财神卡，其种类由好友指定；通过其他方式获得的财神卡，由系统随机发放。
                        <br/> 3.未于活动期内成功使用财神卡抽奖的用户，视为自动放弃获奖机会，奖励不予发放。
                        <br/> 4.本活动所发优惠券奖励均自用户获得之日起15日内有效，过期作废。
                        <br/>
                        <br/>
                        <div class="notes-footer">
                            本活动与Apple Inc.无关
                            <br/> 本活动最终解释权归汇盈金服所有
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="page <c:if test="${tabFlag==2}">active</c:if>" id="page2">
            <div class="active-201701-2">
                <div class="container-1200">
                    <div class="page-items">
                        <div class="page-item page1"></div>
                        <div class="page-item page2 active"></div>
                    </div>
                    <div class="active-range">2017年2月5日 至 2017年2月11日</div>
                    <div class="active-summary">
                        活动期内，凡汇盈金服用户每日登陆皆可获得一次猜灯谜机会，<br/>2分钟内答对谜题即可获得代金券奖励。
                    </div>
                </div>
            </div>
            <div class="active-201701-3">
            	<div class="container-1200">
            		<div class="riddle-container">
	                    <div class="riddle-box">
	                    	<div class="timmer">
	                    		1:59
	                    	</div>
	                    	<div class="riddle-tips">小贴士：<span>“由”字加“了一直(阝)”</span></div>
	                    	<div class="riddle-lantern">
	                    		<div class="lantern-top"></div>
	                    		<div class="lartern-scroll"></div>
	                    		<div class="lantern-bottom"></div>
	                    	</div>

	                    	<div class="riddle-btn start">我要猜灯谜</div>
	                    	<div class="riddle-form">
	                    		<input type="text" class="riddle-input" id="riddleVal" placeholder="请输入答案">
	                    		<div class="riddle-btn confirm">确 认</div>
	                    	</div>
	                    	<div class="riddle-answer">
	                    		谜底：园
	                    	</div>
	                    </div>
	                    <div class="riddle-calender">
	                    
	                    	<div class="calender-title">
	                    	<c:if test="${canReceiveFlag==1}">
	                    		<c:if test="${lanternFestivalFlag==1}">
	                    			您的优惠券已增值为
	                    		</c:if>
	                    		<c:if test="${lanternFestivalFlag==2}">
	                    			恭喜您已获得
	                    		</c:if>
	                    		<span>“价值￥${prizeJine}元代金券”</span>
		                    	<%-- 恭喜您获得<span>“${userPresentCumulativeCoupon}”</span> --%>
		                    	<c:if test="${userPresentCumulativeCouponCount==1}">
				    				一张
				    			</c:if>
				    			<c:if test="${userPresentCumulativeCouponCount==2}">
				    				两张
				    			</c:if>
	                    	</c:if>
	                    	</div>
	                    
	                    	<div class="calender-content">
	                    		<div class="calender-header">2017年2月</div>
	                    		<div class="calender-body">
		                    		<c:forEach items="${userLanternIllumineList}" var="record" begin="0" step="1" varStatus="status">
										<div class="ny2017-6-item">
					    					<c:if test="${record.userAnswerResult==0}">
					    						<div class="item done <c:if test='${record.questionNum==7}'>last</c:if>">
			                    					<div class="date">${record.answerTime }日</div>
			                    				</div>
					    					</c:if>
					    					<c:if test="${record.userAnswerResult==1}">
					    						<div class="item <c:if test='${record.questionNum==7}'>last</c:if>">
			                    					<div class="date">${record.answerTime }日</div>
			                    				</div>
					    					</c:if>
					    					
					    					
					    				</div>
                           			 </c:forEach>
	                    			
	                    			<!-- <div class="item">
	                    				<div class="date">6日</div>
	                    			</div>
	                    			<div class="item">
	                    				<div class="date">7日</div>
	                    			</div>
	                    			<div class="item">
	                    				<div class="date">8日</div>
	                    			</div>
	                    			<div class="item">
	                    				<div class="date">9日</div>
	                    			</div>
	                    			<div class="item">
	                    				<div class="date">10日</div>
	                    			</div>
	                    			<div class="item done last">
	                    				<div class="date">11日</div>
	                    			</div> -->
	                    		</div>
	                    	</div>
	                    </div>
	                	<div class="active-overlayer riddle-overlayer"></div>
	                	<!-- 获得奖励 -->
                        <div class="riddle-pop success">
                            <div class="pop-title">恭喜你</div>
                            <div class="pop-content">
                                <div class="pop-closer" onclick="riddlePopUp.hideRiddlePop('success');window.location.href=window.location.href;"></div>
                                <div class="pop-content-title"></div>
                            </div>
                        </div>
                        <!-- 失败 -->
                        <div class="riddle-pop faild">
                            <div class="pop-title">好可惜</div>
                            <div class="pop-content">
                                <div class="pop-closer" onclick="riddlePopUp.hideRiddlePop('faild');"></div>
                                <div class="pop-content-title">
                                	答题时间已用尽！！<br/>明天再来试试吧！ <!-- 更多精彩，敬请期待！ -->
                                </div>
                            </div>
                        </div>

                        <div class="riddle-pop wrong">
                            <div class="pop-title">很抱歉</div>
                            <div class="pop-content">
                                <div class="pop-closer" onclick="riddlePopUp.hideRiddlePop('wrong');"></div>
                                <div class="pop-content-title">
                                	回答错误！<br/>还有时间，再试试其他答案吧！
                                </div>
                            </div>
                        </div>

                        <div class="riddle-pop start">
                            <div class="pop-title">确认答题</div>
                            <div class="pop-content">
                                <div class="pop-closer" onclick="riddlePopUp.hideRiddlePop('start');"></div>
                                <div class="pop-content-title">今日灯谜即将开启<br/>您准备好了吗？</div>
                                <div class="pop-content-info">点击确认后，2分钟倒计时即刻开始，时间耗尽前回答正确即可获得奖励，中途退出或刷新页面将失去今日答题机会。</div>
                                <div class="pop-content-btn confirm" id="riddleConfirmBtn">确 认</div>
                                
                            </div>
                        </div>
            		</div>
            	</div>
            </div>
            <div class="active-201701-4">
            	<div class="container-1200">
	                <div class="reward-summary">
	                    <div class="title">奖励设置：</div>
	                    <br/>
	                    <br/> 1.首次答对谜题可获得一张10元代金券（单笔投资达20000元可用），<br/>之后每答对一题可为代金券<strong>增值10元</strong>，使用条件不变。
	                    <br/>
	                    <br/> 2.活动最后一天答对谜题者，可获得之前累计最高面值的代金券<strong>张数翻倍</strong>机会，代金券面值不再增值。
	                    <br/>
	                    <br/> 3.首次答对谜题日为活动最后一天者，仅可获得10元代金券奖励一张，代金券数量不翻倍。
                    </div>
                </div>
            </div>
            <div class="active-201701-5">
            	<div class="container-1200">
                    <div class="reward-container">
                        <div class="reward-setting">
                            <div class="title">奖励设置</div>
                            <table width="100%" cellspacing="0" cellpadding="0" border="0">
                                <tbody>
                                <tr>
                                    <td rowspan="2" width="75">灯谜</td>
                                    <td colspan="6" width="480" height="70">前六日共答对题数</td>
                                    <td rowspan="2" width="100">第七日<br/>答对</td>
                                </tr>
                                <tr>
                                    <td height="98">1道</td>
                                    <td>2道</td>
                                    <td>3道</td>
                                    <td>4道</td>
                                    <td>5道</td>
                                    <td>6道</td>
                                </tr>
                                <tr>
                                    <td height="120">奖励</td>
                                    <td class="reward-set">10元<br/>代金券<br/>一张</td>
                                    <td class="reward-set">20元<br/>代金券<br/>一张</td>
                                    <td class="reward-set">30元<br/>代金券<br/>一张</td>
                                    <td class="reward-set">40元<br/>代金券<br/>一张</td>
                                    <td class="reward-set">50元<br/>代金券<br/>一张</td>
                                    <td class="reward-set">60元<br/>代金券<br/>一张</td>
                                    <td>代金券<br/>数量<br/>翻倍</td>
                                </tr>
                                <tr>
                                    <td colspan="2" height="85">奖励门槛</td>
                                    <td colspan="6">满20000元可用</td>
                                </tr>
                                
                                <!-- <tr>
                                    <td style="color:#81511c;">200元代金券</td>
                                    <td style="color:#81511c;">（单笔投资达80000元可用）</td>
                                </tr> -->
                            </tbody></table>
                        </div>
                        <div class="reward-send">
	                        <br/>
	                        <br/>
	                        <br/>
	                        <br/>
                            <div class="title">奖励发放：</div>
                            <br/>
                            代金券奖励将于活动结束后，由系统自动发放至用户汇盈金服账户，用户登陆后可于“优惠券”中查看。


                        </div>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </div>
            <div class="active-201701-7">
                <div class="container-1200">
                    <div class="notes-container">
                    	<br/>
                        <div class="notes-title">注：</div>
                        本活动所发代金券均自用户获得之日起15日内有效，过期作废。
                        <br/>
                        <br/>
                        <div class="notes-footer">
                            本活动与Apple Inc.无关
                            <br/> 本活动最终解释权归汇盈金服所有
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="invite-copy-hint">复制成功，快转发给好友吧!</div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${ctx}/js/qrcode.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.qrcode.js"></script>
	<script src="${ctx}/js/easeljs-0.8.2.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctx}/js/jquery.zclip.min.js"></script>
    <script type="text/javascript">
    var riddleData = {
    		"img" : "${ctx}/img/active/active_201701/pc-${questionImageName}.jpg",
    		"answer" : "${questionAnswer}",
    		"tips" : "小贴士：“${questionHint}”",
    		"questionId" : "${questionId}",
    		"userAnswerFlag" : "${userAnswerFlag}",
    		"showAnswerFlag" : "${showAnswerFlag}",
    		"isLogin" : "${isLogin}"
    		
    			
    	};
    </script>
	<script type="text/javascript" src="${ctx}/js/activity/201701/active-201701-1.js?version=1929874793749"></script>
	<script type="text/javascript" src="${ctx}/js/activity/201701/active-201701-2.js"></script>
	
	
	</body>
</html>