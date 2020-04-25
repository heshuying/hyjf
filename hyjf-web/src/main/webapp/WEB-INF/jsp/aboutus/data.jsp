<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>平台数据 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="team-banner about-banner">
        <jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
        <h2>平台数据</h2>
    </div>
    <div class="about-section">
        <div class="container-1020">
            <div class="report-box">
                <div class="hd clf">
                    <h5 class="fl">平台数据总览</h5>
                </div>
                <div class="bd clf">
                    <ul>
                        <li>
                            <h1 class="num">${tenderMoney }</h1>
                            <small>用户投资总额 (元)</small> </li>
                        <li>
                            <h1 class="num">${recoverInterest}</h1>
                            <small>用户赚取收益 (元)</small> </li>
                        
                    </ul>
                    <hr />
                    <div id="summary" style="height:250px;">
                    </div>
                </div>
            </div>
           
            <!--7日平台数据变化 open-->
            <div class="report-box">
                <div class="hd clf">
                    <h5 class="fl">7日平台数据变化</h5>
                    <small class="fr"><i>数据截止至：${newdate}</i></small> </div>
                <div class="bd clf">
                    <ul>
                        <li>
                            <h1 class="num">${tenderMoney7 }</h1>
                            <small>用户投资总额 (元)</small> </li>
                        <li>
                            <h1 class="num">${recoverInterest7}</h1>
                            <small>用户赚取收益 (元)</small> </li>
                        
                    </ul>
                </div>
            </div>
            <!--7日平台数据变化 end-->
            
            <!--7日平台数据变化 open-->
            <ol class="ipro-list clf">
                <li>
                    <div class="report-box">
                        <div class="hd clf">
                            <h5 class="fl">融资期限分布</h5>
                        </div>
                        <div class="bd clf">
                            <div id="project-maturity" style="height:220px"></div>
                        </div>
                    </div>
                </li>
                
                <li>
                    <div class="report-box">
                        <div class="hd clf">
                            <h5 class="fl">投资金额占比</h5>
                        </div>
                        <div class="bd clf">
                            <div id="invest-money" style="height:220px;"></div>
                        </div>
                    </div>
                </li>
                
            </ol>
            <!--7日平台数据变化 end-->
            
            <!-- <div class="report-box">
                <div class="hd clf">
                    <h5 class="fl">平台交易集中情况</h5>
                    <small class="fr"><i>数据截止至：2016-08-09 00:00:00</i></small>
                </div>
                <div class="bd clf">
                	<h4><center>前十大投资人</center></h4>
                </div>
                <div class="bd clf">
                	<ol class="first-list-list clf">
		                <li>
		                    <div id="first-ten" style="height:220px;"></div>
		                </li>
		                <li>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                </li>
		                <li>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">达*</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                	<p><span class="name">其他</span> <span class="total">5,055.26 万元</span> <span class="percent">50.08%</span></p>
		                </li>
                	</ol>
                </div>
            </div> -->
        </div>
    </div>
    <script>setActById("hdXXPL");</script>
    <script>setActById("subPTSJ");</script>
	<script>setActById("aboutData");</script>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/echarts.common.min.js"></script>
    <script type="text/javascript">
    require.config({
        paths: {
            echarts: '${cdn}/js/'
        }
    });
    require(['echarts', 'echarts/chart/bar'], function(ec) {
        var summary = ec.init(document.getElementById('summary'));
        var option = {
            color: ['#20a6e2', '#66CC66', '#FFD700'],
            tooltip: {
                trigger: 'axis',
                backgroundColor: 'rgba(0,0,0,0.5)',
                axisPointer: {
                    type: 'shadow'
                },
                textStyle: {
                    fontSize: 12
                }
            },
            legend: {
                data: ['汇直投', '汇转让', '汇添金'],
                textStyle: {
                    fontFamily: '微软雅黑, Arial, Verdana, sans-serif'
                },
                y: '5%',
            },
            grid: {
                x: '7%',
                y: '15%',
                x2: '1%',
                y2: '10%',
                x3: '1%',
                y3: '10%',
                borderWidth: 1,
                borderColor: '#eee'
            },
            toolbox: {
                show: false
            },
            calculable: true,
            xAxis: [{
                type: 'category',
                axisLine: {
                    lineStyle: {
                        color: '#999',
                        width: 1,
                        type: 'solid'
                    }
                },
                splitLine: {
                    lineStyle: {
                        color: '#eee',
                        width: 1,
                        type: 'solid'
                    }
                },
                axisLabel: {
                    textStyle: {
                        fontFamily: '微软雅黑, Arial, Verdana, sans-serif',
                        color: '#666'
                    }
                },
                data:${timelist }
            }],
            yAxis: [{
                type: 'value',
                axisLine: {
                    lineStyle: {
                        color: '#999',
                        width: 1,
                        type: 'solid'
                    }
                },
                splitLine: {
                    lineStyle: {
                        color: '#eee',
                        width: 1,
                        type: 'solid'
                    }
                },
                axisLabel: {
                    textStyle: {
                        fontFamily: '微软雅黑, Arial, Verdana, sans-serif',
                        color: '#666'
                    },
                    formatter: '{value} 万'
                },
                splitArea: {
                    show: true,
                    areaStyle: {
                        color: ['rgba(250,250,250,0.2)', 'rgba(200,200,200,0.2)', 'rgba(200,200,200,0.2)']
                    }
                }
            }],
            series: [{
                name: '汇直投',
                type: 'bar',
                stack: '堆积',
                data: ${tendermoneylist } 

            }, {
                name: '汇转让',
                type: 'bar',
                stack: '堆积',
                data: ${creditmoneylist } 

            }, {
                name: '汇添金',
                type: 'bar',
                stack: '堆积',
                data: ${htjmoneylist } 

            }],

        };
        summary.setOption(option)

    });

    


    require(['echarts', 'echarts/chart/pie'], function(ec) {
        var maturity = ec.init(document.getElementById('project-maturity'));
        var option = {
            color: ['#20a6e2', '#FFDC89', '#66CC66', '#F8585B', '#596373'],
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c}笔 ({d}%)",
                backgroundColor: 'rgba(0,0,0,0.5)',
                textStyle: {
                    fontSize: 12
                }
            },
            legend: {
                orient: 'vertical',
                textStyle: {
                    fontFamily: '微软雅黑, Arial, Verdana, sans-serif'
                },
                x: 'right',
                y: 'top',
                data: ['0 - 1个月', '1 - 3个月', '3 - 6个月', '6 - 12个月', '12个月以上']
            },
            toolbox: {
                show: false
            },
            calculable: true,
            series: [{
                name: '项目期限占比',
                type: 'pie',
                radius: [35, 80],
                center: ['40%', '50%'],
                roseType: 'radius',
                width: '100%',
                // for funnel
                max: 20,
                // for funnel
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        labelLine: {
                            show: false
                        }
                    },
                    emphasis: {
                        label: {
                            show: true,
                            textStyle: {
                                fontFamily: '微软雅黑, Arial, Verdana, sans-serif'
                            },
                        },
                        labelLine: {
                            show: true
                        }
                    }
                },
                data: [{
                    value: ${periodInfo.zeroone},
                    name: '0 - 1\u4e2a\u6708'
                }, {
                    value: ${periodInfo.onethree},
                    name: '1 - 3\u4e2a\u6708'
                }, {
                    value: ${periodInfo.threesex},
                    name: '3 - 6\u4e2a\u6708'
                }, {
                    value: ${periodInfo.sextw},
                    name: '6 - 12\u4e2a\u6708'
                }, {
                    value: ${periodInfo.tw},
                    name: '12\u4e2a\u6708\u4ee5\u4e0a'
                }]

            }, ]
        };

        maturity.setOption(option)
    });
    
    require(['echarts', 'echarts/chart/pie'], function(ec) {
        var money = ec.init(document.getElementById('invest-money'));
        var option = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)",
                backgroundColor: 'rgba(0,0,0,0.5)',
                textStyle: {
                    fontSize: 12
                }
            },
            legend: {
                orient: 'vertical',
                x: 'right',
                y: 'top',
                textStyle: {
                    fontFamily: '微软雅黑, Arial, Verdana, sans-serif'
                },
                data: ['1万以下', '1-5万', '5-10万', '10-50万', '50万以上']
            },
            toolbox: {
                show: false,

            },
            calculable: true,
            series: [{
                name: '投资金额',
                type: 'pie',
                radius: ['50%', '90%'],
                center: ['40%', '50%'],
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        labelLine: {
                            show: false
                        }
                    },
                    emphasis: {
                        label: {
                            show: false,
                            position: 'center',
                            textStyle: {
                                fontSize: '30',
                                fontWeight: 'bold'
                            }
                        }
                    }
                },
                data: [{
                    value: ${TendMoneyInfo.zeroone},
                    name: '1\u4e07\u4ee5\u4e0b'
                }, {
                    value: ${TendMoneyInfo.onefive},
                    name: '1-5\u4e07'
                }, {
                    value: ${TendMoneyInfo.fiveten},
                    name: '5-10\u4e07'
                }, {
                    value: ${TendMoneyInfo.tenfive},
                    name: '10-50\u4e07'
                }, {
                    value: ${TendMoneyInfo.five},
                    name: '50\u4e07\u4ee5\u4e0a'
                }]

            }]
        };
        money.setOption(option)
    });
    
    
    /* require(['echarts', 'echarts/chart/pie'], function(ec) {
        var money = ec.init(document.getElementById('first-ten'));
        var option = {
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c}万元 ({d}%)",
                backgroundColor: 'rgba(0,0,0,0.5)',
                textStyle: {
                    fontSize: 12
                }
            },
            legend: {
            	show:false,
                orient: 'vertical',
                x: 'right',
                y: 'top',
                textStyle: {
                    fontFamily: '微软雅黑, Arial, Verdana, sans-serif'
                },
                data: ['1万以下', '1-5万', '5-10万', '10-50万', '50万以上']
            },
            toolbox: {
                show: false,

            },
            calculable: true,
            series: [{
                name: '投资金额',
                type: 'pie',
                radius: ['50%', '90%'],
                center: ['40%', '50%'],
                itemStyle: {
                    normal: {
                        label: {
                            show: false
                        },
                        labelLine: {
                            show: false
                        }
                    },
                    emphasis: {
                        label: {
                            show: false,
                            position: 'center',
                            textStyle: {
                                fontSize: '30',
                                fontWeight: 'bold'
                            }
                        }
                    }
                },
                data: [{
                    value: 112,
                    name: '达*'
                }, {
                    value: 1170,
                    name: '1*'
                }, {
                    value: 157,
                    name: 'y*'
                }, {
                    value: 217,
                    name: 'l*'
                }, {
                    value: 167,
                    name: 's*'
                }, {
                    value: 87,
                    name: '赵*'
                }, {
                    value: 427,
                    name: '刘*'
                }, {
                    value: 107,
                    name: '赵*'
                },{
                    value: 3000,
                    name: '其他'
                }]

            }]
        };
        money.setOption(option)
    }); */


    
    </script>
	</body>
</html>