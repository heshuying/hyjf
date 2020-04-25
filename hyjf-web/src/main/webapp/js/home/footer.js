//$(document).ready(
//		function() {
//			
//			var username = decodeURIComponent(getCookie("hyjfUsername"));// 用户名
//			var sex = getCookie("sex");// 性别
//			var iconurl = getCookie("iconurl");// 自定义头像
//			var roleId = getCookie("roleId");// 用户身份
//			
//			if (roleId == '1') {
//				if($("#loanManage").length > 0){
//					$("#loanManage").remove();
//				}
//			} 
//			/**
//			 * 头部通用信息设置
//			 */
//			headerSet();
//			function headerSet() {
//				if ('' != username) {// 如果cookie中读到用户名
//					var face = $("#face");
//					if(!face.is(':visible')){//如果node是显示的则隐藏node元素，否则显示
//
//						$("#faceUrl").attr("src",webPath + "/img/default.png");
//						// $("#faceUrl").attr("src", "${ctx}" + iconurl);
//					}
//				} else {
//					var login = $("#login");
//					if(!login.is(':visible')){
//						// 显示登录前样式
//						login.show();
//						$("#line").show();
//						$("#register").show();
//					}
//				}
//			}
//			
//		});


/*
 *  @func 打开弹窗
 *  @param ele string 选择器名
 *  @param msg string 填充文字信息或者dom元素
 */
// function popUpWin(obj){
//     var obj = $.extend({
//         "ele" : ".pop-box", //容器
//         "msg" : null, //提示信息
//         "txtname": ".pop-txt", //存放提示信息容器
//         "overlayer" : ".pop-overlayer" //遮罩层
//     }, obj);
//     //显示遮罩层
//     $(obj.overlayer).fadeIn();
//     //显示弹窗
//     $(obj.ele).fadeIn();
//     if(obj.msg){
//         //添加文字信息
//         $(obj.ele).find(obj.txtname).html(obj.msg);
//     }
// }
// /*
// * 关闭弹窗
// */
// function popOutWin(obj){
//     var obj = $.extend({
//         "ele" : ".pop-box", //容器
//         "overlayer" : ".pop-overlayer" //遮罩层
//     }, obj);
//     $(obj.overlayer).fadeOut();
//     $(obj.ele).fadeOut();
// }
////制保留2位小数，如：2，会在2后面补上00.即2.00    
// function toDecimal2(x) {    
//     var f = parseFloat(x);    
//     if (isNaN(f)) {    
//         return false;    
//     }    
//     var f = Math.round(x*100)/100;    
//     var s = f.toString();    
//     var rs = s.indexOf('.');    
//     if (rs < 0) {    
//         rs = s.length;    
//         s += '.';    
//     }    
//     while (s.length <= rs + 2) {    
//         s += '0';    
//     }    
//     return s;    
// }   
 //二维码弹窗
// $(".tool-haspop").hover(function(){
//	$(this).children(".tool-popup").stop().fadeIn(300);
//},function(){
//	$(this).children(".tool-popup").fadeOut(300);
//})
//$(".mobile-client span").hover(function(){
//	$(".mobile-client .tool-popup").stop().fadeIn(300);
//},function(){
//	$(".mobile-client .tool-popup").fadeOut(300);
//})

////股票弹窗
//$("#tool-shares").hover(function(){
//	$(this).children(".shares-panel").stop().fadeIn(300);
//},function(){
//	$(this).children(".shares-panel").fadeOut(300);
//})

