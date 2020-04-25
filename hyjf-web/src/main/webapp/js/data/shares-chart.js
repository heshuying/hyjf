var sharesTool = {
	timeTicket : null
}
// 设置存储容器
var charts = []; // 存放图表
var options = []; // 存放配置项
var sdates = []; // 存放series 数据
// 初始化图表
initChart(0);
$(".shares-panel-tab .item[data-panel=0]").addClass("loaded");


function initChart(i){
	/*
	 * 初始化图表
	 * */
	charts[i] = echarts.init(document.getElementById('chart' + i));
	(function(i) {
		$.ajax({
			type : "post",
			url : sharesurl,
			data : {
				type : i + 1
			},
			dataType : "json",
			success : function(data) {
				var shares_data = data.StockInfoList; // 格式化数据
				if (i == "0") {
					setInfoData(data.stockinfo); // 填充股票信息
				}
				if(shares_data.length == 0){
					$("#chart"+i).children().text("暂无数据");
					return false;
				}
				sdates[i] = shares_data;// 存储数据
				options[i] = setInitOption(i, shares_data,data.stockinfo.dayHigh);// 格式化配置信息
				charts[i].setOption(options[i]);// 初始化图表
				
			}
		})
	})(i);
}
function __fmtSingleDigit(num) {
	/*
	 * 个位数补零
	 * 
	 */
	if (num > 9) {
		return num;
	} else {
		return "0" + num;
	}
}

function setInitOption(idx, shares_data,dayHigh) {
	var seriesName;
	var splitNumber = 5;
	var onehour = 60*60*1000;
	var oneday = 24*onehour;
	var minDate = new Date(shares_data[0].value[0]);
	var maxDate = new Date(minDate);
	var interval = null;
	if (idx == "0") {
		seriesName = "分时";
		splitNumber = 6;
		maxDate = new Date(+maxDate+onehour*11);
	} else if (idx == "1") {
		seriesName = "5日";
		splitNumber = 6;
		maxDate = new Date(+maxDate+oneday*5);
		
	} else if (idx == "2") {
		seriesName = "1月";
		splitNumber = 0;
		maxDate = new Date(+maxDate+oneday*30);
		interval = minDate;
	} else if (idx == "3") {
		seriesName = "1年";
		splitNumber = 2;
		maxDate.setFullYear(maxDate.getFullYear()+1);
		interval = minDate;
	}
	maxDate.setHours(6);
	maxDate.setMinutes(0);
	maxDate.setSeconds(0);
	return {
		tooltip : {
			trigger : 'axis',
			formatter : function(params) {
				params = params[0];
				var date = new Date(params.name);
				if (idx == "2" || idx == "3") {
					return __fmtSingleDigit(date.getFullYear()) + '/'
					+ __fmtSingleDigit(date.getMonth() + 1) + '/'
					+ __fmtSingleDigit(date.getDate()) + "<br/>"
					+ "当前价 " + toDecimal2(params.value[1]);
				}else{
					return __fmtSingleDigit(date.getFullYear()) + '/'
					+ __fmtSingleDigit(date.getMonth() + 1) + '/'
					+ __fmtSingleDigit(date.getDate()) + "<br/>"
					+ __fmtSingleDigit(date.getHours()) + ':'
					+ __fmtSingleDigit(date.getMinutes()) + '<br/>'
					+ "当前价 " + toDecimal2(params.value[1]);
				}
				
			}
		},
		grid : {
			top : "3%",
			left : '3%',
			right : '6%',
			bottom : '0',
			containLabel : true
		},
		xAxis : {
			type : 'time',
			boundaryGap : false,
			splitLine : {
				show : false,
			},
			axisLine : {
				onZero : false,
			},
			axisLabel : {
				formatter : function(value, index) {
					var date = new Date(value);
					if (idx == "0") {
						var texts = [ __fmtSingleDigit(date.getHours()),
								__fmtSingleDigit(date.getMinutes()) ];
						return texts.join(':');
					} else if(idx == "3"){
						var texts = [ __fmtSingleDigit(date.getFullYear()),__fmtSingleDigit(date.getMonth() + 1)
								 ];
						return texts.join('/');
					} else {
						var texts = [ __fmtSingleDigit(date.getMonth() + 1),
										__fmtSingleDigit(date.getDate()) ];
								return texts.join('/');
					}
				}
			},
			splitNumber:splitNumber,
			interval:interval,
			max:maxDate,
			min:minDate
			
		},
		yAxis : {
			type : 'value',
			boundaryGap : [ 0, '100%' ],
			max:Math.ceil(dayHigh)+2
		},
		series : [ {
			name : seriesName,
			type : 'line',
			data : shares_data,
			lineStyle : {
				normal : {
					color : "#2397f0",
					width:1
				}
			},
			showSymbol : false,
			hoverAnimation : false,
			itemStyle : {
				normal : {
					color : "#2397f0"
				}
			}
		} ]
	};
}
function setInfoData(data) {
	// 设置信息
	var infoDom = {
		"now" : $("#sharesNow"),// 当前价格
		"date" : $("#sharesDate"),
		"status" : $("#sharesStatus"),// 状态
		"statusInfo" : $("#sharesStatusInfo"),// 状态信息
		"open" : $("#dataOpen"), // 今开
		"close" : $("#dataPreviousClose"), // 昨收
		"high" : $("#dataHigh"), // 最高
		"low" : $("#dataLow"), // 最低
		"volume" : $("#dataVolume"), // 成交量
		"pe" : $("#dataPERatio"), // 市盈率
		"eps" : $("#dataEPS"),// 每股收益
		"cap" : $("#dataMarketCap")
	// 总市值
	}
	var d = new Date(data.date * 1000);
	var datestr = d.getFullYear() + "/" + __fmtSingleDigit(d.getMonth() + 1)
			+ "/" + __fmtSingleDigit(d.getDate()) + " "
			+ __fmtSingleDigit(d.getHours()) + ":"
			+ __fmtSingleDigit(d.getMinutes());
	infoDom.now.text(toDecimal2(data.nowPrice));// 当前价格
	infoDom.date.text(datestr);// 当前日期
	infoDom.open.text(toDecimal2(data.openPrice));// 今开
	infoDom.close.text(toDecimal2(data.previousClosePrice));// 昨收
	infoDom.high.text(toDecimal2(data.dayHigh));// 最高
	infoDom.low.text(toDecimal2(data.dayLow));// 最低
	infoDom.volume.text(data.volume+"万");// 成交量
	infoDom.pe.text(toDecimal2(data.peRatio));// 市盈率
	infoDom.eps.text(toDecimal2(data.eps));// 每股收益
	infoDom.cap.text(toDecimal2(data.marketCap)+"百万");// 总市值

	if (data.increase > 0) {
		infoDom.status.removeClass("iconfont-down").addClass("iconfont-up").parent().removeClass("green").addClass("red"); // 设置上涨和持平时箭头 ; 设置上涨为红色
		infoDom.statusInfo.text("+" + toDecimal2(Math.abs(data.nowPrice - data.openPrice)) + " (+"
				+ toDecimal2(data.increase) + "%)"); // 设置涨幅信息
	} else if (data.decline > 0) {
		infoDom.status.removeClass("iconfont-up").addClass("iconfont-down").parent().removeClass("red").addClass("green");// 设置下跌时箭头 ; 设置下跌为绿色
		infoDom.statusInfo.text("-" + toDecimal2(Math.abs(data.nowPrice - data.openPrice)) + " (-"
				+ toDecimal2(data.decline) + "%)"); // 设置涨幅信息
	} else {
		infoDom.status.removeClass("iconfont-down").removeClass("iconfont-up").parent().removeClass("green").addClass("red"); // 设置持平时箭头 ;  设置持平为红色
		infoDom.statusInfo.text("+0.00 (+0.00%)"); // 设置涨幅信息
	}
}

$(function() {
	$("#sharesTabTag .item").click(
			function() {
				var self = $(this);
				var idx = self.data("panel");
				var panel = $("#sharesTabBody");
				self.addClass("active").siblings().removeClass("active");
				panel.children("[data-panel=" + idx + "]").addClass("active")
						.siblings().removeClass("active");
				if(!self.hasClass("loaded")){
					initChart(idx);
					self.addClass("loaded");
				}
			});
})
function updateSharesData(chart, sdata) {
	/*
	 * 更新股票数据
	 */
	var option = {
		series : [ {
			data : sdata
		} ]
	}
	chart.setOption(option);
}
sharesTool.timeTicket = setInterval(function() {
	// 每分钟更新股票数据 "type=1"为时分图表
	$.ajax({
		type : "post",
		url : sharesurl,
		data : {
			type : 1
		},
		dataType : "json",
		success : function(data) {
			var shares_data = data.StockInfoList; // 格式化数据
			setInfoData(data.stockinfo); // 填充股票信息
			if(shares_data.length == 0){
				$("#chart0").children().text("暂无数据");
				return false;
			}
			sdates[0] = shares_data;// 存储数据
			updateSharesData(charts[0], shares_data); // 更新重绘图表
			
		}
	})
}, 60000);
