<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>2016年上半年运营报告 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.fullPage.css" />
    	<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/report.css?version=${version}" /> --%>
    	<link rel="stylesheet" type="text/css" href="${ctx}/css/new-report.css" />
	</head>

<body class="june">
    <div id="fullpage">
        <div class="section top" style="background-image: url(${ctx}/img/report/201606/report_201606_top.jpg?v=20180323);">
            <div class="scroll"></div>
        </div>
        <div class="section section1 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>业绩总览</div>
                <dl class="data1">
                    <dt>累计交易额（元）</dt>
                    <dd id="s1data1">7,461,333,063</dd>
                </dl>
                <dl class="data2">
                    <dt>平台注册人数（人）</dt>
                    <dd id="s1data2">175905</dd>
                </dl>
                <dl class="data3">
                    <dt>累计赚取收益（元）</dt>
                    <dd id="s1data3">254,370,140</dd>
                </dl>
                <dl class="data4">
                    <dt>风险保证金（元）</dt>
                    <dd id="s1data4">40,957,549</dd>
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
                <p style="font-size: 18px;">2016年上半年平台累计成交<span id="s2data1" class="data">74286</span>笔，累计成交金额<span class="data"><span id="s2data2">24.93</span>亿</span>元，累计赚取收益<span class="data"><span id="s2data3" class="data">8783.37</span>万</span>元</p>

                <p style="font-size: 18px;">累计充值<span id="s2data4" class="data">58138</span>笔，共<span class="data"><span id="s2data5">10.23</span>亿</span>元<!-- ；累计提现<span id="s2data6" class="data">34103</span>笔，共<span class="data"><span id="s2data7">8.83</span>亿</span>元 --></p>

                <p style="font-size: 18px;text-align: center;">成交量最高单月为<span class="data">3</span>月，共成交<span class="data"><span id="s2data8">5.14</span>亿</span>元</p>
                <p style="font-size: 18px;text-align: center;">上半年预期平均收益率<span class="data"><span id="s2data9">11.88</span>%</span></p>

                </div>
            </div>
        </div>
        <div class="section section3 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>标的分析</div>

                <div class="echarts-box">
                	<div id="echarts3_1"></div>
                	<div id="echarts3_2"></div>
                	<div id="echarts3_3"></div>
                	<div class="data3_4">
                		<div><span class="field field1" style="line-height: 50px;">上半年借款总金额</span>  <span class="data" style="font-size: 34px;"><span id="s3data1">24.93</span>亿元</span></div>
                		<div><span class="field field2" style="line-height: 50px;">上半年发标个数</span>  <span class="data" style="font-size: 34px;"><span id="s3data2">2087</span>个</span></div>
                	</div>
                </div>

            </div>
        </div>
        <div class="section section4 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>风险防控</div>
                <dl class="data1">
                    <dt>累计待还金额（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data1">1418232556.12</span></dd>
                </dl>
                <dl class="data2">
                    <dt>上半年新增待还金额（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data2">1408712209.92</span></dd>
                </dl>
                <dl class="data3">
                    <dt>风险保证金（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data3">40,957,549</span></dd>
                </dl>
            </div>
        </div>
        <div class="section section5 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>债权转让</div>
                <dl class="data1">
                    <dt><div class="dataimg"></div>债转成交金额（元）</dt>
                    <dd id="s5data1">32328010</dd>
                    <dd class="copy" id="s5data1_c">32328010</dd>
                </dl>
                <dl class="data2">
                    <dt><div class="dataimg"></div>成功转让笔数（笔）</dt>
                    <dd id="s5data2">2951</dd>
                    <dd class="copy" id="s5data2_c">2951</dd>
                </dl>
                <dl class="data3">
                    <dt><div class="dataimg"></div>参与转让人数（人）</dt>
                    <dd><span class="field">发起:</span><span id="s5data3">586</span>  <span class="field">承接:</span><span id="s5data3_1">1897</span></dd>
                    <dd class="copy"><span class="field">发起:</span><span id="s5data3_c">147</span>   <span class="field">承接:</span><span id="s5data3_1_c">135</span></dd>
                </dl>
            </div>
        </div>

        <div class="section section6 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>用户分析</div>

                <div class="echarts-box">
                	<div id="echarts6_1" style="width:500px;"></div>
                	<div id="echarts6_2"></div>
                	<div id="echarts6_3" style="margin-left:230px;"></div>
                	<!-- <div class="data3_4">
                		<div>
                			<p style="color:#fff;">人均投资金额（元）</p>
                			<div style="font-size: 34px;">
                					<span id="s6data1">101501.22</span>
                			</div>
                		</div>
                	</div> -->
                </div>

            </div>
        </div>
        <div class="section section6 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>用户分析</div>
				<div class="subtitle">上半年十大投资人</div>
                <div class="echarts-box">
					<div id="echarts6_4"></div>
					<div class="clearfix"></div>
					<div id="echarts6_5"></div>
					<div class="echarts6_table">
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr class="top1">
								<td class="field"><img src="${ctx}/img/report/top1.png" alt="">上半年投资金额最高</td>
								<td class="value"><span id="s7data1">2970</span>万元</td>
								<td class="name">李*</td>
								<!-- <td class="field">68岁</td> -->
							</tr>
							<tr class="top2">
								<td class="field"><img src="${ctx}/img/report/top2.png" alt="">上半年投资次数最多</td>
								<td class="value"><span id="s7data2">758</span>次</td>
								<td class="name">x*</td>
								<!-- <td class="field">31岁</td> -->
							</tr>
							<tr class="top3">
								<td class="field"><img src="${ctx}/img/report/top3.png" alt="">上半年历史回报最高</td>
								<td class="value"><span id="s7data3">73.20</span>万元</td>
								<td class="name">达*</td>
								<!-- <td class="field">68岁</td> -->
							</tr>
						</table>
					</div>
                </div>

            </div>
        </div>

        <div class="section section8 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>客户服务</div>

                <div class="inner-bg">
                	<div class="item1"><span id="s8data1">20585</span> <span class="field">个</span></div>
                	<div class="item2"><span id="s8data2">4486</span> <span class="field">人</span></div>
                	<div class="item3"><span id="s8data3">22894</span> <span class="field">人</span></div>
                	<div class="item4"><span id="s8data4">48906</span> <span class="field">个</span></div>
                </div>

            </div>
        </div>
        <div class="section section9 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>体验优化</div>
                <div class="inner-bg">
					<div class="item item1">
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
					</div>
                </div>
            </div>
        </div>

        <div class="section section10 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>精彩活动</div>

                <div class="inner-bg">
                	<div class="item item1">
                		<div class="image"><img src="${ctx}/img/report/201606/s10i1.jpg" alt=""></div>
                		<div class="con">金玉满堂喜洋洋<br/>压岁钱驾到速来接驾！</div>
                		<div class="date">2016年2月1日至2016年2月29日</div>
                		<div class="join">参与人次   4611人</div>
                	</div>
                	<div class="item item2">
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
        <div class="section section11 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>汇盈公益</div>

                <div class="inner-bg">
                	<div class="item item1">
                		<div class="num">1</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1101.png" alt=""></div>
                		<div class="date">2016年2月4日</div>
                		<div class="con">关爱老人及助学金项目全面启动</div>
                	</div>
                	<div class="item item2">
                		<div class="num">2</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1102.png" alt=""></div>
                		<div class="date">2016年3月4日</div>
                		<div class="con">打造“互联网+金融+公益”全新模式——<br/>“雷锋日”温情满社区</div>
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
                	<div class="item item5">
                		<div class="num">5</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1105.png" alt=""></div>
                		<div class="date">2016年5月6日</div>
                		<div class="con">汇盈金服走入潍坊五村 关爱行动温暖残疾人</div>
                	</div>
                </div>
            </div>
        </div>
        <div class="section section12 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

                <div class="inner-bg">
                	<div class="item item1">
                		<div class="date">2016年1月18日  </div>
                		<div class="con">汇盈金服新版App正式上线</div>
                	</div>
                	<div class="item item2">
                		<div class="date">2016年1月30日  </div>
                		<div class="con">与亚交所就汇盈金服挂牌上市签订战略合作协议</div>
                	</div>
                	<div class="item item3">
                		<div class="date">2016年2月4日</div>
                		<div class="con">关爱老人及助学金项目全面启动</div>
                	</div>
                	<div class="item item4">
                		<div class="date">2016年3月4日</div>
                		<div class="con">与同济大学达成战略合作暨助学金签约仪式顺利举行</div>
                	</div>
                	<div class="item item5">
                		<div class="date">2016年3月11日</div>
                		<div class="con">与复旦大学达成战略合作暨助学金签约仪式顺利举行</div>
                	</div>
                </div>
            </div>
        </div>
        <div class="section section13 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>
                <div class="inner-bg">
                	<div class="item item1">
                		<div class="date">2016年4月10日</div>
                		<div class="con">新版网站正式上线  七大金融服务频道给您更多选择</div>
                	</div>
                	<div class="item item2">
                		<div class="date">2016年5月6日</div>
                		<div class="con">与美国上市公司Sino Fortune Holding Corporation正式签署并购重组协议</div>
                	</div>
                	<div class="item item3">
                		<div class="date">2016年5月17日</div>
                		<div class="con">“新手汇”产品正式上线</div>
                	</div>
                	<div class="item item4">
                		<div class="date">2016年6月7日</div>
                		<div class="con">“尊享汇”产品正式上线</div>
                	</div>
                	<div class="item item5">
                		<div class="date">2016年6月23日</div>
                		<div class="con">汇盈金服会员Club重磅上线 </div>
                	</div>
                </div>
            </div>
        </div>
        <div class="section over">
            <div class="inner">

            </div>
        </div>

    </div>

    <div class="right-nav">
    	<ul>
    		<li class="top active" data-idx="0"></li>
    		<li class="section1" data-idx="1"></li>
    		<li class="section2" data-idx="2"></li>
    		<li class="section3" data-idx="3"></li>
    		<li class="section4" data-idx="4"></li>
    		<li class="section5" data-idx="5"></li>
    		<li class="section6 section7" data-idx="6"></li>
    		<li class="section8" data-idx="8"></li>
    		<li class="section9" data-idx="9"></li>
    		<li class="section10" data-idx="10"></li>
    		<li class="section11" data-idx="11"></li>
    		<li class="section12 section13" data-idx="12"></li>
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

		if (Sys.ie || Sys.firefox || ua.indexOf("rv:11") > -1){
			$("dd.copy").hide();
		}
		if(document.documentElement.clientHeight<=900){
			$('.section .inner').css("zoom",.8);
		}
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
		        	name:"2016年1月",
					value:3.93,
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
					value:3.48,
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
					value:5.14,
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
					value:3.82,
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
					value:4.18,
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
					value:4.39,
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
	        field : ['2016年1月','2016年2月','2016年3月','2016年4月','2016年5月','2016年6月']
        }
        //成交笔数 单位 （笔）
        var data_2_2 = {
        	title:"成交笔数 单位（笔）",
        	data:[{
		        	name:"2016年4月",
					value:8767,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[0]
		        		},
		        		emphasis:{
		        			color:secondColor[0]
		        		}
		        	}
		        },{
		        	name:"2016年5月",
					value:11120,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[1]
		        		},
		        		emphasis:{
		        			color:secondColor[1]
		        		}
		        	}
		        },{
		        	name:"2016年5月",
					value:11318,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[2]
		        		},
		        		emphasis:{
		        			color:secondColor[2]
		        		}
		        	}
		    }],
	        field:['2016年 4月','2016年 5月','2016年 5月']
        }
        //赚取收益 单位 （元）
        var data_2_3 = {
        	title:"赚取收益 单位（万元）",
        	data:[{
		        	name:"2016年4月",
					value:1362,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[0]
		        		},
		        		emphasis:{
		        			color:secondColor[0]
		        		}
		        	}
		        },{
		        	name:"2016年5月",
					value:1362,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[1]
		        		},
		        		emphasis:{
		        			color:secondColor[1]
		        		}
		        	}
		        },{
		        	name:"2016年5月",
					value:1349,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[2]
		        		},
		        		emphasis:{
		        			color:secondColor[2]
		        		}
		        	}
		    }],
	        field:['2016年 4月','2016年 5月','2016年 5月']
        }


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
		        top:'0',
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

        /* section3 标的分析数据
        -----------------------------------------------------------------------------------------*/
        //借款期限
        var data_3_1 = {
        	title:"借款期限",
        	data:[ //借款期限
	            {value:19, name:'30天'},
	            {value:368, name:'1个月'},
	            {value:673, name:'2个月'},
	            {value:451, name:'3个月'},
	            {value:1, name:'4个月'},
	            {value:0, name:'5个月'},
	            {value:560, name:'6个月'},
	            {value:15, name:'12个月'}
	        ],
	        field : ['30天','1个月','2个月','3个月','4个月','5个月','6个月','12个月']
        }
        //借款金额
        var data_3_2 = {
        	title:"借款金额",
        	data:[
	            {value:86, name:'10万以下'},
	            {value:244, name:'10-100万'},
	            {value:1757, name:'100-1000万'},
	            {value:0, name:'1000万及以上'}
	        ],
	        field:['10万以下','10-100万','100-1000万','1000万及以上']
        }
        //标的类型
        var data_3_3 = {
        	title:"标的类型",
        	data :[
	            {value:19, name:'新手汇'},
	            {value:163, name:'汇保贷'},
	            {value:1193, name:'汇典贷'},
	            {value:247, name:'汇小贷'},
	            {value:126, name:'汇车贷'},
	            {value:208, name:'汇房贷'},
	            {value:0, name:'汇租赁'},
	            {value:18, name:'供应贷'},
	            {value:84, name:'汇资产'},
	            {value:1, name:'汇消费'},
	            {value:28, name:'尊享汇'}
	        ],
	        field:['新手汇','汇保贷','汇典贷','汇小贷','汇车贷','汇房贷','汇租赁','供应贷','汇资产','汇消费','尊享汇']
        }

        var echarts3_1 = echarts.init(document.getElementById('echarts3_1'));//借款期限
        var echarts3_2 = echarts.init(document.getElementById('echarts3_2'));//借款金额
        var echarts3_3 = echarts.init(document.getElementById('echarts3_3'));//标的类型
        // 指定图表的配置项和数据
        var echarts3_1_option  = {
        	color:mainColor,
        	title:{
        		text:data_3_1.title,
        		textStyle:mainTextStyle,
        		left:135,
        		top:150
        	},
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        x: 'right',
		        y:'middle',
		        data:data_3_1.field,
		        textStyle:{
		        	color:"#fff"
		        }
		    },
		    position:['0','0'],
		    series: [
		        {
		            name:data_3_1.title,
		            type:'pie',
		            label:{
		            	normal:{
			            	formatter: "{b}\n{d}%"
			            }
		            },
		            radius: ['40%', '55%'],
		            data:data_3_1.data,
		            center: ['35%', '50%']
		        }
		    ]
		};
		var echarts3_2_option  = {
			color:mainColor,
			title:{
        		text:data_3_2.title,
        		textStyle:mainTextStyle,
        		left:135,
        		top:150
        	},
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        x: 'right',
		        y:'middle',
		        data:data_3_2.field,
		        textStyle:{
		        	color:["#fff"]
		        }
		    },
		    series: [
		        {
		            name:'借款金额',
		            type:'pie',
		            radius: ['40%', '55%'],
		            label:{
		            	normal:{
			            	formatter: "{b}\n{d}%"
			            }
		            },
		            data:data_3_2.data,
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
        		top:150
        	},
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        x: 'right',
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
		            radius: ['40%', '55%'],
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
        /* section6 用户分析数据
        -----------------------------------------------------------------------------------------*/

        //年龄、性别分布
        var data_6_1 = {
        	title:"年龄、性别分布",
        	data:{
        		sex:[
	                {value:13411, name:'女'},
	                {value:11147, name:'男'}
	            ],
        		age:[
	                {value:3290, name:'90后'},
	                {value:5607, name:'80后'},
	                {value:4529, name:'70后'},
	                {value:4314, name:'60后'},
	                {value:6821, name:'60前'}
	            ]
        	},
        	field:['90后','80后','70后','60后','60前']
        }
        //终端分布
        var data_6_2 = {
        	title:"终端分布",
        	data:[
                {value:7169, name:'安卓APP'},
                {value:5375, name:'IOS APP'},
                {value:56503, name:'PC'},
                {value:5259, name:'微信'}
            ],
            field:['安卓APP','IOS APP','PC','微信']
        }

        //人均投资额
        var data_6_3 = {
        	title:'人均投资额',
        	subtitle:"101501.22",
        	data:[
                {value:11151, name:'1万以下'},
                {value:6575, name:'1-5万'},
                {value:2306, name:'5-10万'},
                {value:3574, name:'10-50万'},
                {value:957, name:'50万以上'}
            ],
            field:['1万以下','1-5万','5-10万','10-50万','50万以上']
        }
        var echarts6_1 = echarts.init(document.getElementById('echarts6_1'));
        var echarts6_2 = echarts.init(document.getElementById('echarts6_2'));
        var echarts6_3 = echarts.init(document.getElementById('echarts6_3'));
        // 指定图表的配置项和数据
        var echarts6_1_option  = {
        	color:mainColor,
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
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
		    position:['0','0'],
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
		            center: ['50%', '50%'],
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
		            center: ['50%', '50%']
		        }
		    ]
		};
		var echarts6_2_option  = {
			color:mainColor,
			title : {
        		text:data_6_2.title,
        		textStyle:mainTextStyle,
        		left:"center",
        		top:140
        	},
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        x: 'right',
		        y:'middle',
		        data:data_6_2.field,
		        textStyle:{
		        	color:["#fff"]
		        }
		    },
		    series: [
		        {
		            name:data_6_2.title,
		            type:'pie',
		            label:{
		            	normal:{
			            	formatter: "{b}\n{d}%"
			            }
		            },
		            radius: ['40%', '55%'],
		            data:data_6_2.data,
		            center: ['50%', '50%']
		        }
		    ]
		};
		var echarts6_3_option  = {
			color:mainColor,
			title : {
        		text:data_6_3.title,
        		subtext:data_6_3.subtitle,
        		textStyle:mainTextStyle,
        		subtextStyle:{
        			color:"#e0b36f",
        			fontSize:20
        		},
        		left:"center",
        		top:140
        	},
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}元: {c}人 ({d}%)"
		    },
		    legend: {
		        orient: 'vertical',
		        x: 'right',
		        y:'middle',
		        data:data_6_3.field,
		        textStyle:{
		        	color:["#fff"]
		        }
		    },
		    series: [
		        {
		            name:data_6_3.title,
		            type:'pie',
		            label:{
		            	normal:{
			            	formatter: "{b}\n{d}%"
			            }
		            },
		            radius: ['40%', '55%'],
		            data:data_6_3.data,
		            center: ['50%', '50%']
		        }
		    ]
		};

    </script>
    <script>
	/* section7 用户分析数据
    -----------------------------------------------------------------------------------------*/


	//十大投资人
    var data_6_4 = {
    	title:"上半年十大投资人",
    	data:[
	        {
	        	name:"李*",
				value:2970,
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
	        	name:"z*",
				value:2285,
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
	        	name:"l*",
				value:2011,
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
				value:1632,
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
	        	name:"王*",
				value:1542,
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
	        	name:"y*",
				value:1471,
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
	        	name:"t*",
				value:1372,
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
				value:1107,
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
	        	name:"1*",
				value:1060,
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
	        	name:"t*",
				value:1043,
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
        field:['李*','z*','l*','l*','王*','y*','t*','h*','1*','t*']
    };

    var data_6_5 = {
    	title:'\r10大投资人\n金额之和占比',
    	subtitle:"6.21%",//十大投资人占比
    	data:[
            {value:164930887, name:'10大投资人金额'},
            {value:2492818438, name:'其他投资人金额'}
        ]
    }
    var echarts6_4 = echarts.init(document.getElementById('echarts6_4'));
    var echarts6_5 = echarts.init(document.getElementById('echarts6_5'));

    var echarts6_4_option = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        },
		        formatter: "{b}<br/> {c}万元"
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        top:'6%',
		        containLabel: true
		    },
		    yAxis :{
	            type : 'value',
	            axisLabel:{
	            	show:false
	            },
	            axisLine:{
	            	show:false
	            },
	            splitLine:{
	            	show:false
	            },
	            axisTick:{
	            	show:false
	            }
		    },
		    xAxis :{
	            type : 'category',
	            axisTick : {show: false},
	            data : data_6_4.field,
	            axisLabel:{
	            	textStyle:{
	            		color:"#fff"
	            	}
	            },
	            axisLine:{
	            	lineStyle:{
	            		color:"#40adee",
	            		width:1
	            	}
	            },
	            splitLine:{
	            	show:false
	            }
		    },
		    series : [
		        {
		            name:'',
		            type:'bar',
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{c}万",
		                    position:"top",
		                    textStyle:{
		                    	fontSize:16
		                    }
		                }
		            },
		            barWidth:40,
		            data : data_6_4.data
		        }
		    ]
		};
		var echarts6_5_option  = {
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
        		top:135
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
	        anchors:['top','section1','section2','section3','section4','section5','section6','section7','section8','section9','section10','section11','section12','section13','over'],
	        onLeave :function(index, nextIndex, direction){
	        	var idx = nextIndex-1;
	        	var anchors = $("#fullpage").children(".section").eq(idx).data("anchor");
	        	if(idx == 1){
					var s1data1 = new CountUp("s1data1", 0, 7461333063, 0, 1.5, countUpOptions); //累计交易额(元)
					var s1data2 = new CountUp("s1data2", 0, 175905, 0, 1.5, countUpOptions);//平台注册人数(人)
					var s1data3 = new CountUp("s1data3", 0, 254370140, 0, 1.5, countUpOptions);//累计赚取收益(元)
					var s1data4 = new CountUp("s1data4", 0, 40957549, 0, 1.5, countUpOptions);//风险保证金(元)
					s1data1.start();
					s1data2.start();
					s1data3.start();
					s1data4.start();
	        	}else if(idx == 2){
					var s2data1 = new CountUp("s2data1", 0, 74286, 0, 1.5, countUpOptions);
					var s2data2 = new CountUp("s2data2", 0, 24.93, 2, 1.5, countUpOptions);
					var s2data3 = new CountUp("s2data3", 0, 8783.37, 2, 1.5, countUpOptions);
					var s2data4 = new CountUp("s2data4", 0, 58138, 0, 1.5, countUpOptions);
					var s2data5 = new CountUp("s2data5", 0, 10.23, 2, 1.5, countUpOptions);
					/* var s2data6 = new CountUp("s2data6", 0, 34103, 0, 1.5, countUpOptions);
					var s2data7 = new CountUp("s2data7", 0, 8.83, 2, 1.5, countUpOptions); */
					var s2data8 = new CountUp("s2data8", 0, 5.14, 2, 1.5, countUpOptions);
					var s2data9 = new CountUp("s2data9", 0, 11.88, 2, 1.5, countUpOptions);
					s2data1.start();
					s2data2.start();
					s2data3.start();
					s2data4.start();
					s2data5.start();
					/* s2data6.start();
					s2data7.start(); */
					s2data8.start();
					s2data9.start();
					//初始化图表
					echarts2_1.setOption(echarts2_1_option);
	        	}else if(idx == 3){
	        		echarts3_1.setOption(echarts3_1_option);
			        echarts3_2.setOption(echarts3_2_option);
			        echarts3_3.setOption(echarts3_3_option);
			        var s3data1 = new CountUp("s3data1", 0, 24.93, 2, 1.5, countUpOptions);
					var s3data2 = new CountUp("s3data2", 0, 2087, 0, 1.5, countUpOptions);
			        s3data1.start();
					s3data2.start();
	        	}else if(idx == 4){
					var s4data1 = new CountUp("s4data1", 0, 1418232556.12, 2, 1.5, countUpOptions);//累计待还金额(元)
					var s4data2 = new CountUp("s4data2", 0, 1408712209.92, 2, 1.5, countUpOptions);//上半年新增待还金额(元)
					var s4data3 = new CountUp("s4data3", 0, 40957549, 0, 1.5, countUpOptions);//风险保证金(元)
					s4data1.start();
					s4data2.start();
					s4data3.start();
	        	}else if(idx == 5){
					var s5data1 = new CountUp("s5data1", 0, 32328010, 0, 1.5, countUpOptions);//债转成交金额(元)
					var s5data1_c = new CountUp("s5data1_c", 0, 32328010, 0, 1.5, countUpOptions);//债转成交金额(元)副本
					var s5data2 = new CountUp("s5data2", 0, 2951, 0, 1.5, countUpOptions);//成功转让笔数 (笔)
					var s5data2_c = new CountUp("s5data2_c", 0, 2951, 0, 1.5, countUpOptions);//成功转让笔数 (笔)副本
					var s5data3 = new CountUp("s5data3", 0, 586, 0, 1.5, countUpOptions);//参与转让人数(人) 发起
					var s5data3_c = new CountUp("s5data3_c", 0, 586, 0, 1.5, countUpOptions);//参与转让人数(人) 发起 副本
					var s5data3_1 = new CountUp("s5data3_1", 0, 1897, 0, 1.5, countUpOptions);//参与转让人数(人) 承接
					var s5data3_1_c = new CountUp("s5data3_1_c", 0, 1897, 0, 1.5, countUpOptions);//参与转让人数(人) 承接 副本
					s5data1.start();
					s5data1_c.start();
					s5data2.start();
					s5data2_c.start();
					s5data3.start();
					s5data3_c.start();
					s5data3_1.start();
					s5data3_1_c.start();
	        	}else if(idx == 6){
	        		echarts6_1.setOption(echarts6_1_option);
			        echarts6_2.setOption(echarts6_2_option);
			        echarts6_3.setOption(echarts6_3_option);
	        	}else if(idx == 7){
					var s7data1 = new CountUp("s7data1", 0, 2970, 0, 1.5, countUpOptions);//上半年投资金额最高(万元)
					var s7data2 = new CountUp("s7data2", 0, 758, 0, 1.5, countUpOptions);//上半年投资次数最多(次)
					var s7data3 = new CountUp("s7data3", 0, 73.20, 2, 1.5, countUpOptions);//上半年历史回报最高(万元)
					s7data1.start();
					s7data2.start();
					s7data3.start();
					echarts6_4.setOption(echarts6_4_option);
					echarts6_5.setOption(echarts6_5_option);
	        	}else if(idx == 8){
					var s8data1 = new CountUp("s8data1", 0, 20585, 0, 1.5, countUpOptions);//400电话数量(个)
					var s8data2 = new CountUp("s8data2", 0, 22894, 0, 1.5, countUpOptions);//qq客服接待人数(人)
					var s8data3 = new CountUp("s8data3", 0, 4486, 0, 1.5, countUpOptions);//微信客服接待人数(人)
					var s8data4 = new CountUp("s8data4", 0, 48906, 0, 1.5, countUpOptions);//解决问题个数(个)
					s8data1.start();
					s8data2.start();
					s8data3.start();
					s8data4.start();
	        	}
	        	if(idx == 14){
	        		$(".right-nav").fadeOut(300);
	        	}else if(nav.is(":hidden")){
	        		$(".right-nav").fadeIn(300);
	        	}
	        	$(".right-nav ul li").removeClass("active");
	        	$(".right-nav ul li."+anchors).addClass("active");
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
