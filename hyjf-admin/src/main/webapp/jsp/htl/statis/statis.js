var
// --------------------------------------------------------------------------------------------------------------------------------

/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出execlAction
	initAction : "initAction",
	// 查找的Action
	searchAction : "searchAction",
	// 查找的Action
	countAction : "countAction"
},
/* 事件动作处理 */
Events = {
	// 画面调整事件
	resizeAct: function() {
		$.each("marginsChart1,balance1,turnMoney1,turnUser1,totalAmount2,total2,outTotal2,intoTotal2,customerNumber3,\
customerSum3,intoMoney3,outMoney3,moneyEnter4,moneyOut4,numberEnter4,numberOut4".split(","), function(c) {
			c = Page[this],
			c && c.resize();
		});
	},
	// 
	chartByDMClkAct: function(t) {
		t = $(this);
		if(t.hasClass("fn-listView")) {
			return;
		}
		var param = {
				'viewFlag' : t.data("paramvalue")
			},
			reloadFn = t.closest(".panel").data("reloadfn");
		$.post(Action.searchAction, param, function(rs) {
			t.parent().siblings().find(".btn-warning").removeClass("btn-warning").addClass("btn-success");
			t.removeClass("btn-success").addClass("btn-warning");
			reloadFn && window[reloadFn] && window[reloadFn](rs.days,rs.data,rs.data2);

		}).error(function() {
			console.info("error!");
		})
	},
	chartByDPChgAct: function(t) {
		t = $(this);
		var viewFlag = t.data("paramvalue");
		var timeStart;
		var timeEnd;
		if(viewFlag == "5-1" || viewFlag == "6-1" || viewFlag == "9-1" || viewFlag == "10-1"){
			timeStart = "";
			timeEnd = t.val();
		}else if(viewFlag.indexOf("-1") != -1){
			timeStart = t.val();
			timeEnd = t.parent().parent().find("[id$=end-date]").val();
		}else{
			timeStart = t.parent().find("[id$=start-date]").val();
			timeEnd = t.val();
		}
		var  param = {
				'viewFlag' : viewFlag,
				'timeStart' : timeStart,
				'timeEnd' : timeEnd
		},
		reloadFn = t.closest(".panel").data("reloadfn");
		$.post(Action.countAction, param, function(rs) {
			reloadFn && window[reloadFn] && window[reloadFn](rs.count1,rs.count2,rs.count3,rs.count4,rs.count5,rs.count6);
		}).error(function() {
			console.info("error!");
		})
	}
},

// 出借人本金总额
tab1Margins = function(days,data) {
	Page.marginsChart1 = echarts.init(document.getElementById('chart-margins'));
	    var option = {
		color : [ '#4bbd00' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>本金总额: {c}",
			backgroundColor : 'rgba(0,0,0,0.5)',
			axisPointer : {
				type : 'shadow'
			},
			textStyle : {
				fontSize : 12
			}
		},
		dataZoom : {
			show : true,
			start : 80.56,
		},
		legend : {
			data : [ '出借人本金总额' ],
			textStyle : {
				fontSize : '13',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			y : '5%'
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
				}
			},
			formatter:'{value}'
		} ],
		series : [ {
			name : '出借人本金总额',
			type : 'line',
			data :data
		} ]
	};
	Page.marginsChart1.setOption(option);
},

// 资管公司账户余额
tab1Balance = function(days,data) {
	Page.balance1 = echarts.init(document.getElementById('chart-balance'));
	var option = {
		color : [ '#0098d9' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>账户余额: {c}",
			backgroundColor : 'rgba(0,0,0,0.5)',
			axisPointer : {
				type : 'shadow'
			},
			textStyle : {
				fontSize : 12
			}
		},
		dataZoom : {
			show : true,
			start : 80.56
		},
		legend : {
			data : [ '资管公司账户余额' ],
			textStyle : {
				fontSize : '13',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			y : '5%'
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
				}
			},
			formatter:'{value}'
		} ],
		series : [ {
			name : '资管公司账户余额',
			type : 'line',
			data : data
		} ]
	};
	Page.balance1.setOption(option);
},

//
tab1TurnMoney = function(days,data,data2) {
	Page.turnMoney1 = echarts.init(document.getElementById('chart-turn-money'));
	var option = {
		color : [ '#66cc66', '#fb9b45' ],
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
			show : true,
			start : 80.56
		},
		legend : {
			data : [ '转入金额', '转出金额' ],
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
					name : '转入金额',
					type : 'bar',
					data : data
				},
				{
					name : '转出金额',
					type : 'bar',
					data : data2
				} ],
	};
	Page.turnMoney1.setOption(option)
},

// 转入转出用户
tab1TurnUser = function(days,data,data2) {
	Page.turnUser1 = echarts.init(document.getElementById('chart-turn-user'));
	var option = {
		color : [ '#5ab1ef', '#fcc951' ],
		tooltip : {
			trigger : 'axis',
			backgroundColor : 'rgba(0,0,0,0.5)',
			axisPointer : {
				type : 'shadow'
			},
			textStyle : {
				fontSize : 12
			}
		},
		dataZoom : {
			show : true,
			start : 80.56
		},
		legend : {
			data : [ '转入用户', '转出用户' ],
			textStyle : {
				fontSize : '13',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			y : '5%'
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
			'name' : '用户数（人）',
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
			},
		} ],
		series : [
				{
					name : '转入用户',
					type : 'bar',
					data : data
				},
				{
					name : '转出用户',
					type : 'bar',
					data : data2
				} ]
	};
	Page.turnUser1.setOption(option)
},

// 出借人本金-金额分布
tab2TotalAmount = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total-amount").width($("#chart-margins").width());
	Page.totalAmount2 = echarts.init(document.getElementById('chart-total-amount'));
	var option = {
		color : [ '#f8585b', '#b6a2de', '#66cc66', '#ffdc89', '#f89135',
				'#20a6e2', '#d87a80', '#2ec7c9', '#a7d252', '#fbaeae' ],
		tooltip : {
			trigger : 'item',
			formatter : "{a}<br />{b}：{c}元({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ '0 - 1万元', '1 - 2万元', '2 - 3万元', '3 - 4万元', '4 - 5万元',
					'5万元以上' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '本金金额占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : '0 - 1\u4e07\u5143'
			}, {
				value : count2,
				name : '1 - 2\u4e07\u5143'
			}, {
				value : count3,
				name : '2 - 3\u4e07\u5143'
			}, {
				value : count4,
				name : '3 - 4\u4e07\u5143'
			}, {
				value : count5,
				name : '4 - 5\u4e07\u5143'
			}, {
				value : count6,
				name : '5\u4e07\u5143\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.totalAmount2.setOption(option)
},

// 资人本金-人数分布
tab2Total = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total").width($("#chart-margins").width());
	Page.total2 = echarts.init(document.getElementById('chart-total'));
	var option = {
		color : [ '#f8585b', '#b6a2de', '#66cc66', '#ffdc89', '#f89135',
				'#20a6e2', '#d87a80', '#2ec7c9', '#a7d252', '#fbaeae' ],
		tooltip : {
			trigger : 'item',
			formatter : "{a}<br />{b}：{c}人({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ '0 - 1万元', '1 - 2万元', '2 - 3万元', '3 - 4万元', '4 - 5万元',
					'5万元以上' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '本金金额人数占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : '0 - 1\u4e07\u5143'
			}, {
				value : count2,
				name : '1 - 2\u4e07\u5143'
			}, {
				value : count3,
				name : '2 - 3\u4e07\u5143'
			}, {
				value : count4,
				name : '3 - 4\u4e07\u5143'
			}, {
				value : count5,
				name : '4 - 5\u4e07\u5143'
			}, {
				value : count6,
				name : '5\u4e07\u5143\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.total2.setOption(option)
},

// 转出金额-人次分布
tab2OutTotal = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-out-total").width($("#chart-margins").width());
	Page.outTotal2 = echarts.init(document.getElementById('chart-out-total'));
	var option = {
		color : [ '#70458c', '#fcdb00', '#dd127b', '#ec89a5', '#82c247',
				'#61bba2', '#959595', '#986222', '#db2442', '#693446' ],
		tooltip : {
			trigger : 'item',
			formatter : "{a}<br />{b}：{c}人次({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ '0 - 1万元', '1 - 2万元', '2 - 3万元', '3 - 4万元', '4 - 5万元',
					'5万元以上' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '转出金额占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],

			data : [ {
				value : count1,
				name : '0 - 1\u4e07\u5143'
			}, {
				value : count2,
				name : '1 - 2\u4e07\u5143'
			}, {
				value : count3,
				name : '2 - 3\u4e07\u5143'
			}, {
				value : count4,
				name : '3 - 4\u4e07\u5143'
			}, {
				value : count5,
				name : '4 - 5\u4e07\u5143'
			}, {
				value : count6,
				name : '5\u4e07\u5143\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.outTotal2.setOption(option)
},

// 转入金额-人次分布
tab2IntoTotal = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-into-total").width($("#chart-margins").width());
	Page.intoTotal2 = echarts.init(document.getElementById('chart-into-total'));
	var option = {
		color : [ '#ff7f50', '#ffdc89', '#66cc66', '#b398e7', '#9fa9c3',
				'#ecd615', '#e181ce', '#19cacc', '#e6626a', '#9dd134' ],
		tooltip : {
			trigger : 'item',
			formatter : "{a}<br />{b}：{c}人次({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ '0 - 1万元', '1 - 2万元', '2 - 3万元', '3 - 4万元', '4 - 5万元',
					'5万元以上' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '转入金额占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : '0 - 1\u4e07\u5143'
			}, {
				value : count2,
				name : '1 - 2\u4e07\u5143'
			}, {
				value : count3,
				name : '2 - 3\u4e07\u5143'
			}, {
				value : count4,
				name : '3 - 4\u4e07\u5143'
			}, {
				value : count5,
				name : '4 - 5\u4e07\u5143'
			}, {
				value : count6,
				name : '5\u4e07\u5143\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.intoTotal2.setOption(option)
},

// 新老客户-出借人数分布
tab3CustomerNumber = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-customer-number").width($("#chart-margins").width());
	Page.customerNumber3 = echarts.init(document.getElementById('chart-customer-number'));
	var option = {
		color : [ '#6699cc', '#cccc99', '#d1074b', '#dcdc08', '#ff9900',
				'#666699', '#993366', '#666633', '#cc9900', '#cc99cc' ],
		tooltip : {
			trigger : 'item',
			formatter : "注册时间({b})<br />{c}人({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ '0-10天', '11-20天', '21-30天', '30天以上' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '出借人数占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : '0-10\u5929'
			}, {
				value : count2,
				name : '11-20\u5929'
			}, {
				value : count3,
				name : '21-30\u5929'
			}, {
				value : count4,
				name : '30\u5929\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.customerNumber3.setOption(option);
},

// 新老客户-本金金额分布
tab3CustomerSum = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-customer-sum").width($("#chart-margins").width());
	Page.customerSum3 = echarts.init(document.getElementById('chart-customer-sum'));
	var option = {
		tooltip : {
			trigger : 'item',
			formatter : "注册时间({b})<br />{c}元({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ '0-10天', '11-20天', '21-30天', '30天以上' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '本金金额占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : '0-10\u5929'
			}, {
				value : count2,
				name : '11-20\u5929'
			}, {
				value : count3,
				name : '21-30\u5929'
			}, {
				value : count4,
				name : '30\u5929\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.customerSum3.setOption(option)
},

// 新老客户-转入金额分布
tab3IntoMoney = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-into-money").width($("#chart-margins").width());
	Page.intoMoney3 = echarts.init(document.getElementById('chart-into-money'));
	var option = {
		color : [ '#66cccc', '#7e9636', '#ff99cc', '#ff6666', '#a6a62c',
				'#cc3399', '#99cc00', '#ff9900', '#666699', '#ffdc89' ],
		tooltip : {
			trigger : 'item',
			formatter : "注册时间({b})<br />{c}元({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ '0-10天', '11-20天', '21-30天', '30天以上' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '转入金额占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : '0-10\u5929'
			}, {
				value : count2,
				name : '11-20\u5929'
			}, {
				value : count3,
				name : '21-30\u5929'
			}, {
				value : count4,
				name : '30\u5929\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.intoMoney3.setOption(option)
},

// 新老客户-转出金额分布
tab3OutMoney = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-out-money").width($("#chart-margins").width());
	Page.outMoney3 = echarts.init(document.getElementById('chart-out-money'));
	var option = {
		color : [ '#ff6666', '#2ec7c9', '#006699', '#ff9966', '#339933',
				'#ffcc33', '#0099cc', '#33cc99', '#cc3366', '#ff6600' ],
		tooltip : {
			trigger : 'item',
			formatter : "注册时间({b})<br />{c}元({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ '0-10天', '11-20天', '21-30天', '30天以上' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '转出金额占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : '0-10\u5929'
			}, {
				value : count2,
				name : '11-20\u5929'
			}, {
				value : count3,
				name : '21-30\u5929'
			}, {
				value : count4,
				name : '30\u5929\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.outMoney3.setOption(option)
},

// 平台-转入金额分布
tab4MoneyEnter = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-money-enter").width($("#chart-margins").width());
	Page.moneyEnter4 = echarts.init(document.getElementById('chart-money-enter'));
	var option = {
		color : [ '#ef9150', '#9bbe40', '#e6626a', '#b398e7', '#9fa9c3' ],
		tooltip : {
			trigger : 'item',
			formatter : "{b}-{a}<br />{c}元({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ 'PC端', '微官网', 'IOS', 'Android' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '转入金额占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : 'PC\u7aef'
			}, {
				value : count2,
				name : '\u5fae\u5b98\u7f51'
			}, {
				value : count3,
				name : 'IOS'
			}, {
				value : count4,
				name : 'Android'
			} ]
		} ]
	};
	Page.moneyEnter4.setOption(option);
},

// 平台-转出金额分布
tab4MoneyOut = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-money-out").width($("#chart-margins").width());
	Page.moneyOut4 = echarts.init(document.getElementById('chart-money-out'));
	var option = {
		color : [ '#f8585b', '#a084d7', '#66cc66', '#20a6e2', '#fbaeae' ],
		tooltip : {
			trigger : 'item',
			formatter : "{b}-{a}<br />{c}元({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ 'PC端', '微官网', 'IOS', 'Android' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '转出金额占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : 'PC\u7aef'
			}, {
				value : count2,
				name : '\u5fae\u5b98\u7f51'
			}, {
				value : count3,
				name : 'IOS'
			}, {
				value : count4,
				name : 'Android'
			} ]
		} ]
	};
	Page.moneyOut4.setOption(option);
}

// 平台-转入用户数分布
tab4NumberEnter = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-number-enter").width($("#chart-margins").width());
	Page.numberEnter4 = echarts.init(document.getElementById('chart-number-enter'));
	var option = {
		color : [ '#47c8c8', '#7e9636', '#ff9900', '#cc3399', '#666699' ],
		tooltip : {
			trigger : 'item',
			formatter : "{b}-{a}<br />{c}人({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ 'PC端', '微官网', 'IOS', 'Android' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '转入用户数占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : 'PC\u7aef'
			}, {
				value : count2,
				name : '\u5fae\u5b98\u7f51'
			}, {
				value : count3,
				name : 'IOS'
			}, {
				value : count4,
				name : 'Android'
			} ]
		} ]
	};
	Page.numberEnter4.setOption(option);
}

// 平台-转出用户数分布
tab4NumberOut = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-number-out").width($("#chart-margins").width());
	Page.numberOut4 = echarts.init(document.getElementById('chart-number-out'));
	var option = {
		color : [ '#61bba2', '#d8734a', '#1f747e', '#ec89cf', '#82c247',
				'#61bba2', '#959595', '#986222', '#db2442', '#693446' ],
		tooltip : {
			trigger : 'item',
			formatter : "{b}-{a}<br />{c}人({d}%)",
			backgroundColor : 'rgba(0,0,0,0.5)',
			textStyle : {
				fontSize : 12
			}
		},
		legend : {
			orient : 'vertical',
			textStyle : {
				color : '#445566',
				fontSize : '12',
				fontFamily : '微软雅黑, Arial, Verdana, sans-serif'
			},
			x : 'right',
			y : '45',
			data : [ 'PC端', '微官网', 'IOS', 'Android' ]
		},
		toolbox : {
			show : false
		},
		calculable : true,
		series : [ {
			name : '转出用户数占比',
			type : 'pie',
			radius : '60%',
			center : [ '50%', '49%' ],
			data : [ {
				value : count1,
				name : 'PC\u7aef'
			}, {
				value : count2,
				name : '\u5fae\u5b98\u7f51'
			}, {
				value : count3,
				name : 'IOS'
			}, {
				value : count4,
				name : 'Android'
			} ]
		} ]
	};
	Page.numberOut4.setOption(option);
};


// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		// 日历选择器
		$('#start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		
	    $("#start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setDate", selectedDate);
	    });
		// 初始化图表
		setTimeout(function() {  
			// 按天统计事件绑定
			$(".panel-heading-tabs a").click();
			//按时间统计事件绑定
			$(".datepicker:text, .datepicker :text").filter(":not([id$=end-date])").change();
		}, 10);
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 按天统计事件绑定
		$(".panel-heading-tabs a").click(Events.chartByDMClkAct);
		//按时间统计事件绑定
		$(".datepicker:text, .datepicker :text").change(Events.chartByDPChgAct);
	 }
});
