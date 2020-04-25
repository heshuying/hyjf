
var monthJson={
  "monthlyOperationReport": {},
  "operationReportActiveList":[],
  "success": "success",
  "report": {},
  "tenthOperationReportList": {},
  "userOperationReport": {}
}
var id=getQueryString('id');
var apiUrl =webPath+ '/report/reportInfo.do?id='+id;
/*判断IE10以下请求方式用相对路径、避免因为跨域而ajax失效*/
if(navigator.appName == "Microsoft Internet Explorer"&&parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE",""))<10){
  apiUrl ='/report/reportInfo.do?id='+id;
}
window.onload=function(){
  // var nav=[0,1,15,2,3,16,6,7];
  // creatRightNav(nav);
$.get(apiUrl,function(res){
  if(res.success=='success' && typeof(res.monthlyOperationReport)=='undefined'){
    alert('数据为空');
    return;
  }
  if(typeof(res.success)!='undefined' && typeof(res.resultIsNull)=='undefined'){
    // 月度数据
    $.extend(monthJson.monthlyOperationReport,res.monthlyOperationReport)
    // 足迹
    $.extend(monthJson.operationReportActiveList,res.operationReportActiveList)
    // 业绩
    $.extend(monthJson.report,res.report)
    //tenthOperationReport 十大投资人数据
    $.extend(monthJson.tenthOperationReportList,res.tenthOperationReport)
    // 用户分析
    $.extend(monthJson.userOperationReport,res.userOperationReport)

    var activeList = getActiveList();
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
    var nav=[];
    $("[data-nav]").each(function(){
      nav.push($(this).attr('data-nav'));
    });
    creatRightNav(nav);
    //标题赋值
    document.title=monthJson.report.year+"年"+monthJson.monthlyOperationReport.month+"月份运营报告—汇盈金服官网";
    $('#body-section').attr('data-report-id',monthJson.report.id);
    $("#cname").html(res.report.cnName);
    $("#ename").html(res.report.enName);
    /*业绩总览*/
    //trade-amout 累计交易额
    //platform-registers 平台注册人数
    //earn-amout 赚取收益
    performancePreviewCountup=function(){
      // allAmount 累计交易额
      var tradeAmout=countUpIntFloatParam("trade-amout", 0, monthJson.report.allAmount);
      tradeAmout.start();
      // registNum 平台注册人数
      var platformRegisters=countUpIntParam("platform-registers", 0, monthJson.report.registNum);
      platformRegisters.start();
      //allProfit 赚取收益
      var earnAmout=countUpIntFloatParam("earn-amout", 0, monthJson.report.allProfit);
      earnAmout.start();
    }
    performancePreviewCountup();
    /*当月业绩*/
    //month-earn-amout 本月赚取收益
    //add-percent 同比增长
    //average-add-percent 平均收益率
    if(monthJson.monthlyOperationReport.amountIncrease<0){
      $('#month-add-percent-describe').addClass("fall");
    }
    if(monthJson.monthlyOperationReport.profitIncrease<0){
      $('#add-percent-describe').addClass("fall");
    }
    monthPerformanceCountup=function(){
      //successDealNum 本月赚取收益
      var tradeMonth=countUpIntParam("trade-month", 0, monthJson.report.successDealNum);
      tradeMonth.start();
      //operationAmount 成交金额共计（亿元）
      var tradeMonthAmout=countUpBillionParam("trade-month-amout", 0, toBillion(monthJson.report.operationAmount));
      tradeMonthAmout.start();
      // amountIncrease 成交同比增长
      var monthAddPercent=countUpFloatParam("month-add-percent", 0, Math.abs(monthJson.monthlyOperationReport.amountIncrease));
      monthAddPercent.start();
      // operationProfit 本月为用户赚取收益（万元）
      var monthEarnAmout=countUpMillionParam("month-earn-amout", 0, toMillion(monthJson.report.operationProfit));
      monthEarnAmout.start();
      // profitIncrease 赚取收益同比增长
      var addPercent=countUpFloatParam("add-percent", 0,Math.abs(monthJson.monthlyOperationReport.profitIncrease));
      addPercent.start();
      // monthAvgProfit 本月平均预期收益率
      var averageAddPercent=countUpFloatParam("average-add-percent", 0, monthJson.monthlyOperationReport.monthAvgProfit);
      averageAddPercent.start();
    }
    monthPerformanceCountup();
    var tradeBarChart=2;
    var tbarChartBox=['month-trade','month-earn']; //盒子id列表
    var chartTitle=['交易金额 单位（亿元）','赚取收益 单位（万元）'];
    var chartUnit=['亿元','万元'];
    var yAxisCategory=[(monthJson.report.year-1)+'年'+monthJson.monthlyOperationReport.month+'月',
      monthJson.report.year+'年'+monthJson.monthlyOperationReport.month+'月'];
    var categoryData=[[toBillion(monthJson.monthlyOperationReport.lastYearMonthAmount),toBillion(monthJson.report.operationAmount)],
    [toMillion(monthJson.monthlyOperationReport.lastYearMonthProfit),toMillion(monthJson.report.operationProfit)]];
    multipleVBar(tradeBarChart,tbarChartBox,chartTitle,yAxisCategory,categoryData,chartUnit);

    /*渠道分析*/
    //app-deal app成交笔数
    //app-deal-percent APP成交占比
    //wechat-deal wechat成交笔数
    //wechat-deal-percent wechat成交占比
    //pc-deal pc成交笔数
    //pc-deal-percent pc成交占比
    channelAnalysisCountup=function(){
      // app成交笔数
      var appDeal=countUpIntParam("app-deal", 0, monthJson.monthlyOperationReport.monthAppDealNum);
      appDeal.start();
      // app成交占比
      var appDealDercent=countUpFloatParam("app-deal-percent", 0, monthJson.monthlyOperationReport.monthAppDealProportion);
      appDealDercent.start();
      // 微信成交笔数
      var wechatDeal=countUpIntParam("wechat-deal", 0, monthJson.monthlyOperationReport.monthWechatDealNum);
      wechatDeal.start();
      //wechat成交占比
      var wechatDealPercent=countUpFloatParam("wechat-deal-percent", 0, monthJson.monthlyOperationReport.monthWechatDealProportion);
      wechatDealPercent.start();
      // pc成交笔数
      var pcDeal=countUpIntParam("pc-deal", 0, monthJson.monthlyOperationReport.monthPcDealNum);
      pcDeal.start();
      // pc成交占比
      var pcDealPercent=countUpFloatParam("pc-deal-percent", 0, monthJson.monthlyOperationReport.monthPcDealProportion);
      pcDealPercent.start();
    }
    channelAnalysisCountup();
    var pieChartTitle='APP、微信、PC 成交笔数对比图';
    var pieChartBox='compare-three';
    var pieName={pc:'PC',app:'APP',wechat:'微信'};
    var pieValue={
      pc:monthJson.monthlyOperationReport.monthPcDealNum,
      app:monthJson.monthlyOperationReport.monthAppDealNum,
      wechat:monthJson.monthlyOperationReport.monthWechatDealNum};
    var piePercnt={
      pc:monthJson.monthlyOperationReport.monthPcDealProportion,
      app:monthJson.monthlyOperationReport.monthAppDealProportion,
      wechat:monthJson.monthlyOperationReport.monthWechatDealProportion};
    var pieColor={pc:'#00b7ee',app:'#00eea8',wechat:'#356fe3'};
    var pieData=[
      {
        name:pieName.pc,
        value:pieValue.pc,
        percentval:piePercnt.pc,
        itemStyle:{
          normal:{
            color:pieColor.pc
          }
        }
      },{
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
      }];
    solidPieChart(pieChartTitle,pieChartBox,pieName,pieValue,pieColor,pieData);
    /*用户分析*/
    //girl-percent 女性比例
    //boy-percent 男性比例
    costomerAnalysisCountup=function(){
      var girlPercent=countUpFloatParam("girl-percent", 0, monthJson.userOperationReport.womanTenderNumProportion);
      girlPercent.start();
      var boyPercent=countUpFloatParam("boy-percent", 0, monthJson.userOperationReport.manTenderNumProportion);
      boyPercent.start();
    };
    costomerAnalysisCountup();
    var solidBarChart=2;
    var solidChartBox=['customer-age','customer-trade'];
    var solidChartTitle=['出借人年龄分布占比','金额分布占比'];
    var solidyAxisCategory=[
      ['18-29岁','30~39岁','40~49岁','50~59岁','60岁以上'],
      ['1万以下','1万-5万','5万-10万','10万-50万','50万以上']];
    var solidCategoryData=[
      [monthJson.userOperationReport.ageFirstStageTenderProportion,
        monthJson.userOperationReport.ageSecondStageTenderProportion,
        monthJson.userOperationReport.ageThirdStageTenderProportion,
        monthJson.userOperationReport.ageFourthStageTenderProportion,
        monthJson.userOperationReport.ageFirveStageTenderProportion],
      [monthJson.userOperationReport.amountFirstStageTenderProportion,
        monthJson.userOperationReport.amountSecondStageTenderProportion,
        monthJson.userOperationReport.amountThirdStageTenderProportion,
        monthJson.userOperationReport.amountFourthStageTenderProportion,
        monthJson.userOperationReport.amountFirveStageTenderProportion]
      ];
    var solidBgData=[[100,100,100,100,100],[100,100,100,100,100]];
    multipleSolidVBar(solidBarChart,solidChartBox,solidChartTitle,solidyAxisCategory,solidCategoryData,solidBgData);

    /*当月之最*/
    //month-trade-most 本季度投资金额最高
    //month-earn-most 本季度收益金额最高
    //month-number-most 本季度投资次数最高
    monthMostCountup=function(){
      var monthTradeMost=countUpIntFloatParam("month-trade-most", 0, monthJson.tenthOperationReportList.mostTenderAmount); monthTradeMost.start();
      var monthEarnMost=countUpIntFloatParam("month-earn-most", 0, monthJson.tenthOperationReportList.bigMinnerProfit); monthEarnMost.start();
      var monthNumberMost=countUpIntParam("month-number-most", 0,  monthJson.tenthOperationReportList.activeTenderNum); monthNumberMost.start();
    }
    monthMostCountup();
    $('#month-trade-most-name').html(monthJson.tenthOperationReportList.mostTenderUsername);
    $('#month-trade-most-age').html(monthJson.tenthOperationReportList.mostTenderUserAge+"岁");
    $('#month-trade-most-area').html(monthJson.tenthOperationReportList.mostTenderUserArea);

    $('#month-earn-most-name').html(monthJson.tenthOperationReportList.bigMinnerUsername);
    $('#month-earn-most-age').html(monthJson.tenthOperationReportList.bigMinnerUserAge+"岁");
    $('#month-earn-most-area').html(monthJson.tenthOperationReportList.bigMinnerUserArea);

    $('#month-number-most-name').html(monthJson.tenthOperationReportList.activeTenderUsername);
    $('#month-number-most-age').html(monthJson.tenthOperationReportList.activeTenderUserAge+"岁");
    $('#month-number-most-area').html(monthJson.tenthOperationReportList.activeTenderUserArea);

    var first=$('#topten-list li').first();
    for(var i=0;i<10;i++){
      switch (i) {
        case 0:
          first.find('.topten-li-num').html(i+1);
          first.find('.topten-li-name').html(monthJson.tenthOperationReportList.firstTenderUsername);
          first.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.firstTenderAmount)+'万元');
          break;
        case 1:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.secondTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.secondTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 2:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.thirdTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.thirdTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 3:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.fourthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.fourthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 4:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.fifthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.fifthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 5:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.sixthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.sixthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 6:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.seventhTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.seventhTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 7:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.eighthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.eighthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 8:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.ninthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.ninthTenderAmount)+'万元');
          $('#topten-list').append(li);
          break;
        case 9:
          var li=first.clone();
          li.find('.topten-li-num').html(i+1);
          li.find('.topten-li-name').html(monthJson.tenthOperationReportList.tenthTenderUsername);
          li.find('.topten-li-amount').html(toMillion(monthJson.tenthOperationReportList.tenthTenderAmount)+'万元');
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
      value:monthJson.tenthOperationReportList.tenTenderAmount,
      percent:monthJson.tenthOperationReportList.tenTenderProportion,
    },{
      name:'其它出借人金额',
      value:monthJson.tenthOperationReportList.otherTenderAmount,
      percent:monthJson.tenthOperationReportList.otherTenderProportion}
    ];
    var hollowCircleColor=['#00eea8','#00b7ee'];
    hollowCircle(hollowCircleBox[0],hollowCircleTitle[0],hollowCircleCategory,hollowCircleData,hollowCircleColor);
  }else{
    alert("数据请求失败，请刷新重试");
  }
});


}

function performancePreviewCountup(){}
function monthPerformanceCountup(){}
function channelAnalysisCountup(){}
/*用户分析*/
function costomerAnalysisCountup(){}
function monthMostCountup(){}

/* 精彩活动 */
function getActiveList(){
  var actarr = [];
  var footarr = [];
  for(var i=0;i<monthJson.operationReportActiveList.length;i++){
    var item = monthJson.operationReportActiveList[i];
    if(item.activtyType == '2'){
      actarr.push(item)
    }else if(item.activtyType == '3'){
      footarr.push(item)
    }
  }
  return {
    actarr: actarr,
    footarr: footarr
  }
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
      actArr += "</section></div>"
    }
  }
  $actDOM.html(actArr);
  setTimeout(callback,1000);
}



// 格式化足迹
function fmtFootprint(footprintList,month){
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
