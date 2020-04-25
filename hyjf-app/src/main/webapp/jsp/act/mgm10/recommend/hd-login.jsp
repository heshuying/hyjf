<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="${ctx}/css/mgm.css"/>
<title>邀好友集推荐星</title>
</head>
	<body>
		<!--pop-->
		<div class="pop hide">
			<div class="pop-1"></div>
			<div class="pop-update hide pop-div">
				<div class="pop-close"></div>
				<div class="pop-update-div">
					<div>当前版本太低，请升级应用；</div>
					<div>或点击“更多”选择“邀请好友”继续</div>
				</div>
				<div class="active-detail-btn pop-main-btn pop-ok">确定</div>
			</div>
			<div class="pop-main1">
				
			</div>
		</div>
		<!--pop end-->
		<!-- 是否投资标识  1投资过 0未投资-->
		<input type="hidden" id="isInvest" value="${isInvest}">
		<!-- 是否参加活动标识 1可以参加  0不能参加 -->
		<input type="hidden" id="isStaff" value="${isStaff}">
		<!-- app版本标识标识 1无需更新  0需要更新 -->
		<input type="hidden" id="versionStatus" value="${versionStatus}">
		<!-- 不参加活动邀请路径 -->
		<input type="hidden" id="inviteUrl" value="${inviteUrl}">
		
		<div class="hd-unlogin">
			<div class="hd-unlogin-1"></div>
			<!--未登录部分开始-->
			<!--<div class="hd-unlogin-2">
				<div class="hd-unlogin-cont">
					<a class="hd-unlogin-btn" href="javascript:;">立即登录</a>
					<p class="get-start"><img src="${ctx}/images/mgm10/question_mention.png" alt="" />“推荐星”获取秘籍</p>
				</div>
			</div>-->
			<!--未登录部分结束-->
			<!--已登录部分开始-->
			<div class="hd-login-2">
				<div class="hd-login-cont">
					<div class="login-cont-top">
					    <div class="login-top-left">
							<p class="num-color"><c:out value="${prizeSurplusCount}"></c:out></p>
							<b></b>
							<p class="text-color">可使用（个）</p>
					    </div>
					    <div class="login-top-right">
							<p>
								<a href="hyjf://jumpH5/?{'url':'${getUserRecommendStarPrizeListUrl}'}">
									<span><c:out value="${prizeAllCount}"></c:out></span>共获得（个）<img src="${ctx}/images/mgm10/hd-arrows.png" alt="" />
								</a>
							</p>
							<b></b>
							<p>
								<a href="hyjf://jumpH5/?{'url':'${getUserRecommendStarUsedPrizeListUrl}'}">
									<span><c:out value="${prizeUsedCount}"></c:out></span>已使用（个）<img src="${ctx}/images/mgm10/hd-arrows.png" alt="" />
								</a>
							</p>
						</div>
					    <p class="clearboth"></p>
				    </div>
				    <div class="login-cont-bot">
				    
				    <!-- 是否投资标识  1投资过 0未投资-->
					<%-- <input type="hidden" id="isInvest" value="${isInvest}"> --%>
					<!-- 是否参加活动标识 1可以参加  0不能参加 -->
					<%-- <input type="hidden" id="isStaff" value="${isStaff}"> --%>
					<!-- app版本标识标识 1无需更新  0需要更新 -->
					<%-- <input type="hidden" id="versionStatus" value="${versionStatus}"> --%>
				    	<c:if test="${isInvest==1 && isStaff==1 && versionStatus==1}">
					    	<a class="hd-login-btn hd-unlogin-btn" href="hyjf://activityToShare/?{'title':'${title}','content':'${content}','image':'${img}','url':'${inviteUrl}'}">立即邀请好友</a>
					    </c:if>	
					    <c:if test="${isInvest==0 || isStaff==0 || versionStatus==0}">
					    	<a class="hd-login-btn hd-unlogin-btn" id="inviteA">立即邀请好友</a>
					    </c:if>	
					    <p class="get-start"><img src="${ctx}/images/mgm10/question_mention.png" alt="" />“推荐星”获取秘籍</p>
				    </div>
				</div>
			</div>
			<!--已登录部分结束-->
			<div class="hd-unlogin-3">
				<div class="hd-unlogin-cont">
					<a class="hd-unlogin-btn" href="javascript:;" id="goCJ" >速去领奖</a>
					<p class="use-start"><img src="${ctx}/images/mgm10/question_mention.png" alt="" />“推荐星”使用指南</p>
				</div>
			</div>
			<div class="hd-unlogin-41"></div>
			<div class="hd-unlogin-4">
				<div class="hd-detial">
					<dl>
						<dt>活动时间：</dt>
						<dd>2016年10月21日起，2016年11月30日止。</dd>
						<dt>活动规则：</dt>
						<dd>凡于汇盈金服有过任意投资的用户，均可在活动期内，通过推荐好友注册投资等方式获得“推荐星”，以兑换好礼或参与幸运抽奖。</dd>
					</dl>
				</div>
				<a class="hd-unlogin-btn" href="hyjf://jumpH5/?{'url':'${host}/jsp/act/mgm10/recommend/hd-detial.jsp'}">更多详情</a>
			</div>
			<div class="hd-unlogin-5"></div>
		</div>
		
			<!--推荐星获取秘籍弹层-->
		<div class="popup-window getStart">
			<div class="popup-window-bg"></div>
			<div class="popup-window-cont">
				<div class="get-start-rule">
					<img src="${ctx}/images/mgm10/close.png" alt="" />
					<div class="get-start-text">
						<p>1.成功邀请好友注册开户，即可获得1个“推荐星”。</p>
						<p>2.推荐好友自注册之日起30日内累计投资达3000元视为有效邀请，完成一个有效邀请即可获得2个“推荐星”。</p>
						<p>3.每完成3个有效邀请，推荐人可额外再获1个“推荐星”。</p>
					</div>
				</div>
			</div>
		</div>
		<!--推荐星使用秘籍弹层-->
		<div class="popup-window useStart">
			<div class="popup-window-bg"></div>
			<div class="popup-window-cont">
				<div class="use-start-rule get-start-rule">
					<img src="${ctx}/images/mgm10/close.png" alt="" />
					<div class="get-start-text">
						<dl>
							<dt>1.兑换好礼</dt>
							<dd>推荐人可根据不同礼品对应的“推荐星”数量兑换好礼。每月设固定数量奖品，先兑先得，兑完即止。</dd>
							<dt>2.幸运抽奖</dt>
							<dd>推荐人可使用“推荐星”参与幸运抽奖，每次抽奖消耗3个“推荐星”，抽奖次数不限。</dd>
						</dl>
					</div>
				</div>
			</div>
		</div>
		<!--未开户弹窗-->
		<div class="popup-window invest-invite">
			<div class="popup-window-bg"></div>
			<div class="popup-window-cont">
				<div class="get-start-bg get-start-rule">
					<img  src="${ctx}/images/mgm10/close.png" alt="" style="top:-0.9rem" />
					<div class="get-start-text">
						<p class="invest-txt">
							在汇盈金服有过任意投资的用户，方可参与本活动。<br>
						            快去投资参与活动吧！
						</p>
						<a class="invest-moment" href="hyjf://jumpInvest/?">立即投资</a>
						<a class="invite-continue invite-ok" href="hyjf://activityToShare/?{'title':'${title}','content':'${content}','image':'${img}','url':'${inviteUrl}'}">继续邀请，不参加活动</a>
					</div>
				</div>
			</div>
		</div>
		<!--员工不可用弹窗-->
		<div class="popup-window no-worker">
			<div class="popup-window-bg"></div>
			<div class="popup-window-cont">
				<div class="get-start-bg get-start-rule">
					<img  src="${ctx}/images/mgm10/close.png" alt="" style="top:-0.9rem" />
					<div class="get-start-text">
						<p class="invest-unwork">
							本活动仅限非员工参与。
						</p>
						<a class="invest-moment invite-ok" href="hyjf://activityToShare/?{'title':'${title}','content':'${content}','image':'${img}','url':'${inviteUrl}'}">继续邀请</a>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="${ctx}/js/mgm10/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/mgm10/windowCoupop.js"></script>
	<script src="${ctx}/js/mgm10/jq22.js" type="text/javascript" charset="utf-8"></script>
	<script>
  	function setCookie(c_name, value, expiredays) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + expiredays)
		document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
	}
	/** 根据cookie名称获取值 */
	function getCookie(c_name) {
		if (document.cookie.length > 0) {
			var c_start = document.cookie.indexOf(c_name + "=");
			var c_end;
			if (c_start != -1) {
				c_start = c_start + c_name.length + 1;
				c_end = document.cookie.indexOf(";", c_start);
				if (c_end == -1) {
					c_end = document.cookie.length;
				}
				return unescape(document.cookie.substring(c_start, c_end));
			}
		}
		return "";
	}
	function checkCookie(cname) {
		var cname = getCookie(cname);
		if (cname != null && cname != "") {
			return true;
		} else {
			return false;
		}
	}
	
		$("#inviteA").click(function(){
			
			 if("${versionStatus}" == "0"){
				//提示更新版本
					/* $(".btn-update").on("click",function(){ */							
						$(".pop").show();
						$(".pop-update").show();
						var widthPop = $(".pop-update").width() ;
						$(".pop-update").height(widthPop*1.31);
					/* }) */				return false;
			}
			
			//判断是否可以参加
			if("${isStaff}" == "0"){
				$(".popup-window.no-worker").slideDown();
				return false;
			}	
			//判断是否投资过
			if("${isInvest}" == "0"){
				$(".popup-window.invest-invite").slideDown();
				return false;
			}
			
		})
		//关闭邀请
		$(function(){
			$(".invite-ok").click(function(){
				$(".popup-window").hide()
			})
		})
	</script>
	 <script>
				$("#goCJ").click(function(){
					setCookie("dhtab",1);
					window.location.href = "hyjf://jumpH5/?{'url':'${exchangeAwardUrl}'}";
				})
					
	</script>