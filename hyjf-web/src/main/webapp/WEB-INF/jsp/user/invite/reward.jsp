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
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content reward-content p-top-0">
        <div class="container">
            <div class="reward-coupon">
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="active" data-couponid="Wsy" panel="0"><a href="javascript:;">未使用</a></li>
                        <li panel="1" data-couponid="Ysy"><a href="javascript:;">已使用</a></li>
                        <li panel="2" data-couponid="Ysx"><a href="javascript:;">已失效</a></li>
                    </ul>
                    <ul class="tab-panels P-cur-de">
                        <li class="active" panel="0">
                            <div class="coupon-list " id="couponWsy">
                                <div class="swiper-container">
                                    <div class="swiper-wrapper">
                                    	<c:if test="${empty wsyList}">
	                                    	<div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无未使用的优惠券</p></div>
	                                    </c:if>
                                    	<c:forEach items="${wsyList }" var="record" begin="0" step="1" varStatus="status">
                                        	<c:if test="${status.index%8==0}">
                                        		<div class="swiper-slide">
                                        	</c:if>
												<c:if test="${record.couponType eq '加息券'}"><div class="coupon-item jxq"></c:if>
												<c:if test="${record.couponType eq '体验金'}"><div class="coupon-item tyj"></c:if>
												<c:if test="${record.couponType eq '代金券'}"><div class="coupon-item djq"></c:if>
	                                                <div class="main">
	                                                    <span class="num">${record.couponQuota}</span>
	                                                    <span class="unit">
	                                                    <c:if test="${record.couponType eq '加息券'}">%</c:if>
	                                                    <c:if test="${record.couponType != '加息券'}">元</c:if>
	                                                    </span>
	                                                    <span class="type">${record.couponType}</span>
	                                                </div>
	                                                <div class="attr">
	                                                    <table>
	                                                        <tr>
	                                                            <td width="72" align="right">出借金额：</td>
	                                                            <td>${record.tenderQuota}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">操作平台：</td>
	                                                            <td>${record.couponSystem}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">出借期限：</td>
	                                                            <td>${record.projectExpirationType}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">项目类型：</td>
	                                                            <td>${record.projectType}</td>
	                                                        </tr>
	                                                    </table>
	                                                </div>
	                                                <div class="date">${record.addTime}~${record.endTime}</div>
	                                                <div class="checked">已选中</div>
	                                            </div>
										 	<c:if test="${status.index%8==7||fn:length(wsyList)==status.index+1}">
                                       			</div>
                                       		</c:if>
										 </c:forEach>
										 </div>
									<c:if test="${fn:length(wsyList) gt 8}">
                                        <div class="swiper-prev"></div>
                                    	<div class="swiper-next"></div>
                                   	</c:if>
                                </div>
                            </div>
                        </li>
                        <li panel="1">
                            <div class="coupon-list " id="couponYsy">
                                <div class="swiper-container">
                                    <div class="swiper-wrapper">
                                        <c:if test="${empty ysyList}">
	                                    	<div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无已使用的优惠券</p></div>
	                                    </c:if>
                                        <c:forEach items="${ysyList }" var="record" begin="0" step="1" varStatus="status">
                                        	<c:if test="${status.index%8==0}">
                                        		<div class="swiper-slide">
                                        	</c:if>
                                        	<div>
												<c:if test="${record.couponType eq '加息券'}"><div class="coupon-item jxq"></c:if>
												<c:if test="${record.couponType eq '体验金'}"><div class="coupon-item tyj"></c:if>
												<c:if test="${record.couponType eq '代金券'}"><div class="coupon-item djq"></c:if>
	                                                <div class="main">
	                                                    <span class="num">${record.couponQuota}</span>
	                                                    <span class="unit">
	                                                    <c:if test="${record.couponType eq '加息券'}">%</c:if>
	                                                    <c:if test="${record.couponType != '加息券'}">元</c:if>
	                                                    </span>
	                                                    <span class="type">${record.couponType}</span>
	                                                </div>
	                                                <div class="attr">
	                                                    <table>
	                                                        <tr>
	                                                            <td width="72" align="right">出借金额：</td>
	                                                            <td>${record.tenderQuota}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">操作平台：</td>
	                                                            <td>${record.couponSystem}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">出借期限：</td>
	                                                            <td>${record.projectExpirationType}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">项目类型：</td>
	                                                            <td>${record.projectType}</td>
	                                                        </tr>
	                                                    </table>
	                                                </div>
	                                                <div class="date">${record.addTime}~${record.endTime}</div>
	                                                <div class="checked">已选中</div>
	                                            </div>
	                                            </div>
	                                            <c:if test="${status.index%8==7||fn:length(ysyList)==status.index+1}">
                                        			</div>
                                        		</c:if>
											</c:forEach>
                                        </div>
                                    <c:if test="${fn:length(ysyList) gt 8}">
                                        <div class="swiper-prev"></div>
                                    	<div class="swiper-next"></div>
                                   	</c:if>
                                </div>
                            </div>
                        </li>
                        <li panel="2">
                            <div class="coupon-list" id="couponYsx">
                                <div class="swiper-container">
                                    <div class="swiper-wrapper">
	                                    <c:if test="${empty ysxList}">
	                                    	<div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无已失效的优惠券</p></div>
	                                    </c:if>
                                        <c:forEach items="${ysxList}" var="record" begin="0" step="1" varStatus="status">
                                        	<c:if test="${status.index%8==0}">
                                        		<div class="swiper-slide">
                                        	</c:if>
												<div class="coupon-item unava">
	                                                <div class="main">
	                                                    <span class="num">${record.couponQuota}</span>
	                                                    <span class="unit">
	                                                    <c:if test="${record.couponType eq '加息券'}">%</c:if>
	                                                    <c:if test="${record.couponType != '加息券'}">元</c:if>
	                                                    </span>
	                                                    <span class="type">${record.couponType}</span>
	                                                </div>
	                                                <div class="attr">
	                                                    <table>
	                                                        <tr>
	                                                            <td width="72" align="right">出借金额：</td>
	                                                            <td>${record.tenderQuota}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">操作平台：</td>
	                                                            <td>${record.couponSystem}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">出借期限：</td>
	                                                            <td>${record.projectExpirationType}</td>
	                                                        </tr>
	                                                        <tr>
	                                                            <td align="right">项目类型：</td>
	                                                            <td>${record.projectType}</td>
	                                                        </tr>
	                                                    </table>
	                                                </div>
	                                                <div class="date">${record.addTime}~${record.endTime}</div>
	                                                <div class="checked">已选中</div>
	                                            </div>
	                                            <c:if test="${status.index%8==7||fn:length(ysxList)==status.index+1}">
                                        			</div>
                                        		</c:if>
											</c:forEach>
                                    </div>
                                    <c:if test="${fn:length(ysxList) gt 8}">
                                        <div class="swiper-prev"></div>
                                    	<div class="swiper-next"></div>
                                   	</c:if>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="container overflow-visible">
            <div class="reward-invite-info">
                <div class="title">我的奖励</div>
                <div class="content">
                    <div class="item w1">邀请好友 <span class="highlight"><span class="lg font-num">${inviteCount}</span>人</span>
                    </div>
                    <div class="line"></div>
                    <div class="item w2">累计奖励 <span class="black"><span class="lg font-num">${rewardRecordsSum}</span> 元</span>
                    </div>
                    <div class="line"></div>
                    <div class="item w3">优惠券 <span class="black"><span class="lg font-num">0</span>张</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="container overflow-visible">
            <div class="reward-invite">
                <div class="title">好友邀请链接</div>
               <%--  <img src="${ctx}/dist/images/account/invite_flow.jpg" alt="" class="invite-flow" /> --%>
				<div class="share-box">
					<div class="copy-area">
	                    <div class="input">专属链接：<span id="copyTxt">${inviteLink }</span></div>
	                    <div class="btn-group">
	                        <a href="javascript:;" class="btn sm btn-primary" data-clipboard-target="#copyTxt" id="copyBtn">复制链接</a>
	                        <a href="${ctx}/user/invite/download.do" class="btn sm" id="shareQrBtn">下载二维码 <span class="iconfont icon-xiazai"></span></a>
	                        <div id="shareQrPop" class="share-pop">
	                            <div id="qrcode" class="qrcode"></div>
	                        </div>
	                    </div>
	                </div>
	                <!--<div class="share-code">邀请码：<span class="highlight">${userId}</span></div>-->
				</div>
                
                <!-- <div class="share-area">
                                                              分享到：
                    <a href="javascript:;" class="share-item share-weibo" title="分享到微博"></a>
                    <a href="javascript:;" class="share-item share-wechat" title="分享到微信"></a>
                    <a href="javascript:;" class="share-item share-qq" title="分享到腾讯QQ"></a>
                </div> -->

            </div>
        </div>
        <div class="container">
            <div class="reward-invite-log">
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="jljl active" panel="0"><a href="javascript:;">奖励记录</a></li>
                        <li class="yqjl"panel="1"><a href="javascript:;">邀请记录</a></li>
                    </ul>
                    <ul class="tab-panels">
                        <li class="active" panel="0">
                            <div class="invite-log-table">
                                <table cellspacing="0" cellpadding="0" border="0">
                                    <thead>
                                        <tr>
                                            <th width="15%">序号</th>
                                            <th width="10%">好友名称</th>
                                            <th width="13%">我的奖励</th>
                                            <th width="14%">奖励来源</th>
                                            <th width="16%">奖励时间</th>
                                            <th width="20%">备注</th>
                                        </tr>
                                    </thead>
                                    <tbody id="jljlTbody">
                                 	 <tr>
                                   		<td colspan="8">
                                   			<div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                                   		</td>
                                   	</tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="pages-nav" id="jljl-pagination"></div>
                        </li>
                        <li panel="1">
                            <div class="invite-log-table">
                                <table cellspacing="0" cellpadding="0" border="0">
                                    <thead>
                                        <tr>
                                        	<th width="32%">序号</th>
                                            <th width="24%">好友名称</th>
                                            <th width="15%">开户状态</th>
                                            <th width="30%">注册时间</th>
                                        </tr>
                                    </thead>
                                    <tbody id="yqjlTbody">
                                    <tr>
                                   		<td colspan="8">
                                   			<div class="loading"><div class="icon"><div class="text">Loading...</div></div></div>
                                   		</td>
                                   	</tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="pages-nav" id="yqjl-pagination"></div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	  
	<script type="text/javascript">
		var swiperUri = "${cdn}/dist/js/lib/swiper3.jquery.min.js";
		if($.browser.msie && $.browser.version < 10){
			swiperUri = "${cdn}/dist/js/lib/swiper.jquery.min.js";
		}
    	if(!utils.isIe8()){
    		$('body').append("<script src='/dist/js/lib/clipboard.min.js'><//script>")
    	}else{
    		$('body').append("<script src='/dist/js/lib/jquery.zclip.min.js'><//script>")
    	}
		$('body').append($("<script>").attr("src",swiperUri));
    </script>
    <script src="${cdn}/dist/js/lib/jquery.qrcode.js"></script>
    <script src="${cdn}/dist/js/lib/qrcode.js"></script>
    <script src="${cdn}/dist/js/acc-set/reward.js?version=${version}"></script>
    <!-- 设置定位  -->
	<script>setActById("myReward");</script>
</body>
</html>