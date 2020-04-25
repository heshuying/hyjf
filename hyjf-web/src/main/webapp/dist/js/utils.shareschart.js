/*
 * 股票窗口图表
 * 
 */
var utils = utils || {};

$.extend(utils, {
    /*
     * 图表变量
     */
    variable: {
        sharesTicket: null, //更新定时器
        sharesCharts: [], // 存放图表元素
        sharesOptions: [], // 存放配置项
        sharesSdates: [], // 存放series 数据
        sharesUrl: "https://www.hyjf.com/stockInfo/queryStockinfoAction.do" //数据接口
            //sharesUrl: "../dist/js/shares-data.json" //数据接口
    },
    /*
     * @fun 初始化图表 
     * @param i [number] 初始化的图标下标（0:分时,1:5日,2:1月,3:一年）
     */
    initChart: function(i) {
        var _this = this;
        _this.variable.sharesCharts[i] = echarts.init(document.getElementById('chart' + i));
        _this.chartAjax(i);
    },
    /*
     * @fun 调用异步接口
     * @param i [number] 初始化的图标下标（0:分时,1:5日,2:1月,3:一年）
     */
    chartAjax: function(i) {
        var _this = this;

        $.ajax({
            type: "post",
            url: _this.variable.sharesUrl,
            data: {
                type: i + 1
            },
            dataType: "json",
            success: function(data) {
                var shares_data = data.StockInfoList; // 格式化数据
                if (i == "0") {
                    _this.setInfoData(data.stockinfo); // 填充股票信息
                }
                if (shares_data.length === 0) {
                    $("#chart" + i).children().text("暂无数据");
                    return false;
                }
                _this.variable.sharesSdates[i] = shares_data; // 存储数据
                _this.variable.sharesOptions[i] = _this.setInitOption(i, shares_data, data.stockinfo.dayHigh); // 格式化配置信息
                _this.variable.sharesCharts[i].setOption(_this.variable.sharesOptions[i]); // 初始化图表

            }
        });
    },
    /*
     * @fun 设置数据
     * @param i [number] 初始化的图标下标（0:分时,1:5日,2:1月,3:一年）
     */
    setInfoData: function(data) {
        var _this = this;
        // 设置信息
        var infoDom = {
            "now": $("#sharesNow"), // 当前价格
            "date": $("#sharesDate"),
            "status": $("#sharesStatus"), // 状态
            "statusInfo": $("#sharesStatusInfo"), // 状态信息
            "open": $("#dataOpen"), // 今开
            "close": $("#dataPreviousClose"), // 昨收
            "high": $("#dataHigh"), // 最高
            "low": $("#dataLow"), // 最低
            "volume": $("#dataVolume"), // 成交量
            "pe": $("#dataPERatio"), // 市盈率
            "eps": $("#dataEPS"), // 每股收益
            "cap": $("#dataMarketCap")
                // 总市值
        };
        var d = new Date(data.date * 1000);
        var datestr = d.getFullYear() + "/" + _this.fmtSingleDigit(d.getMonth() + 1) + "/" + _this.fmtSingleDigit(d.getDate()) + " " + _this.fmtSingleDigit(d.getHours()) + ":" + _this.fmtSingleDigit(d.getMinutes());
        infoDom.now.text(_this.toDecimal2(data.nowPrice)); // 当前价格
        infoDom.date.text(datestr); // 当前日期
        infoDom.open.text(_this.toDecimal2(data.openPrice)); // 今开
        infoDom.close.text(_this.toDecimal2(data.previousClosePrice)); // 昨收
        infoDom.high.text(_this.toDecimal2(data.dayHigh)); // 最高
        infoDom.low.text(_this.toDecimal2(data.dayLow)); // 最低
        infoDom.volume.text(data.volume + "万"); // 成交量
        infoDom.pe.text(_this.toDecimal2(data.peRatio)); // 市盈率
        infoDom.eps.text(_this.toDecimal2(data.eps)); // 每股收益
        infoDom.cap.text(_this.toDecimal2(data.marketCap) + "百万"); // 总市值

        if (data.increase > 0) {
            infoDom.status.removeClass("iconfont-down").addClass("iconfont-up").parent().removeClass("green").addClass("red"); // 设置上涨和持平时箭头 ; 设置上涨为红色
            infoDom.statusInfo.text("+" + _this.toDecimal2(Math.abs(data.nowPrice - data.openPrice)) + " (+" + _this.toDecimal2(data.increase) + "%)"); // 设置涨幅信息
        } else if (data.decline > 0) {
            infoDom.status.removeClass("iconfont-up").addClass("iconfont-down").parent().removeClass("red").addClass("green"); // 设置下跌时箭头 ; 设置下跌为绿色
            infoDom.statusInfo.text("-" + _this.toDecimal2(Math.abs(data.nowPrice - data.openPrice)) + " (-" + _this.toDecimal2(data.decline) + "%)"); // 设置涨幅信息
        } else {
            infoDom.status.removeClass("iconfont-down").removeClass("iconfont-up").parent().removeClass("green").addClass("red"); // 设置持平时箭头 ;  设置持平为红色
            infoDom.statusInfo.text("+0.00 (+0.00%)"); // 设置涨幅信息
        }
    },
    /*
     * @fun 更新股票数据
     * @param chart [obj] sharesCharts中的图表
     * @param sdata [json] 预设置的图表数据
     */
    updateSharesData: function(chart, sdata) {
        var option = {
            series: [{
                data: sdata
            }]
        };
        chart.setOption(option);
    },
    /*
     * @fun 设置股票配置
     * @param idx [number] 初始化的图标下标（0:分时,1:5日,2:1月,3:一年）
     * @param shares_data [json] 预设置的图表数据
     * @param dayHigh [number] 本日最高值
     */
    setInitOption: function(idx, shares_data, dayHigh) {
        var _this = this;
        var seriesName;
        var splitNumber = 5;
        var onehour = 60 * 60 * 1000;
        var oneday = 24 * onehour;
        var minDate = new Date(shares_data[0].value[0]);
        var maxDate = new Date(minDate);
        var interval = null;
        if (idx == "0") {
            seriesName = "分时";
            splitNumber = 6;
            maxDate = new Date(+maxDate + onehour * 11);
        } else if (idx == "1") {
            seriesName = "5日";
            splitNumber = 6;
            maxDate = new Date(+maxDate + oneday * 5);

        } else if (idx == "2") {
            seriesName = "1月";
            splitNumber = 0;
            maxDate = new Date(+maxDate + oneday * 30);
            interval = minDate;
        } else if (idx == "3") {
            seriesName = "1年";
            splitNumber = 2;
            maxDate.setFullYear(maxDate.getFullYear() + 1);
            interval = minDate;
        }
        maxDate.setHours(6);
        maxDate.setMinutes(0);
        maxDate.setSeconds(0);
        return {
            tooltip: {
                trigger: 'axis',
                formatter: function(params) {
                    params = params[0];
                    var date = new Date(params.name);
                    if (idx == "2" || idx == "3") {
                        return _this.fmtSingleDigit(date.getFullYear()) + '/' + _this.fmtSingleDigit(date.getMonth() + 1) + '/' + _this.fmtSingleDigit(date.getDate()) + "<br/>" + "当前价 " + _this.toDecimal2(params.value[1]);
                    } else {
                        return _this.fmtSingleDigit(date.getFullYear()) + '/' + _this.fmtSingleDigit(date.getMonth() + 1) + '/' + _this.fmtSingleDigit(date.getDate()) + "<br/>" + _this.fmtSingleDigit(date.getHours()) + ':' + _this.fmtSingleDigit(date.getMinutes()) + '<br/>' + "当前价 " + _this.toDecimal2(params.value[1]);
                    }
                }
            },
            grid: {
                top: "3%",
                left: '3%',
                right: '6%',
                bottom: '0',
                containLabel: true
            },
            xAxis: {
                type: 'time',
                boundaryGap: false,
                splitLine: {
                    show: false,
                },
                axisLine: {
                    onZero: false,
                },
                axisLabel: {
                    formatter: function(value, index) {
                        var date = new Date(value);
                        var texts;
                        if (idx == "0") {
                            texts = [_this.fmtSingleDigit(date.getHours()), _this.fmtSingleDigit(date.getMinutes())];
                            return texts.join(':');
                        } else if (idx == "3") {
                            texts = [_this.fmtSingleDigit(date.getFullYear()), _this.fmtSingleDigit(date.getMonth() + 1)];
                            return texts.join('/');
                        } else {
                            texts = [_this.fmtSingleDigit(date.getMonth() + 1), _this.fmtSingleDigit(date.getDate())];
                            return texts.join('/');
                        }
                    }
                },
                splitNumber: splitNumber,
                interval: interval,
                max: maxDate,
                min: minDate
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%'],
                max: Math.ceil(dayHigh) + 2
            },
            series: [{
                name: seriesName,
                type: 'line',
                data: shares_data,
                lineStyle: {
                    normal: {
                        color: "#2397f0",
                        width: 1
                    }
                },
                showSymbol: false,
                hoverAnimation: false,
                itemStyle: {
                    normal: {
                        color: "#2397f0"
                    }
                }
            }]
        };
    },
    /*
     * 绑定股票窗口操作事件
     */
    shareTabAction: function() {
        var _this = this;
        $("#sharesTabTag .item").click(function() {
            var self = $(this);
            var idx = self.data("panel");
            var panel = $("#sharesTabBody");
            self.addClass("active").siblings().removeClass("active");
            panel.children("[data-panel=" + idx + "]").addClass("active")
                .siblings().removeClass("active");
            if (!self.hasClass("loaded")) {
                _this.initChart(idx);
                self.addClass("loaded");
            }
        });

        //（传入0，type为0+1，是sharesCharts下标）为时分图表
        //初始化第一个,分时表
        $(".shares-panel-tab .item[data-panel=0]").addClass("loaded");

        _this.initChart(0);

        _this.variable.sharesTicket = setInterval(function() {
            // 每分钟更新股票数据 "type=1" 
            _this.chartAjax(0);
        }, 60000);
    },
    /*
     * 绑定股票窗口操作
     */
    initShare: function() {
        $("#gupiaoItem").hover(function() {
            $("#gupiaoPanel").stop().fadeIn(300);
        }, function() {
            $("#gupiaoPanel").fadeOut(300);
        });
        this.shareTabAction();// 初始化股票图表
    },
    //app下载hover弹层
    saoShare:function(){
    	$("#saomaItem").hover(function() {
            $("#saoma").stop().fadeIn(300);
        }, function() {
            $("#saoma").fadeOut(300);
        });
//      //弹层里的二维码切换
//      $('#saoma .edition a').each(function(index,el){
//      	$(el).click(function(){
//      		if(!$(el).hasClass()){
//      			$('#saoma .edition a').removeClass('show');
//      			$('#saoma img').removeClass('show');
//      			$('#saoma img').eq(index).addClass('show')
//      			$(el).addClass('show')
//      		}
//      	})
//      })
    }
});
$(function(){
    utils.initShare();
    utils.saoShare();
    
    //头部手机下载
	$(".top-bar .tb3").hover(function() {
        $(".top-bar .saoma").stop().fadeIn(300);
    }, function() {
        $(".top-bar .saoma").fadeOut(300);
    });
    $(".top-bar .saoma").hover(function() {
        $(".top-bar .saoma").stop().fadeIn(300);
    }, function() {
        $(".top-bar .saoma").fadeOut(300);
    });
//  //弹层里的二维码切换
//  $('.top-bar .saoma .edition a').each(function(index,el){
//  	$(el).click(function(){
//  		if(!$(el).hasClass()){
//  			$('.top-bar .saoma .edition a').removeClass('show');
//  			$('.top-bar .saoma img').removeClass('show');
//  			$('.top-bar .saoma img').eq(index).addClass('show')
//  			$(el).addClass('show')
//  		}
//  	})
//  })
})
