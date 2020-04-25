<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="UTF-8">   
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1"/>

    <title>2016年度报告 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.fullPage.css" />
	<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/report-tmp.css?version=${version}" /> --%>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/newweb-report.css" />
</head>

<body class="dec">
    <div id="fullpage">
        <div class="section top" style="background-image: url(${ctx}/img/report/201612/top.jpg?v=20171123);">
            <div class="scroll"></div>
        </div>
        <div class="section section1 yejizonglan repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>业绩总览</div>
                 <dl class="data1">
                    <dt>累计交易额（元）</dt>
                    <dd id="s1data1">10,332,979,404</dd>
                </dl>
                <dl class="data2">
                    <dt>平台注册人数（人）</dt>
                    <dd id="s1data2">203866</dd>
                </dl>
                <dl class="data3">
                    <dt>累计赚取收益（元）</dt>
                    <dd id="s1data3">348,388,571</dd>
                </dl>
                <dl class="data4">
                    <dt>风险保证金（元）</dt>
                    <dd id="s1data4">49,465,710</dd>
                </dl>
            </div>
        </div>
       <div class="section section2 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>全年业绩</div>
                <div class="echarts-box">
	                <div id="echarts2_1" style="width:1050px;margin-left: auto;margin-right: auto;float:none;"></div>
	            	
                </div>
                <div class="data-box">
                <p style="font-size: 18px;">2016年全年平台累计成交<span id="s2data1" class="data">164975</span>笔，累计成交金额<span class="data"><span id="s2data2">54.78</span>亿</span>元，累计赚取收益<span class="data"><span id="s2data3" class="data">16891.97</span>万</span>元</p>

                <!-- <p style="font-size: 18px;">累计充值<span id="s2data4" class="data">58138</span>笔，共<span class="data"><span id="s2data5">10.23</span>亿</span>元 
                                         累计提现<span id="s2data6" class="data">34103</span>笔，共<span class="data"><span id="s2data7">8.83</span>亿</span>元 </p>-->

                <p style="font-size: 18px;text-align: center;">成交量最高单月为<span class="data">9</span>月，共成交<span class="data"><span id="s2data8">5.88</span>亿</span>元</p>
                <p style="font-size: 18px;text-align: center;">全年预期平均收益率<span class="data"><span id="s2data9">11.42</span>%</span></p>

                </div>
            </div>
        </div>
        
        <div class="section section3 qudaofenxi repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>渠道分析</div>
                <div class="qudaofenxi_style2_left">
                    <div class="item-title"><img src="${ctx}/img/report/txt_app.png" alt=""></div>
                    <div class="item"><div class="icon phone"></div>年度成交<span id="s3data1">40955</span>笔，占比<span id="s3data2">24.82</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_weixin.png" alt=""></div>
                    <div class="item"><div class="icon wechat"></div>年度成交<span id="s3data3">7303</span>笔，占比<span id="s3data4">4.43</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_pc.png" alt=""></div>
                    <div class="item"><div class="icon desktop"></div>年度成交<span id="s3data5">116717</span>笔，占比<span id="s3data6">70.75</span>%</div>
                </div>
                <div id="echarts3_1" style="width: 500px;height: 520px;float: left;margin-top: 50px;"></div>
            </div>
        </div>
        <div class="section section3 qudaofenxi repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>渠道分析</div>
                <div class="qudaofenxi_style2_left">
                    <div class="item-title"><img src="${ctx}/img/report/txt_app.png" alt=""></div>
                    <div class="item"><div class="icon phone"></div>年度累计成交金额<span id="s4data1">12.68</span>亿元，占比<span id="s4data2">23.51</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_weixin.png" alt=""></div>
                    <div class="item"><div class="icon wechat"></div>年度累计成交金额<span id="s4data3">2.49</span>亿元，占比<span id="s4data4">4.62</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_pc.png" alt=""></div>
                    <div class="item"><div class="icon desktop"></div>年度累计成交金额<span id="s4data5">38.76</span>亿元，占比<span id="s4data6">70.87</span>%</div>
                </div>
                <div id="echarts4_1" style="width: 500px;height: 520px;float: left;margin-top: 50px;"></div>
            </div>
        </div>
        <div class="section section5 yonghufenxi repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>用户分析</div>
                <div class="echarts-box">
                    <div id="yonghufenxi_1" style="width:500px;height: 400px;"></div>
                    <div id="yonghufenxi_3" style="width:500px;height: 400px;margin-left:0px;"></div>
                </div>
            </div>
        </div> 
        <div class="section section6 dangyuezhizui repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>年度之最</div>
                <div class="dangyuezhizui-left-style2">
                    <div class="item-title">本年度十大投资人</div>
                    <div class="item-box">
                        <div class="item"><div class="name">李*</div><div class="value top3">5,271.43万</div></div>
                        <div class="item"><div class="name">l*</div><div class="value top3">4,200.30万</div></div>
                        <div class="item"><div class="name">l*</div><div class="value top3">3,976.22万</div></div>
                        <div class="item"><div class="name">z*</div><div class="value">3,401.16万</div></div>
                        <div class="item"><div class="name">王*</div><div class="value">2,588.63万</div></div>
                        <div class="item"><div class="name">李*</div><div class="value">2,365.91万</div></div>
                        <div class="item"><div class="name">y*</div><div class="value">2,361.93万</div></div>
                        <div class="item"><div class="name">达*</div><div class="value">2,291.64万</div></div>
                        <div class="item"><div class="name">澄*</div><div class="value">2,118.27万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">1,762.74万</div></div>
                    </div>
                </div>
                <div class="dangyuezhizui-right-style2">
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/zuiduojin.png" alt=""></div>
                        <p class="item-title-txt first">本年度投资金额最高</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data1">5271.43</span> 万元</span>
                            <span class="val2">李*</span>
                            <span class="val3">55岁</span>
                            <span class="val4">青岛市</span>
                        </p>
                    </div>
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/dayingjia.png" alt=""></div>
                        <p class="item-title-txt second">本年度历史回报最高</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data2">142.11</span> 万元</span>
                            <span class="val2">达*</span>
                            <span class="val3">69岁</span>
                            <span class="val4">北京市</span>
                        </p>
                    </div>
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/chahuoyue.png" alt=""></div>
                        <p class="item-title-txt third">本年度投资次数最多</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data3">1909</span>次</span>
                            <span class="val2">x*</span>
                            <span class="val3">32岁</span>
                            <span class="val4">淮安市</span>
                        </p>
                    </div>
                   <!--  <div class="dangyuezhizui-data" style="width: 280px;height: 320px;float:left;">
                        <div class="field">十大投资人投资总额</div>
                        <div class="value">2626万</div>
                        <div class="field">10月业绩总额</div>
                        <div class="value">4.32亿</div>
                    </div> -->
                    <div id="dangyuezhizui_1" style="width: 500px;height: 320px;"></div>
                </div>
                
            </div>
        </div>
         <div class="section section7 kehufuwu repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>客户服务</div>

                <div class="inner-bg">
                    <div class="item1"><span id="s8data1">50854</span> <span class="field">个</span></div>
                    <div class="item2"><span id="s8data2">32943</span> <span class="field">人</span></div>
                    <div class="item3"><span id="s8data3">4917</span> <span class="field">人</span></div>
                    <div class="item4"><span id="s8data4">70120</span> <span class="field">个</span></div>
                </div>
                
            </div>
        </div>
        <div class="section section8 tiyanyouhua repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>体验优化</div>
               <div class="inner-bg">
                    <!-- <div class="item item1">
                        <p class="date">2016年1月18日</p>
                        <p class="des">新版APP上线</p>
                    </div>
                    <div class="item item2">
                        <p class="date">2016年3月20日</p>
                        <p class="des">平台切换金融云服务</p>
                    </div>
                    <div class="item item3">
                        <p class="date">2016年4月10日</p>
                        <p class="des">新版网站上线</p>
                    </div>
                    <div class="item item4">
                        <p class="date">2016年6月23日</p>
                        <p class="des">PC端会员Club上线</p>
                    </div>
                    <div class="item item5">
                        <p class="date">2016年6月07日</p>
                        <p class="des">APP\PC 尊享汇1.0上线</p>
                    </div>
                    <div class="item item6">
                        <p class="date">2016年5月17日</p>
                        <p class="des">APP\PC新手汇上线</p>
                    </div> -->
                </div>
            </div>
        </div>
        <div class="section section8 tyyh2 tiyanyouhua repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>体验优化</div>
               <div class="inner-bg">
                    <!-- <div class="item item1">
                        <p class="date">2016年1月18日</p>
                        <p class="des">新版APP上线</p>
                    </div>
                    <div class="item item2">
                        <p class="date">2016年3月20日</p>
                        <p class="des">平台切换金融云服务</p>
                    </div>
                    <div class="item item3">
                        <p class="date">2016年4月10日</p>
                        <p class="des">新版网站上线</p>
                    </div>
                    <div class="item item4">
                        <p class="date">2016年6月23日</p>
                        <p class="des">PC端会员Club上线</p>
                    </div>
                    <div class="item item5">
                        <p class="date">2016年6月07日</p>
                        <p class="des">APP\PC 尊享汇1.0上线</p>
                    </div>
                    <div class="item item6">
                        <p class="date">2016年5月17日</p>
                        <p class="des">APP\PC新手汇上线</p>
                    </div> -->
                </div>
            </div>
        </div>
         <div class="section section10 jingcaihuodong repeat">
             <div class="inner">
                <div class="title"><span class="line"></span>精彩活动</div>

                <div class="inner-bg" style="width: 1150px;margin-left: auto;margin-right: auto;">
                	<div class="item item1">
                		<div class="image"><img src="${ctx}/img/report/201606/s10i1.jpg" alt=""></div>
                		<div class="con">金玉满堂喜洋洋<br/>压岁钱驾到速来接驾！</div>
                		<div class="date">2016年2月1日至2016年2月29日</div>
                		<div class="join">参与人次   4611人</div>
                	</div>
                	<div class="item item2" style="margin-top:100px">
                		<div class="image"><img src="${ctx}/img/report/201606/s10i2.jpg" alt=""></div>
                		<div class="con">百花齐放<br/>汇盈这“箱”有礼啦！</div>
                		<div class="date">2016年3月1日至2016年3月31日</div>
                		<div class="join">参与人次   4411人</div>
                	</div>
                	<div class="item item3">
                		<div class="image"><img src="${ctx}/img/report/201606/s10i3.jpg" alt=""></div>
                		<div class="con">春风十里<br/>不如送你汇盈好礼!</div>
                		<div class="date">2016年4月15日至2016年4月30日</div>
                		<div class="join">参与人次    1732人</div>
                	</div>
                	<div class="item item4">
                		<div class="image"><img src="${ctx}/img/report/201606/s10i4.jpg" alt=""></div>
                		<div class="con">汇盈F1大师赛<br/>速度开跑 激情无限</div>
                		<div class="date">2016年5月1日至2016年5月31日</div>
                		<div class="join">参与人次   2208人</div>
                	</div>
                </div>
            </div>
        </div>
        <div class="section section10 jingcaihuodong repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>精彩活动</div>
                <div class="active-bg-2 inner-bg" style="width: 1150px;margin-left: auto;margin-right: auto;">
                    <div class="item item1">
                        <div class="image"><img src="${ctx}/img/report/201609/201609_active1.jpg" alt=""></div>
                        <div class="con">投资夺宝<br/>一箭双雕</div>
                        <div class="date">2016年8月11日至2016年9月10日</div>
                        <div class="join"></div>
                    </div>
                     <div class="item item2">
                        <div class="image"><img src="${ctx}/img/report/201610/201610_active1.jpg" alt=""></div>
                        <div class="con">新手有礼！<br/>注册立领68元现金红包</div>
                        <div class="date">2016年9月19日至2016年10月18日</div>
                        <div class="join"></div>
                    </div>
                     <div class="item item3">
                        <div class="image"><img src="${ctx}/img/report/201611/201611_active1.jpg" alt=""></div>
                        <div class="con">万万没想到，<br/>iPhone 7还能这样赢！</div>
                        <div class="date">2016年10月21日至2016年11月30日</div>
                        <div class="join"></div>
                    </div>
                    <div class="item item4" style="margin-top:0;">
                        <div class="image"><img src="${ctx}/img/report/201611/201611_active2.jpg" alt=""></div>
                        <div class="con">加入汇添金<br>为财富添金</div>
                        <div class="date">2016年11月18日至2016年12月2日</div>
                        <div class="join"></div>
                    </div>
                    <div class="item item5" style="margin-top:0">
                        <div class="image"><img src="${ctx}/img/report/201612/201612_active1.jpg" alt=""></div>
                        <div class="con">汇盈金服3周年庆典<br>百亿时刻，邀您共同见证！</div>
                        <div class="date">平台成交额达100亿起至105亿止</div>
                        <div class="join"></div>
                    </div>
                </div>
            </div>
        </div>
         <div class="section section12 huiyinggongyi repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>汇盈公益</div>
                <div class="inner-bg">
                	<div class="item item1">
                		<div class="num">1</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1101.png" alt=""></div>
                		<div class="date">2016年2月4日</div>
                		<div class="con">关爱老人及助学金项目全面启动</div>
                	</div>
                	<div class="item item2" style="margin-top:120px">
                		<div class="num">2</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1102.png" alt=""></div>
                		<div class="date">2016年3月4日</div>
                		<div class="con">打造“互联网+金融+公益”全新模式——“雷锋日”温情满社区</div>
                	</div>
                	<div class="item item3">
                		<div class="num">3</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1103.png" alt=""></div>
                		<div class="date">2016年4月14日</div>
                		<div class="con">发放一对一助学款  59名贫困儿童成首批受助人</div>
                	</div>
                	<div class="item item4">
                		<div class="num">4</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1104.png" alt=""></div>
                		<div class="date">2016年4月15日</div>
                		<div class="con">送温暖献爱心  汇盈金服走进社区看望百岁老人</div>
                	</div>
                </div>
            </div>
        </div>
        <div class="section section12 huiyinggongyi repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>汇盈公益</div>
                <div class="inner-bg">
                	<div class="item item1">
                		<div class="num">5</div>
                		<div class="image"><img src="${ctx}/img/report/201612/s1201.png" alt=""></div>
                		<div class="date">2016年5月6日</div>
                		<div class="con">汇盈金服走入潍坊五村 关爱行动温暖残疾人</div>
                	</div>
                	<div class="item item2" style="margin-top:120px;">
                		<div class="num">6</div>
                		<div class="image"><img src="${ctx}/img/report/201612/s1202.png" alt=""></div>
                		<div class="date">2016年7月4日</div>
                		<div class="con">“七一”慰问送真情  汇盈金服关爱困难老党员</div>
                	</div>
                	<div class="item item3">
                		<div class="num">7</div>
                		<div class="image"><img src="${ctx}/img/report/201612/s1203.png" alt=""></div>
                		<div class="date">2016年7月12日</div>
                		<div class="con">【心系纳雍】汇盈金服情暖灾区</div>
                	</div>
                	<div class="item item4">
                		<div class="num">8</div>
                		<div class="image"><img src="${ctx}/img/report/201612/s1204.png" alt=""></div>
                		<div class="date">2016年12月9日</div>
                		<div class="con">将爱传承——汇盈金服甘肃会宁县暖冬助学公益之行</div>
                	</div>
                </div>
            </div>
        </div>
        
        <div class="section section14 zuji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

               <div class="inner-bg">
                	<div class="item item1">
                		<div class="color1 date">2016年1月18日  </div>
                		<div class="con">汇盈金服新版App正式上线</div>
                	</div>
                	<div class="item item2">
                		<div class="color2 date">2016年1月30日  </div>
                		<div class="con">与亚交所就汇盈金服挂牌上市签订战略合作协议</div>
                	</div>
                	<div class="item item3" style="margin-left:355px !important">
                		<div class="color3 date">2016年2月4日</div>
                		<div class="con">关爱老人及助学金项目全面启动</div>
                	</div>
                	<div class="item item4">
                		<div class="color4 date">2016年3月4日</div>
                		<div class="con">与同济大学达成战略合作暨助学金签约仪式顺利举行</div>
                	</div>
                	<div class="item item5">
                		<div class="color5 date">2016年3月11日</div>
                		<div class="con">与复旦大学达成战略合作暨助学金签约仪式顺利举行</div>
                	</div>
                	<div class="item item6">
                		<div class="color6 date">2016年4月20日</div>
                		<div class="con">新版网站正式上线  七大金融服务频道给您更多选择</div>
                	</div>
                </div>
                
            </div>
        </div>
        <div class="section section14 zuji2 zuji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

                <div class="inner-bg">
                	<div class="item item1">
                		<div class="color7 date">2016年5月14日  </div>
                		<div class="con">与美国上市公司Sino Fortune Holding Corporation正式签署并购重组协议</div>
                	</div>
                	<div class="item item2">
                		<div class="color8 date">2016年5月17日  </div>
                		<div class="con">“新手汇”产品正式上线</div>
                	</div>
                	<div class="item item3" style="margin-left:355px !important">
                		<div class="color9 date">2016年6月7日</div>
                		<div class="con">“尊享汇”产品正式上线</div>
                	</div>
                	<div class="item item4">
                		<div class="color10 date">2016年6月23日</div>
                		<div class="con">汇盈金服会员Club重磅上线  </div>
                	</div>
                	<div class="item item5">
                		<div class="color11 date">2016年8月5日</div>
                		<div class="con">汇盈金服荣获 ISO 认证  赢国际认证组织认可</div>
                	</div>
                	<div class="item item6">
                		<div class="color12 date">2016年9月13日</div>
                		<div class="con">汇盈金服应邀出席“互金G20” 投之家两周年暨A轮融资发布会</div>
                	</div>
                </div>
                
            </div>
        </div>
        <div class="section section14 zuji3 zuji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

                <div class="inner-bg">
                	<div class="item item1">
                		<div class="color13 date">2016年11月16日  </div>
                		<div class="con">“汇添金”上线  智能投标合规新选择</div>
                	</div>
                	<div class="item item2">
                		<div class="color14 date">2016年11月23日  </div>
                		<div class="con">“不忘初心  砥砺前行”——汇盈金服重大战略会议隆重召开</div>
                	</div>
                	<div class="item item3">
                		<div class="color15 date">2016年11月30日</div>
                		<div class="con">汇盈金服受邀参加网贷风险控制与精准营销暨中国网贷百强CEO论坛</div>
                	</div>
                	<div class="item item4">
                		<div class="color16 date">2016年12月11日  </div>
                		<div class="con">“规范发展、共创未来”——汇盈金服参加互金合规划发展高峰论坛</div>
                	</div>
                	<div class="item item5">
                		<div class="color17 date">2016年12月13日</div>
                		<div class="con">汇盈金服累计成交金额突破100亿</div>
                	</div>
                </div>
                
            </div>
        </div>
        <div class="section over">
            <div class="inner"></div>
        </div>

    </div>

    <div class="right-nav">
        <ul>
            <li class="top active" data-idx="0" authors="top"><span>top</span></li>
            <li class="section1" data-idx="1" authors="section1"><span>业绩<br/>总览</span></li>
            <li class="section2" data-idx="2" authors="section2"><span>全年<br/>业绩</span></li>
            <li class="section3 section4" data-idx="3" authors="section3"><span>渠道<br/>分析</span></li>
            <li class="section5" data-idx="5" authors="section5"><span>用户<br/>分析</span></li>
            <li class="section6" data-idx="6" authors="section6"><span>年度<br/>之最</span></li>
            <li class="section7" data-idx="7" authors="section7"><span>客户<br/>服务</span></li>
            <li class="section8 section9" data-idx="8" authors="section8"><span>体验<br/>优化</span></li>
            <li class="section10 section11" data-idx="10" authors="section10"><span>精彩<br/>活动</span></li>
            <li class="section12 section13" data-idx="12" authors="section12"><span>汇盈<br/>公益</span></li>
            <li class="section14 section15 section16" data-idx="14" authors="section14"><span>足迹</span></li>
          <!--   <li class="over" data-idx="17" authors="over"><span>结束</span></li> -->
        </ul>
    </div>
    <script src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.fullPage.js"></script>
    <script type="text/javascript" src="${ctx}/js/echarts.common.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/countUp.min.js"></script>
    <script>
        var Sys = {}; 
        var ua = navigator.userAgent.toLowerCase(); 
        var s; 
        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : 
        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : 
        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : 
        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : 
        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0; 

        if (Sys.ie || Sys.firefox){
            $("dd.copy").hide();
        }
        if(document.documentElement.clientHeight<=900){
            $('.section .inner').css("zoom",.8);
        }

        $(".right-nav").find("li").children("span");
    </script>
    
    <script>
        //echarts默认设置
        var mainColor = ['#5ffae8','#2da9dc','#6675c8','#d53535','#ed4544','#dd8c33','#e0b370','#b9a832','#c73171','#8832c9','#33c888'];
        var secondColor = ['#ed4544','#6474cb','#5efbe7','#e0b36f','#ed4544','#e0b36f','#daae6d','#45b067','#066f28','#06732e'];

        var echarts2_1_color = ['#5efbe7','#2eace0','#6474cb','#da3534','#ed4544','#dd8b32','#e0b36f','#48bd6c','#2baa3f','#da3534','#90c231','#ffef00'];
        var mainTextStyle = {
            color:"#fff",
            fontWeight:"normal"
        }
    </script>
    <script type="text/javascript">
        /* section1 当月业绩
        -----------------------------------------------------------------------------------------*/
        //交易金额 单位 （元）
        var data_2_1 = {
        	title:"交易金额 单位（亿元）",
        	data:[{
		        	name:"2016年1月",
					value:3.98,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[0]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[0]
		        		}
		        	}
		        },{
		        	name:"2016年2月",
					value:3.50,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[1]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[1]
		        		}
		        	}
		        },{
		        	name:"2016年3月",
					value:5.17,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[2]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[2]
		        		}
		        	}
		    	},
		    	{
		        	name:"2016年4月",
					value:3.91,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[3]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[3]
		        		}
		        	}
		        },{
		        	name:"2016年5月",
					value:4.24,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[4]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[4]
		        		}
		        	}
		        },{
		        	name:"2016年6月",
					value:4.44,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[5]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[5]
		        		}
		        	}
		    	},{
		        
		        	name:"2016年7月",
					value:4.82,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[6]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[6]
		        		}
		        	}
		    	},{
		        
		        	name:"2016年8月",
					value:4.50,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[7]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[7]
		        		}
		        	}
		    	},{
		        
		        	name:"2016年9月",
					value:5.88,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[8]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[8]
		        		}
		        	}
		    	},{
		        	name:"2016年10月",
					value:4.38,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[9]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[9]
		        		}
		        	}
		    	},{
		        
		        	name:"2016年11月",
					value:4.87,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[10]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[10]
		        		}
		        	}
		    	},{
		        
		        	name:"2016年12月",
					value:4.91,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[11]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[11]
		        		}
		        	}
		    	}
		    ],
	        field : ['2016年1月','2016年2月','2016年3月','2016年4月','2016年5月','2016年6月','2016年7月','2016年8月','2016年9月','2016年10月','2016年11月','2016年12月']
        };
        /* var data_2_2 = {
            title:"赚取收益 单位 （万元）",
            data:[{
                    name:"2016年11月",
                    value:1434.24,
                    itemStyle:{
                        normal:{
                            color:"#ed4544"
                        },
                        emphasis:{
                            color:"#ed4544"
                        }
                    }
                },{
                    name:"2015年11月",
                    value:1175.52,
                    itemStyle:{
                        normal:{
                            color:"#00b8ec"
                        },
                        emphasis:{
                            color:"#00b8ec"
                        }
                    }
                }
            ],
            field : ['2016年11月','2015年11月']
        } */

        var echarts2_1_option  = {
        	color:echarts2_1_color,
        	title:{
        		text:data_2_1.title,
        		textStyle:mainTextStyle,
        		top:290,
        		left:"center"
        	},
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        },
		        formatter: "{b}<br/> {c}亿元"
		    },
		    grid: {
		        left: '-15',
		        right: '0',
		        bottom: '80',
		        top:'15',
		        containLabel: true
		    },
		    legend: {
		        data:data_2_1.field
		    },
		    xAxis: [
		        {
		            type: 'category',
		            axisLabel:{
		            	textStyle:mainTextStyle
		            },
		            data : data_2_1.field,
		            axisLine:{
		            	lineStyle:{
		            		color:"#6474cb",
		            		width:1
		            	}
		            },
		            splitLine:{
		            	show:false
		            }
		        }
		    ],
		    yAxis:{
	            show:false
	        },
		    series: [
		        {
		            name:data_2_1.title,
		            type:'bar',
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{c}",
		                    position:"top",
		                    textStyle:{
		                    	fontSize:16
		                    }
		                }
		            },
		            barWidth:40,
		            data : data_2_1.data
		        },
		        {
		            name:data_2_1.title,
		            type:'line',
		            data:data_2_1.data
		        }
		    ]
		};
		
        var echarts2_1 = echarts.init(document.getElementById('echarts2_1'));//
        
    </script>
    <script>
        var echarts3_1_option = {
            title: {
                text: 'APP、微信、PC成交笔数对比图',
                left: 'center',
                top: 20,
                textStyle: {
                    color: '#5df8e4',
                    fontSize:30,
                    fontWeight:"normal"
                }
            },

            tooltip : {
                trigger: 'item',
                formatter: "{b} : {c} ({d}%)"
            },

            visualMap: {
                show: false,
                min: 80,
                max: 600,
                inRange: {
                    colorLightness: [0, 1]
                }
            },
            series : [
                {
                    name:'访问来源',
                    type:'pie',
                    radius : '55%',
                    center: ['50%', '50%'],
                    data:[
                        {value:116717, 
                        name:'PC',
                        itemStyle:{
                                normal:{
                                    color:"#607ace"
                                },
                                emphasis:{
                                    color:"#607ace"
                                }
                            }
                            
                        },
                        {
                            value:40955, 
                            name:'APP',
                            itemStyle:{
                                normal:{
                                    color:"#31aad4"
                                },
                                emphasis:{
                                    color:"#31aad4"
                                }
                            }
                            
                        },
                        {
                            value:7303, 
                            name:'微信',
                            itemStyle:{
                                normal:{
                                    color:"#90d7cc"
                                },
                                emphasis:{
                                    color:"#90d7cc"
                                }
                            }
                            
                        }
                    ].sort(function (a, b) { return a.value - b.value}),
                    // roseType: 'angle',
                    label: {
                        normal: {
                            textStyle: {
                                color: '#fff',
                                fontSize:18
                            },
                            formatter: "{b}\n{d}%",
                        }
                    },
                    labelLine: {
                        normal: {
                            lineStyle: {
                                color: '#fff'
                            },
                            smooth: 0.2,
                            length: 8,
                            length2: 10
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: '#c23531',
                            shadowBlur: 50,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        var echarts3_1 = echarts.init(document.getElementById('echarts3_1'));
    </script>
        <script>
        var echarts4_1_option = {
            title: {
                text: 'APP、微信、PC累计成交金额对比图',
                left: 'center',
                top: 20,
                textStyle: {
                    color: '#5df8e4',
                    fontSize:30,
                    fontWeight:"normal"
                }
            },

            tooltip : {
                trigger: 'item',
                formatter: "{b} : {c} ({d}%)"
            },

            visualMap: {
                show: false,
                min: 80,
                max: 600,
                inRange: {
                    colorLightness: [0, 1]
                }
            },
            series : [
                {
                    name:'访问来源',
                    type:'pie',
                    radius : '55%',
                    center: ['50%', '50%'],
                    data:[
                        {value:3876091923, 
                        name:'PC',
                        itemStyle:{
                                normal:{
                                    color:"#607ace"
                                },
                                emphasis:{
                                    color:"#607ace"
                                }
                            }
                            
                        },
                        {
                            value:1268084639, 
                            name:'APP',
                            itemStyle:{
                                normal:{
                                    color:"#31aad4"
                                },
                                emphasis:{
                                    color:"#31aad4"
                                }
                            }
                            
                        },
                        {
                            value:249388230, 
                            name:'微信',
                            itemStyle:{
                                normal:{
                                    color:"#90d7cc"
                                },
                                emphasis:{
                                    color:"#90d7cc"
                                }
                            }
                            
                        }
                    ].sort(function (a, b) { return a.value - b.value}),
                    // roseType: 'angle',
                    label: {
                        normal: {
                            textStyle: {
                                color: '#fff',
                                fontSize:18
                            },
                            formatter: "{b}\n{d}%",
                        }
                    },
                    labelLine: {
                        normal: {
                            lineStyle: {
                                color: '#fff'
                            },
                            smooth: 0.2,
                            length: 8,
                            length2: 10
                        }
                    },
                    itemStyle: {
                        normal: {
                            color: '#c23531',
                            shadowBlur: 50,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        var echarts4_1 = echarts.init(document.getElementById('echarts4_1'));
    </script>
    <script type="text/javascript">
        /* section6 用户分析数据
        -----------------------------------------------------------------------------------------*/

        //年龄、性别分布
        var data_6_1 = {
            title:"年龄、性别分布",
            data:{
                sex:[
                    {value:70905, name:'女'},
                    {value:82809, name:'男'}
                ],
                age:[
                    {value:52332, name:'18-29岁'},
                    {value:37796, name:'30-39岁'},
                    {value:25288, name:'40-49岁'},
                    {value:18044, name:'50-59岁'},
                    {value:20010, name:'60岁以上'}
                ]
            },
            field:['18-29岁','30-39岁','40-49岁','50-59岁','60岁以上']
        }

        //人均投资额
        var yonghufenxi_data_3 = {
            title:'金额分布',
            //subtitle:"101501.22",
            data:[
                {value:49281, name:'1万以下'},
                {value:15350, name:'1-5万'},
                {value:4250, name:'5-10万'},
                {value:7146, name:'10-50万'},
                {value:3598, name:'50万以上'}
            ],
            field:['1万以下','1-5万','5-10万','10-50万','50万以上']
        }
        var yonghufenxi_1 = echarts.init(document.getElementById('yonghufenxi_1'));
        var yonghufenxi_3 = echarts.init(document.getElementById('yonghufenxi_3'));
        // 指定图表的配置项和数据
        var yonghufenxi_1_option  = {
            color:mainColor,
            tooltip: {
            	
                trigger: 'item',
                //formatter: "{a} <br/>{b}: {c} ({d}%)"
                formatter: function(obj){
                	if(obj.data.value == "52332"){ //hack 数字bug 应该显示34.09 插件显示 34.1
               			obj.percent = "34.09";
               		}
                    return obj.seriesName +"<br/>"+obj.data.name+" "+obj.data.value+"("+obj.percent+"%)";
                    
                } 
            },
            legend: {
                orient: 'vertical',
                x: 'right',
                y:'middle',
                data:data_6_1.field,
                textStyle:{
                    color:["#fff"]
                }
            },

            series: [
                {
                    name:data_6_1.title,
                    type:'pie',
                    radius: [0, '30%'],
                    label: {
                        normal: {
                            position: 'inner',
                            formatter:"{b}\n{d}% "
                        }
                    },
                    data:data_6_1.data.sex,
                    center: ['40%', '50%'],
                    color:["#1dcded","#6474cb"]
                },
                {
                    name:data_6_1.title,
                    type:'pie',
                    label:{
                        normal:{
                            /* formatter: "{b}\n{d}%" */
                        	 formatter: function(obj){
                        		 
                        		if(obj.value == "52332"){ //hack 数字bug 应该显示34.09 插件显示 34.1
                        			obj.percent = "34.09";
                        		}
	                            return obj.data.name+"\n"+obj.percent+"%";
	                        } 
                        }
                    },
                    radius: ['40%', '55%'],
                    data:data_6_1.data.age,
                    center: ['40%', '50%']
                }
            ]
        };
        
        var yonghufenxi_3_option  = {
            color:mainColor,
            title : {
                text:yonghufenxi_data_3.title,
                textStyle:mainTextStyle,
                left:"37%",
                top:180
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}元: {c}人 ({d}%)"
            },
            legend: {
                orient: 'vertical',
                x: 'right',
                y:'middle',
                data:yonghufenxi_data_3.field,
                textStyle:{
                    color:["#fff"]
                }
            },
            series: [
                {
                    name:yonghufenxi_data_3.title,
                    type:'pie',
                    label:{
                        normal:{
                            formatter: "{b}\n{d}%"
                        }
                    },
                    radius: ['40%', '55%'],
                    data:yonghufenxi_data_3.data,
                    center: ['45%', '50%']
                }
            ]
        };
        
    </script>
    <script>
    /* section7 用户分析数据
    -----------------------------------------------------------------------------------------*/


    /*//十大投资人
    var data_6_4 = {
        title:"当月十大投资人",
        data:[
            {
                name:"l*",
                value:776,
                itemStyle:{
                    normal:{
                        color:mainColor[0]
                    },
                    emphasis:{
                        color:mainColor[0]
                    }
                }
            },
            {
                name:"李*",
                value:328.17,
                itemStyle:{
                    normal:{
                        color:mainColor[1]
                    },
                    emphasis:{
                        color:mainColor[1]
                    }
                }
            },
            {
                name:"d*",
                value:273.90,
                itemStyle:{
                    normal:{
                        color:mainColor[2]
                    },
                    emphasis:{
                        color:mainColor[2]
                    }
                }
            },
            {
                name:"l*",
                value:220.92,
                itemStyle:{
                    normal:{
                        color:mainColor[3]
                    },
                    emphasis:{
                        color:mainColor[3]
                    }
                }
            },
            {
                name:"z*",
                value:206.07,
                itemStyle:{
                    normal:{
                        color:mainColor[4]
                    },
                    emphasis:{
                        color:mainColor[4]
                    }
                }
            },
            {
                name:"崔*",
                value:186.11,
                itemStyle:{
                    normal:{
                        color:mainColor[5]
                    },
                    emphasis:{
                        color:mainColor[5]
                    }
                }
            },
            {
                name:"李*",
                value:177.41,
                itemStyle:{
                    normal:{
                        color:mainColor[6]
                    },
                    emphasis:{
                        color:mainColor[6]
                    }
                }
            },
            {
                name:"h*",
                value:154.23,
                itemStyle:{
                    normal:{
                        color:mainColor[7]
                    },
                    emphasis:{
                        color:mainColor[7]
                    }
                }
            },
            {
                name:"李*",
                value:153.09,
                itemStyle:{
                    normal:{
                        color:mainColor[8]
                    },
                    emphasis:{
                        color:mainColor[8]
                    }
                }
            },
            {
                name:"腾*",
                value:150,
                itemStyle:{
                    normal:{
                        color:mainColor[9]
                    },
                    emphasis:{
                        color:mainColor[9]
                    }
                }
            }
        ],
        field:['l*','李*','d*','l*','z*','崔*','李*','h*','李*','腾*']
    };*/

    var data_6_5 = {
        title:'\r10大投资人\n金额之和占比',
        subtitle:"7.52%",//十大投资人占比
        data:[
            {value:303382884, name:'10大投资人金额'},
            {value:3730080130, name:'其他投资人金额'}
        ]
    }
    var dangyuezhizui_1 = echarts.init(document.getElementById('dangyuezhizui_1'));
        
    
        var dangyuezhizui_1_option  = {
            color:mainColor,
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            title : {
                text:data_6_5.title,
                textStyle:mainTextStyle,
                subtext:data_6_5.subtitle,
                subtextStyle:{
                    color:"#5ac0fe",
                    fontSize:20
                },
                left:"center",
                top:120
            },
            series: [
                {
                    name:data_6_5.title,
                    type:'pie',
                    label:{
                        normal:{
                            formatter: "{b}\n{d}%",
                            textStyle:{
                                fontSize:16
                            }
                        }
                    },
                    radius: ['40%', '55%'],
                    data:data_6_5.data,
                    center: ['50%', '50%']
                }
            ]
        };
        
    </script>

    <script>
    $(document).ready(function() {
        var nav = $(".right-nav");
        var countUpOptions = {
          useEasing : true, 
          useGrouping : true, 
          separator : ',', 
          decimal : '.', 
          prefix : '', 
          suffix : '' 
        };
        //向下滚屏
        $(document).on('click', '.scroll', function() {
            $.fn.fullpage.moveSectionDown();
        });
        $('#fullpage').fullpage({
            anchors:['top','section1','section2','section3','section3','section5','section6','section7','section8','section8','section10','section10','section12','section12','section14','section14','section14','over'],
            onLeave :function(index, nextIndex, direction){
                var idx = nextIndex-1;
                var anchors = $("#fullpage").children(".section").eq(idx).data("anchor");
                if(idx == 1){
					var s1data1 = new CountUp("s1data1", 0, 10332979404, 0, 1.5, countUpOptions); //累计交易额(元)
					var s1data2 = new CountUp("s1data2", 0, 203866, 0, 1.5, countUpOptions);//平台注册人数(人)
					var s1data3 = new CountUp("s1data3", 0, 348388571, 0, 1.5, countUpOptions);//累计赚取收益(元)
					var s1data4 = new CountUp("s1data4", 0, 49465710, 0, 1.5, countUpOptions);//风险保证金(元)
                    s1data1.start();
                    s1data2.start();
                    s1data3.start();
                    s1data4.start();
                }else if(idx == 2){
                	var s2data1 = new CountUp("s2data1", 0, 164975, 0, 1.5, countUpOptions);
					var s2data2 = new CountUp("s2data2", 0, 54.78, 2, 1.5, countUpOptions);
					var s2data3 = new CountUp("s2data3", 0, 16891.97, 2, 1.5, countUpOptions);
					/* var s2data4 = new CountUp("s2data4", 0, 58138, 0, 1.5, countUpOptions);
					var s2data5 = new CountUp("s2data5", 0, 10.23, 2, 1.5, countUpOptions); */
					/* var s2data6 = new CountUp("s2data6", 0, 34103, 0, 1.5, countUpOptions);
					var s2data7 = new CountUp("s2data7", 0, 8.83, 2, 1.5, countUpOptions); */
					var s2data8 = new CountUp("s2data8", 0, 5.88, 2, 1.5, countUpOptions);
					var s2data9 = new CountUp("s2data9", 0, 11.42, 2, 1.5, countUpOptions);
					s2data1.start();
					s2data2.start();
					s2data3.start();
					/* s2data4.start();
					s2data5.start(); */
					/* s2data6.start();
					s2data7.start(); */
					s2data8.start();
					s2data9.start();
					//初始化图表
					echarts2_1.setOption(echarts2_1_option);
                }else if(idx == 3){
                    var s3data1 = new CountUp("s3data1", 0, 40955, 0, 1.5, countUpOptions);
                    var s3data2 = new CountUp("s3data2", 0, 24.82, 2, 1.5, countUpOptions);
                    var s3data3 = new CountUp("s3data3", 0, 7303, 0, 1.5, countUpOptions);
                    var s3data4 = new CountUp("s3data4", 0, 4.43, 2, 1.5, countUpOptions);
                    var s3data5 = new CountUp("s3data5", 0, 116717, 0, 1.5, countUpOptions);
                    var s3data6 = new CountUp("s3data6", 0, 70.75, 2, 1.5, countUpOptions);
                    s3data1.start();
                    s3data2.start();
                    s3data3.start();
                    s3data4.start();
                    s3data5.start();
                    s3data6.start();
                    echarts3_1.setOption(echarts3_1_option);
                }else if(idx == 4){
                	var s4data1 = new CountUp("s4data1", 0, 12.68, 2, 1.5, countUpOptions);
                    var s4data2 = new CountUp("s4data2", 0, 23.51, 2, 1.5, countUpOptions);
                    var s4data3 = new CountUp("s4data3", 0, 2.49, 2, 1.5, countUpOptions);
                    var s4data4 = new CountUp("s4data4", 0, 4.62, 2, 1.5, countUpOptions);
                    var s4data5 = new CountUp("s4data5", 0, 38.76, 2, 1.5, countUpOptions);
                    var s4data6 = new CountUp("s4data6", 0, 71.87, 2, 1.5, countUpOptions);
                    s4data1.start();
                    s4data2.start();
                    s4data3.start();
                    s4data4.start();
                    s4data5.start();
                    s4data6.start();
                    echarts4_1.setOption(echarts4_1_option);
                    
                }else if(idx == 5){
                	yonghufenxi_1.setOption(yonghufenxi_1_option);
                    yonghufenxi_3.setOption(yonghufenxi_3_option);
                    
                }else if(idx == 6){
                	var s6data1 = new CountUp("s6data1", 0, 5271.43, 2, 1.5, countUpOptions);//当月投资金额最高(万元)
                    var s6data2 = new CountUp("s6data2", 0, 142.11, 2, 1.5, countUpOptions);//当月投资次数最多(次)
                    var s6data3 = new CountUp("s6data3", 0, 1909, 0, 1.5, countUpOptions);//当月历史回报最高(万元)
                    s6data1.start();
                    s6data2.start();
                    s6data3.start();
                    dangyuezhizui_1.setOption(dangyuezhizui_1_option);
                }else if(idx == 7){
                	var s8data1 = new CountUp("s8data1", 0, 50854, 0, 1.5, countUpOptions);//400电话数量(个)
                    var s8data2 = new CountUp("s8data2", 0, 32943, 0, 1.5, countUpOptions);//qq客服接待人数(人)
                    var s8data3 = new CountUp("s8data3", 0, 4917, 0, 1.5, countUpOptions);//微信客服接待人数(人)
                    var s8data4 = new CountUp("s8data4", 0, 70120, 0, 1.5, countUpOptions);//解决问题个数(个)
                    s8data1.start();
                    s8data2.start();
                    s8data3.start();
                    s8data4.start();
                }else if(idx == 8){
                	
                }
                if(idx == 17){
                    $(".right-nav").fadeOut(300);
                }else if(nav.is(":hidden")){
                    $(".right-nav").fadeIn(300);
                }
                $(".right-nav ul li").removeClass("active");
                $(".right-nav ul li[authors="+anchors+"]").addClass("active");
            }
        });


        $(".right-nav ul li").on("click",function(){
            var idx = $(this).data("idx")+1;

            $.fn.fullpage.moveTo(idx);
        })
    });
    </script>
</body>

</html>
