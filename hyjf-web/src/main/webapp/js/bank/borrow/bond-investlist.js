$(document).ready(
	function() {
		/**
		 * 获取投资列表
		 */
		getProjectListPage();
		
		$(document).on("click", ".flipClass", function() {
			
			flip($(this).data("page"));
		});
	
	}
);

function getProjectListPage() {
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	$("#projectList").html(utils.loadingHTML);
	doRequest(
			projectPageSuccessCallback,
			projectPageErrorCallback, 
			webPath+ "/bank/web/borrow/getBorrowList.do?projectType="+$("#projectType").val()+"&borrowClass="+$("#borrowClass").val(),
			$("#listForm").serialize(), true,"flipClass","new-pagination");
}

/**
 * 获取投资列表成功回调
 */
function projectPageSuccessCallback(data) {
	var projectList = data.projectList;
	// 挂载数据
	var projectListStr = "";
	//新手标识
	var newStr = "";
	//加息标识
	var addYield = "";
	if(projectList.length == 0){
		projectListStr ='<div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无出借项目...</p></div>';
		$('#new-pagination').hide();
	}else{
		for (var i = 0; i < projectList.length; i++) {
			var project =projectList[i];
			//新手标识
            if (project.projectType == "4") {
            	newStr = '';//'<span class="bg-borders">新手</span>';
            }else if(project.projectType == "11"){
            	newStr = '<span class="bg-borders">尊享</span>';
            }else if(project.projectType == "13"){
            	newStr = '<span class="bg-borders">优选</span>';
            }else{
            	newStr = '';
            }
            //融通宝加息
            if(project.isIncrease == "true" ){
            	addYield = '<span class="bg-add">+'+ project.borrowExtraYield +'%</span>';
            }
            //项目编号
            var nameStr = project.borrowNid;
            if(nameStr.length > 16){
            	nameStr = nameStr.substring(0,16) + '...';
            }
			projectListStr+='<ul><a  href="' + webPath + '/bank/web/borrow/getBorrowDetail.do?borrowNid=' + project.borrowNid + '">'
			+     '<li class="th1"><div> ' + nameStr + newStr + '</div></li>'
			+     '<li class="th2"><div class="yield">' + project.borrowApr + '%'+ addYield +'</div></li>'
			+     '<li class="th3"><div>'+project.borrowPeriod+project.borrowPeriodType+'</div></li>'
			+     '<li class="th4"><div>'+project.borrowAccount+'</div></li>'
			+     '<li class="th5">'
			+        '<div class="bond-num">'
			+          '<div class="progress-all"><div class="progress-cur" style="width:'+((project.projectType == "11" && project.status == "10" && project.bookingBeginTime != 0 && project.bookingEndTime != 0) ? project.borrowAccountScaleAppoint : project.borrowSchedule)+'%"></div></div>'
			+          '<div class="percent"><span>'
						//尊享汇展示预约进度
			+          ((project.projectType == "11" && project.status == "10" && project.bookingBeginTime != 0 && project.bookingEndTime != 0) ? project.borrowAccountScaleAppoint : project.borrowSchedule)
			+          '%</span></div>'
			+         '</div>'
			+     '</li>';
			if(project.status=="10"){
//				if(project.projectType == "11" && project.bookingStatus == "0" && project.bookingBeginTime !=0 && project.bookingEndTime !=0 ){
//					projectListStr+='<li class="th6"><div class="timeout" id="list1" data-start="' + data.nowTime + '" data-end="' + project.time + '"></div></li>';
//				}else if(project.projectType == "11" && project.bookingStatus == "1"){
//					projectListStr+='<li class="th6"> <div class="btn sm">我要预约</div> </li>';	
//				}else if(project.projectType == "11" && project.bookingStatus == "2"){
//					projectListStr+= '<li class="th6"> <div>预约完成</div> </li>';		
//				}else{
//					projectListStr+=   '<li class="th6"> <div class="th-w3">' + project.time + '</div></li>';						
//                }
				projectListStr+='<li class="th6"><div class="timeout" id="list'+i+'" data-start="' + data.nowTime + '" data-end="' + project.time + '"></div></li>';
			}else if(project.status=="11"){
				projectListStr+= '<li class="th6"> <div class="btn sm">出借</div> </li>';
			}else if(project.status=="12"){
				projectListStr+= '<li class="th6"> <div>复审中</div> </li>';
			}else if(project.status=="13"){
				projectListStr+='<li class="th6"> <div>还款中</div> </li>';
			}
			projectListStr+='</a></ul>';
			addYield = "";
		}
		$('#new-pagination').show();
	}
	
	$("#projectList").html(projectListStr);
	$(".timeout").each(function() {
	    var ele = $(this),
	        s = parseInt(ele.data("end")) - parseInt(ele.data("start"));
		    utils.timer(ele, s,function(){
	        	ele.parent().html('<div class="btn sm">出借</div>')
	        });
	})
}

/**
 * 获取投资列表失败回调
 */
function projectPageErrorCallback(data) {

}

/**
 * 分页按钮发起请求
 * 
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function flip(paginatorPage) {
	$("#paginatorPage").val(paginatorPage);
	$("#pageSize").val(pageSize);
	$("#projectList").html(utils.loadingHTML);
	doRequest(
			projectPageSuccessCallback,
			projectPageErrorCallback, 
			webPath+ "/bank/web/borrow/getBorrowList.do?projectType="+$("#projectType").val()+"&borrowClass="+$("#borrowClass").val(), 
			$("#listForm").serialize(), true,"flipClass","new-pagination");
	scrollTo();
}
function scrollTop(speed){
	$("html,body").animate({
        scrollTop: 0
    }, speed);
}


