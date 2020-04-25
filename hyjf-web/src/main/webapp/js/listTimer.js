function initEvent(){
	$(".con-title.time").each(function() {
        var _self = $(this);
        var timedata = _self.data("time");
        var currTimmer = setInterval(function() {
            var ts = timedata;
            if (ts >= 0) {
                timer(_self, _self.data("time"));
                
            } else {
            	clearInterval(currTimmer);
            	_self.hide();
            	$.ajax({
        			type : "POST",
        			async:false,
        			url : webPath + "/project/getProjectStatus.do",
        			dataType : 'json',
        			data : {
        				"borrowNid" : _self.data("nid"),
        			},
        			success : function(data) {
        				if (data.status == true && data.projectStatus != "10") {
        					_self.prev().children("a").removeClass("wait").addClass("avalible").text("投资中");
        				}
        			},
        			error : function() {
        				
        			}
        		});
            }
        }, 1000);
    });
	//进度
    $(".progress-cur").each(function() {
        var perc = $(this).data("percent");
        if (perc) {
            $(this).animate({ "width": perc });
        }
    });
}
    
//倒计时
function timer(box, ts) {
    /*
     * @param box 存放倒计时容器
     * @param ts 剩余时间
     * @param current 当前时间
     */
    var dd = parseInt(ts / 60 / 60 / 24, 10); //计算剩余的天数  
    var hh = parseInt(ts / 60 / 60 % 24, 10); //计算剩余的小时数  
    var mm = parseInt(ts / 60 % 60, 10); //计算剩余的分钟数  
    var ss = parseInt(ts % 60, 10); //计算剩余的秒数  
    dd = checkTime(dd);
    hh = checkTime(hh);
    mm = checkTime(mm);
    ss = checkTime(ss);
    if (ts >= 0) {
    	box.data("time",ts-1);
        box.text("剩 " + dd + "天" + hh + "时" + mm + "分" + ss + "秒");
    }
}

function checkTime(i) {
    if (i < 10) {
        i = "0" + i;
    }
    return i;
}


