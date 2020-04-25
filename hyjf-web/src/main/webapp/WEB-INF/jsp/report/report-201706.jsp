<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="UTF-8">   
    <meta http-equiv = "X-UA-Compatible" content = "IE=edge,chrome=1"/>

    <title>2017年上半年报告 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.fullPage.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/newweb-report.css" />
	<style>
	.section.section3 .echarts-box {
    width: 1000px;
    height: 680px;
    margin-top: 40px;
    margin-left: auto;
    margin-right: auto;
}
	.section.section3 .echarts-box>div {
    display: block;
    float: left;
    width: 500px;
    height: 340px;
    color: #fff;
}
.section.section3 .data3_4>div {
    display: block;
    width: 400px;
    float: left;
    padding-left: 75px;
    color: #e0b36f;
    font-size: 16px;
    margin-top: 10px;
}
.section.section3 .data3_4>div .field2 {
    color: #2ea8da;
}
.section.section3 .data3_4>div .field {
    float: left;
}
.section.section3 .data3_4>div .data {
    float: left;
}



.seventeen-june .section.section2 .data-box p:first-child {
	background: none;
}

.seventeen-june .section.section2 .data-box p {
	background: url(../img/report/line.png) center top no-repeat;
	padding-top: 20px;
	margin-bottom: 20px;
	text-align: center;
}

.seventeen-june .section.section2 .data-box p {
	font-size: 24px;
	color: #fff;
	margin-top: 10px;
	margin-bottom: 10px;
}

.seventeen-june .section.section2 .data-box p span.data {
	font-size: 34px;
	color: #e0b36f;
}

.seventeen-june .tiyanyouhua .inner-bg {
    background: url(../img/report/201706/s9inner.jpg) no-repeat;
    width: 1052px;
    height: 580px;
    margin-top: 30px;
}
.seventeen-june .tiyanyouhua .inner-bg .item {
    width: 350px;
    float: left;
    text-align: center;
    height: 180px;
}
.seventeen-june .tiyanyouhua .inner-bg .item1 {
    margin-left: 74px;
    margin-top: 193px;
}
.seventeen-june .tiyanyouhua .inner-bg .item2 {
    margin-left: 234px;
    margin-top: 190px;
}
.tiyanyouhua .right-nav li.section11 {
    background-position: 0 -629px;
}

.seventeen-june .jingcaihuodong .inner-bg {
	margin-top: 30px
}

.seventeen-june .jingcaihuodong .inner-bg .item {
	width: 520px;
	height: 240px;
	margin: 0;
	padding-top: 70px;
	padding-left: 24px;
	background: url(../img/report/s12i1.png) 0 0 no-repeat
}

.seventeen-june .jingcaihuodong .inner-bg .item.item1 {
	float: left;
	background: url(../img/report/s12i1.png) 0 0 no-repeat;
}

.seventeen-june .jingcaihuodong .inner-bg .item.item2 {
	float: right;
	background: url(../img/report/s12i3.png) 0 0 no-repeat;
}

.seventeen-june .jingcaihuodong .inner-bg .item.item3 {
	float: left;
	background: url(../img/report/s12i2.png) 0 0 no-repeat;
}

.seventeen-june .jingcaihuodong .inner-bg .item.item4 {
	float: right;
	background: url(../img/report/s12i4.png) 0 0 no-repeat;
}

.seventeen-june .jingcaihuodong .inner-bg .item.item5 {
	float: right;
	background: url(../img/report/s12i5.png) 0 0 no-repeat;
}

.seventeen-june .jingcaihuodong .inner-bg .item .image img {
	width: 200px;
	height: 130px
}

.seventeen-june .jingcaihuodong .inner-bg .item .con {
	height: 90px;
	font-size: 22px
}

.seventeen-june .jingcaihuodong .inner-bg .item {
	padding-top: 62px
}

.seventeen-june .zuji .inner-bg {
    width: 814px;
    height: 706px;
    background: url(../img/report/zjbg12.png) 0 0 no-repeat;
}
.seventeen-june .zuji .inner-bg>div {
    margin-top: 11px;
    margin-bottom: 10px;
}
.seventeen-june .zuji3 .inner-bg>div {
    margin-top: 26px;
    margin-bottom: 16px;
}
.seventeen-june .section.zuji .inner-bg .item2 {
    margin-left: 300px;
}
.seventeen-june .section.zuji .inner-bg .item3 {
    margin-left: 360px;
}
.seventeen-june .section.zuji .inner-bg .item4 {
    margin-left: 289px;
}
.seventeen-june .section.zuji .inner-bg .item5 {
    margin-left: 228px;
}
.seventeen-june .section.zuji .inner-bg .item6{
    margin-left: 170px;
}
.seventeen-june .zuji3 .inner-bg {
    background: url(../img/report/zjbg123.png) 0 0 no-repeat;
}
.seventeen-june .section.zuji3 .inner-bg .item1{
    margin-left: 187px;
}
.seventeen-june .section.zuji3 .inner-bg .item2{
    margin-left: 260px;
}
.seventeen-june .section.zuji3 .inner-bg .item3{
    margin-left: 332px;
}
.seventeen-june .section.zuji3 .inner-bg .item4{
    margin-left: 258px;
}
.seventeen-june .section.zuji3 .inner-bg .item5{
    margin-left: 193px;
}
	</style>
</head>

<body class="seventeen-june">
    <div id="fullpage">
        <div class="section top" style="background-image: url(${ctx}/img/report/201706/top.jpg?v=20171123);">
            <div class="scroll"></div>
        </div>
        <div class="section section1 yejizonglan repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>业绩总览</div>
                <dl class="data1">
                    <dt>累计交易额（元）</dt>
                    <dd id="s1data1">14,159,276,111.33</dd>
                </dl>
                <dl class="data2">
                    <dt>平台注册人数（人）</dt>
                    <dd id="s1data2">300997</dd>
                </dl>
                <dl class="data3">
                    <dt>累计赚取收益（元）</dt>
                    <dd id="s1data3">469,380,546.75</dd>
                </dl>
            </div>
        </div>
        <div class="section section2 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>上半年业绩</div>
                <div class="echarts-box">
	                <div id="echarts2_1" style="width:800px;margin-left: auto;margin-right: auto;float:none;"></div>
	            	
                </div>
                <div class="data-box">
                <p style="font-size: 18px;">2017年上半年平台累计成交<span id="s2data1" class="data">122163</span>笔，累计成交金额<span class="data"><span id="s2data2">37.50</span>亿</span>元，累计赚取收益<span class="data"><span id="s2data3" class="data">9475.38</span>万</span>元</p>
                <p style="font-size: 18px;">累计充值<span id="s2data4" class="data">73914</span>笔，共<span class="data"><span id="s2data5">17.18</span>亿</span>元 </p>
                <p style="font-size: 18px;text-align: center;">成交量最高单月为<span class="data">6</span>月，共成交<span class="data"><span id="s2data8">7.88</span>亿</span>元</p>
                <p style="font-size: 18px;text-align: center;">上半年预期平均收益率<span class="data"><span id="s2data9">9.75</span>%</span></p>

                </div>
            </div>
        </div>
        <div class="section section3 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>标的分析</div>
                <div class="echarts-box">
                	<div id="echarts3_1" style="height:360px"></div>
                	<div id="echarts3_3" style="height:360px"></div>
                	<div class="data3_4">
                		<div><span class="field field2" style="line-height: 50px;">上半年发标个数 &nbsp;</span><span class="data" style="font-size: 34px;"><span id="s3data2">5288</span>个</span></div>
                	</div>
                	<div class="data3_4">
                		<div><span class="field field2" style="line-height: 50px;">待偿金额&nbsp;</span>  <span class="data" style="font-size: 34px;"><span id="s3data3">1861806200</span>元</span></div>
                	</div>
                </div>
            </div>
        </div>
         
        <div class="section section4 qudaofenxi repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>渠道分析</div>
                <div class="qudaofenxi_style2_left">
                    <div class="item-title"><img src="${ctx}/img/report/txt_app.png" alt=""></div>
                    <div class="item"><div class="icon phone"></div>上半年成交<span id="s4data1">53293</span>笔，占比<span id="s4data2">43.62</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_weixin.png" alt=""></div>
                    <div class="item"><div class="icon wechat"></div>年度成交<span id="s4data3">5228</span>笔，占比<span id="s4data4">4.28</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_pc.png" alt=""></div>
                    <div class="item"><div class="icon desktop"></div>年度成交<span id="s4data5">63642</span>笔，占比<span id="s4data6">52.10</span>%</div>
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
                <div class="title"><span class="line"></span>上半年之最</div>
                <div class="dangyuezhizui-left-style2">
                    <div class="item-title">上半年十大投资人</div>
                    <div class="item-box">
                        <div class="item"><div class="name">李*</div><div class="value top3">2,248.80万</div></div>
                        <div class="item"><div class="name">l*</div><div class="value top3">2,124.00万</div></div>
                        <div class="item"><div class="name">l*</div><div class="value top3">2,051.31万</div></div>
                        <div class="item"><div class="name">达*</div><div class="value">1,662.19万</div></div>
                        <div class="item"><div class="name">H*</div><div class="value">1,497.17万</div></div>
                        <div class="item"><div class="name">李*</div><div class="value">1,216.88万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">1,116.55万</div></div>
                        <div class="item"><div class="name">H*</div><div class="value">1,035.94万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">1,029.31万</div></div>
                        <div class="item"><div class="name">b*</div><div class="value">998.36万</div></div>
                    </div>
                </div>
                <div class="dangyuezhizui-right-style2">
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/zuiduojin.png" alt=""></div>
                        <p class="item-title-txt first">上半年投资金额最高</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data1">2248.80</span>万元</span>
                            <span class="val2">李*</span>
                            <span class="val3">62岁</span>
                            <span class="val4">烟台市</span>
                        </p>
                    </div>
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/dayingjia.png" alt=""></div>
                        <p class="item-title-txt second">上半年历史回报最高</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data2">70.32</span> 万元</span>
                            <span class="val2">h*</span>
                            <span class="val3">47岁</span>
                            <span class="val4">青岛市</span>
                        </p>
                    </div>
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/chahuoyue.png" alt=""></div>
                        <p class="item-title-txt third">上半年投资次数最多</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data3">1327</span>次</span>
                            <span class="val2">X*</span>
                            <span class="val3">33岁</span>
                            <span class="val4">淮安市</span>
                        </p>
                    </div>
                    <div id="dangyuezhizui_1" style="width: 500px;height: 320px;"></div>
                </div>
                
            </div>
        </div>
         <div class="section section7 kehufuwu repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>客户服务</div>

                <div class="inner-bg">
                    <div class="item1"><span id="s8data1">32644</span> <span class="field">个</span></div>
                    <div class="item2"><span id="s8data2">37479</span> <span class="field">人</span></div>
                    <div class="item3"><span id="s8data3">6856</span> <span class="field">人</span></div>
                    <div class="item4"><span id="s8data4">79693</span> <span class="field">个</span></div>
                </div>
                
            </div>
        </div>
        <div class="section section8 tiyanyouhua repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>体验优化</div>
               <div class="inner-bg">
                    <div class="item item1">
                        <p class="date">2017年1月4日</p>
                        <p class="des">App新增发现功能</p>
                    </div>
                    <div class="item item2">
                        <p class="date">2017年5月16日</p>
                        <p class="des">App首页改版</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="section section9 jingcaihuodong repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>精彩活动</div>
                <div class="active-bg-2 inner-bg" style="width: 1150px;margin-left: auto;margin-right: auto;">
                    <div class="item item1">
                        <div class="image"><img src="${ctx}/img/report/201701/201701_active1.jpg" alt=""></div>
                        <div class="con">汇盈金服3周年盛典<br/> 百亿时刻，邀您共同见证</div>
                        <div class="date">平台成交额达100亿起至105亿止</div>
                        <div class="join"></div>
                    </div>
                     <div class="item item2">
                        <div class="image"><img src="${ctx}/img/report/201701/201701_active2.jpg" alt=""></div>
                        <div class="con">金鸡报晓<br/>贺岁纳福</div>
                        <div class="date">2017年1月20日至2017年2月11日</div>
                        <div class="join"></div>
                    </div>
                     <div class="item item3">
                        <div class="image"><img src="${ctx}/img/report/201704/201704_active1.jpg" alt=""></div>
                        <div class="con">新手福利<br/>注册即领188元新手红包</div>
                        <div class="date">2017年4月6日起至2017年4月18日</div>
                        <div class="join"></div>
                    </div>
                    <div class="item item4" style="margin-top:0;">
                        <div class="image"><img src="${ctx}/img/report/201704/201704_active2.jpg" alt=""></div>
                        <div class="con">新手限时专享 <br>888元现金红包</div>
                        <div class="date">2017年4月18日起</div>
                        <div class="join"></div>
                    </div>
                    <div class="item item5" style="margin-top:0">
                        <div class="image"><img src="${ctx}/img/report/201706/201706_active1.png" alt=""></div>
                        <div class="con">理财，就“邀”一起玩</div>
                        <div class="date">2017年6月1日至2017年6月30日</div>
                        <div class="join"></div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="section section10 zuji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

               <div class="inner-bg">
                	<div class="item item1">
                		<div class="color1 date">2017年1月6日  </div>
                		<div class="con">“汇聚梦想 赢创未来”——汇盈金服年终总结大会隆重召开</div>
                	</div>
                	<div class="item item2">
                		<div class="color2 date">2017年1月7日  </div>
                		<div class="con">“宁为凤首 春风圣意”——汇盈金服年度庆典盛大绽放</div>
                	</div>
                	<div class="item item3">
                		<div class="color3 date">2017年1月11日</div>
                		<div class="con">汇盈金服应邀出席2017消费金融创新发展论坛 开启财富智慧双碰撞</div>
                	</div>
                	<div class="item item4">
                		<div class="color4 date">2017年2月16日</div>
                		<div class="con">汇盈金服与上海衷和投资管理有限公司达成战略合作 资源渠道又拓宽</div>
                	</div>
                	<div class="item item5">
                		<div class="color5 date">2017年2月17日</div>
                		<div class="con">投之家莅临汇盈金服洽谈深度合作 初定战略合作框架</div>
                	</div>
                	<div class="item item6">
                		<div class="color6 date">2017年3月1日</div>
                		<div class="con">汇盈金服与投之家正式达成战略合作</div>
                	</div>
                </div>
                
            </div>
        </div>
        <div class="section section10 zuji3  zuji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

                <div class="inner-bg">
                	<div class="item item1">
                		<div class="color7 date">2017年3月2日 </div>
                		<div class="con">汇盈金服受邀出席中国网贷百强CEO论坛</div>
                	</div>
                	<div class="item item2">
                		<div class="color8 date">2017年3月22日 </div>
                		<div class="con">汇盈金服与江西银行正式签订存管协议</div>
                	</div>
                	<div class="item item3">
                		<div class="color10 date">2017年5月6日</div>
                		<div class="con">大浪淘沙，沉者为金——之家哥俱乐部济南站顺利举办</div>
                	</div>
                	<div class="item item4">
                		<div class="color11 date">2017年5月24日</div>
                		<div class="con">汇盈金服应邀出席中国网贷百强CEO论坛 一场激烈的思想盛宴</div>
                	</div>
                	<div class="item item5">
                		<div class="color12 date">2017年6月18日</div>
                		<div class="con">网贷整改，不忘初心——之家哥俱乐部青岛站活动顺利举办</div>
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
            <li class="section2" data-idx="2" authors="section2"><span>上半年<br/>业绩</span></li>
            <li class="section3" data-idx="3" authors="section3"><span>标的<br/>分析</span></li>
            <li class="section4" data-idx="4" authors="section4"><span>渠道<br/>分析</span></li>
            <li class="section5" data-idx="5" authors="section5"><span>用户<br/>分析</span></li>
            <li class="section6" data-idx="6" authors="section6"><span>年度<br/>之最</span></li>
            <li class="section7" data-idx="7" authors="section7"><span>客户<br/>服务</span></li>
            <li class="section8" data-idx="8" authors="section8"><span>体验<br/>优化</span></li>
            <li class="section9" data-idx="9" authors="section9"><span>精彩<br/>活动</span></li>
            <li class="section10" data-idx="10" authors="section10"><span>足迹</span></li>
          <!--   <li class="over" data-idx="12" authors="over"><span>结束</span></li> -->
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
        /* section1 上半年业绩
        -----------------------------------------------------------------------------------------*/
        //交易金额 单位 （元）
        var data_2_1 = {
        	title:"交易金额 单位（亿元）",
        	data:[{
		        	name:"2017年1月",
					value:5.61,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[0]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[0]
		        		}
		        	}
		        },{
		        	name:"2017年2月",
					value:4.66,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[1]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[1]
		        		}
		        	}
		        },{
		        	name:"2017年3月",
					value:6.31,
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
		        	name:"2017年4月",
					value:6.30,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[3]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[3]
		        		}
		        	}
		        },{
		        	name:"2017年5月",
					value:6.74,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[4]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[4]
		        		}
		        	}
		        },{
		        	name:"2017年6月",
					value:7.88,
		        	itemStyle:{
		        		normal:{
		        			color:echarts2_1_color[5]
		        		},
		        		emphasis:{
		        			color:echarts2_1_color[5]
		        		}
		        	}
		    	}
		    ],
	        field : ['2017年1月','2017年2月','2017年3月','2017年4月','2017年5月','2017年6月']
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
    <script type="text/javascript">
    /* 渠道分析
    -----------------------------------------------------------------------------------------*/
    //APP、微信、PC成交笔数
        var echarts4_1_option = {
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
                        {value:63642, 
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
                            value:53293, 
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
                            value:5228, 
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
        /*用户分析数据
        -----------------------------------------------------------------------------------------*/
        //年龄、性别分布
        var data_6_1 = {
            title:"年龄、性别分布",
            data:{
                sex:[
                    {value:21732, name:'女'},
                    {value:16964, name:'男'}
                ],
                age:[
                    {value:6813, name:'18-29岁'},
                    {value:7912, name:'30-39岁'},
                    {value:8390, name:'40-49岁'},
                    {value:6942, name:'50-59岁'},
                    {value:8616, name:'60岁以上'}
                ]
            },
            field:['18-29岁','30-39岁','40-49岁','50-59岁','60岁以上']
        }

        //人均投资额
        var yonghufenxi_data_3 = {
            title:'金额分布',
            //subtitle:"101501.22",
            data:[
                {value:13871, name:'1万以下'},
                {value:13651, name:'1-5万'},
                {value:3968, name:'5-10万'},
                {value:5792, name:'10-50万'},
                {value:1413, name:'50万以上'}
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
    <script type="text/javascript">
    /* section7 用户分析数据
    -----------------------------------------------------------------------------------------*/
    //10大投资人占比

    var data_6_5 = {
        title:'\r10大投资人\n金额之和占比',
        subtitle:"4.00%",//十大投资人占比
        data:[
            {value:149805100, name:'10大投资人金额'},
            {value:3598473567, name:'其他投资人金额'}
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
    <script type="text/javascript">
    /* section3 标的分析数据
    -----------------------------------------------------------------------------------------*/
    //借款期限
    var data_3_1 = {
        	title:"借款期限",
        	data:[ //借款期限
	            {value:256, name:'30天'},
	            {value:478, name:'1个月'},
	            {value:852, name:'2个月'},
	            {value:1668, name:'3个月'},
	            {value:162, name:'4个月'},
	            {value:11, name:'5个月'},
	            {value:1733, name:'6个月'},
	            {value:60, name:'12个月'}
	        ],
	        field : ['30天','1个月','2个月','3个月','4个月','5个月','6个月','12个月']
        }
    //标的类型
    var data_3_3 = {
    	title:"标的类型",
    	data :[
            {value:151, name:'供应贷'},
            {value:81, name:'员工贷'},
            {value:114, name:'实鑫车'},
            {value:183, name:'尊享汇'},
            {value:223, name:'新手汇'},
            {value:418, name:'汇保贷'},
            {value:2900, name:'汇典贷'},
            {value:521, name:'汇小贷'},
            {value:371, name:'汇房贷'},
            {value:4, name:'汇投资'},
            {value:21, name:'汇资产'},
            {value:60, name:'汇车贷'},
            
        ],
        field:['供应贷','员工贷','实鑫车','尊享汇','新手汇','汇保贷','汇典贷','汇小贷','汇房贷','汇投资','汇资产','汇车贷']
    }
    var echarts3_1 = echarts.init(document.getElementById('echarts3_1'));//借款期限
    var echarts3_3 = echarts.init(document.getElementById('echarts3_3'));//标的类型
    // 指定图表的配置项和数据
    var echarts3_1_option  = {
    	color:mainColor,
    	title:{
    		text:data_3_1.title,
    		textStyle:mainTextStyle,
    		left:135,
    		top:167
    	},
	    tooltip: {
	        trigger: 'item',
	        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        x: '73%',
	        y:'middle',
	        data:data_3_1.field,
	        textStyle:{
	        	color:"#fff"
	        }
	    },
	    series: [
	        {
	            name:data_3_1.title,
	            type:'pie',
	            label:{
	            	normal:{
		            	formatter: "{b}\n{d}%"
		            }
	            },
	            radius: ['40%', '55.5%'],
	            data:data_3_1.data,
	            center: ['35%', '50%']
	        }
	    ]
	};
	var echarts3_3_option  = {
		color:mainColor,
		title : {
    		text:data_3_3.title,
    		textStyle:mainTextStyle,
    		left:135,
    		top:167
    	},
	    tooltip: {
	        trigger: 'item',
	        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        x: '73%',
	        y:'middle',
	        data:data_3_3.field,
	        textStyle:{
	        	color:["#fff"]
	        }
	    },
	    series: [
	        {
	            name:data_3_3.title,
	            type:'pie',
	            radius: ['40%', '55.5%'],
	            label:{
	            	normal:{
		            	formatter: "{b}\n{d}%"
		            }
	            },
	            data:data_3_3.data,
	            center: ['35%', '50%']
	        }
	    ]
	};
    </script>
    <script type="text/javascript">
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
            anchors:['top','section1','section2','section3','section4','section5','section6','section7','section8','section9','section10','section10','over'],
            onLeave :function(index, nextIndex, direction){
                var idx = nextIndex-1;
                var anchors = $("#fullpage").children(".section").eq(idx).data("anchor");
                if(idx == 1){
                	var s1data1 = new CountUp("s1data1", 0, 14159276111.33, 2, 1.5, countUpOptions); //累计交易额(元)
					var s1data2 = new CountUp("s1data2", 0, 300997, 0, 1.5, countUpOptions);//平台注册人数(人)
					var s1data3 = new CountUp("s1data3", 0,469380546.75, 2, 1.5, countUpOptions);//累计赚取收益(元)
                    s1data1.start();
                    s1data2.start();
                    s1data3.start();
                }else if(idx == 2){
                	var s2data1 = new CountUp("s2data1", 0, 122163, 0, 1.5, countUpOptions);
					var s2data2 = new CountUp("s2data2", 0, 37.50, 2, 1.5, countUpOptions);
					var s2data3 = new CountUp("s2data3", 0, 9475.38, 2, 1.5, countUpOptions);
					var s2data4 = new CountUp("s2data4", 0, 73914, 0, 1.5, countUpOptions);
					var s2data5 = new CountUp("s2data5", 0, 17.18, 2, 1.5, countUpOptions); 
					var s2data8 = new CountUp("s2data8", 0, 7.88, 2, 1.5, countUpOptions);
					var s2data9 = new CountUp("s2data9", 0, 9.75, 2, 1.5, countUpOptions);
					s2data1.start();
					s2data2.start();
					s2data3.start();
					s2data4.start();
					s2data5.start();
					s2data8.start();
					s2data9.start();
					//初始化图表
					echarts2_1.setOption(echarts2_1_option);
                }else if(idx == 3){
                	echarts3_1.setOption(echarts3_1_option);
			        echarts3_3.setOption(echarts3_3_option);
					var s3data2 = new CountUp("s3data2", 0, 5288, 0, 1.5, countUpOptions);
					var s3data3 = new CountUp("s3data3", 0, 1861806200, 0, 1.5, countUpOptions);
					s3data2.start();
					s3data3.start();
                }else if(idx == 4){
                	var s4data1 = new CountUp("s4data1", 0, 53293, 0, 1.5, countUpOptions);
                    var s4data2 = new CountUp("s4data2", 0, 43.62, 2, 1.5, countUpOptions);
                    var s4data3 = new CountUp("s4data3", 0, 5228, 0, 1.5, countUpOptions);
                    var s4data4 = new CountUp("s4data4", 0, 4.28, 2, 1.5, countUpOptions);
                    var s4data5 = new CountUp("s4data5", 0, 63642, 0, 1.5, countUpOptions);
                    var s4data6 = new CountUp("s4data6", 0, 52.10, 2, 1.5, countUpOptions);
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
                	var s6data1 = new CountUp("s6data1", 0, 2248.80, 2, 1.5, countUpOptions);//投资金额最高(万元)
                    var s6data2 = new CountUp("s6data2", 0,  70.32, 2, 1.5, countUpOptions);//历史回报最高(万元)
                    var s6data3 = new CountUp("s6data3", 0, 1327, 0, 1.5, countUpOptions);//投资次数最多(次)
                    s6data1.start();
                    s6data2.start();
                    s6data3.start();
                    dangyuezhizui_1.setOption(dangyuezhizui_1_option);
                }else if(idx == 7){
                	var s8data1 = new CountUp("s8data1", 0, 32644, 0, 1.5, countUpOptions);//400电话数量(个)
                    var s8data2 = new CountUp("s8data2", 0, 37479, 0, 1.5, countUpOptions);//qq客服接待人数(人)
                    var s8data3 = new CountUp("s8data3", 0, 6856, 0, 1.5, countUpOptions);//微信客服接待人数(人)
                    var s8data4 = new CountUp("s8data4", 0, 79693, 0, 1.5, countUpOptions);//解决问题个数(个)
                    s8data1.start();
                    s8data2.start();
                    s8data3.start();
                    s8data4.start();
                }else if(idx == 8){
                	
                }
                if(idx == 12){
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
