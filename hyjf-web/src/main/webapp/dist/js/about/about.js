//合作伙伴
$(".tab-tags li").click(function(e) {
    var _self = $(this);
    var idx = _self.attr("panel");
    var panel = $(".tab-panels");
    _self.siblings("li.active").removeClass("active");
    _self.addClass("active");
    panel.children("li.active").removeClass("active");
    panel.children("li[panel=" + idx + "]").addClass("active");
});

//公司简介  
baguetteBox.run('.about-slider', {
    animation: 'fadeIn'
});

//招贤纳士
$(".jobs-list dl ").delegate("dt", "click", function() {
    var dt = $(this);
    var dd= dt.next("dd");
    var dd2 = $(".jobs-list dl dd");
    var dt2 = $(".jobs-list dl dt");
    if (dd.is(":hidden")) {
    	dd2.slideUp(500);
    	dd.slideDown(500);
    	dt2.find('i').removeClass('icon-offline').addClass('icon-addition');
        dt.find('i').removeClass('icon-addition').addClass('icon-offline');
    } else {
        dd2.slideUp(500);
        dt2.find('i').removeClass('icon-offline').addClass('icon-addition');
    }
})