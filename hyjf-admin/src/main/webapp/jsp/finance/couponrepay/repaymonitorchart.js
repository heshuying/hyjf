var
// --------------------------------------------------------------------------------------------------------------------------------

/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "repayStatisticAction",
},
/* 事件动作处理 */
Events = {
	// 画面调整事件
	resizeAct: function() {
		$.each("repayStatistic".split(","), function(c) {
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
			couponRepayMoney(rs.days,rs.moneyWaitSum,rs.moneyYesSum);
		}).error(function() {
			console.info("error!");
		})
	}
},


//加息券还款统计
couponRepayMoney = function(days,moneyWaitSum,moneyYesSum) {
	Page.repayStatistic = echarts.init(document.getElementById('coupon_repay_statistic'));
	var startPosition = 0;
	if(days && days.length > 7){
		startPosition = ((days.length - 7) /days.length);
		startPosition = startPosition.toFixed(4) * 100;
		startPosition = parseInt(startPosition) + 1;
	}
	
	var option = {
			tooltip : {
		        trigger: 'axis',
		        formatter: function(params) {
		        	if(params.length > 1){
		        		return params[0].name + '<br/>'
		        		+ params[0].seriesName + ' : ' + params[0].value + ' 元<br/>'
		        		+ params[1].seriesName + ' : ' + params[1].value + ' 元';
		        	}else if(params.length = 1){
		        		return params[0].name + '<br/>'
		        		+ params[0].seriesName + ' : ' + params[0].value + ' 元'
		        	}
		        }
		    },
			dataZoom : {
				show : true,
				start : startPosition
			},
			legend : {
				data : [ '加息券实际还款','加息券待还' ],
				textStyle : {
					fontSize : '13',
					fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
				},
				y : '5%'
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
				
				data : days
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
				name : '加息券实际还款',
				type : 'line',
				data :moneyYesSum
			},{
				name : '加息券待还',
				type : 'line',
				data :moneyWaitSum
			} ]
		};
	Page.repayStatistic.setOption(option)
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
			Events.chartByDMClkAct();
		}, 10);
	},
	// 初始化画面事件处理
	initEvents : function() {
	 }
});
