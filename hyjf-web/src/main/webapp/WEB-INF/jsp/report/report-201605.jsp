<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>2016年五月份报告 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/jquery.fullPage.css" />
    	<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/report.css?version=${version}" /> --%>
    	<link rel="stylesheet" type="text/css" href="${ctx}/css/new-report.css" />
	</head>

<body>
    <div id="fullpage">
        <%-- <div class="section top" style="background-image: url(${ctx}/img/report/report_201605_top.jpg);">
            <div class="inner">
                <div class="date">
                    2016 年 5 月
                </div>
                <div class="toptitle">
                    运营报告
                </div>
                <div class="clearfix"></div>
                <div class="scroll"></div>
            </div>
        </div> --%>
        <div class="section top" style="background-image: url(${ctx}/img/report/report_201605_top.jpg?v=20180323);">
            <div class="scroll" style="background-position:0 0;padding-right: 94px;"></div>
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
                	<p>当月预期平均收益率 <span class="data"><span id="s2data1">11.83</span>%</span></p>
	                <p>当月充值 <span class="data" id="s2data2"></span> 笔，共 <span class="data"><span id="s2data3">4.24</span>亿</span> 元</p>
	                <!-- <p>当月提现 <span class="data" id="s2data4">6020</span> 笔，共 <span class="data"><span id="s2data5">3.70</span>亿</span> 元</p> -->
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
                		<div><span class="field field1">借款总金额</span>  <span class="data"><span id="s3data1">410,545,000</span> 元</span></div>
                		<div><span class="field field2">发标个数</span>  <span class="data"><span id="s3data2">337</span>个</span></div>
                	</div>
                </div>

            </div>
        </div>
        <div class="section section4 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>风险防控</div>
                <dl class="data1">
                    <dt>累计待还金额（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data1">1,352,649,281</span></dd>
                </dl>
                <dl class="data2">
                    <dt>当月新增待还金额（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data2">391,753,590</span></dd>
                </dl>
                <dl class="data3">
                    <dt>风险保证金（元）</dt>
                    <dd><div class="dataimg"></div><span id="s4data3">38,952,364</span></dd>
                </dl>
            </div>
        </div>
        <div class="section section5 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>债权转让</div>
                <dl class="data1">
                    <dt><div class="dataimg"></div>债转成交金额（元）</dt>
                    <dd id="s5data1">7,028,786</dd>
                    <dd class="copy" id="s5data1_c">7,028,786</dd>
                </dl>
                <dl class="data2">
                    <dt><div class="dataimg"></div>成功转让笔数（笔）</dt>
                    <dd id="s5data2">221</dd>
                    <dd class="copy" id="s5data2_c">221</dd>
                </dl>
                <dl class="data3">
                    <dt><div class="dataimg"></div>参与转让人数（人）</dt>
                    <dd><span class="field">发起:</span><span id="s5data3">147</span>  <span class="field">承接:</span><span id="s5data3_1">135</span></dd>
                    <dd class="copy"><span class="field">发起:</span><span id="s5data3_c">147</span>   <span class="field">承接:</span><span id="s5data3_1_c">135</span></dd>
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
								<td class="value"><span id="s7data1">536.25</span> 万元</td>
								<td class="name">达*</td>
								<td class="field">68岁</td>
							</tr>
							<tr class="top2">
								<td class="field"><img src="${ctx}/img/report/top2.png" alt="">本月投资次数最多</td>
								<td class="value"><span id="s7data2">112</span>次</td>
								<td class="name">x*</td>
								<td class="field">31岁</td>
							</tr>
							<tr class="top3">
								<td class="field"><img src="${ctx}/img/report/top3.png" alt="">本月历史回报最高</td>
								<td class="value"><span id="s7data3">22.13</span>万元</td>
								<td class="name">达*</td>
								<td class="field">68岁</td>
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
                	<div class="item1"><span id="s8data1">2743</span> <span class="field">个</span></div>
                	<div class="item2"><span id="s8data2">4966</span> <span class="field">人</span></div>
                	<div class="item3"><span id="s8data3">963</span> <span class="field">人</span></div>
                	<div class="item4"><span id="s8data4">8214</span> <span class="field">个</span></div>
                </div>

            </div>
        </div>
        <div class="section section9 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>精彩活动</div>

                <div class="inner-bg">
                	<div class="active-img"><img src="${ctx}/img/report/f1.png" alt=""></div>
                	<div class="active-main">
                		<p>2016年5月活动参与人数 : <span id="s9data1">7681</span>     </p>
	                	<p>各等级红包领取人数</p>
	                	<table cellpadding="0" cellspacing="0" border="0">
	                		<tr class="item1">
	                			<td>60元</td>
	                			<td align="right"><span id="s9data2">1005</span>人</td>
	                		</tr>
	                		<tr class="item2">
	                			<td>150元</td>
	                			<td align="right"><span id="s9data3">930</span>人</td>
	                		</tr>
	                		<tr class="item3">
	                			<td>480元</td>
	                			<td align="right"><span id="s9data4">144</span>人</td>
	                		</tr>
	                		<tr class="item4">
	                			<td>1000元</td>
	                			<td align="right"><span id="s9data5">36</span>人</td>
	                		</tr>
	                		<tr class="item5">
	                			<td>1400元</td>
	                			<td align="right"><span id="s9data6">12</span>人</td>
	                		</tr>
	                	</table>
	                	<table cellpadding="0" cellspacing="0" border="0" class="last">
	                		<tr class="item6">
	                			<td>1800元</td>
	                			<td align="right"><span id="s9data7">8</span>人</td>
	                		</tr>
	                		<tr class="item7">
	                			<td>2300元 </td>
	                			<td align="right"><span id="s9data8">6</span>人</td>
	                		</tr>
	                		<tr class="item8">
	                			<td>3000元 </td>
	                			<td align="right"><span id="s9data9">16</span>人</td>
	                		</tr>
	                		<tr class="item9">
	                			<td>3000元以上 </td>
	                			<td align="right"><span id="s9data10">13</span>人</td>
	                		</tr>
	                	</table>
                	</div>
                </div>
            </div>
        </div>

        <div class="section section10 repeat">
            <div class="inner">
                <div class="title"><span class="line"></span>足迹</div>

                <div class="inner-bg">
                	<div class="item1">
                		<div class="date">2016年5月14日</div>
                		<div class="con">惠众与美国上市公司正式签署并购重组协议  推动创新金融在资本市场发展</div>
                	</div>
                	<div class="item2">
                		<div class="date">2016年5月13日</div>
                		<div class="con">汇盈金服受邀参加第三届中国互联网金融创新大会</div>
                	</div>
                	<div class="item3">
                		<div class="date">2016年5月6日</div>
                		<div class="con">汇盈金服走入潍坊五村 关爱行动温暖残疾人</div>
                	</div>
                </div>
            </div>
        </div>
        <div class="section section11 over">
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
		        	name:"2016年4月",
					value:2.33,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[0]
		        		},
		        		emphasis:{
		        			color:secondColor[0]
		        		}
		        	}
		        },{
		        	name:"2015年5月",
					value:3.81,
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
					value:3.91,
		        	itemStyle:{
		        		normal:{
		        			color:secondColor[2]
		        		},
		        		emphasis:{
		        			color:secondColor[2]
		        		}
		        	}
		    }],
	        field : ['2016年4月','2015年5月','2016年5月']
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
		        	name:"2015年5月",
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
	        field:['2016年 4月','2015年 5月','2016年 5月']
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
		        	name:"2015年5月",
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
	        field:['2016年 4月','2015年 5月','2016年 5月']
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
	            {value:61, name:'1个月'},
	            {value:111, name:'2个月'},
	            {value:69, name:'3个月'},
	            {value:83, name:'6个月'},
	            {value:0, name:'12个月'}
	        ],
	        field : ['1个月','2个月','3个月','6个月','12个月']
        }
        //借款金额
        var data_3_2 = {
        	title:"借款金额",
        	data:[
	            {value:15, name:'10万以下'},
	            {value:24, name:'10-100万'},
	            {value:285, name:'100-1000万'},
	            {value:0, name:'1000万及以上'}
	        ],
	        field:['10万以下','10-100万','100-1000万','1000万及以上']
        }
        //标的类型
        var data_3_3 = {
        	title:"标的类型",
        	data :[
	            {value:192, name:'汇典贷'},
	            {value:35, name:'汇小贷'},
	            {value:32, name:'汇保贷'},
	            {value:27, name:'汇房贷'},
	            {value:3, name:'供应贷'},
	            {value:24, name:'汇车贷'},
	            {value:11, name:'汇资产'},
	            {value:0, name:'汇消费'}
	        ],
	        field:['汇典贷','汇小贷','汇保贷','汇房贷','供应贷','汇车贷','汇资产','汇消费']
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
		        formatter: "{a} <br/>{b}: {c}({d}%)"
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
		        formatter: "{a} <br/>{b}: {c}({d}%)"
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
		        formatter: "{a} <br/>{b}: {c}({d}%)"
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
	                {value:1479, name:'女'},
	                {value:1634, name:'男'}
	            ],
        		age:[
	                {value:705, name:'90后'},
	                {value:816, name:'80后'},
	                {value:423, name:'70后'},
	                {value:366, name:'60后'},
	                {value:471, name:'60前'}
	            ]
        	},
        	field:['90后','80后','70后','60后','60前']
        }
        //终端分布
        var data_6_2 = {
        	title:"终端分布",
        	data:[
                {value:1606, name:'安卓APP'},
                {value:1084, name:'IOS App'},
                {value:7971, name:'PC'},
                {value:760, name:'微信'}
            ],
            field:['安卓APP','IOS App','PC','微信']
        }

        //人均投资额
        var data_6_3 = {
        	title:'人均投资额',
        	subtitle:"34014.8",
        	data:[
                {value:2928, name:'1万以下'},
                {value:2383, name:'1-5万'},
                {value:1552, name:'5-20万'},
                {value:288, name:'20-50万'},
                {value:64, name:'50-100万'},
                {value:16, name:'100-150万'},
                {value:26, name:'150万以上'}
            ],
            field:['1万以下','1-5万','5-20万','20-50万','50-100万','100-150万','150万以上']
        }
        var echarts6_1 = echarts.init(document.getElementById('echarts6_1'));
        var echarts6_2 = echarts.init(document.getElementById('echarts6_2'));
        var echarts6_3 = echarts.init(document.getElementById('echarts6_3'));
        // 指定图表的配置项和数据
        var echarts6_1_option  = {
        	color:mainColor,
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c}({d}%)"
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
		        formatter: "{a} <br/>{b}: {c}({d}%)"
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
        		left:"center",
        		top:140
        	},
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}元: {c}人({d}%)"
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
	        	name:"达*",
				value:536.25,
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
	        	name:"b*",
				value:514.25,
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
	        	name:"王*",
				value:417.36,
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
				value:400,
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
	        	name:"t*",
				value:370,
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
	        	name:"l*",
				value:350,
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
	        	name:"l*",
				value:347.07,
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
	        	name:"z*",
				value:336.81,
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
				value:326.82,
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
	        	name:"g*",
				value:279.99,
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
        field:['达*','b*','王*','l*','t*','l*','l*','z*','李*','g*']
    };

    var data_6_5 = {
    	title:'\r10大投资人\n金额之和占比',
    	subtitle:"9.79%",//十大投资人占比
    	data:[
            {value:38785935, name:'10大投资人金额'},
            {value:357585972, name:'其他投资人金额'}
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
		        formatter: "{b}<br/>{c}万元"
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
		        formatter: "{a} <br/>{b}: {c}({d}%)"
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
	        anchors:['top','section1','section2','section3','section4','section5','section6','section7','section8','section9','section10','section11'],
	        onLeave :function(index, nextIndex, direction){
	        	var idx = nextIndex-1;
	        	var anchors = $("#fullpage").children(".section").eq(idx).data("anchor");
	        	if(idx == 1){
					var s1data1 = new CountUp("s1data1", 0, 6894473204, 0, 1.5, countUpOptions); //累计交易额(元)
					var s1data2 = new CountUp("s1data2", 0, 166412, 0, 1.5, countUpOptions);//平台注册人数(人)
					var s1data3 = new CountUp("s1data3", 0, 234517314, 0, 1.5, countUpOptions);//累计赚取收益(元)
					var s1data4 = new CountUp("s1data4", 0, 38952364, 0, 1.5, countUpOptions);//风险保证金(元)
					s1data1.start();
					s1data2.start();
					s1data3.start();
					s1data4.start();
	        	}else if(idx == 2){
					var s2data1 = new CountUp("s2data1", 0, 11.83, 2, 1.5, countUpOptions);//当月预期平均收益率(%)
					var s2data2 = new CountUp("s2data2", 0, 7241, 0, 1.5, countUpOptions);//当月充值笔数(笔)
					var s2data3 = new CountUp("s2data3", 0, 4.24, 2, 1.5, countUpOptions);//当月充值总金额(亿元)
					/* var s2data4 = new CountUp("s2data4", 0, 6020, 0, 1.5, countUpOptions);//当月提现笔数(笔)
					var s2data5 = new CountUp("s2data5", 0, 3.70, 2, 1.5, countUpOptions);//当月提现总金额(亿元) */
					s2data1.start();
					s2data2.start();
					s2data3.start();
					/* s2data4.start();
					s2data5.start(); */
					//初始化图表
					echarts2_1.setOption(echarts2_1_option);
			        echarts2_2.setOption(echarts2_2_option);
			        echarts2_3.setOption(echarts2_3_option);
	        	}else if(idx == 3){
	        		echarts3_1.setOption(echarts3_1_option);
			        echarts3_2.setOption(echarts3_2_option);
			        echarts3_3.setOption(echarts3_3_option);
	        	}else if(idx == 4){
					var s4data1 = new CountUp("s4data1", 0, 1352649281, 0, 1.5, countUpOptions);//累计待还金额(元)
					var s4data2 = new CountUp("s4data2", 0, 391753590, 0, 1.5, countUpOptions);//当月新增待还金额(元)
					var s4data3 = new CountUp("s4data3", 0, 38952364, 0, 1.5, countUpOptions);//风险保证金(元)
					s4data1.start();
					s4data2.start();
					s4data3.start();
	        	}else if(idx == 5){
					var s5data1 = new CountUp("s5data1", 0, 7028786, 0, 1.5, countUpOptions);//债转成交金额(元)
					var s5data1_c = new CountUp("s5data1_c", 0, 7028786, 0, 1.5, countUpOptions);//债转成交金额(元)副本
					var s5data2 = new CountUp("s5data2", 0, 221, 0, 1.5, countUpOptions);//成功转让笔数 (笔)
					var s5data2_c = new CountUp("s5data2_c", 0, 221, 0, 1.5, countUpOptions);//成功转让笔数 (笔)副本
					var s5data3 = new CountUp("s5data3", 0, 147, 0, 1.5, countUpOptions);//参与转让人数(人) 发起
					var s5data3_c = new CountUp("s5data3_c", 0, 147, 0, 1.5, countUpOptions);//参与转让人数(人) 发起 副本
					var s5data3_1 = new CountUp("s5data3_1", 0, 135, 0, 1.5, countUpOptions);//参与转让人数(人) 承接
					var s5data3_1_c = new CountUp("s5data3_1_c", 0, 135, 0, 1.5, countUpOptions);//参与转让人数(人) 承接 副本
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
					var s7data1 = new CountUp("s7data1", 0, 536.25, 2, 1.5, countUpOptions);//本月投资金额最高(万元)
					var s7data2 = new CountUp("s7data2", 0, 122, 0, 1.5, countUpOptions);//本月投资次数最多(次)
					var s7data3 = new CountUp("s7data3", 0, 22.13, 2, 1.5, countUpOptions);//本月历史回报最高(万元)
					s7data1.start();
					s7data2.start();
					s7data3.start();
					echarts6_4.setOption(echarts6_4_option);
					echarts6_5.setOption(echarts6_5_option);
	        	}else if(idx == 8){
					var s8data1 = new CountUp("s8data1", 0, 2743, 0, 1.5, countUpOptions);//400电话数量(个)
					var s8data2 = new CountUp("s8data2", 0, 4966, 0, 1.5, countUpOptions);//qq客服接待人数(人)
					var s8data3 = new CountUp("s8data3", 0, 963, 0, 1.5, countUpOptions);//微信客服接待人数(人)
					var s8data4 = new CountUp("s8data4", 0, 8214, 0, 1.5, countUpOptions);//解决问题个数(个)
					s8data1.start();
					s8data2.start();
					s8data3.start();
					s8data4.start();
	        	}else if(idx == 9){
					var s9data1 = new CountUp("s9data1", 0, 7681, 0, 1.5, countUpOptions);//2016年5月活动参与人数
					var s9data2 = new CountUp("s9data2", 0, 1005, 0, 1.5, countUpOptions);//各等级红包领取人数 60元 (人)
					var s9data3 = new CountUp("s9data3", 0, 930, 0, 1.5, countUpOptions);//各等级红包领取人数 150元 (人)
					var s9data4 = new CountUp("s9data4", 0, 144, 0, 1.5, countUpOptions);//各等级红包领取人数 480元 (人)
					var s9data5 = new CountUp("s9data5", 0, 36, 0, 1.5, countUpOptions);//各等级红包领取人数 1000元 (人)
					var s9data6 = new CountUp("s9data6", 0, 12, 0, 1.5, countUpOptions);//各等级红包领取人数 1400元 (人)
					var s9data7 = new CountUp("s9data7", 0, 8, 0, 1.5, countUpOptions);//各等级红包领取人数 1800元 (人)
					var s9data8 = new CountUp("s9data8", 0, 6, 0, 1.5, countUpOptions);//各等级红包领取人数 2300元 (人)
					var s9data9 = new CountUp("s9data9", 0, 16, 0, 1.5, countUpOptions);//各等级红包领取人数 3000元 (人)
					var s9data10 = new CountUp("s9data10", 0, 13, 0, 1.5, countUpOptions);//各等级红包领取人数 3000元以上 (人)
					s9data1.start();
					s9data2.start();
					s9data3.start();
					s9data4.start();
					s9data5.start();
					s9data6.start();
					s9data7.start();
					s9data8.start();
					s9data9.start();
					s9data10.start();
	        	}else if(idx == 11){
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
