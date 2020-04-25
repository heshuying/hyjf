$(document).ready(function() {
    /**
     * 1.1交易明细记录
     */
	
	getTradeListPage();
    /**
     * 1.2交易明细记录分页点击
     */
    $(document).on("click", ".tradeClass", function() {
    	getTradeList($(this).data("page"));
    	utils.scrollTo()
    });
    
	/**
	 * 2.1充值记录列表
	 */
    getRechargePage();
	/**
     * 2.2充值记录分页点击
     */
	$(document).on("click", ".rechargeClass", function() {
		getRechargeList($(this).data("page"));
		utils.scrollTo()
	});
	
	/**
	 * 3.1提现记录列表
	 */
    getWithdrawPage();
	/**
     * 3.2提现记录分页点击
     */
	$(document).on("click", ".withdrawClass", function() { 
		getWithdrawList($(this).data("page"));
		utils.scrollTo()
	});

});
	
	/*以下为交易记录*/
	/**
	 * 1.1 获取交易明细记录
	 */
	function getTradeListPage() {/*去交易明细记录 controller*/
	    $("#paginatorPage").val(1);
	    $("#pageSize").val(pageSize);
	    $("#tradeDetailList").html("<tr><td colspan='7'>"+utils.loadingHTML+"</td></tr>");
	    doRequest(tradeListPageSuccessCallback, tradeListPageErrorCallback, 
	    		webPath + "/bank/user/trade/tradelist.do?trade="+$("#trade").val()+"&startDate="+$("#start").val()+"&endDate="+$("#end").val(), 
	    		$("#listForm").serialize(), true, "tradeClass", "trade-pagination");
	}
	
	
	/**
	 * 获取交易明细记录成功回调
	 */
	function tradeListPageSuccessCallback(data) {
	
		var tradeList = data.tradeList;//实体 属性里的一个 List
		var strTradeList = "";
		if (tradeList != null && tradeList.length>0) {

			for (var i = 0; i < tradeList.length; i++) {
				// 用户角色：1投资人，2借款人，3担保机构
				if (tradeList[i].roleId == "2") {
					strTradeList += 
						'<tr class="ui-list">'
						+'	<td class="ui-list-item pl1">'+ tradeList[i].time+ '</td>' /*时间*/
						+'	<td class="ui-list-item pl2">'+ tradeList[i].revuAndExpType + '</td>' /*收支类型*/
						+'	<td class="ui-list-item pl3">'+ tradeList[i].typeName + '</td>' /*交易类型*/
						+'	<td class="ui-list-item pl4">'+ tradeList[i].type+ tradeList[i].money+ '</td>'/*交易金额*/
						+'	<td class="ui-list-item pl5">'+ tradeList[i].balance + '</td>' /*可用余额*/ 
						+'	<td class="ui-list-item pl6">'+ tradeList[i].tradeStatus +'</td>'/*对账状态*/
						+'	<td class="ui-list-item pl7">'+ tradeList[i].borrowNid +'</td>'/*当为借款人时：显示标的号*/					
						+ '</tr>'
				} else {
					strTradeList += 
						'<tr class="ui-list">'
						+'	<td class="ui-list-item pl1">'+ tradeList[i].time+ '</td>' /*时间*/
						+'	<td class="ui-list-item pl2">'+ tradeList[i].revuAndExpType + '</td>' /*收支类型*/
						+'	<td class="ui-list-item pl3">'+ tradeList[i].typeName + '</td>' /*交易类型*/
						+'	<td class="ui-list-item pl4">'+ tradeList[i].type+ tradeList[i].money+ '</td>'/*交易金额*/
						+'	<td class="ui-list-item pl5">'+ tradeList[i].balance + '</td>' /*可用余额*/ 
						+'	<td class="ui-list-item pl6">'+ tradeList[i].tradeStatus +'</td>'/*对账状态*/
						+'	<td class="ui-list-item pl7">'+ tradeList[i].isBank +'</td>'/*操作平台*/					
						+ '</tr>'
				}		
			}
			$('#trade-pagination').show();
		} else {
			strTradeList ='<tr><td colspan="7"><div class="data-empty">  <div class="empty-icon"></div> <p class="align-center">暂无交易明细...</p> </div></td></tr>';
			$('#trade-pagination').hide();
		}
		$("#tradeDetailList").html(strTradeList);
	}
	
	/**
	 * 获取交易明细记录失败回调
	 */
	function tradeListPageErrorCallback(data) {
	
	}
	
	/**
	 * 1.2获取交易明细记录分页按钮发起请求
	 * 
	 * @param successCallback
	 * @param errorCallback
	 * @param url
	 * @param paginatorPage
	 */
	function getTradeList(paginatorPage) {/*去交易明细记录 controller*/
	    $("#paginatorPage").val(paginatorPage);
	    $("#pageSize").val(pageSize);
	    doRequest(tradeListPageSuccessCallback, tradeListPageErrorCallback, 
	    		webPath + "/bank/user/trade/tradelist.do?trade="+$("#trade").val()+"&startDate="+$("#start").val()+"&endDate="+$("#end").val(), 
	    		$("#listForm").serialize(), true, "tradeClass", "trade-pagination");
	}
	
	/*以上为交易明细*/
	
	
	/*以下为充值记录*/
	/**
	 * 获取充值记录
	 */
	function getRechargePage() {
		$("#paginatorPage").val(1);
		$("#pageSize").val(pageSize);
		$("#rechargeList").html("<tr><td colspan='6'>"+utils.loadingHTML+"</td></tr>");
		doRequest(rechargeListPageSuccessCallback, rechargeListPageErrorCallback, 
				webPath + "/bank/user/trade/rechargelist.do?startDate="+$("#start").val()+"&endDate="+$("#end").val(), 
				$("#listForm").serialize(), true, "rechargeClass", "recharge-pagination");
	}

	/**
	 * 获取已还款记录成功回调
	 */
	function rechargeListPageSuccessCallback(data) {
		var rechargeList = data.rechargeList;//实体 属性里的一个 List
		var strTradeList = "";
		if (rechargeList != null && rechargeList.length>0) {
			for (var i = 0; i < rechargeList.length; i++) {
				strTradeList +=
					'<tr class="ui-list">'
					+'	<td class="ui-list-item pl1">'+ rechargeList[i].time + '</td>' /*时间*/
					+'	<td class="ui-list-item pl2">'+ rechargeList[i].money + '</td>' /*充值金额*/
					+'	<td class="ui-list-item pl3">'+ rechargeList[i].fee + '</td>' /*充值手续费*/
					+'	<td class="ui-list-item pl4">'+ rechargeList[i].balance + '</td>'/*到账金额*/
					+'	<td class="ui-list-item pl5">'+ rechargeList[i].status +'</td>'/*状态*/
					+'	<td class="ui-list-item pl6">'+ rechargeList[i].isBank +'</td>'/**/
					+ '</tr>'
			}
			$('#recharge-pagination').show();
		} else {
			strTradeList ='<tr><td colspan="6"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无充值记录...</p></div></td></tr>';
			$('#recharge-pagination').hide();
		}
		$("#rechargeList").html(strTradeList);
	}


	/**
	 * 获取充值记录失败回调
	 */
	function rechargeListPageErrorCallback(data) {

	}

	/**
	 * 获取充值记录分页按钮发起请求
	 * 
	 * @param successCallback
	 * @param errorCallback
	 * @param url
	 * @param paginatorPage
	 */
	function getRechargeList(paginatorPage) {
		$("#paginatorPage").val(paginatorPage);
		$("#pageSize").val(pageSize);
		doRequest(rechargeListPageSuccessCallback, rechargeListPageErrorCallback,
				webPath + "/bank/user/trade/rechargelist.do?startDate="+$("#start").val()+"&endDate="+$("#end").val(), 
				$("#listForm").serialize(), true, "rechargeClass", "recharge-pagination");
	}
	/*以上为充值记录*/
	
	
	/*以下为提现记录*/
	/**
	 * 获取提现记录
	 */
	function getWithdrawPage() {/*去提现记录 controller*/ 
	    $("#paginatorPage").val(1);
	    $("#pageSize").val(pageSize);
	    $("#withDrawalList").html("<tr><td colspan='6'>"+utils.loadingHTML+"</td></tr>");
	    doRequest(withdrawListPageSuccessCallback, withdrawListPageErrorCallback, 
	    		webPath + "/bank/user/trade/withdrawlist.do?startDate="+$("#start").val()+"&endDate="+$("#end").val(), 
	    		$("#listForm").serialize(), true, "withdrawClass", "withDrawalList-pagination");
	}
	
	
	/**
	 * 获取提现记录记录成功回调
	 */
	function withdrawListPageSuccessCallback(data) {
	
		var withdrawList = data.withdrawList;//实体 属性里的一个 List
		var strTradeList = "";
		if (withdrawList != null && withdrawList.length>0) {
			for (var i = 0; i < withdrawList.length; i++) {
				strTradeList +=
					'<tr class="ui-list">'
					+'	<td class="ui-list-item pl1">'+ withdrawList[i].time+ '</td>' /*提现时间*/
					+'	<td class="ui-list-item pl2">'+ withdrawList[i].money+ '</td>' /*提现金额*/
					+'	<td class="ui-list-item pl3">'+ withdrawList[i].fee+ '</td>'/*提现手续费*/
					+'	<td class="ui-list-item pl4">'+ withdrawList[i].balance+ '</td>'/*到账金额*/
					+'	<td class="ui-list-item pl5">'+ withdrawList[i].status +'</td>'/*状态*/
					+ '	<td class="ui-list-item pl6">'+ withdrawList[i].bankFlag +'</td>'/*状态*/
					+ '</tr>'
			}
			$('#withDrawalList-pagination').show();
		} else {
			strTradeList ='<tr><td colspan="6"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无提现记录...</p></div></td></tr>'
			$('#withDrawalList-pagination').hide();
		}
		$("#withDrawalList").html(strTradeList);
	}
	
	/**
	 * 获取提现记录失败回调
	 */
	function withdrawListPageErrorCallback(data) {

	}
	
	/**
	 * 获取提现记录分页按钮发起请求
	 * 
	 * @param successCallback
	 * @param errorCallback
	 * @param url
	 * @param paginatorPage
	 */
	function getWithdrawList(paginatorPage) {
	    $("#paginatorPage").val(paginatorPage);
	    $("#pageSize").val(pageSize);
	    doRequest(withdrawListPageSuccessCallback, withdrawListPageErrorCallback, 
				webPath + "/bank/user/trade/withdrawlist.do?startDate="+$("#start").val()+"&endDate="+$("#end").val(), 
				$("#listForm").serialize(), true, "withdrawClass", "withDrawalList-pagination");
	}
	
	
	
	//------------以下为新js原带---------------
jQuery.divselect = function(divselectid, inputselectid) {
    var inputselect = $(inputselectid);
    $(divselectid + " cite").click(function(event) {
        var ul = $(divselectid + " ul");
        if (ul.css("display") == "none") {
            ul.slideDown("fast");
        } else {
            ul.slideUp("fast");
        }
        var i = $(this).find('i').addClass('spin');
        event.stopPropagation();//阻止事件冒泡
        //点击空白处，下拉框隐藏-------开始
        var tag = $(this).siblings('.ui-ul');
        var flag = true;
        $(document).bind("click",function(e){//点击空白处，设置的弹框消失
        	e = e||window.event;
            var target = $(e.target);
            if(target.closest(tag).length == 0 && flag == true){
                $(tag).hide();
                flag = false;
                $(this).find('i').removeClass('spin');
            }
        });
        //点击空白处，下拉框隐藏-------结束
    });

    $(divselectid + " ul li a").click(function() {
        var txt = $(this).text();
        $(divselectid + " cite").html(txt+'<i class="icon iconfont icon-ananzuiconv265"></i>');
        var value = $(this).data("val");
        inputselect.val(value);
        $(divselectid + " ul").hide();

    });
};
$(function() {
    $.divselect("#divselect", "#inputselect");
    $(".tab-tags li").click(function(e) {
        var _self = $(this);
        var idx = _self.attr("panel");
        var panel = $(".tab-panels");
        _self.siblings("li.active").removeClass("active");
        _self.addClass("active");
        panel.children("li.active").removeClass("active");
        panel.children("li[panel=" + idx + "]").addClass("active");
        clearDatepicker(0);
        if(idx == 0){
            $("#type").show();
        }else{
            $("#type").hide();
        }
    });
});


var dateTime = [];
/*
* 初始化日期插件
*/
function setDatepicker(idx){
    var now = new Date();
    var panel = $(".top-fr");
    if(dateTime[idx] === undefined){
        dateTime[idx] = [];
        dateTime[idx]["start"] = panel.find(".loan-divright").children(".start").datepicker({
            autoclose:true,
            format: "yyyy-mm-dd",
            language: "zh-CN",
            endDate: now,
        }).on("hide", function(e) {
            dateTime[idx]["end"].datepicker('setStartDate', e.date);
        });
        dateTime[idx]["end"] = panel.find(".loan-divright").children(".end").datepicker({
            autoclose:true,
            format: "yyyy-mm-dd",
            language: "zh-CN",
            endDate: now,
        });
    }
}
setDatepicker(0);
function clearDatepicker(i){
    dateTime[i]["start"].datepicker('clearDates');
    dateTime[i]["end"].datepicker('clearDates');
}
	// ------------以上为新js原带---------------
			
	
	// 根据标号获取列表(待还款)  这些方法还需要修改
	function getListByType() {/* 原 getRepayListById*/
		
		$("#trade").val($("#inputselect").val());
		$("#startDate").val($("#start").val());
		$("#endDate").val($("#end").val());
		var idx = $(".tab-tags").children(".active").attr("panel");
		if (idx == "0"){
			getTradeListPage();
		}else if(idx == "1"){
			getRechargePage();
		}else{
			getWithdrawPage();
		}
	}
	//鼠标移上提示
	utils.alt('.tab-panels .loan-div .alt-el');





