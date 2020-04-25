
var yearJson={
  yearOperationReport: {},
  "operationReportActiveList":[],
  "report": {},
  "tenthOperationReportList": {},
  "userOperationReport": {}
}
var id=getQueryString('id');
var apiUrl =webPath+ '/report/reportInfo.do?id='+id;
/*判断IE10以下请求方式用相对路径、避免因为跨域而ajax失效*/
if(navigator.appName == "Microsoft Internet Explorer"&&parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE",""))<10){
  apiUrl ='/report/reportInfo.do?id='+id;
};
window.onload=function(){
  $.get(apiUrl,function(res){
    if(res.success=='success' && typeof(res.yearOperationReport)=='undefined'){
      alert('数据为空');
      return;
    };
  if(typeof(res.success)!='undefined' && typeof(res.resultIsNull)=='undefined'){
    // 年度数据
    $.extend(yearJson.yearOperationReport,res.yearOperationReport);
    // 足迹
    $.extend(yearJson.operationReportActiveList,res.operationReportActiveList);
    // 业绩
    $.extend(yearJson.report,res.report);
    //tenthOperationReport 十大投资人数据
    $.extend(yearJson.tenthOperationReportList,res.tenthOperationReport);
    // 用户分析
    $.extend(yearJson.userOperationReport,res.userOperationReport);
    //标题赋值
    document.title=yearJson.report.year+"年度运营报告—汇盈金服官网";
    $("#year-number-header").html(yearJson.report.year);
    $('#body-section').attr('data-report-id',yearJson.report.id);
    $("#cname").html(yearJson.report.cnName);
    $("#ename").html(yearJson.report.enName);
    //活动、足迹、优化分类整理
    var activeList = getActiveList();
    //格式化 体验优化
    if(activeList.optimization.length>0){
      fmtOptimization(activeList.optimization,function(){
        var expOptSwiper=new Swiper('.swiper-experience-optimization',{
          //autoplay : 5000,//可选选项，自动滑动
          loop : false,//可选选项，开启循环
          pagination : '.experience-optimization-pagination',
          paginationClickable:true,
          calculateHeight:true,
        });
      });
    }else{
      $("#experience-optimization").remove();
    }
    // 格式化 精彩活动
    if(activeList.actarr.length>0){
      fmtActive(activeList.actarr,function(){
        var excEve = new Swiper('.swiper-exciting-events',{
          //autoplay : 5000,//可选选项，自动滑动
          loop : false,//可选选项，开启循环
          pagination : '.exciting-events-pagination',
          paginationClickable:true,
          calculateHeight:true,
        });
      });
    }else{
      $("#exciting-events").remove();
    }
    // 格式化足迹
    if(activeList.footarr.length>0){
      fmtFootprint(activeList.footarr);
    }else{
      $("#footerprints").remove();
    }
    // 创建模块序号
    createSNum(".snum");
    $(".snum-keep").each(function(i,e){
      $(e).html($($(e).data("for")).find(".snum").html());
    });
    //右侧菜单生成
    var nav=[];
    $("[data-nav]").each(function(){
      nav.push($(this).attr('data-nav'));
    });
    creatRightNav(nav);
    /*业绩总览*/
     performancePreviewCountup=function(){
       //allAmount 累计交易额
      var tradeAmout=countUpIntFloatParam("trade-amout", 0, yearJson.report.allAmount); tradeAmout.start();
      //registNum 平台注册人数
      var platformRegisters=countUpIntParam("platform-registers", 0, yearJson.report.registNum); platformRegisters.start();
      //allProfit 赚取收益
      var earnAmout=countUpIntFloatParam("earn-amout", 0, yearJson.report.allProfit); earnAmout.start();
    }
    performancePreviewCountup();
    /*全年业绩*/
    $(".year-number").html(yearJson.report.year);
    //成交最高单月
    $("#year-most-month").html(yearJson.yearOperationReport.yearSuccessMonth);
    yeahPerformanceCountup=function(){
      //本年交易笔数
      var yearTradeNum=countUpIntParam("year-trade-num", 0, yearJson.report.successDealNum); yearTradeNum.start();
      //年度交易(亿元)
      var yearTradeBillion=countUpBillionParam("year-trade-billion", 0, toBillion(yearJson.report.operationAmount)); yearTradeBillion.start();
      //本年赚取收益（万元）
      var yearEarnAmout=countUpMillionParam("year-earn-amout", 0, toMillion(yearJson.report.operationProfit)); yearEarnAmout.start();
      //平均年利率
      var averageYearPercent=countUpFloatParam("average-year-percent", 0, yearJson.yearOperationReport.yearAvgProfit);
      averageYearPercent.start();
      //最高单月成交金额
      var yearMostMonthTrade=countUpBillionParam("year-most-month-trade", 0, toBillion(yearJson.yearOperationReport.yearSuccessMonthAmount)); yearMostMonthTrade.start();
    }
    yeahPerformanceCountup();
    var halfChart=1;
    var halfChartBox=['year-trade']; //盒子id列表
    var halfChartTitle=['交易金额 单位（亿元）'];
    var chartUnit=['亿元'];
    var halfChartyAxisCategory=[[]];
    for(var i=0;i<12;i++){
      halfChartyAxisCategory[0].push(yearJson.report.year+'年'+(i+1)+'月');//
    };
    var halfChartcategoryData=[[
      toBillionFloat(yearJson.yearOperationReport.firstMonthAmount),//1月成交额
      toBillionFloat(yearJson.yearOperationReport.secondMonthAmount),//2月
      toBillionFloat(yearJson.yearOperationReport.thirdMonthAmount),//3月
      toBillionFloat(yearJson.yearOperationReport.fourthMonthAmount),//4月
      toBillionFloat(yearJson.yearOperationReport.fifthMonthAmount),//5月
      toBillionFloat(yearJson.yearOperationReport.sixthMonthAmount),//6月
      toBillionFloat(yearJson.yearOperationReport.seventhMonthAmoun),//7月
      toBillionFloat(yearJson.yearOperationReport.eighteenMonthAmount),//8月
      toBillionFloat(yearJson.yearOperationReport.ninthMonthAmount),//9月
      toBillionFloat(yearJson.yearOperationReport.tenthMonthAmount),//10月
      toBillionFloat(yearJson.yearOperationReport.eleventhMonthAmount),//11月
      toBillionFloat(yearJson.yearOperationReport.twelveMonthAmount),//12月
    ]];
    multipleHBar(halfChart,halfChartBox,halfChartTitle,halfChartyAxisCategory,halfChartcategoryData,chartUnit);

    //借款期限
    var yearReportLegendLabel=['30天以下','30天','1个月','2个月','3个月','4个月','5个月','6个月',
      '9个月','10个月','12个月','15个月','18个月','24个月','其它'];
    var yearReportLegendColor=['#5ed4fb','#2fbcd2','#2eace0','#6474cb','#da3534','#ed4544','#dd8b32',
      '#e0b36f','#e0d16f','#b7e06f','#7ce06f','#11ff92','#2fbcd2','#2eace0','#188c9e'];
    var otherPercent=0;
    var yearReportHdata=[{
         //30天以下占比
        value:yearJson.yearOperationReport.lessThirtyDayProportion,
        init:false,
      },{
        //30天
        value:yearJson.yearOperationReport.thirtyDayProportion,
        init:false,
      },{
        //1个月
        value:yearJson.yearOperationReport.oneMonthProportion,
        init:true,
      },{
        //2个月
        value:yearJson.yearOperationReport.twoMonthProportion,
        init:true,},
      {
        //3个月
        value:yearJson.yearOperationReport.threeMonthProportion,
        init:true,} ,
      {
         //4个月
        value:yearJson.yearOperationReport.fourMonthProportion,
        init:true,} ,
      {
        //5个月
        value:yearJson.yearOperationReport.fiveMonthProportion,
        init:false,} ,
      {
        //6个月
        value:yearJson.yearOperationReport.sixMonthProportion,
        init:true,} ,
      {
        //9个月
        value:yearJson.yearOperationReport.nineMonthProportion,
        init:false,} ,
      {
        //10个月
        value:yearJson.yearOperationReport.tenMonthProportion,
        init:false,} ,
      {
        //12个月
        value:yearJson.yearOperationReport.twelveMonthProportion,
        init:true,} ,
      {
         //15个月
        value:yearJson.yearOperationReport.fifteenMonthProportion,
        init:false,} ,
      {
         //18个月
        value: yearJson.yearOperationReport.eighteenMonthProportion,
        init:false,} ,
      {
        //24个月
        value: yearJson.yearOperationReport.twentyFourMonthProportion,
        init:true,} ,
      {
        //其它
        value: otherPercent,
        init:false,}
    ];
    var legendLabel=[],legendColor=[],hdata=[];
    for(var i=0;i<yearReportHdata.length;i++){
      if(yearReportHdata[i].init || yearReportHdata[i].value>5){
        legendLabel.push(yearReportLegendLabel[i]);
        legendColor.push(yearReportLegendColor[i]);
        hdata.push(yearReportHdata[i].value);
        //其它字段大于0，则显示
      }else if(i==(yearReportHdata.length-1) && otherPercent>0){
        legendLabel.push(yearReportLegendLabel[i]);
        legendColor.push(yearReportLegendColor[i]);
        hdata.push(otherPercent);
      }else{
        otherPercent+=yearReportHdata[i].value;
      }
    }
    var hollowCircleBox=['loan-life']; //盒子id列表
    var hollowCircleTitle=['借款期限'];
    var hollowCircleData=[];
    var hollowCircleColor=legendColor;
    var legendData= new Array();
    for(var i=0;i<legendLabel.length;i++){
      legendData.push({
        name:legendLabel[i],
        textStyle:{
          color:legendColor[i]
        }
      });
      hollowCircleData.push({
        name:legendLabel[i],
        value:hdata[i],
      });
    }
    legendHollowCircle(legendData,hollowCircleBox,hollowCircleTitle,hollowCircleData,hollowCircleColor);

    //渠道分析
    channelAnalysisCountup=function(){
      // app成交笔数
      var appDeal=countUpIntParam("app-deal", 0, yearJson.yearOperationReport.yearAppDealNum);
      appDeal.start();
      // app成交占比
      var appDealDercent=countUpFloatParam("app-deal-percent", 0, yearJson.yearOperationReport.yearAppDealProportion);
      appDealDercent.start();
      // 微信成交笔数
      var wechatDeal=countUpIntParam("wechat-deal", 0, yearJson.yearOperationReport.yearWechatDealNum);
      wechatDeal.start();
      //wechat成交占比
      var wechatDealPercent=countUpFloatParam("wechat-deal-percent", 0, yearJson.yearOperationReport.yearWechatDealProportion);
      wechatDealPercent.start();
      // pc成交笔数
      var pcDeal=countUpIntParam("pc-deal", 0, yearJson.yearOperationReport.yearPcDealNum);
      pcDeal.start();
      // pc成交占比
      var pcDealPercent=countUpFloatParam("pc-deal-percent", 0, yearJson.yearOperationReport.yearPcDealProportion);
      pcDealPercent.start();
    }
    channelAnalysisCountup();
    var pieChartTitle='APP、微信、PC 成交笔数对比图';
    var pieChartBox='compare-three';
    var pieName={pc:'PC',app:'APP',wechat:'微信'};
    var pieColor={pc:'#00b7ee',app:'#00eea8',wechat:'#356fe3'};
    var pieValue={
      pc:yearJson.yearOperationReport.yearPcDealNum   ,
      app:yearJson.yearOperationReport.yearAppDealNum  ,
      wechat:yearJson.yearOperationReport.yearWechatDealNum
    };
    var piePercnt={
      pc:yearJson.yearOperationReport.yearPcDealProportion ,
      app:yearJson.yearOperationReport.yearAppDealProportion ,
      wechat:yearJson.yearOperationReport.yearWechatDealProportion
    };
    var pieData=[{
        name: pieName.pc,
        value: pieValue.pc,
        percentval:piePercnt.pc,
        itemStyle:{
          normal:{
            color:pieColor.pc
          }
        }
      },
      {
        name:pieName.app,
        value:pieValue.app,
        percentval:piePercnt.app,
        itemStyle:{
          normal:{
            color:pieColor.app
          }
        }
      },{
        name:pieName.wechat,
        value:pieValue.wechat,
        percentval:piePercnt.wechat,
        itemStyle:{
          normal:{
            color:pieColor.wechat
          }
        }
      }
    ];
    solidPieChart(pieChartTitle,pieChartBox,pieName,pieValue,pieColor,pieData);
    //渠道分析02
    channelAnalysis02Countup=function(){
      // app成交笔数
      var appDeal=countUpIntFloatParam("app-deal-amount", 0, toBillionFloat(yearJson.yearOperationReport.yearAppDealAmount));
      appDeal.start();
      // app成交占比
      var appDealDercent=countUpFloatParam("app-deal-amount-percent", 0, yearJson.yearOperationReport.yearAppAmountProportion);
      appDealDercent.start();
      // 微信成交笔数
      var wechatDeal=countUpIntFloatParam("wechat-deal-amount", 0, toBillionFloat(yearJson.yearOperationReport.yearWechatDealAmount));
      wechatDeal.start();
      //wechat成交占比
      var wechatDealPercent=countUpFloatParam("wechat-deal-amount-percent", 0, yearJson.yearOperationReport.yearWechatAmountProportion);
      wechatDealPercent.start();
      // pc成交笔数
      var pcDeal=countUpIntFloatParam("pc-deal-amount", 0, toBillionFloat(yearJson.yearOperationReport.yearPcDealAmount));
      pcDeal.start();
      // pc成交占比
      var pcDealPercent=countUpFloatParam("pc-deal-amount-percent", 0, yearJson.yearOperationReport.yearPcAmountProportion);
      pcDealPercent.start();
    }
    channelAnalysis02Countup();
    var pieChartTitle02='APP、微信、PC累计成交金额对比图';
    var pieChartBox02='compare-three-02';
    var pieName02={pc:'PC',app:'APP',wechat:'微信'};
    var pieColor02={pc:'#00b7ee',app:'#00eea8',wechat:'#356fe3'};
    var pieValue02={
      pc:toBillionFloat(yearJson.yearOperationReport.yearPcDealAmount)   ,
      app:toBillionFloat(yearJson.yearOperationReport.yearAppDealAmount)  ,
      wechat:toBillionFloat(yearJson.yearOperationReport.yearWechatDealAmount)
    };
    var piePercnt02={
      pc:yearJson.yearOperationReport.yearPcAmountProportion ,
      app:yearJson.yearOperationReport.yearAppAmountProportion ,
      wechat:yearJson.yearOperationReport.yearWechatAmountProportion
    };
    var pieData02=[{
        name: pieName02.pc,
        value: pieValue02.pc,
        percentval:piePercnt02.pc,
        itemStyle:{
          normal:{
            color:pieColor02.pc
          }
        }
      },
      {
        name:pieName02.app,
        value:pieValue02.app,
        percentval:piePercnt02.app,
        itemStyle:{
          normal:{
            color:pieColor02.app
          }
        }
      },{
        name:pieName02.wechat,
        value:pieValue02.wechat,
        percentval:piePercnt02.wechat,
        itemStyle:{
          normal:{
            color:pieColor02.wechat
          }
        }
      }
    ];
    var tooltip02={
      trigger: 'item',
      formatter : function(obj) {
  			return  obj.data.name + ": "+ obj.data.value+' 亿元';
  		}
    };
    solidPieChartTooltip(pieChartTitle02,pieChartBox02,pieName02,pieValue02,pieColor02,pieData02,tooltip02);
    //用户分析
    costomerAnalysisCountup=function(){
      var girlPercent=countUpFloatParam("girl-percent", 0, yearJson.userOperationReport.womanTenderNumProportion);
      girlPercent.start();
      var boyPercent=countUpFloatParam("boy-percent", 0, yearJson.userOperationReport.manTenderNumProportion);
      boyPercent.start();
    }
    costomerAnalysisCountup();
    var solidBarChart=2;
    var solidChartBox=['customer-age','customer-trade'];
    var solidChartTitle=['出借人年龄分布占比','金额分布占比'];
    var solidyAxisCategory=[
      ['18-29岁','30-39岁','40-49岁','50-59岁','60岁以上'],
      ['1万以下','1万-5万','5万-10万','10万-50万','50万以上']
    ];
    var solidCategoryData=[[
      yearJson.userOperationReport.ageFirstStageTenderProportion,
      yearJson.userOperationReport.ageSecondStageTenderProportion ,
      yearJson.userOperationReport.ageThirdStageTenderProportion,
      yearJson.userOperationReport.ageFourthStageTenderProportion,
      yearJson.userOperationReport.ageFirveStageTenderProportion,
    ],[
      yearJson.userOperationReport.amountFirstStageTenderProportion,
      yearJson.userOperationReport.amountSecondStageTenderProportion,
      yearJson.userOperationReport.amountThirdStageTenderProportion,
      yearJson.userOperationReport.amountFourthStageTenderProportion,
      yearJson.userOperationReport.amountFirveStageTenderProportion,
    ]];
    var solidBgData=[[100,100,100,100,100],[100,100,100,100,100]];
    multipleSolidVBar(solidBarChart,solidChartBox,solidChartTitle,solidyAxisCategory,solidCategoryData,solidBgData);
    //客户服务
    costomerServiceCountup=function(){
      var phoneWorknum=countUpIntParam("phone-worknum", 0, yearJson.yearOperationReport.phoneNum); phoneWorknum.start();//phone-worknum 电话接待人数
      var qqWorknum=countUpIntParam("qq-worknum", 0, yearJson.yearOperationReport.qqCustomerServiceNum); qqWorknum.start();//qq-worknum QQ接待人数
      var wechatWorknum=countUpIntParam("wechat-worknum", 0, yearJson.yearOperationReport.wechatCustomerServiceNum); wechatWorknum.start();//wechat-worknum 微信接待人数
      var questionResolveenum=countUpIntParam("question-resolveenum", 0, yearJson.yearOperationReport.dealQuestionNum); questionResolveenum.start();//question-resolveenum 解决问题数
    }
    costomerServiceCountup();
    //年度之最
    yeahMostCountup=function(){
      var yearTradeMost=countUpIntFloatParam("year-trade-most", 0, yearJson.tenthOperationReportList.mostTenderAmount); yearTradeMost.start();//year-trade-most 本季度投资金额最高
      var yearEarnMost=countUpIntFloatParam("year-earn-most", 0, yearJson.tenthOperationReportList.bigMinnerProfit); yearEarnMost.start();//year-earn-most 本季度收益金额最高
      var yearNumberMost=countUpIntParam("year-number-most", 0, yearJson.tenthOperationReportList.activeTenderNum); yearNumberMost.start();//year-number-most 本季度投资次数最高
    }
    yeahMostCountup();
    $('#year-trade-most-name').html(yearJson.tenthOperationReportList.mostTenderUsername);
    $('#year-trade-most-age').html(yearJson.tenthOperationReportList.mostTenderUserAge+"岁");
    $('#year-trade-most-area').html(yearJson.tenthOperationReportList.mostTenderUserArea);

    $('#year-earn-most-name').html(yearJson.tenthOperationReportList.bigMinnerUsername);
    $('#year-earn-most-age').html(yearJson.tenthOperationReportList.bigMinnerUserAge+"岁");
    $('#year-earn-most-area').html(yearJson.tenthOperationReportList.bigMinnerUserArea);

    $('#year-number-most-name').html(yearJson.tenthOperationReportList.activeTenderUsername);
    $('#year-number-most-age').html(yearJson.tenthOperationReportList.activeTenderUserAge+"岁");
    $('#year-number-most-area').html(yearJson.tenthOperationReportList.activeTenderUserArea);

    var first=$('#topten-list li').first();
    for(var i=0;i<10;i++){
      switch (i) {
        case 0:
          first.find('.topten-li-num').html(i+1);
          first.find('.topten-li-name').html(yearJson.tenthOperationReportList.firstTenderUsername);
          first.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.firstTenderAmount)+'万元');
          break;
        case 1:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.secondTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.secondTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 2:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.thirdTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.thirdTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 3:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.fourthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.fourthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 4:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.fifthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.fifthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 5:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.sixthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.sixthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 6:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.seventhTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.seventhTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 7:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.eighthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.eighthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 8:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.ninthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.ninthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 9:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(yearJson.tenthOperationReportList.tenthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(yearJson.tenthOperationReportList.tenthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        default:
          break;
      }
    }

    var hollowCircleBox=['topten-trade']; //盒子id列表
    var hollowCircleTitle=['10大出借人\n金额之和占比'];
    var hollowCircleCategory=['10大出借人金额','其它出借人金额'];
    var hollowCircleData=[{
      name:'10大出借人金额',
      value:yearJson.tenthOperationReportList.tenTenderAmount,
      percent:yearJson.tenthOperationReportList.tenTenderProportion,
    },{
      name:'其它出借人金额',
      value:yearJson.tenthOperationReportList.otherTenderAmount,
      percent:yearJson.tenthOperationReportList.otherTenderProportion}
    ];
    var hollowCircleColor=['#00eea8','#00b7ee'];
    hollowCircle(hollowCircleBox[0],hollowCircleTitle[0],hollowCircleCategory,hollowCircleData,hollowCircleColor);

  }else{
    alert("数据请求失败，请刷新重试");
  }
});
}
/*业绩总览*/
function performancePreviewCountup(){}
/*全年业绩*/
function yeahPerformanceCountup(){}
/*渠道分析*/
function channelAnalysisCountup(){}
function channelAnalysis02Countup(){}
/*用户分析*/
function costomerAnalysisCountup(){}
/*全年之最*/
function yeahMostCountup(){}
/*客户服务*/
function costomerServiceCountup(){}
/* 精彩活动 */
function getActiveList(){
  var optimization=[];//体验优化
  var actarr = [];//精彩活动
  var footarr = [];//足迹
  for(var i=0;i<yearJson.operationReportActiveList.length;i++){
    var item = yearJson.operationReportActiveList[i];
    if(item.activtyType == '1'){
      optimization.push(item)
    }else if(item.activtyType == '2'){
      actarr.push(item)
    }else if(item.activtyType == '3'){
      footarr.push(item)
    }
  }
  return {
    optimization:optimization,
    actarr: actarr,
    footarr: footarr
  }
}
// 格式化体验优化
function fmtOptimization(optList,callback){
  //activeList = activeList.concat(activeList).concat(activeList)
  var $optDOM = $("#experience-optimization").find(".swiper-wrapper");
  var optArr = '';
  for(var i=0;i<optList.length; i++){
    var item = optList[i];
    if(i%6 == 0){
      optArr += "<section class='box quarter-section6 swiper-slide'><div class='line'>"
    }
    optArr+= '<div class="content-center">';
    optArr+= '<p class="margin10 sub-title-date">';
    optArr+= (item.activtyTime==0?'&nbsp;':toYMD(item.activtyTime));
    optArr+= '</p>';
    optArr+= '<p class="sub-title">'+item.activtyName+'</p></div>'
    if(i%6 == 5 || i == optList.length ){
      optArr += "</div></section>"
    }
  }
  $optDOM.html(optArr);
  setTimeout(callback,1000);
}
// 格式化精彩活动
function fmtActive(activeList,callback){
  //activeList = activeList.concat(activeList).concat(activeList)
  var $actDOM = $("#exciting-events").find(".swiper-wrapper");
  var actArr = '';
  for(var i=0;i<activeList.length; i++){
    var item = activeList[i];
    if(i%6 == 0){
      actArr += "<section class='box section6-box swiper-slide'><div class='line'>"
    }
    actArr+= '<div class="content-center"><img class="margin10 img-max100" src="'+item.activtyPictureUrl+'">	<br>';
    actArr+='<p class="margin10">'+item.activtyName+'</p>';
    if(item.activtyStartTime!=0 && item.activtyEndTime!=0){
      actArr+='<p class="sub-title">'+toMD(item.activtyStartTime)+'-'+toMD(item.activtyEndTime)+'</p>';
    }else if(item.activtyStartTime!=0){
      actArr+='<p class="sub-title">'+toYMD(item.activtyStartTime)+'起'+'</p>';
    }else{
      actArr+='<p class="sub-title">'+'&nbsp;'+'</p>';
    }
    actArr+='</div>';
    if(i%6 == 5 || i == activeList.length ){
      actArr += "</div></section>"
    }
  }
  $actDOM.html(actArr);
  setTimeout(callback,1000);
}
// 格式化足迹
function fmtFootprint(footprintList){
  //footprintList = footprintList.concat(footprintList).concat(footprintList)
  var $footprintDOM = $("#footerprints").find(".section7");
  var footprintArr = '';
  var monthArr=[];
  var monthBox=[];
  //获取月份列表
  for(var i=0;i<footprintList.length; i++){
    var item = footprintList[i];
    //若时间为空，则当前活动不显示
    if(item.activtyTime==0){
      continue;
    }
    monthArr.push((new Date(item.activtyTime*1000)).getMonth()+1);
  };
  if(monthArr.length==0){
    $("#footerprints").remove();
    return;
  }
  monthArr=unique(monthArr);
  for(var i=0;i<monthArr.length;i++){
    monthBox[i]="<div class='section7-box'><p class='content-center'><span class='month-num'>"+monthArr[i]+"月</span></p>";
  }

  for(var i=0;i<footprintList.length; i++){
    var item = footprintList[i];
    for(j=0;j<monthArr.length;j++){
      var m=(new Date(item.activtyTime*1000)).getMonth()+1;
      if(m==monthArr[j]){
        monthBox[j]+= "<div class='month-list month-list-odd'><div class='sub-title'><p>"
          +toYMD(item.activtyTime)+"</p><p class='stitle'>"+item.activtyName+"</p></div></div>";
      }
    }
  }
  for(var i=0;i<monthArr.length;i++){
    footprintArr+=monthBox[i]+"</div>";
  }
  $footprintDOM.html(footprintArr)
}
