<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css" />
<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.bxslider.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/idangerous.swiper.css" />
<title>计划详情</title>
<style type="text/css">
	.detailContent ul li .left{width: 33%;}
	.detailContent ul li .right{width: 67%;}
	.detailContent ul li .right a{color:#49a7f6}
	.jobs-list dl dt{border-top: solid 1px #E5E5E5;}
	.jobs-list dl:first-child  dt{border-top:0}
	.jobs-list dl dt .title{margin-left: 0;}
	.cupop-div {display: none;}
.cupop-div-bg{height:100%;width:100%;position:fixed;top:0;left:0;background:rgba(12,12,12,0.99);overflow:auto;opacity:0.7;z-index:11;}
.cupop-div-cont{
	width:100%;
	position: fixed;
    top: 30%;
    left:0;
    right:0;
    z-index:22;
}
.cupop-div-advisor{   
	 width: 267px;
    text-align: center;
    background: #FFFFFF;
    margin: 0 auto;
    border-radius: 5px;
    -webkit-border-radius: 5px;
   
    }
    .cupop-div-shouquan {   
	 width: 80%;
    text-align: center;
    background: #FFFFFF;
    margin: 0 auto;
    border-radius: 5px;
    -webkit-border-radius: 5px;
   
    }
  .cupop-div-advisor img{ width:146px;margin-bottom: 10px;} 
    .cupop-div-advisor h5{
    	height:39px;font-size:15px;line-height:39px;
    }
     .cupop-div-advisor p{padding:0 15px;line-height:19px;height:52px;border-bottom:1px solid #E4E3E5;}
     .cupop-div-advisor div span{text-align:center;width:48%;display:inline-block;height:44px;line-height:44px;}
     .openPopDivSpan{border-right:1px solid #E4E3E5;}
	.openPopSingle{width:100%;border-right:0}
	.marginTop{margin-top:20px}
	.cupop-div-shouquan p{
	padding:10px 7px;
	border-bottom:1px solid #E4E3E5;
	}
	.cupop-div-shouquan div span{text-align:center;width:48%;display:inline-block;height:44px;line-height:44px;}
	@media only screen and (max-width: 375px) {
	     .cupop-div-shouquan{font-size: 12px;}
	}
	@media only screen and (min-width: 375px) {
		.cupop-div-shouquan{font-size: 16px;}
	}
</style>
</head>
<body>
	<div class="back_img">
		<div><img src="" alt="" /><span></span></div>
	</div>
	

	<div class="cupop-div project-cupop-1">
		<div class="cupop-div-bg"></div>
		<div class="cupop-div-cont">
			<div class="cupop-div-advisor">
				<h5>开通江西银行托管账户</h5>
				<img src="${ctx}/img/openAccount.png?v=20171127" alt="">
				<p class="tac text_grey">汇盈金服使用江西银行的第三方资金托管服务，投资更安全！</p>
				<div>
					<span class="openPopDivSpan"><a class="colorOrange" href="">马上开通</a></span>
				    <span class="openPopDivClose">稍后开通 </span>
				</div>
				
			</div>
		</div>
	</div>
	
	<div class="cupop-div  project-cupop-2">
		<div class="cupop-div-bg"></div>
		<div class="cupop-div-cont">
			<div class="cupop-div-shouquan">
				<p class="tac text_grey">为更好保证您的投资利益，您需在完成风险测评后才可进行投资！</p>
				<div>
					<span class="openPopDivSpan"><a class="colorOrange" href="">立即测评</a></span>
				    <span class="openPopDivClose">稍后再说 </span>
				</div>
				
			</div>
		</div>
	</div>
	<div class="cupop-div project-cupop-3">
		<div class="cupop-div-bg "></div>
		<div class="cupop-div-cont">
			<div class="cupop-div-shouquan">
				<p class="tac text_grey">该产品需开通自动投标功能，立即授权？</p>
				<div>
					<span class="openPopDivSpan"><a class="colorOrange" href="">确认</a></span>
				    <span class="openPopDivClose">取消</span>
				</div>
				
			</div>
		</div>
	</div>
	
	<div class="cupop-div project-cupop-4">
		<div class="cupop-div-bg "></div>
		<div class="cupop-div-cont">
			<div class="cupop-div-shouquan">
				<p class="tac text_grey">用户状态异常，请联系客服</p>
				<div>
				    <span class="openPopSingle colorOrange openPopDivClose">我知道了</span>
				</div>
				
			</div>
		</div>
	</div>
    <div class="cupop-div project-cupop-5">
		<div class="cupop-div-bg "></div>
		<div class="cupop-div-cont">
			<div class="cupop-div-advisor">
				<h5>提示</h5>
				<img src="${ctx}/img/openAccount.png" alt="">
				<p class="tac text_grey">您尚未设置交易密码</p>
				<div>
					<span class="openPopDivSpan"><a class="colorOrange" href="">马上设置</a></span>
				    <span class="openPopDivClose">稍后再说</span>
				</div>
				
			</div>
		</div>
	</div>
	<div class="specialFont response detail_container">
		<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
		<input type="hidden" name="platform" id="platform" value="${platform}" />
		<input type="hidden" name="isSetPassword" id="isSetPassword" value="${isSetPassword}" />
		<input type="hidden" name="sign" id="sign" value="${sign}" /> 
		<input type="hidden" name="randomString" id="randomString" value="${randomString}" />
		<input type="hidden" name="order" id="order" value="${order}" />
		<!-- 登录的情况下 -->
		<c:if test="${not empty token}">
			<input type="hidden" name="token" id="token" value="${token}" />
			<input type="hidden" name="loginUrl" id="loginUrl" value="${loginUrl}" />
			<input type="hidden" name="bankOpenFlag" id="bankOpenFlag" value="${bankOpenFlag}" /><!-- 0未开户 1已开户 -->
			<input type="hidden" name="openAccountUrl" id="openAccountUrl" value="${openAccountUrl}" /><!-- 开户链接 -->
			<input type="hidden" name="isSetPassword" id="isSetPassword" value="${isSetPassword}" /><!-- 0未设置 1已设置：未设置密码去设置 -->
			<input type="hidden" name="forbiddenFlag" id="forbiddenFlag" value="${forbiddenFlag}" /><!-- 0 未禁用(开启) 1禁用(未开启)：未开启报消息 -->
			<input type="hidden" name="riskFlag" id="riskFlag" value="${riskFlag}" /> <!-- 0未测评 1已测评：未评测去评测链接 -->
			<input type="hidden" name="autoInvesFlag" id="autoInvesFlag" value="${autoInvesFlag}" /><!-- 0: 未授权  1:已授权：未授权去授权路径 -->
			<input type="hidden" name="mobile" id="mobile" value="${mobile}" />
			<input type="hidden" name="isSetPassword" id="isSetPassword" value="${isSetPassword}" />
			<input type="hidden" name="riskUrl" id="riskUrl" value="${riskUrl}" />
			<input type="hidden" name="authUrl" id="authUrl" value="${authUrl}" />
		</c:if>
		
		<!-- 标的组成链接 -->
		<c:if test="${not empty planDetail.tabTwoUrl}">
			<input type="hidden" name="consumeUrl" id="consumeUrl" value="${planDetail.tabTwoUrl}" />
		</c:if>
		
		<!-- 加入记录链接 -->
		<c:if test="${not empty planDetail.tabThreeUrl}">
			<input type="hidden" name="accedeUrl" id="accedeUrl" value="${planDetail.tabThreeUrl}" />
		</c:if>
		<!--列表详情头部开始-->
			<div class="detailHead">
			<span class="detailHeadLeft"> 
				<i class="orange"><b>${planDetail.planApr}</b>%</i>
				<em>历史年回报率</em>
			</span>
			<span class="detailHeadMiddle">
				<i><b>${planDetail.planPeriod}天 </b></i> 
				<em>锁定期限</em>
			</span> 
			<span class="detailHeadRight"> 
				<i><b><fmt:formatNumber value="${planDetail.availableInvestAccount}" pattern="#,###" /> </b>元</i> 
				<em>开放额度</em>
			</span>
			<div class="clearBoth"></div>
		</div>
		<!--列表详情头部结束-->
		<div class="grayHeight"></div>
		<!--列表详情内容开始-->
		<div class="detailContent detailContent-mid">
			<ul>
			 	<li>
					<span class="left">计划名称：</span>
					<span class="right">${planDetail.planName}</span>
				</li>
				<li>
				
					<span class="left">计息时间：</span> 
					<span class="right">计划进入锁定期后开始计息</span>
				</li>
				<li>
					<span class="left">还款方式：</span>
					<span class="right return-type">${planDetail.borrowStyleName}</span>
				</li>
				<li>
				    <span class="left">温馨提示：</span> 
				    <span class="right">市场有风险，投资需谨慎<br/>历史回报不等于实际收益<br/>建议投资者类型：稳健型及以上</span>
				</li>
			</ul>
			<div class="clearBoth"></div>
		</div>
		<div class="grayHeight"></div>
		<!--tab开始-->
		<div class="tabTitle">
			<span class="active" id="tab1">
				计划介绍<i class="detail_line"></i>
			</span>
			<span id="tab2">标的组成</span>
			<span id="tab3">加入记录</span>
			<span id="tab4">常见问题</span>
		</div>
		<div class="tabContents" style="padding-top: 0;">
				<!--计划介绍开始-->
				<div class="tabItem active" id="infoOneId">
					<div class="item">
						<div class="detailContent clearPadding">
							<ul>
								<li>
								     <span class="left">计划名称：</span>
								     <span class="right">${planDetail.planName}</span>
								</li>
								<li>
								     <span class="left">计划介绍：</span> 
								     <span class="right">${planDetail.planConcept}</span>
								</li>
								<li>
								     <span class="left">历史年回报率：</span> 
								     <span class="right">${planDetail.planApr}% （历史年回报率不代表实际收益）</span>
								</li>
								<li>
								     <span class="left">锁定期限：</span> 
								     <span class="right">${planDetail.planPeriod}天</span>
								</li>
								<li>
								     <span class="left">还款方式：</span> 
								     <span class="right">${planDetail.borrowStyleName}</span>
								</li>
								<li>
								     <span class="left">投资范围：</span> 
								     <span class="right">请参考标的组成</span>
								</li>
								<li>
								     <span class="left">加入条件：</span> 
								     <span class="right">${planDetail.debtMinInvestment}元起投，以${planDetail.debtInvestmentIncrement}元的倍数递增</span>
								</li>
								<li>
								     <span class="left">计息时间：</span> 
								     <span class="right">计划进入锁定期后开始计息</span>
								</li>
								<li>
								     <span class="left">退出方式：</span> 
								     <span class="right">系统将通过债权转让自动完成退出，您所持债权转让完成的具体时间，视债权转让市场交易情况而定。</span>
								</li>
								<li>
								     <span class="left">到账时间：</span> 
								     <span class="right">计划退出后，预计1-3个工作日内到账具体到 账时间，以实际运营情况为准</span>
								</li>
								<li>
								     <span class="left">费用：</span> 
								     <span class="right">无相关费用</span>
								</li>
								<li>
								     <span class="left">计划服务协议：</span> 
								     <span class="right">点击查看<a href="javascript:;" data-href="${ctx}/hjhagreement/hjhInfo.do" class="agree">《投资服务协议》</a></span>
								</li>
								<li>
								     <span class="left">投资标的协议：</span> 
								     <span class="right">
								     	<span style="display:inline-block;width:60px">点击查看</span>
								     	<a href="javascript:;" data-href="${ctx}/hjhagreement/intermediaryServices.do" class="agree">《居间服务协议》</a>
								     	<br/>
								     	<span style="display:inline-block;width:60px"></span>
								     	<a href="javascript:;" data-href="${ctx}/hjhagreement/hjhDiaryAgreement.do" class="agree" style="display:inline-block">《借款协议》</a>
								     </span>
								</li>
							</ul>
							<div class="clearBoth"></div>
						</div>
					</div>
				</div>	
				<!--计划介绍结束-->
				
				<!--标的组成AJAX请求开始-->
				<div class="tabItem" id="infoTwoId">
					<div class="tac marginTop">
						<img src="${ctx}/img/loadingImg.gif" alt="" class="loadingImg">
					</div>
				</div>
				
				<!--标的组成结束-->
				
				<!--加入记录开始-->
				<div class="tabItem" id="infoThreeId">
					<div class="tac marginTop">
						<img src="${ctx}/img/loadingImg.gif" alt="" class="loadingImg">
					</div>
				</div>
				<!--加入记录结束-->
				
				<!-- 常见问题开始 -->
		        <div class="tabItem">
		        	<div class="item">
						<div class="jobs-list">
							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">1、"计划"安全吗? </span>
								</dt>
								<dd>
									<p>汇盈金服以严谨负责的态度对每笔借款进行严格筛选，同时，"计划"所对应借款均适用汇盈金服用户利益保障机制。</p>
								</dd>
							</dl>
							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">2、"计划"的"锁定期"是什么？ </span>
								</dt>
								<dd>
									<p>"计划"出借计划具有收益期锁定限制，锁定期内，用户不可以提前退出。</p>
								</dd>
							</dl>
							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">3、加入"计划"的用户所获收益处理方式有几种？</span>
								</dt>
								<dd>
									<p>本计划提供两种收益处理方式：循环复投或用户自由支配。计划退出后，用户的本金和收益将返回至其汇盈金服账户中，供用户自由支配。</p>
								</dd>
							</dl>
							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">4、"计划"通过何种方式实现自动投标？</span>
								</dt>
								<dd>
									<p>"计划"不设立平台级别的中间账户，不归集出借人的资金，而是为出借人开启专属计划账户，所有资金通过该专属计划账户流动。</p>
								</dd>
							</dl>
							<dl>
								<dt class="iconfont iconfont-gotop" data-id="">
									<span class="title">5、"计划"到期后，我如何退出并实现收益？ </span>
								</dt>
								<dd>
									<p>"计划"到期后，系统将自动进行资金结算，结算完成后的本金及收益将从用户"计划"账户自动划转至用户普通账户中，用户在T+3个工作日内收到资金。</p>
								</dd>
							</dl>
					    </div>
		            </div>
		       </div>
		</div>
		<!--tab结束-->
		<!--列表详情底部开始-->
		<div class="foot-left">
			<a href=""><img src="${ctx}/img/cal.png" /></a>
		</div>
		<div class="foot bg_grey_new">
			<!-- 立即加入 -->
			<c:if test="${planDetail.planStatus eq 1}">
				<c:if test="${planDetail.availableInvestAccount gt '0.00'}">
					<span class="btn_bg_color textWhite"><a id="investNow"
						class="textWhite hy-jumpH5">立即加入</a></span>
				</c:if>		
			</c:if>
			<!-- 稍后开启1 -->
			<c:if test="${planDetail.planStatus eq 1}"> <!-- 计划关闭 -->
				<c:if test="${planDetail.availableInvestAccount eq '0.00'}">
					<span>稍后开启</span>
				</c:if>	
			</c:if>
			<!-- 稍后开启2 -->
			<c:if test="${planDetail.planStatus eq 2}"> <!-- 计划关闭 -->
				<span>稍后开启</span>
			</c:if>
			
		</div>
		<!--列表详情底部结束-->
		
	</div>
	<script src="${ctx}/js/doT.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/js/jquery.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>	
	<script src="${ctx}/js/fill.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/idangerous.swiper.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/js/jquery.bxslider.js" type="text/javascript"
		charset="utf-8"></script>
	<script src="${ctx}/js/util.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
		//设置计算器URL
		$(function(){
				var urlCurrent = window.location.host+"/hyjf-app/jsp/cal.jsp";
				var types = ["按月计息，到期还本还息","按天计息，到期还本还息","先息后本","等额本息","等额本金","按月计息，到期还本息","按天计息，到期还本息"]
				var percent = parseInt($(".detailHeadLeft b").text());
				var time = parseInt($(".detailHeadMiddle").text());
				var type = $.trim($(".return-type").text())
				for(var i = 0;i<5;i ++){
					if(types[i]==type){
						var calType = i;
					}
				}
				if(!calType){
					for(var j = 0;j<2;j ++){
						if(types[j+5]==type){
							var calType = j;
						}
					}
				}
				$(".foot-left a").prop("href",hyjfArr.hyjf+'://jumpH5/?{"url":"http://'+urlCurrent+'?percent='+percent+'&time='+time+'&calType='+calType+'"}')
				
				//设置图片居中
				var leftFootHeight = ($(".foot-left").height()-25)/2;
				$(".foot-left img").css("margin-top",leftFootHeight+"px")
				//设置计算器背景色
				if($(".foot span").hasClass("btn_bg_color")){
					$(".foot-left").css("background","#eb6100")
				}else{
					$(".foot-left").css("background","#999999")
				}
				
				//设置协议跳转链接
				$(".agree").each(function(){
					var url = $(this).data('href');
					$(this).attr("href",hyjfArr.hyjf+'://jumpH5/?'+'{"url": "'+hyjfArr.locationOrigin+url+'"}');
				})
		})
	</script>
    
    
    <script id="tmpl-towData" type="text/x-dot-template">
        <c:if test="${empty token}">
			<p class="tac marginTop">登录用户可见计划详情，<a href="${planDetail.loginUrl}/?" class="colorBlue hy-jumpLogin">点击登录</a></p>
		</c:if>
		<c:if test="${not empty token}">
		{{? it.consumeList!=0}}
			<div class="item" id="moreTable1">
				<table border="0" cellspacing="0" cellpadding="0" class="borderNone" >
					<tr>
						<th>项目编号</th>
						<th width="30%">历史年回报率</th>
						<th width="24%">项目期限</th>
					</tr>
					{{~ it.consumeList : v : index}}
						<tr>
							<td><a href="${jumpcommend}://jumpH5/?{'url':'{{=location.origin}}${ctx}/hjhproject/getProjectDetail?borrowNid={{=v.borrowNid}}&order=${order}&randomString=${randomString}&token=${token}&sign=${sign}&platform=${platform}&realPlatform=${realPlatform}&version=${version}'}">{{=v.borrowNid}}</a></td>
    						<td>{{=v.borrowApr}}%</td>            						
    						<td>{{=v.borrowPeriod}}</td>
						</tr>
					{{~}}
					
				</table>
				{{if(it.consumeTotal>10){}}
				<div class="loadMore" >加载更多...</div>
				{{}else{}}
				<div></div>
				{{}}}
			</div>
			{{??}}<p class="tac marginTop">暂无可投标的</p>
		{{?}}
        </c:if>
	</script>

	<script id="tmpl-threeData" type="text/x-dot-template">
		<c:if test="${empty token}">
			<p class="tac marginTop">登录用户可见计划详情，<a href="${planDetail.loginUrl}/?" class="colorBlue hy-jumpLogin">点击登录</a></p>
		</c:if>
		<c:if test="${not empty token}">
		{{? it.accedeList!=0}}
			<p class="tac marginTop">
			加入总人次 : {{= it.accedeTimes}}  加入金额 : {{= it.accedeTotal}}元
			</p>

			<div class="item" id="moreTable2">
				<table border="0" cellspacing="0" cellpadding="0" class="borderNone" >
					<tr>
						<th></th>
						<th>投资人</th>
						<th>加入金额（元）</th>
						<th>加入时间</th>
					</tr>
					
					{{~ it.accedeList : v : index}}
						<tr>
							<td class="padding-0">{{? v.vipId!=0}}<img src="${ctx}/img/project-vip.png" alt="vip" class="project-vip-img"/>{{?}}</td>
    						<td>{{=v.userName}}</td>            						
    						<td>{{=v.accedeAccount}}</td>
    						<td>{{=v.accedeTime}}</td>
						</tr>
					{{~}}
					
				</table>
				{{if(it.recordTotal>10){}}
				<div class="loadMore" >加载更多...</div>
				{{}else{}}
				<div></div>
				{{}}}
			</div>
			{{??}}<p class="tac marginTop">暂无加入记录</p>
		{{?}}
		</c:if>
	</script>
	
	<script>
	//loadmore ajax
	//no.2 load more	
		 $("#infoTwoId").delegate(".loadMore","click",function(){
				var $page = Math.floor(($("#moreTable1 table tr").length-1)/10)+1;
				var $planNid = "${planDetail.planNid}";	
				var $platform="${platform}";
				var $token="${token}";
				var $sign="${sign}";
				var $randomString="${randomString}";
				var $order="${order}";
				var $consumeUrl=$("#consumeUrl").val();/* 获取标的组成的URL */

				if($token){
					data = {planNid:$planNid,page:$page,platform:$platform,token:$token,sign:$sign,randomString:$randomString,order:$order}
				}else{
					data ={planNid:$planNid,page:$page,platform:$platform,sign:$sign,randomString:$randomString,order:$order}
				} 
				 $.ajax({
						type:"get",
				 		url:$consumeUrl,
						data:{planNid:$planNid,page:$page,platform:$platform,sign:$sign,randomString:$randomString,order:$order},
						datatype:"json",
						success:function(data){
							var totalPage = Math.floor(data.consumeTotal/10)+1;
							if(!data){
								$("#moreTable1 .loadMore").hide();
							}else if(data){
								var str="";
								for(var i=0;i<data.consumeList.length;i++){
									var url = location.origin+"${ctx}/hjhproject/getProjectDetail?borrowNid="+data.consumeList[i].borrowNid+"&order=${order}&randomString=${randomString}&token=${token}&sign=${sign}&platform=${platform}&realPlatform=${realPlatform}&version=${version}";
									var query = "{'url':'"+url+"'}";
											str=str+'<tr><td>'
											+'<a href="${jumpcommend}://jumpH5/?'+query+'">'+data.consumeList[i].borrowNid+'</a>'
										+'</td><td>'
										+data.consumeList[i].borrowApr+'%</td><td>'+data.consumeList[i].borrowPeriod+'</td></tr>';
								}
								$("#moreTable1 table").append(str);
								if(totalPage==$page){
									$("#moreTable1 .loadMore").hide();
								}
							}						
						} 		
				}) 
			}) 
	</script>
	
	
	<script>
	//no.3 load more
	$("#infoThreeId").delegate(".loadMore","click",function(){
		var $page = Math.floor(($("#moreTable2 table tr").length-1)/10)+1;
		var $planNid = "${planDetail.planNid}";
		var $platform="${platform}";
		var $token="${token}"; 
		var $sign="${sign}";
		var $randomString="${randomString}";
		var $order="${order}";
		var $accedeUrl=$("#accedeUrl").val();/* 获取标的组成的URL */
			 if($token){		
				data = {planNid:$planNid,page:$page,platform:$platform,token:$token,sign:$sign,randomString:$randomString,order:$order};
			}else{
				data ={planNid:$planNid,page:$page,platform:$platform,sign:$sign,randomString:$randomString,order:$order};
			} 
			 $.ajax({
					type:"get",
					url:$accedeUrl,
					data:data,
					datatype:"json",
					success:function(data){
						var totalPage = Math.floor(data.recordTotal/10)+1;
						if(!data){
							$("#moreTable2 .loadMore").hide();
						}else if(data&&data.status=="0"){
							var str="";
							
							var proImg = "";
								for(var i=0;i<data.accedeList.length;i++){
									var vipimg = data.accedeList[i].vipId != '0'?'<img src="${ctx}/img/project-vip.png" alt="vip" class="project-vip-img"/>':'';
									str += '<tr>'
									+ '<td class="padding-0">'+vipimg+'</td>'
									+'<td>'+data.accedeList[i].userName+'</td>'
									+'<td>'+data.accedeList[i].accedeAccount+'</td>'
		    						+'<td>'+data.accedeList[i].accedeTime+'</td>'  
		    						+'</tr>'
								}
							$("#moreTable2 table").append(str);
							if(totalPage==$page){
								$("#moreTable2 .loadMore").hide();
							}
						}						
					} 		
			}) 
		})
	</script>
	
	<script>
	//增加href中 planNid
	function addId(){
		var $planNid = "${planDetail.planNid}";
		var $order="${order}";
		var $token="${token}"; 
	    var $platform="${platform}";
	    var $sign="${sign}";
	    var $randomString="${randomString}";
	    var $mobile = $("#mobile").val();
	    var $openAccountUrl = $("#openAccountUrl").val();
	    var $riskUrl = $("#riskUrl").val();
	    var $authUrl = $("#authUrl").val();
	    var $autoInvesFlag = $("#autoInvesFlag").val();
	    var url = "${planDetail.loginUrl}";
	    var $isSetPassword = $("#isSetPassword").val();
	    var $riskFlag = $("#riskFlag").val();
	    var $forbiddenFlag =$('#forbiddenFlag').val();
	    //console.log(url+'/?{\"url":\"'+$openAccountUrl+"/?token="+$token+"&order="+$order+"&platform="+$platform+"&sign="+$sign+"&randomString="+$randomString+"&mobile="+$mobile+'"}')
	   	//如果未设置交易密码 跳到设置交易密码页面
	   	
	   	$("#investNow").on("click",function(){
			if(url.indexOf("jumpLogin")!=-1){//是否登录
		    	$(this).prop("href",url+"/?");
		   	}else if(url.indexOf("jumpH5")!=-1){    //	开户
			    	var $nHref = url+'/?{"url":"'+$openAccountUrl+"?token="+$token+"&order="+$order+"&platform="+$platform+"&sign="+$sign+"&randomString="+$randomString+"&mobile="+$mobile+'"}';
			    	$('.project-cupop-1').show();
			    	$(".project-cupop-1 .openPopDivSpan a").prop("href",$nHref);
			}else if($isSetPassword == 0){ //设置交易密码
		   		var $isHref = "${jumpcommend}://jumpH5/?{'url':'"+location.origin+"${ctx}/bank/user/transpassword/setPassword.do?sign="+$sign+"&token="+$token+"'}";
			    	$('.project-cupop-5').show();
			    	$(".project-cupop-5 .openPopDivSpan a").prop("href",$isHref);
		    }else if($forbiddenFlag == 1){//是否启用
		   		$('.project-cupop-4').show();
		   	}else if($riskFlag == 0){	//测评
				var $rHref = "${planDetail.riskUrl}"+'/?{"url":"'+$riskUrl+"?token="+$token+"&order="+$order+"&platform="+$platform+"&sign="+$sign+"&randomString="+$randomString+"&mobile="+$mobile+'"}';
			    	$('.project-cupop-2').show();
			    	$(".project-cupop-2 .openPopDivSpan a").prop("href",$rHref);
		    }else if($autoInvesFlag == 0){ //授权
			    	var $aHref = "${planDetail.authUrl}"+'/?{"url":"'+$authUrl+"?token="+$token+"&order="+$order+"&platform="+$platform+"&sign="+$sign+"&randomString="+$randomString+'"}';
			    	$('.project-cupop-3').show();
			    	$(".project-cupop-3 .openPopDivSpan a").prop("href",$aHref);
		    }else{  
			    	/* 跳转投资(为配合移动端将planNid换成borrowNid方便处理) */
			    	var $newHref = url+"/?{"+"\"borrowNid\":\""+ $planNid+"\",\"type\":\"HJH\"}";
			     $("#investNow").prop("href",$newHref);
		    }
		})
	}
	   	
	addId();
	$(".openPopDivClose").on("click",function(){
		$(".cupop-div").hide()
	})
	</script>
	
	<script>
	var $token=$("#token").val();
	
	/* 标的组成 */
	$("#tab2").click(
		function(){
			if($token){
				$.fillTmplByAjax("${planDetail.tabTwoUrl}",{"planNid":"${planDetail.planNid }","platform":"${platform}","token":"${token}","sign":"${sign}","randomString":"${randomString}","order":"${order}"}, "#infoTwoId", "#tmpl-towData");		
			}else{
				$.fillTmplByAjax("${planDetail.tabTwoUrl}",{"planNid":"${planDetail.planNid }","platform":"${platform}","sign":"${sign}","randomString":"${randomString}","order":"${order}"}, "#infoTwoId", "#tmpl-towData");
			}
		}
	);
	
	/* 加入记录  */
	$("#tab3").click(
		function(){
			$(".tabContents").css("height","auto");
			if($token){
				$.fillTmplByAjax("${planDetail.tabThreeUrl}",{"planNid":"${planDetail.planNid }","platform":"${platform}","token":"${token}","sign":"${sign}","randomString":"${randomString}","order":"${order}"}, "#infoThreeId", "#tmpl-threeData");		
			}else{
				$.fillTmplByAjax("${planDetail.tabThreeUrl}",{"planNid":"${planDetail.planNid }","platform":"${platform}","sign":"${sign}","randomString":"${randomString}","order":"${order}"}, "#infoThreeId", "#tmpl-threeData");
			}
		}
	);
	
	</script>
</body>

	<script>
	$(function() {
		$(".jobs-list dl dt").click(
				function() {
					var dt = $(this);
					var dl = dt.parent("dl");
					var otherdl = dl.siblings("dl");
					var dd = dt.next("dd");
					if (dd.is(":hidden")) {
						dd.slideDown(500);
						dl.addClass("active");
						otherdl.removeClass("active")
								.children("dd:visible").slideUp(500);
					} else {
						dd.slideUp(500);
						dl.removeClass("active");
					}
				})
	});
	
	</script>
</body>
	
</html>