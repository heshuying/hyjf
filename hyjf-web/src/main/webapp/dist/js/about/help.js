var sHeight = $('.side-bar').height();
$('.side-tab').css({"min-height":sHeight});
$('.side-bar').css({"min-height":sHeight});

//  左侧栏目展开收起
$("#side-list").delegate("dt", "click", function() {
	var _self = $(this);
	var dd = _self.siblings('dd');
	var i = _self.children('i');
	if(dd.is(":hidden")) {
		dd.show();
		i.removeClass('arrows');
	} else {
		dd.hide();
		i.addClass('arrows');
		
	}
});
//  	左右栏目相对展开收起
$("#side-list").delegate("dd", "click", function() {
	var _self = $(this);
	var idx = _self.index() - 1; //1-
	var dlIdx = _self.parent('dl').index(); //0-
	var sidetab = $('.side-tab-item:eq(' + dlIdx + ')'); //当前对应的section列表
	var issure = sidetab.find('.issure-list:eq(' + idx + ')'); //当前对应的section里的每个小分类内容
	$("#side-list dl dd").find('span').removeClass('active');
	_self.children('span').addClass('active');
	sidetab.removeClass("hide").siblings().addClass('hide');
	issure.removeClass("hide").siblings().addClass('hide');
});

//  	展开详情
$(".issure-list-cont").delegate(".title", "click", function() {
	var _self = $(this);
	var i = _self.children('i');
	var p = _self.next('div');
	var lip = _self.parent('li').siblings('li').children('.title');
	if(p.is(":hidden")) {
		$('.message').slideUp(300);
		$('.title').removeClass('hover').addClass('back');
		p.slideDown(300);
		_self.removeClass('back').addClass('hover');
	} else {
		$('.message').slideUp(300);
		$('.title').removeClass('hover').addClass('back');
	}
});

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
    results = regex.exec(location.search);
    return results == null ? "": decodeURIComponent(results[1]);
}
if(!indexId){
	$("#hp01").click();
}else{
	$("#" + indexId ).click();
}
$(function(){
	if(getParameterByName('side')){
		var side=getParameterByName('side')
		var issure=getParameterByName('issure')
		$('#'+side).click()
		if(issure){
			$(".issure-list-cont .title[itemid='"+issure+"']").click()
		}
	}
})
