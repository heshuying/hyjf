<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" >
    <title>2018年2月份报告 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.fullPage.css" />
	<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/report-tmp.css?version=${version}" /> --%>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/newweb-report.css" />
</head>
<style>
.second2018 .zuji .inner-bg {
	width: 814px;
	height: 706px;
	background: url(../img/report/zjbg12.png) 0 0 no-repeat;
}
.second2018 .zuji .inner-bg .item3 {
	margin-left: 360px;
}

.second2018 .zuji .inner-bg .item4 {
	margin-left: 285px;
}

.second2018 .zuji .inner-bg .item5 {
	margin-left: 220px;
}

.second2018 .zuji .inner-bg .item6 {
	margin-left: 175px;
}

.second2018 .zuji .inner-bg .item2 {
	float: left;
	margin-left: 295px;
}
.second2018 .section.zuji .inner-bg>div {
    width: 700px;
    float: left;
    color: #fff;
    margin-top: 12px;
    margin-bottom: 8px;
}
</style>
<body class="second2018">
    <div id="fullpage">
        <div class="section top" style="background-image: url(${ctx}/img/report/201802/top.jpg)">
            <div class="scroll"></div>
        </div>
        <div class="section section1 yejizonglan repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>业绩总览</div>
                <dl class="data1">
                    <dt>累计交易额（元）</dt>
                    <dd id="s1data1">20451969327.96</dd>
                </dl>
                <dl class="data2">
                    <dt>平台注册人数（人）</dt>
                    <dd id="s1data2">380482</dd>
                </dl>
                <dl class="data3">
                    <dt>累计赚取收益（元）</dt>
                    <dd id="s1data3">702895515.74</dd>
                </dl>
            </div>
        </div>
        <div class="section section2 dangyueyeji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>当月业绩</div>
                <div id="echarts2_1" style="width:520px;height: 220px;margin-top: 50px;float: left;"></div>
                <div class="dangyueyeji_right">
                    <p>本月成交<span class="data" id="s2data1">26333</span>笔</p>
                    <p>成交金额共计<span class="data" id="s2data2">6.64</span>亿元</p>
                    <p>同比增长<span class="data"><span id="s2data3">42.64</span>%</span></p>
                </div>
                <div class="clearfix"></div>
                <div id="echarts2_2" style="width:520px;height: 220px;margin-top: 50px;float: left;"></div>
                <div class="dangyueyeji_right">
                    <p>本月为用户赚取收益<span class="data" id="s2data4">2056.29</span>万元</p>
                    <p>同比增长<span class="data"><span id="s2data5">47.37</span>%</span></p>
                    <p>本月平均历史回报率<span class="data"><span id="s2data6">9.12</span>%</span></p>
                </div>
            </div>
        </div>
        
        <div class="section section3 qudaofenxi repeat">
             <div class="inner">
                <div class="title"><span class="line"></span>渠道分析</div>
                <div class="qudaofenxi_style2_left">
                    <div class="item-title"><img src="${ctx}/img/report/txt_app.png" alt=""></div>
                    <div class="item"><div class="icon phone"></div>本月成交<span id="s4data1">15965</span>笔，占比<span id="s4data2">60.63</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_weixin.png" alt=""></div>
                    <div class="item"><div class="icon wechat"></div>本月成交<span id="s4data3">1124</span>笔，占比<span id="s4data4">4.27</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_pc.png" alt=""></div>
                    <div class="item"><div class="icon desktop"></div>本月成交<span id="s4data5">9244</span>笔，占比<span id="s4data6">35.10</span>%</div>
                </div>
                <div id="echarts4_1" style="width: 500px;height: 520px;float: left;margin-top: 50px;"></div>
            </div>
        </div>

        <div class="section section4 yonghufenxi repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>用户分析</div>
                <div class="echarts-box">
                    <div id="yonghufenxi_1" style="width:500px;height: 400px;"></div>
                    <div id="yonghufenxi_3" style="width:500px;height: 400px;margin-left:0px;"></div>
                    <div id="yonghufenxi_4" style="width:500px;height: 400px;"></div>
                </div>
                
            </div>
        </div>
        <div class="section section5 dangyuezhizui repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>当月之最</div>
                <div class="dangyuezhizui-left-style2">
                    <div class="item-title">本月度十大投资人</div>
                    <div class="item-box">
                        <div class="item"><div class="name">达*</div><div class="value top3">434.74万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value top3">414.43万</div></div>
                        <div class="item"><div class="name">满*</div><div class="value top3">320.28万</div></div>
                        <div class="item"><div class="name">l*</div><div class="value">290.00万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">242.79万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">237.42万</div></div>
                        <div class="item"><div class="name">l*</div><div class="value">236.79万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">200.44万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">200.00万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">194.27万</div></div>
                    </div>
                </div>
                <div class="dangyuezhizui-right-style2">
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/zuiduojin.png" alt=""></div>
                        <p class="item-title-txt first">本月度投资金额最高</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data1">434.74</span>万元</span>
                            <span class="val2">达*</span>
                            <span class="val3">70岁</span>
                            <span class="val4">烟台市</span>
                        </p>
                    </div>
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/dayingjia.png" alt=""></div>
                        <p class="item-title-txt second">本月度历史回报最高</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data2">18.45</span>万元</span>
                            <span class="val2">l*</span>
                            <span class="val3">50岁</span>
                            <span class="val4">青岛市</span>
                        </p>
                    </div>
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/chahuoyue.png" alt=""></div>
                        <p class="item-title-txt third">本月度投资次数最多</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data3">69</span>次</span>
                            <span class="val2">h*</span>
                            <span class="val3">47岁</span>
                            <span class="val4">郑州市</span>
                        </p>
                    </div>
                    <div id="dangyuezhizui_1" style="width: 500px;height: 320px;"></div>
                </div>
                
            </div>
        </div>

        
        
        <div class="section section6 jingcaihuodong repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>精彩活动</div>
                <div class="inner-bg" style="width: 880px;margin-left: 200px;margin-right: auto;margin-top:130px">
                    <div class="item item1" style="width:780px;">
                        <div class="image"><img src="${ctx}/img/report/201802/active.png" alt=""></div>
                        <div class="con" style="height:90px;">压轴盛宴，礼见新年</div>
                        <div class="date">2018年2月15日至2018年3月15日</div>
                        <div class="join"></div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="section section7 zuji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

                <div class="inner-bg">
                	<div class="item1">
                		<div class="date">2018年2月4日 </div>
                		<div class="con">匠心筑梦，不负芳华 ——汇盈金服年度重大战略会议隆重召开!</div>
                	</div>
                	<div class="item2">
                		<div class="date">2018年2月5日 </div>
                		<div class="con">汇心聚力，盈放前行——汇盈金服年度巅峰盛典绚丽绽放！</div>
                	</div>
                	<div class="item3" style="margin-left:355px">
                		<div class="date">2018年2月12日</div>
                		<div class="con">惠众商务顾问（北京）有限公司荣登2017全球区块链企业专利百强榜!</div>
                	</div>
                	<div class="item4">
                		<div class="date">2018年2月12日</div>
                		<div class="con">惠众商务顾问（北京）有限公司喜获国家级高新技术企业认证！</div>
                	</div>
                	<div class="item5">
                		<div class="date">2018年2月13日</div>
                		<div class="con">2017中国网贷投资人满意品牌TOP100——汇盈金服荣耀登榜！</div>
                	</div>
                	<div class="item6">
                		<div class="date">2018年2月27日</div>
                		<div class="con"> 汇盈金服受邀出席2018新时代•新经济高峰论坛并喜获两大奖项!</div>
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
            <li class="section2" data-idx="2" authors="section2"><span>当月<br/>业绩</span></li>
            <li class="section3" data-idx="3" authors="section3"><span>渠道<br/>分析</span></li>
            <li class="section4" data-idx="4" authors="section4"><span>用户<br/>分析</span></li>
            <li class="section5" data-idx="5" authors="section5"><span>当月<br/>之最</span></li>
            <li class="section6" data-idx="6" authors="section6"><span>精彩<br/>活动</span></li>
            <li class="section7" data-idx="7" authors="section7"><span>足迹</span></li>
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

        var echarts2_1_color = ['#ed4544','#6474cb','#5efbe8','#eb9c1b','#d7ed1e','#1eccee'];
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
                    name:"2018年2月",
                    value:6.64,
                    itemStyle:{
                        normal:{
                            color:"#ed4544"
                        },
                        emphasis:{
                            color:"#ed4544"
                        }
                    }
                },{
                    name:"2017年2月",
                    value:4.66,
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
            field : ['2018年2月','2017年2月']
        };
        var data_2_2 = {
            title:"赚取收益 单位 （万元）",
            data:[{
                    name:"2018年2月",
                    value:2056.29,
                    itemStyle:{
                        normal:{
                            color:"#ed4544"
                        },
                        emphasis:{
                            color:"#ed4544"
                        }
                    }
                },{
                    name:"2017年2月",
                    value:1395.28,
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
            field : ['2018年2月','2017年2月']
        }
        var echarts2_1_option  = {
            color:mainColor,
            title:{
                text:data_2_1.title,
                textStyle:mainTextStyle,
                bottom:0,
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
                left: '30',
                right: '30',
                bottom: '20',
                top:'0',
                containLabel: true
            },
            legend: {
                data:data_2_1.field
            },
            yAxis: [
                {
                    type: 'category',
                    axisLabel:{
                        textStyle:{
                            color:"#fff",
                            fontWeight:"normal",
                            fontSize:16
                        }
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
            xAxis:{
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
                            position:"right",
                            textStyle:{
                                fontSize:14
                            }
                        }
                    },
                    barWidth:36,
                    data : data_2_1.data
                }
            ]
        };
        var echarts2_2_option  = {
            color:mainColor,
            title:{
                text:data_2_2.title,
                textStyle:mainTextStyle,
                bottom:0,
                left:"center"
            },
            tooltip : {
                trigger: 'axis',
                axisPointer : {            
                    type : 'shadow' 
                },
                formatter: "{b}<br/> {c}万元"
            },
            grid: {
                left: '30',
                right: '45',
                bottom: '20',
                top:'0',
                containLabel: true
            },
            legend: {
                data:data_2_2.field
            },
            yAxis: [
                {
                    type: 'category',
                    axisLabel:{
                        textStyle:{
                            color:"#fff",
                            fontWeight:"normal",
                            fontSize:16
                        }
                    },
                    data : data_2_2.field,
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
            xAxis:{
                show:false
            },
            series: [
                {
                    name:data_2_2.title,
                    type:'bar',
                    label: {
                        normal: {
                            show: true,
                            formatter: "{c}",
                            position:"right",
                            textStyle:{
                                fontSize:14
                            }
                        }
                    },
                    barWidth:36,
                    data : data_2_2.data
                }
            ]
        };
        var echarts2_1 = echarts.init(document.getElementById('echarts2_1'));//
        var echarts2_2 = echarts.init(document.getElementById('echarts2_2'));//
    
        
    </script>
    <script>
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
                //formatter: "{b} : {c} ({d}%)"
                	formatter : function(obj) {
						if (obj.data.value == "9244") { //hack 数字bug 
							obj.percent = "35.10";
						}
						return  obj.data.name + ":"+ obj.data.value + "(" + obj.percent + "%)";

					}
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
                        {value:9244, 
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
                            value:15965, 
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
                            value:1124, 
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
                            //formatter: "{b}\n{d}%",
                            formatter : function(obj) {
        						if (obj.data.value == "9244") { //hack 数字bug 
        							obj.percent = "35.10";
        						}
        						return  obj.data.name + "\n" + obj.percent + "%";

        					}
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
                    {value:8835, name:'女'},
                    {value:5310, name:'男'}
                ],
                age:[
                    {value:1892, name:'18-29岁'},
                    {value:2795, name:'30-39岁'},
                    {value:3618, name:'40-49岁'},
                    {value:2832, name:'50-59岁'},
                    {value:3008, name:'60岁以上'}
                ]
            },
            field:['18-29岁','30-39岁','40-49岁','50-59岁','60岁以上']
        }

        //人均投资额
        var yonghufenxi_data_3 = {
            title:'金额分布',
            //subtitle:"101501.22",
            data:[
                {value:4689, name:'1万以下'},
                {value:5674, name:'1-5万'},
                {value:1942, name:'5-10万'},
                {value:1713, name:'10-50万'},
                {value:127, name:'50万以上'}
            ],
            field:['1万以下','1-5万','5-10万','10-50万','50万以上']
        }
        var yonghufenxi_1 = echarts.init(document.getElementById('yonghufenxi_1'));
        var yonghufenxi_3 = echarts.init(document.getElementById('yonghufenxi_3'));
        // 指定图表的配置项和数据
        var yonghufenxi_1_option  = {
            color:mainColor,
            
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
                            //formatter: "{b}\n{d}%"
                            	formatter: function(obj){
                                    if(obj.data.value=="3008"){
                                    	obj.percent = "21.26";
                                    }
                                    return obj.data.name +"\n"+obj.percent+"%";
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
    var data_6_5 = {
        title:'\r10大投资人\n金额之和占比',
        subtitle:"4.17%",//十大投资人占比
        data:[
            {value:27711600, name:'10大投资人金额'},
            {value:636741770, name:'其他投资人金额'}
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
            anchors:['top','section1','section2','section3','section4','section5','section6','section7','section8','section9','section10','over'],
            onLeave :function(index, nextIndex, direction){
                var idx = nextIndex-1;
                var anchors = $("#fullpage").children(".section").eq(idx).data("anchor");
                if(idx == 1){
                    var s1data1 = new CountUp("s1data1", 0, 20451969327.96, 2, 1.5, countUpOptions); //累计交易额(元)
                    var s1data2 = new CountUp("s1data2", 0, 380482, 0, 1.5, countUpOptions);//平台注册人数(人)
                    var s1data3 = new CountUp("s1data3", 0, 702895515.74, 2, 1.5, countUpOptions);//累计赚取收益(元)
                    s1data1.start();
                    s1data2.start();
                    s1data3.start();
                }else if(idx == 2){
                    var s2data1 = new CountUp("s2data1", 0, 26333, 0, 1.5, countUpOptions);
                    var s2data2 = new CountUp("s2data2", 0, 6.64, 2, 1.5, countUpOptions);
                    var s2data3 = new CountUp("s2data3", 0, 42.64, 2, 1.5, countUpOptions);
                    var s2data4 = new CountUp("s2data4", 0, 2056.29, 2, 1.5, countUpOptions);
                    var s2data5 = new CountUp("s2data5", 0, 47.37, 2, 1.5, countUpOptions);
                    var s2data6 = new CountUp("s2data6", 0, 9.12, 2, 1.5, countUpOptions);
                    s2data1.start();
                    s2data2.start();
                    s2data3.start();
                    s2data4.start();
                    s2data5.start();
                    s2data6.start();
                    // //初始化图表
                    echarts2_1.setOption(echarts2_1_option);
                    echarts2_2.setOption(echarts2_2_option);
                }else if(idx == 3){
                    var s4data1 = new CountUp("s4data1", 0, 15965, 0, 1.5, countUpOptions);
                    var s4data2 = new CountUp("s4data2", 0, 60.63, 2, 1.5, countUpOptions);
                    var s4data3 = new CountUp("s4data3", 0, 1124, 0, 1.5, countUpOptions);
                    var s4data4 = new CountUp("s4data4", 0, 4.27, 2, 1.5, countUpOptions);
                    var s4data5 = new CountUp("s4data5", 0, 9244, 0, 1.5, countUpOptions);
                    var s4data6 = new CountUp("s4data6", 0, 35.10, 2, 1.5, countUpOptions);
                    s4data1.start();
                    s4data2.start();
                    s4data3.start();
                    s4data4.start();
                    s4data5.start();
                    s4data6.start();
                    echarts4_1.setOption(echarts4_1_option);
                }else if(idx == 4){
                    yonghufenxi_1.setOption(yonghufenxi_1_option);
                    yonghufenxi_3.setOption(yonghufenxi_3_option);
                }else if(idx == 5){
                    var s6data1 = new CountUp("s6data1", 0, 434.74, 2, 1.5, countUpOptions);//当月投资金额最高(万元)
                    var s6data2 = new CountUp("s6data2", 0, 18.45, 2, 1.5, countUpOptions);//当月投资次数最多(次)
                    var s6data3 = new CountUp("s6data3", 0, 69, 0, 1.5, countUpOptions);//当月历史回报最高(万元)
                    s6data1.start();
                    s6data2.start();
                    s6data3.start();
                    dangyuezhizui_1.setOption(dangyuezhizui_1_option);
                }else if(idx == 6){

                }else if(idx == 7){
                    
                }
                if(idx == 8){
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
