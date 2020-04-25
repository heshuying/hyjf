$(document).ready(
    function() {
        /**
         * 获取投资列表
         */
        var type=$("#type").val();
        // if(type!=0){
            getProjectListPage();

            $(document).on("click", ".flipClass", function() {

                flip($(this).data("page"));
            });
        // }
    }
);

function getProjectListPage() {
    $("#paginatorPage").val(1);
    $("#pageSize").val(pageSize);
    $("#projectList").html('<tr><td colspan="6" >'+utils.loadingHTML+'</tr>');
    doRequest(
        projectPageSuccessCallback,
        projectPageErrorCallback,
        webPath+ "/bank/web/user/repay/userRepayDetailAjax.do?borrowNid="+$("#borrowNid").val() + "&verificationFlag=" + $("#verificationFlag").val(),
        $("#listForm").serialize(), true,"flipClass","new-pagination");
}

//下载事件延迟
function downloading(){
    $('.downloadargrement').click(function(){
        if(!$(this).hasClass('disable')){
            var that=this
            location.href = $(that).data('href');
            $(that).next('.loadingargrement').show()
            $(that).addClass('disable')
            setTimeout(function(){
                $(that).removeClass('disable')
                $(that).next('.loadingargrement').hide()
            },60000)
        }
    })
}

/**
 * 获取投资列表成功回调
 */
function projectPageSuccessCallback(data) {
    var projectList = data.transferList;
    // 挂载数据
    var projectListStr = "";
    if(projectList.length == 0){
        projectListStr ='<tr><td colspan="6">暂时没有数据记录</td></tr>';
        $('#new-pagination').hide();
    }else{
        var type=$("#type").val();

        for (var i = 0; i < projectList.length; i++) {
            var project =projectList[i];
            /*非债转的显示借款协议+投资协议(居间服务协议)*/
            // if(project.type==0){
            //     if(project.fddStatus==1){
                    projectListStr += '<tr>'+
                        '<td class="ui-list-item pl1">'+project.undertakerUserName+' </td>'+
                        '<td class="ui-list-item pl2">' + project.creditUserName +'</td>'+
                        '<td class="ui-list-item pl3">'+project.assignCapitalString +'</td>'+
                        '<td class="ui-list-item pl4">'+project.assignOrderDate +'</td>'+
                        '</tr>';
            //     }
            // }
        }


        $('#new-pagination').show();
    }
    $("#projectList").html(projectListStr);
    downloading()
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
    $("#projectList").html('<tr><td colspan="6" >'+utils.loadingHTML+'</tr>');
    doRequest(
        projectPageSuccessCallback,
        projectPageErrorCallback,
        webPath+ "/bank/web/user/repay/userRepayDetailAjax.do?borrowNid="+$("#borrowNid").val() + "&verificationFlag=" + $("#verificationFlag").val(),
        $("#listForm").serialize(), true,"flipClass","new-pagination");
}

