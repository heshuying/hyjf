var
// --------------------------------------------------------------------------------------------------------------------------------
    /* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        // 查询的ActionsubmitTeamAction
        submitAction : "submitTeamAction",
    },
    /* 事件动作处理 */
    Events = {
        // 提交按钮的单击动作事件
        submitFromClkAct : function(){
            if(Page.validate.check(false)) {
                Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
                    if(isConfirm) {
                    $.ajax({
                        url : "validationWorldCupTeam" ,//url
                            type : "POST",
                            dataType: "json",//预期服务器返回的数据类型
                            data :  $('#mainForm').serialize(),
                            success : function(data) {
                                var res=eval(data);
                                if (res.error != null&&res.error != "") {
                                    if(res.error!="请选择球队或球队LOGO！"){
                                        alert(res.error+"选择了多次，请重新选择！");
                                    }else{
                                        alert(res.error);
                                    }
                                }else{
                                    //提交表单
                                    Page.submit();
                                }
                            }
                    });

                    }
                })
            }
        },
        // 刷新按钮的单击动作事件
        refreshClkAct : function() {
            window.location.reload();
        },
        // 边界面板查询按钮的单击事件
        searchClkAct : function(event) {
            $("#paginator-page").val(1);
            Page.submit(Action.searchAction);
        },
        // 重置表单
        resetFromClkAct: function() {
            $(".form-select2").val("").change();
        }
    };

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面的主键
    primaryKey : $("#ids"),
    // 画面布局
    doLayout: function() {
        // 条件下拉框
        $(".form-select2").select2({
            width: 180,
            placeholder: "请选择球队",
            allowClear: true,
            language: "zh-CN"
        }),
            // 日历选择器
            $('#start-date-time').datepicker({
                autoclose: true,
                todayHighlight: true,
                endDate:new Date()
            });
        $('#end-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });

    },
    // 初始化画面事件处理
    initEvents : function() {
        // 刷新按钮的单击事件绑定
        $(".fn-Refresh").click(Events.refreshClkAct),
            // 边界面板查询按钮的单击事件绑定
            $(".fn-Search").click(Events.searchClkAct);
        // 重置按钮的单击事件绑定
        $(".fn-Reset").click(Events.resetFromClkAct);
        //提交按钮绑定单机事件
        $(".fn-Submit").click(Events.submitFromClkAct );
    },
    // 画面初始化
    initialize : function() {
        // 执行成功后刷新画面
        ($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

        // 初始化表单验证
        Page.validate = Page.form.Validform({
            tiptype: 3
        });
    }
});

Page.initialize();

function clickstatusSrch(event) {
    //球队的id
    var teamId=$(event).val();
//    alert(teamId);
    //球队的名称
    var index=$(event).attr("id").substring(10);
    // $("select[name=''+selectName+''")   team1c.id
    var text = $("#statusSrch"+index+" option:selected").text();
    // alert(text);
    $(event).parent().find(".teamteamName"+index).val(text);
    $(event).parent().find(".teamId"+index).val(teamId);
    // alert($(".teamteamName"+index).val());

}
function fileUpload(event) {
    var index=$(event).attr("id").substring(10);
    $('.fileupload').fileupload({
        url : "uploadFile",
        autoUpload : true,
        done : function(e, data) {
            var file = data.result[0];
            $(".teamteamLogo"+index).val(file.imagePath);
            $("#activityConimg"+index).attr("src",file.imagePath);
            $(".fileinput-exists img").attr("style","width: 60px;height: 60px; border-radius: 45px;");
        }
    })
}