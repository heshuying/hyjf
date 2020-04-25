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
        "amount": {
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
        "amount": {
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
        htlAjax();
    }
});

$(".confirm-btn").click(function(){
    $("#detialForm").submit();
})

$("#moneyIpt").keydown(function(){
	$("#ajaxErr").remove();
});
//全投
$(".money-btn.available").click(function(){
    var res = getMaxMoney();
    var ipt = $("#moneyIpt"); //投资额
    ipt.val(res);
    ipt.trigger("change");
    ipt.valid();
})
//初始化幻灯
baguetteBox.run('.new-detail-img-c',{animation:'fadeIn'});
function getMaxMoney(){
    /*
    * @funct 获取最大可投资额
    */
    var total = parseFloat($("#projectData").data("total"));//可投金额
    var balance = parseFloat($("#userData").data("balance"));//用户余额
    var avaPurchase = parseFloat($("#userData").data("user-avapurchase")); //用户可投金额
    var res = Math.min(total,balance,avaPurchase);
    return parseInt(res);
}
function htlAjax(){
	var url = "/hyjf-web/htl/check.do";
	var data = {
			"flag":1,
			"amount":$("#moneyIpt").val()
	};
	$.ajax({
		"url" : url,
		"type": "POST",
		"data":data,
		"success": function(data){
			if(data.error){
				var ipt = $("#moneyIpt");
				ipt.parent().parent().append("<span id='ajaxErr' class='error'>"+data.data+"</span>");
			}else{
				window.location.href = data.url;
			}
		},
	})
}
