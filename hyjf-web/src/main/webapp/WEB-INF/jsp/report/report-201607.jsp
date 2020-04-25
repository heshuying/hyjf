<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>2016年七月份报告 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.fullPage.css" />
    	<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/report.css?version=${version}" /> --%>
    	<link rel="stylesheet" type="text/css" href="${ctx}/css/new-report.css" />
	</head>
<body class="july">
    <div id="fullpage">
        <div class="section top" style="background-image: url(${ctx}/img/report/topbg_july.jpg?v=20171123);">
            <div class="inner">
                <div class="date" style="margin-top:425px">
                  <!--  2016 年 7 月-->
                </div>
                <div class="clearfix"></div>
                <div class="scroll"></div>
            </div>
        </div>
        <div class="section section1 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>业绩总览</div>
                <dl class="data1">
                    <dt>累计交易额（元）</dt>
                    <dd id="s1data1"></dd>
                </dl>
                <dl class="data2">
                    <dt>平台注册人数（人）</dt>
                    <dd id="s1data2"></dd>
                </dl>
                <dl class="data3">
                    <dt>累计赚取收益（元）</dt>
                    <dd id="s1data3"></dd>
                </dl>
                <dl class="data4">
                    <dt>风险保证金（元）</dt>
                    <dd id="s1data4"></dd>
                </dl>
            </div>
        </div>
        <div class="section section2 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>当月业绩</div>
                <div class="echarts-box">
	                <div id="echarts2_1"></div>
	            	<div id="echarts2_2"></div>
	            	<div id="echarts2_3"></div>
                </div>
                <div class="data-box">
                	<p>当月预期平均收益率 <span class="data"><span id="s2data1">10.30</span>%</span></p>
	                <p>当月充值 <span class="data" id="s2data2">8069</span> 笔，共 <span class="data"><span id="s2data3">1.65</span>亿</span> 元</p>
	                <!-- <p>当月提现 <span class="data" id="s2data4">5868</span> 笔，共 <span class="data"><span id="s2data5">1.43</span>亿</span> 元</p> -->
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
                		<div><span class="field field1">借款总金额</span>  <span class="data"><span id="s3data1">478,025,000</span> 元</span></div>
                		<div><span class="field field2">发标个数</span>  <span class="data"><span id="s3data2">423</span>个</span></div>
                	</div>
                </div>
                
            </div>
        </div>
        <div class="section section4 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>风险防控</div>
                <dl class="data1">
                    <dt>累计待还金额（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data1">1,464,070,330</span></dd>
                </dl>
                <dl class="data2">
                    <dt>当月新增待还金额（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data2">477,509,294</span></dd>
                </dl>
                <dl class="data3">
                    <dt>风险保证金（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data3">41,736,514</span></dd>
                </dl>
            </div>
        </div>
        <div class="section section5 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>债权转让</div>
                <dl class="data1">
                    <dt><div class="dataimg"></div>债转成交金额（元）</dt>
                    <dd id="s5data1">4,726,449.00</dd>
                    <dd class="copy" id="s5data1_c">4,726,449.00</dd>
                </dl>
                <dl class="data2">
                    <dt><div class="dataimg"></div>成功转让笔数（笔）</dt>
                    <dd id="s5data2">428</dd>
                    <dd class="copy" id="s5data2_c">428</dd>
                </dl>
                <dl class="data3">
                    <dt><div class="dataimg"></div>参与转让人数（人）</dt>
                    <dd><span class="field">发起:</span><span id="s5data3">142</span>  <span class="field">承接:</span><span id="s5data3_1">159</span></dd>
                    <dd class="copy"><span class="field">发起:</span><span id="s5data3_c">142</span>   <span class="field">承接:</span><span id="s5data3_1_c">159</span></dd>
                </dl>
            </div>
        </div>

        <div class="section section6 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>用户分析</div>

                <div class="echarts-box">
                	<div id="echarts6_1"></div>
                	<div id="echarts6_2"></div>
                	<div id="echarts6_3"></div>
                </div>
                
            </div>
        </div>
        <div class="section section6 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>用户分析</div>
				<div class="subtitle">本月十大投资人</div>
                <div class="echarts-box">
					<div id="echarts6_4"></div>
					<div class="clearfix"></div>
					<div id="echarts6_5"></div>
					<div class="echarts6_table">
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr class="top1">
								<td class="field"><img src="${ctx}/img/report/top1.png" alt="">本月投资金额最高</td>
								<td class="value"><span id="s7data1">830.12</span>万元</td>
								<td class="name">t*</td>
								<!--<td class="field">68岁</td>-->
							</tr>
							<tr class="top2">
								<td class="field"><img src="${ctx}/img/report/top2.png" alt="">本月投资次数最多</td>
								<td class="value"><span id="s7data2">145</span>次</td>
								<td class="name">x*</td>
								<!--<td class="field">31岁</td>-->
							</tr>
							<tr class="top3">
								<td class="field"><img src="${ctx}/img/report/top3.png" alt="">本月历史回报最高</td>
								<td class="value"><span id="s7data3">15.12</span>万元</td>
								<td class="name">l*</td>
								<!--<td class="field">68岁</td>-->
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
                	<div class="item1"><span id="s8data1">5917</span> <span class="field">个</span></div>
                	<div class="item2"><span id="s8data2">591</span> <span class="field">人</span></div>
                	<div class="item3"><span id="s8data3">89</span> <span class="field">人</span></div>
                	<div class="item4"><span id="s8data4">1382</span> <span class="field">个</span></div>
                </div>
                
            </div>
        </div>
         <div class="section section9 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>汇盈公益</div>

                <div class="inner-bg">
                	<div class="item item1">
                		<div class="num">1</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1106.png" alt=""></div>
                		<div class="date">2016年7月4日</div>
                		<div class="con">“七一”慰问送真情  汇盈金服关爱困难老党员</div>
                	</div>
                	<div class="item item2">
                		<div class="num">2</div>
                		<div class="image"><img src="${ctx}/img/report/201606/s1107.png" alt=""></div>
                		<div class="date">2016年7月12日</div>
                		<div class="con">【心系纳雍】汇盈金服情暖灾区</div>
                	</div>
                </div>
            </div>
        </div>
        <div class="section section10 over">
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
    		<!--<li class="section10" data-idx="10"></li>-->
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
			$('.section .inner').css("zoom",.8)
		}
    </script>
    
    <script>
    	//echarts默认设置
    	var mainColor = ['#5df8e4','#2ea8db','#6271c6','#d33434','#e34343','#d98932','#daae6d','#45b067','#066f28','#06732e'];
    	var secondColor = ['#ed4544','#6474cb','#5efbe7','#e0b36f','#ed4544','#e0b36f','#daae6d','#45b067','#066f28','#06732e'];
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
		        	name:"2016年7月",
					value:4.77,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[0]
		        		},
		        		emphasis:{
		        			color:secondColor[0]
		        		}
		        	}
		        },{
		        	name:"2016年6月",
					value:4.38,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[1]
		        		},
		        		emphasis:{
		        			color:secondColor[1]
		        		}
		        	}
		        },{
		        	name:"2015年7月",
					value:3.22,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[2]
		        		},
		        		emphasis:{
		        			color:secondColor[2]
		        		}
		        	}
		    }],
	        field : ['2016年7月','2016年6月','2015年7月']
        }
        //成交笔数 单位 （笔）
        var data_2_2 = {
        	title:"成交笔数 单位 （笔）",
        	data:[{
		        	name:"2016年7月",
					value:14036,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[0]
		        		},
		        		emphasis:{
		        			color:secondColor[0]
		        		}
		        	}
		        },{
		        	name:"2016年6月",
					value:12601,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[1]
		        		},
		        		emphasis:{
		        			color:secondColor[1]
		        		}
		        	}
		        },{
		        	name:"2015年7月",
					value:11207,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[2]
		        		},
		        		emphasis:{
		        			color:secondColor[2]
		        		}
		        	}
		    }],
	        field:['2016年 7月','2016年 6月','2015年7月']
        }
        //赚取收益 单位 （元）
        var data_2_3 = {
        	title:"赚取收益 单位（万元）",
        	data:[{
		        	name:"2016年7月",
					value:1636,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[0]
		        		},
		        		emphasis:{
		        			color:secondColor[0]
		        		}
		        	}
		        },{
		        	name:"2016年6月",
					value:1524,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[1]
		        		},
		        		emphasis:{
		        			color:secondColor[1]
		        		}
		        	}
		        },{
		        	name:"2015年7月",
					value:1284,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[2]
		        		},
		        		emphasis:{
		        			color:secondColor[2]
		        		}
		        	}
		    }],
	        field:['2016年 7月','2016年 6月','2015年 7月']
        }
        var echarts2_1_option  = {
        	color:mainColor,
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
		        bottom: '50',
		        top:'6%',
		        containLabel: true
		    },
		    legend: {
		        data:data_2_1.field,
		        left:0,
		        top:0,
		        textStyle:mainTextStyle
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
		                    position:"top"
		                }
		            },
		            barWidth:40,
		            data : data_2_1.data
		        }
		    ]
		};
		var echarts2_2_option  = {
        	color:mainColor,
        	title:{
        		text:data_2_2.title,
        		textStyle:mainTextStyle,
        		top:290,
        		left:"center"
        	},
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        },
		        formatter: "{b}<br/> {c}笔"
		    },
		    grid: {
		        left: '-15',
		        right: '0',
		        bottom: '50',
		        top:'6%',
		        containLabel: true
		    },
		    legend: {
		        data:data_2_2.field,
		        left:0,
		        top:0,
		        textStyle:mainTextStyle
		    },
		    xAxis: [
		        {
		            type: 'category',
		            axisLabel:{
		            	textStyle:mainTextStyle
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
		    yAxis:{
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
		                    position:"top"
		                }
		            },
		            barWidth:40,
		            data : data_2_2.data
		        }
		    ]
		};
		var echarts2_3_option  = {
        	color:mainColor,
        	title:{
        		text:data_2_3.title,
        		textStyle:mainTextStyle,
        		top:290,
        		left:"center"
        	},
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        },
		        formatter: "{b}<br/> {c}万元"
		    },
		    grid: {
		        left: '-15',
		        right: '0',
		        bottom: '50',
		        top:'6%',
		        containLabel: true
		    },
		    legend: {
		        data:data_2_3.field,
		        left:0,
		        top:0,
		        textStyle:mainTextStyle
		    },
		    xAxis: [
		        {
		            type: 'category',
		            axisLabel:{
		            	textStyle:mainTextStyle
		            },
		            data : data_2_3.field,
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
		            name:data_2_3.title,
		            type:'bar',
		            label: {
		                normal: {
		                    show: true,
		                    formatter: "{c}",
		                    position:"top"
		                }
		            },
		            barWidth:40,
		            data : data_2_3.data
		        }
		    ]
		};
        var echarts2_1 = echarts.init(document.getElementById('echarts2_1'));//
        var echarts2_2 = echarts.init(document.getElementById('echarts2_2'));//
        var echarts2_3 = echarts.init(document.getElementById('echarts2_3'));//
        

        /* section3 标的分析数据
        -----------------------------------------------------------------------------------------*/
        //借款期限
        var data_3_1 = {
        	title:"借款期限",
        	data:[ //借款期限
        	    {value:20,name:'30天'},
	            {value:77, name:'1个月'},
	            {value:83, name:'2个月'},
	            {value:124, name:'3个月'},
	            {value:1, name:'4个月'},
	            {value:92, name:'6个月'},
	            {value:26, name:'12个月'},
	        ],
	        field : ['30天','1个月','2个月','3个月','4个月','6个月','12个月']
        }
        //借款金额
        var data_3_2 = {
        	title:"借款金额",
        	data:[
	            {value:12, name:'10万以下'},
	            {value:70, name:'10-100万'},
	            {value:341, name:'100-1000万'},
	            {value:0, name:'1000万及以上'}
	        ],
	        field:['10万以下','10-100万','100-1000万','1000万及以上']
        }
        //标的类型
        var data_3_3 = {
        	title:"标的类型",
        	data :[
	            {value:14, name:'供应贷'},
	            {value:42, name:'汇保贷'},
	            {value:18, name:'汇车贷'},
	            {value:201, name:'汇典贷'},
	            {value:43, name:'汇房贷'},
	            {value:52, name:'汇小贷'},
	            {value:20, name:'汇资产'},
	            {value:20, name:'新手汇'},
	            {value:13, name:'尊享汇'}
	        ],
	        field:['供应贷','汇保贷','汇车贷','汇典贷','汇房贷','汇小贷','汇资产','新手汇','尊享汇']
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
	                {value:4563, name:'女'},
	                {value:3205, name:'男'}
	            ],
        		age:[
	                {value:156, name:'90后'},
	                {value:1562, name:'80后'},
	                {value:1425, name:'70后'},
	                {value:1560, name:'60后'},
	                {value:2516, name:'60前'}
	            ]
        	},
        	field:['90后','80后','70后','60后','60前']
        }
        //终端分布
        var data_6_2 = {
        	title:"终端分布",
        	data:[
                {value:2087, name:'安卓APP'},
                {value:1491, name:'IOS App'},
                {value:9915, name:'PC'},
                {value:543, name:'微信'}
            ],
            field:['安卓APP','IOS App','PC','微信']
        }

        //人均投资额
        var data_6_3 = {
        	title:'人均投资额',
        	subtitle:"61503.14",
        	data:[
                {value:2680, name:'1万以下'},
                {value:2954, name:'1-5万'},
                {value:972, name:'5-10万'},
                {value:1006, name:'10-50万'},
                {value:156, name:'50万以上'}
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
		            center: ['45%', '50%']
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
		            center: ['45%', '50%']
		        }
		    ]
		};
		var echarts6_2_option  = {
			color:mainColor,
			title : {
        		text:data_6_2.title,
        		textStyle:mainTextStyle,
        		left:185,
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
		            center: ['45%', '50%']
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
        		left:'center',
        		top:140
        	},
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c}人 ({d}%)"
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
    	title:"十大投资人",
    	data:[
	        {
	        	name:"t*",
				value:830.12,
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
	        	name:"l*",
				value:550.00,
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
	        	name:"z*",
				value:459.59,
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
	        	name:"李*",
				value:385.97,
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
	        	name:"l*",
				value:353.43,
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
	        	name:"李*",
				value:340.45,
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
	        	name:"王*",
				value:318.57,
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
	        	name:"澄*",
				value:288.22,
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
	        	name:"s*",
				value:274.50,
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
	        	name:"李*",
				value:273.23,
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
        field:['t*','l*','z*','李*','l*','李*','王*','澄*','s*','李*']
    };

    var data_6_5 = {
    	title:'\r10大投资人\n金额之和占比',
    	subtitle:"8.52%",//十大投资人占比
    	data:[
            {value:40741072, name:'10大投资人金额'},
            {value:437283928, name:'其他投资人金额'}
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
		        top:'3%',
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
		                    position:"top"
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
        		left:'center',
        		top:135
        	},
		    series: [
		        {
		            name:data_6_5.title,
		            type:'pie',
		            label:{
		            	normal:{
			            	formatter: "{b}\n{d}%"
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
	        anchors:['top','section1','section2','section3','section4','section5','section6','section7','section8','section9','section10'],
	        onLeave :function(index, nextIndex, direction){
	        	var idx = nextIndex-1;
	        	var anchors = $("#fullpage").children(".section").eq(idx).data("anchor");
	        	if(idx == 1){
					var s1data1 = new CountUp("s1data1", 0, 7863782576, 0, 1.5, countUpOptions); //累计交易额(元)
					var s1data2 = new CountUp("s1data2", 0, 179128, 0, 1.5, countUpOptions);//平台注册人数(人)
					var s1data3 = new CountUp("s1data3", 0, 268328207, 0, 1.5, countUpOptions);//累计赚取收益(元)
					var s1data4 = new CountUp("s1data4", 0, 41736514, 0, 1.5, countUpOptions);//风险保证金(元)
					s1data1.start();
					s1data2.start();
					s1data3.start();
					s1data4.start();
	        	}else if(idx == 2){
					var s2data1 = new CountUp("s2data1", 0, 10.3, 2, 1.5, countUpOptions);//当月预期平均收益率(%)
					var s2data2 = new CountUp("s2data2", 0, 8069, 0, 1.5, countUpOptions);//当月充值笔数(笔)
					var s2data3 = new CountUp("s2data3", 0, 1.65, 2, 1.5, countUpOptions);//当月充值总金额(亿元)
					/* var s2data4 = new CountUp("s2data4", 0, 5868, 0, 1.5, countUpOptions);//当月提现笔数(笔) */
					/* var s2data5 = new CountUp("s2data5", 0, 1.43, 2, 1.5, countUpOptions);//当月提现总金额(亿元) */
					s2data1.start();
					s2data2.start();
					s2data3.start();
					/* s2data4.start(); */
					/* s2data5.start(); */
					//初始化图表
					echarts2_1.setOption(echarts2_1_option);
			        echarts2_2.setOption(echarts2_2_option);
			        echarts2_3.setOption(echarts2_3_option);
	        	}else if(idx == 3){
	        		echarts3_1.setOption(echarts3_1_option);
			        echarts3_2.setOption(echarts3_2_option);
			        echarts3_3.setOption(echarts3_3_option);
	        	}else if(idx == 4){
					var s4data1 = new CountUp("s4data1", 0, 1464070330, 0, 1.5, countUpOptions);//累计待还金额(元)
					var s4data2 = new CountUp("s4data2", 0, 477509294, 0, 1.5, countUpOptions);//当月新增待还金额(元)
					var s4data3 = new CountUp("s4data3", 0, 41736514, 0, 1.5, countUpOptions);//风险保证金(元)
					s4data1.start();
					s4data2.start();
					s4data3.start();
	        	}else if(idx == 5){
					var s5data1 = new CountUp("s5data1", 0, 4726449, 0, 1.5, countUpOptions);//债转成交金额(元)
					var s5data1_c = new CountUp("s5data1_c", 0, 4726449, 0, 1.5, countUpOptions);//债转成交金额(元)副本
					var s5data2 = new CountUp("s5data2", 0, 428, 0, 1.5, countUpOptions);//成功转让笔数(笔)
					var s5data2_c = new CountUp("s5data2_c", 0, 428, 0, 1.5, countUpOptions);//成功转让笔数(笔)副本
					var s5data3 = new CountUp("s5data3", 0, 142, 0, 1.5, countUpOptions);//参与转让人数(人) 发起
					var s5data3_c = new CountUp("s5data3_c", 0, 142, 0, 1.5, countUpOptions);//参与转让人数(人) 发起 副本
					var s5data3_1 = new CountUp("s5data3_1", 0, 159, 0, 1.5, countUpOptions);//参与转让人数(人) 承接
					var s5data3_1_c = new CountUp("s5data3_1_c", 0, 159, 0, 1.5, countUpOptions);//参与转让人数(人) 承接 副本
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
					var s7data1 = new CountUp("s7data1", 0, 830.12, 2, 1.5, countUpOptions);//本月投资金额最高(万元)
					var s7data2 = new CountUp("s7data2", 0, 145, 0, 1.5, countUpOptions);//本月投资次数最多(次)
					var s7data3 = new CountUp("s7data3", 0, 15.12, 2, 1.5, countUpOptions);//本月历史回报最高(万元)
					s7data1.start();
					s7data2.start();
					s7data3.start();
					echarts6_4.setOption(echarts6_4_option);
					echarts6_5.setOption(echarts6_5_option);
	        	}else if(idx == 8){
					var s8data1 = new CountUp("s8data1", 0, 5917, 0, 1.5, countUpOptions);//400电话数量(个)
					var s8data2 = new CountUp("s8data2", 0, 591, 0, 1.5, countUpOptions);//qq客服接待人数(人)
					var s8data3 = new CountUp("s8data3", 0, 89, 0, 1.5, countUpOptions);//微信客服接待人数(人)
					var s8data4 = new CountUp("s8data4", 0, 1382, 0, 1.5, countUpOptions);//解决问题个数(个)
					s8data1.start();
					s8data2.start();
					s8data3.start();
					s8data4.start();
	        	}else if(idx == 10){
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
