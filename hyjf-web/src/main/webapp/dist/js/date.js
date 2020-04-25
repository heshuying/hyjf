function layDateStart(startId){
    var start = {  
        elem: startId, //选择ID为START的input  
        format: 'YYYY/MM/DD hh:mm:ss', //自动生成的时间格式  
        min: laydate.now(), //设定最小日期为当前日期  
        max: '2099-06-16 23:59:59', //最大日期  
        istime: true, //必须填入时间  
        istoday: false,  //是否是当天  
        startId: laydate.now(0,"YYYY/MM/DD hh:mm:ss"),  //设置开始时间为当前时间  
        choose: function(datas){  
             end.min = datas; //开始日选好后，重置结束日的最小日期  
             end.startId = datas; //将结束日的初始值设定为开始日  
        }  
    };
    laydate(start);
}
function layDateEnd(endId){
    var end = {  
        elem: endId,  
        format: 'YYYY/MM/DD hh:mm:ss',  
        min: laydate.now(),  
        max: '2099-06-16 23:59:59',  
        istime: true,  
        istoday: false,  
        start: laydate.now(0,"YYYY/MM/DD hh:mm:ss"),  
        choose: function(datas){  
            start.max = datas; //结束日选好后，重置开始日的最大日期  
        }  
    };  
    laydate(end);
}