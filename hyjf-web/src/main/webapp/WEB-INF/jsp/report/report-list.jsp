<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>运营报告 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	
    <div class="report-banner" style="background-image:url(${ctx}/img/report-banner.jpg);">
        <div class="hd-innernav">
            <jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
        </div>
    </div>
    <div class="report-section">
        <div class="container-1200">
            <div class="report-txt">
                <p>汇盈金服一直秉承诚信规范、公开透明、平等协作的精神，致力于打造中国领先的线上金融资产交易平台。</p>
                <p class="highlight">我们将定期公布月度、季度、年度运营报告，以最真实有效的数据分析回馈每一位关心汇盈金服的用户。</p>
            </div>
            <ul id="reportList">
            	<%-- <li>
                    <a href="${ctx}/reportpage/report.do?report=report-201612" target="_blank">
                        <img src="${ctx}/img/report/201612/report201612.jpg" alt="">
                        <span class="title">2016年年度运营报告</span>
                    </a>
                </li>
            	<li>
                    <a href="${ctx}/reportpage/report.do?report=report-201611" target="_blank">
                        <img src="${ctx}/img/report/201611/report201611.jpg" alt="">
                        <span class="title">2016年11月份报告</span>
                    </a>
                </li>
            	<li>
                    <a href="${ctx}/reportpage/report.do?report=report-201610" target="_blank">
                        <img src="${ctx}/img/report/201610/report201610.jpg" alt="">
                        <span class="title">2016年10月份报告</span>
                    </a>
                </li>
            	<li>
                    <a href="${ctx}/reportpage/report.do?report=report-201609" target="_blank">
                        <img src="${ctx}/img/report/201609/report201609.jpg" alt="">
                        <span class="title">2016年第三季度报告</span>
                    </a>
                </li>
            	<li>
                    <a href="${ctx}/reportpage/report.do?report=report-201608" target="_blank">
                        <img src="${ctx}/img/report/201608/report201608.jpg?version=2" alt="">
                        <span class="title">2016年8月份报告</span>
                    </a>
                </li>
             	<li>
                    <a href="${ctx}/reportpage/report.do?report=report-201607" target="_blank">
                        <img src="${ctx}/img/report/report201607.jpg?version=2" alt="">
                        <span class="title">2016年7月份报告</span>
                    </a>
                </li>
                <li>
                    <a href="${ctx}/reportpage/report.do?report=report-201606" target="_blank">
                        <img src="${ctx}/img/report/report201606.jpg?version=2" alt="">
                        <span class="title">2016年上半年运营报告</span>
                    </a>
                </li>
                <li>
                    <a href="${ctx}/reportpage/report.do?report=report-201605" target="_blank">
                        <img src="${ctx}/img/report/report201605.jpg?version=2" alt="">
                        <span class="title">2016年5月份报告</span>
                    </a>
                </li>
                
                 --%>
            </ul>
        </div>
    </div>
	
    <div class="clearfix"></div>
    <script>setActById("hdXXPL");</script>
    <script>setActById("subYYBG");</script>
	<script>setActById("aboutReport");</script>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript">
		window.onload = function(){
			var reports = [{
				title : "2016年5月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201605",
				img : "${ctx}/img/report/report201605.jpg?version=2"
			},
			{
				title : "2016年上半年运营报告",
				url : "${ctx}/reportpage/report.do?report=report-201606",
				img : "${ctx}/img/report/report201606.jpg?version=2"
			},
			{
				title : "2016年7月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201607",
				img : "${ctx}/img/report/report201607.jpg?version=2"
			},
			{
				title : "2016年8月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201608",
				img : "${ctx}/img/report/201608/report201608.jpg?version=2"
			},
			{
				title : "2016年第三季度报告",
				url : "${ctx}/reportpage/report.do?report=report-201609",
				img : "${ctx}/img/report/201609/report201609.jpg?version=2"
			},
			{
				title : "2016年10月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201610",
				img : "${ctx}/img/report/201610/report201610.jpg?version=2"
			},
			{
				title : "2016年11月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201611",
				img : "${ctx}/img/report/201611/report201611.jpg?version=2"
			},
			{
				title : "2016年年度运营报告",
				url : "${ctx}/reportpage/report.do?report=report-201612",
				img : "${ctx}/img/report/201612/report201612.jpg?version=2"
			},
			{
				title : "2017年1月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201701",
				img : "${ctx}/img/report/201701/report201701.jpg?version=2"
			},
			{
				title : "2017年2月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201702",
				img : "${ctx}/img/report/201702/report201702.jpg?version=2"
			},
			{
				title : "2017年第一季度报告",
				url : "${ctx}/reportpage/report.do?report=report-201703",
				img : "${ctx}/img/report/201703/report201703.jpg?version=2"
			},
			{
				title : "2017年4月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201704",
				img : "${ctx}/img/report/201704/report201704.jpg?version=2"
			},
			{
				title : "2017年5月份报告",
				url : "${ctx}/reportpage/report.do?report=report-201705",
				img : "${ctx}/img/report/201705/report201705.jpg?version=2"
			}];
			var reStr ="";
			reports.reverse();
			for (var i=0 ;i<reports.length;i++){
				if(i%2 == 0){
					reStr+='<li class="odd">';
				}else{
					reStr+='<li>';
				}
				reStr+=' <a href="'+reports[i].url+'" target="_blank">'
				reStr+=' <img src="'+reports[i].img+'" alt="">'
				reStr+=' <span class="title">'+reports[i].title+'</span></a> </li>'
			}
			document.getElementById("reportList").innerHTML = reStr;
		}
	</script>
	
	</body>
</html>