<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>中秋·国庆双节活动 - 真诚透明自律的互联网金融服务平台</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<style>
    *{
      margin:0;
      padding:0;
    }
    .banner{
      background:url("${cdn}/dist/images/2018-mid-autumn/banner.png") top center no-repeat;
      height: 1166px;
    }
		.mid-autumn{
		   background:url("${cdn}/dist/images/2018-mid-autumn/bg2.png");
       text-align: center;
			 clear:both;
		}
    .intro{
		   background:url("${cdn}/dist/images/2018-mid-autumn/intro.png") center no-repeat;
       width: 1040px;
       height: 340px;
       margin:-497px auto 50px;
		}
    .intro-bg{
       width:850px;
       padding:60px 20px;
       margin:auto;
    }
    .intro-bg .intro-title{
      text-align: left;
      font-size: 24px;
      color:#da1b1b;
      margin-bottom: 20px;
      line-height: 48px;
    }
    .warm-tips{
      text-align: left;
      font-size: 16px;
      line-height: 32px;
    }
    .warm-tips-highlight{
      font-size: 18px;
      color:#fb7a07;
    }
    .festival-menu{
      position: fixed;
      top:200px;
      right: 30px;
      width:262px;
      height:596px;
      background:url("${cdn}/dist/images/2018-mid-autumn/fixnav.png") -10px center no-repeat;
      z-index: 1;
    }

    .festival-menu .up-top{
      margin-top: 50px;
      margin-left:-15px;
    }
    .festival-menu-box{
      margin-top:220px;
    }
    .festival-menu-tab{
      display: inline-block;
      background-color: #fcbb1d;
      border:3px solid #fff4dc;
      color:#b30101;
      padding:15px 0;
      width:160px;
      text-decoration: none;
    }
    .festival-menu-tab.active{
      width:200px;
      color:#fff;
      background-color:#b30101;
    }
		.my-reward-btn{
      position: fixed;
      top:800px;
      right: 0;
      color:#fb0101;
      font-size: 24px;
      background-color:#ffdb3c;
      box-shadow: 0px 10px 30px #8a0101;
      padding:26px 114px 26px 70px;
      border-radius: 80px;
      margin-right: -25px;
      z-index: 1;
		}
		.festival-menu-tab:hover,.my-reward-btn:hover,
		.up-top:hover,.my-reward-cancel:hover{
			cursor: pointer;
		}
    .footer{
    }
    .festival-title{
      height: 286px;
      max-width:1919px;
      margin:auto;
      line-height: 286px;
      position: relative;
      font-size: 34px;
      color:#b30101;
    }
    .festival-content:nth-of-type(2n) .festival-title{
      background:url("${cdn}/dist/images/2018-mid-autumn/odd.png") center no-repeat;
    }
    .festival-content:nth-of-type(2n+1) .festival-title{
      background:url("${cdn}/dist/images/2018-mid-autumn/even.png") center no-repeat;
    }
    .icon-mooncake{
      width:88px;
      height:90px;
      background:url("${cdn}/dist/images/2018-mid-autumn/mooncake.png") center no-repeat;
      margin:auto 20px;
      display: inline-block;
      vertical-align: middle;
    }
    .icon-yuanbao{
      width:107px;
      height:79px;
      background:url("${cdn}/dist/images/2018-mid-autumn/gold.png") center no-repeat;
      margin:auto 20px;
      display: inline-block;
      vertical-align: middle;
    }
		.festival-list{
      position: relative;
      max-width: 1527px;
      margin:0 auto;
    }
    .festival-content:nth-of-type(2n) .festival-list::before{
      content: url("${cdn}/dist/images/2018-mid-autumn/denglong.png");
      position: absolute;
      left:0;
      top:-104px;
    }
    .festival-content:nth-of-type(2n) .festival-list::after{
      content: url("${cdn}/dist/images/2018-mid-autumn/denglong.png");
      position: absolute;
      right:0;
      top:0;
    }
    .festival-content:nth-of-type(2n+1) .festival-list::before{
      content: url("${cdn}/dist/images/2018-mid-autumn/denglong.png");
      position: absolute;
      left:0;
      top:0;
    }
    .festival-content:nth-of-type(2n+1) .festival-list::after{
      content: url("${cdn}/dist/images/2018-mid-autumn/denglong.png");
      position: absolute;
      right:0;
      top:-104px;
    }
    .festival-gift{
      display: inline-block;
      vertical-align: middle;
      margin:20px;
    }
    .festival-gift.jiaxiquan{
      background:url("${cdn}/dist/images/2018-mid-autumn/jiaxiquan_bg.png") center no-repeat;
      width:240px;
      height:225px;
    }
    .festival-gift.jiaxiquan b{
      display: inline-block;
      margin-top:80px;
      font-size: 24px;
      color:#e81717;
    }
    .festival-gift.jiaxiquan span{
      display: inline-block;
      font-size: 14px;
      line-height: 30px;
      margin-top:20px;
    }
    .festival-gift.daijinquan{
    background:url("${cdn}/dist/images/2018-mid-autumn/daijinquan_bg.png") center no-repeat;
    width:263px;
    height:307px;
    }
    .festival-gift.daijinquan b{
      display: inline-block;
      margin-top:60px;
      font-size: 24px;
      color:#e81717;
    }
    .festival-gift.daijinquan span{
      display: inline-block;
      font-size: 14px;
      line-height: 30px;
      margin-top:20px;
    }
    .festival-gift.shiwu{
      background:url("${cdn}/dist/images/2018-mid-autumn/shiwu_bg.png") center no-repeat;
      width:257px;
      height:360px;
    }
    .festival-gift.shiwu img{
      margin-top:50px;
      margin-left: -15px;
    }
    .festival-gift.shiwu b{
      display: inline-block;
      margin-top:24px;
      font-size: 24px;
      color:#e81717;
			padding-right: 15px;
    }
    .festival-gift.shiwu-high b{
      margin-top:14px;
    }
    .festival-gift.shiwu span{
      display: inline-block;
      font-size: 14px;
      line-height: 30px;
      margin-top:26px;
			padding-right: 15px;
    }
    .rule-title{
      margin: 152px auto 55px;
    }
    .rule-bg{
      width:1100px;
      margin:auto;
      background-color: rgba(255,255,255,0.1);
      box-shadow: 0px 10px 38px #8a0101;
      border-radius: 20px;
      padding:16px;
    }
    .rule{
      background-color: #f8e9d6;
      box-shadow: 0px 0px 20px #f8e9d6;
      border-radius: 20px;
      padding:30px 55px;
      text-align: left;
      font-size: 14px;
      line-height: 34px;
      color:#d1092d;
    }
    ol{
      list-style-position: inside;
    }
    .footer-copyright{
      color:#fdb369;
      border:1px solid #fdb369;
      display: inline-block;
      margin-top: 38px;
      padding:7px 68px;
      font-size: 12px;
      line-height: 26px;
    }
    .footer{
      margin-top:-186px;
    }
    .operation-btn {
      margin:78px auto 90px;
      width:415px;
      height: 84px;
      background:url("${cdn}/dist/images/2018-mid-autumn/operation-btn.png") no-repeat;
      background-position: 0 -6px;
    }
    .operation-btn.unstart{
      background-position: 0 -6px;
    }
    .operation-btn.needlogin{
      background-position: 0 -93px;
    }
    .operation-btn.invest{
      background-position: 0 -178px;
    }
    .operation-btn.needlogin,.operation-btn.invest:hover{
      cursor: pointer;
    }
    .operation-btn.over{
      background-position: 0 -268px;
    }
		.hide{
			display: none;
		}
    /**弹出层样式**/
    .dialog{
      background: none;
      width: 1063px;
      margin-left: -531px;
    }
    .my-reward-bg{
      width:1063px;
      height:828px;
      background:url("${cdn}/dist/images/2018-mid-autumn/my-reward.png") center no-repeat;
      position: relative;
    }
    .my-reward-cancel{
      position: absolute;
      right:65px;
      top:104px;
    }
    .my-reward-list{
      margin-top:222px;
      margin-left: 62px;
      background-color: #fff4dc;
      padding:16px;
      width:926px;
      height: 426px;
      box-sizing: border-box;
      text-align: center;
      overflow: auto;
    }
    .my-reward-list thead{
      color:#ad0904;
      font-size: 20px;
    }
    .my-reward-list-title {
      text-align: center;
      position: absolute;
      top:240px;
      left:78px;
      font-size: 0;
    }
    .my-reward-list-title li{
      display: inline-block;
      list-style-type: none;
      font-size: 20px;
      padding:20px;
      color:#ad0904;
      background-color: #fff4dc;
    }
    .my-reward-list td{
      border:1px solid #ad0904;
      height:60px;
      color:#ad0904;
      font-size: 20px;
    }
    .my-reward-list td:nth-of-type(1),.my-reward-list-title li:nth-of-type(1){
      width:124px;
    }
    .my-reward-list td:nth-of-type(2),.my-reward-list-title li:nth-of-type(2){
      width:238px;
    }
    .my-reward-list td:nth-of-type(3),.my-reward-list-title li:nth-of-type(3){
      width:170px;
    }
    .my-reward-list td:nth-of-type(4),.my-reward-list-title li:nth-of-type(4){
      width:150px;
    }
    .my-reward-list td:nth-of-type(5),.my-reward-list-title li:nth-of-type(5){
      width:210px;
    }
		</style>
	</head>

	<body id="top">
		<jsp:include page="/header.jsp"></jsp:include>
		<article class="mid-autumn">
	    <section>
	       <div class="banner"></div>
	       <div class="intro">
	         <div class="intro-bg">
	            <p class="intro-title">金秋十月，中秋遇国庆，2018年9月24日-2018年10月15日期间，在汇盈金服平台投资，即可获得相应礼包！让你的财富在双节假期实现全面增值！</p>
	            <div class="warm-tips">
	              <p>【温馨提示】</p>
	              <ol>
	                <li>本次活动，根据<span class="warm-tips-highlight">单笔投资金额</span>进行奖励。</li>
	                <li>点击某种奖励下方<span class="warm-tips-highlight">就近的“立即投资”按钮</span>——根据“获得条件”成功投资后，即可获得该种奖励。</li>
	              </ol>
	            </div>
	          </div>
	       </div>
	       <div class="festival-menu">
	         <div class="festival-menu-box">
	           <span class="festival-menu-tab active" authors="#mooncake01-festival">黄金豆沙月饼</span><br/>
	           <span class="festival-menu-tab" authors="#mooncake02-festival">黄金玫瑰月饼</span><br/>
	           <span class="festival-menu-tab" authors="#shiwu-festival">黄金周 · 元宝</span><br/>
	         </div>
	         <img id="up-top" class="up-top" src="${cdn}/dist/images/2018-mid-autumn/fixnav-top.png"/>
	       </div>
	       <div class="my-reward-btn" data-status="">我的双节奖励</div>
	      </section>
	      <section class="festival-content festival-content-section" id="mooncake01-festival">
	        <div class="festival-title">
	          <span class="icon-mooncake"></span>双节团圆福利·黄金豆沙月饼
	        </div>
	        <div class="festival-list">
	          <p>
	            <span class="festival-gift jiaxiquan">
	              <b>0.9%加息券</b><br/>
	              <span>1万≤单笔投资金额＜2万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift jiaxiquan">
	              <b>1.0%加息券  </b><br/>
	              <span>2万≤单笔投资金额＜3万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift jiaxiquan">
	              <b>1.1%加息券</b><br/>
	              <span>3万≤单笔投资金额＜5万 <br/>
	                投资期限不限</span>
	            </span>
	          </p>
	          <p>
	            <span class="festival-gift jiaxiquan">
	              <b>1.2%加息券</b><br/>
	              <span>5万≤单笔投资金额＜10万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift jiaxiquan">
	              <b>1.3%加息券</b><br/>
	              <span>10万≤单笔投资金额＜15万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift jiaxiquan">
	              <b>1.4%加息券</b><br/>
	              <span>15万≤单笔投资金额＜20万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift jiaxiquan">
	              <b>1.5%加息券</b><br/>
	              <span>单笔投资金额≥20万<br/>
	                投资期限不限</span>
	            </span>
	          </p>
	        </div>
	        <div class="operation-btn" data-type="1">
	        </div>
	      </section>
	      <section class="festival-content festival-content-section" id="mooncake02-festival">
	        <div class="festival-title">
	          双节爱心福利·黄金玫瑰月饼<span class="icon-mooncake"></span>
	        </div>
	        <div class="festival-list">
	          <p>
	            <span class="festival-gift daijinquan">
	              <b>40元代金券</b><br/>
	              <span>1万≤单笔投资金额＜2万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift daijinquan">
	              <b>80元代金券</b><br/>
	              <span>2万≤单笔投资金额＜3万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift daijinquan">
	              <b>120元代金券</b><br/>
	              <span>3万≤单笔投资金额＜5万 <br/>
	                投资期限不限</span>
	            </span>
	          </p>
	          <p>
	            <span class="festival-gift daijinquan">
	              <b>400元代金券</b><br/>
	              <span>5万≤单笔投资金额＜10万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift daijinquan">
	              <b>800元代金券</b><br/>
	              <span>10万≤单笔投资金额＜15万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift daijinquan">
	              <b>1200元代金券</b><br/>
	              <span>15万≤单笔投资金额＜20万 <br/>
	                投资期限不限</span>
	            </span>
	            <span class="festival-gift daijinquan">
	              <b>1600元代金券</b><br/>
	              <span>单笔投资金额≥20万<br/>
	                投资期限不限</span>
	            </span>
	          </p>
				<img src="${cdn}/dist/images/2018-mid-autumn/coupon_rule.png" style="margin-top: 50px;"/>
	        </div>

	        <div class="operation-btn" data-type="2">
	        </div>
	      </section>
	      <section class="festival-content festival-content-section" id="shiwu-festival">
	        <div class="festival-title">
	          <span class="icon-yuanbao"></span>黄金周·元宝
	        </div>
	        <div class="festival-list">
	          <p>
	            <span class="festival-gift shiwu">
	                <img src="${cdn}/dist/images/2018-mid-autumn/prize01.png"/>
	                <b>旅行四件套<small>(40元)</small></b><br/>
	                <span>1万≤单笔投资金额＜2万 <br/>
	                  投资期限≥3个月
									</span>
	            </span>
	            <span class="festival-gift shiwu">
	                <img src="${cdn}/dist/images/2018-mid-autumn/prize02.png"/>
	                <b>小米充电宝<small>(80元)</small></b><br/>
	                <span>2万≤单笔投资金额＜3万 <br/>
										投资期限≥3个月
									</span>
	            </span>
	            <span class="festival-gift shiwu">
	                <img src="${cdn}/dist/images/2018-mid-autumn/prize03.jpg"/>
	                <b>大闸蟹礼券  <small>(118元)</small></b><br/>
	                <span>3万≤单笔投资金额＜5万 <br/>
										投资期限≥3个月
									</span>
	            </span>
	          </p>
	          <p>
	            <span class="festival-gift shiwu shiwu-high">
	                <img src="${cdn}/dist/images/2018-mid-autumn/prize04.jpg"/>
	                <b>360行车记录仪<small>(400元)  </small></b><br/>
	                <span>5万≤单笔投资金额＜10万 <br/>
										投资期限≥6个月
									</span>
	            </span>
	            <span class="festival-gift shiwu shiwu-high">
	                <img src="${cdn}/dist/images/2018-mid-autumn/prize05.png"/>
	                <b>美菱除湿机<small>(800元)  </small></b><br/>
	                <span>10万≤单笔投资金额＜15万 <br/>
										投资期限≥6个月
									</span>
	            </span>
	            <span class="festival-gift shiwu">
	                <img src="${cdn}/dist/images/2018-mid-autumn/prize06.png"/>
	                <b>中石化加油卡<small>(1200元)  </small></b><br/>
	                <span>15万≤单笔投资金额＜20万 <br/>
										投资期限≥6个月
									</span>
	            </span>
	            <span class="festival-gift shiwu">
	                <img src="${cdn}/dist/images/2018-mid-autumn/prize07.jpg"/>
	                <b>驴妈妈礼品卡<small>(1600元)  </small></b><br/>
	                <span>单笔投资金额≥20万 <br/>
										投资期限≥6个月
									</span>
	            </span>
	          </p>
	        </div>

	        <div class="operation-btn" data-type="3">
	        </div>
	      </section>
	      <section class="festival-content">
	        <img class="rule-title" src="${cdn}/dist/images/2018-mid-autumn/rule-title.png"/>
	        <div class="rule-bg">
	          <div class="rule">
	            <ol>
	              <li>
	                活动期间，点击活动页“立即投资”按钮，并达到获得奖励所需条件，方可获得对应奖励；投资债权转让标的，不可参与本次活动。
	              </li>
	              <li>所获奖励类别，以投资达标时最近一次所点击的“立即投资”按钮选择的奖励类别为准。</li>
	              <li>多次投资并均达到奖励获得条件，则奖励可以叠加获得。</li>
	              <li>散标按投资成功时间，计划按加入时间判定是否为活动期间内的投资。</li>
	              <li>加息券和代金券奖励，由系统实时发放至用户账户，有效期均为7天，自获得之日起开始计算，逾期不补，且不可叠加使用。</li>
	              <li>实物奖励以实际发放为准，由工作人员在活动结束后15个工作日内，电话联系获奖用户进行发放，用户汇盈金服账号绑定的手机号即为获奖联系方式，若始终无人接听，则视为放弃奖励，不予补发。</li>
	              <li>本次活动奖励，不与汇盈金服其他活动奖励同享且不可随意更换。</li>
	              <li>如有疑问，请致电汇盈金服全国免费热线400-900-7878咨询。</li>
	              <li>汇盈金服保留在法律规定范围内对上述规则进行解释的权利。</li>
	            </ol>
	          </div>
	        </div>
	        <div class="footer-copyright">
	          <div class="">
	                  <span>© 汇盈金服 All rights reserved | 惠众商务顾问（北京）有限公司 京ICP备13050958号 | <a class="a-link" href="/homepage/systemSafetyLevelProtectionRecordInit.do" target="_blank">信息系统安全等级保护备案证明（三级）</a></span>
	                  <br/>
	                  <span>市场有风险  投资需谨慎 | 历史回报不等于实际收益</span>
	          </div>
	        </div>
	      </section>
	      <img class="footer" src="${cdn}/dist/images/2018-mid-autumn/footerbg.png"/>
	    </article>
	    <div class="dialog dialog-alert" id="myReward">
	        <div class="my-reward-bg">

	          <img class="my-reward-cancel" id="myRewardCancel" src="${cdn}/dist/images/2018-mid-autumn/close.png"/>
	          <div style="height:1px;"></div>
	          <!-- <ul class="my-reward-list-title">
	            <li>序号</li>
	            <li>单笔投资金额（元）</li>
	            <li>产品类型</li>
	            <li>产品期限</li>
	            <li>奖励名称</li>
	          </ul> -->
	          <div class="my-reward-list">
	          <table cellspacing="0" cellpadding="0">
	            <thead>
	              <tr>
	                <td>序号</td>
	                <td>单笔投资金额（元）</td>
	                <td>产品类型</td>
	                <td>产品期限</td>
	                <td>奖励名称</td>
	              </tr>
	            </thead>
	            <tbody>
	            </tbody>
	          </table>
	          </div>
	        </div>
	    </div>
			<script src="${cdn}/dist/js/lib/jquery.min.js"></script>
			<script src="${cdn}/dist/js/utils.js"></script>
			<script>
			//进入页面查询活动状态
			$.ajax({
				url:webPath + "/midautumn/getStatus.do",
				type:'get',
				data:{},
				success:function(data){
					if(data.status){
						switch(data.status){
							//活动未开始
							case "101":
								$(".operation-btn").addClass("unstart");
								$(".my-reward-btn").data("status","unstart");
								$(".my-reward-btn").addClass("hide");
								break;
							//活动已结束
							case "102":
								$(".operation-btn").addClass("over");
								break;
							//活动已开始，未登录
							case "999":
								$(".operation-btn").addClass("needlogin").data("status","needlogin");
								$(".my-reward-btn").data("status","needlogin");
								break;
							//活动已开始，已登录
							case "000":
								$(".operation-btn").addClass("invest").data("status","invest");
								break;
							default:
								break;
						}
					}else if(!data.status){
						utils.alert({id:'errorCode',title:'错误',content:"网络请求异常，请稍后再试"});
					}
				}
			});
			//状态按钮点击事件
			$(".operation-btn").unbind("click").click(function(){
				switch($(this).data("status")){
					case "needlogin":
						location.href=webPath +"/user/login/init.do?retUrl=/contentarticle/getSecurityPage.do?pageType=2018MidAutumn-Festival"
						break;
					case "invest":
					$.ajax({
						url:webPath + "/midautumn/register.do?activityType="+$(this).attr("data-type"),
						type:'post',
						data:{},
						success:function(data){
							if(data.status){
								switch(data.status){
									//活动已开始，未登录
									case "999":
										location.href=webPath +"/user/login/init.do?retUrl=/contentarticle/getSecurityPage.do?pageType=2018MidAutumn-Festival"
										break;
									//成功
									case "000":
										location.href=webPath+"/hjhplan/initPlanList.do";
										break;
									default:
										break;
								}
							}else if(!data.status){
								utils.alert({id:'errorCode',title:'错误',content:"网络请求异常，请稍后再试"});
							}
						}
					});
						break;
				}
			})
			//我的奖励点击事件
				$(".my-reward-btn").unbind("click").click(function(){
					switch ($(this).data("status")) {
						case "unstart":
							utils.alert({id:'errorCode',title:'温馨提示',content:"活动尚未开始，请耐心等待！"});
							break;
						case "needlogin":
							location.href=webPath +"/user/login/init.do?retUrl=/contentarticle/getSecurityPage.do?pageType=2018MidAutumn-Festival"
							break;
						default:
						//获取奖励列表
							$.ajax({
								url:webPath + "/midautumn/getAwardList.do?activityType="+$(this).attr("data-type"),
								type:'post',
								data:{},
								success:function(data){
									if(data.status){
										switch(data.status){
											//活动已开始，未登录
											case "999":
												location.href=webPath +"/user/login/init.do?retUrl=/contentarticle/getSecurityPage.do?pageType=2018MidAutumn-Festival"
												break;
											//成功
											case "000":
												$(".my-reward-list tbody").empty();
												if(data.list.length>0){
													$.each(data.list,function(n,val){
														$(".my-reward-list tbody").append("<tr><td>"+val.ids+"</td><td>"+val.investMoney+"</td><td>"+val.productType+"</td><td>"+val.productStyle+"</td><td>"+val.rewardName+"</td></tr>")
													})
												}else{
													$(".my-reward-list tbody").append("<tr><td colspan='5'>您还没有获得奖励噢，快去投资吧</td></tr>")
												}
												utils.alert({ id: "myReward" });
												break;
											default:
												break;
										}
									}else if(!data.status){
										utils.alert({id:'errorCode',title:'错误',content:"网络请求异常，请稍后再试"});
									}
								}
							});
							break;
					}
				})
	    //奖励菜单点击事件
		  var html=$('html,body'),menuClick=false;
		  $(".festival-menu .festival-menu-tab").click(function(){
		    var target=$(this);
		    menuClick=true;
		    $(".festival-menu .festival-menu-tab").removeClass('active');
		    $(this).addClass('active');
		    $(html).animate({
		          scrollTop: $($(this).attr('authors')).offset().top
		      }, 500 ,function(){
		        menuClick=false;
		      });
		  });

		  $("#up-top").click(function(){
		    $(html).animate({
		          scrollTop: $("#top").offset().top
		      }, 500 ,function(){
		        menuClick=false;
		      });
		  })
		  $(window).scroll(function(){
			    if(menuClick){
			      return;
			    }
			    var wst =  $(window).scrollTop()+1;
			    // var hst= $("#top").height()/2;
			    // /**判断距顶部大于top1/2 则显示为顶部**/
			    // if(wst<hst ){
			    //   $(".festival-menu .festival-menu-tab").removeClass('active');
			    //   $(".festival-menu .festival-menu-tab[authors='#mooncake01-festival']").addClass("active");
			    //   return;
			    // }
			    /**子菜单滚动条与菜单对应 **/
			    var len=$(".festival-content-section").length;
			    //var act=$("#right-nav span.active").attr("authors");
			    for (i=0; i<len; i++){
			      if($($(".festival-content-section")[i]).offset().top<wst || $($(".festival-content-section")[i]).offset().top==wst){
			        var ele=$($(".festival-content-section")[i]).attr('id');

			        $(".festival-menu .festival-menu-tab").removeClass('active');
			        $(".festival-menu .festival-menu-tab[authors='#"+ele+"']").addClass("active");
			    	}
			     }
			  });
		  </script>
	</body>
</html>
