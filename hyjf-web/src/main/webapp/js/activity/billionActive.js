$(function(){
//			banner累计投资效果
			var account=$.trim($('.account-data').data("txt"));
			if(account.length>11 || account.length<10){
				$('.account-invest').find('.account-invest-digital').animate({top:-730+"px"},800);
			}else{
				    if(account.length == 10){account = "0" + account;}
					for(var j=0;j<account.length;){
						var ss=account.substr(j, 1); 
						var mm =parseInt(ss);
						$('.account-invest:eq('+j+')').find('.account-invest-digital').animate({top:(-mm)*73+"px"},800);
						j++;}
			        } 
			for(var i=0;i<11;i++){
				imgs="<img src='../../img/active/billion/digital-"+parseInt(i)+".png' />";
				$('.account-invest-digital').append(imgs);
			}
			
			
//			tab选择
            $('.billion-active-tab li').click(function(){
            	var index = $(this).data('index');
            	$('.billion-active-tab li').removeClass('activeTab');
            	$(this).addClass('activeTab');
            	$('.billion-active-list>div').hide();
            	$('.active-section-'+index).show();
            })
            
//            钻石选择效果
    		var crownId = parseInt($.trim($('#minStage').val()));
    		var isEnd = parseInt($.trim($('#isEnd').val()));
//    		百亿活动结束
    		if(isEnd == 1){
    			$('.reward-boundary').removeClass('get-reward-sign');
    			$('#stage_dom'+crownId).addClass('win-announce')
    			$('#stage_dom'+crownId).children('img').attr("src","../../img/active/billion/crown.png");
    			$('.reward-countining').children('img').attr("src","../../img/active/billion/diamond-01.png");
    			$('#finish_'+crownId).addClass('win-section');
    			$('#finish_'+(crownId+1)).addClass('win-section');
    			$('#finish_'+crownId).children('img').attr("src","../../img/active/billion/diamond-03.png");
    			$('#finish_'+(crownId+1)).children('img').attr("src","../../img/active/billion/diamond-03.png");
//        		奖励设置发放切换点显示,默认显示奖励发放
    			$('.reward-set-tab').show();
    			$('.reward-set-list li:eq(1)').show();
    		}else if(isEnd == 0){
//    	          百亿活动未结束
//    		奖励设置发放切换点隐藏,默认显示奖励设置
    			$('.reward-set-tab').hide();
    			$('.reward-set-list li:eq(0)').show();
    			$('.reward-countining').each(function(){
    				if($(this).hasClass('now-section')){
    					 $(this).children('img').attr("src","../../img/active/billion/diamond-03.png");
    				}else{
    					 $(this).children('img').attr("src","../../img/active/billion/diamond-01.png");
    				}
    			})
    	    }
            
//		中奖设置tab切换

		$('.reward-set-tab li').click(function(){
			var index = $(this).index();
			$('.reward-set-tab li').removeClass('setActive');
			$(this).addClass('setActive');
			$('.reward-set-list li').hide();
			$('.reward-set-list li:eq('+index+')').show();
		//  $('.reward-set-list>ul').animate({"left":-(index*930)+"px"});
		})
		
		//用户姓名隐藏中间
	var personName = $('.prizeUserName');
		personName.each(function(){
			var _this = $(this);
			var namestr,namelength,first,last;
			namestr =$.trim(_this.text());
			namelength = namestr.length;
			first = namestr.substr(0,1);
		    last = namestr.substr(namestr.length-1,1);
			    if(namelength>1){
					_this.text(first+"****"+last);
				}else if(namelength=1){
					_this.text(first+"****");
		        }
		});
});




function checkTime(i){
	if(i<10){i="0"+i;}else{i= i;}
	return i;
};

//活动二累加计时
$(function(){
	 var theTime,theTime1,theTime2,nowTime,endTime,timer;
	 var stage,now,t1,t2,t3,t4,t5,t6,lastTime,arr;
     stage = parseInt($('#stage').val());//当前进行的阶段;
     now = $('#nowTime').val();//系统当前时间;
     t1 = $('#time100').val();
     t2 = $('#time101').val();
     t3 = $('#time102').val();
     t4 = $('#time103').val();
     t5 = $('#time104').val();
     t6 = $('#time105').val();
     arr = [t1,t2,t3,t4,t5,t6];//存储到达100~105亿的每个亿的时间数组;
     lastTime = now - arr[stage-1];//当前正在进行累加的原始时间;
     $('.reward-boundary').each(function(){
    	var num = $(this).data('num');
 		var aNum = arr[num];
 		var aNum1 = arr[num+1];
 		var endDif = aNum1-aNum;
 		if(aNum>0){
 			if(endDif<0){endDif=0;}
 			 formatSeconds(endDif);
 			  $(this).children('div').html('<p><span>'+checkTime(parseInt(theTime2))+'</span>时</p>'+'<p><span>'+checkTime(parseInt(theTime1))+'</span>分</p>'+'<p><span>'+checkTime(parseInt(theTime))+'</span>秒</p>	')
 		}
});
//     累加器累加计时
     if(lastTime<0){
//    	 出错了
//    	 alert("出错啦！")
     }else{
    	 timer =  setInterval(function(){
    			lastTime=lastTime+1; 
    			formatSeconds(lastTime);
    			$('#stage_dom'+stage).children('div').html('<p><span>'+checkTime(parseInt(theTime2))+'</span>时</p>'+'<p><span>'+checkTime(parseInt(theTime1))+'</span>分</p>'+'<p><span>'+checkTime(parseInt(theTime))+'</span>秒</p>	')
    		},1000);
     }
   
	
function formatSeconds(sec) {
    theTime = parseInt(sec);// 毫秒
    theTime1 = 00;// 分
    theTime2 = 00;// 小时
    if(theTime >= 60) {
        theTime1 = parseInt(theTime/60);
        theTime = parseInt(theTime%60);
        if(theTime1 >= 60) {
	        theTime2 = parseInt(theTime1/60);
	        theTime1 = parseInt(theTime1%60);
        }
    }
}
});

//活动三倒计时

var timer = setInterval(CountDown,1000);
var endTime = $("#nextKillTime").val();
var nowTime = $("#currentTime").val();
var t = endTime-nowTime;
var nextKillId = $("#nextKillId").val();
//t= 30000;
function CountDown(){
	
	
	if(t<=1200000){
	    var m=Math.floor(t/1000/60%60); 
	    var s=Math.floor(t/1000%60);
	    var msg1 = checkTime(m)+"分"+checkTime(s)+"秒"+"后开抢";
	    $('#style-'+nextKillId).html(msg1);
	    $('#style-'+nextKillId).removeClass('colordarkblue');
	    $('#style-'+nextKillId).addClass('colorpink');
	    $('#style-'+nextKillId).attr("href","javascript:;")
	    
	    if(t<=0){
	    	clearInterval(timer);
	    	var msg2 = "<img src='../../img/active/billion/click-sign.png'/>"+"点我抢券"+"<p>剩余<span>"+$("#remainingNum").val()+"</span>张</p>"
		    $('#style-'+nextKillId).removeClass('colorpink');
		    $('#style-'+nextKillId).addClass('colorpurple');
		    $('#style-'+nextKillId).addClass('clickEvent');
		    
		     $('#style-'+nextKillId).html(msg2);
	    }
	}
	t=t-1000;
}

