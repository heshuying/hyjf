var quarterJson={
  quarterOperationReport: {},
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
//季度数字对应汉字
var quarterText=['一','二','三','四'];
window.onload=function(){
  $.get(apiUrl,function(res){
    if(res.success=='success' && typeof(res.quarterOperationReport)=='undefined'){
      alert('数据为空');
      return;
    };
    if(typeof(res.success)!='undefined' && typeof(res.resultIsNull)=='undefined'){
      // 月度数据
      $.extend(quarterJson.quarterOperationReport,res.quarterOperationReport);
      // 足迹
      $.extend(quarterJson.operationReportActiveList,res.operationReportActiveList);
      // 业绩
      $.extend(quarterJson.report,res.report);
      //tenthOperationReport 十大投资人数据
      $.extend(quarterJson.tenthOperationReportList,res.tenthOperationReport);
      // 用户分析
      $.extend(quarterJson.userOperationReport,res.userOperationReport);

      //标题栏数据更新
      document.title=quarterJson.report.year+"年"+quarterText[quarterJson.quarterOperationReport.quarterType-1]+"季度运营报告—汇盈金服官网";
      $('#body-section').attr('data-report-id',quarterJson.report.id);
      $("#cname").html(res.report.cnName);
      $("#ename").html(res.report.enName);

      //分解活动足迹
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
        var tradeAmout=countUpIntFloatParam("trade-amout", 0, quarterJson.report.allAmount);
        tradeAmout.start();
        // registNum 平台注册人数
        var platformRegisters=countUpIntParam("platform-registers", 0, quarterJson.report.registNum);
        platformRegisters.start();
        //allProfit 赚取收益
        var earnAmout=countUpIntFloatParam("earn-amout", 0, quarterJson.report.allProfit);
        earnAmout.start();
      }
      performancePreviewCountup();
      /*当季业绩*/
      $(".year-number").html(quarterJson.report.year);
      $(".quarter-number").html(quarterText[quarterJson.quarterOperationReport.quarterType-1]);//第几季度文字
      if(quarterJson.quarterOperationReport.amountIncrease<0){
        $('#quarter-trade-billion-describe').addClass("fall");
      }
      if(quarterJson.quarterOperationReport.profitIncrease<0){
        $('#quarter-earn-amout-describe').addClass("fall");
      }
      quarterPerformanceCountup=function(){
        //平台累计成交笔数
        var quarterTradeNum=countUpIntParam("quarter-trade-num", 0, quarterJson.report.successDealNum);
        quarterTradeNum.start();
        //平均预期收益率
        var averageEarnPercent=countUpFloatParam("average-earn-percent", 0,quarterJson.quarterOperationReport.quarterAvgProfit);
        averageEarnPercent.start();
        //operationAmount 成交金额共计（亿元）
        var tradeQuarterAmout=countUpBillionParam("quarter-trade-billion", 0, toBillion(quarterJson.report.operationAmount));
        tradeQuarterAmout.start();
        // amountIncrease 成交同比增长
        var quarterAddPercent=countUpFloatParam("quarter-trade-billion-percent", 0, Math.abs(quarterJson.quarterOperationReport.amountIncrease));
        quarterAddPercent.start();
        // operationProfit 本月为用户赚取收益（万元）
        var quarterEarnAmout=countUpMillionParam("quarter-earn-amout", 0, toMillion(quarterJson.report.operationProfit));
        quarterEarnAmout.start();
        // profitIncrease 赚取收益同比增长
        var addPercent=countUpFloatParam("quarter-earn-amout-percent", 0,Math.abs(quarterJson.quarterOperationReport.profitIncrease));
        addPercent.start();
      };
      quarterPerformanceCountup();
      var tradeBarChart=3;
      var tbarChartBox=['quarter-trade','quarter-compare','quarter-earn']; //盒子id列表
      var chartTitle=['交易金额 单位（亿元）','季度同比 单位（亿元）','赚取收益 单位（万元）'];
      var yAxisCategory=[[
        ],[
          (quarterJson.report.year-1)+'年第'+quarterText[quarterJson.quarterOperationReport.quarterType-1]+'季度',
          quarterJson.report.year+'年第'+quarterText[quarterJson.quarterOperationReport.quarterType-1]+'季度',
        ],[
          (quarterJson.report.year-1)+'年第'+quarterText[quarterJson.quarterOperationReport.quarterType-1]+'季度',
          quarterJson.report.year+'年第'+quarterText[quarterJson.quarterOperationReport.quarterType-1]+'季度',
        ]];
      for(var i=1;i<4;i++){
        var m=(quarterJson.quarterOperationReport.quarterType-1)*3+i;
        yAxisCategory[0].push(quarterJson.report.year+'年'+m+'月');
      }
      var categoryData=[[
        toBillionFloat(quarterJson.quarterOperationReport.firstMonthAmount),//当前季度第1月
        toBillionFloat(quarterJson.quarterOperationReport.secondMonthAmount),//当前季度第2月
        toBillionFloat(quarterJson.quarterOperationReport.thirdMonthAmount),//当前季度第3月
      ],[
        toBillionFloat(quarterJson.quarterOperationReport.lastYearQuarterAmount),//去年本季度成交金额
        toBillionFloat(quarterJson.report.operationAmount),//今年本季度成交金额
      ],[
        toMillion(quarterJson.quarterOperationReport.lastYearQuarterProfit),//去年本季度赚取收益
        toMillion(quarterJson.report.operationProfit),//今年本季度赚取收益
      ]];
      multipleHBar(tradeBarChart,tbarChartBox,chartTitle,yAxisCategory,categoryData);
      /*渠道分析*/
      channelAnalysisCountup=function(){
        // app成交笔数
        var appDeal=countUpIntParam("app-deal", 0, quarterJson.quarterOperationReport.quarterAppDealNum);
        appDeal.start();
        // app成交占比
        var appDealDercent=countUpFloatParam("app-deal-percent", 0, quarterJson.quarterOperationReport.quarterAppDealProportion);
        appDealDercent.start();
        // 微信成交笔数
        var wechatDeal=countUpIntParam("wechat-deal", 0, quarterJson.quarterOperationReport.quarterWechatDealNum);
        wechatDeal.start();
        //wechat成交占比
        var wechatDealPercent=countUpFloatParam("wechat-deal-percent", 0, quarterJson.quarterOperationReport.quarterWechatDealProportion);
        wechatDealPercent.start();
        // pc成交笔数
        var pcDeal=countUpIntParam("pc-deal", 0, quarterJson.quarterOperationReport.quarterPcDealNum);
        pcDeal.start();
        // pc成交占比
        var pcDealPercent=countUpFloatParam("pc-deal-percent", 0, quarterJson.quarterOperationReport.quarterPcDealProportion);
        pcDealPercent.start();
      }
      channelAnalysisCountup();
      var pieChartTitle='APP、微信、PC 成交笔数对比图';
      var pieChartBox='compare-three';
      var pieName={pc:'PC',app:'APP',wechat:'微信'};
      var pieValue={
        pc:quarterJson.quarterOperationReport.quarterPcDealNum   ,
        app:quarterJson.quarterOperationReport.quarterAppDealNum  ,
        wechat:quarterJson.quarterOperationReport.quarterWechatDealNum
      };
      var piePercnt={
        pc:quarterJson.quarterOperationReport.quarterPcDealProportion ,
        app:quarterJson.quarterOperationReport.quarterAppDealProportion ,
        wechat:quarterJson.quarterOperationReport.quarterWechatDealProportion
      };
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
      costomerAnalysisCountup=function(){
        var girlPercent=countUpFloatParam("girl-percent", 0, quarterJson.userOperationReport.womanTenderNumProportion);
        girlPercent.start();
        var boyPercent=countUpFloatParam("boy-percent", 0, quarterJson.userOperationReport.manTenderNumProportion);
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
        quarterJson.userOperationReport.ageFirstStageTenderProportion,
        quarterJson.userOperationReport.ageSecondStageTenderProportion ,
        quarterJson.userOperationReport.ageThirdStageTenderProportion,
        quarterJson.userOperationReport.ageFourthStageTenderProportion,
        quarterJson.userOperationReport.ageFirveStageTenderProportion,
      ],[
        quarterJson.userOperationReport.amountFirstStageTenderProportion,
        quarterJson.userOperationReport.amountSecondStageTenderProportion,
        quarterJson.userOperationReport.amountThirdStageTenderProportion,
        quarterJson.userOperationReport.amountFourthStageTenderProportion,
        quarterJson.userOperationReport.amountFirveStageTenderProportion,
      ]];
      var solidBgData=[[100,100,100,100,100],[100,100,100,100,100]];
      multipleSolidVBar(solidBarChart,solidChartBox,solidChartTitle,solidyAxisCategory,solidCategoryData,solidBgData);
      /*季度之最*/
      quarterMostCountup=function(){
        var quarterTradeMost=countUpIntFloatParam("quarter-trade-most", 0, quarterJson.tenthOperationReportList.mostTenderAmount); quarterTradeMost.start();
        var quarterEarnMost=countUpIntFloatParam("quarter-earn-most", 0, quarterJson.tenthOperationReportList.bigMinnerProfit); quarterEarnMost.start();
        var quarterNumberMost=countUpIntParam("quarter-number-most", 0, quarterJson.tenthOperationReportList.activeTenderNum); quarterNumberMost.start();
      };
      quarterMostCountup();
      $('#month-trade-most-name').html(quarterJson.tenthOperationReportList.mostTenderUsername);
      $('#month-trade-most-age').html(quarterJson.tenthOperationReportList.mostTenderUserAge+"岁");
      $('#month-trade-most-area').html(quarterJson.tenthOperationReportList.mostTenderUserArea);

      $('#month-earn-most-name').html(quarterJson.tenthOperationReportList.bigMinnerUsername);
      $('#month-earn-most-age').html(quarterJson.tenthOperationReportList.bigMinnerUserAge+"岁");
      $('#month-earn-most-area').html(quarterJson.tenthOperationReportList.bigMinnerUserArea);

      $('#month-number-most-name').html(quarterJson.tenthOperationReportList.activeTenderUsername);
      $('#month-number-most-age').html(quarterJson.tenthOperationReportList.activeTenderUserAge+"岁");
      $('#month-number-most-area').html(quarterJson.tenthOperationReportList.activeTenderUserArea);
      var first=$('#topten-list li').first();
      for(var i=0;i<10;i++){
        switch (i) {
          case 0:
            first.find('.topten-li-num').html(i+1);
            first.find('.topten-li-name').html(quarterJson.tenthOperationReportList.firstTenderUsername);
            first.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.firstTenderAmount)+'万元');
            break;
          case 1:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.secondTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.secondTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 2:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.thirdTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.thirdTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 3:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.fourthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.fourthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 4:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.fifthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.fifthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 5:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.sixthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.sixthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 6:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.seventhTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.seventhTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 7:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.eighthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.eighthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 8:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.ninthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.ninthTenderAmount)+'万元');
            $('#topten-list').append(li);
            break;
          case 9:
            var li=first.clone();
            li.find('.topten-li-num').html(i+1);
            li.find('.topten-li-name').html(quarterJson.tenthOperationReportList.tenthTenderUsername);
            li.find('.topten-li-amount').html(toMillion(quarterJson.tenthOperationReportList.tenthTenderAmount)+'万元');
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
        value:quarterJson.tenthOperationReportList.tenTenderAmount,
        percent:quarterJson.tenthOperationReportList.tenTenderProportion,
      },{
        name:'其它出借人金额',
        value:quarterJson.tenthOperationReportList.otherTenderAmount,
        percent:quarterJson.tenthOperationReportList.otherTenderProportion}
      ];
      var hollowCircleColor=['#00eea8','#00b7ee'];
      hollowCircle(hollowCircleBox[0],hollowCircleTitle[0],hollowCircleCategory,hollowCircleData,hollowCircleColor);
    }
  });
}

/*业绩总览*/
function performancePreviewCountup(){}
/*当季业绩*/
function quarterPerformanceCountup(){}
/*渠道分析*/
function channelAnalysisCountup(){}
/*用户分析*/
function costomerAnalysisCountup(){}
/*季度之最*/
function quarterMostCountup(){}
/*精彩活动、体验优化、足迹分类*/
function getActiveList(){
  var optimization=[];//体验优化
  var actarr = [];//精彩活动
  var footarr = [];//足迹
  for(var i=0;i<quarterJson.operationReportActiveList.length;i++){
    var item = quarterJson.operationReportActiveList[i];
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
