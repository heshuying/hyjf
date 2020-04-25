//日期显示格式化
function getTrusteePayTime(time){
	if (time && time != "") {
		return '<span>'+time.substring(0,10)+'</span><br/><span>'+time.substring(11,time.length)+'</span>';
	}
	return "";
}

/**
 * 获取待授权标的记录
 */
function getBorrowAuthListPage(paginatorPage) {
	if(paginatorPage){
		$("#paginatorPage").val(paginatorPage);
		$("#empowerList").html("<tr><td colspan='9'>"+utils.loadingHTML+"</td></tr>");
	}else{
		$("#paginatorPage").val(1);
	}
	$("#pageSize").val(pageSize);
	var userId = $("#userId").val();
	doRequest(
			borrowAuthSuccessCallback, 
			borrowAuthErrorCallback, 
			webPath+ "/bank/web/user/borrowauth/need_auth.do?userId=" + userId+"&borrowNid="+$("#auth_borrowNid").val()+"&startDate="+$("#auth_startDate").val()+"&endDate="+$("#auth_endDate").val(),
			$("#listForm").serialize(), 
			true, 
			"authClass", 
			"empower-pagination");
}

/**
* 获取已还款记录成功回调
 */
function borrowAuthSuccessCallback(data) {
	var paidList = data.borrowList;
	var strPaidList = "";
	if (paidList != null) {
		$(paidList).each(function(i, item) {
			strPaidList += 
				'<tr class="loan-div-a">'+ 
					'<td class="item1 loan-div-row">'+ item.borrow_nid+ '</td>'+ // 项目编号
					'<td class="loan-div-row-2">'+ item.borrowApr+ '%</td>'+ 
					'<td class="loan-div-row-2">'+ item.borrowAccount+ '元</td>'+ 
					'<td class="loan-div-row-2">'+ item.borrowPeriod+ '</td>'+ 
					'<td class="loan-div-row-2">'+ item.borrowStyle+ '</td>'+ 
					'<td class="loan-div-row-2">'+ item.entrustedUserName+ '</td>'+ 
					'<td class="loan-div-row-2">'+ item.entrustedRelName+ '</td>'+ 
					'<td class="loan-div-row-3">'+ getTrusteePayTime(item.time)+ '</td>'+ 
					'<td class="loan-div-row-2"><a href="../borrowauth/trustee_pay.do?borrowId='+item.borrow_nid+'" class="btn sm">授权</a></td>'+ 
				' </tr>';
		});// each end
	} else {
		$("#empower-pagination").html("")
	}
	if (strPaidList == "") {
		strPaidList = '<div  class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您所找的页面暂无数据～</p></div>';
		$("#empowerListEmptyStyle").html(strPaidList);
		strPaidList = "";
	} else {
		$("#empowerListEmptyStyle").html("");
	}
	$("#empowerList").html(strPaidList);
}

/**
 * 获取已还款记录失败回调
 */
function borrowAuthErrorCallback(data) {

}

/**
 * 获取已授权的记录
 */
function getBorrowAuthedListPage(paginatorPage) {
	if(paginatorPage){
		$("#paginatorPage").val(paginatorPage);
		$("#empowerOrgList").html("<tr><td colspan='9'>"+utils.loadingHTML+"</td></tr>");
	}else{
		$("#paginatorPage").val(1);
	}
	$("#pageSize").val(pageSize);
	var userId = $("#userId").val();
	doRequest(
			borrowAuthedSuccessCallback, 
			borrowAuthedErrorCallback, 
			webPath+ "/bank/web/user/borrowauth/authed.do?userId=" + userId+"&borrowNid="+$("#authed_borrowNid").val()+"&startDate="+$("#authed_startDate").val()+"&endDate="+$("#authed_endDate").val(),
			$("#listForm").serialize(), 
			true, 
			"authedClass", 
			"empowerOrg-pagination");
}

/**
* 获取已授权记录成功回调
 */
function borrowAuthedSuccessCallback(data) {
	var paidList = data.borrowList;
	var strPaidList = "";
	if (paidList != null) {
		$(paidList).each(function(i, item) {
			strPaidList += 
				'<tr class="loan-div-a">'+
	            '<td class="item1 loan-div-row">'+item.borrow_nid+'</td>'+
	            '<td class="loan-div-row-2">'+item.borrowApr+'%</td>'+
	            '<td class="loan-div-row-2">'+item.borrowAccount+'元</td>'+
	            '<td class="loan-div-row-2">'+item.borrowPeriod+'</td>'+
	            '<td class="loan-div-row-2">'+item.borrowStyle+'</td>'+
	            '<td class="loan-div-row-2">'+item.entrustedUserName+'</td>'+
	            '<td class="loan-div-row-2">'+item.entrustedRelName+'</td>'+
	            '<td class="loan-div-row-3">'+getTrusteePayTime(item.trusteePayTime)+'</td>'+
	            '</tr>';
		});// each end
	} else {
		$("#empowerOrg-pagination").html("")
	}
	if (strPaidList == "") {
		strPaidList = '<div  class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您所找的页面暂无数据～</p></div>';
		$("#empowerOrgListEmptyStyle").html(strPaidList);
		strPaidList = "";
	} else {
		$("#empowerOrgListEmptyStyle").html("");
	}
	$("#empowerOrgList").html(strPaidList);
}

/**
 * 获取已还款记录失败回调
 */
function borrowAuthedErrorCallback(data) {

}

//显示第几个标签
function changeTab(tab){
	$(".tab-tags li:eq("+tab+")").click();
}