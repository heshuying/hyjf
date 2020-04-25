$(document).ready(function(){

  //输入金额校验规则
  var amountRegex = /^[1-9]{1}\d{0,1}[0]{1}$/;
  getUserInfo();
  //遮罩层关闭按钮点击事件
  $("#mbtn").click(function(e){
    $(".model").addClass("hide");
  });
  //输入框校验事件
  $('#coupon-amount').keyup(function(){
    checkAmount($(this),amountRegex);
  });
  $('#coupon-amount').change(function(){
    checkAmount($(this),amountRegex);
  });
  //定制确认
  $("#submit").click(function(){
	  if(!isCustomizable){
		showModel(textTips.createFail,customizableTips,textTips.confirmModel,'',textTips.confirmTrue);
		return;
  	};
    if (!whetherLogin) {
      //  未登录状态跳转登录
      window.location.href = webPath + "/user/login/init.do";
      return;
    }
    if (cashCouponNumber >= 3) {
      showModel(textTips.createFail,textTips.createFailTips,textTips.confirmModel,'',textTips.confirmTrue)
      return;
    }
    var gonext=checkAmount($('#coupon-amount'),amountRegex);
    if(gonext){
      showModel(textTips.confirmTitle,"",textTips.chooseModel,textTips.confirmTrue,textTips.confirmCancel)
    }else{
    	$("#amount-tips").addClass("deeps");	
    }
  });
  //提交定制
  $("#submbtn").click(function(){	
    $.ajax({
      type:'post',
      url:webPath
          + "/activity518/act.do",
      data:{type:1,money:$('#coupon-amount').val()},
      success:function(result){ setSuccess(result)},
      error:function(result){setError(result)}
    });
  });
  $('#go-used').click(function(){
    //去首页
    window.location.href = '/hjhplan/initPlanList.do';
  });
});

//默认状态设置
var whetherLogin=false,
  isCustomizable=true,//是否可定制  
  customizableTips="",
  cashCouponNumber=0,
  usedCoupon=false,//可用代金券
  voucherFormKey = 0, //记录未使用的代金券的金额
  termOfValidity = ""; //记录当前未使用代金券的有效期;
//券类型设置
var voucherForm= {
  "10": "3000元",
  "20": "6000元",
  "30": "10000元",
  "40": "13000元",
  "50": "18000元",
  "60": "22000元",
  "70": "25000元",
  "80": "30000元",
  "90": "35000元",
  "100": "40000元",
  "110": "45000元",
  "120": "50000元",
  "130": "55000元",
  "140": "60000元",
  "150": "65000元",
  "160": "70000元",
  "170": "75000元",
  "180": "80000元",
  "190": "90000元",
  "200": "100000元"
};
//提示文字
var textTips={
  chooseModel:'choose',
  confirmModel:'confirm',
  amountTip:'amount-tips',
  amountErr:'amount-error',
  operationEdit:'edit',
  operationSuccess:'success',
  confirmTitle:"确定要定制该代金券吗?",
  confirmTrue:"确定",
  confirmCancel:"取消",
  creating:"正在定制中。。。",
  createFail:"定制失败",
  createFailTips:"活动期间，每位用户最多可定制3张代金券。",
  mostCreateTips:"老板！！！您的代金券数量已达上限，不能继续定制，谢谢合作！",
  postErrorTitle:'网络连接异常',
  postErrorTips:"请刷新页面重试",
};
function getUserInfo() {
  $.ajax({
    type:'post',
    url:webPath
        + "/activity518/act.do",
    data:{type:2},
    success:function(result){ getSuccess(result)},
    error:function(result){getError(result)}
  });
};
//获取信息成功
function getSuccess(result){
  var res=result;//JSON.parse(result);
  if (res.status == "000") {
    if (res.data.IsLogined) {
      //  判断是否登录
      whetherLogin = res.data.IsLogined; //记录已登录状态
      cashCouponNumber = res.data.number; //记录已定制的代金券的数量
      if (res.data.notUsed) {
        //  判断用户是否有未使用的代金券
        usedCoupon = true; //如果有代金券未使用显示成功定制页面
        voucherFormKey = res.data.cashCoupon; //记录未使用的代金券的金额
        termOfValidity = res.data.termOfValidity; //记录当前未使用代金券的有效期
        $('.coupon-num').html(res.data.cashCoupon);
        $('.coupon-for-loan').html(voucherForm[res.data.cashCoupon]);
        $(".validity-date").html(res.data.termOfValidity);
        $("#operation-edit").removeClass(textTips.operationEdit).addClass(textTips.operationSuccess);
      }
    }
  } else {
	  isCustomizable=false;
	  customizableTips=res.msg;
  }
};
//获取信息失败
function getError(res){
	showModel(textTips.postErrorTitle,res.msg,textTips.confirmModel,'',textTips.confirmTrue);
};
//提交定制成功
function setSuccess(result){
  var res=result;//JSON.parse(result);
  if (res.status == "000") {
    $('#operation-edit').removeClass(textTips.operationEdit).addClass(textTips.operationSuccess);
    $(".model").addClass("hide");
  } else {
    //99 定制请求异常
    //00 已有待使用券，非正常请求定制
    showModel(textTips.createFail,res.msg,textTips.confirmModel,'',textTips.confirmTrue);
  }
};
//提交定制失败
function setError(res){
  showModel(textTips.createFail,textTips.createFailTips,textTips.confirmModel,'',textTips.confirmTrue);
};
function checkAmount(t,amountRegex){
  if (
      !amountRegex.test($(t).val()) ||
      $(t).val() > 200
    ) {
      $("#amount-tips").removeClass(textTips.amountTip).addClass(textTips.amountErr);
      return false;
    } else {
      $("#amount-tips").addClass(textTips.amountTip).removeClass(textTips.amountErr).removeClass("deeps");
      $('.coupon-num').html($('#coupon-amount').val());
      $('.coupon-for-loan').html(voucherForm[$('#coupon-amount').val()]);
      return true;
    }
}
function showModel(title,content,btnType,subtext,canceltext){
  $(".model").removeClass("hide");
  $(".mtitle").html(title);
  $(".mcontent").html(content);
  $("#modelbtn").removeClass().addClass(btnType);
  $(".submbtn").html(subtext);
  $(".cancel").html(canceltext);
}
