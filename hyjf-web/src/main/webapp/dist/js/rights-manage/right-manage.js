        var myChart = echarts.init(document.getElementById('main'));
        console.log($("#accountAwait").val());
        //设置鼠标以上改变列表数据字体颜色
        myChart.on('mouseover',function(param){

            if (typeof param.dataIndex != 'undefined') {
                var id=param.dataIndex;
            }
       
            $('ul.table-main'+':eq('+id+')').find('li').css("color","#fa5e28");
        })
        
         myChart.on('mouseout',function(param){
            
            if (typeof param.dataIndex != 'undefined') {
               var id=param.dataIndex;
            }
            $('ul.table-main li').css("color","#404040");
        
        })
        
    
       
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
                subtext:120,
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
                        {value:335},
                        {value:1048}
                    ]
                }
            ]
        };
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
      });

        
        //tab
        $('.show1').click(function(){
            $(this).addClass("active").siblings().removeClass("active");
            $('.tab-parents').removeClass('show').addClass('hide');
            $('.tab-itempar').removeClass('hide').addClass('show');
        });
        $('.show2').click(function(){
            $(this).addClass("active").siblings().removeClass("active");
            $('.tab-parents').removeClass('hide').addClass('show');
            $('.tab-itempar').removeClass('show').addClass('hide');
        });

        function onShow(btn){
            var i = $(btn).index();
            $(btn).addClass('active-in').siblings().removeClass('active-in');
            $('.tab-divin li').eq(i).addClass('show').siblings().removeClass('show').addClass('hide');
            $('.tab-divin2 li').eq(i).addClass('show').siblings().removeClass('show').addClass('hide');
            
        }
      //箭头
        $('.ui-list-title a').click(function(){
           if($(this).hasClass("selected")){
                if($(this).hasClass("selected-asc")){
                    $(this).removeClass("selected-asc");
                }else{
                    $(this).addClass("selected-asc");
                }
                
            }else{
                $(this).addClass("selected");
                var showedEle = $(".ui-list-title a.selected").not($(this));
                hideDetail(showedEle);
            }   

        });
        
        function hideDetail(ele) {
            ele.removeClass("selected");
        }
        
        var wraperMain = $('.wraper-main');
        var wraperMaincon = $('.wraper-main-con');
      
        function showPop(){
            $('.wraper').css('display','block');
            wraperMain.css('display','block');
            $('body').css('overflow','hidden');
            var height = wraperMain.height();
            wraperMain.css('margin-top','-'+(height+25)/2+'px');
        }
        function closePop(){
            $('.wraper').css('display','none');
            wraperMain.css('display','none');
            $('body').removeAttr('style');
           
        }
        function closePop2(){
            $('.wraper').css('display','none');
            wraperMaincon.css('display','none');
            $('body').removeAttr('style');
        }
        function showPop2(){
            $('.wraper').css('display','block');
            wraperMaincon.css('display','block');
            $('body').css('overflow','hidden');
            var height = wraperMaincon.height();
            wraperMaincon.css('margin-top','-'+(height+25)/2+'px');
        }