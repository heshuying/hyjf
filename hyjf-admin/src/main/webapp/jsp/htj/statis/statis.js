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
		$.each("marginsChart1,balance1,marginsChart2,balance2,totalAmount2,total2,totalAmount4,total6,total4,total5,total7,marginsChart3,balance3,tab2Balance1,tab2Balance2,tab2Balance3,tab2Balance4".split(","), function(c) {
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
		if(viewFlag.indexOf("-1") != -1){
			timeStart = t.val();
			timeEnd = t.parent().parent().find("[id$=end-date]").val();
		}else{
			timeStart = t.parent().find("[id$=start-date]").val();
			timeEnd = t.val();
		}
		var  param = {
				'viewFlag' : viewFlag,
				'daySearch' : timeStart
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
			formatter : " {b} <br/>剩余可投金额: {c}",
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
			data : [ '待成交资产-专属资产' ],
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
			name : '待成交资产-专属资产',
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
			formatter : " {b} <br/>剩余可投金额: {c}",
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
			data : [ '待成交资产-债权转让' ],
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
			name : '待成交资产-债权转让',
			type : 'line',
			data : data
		} ]
	};
	Page.balance1.setOption(option);
},

//数据总览
tab2TotalAmount = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total-amount").width($("#chart-margins").width());
	Page.totalAmount2 = echarts.init(document.getElementById('chart-total-amount'));
	var option = {
		color : [ '#f8585b', '#b6a2de', '#66cc66', '#ffdc89', '#f89135',
				'#20a6e2', '#d87a80', '#2ec7c9', '#a7d252', '#fbaeae' ],
		tooltip : {
			trigger : 'item',
			formatter : "{a}<br />{b}：{c}个({d}%)",
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
			data : [ '1个月', '2 - 4个月', '5 - 8个月', '9 - 12个月', '12 - 24个月',
					'24个月以上' ]
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
				name : '1个月'
			}, {
				value : count2,
				name : '2 - 4个月'
			}, {
				value : count3,
				name : '5 - 8个月'
			}, {
				value : count4,
				name : '9 - 12个月'
			}, {
				value : count5,
				name : '12 - 24个月'
			}, {
				value : count6,
				name : '24个月以上'
			} ]
		} ]
	};
	Page.totalAmount2.setOption(option)
},

// 数据总览
tab2Total = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total").width($("#chart-margins").width());
	Page.total2 = echarts.init(document.getElementById('chart-total'));
	var option = {
		color : [ '#f8585b', '#b6a2de', '#66cc66', '#ffdc89', '#f89135',
				'#20a6e2', '#d87a80', '#2ec7c9', '#a7d252', '#fbaeae' ],
		tooltip : {
			trigger : 'item',
			formatter : "{a}<br />{b}：{c}个({d}%)",
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
			data : [ '1个月', '2 - 4个月', '5 - 8个月', '9 - 12个月', '12 - 24个月',
						'24个月以上'  ]
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
				name : '1个月'
			}, {
				value : count2,
				name : '2 - 4个月'
			}, {
				value : count3,
				name : '5 - 8个月'
			}, {
				value : count4,
				name : '9 - 12个月'
			}, {
				value : count5,
				name : '12 - 24个月'
			}, {
				value : count6,
				name : '24个月以上'
			} ]
		} ]
	};
	Page.total2.setOption(option)
},

// 加入金额
tab4TotalAmount = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total-amount4").width($("#chart-margins").width());
	Page.totalAmount4 = echarts.init(document.getElementById('chart-total-amount4'));
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
			        	  data : [ '0-1万元', '1-2万元', '2-3万元', '3-4万元', '4-5万元',
			        	           '5万元以上' ]
			          },
			          toolbox : {
			        	  show : false
			          },
			          calculable : true,
			          series : [ {
			        	  name : '授权服务金额占比',
			        	  type : 'pie',
			        	  radius : '60%',
			        	  center : [ '50%', '49%' ],
			        	  data : [ {
			        		  value : count1,
			        		  name : '0-1万元'
			        	  }, {
			        		  value : count2,
			        		  name : '1-2万元'
			        	  }, {
			        		  value : count3,
			        		  name : '2-3万元'
			        	  }, {
			        		  value : count4,
			        		  name : '3-4万元'
			        	  }, {
			        		  value : count5,
			        		  name : '4-5万元'
			        	  }, {
			        		  value : count6,
			        		  name : '5万元以上'
			        	  } ]
			          } ]
	};
	Page.totalAmount4.setOption(option)
},

// 资人本金-人数分布
tab4Total = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total4").width($("#chart-margins").width());
	Page.total4 = echarts.init(document.getElementById('chart-total4'));
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
			        	  data : [ '0-1万元', '1-2万元', '2-3万元', '3-4万元', '4-5万元',
			        	           '5万元以上'  ]
			          },
			          toolbox : {
			        	  show : false
			          },
			          calculable : true,
			          series : [ {
			        	  name : '加入人数占比',
			        	  type : 'pie',
			        	  radius : '60%',
			        	  center : [ '50%', '49%' ],
			        	  data : [ {
			        		  value : count1,
			        		  name : '0-1万元'
			        	  }, {
			        		  value : count2,
			        		  name : '1-2万元'
			        	  }, {
			        		  value : count3,
			        		  name : '2-3万元'
			        	  }, {
			        		  value : count4,
			        		  name : '3-4万元'
			        	  }, {
			        		  value : count5,
			        		  name : '4-5万元'
			        	  }, {
			        		  value : count6,
			        		  name : '5万元以上'
			        	  } ]
			          } ]
	};
	Page.total4.setOption(option)
},
//资人本金-人数分布
tab5Total = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total5").width($("#chart-margins").width());
	Page.total5 = echarts.init(document.getElementById('chart-total5'));
	var option = {
			color : [ '#f8585b', '#b6a2de', '#66cc66', '#ffdc89', '#f89135',
			          '#20a6e2', '#d87a80', '#2ec7c9', '#a7d252', '#fbaeae' ],
			          tooltip : {
			        	  trigger : 'item',
			        	  formatter : "{a}<br />{b}：{c}次({d}%)",
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
			        	  data : [ '1次', '2 - 4次', '5 - 8次', '9 - 15次', '16 - 30次',
			        	           '31次以上'  ]
			          },
			          toolbox : {
			        	  show : false
			          },
			          calculable : true,
			          series : [ {
			        	  name : '加入人次人数占比',
			        	  type : 'pie',
			        	  radius : '60%',
			        	  center : [ '50%', '49%' ],
			        	  data : [ {
			        		  value : count1,
			        		  name : '1次'
			        	  }, {
			        		  value : count2,
			        		  name : '2 - 4次'
			        	  }, {
			        		  value : count3,
			        		  name : '5 - 8次'
			        	  }, {
			        		  value : count4,
			        		  name : '9 - 15次'
			        	  }, {
			        		  value : count5,
			        		  name : '16 - 30次'
			        	  }, {
			        		  value : count6,
			        		  name : '31次以上'
			        	  } ]
			          } ]
	};
	Page.total5.setOption(option)
},

//资人本金-人数分布
tab6Total = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total6").width($("#chart-margins").width());
	Page.total6 = echarts.init(document.getElementById('chart-total6'));
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
			        	  data : [ 'PC端', '微官网', 'IOS', 'Android']
			          },
			          toolbox : {
			        	  show : false
			          },
			          calculable : true,
			          series : [ {
			        	  name : '授权服务金额占比',
			        	  type : 'pie',
			        	  radius : '60%',
			        	  center : [ '50%', '49%' ],
			        	  data : [ {
			        		  value : count1,
			        		  name : 'PC端'
			        	  }, {
			        		  value : count2,
			        		  name : '微官网'
			        	  }, {
			        		  value : count3,
			        		  name : 'IOS'
			        	  }, {
			        		  value : count4,
			        		  name : 'Android'
			        	  } ]
			          } ]
	};
	Page.total6.setOption(option)
},
//资人本金-人数分布
tab7Total = function(count1,count2,count3,count4,count5,count6) {
	$("#chart-margins").is(":visible") && $("#chart-total7").width($("#chart-margins").width());
	Page.total7 = echarts.init(document.getElementById('chart-total7'));
	var option = {
			color : [ '#f8585b', '#b6a2de', '#66cc66', '#ffdc89', '#f89135',
			          '#20a6e2', '#d87a80', '#2ec7c9', '#a7d252', '#fbaeae' ],
			          tooltip : {
			        	  trigger : 'item',
			        	  formatter : "{a}<br />{b}：{c}次({d}%)",
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
			        	  data : [ 'PC端', '微官网', 'IOS', 'Android']
			          },
			          toolbox : {
			        	  show : false
			          },
			          calculable : true,
			          series : [ {
			        	  name : '加入人次人数占比',
			        	  type : 'pie',
			        	  radius : '60%',
			        	  center : [ '50%', '49%' ],
			        	  data : [ {
			        		  value : count1,
			        		  name : 'PC端'
			        	  }, {
			        		  value : count2,
			        		  name : '微官网'
			        	  }, {
			        		  value : count3,
			        		  name : 'IOS'
			        	  }, {
			        		  value : count4,
			        		  name : 'Android'
			        	  } ]
			          } ]
	};
	Page.total7.setOption(option)
},
//出借人本金总额
tab1Margins2 = function(days,data) {
	
	Page.marginsChart2 = echarts.init(document.getElementById('chart-margins2'));
	    var option = {
		color : [ '#4bbd00' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>债权数量: {c}",
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
			data : [ '计划持有债权数量-专属资产' ],
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
			'name' : '个',
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
			name : '计划持有债权数量-专属资产',
			type : 'line',
			data :data
		} ]
	};
	Page.marginsChart2.setOption(option);
},

//资管公司账户余额
tab1Balance2 = function(days,data) {
	Page.balance2 = echarts.init(document.getElementById('chart-balance2'));
	var option = {
		color : [ '#0098d9' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>债权数量: {c}",
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
			data : [ '计划持有债权数量-债权转让' ],
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
			'name' : '个',
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
			name : '计划持有债权数量-债权转让',
			type : 'line',
			data : data
		} ]
	};
	Page.balance2.setOption(option);
},

//出借人本金总额
tab1Margins3 = function(days,data) {
	Page.marginsChart3 = echarts.init(document.getElementById('chart-margins3'));
	    var option = {
		color : [ '#4bbd00' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>待还总额: {c}",
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
			data : [ '计划持有债权待还总额' ],
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
			name : '计划持有债权待还总额',
			type : 'line',
			data :data
		} ]
	};
	Page.marginsChart3.setOption(option);
},

//资管公司账户余额
tab1Balance3 = function(days,data) {
	Page.balance3 = echarts.init(document.getElementById('chart-balance3'));
	var option = {
		color : [ '#0098d9' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>已还总额: {c}",
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
			data : [ '计划持有债权已还总额' ],
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
			name : '计划持有债权已还总额',
			type : 'line',
			data : data
		} ]
	};
	Page.balance3.setOption(option);
};
// 出借人加入总额
tab2Balance1 = function(days,data) {
	$("#chart-margins").is(":visible") && $("#chart-tab2Balance1").width($("#chart-margins").width());
	Page.tab2Balance1 = echarts.init(document.getElementById('chart-tab2Balance1'));
	var option = {
		color : [ '#0098d9' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>出借人加入总额: {c}",
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
			data : [ '出借人加入总额' ],
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
			'name' : '金额(万元)',
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
			name : '出借人加入总额',
			type : 'line',
			data : data
		} ]
	};
	Page.tab2Balance1.setOption(option);
},
//应还款总额
tab2Balance2 = function(days,data) {
	$("#chart-margins").is(":visible") && $("#chart-tab2Balance2").width($("#chart-margins").width());
	Page.tab2Balance2 = echarts.init(document.getElementById('chart-tab2Balance2'));
	var option = {
		color : [ '#0098d9' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>应还款总额: {c}",
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
			data : [ '应还款总额' ],
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
			'name' : '金额(万元)',
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
			name : '应还款总额',
			type : 'line',
			data : data
		} ]
	};
	Page.tab2Balance2.setOption(option);
},

// 应还款总额
tab2Balance3 = function(days,data) {
	$("#chart-margins").is(":visible") && $("#chart-tab2Balance3").width($("#chart-margins").width());
	Page.tab2Balance3 = echarts.init(document.getElementById('chart-tab2Balance3'));
	var option = {
		color : [ '#0098d9' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>已还款总额: {c}",
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
			data : [ '已还款总额' ],
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
			'name' : '金额(万元)',
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
			name : '已还款总额',
			type : 'line',
			data : data
		} ]
	};
	Page.tab2Balance3.setOption(option);
},

// 到期公允价值
tab2Balance4 = function(days,data) {
	$("#chart-margins").is(":visible") && $("#chart-tab2Balance4").width($("#chart-margins").width());
	Page.tab2Balance4 = echarts.init(document.getElementById('chart-tab2Balance4'));
	var option = {
		color : [ '#0098d9' ],
		tooltip : {
			trigger : 'axis',
			formatter : " {b} <br/>到期公允价值: {c}",
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
			data : [ '到期公允价值' ],
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
			'name' : '金额(万元)',
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
			name : '到期公允价值',
			type : 'line',
			data : data
		} ]
	};
	Page.tab2Balance4.setOption(option);
},

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
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
