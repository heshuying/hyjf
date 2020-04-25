var timmer;
Number.prototype.toFixed2 = function() {
	return parseFloat(this.toString().replace(/(\.\d{2})\d+$/, "$1"));
}
var tabs = $(".new-detail-tab");
tabs.each(function(){
    var li = $(this).children();
    var len = li.length;
    li.css("width",100/len+"%");
});
$(".new-detail-main").click(function(e) {
	var _self = $(e.target);
	if (_self.is("li")) {
		var idx = _self.attr("panel");
		var panel = _self.parent().next(".new-detail-tab-panel");
		_self.siblings("li.active").removeClass("active");
		_self.addClass("active");
		panel.children("li.active").removeClass("active");
		panel.children("li[panel=" + idx + "]").addClass("active");
	}
})
$.validator.addMethod("increase", function(value, element, params) {
	/*递增金额验证*/
	var increase = parseFloat($("#increase").val()); //递增金额
	var total = parseFloat($("#projectData").data("total")); //可投金额
	var tendermin = parseFloat($("#projectData").data("tendermin"));//起投金额
	if(total<tendermin || total<increase){
		//若金额出现异常，检查是否是最后一笔交易
		return total == value;
	}
    return value%increase == 0; //投资金额对倍增金额求模为0

},"投资递增金额须为"+$("#increase").val()+"元的整数倍");




//初始化幻灯
baguetteBox.run('.new-detail-img-c', {
	animation : 'fadeIn'
});

