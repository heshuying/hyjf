var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要确认会员开户信息吗？", function(isConfirm) {
				isConfirm && Page.submit();
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
};

function selectbymobile() {
    var select = document.getElementById("selectid");
    select.innerHTML="<label class=\"col-xs-2 control-label text-right padding-right-0\" for=\"realName\">\n" +
        "                                <span class=\"symbol required\"></span>查询号码：\n" +
        "                            </label>\n" +
        "                            <div class=\"col-xs-10\">\n" +
        "                                <input type=\"text\" placeholder=\"查询号码\" onclick=\"deletespan()\" " +
        "class=\"form-control input-sm\" id=\"selectno\" name=\"selectno\" value=\"\" maxlength=\"11\" datatype=\"n11-11\" errormsg=\"手机号码 只能是11位数字!\">\n" +
        "                            <span class=\"Validform_checktip Validform_wrong\"></span></div>"
};

function selectbyidcard() {
    var select = document.getElementById("selectid");
    select.innerHTML="<label class=\"col-xs-2 control-label text-right padding-right-0\" for=\"realName\">\n" +
        "                                        <span class=\"symbol required\"></span>查询号码：\n" +
        "                                    </label>\n" +
        "                                    <div class=\"col-xs-10\">\n" +
        "                                        <input type=\"text\" placeholder=\"查询身份证\" onclick=\"deletespan()\" class=\"form-control input-sm\" id=\"selectno\" name=\"selectno\" value=\"\" maxlength=\"18\" datatype=\"s18-18\" errormsg=\"请输入正确的身份证号码!\">\n" +
        "                                     <span class=\"Validform_checktip Validform_wrong\"></span></div>"
}

function deletespan() {
    var span = document.getElementById("deletespan");
    span.innerHTML = "";
}

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
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
	}
}),
    

Page.initialize();
