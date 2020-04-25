<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" >
    <title>年报告 - 汇盈金服官网</title>
    <link rel="stylesheet" href="${ctx}/dist/css/reports/reports.css" />
    <jsp:include page="/head.jsp"></jsp:include>
	<c:set var="webUrl" value="${webUrl}" />
	<script type="text/javascript">
        var webPath = "${webUrl}";
	</script>
</head>
<body>
  <div id='body-section'>
  			<header id="top" data-nav='0' class="authors header-top">
  				<div class='content-center header-circle'>
  					<div class='header-cname'>
              <p class='year-number-header' id='year-number-header'></p>
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
        <section class='section section-even' id='yeah-performance' data-nav='9' data-countup='yeahPerformanceCountup'>
          <div class='title box'>
            <h2 class="snum">02</h2>
            <span class='main-color'>全年业绩</span>
            <div>
              <p class='en-title'>FULL-YEAR RESULTS</p>
              <p>OPERATION REPORT OF HYJF</p>
            </div>
          </div>
          <div class='section-content'>
            <section class='box'>
              <div class='flex1 margin10'>
                <div id='year-trade' style='min-width:850px;height:240px'></div>
              </div>
            </section>
  					<section class='quarter-box'>
  						<h3 class="quarter-title content-center"><span class='year-number'>2018</span>年全年</h3>
              <div>
                <p class="content-center">
                本年度平台累计成交<span class='main-color amount-large margin10' id='year-trade-num'></span>笔，
                本年度累计成交金额<span class='main-color amount-large margin10' id='year-trade-billion'></span>亿元
  							</p>
  							<p>
                  本年度累计赚取收益<span class='main-color amount-large margin10' id='year-earn-amout'></span>万元，
		              平均年利率<span class='main-color amount-large margin10' id='average-year-percent'></span>
  							</p>
  							<p>
                成交量最高单月为<span class='main-color amount-large margin10' id='year-most-month'></span>月，
  							共成交<span class='main-color amount-large margin10' id='year-most-month-trade'></span>亿元
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
  							<div id='loan-life' style='min-width:600px;height:420px'></div>
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
  									<p>全年成交<span class='main-color amount-large margin10' id='app-deal'></span>笔，
  										占比<span class='main-color amount-large margin10' id='app-deal-percent'></span><p>
  								</div>
  							</div>
  							<div class='box channelbox'>
  								<img class='margin10' src='${ctx}/hyjfReport/images/year/wechat.png'/>
  								<div class='flex1'>
  									<img src="https://www.hyjf.com/img/report/txt_weixin.png"/>
  									<p>全年成交<span class='main-color amount-large margin10' id='wechat-deal'></span>笔，
  										占比<span class='main-color amount-large margin10' id='wechat-deal-percent'></span><p>
  								</div>
  							</div>
  							<div class='box channelbox'>
  								<img class='margin10' src='${ctx}/hyjfReport/images/year/pc.png'/>
  								<div class='flex1'>
  									<img src="https://www.hyjf.com/img/report/txt_pc.png"/>
  									<p>全年成交<span class='main-color amount-large margin10' id='pc-deal'></span>笔，
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
        <section class='section section-even' id='channel-analysis-02' data-countup='channelAnalysis02Countup'>
        <div class='title box'>
          <h2 class="snum-keep" data-for="#channel-analysis">04</h2>
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
								<img class='margin10' src='/hyjfReport/images/year/app.png'/>
								<div class='flex1'>
									<img src="https://www.hyjf.com/img/report/txt_app.png"/>
									<p>全年成交金额<span class='main-color amount-large margin10' id='app-deal-amount'></span>亿元，
										金额占比<span class='main-color amount-large margin10' id='app-deal-amount-percent'></span><p>
								</div>
							</div>
							<div class='box channelbox'>
								<img class='margin10' src='/hyjfReport/images/year/wechat.png'/>
								<div class='flex1'>
									<img src="https://www.hyjf.com/img/report/txt_weixin.png"/>
									<p>全年成交金额<span class='main-color amount-large margin10' id='wechat-deal-amount'></span>亿元，
										金额占比<span class='main-color amount-large margin10' id='wechat-deal-amount-percent'></span><p>
								</div>
							</div>
							<div class='box channelbox'>
								<img class='margin10' src='/hyjfReport/images/year/pc.png'/>
								<div class='flex1'>
									<img src="https://www.hyjf.com/img/report/txt_pc.png"/>
									<p>全年成交金额<span class='main-color amount-large margin10' id='pc-deal-amount'></span>亿元，
										金额占比<span class='main-color amount-large margin10' id='pc-deal-amount-percent'></span><p>
								</div>
							</div>
            </div>
            <div class='flex1 margin10 double-column'>
              <div id='compare-three-02' style='min-width:500px;height:420px'></div>
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
  						<p class='flex1 box content-right padright10p'><img class='lrmargin10' src="${ctx}/hyjfReport/images/year/girl.png"/>女性<span id='girl-percent'></span></p>
  						<p class='flex1 box'><img class='lrmargin10' src="${ctx}/hyjfReport/images/year/boy.png"/>男性<span id='boy-percent'></span></p>
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
  			<section class='section section-even' id='yeah-most' data-nav='10' data-countup='yeahMostCountup'>
  				<div class='title box'>
  					<h2 class="snum">06</h2>
  					<span class='main-color'>年度之最</span>
  					<div>
  						<p class='en-title'>TOP OF THE YEAR</p>
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
  							<p class='margin10 echart-title'>本年度出借金额最高（元）</p>
  	            <h3 class='amount amount-bg main-color' id='year-trade-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='year-trade-most-name'></span>
  								<span class='flex1 content-center echart-title' id='year-trade-most-age'></span>
  								<span class='flex1 content-right' id='year-trade-most-area'></span>
  							</p>
  						</div>
  						<div class='flex1 content-center'>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/dyj.png"/>
  							<br/>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/dyjp.png"/>
  							<br/>
  							<p class='margin10 echart-title'>本年度到手收益最高（元）</p>
  	            <h3 class='amount amount-bg main-color' id='year-earn-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='year-earn-most-name'></span>
  								<span class='flex1 content-center echart-title' id='year-earn-most-age'></span>
  								<span class='flex1 content-right' id='year-earn-most-area'></span>
  							</p>
  						</div>
  						<div class='flex1 content-center'>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/chy.png"/>
  							<br/>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/chyp.png"/>
  							<br/>
  							<p class='margin10 echart-title'>本年度出借次数最多（次）</p>
  	            <h3 class='amount amount-bg main-color' id='year-number-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='year-number-most-name'></span>
  								<span class='flex1 content-center echart-title' id='year-number-most-age'></span>
  								<span class='flex1 content-right' id='year-number-most-area'></span>
  							</p>
  						</div>
  					</section>
  					<section class='box'>
  						<div class='flex1 content-center'>
  							<div class='topten-table'>
  								<p class='amount-bg echart-title content-center topten-title'>本年度十大出借人</p>
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
	<script src="${ctx}/dist/js/report/data-year.js"></script>
</body>
</html>
