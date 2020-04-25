<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
	<article class="main-content">
        <div class="container">
            <section class="content" style="padding-bottom: 28px;">
                <div class="main-title">
                   治理信息
                </div>
                <div class="main-tab jion-us">
					<div class="about-government">
						<p class="title">实际控制人与持股股东名单</p>
						<img src="${cdn}/dist/images/aboutUs/government-1@2x.png" width="448" alt="" />
					</div>
                </div>
            </section>
            <section class="content" style="margin-top:25px;padding-top:1px;padding-bottom:60px">
            	<div class="about-government">
					<p class="title">组织架构</p>
					<img src="${cdn}/dist/images/aboutUs/government-2@2x.png" width="867" alt="" />
				</div>
            </section>
            <section class="content" style="margin-top:25px;padding-top:1px;padding-bottom:60px">
            	<div class="about-government">
					<p class="title">员工情况</p>
					<p class="end-date">截止至2017年9月30日，公司总人数为153人</p>
					<div class="pie-box">
	                    <div class="chart3-area">
	                        <div id='chart3' style="width: 450px;height: 280px;">
	                        </div>
	                        <ul class="legend">
	                           <li>
	                                <span class="name">25岁以下:</span>
	                                <span class="value">11.11%</span>
	                                <span class="radius" style="background:rgb(211,73,86)"></span>
	                            </li>
	                            <li>
	                                <span class="name">25至30岁:</span>
	                                <span class="value">60.13%</span>
	                                <span class="radius" style="background:rgb(22,81,122)"></span>
	                            </li>
	                            <li>
	                                <span class="name">31至35岁:</span>
	                                <span class="value">16.99%</span>
	                                <span class="radius" style="background:rgb(103,195,203)"></span>
	                            </li>
	                            <li>
	                                <span class="name">36至40岁:</span>
	                                <span class="value">11.76%</span>
                                    <span class="radius" style="background:rgb(254,216,125)"></span>
	                            </li>
	                        </ul>
	                    </div>
	                    <div class="chart4-area">
	                        <div id='chart4' style="width: 390px;height: 280px;"></div>
	                        <ul class="legend">
	                            <li>
	                                <span class="name">大专及本科:</span>
	                                <span class="value">86.27%</span>
	                                <span class="radius" style="background:rgb(211,73,86)"></span>
	                            </li>
	                            <li>
	                                <span class="name">硕士及博士:</span>
	                                <span class="value">7.19%</span>
	                                <span class="radius" style="background:rgb(22,81,122)"></span>
	                            </li>
	                            <li>
	                                <span class="name">其他:</span>
	                                <span class="value">6.54%</span>
	                                <span class="radius" style="background:rgb(254,216,125)"></span>
	                            </li>
	                        </ul>
	                    </div>
					</div>
					<p class="end-date" style="margin-top: 65px;margin-bottom: 45px;">其中技术人员49人，平均年龄27岁，50%以上的团队成员拥有本科或本科以上学历</p>
					<div class="pie-box">
	                    <div class="chart3-area">
	                        <div id='chart5' style="width: 450px;height: 280px;">
	                        </div>
	                        <ul class="legend">
	                            <li>
	                                <span class="name">25岁以下:</span>
	                                <span class="value">8.16%</span>
	                                <span class="radius" style="background:rgb(211,73,86)"></span>
	                            </li>
	                            <li>
	                                <span class="name">25至30岁:</span>
	                                <span class="value">75.51%</span>
	                                <span class="radius" style="background:rgb(22,81,122)"></span>
	                            </li>
	                            <li>
	                                <span class="name">31至35岁:</span>
	                                <span class="value">12.24%</span>
	                                <span class="radius" style="background:rgb(103,195,203)"></span>
	                            </li>
	                            <li>
	                                <span class="name">36至40岁:</span>
	                                <span class="value">4.08%</span>
                                    <span class="radius" style="background:rgb(254,216,125)"></span>
	                            </li>
	                        </ul>
	                    </div>
	                    <div class="chart4-area">
	                        <div id='chart6' style="width: 390px;height: 280px;"></div>
	                        <ul class="legend">
	                            <li>
	                                <span class="name">大专及本科:</span>
	                                <span class="value">91.84%</span>
	                                <span class="radius" style="background:rgb(211,73,86)"></span>
	                            </li>
	                            <li>
	                                <span class="name">硕士及博士:</span>
	                                <span class="value">6.12%</span>
	                                <span class="radius" style="background:rgb(22,81,122)"></span>
	                            </li>
	                            <li>
	                                <span class="name">其他:</span>
	                                <span class="value">2.04%</span>
	                                <span class="radius" style="background:rgb(254,216,125)"></span>
	                            </li>
	                        </ul>
	                    </div>
					</div>
				</div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/echarts.min.js"></script>
	<script src="${cdn}/dist/js/lib/baguetteBox.min.js"></script>
	<script src="${cdn}/dist/js/about/about.js"></script>
	 <script type="text/javascript">
    	var Chart3=echarts.init(document.getElementById('chart3'));
		option3 = {
		    tooltip: 'hide',
			title: {
		        text: '员工整体年龄结构',
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
                align:'right',
		        x: 'right',
		        data:[{
		        	
		        	name:'25岁以下',
		        	icon:'circle',
		        },
		        {
		        	
		        	name:'25至35岁',
		        	icon:'circle',
		        },
		        {
		        	
		        	name:'36至50岁',
		        	icon:'circle',
		        },
		        {
		        	
		        	name:'50岁以上',
		        	icon:'circle',
		        }
		        ],
		        top:'120px',
		    },
		    color:['#d34956','#16517a','#67c3cb','#f6c66c'],
		    series: [
		        {
		        	center:[110,170],
		            type:'pie',
		            radius: ['50%', '70%'],
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
		            data:[
		                {value:1111, name:'25岁以下'},
		                {value:6013, name:'25至35岁'},
		                {value:1699, name:'36至50岁'},
		                {value:1176, name:'50岁以上'}
		            ]
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
		        text: '员工整体学历情况',
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
		        data:[{
		        	name:'大专及本科',
		        	icon:'circle',
		        },
		        {
		        	name:'硕士及博士',
		        	icon:'circle',
		        },
		        {
		        	name:'其他',
		        	icon:'circle',
		        }
		        ],
		        top:'145px'
		    },
		     color:['#d34956','#16517a','#f6c66c'],
		    series: [
		        {
		        	center:[110,170],
		            type:'pie',
		            radius:  ['50%', '70%'],
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
		            data:[
		                {value:8627, name:'大专及本科'},
		                {value:719, name:'硕士及博士'},
		                {value:654, name:'其他'},

		            ]
		        }
		    ]
		};
		Chart4.setOption(option4);
		
		
		
		var Chart5=echarts.init(document.getElementById('chart5'));
		option5 = {
		    tooltip: 'hide',
			title: {
		        text: '技术人员年龄结构',
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
                align:'right',
		        x: 'right',
		        data:[{
		        	
		        	name:'25岁以下',
		        	icon:'circle',
		        },
		        {
		        	
		        	name:'25至35岁',
		        	icon:'circle',
		        },
		        {
		        	
		        	name:'36至50岁',
		        	icon:'circle',
		        },
		        {
		        	
		        	name:'50岁以上',
		        	icon:'circle',
		        }
		        ],
		        top:'120px',
		    },
		    color:['#d34956','#16517a','#67c3cb','#f6c66c'],
		    series: [
		        {
		        	center:[110,170],
		            type:'pie',
		            radius: ['50%', '70%'],
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
		            data:[
		                {value:816, name:'25岁以下'},
		                {value:7551, name:'25至35岁'},
		                {value:1224, name:'36至50岁'},
		                {value:408, name:'50岁以上'}
		            ]
		        }
		    ]
		};
		Chart5.setOption(option5);
		/*表格3 end*/
		
		
		/*表格4 start*/
		var Chart6=echarts.init(document.getElementById('chart6'));
		option6 = {
		    tooltip: 'hide',
			title: {
		        text: '技术人员学历情况',
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
		        data:[{
		        	name:'大专及本科',
		        	icon:'circle',
		        },
		        {
		        	name:'硕士及博士',
		        	icon:'circle',
		        },
		        {
		        	name:'其他',
		        	icon:'circle',
		        }
		        ],
		        top:'145px'
		    },
		     color:['#d34956','#16517a','#f6c66c'],
		    series: [
		        {
		        	center:[110,170],
		            type:'pie',
		            radius:  ['50%', '70%'],
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
		            data:[
		                {value:9184, name:'大专及本科'},
		                {value:612, name:'硕士及博士'},
		                {value:204, name:'其他'},

		            ]
		        }
		    ]
		};
		Chart6.setOption(option6);
    </script>
	<!-- 设置定位  -->
	<script>setActById("aboutInformation");</script>
	<!-- 导航栏定位  -->
	<script>setActById("indexMessage");</script>
</body>
</html>