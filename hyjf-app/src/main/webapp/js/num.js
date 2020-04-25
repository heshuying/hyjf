function formatNum(str){
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
			str = newStr + ""; //自动补小数点后两位
				   //console.log(str)
		}else{
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
				return str;
}
/*window.onload = function(){
	var num = document.getElementById("about_deal").getElementsByTagName("i");
	for (var i = 0;i<num.length;i++) {
		var a = num[i].innerText;
		var b = formatNum(a);
		num[i].innerHTML = b;
	}		
}*/
/*(function(){
	var $winH = $(window).height();
	for(var i=1;i<6;i++){
		$("#move"+i).css("height",$winH+"px");
	}	
})()

$(window).scroll(function(){
		var h = 10
		var $winH = $(window).height();;
		var $top = $(window).scrollTop();
		if(($top>h)&&($top<$winH-h)){
			$('html,body').animate({scrollTop: $winH+'px'}, 800);
		}else if(($top>h+$winH)&&($top<$winH*2-h)){
			$('html,body').animate({scrollTop: $winH*2+'px'}, 800);
		}
	})*/