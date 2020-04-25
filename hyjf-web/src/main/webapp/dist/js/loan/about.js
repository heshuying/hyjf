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
