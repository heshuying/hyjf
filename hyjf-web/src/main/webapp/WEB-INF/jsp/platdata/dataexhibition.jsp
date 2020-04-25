<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
	<style>
		.earn-gantanhao{
			display:inline;
			color: #ff5b29;
			cursor:pointer;
			position: relative;
		}
		.earn-gantanhao:hover  .gantanhao-tips{
			display:inline;
		}
		.icon-gantanhao:before {
		    content: "\e609";
		}
		.earn-gantanhao .gantanhao-tips {
		    top:23px;
		    position: absolute;
		    font-size:12px;
		    width:240px;
		    padding:5px;
		    margin-left:-128px;
		    color:#999;
		    background:#fff;
		    box-shadow: 0 0 8px #ddd;
		    text-align:center;
		    display:none;
		}
		.gantanhao-tips:after{
		  border: solid transparent;
		  content: ' ';
		  height: 0;
		  left: 50%;
		  top:-10px;
		  border-width: 5px;
  		  border-bottom-color: #fff;
		  position: absolute;
		  width: 0;
		  margin-left:-5px;
		 }
	</style>
	 <section class="data-exhibition main-content">
		<div class="dataBox-1 dataBox">
			<div class="content">
				<div class="title">
					<p>运营数据</p>
				</div>
				<p class="tit">实时数据</p>
				<ul class="number flex-box">
					<li class="l1">
						<p class="data">${investTotal}<span>元</span></p>
						<p class="name">累计交易总额</p>
					</li>
					<li class="l2">
						<p class="data">${interestTotal}<span>元</span></p>
						<p class="name">
							累计用户收益 
							<span class="icon iconfont icon-gantanhao earn-gantanhao">
								<span class="gantanhao-tips">数据为已实际发放收益，不包含待收收益。</span>
							</span>
						</p>
					</li>
					<li class="l1">
						<p class="data">${tenderCounts}<span>笔</span></p>
						<p class="name">累计交易笔数</p>
					</li>
				</ul>
				<div class="chart-tab">
					<a class="select">月交易总额</a>
					<a>月交易笔数</a>
				</div>
				<div class="chart-box">
					<div class="chart-box-child" id="chart1" style="width: 1000px;height: 310px;">
					</div>
					<div class="chart-box-child" id="chart2" style="width: 1000px;height: 310px;display: none;">
					</div>
				</div>
			</div>
			
		</div>
		<div class="dataBox-2 dataBox">
			<div class="content">
				<p class="tit">借款数据统计</p>
				<p class="time">数据截止至${deadline}</p>
				<div class="data-loan">
					<div>
						<p class="data">${totalRepayAwait}<span>元</span></p>
						<p class="name">借贷余额</p>
					</div>
					
					<div>
						<p class="data">${borrowuserCountTotal }<span>人</span></p>
						<p class="name">累计借款人</p>
					</div>
					<div>
						<p class="data">${borrowuserMoneyTopten }<span>%</span></p>
						<p class="name">前十大借款人待还金额占比</p>
					</div>
				</div>
				<br/><br/><br/><br/>
				<div class="data-loan">
					<div>
						<p class="data">${totalRepayCounts }<span>笔</span></p>
						<p class="name">借贷笔数</p>
					</div>
					<div>
						<p class="data">${borrowuserCountCurrent }<span>人</span></p>
						<p class="name">当前借款人</p>
					</div>
					<div>
						<p class="data">${borrowuserMoneyTopone }<span>%</span></p>
						<p class="name">最大单一借款人待还金额占比</p>
					</div>
				</div>
			</div>
		</div>
		<div class="dataBox-2 dataBox">
			<div class="content">
				<p class="tit">其他数据统计</p>
				<p class="time">数据截止至${deadline}</p>
				<div class="data-loan">
					
					<div>
						<p class="data">0<span>元</span></p>
						<p class="name">逾期金额</p>
					</div>
					<div>
						<p class="data">0<span>元</span></p>
						<p class="name">关联关系借款余额</p>
					</div>
					<div>
						<p class="data">0<span>元</span></p>
						<p class="name">逾期90天（不含）以上金额</p>
					</div>
					<div>
						<p class="data">0<span>元</span></p>
						<p class="name">累计代偿金额</p>
					</div>
				</div>
				<br/><br/><br/><br/>
				<div class="data-loan">
					<div>
						<p class="data">0<span>笔</span></p>
						<p class="name">逾期笔数</p>
					</div>
					
					<div>
						<p class="data">0<span>笔</span></p>
						<p class="name">关联关系借款笔数</p>
					</div>
					<div>
						<p class="data">0<span>笔</span></p>
						<p class="name">逾期90天（不含）以上笔数</p>
					</div>
					<div>
						<p class="data">0<span>笔</span></p>
						<p class="name">累计代偿笔数</p>
					</div>
				</div>
			</div>
			
		</div>
		
		<div class="dataBox-3 dataBox">
			<div class="content">
				<p class="tit">出借数据统计</p>
				<p class="time">数据截止至${deadline}</p>
				<ul class="number">
					<li class="l1">
						<p class="data">${registerCounts}<span>人</span></p>
						<p class="name">出借人总数</p>
					</li>
					<li class="l1">
						<p class="data">${tenderuserCountCurrent }<span>人</span></p>
						<p class="name">当前出借人</p>
					</li>
					<li class="l2">
						<p class="data">${perCapitaInvestment}<span>元</span></p>
						<p class="name">人均出借金额</p>
					</li>
					<li class="l1">
						<p class="data">${hour}<span>时</span>${min}<span>分</span>${sec}<span>秒</span></p>
						<p class="name">平均满标用时</p>
					</li>
					<p class="time" style="position: relative;top:50px">1、平台向出借人的收费标准：提现手续费1元/笔；智投服务按照实际签署的协议收取相应费用；债权转让服务费=实际支付金额*1%<br/>
2、汇盈金服评估借款人的借款风险，向借款人按照年化约7%-18%的费率收取服务费。</p>
				</ul>
				<div class="pie-box">
					<div class="chart3-area">
						<div id='chart3' style="width: 450px;height: 280px;">
						</div>
						<ul class="legend">
							<c:forEach items="${ageDataBeanList }" var="record" begin="0" step="1" varStatus="status">
								<li>
									<span class="name">${record.name}</span>
									<span class="value">${record.rateValue}</span>
									<span class="radius" style="${record.styleClass}"></span>
								</li>
							</c:forEach>
						</ul>
					</div>
					<div class="chart4-area">
						<div id='chart4' style="width: 390px;height: 280px;"></div>
						<ul class="legend">
							<c:forEach items="${sexDataBeanList }" var="record" begin="0" step="1" varStatus="status">
								<li>
									<span class="name">${record.name}</span>
									<span class="value">${record.rateValue}</span>
									<span class="radius" style="${record.styleClass}"></span>
								</li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="region-box">
					<div class="region-list">
						<p class="list-tit">出借人地域分布</p>
						<ul>
							<li class="first-li">TOP 10</li>
							<c:forEach items="${top10RegionData }" var="record" begin="0" step="1" varStatus="status">
								<li data-name="${record.name}">
									<span class="area-number">${record.areaNumber}</span>
									<span class="area-privince">${record.name}</span>
									<span class="percentage">${record.rateValue}</span>
								</li>
							</c:forEach>
						</ul>
					</div>
					<div id='chart5' style="width: 450px;height: 360px;"></div>
				</div>
			</div>
		</div>
    </section>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/echarts.min.js"></script>
	<script src="${cdn}/dist/js/lib/china.js"></script>
    <script src="${cdn}/dist/js/exhibition/data-exhibition.js"></script>
	<!-- 设置定位  -->
	<script>setActById("platdatastatistics");</script>
	<script>setActById("aboutPlatformInfo");</script>
	<!-- 设置定位  -->
	<script>setActById("indexMessage");</script>
    <script >
		/*表格1 start*/
		 // 初始化echarts实例
        var Chart1 = echarts.init(document.getElementById('chart1'));
//      // 指定图表的配置项和数据
        var option = {
            tooltip:'hide',
			width:'900px',
			height:'220px',
            xAxis: {
                data: ${monthlyTenderTitle},
                splitLine: {  
	                lineStyle: {  
	                    color: ['#c3c3c3']  
	                }  
	            }, 
	            axisLine:{  
                    lineStyle:{  
                        color:'#c3c3c3',  
                    }  
                },
                axisLabel: {  
                    textStyle: {
		                color: '#404040'
		            } 
                }, 
            },
            yAxis: {
            	splitLine: {  
	                lineStyle: {  
	                    color: ['#c3c3c3']  
	                }  
	            }, 
	            
	            axisLine:{  
                    lineStyle:{  
                        color:'#c3c3c3',  
                    }  
                },
                axisLabel: {
		            textStyle: {
		                color: '#404040'
		            },
		            formatter: '{value}亿元'
		        }
            },
            color:['#e95d36'],
            series: [{
                name: '月交易总额（亿元）',
                type: 'bar',
                barWidth : 30,
                data: ${monthlyTenderData},
            }],
        };

        // 使用刚指定的配置项和数据显示图表。
        Chart1.setOption(option);
		/*表格1 end*/
		
		
		/*表格2 start*/
		var Chart2 = echarts.init(document.getElementById('chart2'));
//      // 指定图表的配置项和数据
        var option = {
            tooltip:'hide',
			width:'900px',
			height:'220px',
            xAxis: {
                data: ${monthlyTenderTitle},
                splitLine: {  
	                lineStyle: {  
	                    color: ['#c3c3c3']  
	                }  
	            }, 
	            axisLine:{  
                    lineStyle:{  
                        color:'#c3c3c3',  
                    }  
                },
                axisLabel: {  
                    textStyle: {
		                color: '#404040'
		            } 
                }, 
            },
            yAxis: {
            	splitLine: {  
	                lineStyle: {  
	                    color: ['#c3c3c3']  
	                }  
	            }, 
	            
	            axisLine:{  
                    lineStyle:{  
                        color:'#c3c3c3',  
                    }  
                },
                axisLabel: {
		            textStyle: {
		                color: '#404040'
		            },
		            formatter: '{value}万笔'
		        }
                
            },
            color:['#e95d36'],
            series: [{
                name: '月交易笔数',
                type: 'bar',
                barWidth : 30,
                data: ${monthlyTenderCountData},
            }],

        };

        // 使用刚指定的配置项和数据显示图表。
        Chart2.setOption(option);
		
		/*表格2 end*/
		/*表格3 start*/
		var Chart3=echarts.init(document.getElementById('chart3'));
		option3 = {
		    tooltip: 'hide',
			title: {
		        text: '出借人年龄分布',
		        textStyle:{
		        	color:'#404040',
		        	fontSize:14,
		        	fontWeight:400,
		        }
	    	},
		    legend: {
	    		show:false,
		    	selectedMode:false,
		        orient: 'vertical',
		        x: 'right',
				//data:${ageData},
		        top:'120px',
		    },
		    color:['#f6c66c','#d34956','#16517a','#67c3cb'],
		    series: [
		        {
		        	center:[110,170],
		            type:'pie',
		            radius: ['45%', '65%'],
		            label: {
		                normal: {
		                    show: false,
		                    position: 'center'
		                },
		                emphasis: {
		                    show: false,
		                }
		            },
		            labelLine: {
		                normal: {
		                    show: false
		                }
		            },
					data:${ageData}
		        }
		    ]
		};
		Chart3.setOption(option3);
		/*表格3 end*/
		
		
		/*表格4 start*/
		var Chart4=echarts.init(document.getElementById('chart4'));
		option4 = {
		    tooltip: 'hide',
			title: {
		        text: '出借人性别分布',
		        textStyle:{
		        	color:'#404040',
		        	fontSize:14,
		        	fontWeight:400,
		        }
	    	},
		    legend: {
	    		show:false,
		    	selectedMode:false,
		        orient: 'vertical',
		        x: 'right',
		        // data:${sexData},
		        top:'145px',
		    },
		    color:['#16517a','#d34956'],
		    series: [
		        {
		        	center:[110,170],
		            type:'pie',
		            radius: ['45%', '65%'],
		            label: {
		                normal: {
		                    show: false,
		                    position: 'center'
		                },
		                emphasis: {
		                    show: false,
		                }
		            },
		            labelLine: {
		                normal: {
		                    show: false
		                }
		            },
		            data:${sexData}
		        }
		    ]
		};
		Chart4.setOption(option4);
		/*表格4 end*/

	var Chart5=echarts.init(document.getElementById('chart5'));	
	var option5 = {
	    color:['#dbf4ff','#d6f1fd','#53b9df'],
        visualMap: {
        	min: 0,
        	max: 6000,
	        left: '-100%',
	        top: 'bottom',
	        text: ['High', 'Low'],
	        inRange: {
	            color: ['#dbf4ff', '#53b9df']
	        },
	        calculable: false
	    },
	    tooltip: {
	        trigger: 'item'
	    },
	    series:[{
	        name: '出借人地域分布',
	        type: 'map',
	        mapType: 'china',

			map: 'china',
	        roam: false,
//	        label: {
//	            normal: {
//	                show: true
//	            },
//	            emphasis: {
//	                show: true
//	            }
//	        },
	        zoom:1.2,
//	        itemStyle: {
//                normal: {
//                    borderColor: '#000',
//                    borderWidth: 0,
//					label:{show:true}
//                },
//				emphasis:{label:{show:true}}
//            },
//			label: {
//				normal: {
//					formatter: '{b}',
//					position: 'right',
//					show: true,
//					color: '#666666',
//					shadowBlur: 10,
//					shadowColor: '#333',
//					borderColor: '#000',
//					borderWidth: 0,
//					fontSize:10,
//					fontWeight:200,
//					position:['top']
//				}
//			},
//			itemStyle: {
//				normal: {
//					borderColor: '#000',
//					borderWidth: 0,
//				}
//			},

			label: {
				normal: {
					formatter: '{b}',
					position: 'right',
					show: true,
					color: '#666666',
					shadowBlur: 10,
					shadowColor: '#333',
					borderColor: '#000',
					borderWidth: 0,
					fontSize:8,
					fontWeight:200,
					position:['top']
				}
			},
			itemStyle: {
				normal: {
					borderColor: '#000',
					borderWidth: 0,
				}
			},
//			itemStyle:{
//				normal:{label:{show:true}},
//				emphasis:{label:{show:true}}
//			},
	        data:${regionData}
	    }]
	}
	Chart5.setOption(option5);
	</script>
	<script>
		 Chart3.on('mouseover',function(param){

		    if (typeof param.seriesIndex != 'undefined') {
		        var id=param.dataIndex;
		    }
		    $('.chart3-area .legend li'+':eq('+id+')').addClass("highlight");
        })
         Chart3.on('mouseout',function(param){
        	
		    if (typeof param.seriesIndex != 'undefined') {
		       var id=param.dataIndex;
		    }
		    $('.chart3-area .legend li').removeClass("highlight");
		
        })
        $(".chart3-area .legend").find('li').mouseenter(function(){
	        var item = $(this);
	        var itemindex = $('.chart3-area .legend li').index(this); 
	        item.addClass("highlight");
	        console.log(itemindex)
	        Chart3.dispatchAction({
	            type: 'highlight',
	            dataIndex: itemindex
	        })
	    }).mouseleave(function(){
	        var item = $(this);
	        var itemindex = $('.chart3-area .legend li').index(this);
            item.removeClass("highlight");
            Chart3.dispatchAction({
                type: 'downplay',
                dataIndex: itemindex
            })
	    });
	    
	    
	    
	    $(".chart4-area .legend").find('li').mouseenter(function(){
	        var item = $(this);
	        var itemindex = $('.chart4-area .legend li').index(this); 
	        item.addClass("highlight");
	        console.log(itemindex)
	        Chart4.dispatchAction({
	            type: 'highlight',
	            dataIndex: itemindex
	        })
	    }).mouseleave(function(){
	        var item = $(this);
	        var itemindex = $('.chart4-area .legend li').index(this);
            item.removeClass("highlight");
            Chart4.dispatchAction({
                type: 'downplay',
                dataIndex: itemindex
            })
	    });
         
         
         
        Chart4.on('mouseover',function(param){

		    if (typeof param.seriesIndex != 'undefined') {
		        var id2=param.dataIndex;
		    }
		    $('.chart4-area .legend li'+':eq('+id2+')').addClass("highlight");
        })
         Chart4.on('mouseout',function(param){
        	
		    if (typeof param.seriesIndex != 'undefined') {
		       var id2=param.dataIndex;
		    }
		    $('.chart4-area .legend li').removeClass("highlight");
		
        })
        $(".region-list").find('li').mouseenter(function(){
	        var item = $(this);
	        var itemname = item.data('name');
	        if(!item.hasClass('first-li') && itemname != ''){
	            item.addClass("highlight");
	            Chart5.dispatchAction({
	                type: 'mapSelect',
	                name: itemname
	            })
	        }
	    }).mouseleave(function(){
	        var item = $(this);
	        var itemname = item.data('name');
	        if(!item.hasClass('first-li') && itemname != ''){
	            item.removeClass("highlight");
	            Chart5.dispatchAction({
	                type: 'mapUnSelect',
	                name: itemname
	            })
	        }
	    });
    Chart5.on('mouseover',function(param){
        if (typeof param.name!= 'undefined') {
            $('.region-list').find('li[data-name='+param.name+']').addClass('highlight');
        }
    })
	Chart5.on('mouseout',function(param){
		if (typeof param.name!= 'undefined') {$('.region-list').find('li[data-name='+param.name+']').removeClass('highlight');
		}
	})

	</script>
</body>
</html>