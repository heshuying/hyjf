var
// --------------------------------------------------------------------------------------------------------------------------------

/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出execlAction
	initAction : "initAction",
	// 查找的Action
	searchAction : "statisSearchAction",
	// 查找的Action
	countAction : "countAction"
},
/* 事件动作处理 */
Events = {
	// 画面调整事件
	resizeAct: function() {
		$.each("marginsChart1,turnMoney1".split(","), function(c) {
			c = Page[this],
			c && c.resize();
		});
	},
	// 
	chartByDMClkAct: function(t) {
		t = $(this);
		var param = {
				'viewFlag' : t.data("paramvalue")
			};
		$.post(Action.searchAction, param, function(rs) {
			tab1Margins(rs.timepoint,rs.data2);
			tab1TurnMoney(rs.days,rs.data);
		}).error(function() {
			console.info("error!");
		})
	}
},
// 子账户“充值手续费垫付”余额统计
tab1Margins = function(timepoint,data2) {
	Page.marginsChart1 = echarts.init(document.getElementById('chart-margins'));
	    var option = {
		color : [ '#4bbd00' ],
		tooltip : {
			trigger : 'axis',
			formatter : " 时间节点：{b} <br/>账户余额: {c}",
			backgroundColor : 'rgba(0,0,0,0.5)',
			axisPointer : {
				type : 'shadow'
			},
			textStyle : {
				fontSize : 12
			}
		},
		dataZoom : {
			show : false,
		},
		grid : {
			x : '8%',
			y : '17%',
			x2 : '8%',
			y2 : '15%',
			borderWidth : 1,
			borderColor : '#eee'
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			boundaryGap : false,
			axisLine : {
				lineStyle : {
					color : '#acacac',
					width : 1,
					type : 'solid'
				}
			},
			splitLine : {
				lineStyle : {
					color : '#ebebeb',
					width : 1,
					type : 'solid'
				}
			},
			axisLabel : {
				textStyle : {
					fontFamily : '微软雅黑, Arial, Verdana, sans-serif',
					color : '#666'
				}
			},
			
			data : timepoint
		} ],
		yAxis : [ {
			'name' : '金额（元）',
			type : 'value',
			axisLine : {
				lineStyle : {
					color : '#acacac',
					width : 1,
					type : 'solid'
				}
			},
			splitLine : {
				lineStyle : {
					color : '#ebebeb',
					width : 1,
					type : 'solid'
				}
			},
			axisLabel : {
				textStyle : {
					fontFamily : '微软雅黑, Arial, Verdana, sans-serif',
					color : '#666'
				}
			},
			formatter:'{value}'
		} ],
		series : [ {
			name : '充值手续费垫付账户余额',
			type : 'line',
			data :data2
		} ]
	};
	Page.marginsChart1.setOption(option);
},


//垫付金额
tab1TurnMoney = function(days,data) {
	Page.turnMoney1 = echarts.init(document.getElementById('chart-turn-money'));
	var _length = parseFloat(data.length);
	var _flag = true;
	var _dis;
	if(_length<7 || _length==7){
		_dis = 0;
		_flag = false;
	}else{
		_dis = 7/_length;
		_dis = 1-_dis.toFixed(4);
		_dis = parseInt(_dis*100)+1;
	}
	var option = {
		color : [ '#66cc66'],
		tooltip : {
			trigger : 'axis',
			backgroundColor : 'rgba(0,0,0,0.5)',
			axisPointer : {
				type : 'shadow'
			},
			textStyle : {
				fontSize : 12
			},
		},
		dataZoom : {
			show : _flag,
			start : _dis
		},
		legend : {
			data : [ '充值手续费垫付金额' ],
			textStyle : {
				fontSize : '13',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			y : '5%',
		},
		grid : {
			x : '8%',
			y : '17%',
			x2 : '8%',
			y2 : '15%',
			borderWidth : 1,
			borderColor : '#eee'
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			axisLine : {
				lineStyle : {
					color : '#acacac',
					width : 1,
					type : 'solid'
				}
			},
			splitLine : {
				lineStyle : {
					color : '#ebebeb',
					width : 1,
					type : 'solid'
				}
			},
			axisLabel : {
				textStyle : {
					fontFamily : '微软雅黑, Arial, Verdana, sans-serif',
					color : '#666',
				}
			},
			data : days
		} ],
		yAxis : [ {
			'name' : '金额（万元）',
			type : 'value',
			axisLine : {
				lineStyle : {
					color : '#acacac',
					width : 1,
					type : 'solid'
				}
			},
			splitLine : {
				lineStyle : {
					color : '#ebebeb',
					width : 1,
					type : 'solid'
				}
			},
			axisLabel : {
				textStyle : {
					fontFamily : '微软雅黑, Arial, Verdana, sans-serif',
					color : '#666'
				},
				formatter : '{value}'
			}
		} ],
		series : [
				{
					name : '垫付金额',
					type : 'bar',
					data : data
				} ],
	};
	Page.turnMoney1.setOption(option)
}


// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
		}),
		// 日历范围限制
		$('.input-daterange :text[id$="start-date"]').on("change", function(evnet, d) {
			d = $(this).parent().nextAll(".form-control").datepicker("getDate"),
			d && $(this).datepicker("setEndDate", d)
		}),
		$('.input-daterange :text[id$=end-date]').on("change", function(evnet, d) {
			d = $(this).prevAll(".input-icon").find(":text").datepicker("getDate"),
			d && $(this).datepicker("setStartDate", d)
		});
		
		// 初始化图表
		setTimeout(function() {  
			// 按天统计事件绑定
			$(".fn-todayView").click();
		}, 10);
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 按天统计事件绑定
		$(".fn-todayView").click(Events.chartByDMClkAct);
		$(".fn-yestodayView").click(Events.chartByDMClkAct);
	 }
});
