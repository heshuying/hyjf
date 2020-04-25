var

Action = {

    savemodifyUser:"savemodifyUser"

}

var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
        var param = {};
        param.mobile = $("#mobile").val();
        param.userId = $("#userId").val();
		console.log("param：" + param);
		// return;
        $.ajax({
            url : Action.savemodifyUser,
            type : "POST",
            async : true,
            data : JSON.stringify(param),
            dataType: "json",
            contentType : "application/json",
            success : function(data) {
                if (data.status == "success") {
                    Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.cancelClkAct()});
                } else {
                    Page.confirm("",data.result,"error",{showCancelButton: false}, null);
                }
            },
            error : function(err) {
                Page.coverLayer();
                Page.notice("操作错误!", "","error");
            }
        });
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {

	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct),
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
		selectCheck();
	}
}),

Page.initialize();

function selectCheck(){
	if($("#userRole").val()==2){
		$("#borrowerTypeDiv").css('display','block');
		$("#borrowerType").attr("datatype","*1-100");
		if($("#borrowerType").val() =="" ||$("#borrowerType").val() ==" " ||$("#borrowerType").val() ==null){
			 $("#borrowerType").get(0).selectedIndex = 1;
		}
	}else{
		$("#borrowerType").val(" ");
		$("#borrowerTypeDiv").css('display','none');
		$("#borrowerType").attr("datatype","*0-100");
	}
}
