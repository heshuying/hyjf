var
// --------------------------------------------------------------------------------------------------------------------------------

/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出execlAction
	initAction : "initAction",
	// 查找的Action
	searchAction : "searchAction"
},
/* 事件动作处理 */
Events = {
	
},

// 出借人本金总额
tab1Margins = function(days,data) {
	alert(days);
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
			
//			data : [ '2015-05-31', '2015-06-01', '2015-06-02', '2015-06-03',
//					'2015-06-04', '2015-06-05', '2015-06-06', '2015-06-07',
//					'2015-06-08', '2015-06-09', '2015-06-10', '2015-06-11',
//					'2015-06-12', '2015-06-13', '2015-06-14', '2015-06-15',
//					'2015-06-16', '2015-06-17', '2015-06-18', '2015-06-19',
//					'2015-06-19', '2015-06-20', '2015-06-21', '2015-06-22',
//					'2015-06-23', '2015-06-24', '2015-06-25', '2015-06-26',
//					'2015-06-27', '2015-06-28', '2015-06-29', '2015-06-30',
//					'2015-07-15', '2015-07-16', '2015-07-17', '2015-12-19' ]
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
//			data : [ 163.62, 215.29, 225.37, 171.88, 197.51, 222.68, 225.17,
//					243.43, 255.96, 212.99, 192.67, 199.85, 247.87, 246.2,
//					247.86, 222.98, 234.25, 228.1, 245.64, 197.51, 197.51,
//					222.68, 225.17, 243.43, 255.96, 212.99, 192.67, 199.85,
//					247.87, 246.2, 247.86, 222.98, 234.25, 228.1, 245.64,
//					1869.74 ]
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
//			data : [ '2015-05-31', '2015-06-01', '2015-06-02', '2015-06-03',
//					'2015-06-04', '2015-06-05', '2015-06-06', '2015-06-07',
//					'2015-06-08', '2015-06-09', '2015-06-10', '2015-06-11',
//					'2015-06-12', '2015-06-13', '2015-06-14', '2015-06-15',
//					'2015-06-16', '2015-06-17', '2015-06-18', '2015-06-19',
//					'2015-06-19', '2015-06-20', '2015-06-21', '2015-06-22',
//					'2015-06-23', '2015-06-24', '2015-06-25', '2015-06-26',
//					'2015-06-27', '2015-06-28', '2015-06-29', '2015-06-30',
//					'2015-07-15', '2015-07-16', '2015-07-17', '2015-12-19' ]
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
//			data : [ 203.13, 254.76, 264.83, 211.32, 236.95, 262.08, 264.56,
//					282.82, 295.32, 252.32, 231.97, 239.11, 287.11, 285.43,
//					287.08, 262.16, 273.39, 267.22, 284.75, 236.95, 236.95,
//					262.08, 264.56, 282.82, 295.32, 252.32, 231.97, 239.11,
//					287.11, 285.43, 287.08, 262.16, 273.39, 267.22, 284.75,
//					1076.63 ]
			data : data
		} ]
	};
	Page.balance1.setOption(option);
},

//
tab1TurnMoney = function() {
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
			data : [ '2015-05-31', '2015-06-01', '2015-06-02', '2015-06-03',
					'2015-06-04', '2015-06-05', '2015-06-06', '2015-06-07',
					'2015-06-08', '2015-06-09', '2015-06-10', '2015-06-11',
					'2015-06-12', '2015-06-13', '2015-06-14', '2015-06-15',
					'2015-06-16', '2015-06-17', '2015-06-18', '2015-06-19',
					'2015-06-19', '2015-06-20', '2015-06-21', '2015-06-22',
					'2015-06-23', '2015-06-24', '2015-06-25', '2015-06-26',
					'2015-06-27', '2015-06-28', '2015-06-29', '2015-06-30',
					'2015-07-15', '2015-07-16', '2015-07-17', '2015-12-19' ]
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
					data : [ 0.93, 95.82, 34.65, 19.71, 38.19, 52.71, 13.45,
							29.27, 38.66, 31.18, 18.88, 25.35, 80.03, 11.14,
							22.12, 19.64, 31.2, 12.02, 36.78, 38.19, 38.19,
							52.71, 13.45, 29.27, 38.66, 31.18, 18.88, 25.35,
							80.03, 11.14, 22.12, 19.64, 31.2, 12.02, 36.78, 0 ]
				},
				{
					name : '转出金额',
					type : 'bar',
					data : [ 7.47, 33.9, 24.57, 73.2, 12.56, 27.55, 10.96, 11,
							26.13, 74.15, 39.19, 18.18, 32.01, 12.81, 20.46,
							44.52, 19.93, 18.17, 19.24, 12.56, 12.56, 27.55,
							10.96, 11, 26.13, 74.15, 39.19, 18.18, 32.01,
							12.81, 20.46, 44.52, 19.93, 18.17, 19.24, 0 ]
				} ],
	};
	Page.turnMoney1.setOption(option)
},

// 转入转出用户
tab1TurnUser = function() {
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
			data : [ '2015-05-31', '2015-06-01', '2015-06-02', '2015-06-03',
					'2015-06-04', '2015-06-05', '2015-06-06', '2015-06-07',
					'2015-06-08', '2015-06-09', '2015-06-10', '2015-06-11',
					'2015-06-12', '2015-06-13', '2015-06-14', '2015-06-15',
					'2015-06-16', '2015-06-17', '2015-06-18', '2015-06-19',
					'2015-06-19', '2015-06-20', '2015-06-21', '2015-06-22',
					'2015-06-23', '2015-06-24', '2015-06-25', '2015-06-26',
					'2015-06-27', '2015-06-28', '2015-06-29', '2015-06-30',
					'2015-07-15', '2015-07-16', '2015-07-17', '2015-12-19' ]
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
					data : [ 4, 92, 77, 48, 62, 78, 18, 22, 39, 62, 45, 61, 81,
							28, 19, 63, 55, 54, 66, 62, 62, 78, 18, 22, 39, 62,
							45, 61, 81, 28, 19, 63, 55, 54, 66, 0 ]
				},
				{
					name : '转出用户',
					type : 'bar',
					data : [ 10, 45, 27, 40, 24, 29, 7, 8, 25, 47, 28, 27, 42,
							13, 16, 39, 30, 24, 39, 24, 24, 29, 7, 8, 25, 47,
							28, 27, 42, 13, 16, 39, 30, 24, 39, 0 ]
				} ]
	};
	Page.turnUser1.setOption(option)
},

// 出借人本金-金额分布
tab2TotalAmount = function() {
	$("#chart-total-amount").width($("#chart-margins").width());
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
				value : 1520550,
				name : '0 - 1\u4e07\u5143'
			}, {
				value : 1099323,
				name : '1 - 2\u4e07\u5143'
			}, {
				value : 755105,
				name : '2 - 3\u4e07\u5143'
			}, {
				value : 857750,
				name : '3 - 4\u4e07\u5143'
			}, {
				value : 959807,
				name : '4 - 5\u4e07\u5143'
			}, {
				value : 13504881,
				name : '5\u4e07\u5143\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.totalAmount2.setOption(option)
},

// 资人本金-人数分布
tab2Total = function() {
	$("#chart-total").width($("#chart-margins").width());
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
				value : 5937,
				name : '0 - 1\u4e07\u5143'
			}, {
				value : 86,
				name : '1 - 2\u4e07\u5143'
			}, {
				value : 32,
				name : '2 - 3\u4e07\u5143'
			}, {
				value : 25,
				name : '3 - 4\u4e07\u5143'
			}, {
				value : 21,
				name : '4 - 5\u4e07\u5143'
			}, {
				value : 83,
				name : '5\u4e07\u5143\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.total2.setOption(option)
},

// 转出金额-人次分布
tab2OutTotal = function() {
	$("#chart-out-total").width($("#chart-margins").width());
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
				value : 8970,
				name : '0 - 1\u4e07\u5143'
			}, {
				value : 815,
				name : '1 - 2\u4e07\u5143'
			}, {
				value : 409,
				name : '2 - 3\u4e07\u5143'
			}, {
				value : 231,
				name : '3 - 4\u4e07\u5143'
			}, {
				value : 384,
				name : '4 - 5\u4e07\u5143'
			}, {
				value : 527,
				name : '5\u4e07\u5143\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.outTotal2.setOption(option)
},

// 转入金额-人次分布
tab2IntoTotal = function() {
	$("#chart-into-total").width($("#chart-margins").width());
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
				value : 13883,
				name : '0 - 1\u4e07\u5143'
			}, {
				value : 899,
				name : '1 - 2\u4e07\u5143'
			}, {
				value : 454,
				name : '2 - 3\u4e07\u5143'
			}, {
				value : 256,
				name : '3 - 4\u4e07\u5143'
			}, {
				value : 406,
				name : '4 - 5\u4e07\u5143'
			}, {
				value : 615,
				name : '5\u4e07\u5143\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.intoTotal2.setOption(option)
},

// 新老客户-出借人数分布
tab3CustomerNumber = function() {
	$("#chart-customer-number").width($("#chart-margins").width());
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
				value : 0,
				name : '0-10\u5929'
			}, {
				value : 0,
				name : '11-20\u5929'
			}, {
				value : 1,
				name : '21-30\u5929'
			}, {
				value : 6183,
				name : '30\u5929\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.customerNumber3.setOption(option);
},

// 新老客户-本金金额分布
tab3CustomerSum = function() {
	$("#chart-customer-sum").width($("#chart-margins").width());
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
				value : 0,
				name : '0-10\u5929'
			}, {
				value : 0,
				name : '11-20\u5929'
			}, {
				value : 900,
				name : '21-30\u5929'
			}, {
				value : 18696516,
				name : '30\u5929\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.customerSum3.setOption(option)
},

// 新老客户-转入金额分布
tab3IntoMoney = function() {
	$("#chart-into-money").width($("#chart-margins").width());
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
				value : 0,
				name : '0-10\u5929'
			}, {
				value : 0,
				name : '11-20\u5929'
			}, {
				value : 900,
				name : '21-30\u5929'
			}, {
				value : 154323413,
				name : '30\u5929\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.intoMoney3.setOption(option)
},

// 新老客户-转出金额分布
tab3OutMoney = function() {
	$("#chart-out-money").width($("#chart-margins").width());
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
				value : 0,
				name : '0-10\u5929'
			}, {
				value : 0,
				name : '11-20\u5929'
			}, {
				value : 0,
				name : '21-30\u5929'
			}, {
				value : 135626887,
				name : '30\u5929\u4ee5\u4e0a'
			} ]
		} ]
	};
	Page.outMoney3.setOption(option)
},

// 平台-转入金额分布
tab4MoneyEnter = function() {
	$("#chart-money-enter").width($("#chart-margins").width());
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
				value : 146852083.00,
				name : 'PC\u7aef'
			}, {
				value : 7283047.00,
				name : '\u5fae\u5b98\u7f51'
			}, {
				value : 189183.00,
				name : 'IOS'
			}, {
				value : 0,
				name : 'Android'
			} ]
		} ]
	};
	Page.moneyEnter4.setOption(option);
},

// 平台-转出金额分布
tab4MoneyOut = function() {
	$("#chart-money-out").width($("#chart-margins").width());
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
				value : 101302.00,
				name : 'PC\u7aef'
			}, {
				value : 201.00,
				name : '\u5fae\u5b98\u7f51'
			}, {
				value : 47809.00,
				name : 'IOS'
			}, {
				value : 0,
				name : 'Android'
			} ]
		} ]
	};
	Page.moneyOut4.setOption(option);
}

// 平台-转入用户数分布
tab4NumberEnter = function() {
	$("#chart-number-enter").width($("#chart-margins").width());
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
				value : 6106,
				name : 'PC\u7aef'
			}, {
				value : 284,
				name : '\u5fae\u5b98\u7f51'
			}, {
				value : 6,
				name : 'IOS'
			}, {
				value : 0,
				name : 'Android'
			} ]
		} ]
	};
	Page.numberEnter4.setOption(option);
}

// 平台-转出用户数分布
tab4NumberOut = function() {
	$("#chart-number-out").width($("#chart-margins").width());
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
				value : 3,
				name : 'PC\u7aef'
			}, {
				value : 1,
				name : '\u5fae\u5b98\u7f51'
			}, {
				value : 2,
				name : 'IOS'
			}, {
				value : 0,
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
			// 出借人本金总额
//			tab1Margins();
			// 资管公司账户余额
//			tab1Balance();
			// 转入转出金额
			tab1TurnMoney();
			// 转入转出用户
			tab1TurnUser();
			// 出借人本金-金额分布
			tab2TotalAmount();
			// 资人本金-人数分布
			tab2Total();
			// 转出金额-人次分布
			tab2OutTotal();
			// 转入金额-人次分布
			tab2IntoTotal();
			// 新老客户-出借人数分布
			tab3CustomerNumber();
			// 新老客户-本金金额分布
			tab3CustomerSum();
			// 新老客户-转入金额分布
			tab3IntoMoney();
			// 新老客户-转出金额分布
			tab3OutMoney();
			// 平台-转入金额分布
			tab4MoneyEnter();
			// 平台-转出金额分布
			tab4MoneyOut();
			// 平台-转入用户数分布
			tab4NumberEnter();
			// 平台-转出用户数分布
			tab4NumberOut();
		}, 10);
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 出借人本金总额
		$("#tab_sjzl_1 .fn-dayView").click(function() {
			var param = $(this).data("paramvalue");
			$.post(Action.searchAction, {'viewFlag' : param}, function(rs) {
				//alert(rs.days);
				// 出借人本金总额
				tab1Margins(rs.days,rs.data);
				// 资管公司账户余额
				tab1Balance(rs.days,rs.data);
			}).error(function() {
				console.info("error!");
			})
		}).click();
	 }
});
