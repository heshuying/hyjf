var halfJson={
  halfYearOperationReport: {},
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
    if(res.success=='success' && typeof(res.halfYearOperationReport)=='undefined'){
      alert('数据为空');
      return;
    };
    if(typeof(res.success)!='undefined' && typeof(res.resultIsNull)=='undefined'){
      // 半年度数据
      $.extend(halfJson.halfYearOperationReport,res.halfYearOperationReport);
      // 足迹
      $.extend(halfJson.operationReportActiveList,res.operationReportActiveList);
      // 业绩
      $.extend(halfJson.report,res.report);
      //tenthOperationReport 十大投资人数据
      $.extend(halfJson.tenthOperationReportList,res.tenthOperationReport);
      // 用户分析
      $.extend(halfJson.userOperationReport,res.userOperationReport);
      //标题更新
      document.title=halfJson.report.year+"上半年运营报告—汇盈金服官网";
      $('#body-section').attr('data-report-id',halfJson.report.id);
      $("#cname").html(res.report.cnName);
      $("#ename").html(res.report.enName);
      //体验优化、精彩活动、足迹数据列表整理
      var activeList = getActiveList();
      //格式化 体验优化
      if(activeList.optimization.length>0){
        fmtOptimization(activeList.optimization,function(){
          setTimeout(function(){
        	  var expOptSwiper=new Swiper('.swiper-experience-optimization',{
	            //autoplay : 5000,//可选选项，自动滑动
	            loop : false,//可选选项，开启循环
	            pagination : '.experience-optimization-pagination',
	            paginationClickable:true,
	            calculateHeight:true,
	          });
          },300)
        });
      }else{
        $("#experience-optimization").remove();
      }
      // 格式化 精彩活动
      if(activeList.actarr.length>0){
        fmtActive(activeList.actarr,function(){
          setTimeout(function(){
        	  var excEve = new Swiper('.swiper-exciting-events',{
                  //autoplay : 5000,//可选选项，自动滑动
                  loop : false,//可选选项，开启循环
                  pagination : '.exciting-events-pagination',
                  paginationClickable:true,
                  calculateHeight:true,
                });
          },300)
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
      //创建右侧菜单
      var nav=[];
      $("[data-nav]").each(function(){
        nav.push($(this).attr('data-nav'));
      });
      creatRightNav(nav);
      /*业绩总览*/
      performancePreviewCountup=function(){
        // allAmount 累计交易额
        var tradeAmout=countUpIntFloatParam("trade-amout", 0, halfJson.report.allAmount);
        tradeAmout.start();
        // registNum 平台注册人数
        var platformRegisters=countUpIntParam("platform-registers", 0, halfJson.report.registNum);
        platformRegisters.start();
        //allProfit 赚取收益
        var earnAmout=countUpIntFloatParam("earn-amout", 0, halfJson.report.allProfit);
        earnAmout.start();
      }
      performancePreviewCountup();
      /*上半年业绩*/
      $(".half-number").html(halfJson.report.year);
      $("#half-most-month").html(halfJson.halfYearOperationReport.halfYearSuccessMonth);
      halfPerformanceCountup=function(){
        //上半年交易笔数
        var halfTradeNum=countUpIntParam("half-trade-num", 0, halfJson.report.successDealNum); halfTradeNum.start();
        //上半年交易亿元
        var halfTradeBillion=countUpBillionParam("half-trade-billion", 0, toBillion(halfJson.report.operationAmount)); halfTradeBillion.start();
        //上半年充值笔数
        var halfRechargeNum=countUpIntParam("half-recharge-num", 0, halfJson.halfYearOperationReport.halfYearRechargeDeal); halfRechargeNum.start();
        //上半年充值亿元
        var halfRechargeBillion=countUpBillionParam("half-recharge-billion", 0, toBillion(halfJson.halfYearOperationReport.halfYearRechargeAmount)); halfRechargeBillion.start();
        //上半年赚取收益（万元）
        var halfEarnAmout=countUpMillionParam("half-earn-amout", 0, toMillion(halfJson.report.operationProfit)); halfEarnAmout.start();
        //平均年利率
        var averageHalfPercent=countUpFloatParam("average-half-percent", 0, halfJson.halfYearOperationReport.halfYearAvgProfit);
        averageHalfPercent.start();
        //最高单月成交金额
        var halfMostMonthTrade=countUpBillionParam("half-most-month-trade", 0, toBillion(halfJson.halfYearOperationReport.halfYearSuccessMonthAmount)); halfMostMonthTrade.start();
      }
      halfPerformanceCountup();
      var tradeBarChart=1;
      var tbarChartBox=['half-trade']; //盒子id列表
      var chartTitle=['交易金额 单位（亿元）'];
      var chartUnit=['亿元'];
      var yAxisCategory=[[]];
      for(var i=0;i<6;i++){
        yAxisCategory[0].push(halfJson.report.year+'年'+(i+1)+'月');//
      };
      var categoryData=[[
        toBillionFloat(halfJson.halfYearOperationReport.firstMonthAmount),//1月成交额
        toBillionFloat(halfJson.halfYearOperationReport.secondMonthAmount),//2月
        toBillionFloat(halfJson.halfYearOperationReport.thirdMonthAmount),//3月
        toBillionFloat(halfJson.halfYearOperationReport.fourthMonthAmount),//4月
        toBillionFloat(halfJson.halfYearOperationReport.fifthMonthAmount),//5月
        toBillionFloat(halfJson.halfYearOperationReport.sixthMonthAmount),//6月
      ]];
      multipleHBar(tradeBarChart,tbarChartBox,chartTitle,yAxisCategory,categoryData,chartUnit);
      /*借款期限*/
      var yearReportLegendLabel=['30天以下','30天','1个月','2个月','3个月','4个月','5个月','6个月',
        '9个月','10个月','12个月','15个月','18个月','24个月','其它'];
      var yearReportLegendColor=['#5ed4fb','#2fbcd2','#2eace0','#6474cb','#da3534','#ed4544','#dd8b32',
        '#e0b36f','#e0d16f','#b7e06f','#7ce06f','#11ff92','#2fbcd2','#2eace0','#188c9e'];
      var otherPercent=0;
      var yearReportHdata=[{
           //30天以下占比
          value:halfJson.halfYearOperationReport.lessThirtyDayProportion,
          init:false,
        },{
          //30天
          value:halfJson.halfYearOperationReport.thirtyDayProportion,
          init:false,
        },{
          //1个月
          value:halfJson.halfYearOperationReport.oneMonthProportion,
          init:true,
        },{
          //2个月
          value:halfJson.halfYearOperationReport.twoMonthProportion,
          init:true,},
        {
          //3个月
          value:halfJson.halfYearOperationReport.threeMonthProportion,
          init:true,} ,
        {
           //4个月
          value:halfJson.halfYearOperationReport.fourMonthProportion,
          init:true,} ,
        {
          //5个月
          value:halfJson.halfYearOperationReport.fiveMonthProportion,
          init:false,} ,
        {
          //6个月
          value:halfJson.halfYearOperationReport.sixMonthProportion,
          init:true,} ,
        {
          //9个月
          value:halfJson.halfYearOperationReport.nineMonthProportion,
          init:false,} ,
        {
          //10个月
          value:halfJson.halfYearOperationReport.tenMonthProportion,
          init:false,} ,
        {
          //12个月
          value:halfJson.halfYearOperationReport.twelveMonthProportion,
          init:true,} ,
        {
           //15个月
          value:halfJson.halfYearOperationReport.fifteenMonthProportion,
          init:false,} ,
        {
           //18个月
          value: halfJson.halfYearOperationReport.eighteenMonthProportion,
          init:false,} ,
        {
          //24个月
          value: halfJson.halfYearOperationReport.twentyFourMonthProportion,
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
      /*渠道分析*/
      channelAnalysisCountup=function(){
        // app成交笔数
        var appDeal=countUpIntParam("app-deal", 0, halfJson.halfYearOperationReport.halfYearAppDealNum);
        appDeal.start();
        // app成交占比
        var appDealDercent=countUpFloatParam("app-deal-percent", 0, halfJson.halfYearOperationReport.halfYearAppDealProportion);
        appDealDercent.start();
        // 微信成交笔数
        var wechatDeal=countUpIntParam("wechat-deal", 0, halfJson.halfYearOperationReport.halfYearWechatDealNum);
        wechatDeal.start();
        //wechat成交占比
        var wechatDealPercent=countUpFloatParam("wechat-deal-percent", 0, halfJson.halfYearOperationReport.halfYearWechatDealProportion);
        wechatDealPercent.start();
        // pc成交笔数
        var pcDeal=countUpIntParam("pc-deal", 0, halfJson.halfYearOperationReport.halfYearPcDealNum);
        pcDeal.start();
        // pc成交占比
        var pcDealPercent=countUpFloatParam("pc-deal-percent", 0, halfJson.halfYearOperationReport.halfYearPcDealProportion);
        pcDealPercent.start();
      }
      channelAnalysisCountup();
      var pieChartTitle='APP、微信、PC 成交笔数对比图';
      var pieChartBox='compare-three';
      var pieName={pc:'PC',app:'APP',wechat:'微信'};
      var pieColor={pc:'#00b7ee',app:'#00eea8',wechat:'#356fe3'};
      var pieValue={
        pc:halfJson.halfYearOperationReport.halfYearPcDealNum   ,
        app:halfJson.halfYearOperationReport.halfYearAppDealNum  ,
        wechat:halfJson.halfYearOperationReport.halfYearWechatDealNum
      };
      var piePercnt={
        pc:halfJson.halfYearOperationReport.halfYearPcDealProportion ,
        app:halfJson.halfYearOperationReport.halfYearAppDealProportion ,
        wechat:halfJson.halfYearOperationReport.halfYearWechatDealProportion
      };
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
        var girlPercent=countUpFloatParam("girl-percent", 0, halfJson.userOperationReport.womanTenderNumProportion);
        girlPercent.start();
        var boyPercent=countUpFloatParam("boy-percent", 0, halfJson.userOperationReport.manTenderNumProportion);
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
        halfJson.userOperationReport.ageFirstStageTenderProportion,
        halfJson.userOperationReport.ageSecondStageTenderProportion ,
        halfJson.userOperationReport.ageThirdStageTenderProportion,
        halfJson.userOperationReport.ageFourthStageTenderProportion,
        halfJson.userOperationReport.ageFirveStageTenderProportion,
      ],[
        halfJson.userOperationReport.amountFirstStageTenderProportion,
        halfJson.userOperationReport.amountSecondStageTenderProportion,
        halfJson.userOperationReport.amountThirdStageTenderProportion,
        halfJson.userOperationReport.amountFourthStageTenderProportion,
        halfJson.userOperationReport.amountFirveStageTenderProportion,
      ]];
      var solidBgData=[[100,100,100,100,100],[100,100,100,100,100]];
      multipleSolidVBar(solidBarChart,solidChartBox,solidChartTitle,solidyAxisCategory,solidCategoryData,solidBgData);
      /*客户服务*/
      costomerServiceCountup=function(){
        var phoneWorknum=countUpIntParam("phone-worknum", 0, halfJson.halfYearOperationReport.phoneNum); phoneWorknum.start();//phone-worknum 电话接待人数
        var qqWorknum=countUpIntParam("qq-worknum", 0, halfJson.halfYearOperationReport.qqCustomerServiceNum); qqWorknum.start();//qq-worknum QQ接待人数
        var wechatWorknum=countUpIntParam("wechat-worknum", 0, halfJson.halfYearOperationReport.wechatCustomerServiceNum); wechatWorknum.start();//wechat-worknum 微信接待人数
        var questionResolveenum=countUpIntParam("question-resolveenum", 0, halfJson.halfYearOperationReport.dealQuestionNum); questionResolveenum.start();//question-resolveenum 解决问题数
      }
      costomerServiceCountup();
      /*上半年之最*/
      halfMostCountup=function(){
        var halfTradeMost=countUpIntFloatParam("half-trade-most", 0, halfJson.tenthOperationReportList.mostTenderAmount); halfTradeMost.start();//half-trade-most 本季度投资金额最高
        var halfEarnMost=countUpIntFloatParam("half-earn-most", 0, halfJson.tenthOperationReportList.bigMinnerProfit); halfEarnMost.start();//half-earn-most 本季度收益金额最高
        var halfNumberMost=countUpIntParam("half-number-most", 0, halfJson.tenthOperationReportList.activeTenderNum); halfNumberMost.start();//half-number-most 本季度投资次数最高
      }
      halfMostCountup();
      $('#half-trade-most-name').html(halfJson.tenthOperationReportList.mostTenderUsername);
      $('#half-trade-most-age').html(halfJson.tenthOperationReportList.mostTenderUserAge+"岁");
      $('#half-trade-most-area').html(halfJson.tenthOperationReportList.mostTenderUserArea);

      $('#half-earn-most-name').html(halfJson.tenthOperationReportList.bigMinnerUsername);
      $('#half-earn-most-age').html(halfJson.tenthOperationReportList.bigMinnerUserAge+"岁");
      $('#half-earn-most-area').html(halfJson.tenthOperationReportList.bigMinnerUserArea);

      $('#half-number-most-name').html(halfJson.tenthOperationReportList.activeTenderUsername);
      $('#half-number-most-age').html(halfJson.tenthOperationReportList.activeTenderUserAge+"岁");
      $('#half-number-most-area').html(halfJson.tenthOperationReportList.activeTenderUserArea);
      var first=$('#topten-list li').first();
      for(var i=0;i<10;i++){
        switch (i) {
          case 0:
            first.find('.topten-li-num').html(i+1);
            first.find('.topten-li-name').html(halfJson.tenthOperationReportList.firstTenderUsername);
            first.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.firstTenderAmount)+'万元');
            break;
          case 1:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.secondTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.secondTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 2:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.thirdTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.thirdTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 3:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.fourthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.fourthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 4:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.fifthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.fifthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 5:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.sixthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.sixthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 6:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.seventhTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.seventhTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 7:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.eighthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.eighthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 8:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.ninthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.ninthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 9:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(halfJson.tenthOperationReportList.tenthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(halfJson.tenthOperationReportList.tenthTenderAmount)+'万元');
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
        value:halfJson.tenthOperationReportList.tenTenderAmount,
        percent:halfJson.tenthOperationReportList.tenTenderProportion,
      },{
        name:'其它出借人金额',
        value:halfJson.tenthOperationReportList.otherTenderAmount,
        percent:halfJson.tenthOperationReportList.otherTenderProportion}
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

/*上半年业绩*/
function halfPerformanceCountup(){
}
/*渠道分析*/
function channelAnalysisCountup(){}
/*用户分析*/
function costomerAnalysisCountup(){}
/*上半年之最*/
function halfMostCountup(){}
/*客户服务*/
function costomerServiceCountup(){}
//体验优化、精彩活动、足迹数据列表整理
function getActiveList(){
  var optimization=[];//体验优化
  var actarr = [];//精彩活动
  var footarr = [];//足迹
  for(var i=0;i<halfJson.operationReportActiveList.length;i++){
    var item = halfJson.operationReportActiveList[i];
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
  var optArr = '',time="";
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
