//设置日利率 月利率
var rateUnit = $(".bd-rateunit").text();
var rateName = rateUnit.indexOf("%")!==-1?"月":"日";
$(".bd-ratename").text(rateName);