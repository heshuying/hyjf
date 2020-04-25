<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head lang="zh-cmn-Hans">
<script type="text/javascript">
if (window.location.toString().indexOf('pref=padindex') != -1) {} else {
	if (/AppleWebKit.*Mobile/i.test(navigator.userAgent) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))) {
		if (window.location.href.indexOf("?mobile") < 0) {
			try {
				if (/Android|Windows Phone|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
					window.location.href = "http://weixin.huiyingdai.com"
				} else if (/iPad/i.test(navigator.userAgent)) {
					//window.location.href = "http://www.huiyingdai.com/?ipad"
				} else {
					window.location.href = "http://weixin.huiyingdai.com"
				}
			} catch (e) {}
		}
	}
}
</script>
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>汇盈金服 - 合作伙伴 - 真诚透明自律的互联网金融服务平台</title>
<meta name="keywords" content="互联网金融,出借理财,网络理财,互联网理财,p2p理财,汇盈金服，网贷平台,个人理财,P2P网贷平台，网络借贷，HuiYingDai.com" />
<meta name="description" content="汇盈金服huiyingdai.com - 提供安全、精准、高效的网络投融资服务。汇盈金服有严格的风险控制,大型机构100%本息担保,第三方资金托管,保障资金安全。" />
<!-- start: Mobile Specific -->

<meta name="mobile-agent" content="format=xhtml;url=http://weixin.huiyingdai.com" />

<!-- Set render engine for 360 browser -->
<meta name="renderer" content="webkit">

<!-- 禁止数字识自动别为电话号码 -->
<meta name="format-detection" content="telephone=no"/>

<!-- 添加 favicon icon -->
<link rel="shortcut icon" type="image/ico" href="http://www.hyjf.com/themes/default/i/favicon.ico" />

<!--<link rel="canonical" href="http://www.example.com/">-->
<link rel="stylesheet" type="text/css" href="http://www.hyjf.com/themes/default/css/2015news.css?vt2.2">
<script src="http://img.huiyingdai.com/themes/default/js/jquery.min.js"></script> 
<!--[if lt IE 9]><script src="http://img.huiyingdai.com/themes/default/js/html5shiv.min.js" type="text/javascript"></script><![endif]-->
<!--[if lt IE 10]><script src="http://img.huiyingdai.com/themes/default/js/jquery.placeholder.min.js" type="text/javascript"></script><![endif]-->
</head>

<body>
<!--[if lt IE 9]>
<div class="itips-ie"><div class="inner"> <i class="iconfont">&#xe61c;</i> 你的浏览器不支持本平台的一些新特性，请升级你的浏览器至<a target="_blank" rel="nofollow" href="http://se.360.cn/">360浏览器</a>或<a target="_blank" rel="nofollow" href="http://browsehappy.com/">Chrome</a>。</div></div>
<![endif]-->
<!--header start-->
<div class="hd-topbar">
  <div class="inner">
    <div class="fl"><b>客服热线：</b><span>4000-655-000（8:30-17:30）</span><b>关注我们：</b><a class="iconfont weixin" href="javascript:;">&#xe605;</a><a class="iconfont weibo" target="_blank" href="http://weibo.com/huiyingdai">&#xe60d;</a><a class="iconfont iqun" target="_blank" href="http://jq.qq.com/?_wv=1027&k=TYuR8a" title="点击加入官方交流群">&#xe628;</a></div>
    <div class="fr"><a  href="http://www.hyjf.com/activity.html">最新活动</a><em></em><a  href="http://www.hyjf.com/help.html">帮助中心</a><em></em><a  href="http://www.hyjf.com/article/guide.html">新手指引</a><em></em><a href="http://bbs.huiyingdai.com/" target="_blank" class="ibbs">互动论坛</a></div>
  </div>
</div>
<header>
  <div class="inner">
    <h1 class="new-logo fl"> <a href="/"> <img title="汇盈金服" alt="汇盈金服" src="http://img.huiyingdai.com/themes/default/images/logov2.png"></a></h1>
    <div class="new-nav fl">
      <ul>
         <li><a href="http://www.hyjf.com/">首页</a>
                </li>
         <li><a href="http://www.hyjf.com/invest.html">我要出借<span class="arrow-down"></span></a>
                <ul>
          <li class="dropdown"><em>◆</em><i>◆</i></li>
                    <li><a href="http://www.hyjf.com/invest/stock.html">汇天利</a></li>
                    <li><a href="http://www.hyjf.com/invest/prolist.html">汇直投</a></li>
                    <li><a href="http://www.hyjf.com/consume/prolist.html">汇消费</a></li>
                    <li><a href="http://www.hyjf.com/creditAssign/credit_list.html">汇转让</a></li>
                  </ul>
                </li>
         <li><a href="http://www.hyjf.com/loan.html">我要融资</a>
                </li>
         <li><a href="http://www.hyjf.com/article/security.html">安全保障</a>
                </li>
         <li class="active"><a href="http://www.hyjf.com/about.html">关于我们</a>
                </li>
              </ul>
    </div>
          <div class="userinfo">
      <div class="nav-userinfo" ><a class="btn-login" href="http://www.hyjf.com/web/login.html">登录</a> <a class="btn-signup" href="http://www.hyjf.com/web/register.html">注册</a> </div>
      <div class="mian-userinfo" style="display:none;">
        <div class="userinfo-avtar"><b><i class="iconfont">&#xe608;</i></b><a href="http://www.hyjf.com/user.html"><span><font id="this_user_info">我的资产</font><i class="iconfont">&#xe60f;</i></span></a></div>
        <ul>
          <li><a href="http://www.hyjf.com/user.html">资产总览</a></li>
          <li><a href="http://www.hyjf.com/user/tender_list.html">我的出借</a></li>
		  <li><a href="http://www.hyjf.com/user/credit_list.html">债权转让</a></li>
          <li><a href="http://www.hyjf.com/user/transaction.html">交易明细</a></li>
          <li><a href="http://www.hyjf.com/user/account_profile.html">安全中心</a></li>
          <li><a href="http://www.hyjf.com/web/logout.html">退出登录</a></li>
        </ul>
      </div>
	  </div>
  </div>
</header>
<!--header end--> 
 
<!--warp start-->
<section class="new-warp clf">
  <div class="inner"> <div class="about-nav fl">
      <h2>关于汇盈金服<i class="iconfont">&#xe60f;</i></h2>
       <ul class="yk_lt">
            <li  class="active"><a href="${pageContext.request.contextPath }/jsp/manager/content/content/about.jsp"><i></i>关于我们</a></li>
            <li ><a href="${pageContext.request.contextPath }/jsp/manager/content/content/boss.jsp"><i></i>创始人</a></li>
            <li ><a href="${pageContext.request.contextPath }/jsp/manager/content/content/partner.jsp"><i></i>合作伙伴</a></li>
            <li ><a href="${pageContext.request.contextPath }/jsp/manager/content/content/connect.jsp"><i></i>联系我们</a></li>
            <li ><a href="${pageContext.request.contextPath }/jsp/manager/content/content/job.jsp"><i></i>招贤纳士</a></li>
            <li ><a href="${pageContext.request.contextPath }/jsp/manager/content/content/event.jsp"><i></i>公司纪事</a></li>
            <li ><a href="http://www.hyjf.com/about/report.html"><i></i>平台数据</a></li>
            <li ><a href="http://www.hyjf.com/news/2.html"><i></i>网站公告</a></li>
            <li ><a href="http://www.hyjf.com/news/3.html"><i></i>媒体报道</a></li>
      </ul>
      <div class="userHelp"><a href="http://www.hyjf.com/help.html">帮助中心</a><i class="iconfont">&#xe617;</i></div>
</div>
    <div class="about-container fr">
      <h1 class="about-nav-name"><Span>合作伙伴</Span>
					<ul class="tabul">
						<li><a class="active" href="javascript:;">服务支持</a></li>
						<li><a href="javascript:;">法律支持</a></li>
						<li><a href="javascript:;">金融机构</a></li>
						<li><a href="javascript:;">其他机构</a></li>
					</ul>
				</h1>
				<!-- 第一个 -->
				<div class="about-info partners">
					<div class="ipartner">
						<dl>
							<dt>
								<a href="http://www.chinapnr.com/" target="_blank"> <img
									alt="汇付天下" src="/data/upfiles/image/20150531/1433077442426.jpg">
								</a>
							</dt>
							<dd>
								<h3>汇付天下</h3>
								<p>
									<span
										style="color: #676767; font-family: 'microsoft yahei'; font-size: 13px; line-height: 30px; background-color: #FFFFFF;">
										<p class="MsoNormal">
											其于<span>2006年7月成立，总部设于上海，并在北京、广州、深圳、成都等<span>30</span>多个城市设立分公司，出借额近<span>10</span>亿元人民币，核心团队由中国金融行业资深管理人士组成。<span>2011</span>年<span>5</span>月汇付天下首批获得央行颁发的《支付业务许可证》，首家获得证监会批准开展网上基金销售支付结算业务，是中国支付清算协会网络支付工作委员会副理事长单位。
											
										</p>
									</span>
								<h3
									style="color: #666666; font-family: 'Microsoft YaHei', Verdana, Helvetica, sans-serif; background-color: #FFFFFF;">
								</h3>
								</p>
							</dd>
						</dl>
					</div>
					<!-- 第二个 -->
					<div class="ipartner">
						<dl>
							<dt>
								<a href="http://www.zhongzhuncpa.com/" target="_blank"> <img
									alt="中准会计师事务所有限公司"
									src="/data/upfiles/image/20151103/1446517945867.jpg">
								</a>
							</dt>
							<dd>
								<h3>中准会计师事务所有限公司</h3>
								<p>
									<span
										style="color: #676767; font-family: 'microsoft yahei'; font-size: 13px; line-height: 30px; background-color: #FFFFFF;">
										<p class="MsoNormal">
											其是全国百强会计师事务所之一。具有财政部、证监会批准的证券、期货相关业务审计资格；具有人民银行、财政部批准的金融相关审计业务<span>A</span>级资格；具有建设部批准的工
											程造价咨询甲级资质；具有司法鉴定资格。
										</p>
									</span>
								<h3
									style="color: #666666; font-family: 'Microsoft YaHei', Verdana, Helvetica, sans-serif; background-color: #FFFFFF;">
								</h3>
								</p>
							</dd>
						</dl>
					</div>
					<!-- 	第三个 -->
					<div class="ipartner">
						<dl>
							<dt>
								<a href="http://www.caaqd.com/" target="_blank"> <img
									alt="青岛中艺拍卖有限公司"
									src="/data/upfiles/image/20150531/1433075822715.jpg">
								</a>
							</dt>
							<dd>
								<h3>青岛中艺拍卖有限公司</h3>
								<p>
									<span
										style="color: #676767; font-family: 'microsoft yahei'; font-size: 13px; line-height: 30px; background-color: #FFFFFF;">
										<p class="MsoNormal">
											其是经国家工商注册，具有相关资质，专业从事资产与艺术品拍卖的股份制企业。公司成立伊始，于<span>7</span>月<span>17</span>日举行的“中艺<span>2011</span>首届书画精品拍卖会”，获得圆满成功，拍品成交率和总成交额均创佳绩，改写了地区艺术品拍卖的记录，社会各界和各地区同行反应良好。
										</p>
									</span>
								<h3
									style="color: #666666; font-family: 'Microsoft YaHei', Verdana, Helvetica, sans-serif; background-color: #FFFFFF;">
								</h3>
								</p>
							</dd>
						</dl>
					</div>
					<!-- 	第四个 -->
					<div class="ipartner">
						<dl>
							<dt>
								<a href="http://www.caaqd.com/" target="_blank"> <img
									alt="青岛中艺拍卖有限公司"
									src="/data/upfiles/image/20150531/1433075822715.jpg">
								</a>
							</dt>
							<dd>
								<h3>青岛中艺拍卖有限公司</h3>
								<p>
									<span
										style="color: #676767; font-family: 'microsoft yahei'; font-size: 13px; line-height: 30px; background-color: #FFFFFF;">
										<p class="MsoNormal">
											其是经国家工商注册，具有相关资质，专业从事资产与艺术品拍卖的股份制企业。公司成立伊始，于<span>7</span>月<span>17</span>日举行的“中艺<span>2011</span>首届书画精品拍卖会”，获得圆满成功，拍品成交率和总成交额均创佳绩，改写了地区艺术品拍卖的记录，社会各界和各地区同行反应良好。
										</p>
									</span>
								<h3
									style="color: #666666; font-family: 'Microsoft YaHei', Verdana, Helvetica, sans-serif; background-color: #FFFFFF;">
								</h3>
								</p>
							</dd>
						</dl>
					</div></section>
<!--warp end--> 
<!--footer start-->
<footer>
  <div class="inner">
    <div class="help_service">
      <div class="ft-helper">
        <dl>
            <dt><a href="http://www.hyjf.com/article/guide.html">新手指引</a></dt>
          <dd> <a href="http://www.hyjf.com/invest.html">我要出借</a> <a href="http://www.hyjf.com/article/security.html">安全保障</a> <a href="http://www.hyjf.com/article/contract.html">CFCA</a> <a href="http://www.hyjf.com/article/deposit.html">汇付天下</a><a href="http://www.hyjf.com/about/report.html#summary">风险保证金</a> </dd>
        </dl>
        <dl>
          <dt><a href="http://www.hyjf.com/help.html">帮助中心</a></dt>
          <dd> <a href="http://www.hyjf.com/help/40.html">名词解释</a> <a href="http://www.hyjf.com/help/37.html">账户管理</a> <a href="http://www.hyjf.com/help/36.html">出借问题</a> <a href="http://www.hyjf.com/help/38.html">融资问题</a> <a href="http://www.hyjf.com/help/re_charge.html">充值提现</a> </dd>
        </dl>
        <dl>
          <dt><a href="http://www.hyjf.com/about/index.html">关于我们</a></dt>
          <dd> <a href="http://www.hyjf.com/about/team.html">团队介绍</a> <a href="http://www.hyjf.com/about/contact.html">联系我们</a> <a href="http://www.hyjf.com/about/jobs.html">招贤纳士</a> <a href="http://www.hyjf.com/about/report.html">平台数据</a> <a href="http://www.hyjf.com/help/83.html">产品介绍</a> </dd>
        </dl>
      </div>
      <div class="ft-service">
        <dl>
          <dt><span class="iconfont">&#xe606;</span><a target="_blank" href="http://b.qq.com/webc.htm?new=0&sid=4000655000&eid=218808P8z8p8p8z8K8p80&o=www.huiyingdai.com&q=7">在线客服</a></dt>
          <dd>
            <p> <strong>4000-655-000</strong><br>
              服务时间：8:30-17:30&nbsp;&nbsp;&nbsp;7*8小时 </p>
            <div class="ft-serv-handle"> <a class="iconfont weixin" title="微信" href="javascript:;">&#xe605;</a> <a class="iconfont" title="新浪微博" target="_blank" href="http://weibo.com/huiyingdai" rel="nofollow">&#xe60d;</a> <a class="iconfont" title="腾讯微博" target="_blank" href="http://t.qq.com/huiyingdai" rel="nofollow">&#xe60b;</a> <a class="iconfont" title="汇盈金服论坛" target="_blank" href="http://bbs.huiyingdai.com">&#xe608;</a> <a class="iconfont" title="email" target="_blank" href="mailto:jubao@huiyingdai.com?subject=投诉问题：&body=投诉描述：">&#xe609;</a> </div>
          </dd>
        </dl>
      </div>
     <!--  <div class="ft-helper">
       <dl>
           <dt><a href="http://www.hyjf.com/help/newbie.html">客户服务</a></dt>
         <dd> <a target="_blank" href="http://weibo.com/huiyingdai">新浪微博</a> <a class="weixin" href="javascript:;">官方微信</a> <a target="_blank" href="http://b.qq.com/webc.htm?new=0&sid=4000655000&eid=218808P8z8p8p8z8K8p80&o=www.huiyingdai.com&q=7">在线客服</a> <a title="汇盈金服论坛" target="_blank" href="http://bbs.huiyingdai.com">在线问答</a><a href="http://www.hyjf.com/about/contact.html">联系我们</a> </dd>
       </dl>
       <dl>
         <dt><a href="http://www.hyjf.com/help.html">安全保障</a></dt>
         <dd> <a href="http://www.hyjf.com/help/63.html">本息保障</a> <a href="http://www.hyjf.com/help/66.html">风控审核</a> <a href="http://www.hyjf.com/help/62.html">贷后管理</a> <a href="http://www.hyjf.com/about/partners.html">合作伙伴</a> <a href="http://www.hyjf.com/article/guide.html">业务模式</a> </dd>
       </dl>
       <dl>
         <dt><a href="http://www.hyjf.com/help/lend.html">新手投标</a></dt>
         <dd> <a href="http://www.hyjf.com/help/83.html">投标介绍</a> <a href="http://www.hyjf.com/invest.html">马上出借</a> <a href="http://www.hyjf.com/about/events.html">平台日志</a> <a href="http://www.hyjf.com/help.html">帮助中心</a> <a href="http://www.hyjf.com/activity.html">最新活动</a> </dd>
       </dl>
     </div> -->
    </div>
      <div class="ft-wap">
      <ul class="mobile-client">
        <li><a href="http://www.hyjf.com/article/mobile.html"><i class="iconfont">&#xe60e;</i>iPhone版</a></li>
        <li><a href="http://www.hyjf.com/article/mobile.html"><i class="iconfont">&#xe60a;</i>Android版</a></li>
        <li><a target="_blank" href="http://weixin.huiyingdai.com"><i class="iconfont">&#xe600;</i>访问手机版</a></li>
      </ul>
      <dl>
        <dt>
          <p>扫描下载客户端</p>
        </dt>
        <dd> <img width="120px" class="lazyload" data-original="http://img.huiyingdai.com/themes/default/images/mobile/1121549398.png" style="margin-top:8px"> </dd>
      </dl>
    </div>
    <div class="ft-record">
      <div class="ft-approve"> <a href="https://ss.knet.cn/verifyseal.dll?sn=e13121111010044010zk2c000000&amp;ct=df&amp;a=1&amp;pa=0.8610921316269518" rel="nofollow" target="_blank" class="icon-approve approve01"></a> <a href="https://search.szfw.org/cert/l/CX20141015005268005352" rel="nofollow" target="_blank" class="icon-approve approve02"></a> <a href="http://webscan.360.cn/index/checkwebsite?url=huiyingdai.com" rel="nofollow" target="_blank" class="icon-approve approve03"></a> <a href="http://www.cfca.com.cn/" rel="nofollow" target="_blank" class="icon-approve approve04"></a> </div>
      <div class="ft-identity"><strong>© </strong>汇盈金服&nbsp;All&nbsp;rights&nbsp;reserved<span>|</span>惠众商务顾问（北京）有限公司<span>|</span>京ICP备13050958号<span>|</span><a href="http://img.huiyingdai.com/themes/default/images/beian.jpg" target="_blank">公安安全备案证：37021313127</a></div>
    </div>
  </div>
</footer>
<!--footer end--> 
<!-- jScript start -->
<script src="http://www.hyjf.com/themes/default/js/main.js"></script>
<!-- jScript end-->
</body>
</html>

