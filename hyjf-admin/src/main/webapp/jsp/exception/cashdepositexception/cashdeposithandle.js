var Action = {
	validateBefore : "validateBeforeAction",
	insertAction:"insertAction",
	updateAction:"updateAction"
};

var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
    // 确认按键单击事件绑定
    confirmClkAct : function() {
        if(Page.validate.check(false) && Page.form.find(".has-error").length == 0) {
		var flag=$('input:radio:checked').val();
         var message= (flag==1?"确定要重新验证合作额度？":"确定要流标？");
            Page.confirm("", message, function(isConfirm) {
                if(isConfirm) {
                    Page.coverLayer("正在操作数据，请稍候...");
                    $.post($("#mainForm").attr("action"), $("#mainForm").serialize(), function(
                        data) {
                        if (data.success) {
                            parent.$.colorbox.close();
                        } else {
                            Page.coverLayer();
                            setTimeout(function(){Page.alert("错误", data.msg);
                            },200)
                          //
                        }
                    });
                }
            })
        }
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
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		$(".event-categories").change(Events.checkDisabled);
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct())
				|| Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
		if($("#status").val()=='success'){
			$('#idTypeQ').attr("disabled","disabled");
			$('#idTypeG').attr("disabled","disabled");
			$('#name').attr("readonly","readonly");
			$('#idNo').attr("readonly","readonly");
		}
	}
}),

Page.initialize();
