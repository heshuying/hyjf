<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="UTF-8">
		<title>平台数据 </title>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
</head>
<body class="bg_grey response">
		<section class="hyjf-data-item">
			<p class="hyjf-data-item-title">平台数据总览</p>
			<div class="hyjf-data-item-div hyjf-data-item-div-1">
				<p class="hyjf-data-item-p-1 hyjf-color" id="tenderMoney"></p>
				<p class="hyjf-data-item-p-2 color898989">用户投资总额(元)</p>
			</div>
			<div class="hyjf-data-item-div">
				<p class="hyjf-data-item-p-1 hyjf-color" id="recoverInterest"></p>
				<p class="hyjf-data-item-p-2 color898989">用户赚取收益(元)</p>
			</div>
			<div class="clearBoth"></div>
			<p class="tar font12">单位:万元&nbsp;&nbsp;</p>
			<img src="${ctx}/img/data-legend-1.png" class="data-legend-1"/>
			<div id="summary"></div>
		</section>
		<section class="hyjf-data-item">
			<p class="hyjf-data-item-title">7日平台数据变化<span class="color898989" id="newdate"></span></p>
			<div class="hyjf-data-item-div hyjf-data-item-div-1">
				<p class="hyjf-data-item-p-1 hyjf-color" id="tenderMoney7"></p>
				<p class="hyjf-data-item-p-2 color898989">用户投资总额(元)</p>
			</div>
			<div class="hyjf-data-item-div">
				<p class="hyjf-data-item-p-1 hyjf-color" id="recoverInterest7"></p>
				<p class="hyjf-data-item-p-2 color898989">用户赚取收益(元)</p>
			</div>
			<div class="clearBoth"></div>
		</section>
		<section class="hyjf-data-item">
			<p class="hyjf-data-item-title">融资期限分布</p>
			<img src="${ctx}/img/data-legend-2.png" class="data-legend-2"/>
			<div id="project-maturity"></div>
		</section>
		<section class="hyjf-data-item">
			<p class="hyjf-data-item-title">投资金额占比</p>
			<img src="${ctx}/img/data-legend-3.png" class="data-legend-3"/>
			<div id="invest-money"></div>
		</section>
	<script src="${ctx}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/js/echarts.min.js" type="text/javascript" charset="utf-8"></script>


	<script>

	</script>
	<script type="text/javascript">
	</script>
	
	
	<script>
	 $.ajax({
	        type: "post",
	        dataType: "json",
	        url: '${ctx}/homepage/getPlatformData',
	        success: function (data) {
	            if (data != "" && data.status == "0") {
	            	//数据更新
	            	$("#tenderMoney").html(data.tenderMoney);
	            	$("#recoverInterest").html(data.recoverInterest);
	            	$("#newdate").html("(数据截止至："+data.newdate+")");
	            	$("#tenderMoney7").html(data.tenderMoney7);
	            	$("#recoverInterest7").html(data.recoverInterest7);
	            	
	            	//柱状图
	        		var summary = echarts.init(document.getElementById('summary'));
	                var option = {
	                    color: ['#20a6e2', '#66CC66'],
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
	                        data: ['汇直投', '汇转让'],
	                        textStyle: {
	                            fontFamily: '微软雅黑, Arial, Verdana, sans-serif'
	                        },
	                        y: '5%',
	                        x: '5%',
	                        z:5,
	                        width:'50px',
	                        height:'20px',
	                        show:'true'
	                        
	                    },
	                    grid: {
	                        x: '7%',
	                        y: '15%',
	                        x2: '1%',
	                        y2: '10%',
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
	                        data:data.timelist
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
	         					rotate: 60,//60度角倾斜显示
	                            formatter: '{value}',
	                        },
	                        splitArea: {
	                            show: true,
	                            areaStyle: {
	                                color: ['rgba(250,250,250,0.2)', 'rgba(200,200,200,0.2)']
	                            }
	                        }
	                    }],
	                    series: [{
	                        name: '汇直投',
	                        type: 'bar',
	                        stack: '堆积',
	                        data: data.tendermoneylist

	                    }, {
	                        name: '汇转让',
	                        type: 'bar',
	                        stack: '堆积',
	                        data: data.creditmoneylist

	                    }],

	                };
	                summary.setOption(option);
	                
	             //饼图1   
	       		 var maturity = echarts.init(document.getElementById('project-maturity'));
	             var option1 = {
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
	                 	show:true,
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
	                 calculable: false,
	                 series: [{
	                     name: '项目期限占比',
	                     type: 'pie',
	                     radius: [35, 80],
	                     center: ['40%', '50%'],
	                     roseType: 'radius',
	                     width: '100%',
	                     max: 20,
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
	                            /*  labelLine: {
	                                 show: true
	                             } */
	                         }
	                     },
	                     data: [{
	                         value: data.periodInfo.zeroone,
	                        /*  name: '0 - 1\u4e2a\u6708' */
	                     }, {
	                         value: data.periodInfo.onethree,
	                         /* name: '1 - 3\u4e2a\u6708' */
	                     }, {
	                         value: data.periodInfo.threesex,
	                        /*  name: '3 - 6\u4e2a\u6708' */
	                     }, {
	                         value: data.periodInfo.sextw,
	                        /*  name: '6 - 12\u4e2a\u6708' */
	                     }, {
	                         value: data.periodInfo.tw,
	                        /*  name: '12\u4e2a\u6708\u4ee5\u4e0a' */
	                     }]

	                 }, ]
	             };

	             maturity.setOption(option1);
	                
	             
	            //饼图二
	     		var money = echarts.init(document.getElementById('invest-money'));
	            var option = {
	            	color:['#ff7d50', '#87cffb', '#db71d8', '#32cd33', '#6295ef'],
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
	                        value: data.TendMoneyInfo.zeroone,
	                        name: '1\u4e07\u4ee5\u4e0b'
	                    }, {
	                        value: data.TendMoneyInfo.onefive,
	                        name: '1-5\u4e07'
	                    }, {
	                        value: data.TendMoneyInfo.fiveten,
	                        name: '5-10\u4e07'
	                    }, {
	                        value: data.TendMoneyInfo.tenfive,
	                        name: '10-50\u4e07'
	                    }, {
	                        value: data.TendMoneyInfo.five,
	                        name: '50\u4e07\u4ee5\u4e0a'
	                    }]

	                }]
	            };
	            money.setOption(option)
	                
	            }
	        }
	    });
	</script>
	
	</body>
</html>