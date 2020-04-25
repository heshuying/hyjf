"use strict";
Number.prototype.toFixed2=function (){
    return parseFloat(this.toString().replace(/(\.\d{2})\d+$/,"$1"));
}
$(".new-detail-main").click(function(e){
    var _self = $(e.target);
    if(_self.is("li")){
        var idx = _self.attr("panel");
        var panel = _self.parent().next(".new-detail-tab-panel");
        _self.siblings("li.active").removeClass("active");
        _self.addClass("active");
        panel.children("li.active").removeClass("active");
        panel.children("li[panel="+idx+"]").addClass("active");
    }
})

var validate = $("#detialForm").validate({
    "rules": {
        "money": {
            "required":true,
            "number":true,
            "min": 1,
            "max": getMaxMoney()
        },
        "termcheck":{
            required:{
                depends:function(){
                    return $(".new-detail-inner.htl").length;
                }
            }
        }
    },
    "messages": {
        "money": {
            "required":"亲，您还没有填写投资金额",
            "number":"亲，只能填写数字",
            "min": "投标金额应大于 1 元",
            "max": "投标金额应小于 "+getMaxMoney()+" 元"
        },
        "termcheck":"请先阅读并同意汇盈金服投资协议"
    },
    "ignore": ".ignore",
    errorPlacement: function(error, element) {
        if(element.parents(".new-detail-inner.htl").length){
            error.insertAfter(element.parent());
        }
        else{
            error.insertAfter(element.parent().parent());
        }
    },
    submitHandler: function(form) {
        form.submit();
    }
});

$(".confirm-btn.avaliable").click(function(){
    $("#detialForm").submit();
})


//全投
$(".money-btn.available").click(function(){
    var res = getMaxMoney();
    var ipt = $("#moneyIpt"); //投资额
    ipt.val(res);
    ipt.trigger("change");
    ipt.valid();
})
//金额变动
$("#moneyIpt").change(function(){
    var _self = $(this);
    priceChanged(_self);
}).keyup(function(){
    var _self = $(this);
    priceChanged(_self);
});
function getMaxMoney(){
    /*
    * @funct 获取最大可投资额
    */
    var total = $("#projectData").data("total");//可投金额
    var balance = $("#userData").data("balance");//可投金额
    var res = total >= balance ? balance : total;
    return parseFloat(res);
}
function priceChanged(ipt){
    /*
    * @funct 投资金额变动检测
    * @param ipt jqueryObject 金额输入框 
    */
    var projectD = $("#projectData");
    var userD = $("#userData");
    var income = $("#income");
    //收益计算相关数据对象
    var data = {
        "type" : projectD.data("type"), //还款方式 1:等额本息；2：等额本金；3：到期还本还息（按月计息）；4：按天计息，到期还本还息；5:先息后本
        "monthCount" : projectD.data("month-count"),//还款月数
        "dayCount" : projectD.data("day-count"),//还款天数
        "annualRate" : projectD.data("annual-rate"),//年利率
        "capital" : ipt.val(),//贷款本金
    }
    if(ipt.valid()){
        income.text(getIncome(data));
    }else{
        income.text("0.00");
    }
}

function getIncome(obj){
    /*
    * @funct 收益计算
    * @obj object 收益计算相关数据对象（属性在下面变量声明）
    */
    var type = obj.type;//还款方式
    var monthCount = obj.monthCount; //还款月数
    var dayCount = obj.dayCount;//剩余天数
    var annualRate = obj.annualRate;   //年利率
    var capital = obj.capital  //贷款本金


    var monthlyRate = annualRate / 12; //月利率
    var totalRate = 0; //存储总利息
    if(obj.type == "1"){
        /* 
            等额本息  
            -----------------
            月利率       = 年利率/12
            每月偿还本息 =〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
            总利息       = 还款月数×每月月供额-贷款本金 
        */
        var monthlyRatePow = Math.pow(1+monthlyRate,monthCount);
        var monthIdx = 1;
        var monthlyTotal = capital*monthlyRate*monthlyRatePow / (Math.pow(1+monthlyRate,monthCount)-1);//每月偿还本息（舍去小数点后两位）
        totalRate = monthCount*monthlyTotal.toFixed2(2)-capital//总利息
    }else if(obj.type == "2"){
        /* 
            等额本金  
            -----------------
            每月偿还本息   = (贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率
            每月应还本金   = 贷款本金÷还款月数
            每月应还利息   = 剩余本金×月利率=(贷款本金-已归还本金累计额)×月利率
            每月月供递减额 = 每月应还本金×月利率=贷款本金÷还款月数×月利率
            总利息         = 〔(总贷款额÷还款月数+总贷款额×月利率)+总贷款额÷还款月数×(1+月利率)〕÷2×还款月数-总贷款额
        */
        totalRate = (capital/monthCount+capital*monthlyRate+capital/monthCount*(1+monthlyRate))/2*monthCount - capital;
    }else if(obj.type == "3"){
        /*
            到期还本还息（按月计息）
            应还利息=投资本金*年化收益÷12*融资月数
            应还本息=本金+应还总利息=投资本金*【1+（年化收益÷12*融资月数）】；
        */
        totalRate = (capital*monthlyRate*monthCount).toFixed2(2);
    }else if(obj.type == "4"){
        /*
            按天计息，到期还本还息
            应还利息=投资本金*年化收益÷360*融资天数
            应还本息=本金+应还总利息=投资本金*（1*（年化收益÷360*融资天数））；
        */
        
        totalRate = capital*annualRate/360*dayCount;
    }else if(obj.type == "5"){
        /*
            先息后本
            每月应还利息=投资本金*年化收益率/12*投资月数/还款期数
        */
        totalRate = capital*monthlyRate*monthCount;
    }
    return totalRate.toFixed2(2);
}

//初始化幻灯
baguetteBox.run('.new-detail-img-c',{animation:'fadeIn'});