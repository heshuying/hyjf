//创建基于jquery的插件
/*简介
 * 1、formatMoney－－－格式化钱数（每隔三位加逗号，小数点自动补齐两位）；注意必须放在页面末尾（要不然找不到dom元素)
 * 2、
 * 
 */
//类方法
/*格式化数据开始*/
;(function($){
	$.fn.extend({
		"formatString":function(){
			alert("test");
		}
	});
})(jQuery);
/*格式化数据结束*/

//静态方法
;(function($){
	$.extend({
//		格式化钱数（每隔三位加逗号，小数点自动补齐两位）；注意必须放在页面末尾（要不然找不到dom元素)
		"formatMoney":function(str,id){
			var newStr = "";
			var count = 0;
			if(str.indexOf(".")==-1){
			   for(var i=str.length-1;i>=0;i--){
				 if(count % 3 == 0 && count != 0){
				   newStr = str.charAt(i) + "," + newStr;
				 }else{
				   newStr = str.charAt(i) + newStr;
				 }
				 count++;
			   }
			   str = newStr + ".00"; //自动补小数点后两位
			}
			else
			{
			   for(var i = str.indexOf(".")-1;i>=0;i--){
				 if(count % 3 == 0 && count != 0){
				   newStr = str.charAt(i) + "," + newStr;
				 }else{
				   newStr = str.charAt(i) + newStr; //逐个字符相接起来
				 }
				 count++;
			   }
			   str = newStr + (str + "00").substr((str + "00").indexOf("."),3);
			 }
			document.getElementById(id).innerHTML=str;
		}
	});
})(jQuery)
