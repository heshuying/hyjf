// --------------------------------------------------------------------------------------------------------------------------------
// 二维码生成工具
$(function() {
	$("#qrcode").each(function() {
		$(this).qrcode({
			text: $("#qrcodeValue").val(),
			render: "canvas", 
			width: 120, height: 120,
			typeNumber: -1, 
			correctLevel: QRErrorCorrectLevel.H,
			background: "#ffffff",
			foreground: "#000000"
		})
	});
})

$(function() {
	$('.record .tab a').click(function(){
		$('.record .tab a').removeClass('choice');
		$(this).addClass('choice');
//		var index=$(this).index('.record .tab a');
//		$('.table-div').hide().eq(index).show();
		var curTab = $(this).attr("data-for");
		if(isLogin == 1){
			if(curTab == "all"){
				getInviteListPage(0, 1, 5);
			}else if(curTab == "already"){
				getInviteListPage(1, 1, 5);
			}else if(curTab == "unfinished"){
				getInviteListPage(2, 1, 5);
			}
		}
		
	})
})

$(document).ready(
	function() {
		if(isLogin == 1){
			getInviteListPage(0, 1, 5);
		}
	
	}
);

// ajax请求列表
function getInviteListPage(investType, page, pageSize){
	var paramsInner = {
			url : webPath + "/activity/inviteseven/getInviteList.do",
			type : 'post',
			dataType : 'json',
			timeout : 60000,
			success : function(data) {
				var inviteListStr = "";
				if(data.data.length == 0){
					
				}else{
					for(var i = 0; i < data.data.length; i++){
						var record = data.data[i];
						inviteListStr += '<tr>' 
							+'<td>' + record.mobileInvited + '</td>'
							+'<td>' + record.usernameInvited + '</td>'
							+'<td>' + getDateTimeFormated(record.registTime) + '</td>'
							+'<td>' + getDateTimeFormated(record.investTime) + '</td>'
							+'<td>' + (record.moneyFirst == 0? "无" : (record.moneyFirst + '元')) + '</td>'
							+'</tr>';
					}
				}
				
				$("#recordInvite").html(inviteListStr);
				
				handlePages(data.paginator, "flipClass", "new-pagination", investType, page, pageSize);
			},
			error : function(xhr, textStatus, errorThrown) {
				var err = {
					'result_code' : 1,
					'error_code' : 1,
					'error' : errorThrown
				};
				errorCallback(err);
			},
			data : "investType=" + investType + "&page=" + page + "&pageSize=" + pageSize
		};
		$.ajax(paramsInner);

}

// 分页处理
function handlePages(paginator, flipClass, paginationId, investType, page, pageSize) {

	if (flipClass == null) {
		flipClass = "flip";
	}
	var pageStr = "";
	if (paginator.slider.length > 0) {
		if (paginator.hasPrePage == true) {
			pageStr = pageStr + "<a class='"+flipClass+" prev' href='javascript:getInviteListPage(" + investType + "," + (page-1) + "," + pageSize + ")' data-page='" + (paginator.page - 1) + "' itemid='pgprev' >上一页</a>";
		} else {
			pageStr = pageStr + "<div class='prev' itemid='pgprev'>上一页</div>";
		}

		for (var i = 0; i < paginator.slider.length; i++) {
			if (paginator.slider[i] == paginator.page) {
				pageStr = pageStr + "<a class='"+flipClass+" active' data-page='" + paginator.page + "' itemid='pg"+paginator.slider[i]+"'>" + paginator.page + "</a>"
			} else {
				pageStr = pageStr + "<a class='"+flipClass+"' href='javascript:getInviteListPage(" + investType + "," + paginator.slider[i] + "," + pageSize + ")' data-page='" + paginator.slider[i] + "' itemid='pg"+paginator.slider[i]+"'>" + paginator.slider[i] + "</a>";
			}
		}
		if (paginator.hasNextPage == true) {
			pageStr = pageStr + "<a class='"+flipClass+" next' href='javascript:getInviteListPage(" + investType + "," + (page+1) + "," + pageSize + ")' data-page='" + (paginator.page + 1) + "' itemid='pgnext'>下一页</a>";
		} else {
			pageStr = pageStr + "<div class='next' itemid='pgnext'>下一页</div>";
		}
	}
	// 挂载分页
	$("#" + paginationId).html(pageStr);

}

// 时间格式化
function getDateTimeFormated(timestamp){
	if(!timestamp){
		return "无";
	}
	
	var time = new Date(parseInt(timestamp)*1000);
	var y = time.getFullYear();//年
	var m = time.getMonth() + 1;//月
	var d = time.getDate();//日
	var h = time.getHours();//时
	var mm = time.getMinutes();//分
	var s = time.getSeconds();//秒
	return time.getFullYear() + "-" + (time.getMonth() + 1) + "-" + time.getDate();
}



