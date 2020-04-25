$('.ui-list-title a').click(function(){
	var sortValue;
   if($(this).hasClass("selected")){
        $(this).removeClass("selected");
        sortValue = $(this).attr("sortValue");
        getProjectListPage(sortValue + "=ASC");
    }else{
        $(this).addClass("selected");
        var showedEle = $(".ui-list-title a.selected").not($(this));
		hideDetail(showedEle);
		sortValue = $(this).attr("sortValue");
        getProjectListPage(sortValue + "=DESC");
    }   

});
function hideDetail(ele) {
    ele.removeClass("selected");
}

$(document).ready(
	function() {
		/**
		 * 获取投资列表
		 */
		getProjectListPage("tenderTimeSort=ASC");
		
		$(document).on("click", ".flipClass", function() {
			flip($(this).data("page"));
		});
	
	}
);

function getProjectListPage(sortValue) {
	$("#paginatorPage").val(1);
	$("#pageSize").val(pageSize);
	doRequest(
			projectPageSuccessCallback,
			projectPageErrorCallback, 
			webPath + "/bank/user/credit/canCreditListData.do?" + sortValue,
			$("#listForm").serialize(), true,"flipClass","new-pagination");
}

/**
 * 获取投资列表成功回调
 */
function projectPageSuccessCallback(data) {
	var recordList = data.recordList;
	var recordHtml = "";
	recordHtml += '<tbody>';
	if(recordList.length>0){
		for(i=0;i<recordList.length;i++){
			var record = recordList[i];
			
			recordHtml +=  '<tr>';
			recordHtml +=     '<td class="ui-list-item pl1">'+record.borrowNid+'</td>';
			recordHtml +=     '<td class="ui-list-item pl2">'+record.borrowPeriod + " / " + record.borrowApr+'%</td>';
			recordHtml +=     '<td class="ui-list-item pl3">'+record.tenderTime+'</td>';
			recordHtml +=     '<td class="ui-list-item pl4">'+record.creditAccount+'</td>';
			recordHtml +=     '<td class="ui-list-item pl5">'+record.tenderPeriod+'天</td>';
			recordHtml +=     '<td class="ui-list-item pl6">'+record.lastDays+'天</td>';
			recordHtml +=     '<td class="ui-list-item pl7">';
			recordHtml +=         '<a class="value" style="text-decoration: none;" href="#" onclick="checkCanCredit(\'' + record.borrowNid + '\',\'' + record.tenderNid + '\')">转让<span class="iconfont icon-more"></span></a>';
			recordHtml +=     '</td>';
			recordHtml +=  '</tr>';
		}
		$("#new-pagination").show();
	}else{
		recordHtml += 
		'<tr>' + '	<td class="ui-list-item" class="empty-icon" colspan="7"><div class="data-empty" style="background:#fff;"><div class="empty-icon"></div><p class="align-center">暂无可转让债权</p></div></td>' + '</tr>'
		$("#new-pagination").hide();
	}
	recordHtml += '</tbody>';
	$("#tableList tbody").remove();
	$("#tableList").append(recordHtml);
	
}

/**
 * 获取公司动态信息失败回调
 */
function projectPageErrorCallback(data) {
	console.log("error");
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
	doRequest(
			projectPageSuccessCallback,
			projectPageErrorCallback, 
			webPath + "/bank/user/credit/canCreditListData.do", 
			$("#listForm").serialize(), true,"flipClass","new-pagination");
	scrollTo();
}

function checkCanCredit(borrowNid, tenderNid){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: webPath +"/bank/user/credit/checkCanCredit.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"borrowNid":borrowNid,
			"tenderNid":tenderNid
		},
		error: function(request) {
	        utils.alert({
 				id:"errPop",
 				type:"alert",
 				content:"连接服务器出错,请稍后重试"
 			});
	    },
		success: function(data){
			if(data.resultFlag==0){
				window.location.href = webPath+'/bank/user/credit/tendertocreditdetail.do?borrowNid='+borrowNid+'&tenderNid='+tenderNid;
			}else{
				utils.alert({
	     				id:"errPop",
	     				type:"alert",
	     				content:data.msg
	     			});
			}
		}
	});
}

