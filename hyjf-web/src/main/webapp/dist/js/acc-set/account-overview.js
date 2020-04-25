        var myChart = echarts.init(document.getElementById('main'));
        
        //设置鼠标以上改变列表数据字体颜色
        myChart.on('mouseover',function(param){

		    if (typeof param.seriesIndex != 'undefined') {
		        var id=param.data.id;
		    }
		     
		    $('ul.table-main'+':eq('+id+')').find('li').css("color","#fa5e28");
        })
         myChart.on('mouseout',function(param){
        	
		    if (typeof param.seriesIndex != 'undefined') {
		       var id=param.data.id;
		    }
		    $('ul.table-main li').css("color","#404040");
		
        })
         //alt提示
        function alt(el){
	    	$(el).hover(function(){
	    		var word=$(this).data('alt');
	    		var alt=$(this).find('.alt-tit');
	    		if(alt.length){
	    			$(this).find('.alt-tit').stop().fadeIn(150);
	    			$('.function a .alt-tit-show').stop().fadeOut(150);
	    		}else{
	    			$(this).append('<span class="alt-tit">'+word+'</span>')
	    			$(this).find('.alt-tit').stop().fadeIn(150);
	    			$('.function a .alt-tit-show').stop().fadeOut(150);
	    		}
	    	},function(){
	    		$(this).find('.alt-tit').stop().fadeOut(150);
	    		$('.function a .alt-tit-show').stop().fadeIn(150);
	    	})
	    }
		alt('.basic-infor .basic .info .function .state')
         //刷新
         $('.amount .refresh').click(function(){
         	var that=this
         	$(that).find('i').addClass('rotate');
         	$.ajax({
				url : webPath+"/bank/user/synbalance/init.do",
				type: "post"
			})
			.done(function(res){
				if(res.status == true){
					var str = res.info;
					var strarr = str.split(".");
					var strhtml = strarr[0]+'.<span class="sm">'+strarr[1]+'</span>'
					$("#bankBalance").html(strhtml);
					
				}else{
					utils.alert({"id":"syncPop",content: res.message});
				}
				$(that).find('i').removeClass('rotate');
			})
			.fail(function(){
				$(that).find('i').removeClass('rotate');
			})
         })
         /*alert($("#accountAwait").val());*/
$(function(){
	option = {
    	title:{
        	show:true,
        	text:'总计(元)',
        	textStyle:{
        		color:'#404040',
        		fontSize:14,
        		fontWeight:'bolder',
        	},
        	x:'center',
        	y:'133px',
        	subtext:$("#accountAwait").val(),
        	subtextStyle:{
        		color:'#fa5e28',
        		fontSize:14,
        	}
        },
        series: [
           
            {
                
                type:'pie',
                radius: ['37%', '55%'],
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                
                color:['#fa5e28','#f19725'],  
                label: {
                    normal: {
                         show: false
                    }
                },
                data:[
                    {value:$("#bankAccountAwait").val(), id:0},
                    {value:$("#planAccountWait").val(),id:1},
                ]
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
});