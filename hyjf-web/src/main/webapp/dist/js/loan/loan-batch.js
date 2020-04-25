//input日期插件
var dateTime = [];
function setDatepicker(idx){
    var now = new Date();
    if(dateTime[idx] === undefined){
        dateTime[idx] = [];
        dateTime[idx]["start"] = $("#startDate").datepicker({
            autoclose:true,
            format: "yyyy-mm-dd",
            language: "zh-CN",
//            endDate: now,
        }).on("hide", function(e) {
            dateTime[idx]["end"].datepicker('setStartDate', e.date);
        });
        dateTime[idx]["end"] = $("#endDate").datepicker({
            autoclose:true,
            format: "yyyy-mm-dd",
            language: "zh-CN",
//            endDate: now,
        });
    }
}
setDatepicker(0);
//应还时间点击搜索
$('#timeSerch').click(function(){
	var userId = $("#userId").val();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	location.href = webPath + '/bank/web/user/repay/orgUserBatchRepayPage.do?userId=' + userId + '&startDate=' + startDate + '&endDate=' + endDate;
})

$(document).ready(function() {
    var userId = $("#userId").val();
	var errCode = $("#errCode").val();
	if (errCode == "901") {
		utils.alert({
			id:"errPop",
			type:"alert",
			content:"筛选时间间隔需小于等于7天，请重新筛选！"
		});
        // window.history.go(-1);
        // location.href = webPath + '/bank/web/user/repay/orgUserBatchRepayPage.do?userId=' + userId;
		//去掉loading样式
        $("#repayListEmptyStyle").html('<div class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您输入的时间超过7天～</p ></div>');
        $("#repayList").html("");
        return;
	}

    /**
     * 待还款记录
     */
    getRepayListPage();

    /**
     * 待还款记录分页点击
     */
    $(document).on("click", ".repayClass", function() {
    	console.log(11);
    	getRepayList($(this).data("page"));
    });
});


/**
 * 获取待还款记录
 */
function getRepayListPage() {
    var endDate = $("#endDate").val();
    var startDate=$("#startDate").val();
    $("#paginatorPage").val(1);
    $("#pageSize").val(pageSize);
    doRequest(repayListPageSuccessCallback, repayListPageErrorCallback,
    		webPath + "/bank/web/user/repay/repaylist.do?status=0&repayStatus=0&startDate="+startDate+"&endDate="+ endDate
    		,$("#listForm").serialize(), true, "repayClass", "repay-pagination");
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
			strRepayList +=
				'<li >'
				/*应还时间*/
				+'<span class="l1">'+ repayList[i].repayYesTime + '</span>'
				+'<span class="l2">'+ repayList[i].borrowNid+ '</span>' /*项目编号*/
				+'<span class="l3">'+ "第"+repayList[i].orgBorrowPeriod+"期" + '</span>'/*期数*/
				+'<span class="l4">'+ repayList[i].borrowInterest+"%" + '</span>'/*预期年收益*/
				+'<span class="l5">'+ repayList[i].borrowAccount +'元</span>'/*借款金额*/
				+'<span class="l6">'+ repayList[i].accountTotal+'元</span>'/*应还总额*/
				+'</li>'
		}
		$("#listSize").val(repayList.length);
	} else {
		$("#repay-pagination").html("");
		$("#listSize").val(0);
	}
	if (strRepayList == "") {
		strRepayList ='<div class="data-empty"><div class="empty-icon"></div><p class="align-center">咦，您所找的页面暂无数据～</p></div>';
		$("#repayListEmptyStyle").html(strRepayList);
		strRepayList = "";
	}else{
		$("#repayListEmptyStyle").html("");
	}
	$("#repayList").html(strRepayList);
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
    $("#repayList").html("<li><div>"+utils.loadingHTML+"</div></li>");
    doRequest(repayListPageSuccessCallback, repayListPageErrorCallback,
    		webPath + "/bank/web/user/repay/repaylist.do?status=0&repayStatus=0&startDate="+$("#startDate").val()+"&endDate=" + endDate
    		,$("#listForm").serialize(), true, "repayClass", "repay-pagination");
}
var flag =false;
function checkRepay(){
	var repayTotal =$("#repayTotal").val();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	$.ajax({
		type:"post",
		url:webPath + "/bank/web/user/repay/orgUserStartBatchRepayCheckAction.do?repayTotal=" + repayTotal + "&startDate="+startDate+"&endDate=" + endDate,
//		data:$('.borrower-arr form').serialize(),
		async:false,
		success:function(data){
			
			if (data != '') {
				var msg = "";
				if ('998' == data) {
					msg = "用户在银行没开户!";
				}else if('999' == data){
					msg = "系统正在处理还款，请10分钟后再试!";
				}else if('997' == data){
					msg = "用户银行可用余额不足,请充值!";
				}else if('996' == data){
                    msg = "批量还款仅支持提前28天内还款 \n" + " 请重新输入检索应还时间!";
                }
				utils.alert({
					id:'confirmBox',
					title:'提示',
					content:msg
				})
				flag = false;
			}else{
				flag = true;
			}
		}
	});
}



//输入平台密码还款
$('#pwdsubmit').click(function(){
	var password = $("#password").val();
	var pwd = $.md5(password);
	var endDate = $("#endDate").val();
	var startDate = $("#startDate").val();
	var size = $("#listSize").val();
    var days = 0;
    if(endDate != null) {
        var enddate = new Date(endDate);  //结束时间
        var startdate = new Date().toLocaleDateString();    //开始时间
        var result = enddate.getTime() - new Date(startdate).getTime();   //时间差的毫秒数
        //计算出相差天数
        days = Math.floor(result / (24 * 3600 * 1000));

    }
    if(days >= 28){
        utils.alert({
            id:'confirmBox',
            title:'提示',
            content:"<div>批量还款仅支持提前28天内还款 </br> 请重新输入检索应还时间!</div>"
        })
    }else{
        if (size > 0) {
            $.ajax({
                type:"post",
                url:webPath + "/bank/web/user/repay/checkPassword.do?password=" + pwd,
                data:$('.borrower-arr form').serialize(),
                success:function(data){
                    if(data){
                        //密码正确弹出确认框
                        $('.borrower-arr .small').remove()
                        utils.alert({
                            id:'confirmBox',
                            title:'确认',
                            content:'您确认要还款吗?',
                            fnconfirm:function(){
                                $("#password").val(pwd);
                                checkRepay();
                                var index = flag;
                                if (index) {
                                    //提交表单跳转页面
                                    $('#info').submit()
                                }else{
                                }
                            }
                        })
                    }else{
                        $('.borrower-arr .small').show();
                    }
                }
            });
        }else{
            utils.alert({
                id:'confirmBox',
                title:'提示',
                content:'您没有需要还款的项目!'
            })
        }
	}
	
	
})
