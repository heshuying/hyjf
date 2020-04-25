$(document).ready(function() {
    getTradeListPage();

    $(document).on("click", ".tradeClass", function() {
        getTradeList($(this).data("page"));
        utils.scrollTo()
    });

    $(document).on("click", "#find-btn", function() {
        getTradeListPage();
    });

    $(document).on("click", "#export-btn", function() {
        exportData($('#page').val());
    });

    $(document).on("click", "#clear-btn", function() {
        clearData();
    });
    $(document).on("click", "#cancle-btn", function() {
        cancleLogin();
    });
});



    /*以下为交易记录*/
    /**
     * 1.1 获取记录
     */
    function getTradeListPage() {/**/
        $("#paginatorPage").val(1);
        $("#pageSize").val(pageSize);
        $("#qianleList").html("<tr><td colspan='7'>" + utils.loadingHTML + "</td></tr>");
        doRequest(tradeListPageSuccessCallback, tradeListPageErrorCallback,
            webPath + "/qianle/search.do",
            $("#listForm").serialize(), true, "tradeClass", "trade-pagination");
    }

    function exportData(data) {
        window.location.href=
        webPath + "/qianle/exportdata.do?Type="+$("#Type").val()+"&addTimeStart="+
        $("#addTimeStart").val()+"&addTimeEnd="+$("#addTimeEnd").val()+"&regTimeStart="
            +$("#regTimeStart").val()+"&regTimeEnd="+$("#regTimeEnd").val()
            +"&reffername="+
            encodeURI(encodeURI($("#reffername").val()))+"&username="+
            encodeURI(encodeURI($("#username").val()))+"&truename="+
            encodeURI(encodeURI($("#truename").val()))+
            "&paginatorPage="+$("#page").val();

    }


/**
 * 成功回调
 */
function tradeListPageSuccessCallback(data) {

    var tradeList = data.list;//实体 属性里的一个 List
    var num=parseInt(data.paginatorPage);
    var summoney=data.money.summoney;
    var yearmoney=data.money.yearmoney;
    var commission=data.money.commission;
    $('#summoney').html(summoney.toFixed(2)+"元");
    $('#yearmoney').html(yearmoney.toFixed(2)+"元");
    $('#commission').html(commission.toFixed(2)+"元");
    $('#page').val(num);
    var strTradeList = "";
    if (tradeList != null && tradeList.length>0) {

        for (var i = 0; i < tradeList.length; i++) {
                strTradeList +=
                    '<tr class="ui-list">'
                    +'	<td style="width:120px;" class="ui-list-item pl1" >'+ ((num-1)*10+i+1)+ '</td>' /*序号*/
                    +'	<td style="width:120px;" class="ui-list-item pl2">'+ tradeList[i].reg_time+ '</td>' /*注册日期*/
                    +'	<td style="width:120px;" class="ui-list-item pl3">'+ tradeList[i].username + '</td>' /*用户名*/
                    +'	<td style="width:120px;" class="ui-list-item pl4">'+ tradeList[i].truename + '</td>' /*姓名*/
                    +'	<td style="width:120px;" class="ui-list-item pl5">'+ tradeList[i].mobile+ '</td>'/*手机号*/
                    +'	<td style="width:120px;" class="ui-list-item p23">'+ tradeList[i].reffername +'</td>'/*推荐人*/
                    +'	<td style="width:120px;" class="ui-list-item pl6">'+ tradeList[i].type + '</td>' /*投资类型*/
                    +'	<td style="width:120px;" class="ui-list-item pl7">'+ tradeList[i].plannid +'</td>'/*项目/计划编号*/
                    +'	<td style="width:120px;" class="ui-list-item pl9">'+ tradeList[i].account +'</td>'/*投资金额*/
                    +'	<td style="width:120px;" class="ui-list-item p20">'+ tradeList[i].borrow_period +'</td>'/*投资期限*/
                    +'	<td style="width:120px;" class="ui-list-item p21">'+ tradeList[i].yearAccount +'</td>'/*年化金额*/
                    +'	<td style="width:120px;" class="ui-list-item p22">'+ tradeList[i].first +'</td>'/*是否首投*/
                    +'	<td style="width:120px;" class="ui-list-item p22">'+ tradeList[i].money +'</td>'/*佣金7%*/
                    +'	<td style="width:120px;" class="ui-list-item p25">'+ tradeList[i].addtimes +'</td>'/*投资日期*/
                    + '</tr>'
        }
        $('#trade-pagination').show();
    } else {
        strTradeList ='<tr><td colspan="7"><div class="data-empty">  <div class="empty-icon"></div> <p class="align-center">暂无数据...</p> </div></td></tr>';
        $('#trade-pagination').hide();
    }
    $("#qianleList").html(strTradeList);
}

/**
 * 失败回调
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
        webPath + "/qianle/search.do",
        $("#listForm").serialize(), true, "tradeClass", "trade-pagination");
}

function clearData() {/*清除查询 */
    $("#regTimeStart").val('');
    $("#regTimeEnd").val('');
    $("#addTimeStart").val('');
    $("#addTimeEnd").val('');
    $("#Type").val('1');
    $("#truename").val('');
    $("#username").val('');
    $("#reffername").val('');
}

function cancleLogin() {
    window.location.href="cancle.do"
}
