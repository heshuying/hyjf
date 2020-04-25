<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>我的推荐星 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.jscrollpane.css" />
    	<link rel="stylesheet" type="text/css" href="${ctx}/css/active/active-201610.css" />
    	<link rel="stylesheet" type="text/css" href="${ctx}/css/idangerous.swiper.css" />
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
    <!--奖品图片-->
    <c:forEach items="${prizeDrawListObj }" var="prizeDraw" varStatus="status">
		<img src="${prizeDraw.prizePicUrl}" id="drawimg${status.index }" style="display:none;" />
	</c:forEach>
    <div class="active-201610-1"></div>
    <div class="active-201610-2">
        <div class="tjx-section">
        	<!-- 已登录 -->
        	<c:if test="${status==1}">
            <div class="tjx-info-box">
                <div class="tjx-summary">
                    <div class="item item1">
                        <div class="count"><c:out value="${prizeSurplusCount}"></c:out> <span class="start"></span></div>
                        <div class="field">可使用（个）</div>
                        <div class="line"></div>
                    </div>
                    <div class="item item2">
                        <div class="count"><c:out value="${prizeAllCount}"></c:out> <span class="start"></span></div>
                        <div class="field">共获得（个）</div>
                        <div class="line"></div>
                    </div>
                    <div class="item item3">
                        <div class="count"><c:out value="${prizeUsedCount}"></c:out> <span class="start"></span></div>
                        <div class="field">已使用（个）</div>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="tjx-tab">
                    <div class="tab-items">
                        <div class="tab-item active" data-panel="0">推荐星获取记录
                            <div class="line"></div>
                        </div>
                        <div class="tab-item" data-panel="1">推荐星使用记录</div>
                    </div>
                    <div class="tab-panels">
                        <div class="tab-panel active" data-panel="0">
                            <table cellpadding="0" cellspacing="0" border="0" width="800">
                                <tr>
                                    <th width="25%" align="left">获得方式</th>
                                    <th width="25%" align="left">推荐好友</th>
                                    <th width="25%" align="center">获得推荐星数</th>
                                    <th width="25%" align="left">获得时间</th>
                                </tr>
                            </table>
                            <div class="tjx-scroll-area horizontal-only">
                              <c:if test="${! empty userRecommendStarPrizeList}">
                                <table cellpadding="0" cellspacing="0" border="0" width="800">
                                	<c:forEach items="${userRecommendStarPrizeList}" var="record" begin="0" step="1" varStatus="status">
	                                    <tr>
	                                        <td width="25%" align="left"><c:out value="${record.recommendSource }"></c:out></td>
	                                        <td width="25%" align="left"><c:out value="${record.inviteByUser }"></c:out></td>
	                                        <td width="25%" align="center"><c:out value="${record.recommendCount }"></c:out></td>
	                                        <td width="25%" align="left"><c:out value="${record.sendTime }"></c:out></td>
	                                    </tr>
                                  	</c:forEach>
                                </table>
                               </c:if>
                               <c:if test="${empty userRecommendStarPrizeList}">
                                <div class="tjx-scroll-area-noitem">
                              	  暂无记录
                            	</div>
                            	</c:if>
                            </div>
                        </div>
                        <div class="tab-panel" data-panel="1">
                            <table cellpadding="0" cellspacing="0" border="0" width="800">
                                <tr>
                                    <th width="25%" align="left">使用方式</th>
                                    <th width="25%" align="left">奖品名称</th>
                                    <th width="25%" align="center">使用推荐星数量</th>
                                    <th width="25%" align="left">使用时间</th>
                                </tr>
                            </table>
                            <div class="tjx-scroll-area horizontal-only">
                              <c:if test="${!empty userRecommendStarUsedList}">
                                <table cellpadding="0" cellspacing="0" border="0" width="800">
                                    <c:forEach items="${userRecommendStarUsedList}" var="record" begin="0" step="1" varStatus="status">
	                                    <tr>
	                                        <td width="25%" align="left"><c:out value="${record.prizeKind }"></c:out></td>
	                                        <td width="25%" align="left"><c:out value="${record.prizeName }"></c:out></td>
	                                        <td width="25%" align="center"><c:out value="${record.prizeCount }"></c:out></td>
	                                        <td width="25%" align="left"><c:out value="${record.addTime }"></c:out></td>
	                                    </tr>
                                  	</c:forEach>
                                </table>
                                </c:if>
                                <c:if test="${empty userRecommendStarUsedList}">
                                <div class="tjx-scroll-area-noitem">
                              	  暂无记录
                            	</div>
                            	</c:if>
                            </div>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="clearfix"></div>
            </div>
            <div class="tjx-control">
                <div class="recoment-code">
                    <div class="field">我的邀请码</div>
                    <div class="value"><c:out value="${userId}"></c:out></div>
                    <div class="txt">邀请好友注册时在“推荐人”一栏中填写您的邀请码即可。</div>
                </div>
                <div class="qr-area">
                    <div class="qr">
                        <div id="qrcode"></div>
                    </div>
                    <div class="url" id="copy-url">${inviteLink }</div>
                    <a class="qr-btn first" id="dlQr" href="${ctx}/activity/recommend/download.do">下载二维码</a>
                    <div class="qr-btn" id="copy">复制链接</div>
                    <div class="clearfix"></div>
                    <div class="txt">邀请好友点击您的邀请链接或扫描您的二维码进入页面注册即可。</div>
                </div>
            </div>
            </c:if>
        	<!-- end已登录 -->
        	<!-- 未登录 -->
        	<c:if test="${status==0}">
        		<div class="clearfix"></div>
        		<a href="${ctx}${loginUrl}"><div class="dl-btn"></div></a>
        		<div class="dl-sub">登录后查看推荐星，获取专属邀请链接</div>
        	</c:if>
        	<!-- end未登录 -->
			

        	<div class="clearfix"></div>
        </div>
    </div>
    <div class="active-201610-3">
        <div class="main-info" style="margin-top: 180px;">
            <p>每月设固定数量奖品，先兑先得，兑完即止！</p>
            <c:if test="${status==1}">
            	<p>您当前拥有 <span class="val">${recommendCount }</span> 颗推荐星可用</p>
            </c:if>
        </div>
    </div>
    <div class="active-201610-4">
        <div class="container-1200">
            <div class="dh-section">
               <c:forEach items="${prizeChangeList }" var="prizeChange">
               <c:if test="${1==1}">
                <div class="item">
                    <div class="dh-img">
                        <img src="${prizeChange.prizePicUrl }" alt="">
                    </div>
                    <div class="dh-ctrl">
                        <div class="title"><span class="data-val">${prizeChange.prizeName}</span></div>
                	     <input type="hidden" name="groupCode" value="${prizeChange.prizeGroupCode }">
                        <div class="ctrl-box">
                            <div class="less"></div>
                            <div class="count">
                                <input type="text" value="1" data-max="${prizeChange.prizeReminderQuantity}" data-price="${prizeChange.recommendQuantity}" />
                            </div>
                            <div class="more"></div>
                            	剩余<span class="data-val">${prizeChange.prizeReminderQuantity}</span>张
                        </div>
                        <a href="javascript:;" class="dh-btn ended">活动结束</a>
                    </div>
                    <div class="dh-txt itemArea">${prizeChange.remark}</div>
                </div>
                </c:if>
                </c:forEach>
                <c:forEach items="${prizeChangeList }" var="prizeChange">
                <c:if test="${1==2 }">
                <div class="item">
                    <div class="dh-img">
                        <img src="${prizeChange.prizePicUrl }" alt="">
                    </div>
                    <div class="dh-ctrl">
                        <div class="title"><span class="data-val">${prizeChange.prizeName}</span></div>
                        <div class="ctrl-box">
                            <div class="less"></div>
                            <div class="count">
                                <input type="text" value="0" disabled="disabled" data-max="${prizeChange.prizeReminderQuantity}" data-price="${prizeChange.recommendQuantity}" />
                            </div>
                            <div class="more"></div>
                          	  剩余<span class="data-val">${prizeChange.prizeReminderQuantity}</span>张
                        </div>
                        <a href="javascript:;" class="dh-btn done">已抢光</a>
                    </div>
                    <div class="dh-txt itemArea">${prizeChange.remark}</div>
                </div>
                </c:if>
                </c:forEach>
                
            </div>
        </div>
    </div>
    <div class="active-201610-5">
        <div class="main-info" style="margin-top: 180px;">
            <p>每次抽奖消耗${needCount }个“推荐星”，抽奖次数不限。</p>
            <c:if test="${status==1}">
            	<p>您当前拥有 <span class="val">${canDrawCount }</span> 次抽奖机会</p>
            </c:if>
        </div>
    </div>
    <div class="active-201610-6">
        <div class="choujiang-section">
            <div class="turnplate" style="background-image:url(${ctx}/img/active/active_201610/choujiang/zp-bg.png);background-size:100% 100%;">
                <canvas class="item" id="wheelcanvas" width="600" height="600"></canvas>
                <%--<img class="pointer" src="${ctx}/img/active/active_201610/choujiang/lottery-btn.png" />--%>
                <img class="pointer" src="${ctx}/img/active/active_201610/choujiang/lottery-end-btn.png" />
            </div>
            <div class="choujiang-list-box">
            	<table cellspacing="0" cellpadding="0" class="choujiang-list-hd">
                    <tr>
                        <td width="50%">用户名</td>
                        <td width="50%">获得奖励</td>
                    </tr>
                </table>
                <c:if test="${! empty prizeWinList }">
                <div class="swiper-container choujiang-list">
				  <div class="swiper-wrapper">
				      <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="0" end = "7">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				      <c:if test="${fn:length(prizeWinList)>8}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="8" end = "15">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>16}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="16" end = "23">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>24}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="24" end = "31">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>32}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="32" end = "39">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>40}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="40" end = "47">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>48}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="48" end = "55">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>56}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="56" end = "63">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>64}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="64" end = "71">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>72}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="72" end = "79">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>80}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="80" end = "87">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>88}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="88" end = "95">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				     <c:if test="${fn:length(prizeWinList)>96}">
				       <div class="swiper-slide item">
				      	<table cellspacing="0" cellpadding="0" width="100%">
				      	<c:forEach items="${prizeWinList }" var="prizeWin" begin="96" end = "100">
	                        <tr>
	                            <td width="50%">${empty prizeWin.userName?'':fn:substring(prizeWin.userName,0,1)}****${empty prizeWin.userName?'':fn:substring(prizeWin.userName,fn:length(prizeWin.userName)-1,fn:length(prizeWin.userName))}</td>
	                            <td width="50%">${prizeWin.prizeName }</td>
	                        </tr>
	                    </c:forEach>
	                    </table>
				      </div>
				     </c:if>
				  </div>
				</div>
				</c:if>
				<c:if test="${empty prizeWinList }">
					<div class="choujiang-list-noitem">
	                	还没有人来抽奖，快来做第一个吃螃蟹的人吧
	                </div>
                </c:if>
            	
            </div>
        </div>
    </div>
    <div class="active-201610-7"></div>
    <div class="active-201610-8">
        <div class="dh-detail-container">
            <div class="title">活动时间：</div>
            <div class="content">2016年10月21日起，2016年11月30日止。</div>
            <div class="title">活动规则：</div>
            <div class="content">凡于汇盈金服有过任意投资的用户，均可在活动期内，通过推荐好友注册投资等方式获得“推荐星”，以兑换好礼或参与幸运抽奖。</div>
            <div class="title">“推荐星”获取秘籍：</div>
            <div class="content">1.成功邀请好友注册开户，即可获得1个“推荐星”。
                <br/> 2.推荐好友自注册之日起30日内累计投资达3000元视为有效邀请，完成一个有效邀请即可获得2个“推荐星”。
                <br/> 3.每完成3个有效邀请，推荐人可额外再获1个“推荐星”。
            </div>
            <div class="title">“推荐星”使用指南：</div>
            <div class="content">1.兑换好礼
                <br/> 推荐人可根据不同礼品对应的“推荐星”数量兑换好礼。每月设固定数量奖品，先兑先得，兑完即止。
                <br/> 2.幸运抽奖
                <br/> 推荐人可使用“推荐星”参与幸运抽奖，每次抽奖消耗3个“推荐星”，抽奖次数不限。
            </div>
            <div class="title">奖励发放：</div>
            <div class="content">1.优惠券奖励将于推荐人成功兑换或抽取后发放至推荐人汇盈金服账户，用户登录后于“优惠券”中查看。
                <br/> 2.非优惠券奖励将于次月的前5个工作日内，由客服电话回访中奖用户核实身份信息后，统一采购发放。如遇货源紧缺，发放时间相应顺延。
                <br/> 3.话费奖励将充值至推荐人平台注册手机号中，非三大运营商（移动、联通、电信）号码请及时联系客服。
                <br/> 4.其他电子卡券信息将以短信形式发送至推荐人平台注册手机号，请注意查收，并于有效期内激活使用。
            </div>
            <div class="title">注：</div>
            <div class="content">1.仅限投资新手汇、汇直投项目金额参与活动统计。
                <br/> 2.本活动所发优惠券均自获得之日起30日内有效，过期作废。其中，10元代金券单笔投资达2000元可用；20元代金券单笔投资达5000元可用；30元、50元代金券均单笔投资达10000元可用。
                <br/> 3.已用于兑换或抽奖的“推荐星”将被消耗，未使用的可于活动期内累计，活动结束时仍未使用的视为用户自动放弃使用机会，不再给予奖励。
                <br/> 4.兑换好礼奖品数量将于活动期内每月1日0点更新。
                <br/> 5.由于用户平台注册手机号停机或更换、泄露电子卡券信息、未于有效期内及时激活或其他个人原因，造成无法成功获取奖励的，用户自担损失。
                <br/> 6.活动期内，客服将随机抽取新注册用户进行电话回访核实信息，如遇同一推荐人的多个邀请好友手机号不存在、停机、空号、多次无人接听或多个电话同一人接听的，该推荐人将被视为恶意刷奖用户。对于恶意刷奖者，一经发现，汇盈金服将取消其活动参与资格，所得奖励不予承兑，并保留追究其相关法律责任的权利。
            </div>
            <div class="title">本活动与Apple Inc.无关</div>
            <div class="title">本活动最终解释权归汇盈金服所有</div>
        </div>
    </div>

    <div class="pop-over"></div>
    <!-- 抽取成功 -->
    <div class="choujiang-pop pop-big" id="cjcgPop">
    	<div class="pop-close-201610"></div>
    	<div class="main">
    		<div class="title">恭喜您</div>
    		<div class="content">
    			<div class="choujiang-popimg">
    				<img id="prizeUrlForDrawSuccess" src="${ctx}/img/active/active_201610/dh_img.jpg" alt="">
    			</div>
    			<div class="popmsg-1">您已成功抽取 <span id="prizeNameForDraw" class="yellow">20元代金券</span></div>
				<div class="popmsg-2"><span id="remarkForDraw" class="yellow">金额范围：20000~50000元可用</span></div>
				<div class="popmsg-3" style="margin-top: 20px;"><span id="prizeCountForDraw" style="margin-right:30px;">数量：1</span id="recommendUsedForDraw">  使用推荐星：3个</div>
				<div class="line clearfix"></div>
				<div id="successMsgForDraw" class="popmsg-3" style="margin-top:16px;">优惠券已发放至您的汇盈金服账户，登录后于“优惠券”中查看。</div>

    		</div>
			<div id="drawSuccessButton" class="pop-btn-1">知道了</div>
    	</div>
    </div>
	

	<!-- 兑换成功 -->
    <div class="choujiang-pop pop-big" id="dhcgPop">
    	<div class="pop-close-201610"></div>
    	<div class="main">
    		<div class="title">恭喜您</div>
    		<div class="content">
    			<div class="choujiang-popimg">
    				<img id="prizeUrlForChangeSuccess" src="${ctx}/img/active/active_201610/dh_img.jpg" alt="">
    			</div>
    			<div class="popmsg-1">您已成功兑换 <span id="prizeNameForChangeSuccess" class="yellow">20元代金券</span></div>
				<div class="popmsg-2"><span id="remarkForChangeSuccess" class="yellow">金额范围：20000~50000元可用</span></div>
				<div class="popmsg-3" style="margin-top: 20px;"><span id="countForChangeSuccess" style="margin-right:30px;">数量：1</span>  <span id="recommendForChangeSuccess">使用推荐星：3个</span></div>
				<div class="line clearfix"></div>
				<div id="successMsgForChange" class="popmsg-3" style="margin-top:16px;">优惠券已发放至您的汇盈金服账户，登录后于“优惠券”中查看。</div>

    		</div>
			<div id="prizeChangeSuccessButon" class="pop-btn-1">知道了</div>
    	</div>
    </div>

	<!-- 确认兑换 -->
    <div class="choujiang-pop pop-small" id="qrdhPop">
    	<div class="pop-close-201610"></div>
    	<div class="main">
    		<div class="title">确认兑换</div>
    		<input type="hidden" id="groupCodeConfirm" name="groupCodeConfirm" value="">
    		<div class="content">
    			<div class="popmsg-1">您选择兑换 <span id="prizeNameForChange" class="yellow">20元代金券</span></div>
				<div class="popmsg-2"><span class="yellow" id="popArea">金额范围：20000~50000元可用</span></div>
				<div class="popmsg-3" style="margin-top: 20px;"><span style="margin-right:30px;">数量：<span id="count"></span></span>  使用推荐星：<span id="total"></span>个</div>
    		</div>
			<div id="changeConfirm" class="pop-btn-1" style="margin-top: 90px;">确定</div>
    	</div>
    </div>
	
	<!-- 推荐星不足 -->
    <div class="choujiang-pop pop-small" id="tjcbzPop"> 
    	<div class="pop-close-201610"></div>
    	<div class="main">
    		<div class="title">推荐星不足</div>
    		<div class="content">
				<div class="popmsg-2">您的推荐星不足，快去邀请好友获得推荐星吧！</div>
    		</div>
			<div id="recommendNotEnough" class="pop-btn-1" style="margin-top: 120px;">确定</div>
    	</div>
    </div>
    
    <!-- 温馨提示 -->
    <div class="choujiang-pop pop-small" id="wxtsPop"> 
    	<div class="pop-close-201610"></div>
    	<div class="main">
    		<div class="title">温馨提示</div>
    		<div class="content">
				<div id="wxtsMsg" class="popmsg-2">提示信息</div>
    		</div>
			<div id="wxtsButtonConfirm" class="pop-btn-1" style="margin-top: 120px;">确定</div>
    	</div>
    </div>

	<!-- 员工提示 -->
    <div class="choujiang-pop pop-small" id="ygtxPop">
    	<div class="pop-close-201610"></div>
    	<div class="main">
    		<div class="content">
				<div class="popmsg-2" style="margin-top: 80px; text-align: center;">本活动仅限非员工参与！</div>
    		</div>
			<div class="pop-btn-1" style="margin-top: 120px;">继续邀请，不参加活动</div>
    	</div>
    </div>

	<!-- 老用户提示 -->
    <div class="choujiang-pop pop-small" id="ljtzPop">
    	<div class="pop-close-201610"></div>
    	<div class="main">
    		<div class="content">
				<div class="popmsg-1" style="margin-top: 40px;">在汇盈金服有过任意投资的用户，方可参与本活动。快去投资参与活动吧！</div>
    		</div>
			<div class="pop-btn-1" style="margin-top: 100px;">立即投资</div>
			<div class="pop-btn-2" style="margin-top: 20px;">继续邀请，不参加活动</div>
    	</div>
    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    <div class="invite-copy-hint" style="display: none;">复制成功，快转发给好友吧!</div>
    <script type="text/javascript" src="${ctx}/js/jquery.jscrollpane.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.mousewheel.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/js/awardRotate.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/js/idangerous.swiper.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/js/qrcode.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.qrcode.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.zclip.min.js"></script>
	<!--[if IE]><script type="text/javascript" src="${ctx}/js/excanvas.js"></script><![endif]-->
    <script>
    $(".tab-item").click(function() {
        var item = $(this);
        var idx = item.data("panel");
        var panel = $(".tab-panel[data-panel=" + idx + "]");
        item.addClass("active").siblings().removeClass("active");
        panel.addClass("active").siblings().removeClass("active");
        panel.find('.tjx-scroll-area').jScrollPane();
    });
    var choujiangList = new Swiper('.choujiang-list', {
        autoplay: 5000,
        //paginationClickable: true,
        loop: "true",
        mode:"vertical"
    });
    $('.prev').on('click', function(e) {
        e.preventDefault()
        banner.swipePrev()
    })
    $('.next').on('click', function(e) {
        e.preventDefault()
        banner.swipeNext()
    })

    $(function() {
        $(".tab-panel.active").find('.tjx-scroll-area').jScrollPane();

        //减少
        $(".ctrl-box .less").click(function() {
            var ipt = $(this).siblings(".count").children("input");
            var btnTxt = $(this).parent().siblings(".dh-btn").children(".itemTotal");
            var count = ipt.val();
            var price = ipt.data("price");
            var max = ipt.data("max");
            if (count <= 1) {
                return false;
            }
            ipt.val(--count);
            btnTxt.text(count*price);
            
        })

        //增加
        $(".ctrl-box .more").click(function() {
                var ipt = $(this).siblings(".count").children("input");
                var btnTxt = $(this).parent().siblings(".dh-btn").children(".itemTotal");
                var count = ipt.val();
                var price = ipt.data("price");
                var max = ipt.data("max");
                if (overMax(count, max)) {
                    return false;
                }
                ipt.val(++count);
                btnTxt.text(count*price);
            })
            //数据变化
        $(".ctrl-box .count").children("input").keypress(function(e) {
	            var reg = /^[0-9]$/;
	            var k = e.which || e.keyCode;
	            if (!reg.test(e.key) && k!=8 && k!=37 && k!=39 && k!=46 ) {
	                return false;
	            }
            })
            .keyup(function(){
            	var ipt = $(this);
                var count = ipt.val();
                var price = ipt.data("price");
                var max = ipt.data("max");
                var btnTxt = $(this).parent().parent().siblings(".dh-btn").children(".itemTotal"); //按钮上的总星数
                if (overMax(count, max)) {
                    ipt.val(max);
                }
                btnTxt.text(ipt.val()*price);
            })

        function overMax(val, max) {
            /*是否超过最大值*/
            if (parseInt(val) >= parseInt(max)) {
                return true;
            } else {
                return false;
            }
        }
    });
    </script>
    <script type="text/javascript">
    var loginStatus = "${status}";
    
    var prizeDrawList = eval('(' + '${prizeDrawList}' + ')');
    var turnplate = {
    	groupCode:[],
        restaraunts: [], //大转盘奖品名称
        colors: [], //大转盘奖品区块对应背景颜色
        outsideRadius: 240, //大转盘外圆的半径
        textRadius: 208, //大转盘奖品位置距离圆心的距离
        insideRadius: 56, //大转盘内圆的半径
        startAngle: 0, //开始角度
        bRotate: false //false:停止;ture:旋转
    };

    $(document).ready(function() {
        //动态添加大转盘的奖品与奖品区域背景颜色
        for(var i=0; i<prizeDrawList.length; i++){
    		turnplate.restaraunts.push(prizeDrawList[i].prizeName);
    		turnplate.groupCode.push(prizeDrawList[i].prizeGroupCode);
    		if(i%2==0){
    			turnplate.colors.push("#9d3aed");
    		}else{
    			turnplate.colors.push("#521187");
    		}
    	}
        var rotateTimeOut = function() {
            $('#wheelcanvas').rotate({
                angle: 0,
                animateTo: 2160,
                duration: 8000,
                callback: function() {
                    alert('网络超时，请检查您的网络设置！');
                }
            });
        };

        //旋转转盘 item:奖品位置; txt：提示语;
        var rotateFn = function(item, txt) {
            var angles = item * (360 / turnplate.restaraunts.length) - (360 / (turnplate.restaraunts.length * 2));
            if (angles < 270) {
                angles = 270 - angles;
            } else {
                angles = 360 - angles + 270;
            }
            $('#wheelcanvas').stopRotate();
            $('#wheelcanvas').rotate({
                angle: 0,
                animateTo: angles + 1800,
                duration: 8000,
                callback: function() {
                    turnplate.colors[item - 1] = "#f39800";
                    var a = turnplate.colors;
                    drawRouletteWheel()
                    turnplate.bRotate = !turnplate.bRotate;
                    //alert("恭喜中奖");

		    		var pop = $("#cjcgPop");
		    		openPop(pop);
                }
            });
        };
        var item = 1;
        /*$('.pointer').click(function() {
            if (turnplate.bRotate) return;
            if(loginStatus == "0"){
    			window.location.href = "${ctx}${loginUrl}";
    			return;
    		}
        	$.ajax({
    			url : 'doPrizeDraw.do',
    			type : "POST",
    			async : true,
    			data : "",
    			success : function(result) {
    				if(result.status == 0){
    					$("#prizeNameForDraw").text(result.prizeName);
    					$("#prizeUrlForDrawSuccess").attr({src: result.prizePicUrl});
    					$("#remarkForDraw").text(result.remark);
    					$("#prizeCountForDraw").text("数量：" + result.prizeCount);
    					$("#recommendUsedForDraw").text("将使用推荐星：" + result.recommendCost + "个");
    					$("#successMsgForDraw").text(result.successMsg);
    					for(var i=0; i<turnplate.groupCode.length; i++){
    						if(turnplate.groupCode[i] == result.groupCode){
    							item = i+1;
    							rotateFn(item, turnplate.restaraunts[item - 1]);
    						}
    					}
    				}else if(result.errCode == 2){
    					openPop($("#tjcbzPop"));
    				}else {
    					$("#wxtsMsg").text(result.statusDesc);
    					openPop($("#wxtsPop"));
    				}
    			},
    			error : function(err) {
    				$("#wxtsMsg").text("数据取得失败!");
					openPop($("#wxtsPop"));
    			}
    		});
            turnplate.bRotate = !turnplate.bRotate;
            //获取随机数(奖品个数范围内)
            //var item = rnd(1, turnplate.restaraunts.length);
            //奖品数量等于10,指针落在对应奖品区域的中心角度[252, 216, 180, 144, 108, 72, 36, 360, 324, 288]
            //rotateFn(item, turnplate.restaraunts[item - 1]);
        });*/
    });

    function rnd(n, m) {
        var random = Math.floor(Math.random() * (m - n + 1) + n);
        return random;

    }


    //页面所有元素加载完毕后执行drawRouletteWheel()方法对转盘进行渲染
    window.onload = function() {
        drawRouletteWheel();
    };

    function drawRouletteWheel() {
        var canvas = document.getElementById("wheelcanvas");
        if (canvas.getContext) {
            //根据奖品个数计算圆周角度
            var arc = Math.PI / (turnplate.restaraunts.length / 2);
            var ctx = canvas.getContext("2d");
            //在给定矩形内清空一个矩形
            ctx.clearRect(0, 0, 600, 600);
            //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式  
            ctx.strokeStyle = "#FFBE04";
            //font 属性设置或返回画布上文本内容的当前字体属性
            ctx.font = '18px Microsoft YaHei';
            for (var i = 0; i < turnplate.restaraunts.length; i++) {
                var angle = turnplate.startAngle + i * arc;
                ctx.fillStyle = turnplate.colors[i];
                ctx.beginPath();
                //arc(x,y,r,起始角,结束角,绘制方向) 方法创建弧/曲线（用于创建圆或部分圆）    
                ctx.arc(300, 300, turnplate.outsideRadius, angle, angle + arc, false);
                ctx.arc(300, 300, turnplate.insideRadius, angle + arc, angle, true);
                ctx.stroke();
                ctx.fill();
                //锁画布(为了保存之前的画布状态)
                ctx.save();

                //改变画布文字颜色
                var b = i + 2;
                if (b % 2) {
                    ctx.fillStyle = "#FFFFFF";
                } else {
                    ctx.fillStyle = "#FFFFFF";
                };

                //----绘制奖品开始----


                var text = turnplate.restaraunts[i];
                var line_height = 17;
                //translate方法重新映射画布上的 (0,0) 位置
                ctx.translate(300 + Math.cos(angle + arc / 2) * turnplate.textRadius, 300 + Math.sin(angle + arc / 2) * turnplate.textRadius);

                //rotate方法旋转当前的绘图
                ctx.rotate(angle + arc / 2 + Math.PI / 2);

                /** 下面代码根据奖品类型、奖品名称长度渲染不同效果，如字体、颜色、图片效果。(具体根据实际情况改变) **/
                if (text.length <= 8) { //判断字符进行换行
                    var texts = [text];
                    ctx.font = '18px Microsoft YaHei';
                    for (var j = 0; j < texts.length; j++) {
                        ctx.fillText(texts[j] , -ctx.measureText(texts[j]).width / 2, j * line_height);
                    }
                } else if (text.length > 8) { //奖品名称长度超过一定范围 
                    ctx.font='16px Microsoft YaHei';
                    var stxt = parseInt(text.length/2);
                    var nowtext = text.substring(0, stxt) + "||" + text.substring(stxt);
                    var texts = nowtext.split("||");
                    for (var j = 0; j < texts.length; j++) {
                        ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
                    }
                } else {
                    //在画布上绘制填色的文本。文本的默认颜色是黑色
                    //measureText()方法返回包含一个对象，该对象包含以像素计的指定字体宽度
                    ctx.fillText(text, -ctx.measureText(text).width / 2, 0);
                }

                //添加对应图标

                for(var imgi = 0;imgi<turnplate.restaraunts.length; imgi++){
                    if (text.indexOf(turnplate.restaraunts[imgi]) >= 0) {
                        var img = document.getElementById("drawimg"+imgi);
                        var width = $(img).width();
                        var height = $(img).height();
                        var newH = height / width * 66;
                        img.onload = function() {
                            ctx.drawImage(img, -33, 25, 66, newH);
                        };
                        ctx.drawImage(img, -33, 25, 66, newH);
                    };
                }


                //把当前画布返回（调整）到上一个save()状态之前 
                ctx.restore();
                //----绘制奖品结束----
            }
        }
    };
    </script>
    

    <script>
    	$(".pop-close-201610").click(function(){
    		closePop($(".choujiang-pop"));
    		window.location.reload();
    	});
    	function openPop(pop){
    		$(".pop-over").show();
    		pop.show();
    	}
    	function closePop(pop){
    		$(".pop-over").hide();
    		pop.hide();
    	}

    	//点击兑换
    	/*$(".dh-btn").click(function(){
    		if(loginStatus == "0"){
    			window.location.href = "${ctx}${loginUrl}";
    			return;
    		}
    		var _self = $(this);
    		var pop = $("#qrdhPop");
    		var dataEle =  _self.parent(".dh-ctrl").find("input[type=text]");
    		var dataTitle = _self.parent(".dh-ctrl").find(".title").text();
    		var dataArea = _self.parent(".dh-ctrl").siblings(".itemArea").text();
    		var groupCode = _self.parent(".dh-ctrl").find("input").val();
    		var count = dataEle.val();
    		var price = dataEle.data("price");
    		if(!_self.hasClass("done")){
    			pop.find("#count").text(count);
    			pop.find("#total").text(price*count);
    			pop.find("#prizeNameForChange").text(dataTitle);
    			pop.find("#popArea").text(dataArea);
    			pop.find("#groupCodeConfirm").val(groupCode);
    			openPop(pop);
    		}
    	});*/
    	
    	$("#changeConfirm").click(function(){
    		var groupCode = $("#qrdhPop").find("#groupCodeConfirm").val();;
			var changeCount = $("#qrdhPop").find("#count").text();
			closePop($("#qrdhPop"));
			$.ajax({
				url : 'prizeChangeCheck.do',
				type : "POST",
				async : true,
				data : "groupCode=" + groupCode + "&changeCount=" + changeCount,
				success : function(result) {
					if(result.status == 0){
						$.ajax({
							url : 'doPrizeChange.do',
							type : "POST",
							async : true,
							data : "groupCode=" + groupCode + "&changeCount=" + changeCount,
							success : function(result2) {
								if(result2.status == 0){
									$("#prizeNameForChangeSuccess").text(result2.prizeName);
									$("#prizeUrlForChangeSuccess").attr({src: result2.prizePicUrl});
									$("#remarkForChangeSuccess").text(result2.remark);
									$("#countForChangeSuccess").text("数量：" + result2.prizeCount);
									$("#recommendForChangeSuccess").text("使用推荐星：" + result2.recommendCost + "个")
									$("#successMsgForChange").text(result2.successMsg);
									closePop($("#qrdhPop"));
									openPop($("#dhcgPop"));
								}else if(result2.errCode == 2){
									// 推荐星不足
									openPop($("#tjcbzPop"));
								}else if(result2.errCode == 4){
									$("#wxtsMsg").text("奖品不足");
			    					openPop($("#wxtsPop"));
								}else {
									$("#wxtsMsg").text(result2.statusDesc);
			    					openPop($("#wxtsPop"));
								}
							},
							error : function(err) {
								$("#wxtsMsg").text("数据取得失败!");
		    					openPop($("#wxtsPop"));
							}
						});
					}else if(result.errCode == 2){
						// 推荐星不足
						openPop($("#tjcbzPop"));
					}else if(result.errCode == 4){
						$("#wxtsMsg").text("奖品不足");
    					openPop($("#wxtsPop"));
					}else {
						$("#wxtsMsg").text(result.statusDesc);
    					openPop($("#wxtsPop"));
					}
					
				},
				error : function(err) {
					$("#wxtsMsg").text("数据取得失败!");
					openPop($("#wxtsPop"));
				}
			});
    	});
    	
    	$("#prizeChangeSuccessButon").click(function(){
    		closePop($("#dhcgPop"));
    		window.location.reload();
    	});
    	
    	$("#drawSuccessButton").click(function(){
    		closePop($("#cjcgPop"));
    		window.location.reload();
    	});
    	
    	$("#recommendNotEnough").click(function(){
    		closePop($("#tjcbzPop"));
    		window.location.reload();
    	})
    	
    	$("#wxtsButtonConfirm").click(function(){
    		closePop($("#wxtsPop"));
    		window.location.reload();
    	})
    	
    	$("#qrcode").each(function() {
			$(this).qrcode({
				text: "${shareUrl}",
				render: "canvas", 
				width: 130, 
				height: 130,
				typeNumber: 0, 
				correctLevel: QRErrorCorrectLevel.L,
				background: "#ffffff",
				foreground: "#000000"
			})
		});
    	/* function convertCanvasToImageUrl(canvas) {
    		return canvas.toDataURL("image/png");
    	};

    	 
    	var cvsbox = document.getElementById("qrcode");
    	var cvs = cvsbox.getElementsByTagName("canvas");
    	
    	$("#dlQr").click(function(){
    		var url = convertCanvasToImageUrl(cvs[0]);
    		window.open(url);
    	}); */
		$("#copy").zclip({   
			path: "${ctx}/js/ZeroClipboard.swf",
			copy: $("#copy-url").html(),
			afterCopy:function(){/* 复制成功后的操作 */
				$(".invite-copy-hint").show().delay(1500).fadeOut(300);
	        }
		});
    </script>

	
	
	
	</body>
</html>