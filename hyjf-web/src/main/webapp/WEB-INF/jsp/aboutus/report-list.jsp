<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
	<jsp:include page="/head.jsp"></jsp:include>
	<style type='text/css'>
	.main-content .content .main-title{
		line-height: 45px;
		height: 45px;
		margin-top:-25px;
	}
	.report-list .statistics > span {
	  display: inline-block;
	  margin-right: 25px;
	}

	.report-list .statistics > span .statistics-num {
	  font-size: 1.5em;
	}

	.report-list ul {
	  padding: 0;
	  margin: 20px auto;
	}

	.report-list .content-list li {
	  display: inline-block;
	  box-sizing: border-box;
	  text-align: center;
	  padding: 40px 40px 20px;
	  width: 31%;
	  margin: 1%;
	  /*设置只显示6个*/
	}

	.report-list .content-list li h2 {
	  font-weight: normal;
		font-size: 1.85em;
		margin:0;
		padding-top: 1.5em;
	}

	.report-list .content-list li a {
	  color: #fff;
	  text-decoration: none;
	}

	.report-list .content-list li:nth-of-type(2n+1) {
	  background: #2054b4 url("${cdn}/hyjfReport/images/list-circle.png") no-repeat top center;
	}

	.report-list .content-list li:nth-of-type(2n) {
	  background: #2054b4 url("${cdn}/hyjfReport/images/list-bg-circle.png") no-repeat top center;
	}

	.report-list .content-list li:nth-of-type(n+7) {
	  display: none;
	}
	.report-list .content-list li .date{
		margin:0.5em auto;
	}

	.report-list .content-list li:hover .more {
	  color: #fff;
	  background-color: #fb7243;
	}

	.report-list .content-list li .more {
	  display: inline-block;
	  background-color: #fff;
	  color: #2054b4;
	  padding: 4px 30px;
	  border-radius: 30px;
	  margin-top: 1em;
	}
	.hide{
		display: none;
	}
	</style>
</head>
<body>
<jsp:include page="/header.jsp"></jsp:include>
<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
<article class="main-content report-list">
	<div class="container">
		<section class="about-detial content">
			<div class="no-border main-title">
				运营报告
			</div>
			<div class="operation-content">
				<p class="statistics">
          <span>累计出借：<span class="statistics-num" id='statistics-trade'>${sumTender }</span>元</span>
          <span>累计收益：<span class="statistics-num" id='statistics-earn'>${sumProfit }</span>元</span>
        </p>
				<p>
					汇盈金服一直秉承诚信规范、公开透明、平等协作的精神，致力于打造中国领先的线上金融资产交易平台。我们将定期公布月度、季度、年度运营报告，以最真实有效的数据分析回馈每一位关心汇盈金服的用户。
				</p>
				<div class="content-list">
					<ul id='report-list' class='hide'>
						<li>
							<a href="${ctx}/aboutus/reportDetail.do?report=report-201803" class="operation-bg-1" itemid="lt1"  target="_blank">
								<h2 class="title">一季度运营报告</h2>
								<p class="date">
									2018年3月31日
								</p>
								<p>
                  <span class="more">查看详情</span>
                </p>
							</a>
						</li>
					   <li>
							<a href="${ctx}/aboutus/reportDetail.do?report=report-201802" class="operation-bg-1" itemid="lt1"  target="_blank">
								<h2 class="title">2月份运营报告</h2>
								<p class="date">
									2018年2月28日
								</p>
                <p>
                  <span class="more">查看详情</span>
                </p>
							</a>
						</li>
					    <li>
							<a href="${ctx}/aboutus/reportDetail.do?report=report-201801" class="operation-bg-2" itemid="lt1"  target="_blank">
								<h2 class="title">1月份运营报告</h2>
								<p class="date">
									2018年1月31日
								</p>
                <p>
                  <span class="more">查看详情</span>
                </p>
							</a>
						</li>
					    <li>
							<a href="${ctx}/aboutus/reportDetail.do?report=report-201712" class="operation-bg-3" itemid="lt1"  target="_blank">
								<h2 class="title">年度运营报告</h2>
								<p class="date">
									2017年12月31日
								</p>
                <p>
                  <span class="more">查看详情</span>
                </p>
							</a>
						</li>
              <li>
              <a href="${ctx}/aboutus/reportDetail.do?report=report-201711" class="operation-bg-1" itemid="lt1"  target="_blank">
                <h2 class="title">11月份运营报告</h2>
                <p class="date">
                  2017年11月30日
                </p>
                <p>
                  <span class="more">查看详情</span>
                </p>
              </a>
            </li>
              <li>
              <a href="${ctx}/aboutus/reportDetail.do?report=report-201710" class="operation-bg-1" itemid="lt1"  target="_blank">
                <h2 class="title">10月份运营报告</h2>
                <p class="date">
                  2017年10月31日
                </p>
                <p>
                  <span class="more">查看详情</span>
                </p>
              </a>
            </li>
					</ul>
				</div>
			</div>
		</section>
	</div>
</article>
<jsp:include page="/footer.jsp"></jsp:include>
<!-- 设置定位  -->
<script>setActById("aboutReport");</script>
<script>setActById("aboutPlatformInfo");</script>
<!-- 设置定位  -->
<script>setActById("indexMessage");</script>
</body>
</html>
<script type="text/javascript">
	function getLastDay(year,month)
	{
	 var new_year = year;  //取当前的年份
	 var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）
	 if(month>12)      //如果当前大于12月，则年份转到下一年
	 {
	 new_month -=12;    //月份减
	 new_year++;      //年份增
	 }
	 var new_date = new Date(new_year,new_month,1);        //取当年当月中的第一天
	 return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期
	}
	window.onload = function(){
		var apiUrl = "${ctx}"+'/report/reportList.do?isRelease=1';
		var reportDetailUrl="${ctx}"+'/report/initMonthReport.do?id=';
		$.get(apiUrl,function(res){
			if(typeof(res.success)!='undefined' && res.success=='success'){
				if(typeof(res.recordList)=='undefined'){
					$('#report-list').removeClass('hide');
					return;
				}
				var releaseRes=[];
	      var n=0;
	      for(var i=0;i<res.recordList.length;i++){
	        if(res.recordList[i].isRelease==1 && res.recordList[i].isDelete==0){
	          releaseRes.push(res.recordList[i]);
	          n++;
	        }
	        if(n==6){
	          break;
	        }
	      }
	      var length=releaseRes.length;
	      length= length>6? 6 : length;
	      for(var i=(length-1);i>-1;i--){
	        var li=$("#report-list").find('li').first().clone();
	        $(li).find('a').attr('href',reportDetailUrl+releaseRes[i].id);
	        $(li).find('.title').html(releaseRes[i].typeRealName+"运营报告");
	        $(li).find('.date').html(releaseRes[i].year+'年'+releaseRes[i].sortMonth+'月'+(releaseRes[i].sortMonth== '6' ? '30' : releaseRes[i].sortDay)+'日');
	        $("#report-list").prepend(li);
	      }
				$('#report-list').removeClass('hide');
    }else{
				alert("最新数据获取失败，请刷新重试！");
				$('#report-list').removeClass('hide');
			}
		});
	}
</script>
</script>
