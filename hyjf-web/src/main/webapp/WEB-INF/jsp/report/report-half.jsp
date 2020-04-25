<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" >
    <title>半年度运营报告 - 汇盈金服官网</title>
    <link rel="stylesheet" href="${ctx}/dist/css/reports/reports.css" />
    <jsp:include page="/head.jsp"></jsp:include>
	<c:set var="webUrl" value="${webUrl}" />
	<script type="text/javascript">
        var webPath = "${webUrl}";
	</script>
</head>
<body>
  <div id="body-section">
  			<header id="top" class="authors header-top" data-nav='0'>
  				<div class='content-center header-circle'>
  					<div class='header-cname'>
  						<p class='cname-line'>
  							<img src='${ctx}/hyjfReport/images/header/logo.png' class='logo'/>
  							<img src='${ctx}/hyjfReport/images/header/logo-text.png' class='logo-text'/>
  						</p>
  						<span class='cname' id='cname'></span>
  						<p class='report-title'>运营报告</p>
  						<img src='${ctx}/hyjfReport/images/header/header-circle-line.png' class='header-circle-line'/>
  						<h1 class='ename' id='ename'></h1>
  						<p>WWW.HYJF.COM</p>
  					</div>
  				</div>
    		</header>
    		<section class='section section-odd' id='performance-preview' data-nav='1' data-countup='performancePreviewCountup'>
          <div class='title box'>
            <h2 class="snum">01</h2>
            <span class='main-color'>业绩总览</span>
            <div>
              <p class='en-title'>PERFORMANCE OVERVIEW</p>
              <p>OPERATION REPORT OF HYJF</p>
            </div>
          </div>
          <div class='box section-content tbmargin100'>
            <div class='flex1 content-center margin10'>
              <img src='${ctx}/hyjfReport/images/module1-icon-1.png'/>
              <p class='margin10'>累计交易额（元）</p>
              <h3 class='amount amount-bg main-color' id='trade-amout'></h3>
            </div>
            <div class='flex1 content-center margin10'>
              <img src='${ctx}/hyjfReport/images/module1-icon-2.png'/>
              <p class='margin10'>平台注册人数（人）</p>
              <h3 class='amount amount-bg main-color' id='platform-registers'></h3>
            </div>
            <div class='flex1 content-center margin10'>
              <img src='${ctx}/hyjfReport/images/module1-icon-3.png'/>
              <p class='margin10'>累计赚取收益（元）
  							<span class="icon iconfont icon-gantanhao earn-gantanhao">
  								<span class='gantanhao-tips'>数据为已实际发放收益，不包含待收收益。</span>
  							</span>
  						</p>
              <h3 class='amount amount-bg main-color' id='earn-amout'></h3>
            </div>
          </div>
    		</section>
        <section class='section section-even' id='half-performance' data-nav='13' data-countup='halfPerformanceCountup'>
          <div class='title box'>
            <h2 class="snum">02</h2>
            <span class='main-color'>上半年业绩</span>
            <div>
              <p class='en-title'>THE FIRST HALF YEAR RESULTS</p>
              <p>OPERATION REPORT OF HYJF</p>
            </div>
          </div>
          <div class='section-content'>
            <section class='box'>
              <div class='flex1 margin10'>
                <div id='half-trade' style='min-width:850px;height:240px'></div>
              </div>
            </section>
  					<section class='quarter-box'>
  						<h3 class="quarter-title content-center"><span class='half-number'>2018</span>年上半年</h3>
              <div class="content-center">
                <p class="content-center">
                上半年平台累计成交<span class='main-color amount-large margin10' id='half-trade-num'></span>笔，
                上半年累计成交金额<span class='main-color amount-large margin10' id='half-trade-billion'></span>亿元
  							</p>
                <p>
                上半年累计充值笔数<span class='main-color amount-large margin10' id='half-recharge-num'></span>笔，
                上半年累计充值金额<span class='main-color amount-large margin10' id='half-recharge-billion'></span>亿元
  							</p>
  							<p>
                 上半年累计赚取收益<span class='main-color amount-large margin10' id='half-earn-amout'></span>万元，
				         平均年利率<span class='main-color amount-large margin10' id='average-half-percent'></span>
  							</p>
  							<p>
                成交量最高单月为<span class='main-color amount-large margin10' id='half-most-month'></span>月，
  							共成交<span class='main-color amount-large margin10' id='half-most-month-trade'></span>亿元
  							</p>
              </div>
            </section>
          </div>
    		</section>
  			<section class='section section-odd' id='loan-term' data-nav='8'>
  				<div class='title box'>
  					<h2 class="snum">03</h2>
  					<span class='main-color'>借款期限</span>
  					<div>
              <p class='en-title'>LIFE OF LOAN</p>
						  <p>OPERATION REPORT OF HYJF</p>
  					</div>
  				</div>
  				<div class='section-content'>
  					<section class='box'>
  						<div class='flex1 margin10 double-column'>
  							<div id='loan-life' style='min-width:700px;height:420px'></div>
  						</div>
  						<div class='flex1 content-center double-column'>
  							<img src="${ctx}/hyjfReport/images/year/loan.png"/>
  						</div>
  					</section>
  				</div>
  			</section>
  			<section class='section section-even' id='channel-analysis' data-nav='2' data-countup='channelAnalysisCountup'>
          <div class='title box'>
            <h2 class="snum">04</h2>
            <span class='main-color'>渠道分析</span>
            <div>
              <p class='en-title'>CHANNEL ANALYSIS</p>
              <p>OPERATION REPORT OF HYJF</p>
            </div>
          </div>
          <div class='section-content'>
            <section class='box'>
              <div class='flex1 double-column'>
                <div class='box channelbox'>
  								<img class='margin10' src='${ctx}/hyjfReport/images/year/app.png'/>
  								<div class='flex1'>
  									<img src="https://www.hyjf.com/img/report/txt_app.png"/>
  									<p>上半年成交<span class='main-color amount-large margin10' id='app-deal'></span>笔，
  										占比<span class='main-color amount-large margin10' id='app-deal-percent'></span><p>
  								</div>
  							</div>
  							<div class='box channelbox'>
  								<img class='margin10' src='${ctx}/hyjfReport/images/year/wechat.png'/>
  								<div class='flex1'>
  									<img src="https://www.hyjf.com/img/report/txt_weixin.png"/>
  									<p>上半年成交<span class='main-color amount-large margin10' id='wechat-deal'></span>笔，
  										占比<span class='main-color amount-large margin10' id='wechat-deal-percent'></span><p>
  								</div>
  							</div>
  							<div class='box channelbox'>
  								<img class='margin10' src='${ctx}/hyjfReport/images/year/pc.png'/>
  								<div class='flex1'>
  									<img src="https://www.hyjf.com/img/report/txt_pc.png"/>
  									<p>上半年成交<span class='main-color amount-large margin10' id='pc-deal'></span>笔，
  										占比<span class='main-color amount-large margin10' id='pc-deal-percent'></span><p>
  								</div>
  							</div>
              </div>
              <div class='flex1 margin10 double-column'>
                <div id='compare-three' style='min-width:500px;height:420px'></div>
              </div>
            </section>
          </div>
    		</section>
  			<section class='section section-odd' id='costomer-analysis' data-nav='3' data-countup='costomerAnalysisCountup'>
  				<div class='title box'>
  					<h2 class="snum">05</h2>
  					<span class='main-color'>用户分析</span>
  					<div>
              <p class='en-title'>CUSTOMER ANALYSIS</p>
						  <p>OPERATION REPORT OF HYJF</p>
  					</div>
  				</div>
  				<div class='section-content'>
  					<section class='box'>
  						<p class='flex1 box content-right padright10p'><img class='margin10' src="${ctx}/hyjfReport/images/year/girl.png"/>女性<span id='girl-percent'></span></p>
  						<p class='flex1 box'><img class='margin10' src="${ctx}/hyjfReport/images/year/boy.png"/>男性<span id='boy-percent'></span></p>
  					</section>
  					<section class='box'>
  						<div class='flex1 double-column'>
  							<div id='customer-age' style='min-width:45%;height:340px'></div>
  						</div>
  						<div class='flex1 double-column'>
  							<div id='customer-trade' style='min-width:45%;height:340px'></div>
  						</div>
  					</section>
  				</div>
  			</section>
  			<section class='section section-even' id='half-most' data-nav='14' data-countup='halfMostCountup'>
  				<div class='title box'>
  					<h2 class="snum">06</h2>
  					<span class='main-color'>上半年之最</span>
  					<div>
              <p class='en-title'>TOP OF THE FIRST HALF YEAR</p>
						  <p>OPERATION REPORT OF HYJF</p>
  					</div>
  				</div>
  				<div class='section-content'>
  					<section class='box'>
  						<div class='flex1 content-center'>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/zdj.png"/>
  							<br/>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/zdjp.png"/>
  							<br/>
  							<p class='margin10 echart-title'>上半年出借金额最高（元）</p>
  	            <h3 class='amount amount-bg main-color' id='half-trade-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='half-trade-most-name'></span>
  								<span class='flex1 content-center echart-title' id='half-trade-most-age'></span>
  								<span class='flex1 content-right' id='half-trade-most-area'></span>
  							</p>
  						</div>
  						<div class='flex1 content-center'>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/dyj.png"/>
  							<br/>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/dyjp.png"/>
  							<br/>
  							<p class='margin10 echart-title'>上半年到手收益最高（元）</p>
  	            <h3 class='amount amount-bg main-color' id='half-earn-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='half-earn-most-name'></span>
  								<span class='flex1 content-center echart-title' id='half-earn-most-age'></span>
  								<span class='flex1 content-right' id='half-earn-most-area'></span>
  							</p>
  						</div>
  						<div class='flex1 content-center'>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/chy.png"/>
  							<br/>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/chyp.png"/>
  							<br/>
  							<p class='margin10 echart-title'>上半年出借次数最多（次）</p>
  	            <h3 class='amount amount-bg main-color' id='half-number-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='half-number-most-name'></span>
  								<span class='flex1 content-center echart-title' id='half-number-most-age'></span>
  								<span class='flex1 content-right' id='half-number-most-area'></span>
  							</p>
  						</div>
  					</section>
  					<section class='box'>
  						<div class='flex1 content-center'>
  							<div class='topten-table'>
  								<p class='amount-bg echart-title content-center topten-title'>上半年十大出借人</p>
  								<ul class='content-center topten-li' id='topten-list'>
  									<li><span class='topten-li-num'></span><span class='topten-li-name'></span><span class='topten-li-amount'></span></li>
  								</ul>
  							</div>
  						</div>
  						<div class='flex1'>
  							<div id='topten-trade' style='min-width:500px;height:540px;'></div>
  						</div>
  					</section>
  				</div>
  			</section>
  			<section class='section section-odd' id='costomer-service' data-nav='4' data-countup='costomerServiceCountup'>
  				<div class='title box'>
  					<h2 class="snum">07</h2>
  					<span class='main-color'>客户服务</span>
  					<div>
              <p class='en-title'>CUSTOMER SERVICE</p>
						  <p>OPERATION REPORT OF HYJF</p>
  					</div>
  				</div>
  				<div class='section-content'>
  					<section class='box customer-service-section'>
  						<div class='content-center'>
  							<p class='margin10 main-color'><i class='icon iconfont icon-kefu'></i>电话接待人数</p>
  							<p class='sub-statistics'><span id='phone-worknum'></span>人</p>
  						</div>
  						<div class='content-center'>
  							<p class='margin10 main-color'><i class='icon iconfont icon-qq'></i>QQ客服接待人数</p>
  							<p class='sub-statistics'><span id='qq-worknum'></span>人</p>
  						</div>
  						<div class='content-center'>
  							<p class='margin10 main-color'><i class='icon iconfont icon-custom-wechat'></i>微信客服接待人数</p>
  							<p class='sub-statistics'><span id='wechat-worknum'></span>人</p>
  						</div>
  						<div class='content-center'>
  							<p class='margin10 main-color'><i class='icon iconfont icon-file-1'></i>解决问题数</p>
  							<p class='sub-statistics'><span id='question-resolveenum'></span>人</p>
  						</div>
  					</section>
  				</div>
  			</section>
  			<section class='section section-even' id='experience-optimization' data-nav='5'>
  				<div class='title box'>
  					<h2 class="snum">08</h2>
  					<span class='main-color'>体验优化</span>
  					<div>
              <p class='en-title'>EXPERIENCE OPTIMIZATION</p>
			        <p>OPERATION REPORT OF HYJF</p>
  					</div>
  				</div>
  				<div class='section-content swiper-experience-optimization'>
  					<article class='swiper-wrapper'>
  						<section class='box quarter-section6 swiper-slide'>
  							<div class='line'>
  								<div class='content-center'>
  									<p class='margin10 sub-title-date'>6月1日-6月3日</p>
  									<p class='sub-title'>APP上线</p>
  								</div>
  								<div class='content-center'>
  									<p class='margin10 sub-title-date'>6月1日-6月3日</p>
  									<p class='sub-title'>APP上线</p>
  								</div>
  								<div class='content-center'>
  									<p class='margin10 sub-title-date'>6月1日-6月3日</p>
  									<p class='sub-title'>APP上线</p>
  								</div>
  								<div class='content-center'>
  									<p class='margin10 sub-title-date'>6月1日-6月3日</p>
  									<p class='sub-title'>APP上线</p>
  								</div>
  								<div class='content-center'>
  									<p class='margin10 sub-title-date'>6月1日-6月3日</p>
  									<p class='sub-title'>APP上线</p>
  								</div>
  								<div class='content-center'>
  									<p class='margin10 sub-title-date'>6月1日-6月3日</p>
  									<p class='sub-title'>APP上线</p>
  								</div>
  							</div>
  						</section>
  					</article>
  					<div class='pagination experience-optimization-pagination'></div>
  				</div>
  			</section>
  			<section class='section section-odd' id='exciting-events' data-nav='6'>
  				<div class='title box'>
  					<h2 class="snum">09</h2>
  					<span class='main-color'>精彩活动</span>
  					<div>
              <p class='en-title'>EXCITING ACTIVITIES</p>
			        <p>OPERATION REPORT OF HYJF</p>
  					</div>
  				</div>
  				<div class='section-content swiper-exciting-events'>
  					<article class='swiper-wrapper'>
  						<section class='box section6-box swiper-slide'>
  							<div class='line'>
  								<div class='content-center'>
  									<img class='margin10' src="${ctx}/hyjfReport/images/year/events.jpg"/>
  									<br/>
  									<p class='margin10'>本月度出借金额最高（元）</p>
  									<p class='sub-title'>6月1日-6月3日</p>
  								</div>
  								<div class='content-center'>
  									<img class='margin10' src="${ctx}/hyjfReport/images/year/events.jpg"/>
  									<br/>
  									<p class='margin10'>本月度出借金额最高（元）</p>
  									<p class='sub-title'>6月1日-6月3日</p>
  								</div>
  							</div>
  						</section>
  						<section class='box section6-box swiper-slide'>
  							<div class='line'>
  								<div class='content-center'>
  									<img class='margin10' src="https://www.hyjf.com/img/report/zuiduojin.png"/>
  									<br/>
  									<p class='margin10'>本月度出借金额最高（元）</p>
  									<p class='sub-title'>6月1日-6月3日</p>
  								</div>
  								<div class='content-center'>
  									<img class='margin10' src="https://www.hyjf.com/img/report/zuiduojin.png"/>
  									<br/>
  									<p class='margin10'>本月度出借金额最高（元）</p>
  									<p class='sub-title'>6月1日-6月3日</p>
  								</div>
  								<div class='content-center'>
  									<img class='margin10' src="https://www.hyjf.com/img/report/zuiduojin.png"/>
  									<br/>
  									<p class='margin10'>本月度出借金额最高（元）</p>
  									<p class='sub-title'>6月1日-6月3日</p>
  								</div>
  								<div class='content-center'>
  									<img class='margin10' src="https://www.hyjf.com/img/report/zuiduojin.png"/>
  									<br/>
  									<p class='margin10'>本月度出借金额最高（元）</p>
  									<p class='sub-title'>6月1日-6月3日</p>
  								</div>
  								<div class='content-center'>
  									<img class='margin10' src="https://www.hyjf.com/img/report/zuiduojin.png"/>
  									<br/>
  									<p class='margin10'>本月度出借金额最高（元）</p>
  									<p class='sub-title'>6月1日-6月3日</p>
  								</div>
  							</div>
  						</section>
  					</article>
  					<div class='pagination exciting-events-pagination'></div>
  				</div>
  			</section>
  			<section class='section section-even' id='footerprints' data-nav='7'>
  				<div class='title box'>
  					<h2 class="snum">10</h2>
  					<span class='main-color'>足迹</span>
  					<div>
  						<p class='en-title'>FOOTPRINT</p>
  						<p>OPERATION REPORT OF HYJF</p>
  					</div>
  				</div>
  				<div class='section-content footerprints'>
  					<p class='content-center footerprints-title'>
  						<img class='' src="${ctx}/hyjfReport/images/plane.png"/>
  					</p>
  					<section class='section7'>
  					</section>
  				</div>
  			</section>
  			<footer style="background-color:#000;" id="footer" class="authors content-center">
    			<img src='${ctx}/hyjfReport/images/footer.jpg'/>
    		</footer>
      </div>
    <div class="right-nav" id='right-nav'>
        <ul>
        </ul>
    </div>
    <script src="${ctx}/dist/js/lib/jquery.min.js"></script>
	<script src="${ctx}/dist/js/idangerous.swiper.min.js"></script>
    <script src="${ctx}/dist/js/lib/echarts.common.min.js"></script>
    <script src="${ctx}/dist/js/lib/countUp.min.js"></script>
    <script src="${ctx}/dist/js/report/report.js"></script>
	<script src="${ctx}/dist/js/report/data-half.js"></script>
</body>
</html>
