/**浮动菜单**/
var navListSummary=[
  //0
  {text:'顶部',id:'top'},
  //1
  {text:'业绩总览',id:'performance-preview'},
  //2
  {text:'渠道分析',id:'channel-analysis'},
  //3
  {text:'用户分析',id:'costomer-analysis'},
  //4
  {text:'客户服务',id:'costomer-service'},
  //5
  {text:'体验优化',id:'experience-optimization'},
  //6
  {text:'精彩活动',id:'exciting-events'},
  //7
  {text:'足迹',id:'footerprints'},
  //8
  {text:'借款期限',id:'loan-term'},
  //9
  {text:'全年业绩',id:'yeah-performance'},
  //10
  {text:'年度之最',id:'yeah-most'},
  //11
  {text:'当季业绩',id:'quarter-performance'},
  //12
  {text:'季度之最',id:'quarter-most'},
  //13
  {text:'上半年业绩',id:'half-performance'},
  //14
  {text:'上半年之最',id:'half-most'},
  //15
  {text:'当月业绩',id:'month-performance'},
  //16
  {text:'当月之最',id:'month-most'},
];

var html=$('html,body'),menuClick=false;
/**创建模块标题序号**/
function createSNum(ele){
  $(ele).each(function(i,e){
    var str=i+1<10?"0"+(i+1):(i+1);
    $(e).html(str);
  })
}
/**创建右侧菜单**/
function creatRightNav(navlist){
  var oddClass='li-odd',evenClass='li-even',cls='';
  for(var i=0;i<navlist.length;i++){
    var li="";
    var m=(i+1)%2;
    if(m>0){
      cls=oddClass;
    }else{
      cls=evenClass;
    }
    switch (parseInt(navlist[i])) {
      case 0:
        li="<li class='"+cls+"'><span class='double-height active' authors='#"+navListSummary[navlist[i]].id+"'>";
        li+=navListSummary[navlist[i]].text+"</span></li>";
        break;
      case 7:
        li="<li class='"+cls+"'><span class='double-height' authors='#"+navListSummary[navlist[i]].id+"'>";
        li+=navListSummary[navlist[i]].text+"</span></li>";
        break;
      default:
        li="<li class='"+cls+"'><span authors='#"+navListSummary[navlist[i]].id+"'>";
        li+=navListSummary[navlist[i]].text+"</span></li>";
    }
    $('#right-nav ul').append(li);
  }

  $("#right-nav span").click(function(){
    var target=$(this);
    menuClick=true;
    $("#right-nav span").removeClass('active');
    $(this).addClass('active');
    $(html).animate({
          scrollTop: $($(this).attr('authors')).offset().top
      }, 500 ,function(){
        menuClick=false;
        if(typeof($(target).attr('data-countup-started'))!='undefined'){
          return;
        }
        if(typeof($($(target).attr('authors')).attr('data-countup'))!='undefined'){
          countUpStart($($(target).attr('authors')).attr('data-countup'));
          $(target).attr('data-countup-started','true');
        }
      });
    return false;
  });

  $(window).scroll(function(){
    if(menuClick){
      return;
    }
    var wst =  $(window).scrollTop();
    var hst= $("#top").height()/2;
    /**判断距顶部大于top1/2 则显示为顶部**/
    if(wst<hst ){
      $("#right-nav span").removeClass('active');
      $("#right-nav span[authors='#top']").addClass("active");
      return;
    }
    /**子菜单滚动条与菜单对应 **/
    var len=$("#body-section >section").length+1;
    //var act=$("#right-nav span.active").attr("authors");
    for (i=1; i<len; i++){
      if($("#body-section >section:nth-of-type("+i+")").offset().top<wst || $("#body-section >section:nth-of-type("+i+")").offset().top==wst){
        var ele=$("#body-section >section:nth-of-type("+i+")").attr('id');

        $("#right-nav span").removeClass('active');
        $("#right-nav span[authors='#"+ele+"']").addClass("active");
        if(typeof($("#right-nav span[authors='#"+ele+"']").attr('data-countup-started'))!='undefined'){
          continue;
        }
        if(typeof($($("#right-nav span[authors='#"+ele+"']").attr('authors')).attr('data-countup'))!='undefined'){
          countUpStart($($("#right-nav span[authors='#"+ele+"']").attr('authors')).attr('data-countup'));
          $("#right-nav span[authors='#"+ele+"']").attr('data-countup-started','true');
        }
    	}
     }
  });
}

/**动态调用countUp**/

var countUpOptions = {
  useEasing: true,
  useGrouping: true,
  separator: ',',
  decimal: '.',
};
var countUpFloatOptions={
  useEasing: true,
  useGrouping: true,
  separator: ',',
  decimal: '.',
  suffix:'%'
};
function countUpStart(func){
  var countUpFunc=func+'()';
  eval(countUpFunc);
};

/** decimals 小数位数, duration动态时间 **/
function countUpIntParam(id,start,end,decimals,duration,option){
  return new CountUp(id,start,end,0,1.5,countUpOptions);
}
function countUpIntFloatParam(id,start,end,decimals,duration,option){
  return new CountUp(id,start,end,2,1.5,countUpOptions);
}
function countUpFloatParam(id,start,end,decimals,duration,option){
  return new CountUp(id,start,end,2,1.5,countUpFloatOptions);
}
function countUpMillionParam(id,start,end,decimals,duration,option){
  return new CountUp(id,start,end,2,1.5,countUpOptions);
}
function countUpBillionParam(id,start,end,decimals,duration,option){
  return new CountUp(id,start,end,2,1.5,countUpOptions);
}
function toMillion(amount){
  return (parseInt(amount/100)/100).toFixed(2);//截取2位小数
}
function toBillionFloat(amount){
  return (parseInt(toMillion(amount)/100)/100).toFixed(2);//截取2位小数
}
function toBillion(amount){
  return (parseInt(toMillion(amount)/100)/100).toFixed(2);//截取2位小数
}
/**纵向渐变双色交替**/
function multipleVBar(mvChart,mvChartBox,mvTitle,mvyAxisCategory,mvCategoryData,chartUnit){
  for(var i=0;i<mvChart;i++){
    var mtradeChart=echarts.init(document.getElementById(mvChartBox[i]));
    var option = {
        title:{
          text:mvTitle[i],
          left:'center',
          textStyle :{
            color:'#fff',
            fontWeight:'lighter',
            fontSize:13
          },
          bottom:30
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            formatter : '{b} <br/> {c} '+chartUnit[i],
        },
        grid: {
          left:10,
          containLabel: true
        },
        xAxis: {
            show:false,
        },
        yAxis: {
            type: 'category',
            data: mvyAxisCategory,
            splitLine: {show: false},
            axisLine: {
              lineStyle:{
                color:"#6474cb",
              }
            },
            axisLabel:{
              textStyle:{
                color:'#fff'
              }
            },
            offset: 10,
            nameTextStyle: {
                fontSize: 15
            }
        },
        series: [
            {
                type: 'bar',
                data: mvCategoryData[i],
                barWidth: 24,
                barGap: 10,
                smooth: true,
                label: {
                  emphasis:{
                  },
                  normal: {
                      show: true,
                      position: 'right',
                      textStyle: {
                          color:function(params){
                            return normalColor(params);
                          },
                          fontSize: 13
                      }
                  }
                },
                itemStyle: {
                    emphasis: {
                    },
                    normal:{
                      color: function(params){
                          var newcolor=normalColor(params);
                          return new echarts.graphic.LinearGradient(
                              0, 0, 1, 0,
                              [
                                  {offset: 0, color: 'rgba(255,255,255,0)'},
                                  {offset: 1, color: newcolor}
                              ]
                          )
                       }
                    }
                }
            }
        ]
    };
    mtradeChart.setOption(option);
  }
}

/**横向渐变双色交替**/
function multipleHBar(mhChart,mhChartBox,mhTitle,mhxAxisCategory,mhCategoryData){
  var colorList=['#6474cb','#00b7ee'];
  for(var i=0;i<mhChart;i++){
    var mtradeChart=echarts.init(document.getElementById(mhChartBox[i]));
    var option = {
        title:{
          text:mhTitle[i],
          left:'center',
          top:'bottom',
          textStyle :{
            color:'#fff',
            fontWeight:'lighter',
            fontSize:13
          },
          bottom:30
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
          left:10,
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: mhxAxisCategory[i],
          splitLine: {show: false},
          axisLine: {
            lineStyle:{
              color:"#6474cb",
            }
          },
          axisLabel:{
            interval:0,//横轴信息全部显示
            textStyle:{
              color:'#fff'
            }
          },
          offset: 10,
          nameTextStyle: {
              fontSize: 15,
          }
        },
        yAxis: {
          show:false,
        },
        series: [
            {
                name: '统计',
                type: 'bar',
                data: mhCategoryData[i],
                barWidth: 24,
                barGap: 10,
                smooth: true,
                label: {
                  normal: {
                      show: true,
                      position: 'top',
                      distance:20,
                      textStyle: {
                          color:function(params){
                            return normalColor(params);
                          },
                          fontSize: 13
                      }
                  }
                },
                itemStyle: {
                    emphasis: {
                    },
                    normal:{
                      color: function(params){
                          var newcolor=normalHColor(params);
                          // return newcolor;
                          return new echarts.graphic.LinearGradient(
                              0, 0, 0, 1,
                              [
                                  {offset: 1, color: 'rgba(255,255,255,0)'},
                                  {offset: 0, color: newcolor}
                              ]
                          )
                       }
                    }
                }
            },
            {
                name: '线图',
                type: 'line',
                data: mhCategoryData[i],
                itemStyle:{
                  normal:{
                    color:"#00eec4"
                  }
                },
                lineStyle:{
                  normal:{
                    color:"#00eec4",
                    width:1,
                  }
                }
            }
        ]
    };
    mtradeChart.setOption(option);
  }
}

/**实心饼图**/
function solidPieChart(pieChartTitle,pieChartBox,pieName,pieValue,pieColor,pieData){
  var mcompareChart=echarts.init(document.getElementById(pieChartBox));
  var compareOption={
    title: {
      text: pieChartTitle,
      left: 'center',
      top: 0,
      textStyle: {
          color: '#fff',
          fontSize:30,
          fontWeight:"normal"
      }
    },
    tooltip : {
      trigger: 'item',
      formatter : function(obj) {
  			return  obj.data.name + ": "+ obj.data.value+' 笔';
  		}
    },
    // visualMap: {
    //   show: false,
    //   min: 80,
    //   max: 600,
    //   inRange: {
    //       colorLightness: [0, 1]
    //   }
    // },
    series : [
      {
        name:'对比数据',
        type:'pie',
        radius : '55%',
        center: ['50%', '50%'],
        data:pieData.sort(function (a, b) { return a.value - b.value}),
        label: {
            normal: {
                textStyle: {
                    color: '#fff',
                    fontSize:18
                },
            formatter : function(obj) {
    					// if (obj.data.value == "9244") { //hack 数字bug
    					// 	obj.percent = "35.10";
    					// }
    					return  obj.data.name + "\n" + obj.data.percentval + "%";
  				   }
          }
        },
        labelLine: {
            normal: {
                lineStyle: {
                    color: '#fff'
                },
                smooth: 0.2,
                length: 8,
            }
        },
        itemStyle: {
            normal: {
                // color: '#c23531',
                shadowColor: 'rgba(0,0, 0, 0.5)',
                // shadowBlur: 50,
            }
        }
      }
    ]
  };
  mcompareChart.setOption(compareOption);
}
function solidPieChartTooltip(pieChartTitle,pieChartBox,pieName,pieValue,pieColor,pieData,tooltip){
  var mcompareChart=echarts.init(document.getElementById(pieChartBox));
  var compareOption={
    title: {
      text: pieChartTitle,
      left: 'center',
      top: 0,
      textStyle: {
          color: '#fff',
          fontSize:30,
          fontWeight:"normal"
      }
    },
    tooltip : tooltip,
    series : [
      {
        name:'对比数据',
        type:'pie',
        radius : '55%',
        center: ['50%', '50%'],
        data:pieData.sort(function (a, b) { return a.value - b.value}),
        label: {
            normal: {
                textStyle: {
                    color: '#fff',
                    fontSize:18
                },
            formatter : function(obj) {
    					return  obj.data.name + "\n" + obj.data.percentval + "%";
  				   }
          }
        },
        labelLine: {
            normal: {
                lineStyle: {
                    color: '#fff'
                },
                smooth: 0.2,
                length: 8,
            }
        },
        itemStyle: {
            normal: {
                shadowColor: 'rgba(0,0, 0, 0.5)',
            }
        }
      }
    ]
  };
  mcompareChart.setOption(compareOption);
}
/**5色实色柱状图**/
function multipleSolidVBar(tradeBarChart,tbarChartBox,chartTitle,yAxisCategory,categoryData){
  for(var i=0;i<tradeBarChart;i++){
    var mtradeChart=echarts.init(document.getElementById(tbarChartBox[i]));
    var option = {
        title:{
          text:chartTitle[i],
          left:'center',
          textStyle :{
            color:'#00eea8',
            fontWeight:'lighter',
            fontSize:13
          },
          top:30
        },
        // tooltip: {
        //     trigger: 'axis',
        //     axisPointer: {
        //         type: 'shadow'
        //     }
        // },
        grid: {
          left:10,
          containLabel: true
        },
        xAxis: {
            show:false,
        },
        yAxis: {
            type: 'category',
            data: yAxisCategory[i],
            splitLine: {show: false},
            offset:10,
            axisLine: {
              lineStyle:{
                color:"#fff",
              }
            },
            axisTick:{
              show:false
            },
            axisLabel:{
              textStyle:{
                color:'#fff'
              }
            },
            nameTextStyle: {
                fontSize: 15
            }
        },
        series: [
        //{
        //   type:'bar',
        //   barWidth: 24,
        //   barGap: '-100%',
        //   data:solidBgData,
        //   silent:true,
        //   itemStyle:{
        //     normal:{
        //       color:'#ddd'
        //     }
        //   }
        // },
            {
                name: '数量',
                type: 'bar',
                data: categoryData[i],
                barWidth: 18,
                barGap: 10,
                smooth: true,
                label: {
                  emphasis:{
                  },
                  normal: {
                      show: true,
                      position: 'right',
                      formatter:function(params){
                        return params.value+'%';
                      },
                      textStyle: {
                          fontSize: 13
                      }
                  },
                },
                itemStyle: {
                    emphasis: {
                    },
                    normal:{
                      color: function(params){
                          return customerColor(params);
                       }
                    }
                }
            }
        ]
    };
    mtradeChart.setOption(option);
  }
}

/**空心圆饼图**/
function hollowCircle(hollowCircleBox,hollowCircleTitle,hollowCircleCategory,hollowCircleData,hollowCircleColor){
  var hollowCircleChart=echarts.init(document.getElementById(hollowCircleBox));
  var option = {
      title:{
        text:hollowCircleTitle,
        left:'center',
        top:'center',
        textStyle :{
          color:'#fff',
          fontWeight:'lighter',
          fontSize:16
        },
      },
      tooltip : {
        trigger: 'item',
        formatter : function(params){
          return params.name+'：'+params.value+'（元）';
        },
      },
      series: [
        {
          name:'内环',
          type:'pie',
          data:[100],
          silent:true,
          radius:['29%','30%'],
          center: ['50%', '50%'],
          labelLine: {
              normal: {
                  show: false
              }
          },
          itemStyle: {
              emphasis: {
              },
              normal:{
                color:'rgba(255,255,255,0.1)'
              }
          }
        },{
          name:'内环边',
          type:'pie',
          data:[100],
          silent:true,
          radius:['34%','35%'],
          center: ['50%', '50%'],
          labelLine: {
              normal: {
                  show: false
              }
          },
          itemStyle: {
              emphasis: {
              },
              normal:{
                color:'rgba(255,255,255,0.1)'
              }
          }
        },{
            name:'外环',
            type:'pie',
            data:[100],
            silent:true,
            radius:['55%','67%'],
            center: ['50%', '50%'],
            labelLine: {
                normal: {
                    show: false
                }
            },
            itemStyle: {
                emphasis: {
                },
                normal:{
                  color:'rgba(255,255,255,0.1)'
                }
            }
          },{
                name: '10大投资人',
                type: 'pie',
                data: hollowCircleData,
                radius: ['40%', '55%'],
                center: ['50%', '50%'],
                label: {
                  normal: {
                    formatter:function(params){
                      return params.name+'\n'+params.percent+'%';
                    },
                    textStyle: {
                        // color:function(params){
                        //   var index=params.dataIndex;
                        //   console.log(hollowCircleColor[index]);
                        //   return hollowCircleColor[index];
                        // },
                        // color:'#fff',
                        fontSize: 16
                    }
                  },
                },
                itemStyle: {
                    normal:{
                      color: function(params){
                          var index=params.dataIndex;
                          return hollowCircleColor[index];
                       }
                    }
                }
            }
      ]
  };
  hollowCircleChart.setOption(option);
}

/**有图例组件空心圆饼图**/
function legendHollowCircle(legendData,hollowCircleBox,hollowCircleTitle,hollowCircleData,hollowCircleColor){

  var hollowCircleChart=echarts.init(document.getElementById(hollowCircleBox));
  var option = {
      title:{
        text:hollowCircleTitle,
        left:'center',
        top:'center',
        textStyle :{
          color:'#fff',
          fontWeight:'lighter',
          fontSize:13
        },
      },
      tooltip : {
        trigger: 'item',
        formatter : function(params){
          return params.name+' '+params.percent+'%';
        },
      },
      legend:{
        top:'center',
        left:'right',
        align:'left',
        orient:'vertical',
        data:legendData
      },
      series: [
        {
          name:'内环',
          type:'pie',
          data:[100],
          silent:true,
          radius:['0%','40%'],
          center: ['50%', '50%'],
          labelLine: {
              normal: {
                  show: false
              }
          },
          itemStyle: {
              emphasis: {
              },
              normal:{
                color:'#2d334e'
              }
          }
        },{
          name:'外环边',
          type:'pie',
          data:[100],
          silent:true,
          radius:['64%','65%'],
          center: ['50%', '50%'],
          labelLine: {
              normal: {
                  show: false
              }
          },
          itemStyle: {
              emphasis: {
              },
              normal:{
                color:'#2d334e'
              }
          }
        },{
            name:'外环',
            type:'pie',
            data:[100],
            silent:true,
            radius:['55%','60%'],
            center: ['50%', '50%'],
            labelLine: {
                normal: {
                    show: false
                }
            },
            itemStyle: {
                emphasis: {
                },
                normal:{
                  color:'#2d334e'
                }
            }
          },{
                name: '借款类别',
                type: 'pie',
                data: hollowCircleData,
                radius: ['40%', '55%'],
                center: ['50%', '50%'],
                label: {
                  normal: {
                    formatter:function(params){
                      if(params.name=='其它'){
                        return '其它';
                      }else{
                        return params.percent+'%';
                      }
                    },
                    textStyle: {
                        fontSize: 13
                    }
                  },
                },
                itemStyle: {
                    normal:{
                      color: function(params){
                          var index=params.dataIndex;
                          return hollowCircleColor[index];
                       }
                    }
                }
            }
      ]
  };
  hollowCircleChart.setOption(option);
}

function normalColor(params){
    var index=params.dataIndex;
    var alternateNumber = 2;//表示按几种颜色交替出现
    var ncolor="";//最终显示颜色
    var colorList=['#00b7ee','#6474cb'];//颜色降序
    if(index < alternateNumber){
        ncolor = colorList[index];
    }else{
        var rowNumber=Math.floor(index/alternateNumber);
          ncolor = colorList[index-rowNumber*alternateNumber];
    }
    return ncolor;
}

function normalHColor(params){
    var index=params.dataIndex;
    var alternateNumber = 2;//表示按几种颜色交替出现
    var ncolor="";//最终显示颜色
    var colorList=['#6474cb','#00b7ee'];
    if(index < alternateNumber){
        ncolor = colorList[index];
    }else{
        var rowNumber=Math.floor(index/alternateNumber);
          ncolor = colorList[index-rowNumber*alternateNumber];
    }
    return ncolor;
}

function customerColor(params){
    var index=params.dataIndex;
    var alternateNumber = 5;//表示按几种颜色交替出现
    var ncolor="";//最终显示颜色
    var colorList=['#b7713d','#b73d76','#903db7','#00b7ee','#00eea8'];//颜色降序
    if(index < alternateNumber){
        ncolor = colorList[index];
    }else{
        var rowNumber=Math.floor(index/alternateNumber);
          ncolor = colorList[index-rowNumber*alternateNumber];
    }
    return ncolor;
}

function emphasisColor(){
}

function toMD(timestamp){
  var t=new Date(timestamp*1000);
  var m=t.getMonth()+1+'月';
  var d=t.getDate()+'日';
  return m+d;
}

function toYMD(timestamp){
  var t=new Date(timestamp*1000);
  var y=t.getFullYear()+'年';
  var m=t.getMonth()+1+'月';
  var d=t.getDate()+'日';
  return y+m+d;
}
function getQueryString(name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
      return unescape(r[2]);
  }
  return null;
}
function sortMonth(a,b){
  return a-b;
}
function unique (arr) {
  // const seen = new Map();
  // return arr.filter((a) => !seen.has(a) && seen.set(a, 1));
  arr.sort(sortMonth);
    var re = [arr[0]];
    for (var i = 1; i < arr.length; i++) {
        if (arr[i] !== re[re.length - 1]) {
            re.push(arr[i]);
        }
    }
    return re;
}
