
//产品详情tab切换
$(".tab-tags li").click(function(e) {
    var _self = $(this);
    var idx = _self.attr("panel");
    var panel = $(".tab-panels");
    _self.siblings("li.active").removeClass("active");
    _self.addClass("active");
    panel.children("li.active").removeClass("active");
    panel.children("li[panel=" + idx + "]").addClass("active");
});



//投资记录
/*$(document).click(function(e) {
    if ($(e.target).attr("id") == "money-error" || $(e.target).attr("id") == "ajaxErr") {
        $(e.target).remove();
    }
});*/
$(document).ready(function() {
    /**
     * 获取投资记录
     */
    getProjectInvestListPage();
    getProjectUndertakeListPage();
    /**
     * 投资记录分页点击
     */
    $(document).on("click", ".investClass", function() {
        investList($(this).data("page"));
    })
    	.on("click", ".undertakeClass", function() {
    		undertakeList($(this).data("page"));
    })

    //汇消费
	if ($("#type").val() == '8') {
		/**
		 * 债权信息列表
		 */
		getProjectConsumeListPage();
		$(document).on("click", ".consumeClass", function() {
			consumeList($(this).data("page"));
		});
	}


});
/**
 * 获取投资记录
 */
function getProjectInvestListPage() {
    $("#paginatorPage").val(1);
    $("#pageSize").val(pageSize);
    doRequest(projectInvestPageSuccessCallback, projectInvestPageErrorCallback, webPath + "/hjhdetail/getBorrowInvest.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "investClass", "invest-pagination");
}
/**
 * 获取投资记录成功回调
 */
function projectInvestPageSuccessCallback(data) {

    var projectInvestList = data.projectInvestList;
    var investTotal = "加入总人次：" + data.investTimes;
    var investTimes = "加入金额：" + data.investTotal + "元";
    // 挂载数据
    var projectInvestListStr = "";
    if (projectInvestList.length == 0) {
        projectInvestListStr = projectInvestListStr + '<tr>' + '<td colspan="4"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无投资记录</p></div></td>' + '</tr>'
    } else {
        for (var i = 0; i < projectInvestList.length; i++) {
            var projectInvest = projectInvestList[i];
            var vipflg = "";
            if (projectInvest.vipId != "0") {
                vipflg = '<div class="tag">VIP</div>';
            }
            projectInvestListStr = projectInvestListStr
                    + '<tr>'
                    + '   <td><span>' + projectInvest.userName + '</span>'+vipflg+'</td>'
                    + '   <td>' + projectInvest.account + '</td>'
                    + '   <td>' + projectInvest.client + '</td>'
                    + '   <td><span class="dark">' + projectInvest.investTime + '</span></td>'
                    + ' </tr>'
        }
    }

    $("#projectInvestList").html(projectInvestListStr);
    $("#investTotal").html(investTotal);
    $("#investTimes").html(investTimes);
}
/**
 * 获取投资记录失败回调
 */
function projectInvestPageErrorCallback(data) {

}
/**
 * 获取投资记录分页按钮发起请求
 *
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function investList(paginatorPage) {
    $("#paginatorPage").val(paginatorPage);
    $("#pageSize").val(pageSize);
    doRequest(projectInvestPageSuccessCallback, projectInvestPageErrorCallback, webPath + "/hjhdetail/getBorrowInvest.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "investClass", "invest-pagination");
}

// -------

/**
 * 获取承接记录
 */
function getProjectUndertakeListPage() {
    $("#paginatorPage").val(1);
    $("#pageSize").val(pageSize);
    doRequest(projectUndertakePageSuccessCallback, projectUndertakePageErrorCallback, webPath + "/hjhdetail/getBorrowUndertake.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "undertakeClass", "undertake-pagination");
}
/**
 * 获取承接记录成功回调
 */
function projectUndertakePageSuccessCallback(data) {

    var projectUndertakeList = data.projectUndertakeList;
    var undertakeTotal = "承接总人次：" + data.undertRecordTotle;
    var undertakeTimes = "承接金额：" + data.sumUndertakeAccount + "元";
    // 挂载数据
    var projectUndertakeListStr = "";
    if (projectUndertakeList.length == 0) {
        projectUndertakeListStr = projectUndertakeListStr + '<tr>' + '<td colspan="4"><div class="data-empty"><div class="empty-icon"></div><p class="align-center">暂无承接记录</p></div></td>' + '</tr>'
    } else {
        for (var i = 0; i < projectUndertakeList.length; i++) {
            var projectUndertake = projectUndertakeList[i];
            var vipflg = "";
            if (projectUndertake.vipId != "0") {
                vipflg = '<div class="tag">VIP</div>';
            }
            projectUndertakeListStr = projectUndertakeListStr
                    + '<tr>'
                    + '   <td><span>' + projectUndertake.userName + '</span>'+vipflg+'</td>'
                    + '   <td>' + projectUndertake.account + '</td>'
                    + '   <td>' + projectUndertake.client + '</td>'
                    + '   <td><span class="dark">' + projectUndertake.undertakeTime + '</span></td>'
                    + ' </tr>'
        }
    }

    $("#projectUndertakeList").html(projectUndertakeListStr);
    $("#undertakeTotal").html(undertakeTotal);
    $("#undertakeTimes").html(undertakeTimes);
}
/**
 * 获取承接记录失败回调
 */
function projectUndertakePageErrorCallback(data) {

}
/**
 * 获取承接记录分页按钮发起请求
 *
 * @param successCallback
 * @param errorCallback
 * @param url
 * @param paginatorPage
 */
function undertakeList(paginatorPage) {
    $("#paginatorPage").val(paginatorPage);
    $("#pageSize").val(pageSize);
    doRequest(projectUndertakePageSuccessCallback, projectUndertakePageErrorCallback, webPath + "/hjhdetail/getBorrowUndertake.do?borrowNid=" + $("#borrowNid").val(), $("#listForm").serialize(), true, "undertakeClass", "undertake-pagination");
}


// -----

$(function(){
	//token过期刷新
	var productToken = getCookie("productToken");
	if(productToken != ""){
		setCookie("productToken","");
		utils.refresh();
	}
})
