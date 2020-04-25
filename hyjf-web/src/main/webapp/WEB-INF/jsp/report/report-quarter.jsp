<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" >
    <title>季度报告 - 汇盈金服官网</title>
    <link rel="stylesheet" href="${ctx}/dist/css/reports/reports.css" />
    <jsp:include page="/head.jsp"></jsp:include>
	<c:set var="webUrl" value="${webUrl}" />
	<script type="text/javascript">
        var webPath = "${webUrl}";
	</script>
</head>
<body>
  <div id='body-section'>
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
        <section class='section section-even' id='quarter-performance' data-nav='11' data-countup='quarterPerformanceCountup'>
          <div class='title box'>
            <h2 class="snum">02</h2>
            <span class='main-color'>当季业绩</span>
            <div>
              <p class='en-title'>PERFORMANCE OF THE SEASON</p>
              <p>OPERATION REPORT OF HYJF</p>
            </div>
          </div>
          <div class='section-content'>
            <section class='box'>
              <div class='flex1 margin10'>
                <div id='quarter-trade' style='min-width:350px;height:240px'></div>
              </div>
              <div class='flex1'>
                <div id='quarter-compare' style='min-width:350px;height:240px'></div>
              </div>
  						<div class='flex1'>
                <div id='quarter-earn' style='min-width:350px;height:240px'></div>
              </div>
            </section>
  					<section class='quarter-box'>
  						<h3 class="quarter-title content-center">
  							<span class='year-number'></span>年第<span class='quarter-number'></span>季度
  						</h3>
              <div>
                <p class="content-center">
                平台累计成交<span class='main-color amount-large margin10' id='quarter-trade-num'></span>笔，
                平均年利率<span class='main-color amount-large margin10'id='average-earn-percent'></span>
                </p>
                <p>
                本季度成交金额共计<span class='main-color amount-large margin10' id='quarter-trade-billion'></span>亿元，
                同比<span class='percent-describe' id="quarter-trade-billion-describe"></span>
                  <span class='main-color amount-large margin10' id='quarter-trade-billion-percent'></span>
                </p>
                <p>
                本季度为用户赚取收益<span class='main-color amount-large margin10' id='quarter-earn-amout'></span>万元，
                  同比<span class='percent-describe' id="quarter-earn-amout-describe"></span>
                    <span class='main-color amount-large margin10' id='quarter-earn-amout-percent'></span>
                </p>
              </div>
            </section>
          </div>
    		</section>
  			<section class='section section-odd' id='channel-analysis' data-nav='2' data-countup='channelAnalysisCountup'>
          <div class='title box'>
            <h2 class="snum">03</h2>
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
  									<p>本季度成交<span class='main-color amount-large margin10' id='app-deal'>256，236</span>笔，
  										占比<span class='main-color amount-large margin10' id='app-deal-percent'>43.62%</span><p>
  								</div>
  							</div>
  							<div class='box channelbox'>
  								<img class='margin10' src='${ctx}/hyjfReport/images/year/wechat.png'/>
  								<div class='flex1'>
  									<img src="https://www.hyjf.com/img/report/txt_weixin.png"/>
  									<p>本季度成交<span class='main-color amount-large margin10' id='wechat-deal'>256，236</span>笔，
  										占比<span class='main-color amount-large margin10' id='wechat-deal-percent'>43.62%</span><p>
  								</div>
  							</div>
  							<div class='box channelbox'>
  								<img class='margin10' src='${ctx}/hyjfReport/images/year/pc.png'/>
  								<div class='flex1'>
  									<img src="https://www.hyjf.com/img/report/txt_pc.png"/>
  									<p>本季度成交<span class='main-color amount-large margin10' id='pc-deal'>256，236</span>笔，
  										占比<span class='main-color amount-large margin10' id='pc-deal-percent'>43.62%</span><p>
  								</div>
  							</div>
              </div>
  	            <div class='flex1 margin10 double-column'>
  	              <div id='compare-three' style='min-width:500px;height:420px'></div>
  	            </div>
            </section>
          </div>
    		</section>
  			<section class='section section-even' id='costomer-analysis' data-nav='3' data-countup='costomerAnalysisCountup'>
  				<div class='title box'>
  					<h2 class="snum">04</h2>
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
  			<section class='section section-odd' id='quarter-most' data-nav='12' data-countup='quarterMostCountup'>
  				<div class='title box'>
  					<h2 class="snum">05</h2>
  					<span class='main-color'>当季之最</span>
  					<div>
              <p class='en-title'>TOP OF THE SEASON</p>
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
  							<p class='margin10 echart-title'>本季度出借金额最高（元）</p>
  	            <h3 class='amount amount-bg main-color' id='quarter-trade-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='month-trade-most-name'></span>
  								<!-- 满** -->
  								<span class='flex1 content-center echart-title' id='month-trade-most-age'></span>
  								<!-- 46岁 -->
  								<span class='flex1 content-right' id='month-trade-most-area'></span>
  								<!-- 青岛市 -->
  							</p>
  						</div>
  						<div class='flex1 content-center'>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/dyj.png"/>
  							<br/>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/dyjp.png"/>
  							<br/>
  							<p class='margin10 echart-title'>本季度到手收益最高（元）</p>
  	            <h3 class='amount amount-bg main-color' id='quarter-earn-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='month-earn-most-name'></span>
  								<!-- 满** -->
  								<span class='flex1 content-center echart-title' id='month-earn-most-age'></span>
  								<!-- 46岁 -->
  								<span class='flex1 content-right' id='month-earn-most-area'></span>
  								<!-- 青岛市 -->
  							</p>
  						</div>
  						<div class='flex1 content-center'>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/chy.png"/>
  							<br/>
  							<img class='margin10' src="${ctx}/hyjfReport/images/year/chyp.png"/>
  							<br/>
  							<p class='margin10 echart-title'>本季度出借次数最多（次）</p>
  	            <h3 class='amount amount-bg main-color' id='quarter-number-most'></h3>
  							<p class='box topten-customer-info'>
  								<span class='flex1' id='month-number-most-name'></span>
  								<!-- 满** -->
  								<span class='flex1 content-center echart-title' id='month-number-most-age'></span>
  								<!-- 46岁 -->
  								<span class='flex1 content-right' id='month-number-most-area'></span>
  								<!-- 青岛市 -->
  							</p>
  						</div>
  					</section>
  					<section class='box'>
  						<div class='flex1 content-center'>
  							<div class='topten-table'>
  								<p class='amount-bg echart-title content-center topten-title'>本季度十大出借人</p>
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
  			<section class='section section-even' id='experience-optimization' data-nav='5'>
  				<div class='title box'>
  					<h2 class="snum">06</h2>
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
  					<h2 class="snum">07</h2>
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
  					<h2 class="snum">08</h2>
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
	<script src="${ctx}/dist/js/report/data-quarter.js"></script>
</body>
</html>
