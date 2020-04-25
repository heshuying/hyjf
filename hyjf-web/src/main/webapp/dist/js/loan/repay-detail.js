var dateTime = [];
//还款排序
	//箭头
$('.ui-list-title a').click(function(){
	var orderFlag = $(this).data("val");
   if($(this).hasClass("selected")){
        if($(this).hasClass("selected-asc")){
            $(this).removeClass("selected-asc");
            //降序
            if (orderFlag==1) {//还款时间排序
            	$("#repayOrder").val(1);
            	$("#checkTimeOrder").val("");
            	getRepayListPage();
    		}else{//到账时间排序
    			$("#repayOrder").val("");
            	$("#checkTimeOrder").val(1);
            	getRepayListPage();
    		}
        }else{
            $(this).addClass("selected-asc");
            if (orderFlag==1) {//还款时间排序
            	$("#repayOrder").val(0);
            	$("#checkTimeOrder").val("");
            	getRepayListPage();
    		}else{//到账时间排序
    			$("#repayOrder").val("");
            	$("#checkTimeOrder").val(0);
            	getRepayListPage();
    		}
        }
       
    }else{
    	$(this).addClass("selected");
    	var showedEle = $(".ui-list-title a.selected").not($(this));
        hideDetail2(showedEle);
        
        if($(this).hasClass("selected-asc")){
            $(this).removeClass("selected-asc");
            //降序
            if (orderFlag==1) {//还款时间排序
            	$("#repayOrder").val(1);
            	$("#checkTimeOrder").val("");
            	getRepayListPage();
    		}else{//到账时间排序
    			$("#repayOrder").val("");
            	$("#checkTimeOrder").val(1);
            	getRepayListPage();
    		}
        }else{
            $(this).addClass("selected-asc");
            if (orderFlag==1) {//还款时间排序
            	$("#repayOrder").val(0);
            	$("#checkTimeOrder").val("");
            	getRepayListPage();
    		}else{//到账时间排序
    			$("#repayOrder").val("");
            	$("#checkTimeOrder").val(0);
            	getRepayListPage();
    		}
        }
    }   
});

function hideDetail2(ele) {
    ele.removeClass("selected");
}
function getRepayMoneyTotalAction(){
    var roleId = $("#roleId").val();
    var borrowNid=$("#repayBorrowNidSrch").val();
    var repayStartDateSrch=$("#repayStartDateSrch").val();
    var repayEndDateSrch=$("#repayEndDateSrch").val();
    var item7=$(".item7").index();//item7不等于7，即点击了垫付链接，查询本期应还总额和应还笔数
    if(item7 ==7){
        //查询本期应还总额和应还笔数
        $.ajax({
            url :  webPath + "/bank/web/user/repay/getRepayMoneyTotalAction.do",//url
            type : "POST",
            async : false,
            dataType: "json",
            data :  {roleId:roleId,borrowNid:borrowNid,repayStartDateSrch:repayStartDateSrch,repayEndDateSrch:repayEndDateSrch},
            success : function(data) {
                if (data!=null||data!="") {
                    var res=eval(data);
                    repayMoneyTotal=res.repayMoneyTotal.toFixed(2);
                    repayMoneyNum=res.repayMoneyNum;
                    $("#repayMoneyNum").html(repayMoneyNum);
                    $("#repayMoneyTotal").html(repayMoneyTotal);
                }
            }
        });
    }

}

//下载延迟
function downloading(){
	$('.downloadargrement').off('click').click(function(){
		if(!$(this).hasClass('disable')){
			var that=this
			location.href = $(that).data('href');
			$(that).find('.loadingargrement').show()
			$(that).addClass('disable')
			setTimeout(function(){
				$(that).removeClass('disable')
				$(that).find('.loadingargrement').hide()
			},60000)
		}
	})
}

$(document).ready(function() {
    var repayMoneyTotal=0;
    var repayMoneyNum=0;
    var roleId = $("#roleId").val();
    /**
     * 待还款记录
     */
	getRepayListPage();
    /**
     * 待还款记录分页点击
     */
    $(document).on("click", ".repayClass", function() {
    	getRepayList($(this).data("page"));
    });
    if(roleId == 2){
		/**
		 * 已还款列表
		 */
	    getPaidListPage();
	    
		/**
	     * 已还款记录分页点击
	     */
		$(document).on("click", ".paidClass", function() {
			getPaidList($(this).data("page"));
		});
		
    }
	if(roleId == 3){
		 /**
	     * 垫付机构已垫付列表
	     * 
	     */
	    getRepayOrgListPage();
	
		/**
		 * 已垫付还款记录分页点击
		 */
		$(document).on("click", ".paidClass", function() {
			getRepayOrgList($(this).data("page"));
		});
	}
	
	var showFlag = $("#showFlag").val();
	if (showFlag != null && showFlag != '') {
		console.log("开始变更!");
		$("#alerdyRepay").addClass("active");
		$("#waitRepay").removeClass("active");
		
		$("#alerdyTitle").addClass("active");
		$("#waitTitle").removeClass("active");
		
	}
	if('tab_index' in window&&tab_index&&tab_index!=""){
		$(".tab-tags li:eq("+tab_index+")").click();
	}
});

	/**
	 * 获取待还款记录
	 */
	function getRepayListPage() {
	    $("#paginatorPage").val(1);
	    $("#pageSize").val(pageSize);
	    var endDate = $("#endDate").val();
		//查询本期应还总额和应还笔数
		getRepayMoneyTotalAction();
	    doRequest(repayListPageSuccessCallback, repayListPageErrorCallback, 
	    		webPath + "/bank/web/user/repay/repaylist.do?status=0&borrowNid="+$("#borrowNid").val()+"&startDate="+$("#startDate").val()+"&endDate="+endDate
	    		+"&checkTimeOrder="+$("#checkTimeOrder").val()+"&repayOrder="+$("#repayOrder").val(),$("#listForm").serialize(), true, "repayClass", "repay-pagination");
	}
	/**
	 * 获取待还款记录成功回调
	 */
	function repayListPageSuccessCallback(data) {
	
		var repayList = data.repayList;
		var roleId = data.roleId;
		var strRepayList = "";
		if (repayList != null) {
			for (var i = 0; i < repayList.length; i++) {
				//待还款 0 和 还款中 2  repayList[i].status
				var agreementStr = "";
				if (repayList[i].projectType != '13' && repayList[i].fddStatus=='1') {
					agreementStr = '<a data-href="' + webPath + '/bank/web/user/repay/downloadBorrowerPdf.do?borrowNid='+ repayList[i].borrowNid + '" class="font-bg font-nocolor downloadargrement">下载协议<span class="loadingargrement" style="display:none"><img style="width:12px;margin-left:5px;position: relative;top: 2px;" src="'+webPath+'/dist/images/loading.gif"/></span></a>';
				}
				if (roleId == "3") {
					strRepayList +=
						'<tr class="loan-div-a">'
						/*应还时间*/
						+'	<td class="item1 loan-div-row">'
						+'		<span class="font-nocolor">'+ repayList[i].repayYesTime + '</span>'
						+		agreementStr 
						+ '	</td>'
						+'	<td class="item2 loan-div-row">'+ repayList[i].borrowNid+ '</td>' /*项目编号*/
						+'	<td class="item3 loan-div-row">'+ "第"+repayList[i].orgBorrowPeriod+"期" + '</td>'/*期数*/
						+'	<td class="item4 loan-div-row">'+ repayList[i].borrowInterest+"%" + '</td>'/*预期年收益*/
						+'	<td class="item5 loan-div-row">'+ repayList[i].borrowAccount +'元</td>'/*借款金额*/
						+'	<td class="item6 loan-div-row">'+ repayList[i].borrowTotal+'元</td>'/*应还总额*/
						+'	<td class="item6 loan-div-row">'+ repayList[i].realAccountYes+'元</td>'/*本期应还总额*/
				}else{
					strRepayList +=
						'<tr class="loan-div-a">'
						/*应还时间*/
						+'	<td class="item1 loan-div-row">'
						+'		<span class="font-nocolor">'+ repayList[i].repayTime + '</span>'
						+		agreementStr 
						+ '	</td>'
						+'	<td class="item2 loan-div-row">'+ repayList[i].borrowNid+ '</td>' /*项目编号*/
						+'	<td class="item3 loan-div-row">'+ repayList[i].borrowInterest+"%" + '</td>'/*预期年收益*/
						+'	<td class="item4 loan-div-row">'+ repayList[i].borrowAccount +'元</td>'/*借款金额*/
						+'	<td class="item5 loan-div-row">'+ repayList[i].yesAccount +'元</td>'/*到账金额*/
						+'	<td class="item5 loan-div-row">'+ repayList[i].yesAccountTime +'</td>'/*到账时间*/
						+'	<td class="item5 loan-div-row">'+ repayList[i].borrowTotal+'元</td>'/*应还总额*/
						+'	<td class="item5 loan-div-row">'+ repayList[i].realAccountYes+'元</td>'/*本期应还总额*/
				}
					/*操作*/
					if (roleId == "3") {// 垫付机构  暂不支持汇添金还款
						// 常规还款
						var repaydate = new Date(repayList[i].repayTime);
						var nowdate = data.nowdate; // new Date();
						var teststau = repayList[i].status;
						if (repayList[i].status == 0 ) { // 3天以内，垫付机构可以代为还款
							// if (repaydate - nowdate <= 3 * 24 * 60 * 60 * 1000) {
								strRepayList += '<td class="item7 loan-div-row"> <a href="' + webPath+'/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" class="btn sm">垫付</a></td>'
						         +'</tr>';
							// }else{
							// 	strRepayList += '<td class="item7 loan-div-row"> <span style  class="btn btn-disabled sm">垫付</span></td>'
						     //     +'</tr>';
							// }
							
						} else {
							if(repayList[i].repayMoneySource == 0 ){
								strRepayList += '<td class="item7 loan-div-row"><a href="' + webPath+'/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" class="btn sm" style="white-space: nowrap;">个人还款中</a></td>' 
											 +'</tr>';
							}else{
								strRepayList += '<td class="item7 loan-div-row"><a href="' + webPath+'/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" class="btn sm">机构还款中</a></td>' 
									 +'</tr>';
							}
						}
					} else { // 借款人
						if (repayList[i].status == 0){
							strRepayList  +='<td class="item7 loan-div-row"><a href="' + webPath+'/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" class="btn sm">还款</a></td>'
							  +'</tr>';
						} else {
							if(repayList[i].repayMoneySource == 0 ){
								strRepayList += '<td class="item7 loan-div-row"><a href="' + webPath+'/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" class="btn sm">还款中</a></td>' 
									 +'</tr>';
							}else{
								strRepayList += '<td class="item7 loan-div-row"><a href="' + webPath+'/bank/web/user/repay/repaydetail.do?borrowNid=' + repayList[i].borrowNid + '" class="btn sm" style="white-space: nowrap;">机构还款中</a></td>' 
									 +'</tr>';
							}
						}
					}
			}
		} else {
			$("#repay-pagination").html("");
		}
		if (strRepayList == "") {
			strRepayList ='<div class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您所找的页面暂无数据～</p></div>';
			$("#repayListEmptyStyle").html(strRepayList);
			strRepayList = "";
		}else{
			$("#repayListEmptyStyle").html("");
		}
		$("#repayList").html(strRepayList);
		downloading()
	}
	
	
	/**
	 * 获取待还款记录失败回调
	 */
	function repayListPageErrorCallback(data) {
	
	}
	/**
	 * 获取待还款记录分页按钮发起请求
	 * 
	 * @param successCallback
	 * @param errorCallback
	 * @param url
	 * @param paginatorPage
	 */
	function getRepayList(paginatorPage) {
	    $("#paginatorPage").val(paginatorPage);
	    $("#pageSize").val(pageSize);
	    var endDate = $("#endDate").val();
	    $("#repayList").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
	    doRequest(repayListPageSuccessCallback, repayListPageErrorCallback,
	    		webPath + "/bank/web/user/repay/repaylist.do?status=0&borrowNid="+$("#borrowNid").val()+"&startDate="+$("#startDate").val()+"&endDate="+endDate+
	    		"&checkTimeOrder="+$("#checkTimeOrder").val()+"&repayOrder="+$("#repayOrder").val(),$("#listForm").serialize(), true, "repayClass", "repay-pagination");
	}

	/**
	 * 获取已还款记录
	 */
	function getPaidListPage() {
		$("#paginatorPage").val(1);
		$("#pageSize").val(pageSize);
		var endDate = $("#endDate").val();
		doRequest(paidListPageSuccessCallback, paidListPageErrorCallback, 
				webPath + "/bank/web/user/repay/repaylist.do?status=1&borrowNid="+$("#borrowNid").val()+"&startDate="+$("#startDate").val()+"&endDate="+endDate, 
				$("#listForm").serialize(), true, "paidClass", "paid-pagination");
	}
	
	/**
	 * 获取垫付机构已垫付记录
	 * 
	 */
	function getRepayOrgListPage(){
		$("#paginatorPage").val(1);
		$("#pageSize").val(pageSize);
		var endDate = $("#endDate").val();
		doRequest(repayOrgListPageSuccessCallback, repayOrgListPageErrorCallback, 
				webPath + "/bank/web/user/repay/repayOrglist.do?status=1&borrowNid="+$("#borrowNid").val()+"&startDate="+$("#startDate").val()+"&endDate="+endDate, 
				$("#listForm").serialize(), true, "paidClass", "paid-pagination");
	}
	
	/**
	 * 获取已垫付还款记录分页按钮发起请求
	 * 
	 * @param successCallback
	 * @param errorCallback
	 * @param url
	 * @param paginatorPage
	 */
	function getRepayOrgList(paginatorPage) {
	    $("#paginatorPage").val(paginatorPage);
	    $("#pageSize").val(pageSize);
	    var endDate = $("#endDate").val();
	    $("#repayOrgList").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
	    doRequest(repayOrgListPageSuccessCallback, repayOrgListPageErrorCallback,
	    		webPath + "/bank/web/user/repay/repayOrglist.do?status=1&borrowNid="+$("#borrowNid").val()+"&startDate="+$("#startDate").val()+"&endDate="+endDate, 
	    		$("#listForm").serialize(), true, "paidClass", "paid-pagination");
	}

	/**
	 * 获取已还款记录成功回调
	 */
	function repayOrgListPageSuccessCallback(data) {
		var paidList = data.repayList;//依然是paidList
		var roleId = data.roleId;
		var strPaidList = "";
		if (paidList != null) {
			for (var i = 0; i < paidList.length; i++) {
				var agreementStr = "";
				// 融通宝不展示协议
				if (paidList[i].projectType != '13' && paidList[i].fddStatus=='1' ) {
					agreementStr = '<a data-href="' + webPath + '/bank/web/user/repay/downloadBorrowerPdf.do?borrowNid='+ paidList[i].borrowNid + '" class="font-bg font-nocolor downloadargrement">下载协议<span class="loadingargrement" style="display:none"><img style="width:12px;margin-left:5px;position: relative;top: 2px;" src="'+webPath+'/dist/images/loading.gif"/></span></a>';
				}
				if(roleId == "3"){//垫付机构显示当期应还时间 add by cwyang
					strPaidList +=
						'<tr class="loan-div-a">'
						/*应还时间*/
						+'	<td class="bor1 loan-div-row">'
						+'		<span class="font-nocolor">'+ paidList[i].repayYesTime + '</span>'
						+		agreementStr		
						+'	</td>'
						+'	<td class="bor2 loan-div-row">'+ paidList[i].borrowNid + '</td>'/*项目编号*/
						+'	<td class="bor3 loan-div-row">第'+ paidList[i].repayPeriod + '期</td>'/*期数*/
						+'	<td class="bor4 loan-div-row">'+ paidList[i].borrowInterest + "%" + '</td>'/*预期年收益*/
						+'	<td class="bor5 loan-div-row">'+ paidList[i].repayTotal +'元'+ '</td>'/*已还总额*/
						+'	<td class="bor6 loan-div-row">'+ paidList[i].repayYesTime+ '</td>'/*实还时间*/
						+'	<td class="bor7 loan-div-row">'+ paidList[i].repayMoneySource + '</td>'/*实还时间*/
						+ '	<td class="bor8 loan-div-row"><span class="grey">已还款</span></td>'/*状态*/
						+'</tr>';
				}else{
					
					strPaidList +=
						'<tr class="loan-div-a">'
						/*应还时间*/
						+'	<td class="bor1 loan-div-row">'
						+'		<span class="font-nocolor">'+ paidList[i].repayTime + '</span>'
						+		agreementStr		
						+'	</td>'
						+'	<td class="bor2 loan-div-row">'+ paidList[i].borrowNid + '</td>'/*项目编号*/
						+'	<td class="bor3 loan-div-row">第'+ paidList[i].repayPeriod + '期</td>'/*期数*/
						+'	<td class="bor4 loan-div-row">'+ paidList[i].borrowInterest + "%" + '</td>'/*预期年收益*/
						+'	<td class="bor5 loan-div-row">'+ paidList[i].repayTotal +'元'+ '</td>'/*已还总额*/
						+'	<td class="bor6 loan-div-row">'+ paidList[i].repayYesTime+ '</td>'/*实还时间*/
						+'	<td class="bor7 loan-div-row">'+ paidList[i].repayMoneySource + '</td>'/*实还时间*/
						+ '	<td class="bor8 loan-div-row"><span class="grey">已还款</span></td>'/*状态*/
						+'</tr>';
				}
				
			}
		} else {
			$("#paid-pagination").html("")
		}
		if (strPaidList == "") {
			strPaidList ='<div  class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您所找的页面暂无数据～</p></div>';
			$("#paidListEmptyStyle").html(strPaidList);
			strPaidList = "";
		}else{
			$("#paidListEmptyStyle").html("");
		}
		$("#repayOrgList").html(strPaidList);
		downloading()
	}

	/**
	 * 获取已还款记录失败回调
	 */
	function repayOrgListPageErrorCallback(data) {

	}

	/**
	 * 获取已还款记录成功回调
	 */
	function paidListPageSuccessCallback(data) {
		var paidList = data.repayList;//依然是paidList
		var roleId = data.roleId;
		var strPaidList = "";
		if (paidList != null) {
			for (var i = 0; i < paidList.length; i++) {
				var agreementStr = "";
				// 融通宝不展示协议
				if (paidList[i].projectType != '13' && paidList[i].fddStatus == "1") {
					agreementStr = '<a data-href="' + webPath + '/bank/web/user/repay/downloadBorrowerPdf.do?borrowNid='+ paidList[i].borrowNid + '" class="font-bg font-nocolor downloadargrement">下载协议<span class="loadingargrement" style="display:none"><img style="width:12px;margin-left:5px;position: relative;top: 2px;" src="'+webPath+'/dist/images/loading.gif"/></span></a>';
				}
				if (roleId == "3") {//垫付机构显示当期应还时间 add by cwyang
					strPaidList +=
						'<tr class="loan-div-a">'
						/*应还时间*/
						+'	<td class="bor1 loan-div-row">'
						+'		<span class="font-nocolor">'+ paidList[i].repayYesTime +'</span>'
						+		agreementStr		
						+'	</td>'
						+'	<td class="bor2 loan-div-row">'+ paidList[i].borrowNid + '</td>'/*项目编号*/
						+'	<td class="bor3 loan-div-row">'+ paidList[i].borrowInterest + "%" + '</td>'/*预期年收益*/
						+'	<td class="bor4 loan-div-row">'+ paidList[i].accountTotal +'元'+ '</td>'/*已还总额*/
						+'	<td class="bor5 loan-div-row">'+ paidList[i].repayActionTime+ '</td>'/*实还时间*/
						+ '	<td class="bor8 loan-div-row"><span class="grey">已还款</span></td>'/*状态*/
						+'</tr>';
				}else{
					strPaidList +=
						'<tr class="loan-div-a">'
						/*应还时间*/
						+'	<td class="bor1 loan-div-row">'
						+'		<span class="font-nocolor">'+ paidList[i].repayTime +'</span>'
						+		agreementStr		
						+'	</td>'
						+'	<td class="bor2 loan-div-row">'+ paidList[i].borrowNid + '</td>'/*项目编号*/
						+'	<td class="bor3 loan-div-row">'+ paidList[i].borrowInterest + "%" + '</td>'/*预期年收益*/
						+'	<td class="bor4 loan-div-row">'+ paidList[i].accountTotal +'元'+ '</td>'/*已还总额*/
						+'	<td class="bor5 loan-div-row">'+ paidList[i].repayActionTime+ '</td>'/*实还时间*/
						+ '	<td class="bor8 loan-div-row"><span class="grey">已还款</span></td>'/*状态*/
						+'</tr>';
				}
			}
		} else {
			$("#paid-pagination").html("")
		}
		if (strPaidList == "") {
			strPaidList ='<div  class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您所找的页面暂无数据～</p></div>';
			$("#paidListEmptyStyle").html(strPaidList);
			strPaidList = "";
		}else{
			$("#paidListEmptyStyle").html("");
		}
		$("#paidList").html(strPaidList);
		downloading()
	}

	/**
	 * 获取已还款记录失败回调
	 */
	function paidListPageErrorCallback(data) {

	}

	/**
	 * 获取已还款记录分页按钮发起请求
	 * 
	 * @param successCallback
	 * @param errorCallback
	 * @param url
	 * @param paginatorPage
	 */
	function getPaidList(paginatorPage) {
		$("#paginatorPage").val(paginatorPage);
		$("#pageSize").val(pageSize);
		var endDate = $("#endDate").val();
		$("#paidList").html("<tr><td colspan='8'>"+utils.loadingHTML+"</td></tr>");
		doRequest(paidListPageSuccessCallback, paidListPageErrorCallback,
				webPath + "/bank/web/user/repay/repaylist.do?status=1&borrowNid="+$("#borrowNid").val()+"&startDate="+$("#startDate").val()+"&endDate="+endDate, 
				$("#listForm").serialize(), true, "paidClass", "paid-pagination");
	}
	
	/**
	 *  垫付机构批量还款按钮
	 */
	$(".btn-batch").click(function(){
		var userId = $("#userId").val();
		//TODO 开始批量还款
		location.href = webPath + '/bank/web/user/repay/orgUserBatchRepayPage.do?userId=' + userId;
	});
	//------------以下为loan.js原带---------------
	$(".tab-tags li").click(function(e) {
		var _self = $(this);
		var idx = _self.attr("panel");
		var panel = $(".tab-panels");
		_self.siblings("li.active").removeClass("active");
		_self.addClass("active");
		panel.children("li.active").removeClass("active");
		panel.children("li[panel=" + idx + "]").addClass("active");
		setDatepicker(0);
	});
	
	if('tab_index' in window&&tab_index&&tab_index!=""){
		$(".tab-tags li:eq("+tab_index+")").click();
	}
	
	// 展开收起
	$(".details").click(function(e) {
		var _self = $(this);
		if (_self.hasClass("showed")) {
			hideDetail(_self);//调用 showDetail
		} else {
			showDetail(_self);
			var showedEle = $(".details.showed").not(_self);
			hideDetail(showedEle);//调用 hideDetail
		}
	});
	function showDetail(ele) {
		var i = ele.find('i');
		var listin = ele.next();
		ele.addClass("showed");
		listin.addClass('show');
		i.addClass('icon-offline').removeClass('icon-addition');
		ele.find('span').text('收起');
		ele.parent().addClass('list-bg');
	}
	
	function hideDetail(ele) {
		var i = ele.find('i');
		var listin = ele.next();
		ele.removeClass("showed");
		listin.removeClass('show');
		i.removeClass('icon-offline').addClass('icon-addition');
		ele.find('span').text('展开');
		ele.parent().removeClass('list-bg');
	}
	// ------------以上为loan.js原带---------------
			
	// 根据标号获取列表(待还款)
	function getRepayListById() {
		$("#borrowNid").val($("#repayBorrowNidSrch").val());
		$("#startDate").val($("#repayStartDateSrch").val());
		$("#endDate").val($("#repayEndDateSrch").val());
		getRepayListPage();
	}
	
	// 根据标号获取列表(已还款)
	function getPaidListById() {
		$("#borrowNid").val($("#paidBorrowNidSrch").val());
		$("#startDate").val($("#paidStartDateSrch").val());
		$("#endDate").val($("#paidEndDateSrch").val());
		getPaidListPage();
	}
	
	function getOrgRepayListById() {
		$("#borrowNid").val($("#paidBorrowNidSrch").val());
		$("#startDate").val($("#paidStartDateSrch").val());
		$("#endDate").val($("#paidEndDateSrch").val());
		getRepayOrgListPage();
	}

	/*
	* 初始化日期插件
	*/
	function setDatepicker(idx){
	    var now = new Date();
	    var panel = $(".tab-panels");
	    if(dateTime[idx] === undefined && panel.length!=0){
	        dateTime[idx] = [];
	        dateTime[idx]["start"] = panel.children("li[panel=" + idx + "]").find(".loan-divright").children(".start").datepicker({
	            autoclose:true,
	            format: "yyyy-mm-dd",
	            language: "zh-CN",
	            //endDate: now,
	        }).on("hide", function(e) {
	            dateTime[idx]["end"].datepicker('setStartDate', e.date);
	        });
	        dateTime[idx]["end"] = panel.children("li[panel=" + idx + "]").find(".loan-divright").children(".end").datepicker({
	            autoclose:true,
	            format: "yyyy-mm-dd",
	            language: "zh-CN",
	            //endDate: now,
	        });
	    }
	}
	setDatepicker(0);
	setDatepicker(1);
	setDatepicker(2);
	setDatepicker(3);
