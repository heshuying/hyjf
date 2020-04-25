<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" >
    <title>2017年二月份报告 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.fullPage.css" />
	<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/report-tmp.css" /> --%>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/newweb-report.css" /> 
</head>
<body class="">
    <div id="fullpage">
        <div class="section top" style="background-image: url(${ctx}/img/report/201702/top.jpg?v=20171123);">
            <div class="scroll"></div>
        </div>
        <div class="section section1 yejizonglan repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>业绩总览</div>
                <dl class="data1">
                    <dt>累计交易额（元）</dt>
                    <dd id="s1data1">11428812767</dd>
                </dl>
                <dl class="data2">
                    <dt>平台注册人数（人）</dt>
                    <dd id="s1data2">210883</dd>
                </dl>
                <dl class="data3">
                    <dt>累计赚取收益（元）</dt>
                    <dd id="s1data3">382190331</dd>
                </dl>
                <dl class="data4">
                    <dt>风险保证金（元）</dt>
                    <dd id="s1data4">56672325</dd>
                </dl>
            </div>
        </div>
        <div class="section section2 dangyueyeji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>当月业绩</div>
                <div id="echarts2_1" style="width:520px;height: 220px;margin-top: 50px;float: left;"></div>
                <div class="dangyueyeji_right">
                    <p>本月成交<span class="data" id="s2data1">14009</span>笔</p>
                    <p>成交金额共计<span class="data" id="s2data2">4.66</span>亿元</p>
                    <p>同比增长<span class="data"><span id="s2data3">33.90</span>%</span></p>
                </div>
                <div class="clearfix"></div>
                <div id="echarts2_2" style="width:520px;height: 220px;margin-top: 50px;float: left;"></div>
                <div class="dangyueyeji_right">
                    <p>本月为用户赚取收益<span class="data" id="s2data4">1395.28</span>万元</p>
                    <p>同比增长<span class="data"><span id="s2data5">15.47</span>%</span></p>
                    <p>本月平均历史回报率<span class="data"><span id="s2data6">9.70</span>%</span></p>
                </div>
            </div>
        </div>
        
        <div class="section section3 qudaofenxi repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>渠道分析</div>
                <div class="qudaofenxi_style2_left">
                    <div class="item-title"><img src="${ctx}/img/report/txt_app.png" alt=""></div>
                    <div class="item"><div class="icon phone"></div>本月成交<span id="s4data1">5733</span>笔，占比<span id="s4data2">40.91</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_weixin.png" alt=""></div>
                    <div class="item"><div class="icon wechat"></div>本月成交<span id="s4data3">207</span>笔，占比<span id="s4data4">1.48</span>%</div>
                    <div class="item-title"><img src="${ctx}/img/report/txt_pc.png" alt=""></div>
                    <div class="item"><div class="icon desktop"></div>本月成交<span id="s4data5">8074</span>笔，占比<span id="s4data6">57.61</span>%</div>
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
                        <div class="item"><div class="name">b*</div><div class="value top3">511.97万</div></div>
                        <div class="item"><div class="name">l*</div><div class="value top3">481.00万</div></div>
                        <div class="item"><div class="name">H*</div><div class="value">383.34万</div></div>
                        <div class="item"><div class="name">l*</div><div class="value">344.72万</div></div>
                        <div class="item"><div class="name">李*</div><div class="value">250.23万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">202.00万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">200.00万</div></div>
                        <div class="item"><div class="name">李*</div><div class="value">167.01万</div></div>
                        <div class="item"><div class="name">h*</div><div class="value">163.41万</div></div>
                        <div class="item"><div class="name">c*</div><div class="value">162.00万</div></div>
                    </div>
                </div>
                <div class="dangyuezhizui-right-style2">
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/zuiduojin.png" alt=""></div>
                        <p class="item-title-txt first">本月度投资金额最高</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data1">511.97</span> 万元</span>
                            <span class="val2">b*</span>
                            <span class="val3">55岁</span>
                            <span class="val4">牡丹江市</span>
                        </p>
                    </div>
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/dayingjia.png" alt=""></div>
                        <p class="item-title-txt second">本月度历史回报最高</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data2">11.28</span> 万元</span>
                            <span class="val2">H*</span>
                            <span class="val3">45岁</span>
                            <span class="val4">常州市</span>
                        </p>
                    </div>
                    <div class="item">
                        <div class="item-title"><img src="${ctx}/img/report/chahuoyue.png" alt=""></div>
                        <p class="item-title-txt third">本月度投资次数最多</p>
                        <p class="item-value">
                            <span class="val1"><span id="s6data3">164</span>次</span>
                            <span class="val2">x*</span>
                            <span class="val3">33岁</span>
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

        
        
        <div class="section section6 jingcaihuodong repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>精彩活动</div>
                <div class="jchd-201702">
                      <img src="${ctx}/img/report/201702/201702_active.png" alt="">
                </div>
            </div>
        </div>
        
        <div class="section section7 zuji repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

                <div class="inner-bg" style="background:url(../img/report/s1702innerbg.png)">
                	<div class="item1">
                		<div class="date">2017年2月16日</div>
                		<div class="con">汇盈金服与上海衷和投资管理有限公司达成战略合作  资源渠道又拓宽</div>
                	</div>
                	<div class="item2">
                		<div class="date">2017年2月17日</div>
                		<div class="con">投之家莅临汇盈金服洽谈深度合作  初定战略合作框架</div>
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
                    name:"2016年2月",
                    value:3.48,
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
            field : ['2016年2月','2017年2月']
        };
        var data_2_2 = {
            title:"赚取收益 单位 （万元）",
            data:[{
                    name:"2016年2月",
                    value:1208.29,
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
            field : ['2016年2月','2017年2月']
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
                        {value:8074, 
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
                            value:5733, 
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
                            value:207, 
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
                    {value:5241, name:'女',total:278675100},
                    {value:3499, name:'男',total:179542900}
                ],
                age:[
                    {value:1367, name:'18-29岁'},
                    {value:1588, name:'30-39岁'},
                    {value:1978, name:'40-49岁'},
                    {value:1632, name:'50-59岁'},
                    {value:2175, name:'60岁以上'}
                ]
            },
            field:['18-29岁','30-39岁','40-49岁','50-59岁','60岁以上']
        }

        //人均投资额
        var yonghufenxi_data_3 = {
            title:'金额分布',
            //subtitle:"101501.22",
            data:[
                {value:2942, name:'1万以下'},
                {value:3855, name:'1-5万'},
                {value:988, name:'5-10万'},
                {value:854, name:'10-50万'},
                {value:101, name:'50万以上'}
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
                    if(obj.data.total){
                        console.log(obj);
                        return obj.seriesName +"<br/>"+obj.data.name+" "+obj.data.value+"("+obj.percent+"%) <br/>投资金额:"+obj.data.total;
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
                            formatter: "{b}\n{d}%"
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
        subtitle:"5.07",//十大投资人占比
        data:[
            {value:21826800, name:'10大投资人金额'},
            {value:408992200, name:'其他投资人金额'}
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
                    var s1data1 = new CountUp("s1data1", 0, 11428812767, 0, 1.5, countUpOptions); //累计交易额(元)
                    var s1data2 = new CountUp("s1data2", 0, 210883, 0, 1.5, countUpOptions);//平台注册人数(人)
                    var s1data3 = new CountUp("s1data3", 0, 382190331, 0, 1.5, countUpOptions);//累计赚取收益(元)
                    var s1data4 = new CountUp("s1data4", 0, 56672325, 0, 1.5, countUpOptions);//风险保证金(元)
                    s1data1.start();
                    s1data2.start();
                    s1data3.start();
                    s1data4.start();
                }else if(idx == 2){
                    var s2data1 = new CountUp("s2data1", 0, 14009, 0, 1.5, countUpOptions);
                    var s2data2 = new CountUp("s2data2", 0, 4.66, 2, 1.5, countUpOptions);
                    var s2data3 = new CountUp("s2data3", 0, 33.90, 2, 1.5, countUpOptions);
                    var s2data4 = new CountUp("s2data4", 0, 1395.28, 2, 1.5, countUpOptions);
                    var s2data5 = new CountUp("s2data5", 0, 15.47, 2, 1.5, countUpOptions);
                    var s2data6 = new CountUp("s2data6", 0, 9.70, 2, 1.5, countUpOptions);
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
                    var s4data1 = new CountUp("s4data1", 0, 5733, 0, 1.5, countUpOptions);
                    var s4data2 = new CountUp("s4data2", 0, 40.91, 2, 1.5, countUpOptions);
                    var s4data3 = new CountUp("s4data3", 0, 207, 0, 1.5, countUpOptions);
                    var s4data4 = new CountUp("s4data4", 0, 1.48, 2, 1.5, countUpOptions);
                    var s4data5 = new CountUp("s4data5", 0, 8074, 0, 1.5, countUpOptions);
                    var s4data6 = new CountUp("s4data6", 0, 57.61, 2, 1.5, countUpOptions);
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
                    var s6data1 = new CountUp("s6data1", 0, 511.97, 2, 1.5, countUpOptions);//当月投资金额最高(万元)
                    var s6data2 = new CountUp("s6data2", 0, 11.28, 2, 1.5, countUpOptions);//当月投资次数最多(次)
                    var s6data3 = new CountUp("s6data3", 0, 164, 0, 1.5, countUpOptions);//当月历史回报最高(万元)
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
