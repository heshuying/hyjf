var
// --------------------------------------------------------------------------------------------------------------------------------
	/* 对应JAVA的Controllor的@RequestMapping */
    Action = {
        searchPayAllianceCode:"searchPayAllianceCode"
    },
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要保存当前的会员信息吗？", function(isConfirm) {
				isConfirm && Page.submit();
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
    searchClkAct:function () {
		//
        $("#payWarring").text("");
        var param = {};
        param.bank = $("#bank").val();
        param.cardNo = $("#cardNo").val();
        param.userId = $("#userId").val();
        $.ajax({
            url : Action.searchPayAllianceCode,
            type : "POST",
            async : true,
            data : JSON.stringify(param),
            dataType: "json",
            contentType : "application/json",
            success : function(data) {
                if (data.status == "bSuccess") {
                    $("#payAllianceCode").val(data.payAllianceCode)
                } else if(data.status == "cSuccess"){
                    $("#payAllianceCode").val(data.payAllianceCode);
                    $("#payWarring").addClass( "Validform_checktip Validform_wrong" );
                    $("#payWarring").text(data.message);
				}else {
                    $("#payWarring").addClass( "Validform_checktip Validform_wrong" );
                    $("#payWarring").text(data.message);
                }
            },
            error : function(err) {
                Page.coverLayer();
                Page.primaryKey.val("");
                Page.notice("企业用户信息补录发生错误,请重新操作!", "","error");
            }
        });
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
        $(".fn-Search").click(Events.searchClkAct);
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
}),

Page.initialize();


