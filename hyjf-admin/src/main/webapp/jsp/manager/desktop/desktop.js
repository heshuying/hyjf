// 平台数据总览
function chart1() {
	Page.chart1 = echarts.init(document.getElementById('chart1'));
	var option = {
		color: ['#20a6e2', '#F89135', '#66CC66' ],
		tooltip: {
			trigger: 'axis',
			backgroundColor: 'rgba(0,0,0,0.5)',
			axisPointer: {type: 'shadow'},
			textStyle: {fontSize : 12}
		},
		legend: {
			data: ['汇直投', '汇天利', '汇转让'],
			textStyle: {fontFamily: '微软雅黑, Arial, Verdana, sans-serif'},
			y: '5%',
		},
		grid: {
			x: '7%',
			y: '15%',
			x2: '1%',
			y2: '10%',
			borderWidth:1,
			borderColor:'#eee'
		},
		toolbox: {
			show: false
		},
		calculable: true,
		xAxis: [{
			type: 'category',
			axisLine: {lineStyle: {color: '#999',width: 1,type: 'solid'}},
			splitLine:{lineStyle:{color: '#eee',width: 1,type: 'solid'}},
			axisLabel:{textStyle: {fontFamily: '微软雅黑, Arial, Verdana, sans-serif',color: '#666'}},
			data: ['2014-12','2015-01','2015-02','2015-03','2015-04','2015-05','2015-06','2015-07','2015-08','2015-09','2015-10','2015-11']	
		}],
		yAxis: [{
			type: 'value',
			axisLine: {lineStyle: {color: '#999',width: 1,type: 'solid'}},
			splitLine:{lineStyle:{color: '#eee',width: 1,type: 'solid'}},
			axisLabel:{textStyle: {fontFamily: '微软雅黑, Arial, Verdana, sans-serif',color: '#666'},formatter:'{value} 万'},
			splitArea :{show:true,areaStyle:{color: ['rgba(250,250,250,0.2)','rgba(200,200,200,0.2)']}}
		}],
		series: [		
		{
			name: '汇直投',
			type: 'bar',
			stack: '堆积',
			data: [14392,15618,16310,20876,24342,23312,25679,32220,31246,36175,36507,35113]
		}, {
			name: '汇天利',
			type: 'bar',
			stack: '堆积',
			data: [0,0,0,0,160,667,1153,3856,4554,7598,9602,8575]
		}, {
			name: '汇转让',
			type: 'bar',
			stack: '堆积',
			data: [0,0,0,0,0,0,0,0,0,0,120,265]
		}],
         
	};
	Page.chart1.setOption(option);
}

// 平台保证金变化
function chart2() {
	Page.chart2 = echarts.init(document.getElementById('chart2'));
	var option = {
	        color: ['#20a6e2'],
			tooltip: {
				trigger: 'axis',
				backgroundColor: 'rgba(0,0,0,0.5)',
				axisPointer: {type: 'shadow'},
				textStyle: {fontSize : 12}
			},
			legend: {
				data: [],
				textStyle: {fontFamily: '微软雅黑, Arial, Verdana, sans-serif'},
				y: '5%',
			},
	    toolbox: {
	        show : false,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
			grid: {
				x: '10%',
				y: '15%',
				x2: '3.5%',
				y2: '10%',
				borderWidth:1,
				borderColor:'#eee'
			},
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            axisLine: {lineStyle: {color: '#999',width: 1,type: 'solid'}},
				splitLine:{lineStyle:{color: '#eee',width: 1,type: 'solid'}},
				axisLabel:{textStyle: {fontFamily: '微软雅黑, Arial, Verdana, sans-serif',color: '#666'}},
				data: ['2015-05','2015-06','2015-07','2015-08','2015-09','2015-10','2015-11']
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
				axisLine: {lineStyle: {color: '#999',width: 1,type: 'solid'}},
				splitLine:{lineStyle:{color: '#eee',width: 1,type: 'solid'}},
				axisLabel:{textStyle: {fontFamily: '微软雅黑, Arial, Verdana, sans-serif',color: '#666'},formatter:'{value} 万'},
				splitArea :{show:true,areaStyle:{color: ['rgba(250,250,250,0.2)','rgba(200,200,200,0.2)']}}
	        }
	    ],
	    series : [
	        {
	            name:'平台保证金',
	            type:'line',
	            smooth:true,
	            itemStyle: {normal: {areaStyle: {type: 'default'}}},
	            data:[6,36,304,284,348,653,893]
	        }
	    ]
	};
	
	Page.chart2.setOption(option)

}


var Events = {
	// 画面调整事件
	resizeAct: function() {
		Page.chart1.resize();
		Page.chart2.resize();
	},
}

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		// 平台数据总览
		chart1();
		// 平台保证金变化
		chart2();
	}
});